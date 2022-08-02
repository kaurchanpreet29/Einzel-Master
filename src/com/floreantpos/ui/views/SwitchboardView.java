/*
 *
 * Created on August 14, 2006, 11:45 PM
 */

package com.floreantpos.ui.views;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;

import com.ctc.wstx.util.DataUtil;
import com.floreantpos.ITicketList;
import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.actions.AuthorizeTicketAction;
import com.floreantpos.actions.NewBarTabAction;
import com.floreantpos.actions.OpenKitchenDisplayAction;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.actions.ShutDownAction;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.customer.CustomerExplorer;
import com.floreantpos.demo.KitchenDisplay;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.extension.OrderServiceExtension;
import com.floreantpos.main.Application;
import com.floreantpos.model.AttendenceHistory;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Shift;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.UserType;
import com.floreantpos.model.dao.AttendenceHistoryDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.DriverReport;
import com.floreantpos.report.MonthReportViewer;
import com.floreantpos.report.MonthSalesReport;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportViewer;
import com.floreantpos.report.SalesReport;
import com.floreantpos.report.ServerReport;
import com.floreantpos.services.TicketService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TicketListView.TicketListTable;
import com.floreantpos.ui.dialog.BarCodeDialog;
import com.floreantpos.ui.dialog.Calculator;
import com.floreantpos.ui.dialog.CardDialog.CardPaymentType;
import com.floreantpos.ui.dialog.CashBookExplorer;
import com.floreantpos.ui.dialog.CustomerDialog;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.ManagerDialog;
import com.floreantpos.ui.dialog.MonthReportViewerDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.OfficialPaymentDialog;
import com.floreantpos.ui.dialog.OfficialPaymentDialog.OfficialPaymentType;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.PaperBuyDialog;
import com.floreantpos.ui.dialog.PayoutDialog;
import com.floreantpos.ui.dialog.ReportViewerDialog;
import com.floreantpos.ui.dialog.ReservationDialog;
import com.floreantpos.ui.dialog.VoidTicketDialog;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.ui.views.order.DefaultOrderServiceExtension;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;
import com.floreantpos.ui.views.payment.SettleTicketDialog;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;
import com.floreantpos.util.PosGuiUtil;
import com.floreantpos.util.TicketAlreadyExistsException;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.transaction.FiskalyPaymentType;
import com.khana.tse.fiskaly.transaction.FiskalyReceiptType;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author MShahriar
 */
public class SwitchboardView extends JPanel implements ActionListener,
ITicketList, KeyListener {
	private final AutoLogoffHandler logoffHandler = new AutoLogoffHandler();

	public final static String VIEW_NAME = com.floreantpos.POSConstants.SWITCHBOARD;

	private OrderServiceExtension orderServiceExtension;

	private static SwitchboardView instance;

	private Timer autoLogoffTimer = new Timer(1000, logoffHandler);

	// private Timer ticketListUpdater;

	/** Creates new form SwitchboardView */
	public SwitchboardView() {
		initComponents();
		btnBackOffice.addActionListener(this);
		btnEditTicket.addActionListener(this);
		btnGroupSettle.addActionListener(this);
		btnLogout.addActionListener(this);
		btnSalesReport.addActionListener(this);
		btnMonthSalesReport.addActionListener(this);
		btnCalculator.addActionListener(this);
		btnDriverReport.addActionListener(this);
		btnServerReport.addActionListener(this);
		btnCustomer.addActionListener(this);
		btnPayroll.addActionListener(this);
		btnReservation.addActionListener(this);
		btnNewTicket.addActionListener(this);
		btnPayout.addActionListener(this);
		btnOrderInfo.addActionListener(this);
		btnSettleTicket.addActionListener(this);
		btnSplitTicket.addActionListener(this);
		btnVoidTicket.addActionListener(this);
		btnCashBook.addActionListener(this);
		// orderServiceExtension =
				// Application.getPluginManager().getPlugin(OrderServiceExtension.class);
		orderServiceExtension = Application.getCustomerPlugin();
		// if (orderServiceExtension == null) {

		applyComponentOrientation(ComponentOrientation.getOrientation(Locale
				.getDefault()));

		instance = this;
	}

	public class ArrowAction extends AbstractAction {

		private String cmd;

		public ArrowAction(String cmd) {
			this.cmd = cmd;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (cmd.equalsIgnoreCase("2")) {
				doCreateNewTicket(TicketType.DINE_IN);
				;
			} else if (cmd.equalsIgnoreCase("ESC")) {
				doLogout();
			} else if (cmd.equalsIgnoreCase("ArrowUp")) {
				arrowUp();
			} else if (cmd.equalsIgnoreCase("ArrowDown")) {
				arrowDown();
			} else if (cmd.equalsIgnoreCase("Enter")) {
				selectTicket();
			}
		}
	}

	public static SwitchboardView getInstance() {
		return instance;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	Restaurant restaurant;
	private void initComponents() {
		InputMap im = getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "2");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESC");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "ArrowUp");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "ArrowDown");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");

		am.put("2", new ArrowAction("2"));
		am.put("ESC", new ArrowAction("ESC"));
		am.put("ArrowUp", new ArrowAction("ArrowUp"));
		am.put("ArrowDown", new ArrowAction("ArrowDown"));
		am.put("Enter", new ArrowAction("Enter"));
		am.put("Left", new ArrowAction("Left"));
		am.put("Right", new ArrowAction("Right"));

		this.setFocusable(true);

		restaurant = RestaurantDAO.getInstance().getRestaurant();
		lblUserName = new javax.swing.JLabel();
		btnAllTickets = new com.floreantpos.swing.PosButton();
		btnAllTickets.setBackground(Color.BLACK);
		btnAllTickets.setForeground(Color.WHITE);
		 
		
		if(StringUtils.isNotEmpty(POSConstants.ALL_ORDERS))
			btnAllTickets.setText(POSConstants.ALL_ORDERS);
		else
			btnAllTickets.setText("ALLE BESTELLUNGEN");
		
		javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
		javax.swing.JPanel bottomLeftPanel = new javax.swing.JPanel();
		openTicketList = new com.floreantpos.ui.TicketListView(this);
		javax.swing.JPanel activityPanel = new javax.swing.JPanel();
		btnNewTicket = new com.floreantpos.swing.PosButton();
		btnEditTicket = new com.floreantpos.swing.PosButton();
		btnVoidTicket = new com.floreantpos.swing.PosButton();
		btnVoidTicket.setEnabled(false);
		btnCashBook = new com.floreantpos.swing.PosButton();
		btnCashBook.setEnabled(false);
		btnPayout = new com.floreantpos.swing.PosButton();
		btnOrderInfo = new com.floreantpos.swing.PosButton();
		javax.swing.JPanel bottomRightPanel = new javax.swing.JPanel();
		btnShutdown = new com.floreantpos.swing.PosButton(new ShutDownAction());
		btnShutdown.setBackground(Color.RED);
		btnLogout = new com.floreantpos.swing.PosButton();
		btnBackOffice = new com.floreantpos.swing.PosButton();
		btnSalesReport = new com.floreantpos.swing.PosButton();
		btnMonthSalesReport = new com.floreantpos.swing.PosButton();
		btnCalculator = new com.floreantpos.swing.PosButton();
		btnDriverReport = new com.floreantpos.swing.PosButton();
		btnServerReport = new com.floreantpos.swing.PosButton();
		btnCustomer = new com.floreantpos.swing.PosButton();
		btnPayroll = new com.floreantpos.swing.PosButton();
		btnReservation = new com.floreantpos.swing.PosButton();
		btnAuthorize = new PosButton(new AuthorizeTicketAction());
		btnAuthorize.setBackground(Color.BLUE);
		btnKitchenDisplay = new PosButton(new OpenKitchenDisplayAction());
		btnKitchenDisplay.setBackground(Color.getHSBColor(159, 63, 90));

		setLayout(new java.awt.BorderLayout(10, 10));
		cbDrivers = new JComboBox();
		tbTotal = new JTextField();
		tbTotal.setEditable(false);
		cbDrivers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbDrivers.getSelectedIndex() != -1) {
					tbTotal.setText(null);
					btnAllTickets.setBackground(Color.BLACK);
					updateTicketList(1, cbDrivers.getSelectedItem().toString());
				}

			}
		});
		createHeaderPanel();

		bottomPanel.setLayout(new java.awt.BorderLayout(5, 5));
		bottomPanel.setFocusable(false);
		TitledBorder createTitledBorder = javax.swing.BorderFactory
				.createTitledBorder(null, POSConstants.OPEN_TICKETS_AND_ACTIVITY,
						javax.swing.border.TitledBorder.CENTER,
						javax.swing.border.TitledBorder.DEFAULT_POSITION);
		createTitledBorder.setTitleColor(Color.WHITE);
		bottomLeftPanel.setBorder(createTitledBorder);
		bottomLeftPanel.setLayout(new java.awt.BorderLayout(5, 5));
		bottomLeftPanel.add(openTicketList, java.awt.BorderLayout.CENTER);
		bottomLeftPanel.setFocusable(false);
		activityPanel.setLayout(new java.awt.GridLayout(2, 0, 5, 5));
		activityPanel.setFocusable(false);
		if(StringUtils.isNotEmpty(POSConstants.NEW_ORDER))
			btnNewTicket.setText(POSConstants.NEW_ORDER);
		else
			btnNewTicket.setText("BESTELLUNG");
		btnNewTicket.setBackground(new Color(2, 64, 2));
		btnNewTicket.setForeground(Color.WHITE);
		activityPanel.setBackground(new Color(5, 29, 53));

		btnBarTab = new PosButton("BAR TAB");
		activityPanel.add(btnNewTicket);

		btnBarTab.setAction(new NewBarTabAction(this));

		btnEditTicket.setText(POSConstants.CAPITAL_EDIT);
		btnEditTicket.setBackground(new Color(2, 64, 2));
		btnEditTicket.setForeground(Color.WHITE);

		btnBarcode = new PosButton();
		btnBarcode.setBackground(new Color(2, 64, 2));
		btnBarcode.setForeground(Color.WHITE);
		btnBarcode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				showBarCode();
			}
		});
		btnBarcode.setText("BARCODE");
		if (TerminalConfig.isBarCodeEnable())
			activityPanel.add(btnBarcode);
		btnSettleTicket = new com.floreantpos.swing.PosButton();

		if (restaurant.getSettle() != null) {
			btnSettleTicket.setText(restaurant.getSettle());
		} else
			btnSettleTicket.setText(POSConstants.CAPITAL_SETTLE);

		btnSettleTicket.setBackground(new Color(102, 178, 255));

		btnGroupSettle = new com.floreantpos.swing.PosButton();

		btnSplitTicket = new com.floreantpos.swing.PosButton();
		btnSplitTicket.setBackground(new Color(102, 178, 255));

		if (restaurant.getSplitticket() != null)
			btnSplitTicket.setText(restaurant.getSplitticket());
		else
			btnSplitTicket.setText(POSConstants.CAPITAL_SPLIT);

		btnCashBook.setText(TerminalConfig.getCashbookText());
		btnCashBook.setBackground(new Color(2, 64, 2));
		btnCashBook.setForeground(Color.WHITE);
		if(TerminalConfig.isCashBookEnable())
			activityPanel.add(btnCashBook);

		btnShowOldTicket = new com.floreantpos.swing.PosButton();
		btnShowOldTicket.setBackground(new Color(2, 64, 2));
		btnShowOldTicket.setForeground(Color.WHITE);
		if (restaurant.getOldticket() != null)
			btnShowOldTicket.setText(restaurant.getOldticket());
		else
			btnShowOldTicket.setText(POSConstants.REOPEN);

		btnShowOldTicket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showOldTicket();
			}
		});

		if (TerminalConfig.isTicketArchive())
			activityPanel.add(btnShowOldTicket);

		btnReopenTicket = new com.floreantpos.swing.PosButton();
		if (restaurant.getReopen() != null) {
			btnReopenTicket.setText(restaurant.getReopen());
		} else
			btnReopenTicket.setText("WIEDER OFFNEN");

		btnReopenTicket.setEnabled(false);
		btnReopenTicket.setBackground(new Color(125, 6, 42));
		btnReopenTicket.setForeground(Color.WHITE);
		btnReopenTicket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doReopenTicket();
			}
		});
		if (TerminalConfig.isTicketReopen())
			activityPanel.add(btnReopenTicket);

		if (restaurant.getDeleteticket() != null)
			btnVoidTicket.setText(restaurant.getDeleteticket());
		else
			btnVoidTicket.setText(POSConstants.VOID);

		btnVoidTicket.setBackground(new Color(125, 6, 42));
		btnVoidTicket.setForeground(Color.WHITE);
		if (TerminalConfig.isTicketDelete())
			activityPanel.add(btnVoidTicket);

		btnPayout.setText(POSConstants.CAPITAL_PAY_OUT);

		btnOrderInfo.setText(POSConstants.ORDER_INFO);

		bottomLeftPanel.add(activityPanel, java.awt.BorderLayout.SOUTH);
		bottomLeftPanel.setBackground(new Color(5, 29, 53));

		btnGroupSettle.setText("<html><body>" + POSConstants.CAPITAL_GROUP + "<br>"
				+ POSConstants.CAPITAL_SETTLE + "</body></html>");
		btnGroupSettle.setBackground(new Color(125, 6, 42));

		btnAllTickets.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbDrivers.getItemCount() == 0) {
					List<User> driversList = UserDAO.getInstance().findDrivers();
					for (Iterator itr = driversList.iterator(); itr.hasNext();) {
						User user = (User) itr.next();
						cbDrivers.addItem(user.getFirstName());
					}
				}
				btnAllTickets.setBackground(new Color(2, 64, 2));
				updateView(POSConstants.ALL_TICKETS);
			}
		});
		btnCloseOrder = new PosButton();
		btnCloseOrder.setBackground(new Color(125, 6, 42));
		btnCloseOrder.setForeground(Color.WHITE);
		btnCloseOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				doCloseOrder();
			}
		});
		if (restaurant.getCloseticket() != null)
			btnCloseOrder.setText(restaurant.getCloseticket());
		else
			btnCloseOrder.setText(POSConstants.CLOSE_ORDER);
		/*
		 * if (TerminalConfig.isHomeDeliveryEnable() ||
		 * TerminalConfig.isOnlineBtnEnable()) activityPanel.add(btnCloseOrder);
		 */

		bottomPanel.add(bottomLeftPanel, java.awt.BorderLayout.CENTER);

		// bottomRightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
		// "OTHERS", javax.swing.border.TitledBorder.CENTER,
		// javax.swing.border.TitledBorder.DEFAULT_POSITION));
		bottomRightPanel.setFocusable(false);
		bottomRightPanel.setBackground(new Color(5, 29, 53));
		btnLogout.setText(POSConstants.CAPITAL_LOGOUT + " (ESC)");
		btnLogout.setBackground(new Color(125, 6, 42));
		btnLogout.setForeground(Color.WHITE);

		btnBackOffice.setText(POSConstants.CAPITAL_BACK_OFFICE);
		btnBackOffice.setBackground(new Color(57, 46, 46));
		btnBackOffice.setForeground(Color.WHITE);

		btnCalculator.setText("TASCHENRECHNER");
		btnCalculator.setBackground(new Color(57, 46, 46));
		btnCalculator.setForeground(Color.WHITE);
 

		if(StringUtils.isNotEmpty(POSConstants.DAILY_REPORT))
			btnSalesReport.setText(POSConstants.DAILY_REPORT);
		else
			btnSalesReport.setText("TAGESABSCHLUSS");
		
		btnSalesReport.setBackground(new Color(57, 46, 46));
		btnSalesReport.setForeground(Color.WHITE);
  
		if(StringUtils.isNotEmpty(POSConstants.MONTHLY_REPORT))
			btnMonthSalesReport.setText(POSConstants.MONTHLY_REPORT);
		else
			btnMonthSalesReport.setText("MONATSABSCHLUSS");
		
		btnMonthSalesReport.setBackground(new Color(57, 46, 46));
		btnMonthSalesReport.setForeground(Color.WHITE);

		if(StringUtils.isNotEmpty(POSConstants.DRIVER_REPORT))
			btnDriverReport.setText(POSConstants.DRIVER_REPORT);
		else
			btnDriverReport.setText("FAHRER ABSCHLUSS");
		
		btnDriverReport.setBackground(new Color(57, 46, 46));
		btnDriverReport.setForeground(Color.WHITE);
		 
		if(StringUtils.isNotEmpty(POSConstants.EMPLOYEE_REPORT))
			btnServerReport.setText(POSConstants.EMPLOYEE_REPORT);
		else
			btnServerReport.setText("MITARBEITERABSCHLUSS");
		
		btnServerReport.setBackground(new Color(57, 46, 46));
		btnServerReport.setForeground(Color.WHITE);

		if(StringUtils.isNotEmpty(POSConstants.CUSTOMER))
			btnCustomer.setText(POSConstants.CUSTOMER);
		else
			btnCustomer.setText("KUNDEN");
		
		btnMonthSalesReport.setBackground(new Color(57, 46, 46));
		btnMonthSalesReport.setForeground(Color.WHITE);
		btnCustomer.setBackground(new Color(57, 46, 46));
		btnCustomer.setForeground(Color.WHITE);

		if(StringUtils.isNotEmpty(POSConstants.BON_BESTELLUGEN))
			btnPayroll.setText(POSConstants.BON_BESTELLUGEN);
		else
			btnPayroll.setText("BON BESTELLEN");
		
		btnPayroll.setBackground(new Color(57, 46, 46));
		btnPayroll.setForeground(Color.WHITE);

		if(StringUtils.isNotEmpty(POSConstants.TERMINE))
			btnReservation.setText(POSConstants.TERMINE);
		else
			btnReservation.setText("Termine");
		
		btnReservation.setBackground(new Color(57, 46, 46));
		btnReservation.setForeground(Color.WHITE);

		bottomPanel.add(bottomRightPanel, java.awt.BorderLayout.EAST);
		bottomPanel.setBackground(new Color(5, 29, 53));
		bottomRightPanel.setLayout(new MigLayout(
				"aligny bottom, insets 1 2 1 2, gapy 10", "[140px]", "[][][][][]"));

		final FloorLayoutPlugin floorLayoutPlugin = Application.getPluginManager()
				.getPlugin(FloorLayoutPlugin.class);
		if (floorLayoutPlugin != null) {		
			PosButton btnTicketsAndTables = new PosButton();
			
			if(StringUtils.isNotEmpty(POSConstants.TISCHE))
				btnTicketsAndTables.setText(POSConstants.TISCHE);
			else
				btnTicketsAndTables.setText("TISCHE");
			
			btnTicketsAndTables.setBackground(Color.getHSBColor(159, 63, 90));
			btnTicketsAndTables.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					floorLayoutPlugin.openTicketsAndTablesDisplay();
				}
			});

			bottomRightPanel.add(btnTicketsAndTables, "height pref!,grow,wrap");
		}

		if (TerminalConfig.isBonRoll()) {
			bottomRightPanel.add(btnPayroll, "height pref!,grow,wrap");
		}

		if (TerminalConfig.isCalculator()) {
			bottomRightPanel.add(btnCalculator, "height pref!,grow,wrap");
		}
		bottomRightPanel.add(btnSalesReport, "height pref!,grow,wrap");

		if (TerminalConfig.isMonthReport()) {
			bottomRightPanel.add(btnMonthSalesReport, "height pref!,grow,wrap");
		}

		if (TerminalConfig.isServerReport()) {
			bottomRightPanel.add(btnServerReport, "height pref!,grow,wrap");
		}

		if (TerminalConfig.isMainCustomerButton()) {
			bottomRightPanel.add(btnCustomer, "height pref!,grow,wrap");
		}

		bottomRightPanel.add(btnBackOffice, "height pref!,grow,wrap");

		if (TerminalConfig.isReservation()) {
			bottomRightPanel.add(btnReservation, "height pref!,grow,wrap");
		}

		bottomRightPanel.add(btnLogout, "height pref!,grow,wrap");

		add(bottomPanel, java.awt.BorderLayout.CENTER);
		this.setFocusable(true);
		setBackground(new Color(5, 29, 53));
	}// </editor-fold>//GEN-END:initComponents

	private void selectTicket() {
		JXTable table = openTicketList.getTable();
		int index = table.getSelectedRow();
		if (index != -1) {
			Ticket ticket = openTicketList.getSelectedTicket();
			if (!ticket.isPaid())
				editTicket(ticket);
			else if (ticket.isPaid()
					&& (ticket.getTicketType().compareTo(TicketType.HOME_DELIVERY.name()) == 0)) {
				doCloseOrder();
			}
		}
	}

	private void arrowDown() {
		TicketListTable table = openTicketList.getTable();
		int nextRow = table.getSelectedRow() + 1;
		if (table.getSelectedRow() != -1) {
			if (table.getRowCount() > 4 && table.getSelectedRow() > 3) {
				table.scrollDown();
			}
			if (nextRow <= table.getRowCount() - 1)
				table.setRowSelectionInterval(0, nextRow);
		} else if (table.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
		}
	}

	private void arrowUp() {
		TicketListTable table = openTicketList.getTable();
		int previousRow = table.getSelectedRow() - 1;

		if (table.getSelectedRow() != -1) {
			if (table.getRowCount() > 3 && table.getSelectedRow() < 5) {
				table.scrollUp();
			}
			if (previousRow >= 0)
				table.setRowSelectionInterval(0, previousRow);
		} else if (table.getRowCount() > 0) {
			table.setRowSelectionInterval(0, 0);
		}
	}

	private void createHeaderPanel() {
		JPanel statusPanel = new JPanel(new MigLayout());
		// statusPanel.setPreferredSize(new Dimension(100, 40));
		java.awt.Font headerFont = new java.awt.Font("Dialog", Font.BOLD, 12);

		lblUserName.setFont(headerFont);

		statusPanel.add(lblUserName);
		statusPanel.add(btnAllTickets);
		if (TerminalConfig.isHomeDeliveryEnable()
				|| TerminalConfig.isOnlineBtnEnable()
				|| TerminalConfig.isPickupEnable()) {
			// statusPanel.add(btnOpenHomeTickets);
			/*
			 * if(!TerminalConfig.isPickupEnable())
			 * statusPanel.add(btnCloseHomeTickets);
			 */
		}

		/*
		 * if(TerminalConfig.isHomeDeliveryEnable() &&
		 * (!TerminalConfig.isPickupEnable())) { statusPanel.add(cbDrivers); }
		 */
		statusPanel.add(tbTotal);
		if(TerminalConfig.isDebugMode()) {
		dpDateTransfer = new JXDatePicker();
		dpDateTransfer.getMonthView().setFont(new Font("Times New Roman", Font.PLAIN, 28));
		dpDateTransfer.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		dpDateTransfer.setDate(new Date());
		dpDateTransfer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTicketListDate(dpDateTransfer.getDate());
			}
		});
		statusPanel.add(dpDateTransfer);
		}
		cbDrivers.setBackground(Color.WHITE);
		cbDrivers.setFont(new Font("Times New Roman", Font.BOLD, 16));
		cbDrivers.setPreferredSize(new Dimension(100, 40));

		tbTotal.setBackground(new Color(255, 229, 204));
		tbTotal.setFont(new Font("Times New Roman", Font.BOLD, 16));
		tbTotal.setPreferredSize(new Dimension(100, 40));

		timerLabel.setHorizontalAlignment(JLabel.RIGHT);
		timerLabel.setFont(headerFont);
		statusPanel.add(timerLabel);
		statusPanel.setBackground(new Color(5, 29, 53));
		add(statusPanel, java.awt.BorderLayout.NORTH);
	}

	protected void doCloseOrder() {
		Ticket ticket = getFirstSelectedTicket();
		if (ticket == null)
			return;
		int due = (int) POSUtil.getDouble(ticket.getDueAmount());
		if (due != 0) {
			POSMessageDialog.showError("Diese Rechnung ist nicht bezahlt");
			return;
		}

		int option = JOptionPane.showOptionDialog(Application.getPosWindow(),
				"Moechten Sie dieser Rechnung# " + ticket.getId() + " schliessen",
				"Bestaetigen", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);

		if (option != JOptionPane.OK_OPTION) {
			return;
		}

		ticket.setClosed(true);
		TicketDAO.getInstance().saveOrUpdate(ticket);

		User driver = ticket.getAssignedDriver();
		if (driver != null) {
			driver.setAvailableForDelivery(true);
			UserDAO.getInstance().saveOrUpdate(driver);
		}

		updateTicketList(POSConstants.HOME_CLOSED);
	}

	protected void doAssignDriver() {
		try {

			Ticket ticket = getFirstSelectedTicket();

			if (ticket == null) {
				return;
			}

			if (ticket.getType() != TicketType.HOME_DELIVERY) {
				POSMessageDialog
				.showError("Beauftragen eines Fahrers ist nicht moeglich fuer diese Bestellung");
				return;
			}
			if (StringUtils.isEmpty(ticket.getDeliveryAddress())) {
				POSMessageDialog
				.showError("Beauftragen eines Fahrers ist nicht moeglich fuer diese Bestellung");
				return;
			}
			User assignedDriver = ticket.getAssignedDriver();
			if (assignedDriver != null) {
				int option = JOptionPane.showOptionDialog(Application.getPosWindow(),
						"Zugewiesen an " + assignedDriver.getFirstName()
						+ ". Moechten Sie das aendern?", "Bestaetigung",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						null, null);

				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			orderServiceExtension.assignDriver(ticket.getId());
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
			LogFactory.getLog(SwitchboardView.class).error(e);
		}
	}

	private void doReopenTicket() {
		try {

			Ticket ticket = getFirstSelectedTicket();
			 
			int ticketId = 0;
			if (ticket == null) {
				
				
				if(StringUtils.isNotEmpty(POSConstants.Geben_Oder_Scannen))
					ticketId = NumberSelectionDialog2
					.takeIntInput(POSConstants.Geben_Oder_Scannen);
				else
					ticketId = NumberSelectionDialog2
						.takeIntInput("Geben Oder Scannen Sie die Bestellungsnummer ein");

				if ((ticketId == -1) || (ticketId == 0)) {
					return;
				}
				ticket = TicketDAO.getInstance().loadFullTicket(ticketId);
			}

			if (ticket == null) {
				throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + ticketId
						+ " " + POSConstants.FOUND);
			}
			if (ticket.isVoided()) {
				if(StringUtils.isNotEmpty(POSConstants.Geloechte_Rechnungen))
					throw new PosException(POSConstants.Geloechte_Rechnungen);
				else
				    throw new PosException("Geloechte Rechnungen koennten nicht geoeffnet");
			}
			ShopTable tab = ShopTableDAO.getInstance().getByNumber(
					ticket.getTableNumbers());
			if (tab != null && tab.isOccupied()) {
				POSMessageDialog.showError("Tisch#" + ticket.getTableNumbers()
				+ " ist belegt");
				return;
			}
			ticket.setClosed(false);
			if(Application.getCurrentUser().getFirstName().compareTo("Master")!=0) {
				ticket.setClosingDate(null);
			}

			ticket.setReOpened(true);
			String tableNumber = ticket.getTableNumbers();
			ticket.setPaid(false);
			ticket.setCashPayment(false);
			ticket.setOnlinePayment(false);
			
			
			TicketDAO.getInstance().saveOrUpdate(ticket);

			updateTicketList();

			if ((tableNumber.compareTo("99") != 0)
					&& (tableNumber.compareTo("98") != 0)) {
				ShopTable table = ShopTableDAO.getInstance().getByNumber(tableNumber);
				if (table != null) {

					table.setOccupied(true);
					ShopTableDAO.getInstance().saveOrUpdate(table);

				}
			}

			// String ticketTotalAmount = Application.getCurrencySymbol() +
			// NumberUtil.formatNumber(ticket.getTotalAmount());
			// String amountMessage = "<span style='color: red; font-weight: bold;'>"
			// + ticketTotalAmount + "</span>";
			// String message = "<html><body>Ticket amount is " + ticketTotalAmount
			// +
			// ". To reopen ticket, you need to refund that amount to system.<br/>Please press <b>OK</b> after you refund amount "
			// + amountMessage
			// + "</body></html>";
			//
			// int option = JOptionPane.showOptionDialog(this, message, "Alert!",
			// JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
			// null, null);
			// if (option != JOptionPane.OK_OPTION) {
			// return;
			// }
			if(TerminalConfig.isTseEnable()) {
				OrderView.getInstance().getTicketView().getPosTseController().setRestartReceipt(true);
			}
			
			OrderView.getInstance().getTicketView().editTicket(ticket);
			
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getLocalizedMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showOldTicket() {
		try {

			Ticket ticket = getFirstSelectedTicket();
			int ticketId = 0;
			if (ticket == null) {
				
				if(StringUtils.isNotEmpty(POSConstants.Geben_Oder_Scannen))
					ticketId = NumberSelectionDialog2
					.takeIntInput(POSConstants.Geben_Oder_Scannen);
				else
			      	ticketId = NumberSelectionDialog2
						.takeIntInput("Geben Oder Scannen Sie die Bestellungsnummer ein");

				if ((ticketId == -1) || (ticketId == 0)) {
					return;
				}
				ticket = TicketDAO.getInstance().loadFullTicket(ticketId);
			} else {
				ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
			}
			if (ticket == null) {
				throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + ticketId
						+ " " + POSConstants.FOUND);
			}

			if (ticket.isVoided()) {
				if(StringUtils.isNotEmpty(POSConstants.Geloechte_Rechnungen))
					throw new PosException(POSConstants.Geloechte_Rechnungen);
				else
				    throw new PosException("Geloechte Rechnungen koennten nicht geoeffnet");
			}

			OrderInfoView view = new OrderInfoView(Arrays.asList(ticket));
			OrderInfoDialog dialog = new OrderInfoDialog(view, false);
			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getLocalizedMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(this, POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doClockOut() {
		int option = JOptionPane.showOptionDialog(this,
				POSConstants.CONFIRM_CLOCK_OUT, POSConstants.CONFIRM,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
				null);
		if (option != JOptionPane.YES_OPTION) {
			return;
		}

		User user = Application.getCurrentUser();
		AttendenceHistoryDAO attendenceHistoryDAO = new AttendenceHistoryDAO();
		AttendenceHistory attendenceHistory = attendenceHistoryDAO
				.findHistoryByClockedInTime(user);
		if (attendenceHistory == null) {
			attendenceHistory = new AttendenceHistory();
			Date lastClockInTime = user.getLastClockInTime();
			Calendar c = Calendar.getInstance();
			c.setTime(lastClockInTime);
			attendenceHistory.setClockInTime(lastClockInTime);
			attendenceHistory.setClockInHour(Short.valueOf((short) c
					.get(Calendar.HOUR)));
			attendenceHistory.setUser(user);
			attendenceHistory.setTerminal(Application.getInstance().getTerminal());
			attendenceHistory.setShift(user.getCurrentShift());
		}

		Shift shift = user.getCurrentShift();
		Calendar calendar = Calendar.getInstance();

		user.doClockOut(attendenceHistory, shift, calendar);

		Application.getInstance().logout();
	}

	private synchronized void doShowBackoffice() {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		if (window == null) {
			window = new BackOfficeWindow();
			Application.getInstance().setBackOfficeWindow(window);
		}
		window.setVisible(true);
		window.toFront();
	}

	private void doLogout() {
		BackOfficeWindow.getInstance().dispose();
		KitchenDisplay.instance.dispose();
		Application.getInstance().logout();
	}

	private void doSettleTicket() {
		try {
			Ticket ticket = null;

			List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

			if (selectedTickets.size() > 0) {
				ticket = selectedTickets.get(0);
			} else {
				int ticketId = NumberSelectionDialog2
						.takeIntInput("Geben Oder Scannen Sie die Bestellungsnummer ein");
				ticket = TicketService.getTicket(ticketId);
			}
			if (ticket != null) {
				new SettleTicketAction(ticket.getId()).execute();

				updateTicketList();
			}

		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doShowOrderInfo() {
		doShowOrderInfo(openTicketList.getSelectedTickets());
	}

	private void doShowOrderInfo(List<Ticket> tickets) {
		try {

			if (tickets.size() == 0) {
				POSMessageDialog.showMessage(POSConstants.SELECT_ONE_TICKET_TO_PRINT);
				return;
			}

			List<Ticket> ticketsToShow = new ArrayList<Ticket>();

			for (int i = 0; i < tickets.size(); i++) {
				Ticket ticket = tickets.get(i);
				ticketsToShow.add(TicketDAO.getInstance()
						.loadFullTicket(ticket.getId()));
			}

			OrderInfoView view = new OrderInfoView(ticketsToShow);
			OrderInfoDialog dialog = new OrderInfoDialog(view, false);
			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showBarCode() {
		BarCodeDialog dialog = new BarCodeDialog();
		dialog.setSize(800, 400);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(Application.getPosWindow());
		dialog.setVisible(true);

	}

	private void showCalculator() {
		try {
			Calculator dialog = new Calculator();
			dialog.pack();
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	public void showSalesReport() {
		try {
			Report report = new SalesReport();
			report.setType(1);
			ReportViewer viewer = new ReportViewer(report);
			ReportViewerDialog dialog = new ReportViewerDialog(viewer);
			viewer.setDialog(dialog);
			dialog.setSize(800, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showMonthSalesReport() {
		try {
			Report report = new MonthSalesReport();
			report.setType(1);
			MonthReportViewer viewer = new MonthReportViewer(report);
			MonthReportViewerDialog dialog = new MonthReportViewerDialog(viewer);
			viewer.setDialog(dialog);
			dialog.setSize(800, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showCustomerData() {
		CustomerExplorer explorer = new CustomerExplorer();
		CustomerDialog dialog = new CustomerDialog(explorer);
		dialog.pack();
		dialog.open();
	}

	private void showDriverReport() {
		try {
			Report report = new DriverReport();
			report.setType(2);
			ReportViewer viewer = new ReportViewer(report);
			ReportViewerDialog dialog = new ReportViewerDialog(viewer);
			dialog.setSize(800, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showServerReport() {
		try {
			Report report = new ServerReport();
			report.setType(3);
			ReportViewer viewer = new ReportViewer(report);
			ReportViewerDialog dialog = new ReportViewerDialog(viewer);
			viewer.setDialog(dialog);
			dialog.setSize(800, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void showReservation() {
		try {

			ReservationView viewer = new ReservationView();
			ReservationDialog dialog = new ReservationDialog(viewer);
			dialog.setSize(1180, 700);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doVoidTicket() {
		try {
			Ticket ticket = null;

			List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

			if (selectedTickets.size() > 0) {
				ticket = selectedTickets.get(0);
			} else {
				int ticketId=0;
				
				if(StringUtils.isNotEmpty(POSConstants.Geben_Oder_Scannen))
					 ticketId = NumberSelectionDialog2
					.takeIntInput(POSConstants.Geben_Oder_Scannen);
				else
				      ticketId = NumberSelectionDialog2
						.takeIntInput("Geben Oder Scannen Sie die Bestellungsnummer ein");
				ticket = TicketService.getTicket(ticketId);
			}

			if (ticket != null) {
				Ticket ticketToVoid = TicketDAO.getInstance().loadFullTicket(
						ticket.getId());
				VoidTicketDialog voidTicketDialog = new VoidTicketDialog(
						Application.getPosWindow(), true);
				voidTicketDialog.setTicket(ticketToVoid);
				voidTicketDialog.open();

				if (!voidTicketDialog.isCanceled()) {
					updateView(POSConstants.ALL_TICKETS);
				}
			}
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	private void doSplitTicket() {
		try {
			Ticket selectedTicket = getFirstSelectedTicket();

			if (selectedTicket == null) {
				return;
			}

			// if (selectedTicket.getTotalAmount() != selectedTicket.getDueAmount()) {
			// POSMessageDialog.showMessage(POSConstants.PARTIAL_PAID_VOID_ERROR);
			// return;
			// }

			// initialize the ticket.
			Ticket ticket = TicketDAO.getInstance().loadFullTicket(
					selectedTicket.getId());

			SplitTicketDialog dialog = new SplitTicketDialog();
			dialog.setTicket(ticket);
			dialog.open();
			updateTicketList();
		} catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}

	public void doEditTicket() {
		try {
			Ticket ticket = null;

			List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

			if (selectedTickets.size() > 0) {
				ticket = selectedTickets.get(0);
			} else {
				int ticketId=0;
				if(StringUtils.isNotEmpty(POSConstants.Geben_Oder_Scannen))
					 ticketId = NumberSelectionDialog2
					.takeIntInput(POSConstants.Geben_Oder_Scannen);
				else					
				   ticketId = NumberSelectionDialog2
						.takeIntInput("Geben Oder Scannen Sie die Bestellungsnummer ein");
				
				if (ticketId == 0)
					return;
				ticket = TicketService.getTicket(ticketId);
			}
			if (ticket != null)
				editTicket(ticket);
		} catch (PosException e) {
			POSMessageDialog.showError(this, e.getMessage());
		} catch (Exception e) {
			POSMessageDialog.showError(this, e.getMessage(), e);
		}
	}

	public void editTicket(Ticket ticket) {
		if (ticket.isPaid()) {
			ErrorMessageDialog messageDialog = new ErrorMessageDialog(
					"Moechten Sie die Zahlung aendern ?", true);
			messageDialog.pack();
			messageDialog.open();
			if (messageDialog.isCancelled())
				return;

			changePayment(ticket);
			return;
		}

		Ticket ticketToEdit = TicketDAO.getInstance()
				.loadFullTicket(ticket.getId());
		OrderView.getInstance().setCurrentTicket(ticketToEdit);			
		RootView.getInstance().showView(OrderView.VIEW_NAME);
		//if everyItem should update comment below
		if(OrderView.getInstance().getTicketView().isTseEnable())
			OrderView.getInstance().getTicketView().getPosTseController().setRestartReceipt(false);
	}

	protected void changePayment(Ticket ticket) {
		OfficialPaymentDialog dialog = new OfficialPaymentDialog();
		dialog.setUndecorated(true);
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return;
		}
		FiskalyKeyParameter param= new FiskalyKeyParameter();
		boolean isTse = TerminalConfig.isTseEnable();
		try {

			if(isTse&&ticket.getTseReceiptDataId()!=null&&ticket.getTseReceiptDataId()>0)
				param = OrderView.getInstance().getTicketView().getPosTseController().tseStartReceipt(Application.getInstance().getParam());
		}catch(Exception ex) {

		}
		FiskalyPaymentType type = FiskalyPaymentType.CASH;


		if (dialog.getOfficialPaymentType() != null) {
			if (dialog.getOfficialPaymentType().ordinal() == OfficialPaymentType.CASH
					.ordinal()) {
				ticket.setCashPayment(true);
				ticket.setGutscheinPayment(null);
				ticket.setCardpaymenttype(null);
				ticket.setOnlinePayment(null);
				ticket.setRechnugpaid(false);
				ticket.setRechnugPayemnt(false);
			} else if (dialog.getOfficialPaymentType().ordinal() == OfficialPaymentType.ONLINE
					.ordinal()) {
				ticket.setOnlinePayment(true);
				ticket.setGutscheinPayment(null);
				ticket.setCardpaymenttype(null);
				ticket.setCashPayment(null);
				ticket.setRechnugpaid(true);
				ticket.setRechnugPayemnt(true);
				type = FiskalyPaymentType.NON_CASH;
			} else if (dialog.getOfficialPaymentType().ordinal() == OfficialPaymentType.GUTSCHEIN.ordinal()) {
				ticket.setOnlinePayment(null);
				ticket.setCardpaymenttype(null);
				ticket.setCashPayment(null);
				ticket.setGutscheinPayment(true);
				type = FiskalyPaymentType.NON_CASH;
			
			} else {
				ticket.setCardpaymenttype(dialog.getOfficialPaymentType().ordinal());
				ticket.setCashPayment(false);
				ticket.setOnlinePayment(null);
				ticket.setGutscheinPayment(null);
				ticket.setRechnugpaid(false);
				ticket.setRechnugPayemnt(false);
				type = FiskalyPaymentType.NON_CASH;
			}
		} else {
			ticket.setCardpaymenttype(CardPaymentType.KARTE.ordinal());
			ticket.setCashPayment(false);
			ticket.setOnlinePayment(null);
			ticket.setRechnugpaid(false);
			ticket.setRechnugPayemnt(false);
			type = FiskalyPaymentType.NON_CASH;
		}

		if(isTse&&ticket.getTseReceiptDataId()!=null&&ticket.getTseReceiptDataId()>0) {
			try {
				TSEReceiptData receiptData1 = OrderView.getInstance().getTicketView().getPosTseController().tseFinishReceipt(param, ticket.getTicketItems(),type, FiskalyReceiptType.RECEIPT);
				if(ticket.getTseReceiptDataId()!=null)
					receiptData1.setId(ticket.getTseReceiptDataId());
				TSEReceiptDataDAO.getInstance().saveOrUpdate(receiptData1);
			}catch(Exception ex) {

			}
		}

		OrderController.saveOrder(ticket);
		return;

	}

	public void doCreateNewTicket(final TicketType ticketType) {
		try {
			OrderServiceExtension orderService = new DefaultOrderServiceExtension();
			orderService.createNewTicket(ticketType);
			//      if (orderService.getSelectedTable() != 0) {
			//        String tableSel = PosGuiUtil.getSelectedTable() + "";
			//        List<Ticket> ticketList = TicketDAO.getInstance().findOpenTickets();
			//        for (Iterator<Ticket> itr = ticketList.iterator(); itr.hasNext();) {
			//          Ticket ticket = itr.next();
			//          if (ticket.getType() == TicketType.DINE_IN && (!ticket.isPaid())) {
			//            if (ticket.getTableNumbers().compareTo(tableSel) == 0) {
			//              editTicket(ticket);
			//              break;
			//            }
			//          }
			//        }
			//      }
			List<Ticket> ticketList = TicketDAO.getInstance().findOpenTickets();
			boolean reOpen=false;
			if(ticketList!=null) {
				for (Iterator<Ticket> itr = ticketList.iterator(); itr.hasNext();) {
					Ticket ticket = itr.next();
					if (ticket.getType() == TicketType.DINE_IN && (!ticket.isPaid())) {
						if(Application.getCurrentUser().isAdministrator()&&!TerminalConfig.isMultiUser()||Application.getCurrentUser().getFirstName().compareTo(ticket.getOwner().getFirstName())==0) {
							reOpen = true;
							editTicket(ticket);
							break; 
						} else {
							OrderView.getInstance().getTicketView().setTseEnable(TerminalConfig.isTseEnable());
						}
					}
				} 
			}else
				OrderView.getInstance().getTicketView().setTseEnable(TerminalConfig.isTseEnable());
			if(!reOpen)
				OrderView.getInstance().getTicketView().setTseEnable(TerminalConfig.isTseEnable());

		} catch (TicketAlreadyExistsException e) {
			int option = JOptionPane.showOptionDialog(Application.getPosWindow(),
					POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
					null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
		OrderView.getInstance().getTicketView().focusToScanid();
	}

	protected void doHomeDelivery(TicketType ticketType) {
		try {

			orderServiceExtension.createNewTicket(ticketType);

		} catch (TicketAlreadyExistsException e) {

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(),
					POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
					null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
	}

	protected void doOnlineService(TicketType ticketType) {
		try {

			orderServiceExtension.createNewOnlineTicket(ticketType);

		} catch (TicketAlreadyExistsException e) {

			int option = JOptionPane.showOptionDialog(Application.getPosWindow(),
					POSConstants.EDIT_TICKET_CONFIRMATION, POSConstants.CONFIRM,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
					null);
			if (option == JOptionPane.YES_OPTION) {
				editTicket(e.getTicket());
				return;
			}
		}
	}

	private void doTakeout(TicketType titcketType) {
		Application application = Application.getInstance();

		Ticket ticket = new Ticket();

		ticket.setPriceIncludesTax(application.isPriceIncludesTax());
		ticket.setType(titcketType);
		ticket.setTerminal(application.getTerminal());
		ticket.setOwner(Application.getCurrentUser());
		ticket.setShift(application.getCurrentShift());

		Calendar currentTime = Calendar.getInstance();
		ticket.setCreateDate(currentTime.getTime());
		ticket.setCreationHour(currentTime.get(Calendar.HOUR_OF_DAY));

		OrderView.getInstance().setCurrentTicket(ticket);
		RootView.getInstance().showView(OrderView.VIEW_NAME);
	}

	private void doPayout() {
		PayoutDialog dialog = new PayoutDialog(Application.getPosWindow(), true);
		dialog.open();
	}

	private void doShowManagerWindow() {
		ManagerDialog dialog = new ManagerDialog();
		dialog.open();

		updateTicketList();
	}

	private void doGroupSettle() {
		List<Ticket> selectedTickets = openTicketList.getSelectedTickets();
		if (selectedTickets == null) {
			return;
		}

		for (int i = 0; i < selectedTickets.size(); i++) {
			Ticket ticket = selectedTickets.get(i);

			Ticket fullTicket = TicketDAO.getInstance()
					.loadFullTicket(ticket.getId());

			SettleTicketDialog posDialog = new SettleTicketDialog();
			posDialog.setTicket(fullTicket);
			posDialog.setSize(800, 700);
			posDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			posDialog.open();
		}

		updateTicketList();
	}

	public void updateView(String type) {
		updateButtons();
		if (type == POSConstants.DINE_IN_OPEN)
			updateTicketList();
		else
			updateTicketList(type);
	}

	public void updateButtons() {
		User user = Application.getCurrentUser();
		UserType userType = user.getType();
		if (userType != null) {
			Set<UserPermission> permissions = userType.getPermissions();
			if (permissions != null) {
				btnNewTicket.setEnabled(false);
				btnBackOffice.setEnabled(false);
				btnEditTicket.setEnabled(false);
				btnGroupSettle.setEnabled(false);
				btnSalesReport.setEnabled(false);
				btnMonthSalesReport.setEnabled(false);
				btnDriverReport.setEnabled(false);
				btnServerReport.setEnabled(false);
				btnReservation.setEnabled(false);
				btnPayout.setEnabled(false);
				btnReopenTicket.setEnabled(false);
				btnSettleTicket.setEnabled(false);
				btnSplitTicket.setEnabled(false);
				btnVoidTicket.setEnabled(false);
				btnCashBook.setEnabled(false);
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PAY_OUT)) {
						btnPayout.setEnabled(true);
					} else if (permission.equals(UserPermission.SETTLE_TICKET)) {
						btnSettleTicket.setEnabled(true);
						btnGroupSettle.setEnabled(true);
					} else if (permission.equals(UserPermission.PERFORM_MANAGER_TASK)) {
						btnSalesReport.setEnabled(true);
						btnReopenTicket.setEnabled(true);
						btnVoidTicket.setEnabled(true);
					} else if (permission.equals(UserPermission.SPLIT_TICKET)) {
						btnSplitTicket.setEnabled(true);
					} else if (permission.equals(UserPermission.VIEW_BACK_OFFICE)) {
						btnReservation.setEnabled(true);
					} else if (permission.equals(UserPermission.PAY_OUT)) {
						btnPayout.setEnabled(true);
					} else if (permission.equals(UserPermission.EDIT_TICKET)) {
						btnEditTicket.setEnabled(true);
					} else if (permission.equals(UserPermission.CREATE_TICKET)) {
						btnNewTicket.setEnabled(true);
					} else if (permission
							.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						btnReopenTicket.setEnabled(true);
						btnVoidTicket.setEnabled(true);
						btnCashBook.setEnabled(true);
						btnSalesReport.setEnabled(true);
						btnMonthSalesReport.setEnabled(true);
						btnDriverReport.setEnabled(true);
						btnServerReport.setEnabled(true);
						btnBackOffice.setEnabled(true);
					}
				}
			}
		}
	}

	public void updateTicketList(int type, String name) {
		User user = Application.getCurrentUser();

		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> openTickets = new ArrayList();

		if (user.getType() != null) {
			Set<UserPermission> permissions = user.getType().getPermissions();
			if (permissions != null) {
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						break;
					}
				}
			}
		}
		List<Ticket> temp = dao.findTicketsByDriver(name);
		Double totalAmount = 0.00;
		for (Iterator itr = temp.iterator(); itr.hasNext();) {
			Ticket ticket = (Ticket) itr.next();
			if (ticket.getAssignedDriver() != null
					&& ticket.getAssignedDriver().getFirstName() != null) {
				if ((ticket.getAssignedDriver().getFirstName().compareTo(name) == 0)) {
					if ((ticket.getCashPayment() == null)
							|| (ticket.getCashPayment() == false)) {
						openTickets.add(ticket);
					} else {
						openTickets.add(ticket);
						totalAmount += ticket.getTotalAmount();
					}
				}
			}
		}
		tbTotal.setText(NumberUtil.formatNumber(totalAmount) + " €");
		openTicketList.setTickets(openTickets);
	}
	
	JXDatePicker dpDateTransfer;			
	
	public void updateTicketListDate(Date myDate) {
		User user = Application.getCurrentUser();
		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> openTickets = new ArrayList();

		if (user.getType() != null) {
			Set<UserPermission> permissions = user.getType().getPermissions();
			if (permissions != null) {
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						break;
					}
				}
			}
		}
		List<Ticket> tempList = dao.findTicketsToday(BusinessDateUtil.startOfOfficialDay(myDate), BusinessDateUtil.endOfOfficialDay(myDate));
		Double totalAmount = 0.00;
		for (Iterator itr = tempList.iterator(); itr.hasNext();) {
			Ticket ticket = (Ticket) itr.next();
			openTickets.add(ticket);
			totalAmount += ticket.getTotalAmount();
		}
		tbTotal.setText(NumberUtil.formatNumber(totalAmount) + " €");
		openTicketList.setTickets(openTickets);
	}
	
	

	public void updateTicketList(String type) {
		restaurant = RestaurantDAO.getRestaurant();
		User user = Application.getCurrentUser();

		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> openTickets = null;
		boolean showAllOpenTicket = false;
		if (user.getType() != null) {
			Set<UserPermission> permissions = user.getType().getPermissions();
			if (permissions != null) {
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						showAllOpenTicket = true;
						break;
					}
				}
			}
		}
		updateButtonText(dao);
		if (type == POSConstants.HOME_OPEN) {
			openTickets = dao.findOpenHomeTickets();
			Double totalAmount = 0.00;
			for (Iterator<Ticket> itr = openTickets.iterator(); itr.hasNext();) {
				Ticket ticket = itr.next();
				totalAmount += ticket.getTotalAmount();
			}
			tbTotal.setText(NumberUtil.formatNumber(totalAmount) + " €");
			openTicketList.setTickets(openTickets);

		} else if (type == POSConstants.HOME_CLOSED) {
			openTickets = dao.findClosedHomeTickets();
			Double totalAmount = 0.00;
			for (Iterator<Ticket> itr = openTickets.iterator(); itr.hasNext();) {
				Ticket ticket = itr.next();
				totalAmount += ticket.getTotalAmount();
			}
			tbTotal.setText(NumberUtil.formatNumber(totalAmount) + " €");
			openTicketList.setTickets(openTickets);

		} else if (type == POSConstants.ALL_TICKETS) {
			if (showAllOpenTicket) {
				if (TerminalConfig.isAllTickets()) {
					openTickets = dao.findAllCurrentTickets();
				} else {
					Date startDate = BusinessDateUtil.startOfOfficialDay(restaurant.getStartToday()!=null?restaurant.getStartToday():new Date());
					Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());
					openTickets = dao.findAllCurrentTicketsDate(startDate, endDate);
				}
			} else {
				openTickets = dao.findAllCurrentTickets(Application.getCurrentUser());
			}
			Double totalAmount = 0.0;
			for (Iterator<Ticket> itr = openTickets.iterator(); itr.hasNext();) {
				Ticket ticket = itr.next();
				totalAmount += ticket.getTotalAmount();
			}
			tbTotal.setText(NumberUtil.formatNumber(totalAmount) + " €");
			openTicketList.setTickets(openTickets);

		}

	}

	public void updateButtonText(TicketDAO dao) {
		User user = Application.getCurrentUser();

		List<Ticket> openTickets = null;
		boolean showAllOpenTicket = false;
		if (user.getType() != null) {
			Set<UserPermission> permissions = user.getType().getPermissions();
			if (permissions != null) {
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						showAllOpenTicket = true;
						break;
					}
				}
			}
		}
		int allTickets;
		if (showAllOpenTicket) {
			if (TerminalConfig.isAllTickets()) {
				allTickets = dao.findAllCurrentTickets().size();
			} else {
				Date startDate = BusinessDateUtil.startOfOfficialDay(restaurant.getStartToday()!=null?restaurant.getStartToday():new Date());
				Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());
				allTickets = dao.findAllCurrentTicketsDate(startDate, endDate).size();
			}
		}
		else
			allTickets = dao.findAllCurrentTickets(Application.getCurrentUser())
			.size();

		if(StringUtils.isNotEmpty(POSConstants.ALL_ORDERS))
			btnAllTickets.setText(POSConstants.ALL_ORDERS +"(" + allTickets + ")");
		else
			btnAllTickets.setText("ALLE BESTELLUNGEN (" + allTickets + ")");
	}

	@Override
	public void updateTicketList() {
		User user = Application.getCurrentUser();

		TicketDAO dao = TicketDAO.getInstance();
		List<Ticket> openTickets = null;

		boolean showAllOpenTicket = false;
		if (user.getType() != null) {
			Set<UserPermission> permissions = user.getType().getPermissions();
			if (permissions != null) {
				for (UserPermission permission : permissions) {
					if (permission.equals(UserPermission.PERFORM_ADMINISTRATIVE_TASK)) {
						showAllOpenTicket = true;
						break;
					}
				}
			}
		}
		updateButtonText(dao);
		if (showAllOpenTicket) {
			openTickets = dao.findOpenTickets();
			Double totalAmount = 0.00;
			for (Iterator<Ticket> itr = openTickets.iterator(); itr.hasNext();) {
				Ticket ticket = itr.next();

				totalAmount += ticket.getTotalAmount();
			}
			tbTotal.setText(NumberUtil.formatNumber(totalAmount) + " €");
		} else {
			openTickets = dao.findOpenTicketsForUser(user);
		}
		openTicketList.setTickets(openTickets);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private com.floreantpos.swing.PosButton btnBackOffice;
	private com.floreantpos.swing.PosButton btnEditTicket;
	private com.floreantpos.swing.PosButton btnGroupSettle;
	private com.floreantpos.swing.PosButton btnLogout;
	private com.floreantpos.swing.PosButton btnSalesReport;
	private com.floreantpos.swing.PosButton btnMonthSalesReport;
	private com.floreantpos.swing.PosButton btnCalculator;
	private com.floreantpos.swing.PosButton btnDriverReport;
	private com.floreantpos.swing.PosButton btnServerReport;
	private com.floreantpos.swing.PosButton btnCustomer;
	private com.floreantpos.swing.PosButton btnPayroll;
	private com.floreantpos.swing.PosButton btnReservation;
	private com.floreantpos.swing.PosButton btnAuthorize;
	private com.floreantpos.swing.PosButton btnKitchenDisplay;
	private com.floreantpos.swing.PosButton btnNewTicket;
	private com.floreantpos.swing.PosButton btnPayout;
	private com.floreantpos.swing.PosButton btnOrderInfo;
	private com.floreantpos.swing.PosButton btnReopenTicket;
	private com.floreantpos.swing.PosButton btnShowOldTicket;
	private com.floreantpos.swing.PosButton btnSettleTicket;
	private com.floreantpos.swing.PosButton btnShutdown;
	private com.floreantpos.swing.PosButton btnSplitTicket;
	private com.floreantpos.swing.PosButton btnVoidTicket;
	private com.floreantpos.swing.PosButton btnCashBook;
	private com.floreantpos.swing.PosButton btnBarTab;
	private javax.swing.JLabel lblUserName;
	public com.floreantpos.swing.PosButton btnAllTickets;
	private com.floreantpos.ui.TicketListView openTicketList;
	private PosButton btnBarcode;
	private PosButton btnCloseOrder;
	private JComboBox cbDrivers;
	private JTextField tbTotal;
	private JLabel timerLabel = new JLabel();

	// End of variables declaration//GEN-END:variables

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);

		if (aFlag) {
			logoffHandler.reset();
			if (TerminalConfig.isAutoLogoffEnable()) {
				autoLogoffTimer.start();

			}
		} else {
			autoLogoffTimer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == btnBackOffice) {
			doShowBackoffice();
		} else if (source == btnEditTicket) {
			doEditTicket();
		} else if (source == btnGroupSettle) {
			doGroupSettle();
		} else if (source == btnLogout) {
			doLogout();
		} else if (source == btnSalesReport) {
			showSalesReport();
		} else if (source == btnMonthSalesReport) {
			showMonthSalesReport();
		} else if (source == btnCalculator) {
			showCalculator();
		} else if (source == btnDriverReport) {
			showDriverReport();
		} else if (source == btnServerReport) {
			showServerReport();
		} else if (source == btnCustomer) {
			showCustomerData();
		} else if (source == btnPayroll) {
			paperOrder();
		} else if (source == btnReservation) {
			showReservation();
		} else if (source == btnNewTicket) {
			doCreateNewTicket(TicketType.DINE_IN);
		} else if (source == btnPayout) {
			doPayout();
		} else if (source == btnOrderInfo) {
			doShowOrderInfo();
		} else if (source == btnReopenTicket) {
			doReopenTicket();
		} else if (source == btnShowOldTicket) {
			showOldTicket();
		} else if (source == btnSettleTicket) {
			doSettleTicket();
		} else if (source == btnSplitTicket) {
			doSplitTicket();
		} else if (source == btnVoidTicket) {
			doVoidTicket();
		} else if (source == btnCashBook) {
			showCashBook();
		}

	}

	public void showCashBook() {
		CashBookExplorer explorer = new CashBookExplorer();
		explorer.pack();
		explorer.open();

		if (explorer.isCanceled())
			return;
	}

	public Ticket getFirstSelectedTicket() {
		List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			
			if(StringUtils.isNotEmpty(POSConstants.Bestellung_aus))
				POSMessageDialog.showMessage(POSConstants.Bestellung_aus);
			else
				POSMessageDialog.showMessage("Bitte Waehlen Sie eine Bestellung aus");
			
			return null;
		}
		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	@Override
	public Ticket getSelectedTicket() {
		List<Ticket> selectedTickets = openTicketList.getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	// private class TicketListUpdaterTask implements ActionListener {
	//
	// public void actionPerformed(ActionEvent e) {
	// updateTicketList();
	// }
	//
	// }

	private class AutoLogoffHandler implements ActionListener {
		int countDown = TerminalConfig.getAutoLogoffTime();

		@Override
		public void actionPerformed(ActionEvent e) {
			if (PosGuiUtil.isModalDialogShowing()) {
				reset();
				return;
			}

			--countDown;
			int min = countDown / 60;
			int sec = countDown % 60;

			if (min < 1) {
				timerLabel.setForeground(Color.RED);
			} else {
				timerLabel.setForeground(new Color(0, 153, 76));
			}
			String seconds = sec + "";
			if (seconds.length() == 1)
				seconds = 0 + seconds;

			timerLabel.setText("<html><body><h2> " + min + ":" + seconds
					+ "</h2></body></html>");
			if (countDown == 0) {
				doLogout();
			}
		}

		public void reset() {
			timerLabel.setText("");
			countDown = TerminalConfig.getAutoLogoffTime();
		}

	}

	public void paperOrder() {
		PaperBuyDialog dialog = new PaperBuyDialog();
		dialog.setPreferredSize(new Dimension(800, 600));
		dialog.pack();
		dialog.open();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
