package com.floreantpos.ui.forms;

import java.sql.SQLException;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;




import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CustomerForm extends BeanEditor<Customer> {
	private JTextField tfLoyaltyNo;
	private JTextField tfAddress;
	private JTextArea tfDescription;
	private JLabel lblDescription;
	private JTextField tfDoorNo;
	private JRadioButton rbHerr;
	private JRadioButton rbFrau;
	private JComboBox cmbSaluation;
	private JTextField tfName;
	private JTextField tfFirmName;
	private JTextField tfPhone;
	private JTextField tfCity;
	private JTextField tfZipcode;
	JComboBox<String> cbType;

	public CustomerForm() throws ClassNotFoundException, SQLException {
		createUI();
	}

	public CustomerForm(String Phone) throws ClassNotFoundException, SQLException
	{
		createUI();
		tfPhone.setText(Phone);
	}
	public CustomerForm(Customer customer) throws ClassNotFoundException, SQLException
	{
		createUI();
		setBean(customer);

		/*if (customer == null)
			return;
		if (customer.getAddress() != null)
		{

			if (((customer.getName()) != null))
				tfName.setText(customer.getName());
			if ((customer.getTelephoneNo()) != null)
				tfPhone.setText(customer.getTelephoneNo());
			if ((customer.getDoorNo()) != null)
				tfDoorNo.setText(customer.getDoorNo());
			if ((customer.getAddress()) != null)
				tfAddress.setText(customer.getAddress());
			if ((customer.getZipCode()) != null)
				tfZipCode.setText(customer.getZipCode());
			if(customer.getNote() != null)
				tfDescription.setText(customer.getNote());
		}*/
	}
	public CustomerForm(com.floreantpos.onlineOrder.Customer customer) throws ClassNotFoundException, SQLException
	{
		createUI();
		if (customer == null)
			return;
		if (customer.getDeliveryAddress() != null)
		{
			if (((customer.getDeliveryAddress().getFirstName()) != null) && (customer.getDeliveryAddress().getLastName() != null))
				tfName.setText(customer.getDeliveryAddress().getFirstName() + " " + customer.getDeliveryAddress().getLastName());
			if ((customer.getDeliveryAddress().getPhoneNo()) != null)
				tfPhone.setText(customer.getDeliveryAddress().getPhoneNo());
			if ((customer.getDeliveryAddress().getHouseNo()) != null)
				tfDoorNo.setText(customer.getDeliveryAddress().getHouseNo());
			if ((customer.getDeliveryAddress().getStreet()) != null)
				tfAddress.setText(customer.getDeliveryAddress().getStreet());
		}
	}

	public void createUI() throws ClassNotFoundException, SQLException
	{
		setLayout(new MigLayout());
		setBackground(new Color(209,222,235));
		final Border border = BorderFactory.createLineBorder(new Color(0,51,0), 1);
		final Border defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

		tfLoyaltyNo = new JTextField(20);

		tfLoyaltyNo.setText(TerminalConfig.getLoyaltyNo());
		//		add(tfLoyaltyNo);
		tfLoyaltyNo.setEditable(true);
		tfLoyaltyNo.setBackground(new Color(255,204,229));
		tfLoyaltyNo.setFont(new Font("Times New Roman", Font.BOLD, 20));

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new MigLayout());
		mainPanel.setBackground(new Color(209,222,235));
		JLabel lblAddress = new JLabel("Strasse(*)");

		if(StringUtils.isNotEmpty(POSConstants.Strasse))
			lblAddress.setText(POSConstants.Strasse+"(*)");
		 
		tfAddress = new JTextField(30);
		tfAddress.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfAddress.setBackground(new Color(178,225,249));
				tfAddress.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfAddress.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfAddress.setBackground(Color.WHITE);
				tfAddress.setFont(null);
				tfAddress.setBorder(defaultBorder);
			}
		});

		//		rbHerr = new JRadioButton();
		//		rbHerr.setText(POSConstants.HERR);
		//		rbHerr.setBackground(new Color(209,222,235));
		//		rbHerr.setSelected(true);
		//		rbFrau = new JRadioButton();
		//		rbFrau.setText(POSConstants.FRAU);
		//		rbFrau.setBackground(new Color(209,222,235));
		//		cmbSaluation = new ButtoncmbSaluation();
		//		cmbSaluation.add(rbHerr);
		//		cmbSaluation.add(rbFrau);

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
	
		cbType.addItem(String.valueOf(1));
		
			for (int i=2; i<20;i++) {
				try {
						cbType.addItem(String.valueOf(i));
				}catch(Exception ex) {
					
				}
				
			
		}
		
		cbType.setSelectedIndex(0);

		JLabel lblName = new JLabel("Name(*)");

		tfName = new JTextField(30);

		tfName.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfName.setBackground(new Color(178,225,249));
				tfName.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfName.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfName.setBackground(Color.WHITE);
				tfName.setFont(null);
				tfName.setBorder(defaultBorder);
			}

		});
		
		
		JLabel lblFirmName = new JLabel("Firma Name");
		if(StringUtils.isNotEmpty(POSConstants.Firma_Name))
			lblFirmName.setText(POSConstants.Firma_Name);

		tfFirmName = new JTextField(30);

		tfFirmName.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfFirmName.setBackground(new Color(178,225,249));
				tfFirmName.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfFirmName.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfFirmName.setBackground(Color.WHITE);
				tfFirmName.setFont(null);
				tfFirmName.setBorder(defaultBorder);
			}

		});
		
		
		
		JLabel lblDoor = new JLabel("Nr");


		tfDoorNo = new JTextField(30);



		tfDoorNo.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfDoorNo.setBackground(new Color(178,225,249));
				tfDoorNo.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfDoorNo.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfDoorNo.setBackground(Color.WHITE);
				tfDoorNo.setFont(null);
				tfDoorNo.setBorder(defaultBorder);
			}

		});
		JLabel lblPhone = new JLabel("Tel.");
		tfPhone = new JTextField(30);

		tfPhone.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfPhone.setBackground(new Color(178,225,249));
				tfPhone.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfPhone.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfPhone.setBackground(Color.WHITE);
				tfPhone.setFont(null);
				tfPhone.setBorder(defaultBorder);
			}

		});
		JLabel lblZip = new JLabel("PLZ");
		if(StringUtils.isNotEmpty(POSConstants.PLZ))
			lblZip.setText(POSConstants.PLZ);
		
		tfZipcode = new JTextField(30);

		tfZipcode.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfZipcode.setBackground(new Color(178,225,249));
				tfZipcode.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfZipcode.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfZipcode.setBackground(Color.WHITE);
				tfZipcode.setFont(null);
				tfZipcode.setBorder(defaultBorder);
			}

		});
		JLabel lblCity = new JLabel("Stadt");
		if(StringUtils.isNotEmpty(POSConstants.Stadt))
			lblCity.setText(POSConstants.Stadt);
		
		tfCity = new JTextField(30);

		tfCity.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				tfCity.setBackground(new Color(178,225,249));
				tfCity.setFont(new Font("Tahoma", Font.BOLD, 15));
				tfCity.setBorder(border);
			}

			@Override
			public void focusLost(FocusEvent e) {
				tfCity.setBackground(Color.WHITE);
				tfCity.setFont(null);
				tfCity.setBorder(defaultBorder);
			}

		});

		JLabel lblAnrede = new JLabel("Anrede");
		if(StringUtils.isNotEmpty(POSConstants.Salutation))
			lblAnrede.setText(POSConstants.Salutation);
		
		JLabel lblPriceList = new JLabel("Price Category ");
		if(TerminalConfig.isPriceCategory()) {
			mainPanel.add(lblAnrede,"cell 0 0 1 0, growx");
			mainPanel.add(cmbSaluation,"cell 0 0 2 0, growx");
			mainPanel.add(lblPriceList,"cell 1 0 1 0");
			mainPanel.add(cbType,"cell 1 0 0 1, growx");
		}else {
			mainPanel.add(lblAnrede,"cell 0 0, growx");
			mainPanel.add(cmbSaluation,"cell 1 0 2 0");
		}

		if(StringUtils.isNotEmpty(POSConstants.Kunden_Nr))
			 mainPanel.add(new JLabel(POSConstants.Kunden_Nr),"cell 0 1");
		else
		    mainPanel.add(new JLabel("Kunden Nr."),"cell 0 1");
		
		mainPanel.add(tfLoyaltyNo, "cell 1 1, growx");
		//		mainPanel.add(rbHerr);
		//		mainPanel.add(rbFrau,"growx,wrap");
		mainPanel.add(lblName,"cell 0 2");
		mainPanel.add(tfName,"cell 1 2");
		mainPanel.add(lblAddress,"cell 0 3");
		mainPanel.add(tfAddress,"cell 1 3");
		mainPanel.add(lblDoor,"cell 0 4");
		mainPanel.add(tfDoorNo,"cell 1 4");
		mainPanel.add(lblPhone,"cell 0 5");
		mainPanel.add(tfPhone,"cell 1 5");
		mainPanel.add(lblZip,"cell 0 6");
		mainPanel.add(tfZipcode,"cell 1 6");
		mainPanel.add(lblCity,"cell 0 7");
		mainPanel.add(tfCity,"cell 1 7");
		mainPanel.add(lblFirmName,"cell 0 8");
		mainPanel.add(tfFirmName,"cell 1 8");
		JPanel despanel = new JPanel();
		despanel.setLayout(new MigLayout());

		
		if(StringUtils.isNotEmpty(POSConstants.Bemerkung))
			despanel.add(new JLabel(POSConstants.Bemerkung));
		else
			despanel.add(new JLabel("Bemerkung"));
		
		tfDescription = new JTextArea(30,30); 
		tfDescription.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {

				int length = tfDescription.getText().length();
				if(length >= 1500)
				{
					lblDescription.setText("Felher");
					return;
				}
				length = 1500 - length;
				lblDescription.setText(length + " uebrig");
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				int length = tfDescription.getText().length();
				if(length >= 1500)
				{
					lblDescription.setText("Felher");
					return;
				}
				length = 1500 - length;
				lblDescription.setText(length + " uebrig");
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		despanel.add(tfDescription);
		lblDescription = new JLabel();
		lblDescription.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblDescription.setForeground(Color.RED);
		despanel.add(lblDescription);

		despanel.setBackground(new Color(209,222,235));

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(mainPanel,"growx,wrap");
		add(despanel,"wrap");
		this.setBackground(Color.BLUE);
		updateView();

		revalidate();
		repaint();
		this.setBackground(new Color(209,222,235));
	}
	public void setFieldsEditable(boolean editable) {
		tfName.setEditable(editable);
		tfPhone.setEditable(editable);
		tfLoyaltyNo.setEditable(editable);
		tfAddress.setEditable(editable);
		tfDoorNo.setEditable(editable);
		tfZipcode.setEditable(editable);
		tfCity.setEditable(editable);
		tfFirmName.setEditable(editable);
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			Customer customer = (Customer) getBean();
			CustomerDAO.getInstance().saveOrUpdate(customer);
			return true;
		} catch (IllegalModelStateException e) {
		} catch (StaleObjectStateException e) {
			BOMessageDialog.showError(this, "It seems this Customer is modified by some other person or terminal. Save failed.");
		}

		return false;
	}

	@Override
	protected void updateView() {
		Customer customer = (Customer) getBean();
		setBackground(new Color(209,222,235));
		if(customer != null) {

			tfName.setText(customer.getName());
			tfAddress.setText(customer.getAddress());
			tfDoorNo.setText(customer.getDoorNo());
			tfLoyaltyNo.setText(customer.getLoyaltyNo());
			tfDescription.setText(customer.getDescription());
			tfPhone.setText(customer.getTelephoneNo());
			tfFirmName.setText(customer.getFirmName());
			tfCity.setText(customer.getCity());
			tfZipcode.setText(customer.getZipCode());
			if(TerminalConfig.isPriceCategory())
				cbType.setSelectedIndex(customer.getPriceCategory());
			if (customer.getSalutation() != null)
			{
				setSaluation(customer.getSalutation());
			}

		}
		else {

			/*tfName.setText("");
			tfDoB.setText("");
			tfAddress.setText("");
			tfCity.setText("");
			tfLandMark.setText("");
			tfCreditLimit.setText("");
			tfEmail.setText("");
			tfLoyaltyNo.setText("");
			tfPhone.setText("");
			tfBellName.setText("");
			//tfZipCode.setText("");
			cbVip.setSelected(false);
			tfFirmName.setText("");
			rbHerr.setSelected(false);
			rbFrau.setSelected(false);*/
		}
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		
		if (!editMode) {
			try {
				int id = Integer.parseInt(tfLoyaltyNo.getText());				
				Customer customer = CustomerDAO.getInstance().findByLoyalty(tfLoyaltyNo.getText());
				if(customer!=null&&customer.getAutoId()>0) {
					JOptionPane.showMessageDialog(BackOfficeWindow.getInstance(), "Kunde mit Kundennummer "+tfLoyaltyNo.getText()+"ist existieret");
					return false;
				}					
			} catch (Exception e) {
			}
		}
		
		if(tfDescription.getText().length() >= 1500)
		{
			POSMessageDialog.showError("Bemerkung ist zuu groess");
			return false;
		}
		
		String phone = tfPhone.getText();
		if(phone.contains("/") || phone.contains("-") || phone.contains(":") || phone.contains("(") || phone.contains(")"))
		{
			POSMessageDialog.showError("Telephon Nr muss nicht '/' oder '-' oder ':' oder ')' haben ");
			return false;
		}
		Customer customer = (Customer) getBean();

		if(customer == null) {
			customer = new Customer();
		}
		customer.setDescription(tfDescription.getText());
		customer.setName(tfName.getText());
		customer.setAddress(tfAddress.getText());
		customer.setDoorNo(tfDoorNo.getText());
		customer.setLoyaltyNo(tfLoyaltyNo.getText());
		customer.setTelephoneNo(tfPhone.getText());
		customer.setCity(tfCity.getText());
		customer.setFirmName(tfFirmName.getText());
		customer.setZipCode(tfZipcode.getText());
		customer.setSalutation(cmbSaluation.getSelectedItem().toString());
		if(TerminalConfig.isPriceCategory())
			customer.setPriceCategory(Integer.parseInt(cbType.getSelectedItem().toString()));
		else
			customer.setPriceCategory(1);
		int id = Integer.parseInt(tfLoyaltyNo.getText());  
		TerminalConfig.setLoyaltyNo((id+1)+"");
		revalidate(); 
		repaint();

		setBean(customer);
		return true;
	}
	
	public void setSaluation(String saluation) {
		if(saluation.compareTo("An")==0)
			cmbSaluation.setSelectedIndex(1);
		else if(saluation.compareTo("Herr")==0)
			cmbSaluation.setSelectedIndex(2);
		else if(saluation.compareTo("Frau")==0)
			cmbSaluation.setSelectedIndex(3);
		else if(saluation.compareTo("To")==0)
			cmbSaluation.setSelectedIndex(4);
		else if(saluation.compareTo("Mr")==0)
			cmbSaluation.setSelectedIndex(5);
		else if(saluation.compareTo("Miss")==0)
			cmbSaluation.setSelectedIndex(6);
		else
			cmbSaluation.setSelectedIndex(0);
	}


	@Override
	public String getDisplayText() {
		if(editMode) {
			return "Bearbeiten";
		}
		if (tfName.getText().length() > 1)
			return "Bearbeiten Kunde: "+tfLoyaltyNo.getText();
		else
			return "Neuer Kunde Nr. "+ tfLoyaltyNo.getText();
	}

	/* public static void main(String[] args) throws Exception {
    	String font = new String();

    	System.out.println();
         System.out.println(Arrays.asList(GraphicsEnvironment
            .getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
    }*/

}



