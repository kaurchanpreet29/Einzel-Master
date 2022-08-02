package com.floreantpos.ui.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class UiUtil {
	public static JXDatePicker getCurrentMonthStart() {
		Locale locale = Locale.getDefault();
		
		Calendar c = Calendar.getInstance(locale);
		c.set(Calendar.DAY_OF_MONTH, 1);
		JXDatePicker datePicker = new JXDatePicker(c.getTime(), locale);
		
		return datePicker;
	}
	
	
	
	public static String  getAnydesk() throws IOException {
		BufferedReader br = null;
		String id = "";
		try {
    	File myFile1 = new File("C:\\ProgramData\\AnyDesk");
    	String contents[] = myFile1.list();
    	File myFile = new File("C:\\ProgramData\\AnyDesk\\"+contents[0]+"\\system.conf");
		try {
			br = new BufferedReader(new FileReader(myFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	try {
    	    StringBuilder sb = new StringBuilder();
    	    String line = br.readLine();

    	    while (line != null) {
    	    	if(line.contains("ad.anynet.id")) {
    	    		id = line.substring(line.indexOf("=")+1, line.length());
    	    	}    	    		
    	    	sb.append(line);
    	        sb.append(System.lineSeparator());
    	        line = br.readLine();
    	       
    	    }
    	} finally {
    	    br.close();
    	}
		}catch(Exception ex) {
			
		}
    	return id;
	}
	

	public static JXDatePicker getCurrentMonthEnd() {
		Locale locale = Locale.getDefault();
		
		Calendar c = Calendar.getInstance(locale);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		JXDatePicker datePicker = new JXDatePicker(c.getTime(), locale);
		
		return datePicker;
	}
}
