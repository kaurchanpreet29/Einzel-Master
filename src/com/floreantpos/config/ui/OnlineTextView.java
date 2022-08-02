package com.floreantpos.config.ui;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.FixedLengthDocument;

public class OnlineTextView extends OnlineConfigurationView {
	private JTextArea tfOfferText;
	private JTextArea tfDiscountText;
	private JTextArea tfTempClosed;
	private JTextArea tfNurVorBestellung;
	private JTextArea tfRestaurantClosed;
	private JTextArea tfIndexText;
	private JTextArea tfDeliveryText;
	
	public OnlineTextView() {
		super();
		setLayout(new MigLayout("", "[grow][grow][][grow]", "[][][][][][][][][][][][][][][][][]"));
		
		
		JLabel lblOfferText = new JLabel("Angebot Text");
		add(lblOfferText, "cell 0 0,alignx trailing");
		
		tfOfferText = new JTextArea(3,500);
		tfOfferText.setDocument(new FixedLengthDocument(50));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tfOfferText);
		scrollPane.setPreferredSize(new Dimension(500,80));
		add(tfOfferText, "cell 1 0,growx");
		
		JLabel lblDiscountText = new JLabel("Rabatt Text");
		add(lblDiscountText, "cell 0 1,alignx trailing");
		
		tfDiscountText = new JTextArea(3,500);
		tfDiscountText.setDocument(new FixedLengthDocument(50));
		JScrollPane scrollPaneDiscount = new JScrollPane();
		scrollPaneDiscount.setViewportView(tfDiscountText);
		scrollPaneDiscount.setPreferredSize(new Dimension(500,80));
		add(tfDiscountText, "cell 1 1,growx");
	
		JLabel lblTempClosedText = new JLabel("Temporarily Closed Text");
		add(lblTempClosedText, "cell 0 2,alignx trailing");
		
		tfTempClosed = new JTextArea(3,500);
		tfTempClosed.setDocument(new FixedLengthDocument(50));
		JScrollPane scrollPaneTempClosed= new JScrollPane();
		scrollPaneTempClosed.setViewportView(tfTempClosed);
		scrollPaneTempClosed.setPreferredSize(new Dimension(500,80));
		add(tfTempClosed, "cell 1 2,growx");
		
		JLabel lblNurVorBestellungText = new JLabel("Nur Vorbestellung Text");
		add(lblNurVorBestellungText, "cell 0 3,alignx trailing");
		
		tfNurVorBestellung = new JTextArea(3,500);
		JScrollPane scrollPaneNur = new JScrollPane();
		scrollPaneNur.setViewportView(tfNurVorBestellung);
		scrollPaneNur.setPreferredSize(new Dimension(500,80));
		add(tfNurVorBestellung, "cell 1 3,growx");
		
		JLabel lblRestaurantClosed = new JLabel("Restaurant Closed");
		add(lblRestaurantClosed, "cell 0 4,alignx trailing");
		
		tfRestaurantClosed = new JTextArea(3,500);
		JScrollPane scrollPaneRestaurantClosed = new JScrollPane();
		scrollPaneRestaurantClosed.setViewportView(tfRestaurantClosed);
		scrollPaneRestaurantClosed.setPreferredSize(new Dimension(500,80));
		add(tfRestaurantClosed, "cell 1 4,growx");
		
		JLabel lblIndexText = new JLabel("Index Text");
		add(lblIndexText, "cell 0 5,alignx trailing");
		
		tfIndexText = new JTextArea(3,500);
		JScrollPane scrollPaneIndex= new JScrollPane();
		scrollPaneIndex.setViewportView(tfIndexText);
		scrollPaneIndex.setPreferredSize(new Dimension(500,60));
		add(tfIndexText, "cell 1 5,growx");
		
		JLabel lblDeliveryText = new JLabel("Lieferservice Text");
		add(lblDeliveryText, "cell 0 7,alignx trailing");
		
		tfDeliveryText = new JTextArea(3,500);
		JScrollPane scrollPaneDelivery = new JScrollPane();
		scrollPaneDelivery.setViewportView(tfDeliveryText);
		scrollPaneDelivery.setPreferredSize(new Dimension(500,60));
		add(tfDeliveryText, "cell 1 7 ,grow");
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
	    Restaurant restaurant = RestaurantDAO.getRestaurant();
	    String query = "UPDATE config SET angebot_text='"+ tfOfferText.getText()
	    		+ "',discount_text='" + tfDiscountText.getText() + "',temp_closed_text='" + tfTempClosed.getText() + "',nur_vorbestellung_text='"
	    		+ tfNurVorBestellung.getText() + "',restaurant_closed_text='"+ tfRestaurantClosed.getText() + "',index_text='"
	    		+ tfIndexText.getText() + "',lieferservice_text='" + tfDeliveryText.getText()
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
	      
	        tfOfferText.setText(rs.getString("angebot_text"));
	        tfDiscountText.setText(rs.getString("discount_text"));
	        tfIndexText.setText(rs.getString("index_text"));
	        tfDeliveryText.setText(rs.getString("lieferservice_text"));
	        tfNurVorBestellung.setText(rs.getString("nur_vorbestellung_text"));
	        tfRestaurantClosed.setText(rs.getString("restaurant_closed_text"));
	        tfTempClosed.setText(rs.getString("temp_closed_text"));
	        
	    }
	    preparedStmt.close();
	    conn.close();
	    setInitialized(true);
	}
	
	@Override
	public String getName() {
		return "Online Text";
	}
}
