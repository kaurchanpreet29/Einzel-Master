package com.floreantpos.add.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.ui.views.payment.KalaResponse;

import antlr.collections.List;

public class EasyPayment {
	static String status = null;
	 static String KatenType=null;
	 String scOutput = null;
	 String check=null;
	 int count=0;
	 static String zvt = TerminalConfig.getZvtCardPayment();		
	
	 String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
	 public static String checkService() throws InterruptedException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {		  
		 
		 String scOutput = null;
		 String check=null;
		 int count=0;
		/* String serviceName;
		 Double amount;
		 System.out.println("Amount= "+amount);
		 String newValue = new Double(amount).toString();
		 String value = newValue.replace(".", "");
		 System.out.println("Converted Amount= "+value);
		 System.out.println("location of zvt \n"+zvt);
		 if(value.length()==4) {
			 value=value.concat("0");
		 }else if(value.length()==3) {
			 value=value.concat("0");
		 }else if(value.length()==2){
			 value=value.concat("0");
		 }else if(value.length()==1) {
			 value=value.concat("00");
		 }else {
			 value=value;
		 }
		 String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		 String test = "C:\\Users\\office02\\Documents\\GitHub\\KhanaRestaurant\\EasyZVT.exe KasseNr="+timeStamp+" Dialog=0 COM=LAN IP=192.168.0.11 Port=5577 Protokollpfad=C:\\Users\\office02\\Documents\\GitHub\\KhanaRestaurant\\protocol Ausgabepfad=C:\\Users\\office02\\Documents\\GitHub\\KhanaRestaurant\\result Betrag="+value;
		 //cmdTester(test);
*/		while(scOutput==null||scOutput.contains("EasyZVT.exe")) {  
		try {		  
			scOutput = null;
			String line;			
			Process p = Runtime.getRuntime().exec
		    	    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
		    BufferedReader input =
		            new BufferedReader(new InputStreamReader(p.getInputStream()));
		    while ((line = input.readLine()) != null) {
		        scOutput +=  line + "\n" ;

		    }
            if(scOutput.contains("EasyZVT.exe")){
            	count=1;
            }
            check = RegReader.readString (
            		RegReader.HKEY_CURRENT_USER, "Software\\GUB\\ZVT", "ErgebnisText");
//    			    System.out.println("user name\n " + System.getProperty("user.name"));    			       			    
    			   
            Thread.sleep(1000);
		  } catch (IOException e) {
		    e.printStackTrace();
		  } 
		}
				
		
		if (count==1&&check.contains("Zahlung")&&check.contains("erfolgt")) {
			System.out.println("Payement is succesfull");
			status = "SP";
		} else if(count==1){
			System.out.println("Payement is unsuccesfull please try with other card");
			status = "USP";

		} else {
			System.out.println("The EasyZVT is not used yet");
		}
		return status;
	}
	
	public static void zvtExecute(int amount) throws IOException {
		String zvt =TerminalConfig.getZvtCardPayment()+amount;
		Runtime rt = Runtime.getRuntime();
		rt.exec(zvt);
		}
	public static String cardType() {
		try {
		KatenType = RegReader.readString (
	    		RegReader.HKEY_CURRENT_USER,                             //HKEY
 			   "Software\\GUB\\ZVT",           //Key
 			   "KartentypLang");
	    //System.out.println("Type of Card\n " + KatenType);
		}catch(Exception e) {
			System.out.println(e);

		}
	    
		return KatenType;
	}
	}
