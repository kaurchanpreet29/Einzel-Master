package com.floreantpos.config.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.POSTextField;

public class OnlinePaymentView extends OnlineConfigurationView {
	private POSTextField tfMinorder;
	private JComboBox cbCashPayment;
	private JComboBox cbOnlinePayment;
	private JComboBox cbCoupon;
	private List<String> openList;
	private JTextArea tfCheckout;
	public OnlinePaymentView() {
		super();
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[][][][][][][][][][][][][][][][][]"));
		
		JLabel lblMinorder = new JLabel("MindesBestellung");
		add(lblMinorder);
		
		tfMinorder = new POSTextField();
		tfMinorder.setDocument(new FixedLengthDocument(50));
		add(tfMinorder, "wrap");
		
		JLabel lblCashPayment = new JLabel("Bar Zahlung");
		add(lblCashPayment, "cell 0 1");
		
		openList = new ArrayList();
		openList.add("Ja");
		openList.add("Nein");
		cbCashPayment = new JComboBox();
		cbCashPayment.setBackground(Color.WHITE);
		cbCashPayment.setModel(new ComboBoxModel(openList));
		add(cbCashPayment, "wrap");
		
		JLabel lblOnlinePayment = new JLabel("Online Zahlung");
		add(lblOnlinePayment);
		
		cbOnlinePayment = new JComboBox();
		cbOnlinePayment.setBackground(Color.WHITE);
		cbOnlinePayment.setModel(new ComboBoxModel(openList));
		add(cbOnlinePayment, "wrap");
		
		JLabel lblCoupon = new JLabel("Gutschein");
		add(lblCoupon);
		
		cbCoupon= new JComboBox();
		cbCoupon.setBackground(Color.WHITE);
		cbCoupon.setModel(new ComboBoxModel(openList));
		add(cbCoupon, "wrap");
		
		JLabel lblCheckout = new JLabel("Checkout Text");
		add(lblCheckout);
		
		tfCheckout = new JTextArea(3,500);
		JScrollPane scrollPaneCheckout = new JScrollPane();
		scrollPaneCheckout.setViewportView(tfCheckout);
		scrollPaneCheckout.setPreferredSize(new Dimension(500,60));
		add(tfCheckout, "wrap");
		
	}
	@Override
	public boolean save() throws Exception {
		if(!isInitialized()) {
			return true;
		}
		int cashpayment;
	    if(cbCashPayment.getSelectedIndex() == 0)cashpayment = 1;else cashpayment = 0;
	    int onlinepayment;
	    if(cbOnlinePayment.getSelectedIndex() == 0)	onlinepayment = 1;else onlinepayment = 0;
	    int couponpayment;
	    if(cbCoupon.getSelectedIndex() == 0) couponpayment = 1;else couponpayment = 0;
        
        String myDriver = "org.gjt.mm.mysql.Driver";
	    String myUrl = "jdbc:mysql://vwp9989.webpack.hosteurope.de/db10975332-khana";
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, "dbu10975332", "ch03ms23");
	    Restaurant restaurant = RestaurantDAO.getRestaurant();
	    String query = "UPDATE config SET payment_bar="+ cashpayment + ",payment_online=" 
	    		+ onlinepayment + ",payment_gutschein=" + couponpayment + ",mindestbestellung='"
	    		+ tfMinorder.getText() + "',checkout_text='"+ tfCheckout.getText() 
	    		+ "' WHERE restaurant_name='"+ restaurant.getName() + "'";
	    
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
	    restaurant.getName() + "'";
	    PreparedStatement preparedStmt = conn.prepareStatement(query);
	    
	    ResultSet rs = preparedStmt.executeQuery();
	    while (rs.next())
	      {
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
	        tfMinorder.setText(rs.getString("mindestbestellung"));
	        tfCheckout.setText(rs.getString("checkout_text"));
	    }
	    preparedStmt.close();
	    conn.close();
	    setInitialized(true);
	}
	
	@Override
	public String getName() {
		return "Online Payment";
	}
}
