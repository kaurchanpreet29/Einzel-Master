package com.floreantpos.ui;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.floreantpos.model.Street;
import com.floreantpos.model.StreetDB;




public class DBAccess
{
	List<Street> streetList;
    public static void main(String[] args) throws ClassNotFoundException, SQLException
    {
       DBAccess access = new DBAccess();
       //access.generateDb();
       StreetDB streetDb = new StreetDB();
       streetDb.setStreet(access.getStreetList());
       StreetDB.save(streetDb);
    }
    
    public void generateDb()
    {
    	try{
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection conn=DriverManager.getConnection("jdbc:ucanaccess://c:/PLZ.mdb;memory=false");
        Statement s = conn.createStatement();
       
        String delTable = "Delete * FROM StreetDB";
        s.execute(delTable);
        
        String selTable = "SELECT * FROM Adressen WHERE Postleitzahl >= 65900 AND Postleitzahl <= 65999";
        s.execute(selTable);
        ResultSet rs = s.getResultSet();
       
        int count = 0;
        while((rs!=null) && (rs.next()))
        {
           int plz =  Integer.parseInt(rs.getString(5));
            
            String tableName = "Strassen";
            // Fetch table
            String selTable_str = "SELECT * FROM " + tableName + " WHERE ID = "+ rs.getString(2);
            s.execute(selTable_str);
            
            ResultSet rs_str = s.getResultSet();
            String strasse = "";
            while((rs_str!=null) && (rs_str.next()))
            {
            	strasse = rs_str.getString(5);
            	break;
            }
            
            String selTable_orteile = "SELECT * FROM Ortteile WHERE ID = "+ rs.getString(4);
            s.execute(selTable_orteile);
            
            ResultSet rs_ortteile = s.getResultSet();
            String bezirk = ""; 
            while((rs_ortteile!=null) && (rs_ortteile.next()))
            {
            	bezirk = rs_ortteile.getString(2);
            	break;
            }
            
            String selTable_ort = "SELECT * FROM Orte WHERE ID = "+ rs.getString(3);
            s.execute(selTable_ort);
            
            ResultSet rs_ort = s.getResultSet();
            String ort = "";
            while((rs_ort!=null) && (rs_ort.next()))
            {
            	ort =  rs_ort.getString(2);
            	break;
            }
            
            String insTable = "INSERT INTO StreetDB (PLZ,STR,BEZERK,ORT) VALUES (" + plz + ",'"+ strasse + 
            		"','"+ bezirk + "','"+ ort +"')";
            s.execute(insTable);
            
            
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            count++;
        }
        
        System.out.println("Total Count: "+ count);
        
        s.close();
        conn.close();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    public List<Street> getStreetList() throws ClassNotFoundException, SQLException
    {
    	if(streetList != null) return streetList;
    	
    	streetList = new ArrayList();
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection conn=DriverManager.getConnection("jdbc:ucanaccess://c:/PLZ.mdb;memory=false");
        Statement s = conn.createStatement();
        String selTable = "SELECT * FROM StreetDB";
        s.execute(selTable);
        ResultSet rs = s.getResultSet();
        while((rs!=null) && (rs.next()))
        {
        	Street street = new Street();
        	street.setPlz(rs.getString(1));
        	street.setName(rs.getString(2));
        	street.setBezirk(rs.getString(3));
        	street.setOrt(rs.getString(4));
        	streetList.add(street);
        }
        return streetList;
    }
    
    
    
}