package com.floreantpos.add.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.SalesReport;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.DateTimeProvideUtil;
import com.floreantpos.util.NumberUtil;

public class FinazPrufung {
	private static FinazPrufung fPrufung;
	public static FinazPrufung getfPrufung() {
		if(fPrufung==null)
			return new FinazPrufung();
		else
			return fPrufung;
	}

	public FinazPrufung() {

	}

	static File[] oldListRoot = File.listRoots();
	static String drive = "L:/";

	public static String getDrive() {
		return drive;
	}

	public static void setDrive(String drive) {
		FinazPrufung.drive = drive;
	}

	//	public static void waitForNotifying() {
	//		Thread t = new Thread(new Runnable() {
	//			public void run() {
	//				while (true) {
	//					try {
	//						Thread.sleep(100);
	//					} catch (InterruptedException e) {
	//						e.printStackTrace();
	//					}
	//					if (File.listRoots().length > oldListRoot.length) {
	//						// System.out.println("new drive detected");
	//						oldListRoot = File.listRoots();
	//						System.out.println("drive" + oldListRoot[oldListRoot.length-2] + " detected");
	//						String drive = oldListRoot[oldListRoot.length-2].toString();
	//						// drive.replace("\\", "/");
	//						System.out.println("new drive detected" + drive);
	//
	//						setDrive(drive);
	//
	//					} else if (File.listRoots().length < oldListRoot.length) {
	//						System.out.println(oldListRoot[oldListRoot.length - 1] + " drive removed");
	//
	//						oldListRoot = File.listRoots();
	//
	//					}
	//
	//				}
	//			}
	//		});
	//		t.start();
	//	}

	public File getFileDirectory() {
		SimpleDateFormat df1 = new SimpleDateFormat("dd_MM_yyyy");

		try {
			directory = new File(getDrive());
			String mainDirectory = df1.format(new Date());
			directory = new File(directory + "/gdpdu/" + mainDirectory);
			directory.mkdirs();
			FileWriter writer = new FileWriter(directory.toString() + "/Rechnungen.csv");
		}catch(Exception ex) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("Bitte Waehlen Sie Ordner ein");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.showOpenDialog(null);
			directory = chooser.getSelectedFile();						     

			if (!directory.exists()) {
				directory.mkdirs();
			}
			String mainDirectory = df1.format(new Date());
			directory = new File(directory + "/gdpdu/" + mainDirectory);

			if (!directory.exists()) {
				directory.mkdirs();  
			}
		}

		return directory;
	}

	public File directory;

	public void showGdpdu() throws IOException, ClassNotFoundException, SQLException {
		directory = getFileDirectory();
		boolean failed = false;
		
		/*Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
	       
	      String URL = "jdbc:derby:database/derby-single/posdb;create=true";
	      Connection conn1 = DriverManager.getConnection(URL);
	      
		  Statement stmt = conn1.createStatement();
	      Statement stmt1 = conn1.createStatement();
	      
	      int uniqId=1;
	      ResultSet getTableId = stmt.executeQuery("SELECT * FROM TICKET ORDER BY ID ASC");
	       
	      while (getTableId.next()) {	    	
			 String sql_ = "UPDATE TICKET SET GDPDUID ="+uniqId+" WHERE ID="+getTableId.getInt("ID");
	   	     System.out.println("uniqId::"+uniqId);
	   	     stmt1.executeUpdate(sql_); 
	   	  uniqId++;
	      } */
		if (!GdpduTicket(directory.getPath())) {
			failed = true;
			System.out.println("failed1");
		}
		
		if (!failed && !GdpduTicketItem(directory.getPath())) {
			failed = true;
			System.out.println("failed2");
		}

		if (!failed && !GdpduStornoTicket(directory.getPath())) {
			failed = true;
			System.out.println("failed3");
		}

		if (!failed && !GdpduSalesReport(directory.getPath())) {
			failed = true;
			System.out.println("failed4");
		}
		
		if (!failed && !GdpduMenuItem(directory.getPath())) {
			failed = true;
			System.out.println("failed5");
		}

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");			
			ModifyIndexXml xml = new ModifyIndexXml();
			xml.init(directory.getPath(), formatter.format(startDate), formatter.format(endDate));
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		if (!failed) {
			AnimationLoader.getLoader().CloseBusiDialog();
			JOptionPane.showMessageDialog(null, "Erfolgt");
		} else {
			AnimationLoader.getLoader().CloseBusiDialog();
			JOptionPane.showMessageDialog(null, "Nicht Erfolgt");
		} 	
		return;
	}

	public boolean GdpduSalesReport(String dir) throws IOException {
		System.out.println("GdpduSalesReport start ");
		FileWriter writer = new FileWriter(dir + "/Tagesbericht.csv");
		writer.append("Id");
		writer.append(';');
		writer.append("von");
		writer.append(';');
		writer.append("bis");
		writer.append(';');
		writer.append("Datum");
		writer.append(';');
		writer.append("Speisen+Getraenke");
		writer.append(';');
		writer.append("Speisen+Getraenke Mwst.");
		writer.append(';');
		writer.append("Umsatz Gesamt");
		writer.append(';');
		writer.append("Mwst 19%");
		writer.append(';');
		writer.append("Mwst 7%");
		writer.append(';');
		writer.append("Bar bezahlt");
		writer.append(';');
		writer.append("Bar bezahlt Mwst.");
		writer.append(';');
		writer.append("EC karte");
		writer.append(';');
		writer.append("EC karte Mwst.");
		writer.append(';');
		writer.append("Online");
		writer.append(';');
		writer.append("Online Mwst.");
		writer.append(';');
		writer.append('\n');
		List<Salesreportdb> saleslist = null;
		//List<Salesreportdb> saleslist = SalesReportDAO.getInstance().findAll();
		if (!startDateSetted)
			saleslist = SalesReportDAO.getInstance().findAll();
		else {
			//			startDate = BusinessDateUtil.startOfOfficialDay(startDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, -1);
			Date startDate1 = cal.getTime();
			//			startDate = BusinessDateUtil.endOfOfficialDay(startDate);
			System.out.println("sALESrEPORT From: " + startDate1 + " To: " + endDate);
			saleslist = SalesReportDAO.getInstance().findAllSaleReport(startDate1, endDate);
			  try {
				  saleslist.sort((p1, p2) -> p1.getSalesid().compareTo(p2.getSalesid()));
			  } catch(Exception ex) {

			  }		
			}

		for (Salesreportdb salesreport : saleslist) {
			Double totalfooddrinks = salesreport.getFood() + salesreport.getBeverage();
			Double totalfooddrinkstax = salesreport.getFoodtax() + salesreport.getBeveragetax();
			writer.append(salesreport.getSalesid() + "");
			writer.append(';');

			SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
			String startdate = dateTimeFormatter.format(salesreport.getStartdate());
			String enddate = dateTimeFormatter.format(salesreport.getEnddate());

			writer.append(startdate);
			writer.append(';');
			writer.append(enddate);
			writer.append(';');
			writer.append(salesreport.getReporttime());
			writer.append(';');
			writer.append(NumberUtil.formatNumber(totalfooddrinks));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(totalfooddrinkstax));
			writer.append(';');

			writer.append(NumberUtil.formatNumber(salesreport.getAwt()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getAwtn()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getAwts()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCashpayment()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCashtax()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCardpayment()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getCardtax()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getOnline()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(salesreport.getOnlinetax()));
			writer.append(';');
			writer.append('\n');
		}
		writer.flush();
		writer.close();
		
		System.out.println("GdpduSalesReport Finish");
		return true;		
	}
	

	public boolean GdpduMenuItem(String dir) throws IOException {
		System.out.println("GdpduMenuItem Report started");
		FileWriter writer = new FileWriter(dir + "/Speisekarte.csv");
		writer.append("Artikel Id");
		writer.append(';');
		writer.append("Name");
		writer.append(';');
		writer.append("Preis");
		writer.append(';');
		writer.append("Steuer");
		writer.append(';');
		writer.append("Kategorie");
		writer.append(';');
		writer.append("Gruppe");
		writer.append(';');
		writer.append('\n');
		try {
			List<MenuItem> menuitems = MenuItemDAO.getInstance().findAll();
			for (MenuItem item : menuitems) {
				writer.append(item.getItemId());
				writer.append(';');
				writer.append(item.getName());
				writer.append(';');
				writer.append(NumberUtil.formatNumber(item.getPrice()));
				writer.append(';');
				writer.append(NumberUtil.formatNumber(item.getTax().getRate()));
				writer.append(';');
				MenuCategory category = null;
				MenuGroup group = item.getParent();
				if (group != null) {
					category = group.getParent();
				}
				writer.append(category != null ? category.getName() : StringUtils.EMPTY);
				writer.append(';');
				writer.append(group != null ? group.getName() : StringUtils.EMPTY);
				writer.append(';');
				writer.append('\n');
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		writer.flush();
		writer.close();
		System.out.println("GdpduMenuItem Report finished");
		return true;
	}

	private String md5(String object) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (md != null) {
			md.update(object.getBytes());
			byte[] digest = md.digest();
			return DatatypeConverter.printHexBinary(digest).toUpperCase();
		}
		return StringUtils.EMPTY;
	}

	private boolean startDateSetted = false;

	public boolean setStartDate() {
		DateTimeProvideUtil dateDialog = new DateTimeProvideUtil();
		dateDialog.pack();
		dateDialog.open();
		if (!dateDialog.isCanceled()) {
			startDate = dateDialog.getDateTime();
			endDate = dateDialog.getEndDate();
			startDateSetted = true;
		}
		return startDateSetted;
	}

	public Date startDate;
	public Date endDate;

	public boolean GdpduTicket(String dir) throws IOException {		
		System.out.println("GdpduTicket Start");
		System.out.println(dir);
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		 
		/*try {
			startDate = df.parse(TerminalConfig.getStartDate());
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError("Fehler");
			return false;
		}*/
		
		Integer index = 1;
		if (!setStartDate()) {
			try {
				startDate = df.parse(TerminalConfig.getStartDate());
				endDate = new Date();
			} catch (Exception e) {
				e.printStackTrace();
				POSMessageDialog.showError("Fehler");
				return false;
			}
		} else {
			try {
				Date startDate1 = df.parse(TerminalConfig.getStartDate());
				Date endDate1 = new Date();
				List<Ticket> ticketsAll = TicketDAO.getInstance().findGdpduTickets(startDate1, endDate1);
				List<Ticket> ticketsAfter = TicketDAO.getInstance().findGdpduTickets(startDate, endDate1);
				index = ticketsAll.size() - ticketsAfter.size();
				index = index + 1;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Session session = TicketDAO.getInstance().createNewSession();
		Transaction tx = session.beginTransaction();
		AnimationLoader.getLoader().showInsertDialog();
		startDate = BusinessDateUtil.startOfOfficialDay(startDate);
		Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());
		
		System.out.println("startDate: "+ startDate +" , endDate: "+ endDate);
		
		try {
			FileWriter writer = new FileWriter(dir + "/Rechnungen.csv");
			writer.append("Id");
			writer.append(';');
			writer.append("Kellner");
			writer.append(';');
			writer.append("Zeit_Erstellt");
			writer.append(';');
			writer.append("Type");
			writer.append(';');
			writer.append("Gesamt");
			writer.append(';');
			writer.append("Status");
			writer.append(';');
			writer.append("Hash");
			writer.append(';');
			writer.append('\n');
			List<Ticket> tickets = TicketDAO.getInstance().findGdpduTickets(startDate, endDate);

			List<Ticket> newTickets = TicketDAO.getInstance().findGdpduTicketsNoSrToday();
			if (newTickets != null && !newTickets.isEmpty()) {
				tickets.addAll(newTickets);
			}

			Collections.sort(tickets, new Ticket.ItemComparator());
			 
			for (Ticket ticket : tickets) {
				writer.append(index + "");

				System.out.println("ticket Id: "+ ticket.getId());
				ticket.setGdpduid(index);
				writer.append(';');
				User owner = ticket.getOwner();
				writer.append(owner != null ? owner.getFirstName() : StringUtils.EMPTY);
				writer.append(';');
				SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
				String createDate = ticket.getCreateDate() != null ? dt.format(ticket.getCreateDate()) : StringUtils.EMPTY;
				writer.append(createDate);
				writer.append(';');
				writer.append(ticket.getType() == TicketType.DINE_IN ? POSConstants.DINE_IN : POSConstants.HOME_DELIVERY);
				writer.append(';');
				String totalAmount = NumberUtil.formatNumber(ticket.getTotalAmount());
				writer.append(NumberUtil.formatNumber(ticket.getTotalAmount()));
				writer.append(';');
				String status = "";
				if (ticket.isPaid()) {
					if (ticket.getCashPayment() != null && ticket.getCashPayment())
						status = "Bar Bezahlt";
					else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment())
						status = "Online Bezahlt";
					else if (ticket.getCashPayment() != null && !ticket.getCashPayment())
						status = "Karte Bezahlt";
				} else {
					status = "Nicht bezahlt";
				}
				writer.append(status);
				writer.append(';');
				String hash = md5(String.valueOf(index + createDate + totalAmount));
				writer.append(hash);
				writer.append(';');
				writer.append('\n');
				session.saveOrUpdate(ticket);
				index++;
			}
			writer.flush();
			writer.close();
			tx.commit();
			session.close();
			
			System.out.println("GdpduTicket Finish");
			
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();				
			return false;
		}
		
	}			

	public static class TicketComparator implements Comparator {
		public TicketComparator() {

		}

		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof Ticket) || !(o2 instanceof Ticket))
				throw new ClassCastException();

			Ticket e1 = (Ticket) o1;
			Ticket e2 = (Ticket) o2;
			int compare = 0;
			try {
				compare = e1.getTicketid() - e2.getTicketid();
			} catch(Exception ex) {

			}
			return compare;
		}
	}

	/*public static class DeletedTicketComparator implements Comparator {
		public DeletedTicketComparator() {

		}

		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof DeletedKitchenTicket) || !(o2 instanceof DeletedKitchenTicket))
				throw new ClassCastException();
			DeletedKitchenTicket e1 = (DeletedKitchenTicket) o1;
			DeletedKitchenTicket e2 = (DeletedKitchenTicket) o2;
			int compare = 0;
			try {
				compare = e1.getTicketid() - e2.getTicketid();
			}catch(Exception ex) {

			}
			return compare;
		}
	}*/

	public static void waitForNotifying() {
		count =0;
		String[] drive_name = new String[]{ "D","E", "F", "G", "H", "I" ,"J","K", "L","M", "N"};
		//here we initialize an array for the usb that is to be inserted
		File[] usb = new File[drive_name.length];
		//if the usb is detected then it is assigned True else False
		boolean[] usb_detected = new boolean[drive_name.length];

		for ( int i = 0; i < drive_name.length; ++i )
		{
			usb[i] = new File(drive_name[i]+":/");
			usb_detected[i] = usb[i].canRead();
		}

		POSMessageDialog.showError("Bitte Usb Einstecken!!!");		
		detect(usb,drive_name,usb_detected);	
	}	

	static int count;    
	public static void detect(File[] usb,String[] drive_name,boolean[] usb_detected)
	{
		boolean found=false;

		while(!found&&count<=50)
		{
			for ( int i = 0; i < drive_name.length; ++i )
			{
				boolean if_detected;
				if_detected = usb[i].canRead();
				if ( if_detected != usb_detected[i] )
				{
					if ( if_detected )
						System.out.println("USB "+drive_name[i]+" detected ");
					setDrive(drive_name[i]+":/");
					found = true;
					break;
				}
			}
			count++;
		}
	}

	public boolean GdpduTicketItem(String dir) throws IOException {
		System.out.println("GdpduTicketItem Start");
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Restaurant restaurant = RestaurantDAO.getRestaurant();
			 
			
			/*try {
				startDate = df.parse(TerminalConfig.getStartDate());
			} catch (Exception e) {
				e.printStackTrace();
				POSMessageDialog.showError("Fehler");
				return false;
			}*/
			startDate = BusinessDateUtil.startOfOfficialDay(startDate);
			//Date endDate = BusinessDateUtil.endOfOfficialDay(new Date());
			SimpleDateFormat df1 = new SimpleDateFormat("dd_MM_yyyy");
			String str = df1.format(startDate) + "_bis_" + df.format(endDate);
			FileWriter writer = new FileWriter(dir + "/Artikel.csv");
			writer.append("Id");
			writer.append(';');
			writer.append("Name");
			writer.append(';');
			writer.append("Gruppe");
			writer.append(';');
			writer.append("Kategorie");
			writer.append(';');
			writer.append("Zeit erstellt");
			writer.append(';');
			writer.append("Steuer");
			writer.append(';');
			writer.append("Einzelpreis");
			writer.append(';');
			writer.append("Anz.");
			writer.append(';');
			writer.append("Gesamt");
			writer.append(';');
			writer.append('\n');

			List<TicketItem> ticketItems = TicketItemDAO.getInstance().findAllByDates(startDate, endDate);
			Collections.sort(ticketItems, new TicketItem.DateComparator());
			for (TicketItem ticketItem : ticketItems) {
				Ticket ticket = ticketItem.getTicket();
				if (ticket == null || ticket.getGdpduid() == 0) {
					continue;
				}
				if (ticket != null && ticket.getGdpduid() != null) {
					writer.append(ticket.getGdpduid() + "");
					writer.append(';');
				}
				writer.append(ticketItem.getName());
				writer.append(';');
				writer.append(ticketItem.getGroupName() != null ? ticketItem.getGroupName() : StringUtils.EMPTY);
				writer.append(';');
				writer.append(ticketItem.getCategoryName() != null ? ticketItem.getCategoryName() : StringUtils.EMPTY);
				writer.append(';');
				String date = null;
				/*if (ticketItem.getModifiedTime() != null) {
					SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					date = ticketItem.getCreateDate() == null
							? ticket.getCreateDate() != null ? dt.format(ticket.getCreateDate()) : null
									: dt.format(ticketItem.getCreateDate());
				}
				writer.append(date != null ? date : "");*/
				
				if (ticketItem.getModifiedTime() != null) {
					SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					date = ticket.getCreateDate() != null ? dt.format(ticket.getCreateDate()) : null;
					date = dt.format(ticket.getCreateDate());
				}
				writer.append(date != null ? date : "");
				
				writer.append(';');

				writer.append(NumberUtil.formatNumber(ticketItem.getTaxRate()));
				writer.append(';');
				writer.append(NumberUtil.formatNumber(ticketItem.getUnitPrice()));
				writer.append(';');
				writer.append(ticketItem.getItemCount() + "");
				writer.append(';');
				writer.append(NumberUtil.formatNumber(ticketItem.getTotalAmount()));
				writer.append(';');
				writer.append('\n');
			}
			writer.flush();
			writer.close();
			System.out.println("GdpduTicketItem Finish");
		} catch (Exception xpxw) {
			xpxw.printStackTrace();
			return false;
		}
		return true;
	}

	List<Ticket> ticketsAll;

	public List<Ticket> getTicketsAll() {
		return ticketsAll;
	}

	public void setTicketsAll(List<Ticket> ticketsAll) {
		this.ticketsAll = ticketsAll;
	}

	public boolean GdpduStornoTicket(String dir) throws IOException {
	/*	System.out.println("GdpduStornoTicket Report started");
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		try {
			SimpleDateFormat df1 = new SimpleDateFormat("dd_MM_yyyy");
			String str = df1.format(startDate) + "_bis_" + df.format(endDate);
			FileWriter writer = new FileWriter(dir + "/Storno.csv");
			writer.append("Id");
			writer.append(';');
			writer.append("Tisch");
			writer.append(';');
			writer.append("Anzahl Der Artikel");
			writer.append(';');
			writer.append("Zeit erstellt");
			writer.append(';');
			writer.append("Gesamt");
			writer.append(';');
			writer.append("Grund der Storno");
			writer.append(';');
			writer.append('\n');

			System.out.println("Von "+startDate+" Bis "+endDate);
			List<DeletedKitchenTicket> Voidedtickets = DeletedKitchenTicketDAO.getInstance().findDeletedTickets(startDate, endDate);
			List<Ticket> Voidtickets = TicketDAO.getInstance().findVoidedTickets(startDate, endDate, true);
			System.out.println("Voidtickets>>"+Voidtickets.size());
			try {
				Collections.sort(Voidedtickets, new DeletedTicketComparator());
			}catch(Exception ex) {

			}
			Integer index = 1;

			for (Ticket tickett : Voidtickets) {
				Ticket ticket = TicketDAO.getInstance().loadFullTicket(tickett.getId());
				try {					 
					System.out.println("ticket.getGdpduid(): " +ticket.getGdpduid()+ " , voidreason: " +ticket.getVoidReason());
					writer.append(ticket.getGdpduid() + "");
					writer.append(';');
					writer.append(ticket.getTableNumbers());
					writer.append(';');
					writer.append(ticket.getTicketItems().size() + "");
					writer.append(';');										
					SimpleDateFormat dt = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					String date = ticket.getCreateDate() != null ? dt.format(ticket.getCreateDate()) : dt.format(ticket.getCreateDate());				
					writer.append(date != null ? date : "");
					writer.append(';');
					writer.append(ticket.getTotalAmount()+"");
					writer.append(';');
					writer.append(
							ticket.getVoidReason() != null ? ticket.getVoidReason() : "Bedienungsfehler");
					writer.append(';');
					writer.append('\n');
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}

			index++;

			writer.flush();
			writer.close();
		} catch (Exception xpxw) {
			
			xpxw.printStackTrace();
			return false;
		}
		System.out.println("GdpduStornoTicket Report Finished");*/
		return true;
	}

	public void GdpduTax(String dir) throws IOException {
		FileWriter writer = new FileWriter(dir + "/steuer.csv");
		writer.append("Id");
		writer.append(';');
		writer.append("Name");
		writer.append(';');
		writer.append("Steuer");
		writer.append(';');
		writer.append('\n');

		List<Tax> taxes = TaxDAO.getInstance().findAll();
		for (Tax tax : taxes) {
			writer.append(tax.getId() + "");
			writer.append(';');
			writer.append(tax.getName());
			writer.append(';');
			writer.append(NumberUtil.formatNumber(tax.getRate()));
			writer.append(';');
			writer.append('\n');
		}
		writer.flush();
		writer.close();
	}

	public void GdpduCustomer() throws IOException {
		FileWriter writer = new FileWriter("gdpdu/kunden.csv");
		writer.append("Nr.");
		writer.append(';');
		writer.append("Name");
		writer.append(';');
		writer.append("Addresse");
		writer.append(';');
		writer.append("Haus Nr.");
		writer.append(';');
		writer.append("Stadt");
		writer.append(';');
		writer.append("Netto");
		writer.append(';');
		writer.append("Rabatt Gesamt");
		writer.append(';');
		writer.append("Steuer Gesamt");
		writer.append(';');
		writer.append("Gesamt");
		writer.append(';');
		writer.append("Status");
		writer.append(';');
		writer.append('\n');

		List<Ticket> tickets = TicketDAO.getInstance().findAllDeliveryTickets();
		for (Ticket ticket : tickets) {
			String customerno = ticket.getProperty(Ticket.CUSTOMER_LOYALTY_NO);
			String customername = ticket.getProperty(Ticket.CUSTOMER_NAME);
			String customeraddress = ticket.getProperty(Ticket.CUSTOMER_ADDRESS);
			String customerdoor = ticket.getProperty(Ticket.CUSTOMER_DOOR);
			String customercity = ticket.getProperty(Ticket.CUSTOMER_CITYNAME);

			if (customerno != null)
				writer.append(customerno);
			else
				writer.append("");

			writer.append(';');

			if (customername != null)
				writer.append(customername);
			else
				writer.append("");

			writer.append(';');

			if (customeraddress != null)
				writer.append(customeraddress);
			else
				writer.append("");

			writer.append(';');

			if (customerdoor != null)
				writer.append(customerdoor);
			else
				writer.append("");

			writer.append(';');

			if (customercity != null)
				writer.append(customercity);
			else
				writer.append("");

			writer.append(';');

			writer.append(NumberUtil.formatNumber(ticket.getTotalAmount() - (ticket.getTaxAmount())));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(ticket.getDiscountAmount()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(ticket.getTaxAmount()));
			writer.append(';');
			writer.append(NumberUtil.formatNumber(ticket.getTotalAmount()));
			writer.append(';');
			String status = "";
			if (ticket.isPaid()) {
				if (ticket.getCashPayment() != null && ticket.getCashPayment())
					status = "Bar Bezahlt";
				else if (ticket.getOnlinePayment() != null && ticket.getOnlinePayment())
					status = "Online Bezahlt";
				else if (ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment())
					status = "Gutschein eingelöst";
				else
					status = "Karte Bezahlt";

			} else {
				status = "Nicht bezahlt";
			}
			writer.append(status);
			writer.append(';');
			writer.append('\n');
		}
		writer.flush();
		writer.close();
	}

	/*public void GdpduPersonal() throws IOException {
		FileWriter writer = new FileWriter("gdpdu/personal.csv");
		writer.append("Kellner name");
		writer.append(';');
		writer.append("Bestellungen Anz.");
		writer.append(';');
		writer.append("Steuer");
		writer.append(';');
		writer.append("Gesamt");
		writer.append(';');
		writer.append('\n');
		java.util.List<User> userList = UserDAO.getInstance().findAll();
		for (Iterator<User> itr = userList.iterator(); itr.hasNext();) {
			int finalOrders = 0;
			User user = itr.next();
			if (user.isAdministrator() || user.isDriver()) {
				Double totalAmount = 0.0;
				int totalOrders = 0;
				Double totalTax = 0.00;
				java.util.List<Ticket> ticketList = TicketDAO.getInstance().findByOwner(user);
				for (Iterator<Ticket> tcktItr = ticketList.iterator(); tcktItr.hasNext();) {
					Ticket ticket = tcktItr.next();
					totalAmount += ticket.getTotalAmount();
					totalTax += ticket.getTaxAmount();
					totalOrders++;
				}
				writer.append(user.getFirstName());
				writer.append(';');
				writer.append(totalOrders + "");
				writer.append(';');
				writer.append(NumberUtil.formatNumber(totalTax));
				writer.append(';');
				writer.append(NumberUtil.formatNumber(totalAmount));
				writer.append(';');
				writer.append('\n');
			}
		}
		writer.flush();
		writer.close();
	}*/

	public void GdpduGroupStatistics() throws IOException {
		FileWriter writer = new FileWriter("gdpdu/warengruppen.csv");
		writer.append("Id");
		writer.append(';');
		writer.append("Gruppen name");
		writer.append(';');
		writer.append("Anz.");
		writer.append(';');
		writer.append("Steuer gesamt");
		writer.append(';');
		writer.append("Gesamt");
		writer.append(';');
		writer.append('\n');
		GenericDAO dao = new GenericDAO();
		Session session = dao.getSession();
		Criteria criteria = session.createCriteria(MenuGroup.class);

		List<MenuGroup> groups = criteria.list();

		MenuGroup miscGroup = new MenuGroup();
		miscGroup.setId(999);
		miscGroup.setName(com.floreantpos.POSConstants.MISC);
		groups.add(miscGroup);
		for (MenuGroup group : groups) {
			criteria = session.createCriteria(TicketItem.class, "item");
			criteria.createCriteria("ticket", "t");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.sum(TicketItem.PROP_ITEM_COUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_TAX_AMOUNT));
			projectionList.add(Projections.sum(TicketItem.PROP_SUBTOTAL_AMOUNT));
			criteria.setProjection(projectionList);
			criteria.add(Restrictions.eq("item." + TicketItem.PROP_GROUP_NAME, group.getName()));
			List datas = criteria.list();
			if (datas.size() > 0) {
				Object[] objects = (Object[]) datas.get(0);

				writer.append(group.getId() + "");
				writer.append(';');
				writer.append(group.getName());
				writer.append(';');

				if (objects.length > 0 && objects[0] != null)
					writer.append(((Number) objects[0]).intValue() + "");
				else
					writer.append("");

				writer.append(';');

				if (objects.length > 1 && objects[1] != null)
					writer.append(((Number) objects[1]).intValue() + "");
				else
					writer.append("");

				writer.append(';');

				if (objects.length > 1 && objects[2] != null)
					writer.append(((Number) objects[2]).intValue() + "");
				else
					writer.append("");

				writer.append(';');
			}
			writer.append("\n");
		}
		writer.flush();
		writer.close();
	}
}
