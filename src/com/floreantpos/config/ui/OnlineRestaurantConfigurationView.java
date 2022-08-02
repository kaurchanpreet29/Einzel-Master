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

public class OnlineRestaurantConfigurationView extends OnlineConfigurationView {
	private RestaurantDAO dao;
	private Restaurant restaurant;
	private POSTextField tfRestaurantName;
	private POSTextField tfStrasse;
	private POSTextField tfStrasseNr;
	private POSTextField tfOrt;
	private POSTextField tfTelephone;
	private POSTextField tfFax;
	private POSTextField tfTelephone2;
	private POSTextField tfHandy;
	private POSTextField tfInhaber;
	private POSTextField tfEmail;
	private POSTextField tfWebsite;
	private POSTextField tfShopsite;
	private POSTextField tfEprint;
	private POSTextField tfTax;
	private POSTextField tfUstId;
	private JTextField tfZipCode;

		
	public OnlineRestaurantConfigurationView() {
		super();
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[grow][][][][][][][][][][][][][][][][]"));
		JLabel lblNewLabel = new JLabel("Restaurant name" + ":");
		add(lblNewLabel, "cell 0 0,alignx trailing");
		setBackground(new Color(209,222,235));
		tfRestaurantName = new POSTextField();
		add(tfRestaurantName, "cell 1 0,growx");
		
		
		JLabel lblStrasse = new JLabel("Strasse" + ":");
		add(lblStrasse, "cell 0 1,alignx trailing");
		
		tfStrasse = new POSTextField();
		tfStrasse.setDocument(new FixedLengthDocument(50));
		add(tfStrasse, "cell 1 1,growx");
		
		JLabel lblStrasseNr = new JLabel("StrasseNr" + ":");
		add(lblStrasseNr, "cell 0 2,alignx trailing");
		
		tfStrasseNr = new POSTextField();
		tfStrasseNr.setDocument(new FixedLengthDocument(50));
		add(tfStrasseNr, "cell 1 2,growx");
		
		JLabel lblAddressLine_2 = new JLabel("Stadt" + ":");
		add(lblAddressLine_2, "cell 0 3,alignx trailing");
		
		tfOrt= new POSTextField();
		tfOrt.setDocument(new FixedLengthDocument(50));
		add(tfOrt, "cell 1 3,growx");
		
		JLabel lblZipCode = new JLabel("Plz");
		add(lblZipCode, "cell 0 4,alignx trailing");
		
		tfZipCode = new JTextField();
		tfZipCode.setDocument(new FixedLengthDocument(50));
		add(tfZipCode, "cell 1 4,growx");
	
		
		JLabel lblPhone = new JLabel("Telefon 1");
		add(lblPhone, "cell 0 5,alignx trailing");
		
		tfTelephone = new POSTextField();
		tfTelephone.setDocument(new FixedLengthDocument(50));
		add(tfTelephone, "cell 1 5,growx");
		
		JLabel lblPhone2 = new JLabel("Telefon 2");
		add(lblPhone2, "cell 0 6,alignx trailing");
		
		tfTelephone2 = new POSTextField();
		tfTelephone2.setDocument(new FixedLengthDocument(50));
		add(tfTelephone2, "cell 1 6,growx");
		
		JLabel lblHandy = new JLabel("Handy");
		add(lblHandy, "cell 0 7,alignx trailing");
		
		tfHandy = new POSTextField();
		tfHandy.setDocument(new FixedLengthDocument(50));
		add(tfHandy, "cell 1 7,growx");
		
		JLabel lblFax = new JLabel("Fax");
		add(lblFax, "cell 0 8,alignx trailing");
		
		tfFax = new POSTextField();
		tfFax.setDocument(new FixedLengthDocument(50));
		add(tfFax, "cell 1 8,growx");
		
		JLabel lblEmail = new JLabel("Email");
		add(lblEmail, "cell 0 9,alignx trailing");
		
		tfEmail = new POSTextField();
		tfEmail.setDocument(new FixedLengthDocument(50));
		add(tfEmail, "cell 1 9,growx");
		
		JLabel lblEprint = new JLabel("EPrint");
		add(lblEprint, "cell 0 10,alignx trailing");
		
		tfEprint = new POSTextField();
		tfEprint.setDocument(new FixedLengthDocument(50));
		add(tfEprint, "cell 1 10,growx");
		
		JLabel lblWebsite = new JLabel("Website");
		add(lblWebsite, "cell 0 11,alignx trailing");
		
		tfWebsite = new POSTextField();
		tfWebsite.setDocument(new FixedLengthDocument(50));
		add(tfWebsite, "cell 1 11,growx");
		
		JLabel lblShopsite = new JLabel("Shop URL");
		add(lblShopsite, "cell 0 12,alignx trailing");
		
		tfShopsite= new POSTextField();
		tfShopsite.setDocument(new FixedLengthDocument(50));
		add(tfShopsite, "cell 1 12,growx");
		
		JLabel lblInhaber = new JLabel("Inhaber");
		add(lblInhaber, "cell 0 13,alignx trailing");
		
		tfInhaber = new POSTextField();
		tfInhaber.setDocument(new FixedLengthDocument(50));
		add(tfInhaber, "cell 1 13,growx");
		
		JLabel lblTax = new JLabel("Steuer Id");
		add(lblTax, "cell 0 14,alignx trailing");
		
		tfTax = new POSTextField();
		tfTax.setDocument(new FixedLengthDocument(50));
		add(tfTax, "cell 1 14,growx");
		
		JLabel lblUstId = new JLabel("Ust Id");
		add(lblUstId, "cell 0 15,alignx trailing");
		
		tfUstId = new POSTextField();
		tfUstId.setDocument(new FixedLengthDocument(50));
		add(tfUstId, "cell 1 15,growx");
		/*JSeparator separator = new JSeparator();
		separator.setBackground(Color.black);
		add(separator, "cell 0 8 4 1,growx");*/
	}
	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}
		int found = 0;
		String myDriver = "org.gjt.mm.mysql.Driver";
	    String myUrl = "jdbc:mysql://vwp9989.webpack.hosteurope.de/db10975332-khana";
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "dbu10975332", "ch03ms23");
	   
	    Restaurant restaurant = RestaurantDAO.getRestaurant();
	    String queryCheck = "SELECT * FROM config WHERE restaurant_id="+ "'"+TerminalConfig.getTerminalId()+"' AND restaurant_name='"+
	    restaurant.getName() + "'";
	    PreparedStatement preparedStmtCheck = conn.prepareStatement(queryCheck);
	    ResultSet rs = preparedStmtCheck.executeQuery();
	    while(rs.next())
	    {
	    	found = 1;
	    	break;
	    }
	    
	    if(found == 1)
	    {
	    	 String query = "UPDATE config SET str='"+tfStrasse.getText() 
	 	    		+ "',str_nr='" + tfStrasseNr.getText() + "',plz='" + tfZipCode.getText() + "',ort='"
	 	    		+ tfOrt.getText() + "',tel_1='"+ tfTelephone.getText() + "',tel_2='"
	 	    		+ tfTelephone2.getText() + "',handy='"+ tfHandy.getText() + "',fax='" + tfFax.getText() 
	 	    		+ "',email='" +tfEmail.getText() + "',eprint='"+ tfEprint.getText() + "',website='" + tfWebsite.getText()
	 	    		+ "',shopsite='" + tfShopsite.getText() + "',inhaber='"+ tfInhaber.getText() + "',steur_id='" + tfTax.getText()
	 	    		+ "',ust_id='" + tfUstId.getText() + "' WHERE restaurant_name='"+ restaurant.getName() + "'";
		    
		   System.out.println(query);
		   PreparedStatement preparedStmt = conn.prepareStatement(query);
		   preparedStmt.executeUpdate();
		}
	    /*else
	    {
	    	String query = "INSERT INTO config (restaurant_id,restaurant_name,str,str_nr,plz,ort,"
	    		+ "tel_1,tel_2,handy,fax,email,eprint,website,shopsite,inhaber,steur_id,ust_id) "
	    		+ "values('"+TerminalConfig.getTerminalId()+"','" + tfRestaurantName.getText()
	    		+ "','"+ tfStrasse.getText() + "','" + tfStrasseNr.getText() + "','"+ tfZipCode.getText() + "','"+ 
	    	    tfOrt.getText() + "','" + tfTelephone.getText() + "','" + tfTelephone2.getText() + "','" + tfHandy.getText() + "','"+ 
	    		tfFax.getText() + "','" +tfEmail.getText() + "','" + tfEprint.getText() + "','" + tfWebsite.getText() + "','" + 
				tfShopsite.getText() + "','" + tfInhaber.getText() + "','" + tfTax.getText() + "','"+ tfUstId.getText() +"')";
	    
	    	System.out.println(query);
	    	PreparedStatement preparedStmt = conn.prepareStatement(query);
	    	preparedStmt.executeUpdate();
	    }*/
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
	    restaurant.getName() + "'";
	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	    
	    ResultSet rs = preparedStmt.executeQuery();
	    while (rs.next())
	      {
	        /*if(rs.getBoolean("open"))
	        	cbOpen.setSelectedIndex(0);
	        else
	        	cbOpen.setSelectedIndex(1);
	    	if(rs.getBoolean("vorbestellung"))
	        	cbVorBestellung.setSelectedIndex(0);
	        else
	        	cbVorBestellung.setSelectedIndex(1);*/
	    	tfRestaurantName.setText(rs.getString("restaurant_name"));
	    	tfStrasse.setText(rs.getString("str"));
	        tfStrasseNr.setText(rs.getString("str_nr"));
	        tfZipCode.setText(rs.getString("plz"));
	        tfOrt.setText(rs.getString("ort"));
	        tfTelephone.setText(rs.getString("tel_1"));
	        tfTelephone2.setText(rs.getString("tel_2"));
	        tfHandy.setText(rs.getString("handy"));
	        tfFax.setText(rs.getString("fax"));
	        tfEmail.setText(rs.getString("email"));
	        tfEprint.setText(rs.getString("eprint"));
	        tfWebsite.setText(rs.getString("website"));
	        tfShopsite.setText(rs.getString("shopsite"));
	        tfInhaber.setText(rs.getString("inhaber"));
	        tfTax.setText(rs.getString("steur_id"));
	        tfUstId.setText(rs.getString("ust_id"));
	        /*if(rs.getBoolean("angebot"))
	        	cbOffer.setSelectedIndex(0);
	        else
	        	cbOffer.setSelectedIndex(1);
	        if(rs.getBoolean("discount"))
	        	cbDiscount.setSelectedIndex(0);
	        else
	        	cbDiscount.setSelectedIndex(1);
	        tfOfferText.setText(rs.getString("angebot_text"));
	        tfDiscountText.setText(rs.getString("discount_text"));
	        tfMinorder.setText(rs.getString("mindestbestellung"));
	        if(rs.getBoolean("payment_bar"))
	        	cbCashPayment.setSelectedIndex(0);
	        else
	        	cbCashPayment.setSelectedIndex(1);
	        if(rs.getBoolean("payment_online"))
	        	cbOnlinePayment.setSelectedIndex(0);
	        else
	        	cbOnlinePayment.setSelectedIndex(1);
	        if(rs.getBoolean("payment_gutschein"))
	        	cbCoupon.setSelectedIndex(0);
	        else
	        	cbCoupon.setSelectedIndex(1);
	        tfGoogleVerfication.setText(rs.getString("google_site_verification"));
	        tfTempClosed.setText(rs.getString("temp_closed_text"));
	        tfNurVorBestellung.setText(rs.getString("nur_vorbestellung_text"));
	        tfRestaurantClosed.setText(rs.getString("restaurant_closed_text"));
	        tfIndexText.setText(rs.getString("index_text"));
	        tfCheckout.setText(rs.getString("checkout_text"));*/
	    }
	    preparedStmt.close();
	    conn.close();
	    setInitialized(true);
	}
	
	@Override
	public String getName() {
		return com.floreantpos.POSConstants.CONFIG_TAB_RESTAURANT;
	}
}
