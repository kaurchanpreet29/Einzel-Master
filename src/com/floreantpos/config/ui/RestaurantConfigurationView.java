package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.dialog.NotesDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

import javax.swing.JTextField;

public class RestaurantConfigurationView extends ConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	private POSTextField tfRestaurantName;
	private POSTextField tfAddressLine1;
	private POSTextField tfAddressLine2;
	private POSTextField tfAddressLine3;
	private POSTextField tfTelephone;
	private POSTextField tfFax;
	private POSTextField tfEmail;
	private POSTextField tfCurrencyName;
	private POSTextField tfCurrencySymbol;
	private POSTextField tfTicketFooter;
	private POSTextField tfTicketFooter1;
	private POSTextField tfTicketFooter2;
	private POSTextField tfKdisplay1;
	private POSTextField tfKdisplay2;
	private JButton btnGreetingText;
	private JButton btnFooterText;
	JTextField greetingTextField;
	JTextField footerTextField;
	private JTextField tfOnlineUpdateLink = new JTextField();



	private JTextField tfZipCode;
	List hourList;
	public RestaurantConfigurationView() {
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[grow][][][][][][][][][][][][][][][][]"));
		
		JLabel lblNewLabel = new JLabel();
		
		if(StringUtils.isNotEmpty(POSConstants.Geschaeft_name + ":"))
			lblNewLabel.setText(POSConstants.Geschaeft_name + ":");
		else
			lblNewLabel.setText("Name des Geschaeft" + ":");
		
		add(lblNewLabel, "cell 0 1,alignx trailing");
		setBackground(new Color(209,222,235));
		tfRestaurantName = new POSTextField();
		tfRestaurantName.setDocument(new FixedLengthDocument(80));
		add(tfRestaurantName, "cell 1 1 3 1,growx");
		
		JLabel lblAddressLine = new JLabel("Anschrift" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Anschrift + ":"))
			lblAddressLine.setText(POSConstants.Anschrift + ":");
		else
			lblAddressLine.setText("Anschrift" + ":");
		
		add(lblAddressLine, "cell 0 2,alignx trailing");
		
		tfAddressLine1 = new POSTextField();
		tfAddressLine1.setDocument(new FixedLengthDocument(80));
		add(tfAddressLine1, "cell 1 2 3 1,growx");
		
		JLabel lblAddressLine_1 = new JLabel("Zusatz" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Zusatz + ":"))
			lblAddressLine_1.setText(POSConstants.Zusatz + ":");
		else
			lblAddressLine_1.setText("Zusatz" + ":");
		
		add(lblAddressLine_1, "cell 0 3,alignx trailing");
		
		tfAddressLine2 = new POSTextField();
		tfAddressLine2.setDocument(new FixedLengthDocument(80));
		add(tfAddressLine2, "cell 1 3 3 1,growx");
		
		JLabel lblAddressLine_2 = new JLabel("Stadt" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Stadt + ":"))
			lblAddressLine_2.setText(POSConstants.Stadt + ":");
		else
			lblAddressLine_2.setText("Stadt" + ":");
		
		add(lblAddressLine_2, "cell 0 4,alignx trailing");
		
		tfAddressLine3 = new POSTextField();
		tfAddressLine3.setDocument(new FixedLengthDocument(80));
		add(tfAddressLine3, "cell 1 4 3 1,growx");
		
		JLabel lblZipCode = new JLabel("PLZ");
		
		if(StringUtils.isNotEmpty(POSConstants.PLZ + ":"))
			lblZipCode.setText(POSConstants.PLZ + ":");
		else
			lblZipCode.setText("PLZ" + ":");
		
		add(lblZipCode, "cell 0 5,alignx trailing");		
		tfZipCode = new JTextField();
		add(tfZipCode, "cell 1 5,growx");
		tfZipCode.setColumns(10);
		
		JLabel lblEmail = new JLabel("E-mail");
		add(lblEmail, "cell 2 5,alignx trailing");
		
		tfEmail  = new POSTextField();
		add(tfEmail, "cell 3 5,growx");
		
		JLabel lblPhone = new JLabel("Telefon");
		
		if(StringUtils.isNotEmpty(POSConstants.Telefon))
			lblPhone.setText(POSConstants.Telefon);
		else
			lblPhone.setText("Telefon");
		
		add(lblPhone, "cell 0 6,alignx trailing");
		
		tfTelephone = new POSTextField();
		add(tfTelephone, "cell 1 6 2 1,growx");
		
		JLabel lblFax = new JLabel("Fax");
		add(lblFax, "cell 0 7,alignx trailing");
		
		tfFax = new POSTextField();
		add(tfFax, "cell 1 7 2 1,growx");
		
		JLabel lblCurrencyName = new JLabel("Waehrung" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Waehrung + ":"))
			lblCurrencyName.setText(POSConstants.Waehrung + ":");
		else
			lblCurrencyName.setText("Waehrung" + ":");
		
		add(lblCurrencyName, "cell 0 10,alignx trailing");
		
		tfCurrencyName = new POSTextField();
		add(tfCurrencyName, "flowx,cell 1 10");
		
		JLabel lblCurrencySymbol = new JLabel("Waehrungssymbol" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Waehrungssymbol + ":"))
			lblCurrencySymbol.setText(POSConstants.Waehrungssymbol + ":");
		else
			lblCurrencySymbol.setText("Waehrungssymbol" + ":");
		
		add(lblCurrencySymbol, "cell 2 10,alignx trailing");
		
		tfCurrencySymbol = new POSTextField();
		add(tfCurrencySymbol, "cell 3 10,growx");
		
		JLabel lblTicketFooterMessage = new JLabel("Bon Fusszeile 1" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Bon_Fusszeile1 + ":"))
			lblTicketFooterMessage.setText(POSConstants.Bon_Fusszeile1 + ":");
		else
			lblTicketFooterMessage.setText("Bon Fusszeile 1" + ":");
		
		add(lblTicketFooterMessage, "cell 0 14,alignx trailing");
		
		tfTicketFooter = new POSTextField();
		add(tfTicketFooter, "cell 1 14 3 1,growx");
	
		JLabel lblTicketFooterMessage1 = new JLabel("Bon Fusszeile 2" + ":");
		
		if(StringUtils.isNotEmpty(POSConstants.Bon_Fusszeile2 + ":"))
			lblTicketFooterMessage1.setText(POSConstants.Bon_Fusszeile2 + ":");
		else
			lblTicketFooterMessage1.setText("Bon Fusszeile 2" + ":");
		
		add(lblTicketFooterMessage1, "cell 0 15,alignx trailing");
		
		tfTicketFooter1 = new POSTextField();
		add(tfTicketFooter1, "cell 1 15 3 1,growx");
	
		JLabel lblTicketFooterMessage2 = new JLabel("Steuer-Nr.");
		
		if(StringUtils.isNotEmpty(POSConstants.Steuer_Nr ))
			lblTicketFooterMessage2.setText(POSConstants.Steuer_Nr);
		else
			lblTicketFooterMessage2.setText("Steuer-Nr.");
		
		add(lblTicketFooterMessage2, "cell 0 16,alignx trailing");
		
		tfTicketFooter2 = new POSTextField();
		add(tfTicketFooter2, "cell 1 16 3 1,growx");
		
		JLabel lblKdisplay1 = new JLabel("Kunden Display Text1");
		
		if(StringUtils.isNotEmpty(POSConstants.Kunden_Display_Text1 ))
			lblKdisplay1.setText(POSConstants.Kunden_Display_Text1);
		else
			lblKdisplay1.setText("Kunden Display Text1");
		
		add(lblKdisplay1, "cell 0 17,alignx trailing");
		
		tfKdisplay1 = new POSTextField();
		add(tfKdisplay1, "cell 1 17,growx");
		tfKdisplay1.setColumns(10);
		
		JLabel lblKdisplay2 = new JLabel("Kunden Display Text2");
		
		if(StringUtils.isNotEmpty(POSConstants.Kunden_Display_Text2 ))
			lblKdisplay2.setText(POSConstants.Kunden_Display_Text2);
		else
			lblKdisplay2.setText("Kunden Display Text2");
		
		add(lblKdisplay2, "cell 2 17,alignx trailing");
		tfKdisplay2  = new POSTextField();
		add(tfKdisplay2, "cell 3 17,growx, wrap");
		tfKdisplay2.setColumns(10);
		greetingTextField = new JTextField();
		
		btnGreetingText = new JButton("Grußtext");
		
		if(StringUtils.isNotEmpty(POSConstants.Grußtext ))
			btnGreetingText.setText(POSConstants.Grußtext);
		else
			btnGreetingText.setText("Grußtext");
		
		btnGreetingText.setBackground(new Color(104, 244, 66));
		
		btnGreetingText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NotesDialog dialog = new NotesDialog(Application.getPosWindow(), true);
				dialog.setTitle("Grußtext");
				dialog.setNote(restaurant.getGreetingText());
				dialog.pack();
				dialog.open();

				if(dialog.isCanceled()) {
					return;
				}
				greetingTextField.setText(dialog.getNote());			
			}
		});
		
		footerTextField = new JTextField();				
		btnFooterText = new JButton("Footer text");
		btnFooterText.setBackground(new Color(104, 244, 66));		
		btnFooterText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NotesDialog dialog = new NotesDialog(Application.getPosWindow(), true);
				dialog.setTitle("Footer text");
				dialog.setNote(restaurant.getFooterText());
				dialog.pack();
				dialog.open();

				if(dialog.isCanceled()) {
					return;
				}
				footerTextField.setText(dialog.getNote());			
			}
		});

		add(btnGreetingText, "cell 0 18,growx");
		add(btnFooterText, "cell 1 18,growx, wrap");
		if(Application.getCurrentUser().getFirstName().compareTo("Super-User")==0) {
			add(new JLabel("Kasse UpdateLink: "));
			add(tfOnlineUpdateLink);}
		
	}
	
	public void populateCombo(JComboBox cbHour)
	{
		cbHour.addItem("06:15");cbHour.addItem("06:30");cbHour.addItem("06:45");cbHour.addItem("07:00");
		cbHour.addItem("07:15");cbHour.addItem("07:30");cbHour.addItem("07:45");cbHour.addItem("08:00");
		cbHour.addItem("08:15");cbHour.addItem("08:30");cbHour.addItem("08:45");cbHour.addItem("09:00");
		cbHour.addItem("09:15");cbHour.addItem("09:30");cbHour.addItem("09:45");cbHour.addItem("10:00");
		cbHour.addItem("10:15");cbHour.addItem("10:30");cbHour.addItem("10:45");cbHour.addItem("11:00");
		cbHour.addItem("11:15");cbHour.addItem("11:30");cbHour.addItem("11:45");cbHour.addItem("12:00");
		cbHour.addItem("12:15");cbHour.addItem("12:30");cbHour.addItem("12:45");cbHour.addItem("13:00");
		cbHour.addItem("13:15");cbHour.addItem("13:30");cbHour.addItem("13:45");cbHour.addItem("14:00");
		cbHour.addItem("14:15");cbHour.addItem("14:30");cbHour.addItem("14:45");cbHour.addItem("15:00");
		cbHour.addItem("15:15");cbHour.addItem("15:30");cbHour.addItem("15:45");cbHour.addItem("16:00");
		cbHour.addItem("16:15");cbHour.addItem("16:30");cbHour.addItem("16:45");cbHour.addItem("17:00");
		cbHour.addItem("17:15");cbHour.addItem("17:30");cbHour.addItem("17:45");cbHour.addItem("18:00");
		cbHour.addItem("18:15");cbHour.addItem("18:30");cbHour.addItem("18:45");cbHour.addItem("19:00");
		cbHour.addItem("19:15");cbHour.addItem("19:30");cbHour.addItem("19:45");cbHour.addItem("20:00");
		cbHour.addItem("20:15");cbHour.addItem("20:30");cbHour.addItem("20:45");cbHour.addItem("21:00");
		cbHour.addItem("21:15");cbHour.addItem("21:30");cbHour.addItem("21:45");cbHour.addItem("22:00");
		cbHour.addItem("22:15");cbHour.addItem("22:30");cbHour.addItem("22:45");cbHour.addItem("23:00");
		cbHour.addItem("23:15");cbHour.addItem("23:30");cbHour.addItem("23:45");cbHour.addItem("24:00");
		cbHour.addItem("00:15");cbHour.addItem("24:30");cbHour.addItem("00:45");cbHour.addItem("01:00");
	}
	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}
		
		String name = null;
		String addr1 = null;
		String addr2 = null;
		String addr3 = null;
		String telephone = null;
		String fax = null;
		String currencyName = null;
		String currencySymbol = null;
		
		int capacity = 0;
		int tables = 0;
		double serviceCharge = 0;
		double gratuityPercentage = 0;

		name = tfRestaurantName.getText();
		addr1 = tfAddressLine1.getText();
		addr2 = tfAddressLine2.getText();
		addr3 = tfAddressLine3.getText();
		telephone = tfTelephone.getText();
		fax = tfFax.getText();
		currencyName = tfCurrencyName.getText();
		currencySymbol = tfCurrencySymbol.getText();
		
		if(StringUtils.isEmpty(currencyName)) {
			currencyName = com.floreantpos.POSConstants.DOLLAR;
		}
		if(StringUtils.isEmpty(currencySymbol)) {
			currencySymbol = "$";
		}
		restaurant.setName(name);
		restaurant.setAddressLine1(addr1);
		restaurant.setAddressLine2(addr2);
		restaurant.setAddressLine3(addr3);
		restaurant.setZipCode(tfZipCode.getText());
		restaurant.setEmail(tfEmail.getText());
		restaurant.setTelephone(telephone);
		restaurant.setFax(fax);
		restaurant.setCapacity(capacity);
		restaurant.setTables(tables);
		restaurant.setCurrencyName(currencyName);
		restaurant.setCurrencySymbol(currencySymbol);
		restaurant.setServiceChargePercentage(serviceCharge);
		restaurant.setDefaultGratuityPercentage(gratuityPercentage);
		restaurant.setTicketFooterMessage(tfTicketFooter.getText());
		restaurant.setTicketFooterMessage1(tfTicketFooter1.getText());
		restaurant.setTicketFooterMessage2(tfTicketFooter2.getText());
		restaurant.setGreetingText(greetingTextField.getText());
		restaurant.setFooterText(footerTextField.getText());
		restaurant.setUpdateLink(tfOnlineUpdateLink.getText());
		dao.saveOrUpdate(restaurant);		
		Application.getInstance().refreshRestaurant();
		TerminalConfig.setKundenDisplayText1(tfKdisplay1.getText());
		TerminalConfig.setKundenDisplayText2(tfKdisplay2.getText());
		return true;
	}
	
	@Override
	public void initialize() throws Exception {
		dao = new RestaurantDAO();
		restaurant = dao.get(Integer.valueOf(1));
		tfRestaurantName.setText(restaurant.getName());
		tfAddressLine1.setText(restaurant.getAddressLine1());
		tfAddressLine2.setText(restaurant.getAddressLine2());
		tfAddressLine3.setText(restaurant.getAddressLine3());
		tfZipCode.setText(restaurant.getZipCode());
		tfEmail.setText(restaurant.getEmail());
		tfTelephone.setText(restaurant.getTelephone());
		tfFax.setText(restaurant.getFax());
		tfCurrencyName.setText(restaurant.getCurrencyName());
		tfCurrencySymbol.setText(restaurant.getCurrencySymbol());
		tfTicketFooter.setText(restaurant.getTicketFooterMessage());
		tfTicketFooter1.setText(restaurant.getTicketFooterMessage1());
		tfTicketFooter2.setText(restaurant.getTicketFooterMessage2());
		tfOnlineUpdateLink.setText(restaurant.getUpdateLink());

		
		tfKdisplay1.setText(TerminalConfig.getKundenDisplayText1());
		tfKdisplay2.setText(TerminalConfig.getKundenDisplayText2());
		greetingTextField.setText(restaurant.getGreetingText()!=null?restaurant.getGreetingText():"");
		footerTextField.setText(restaurant.getFooterText()!=null?restaurant.getFooterText():"");
		setInitialized(true);
	}
	
	@Override
	public String getName() {
		if(StringUtils.isNotEmpty(POSConstants.Geschaeft))
		   return POSConstants.Geschaeft;
		else
		   return "Geschaeft";
	}
}

