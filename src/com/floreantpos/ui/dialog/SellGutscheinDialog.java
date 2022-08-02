package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTable;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Gutschein;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.GutscheinDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.CardDialog.CardPaymentType;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.PrintTicket;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
public class SellGutscheinDialog extends POSDialog implements ActionListener {

	private PosButton fiveCent;
	private PosButton tenCent;
	private PosButton twentyCent;
	private PosButton fiftyCent;
	private PosButton oneEuro;
	private PosButton twoEuro;

	private PosButton fiveEuro;
	private PosButton tenEuro;
	private PosButton twentyEuro;
	private PosButton fiftyEuro;
	private PosButton hundredEuro;
	private PosButton twohundredEuro;

	private JTextField tfTotalAmount;
	private JTextField tfGutscheinName;
	private JTextField tfGutscheinNumber;
	private JTextField tfAnzahl;
	private JComboBox<String> zahlArtList;  

	private PosButton printButton = new PosButton("Druck");
	private PosButton addMore = new PosButton("Add-More");
	boolean added;
	Double totalAmount = 0.0;
	JLabel lblSubTotalAmount;

	public SellGutscheinDialog() {
		init();
	}

	public void init() {
		getContentPane().setBackground(new Color(35, 35, 36));
		initNumberPanel();
		added = false;
	}

	public void createTicketAndPrint() {
		
		if(added&&tfTotalAmount.getText().compareTo("0.00")!=0)
			addGutschein(); System.out.println("FirstAdded");
		
		Ticket ticket = new Ticket();		
		Application application = Application.getInstance();
		ticket.setType(TicketType.DINE_IN);
		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setTerminal(application.getTerminal());
		/*ShopTable shopTable = ShopTableDAO.getInstance().getByNumber("898");
		if (shopTable == null) {
			shopTable = new ShopTable();
			shopTable.setFloor(1);
			shopTable.setName("898");
			shopTable.setNumber("898");
			shopTable.setTickettype(TicketType.DINE_IN.name());
			ShopTableDAO.getInstance().saveOrUpdate(shopTable);
		}*/
		//ticket.addTotables(shopTable);
		ticket.setCreateDate(new Date());
		ticket.setSoldGutschein(true);
		ticket.setOwner(Application.getCurrentUser());
		
		final Restaurant restaurant = RestaurantDAO.getRestaurant();
		int rechnungsNr = 0;
		if (ticket.getTicketid() == null || ticket.getTicketid() == 0) {
			rechnungsNr = restaurant.getTicketid() != null ? restaurant.getTicketid() : 1;
			ticket.setTicketid(rechnungsNr);
			
			restaurant.setTicketid(rechnungsNr+1);
			RestaurantDAO.getInstance().saveOrUpdate(restaurant);
		}
		
		String paidString = tfTotalAmount.getText();
		paidString = paidString.replace(".", "");

		double paidAmount = 0.00;    

		if(added&&!gutsCheinList.isEmpty()) {
			for(Gutschein guts : gutsCheinList) {				
				if(guts.getCount()>1) {
					System.out.println("guts.getCount()>1");
					for(int i =0;i<guts.getCount();i++) {
						Gutschein gutschein = new Gutschein();
						gutschein.setBarcode(generateGutschein());
						gutschein.setClosed(false);
						gutschein.setName(guts.getName());
						gutschein.setRechnungNr(String.valueOf(rechnungsNr));
						gutschein.setUnlimited(true);
						gutschein.setValue(guts.getValue()/guts.getCount());
						gutschein.setCount(1);
						SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
						Date date = new Date();
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.YEAR, 2);
						//    		    try {
						date = cal.getTime();
						//    		      date = df.parse("31.12.2030");
						//    		    } catch (Time e1) {
						//    		      e1.printStackTrace();
						//    		    }
						gutschein.setExpiryDate(date);
						final User user = Application.getInstance().getCurrentUser();
						if (user != null) {
							gutschein.setCreatedBy(user.getFirstName());
						}
						gutschein.setCreatedDate(new Date());
						GutscheinDAO.getInstance().saveOrUpdate(gutschein);
						JReportPrintService.printGutschein(gutschein);
					}
					
				} else {
					System.out.println("guts.getCount()==1");
					Gutschein gutschein = new Gutschein();
					gutschein.setBarcode(guts.getBarcode());
					gutschein.setClosed(false);
					gutschein.setName(guts.getName());
					gutschein.setRechnungNr(String.valueOf(rechnungsNr));
					gutschein.setUnlimited(true);
					gutschein.setValue(guts.getValue());
					gutschein.setCount(1);
					SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
					Date date = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.YEAR, 2);
					//    		    try {
					date = cal.getTime();
					gutschein.setExpiryDate(date);
					final User user = Application.getInstance().getCurrentUser();
					if (user != null) {
						gutschein.setCreatedBy(user.getFirstName());
					}
					gutschein.setCreatedDate(new Date());
					GutscheinDAO.getInstance().saveOrUpdate(gutschein);
					JReportPrintService.printGutschein(gutschein);
				}
				System.out.println("gUTCHEINvALUE: "+guts.getValue()+ " , gutscheinCount: " + guts.getCount());
				ticket.addToticketItems(createTicketItem(ticket, guts.getValue()/guts.getCount(),
						"Gutschein-" 
								+ NumberUtil.formatNumber(guts.getValue()/guts.getCount())+" €",guts.getCount()));			
			}

			paidAmount = totalAmount;
		}else {
			System.out.println("Gutschein List Empty");
			try {
				paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double.parseDouble(paidString.replaceAll(",", "."));
				System.out.println("paidAmount>> " + paidAmount);
			} catch (Exception e) {
			}
              ticket.setTotalAmount(paidAmount);
			int count = 1;
			try {
				count = Integer.parseInt(tfAnzahl.getText());
			}catch(Exception ex) {
				
			}
			
			String name = this.tfGutscheinName.getText();

			if(count>1) {
				for(int i =0;i<count;i++) {
					Gutschein gutschein = new Gutschein();
					gutschein.setBarcode(generateGutschein());
					gutschein.setClosed(false);
					gutschein.setUnlimited(true);
					gutschein.setValue(paidAmount);
					gutschein.setCount(1);			
					gutschein.setName(name);
					gutschein.setRechnungNr(String.valueOf(rechnungsNr));
					SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
					Date date = new Date();
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.YEAR, 2);
					// try {
					date = cal.getTime();
					//    	      date = df.parse("31.12.2030");
					//    	    } catch (Time e1) {
					//    	      e1.printStackTrace();
					//    	    }
					gutschein.setExpiryDate(date);
					final User user = Application.getInstance().getCurrentUser();
					if (user != null) {
						gutschein.setCreatedBy(user.getFirstName());
					}
					gutschein.setCreatedDate(new Date());
					GutscheinDAO.getInstance().saveOrUpdate(gutschein);
					JReportPrintService.printGutschein(gutschein);
				}
				
			} else {
				Gutschein gutschein = new Gutschein();
				gutschein.setBarcode(this.tfGutscheinNumber.getText());
				gutschein.setClosed(false);
				gutschein.setUnlimited(true);
				gutschein.setValue(paidAmount);
				gutschein.setCount(1);			
				gutschein.setName(name);
				gutschein.setRechnungNr(String.valueOf(rechnungsNr));
				SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				Date date = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.YEAR, 2);		
				date = cal.getTime();
				gutschein.setExpiryDate(date);
				final User user = Application.getInstance().getCurrentUser();
				if (user != null) {
					gutschein.setCreatedBy(user.getFirstName());
				}
				gutschein.setCreatedDate(new Date());
				GutscheinDAO.getInstance().saveOrUpdate(gutschein);
				JReportPrintService.printGutschein(gutschein);
			}
			
			ticket.addToticketItems(createTicketItem(ticket, paidAmount,
					"Gutschein-"
							+NumberUtil.formatNumber(paidAmount) +" €",count));
System.out.println("paidAmount: " + paidAmount + ", Count: " + count);

//	    	ticket.addToticketItems(createTicketItem(ticket, paidAmount,
//			    	        this.tfGutscheinName.getText().substring(0,  (Math.min(tfGutscheinName.getText().length(), 10))) +"-" 
//			    	        + paidAmount+" €",1));
		}
	
		ticket.calculatePriceGutshein();
		ticket.setPaidAmount(ticket.getTotalAmount());
		ticket.setDueAmount(0.00);
		ticket.setPaid(true);
		ticket.setClosed(true);
		ticket.setPrinted(true);
		if(zahlArtList.getSelectedItem()!=null&&zahlArtList.getSelectedItem().toString().compareTo("  KARTE  ")==0) {
			ticket.setCashPayment(false);
			ticket.setCardpaymenttype(CardPaymentType.GUTSCHEIN.ordinal());
		} else {
			ticket.setCashPayment(true);
		}

		System.out.println("totalAmount: "+ ticket.getTotalAmount() + ", isPaid: " + ticket.getPaidAmount() + ", cashPaymnt: "+ ticket.getCashPayment());
		OrderController.saveOrder(ticket);

		new PrintTicket(ticket, false, PrintType.REGULAR, true, false);
		this.dispose();
	}
	
	private TicketItem createTicketItem(final Ticket ticket, double price, String name, int count) {
		final TicketItem ticketItem = new TicketItem();
		ticketItem.setBeverage(false);
		ticketItem.setBon(0);
		ticketItem.setCategoryName(com.floreantpos.POSConstants.MISC);
		ticketItem.setGroupName(com.floreantpos.POSConstants.MISC);
		ticketItem.setItemCount(count);
		ticketItem.setItemId(ticketItem.getItemId());
		ticketItem.setId(null);
		ticketItem.setName(name);
	    ticketItem.setTotalAmount(price * count);
	    ticketItem.setSubtotalAmount(price * count);
	    ticketItem.setSubtotalAmountWithoutModifiers(price * count);
	    ticketItem.setTotalAmountWithoutModifiers(price * count);
		ticketItem.setPfand(false);
		ticketItem.setPriceIncludesTax(true);
		ticketItem.setPrintedToKitchen(ticketItem.isPrintedToKitchen());
		ticketItem.setPrintorder(0);
		ticketItem.setShouldPrintToKitchen(true);
		//ticketItem.setTableNumber("898");
		ticketItem.setTaxRate(0.00);
		ticketItem.setTaxAmount(0.00);
		 
		ticketItem.setTicket(ticket);
		ticketItem.setUnitPrice(price);
		return ticketItem;
	}

	DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
		Font font = new Font("Times New Roman", Font.BOLD, 15);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			this.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.setForeground(new Color(246, 196, 120));
			setFont(font);
			return this;
		}
	};

	DefaultTableCellRenderer amountRenderer = new DefaultTableCellRenderer() {
		Font font = new Font("Times New Roman", Font.BOLD, 15);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			this.setAlignmentX(Component.CENTER_ALIGNMENT);

			this.setForeground(new Color(102, 223, 102));
			setFont(font);
			return this;
		}

	};
	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
		Font font = new Font("Times New Roman", Font.BOLD, 15);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
			setFont(font);
			return this;
		}

	}; 



	public void initNumberPanel() {	  
		table = new GutscheinListTable();
		table.setSortable(false);
		table.setModel(tableModel = new GutscheinListTableModel());
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.WHITE);
		table.setBackground(Color.BLACK);
		table.setForeground(Color.WHITE);
		table.setSelectionBackground(Color.GRAY);
		table.setSelectionForeground(Color.WHITE);
		cellRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumn(0).setCellRenderer(cellRenderer);
		table.getColumn(1).setCellRenderer(otherRenderer);
		table.getColumn(2).setCellRenderer(amountRenderer);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);
		table.setRowHeight(60);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(80);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(80);
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(700,400));
		scrollPane
		.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));	  


		fiveCent = new PosButton();
		fiveCent.setText("5 ¢");
		fiveCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(0.05);
			}
		});

		tenCent = new PosButton();
		tenCent.setText("10 ¢");
		tenCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(0.10);
			}
		});

		twentyCent = new PosButton();
		twentyCent.setText("20 ¢");
		twentyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(0.20);
			}
		});

		fiftyCent = new PosButton();
		fiftyCent.setText("50 ¢");
		fiftyCent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(0.50);
			}
		});

		oneEuro = new PosButton();
		oneEuro.setText("1 €");
		oneEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(1.00);
			}
		});

		twoEuro = new PosButton();
		twoEuro.setText("2 €");
		twoEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(2.00);

			}
		});

		fiveEuro = new PosButton();
		try {
			fiveEuro.setIcon(IconFactory.getIconFromImage("fiveeuro.png"));
		} catch (Exception e) {
		}
		fiveEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(5.00);       
			}
		});

		tenEuro = new PosButton();
		try {
			tenEuro.setIcon(IconFactory.getIconFromImage("tenEuro.png"));
		} catch (Exception e) {
		}
		tenEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(10.00);
			}
		});

		twentyEuro = new PosButton();
		try {
			twentyEuro.setIcon(IconFactory.getIconFromImage("twentyEuro.png"));
		} catch (Exception e) {
		}
		twentyEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(20.00);
			}
		});

		fiftyEuro = new PosButton();
		try {
			fiftyEuro.setIcon(IconFactory.getIconFromImage("fiftyEuro.png"));
		} catch (Exception e) {
		}
		fiftyEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(50.00);
			}
		});

		hundredEuro = new PosButton();
		try {
			hundredEuro.setIcon(IconFactory.getIconFromImage("hundredEuro.png"));
		} catch (Exception e) {
		}
		hundredEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(100.00);
			}
		});

		twohundredEuro = new PosButton();
		try {
			twohundredEuro.setIcon(IconFactory.getIconFromImage("twohundredEuro.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		twohundredEuro.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTotalValue(200.00);
			}
		});

		setCoinFont(fiveCent);
		setCoinFont(tenCent);
		setCoinFont(twentyCent);
		setCoinFont(fiftyCent);
		setCoinFont(oneEuro);
		setCoinFont(twoEuro);

		JPanel currencyPanel = new JPanel();
		currencyPanel.setLayout(new GridLayout(4, 2, 0, 0));
//		currencyPanel.add(fiveCent);
//		currencyPanel.add(tenCent);
//		currencyPanel.add(twentyCent);
//		currencyPanel.add(fiftyCent);
		currencyPanel.add(oneEuro);
		currencyPanel.add(twoEuro);
		currencyPanel.add(fiveEuro);
		currencyPanel.add(tenEuro);
		currencyPanel.add(twentyEuro);
		currencyPanel.add(fiftyEuro);
		currencyPanel.add(hundredEuro);
		currencyPanel.add(twohundredEuro);

		JPanel keyPanel = new JPanel();

		zahlArtList = new JComboBox<String>();
		zahlArtList.setEditable(false);
		zahlArtList.setBackground(Color.WHITE);
		zahlArtList.setSize(30, 30);
		zahlArtList.addItem("   BAR   ");
		zahlArtList.addItem("  KARTE  ");

		JLabel lblZahlArt = new JLabel("Zahl-Art.");
		if(StringUtils.isNotEmpty(POSConstants.Zahl_Art))
			lblZahlArt.setText(POSConstants.Zahl_Art);
		
		lblZahlArt.setFont(new Font(null, Font.BOLD, 18));
		lblZahlArt.setForeground(Color.WHITE);

		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		keyPanel.add(qwertyKeyPad);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new MigLayout());
		JLabel lblTotal = new JLabel("Gesamt");
		lblTotal.setFont(new Font(null, Font.BOLD, 18));
		lblTotal.setForeground(Color.WHITE);

		JLabel lblPaidAmount = new JLabel("Bezahlt");
		lblPaidAmount.setFont(new Font(null, Font.BOLD, 18));
		lblPaidAmount.setForeground(Color.WHITE);
		PosButton resetButton = new PosButton("RESET");
		resetButton.setFont(new Font(null, Font.BOLD, 18));
		resetButton.setForeground(Color.WHITE);
		resetButton.setBackground(new Color(125, 6, 42));
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfTotalAmount.setText("0.00");
			}
		});

		PosButton cancelButton = new PosButton("ABBRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		
		cancelButton.setFont(new Font(null, Font.BOLD, 18));
		cancelButton.setForeground(Color.WHITE);
		cancelButton.setBackground(new Color(125, 6, 42));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		getContentPane().setLayout(new GridLayout(2, 1, 5, 5));
		getContentPane().setBackground(new Color(35, 35, 36));

		JLabel lblGutscheinNumber = new JLabel("Gutschein Nummer");
		if(StringUtils.isNotEmpty(POSConstants.Gutschein_Nummer))
			lblGutscheinNumber.setText(POSConstants.Gutschein_Nummer);
		
		lblGutscheinNumber.setFont(new Font(null, Font.BOLD, 18));
		lblGutscheinNumber.setForeground(Color.WHITE);
		tfGutscheinNumber = new JTextField(15);
		tfGutscheinNumber.setEditable(false);
		setTextFieldFont(tfGutscheinNumber);
		tfGutscheinNumber.setText(generateGutschein());

		JLabel lblGutscheinName = new JLabel("Gutschein Note");
		if(StringUtils.isNotEmpty(POSConstants.Gutschein_Note))
			lblGutscheinName.setText(POSConstants.Gutschein_Note);
		
		lblGutscheinName.setFont(new Font(null, Font.BOLD, 18));
		lblGutscheinName.setForeground(Color.WHITE);
		tfGutscheinName = new JTextField(15);
		tfGutscheinName.grabFocus();
		setTextFieldFont(tfGutscheinName);
		tfGutscheinName.setText("Geschenkgutschein");

		JLabel lblTotalAmount = new JLabel("Gutschein preis");
		if(StringUtils.isNotEmpty(POSConstants.Gutschein_preis))
			lblTotalAmount.setText(POSConstants.Gutschein_preis);
		
		lblTotalAmount.setFont(new Font(null, Font.BOLD, 18));
		lblTotalAmount.setForeground(Color.WHITE);
		tfTotalAmount = new JTextField(15);
		tfTotalAmount.setEditable(false);
		setTextFieldFont(tfTotalAmount);
		tfTotalAmount.setText("0.00");


		JLabel lblAnzahl = new JLabel("Anzahl");
		if(StringUtils.isNotEmpty(POSConstants.SALES_COUNT))
			lblAnzahl.setText(POSConstants.SALES_COUNT);
		
		lblAnzahl.setFont(new Font(null, Font.BOLD, 18));
		lblAnzahl.setForeground(Color.WHITE);
		tfAnzahl = new JTextField(15);
		setTextFieldFont(tfAnzahl);
		tfAnzahl.setText("1");    

		printButton.setBackground(new Color(2, 64, 2));
		printButton.setForeground(Color.WHITE);
		printButton.setFont(new Font("Tahoma", Font.BOLD, TerminalConfig.getTouchScreenFontSize()));
		printButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//        String paidString = tfTotalAmount.getText();
				//        double totalAmount = 0;
				//        try {
				//          totalAmount = StringUtils.isBlank(paidString) ? 0.00 : Double.parseDouble(paidString.replaceAll(",", "."));
				//        } catch (Exception ex) {
				//        }
				if (totalAmount > 0) {
					createTicketAndPrint();
				}
			 }
		});

		addMore.setBackground(new Color(2, 64, 2));
		addMore.setForeground(Color.WHITE);
		addMore.setFont(new Font("Tahoma", Font.BOLD, TerminalConfig.getTouchScreenFontSize()));
		addMore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(tfTotalAmount.getText().compareTo("0.00")!=0) {
					addGutschein();
					added = true;
				}				
			}
		});

		topPanel.add(lblAnzahl);
		topPanel.add(tfAnzahl, "growx, wrap");

		topPanel.add(lblGutscheinNumber);
		topPanel.add(tfGutscheinNumber, "growx, wrap");

		topPanel.add(lblGutscheinName);
		topPanel.add(tfGutscheinName, "growx, wrap");

		topPanel.add(lblTotalAmount);
		topPanel.add(tfTotalAmount, "growx, wrap");

		JLabel lblSubTotalText = new JLabel("Gesamt Preis ");
		if(StringUtils.isNotEmpty(POSConstants.TOTAL))
			lblSubTotalText.setText(POSConstants.TOTAL);
		
		lblSubTotalText.setFont(new Font(null, Font.BOLD, 18));
		lblSubTotalText.setForeground(Color.WHITE);

		lblSubTotalAmount = new JLabel(NumberUtil.formatNumber(totalAmount));
		lblSubTotalAmount.setFont(new Font(null, Font.BOLD, 18));
		lblSubTotalAmount.setForeground(Color.WHITE);	


		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout());
		buttonPanel.add(lblSubTotalText);
		
		buttonPanel.add(lblZahlArt);
		buttonPanel.add(zahlArtList, "growx, wrap");
		
		buttonPanel.add(lblSubTotalAmount, "growx");
		buttonPanel.add(add(addMore), "grow, wrap");
		buttonPanel.add(printButton,"growx");
		buttonPanel.add(resetButton,"growx");
		buttonPanel.add(cancelButton,"growx, wrap");

		JPanel northWestDividerContainer = new JPanel();
		northWestDividerContainer.setLayout(new GridLayout(3, 1, 5, 5));
		northWestDividerContainer.add(scrollPane);
		northWestDividerContainer.add(topPanel);
		northWestDividerContainer.add(buttonPanel);

		northWestDividerContainer.setBackground(new Color(35, 35, 36));
		buttonPanel.setBackground(new Color(35, 35, 36));
		topPanel.setBackground(new Color(35, 35, 36));
		keyPanel.setBackground(new Color(35, 35, 36));
		currencyPanel.setBackground(new Color(35, 35, 36));

		JPanel northContainer = new JPanel();
		northContainer.setLayout(new GridLayout(0, 2, 0, 0));
		northContainer.add(northWestDividerContainer);
		northContainer.add(currencyPanel);

		JPanel southContainer = new JPanel();
		southContainer.setLayout(new GridLayout(0, 1, 0, 0));
		southContainer.add(keyPanel);

		getContentPane().add(northContainer);
		getContentPane().add(southContainer);

	}

	public String generateGutschein() {
		while (true) {
			Random randomGenerator = new Random();
			int randomNumber = randomGenerator.nextInt(2147483640);
			if (randomNumber > 0) {
				if (GutscheinDAO.getInstance().findByBarcode(randomNumber + "")==null)
					return String.valueOf(randomNumber);
			}
		}
	}

	public void setFont(PosButton button, String text) {
		button.setBackground(new Color(102, 51, 0));
		button.setForeground(Color.WHITE);
		button.setFont(new Font(null, Font.BOLD, 22));
		if (text.length() > 0) {
			button.setText(text);
		}
	}

	private void setTextFieldFont(JTextField textField) {
		textField.setFont(new Font(null, Font.BOLD, 20));
		textField.setBackground(Color.BLACK);
		textField.setForeground(Color.WHITE);
		textField.setCaretColor(Color.WHITE);
	}

	public void setCoinFont(PosButton button) {
		button.setBackground(Color.BLACK);
		button.setForeground(Color.WHITE);
		button.setFont(new Font(null, Font.BOLD, 22));
	}

	private void setTotalValue(double cents) {
		String paidString = tfTotalAmount.getText();
		paidString = paidString.replace(".", "");
		int anzahl = 1;
		try {
			anzahl = Integer.parseInt(tfAnzahl.getText());
		}catch(Exception ex) {
			
		}
		try {
			Double paidAmount = StringUtils.isBlank(paidString) ? 0.00 : Double.parseDouble(paidString.replaceAll(",", "."));
			paidAmount = paidAmount + cents;
			tfTotalAmount.setText(NumberUtil.formatNumber(paidAmount));
			totalAmount +=cents*anzahl;
			lblSubTotalAmount.setText(NumberUtil.formatNumber(totalAmount)+" €");
		} catch (Exception e) {

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * Gutschein Table
	 */

	public GutscheinListTable table;
	public GutscheinListTableModel tableModel;
	List<Gutschein> gutsCheinList = new ArrayList<>();

	public List<Gutschein> getSelectedGutschein() {
		int[] selectedRows = table.getSelectedRows();
		ArrayList<Gutschein> tickets = new ArrayList<Gutschein>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			Gutschein gutschein = (Gutschein) tableModel.getRowData(selectedRows[i]);
			tickets.add(gutschein);
		}
		return tickets;
	}

	public class GutscheinListTable extends JXTable {

		public GutscheinListTable() {
			setColumnControlVisible(true);
		}
	}

	public void addGutschein() {
		Gutschein gutschein = new Gutschein();		
		gutschein.setBarcode(tfGutscheinNumber.getText());
		int count = 1;
		try {
			count = Integer.parseInt(tfAnzahl.getText());
		}catch(Exception ex ) {

		}
		String paidString = tfTotalAmount.getText();
		paidString = paidString.replace(".", "");
		
		Double gutscheinValue = Double.parseDouble(paidString.replace(",", "."))*count;
//		totalAmount = totalAmount+gutscheinValue;				
//		lblSubTotalAmount.setText(NumberUtil.formatNumber(totalAmount)+" €");
		gutschein.setCount(count);
		gutschein.setValue(gutscheinValue);
		gutschein.setName(tfGutscheinName.getText());
		gutsCheinList.add(gutschein);
		tfGutscheinNumber.setText(generateGutschein());
		tfTotalAmount.setText("0.00");
		tfGutscheinName.setText("Geschenkgutschein");
		tfAnzahl.setText("1");
		tableModel.addItem(gutschein);
	}
	private class GutscheinListTableModel extends ListTableModel {

		public GutscheinListTableModel() {

			super(new String[] { "Anzahl", "Gutshein-Note","SUMME"});

		}


		public Object getValueAt(int rowIndex, int columnIndex) {
			Gutschein gutschein = (Gutschein) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return gutschein.getCount();

			case 1:
				return gutschein.getName();

			case 2:		

				return NumberUtil.formatNumber(gutschein.getValue());

			}

			return null;
		}
	}


}
