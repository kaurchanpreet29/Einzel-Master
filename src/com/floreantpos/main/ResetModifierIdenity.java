package com.floreantpos.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.floreantpos.config.AppConfig;
import com.floreantpos.config.TerminalConfig;


public class ResetModifierIdenity {
	int i = 0;
//	String dbURL = AppConfig.getConnectString();
	String dbURL = "jdbc:derby:database/derby-single/posdb";

	
	
//	if(te)
	String user = "app";
	String password = "sa";
	public void reset() {
		try {
			//				DatabaseConfigurationDialog.show(Application.getPosWindow());
			if(TerminalConfig.isKhanaServer())
				dbURL = AppConfig.getConnectString();
			resetDb();
		} catch (SQLException | InterruptedException e) { 

			e.printStackTrace();
		}
	}


	public void resetDb() throws SQLException, InterruptedException {
		Connection conn = null;
		Statement statement = null;
		try {
			System.out.println("Updating table column");
			conn = DriverManager.getConnection(dbURL, user, password);
			statement = conn.createStatement();
			try {
				statement
				.execute("ALTER TABLE MENU_ITEM ALTER COLUMN NAME SET DATA TYPE VARCHAR(300)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE MENU_ITEM ALTER COLUMN ITEMID SET DATA TYPE VARCHAR(40)");		
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE MENU_CATEGORY ALTER COLUMN NAME SET DATA TYPE VARCHAR(60)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE MENU_GROUP ALTER COLUMN NAME SET DATA TYPE VARCHAR(60)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE TICKET_ITEM ALTER COLUMN ITEM_NAME SET DATA TYPE VARCHAR(70)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE TICKET_ITEM ALTER COLUMN GROUP_NAME SET DATA TYPE VARCHAR(60)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE TICKET_ITEM ALTER COLUMN CATEGORY_NAME SET DATA TYPE VARCHAR(60)");
			}catch(Exception exx) {

			}
			
			
			try {
				statement
				.execute("ALTER TABLE CUSTOMER ALTER COLUMN CITY SET DATA TYPE VARCHAR(120)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE CUSTOMER ALTER COLUMN FIRMNAME SET DATA TYPE VARCHAR(120)");
			}catch(Exception exx) {

			}
			try {
				statement
				.execute("ALTER TABLE CUSTOMER ALTER COLUMN BEZERK SET DATA TYPE VARCHAR(120)");
			}catch(Exception exx) {

			}


			//			statement
			//			.execute("ALTER TABLE RESTAURANT ALTER COLUMN ADDRESS_LINE1 SET DATA TYPE VARCHAR(60)");


			/*String dbURLServer = "jdbc:derby:database/derby-server/posdb";
				conn = DriverManager.getConnection(dbURLServer, user, password);
				statement = conn.createStatement();
				System.out.println("Updating table column");				
				statement
				.execute("ALTER TABLE TICKET_ITEM ALTER COLUMN GROUP_NAME SET DATA TYPE VARCHAR(60)");
				statement
				.execute("ALTER TABLE TICKET_ITEM ALTER COLUMN CATEGORY_NAME SET DATA TYPE VARCHAR(60)");
				statement
				.execute("ALTER TABLE TICKET_ITEM ALTER COLUMN STORNO_REASON SET DATA TYPE VARCHAR(60)");
			 */
			if(statement != null) {
				statement.close();
			}
			if(conn != null) {
				conn.close();
				System.out.println("Updating table column finished");

			}				


		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(statement != null) {
				statement.close();
			}
			if(conn != null) {
				conn.close();
			}
		}


	}

}
