package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;

public class TicketExplorer extends TransparentPanel {
	DecimalFormat numberFormat = new DecimalFormat("0.00"); //$NON-NLS-1$
	JXTable explorerTable;
	public List<Ticket> tickets;	
	TicketExplorerTableModel model;
	private JXDatePicker dpDate;
	
	private JXDatePicker dpDateTransfer;
	PosButton btnPrint;
	PosButton btnPrintAll;
	PosButton dpDateToday;
	PosButton btnRechPaid;
	JCheckBox cbA4 = new JCheckBox("A4");
	JCheckBox cbA4_ = new JCheckBox("A4.");
	JButton btnUpdateTax;
	PosButton btnTransfer;
	JLabel lblCash = new JLabel("Bar");
	JLabel lblRechnug = new JLabel("Rechnug");
	JTextField tfCash =new JTextField(10);
	JLabel lblCard = new JLabel("Karte");
	JTextField tfCard =new JTextField(10);
	JTextField tfRechnug =new JTextField(10);

	JLabel lblTotal = new JLabel("Gesamt");
	JTextField tfTotal =new JTextField(10);
	boolean isWholeSale;
	JComboBox dpMonth;
	JComboBox dpYear;
	boolean chk=false;
	public TicketExplorer() {
		this.setPreferredSize(new Dimension(800, 500));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout());
		isWholeSale = TerminalConfig.isWholeSale();
		cbA4.setBackground(new Color(209, 222, 235));
		cbA4_.setBackground(new Color(209, 222, 235));
		Restaurant rest = RestaurantDAO.getRestaurant();
		cbA4_.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("rest.getStartToday()_"+rest.getStartToday());
				Date fromDate = BusinessDateUtil.startOfOfficialDay(rest.getStartToday());
				Date toDate = BusinessDateUtil.endOfOfficialDay(rest.getStartToday());
				if(cbA4_.isSelected()) {			
					tickets = TicketDAO.getInstance().findDateTickets_zws(fromDate,toDate);					
				}
				else
				{
					tickets = TicketDAO.getInstance().findDateTickets(fromDate,toDate);
				}
				tickets = tickets.stream()
						.filter(ticket -> ticket.getTotalAmount() > 0.00||ticket.isRefunded())
						.collect(Collectors.toList());
				Collections.reverse(tickets);
				updateText(tickets);
				model.setRows(tickets); 
				explorerTable.repaint();
				explorerTable.revalidate();
			}
		});
		dpDate = new JXDatePicker();
		dpDate.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		
		dpDate.setDate(rest.getStartToday()!=null?rest.getStartToday():new Date());
		dpDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				Date fromDate = BusinessDateUtil.startOfOfficialDay(dpDate.getDate());
				Date toDate = BusinessDateUtil.endOfOfficialDay(dpDate.getDate());
				System.out.println("Bstart "+dpDate.getDate() +" Changed "+fromDate+" Changed End "+toDate);

				tickets = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
				
				tickets = tickets.stream()
						.filter(ticket -> ticket.getTotalAmount() > 0.00||ticket.isRefunded())
						.collect(Collectors.toList());
				Collections.reverse(tickets);
				updateText(tickets);
				model.setRows(tickets);
				explorerTable.repaint();
				explorerTable.revalidate();
			}
		}); 
		
		/*dpDateToday = new PosButton("ZWS");
		dpDateToday.setFont(new Font("Times New Roman", Font.BOLD, 16));
		dpDateToday.setBackground(new Color(102, 255, 102));
		 
		dpDateToday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
			
				Date fromDate = BusinessDateUtil.startOfOfficialDay(rest.getStartToday());
				Date toDate = BusinessDateUtil.endOfOfficialDay(rest.getStartToday());
				
				tickets = TicketDAO.getInstance().findDateTickets_zws(fromDate,toDate);
				tickets = tickets.stream()
						.filter(ticket -> ticket.getTotalAmount() > 0.00||ticket.isRefunded())
						.collect(Collectors.toList());
				Collections.reverse(tickets);
				updateText(tickets);
				model.setRows(tickets); 
				explorerTable.repaint();
				explorerTable.revalidate();
			}

		}); 

		buttonPanel.add(dpDate);*/
		
		dpMonth = new JComboBox();
		dpMonth.setPreferredSize(new Dimension(100, 35));
		dpMonth.setFont(new Font("Times New Roman", Font.BOLD, 16));
		dpMonth.setBackground(Color.WHITE);
		DateUtil.populateMonth(dpMonth);

		dpMonth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateMonthView();
			}
		});

		dpYear = new JComboBox();
		dpYear.setPreferredSize(new Dimension(100, 35));
		dpYear.setFont(new Font("Times New Roman", Font.BOLD, 16));
		dpYear.setBackground(Color.WHITE);
		DateUtil.populateYear(dpYear);   

		btnPrint = new PosButton(POSConstants.PRINT);
		btnPrint.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnPrint.setBackground(new Color(102, 255, 102));
		btnPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int index = explorerTable.getSelectedRow();
				if (index < 0)
					return;

				Ticket ticket = tickets.get(index);
				if (ticket == null) {
					return;
				}
				ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
				if (ticket == null) {
					return;
				}
				OrderInfoView view = null;
				try {
					view = new OrderInfoView(Arrays.asList(ticket));
				} catch (Exception e1) {
					e1.printStackTrace();
					return;
				}
				OrderInfoDialog dialog = new OrderInfoDialog(view, false);
				dialog.setSize(400, 600);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);				
			}

		}); 

		btnPrintAll = new PosButton("   Druck Alle   ");
		
		if(StringUtils.isNotEmpty(POSConstants.Druck_Alle))
			btnPrintAll.setText(POSConstants.Druck_Alle);
		
		btnPrintAll.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnPrintAll.setBackground(new Color(102, 255, 102));
		btnPrintAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ErrorMessageDialog dialog = new ErrorMessageDialog(
							"Sind Sie sicher ?", true, true);
					dialog.pack();
					dialog.open();
					if(!dialog.isCancelled())						
						printReport(cbA4.isSelected());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});


		btnRechPaid = new PosButton("Zahlungsstatus");
		if(StringUtils.isNotEmpty(POSConstants.Zahlungsstatus))
			btnRechPaid.setText(POSConstants.Zahlungsstatus);
		
		btnRechPaid.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnRechPaid.setBackground(new Color(102, 255, 102));
		btnRechPaid.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int index = explorerTable.getSelectedRow();
					if (index < 0)
						return;

					Ticket ticket = tickets.get(index);
					if (ticket == null) {
						return;
					}
					if(ticket.getRechnugPayemnt()!=null&&!ticket.getRechnugPayemnt())
						return;
					String message = "Möchten Sie den Zahlungsstatus ändern??";
					int option = JOptionPane.showOptionDialog(null, message, "ACHTUNG", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);

					if(option != JOptionPane.YES_OPTION) {
						return;
					}
					if(ticket.isRechnugpaid()) {
						ticket.setRechnugpaid(false);
					}else {
						ticket.setRechnugpaid(true);
					}

					TicketDAO.getInstance().saveOrUpdate(ticket);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnUpdateTax = new JButton("Update Tax");
		btnUpdateTax.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnUpdateTax.setBackground(Color.YELLOW);
		btnUpdateTax.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(TerminalConfig.isSpecial())
						updateTaxRateSpecial();
					else
						updateTaxRate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});   

		setLabelFont(lblCash);
		setLabelFont(lblCard);
		setLabelFont(lblTotal);
		setLabelFont(lblRechnug);
		setTextFont(tfCard);
		setTextFont(tfCash);
		setTextFont(tfRechnug);
		setTextFont(tfTotal);

		buttonPanel.add(lblCash, "growx");
		buttonPanel.add(tfCash, "growx");
		buttonPanel.add(lblCard, "growx");
		buttonPanel.add(tfCard, "growx");
		if(isWholeSale) {
			buttonPanel.add(lblRechnug, "growx");
			buttonPanel.add(tfRechnug, "growx");
		}
		buttonPanel.add(lblTotal, "growx");
		buttonPanel.add(tfTotal, "growx, wrap");

		buttonPanel.add(dpDate, "growx");
		buttonPanel.add(dpMonth, "growx");
		buttonPanel.add(dpYear, "growx");

		buttonPanel.add(btnPrint, "growx");

		if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {  	
			dpDateTransfer = new JXDatePicker();
			dpDateTransfer.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
			dpDateTransfer.setFont(new Font("Times New Roman", Font.PLAIN, 22));
			dpDateTransfer.setDate(new Date());
			dpDateTransfer.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					btnTransfer.setEnabled(true);
				}

			});
			btnTransfer = new PosButton("Transfer");
			btnTransfer.setFont(new Font("Times New Roman", Font.BOLD, 16));
			btnTransfer.setBackground(new Color(102, 255, 102));
			btnTransfer.setEnabled(false);
			btnTransfer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						int[] rows = explorerTable.getSelectedRows();
						if (rows.length > 0) {
							for (int index = 0; index < rows.length; index++) {
								Ticket ticket = tickets.get(rows[index]);
								ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
								Calendar currentTime = Calendar.getInstance();
								currentTime.setTime(dpDateTransfer.getDate());
								currentTime.set(Calendar.HOUR_OF_DAY, ticket.getCreationHour());
								currentTime.set(Calendar.MINUTE, ticket.getCreateDate().getMinutes());
								ticket.setCreateDate(currentTime.getTime());
								ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
								ticket.setClosingDate(currentTime.getTime());
								TicketDAO.getInstance().saveOrUpdate(ticket);
							}
							Date fromDate = BusinessDateUtil.startOfOfficialDay(dpDate.getDate());
							Date toDate = BusinessDateUtil.endOfOfficialDay(dpDate.getDate());
							System.out.println("Cstart "+dpDate.getDate() +" Changed "+fromDate+" Changed End "+toDate);

							tickets = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
							tickets = tickets.stream()
									.filter(tick -> tick.getTotalAmount() > 0.00||tick.isRefunded())
									.collect(Collectors.toList());
							Collections.reverse(tickets);
							updateText(tickets);
							model.setRows(tickets);
							explorerTable.repaint();
							explorerTable.revalidate();

						}
					} catch (Throwable x) {
						BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
					}
				}			
			});

			buttonPanel.add(dpDateTransfer,"growx, wrap");			
			buttonPanel.add(btnTransfer);
			buttonPanel.add(btnUpdateTax);
			buttonPanel.add(btnPrintAll);
			buttonPanel.add(cbA4);
			//buttonPanel.add(cbA4_, "growx");

		} else if(Application.getCurrentUser().getFirstName().compareTo("Master")==0) {				
		    buttonPanel.add(btnUpdateTax);
		    if(TerminalConfig.isDupTseBackendDispaly()) {buttonPanel.add(cbA4_); }
			 
			 buttonPanel.add(btnPrintAll);	
			 buttonPanel.add(cbA4, "growx, wrap");
			
		} else if(TerminalConfig.isPayTransfer()||isWholeSale){
			dpDateTransfer = new JXDatePicker();
			dpDateTransfer.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
			dpDateTransfer.setFont(new Font("Times New Roman", Font.PLAIN, 22));
			dpDateTransfer.setDate(new Date());			

			btnTransfer = new PosButton("Liefer Datum");
			if(StringUtils.isNotEmpty(POSConstants.Liefer_Datum))
				btnTransfer.setText(POSConstants.Liefer_Datum);
			
			btnTransfer.setFont(new Font("Times New Roman", Font.BOLD, 16));
			btnTransfer.setBackground(new Color(102, 255, 102));
			btnTransfer.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						int[] rows = explorerTable.getSelectedRows();
						if (rows.length > 0) {
							for (int index = 0; index < rows.length; index++) {
								Ticket ticket = tickets.get(rows[index]);
								ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());								
								ticket.setDeliveryDate(dpDateTransfer.getDate());								
								TicketDAO.getInstance().saveOrUpdate(ticket);
							}
							Date fromDate = BusinessDateUtil.startOfOfficialDay(dpDate.getDate());
							Date toDate = BusinessDateUtil.endOfOfficialDay(dpDate.getDate());
							System.out.println("Astart "+dpDate.getDate() +" Changed "+fromDate+" Changed End "+toDate);

							tickets = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
							tickets = tickets.stream()
									.filter(tick -> tick.getTotalAmount() > 0.00||tick.isRefunded())
									.collect(Collectors.toList());
							Collections.reverse(tickets);
							updateText(tickets);
							model.setRows(tickets);
							explorerTable.repaint();
							explorerTable.revalidate();
						}
					} catch (Throwable x) {
						BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
					}
				}			
			});
			
			buttonPanel.add(dpDateTransfer,"growx");			
			buttonPanel.add(btnTransfer,"growx, wrap");	
			buttonPanel.add(btnRechPaid,"growx");			
			buttonPanel.add(btnPrintAll, "growx");
			buttonPanel.add(cbA4, "growx, wrap");
			 
		}
		
		setLayout(new BorderLayout());
		Date fromDate = BusinessDateUtil.startOfOfficialDay(dpDate.getDate());
		Date toDate = BusinessDateUtil.endOfOfficialDay(dpDate.getDate());
		
			tickets = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
		
		tickets = tickets.stream().filter(ticket -> ticket.getTotalAmount() > 0.00||ticket.isRefunded())
				.collect(Collectors.toList());
		Collections.reverse(tickets);
		updateText(tickets);

		model = new TicketExplorerTableModel();
		explorerTable = new JXTable(model);
		explorerTable.setRowHeight(45);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		explorerTable.setDefaultRenderer(Object.class, centerRenderer);
		explorerTable.getTableHeader().setBackground(new Color(209, 222, 235));
		model.setRows(tickets);
		explorerTable.repaint();
		explorerTable.revalidate();
		explorerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me)) {
					int index_ = explorerTable.getSelectedRow();
					int[] indexs = explorerTable.getSelectedRows();
					if (indexs.length < 0)
						return;
					for(int index:indexs) {
					Ticket ticket = tickets.get(index);
					if (ticket == null) {
						return;
					}
					if(ticket.getTseReceiptTxId()!=null&&StringUtils.isNotEmpty(ticket.getTseReceiptTxRevisionNr()))
						return;

					if (ticket.isDrawerResetted()) {
						POSMessageDialog.showError("Bearbeitung ist nicht moeglich: Tages Abschluss gedruckt");
						return;
					}

					if(ticket.getCashPayment() != null && !ticket.getCashPayment()) {
						POSMessageDialog.showError("Die bezahlte Rechnung mit EC karte kann nicht gelöschen");
						return;
					}

					boolean master = Application.getCurrentUser().getFirstName().compareTo("Master")==0;
					 
					
					if (master) {
						if (ticket.getTseReceiptTxId() != null
								&& StringUtils.isNotEmpty(ticket.getTseReceiptTxRevisionNr()))
							return;
						if (ticket.isDrawerResetted()) {
							POSMessageDialog.showError("Bearbeitung ist nicht moeglich: Tages Abschluss gedruckt");
							return;
						}
						
						if (ticket.getGutscheinPayment()!=null&&ticket.getGutscheinPayment()) {
							POSMessageDialog.showError("Bearbeitung ist nicht moeglich: Gutschein Eingelöst");
							return;
						}

						if (ticket.getSoldGutschein()!=null&&ticket.getSoldGutschein()) {
							POSMessageDialog.showError("Bearbeitung ist nicht moeglich: Verkauft Gutschein");
							return;
						}

						if (ticket.getCardAmount() != null && ticket.getCardAmount()>0.00 && !ticket.getCashPayment()) {
							POSMessageDialog.showError("Die bezahlte Rechnung mit EC karte");
							return;
						}

						if (ticket.getPrinted() != null && ticket.getPrinted()
								|| ticket.getTicketid() != null && ticket.getTicketid() > 0) {
							if (!POSMessageDialog.showYesNo("Rechnug ist Gedrukt Moechten Sie  fortsetzen?")) {
								return;
							}
						}
						TicketDAO.getInstance().delete(ticket.getId());
						/*Date fromDate = DateUtils.startOfDay(dpDate.getDate());
						Date toDate = DateUtils.endOfDay(dpDate.getDate());*/

					} else if (Application.getCurrentUser().getFirstName() != null && Application.getCurrentUser().getFirstName().equals("Super-User")) {

						if (ticket.getTseReceiptTxId() != null
								&& StringUtils.isNotEmpty(ticket.getTseReceiptTxRevisionNr()) && rest.isTseLive())
							return;				
						
						TicketDAO.getInstance().delete(ticket.getId());
					}					
					}
					
					if(cbA4_.isSelected())
					{
						tickets = TicketDAO.getInstance().findDateTickets_zws(fromDate, toDate);
					}
					else
					{
						tickets = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
					}
					Collections.reverse(tickets);
					/*tickets = tickets.stream()
							.filter(ticket1 -> ticket1.getTotalAmount() > 0.00&&!ticket.isRefunded())
							.collect(Collectors.toList());*/
					tickets = tickets.stream()
							.filter(ticket1 -> ticket1.getTotalAmount() > 0.00)
							.collect(Collectors.toList());
					updateText(tickets);
					model.setRows(tickets);
			   }				
			}			
		});

		JScrollPane jScrollPane = new JScrollPane(explorerTable);
		jScrollPane.getViewport().setBackground(new Color(209, 222, 235));
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane, BorderLayout.CENTER);
		buttonPanel.setBackground(new Color(209, 222, 235));
		add(buttonPanel, BorderLayout.SOUTH);
	}

	protected void updateMonthView() {
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		int year = Integer.parseInt(dpYear.getSelectedItem().toString());
		int month = DateUtil.getMonth(dpMonth.getSelectedItem().toString());

		c.set(year, month - 1, 1);
		Date fromDate = c.getTime();
		d.set(year, month, 1);
		d.add(Calendar.DAY_OF_MONTH, -1);
		Date toDate = d.getTime();

		fromDate = BusinessDateUtil.startOfOfficialDay(fromDate);
		toDate = BusinessDateUtil.endOfOfficialDay(toDate);
		System.out.println("From:  "+fromDate+" To : "+toDate+" "+month+" "+year);
		tickets = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
		Collections.reverse(tickets);
		model.setRows(tickets);
		updateText(tickets);
		explorerTable.repaint();
		explorerTable.revalidate();
	}

	public void printReport(boolean a4) throws Exception
	{
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			if(ticket == null)continue;
			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			final Restaurant restaurant = RestaurantDAO.getRestaurant();
			if (ticket.getTicketid() == null || ticket.getTicketid() == 0) {
				int restaurantTicketId = restaurant.getTicketid() != null ? restaurant.getTicketid() : 1;
				ticket.setTicketid(restaurantTicketId);					
				restaurant.setTicketid(++restaurantTicketId);				
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}
			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
			HashMap map;

			if(a4) {
				map =JReportPrintService.populateTicketPropertiesA4(ticket, printProperties, null);
			}else {
				if(TerminalConfig.isLogoEnabled()) {
					map =JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,true, 0.00);
				} else {
					map =JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,false, 0.00);
				}
			}

			JasperPrint	jasperPrint;

			if (a4){
				jasperPrint = JReportPrintService.createA4Print(ticket, map, null);
				jasperPrint.setProperty("printerName", Application.getPrinters().getA4Printer());
				jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
				JReportPrintService.printQuitelyA4(jasperPrint);
			}else {
				jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
				JReportPrintService.printQuitely(jasperPrint);
			}			
		}		
	}

	public void updateTaxRate() {
		
		System.out.println("update tax rate function called");		
		
		Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
		Tax home = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			if(ticket == null)continue;
			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			List<TicketItem> itemList = ticket.getTicketItems();
			for(TicketItem item:itemList) {	
				System.out.println("item name : "+item.getName()+"   item tax rate : "+item.getTaxRate());

				Double taxRate1 = item.getTaxRate();
				try {					
					if(item.getTaxRate()==19||item.getTaxRate()==16) {
						taxRate1 = dineIn.getRate();
					}else if(item.getTaxRate()==7||item.getTaxRate()==5) {
						taxRate1 = home.getRate();
					}else if(item.getTaxRate()==0) {
						taxRate1 = 0.0;
					}
				}catch(Exception ex) {
					System.out.println("update tax rate ticket exception : "+ex.getMessage());
				}
				item.setTaxRate(taxRate1);
				TicketItemDAO.getInstance().saveOrUpdate(item);				
			}
			Double discountAmnt = ticket.getDiscountAmount();
			ticket.calculatePrice();
			ticket.setDiscountAmount(discountAmnt);
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
	}

	public void updateTaxRateSpecial() {
		Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
		Tax home = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			if(ticket == null)continue;
			ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			List<TicketItem> itemList = ticket.getTicketItems();
			for(TicketItem item:itemList) {				
				Double taxRate1 = item.getTaxRate();
				if(Application.getCurrentUser().getFirstName().compareTo("Master")==0) {
					if(item.getCategoryName().compareTo(POSConstants.MISC)==0) {						 
						if(item.getTaxRate()==19||item.getTaxRate()==16) {
							taxRate1 = dineIn.getRate();
						}else if(item.getTaxRate()==7||item.getTaxRate()==5) {
							taxRate1 = home.getRate();
						} else {
							taxRate1 = 0.0;
						}

					} else {						
						try {
							String taxRate = MenuCategoryDAO.getInstance().findByNameUnique(item.getCategoryName()).getType();
							if (taxRate.compareTo(POSConstants.DINE_IN) == 0) {
								taxRate1 = dineIn.getRate();
							} else if (taxRate.compareTo(POSConstants.HOME_DELIVERY) == 0) {
								taxRate1 = home.getRate();
							} else {
								taxRate1 = 0.0;
							}
						}catch(Exception ex) {

						}
					}
				} else if(item.getTaxRate()==19||item.getTaxRate()==16)
					taxRate1 = dineIn.getRate();					
				else if (item.getTaxRate()==7||item.getTaxRate()==5)
					taxRate1 = home.getRate();

				item.setTaxRate(taxRate1);
				TicketItemDAO.getInstance().saveOrUpdate(item);				
			}

			Double discountAmnt = ticket.getDiscountAmount();
			ticket.calculatePrice();
			ticket.setDiscountAmount(discountAmnt);
			TicketDAO.getInstance().saveOrUpdate(ticket);
		}
	}
	
	public static boolean updateTicketItems(int ticketId)  throws Exception{
	      //Registering the driver
	      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	      boolean result= false;
	      
	      String conection = AppConfig.getConnectString();
	      String URL = conection+";create=true";
	      Connection conn = DriverManager.getConnection(URL);
	     
	      Statement stmt = conn.createStatement();
	      Statement stmt1 = conn.createStatement();
	      Statement stmt2 = conn.createStatement();
	      int itemId=0;
	      ResultSet getTableId = stmt.executeQuery("SELECT * FROM TICKET_ITEM WHERE TICKET_ID="+ticketId);
	       
	      int i=0;
	      
	      while (getTableId.next()) {
	    	  itemId=getTableId.getInt("ID");
	    	  
	    	  String sql = "UPDATE TICKET_ITEM SET ITEM_ORDER="+ i +" WHERE TICKET_ID="+ticketId +" AND ID="+getTableId.getInt("ID");
		          stmt2.executeUpdate(sql);
		         
	    	  i++;	
		        result=true;
	      }
	      
	      getTableId.close();
	       	     
	      stmt.close();
	      stmt1.close();
	      stmt2.close();
	      conn.close();
	      
	      return result;
	   }

	public void updateText(List<Ticket> tickets) {
		double totalCash = 0.00;
		double totalCard = 0.00;
		double total = 0.00;
		double totalRechnug = 0.00;

		for(Ticket ticket: tickets) {			
			 
			if(TerminalConfig.isTagesAbsRefresh()) {
				try {
				chk=updateTicketItems(ticket.getId());
				 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				 
			}
			
			if(isWholeSale)
				ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			if(ticket.getSplitPayment() != null && ticket.getSplitPayment()) {
				totalCash += (ticket.getTotalAmount()-ticket.getCardAmount());
				totalCard += ticket.getCardAmount();
			} else if(isWholeSale&&ticket.getRechnugPayemnt()!=null&&ticket.getRechnugPayemnt()) {
				totalRechnug += ticket.getTotalAmount();
			} else if(ticket.getCashPayment() != null && ticket.getCashPayment()) {
				totalCash += ticket.getTotalAmount();
			} else if(ticket.getCashPayment() != null && !ticket.getCashPayment()) {
				totalCard += ticket.getTotalAmount();
			}

			total += ticket.getTotalAmount();
			
		};
		 
		 
		tfCash.setText(NumberUtil.formatNumber(totalCash) + Application.getCurrencySymbol());         
		tfCard.setText(NumberUtil.formatNumber(totalCard) + Application.getCurrencySymbol());
		tfTotal.setText(NumberUtil.formatNumber(total) + Application.getCurrencySymbol());
		if(isWholeSale)
			tfRechnug.setText(NumberUtil.formatNumber(totalRechnug) + Application.getCurrencySymbol());

	}
	
	public void setLabelFont(JLabel lbl) {
		lbl.setFont(new Font(null,Font.PLAIN, 20));
	}

	public void setTextFont(JTextField tf) {
		tf.setFont(new Font(null,Font.PLAIN, 20));
		tf.setEditable(false);
	}

	class TicketExplorerTableModel extends ListTableModel {		
		String[] columnNames = { "ID", POSConstants.USER,
				com.floreantpos.POSConstants.CREATE_TIME, POSConstants.SALES_NET,
				com.floreantpos.POSConstants.DISCOUNT, POSConstants.TAX,
				com.floreantpos.POSConstants.TOTAL, "Status" };
		
		String[] columnNames_ = { "ID", POSConstants.USER,
				com.floreantpos.POSConstants.CREATE_TIME, POSConstants.SALES_NET,
				com.floreantpos.POSConstants.DISCOUNT, POSConstants.TAX,
				com.floreantpos.POSConstants.TOTAL, "Status" , com.floreantpos.POSConstants.PRINT };
		
		String[] columnNames1 = { "ID", POSConstants.USER,
				com.floreantpos.POSConstants.CREATE_TIME,com.floreantpos.POSConstants.CUSTOMER, com.floreantpos.POSConstants.Anschrift,
				com.floreantpos.POSConstants.DISCOUNT,
				com.floreantpos.POSConstants.TOTAL, "Status", com.floreantpos.POSConstants.PRINT };
		TicketExplorerTableModel() {
			if(TerminalConfig.isWholeSale()) {
				setColumnNames(columnNames1);
			} 
			
			if(TerminalConfig.isRechGedruktDisplay()){ 
				setColumnNames(columnNames_);
			} else {
				setColumnNames(columnNames);
			}
		}
		@Override
		public int getRowCount() {
			if (tickets == null) {
				return 0;
			}
			return tickets.size();
		}

		@Override
		public int getColumnCount() {
			int colmLeng=0;
			if(isWholeSale) {
				colmLeng= columnNames1.length;
			
			} else if(TerminalConfig.isRechGedruktDisplay()){
				colmLeng=columnNames_.length;
			} else {
				colmLeng= columnNames.length;
			}
			
			return colmLeng;
		}

		@Override
		public String getColumnName(int column) {
			String columnName="";
			if(isWholeSale)
				columnName= columnNames1[column];
			
			else if(TerminalConfig.isRechGedruktDisplay())
				columnName= columnNames_[column];
			else
				columnName= columnNames[column];
			
			return columnName;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0&&columnIndex==2)
				return true;    	
			return false;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			try
			{ 	SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yy,   HH:mm");
			Ticket ticket = tickets.get(rowIndex);
			if(ticket == null) {
				return;
			}
			if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0&&columnIndex==2)
			{			
				if(value.toString() != null && value.toString().length() > 0) {
					ticket.setCreateDate(dt.parse(value.toString()));
					Calendar currentTime = Calendar.getInstance();
					currentTime.setTime(dt.parse(value.toString()));		              
					ticket.setCreateDate(currentTime.getTime());
					ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));
					ticket.setClosingDate(currentTime.getTime());
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}
			}
			}
			catch(Exception e){e.printStackTrace();}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (tickets == null)
				return ""; //$NON-NLS-1$

			Ticket ticket = null;
			try {
				ticket =tickets.get(rowIndex);
			} catch (Exception e) {}


			if(ticket == null) {
				return null;
			}
			String customerName="";
			String customerPostCode="";
			String customerCity="";
			String customerFirmName="";
			String customerDoor="";
			String customerAddress="";
			if(isWholeSale) {
				try {
					ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
					customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
					if (customerName == null || customerName.compareTo("null") == 0 || customerName.length() <= 0)
						customerName = "";
					customerPostCode = ticket.getProperty(Ticket.CUSTOMER_POSTCODE);
					if (customerPostCode == null || customerPostCode.compareTo("null") == 0 || customerPostCode.length() <= 0)
						customerPostCode = "";
					customerCity = ticket.getProperty(Ticket.CUSTOMER_CITYNAME);
					if (customerCity == null || customerCity.compareTo("null") == 0 || customerCity.length() <= 0)
						customerCity = "";
					customerFirmName = ticket.getProperty(Ticket.CUSTOMER_FIRMNAME);
					if (customerFirmName == null || customerFirmName.compareTo("null") == 0 || customerFirmName.length() <= 0)
						customerFirmName = "";
					customerDoor = ticket.getProperty(Ticket.CUSTOMER_DOOR);
					if (customerDoor == null || customerDoor.compareTo("null") == 0 || customerDoor.length() <= 0)
						customerDoor = "";
					customerAddress = ticket.getProperty(Ticket.CUSTOMER_ADDRESS);
					if (customerAddress == null || customerAddress.compareTo("null") == 0 || customerAddress.length() <= 0)
						customerAddress = "";
				}catch(Exception ex) {

				}				

			}

			switch (columnIndex) {
			case 0:
				return tickets.size() - rowIndex;

			case 1:
				User owner = ticket.getOwner();
				return owner != null ? owner.getFirstName() : "";

			case 2:
				SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yy,   HH:mm");
				String date = dt.format(ticket.getCreateDate());
				return date;
			case 3:
				if(isWholeSale) {
					if(StringUtils.isEmpty(customerFirmName))
						return customerName;
					else if (!StringUtils.isEmpty(customerName))
						return customerFirmName+" ("+customerName+")";
				}else					
					return NumberUtil.formatNumber(Double
							.valueOf(ticket.getTotalAmount() - (ticket.getTaxAmount())));

			case 4:
				if(isWholeSale) {
					if(!StringUtils.isEmpty(customerAddress)&&!StringUtils.isEmpty(customerDoor))
						return customerAddress+" "+customerDoor+","+customerPostCode+" "+customerCity;
					else
						return customerAddress+" "+customerDoor+" "+customerPostCode+" "+customerCity;
				} else
					return NumberUtil.formatNumber(Double.valueOf(ticket.getDiscountAmount()));

			case 5:
				if(isWholeSale) {
					return NumberUtil.formatNumber(Double.valueOf(ticket.getDiscountAmount()));
				} else
					return NumberUtil.formatNumber(Double.valueOf(ticket.getTaxAmount()));
			case 6:	
				if(ticket.getGutscheinPayment()!=null && ticket.getGutscheinPayment()==true) {
					return NumberUtil.formatNumber(ticket.getTotalAmount());
				}
				return NumberUtil.formatNumber(ticket.getTotalAmount()+ticket.getDiscountAmount());

			
			case 7: {
				String status = "";
				if (ticket.isPaid()) {
					if (ticket.getSplitPayment()!=null&&ticket.getSplitPayment()) {
						status = POSConstants.BOTH_PAID;
					} else if (ticket.getRechnugPayemnt()) {
						 
						if (ticket.isRechnugpaid()&&isWholeSale) {						 
							return POSConstants.BILL+", Bezahlt";
						} else if(isWholeSale) {
							return POSConstants.BILL+", Offen";
						} else {							 
							status = POSConstants.BILL +" Bezahlt";
							//return POSConstants.BILL+", Bezahlt";
						}
					}  else if(ticket.getGutscheinPayment()!=null && ticket.getGutscheinPayment() ){
						status = "Gudschein";
					} else if (ticket.getCashPayment()) {
						status = POSConstants.CASH_PAID;
							
					} else {
						status = POSConstants.CARD_PAID;
					}

					if (ticket.isDrawerResetted())
						status = status + ", T";

				} else {
					status = POSConstants.NOT_PAID;
					if (ticket.isVoided())
						status = status + ", L";
					if (ticket.isDrawerResetted()) {
						System.out.println("ticket.isDrawerResetted(): " + ticket.isDrawerResetted());
						status = status + ", T";}
				}
				return status;
			}
			
			case 8:
				String printStatus ="";
				if(ticket.getPrinted()||ticket.isDrawerResetted()||ticket.getTicketid()!=null&&ticket.getTicketid()>1)
					printStatus = "Gedr.";				
				return printStatus;	
				
			}
			return null;
		}
	}
}
