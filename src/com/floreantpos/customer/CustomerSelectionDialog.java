package com.floreantpos.customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosSmallButton;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.DeliveryHistoryDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.CustomerForm;
import com.floreantpos.ui.views.order.OrderController;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.TicketView;
import com.floreantpos.util.NumberUtil;
import com.jidesoft.dialog.JideOptionPane;

public class CustomerSelectionDialog extends POSDialog{

	private PosSmallButton btnCreateNewCustomer;

	private CustomerTable customerTable;
	private POSTextField tfPhone;
	private POSTextField tfLoyaltyNo;
	private POSTextField tfName;
	private POSTextField tfZip;
	private POSTextField tfCity;

	private POSTextField tfAddress;
	private POSTextField tfBellName;
	private POSTextField tfFirmName;
	private PosSmallButton btnSelet;
	private JComboBox cmbSaluation;
	JComboBox<String> cbType;
	List<Customer> allList;

	protected Customer selectedCustomer;
	private PosSmallButton btnRemoveCustomer;

	private Ticket ticket;
	public CustomerSelectionDialog(Ticket ticket) {
		this.ticket = ticket;
		setTitle("Kundendaten");
		setBackground(new Color(5,29,53));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(screenSize);
		allList = CustomerDAO.getInstance().findAll();
		try {
			loadCustomerFromTicket();
		}catch(Exception ex) {

		}

	}
	@Override 
	public void initUI() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {

				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
						return false;
					}
				}
				return false;
			}});
		setPreferredSize(new Dimension(690, 553));
		getContentPane().setLayout(new MigLayout("insets 0", "[grow]", ""));


		cmbSaluation = new JComboBox<>();
		cmbSaluation.addItem("");
		cmbSaluation.addItem("An");	
		cmbSaluation.addItem("Herr");
		cmbSaluation.addItem("Frau");	
		cmbSaluation.addItem("To");
		cmbSaluation.addItem("Mr.");
		cmbSaluation.addItem("Miss");
		cmbSaluation.addItem("Mrs.");

		cbType = new javax.swing.JComboBox();
		cbType.setFont(new Font(null, Font.PLAIN, 18));
		cbType.setName("Price Category");
		cbType.setBackground(Color.WHITE);  
		for (int i=1; i<20;i++) {
			try {
				cbType.addItem(String.valueOf(i));
			}catch(Exception ex) {

			}


		}
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(5,29,53));
		mainPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		getContentPane().add(mainPanel, "cell 0 0,grow");
		mainPanel.setLayout(new MigLayout());
		JLabel lblAnrede = new JLabel("Anrede");
		lblAnrede.setForeground(Color.WHITE);
		JLabel lblPriceList = new JLabel("Price Category ");
		lblPriceList.setForeground(Color.WHITE);


		btnSelet = new PosSmallButton();
		//panel.add(btnSelet);
		btnSelet.setText("   OK   ");
		btnSelet.setBackground(new Color(50, 168, 82));
		btnSelet.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Customer customer = null;
				try {
					customer = customerTable.getSelectedCustomer();
					doSetCustomer(customer);
				}catch(Exception ex) {

				}

			}
		});

		if(TerminalConfig.isPriceCategory()) {
			mainPanel.add(lblAnrede,"cell 0 0 1 0, growx");
			mainPanel.add(cmbSaluation,"cell 0 0 2 0, growx");
			mainPanel.add(lblPriceList,"cell 1 0");
			mainPanel.add(cbType,"cell 2 0, growx");
			mainPanel.add(new JLabel("       "),"cell 3 0, growx");
			mainPanel.add(btnSelet, "cell 4 0 2 0, growx");
		}else {
			mainPanel.add(lblAnrede,"cell 0 0, growx");
			mainPanel.add(cmbSaluation,"cell 1 0 2 0");
			mainPanel.add(btnSelet,"cell 3 0 ");
		}

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(5,29,53));
		panel_4.setBorder(new TitledBorder(null, "", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		getContentPane().add(panel_4, "cell 0 1,grow");
		panel_4.setLayout(new MigLayout("", "[][grow][][grow]", "[grow][][][]"));
		JLabel lblByCustomer = new JLabel("Kunden-Nr");
		lblByCustomer.setForeground(Color.WHITE);
		panel_4.add(lblByCustomer, "cell 0 3,alignx trailing");

		tfLoyaltyNo = new POSTextField();
		panel_4.add(tfLoyaltyNo, "cell 1 3, growx");
		tfLoyaltyNo.setColumns(16);
		tfLoyaltyNo.setText("");

		JLabel lblByName = new JLabel("Name");
		lblByName.setForeground(Color.WHITE);
		panel_4.add(lblByName, "cell 2 0,alignx trailing");

		tfName = new POSTextField();
		panel_4.add(tfName, "cell 3 0, growx");
		tfName.setColumns(16);

		JLabel lblByAddress = new JLabel("Str.");
		lblByAddress.setForeground(Color.WHITE);
		panel_4.add(lblByAddress, "cell 0 1,alignx trailing");

		tfAddress = new POSTextField();
		panel_4.add(tfAddress, "cell 1 1, growx");
		tfAddress.setColumns(16);

		tfAddress.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				String text = tfAddress.getText();
				if (text.length() == 1) {
					text = text.toUpperCase();
					tfAddress.setText(text);
				} 

			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});



		JLabel lblByBellName = new JLabel("Haus Nr.");
		lblByBellName.setForeground(Color.WHITE);
		panel_4.add(lblByBellName, "cell 2 1,alignx trailing");

		tfBellName = new POSTextField();
		panel_4.add(tfBellName, "cell 3 1, growx");
		tfBellName.setColumns(16);



		JLabel lblByZipCode = new JLabel("PLZ");
		lblByZipCode.setForeground(Color.WHITE);
		panel_4.add(lblByZipCode, "cell 0 2,alignx trailing");

		tfZip = new POSTextField();
		panel_4.add(tfZip, "cell 1 2, growx");
		tfZip.setColumns(16);		

		JLabel lblStadt = new JLabel("Stadt: ");
		lblStadt.setForeground(Color.WHITE);
		panel_4.add(lblStadt, "cell 2 2,alignx trailing");

		tfCity = new POSTextField();
		panel_4.add(tfCity, "cell 3 2, growx");
		tfCity.setColumns(16);

		tfCity.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {

				String text = tfCity.getText();
				if (text.length() == 1) {
					text = text.toUpperCase();
					tfCity.setText(text);
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doSearchCustomer();
					clearAllContents();
				}
				else
				{
					doSearchCustomer();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
//		tfCity.getDocument().addDocumentListener(new DocumentListener() {
//			@Override
//			public void changedUpdate(DocumentEvent arg0) {
//				doSearchCustomer();       
//			}
//			@Override
//			public void insertUpdate(DocumentEvent e) {
//				doSearchCustomer();       
//			}
//			@Override
//			public void removeUpdate(DocumentEvent e) {
//				doSearchCustomer();         
//			}
//		});


		JLabel lblFirmName = new JLabel("Firma");
		lblFirmName.setForeground(Color.WHITE);
		panel_4.add(lblFirmName, "cell 0 0,alignx trailing");

		tfFirmName = new POSTextField();
		panel_4.add(tfFirmName, "cell 1 0, growx");
		tfFirmName.setColumns(16);

		tfFirmName.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e){}
			@Override
			public void keyReleased(KeyEvent e) {
				String text = tfFirmName.getText();
				if (text.length() == 1) {
					text = text.toUpperCase();
					tfFirmName.setText(text);
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					doSearchCustomer();
					clearAllContents();
				}
				else
				{
					doSearchCustomer();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});


		JLabel lblByPhone = new JLabel("Telefon");
		lblByPhone.setForeground(Color.WHITE);
		panel_4.add(lblByPhone, "cell 2 3,alignx trailing");

		tfPhone = new POSTextField();
		panel_4.add(tfPhone, "cell 3 3, growx");
		tfPhone.setColumns(16);
//		tfPhone.addKeyListener(new KeyListener() {
//
//			public void keyTyped(KeyEvent e){}
//			@Override
//			public void keyReleased(KeyEvent e) {
//				if(e.getKeyCode() == KeyEvent.VK_ENTER)
//				{
//					doSearchCustomer();
//					clearAllContents();
//				}
//				else
//				{
//					doSearchCustomer();
//				}
//			}
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//			}
//		});

//		tfPhone.getDocument().addDocumentListener(new DocumentListener() {
//			@Override
//			public void changedUpdate(DocumentEvent arg0) {
//				doSearchCustomer();       
//			}
//			@Override
//			public void insertUpdate(DocumentEvent e) {
//				doSearchCustomer();       
//			}
//			@Override
//			public void removeUpdate(DocumentEvent e) {
//				doSearchCustomer();         
//			}
//		});



		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel_4.add(panel_2, "cell 0 4 4 4,growx");
		panel_2.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFocusable(false);
		panel_2.add(scrollPane, BorderLayout.CENTER);

		customerTable = new CustomerTable();
		customerTable.setModel(new CustomerListTableModel());
		customerTable.setFocusable(false);
		customerTable.setRowHeight(35);

		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

		customerTable.getColumnModel().getColumn(0).setCellRenderer(otherRenderer);
		customerTable.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		customerTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		customerTable.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
		customerTable.getColumnModel().getColumn(4).setCellRenderer(otherRenderer);


		customerTable.getColumnModel().getColumn(0).setMinWidth(80);
		customerTable.getColumnModel().getColumn(1).setMinWidth(100);
		customerTable.getColumnModel().getColumn(2).setMinWidth(100);
		customerTable.getColumnModel().getColumn(3).setMinWidth(300);
		customerTable.getColumnModel().getColumn(4).setMinWidth(80);

		customerTable.setBackground(Color.BLACK);
		customerTable.setForeground(Color.WHITE);
		customerTable.setFocusable(false);
		customerTable.setSelectionBackground(Color.GRAY);
		customerTable.getTableHeader().setBackground(Color.BLACK);
		customerTable.getTableHeader().setForeground(Color.WHITE);
		customerTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		customerTable.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {

				try
				{	
					if (me.getClickCount() == 2) {
						Customer customer = null;
						try {
							customer = customerTable.getSelectedCustomer();
							doSetCustomer(customer);
						}catch(Exception ex) {

						}
					}


				}catch(Exception ex) {

				}
			}
		});
		scrollPane.setViewportView(customerTable);       

		JPanel panel = new JPanel();
		panel.setBackground(new Color(5,29,53));
		panel_2.add(panel, BorderLayout.SOUTH);
		panel_2.setBackground(new Color(5,29,53));

		PosSmallButton btnHistory = new PosSmallButton();
		btnHistory.setText("HISTORIE");
		
		btnHistory.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if (ticket != null) {
					Customer customer = customerTable.getSelectedCustomer();
									
					if(customer!=null) {
						List<Ticket> list = TicketDAO.getInstance().findTktByCustomer(customer);
						if(list!=null&&list.size()>0) {
							DeliveryHistoryDialog dialog = null;
							Collections.reverse(list);
							try {
								dialog = new DeliveryHistoryDialog(list, "Bestellung History");
							} catch (Exception ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
							dialog.pack();
			    			dialog.open();
						}
						
					} else {
						JideOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Bitte waehlen sie Kunden ein!!!");
					}					
				}
			}
		});
		
		//if(TerminalConfig.isWholeSale())
			panel.add(btnHistory);

		btnCreateNewCustomer = new PosSmallButton();
		btnCreateNewCustomer.setFocusable(false);
		//		panel.add(btnCreateNewCustomer);
		btnCreateNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					doCreateNewCustomer();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCreateNewCustomer.setText(POSConstants.NEW);

		btnRemoveCustomer = new PosSmallButton();
		btnRemoveCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doRemoveCustomerFromTicket();
			}
		});
		btnRemoveCustomer.setText(POSConstants.REMOVE);
		btnRemoveCustomer.setBackground(new Color(255,153,153));


		PosSmallButton btnSelect = new PosSmallButton();
		btnSelect.setText(POSConstants.SAVE);
		btnSelect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Customer customer = null;				
				String name = tfName.getText();
				String firmName = tfFirmName.getText();
				String loyalty = tfLoyaltyNo.getText();
				//
				//				if(customer!=null&&customer.getName().compareTo(name)==0||customer!=null&&customer.getLoyaltyNo().compareTo(loyalty)==0||customer!=null&&customer.getFirmName().compareTo(firmName)==0) {      
				//					doSetCustomer(customer);				
				//				}else{
				String phone = tfPhone.getText();
				String zipCode = tfZip.getText();
				String Address = tfAddress.getText();
				String bellName = tfBellName.getText();
				String cityName = tfCity.getText();
				if(StringUtils.isEmpty(loyalty)) {
					JOptionPane.showMessageDialog(null, "Kunden nr. Muss sein!");
					return;
				}
				customer = new Customer();
				customer.setTelephoneNo(phone);
				customer.setName(name);
				customer.setLoyaltyNo(loyalty);
				customer.setZipCode(zipCode);
				customer.setAddress(Address);
				customer.setDoorNo(bellName);
				customer.setFirmName(firmName);
				customer.setCity(cityName);
				customer.setSalutation(cmbSaluation.getSelectedItem().toString());
				if(TerminalConfig.isPriceCategory())
					customer.setPriceCategory(Integer.parseInt(cbType.getSelectedItem().toString()));
				else
					customer.setPriceCategory(1);
				int option = JOptionPane.showOptionDialog(Application.getPosWindow(), POSConstants.SAVE_CUSTOMER, "Wollen Sie Speichern?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);
				if(option == JOptionPane.YES_OPTION) {
					try {
						CustomerDAO.getInstance().saveOrUpdate(customer);
						TerminalConfig.setLoyaltyNo(String.valueOf(Integer.parseInt(loyalty)+1));
					}catch(Exception ex) {
						ex.printStackTrace();
					}
				}
				doSetCustomer(customer);



			}
		});
		btnSelect.setBackground(new Color(102,255,102));
		panel.add(btnSelect);
		panel.add(new JLabel("       "));

		PosSmallButton btnEdit = new PosSmallButton();
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					Customer customer = customerTable.getSelectedCustomer();
					if(customer == null) {						
						return;
					}
					doEditCustomer(customer, customerTable.getSelectedRow());
				}
				catch(Exception ex){}
			}
		});
		btnEdit.setText("BEARBEITEN");
		btnEdit.setBackground(new Color(102,255,102));
		//		panel.add(btnCreateNewCustomer);
		panel.add(btnEdit);

		PosSmallButton btnCancel = new PosSmallButton();
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});

		panel.add(btnRemoveCustomer);
		btnCancel.setText("ABBRECHEN");
		panel.add(btnCancel);

		JPanel panel_3 = new JPanel(new BorderLayout());
		getContentPane().add(panel_3, "cell 0 2,grow, gapright 2px");
		getContentPane().setBackground(new Color(5,29,53));
		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		panel_3.add(qwertyKeyPad);
		panel_3.setBackground(new Color(5,29,53));

		tfLoyaltyNo.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				doSearchCustomer();       
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				doSearchCustomer();       
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				doSearchCustomer();         
			}
		});

		tfName.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				String text = tfName.getText();
				if (text.length() == 1) {
					text = text.toUpperCase();
					tfName.setText(text);
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		tfName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				doSearchCustomer();       
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				doSearchCustomer();       
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				doSearchCustomer();         
			}
		});

		tfFirmName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {				
				doSearchCustomer();       
			}
			@Override
			public void insertUpdate(DocumentEvent e) {			
				doSearchCustomer();       
			}
			@Override
			public void removeUpdate(DocumentEvent e) {			
				doSearchCustomer();         
			}
		});
		tfCity.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {				
				doSearchCustomer();       
			}
			@Override
			public void insertUpdate(DocumentEvent e) {			
				doSearchCustomer();       
			}
			@Override
			public void removeUpdate(DocumentEvent e) {			
				doSearchCustomer();         
			}
		});

	}
	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
		Font font = new Font("Times New Roman", Font.BOLD, 16);

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			//			setFont(font);
			this.setHorizontalAlignment(SwingConstants.CENTER);
			return this;
		}

	};

	private void loadCustomerFromTicket() {
		String customerIdString = ticket.getProperty(Ticket.CUSTOMER_ID);
		if(StringUtils.isNotEmpty(customerIdString)) {
			int customerId = Integer.parseInt(customerIdString);
			Customer customer = CustomerDAO.getInstance().get(customerId);

			List<Customer> list = new ArrayList<Customer>();
			list.add(customer);
			customerTable.setModel(new CustomerListTableModel(list));
		} else {
			doSearchCustomer();
		}
	}

	protected void doSetCustomer(Customer customer) {
		ticket.setCustomer(customer);
		TicketView ticketView = OrderView.getInstance().getTicketView();
		//    ticket.setKunden(customer);
		if(TerminalConfig.isPriceCategoryKunden()) {
			ticketView.setPriceCategory(Integer.parseInt(customer.getLoyaltyNo()));
		}else if(TerminalConfig.isPriceCategory()) {
			int pricCategory = customer.getPriceCategory();
			if(pricCategory==0)
				ticketView.setPriceCategory(1);
			else
			ticketView.setPriceCategory(pricCategory);
		}
		ticketView.updateView();
		//		OrderController.saveOrder(ticket);
		setCanceled(false);
		dispose();

	}

	protected void doRemoveCustomerFromTicket() {
		int option = JOptionPane.showOptionDialog(this, "Remove customer from ticket?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(option != JOptionPane.YES_OPTION) {
			return;
		}

		ticket.removeCustomer();
		OrderController.saveOrder(ticket);
		setCanceled(false);
		dispose();
	}

	protected void doSearchCustomer() {
		String phone = tfPhone.getText();
		String name = tfName.getText();
		String loyalty = tfLoyaltyNo.getText();
		String zipCode = tfZip.getText();
		String Address = tfAddress.getText();
		String bellName = tfBellName.getText();
		String firmName = tfFirmName.getText();
		String cityName = tfCity.getText();
		if (StringUtils.isEmpty(phone) && StringUtils.isEmpty(loyalty) && StringUtils.isEmpty(name) && StringUtils.isEmpty(zipCode) && StringUtils.isEmpty(Address)&& StringUtils.isEmpty(bellName) && StringUtils.isEmpty(firmName)) {
			Collections.sort(allList, new Customer.customerNameComparator());
			customerTable.setModel(new CustomerListTableModel(allList));
			if(allList.size() > 0)
			{
				customerTable.setRowSelectionInterval(0, 0);
			}
			return;
		}

		//		List<Customer> list = CustomerDAO.getInstance().findBy(phone, loyalty, name,zipCode,Address,cityName,firmName,phone);
		List<Customer> list = getFilterd(loyalty, name, firmName, cityName);
		try {
		Collections.sort(list, new Customer.customerNameComparator());
		}catch(Exception ex) {
			
		}
		customerTable.setModel(new CustomerListTableModel(list));
		if(list.size() > 0)
		{
			customerTable.setRowSelectionInterval(0, 0);
		}
	}


	private List<Customer> getFilterd(String loyalty, String name,String firmName,String cityName) {
		List<Customer> list = new ArrayList(); 
		if(!StringUtils.isEmpty(loyalty)){
			char [] text = loyalty.toCharArray();
			text[0] = Character.toUpperCase(text[0]); 
			loyalty = new String(text);

			int index = 0;
			if(loyalty.indexOf(" ") != -1)
			{
				index = loyalty.indexOf(' ');
				text = loyalty.toCharArray();
				try{
					text[index+1] = Character.toUpperCase(text[index+1]);
					loyalty = new String(text);
				}
				catch(Exception ex){}
			}
			for(Iterator<Customer> itr = allList.iterator();itr.hasNext();)
			{
				Customer customer = itr.next();
				try {
					if(customer.getLoyaltyNo().contains(loyalty))
					{
						list.add(customer);
					}
				}catch(Exception ex) {}

			}
		}
		if(!StringUtils.isEmpty(name)){
			name = name.toLowerCase();
			for(Iterator<Customer> itr = allList.iterator();itr.hasNext();)
			{
				Customer customer = itr.next();
				try {
					if(customer.getName().toLowerCase().contains(name))
					{
						list.add(customer);
					}
				}catch(Exception ex) {}

			}
		}

		if(!StringUtils.isEmpty(firmName)){
			firmName = firmName.toLowerCase();
			for(Iterator<Customer> itr = allList.iterator();itr.hasNext();)
			{
				Customer customer = itr.next();
				try {
					if(customer.getFirmName().toLowerCase().contains(firmName))
					{
						list.add(customer);
					}
				}catch(Exception ex) {}

			}
		}
		if(!StringUtils.isEmpty(cityName)){
			cityName = cityName.toLowerCase();
			for(Iterator<Customer> itr = allList.iterator();itr.hasNext();)
			{
				Customer customer = itr.next();
				try {
					if(customer.getCity().toLowerCase().contains(cityName))
					{
						list.add(customer);
					}
				}catch(Exception ex) {}

			}
		}
		return list;
	}



	protected void clearAllContents()
	{
		tfPhone.setText("");
		tfLoyaltyNo.setText("");
		tfName.setText("");
		tfZip.setText("");
		tfBellName.setText("");
		tfFirmName.setText("");
	}
	protected void doCreateNewCustomer() throws ClassNotFoundException, SQLException {
		CustomerForm form = new CustomerForm();
		BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true,true);
		//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setPreferredSize(new Dimension(500, 500));
		dialog.open();

		if (!dialog.isCanceled()) {
			selectedCustomer = (Customer) form.getBean();

			CustomerListTableModel model = (CustomerListTableModel) customerTable.getModel();
			model.addItem(selectedCustomer);
		}
	}
	protected void doEditCustomer(Customer customer, int index) throws ClassNotFoundException, SQLException {
		CustomerForm form = new CustomerForm();
		form.setBean(customer);
		form.setEditMode(true);
		BeanEditorDialog dialog = new BeanEditorDialog(form, Application.getPosWindow(), true,true);
		dialog.open();
		if (!dialog.isCanceled()) {
			ticket.removeCustomer();
			OrderController.saveOrder(ticket);
			selectedCustomer = (Customer) form.getBean();

			CustomerListTableModel model = (CustomerListTableModel) customerTable.getModel();
			model.updateItem(index);
		}

	}
	@Override
	public String getName() {
		return "C";
	}

}
