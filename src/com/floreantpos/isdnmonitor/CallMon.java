package com.floreantpos.isdnmonitor;

import org.apache.commons.lang.StringUtils;
import org.capi.capi20.*;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.customer.CustomerListTableModel;
import com.floreantpos.model.CallList;
import com.floreantpos.model.Customer;
import com.floreantpos.model.dao.CallListDAO;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.ui.dialog.CallerDialogMain;
import com.floreantpos.ui.dialog.POSMessageDialog;

import net.sourceforge.jcapi.*;
import net.sourceforge.jcapi.util.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JOptionPane;
public class CallMon implements CapiListener, ActionListener 
{
	private static final boolean Iterator = false;
	static int i = 1;
	public CallMon()
	{
		run();
	}
	
	public void deleteAll()
	{
		List <CallList> callList = CallListDAO.getInstance().findAll();
		for (CallList call: callList)
		{
			CallListDAO.getInstance().delete(call);
		}
	}
	/*public void findandDeletePhone(String phone)
	{
		CallList deleteCaller = null;
		for(Iterator<CallList> itr = CallListDAO.getInstance().findAll().iterator();itr.hasNext();)
		{
			CallList obj = itr.next();
			if(obj.getPhone().compareTo(phone) == 0)
			{
				deleteCaller = obj;
				break;
			}
			
		}
		if(deleteCaller != null)
			CallListDAO.getInstance().delete(deleteCaller);
		
	}*/
	public void addCall(String calling)
	{
		CallList caller = null;
		CallList deleteCaller = null;
		String currentDate = Calendar.getInstance().getTime().toString();
		int dateIndex = currentDate.indexOf('C');
		currentDate = currentDate.substring(0, dateIndex);
		int found = 0;
		for(Iterator<CallList> itr = CallListDAO.getInstance().findAll().iterator();itr.hasNext();)
		{
			CallList obj = itr.next();
			if(obj.getPhone().compareTo(calling) == 0)
			{
				deleteCaller = obj;
				int number = obj.getNumber()+1;
				caller = new CallList();
				caller.setPhone(calling);
				caller.setTime(currentDate);
				caller.setNumber(number);
				found = 1;
				break;
			}
		}
		if(found == 1)
		{
			if(deleteCaller != null)
				CallListDAO.getInstance().delete(deleteCaller);
		}
		
		if(caller != null)
		{
			CallListDAO.getInstance().saveOrUpdate(caller);
		}
		else
		{
			caller = new CallList();
			caller.setPhone(calling);
			caller.setTime(currentDate);
			caller.setNumber(1);
			CallListDAO.getInstance().saveOrUpdate(caller);
		}
	}
	
	public void capiEventRaised()
	{
		try
		{
			byte[] conf = null;
			JcapiMessage m = (JcapiMessage)capi.getMessage(appID),req = null, resp = null;;
			dump(false,m.getBytes());
			int mid = m.getMessageID();
			Object plci = null;
			Object ncci = null;
			
			switch (m.getType())
			{
				case JcapiMessage.CONNECT_IND:
					plci = m.getValue("PLCI");
					if (!stopped)
					{
						byte[] cdn = m.getStructValue("Called party number");
						byte[] cln = m.getStructValue("Calling party number");
						String called = "";
						if (cdn.length>1)
							called = new String(ByteArray.getBytes(cdn,1,cdn.length-1));
						String calling = "unknown";
						if (cln.length>2)
						{
							calling = new String(ByteArray.getBytes(cln,2,cln.length-2));
							
					       	try {
								SoundUtils.tone(1000,100);
							} catch (LineUnavailableException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    	
					    	 String phone = calling;
					    	 String name ="";
					    	 String address1 = "";
					    	 String address2 = "";
					    	 int found = 0;
					    	 if (!(StringUtils.isEmpty(phone)))
					    	 {
					    	      List<Customer> list = CustomerDAO.getInstance().findBy(phone, null, null);
					    	      for(Iterator<Customer> itr = list.iterator();itr.hasNext();)
					    	      {
					    	    	 Customer customer = itr.next();
					    	    	 if((customer != null) && (customer.getName() != null))
					    	    	 {
					    	    		 name = customer.getName();
					    	    		 address1 = customer.getAddress() + " " + customer.getDoorNo();
					    	    		 address2 = customer.getZipCode() + "," + customer.getCity();
					    	    		 found = 1;
					    	    		 break;
					    	    	 }
					    	      }
					    	 
					    	 }
					    	 if ((found != 1) && (!(StringUtils.isEmpty(phone))))
					    	 {
					    	      List<Customer> list = CustomerDAO.getInstance().findBy2(phone, null, null);
					    	      for(Iterator<Customer> itr = list.iterator();itr.hasNext();)
					    	      {
					    	    	 Customer customer = itr.next();
					    	    	 if((customer != null) && (customer.getName() != null))
					    	    	 {
					    	    		 name = customer.getName();
					    	    		 address1 = customer.getAddress() + " " + customer.getDoorNo();
					    	    		 address2 = customer.getZipCode() + "," + customer.getCity();
					    	    		 break;
					    	    	 }
					    	      }
					    	 
					    	 }
					    	CallerDialogMain dialog = new CallerDialogMain(calling,name,address1,address2);
					    	addCall(calling);
						}
						else if (cln.length>1)
						{
							int pi = cln[1] & 0x60;
							if (pi == 0x20) calling = "restricted";
							if (pi == 0x40) calling = "n.a.";
						}
						byte[] bc = m.getStructValue("BC");
						String ct = "";
						if (bc.length>0)
							switch(bc[0] & 0x1f)
							{
								case 0x00 : ct="speech"; break;
								case 0x08 : ct="digital (unrestricted)"; break;
								case 0x09 : ct="digital (restricted)"; break;
								case 0x10 : ct="3.1 kHz audio"; break;
								case 0x11 : ct="7 kHz audio"; break;
								case 0x18 : ct="video"; break;
							}				
					}
					
					m = (JcapiMessage)capi.createMessage(appID,JcapiMessage.ALERT_REQ,mid);
					m.setValue("PLCI",plci);
					conf = m.getBytes();
					dump(true,conf);
					capi.putMessage(m);
					i++;
					break;
				case JcapiMessage.DISCONNECT_IND:
					plci = m.getValue("PLCI");
					short reason = m.getWordValue("Reason");
					m = (JcapiMessage)capi.createMessage(appID,JcapiMessage.DISCONNECT_RESP,mid);
					m.setValue("PLCI",plci);
					conf = m.getBytes();
					dump(true,conf);
					capi.putMessage(m);
					break;
				case JcapiMessage.INFO_IND:
					plci = m.getValue("Controller/PLCI");
					m = (JcapiMessage)capi.createMessage(appID,JcapiMessage.INFO_RESP,mid);
					m.setValue("Controller/PLCI",plci);
					conf = m.getBytes();
					dump(true,conf);
					capi.putMessage(m);
					break;
				case JcapiMessage.CONNECT_B3_IND:
					rncci = m.getValue("NCCI");
					resp = (JcapiMessage)capi.createMessage(appID,JcapiMessage.CONNECT_B3_RESP,mid);
					resp.setValue("NCCI",rncci);
					dump(true,resp.getBytes());
					
					capi.putMessage(resp);
					break;
				case JcapiMessage.CONNECT_CONF:
					splci = m.getValue("PLCI");
					int cc_info = m.getWordValue("Info");
					if (cc_info != 0 && debug)
					{
						System.out.println("no connection, cause: 0x"+Integer.toHexString(cc_info));
					}
					break;
				case JcapiMessage.CONNECT_ACTIVE_IND:
					plci = m.getValue("PLCI");
					resp = (JcapiMessage)capi.createMessage(appID,JcapiMessage.CONNECT_ACTIVE_RESP,mid);
					resp.setValue("PLCI",plci);
					dump(true,resp.getBytes());
					capi.putMessage(resp);
					if (plci.equals(splci))
					{
						req = (JcapiMessage)capi.createMessage(appID,JcapiMessage.CONNECT_B3_REQ,mn++);
						req.setValue("PLCI",plci);
						dump(true,req.getBytes());
						capi.putMessage(req);
					}
					break;
				case JcapiMessage.CONNECT_B3_CONF:
					sncci = m.getValue("NCCI");
					int cbc_info = m.getWordValue("Info");
					if (cbc_info != 0 && debug)
					{
						System.out.println("no B3 connection, cause: 0x"+Integer.toHexString(cbc_info));
					}
					break;
				case JcapiMessage.CONNECT_B3_ACTIVE_IND:
					ncci = m.getValue("NCCI");
					resp = (JcapiMessage)capi.createMessage(appID,JcapiMessage.CONNECT_B3_ACTIVE_RESP,mid);
					resp.setValue("NCCI",ncci);
					dump(true,resp.getBytes());
					capi.putMessage(resp);
					if (ncci.equals(rncci))
					{
						rca = true;
					}
					else
					{
						sca = true;
					}
					if (rca && sca)
					{
						req = (JcapiMessage)capi.createMessage(appID,JcapiMessage.DATA_B3_REQ,mn++);
						req.setB3Data(tdata);
						req.setValue("NCCI",sncci);
						req.setWordValue("Data handle",0);
						dump(true,req.getBytes());
						capi.putMessage(req);
						if (debug)
							System.out.println("Data sent");
					}
					break;
				case JcapiMessage.DISCONNECT_B3_IND:
					ncci = m.getValue("NCCI");
					resp = (JcapiMessage)capi.createMessage(appID,JcapiMessage.DISCONNECT_B3_RESP,mid);
					resp.setValue("NCCI",ncci);
					dump(true,resp.getBytes());
					capi.putMessage(resp);
					if (ncci.equals(rncci))
					{
						rca = false;
					}
					else
					{
						sca = false;
					}
					if (!rca && !sca)
					{
						req = (JcapiMessage)capi.createMessage(appID,JcapiMessage.DISCONNECT_REQ,mn++);
						req.setValue("PLCI",splci);
						dump(true,req.getBytes());
						capi.putMessage(req);
					}
					break;
				case JcapiMessage.DISCONNECT_B3_CONF:
					break;
				case JcapiMessage.DISCONNECT_CONF:
					plci = m.getValue("PLCI");
					int dc_info = m.getWordValue("Info");
					if (dc_info != 0 && debug)
					{
						System.out.println("error disconnecting, cause: 0x"+Integer.toHexString(dc_info));
					}
					break;
				case JcapiMessage.DATA_B3_CONF:
					sncci = m.getValue("NCCI");
					int dbc_info = m.getWordValue("Info");
					if (dbc_info != 0 && debug)
					{
						System.out.println("error sending data: 0x"+Integer.toHexString(dbc_info));
					}
					break;
				case JcapiMessage.DATA_B3_IND:
					ncci = m.getValue("NCCI");
					handle = m.getWordValue("Data handle");
					/*
					data = m.getDwordValue("Data");
					dlen = m.getWordValue("Data length");
					*/
					if (debug)
					{
						System.out.println("Data received: "+ByteArray.hexString(m.getB3Data()," "));
					}
					resp = (JcapiMessage)capi.createMessage(appID,JcapiMessage.DATA_B3_RESP,mid);
					resp.setValue("NCCI",rncci);
					resp.setWordValue("Data handle",handle);
					dump(true,resp.getBytes());
					capi.putMessage(resp);

					req = (JcapiMessage)capi.createMessage(appID,JcapiMessage.DISCONNECT_B3_REQ,mn++);
					req.setValue("NCCI",ncci);
					dump(true,req.getBytes());
					capi.putMessage(req);
					break;
			}
		} catch(CapiException e)
		{
			if (debug) System.out.println(e.getMessage());
		}
	}

	public void capiExceptionThrown(CapiException e)
	{
		e.printStackTrace();
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		
	}

	public void dispose()
	{
		try
		{
			capi.removeListener(appID,this);
			capi.release(appID);
		} catch(CapiException e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private static void dump(boolean appl, byte[] msg)
	{
		if (debug) System.out.println((appl?" Appl: ":" CAPI: ")+ByteArray.hexString(msg," "));
	}
	
	private void run()
	{
		capi = new Jcapi();
		if (debug) capi.test();
		//if (debug) try {System.in.read();} catch(Exception e){}
		
		try
		{
			// register this application
			appID = capi.register(4096,2,2,128);
			if (debug) System.out.println("application ID: "+appID);
			
			// build listen request
			JcapiMessage lreq = (JcapiMessage)capi.createMessage(appID,JcapiMessage.LISTEN_REQ,1);
			lreq.setDwordValue("controller",1);
			lreq.setDwordValue("Info mask",0x3ff);
			lreq.setDwordValue("CIP mask",1);
			byte[] lr = lreq.getBytes();
			if (debug) 
			{
				System.out.println("sending listen request...");
				System.out.flush();
			}
			
			dump(true,lr);
			
			// send listen request
			capi.putMessage(lreq);

			// wait for listen conf
			if (debug) 
			{
				System.out.println("waiting for listen confirmation...");
				System.out.flush();
			}
			JcapiMessage answer = null;
			boolean retry = true;
			do
			{
				try
				{
					if (debug) System.out.print(".");
					answer = (JcapiMessage)capi.getMessage(appID);
					retry=false;
					if (debug) System.out.println();
				} catch(CapiException e)
				{
					if (e.getCapiCode() != 0x1104)	// "queue is empty"
						throw e;
				}
			} while (retry);
			
			// read message
			if (answer.getType() != JcapiMessage.LISTEN_CONF)
				throw new CapiException("unexpected response to listen request: 0x"+Integer.toHexString(answer.getType()));
			dump(false,answer.getBytes());
			int info = answer.getWordValue("Info");
			if (info != 0)
				throw new CapiException("listen request error: 0x"+Integer.toHexString(info));
			
			capi.addListener(appID,this);
		} catch(Exception e)
		{
			e.printStackTrace();
			try
			{
				capi.release(appID);
			} catch(CapiException jce)
			{}
			POSMessageDialog.showError("Bitte ueberpruefung Sie Ihre ISDN Karte");
			TerminalConfig.setIsdn(false);
			System.exit(0);
		}
	}
	
	/*public static void main(String[] argv)
	{
		CallMon monitor = new CallMon();
	}*/
	static int handle = 0, data = 0, dlen = 0;
	static Jcapi capi = null;
	static int appID = 0, mn = 0x80 ;
	static boolean debug=true, rca = false, sca = false;
	static byte[] tdata = { 1, 2, 3, 4, 5, 7, 8, 9, 10, 11, 12, 13, 14, (byte) -1};
	//static Vector plcis = new Vector(MAX_CON);
	DateFormat df = null;
	private boolean stopped = false;
	Object rplci = null, splci = null, rncci = null, sncci = null;
}


