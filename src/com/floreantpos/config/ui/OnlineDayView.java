package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class OnlineDayView extends OnlineConfigurationView {
	private JComboBox cbMonLOpen;
	private JComboBox cbTueLOpen;
	private JComboBox cbWedLOpen;
	private JComboBox cbThuLOpen;
	private JComboBox cbFriLOpen;
	private JComboBox cbSatLOpen;
	private JComboBox cbSunLOpen;
	private JComboBox cbHolLOpen;
		
	private JComboBox cbMonDOpen;
	private JComboBox cbTueDOpen;
	private JComboBox cbWedDOpen;
	private JComboBox cbThuDOpen;
	private JComboBox cbFriDOpen;
	private JComboBox cbSatDOpen;
	private JComboBox cbSunDOpen;
	private JComboBox cbHolDOpen;
	
	private JComboBox cbMonLClose;
	private JComboBox cbTueLClose;
	private JComboBox cbWedLClose;
	private JComboBox cbThuLClose;
	private JComboBox cbFriLClose;
	private JComboBox cbSatLClose;
	private JComboBox cbSunLClose;
	private JComboBox cbHolLClose;
	
	private JComboBox cbMonDClose;
	private JComboBox cbTueDClose;
	private JComboBox cbWedDClose;
	private JComboBox cbThuDClose;
	private JComboBox cbFriDClose;
	private JComboBox cbSatDClose;
	private JComboBox cbSunDClose;
	private JComboBox cbHolDClose;
	
	private JCheckBox cbMon;
	private JCheckBox cbTue;
	private JCheckBox cbWed;
	private JCheckBox cbThu;
	private JCheckBox cbFri;
	private JCheckBox cbSat;
	private JCheckBox cbSun;
	private JCheckBox cbHol;
	public OnlineDayView() {
		super();
		setLayout(new GridLayout(9,6,10,10));
		setBackground(new Color(209,222,235));
		JLabel lblEmpty = new JLabel("");
		add(lblEmpty);
		JLabel lblLunchOpen = new JLabel("Open 1");
		add(lblLunchOpen);
		JLabel lblLunchClose = new JLabel("Close 1");
		add(lblLunchClose);
		JLabel lblDinnerOpen = new JLabel("Open 2");
		add(lblDinnerOpen);
		JLabel lblDinnerClose = new JLabel("Close 2");
		add(lblDinnerClose);
		JLabel lblRuhig = new JLabel("Ruhig");
		add(lblRuhig);
		
		
		List lunchList = new ArrayList();
		lunchList.add("");
		lunchList.add("00:00");
		lunchList.add("00:30");
		lunchList.add("01:00");
		lunchList.add("01:30");
		lunchList.add("02:00");
		lunchList.add("02:30");
		lunchList.add("03:00");
		lunchList.add("03:30");
		lunchList.add("04:00");
		lunchList.add("04:30");
		lunchList.add("05:00");
		lunchList.add("05:30");
		lunchList.add("06:00");
		lunchList.add("06:30");
		lunchList.add("07:00");
		lunchList.add("07:30");
		lunchList.add("08:00");
		lunchList.add("08:30");
		lunchList.add("09:00");
		lunchList.add("09:30");
		lunchList.add("10:00");
		lunchList.add("10:30");
		lunchList.add("11:00");
		lunchList.add("11:30");
		lunchList.add("12:00");
		lunchList.add("12:30");
		lunchList.add("13:00");
		lunchList.add("13:30");
		lunchList.add("14:00");
		lunchList.add("14:30");
		lunchList.add("15:00");
		lunchList.add("15:30");
		
		List dinnerList = new ArrayList();
		dinnerList.add("");
		dinnerList.add("15:30");
		dinnerList.add("16:00");
		dinnerList.add("16:30");
		dinnerList.add("17:00");
		dinnerList.add("17:30");
		dinnerList.add("18:00");
		dinnerList.add("18:30");
		dinnerList.add("19:00");
		dinnerList.add("19:30");
		dinnerList.add("20:00");
		dinnerList.add("20:30");
		dinnerList.add("20:00");
		dinnerList.add("20:30");
		dinnerList.add("21:00");
		dinnerList.add("21:30");
		dinnerList.add("22:00");
		dinnerList.add("22:30");
		dinnerList.add("23:00");
		dinnerList.add("23:30");
		
		JLabel lblMonday = new JLabel("Montag");
		add(lblMonday);
		cbMonLOpen = new JComboBox();
		cbMonLOpen.setModel(new ComboBoxModel(lunchList));
		cbMonLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbMonLOpen.setBackground(Color.WHITE);
		add(cbMonLOpen);
		cbMonLClose = new JComboBox();
		cbMonLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbMonLClose.setBackground(Color.WHITE);
		cbMonLClose.setModel(new ComboBoxModel(lunchList));
		add(cbMonLClose);
		cbMonDOpen = new JComboBox();
		cbMonDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbMonDOpen.setModel(new ComboBoxModel(dinnerList));
		cbMonDOpen.setBackground(Color.WHITE);
		add(cbMonDOpen);
		cbMonDClose = new JComboBox();
		cbMonDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbMonDClose.setModel(new ComboBoxModel(dinnerList));
		cbMonDClose.setBackground(Color.WHITE);
		add(cbMonDClose);
		cbMon = new JCheckBox();
		cbMon.setBackground(new Color(209,222,235));
		cbMon.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbMon.isSelected())
				{
					cbMonLOpen.setSelectedIndex(0);
					cbMonLClose.setSelectedIndex(0);
					cbMonDOpen.setSelectedIndex(0);
					cbMonDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		add(cbMon);
		
		JLabel lblTuesday = new JLabel("Dienstag");
		add(lblTuesday);
		cbTueLOpen = new JComboBox();
		cbTueLOpen.setBackground(Color.WHITE);
		cbTueLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbTueLOpen.setModel(new ComboBoxModel(lunchList));
		add(cbTueLOpen);
		cbTueLClose = new JComboBox();
		cbTueLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbTueLClose.setModel(new ComboBoxModel(lunchList));
		cbTueLClose.setBackground(Color.WHITE);
		add(cbTueLClose);
		cbTueDOpen = new JComboBox();
		cbTueDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbTueDOpen.setBackground(Color.WHITE);
		cbTueDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbTueDOpen);
		cbTueDClose = new JComboBox();
		cbTueDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbTueDClose.setModel(new ComboBoxModel(dinnerList));
		cbTueDClose.setBackground(Color.WHITE);
		add(cbTueDClose);
		cbTue= new JCheckBox();
		cbTue.setBackground(new Color(209,222,235));
		cbTue.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbTue.isSelected())
				{
					cbTueLOpen.setSelectedIndex(0);
					cbTueLClose.setSelectedIndex(0);
					cbTueDOpen.setSelectedIndex(0);
					cbTueDClose.setSelectedIndex(0);
				}
			}
		});
		add(cbTue);
		
		JLabel lblWed = new JLabel("Mittwoch");
		add(lblWed);
		cbWedLOpen = new JComboBox();
		cbWedLOpen.setBackground(Color.WHITE);
		cbWedLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbWedLOpen.setModel(new ComboBoxModel(lunchList));
		add(cbWedLOpen);
		cbWedLClose = new JComboBox();
		cbWedLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbWedLClose.setModel(new ComboBoxModel(lunchList));
		cbWedLClose.setBackground(Color.WHITE);
		add(cbWedLClose);
		cbWedDOpen = new JComboBox();
		cbWedDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbWedDOpen.setBackground(Color.WHITE);
		cbWedDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbWedDOpen);
		cbWedDClose = new JComboBox();
		cbWedDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbWedDClose.setBackground(Color.WHITE);
		cbWedDClose.setModel(new ComboBoxModel(dinnerList));
		add(cbWedDClose);
		cbWed= new JCheckBox();
		cbWed.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbWed.isSelected())
				{
					cbWedLOpen.setSelectedIndex(0);
					cbWedLClose.setSelectedIndex(0);
					cbWedDOpen.setSelectedIndex(0);
					cbWedDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		cbWed.setBackground(new Color(209,222,235));
		add(cbWed);
		
		JLabel lblThu = new JLabel("Donnerstag");
		add(lblThu);
		cbThuLOpen = new JComboBox();
		cbThuLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbThuLOpen.setBackground(Color.WHITE);
		cbThuLOpen.setModel(new ComboBoxModel(lunchList));
		add(cbThuLOpen);
		cbThuLClose = new JComboBox();
		cbThuLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbThuLClose.setModel(new ComboBoxModel(lunchList));
		cbThuLClose.setBackground(Color.WHITE);
		add(cbThuLClose);
		cbThuDOpen = new JComboBox();
		cbThuDOpen.setBackground(Color.WHITE);
		cbThuDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbThuDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbThuDOpen);
		cbThuDClose = new JComboBox();
		cbThuDClose.setBackground(Color.WHITE);
		cbThuDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbThuDClose.setModel(new ComboBoxModel(dinnerList));
		add(cbThuDClose);
		cbThu= new JCheckBox();
		cbThu.setBackground(new Color(209,222,235));
		cbThu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbThu.isSelected())
				{
					cbThuLOpen.setSelectedIndex(0);
					cbThuLClose.setSelectedIndex(0);
					cbThuDOpen.setSelectedIndex(0);
					cbThuDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		add(cbThu);
		
		JLabel lblFri = new JLabel("Freitag");
		add(lblFri);
		cbFriLOpen = new JComboBox();
		cbFriLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbFriLOpen.setModel(new ComboBoxModel(lunchList));
		cbFriLOpen.setBackground(Color.WHITE);
		add(cbFriLOpen);
		cbFriLClose = new JComboBox();
		cbFriLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbFriLClose.setModel(new ComboBoxModel(lunchList));
		cbFriLClose.setBackground(Color.WHITE);
		add(cbFriLClose);
		cbFriDOpen = new JComboBox();
		cbFriDOpen.setBackground(Color.WHITE);
		cbFriDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbFriDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbFriDOpen);
		cbFriDClose = new JComboBox();
		cbFriDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbFriDClose.setBackground(Color.WHITE);
		cbFriDClose.setModel(new ComboBoxModel(dinnerList));
		add(cbFriDClose);
		cbFri= new JCheckBox();
		cbFri.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbFri.isSelected())
				{
					cbFriLOpen.setSelectedIndex(0);
					cbFriLClose.setSelectedIndex(0);
					cbFriDOpen.setSelectedIndex(0);
					cbFriDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		cbFri.setBackground(new Color(209,222,235));
		add(cbFri);
		
		JLabel lblSat = new JLabel("Samstag");
		add(lblSat);
		cbSatLOpen = new JComboBox();
		cbSatLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSatLOpen.setBackground(Color.WHITE);
		cbSatLOpen.setModel(new ComboBoxModel(lunchList));
		add(cbSatLOpen);
		cbSatLClose = new JComboBox();
		cbSatLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSatLClose.setModel(new ComboBoxModel(lunchList));
		cbSatLClose.setBackground(Color.WHITE);
		add(cbSatLClose);
		cbSatLClose.setBackground(Color.WHITE);
		cbSatDOpen = new JComboBox();
		cbSatDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSatDOpen.setBackground(Color.WHITE);
		cbSatDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbSatDOpen);
		cbSatDClose = new JComboBox();
		cbSatDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSatDClose.setBackground(Color.WHITE);
		cbSatDClose.setModel(new ComboBoxModel(dinnerList));
		add(cbSatDClose);
		cbSat= new JCheckBox();
		cbSat.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbSat.isSelected())
				{
					cbSatLOpen.setSelectedIndex(0);
					cbSatLClose.setSelectedIndex(0);
					cbSatDOpen.setSelectedIndex(0);
					cbSatDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		cbSat.setBackground(new Color(209,222,235));
		add(cbSat);
	
		JLabel lblSun = new JLabel("Sontag");
		add(lblSun);
		cbSunLOpen = new JComboBox();
		cbSunLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSunLOpen.setModel(new ComboBoxModel(lunchList));
		cbSunLOpen.setBackground(Color.WHITE);
		add(cbSunLOpen);
		cbSunLClose = new JComboBox();
		cbSunLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSunLClose.setModel(new ComboBoxModel(lunchList));
		cbSunLClose.setBackground(Color.WHITE);
		add(cbSunLClose);
		cbSunDOpen = new JComboBox();
		cbSunDOpen.setBackground(Color.WHITE);
		cbSunDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSunDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbSunDOpen);
		cbSunDClose = new JComboBox();
		cbSunDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbSunDClose.setBackground(Color.WHITE);
		cbSunDClose.setModel(new ComboBoxModel(dinnerList));
		add(cbSunDClose);
		cbSun= new JCheckBox();
		cbSun.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbSun.isSelected())
				{
					cbSunLOpen.setSelectedIndex(0);
					cbSunLClose.setSelectedIndex(0);
					cbSunDOpen.setSelectedIndex(0);
					cbSunDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		cbSun.setBackground(new Color(209,222,235));
		add(cbSun);
	
		JLabel lblHol = new JLabel("Ruhigtag");
		add(lblHol);
		cbHolLOpen = new JComboBox();
		cbHolLOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbHolLOpen.setBackground(Color.WHITE);
		cbHolLOpen.setModel(new ComboBoxModel(lunchList));
		add(cbHolLOpen);
		cbHolLClose = new JComboBox();
		cbHolLClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbHolLClose.setModel(new ComboBoxModel(lunchList));
		cbHolLClose.setBackground(Color.WHITE);
		add(cbHolLClose);
		cbHolDOpen = new JComboBox();
		cbHolDOpen.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbHolDOpen.setBackground(Color.WHITE);
		cbHolDOpen.setModel(new ComboBoxModel(dinnerList));
		add(cbHolDOpen);
		cbHolDClose = new JComboBox();
		cbHolDClose.setFont(new Font("Times New Roman", Font.BOLD, 20));
		cbHolDClose.setBackground(Color.WHITE);
		cbHolDClose.setModel(new ComboBoxModel(dinnerList));
		add(cbHolDClose);
		cbHol= new JCheckBox();
		cbHol.setBackground(new Color(209,222,235));
		cbHol.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cbHol.isSelected())
				{
					cbHolLOpen.setSelectedIndex(0);
					cbHolLClose.setSelectedIndex(0);
					cbHolDOpen.setSelectedIndex(0);
					cbHolDClose.setSelectedIndex(0);
				}
			}
		  }
		);
		add(cbHol);
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
	    String query = "UPDATE config SET restaurant_lunch_open_monday='"+ cbMonLOpen.getSelectedItem() + "',restaurant_lunch_closed_monday='" + cbMonLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_monday='" + cbMonDOpen.getSelectedItem() + "',restaurant_dinner_closed_monday='" + cbMonDClose.getSelectedItem() + "',"
	       		+ "restaurant_lunch_open_tuesday='"+ cbTueLOpen.getSelectedItem() + "',restaurant_lunch_closed_tuesday='" + cbTueLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_tuesday='" + cbTueDOpen.getSelectedItem() + "',restaurant_dinner_closed_tuesday='" + cbTueDClose.getSelectedItem() + "'," 
	    		+ "restaurant_lunch_open_wednesday='"+ cbWedLOpen.getSelectedItem() + "',restaurant_lunch_closed_wednesday='" + cbWedLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_wednesday='" + cbWedDOpen.getSelectedItem() + "',restaurant_dinner_closed_tuesday='" + cbWedDClose.getSelectedItem() + "',"
	       		+ "restaurant_lunch_open_thursday='"+ cbThuLOpen.getSelectedItem() + "',restaurant_lunch_closed_thursday='" + cbThuLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_thursday='" + cbThuDOpen.getSelectedItem() + "',restaurant_dinner_closed_thursday='" + cbThuDClose.getSelectedItem() + "',"
	       		+ "restaurant_lunch_open_friday='"+ cbFriLOpen.getSelectedItem() + "',restaurant_lunch_closed_friday='" + cbFriLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_friday='" + cbFriDOpen.getSelectedItem() + "',restaurant_dinner_closed_friday='" + cbFriDClose.getSelectedItem() + "',"
	       		+ "restaurant_lunch_open_saturday='"+ cbSatLOpen.getSelectedItem() + "',restaurant_lunch_closed_saturday='" + cbSatLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_saturday='" + cbSatDOpen.getSelectedItem() + "',restaurant_dinner_closed_saturday='" + cbSatDClose.getSelectedItem() + "',"
	       		+ "restaurant_lunch_open_sunday='"+ cbSunLOpen.getSelectedItem() + "',restaurant_lunch_closed_sunday='" + cbSunLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_sunday='" + cbSunDOpen.getSelectedItem() + "',restaurant_dinner_closed_sunday='" + cbSunDClose.getSelectedItem() + "',"
	       		+ "restaurant_lunch_open_holiday='"+ cbHolLOpen.getSelectedItem() + "',restaurant_lunch_closed_holiday='" + cbHolLClose.getSelectedItem()
	       		+ "',restaurant_dinner_open_holiday='" + cbHolDOpen.getSelectedItem() + "',restaurant_dinner_closed_holiday='" + cbHolDClose.getSelectedItem() 
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
	       	cbMonLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_monday"));
	       	cbMonLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_monday"));
	       	cbMonDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_monday"));
	       	cbMonDClose.setSelectedItem(rs.getString("restaurant_dinner_open_monday"));
	       	
	       	cbTueLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_tuesday"));
	       	cbTueLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_tuesday"));
	       	cbTueDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_tuesday"));
	       	cbTueDClose.setSelectedItem(rs.getString("restaurant_dinner_open_tuesday"));
	       	
	       	cbWedLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_wednesday"));
	       	cbWedLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_wednesday"));
	       	cbWedDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_wednesday"));
	       	cbWedDClose.setSelectedItem(rs.getString("restaurant_dinner_open_wednesday"));
	       	
	       	cbThuLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_thursday"));
	       	cbThuLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_thursday"));
	       	cbThuDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_thursday"));
	       	cbThuDClose.setSelectedItem(rs.getString("restaurant_dinner_open_thursday"));
	       	
	       	cbFriLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_friday"));
	       	cbFriLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_friday"));
	       	cbFriDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_friday"));
	       	cbFriDClose.setSelectedItem(rs.getString("restaurant_dinner_open_friday"));
	       	
	       	cbSatLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_saturday"));
	       	cbSatLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_saturday"));
	       	cbSatDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_saturday"));
	       	cbSatDClose.setSelectedItem(rs.getString("restaurant_dinner_open_saturday"));
	       	
	       	cbSunLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_sunday"));
	       	cbSunLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_sunday"));
	       	cbSunDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_sunday"));
	       	cbSunDClose.setSelectedItem(rs.getString("restaurant_dinner_open_sunday"));
	       	
	       	cbHolLOpen.setSelectedItem(rs.getString("restaurant_lunch_open_holiday"));
	       	cbHolLClose.setSelectedItem(rs.getString("restaurant_lunch_closed_holiday"));
	       	cbHolDOpen.setSelectedItem(rs.getString("restaurant_dinner_open_holiday"));
	       	cbHolDClose.setSelectedItem(rs.getString("restaurant_dinner_open_holiday"));
	       	
	       	
	       	
	       	
	    }
	    preparedStmt.close();
	    conn.close();
	    
	    setInitialized(true);
	}
	
	@Override
	public String getName() {
		return "Tages Plan";
	}

}
