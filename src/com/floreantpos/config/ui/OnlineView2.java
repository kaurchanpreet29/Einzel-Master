package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.dialog.POSMessageDialog;

import javax.swing.JTextField;

public class OnlineView2 extends OnlineConfigurationView {
	private JTextArea tfOfferText;
	private JTextArea tfDiscountText;
	private POSTextField tfFacebooklink;
	private POSTextField tfGooglelink;
	private JTextField tfDiscount;
	private POSTextField tfGoogleVerfication;
	private JComboBox cbOpen;
	private JComboBox cbPrintkasse;
	private JComboBox cbPrintemail;
	private JComboBox cbPrintFax;
	private JComboBox cbPrinteprint;
	private JComboBox cbSmsshop;
	private JComboBox cbVorBestellung;
	private JComboBox cbOffer;
	private JComboBox cbSmskunden;	
	public OnlineView2() {
		super();
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[][][][][][][][][][][][][][][][][]"));
		
		
		JLabel lblOpen = new JLabel("Öffnen" + ":");
		add(lblOpen, "cell 0 0,alignx trailing");
		
		List<String> openList = new ArrayList();
		openList.add("Ja");
		openList.add("Nein");
		cbOpen = new JComboBox();
		cbOpen.setModel(new ComboBoxModel(openList));
		add(cbOpen, "cell 1 0,growx");
		cbOpen.setBackground(Color.WHITE);
		
		JLabel lblVorbestellung = new JLabel("VorBestellung" + ":");
		add(lblVorbestellung, "cell 0 1,alignx trailing");
		
		cbVorBestellung = new JComboBox();
		cbVorBestellung.setModel(new ComboBoxModel(openList));
		cbVorBestellung.setBackground(Color.WHITE);
		add(cbVorBestellung, "cell 1 1,growx");
		
		JLabel lblOffer = new JLabel("Angebot");
		add(lblOffer, "cell 0 2,alignx trailing");
		
		cbOffer = new JComboBox();
		cbOffer.setModel(new ComboBoxModel(openList));
		cbOffer.setBackground(Color.WHITE);
		add(cbOffer, "cell 1 2,growx");
		
		JLabel lblDiscount = new JLabel("Rabatt (%)");
		add(lblDiscount, "cell 0 3,alignx trailing");
		
		tfDiscount = new JTextField();
		tfDiscount.setDocument(new FixedLengthDocument(2));
		tfDiscount.setBackground(Color.WHITE);
		add(tfDiscount, "cell 1 3,growx");
		
		JLabel lblGoogleVerfication = new JLabel("Google Verification");
		add(lblGoogleVerfication, "cell 0 4,alignx trailing");
		
		tfGoogleVerfication= new POSTextField();
		tfGoogleVerfication.setDocument(new FixedLengthDocument(500));
		add(tfGoogleVerfication, "cell 1 4,growx");
		
		JLabel lblFacebooklink = new JLabel("Facebook link");
		add(lblFacebooklink, "cell 0 5,alignx trailing");
		tfFacebooklink= new POSTextField();
		tfFacebooklink.setDocument(new FixedLengthDocument(500));
		add(tfFacebooklink, "cell 1 5,growx");
		
		JLabel lblGooglelink = new JLabel("Google link");
		add(lblGooglelink, "cell 0 6,alignx trailing");
		tfGooglelink= new POSTextField();
		tfGooglelink.setDocument(new FixedLengthDocument(500));
		add(tfGooglelink, "cell 1 6,growx");
		
		JLabel lblPrintkasse = new JLabel("Kasse Druck");
		add(lblPrintkasse, "cell 0 7,alignx trailing");
		cbPrintkasse = new JComboBox();
		cbPrintkasse.setModel(new ComboBoxModel(openList));
		cbPrintkasse.setBackground(Color.WHITE);
		add(cbPrintkasse, "cell 1 7,growx");
		
		JLabel lblPrintemail = new JLabel("Email Druck");
		add(lblPrintemail, "cell 0 8,alignx trailing");
		cbPrintemail = new JComboBox();
		cbPrintemail.setModel(new ComboBoxModel(openList));
		cbPrintemail.setBackground(Color.WHITE);
		add(cbPrintemail, "cell 1 8,growx");
		
		JLabel lblPrintfax = new JLabel("Fax Druck");
		add(lblPrintfax, "cell 0 9,alignx trailing");
		cbPrintFax = new JComboBox();
		cbPrintFax.setModel(new ComboBoxModel(openList));
		cbPrintFax.setBackground(Color.WHITE);
		add(cbPrintFax, "cell 1 9,growx");
		
		JLabel lblPrinteprint = new JLabel("Eprint Druck");
		add(lblPrinteprint, "cell 0 10,alignx trailing");
		cbPrinteprint = new JComboBox();
		cbPrinteprint.setModel(new ComboBoxModel(openList));
		cbPrinteprint.setBackground(Color.WHITE);
		add(cbPrinteprint, "cell 1 10,growx");
		
		JLabel lblSmsshop = new JLabel("SMS Shop");
		add(lblSmsshop, "cell 0 11,alignx trailing");
		cbSmsshop = new JComboBox();
		cbSmsshop.setModel(new ComboBoxModel(openList));
		cbSmsshop.setBackground(Color.WHITE);
		add(cbSmsshop, "cell 1 11,growx");
		
		JLabel lblSmskunden = new JLabel("SMS Kunden");
		add(lblSmskunden, "cell 0 12,alignx trailing");
		cbSmskunden = new JComboBox();
		cbSmskunden.setModel(new ComboBoxModel(openList));
		cbSmskunden.setBackground(Color.WHITE);
		add(cbSmskunden, "cell 1 12,growx");
	}
	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}
		
		String myDriver = "org.gjt.mm.mysql.Driver";
	    String myUrl = "jdbc:mysql://vwp9989.webpack.hosteurope.de/db10975332-khana";
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "dbu10975332", "ch03ms23");
	   
	    int open;
	    if(cbOpen.getSelectedIndex() == 0)open = 1;else open = 0;
	    int vorbestellung;
	    if(cbVorBestellung.getSelectedIndex() == 0)	vorbestellung = 1;else vorbestellung = 0;
	    int printkasse;
	    if(cbPrintkasse.getSelectedIndex() == 0) printkasse = 1;else printkasse = 0;
        int printemail;
	    if(cbPrintemail.getSelectedIndex() == 0)printemail = 1;else printemail = 0;
	    int printfax;
        if(cbPrintFax.getSelectedIndex() == 0)printfax = 1;else printfax = 0;
        int printeprint;
        if(cbPrinteprint.getSelectedIndex() == 0)printeprint = 1;else printeprint = 0;
        int smsshop;
        if(cbSmsshop.getSelectedIndex() == 0)smsshop = 1;else smsshop = 0;
        int smskunden;
        if(cbSmskunden.getSelectedIndex() == 0)smskunden = 1;else smskunden = 0;
        int offer;
        if(cbOffer.getSelectedIndex() == 0)offer = 1;else offer = 0;
	    Restaurant restaurant = RestaurantDAO.getRestaurant();
	    String query = "UPDATE config SET open="+ open + ",vorbestellung=" + vorbestellung 
	    		+ ",angebot=" + offer + ",print_kasse=" + printkasse + ",print_email="+ printemail 
	    		+ ",print_fax=" + printfax + ",print_eprint=" +  printeprint +",sms_shop=" + smsshop
	    		+ ",sms_customer=" + smskunden +  ",discount='" + tfDiscount.getText() + "',google_site_verification='" 
	    		+ tfGoogleVerfication.getText() + "',facebook_link='" + tfFacebooklink.getText() + "',google_link='"
	    		+ tfGooglelink.getText() + "' WHERE restaurant_name='"+ restaurant.getSecondaryName() + "'";
	    
	    System.out.println(query);
	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	    preparedStmt.executeUpdate();
		return true;
	}
	
	@Override
	public void initialize() throws Exception {
		String myDriver = "org.gjt.mm.mysql.Driver";
	    String myUrl = "jdbc:mysql://vwp9989.webpack.hosteurope.de/db10975332-khana";
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "dbu10975332", "ch03ms23");
	    Restaurant restaurant = RestaurantDAO.getRestaurant();
	    
	    String query = "SELECT * FROM config WHERE restaurant_id="+ "'"+TerminalConfig.getTerminalId()+"' AND restaurant_name='"+
	    restaurant.getSecondaryName()+ "'";
	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	    
	    ResultSet rs = preparedStmt.executeQuery();
	    while (rs.next())
	      {
	        if(rs.getBoolean("open"))
	        	cbOpen.setSelectedIndex(0);
	        else
	        	cbOpen.setSelectedIndex(1);
	    	if(rs.getBoolean("vorbestellung"))
	        	cbVorBestellung.setSelectedIndex(0);
	        else
	        	cbVorBestellung.setSelectedIndex(1);
	    	
	        if(rs.getBoolean("angebot"))
	        	cbOffer.setSelectedIndex(0);
	        else
	        	cbOffer.setSelectedIndex(1);
	        tfDiscount.setText(rs.getString("discount"));
	        tfGoogleVerfication.setText(rs.getString("google_site_verification"));
	        tfFacebooklink.setText(rs.getString("facebook_link"));
	        tfGooglelink.setText(rs.getString("google_link"));
	        if(rs.getBoolean("print_kasse"))
	        	cbPrintkasse.setSelectedIndex(0);
	        else
	        	cbPrintkasse.setSelectedIndex(1);
	        if(rs.getBoolean("print_email"))
	        	cbPrintemail.setSelectedIndex(0);
	        else
	        	cbPrintemail.setSelectedIndex(1);
	        if(rs.getBoolean("print_fax"))
	        	cbPrintFax.setSelectedIndex(0);
	        else
	        	cbPrintFax.setSelectedIndex(1);
	        if(rs.getBoolean("print_eprint"))
	        	cbPrinteprint.setSelectedIndex(0);
	        else
	        	cbPrinteprint.setSelectedIndex(1);
	        if(rs.getBoolean("sms_shop"))
	        	cbSmsshop.setSelectedIndex(0);
	        else
	        	cbSmsshop.setSelectedIndex(1);
	        if(rs.getBoolean("sms_customer"))
	        	cbSmskunden.setSelectedIndex(0);
	        else
	        	cbSmskunden.setSelectedIndex(1);
	    }
	    preparedStmt.close();
	    conn.close();
	    setInitialized(true);
	}
	
	@Override
	public String getName() {
		return "Online Shop";
	}
}
