package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.CashBookDataExplorer;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CashBookData;
import com.floreantpos.model.Cashbook;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.CashbookDAO;
import com.floreantpos.model.dao.CashbookDataDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.SalesReport;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class CashBookExplorer extends POSDialog {
	private List<Cashbook> cashbookList;
	private JXTable table;
	private CashbookTableModel tableModel;
	JXDatePicker dpDate;
	JTextField tfTotal;
	private JComboBox dpMonth;
	private JComboBox dpYear;
	private String monthText;
	DateFormat fmt = new SimpleDateFormat("MMMM dd, yyyy");
	DateFormat fmt1 = new SimpleDateFormat("MMMM - yyyy");
	Restaurant rest;

	public CashBookExplorer() {
		setTitle("Kassenbuch");
		rest = RestaurantDAO.getRestaurant();
		dpDate = new JXDatePicker();
		dpDate.setFont(new Font(null, Font.BOLD, 24));
		dpDate.setDate(new Date());	
		this.setPreferredSize(new Dimension(1000, 600));
		final CashbookDAO dao = new CashbookDAO();
		cashbookList = dao.findByDate(DateUtil.startOfDay(dpDate.getDate()), DateUtil.endOfDay(dpDate.getDate()));

		tableModel = new CashbookTableModel();
		table = new JXTable(tableModel);
		table.setRowHeight(35);
		refreshCashbookData();
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);


		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.getTableHeader().setBackground(new Color(209, 222, 235));
		setLayout(new BorderLayout(5, 5));
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane, BorderLayout.CENTER);
		jScrollPane.getViewport().setBackground(new Color(209, 222, 235));
		// ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();

		final PosButton editButton = new PosButton("Bearbeiten");
		editButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		editButton.setBackground(new Color(102, 255, 102));

		final PosButton addButton = new PosButton("Hinzufuegen");
		addButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		addButton.setBackground(new Color(102, 255, 102));

		final PosButton cashbookDataButton = new PosButton("Beschribung Hinzufügen");
		cashbookDataButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		cashbookDataButton.setBackground(new Color(102, 255, 102));

		final PosButton deleteButton = new PosButton("Loeschen");
		deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		deleteButton.setBackground(new Color(255, 153, 153));

		cashbookDataButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CashBookDataExplorer explorer = new CashBookDataExplorer();
				explorer.setPreferredSize(new Dimension(800, 600));
				explorer.pack();
				explorer.open();
				refreshCashbookData();
				updateTillDate();
			}
		});
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					Cashbook cashbook = (Cashbook) tableModel.getRowData(index);
					CashBookForm dialog = new CashBookForm(cashbook);
					dialog.pack();
					dialog.open();
					if (dialog.isCanceled())
						return;
					cashbookList = dao.findByDate(DateUtil.startOfDay(dpDate.getDate()),
							DateUtil.endOfDay(dpDate.getDate()));
					tableModel.setRows(cashbookList);
					updateTotal();
					table.repaint();
					table.revalidate();

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CashBookForm dialog = new CashBookForm(dpDate.getDate(), false);
					dialog.pack();
					dialog.open();

					cashbookList = dao.findByDate(DateUtil.startOfDay(dpDate.getDate()),
							DateUtil.endOfDay(dpDate.getDate()));
					tableModel.setRows(cashbookList);
					updateTotal();
					table.repaint();
					table.revalidate();

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					// Cashbook cashbook = cashbookList.get(index);
					Cashbook cashbook = (Cashbook) tableModel.getRowData(index);
					CashbookDAO.getInstance().delete(cashbook);
					cashbookList = dao.findByDate(DateUtil.startOfDay(dpDate.getDate()),
							DateUtil.endOfDay(dpDate.getDate()));
					tableModel.setRows(cashbookList);
					updateTotal();
					table.repaint();
					table.revalidate();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		final PosButton printButton = new PosButton("Druck");
		printButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		printButton.setBackground(new Color(102, 255, 102));
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					HashMap map = new HashMap();
					map.put("month", monthText);
					map.put("zero", "0%");
					map.put("ninteen", "19%");
					map.put("seven", "7%");
					map.put("Kassenbestand", "Kassenbestand");
					List<Cashbook> cashbooklist = tableModel.getRows();
					Double einnahmenz = 0.00;
					Double einnahmens = 0.00;
					Double einnahmenn = 0.00;
					Double ausgabenz = 0.00;
					Double ausgabens = 0.00;
					Double ausgabenn = 0.00;
					Double vorsteuerz = 0.00;
					Double vorsteuers = 0.00;
					Double vorsteuern = 0.00;
					Double mwstz = 0.00;
					Double mwsts = 0.00;
					Double mwstn = 0.00;
					Double total = 0.00;
					/*
					 * for (Cashbook cashbook : cashbooklist) { com.floreantpos.model.CashBookData
					 * info = CashbookDataDAO.getInstance()
					 * .findByBeschreibung(cashbook.getBeschreibung()).stream().findFirst().orElse(
					 * null); if (info == null || info.isEinzahlung()) { total +=
					 * cashbook.getBetrag(); einnahmenn += cashbook.getBetrag(); //mwstn +=
					 * cashbook.getTaxamount(); } else { total -= cashbook.getBetrag(); //ausgabens
					 * += cashbook.getBetrag(); ausgabenn += cashbook.getBetrag();
					 * 
					 * } }
					 */

					for (Cashbook cashbook : cashbooklist) {
						if (cashbook.getAdddata()) {
							total += cashbook.getBetrag();
							einnahmenn += cashbook.getBetrag();
						} else {
							total -= cashbook.getBetrag();
							ausgabenn += cashbook.getBetrag();
						}
						if (cashbook.getBeschreibung().contains("Anfangs Bestand")
								|| cashbook.getBeschreibung().contains("Anfangs Bestand")) {
							if (cashbook.getAdddata()) {
								map.put("anfangsbestand", "von " + cashbook.getBezeichnung() + "   "
										+ NumberUtil.roundToTwoDigit(cashbook.getBetrag()));
							} else {
								map.put("anfangsbestand", "von " + cashbook.getBezeichnung() + "   - "
										+ NumberUtil.roundToTwoDigit(cashbook.getBetrag()));
							}
						}
					}

					map.put("einnahmenzero", NumberUtil.formatNumber(einnahmenz));
					map.put("einnahmenn", NumberUtil.formatNumber(einnahmenn));
					map.put("einnahmens", NumberUtil.formatNumber(einnahmens));

					map.put("ausgabenzero", NumberUtil.formatNumber(ausgabenz));
					map.put("ausgabenn", NumberUtil.formatNumber(ausgabenn));
					map.put("ausgabens", NumberUtil.formatNumber(ausgabens));

					map.put("vorsteuerzero", NumberUtil.formatNumber(vorsteuerz));
					map.put("vorsteuern", NumberUtil.formatNumber(vorsteuern));
					map.put("vorsteuers", NumberUtil.formatNumber(vorsteuers));

					map.put("mwstz", NumberUtil.formatNumber(mwstz));
					map.put("mwstn", NumberUtil.formatNumber(mwstn));
					map.put("mwsts", NumberUtil.formatNumber(mwsts));
					map.put("KassenbestandAmt", NumberUtil.formatNumber(total, true) + " €");

					ReportUtil.populateRestaurantProperties(map);
					JasperReport masterReport = (JasperReport) JRLoader.loadObject(
							SalesReport.class.getResource("/com/floreantpos/report/template/CashbookReport.jasper"));
					JasperPrint print = JasperFillManager.fillReport(masterReport, map,
							new JRTableModelDataSource(tableModel));
					print.setName("Kassenbuch");
					print.setProperty("printerName", Application.getPrinters().getA4Printer());
					JReportPrintService.printQuitelyA4(print);

				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		final PosButton pdfButton = new PosButton("Zum-PDF");
		pdfButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
		pdfButton.setBackground(new Color(102, 255, 102));
		pdfButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					HashMap map = new HashMap();
					map.put("month", monthText);
					map.put("zero", "0%");
					map.put("ninteen", "19%");
					map.put("seven", "7%");
					map.put("Kassenbestand", "Kassenbestand");
					List<Cashbook> cashbooklist = tableModel.getRows();
					Double einnahmenn = 0.00;
					Double ausgabenn = 0.00;
					Double total = 0.00;
					for (Cashbook cashbook : cashbooklist) {
						if (cashbook.getAdddata()) {
							total += cashbook.getBetrag();
							einnahmenn += cashbook.getBetrag();
						} else {
							total -= cashbook.getBetrag();
							ausgabenn += cashbook.getBetrag();
						}
						if (cashbook.getBeschreibung().contains("Anfangs Bestand")
								|| cashbook.getBeschreibung().contains("Anfangs Bestand")) {
							if (cashbook.getAdddata()) {
								map.put("anfangsbestand", "von " + cashbook.getBezeichnung() + "   "
										+ NumberUtil.roundToTwoDigit(cashbook.getBetrag()));
							} else {
								map.put("anfangsbestand", "von " + cashbook.getBezeichnung() + "   - "
										+ NumberUtil.roundToTwoDigit(cashbook.getBetrag()));
							}
						}

					}
					map.put("einnahmenn", NumberUtil.formatNumber(einnahmenn));

					map.put("ausgabenn", NumberUtil.formatNumber(ausgabenn));

					map.put("KassenbestandAmt", NumberUtil.formatNumber(total, true) + " €");

					ReportUtil.populateRestaurantProperties(map);
					JasperReport masterReport = (JasperReport) JRLoader.loadObject(
							SalesReport.class.getResource("/com/floreantpos/report/template/CashbookReport.jasper"));
					JasperPrint print = JasperFillManager.fillReport(masterReport, map,
							new JRTableModelDataSource(tableModel));
					print.setName("Kassenbuch");
					print.setProperty("printerName", Application.getPrinters().getA4Printer());
					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
					String formatdate = format.format(new Date());
					String name = "Kassenbuch_" + formatdate;
					FileOutputStream outputstream = new FileOutputStream(new File("pdf/" + name + ".pdf"));
					JasperExportManager.exportReportToPdfStream(print, outputstream);
					outputstream.close();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		/*
		 * Anfangs bestand
		 */
		final PosButton anfangsBestand = new PosButton("Anfangs Bestand");
		anfangsBestand.setFont(new Font("Times New Roman", Font.BOLD, 15));
		anfangsBestand.setBackground(new Color(102, 255, 102));
		anfangsBestand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// calculateAnfangsBestand();
				calculateAnfangsBestandDaily();
				cashbookList = dao.findByDate(DateUtil.startOfDay(dpDate.getDate()),
						DateUtil.endOfDay(dpDate.getDate()));
				tableModel.setRows(cashbookList);
				updateTotal();
				table.repaint();
				table.revalidate();
			}
		});


		final PosButton btnCalculate = new PosButton("Update");

		btnCalculate.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnCalculate.setBackground(new Color(102, 255, 102));
		btnCalculate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar c = Calendar.getInstance();
				Calendar d = Calendar.getInstance();
				int year = Integer.parseInt(dpYear.getSelectedItem().toString());
				int month = DateUtil.getMonth(dpMonth.getSelectedItem().toString());

				c.set(year, month - 1, 1);
				Date fromDate = c.getTime();
				d.set(year, month, 1);
				d.add(Calendar.DAY_OF_MONTH, -1);
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				try {
					fromDate = df.parse(TerminalConfig.getStartDate());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Date toDate = dpDate.getDate();
				long loop = daysBetween(fromDate, toDate);
				for(int i=1; i<=(loop+1);i++) {
					Date startDate = BusinessDateUtil.startOfOfficialDay(fromDate);
					toDate = BusinessDateUtil.endOfOfficialDay(fromDate);
					List<Ticket> ticketList = TicketDAO.getInstance().findAllTickets(startDate, toDate);
					if(CashbookDAO.getInstance().findByDateAndKonto(startDate, toDate, 8300).size()==0&&CashbookDAO.getInstance().findByDateAndKonto(startDate, toDate, 8400).size()==0)					
						CalculateForKassenBook(ticketList, fromDate);
					try {						
						updateCashbook(CashbookDAO.getInstance().findByDate(DateUtil.startOfDay(fromDate),DateUtil.endOfDay(fromDate)), SalesReportDAO.getInstance().findByDate(fromDate).get(0).getSalesid());
					}catch(Exception ex) {
//						ex.printStackTrace();
						updateCashbook(CashbookDAO.getInstance().findByDate(DateUtil.startOfDay(fromDate),DateUtil.endOfDay(fromDate)), 0);
					}
					
					c.set(Calendar.DATE, c.get(Calendar.DATE)+1);
					fromDate = c.getTime();
				}
				JOptionPane.showMessageDialog(null, "Erfolg");
				cashbookList = dao.findByDate(fromDate,	toDate);
				tableModel.setRows(cashbookList);
				updateTotal();
				table.repaint();
				table.revalidate();
			}
		});


		final PosButton btnDatevExport = new PosButton("Datev-Export");
		btnDatevExport.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnDatevExport.setBackground(new Color(102, 255, 102));
		btnDatevExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {				
				List<Cashbook> cashbooklistTemp = getCashbookList(false);
				List<Cashbook> cashbooklist = new ArrayList<>();
				try {
					for (Cashbook cash : cashbooklistTemp) {
						if (cash.getBeschreibung().contains("Anfangs Bestand Ein.")
								|| cash.getBeschreibung().contains("Anfangs Bestand Aus.")) {

						}else
						cashbooklist.add(cash);
					}					
					doDatevExport(cashbooklist);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});


		tfTotal = new JTextField(20);
		tfTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		tfTotal.setFont(new Font(null, Font.BOLD, 14));
		JButton pck_btn = (JButton) dpDate.getComponent(1);
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/calendar.png"));
		pck_btn.setIcon(imageIcon);

		dpDate.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 20));
		dpDate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButton.setEnabled(true);
				editButton.setEnabled(true);
				deleteButton.setEnabled(true);
				updateTillDate();
			}
		});
		TransparentPanel northpanel = new TransparentPanel();
		northpanel.setBackground(new Color(209, 222, 235));
		northpanel.setLayout(new MigLayout());
		northpanel.add(dpDate, "cell 5 0");

		dpMonth = new JComboBox();
		dpMonth.setPreferredSize(new Dimension(100, 35));
		dpMonth.setFont(new Font("Times New Roman", Font.BOLD, 22));
		dpMonth.setBackground(Color.WHITE);
		DateUtil.populateMonth(dpMonth);
		Calendar c = Calendar.getInstance();
		dpMonth.setSelectedIndex(c.getTime().getMonth());
		dpMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addButton.setEnabled(false);
				editButton.setEnabled(false);
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

				monthText = dpMonth.getSelectedItem().toString() + " - " + dpYear.getSelectedItem().toString();

				List<Cashbook> testList = new ArrayList<>();
				System.out.println("From " + fromDate + ", to " + toDate);

				List<Cashbook> newList = CashbookDAO.getInstance().findByDate(fromDate, toDate);
				newList.forEach(dt -> System.out.println(dt.getDate()));
				System.out.println(newList.size());
				Collections.sort(newList, new Cashbook.CashbookComparator());
				int i = 0;
				boolean first = false;
				for (Cashbook cBook : newList) {
					
					if (cBook.getBeschreibung().contains("Anfangs Bestand")&&!first) {
						testList.add(cBook);
						first = true;
					}else if (cBook.getBeschreibung().contains("Anfangs Bestand")){
						
					} else {
						testList.add(cBook);
					}
					i++;
					System.out.println(cBook.getDate());

				}
				
				tableModel.setRows(testList);
				updateTotal();
				table.repaint();
				table.validate();
			}
		});
		dpYear = new JComboBox();
		dpYear.setPreferredSize(new Dimension(100, 35));
		dpYear.setFont(new Font("Times New Roman", Font.BOLD, 22));
		dpYear.setBackground(Color.WHITE);
		DateUtil.populateYear(dpYear);
		northpanel.add(dpMonth, "cell 6 0");
		northpanel.add(dpYear, "cell 7 0");
		northpanel.add(tfTotal, "cell 8 0");
		northpanel.add(btnCalculate, "cell 9 0");
		northpanel.add(btnDatevExport, "cell 10 0, wrap");

		add(northpanel, BorderLayout.NORTH);
		TransparentPanel panel = new TransparentPanel();
		panel.setBackground(new Color(209, 222, 235));
		getContentPane().setBackground(new Color(209, 222, 235));
		panel.setLayout(new MigLayout());
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(cashbookDataButton);
		panel.add(printButton);
		panel.add(pdfButton);
		panel.add(anfangsBestand);
		add(panel, BorderLayout.SOUTH);
		updateTotal();
	}


	private List<Cashbook> getCashbookList(boolean all){
		List<Cashbook> cashList = new ArrayList<>();
		if(!all) {
			Calendar c = Calendar.getInstance();
			Calendar d = Calendar.getInstance();
			int year = Integer.parseInt(dpYear.getSelectedItem().toString());
			int month = DateUtil.getMonth(dpMonth.getSelectedItem().toString());
			c.set(year, month - 1, 1);
			Date fromDate = c.getTime();
			d.set(year, month, 1);
			d.add(Calendar.DAY_OF_MONTH, -1);
			Date toDate = d.getTime();
			cashList = CashbookDAO.getInstance().findByDate(fromDate, toDate);

			return cashList;
		}else {
			cashList = CashbookDAO.getInstance().findAll();
			return cashList;
		}

	}

	private static long daysBetween(Date start, Date end) {
		long difference =  (end.getTime()-start.getTime())/86400000;
		return Math.abs(difference);
	}

	public void CalculateForKassenBook(List<Ticket> ticketList, Date date) {
		Double IMHAUS_BAR = 0.00;
		Double totalBar19 = 0.00;
		Double totalBar7 = 0.00;
		Double totalCard19 = 0.00;
		Double totalCard7 = 0.00;
		for(Ticket ticket: ticketList) {			
			if(ticket.getTicketType()!=null&&!ticket.isVoided()) {				
				if(ticket.getSplitPayment() != null &&ticket.getSplitPayment()) {
					if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {
						totalBar19 += (ticket.getTotalAmount()-ticket.getCardAmount());
						totalCard19 += ticket.getCardAmount();						
					} else {
							ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
							double Nineteen =0.0;
							double seven =0.0;
							double cardAmnt =ticket.getCardAmount();
							double barAmnt =ticket.getTotalAmount()-ticket.getCardAmount();
							for(TicketItem item:ticket.getTicketItems()) {
								if (item.isBeverage()) {
									Nineteen += item.getSubtotalAmount(); 
								} else {
									seven += item.getSubtotalAmount();
								}
							}							
							if(cardAmnt<Nineteen) {
								totalCard19 += cardAmnt;
								totalBar19 += Nineteen-cardAmnt;
								totalBar7 += seven;
							}else if(cardAmnt<seven) {
								totalCard7 += cardAmnt;
								totalBar7 += seven-cardAmnt;
								totalBar19 += Nineteen;
							}else if(barAmnt<Nineteen) {
								totalBar19 += barAmnt;
								totalCard19 += Nineteen-barAmnt;
								totalCard7 += seven;
							}else if(barAmnt<seven) {
								totalBar7 += barAmnt;
								totalCard7 += seven-barAmnt;
								totalCard19 += Nineteen;
							}else {
								totalBar7 += seven/2;
								totalBar19 +=  Nineteen/2;
								totalCard7 += seven/2;
								totalCard19 += Nineteen/2;
							}
							
					}

				}else if(ticket.getCashPayment()!=null&&ticket.getCashPayment()) {
					if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {
						if (ticket.getCashPayment() != null && ticket.getCashPayment()) { 
							totalBar19 += ticket.getTotalAmount();	
						}
					} else {						
							ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
							for(TicketItem item:ticket.getTicketItems()) {
								if (item.isBeverage()) {
									totalBar19 += item.getSubtotalAmount(); 
								} else {
									totalBar7 += item.getSubtotalAmount();
								}
							}						
					}

				}else {
					if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {					
							totalCard19 += ticket.getTotalAmount();							
					} else {
							ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
							for(TicketItem item:ticket.getTicketItems()) {
								if (item.isBeverage()) {
									totalCard19 += item.getSubtotalAmount(); 
								} else {
									totalCard7 += item.getSubtotalAmount();
								}
						}
					}
					
				}
			}

		}
		IMHAUS_BAR = totalBar19+totalBar7;
		SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy");
		Calendar calll = Calendar.getInstance();
		calll.setTime(date);		
		String formatdate = format.format(calll.getTime());
		if(totalBar19>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Bar 19% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8400);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalBar19));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		if(totalBar7>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Bar 7% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8300);;
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalBar7));	
			cashbookInf.setSalesId(rest.getSalesid());
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		if(totalCard19>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Karte 19% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8400);
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setBetrag(Math.abs(totalCard19));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		if(totalCard7>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Karte 7% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8300);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalCard7));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		if(totalCard19>0.00||totalCard7>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Karte Zahlung");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(false);
			cashbookInf.setKonto(2000);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalCard7+totalCard19));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		
	}
	
	public void updateCashbook(List<Cashbook> cashbookList, int SalesId) {
		if(cashbookList!=null&&cashbookList.size()>0) {
			for(Cashbook cash:cashbookList) {
				if (cash.getBeschreibung().contains("Anfangs Bestand Ein.")|| cash.getBeschreibung().contains("Anfangs Bestand Aus.")){
					 continue;
				} else if (cash.getBeschreibung().contains("Umsatz Bar")){
						CashbookDAO.getInstance().delete(cash);
				} else {
					List<CashBookData> data = CashbookDataDAO.getInstance().findByBeschreibung(cash.getBeschreibung());
					if(data !=null&&data.size()>0)
						cash.setKonto(data.get(0).getKonto());
					cash.setSalesId(SalesId);
					CashbookDAO.getInstance().saveOrUpdate(cash);
				}
			}
		}
		
	}

	public void updateTillDate() {
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		c.setTime(dpDate.getDate());
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		c.set(year, month, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Date from = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
		final Date to = dpDate.getDate();

		final Date fromDate = DateUtil.startOfDay(dpDate.getDate());
		final Date toDate = DateUtil.endOfDay(dpDate.getDate());
		monthText = fmt.format(toDate);

		List<Cashbook> testList = CashbookDAO.getInstance().findAll();
		System.out.println("From " + fromDate + ", to " + toDate);
		List<Cashbook> newList = CashbookDAO.getInstance().findByDate(DateUtil.startOfDay(dpDate.getDate()),
				DateUtil.endOfDay(dpDate.getDate()));
		newList.forEach(dt -> System.out.println(dt.getDate()));
		CashbookDAO dao = new CashbookDAO();
		Collections.sort(newList, new Cashbook.CashbookComparator());
		tableModel.setRows(newList);
		table.repaint();
		table.validate();
		updateTotal();
	}

	public void refreshCashbookData() {
		//		final Session session = CashbookDAO.getInstance().createNewSession();
		//		final Transaction tx = session.beginTransaction();
		//
		//		cashbookList.forEach(data -> {
		//			Integer id = data.getBeschreibungId();
		//			if (id != null) {
		//				final CashBookData cashbookData = CashbookDataDAO.getInstance().get(id);
		//				if (cashbookData != null) {
		//					data.setBeschreibung(cashbookData.getBeschreibung());
		//					session.saveOrUpdate(data);
		//				}
		//			}
		//		});
		//		tx.commit();
		//		session.close();
		tableModel.setRows(cashbookList);
		table.repaint();
		tableModel.fireTableDataChanged();
	}

	public void updateTotal() {
		Double total = 0.00;
		List<Cashbook> cashbooklist = tableModel.getRows();
		double einnahmen = 0.00;
		double ausgaben = 0.00;
		for (Cashbook cashbook : cashbooklist) {
			if (cashbook.getAdddata()) {
				einnahmen += cashbook.getBetrag();
				total += cashbook.getBetrag();
			} else {
				ausgaben += cashbook.getBetrag();
				total -= cashbook.getBetrag();
			}
		}

		final String ein = "+" + NumberUtil.formatNumber(einnahmen, true);
		final String aus = "-" + NumberUtil.formatNumber(ausgaben, true);
		final String tot = "=" + NumberUtil.formatNumber(total, true);

		tfTotal.setText(ein + aus + tot + " €");
	}

	class CashbookTableModel extends ListTableModel {
		String[] columnNames = { "Beleg", "Datum", "Beschreibung", "Bezeichnung","Einnahmen", "Ausgaben" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		double total = 0.00;

		CashbookTableModel() {
			setColumnNames(columnNames);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex == 0)
				total = 0.00;

			Cashbook item = (Cashbook) rows.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return String.valueOf(rowIndex + 1);
			case 1:
				DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
				if (item.getDate() != null)
					return format.format(item.getDate());
				else
					return (dpMonth.getSelectedItem() + "-" + dpYear.getSelectedItem());
			case 2:
				return item.getBeschreibung();
			case 3:
				return item.getBezeichnung();
			case 4:
				if (item.getAdddata())
					return NumberUtil.formatNumber(item.getBetrag());
				else
					return "0,00";

			case 5:
				if (!item.getAdddata())
					return NumberUtil.formatNumber(item.getBetrag());
				else
					return "0,00";
			}
			return null;
		}

		public void addCashbook(Cashbook cashbook) {
			super.addItem(cashbook);

		}

		public void updateCashbook(int index) {
			super.updateItem(index);
		}

		public void deleteMenuItem(Cashbook category, int index) {
			super.deleteItem(index);

		}
	}

	public void calculateAnfangsBestand() {
		Cashbook cashbookInf = new Cashbook();
		List<Cashbook> cashbooklist = tableModel.getRows();
		boolean exist = false;
		for (Cashbook cash : cashbooklist) {
			if (cash.getBeschreibung().contains("Anfangs Bestand Ein.")
					|| cash.getBeschreibung().contains("Anfangs Bestand Aus.")) {
				exist = true;
				break;
			}
		}
		if (exist) {
			return;
		}

		Date startDate = dpDate.getDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date from = cal.getTime();
		cal.add(Calendar.DAY_OF_MONTH, 31);
		Date toDate = cal.getTime();

		List<Cashbook> testList = CashbookDAO.getInstance().findByDate(DateUtil.startOfDay(from), toDate);
		List<Cashbook> newList = new ArrayList<>();
		double value = 0.00;
		for (Cashbook cBook : testList) {
			// System.out.println(from+"-"+toDate+" cashbook "+cBook.getDate());
			if (cBook.getDate().getMonth() == from.getMonth()) {
				newList.add(cBook);
				value += cBook.getBetrag();
				System.out.println(" cashbook--- " + cBook.getDate() + cBook.getBeschreibung() + value);
			}

		}
		double total = 0.00;
		double einnahmen = 0.00;
		double ausgaben = 0.00;
		for (Cashbook cashbook : newList) {
			if (cashbook.getAdddata()) {
				einnahmen += cashbook.getBetrag();
				total += cashbook.getBetrag();
			} else {
				ausgaben += cashbook.getBetrag();
				total -= cashbook.getBetrag();
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy");

		cal.setTime(startDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);

		Calendar calll = Calendar.getInstance();
		calll.setTime(dpDate.getDate());
		calll.add(Calendar.MONTH, -1);
		String formatdate = format.format(calll.getTime());
		if (total < 0) {
			cashbookInf.setBeschreibung("Anfangs Bestand Aus.");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(false);
			cashbookInf.setDate(cal.getTime());
			cashbookInf.setBetrag(Math.abs(total));
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		} else {
			cashbookInf.setBeschreibung("Anfangs Bestand Ein.");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setDate(cal.getTime());
			cashbookInf.setBetrag(Math.abs(total));
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		updateTillDate();

	}

	/*
	 * Datev Export
	 */
	public void doDatevExport(List<Cashbook> cashbooklist) throws IOException {
		try {
			Collections.sort(cashbooklist, new Cashbook.CashbookComparator());
			String path1 = new File(System.getProperty("user.dir"))+"/resources/extf.xlsx";
			File file = new File(path1);
			FileInputStream fis1 = new FileInputStream(file);
			XSSFWorkbook fs1 = new XSSFWorkbook(fis1);
			XSSFSheet sheet1 = fs1.getSheetAt(0);
			XSSFRow row1;
			XSSFCell cell1;
			try{
				Workbook wb = new XSSFWorkbook();
				Sheet sheet = wb.createSheet();
				Row row = sheet.createRow(0);

				SimpleDateFormat format = new SimpleDateFormat("ddMM");

				File destinationFolder = new File("C:\\Data\\Export\\");
				if (!destinationFolder.exists())
				{
					destinationFolder.mkdirs();
				}
				JFileChooser fileChooser = new JFileChooser();	
				fileChooser.setCurrentDirectory(new java.io.File("C:\\Data\\Export\\"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
				
				int year = Integer.parseInt(dpYear.getSelectedItem().toString());
				int month = DateUtil.getMonth(dpMonth.getSelectedItem().toString());
				Calendar cal = Calendar.getInstance();

				cal.set(year, month - 1, 1);			
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
				Date from = cal.getTime();	
				
				cal.set(year, month, 1);
				cal.add(Calendar.DAY_OF_MONTH, -1);			
				Date toDate = cal.getTime();
				
				File newFile = fileChooser.getSelectedFile();
					newFile = new File(newFile.getPath()+"\\"+"extf_"+rest.getName().toLowerCase()+"_"+rest.getAddressLine3().toLowerCase()+"_"+df.format(from)+".csv");
				
				if(!newFile.getName().toLowerCase().endsWith(".csv"))
					newFile = new File(newFile.getPath()+".csv");

				for (int i = 0; i < cashbooklist.size()+2; i++) {
					row = sheet.createRow(i);
					if(i==0||i==1) {		
						row1 = sheet1.getRow(i);
						if(row1 != null) {
							for(int c = 0; c < getColumn(file); c++) {
								cell1 = row1.getCell((short)c);
								if(c==16) {
									row.createCell(c).setCellValue(df1.format(from)+"-"+df1.format(toDate));
								}else if(c==14) {
									row.createCell(c).setCellValue(df1.format(from));
								}else if(c==15) {
									row.createCell(c).setCellValue(df1.format(toDate));
								}else{
									if(cell1!=null) {
										if(cell1.toString().contains("."))
											row.createCell(c).setCellValue(cell1.toString().substring(0,cell1.toString().indexOf(".")));
										else
											row.createCell(c).setCellValue(cell1.toString());
									}							
									else
										row.createCell(c).setCellValue("");
								}
								
							}
						}
					}else {
						Cashbook cashbook = cashbooklist.get(i-2);
						for (int j = 0; j < getColumn(file); j++) {
							try {
								if(j == 0)
									row.createCell(j).setCellValue(NumberUtil.formatNumber(cashbook.getBetrag()));
								else if(j == 1)
									if(cashbook.getAdddata())
										row.createCell(j).setCellValue("H");
									else if(!cashbook.getAdddata())
										row.createCell(j).setCellValue("S");
									else
										row.createCell(j).setCellValue("");								
								else if(j == 2)
									row.createCell(j).setCellValue("EUR");								
								else if(j == 6)
									row.createCell(j).setCellValue(cashbook.getKonto()+"");									
								else if(j == 7)
									row.createCell(j).setCellValue(1360);
								else if(j == 9)
									row.createCell(j).setCellValue(format.format(cashbook.getDate()));
								else if(j == 10)
									row.createCell(j).setCellValue(cashbook.getBezeichnung());
								else if(j == 12)
									row.createCell(j).setCellValue("0");		
								else if(j == 13)
									row.createCell(j).setCellValue(cashbook.getBeschreibung());
								else if(j == 47)
									row.createCell(j).setCellValue("Abschlussnummer");		
								else if(j == 48)
									row.createCell(j).setCellValue(cashbook.getSalesId());
								else if(j == 49)
									row.createCell(j).setCellValue("Filialnummer");
								else if(j == 50)
									row.createCell(j).setCellValue("0");
								else if(j == 51)
									row.createCell(j).setCellValue("Filialname");
								else if(j == 52)
									row.createCell(j).setCellValue(rest.getName().toLowerCase());
								else
									row.createCell(j).setCellValue("");
							}catch(Exception ex) {
								row.createCell(j).setCellValue("");
								ex.printStackTrace();
							}
						}
					}
				}
				FileOutputStream fileOut = new FileOutputStream(newFile);
				wb.write(fileOut);
				fileOut.close();
				wb.close();
				JOptionPane.showMessageDialog(null, "Erfolg");
			}catch(IOException e){ 
				JOptionPane.showMessageDialog(null, "Fehler::: "+e.getMessage());}

		}catch(FileNotFoundException ex) {JOptionPane.showMessageDialog(null, "Fehler::: "+ex.getMessage());}
			}

	public int getColumn(File file) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);

			XSSFWorkbook fs = new XSSFWorkbook(fis);
			XSSFSheet sheet = fs.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;

			int rows;
			rows = sheet.getPhysicalNumberOfRows();

			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows

			row = sheet.getRow(0);
			if(row != null) {
				tmp = sheet.getRow(1).getPhysicalNumberOfCells();
				if(tmp > cols) cols = tmp;
			}
			return cols;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}



	public void calculateAnfangsBestandDaily() {
		Cashbook cashbookInf = new Cashbook();

		List<Cashbook> cashbooklist = CashbookDAO.getInstance().findByDate(DateUtil.startOfDay(dpDate.getDate()),
				DateUtil.endOfDay(dpDate.getDate()));
		boolean exist = false;
		for (Cashbook cash : cashbooklist) {
			if (cash.getBeschreibung().contains("Anfangs Bestand Ein.")
					|| cash.getBeschreibung().contains("Anfangs Bestand Aus.")) {
				exist = true;
				break;
			}
		}
		if (exist) {
			return;
		}

		Date startDate = dpDate.getDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date from = cal.getTime();

		List<Cashbook> testList = CashbookDAO.getInstance().findByDate(DateUtil.startOfDay(from),
				DateUtil.endOfDay(from));
		List<Cashbook> newList = new ArrayList<>();
		double value = 0.00;
		for (Cashbook cBook : testList) {
			if (cBook.getDate().getMonth() == from.getMonth()) {
				newList.add(cBook);
				value += cBook.getBetrag();
				System.out.println(" cashbook--- " + cBook.getDate() + cBook.getBeschreibung() + value);
			}

		}
		double total = 0.00;
		double einnahmen = 0.00;
		double ausgaben = 0.00;
		for (Cashbook cashbook : newList) {
			if (cashbook.getAdddata()) {
				einnahmen += cashbook.getBetrag();
				total += cashbook.getBetrag();
			} else {
				ausgaben += cashbook.getBetrag();
				total -= cashbook.getBetrag();
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy");

		Calendar calll = Calendar.getInstance();
		calll.setTime(dpDate.getDate());
		calll.set(Calendar.HOUR_OF_DAY, new Date().getHours());
		calll.set(Calendar.MINUTE, new Date().getMinutes());
		String formatdate = format.format(calll.getTime());
		if (total < 0) {
			cashbookInf.setBeschreibung("Anfangs Bestand Aus.");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(false);
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(total));
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		} else {
			cashbookInf.setBeschreibung("Anfangs Bestand Ein.");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(total));
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		System.out.println(" cashbook Value " + total);
		updateTillDate();

	}
}
