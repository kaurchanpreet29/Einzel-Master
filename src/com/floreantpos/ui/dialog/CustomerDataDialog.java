package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.floreantpos.model.Customer;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;

import antlr.StringUtils;
import bsh.StringUtil;
import net.miginfocom.swing.MigLayout;

public class CustomerDataDialog extends POSDialog{
	Ticket ticket;
	public CustomerDataDialog(Ticket ticket)
	{
		super();
		this.ticket = ticket;
		initComponents();
	}
	JPanel QwertyPanel; 
	JPopupMenu popUp;
	public JComboBox<String> cmbSaluation;
	public JTextField tfa4Text1;
	public JTextField tfa4Text2;
	public JTextField tfa4Text3;
	public JTextField tfa4Text4;
	public JTextField tfa4Text5;
	public JTextField tfa4Text6;
	public JTextField tfa4Text7;
	public JComboBox<Customer> customerList;
JComboBox<String> cbType;
Customer customer;
private void initComponents() {
		QwertyPanel = new JPanel();
		QwertyPanel.setBackground(new Color(5,29,53));
		QwertyKeyPad qwerty = new QwertyKeyPad();
		JPanel pan = new JPanel();
		setLayout(new MigLayout("insets 0, gapx 1", "[grow]", ""));
		pan.setLayout(new MigLayout("insets 0, gapx 1", "[grow][grow]", ""));
		pan.setLayout(new MigLayout());
		cmbSaluation = new JComboBox<>();
		cmbSaluation.addItem("Herr");
		cmbSaluation.addItem("Frau");
		cmbSaluation.addItem("An");	
		cmbSaluation.addItem("To");
		cmbSaluation.addItem("Mr.");
		cmbSaluation.addItem("Miss");
		cmbSaluation.addItem("Mrs.");
		tfa4Text1 = new JTextField(40);
		tfa4Text2 = new JTextField(40);
		tfa4Text3 = new JTextField(40);
		tfa4Text4 = new JTextField(40);
		tfa4Text5 = new JTextField(40);
		tfa4Text6 = new JTextField(40);
		tfa4Text7 = new JTextField(40);

		customerList = new JComboBox<>();
		
		cbType = new javax.swing.JComboBox();
		cbType.setFont(new Font(null, Font.PLAIN, 18));
		cbType.setBackground(Color.WHITE);  
	
		for (int i = 0;i<=35;i++) {
			cbType.addItem(i+"");
		}
		cbType.setSelectedIndex(0);

		customerList.setEditable(true);
		customerList.setFocusable(true);
	
		
		for(Customer str:CustomerDAO.getInstance().findAll()) {
			customerList.addItem(str);
		}


		addCustomer = true;
		AutoCompleteDecorator.decorate(customerList);
		customerList.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				try {
					if (event.getStateChange() == ItemEvent.SELECTED) {
						customer = customerList.getItemAt(customerList.getSelectedIndex());
						doOldInfoCustomer(customer);
					}	

				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		JLabel kunden = new JLabel("Kunden");
		JLabel pkategory = new JLabel("Price Category");
		JLabel Anrede = new JLabel("Anrede");
		JLabel Firma = new JLabel("Firma");
		JLabel vName = new JLabel("Vorname Name");
		JLabel Straße = new JLabel("Straße");
		JLabel hausNr = new JLabel("Haus Nr.");
		JLabel Postleitzahl = new JLabel("Postleitzahl");
		JLabel Ort = new JLabel("Ort");
		
		setFont(kunden);
		setFont(pkategory);
		setFont(Firma);
		setFont(vName);
		setFont(Straße);
		setFont(hausNr);
		setFont(Postleitzahl);
		setFont(Ort);
		setFont(Anrede);
		
		cmbSaluation.setFont(new Font(null, Font.PLAIN, 18));
		
		setFont(tfa4Text1);
		setFont(tfa4Text2);
		setFont(tfa4Text3);
		setFont(tfa4Text4);
		setFont(tfa4Text5);
		setFont(tfa4Text6);
		
		customerList.setFont(new Font(null, Font.PLAIN, 18));
		pan.add(kunden,"cell 0 0, wrap");
		pan.add(customerList, "cell 1 0, growx, wrap");		
		pan.add(Anrede,"cell 0 1, growx");
		pan.add(cmbSaluation, "cell 1 1 0 0, growx");
		pan.add(pkategory,"cell 1 1 1 0, growx");
		pan.add(cbType, "cell 1 1 2 0, growx, wrap");
		pan.add(Firma,"cell 0 2, wrap");
		pan.add(tfa4Text1, "cell 1 2, wrap");
		pan.add(vName,"cell 0 3, wrap");
		pan.add(tfa4Text2, "cell 1 3, wrap");
		pan.add(Straße,"cell 0 4, wrap");
		pan.add(tfa4Text3, "cell 1 4, wrap");
		pan.add(hausNr,"cell 0 5, wrap");
		pan.add(tfa4Text4, "cell 1 5, wrap");
		pan.add(Postleitzahl,"cell 0 6, wrap");
		pan.add(tfa4Text5, "cell 1 6, wrap");		
		pan.add(Ort,"cell 0 7, wrap");
		pan.add(tfa4Text6, "cell 1 7, wrap");
		
		JPanel buttomPanel = new JPanel();
		PosButton btnOk = new PosButton("  OK ");
		btnOk.setBackground(Color.GREEN);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doAddCustomerInfo();
				setCanceled(false);
				dispose();
			}
		});
		PosButton btnCancel = new PosButton("ABBRECHEN");
		btnCancel.setBackground(Color.RED);
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		
		buttomPanel.setLayout(new GridLayout());
		buttomPanel.add(btnOk);
		buttomPanel.add(btnCancel);

		setBackground(new Color(128,128,128));
		doOldInfo(ticket);
		add(pan, "cell 0 0, growx, wrap");
		add(qwerty, "cell 0 1, growx, wrap");
		add(buttomPanel, "cell 0 2, growx, wrap"); 



	}
//	Customer newCustomer;

	public Customer getNewCustomer() {
		return customer;
	}
	
	
	public void setFont(JLabel label) {
		label.setFont(new Font(null, Font.PLAIN,18));
	}
	
	public void setFont(JTextField label) {
		label.setFont(new Font(null, Font.PLAIN,18));
	}
	
	boolean addCustomer;
	boolean updateCustomer;
	public void doAddCustomerInfo(){
//		List<Customer> cList = CustomerDAO.getInstance().findAll();
		if(!updateCustomer)
			customer = new Customer();
//		customer.setAutoId(cList.size()+1);

		if(!tfa4Text1.getText().isEmpty()) {
			customer.setFirmName(tfa4Text1.getText());
		}else {
			customer.setFirmName("");
		}

		if(tfa4Text2.getText()!="") {
			customer.setName(tfa4Text2.getText());
		}

		if(tfa4Text3.getText()!="") {
			customer.setAddress(tfa4Text3.getText());
		}

		if(tfa4Text4.getText()!="") {
			customer.setDoorNo(tfa4Text4.getText());
		}		

		if(tfa4Text5.getText()!="") {
			customer.setZipCode(tfa4Text5.getText());
		}

		if(tfa4Text6.getText()!="") {
			customer.setCity(tfa4Text6.getText());
		}

		//		if(tfa4Text6.getText()!=null) {
		//			newCustomer.setLandMark(tfa4Text7.getText());
		//		}

		if(cmbSaluation.getSelectedItem()!=null) {
			customer.setSalutation(cmbSaluation.getSelectedItem().toString());
		}else {
			customer.setSalutation("Herr");
		}
		customer.setPriceCategory(Integer.parseInt(cbType.getSelectedItem().toString()));
		
		try {
			if(addCustomer)
				CustomerDAO.getInstance().save(customer);
			if(updateCustomer)
				CustomerDAO.getInstance().saveOrUpdate(customer);
		}catch(Exception ex) {
			POSMessageDialog.showError("Fehler bei Speichen");
		}			

	}

	public void doOldInfo(Ticket ticket) {
		try {
			if(ticket != null&&ticket.getId()!=null)			{						
				ticket = TicketDAO.getInstance().load(ticket.getId());
				if(ticket.getProperty(Ticket.CUSTOMER_FIRMNAME)!=null)
					tfa4Text1.setText(ticket.getProperty(Ticket.CUSTOMER_FIRMNAME));
				if(ticket.getProperty(Ticket.CUSTOMER_NAME)!=null) {
					tfa4Text2.setText(ticket.getProperty(Ticket.CUSTOMER_NAME));
					addCustomer = false;
					updateCustomer = true;
					try {
						customer = CustomerDAO.getInstance().findByLoyalty(ticket.getProperty(Ticket.CUSTOMER_LOYALTY_NO));
						System.out.println("Loyalty Nr. "+ ticket.getProperty(Ticket.CUSTOMER_LOYALTY_NO));
					}catch(Exception ex) {
					ex.printStackTrace();
					}
					
					if(customer==null)
						customer = new Customer();
				}
				if(ticket.getProperty(Ticket.CUSTOMER_ADDRESS)!=null)
					tfa4Text3.setText(ticket.getProperty(Ticket.CUSTOMER_ADDRESS));
				if(ticket.getProperty(Ticket.CUSTOMER_DOOR)!=null)
					tfa4Text4.setText(ticket.getProperty(Ticket.CUSTOMER_DOOR));
				if(ticket.getProperty(Ticket.CUSTOMER_POSTCODE)!=null)
					tfa4Text5.setText(ticket.getProperty(Ticket.CUSTOMER_POSTCODE));
				if(ticket.getProperty(Ticket.CUSTOMER_CITYNAME)!=null)
					tfa4Text6.setText(ticket.getProperty(Ticket.CUSTOMER_CITYNAME));
				if(ticket.getProperty(Ticket.CUSTOMER_LANDMARK)!=null)
					tfa4Text7.setText(ticket.getProperty(Ticket.CUSTOMER_LANDMARK));
							
				if(ticket.getProperty(Ticket.CUSTOMER_SALUTATION)!=null) {
					setSaluation(ticket.getProperty(Ticket.CUSTOMER_SALUTATION));
				}
				
			}
		}catch(Exception ex) {
			System.out.println("Customer load error...");
		}

	}


	public void doOldInfoCustomer(Customer customer) {
		try {				
			tfa4Text1.setText(customer.getFirmName()!=null?customer.getFirmName():"");
			if(customer.getName()!=null) {
				tfa4Text2.setText(customer.getName());
				addCustomer = false;
				updateCustomer = true;
			}

			if(customer.getAddress()!=null)
				tfa4Text3.setText(customer.getAddress());


			if(customer.getDoorNo()!=null)
				tfa4Text4.setText(customer.getDoorNo());


			if(customer.getZipCode()!=null)
				tfa4Text5.setText(customer.getZipCode());


			if(customer.getCity()!=null)
				tfa4Text6.setText(customer.getCity());


			if(customer.getSalutation()!=null) {
				setSaluation(customer.getSalutation());
			}
			cbType.setSelectedIndex(customer.getPriceCategory());

		}catch(Exception ex) {
			tfa4Text1.setText("");
			tfa4Text2.setText(customerList.getSelectedItem().toString());
			tfa4Text3.setText("");
			tfa4Text4.setText("");
			tfa4Text5.setText("");
			tfa4Text6.setText("");
			cmbSaluation.setSelectedIndex(0);
			cbType.setSelectedIndex(0);
//			ex.printStackTrace();
		}

	}
	
	public void setSaluation(String saluation) {
		if(saluation.compareTo("An")==0)
			cmbSaluation.setSelectedIndex(0);
		else if(saluation.compareTo("Herr")==0)
			cmbSaluation.setSelectedIndex(1);
		else if(saluation.compareTo("Frau")==0)
			cmbSaluation.setSelectedIndex(2);
		else if(saluation.compareTo("To")==0)
			cmbSaluation.setSelectedIndex(3);
		else if(saluation.compareTo("Mr")==0)
			cmbSaluation.setSelectedIndex(4);
		else if(saluation.compareTo("Miss")==0)
			cmbSaluation.setSelectedIndex(5);
		else
			cmbSaluation.setSelectedIndex(6);
	}


}
