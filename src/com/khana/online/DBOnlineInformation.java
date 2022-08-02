package com.khana.online;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.services.SystemServiceUtil;
import com.floreantpos.ui.util.UiUtil;
public class DBOnlineInformation implements Runnable{
	Thread t;
	String myDriver = "org.gjt.mm.mysql.Driver";
	String myUrl = "jdbc:mysql://vwp9989.webpack.hosteurope.de/db10975332-khana";
	//vwp9989.webpack.hosteurope.de/db10975332-60310";

	protected Connection conn;
	public DBOnlineInformation(){
		try {
			t = new Thread(this);
		Class.forName(myDriver);
		t.start();
		}
		catch(Exception e){e.printStackTrace();}
	}

	public void run(){
			try {
//				updateInfo();
				updateAnyDesk();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}

	
	public void updateAnyDesk() throws SQLException {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String dt = dateFormat.format(new Date());		
//			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "dbu10975332", "ch03ms23");
			Restaurant rest = RestaurantDAO.getRestaurant();
			String anydesk1 = "";
			String remoteId = rest.getRemoteId()!=null?rest.getRemoteId():"";
			if(remoteId.isEmpty()) {
				remoteId = UiUtil.getAnydesk();
				rest.setRemoteId(remoteId);
				RestaurantDAO.getInstance().saveOrUpdate(rest);
			}
			try {
				anydesk1 = rest.getRemoteId();
			}catch(Exception ex) {
				
			}
			
			if(anydesk1.isEmpty())
				anydesk1 =UiUtil.getAnydesk();
		
			String stuerid = rest.getTicketFooterMessage2().trim();			
			String street = rest.getAddressLine1().trim();
			String zipcode = rest.getZipCode().trim();
			String stadt = rest.getAddressLine3().trim();
			String terminal1=("EZ_"+SystemServiceUtil.getMyService().getProcessorSerialNumber());		
			String kunden1=rest.getName();
			if(kunden1.contains("'"))
				kunden1 = kunden1.replace("'", "''");
			if(street.contains("'"))
				street = street.replace("'", "''");
			if(stadt.contains("'"))
				stadt = stadt.replace("'", "''");

			String updateMessage = "Updated-Ver-"+Application.getInstance().VERSION;
			if(TerminalConfig.isBuildMode())
				updateMessage = "Not Updated-Ver-"+Application.getInstance().VERSION;
			
			
			int found = 0;
			String tableName = "kassen_protokoll";
			if(TerminalConfig.isBuildMode())
				tableName = "Kassen_Protokoll_copy";
			String queryCheck = "SELECT * FROM " + tableName +" where anydesk='"+ anydesk1 + "'";
			PreparedStatement preparedStmtCheck = conn.prepareStatement(queryCheck);
			ResultSet rs = preparedStmtCheck.executeQuery();
			while(rs.next())
			{
				found = 1;
				break;
			}
             
			if (found == 0) {	
				String query = "INSERT INTO " + tableName + " (anydesk, terminal, steuerid, kunden, note, street, zipcode, stadt, datum, foodbeeId) "
						+ "VALUES("+ anydesk1 + ",'"
						+ terminal1 + "','" 
						+ stuerid  + "','"
						+ kunden1  + "','"
						+ updateMessage  + "','"
						+ street  + "','"
						+ zipcode  + "','"
						+ stadt  + "','"
						+ dt  + "','"
						+ 0 + "')";

					Statement stmt = conn.createStatement();
					stmt.execute(query);
					System.out.println("Addded");
				
			} else {
				String query = "UPDATE " + tableName +" set anydesk='"+ anydesk1 + "',terminal='"+ terminal1
						+ "',steuerid='"+ stuerid+ "',foodbeeId='0',kunden='"+ kunden1 + "',street='"+ street+ "',zipcode='"+ zipcode+ "',stadt='"+ stadt+"' WHERE anydesk='"+ anydesk1 +"'";
				
					Statement stmt = conn.createStatement();
					stmt.execute(query);
					System.out.println("Updated");
			}

		} catch(Exception e) {
			e.printStackTrace();		
			
		}
		finally{
			conn.close();   
		}
	}
}
