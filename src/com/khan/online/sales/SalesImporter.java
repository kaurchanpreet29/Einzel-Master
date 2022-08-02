package com.khan.online.sales;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.Report;
import com.floreantpos.report.SalesReport;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;

public class SalesImporter {
	private static SalesImporter importer;	

	public static SalesImporter getImporter() {
		return importer = new SalesImporter();
	}

	private static int SalesId;
	private static boolean takeSalesReport;

	public void importSalesReport(File file) {
		oldSalesId = 0;
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(file);
			List<String> lines = IOUtils.readLines(inputStream);
			List<Ticket> ticketList = new ArrayList<Ticket>();

			boolean firstLine = true;
			for (String line : lines) {
				if (firstLine)
				{
					firstLine = false;
					String[] strings = line.split(";");
					try {
						String check = strings[11];
					}catch(Exception ex) {
						POSMessageDialog.showError("Please try once with correct Format..");
						return;
					}
				}
				else
				{
					Ticket ticket = fromCSV(line);
					if(ticket!=null) {
						try {		        			
							TicketDAO dao = new TicketDAO();
							Session session = dao.getSession();
							Transaction tx = session.beginTransaction();
							session.saveOrUpdate(ticket);
							tx.commit();
							dao.closeSession(session);							
						}catch(Exception ex) {
							ex.printStackTrace();
						}        			        		

						if(oldSalesId ==0) {
							System.out.println("here0 "+" "+salesId+" "+name+" "+price);
							oldSalesId =salesId;
							TerminalConfig.setSalesId(Integer.toString(salesId));
							setStartDate(date);
						}else if(oldSalesId!=salesId) {
							System.out.println("here");
							doRefreshReport(true);
							TerminalConfig.setSalesId(Integer.toString(salesId));
							oldSalesId =salesId;
							setStartDate(date);
							System.out.println("here2 "+" "+salesId+" "+name+" "+price);
							ticketList = new ArrayList<Ticket>();
						}

						//TicketDAO.getInstance().save(ticket);
						ticketList.add(ticket);
						System.out.println("SalesID "+salesId+" Name "+name+" Price "+price);
					}


				}
			}
			doRefreshReport(true);
			TerminalConfig.setSalesId(Integer.toString(salesId));
			oldSalesId =salesId;
			setStartDate(date);
			System.out.println("here1 "+" "+salesId+" "+name+" "+price);
			ticketList = new ArrayList<Ticket>();

		}
		catch (Exception e)
		{
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), e.getMessage(), e);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}
	}

	public void takeAutoTages(Date date1, String salesId) {
		try {
			setStartDate(date1);
			TerminalConfig.setSalesId(salesId);
			this.date = date1;
			doRefreshReport(true);
		}catch(Exception ex) {
			
		}
		
	}
	
	

	public static int getSalesId() {
		return SalesId;
	}

	public static boolean isTakeSalesReport() {
		return takeSalesReport;
	}

	public static void setTakeSalesReport(boolean takeSalesReport) {
		SalesImporter.takeSalesReport = takeSalesReport;
	}

	public static void setSalesId(int salesId) {
		SalesId = salesId;
	}

	public static Ticket fromCSV(String csvLine) {

		if(StringUtils.isEmpty(csvLine)) {
			return null;
		}

		String[] strings = csvLine.split(";");

		Ticket ticket = new Ticket();

		int index = 0;
		DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		try {

			//					"Z", "Rechnungs-Nr.", "Beleg", "Produkte", "Anzahl",
			//					"Einnahme", "Gesamt", "Steuer", "Bezhalt","Storno"

			salesId = Integer.parseInt(strings[index++]);
			ticket.setId(Integer.parseInt(strings[index++]));
			date = dateFormat1.parse(strings[index++]);
			ticket.setCreateDate(date);
			name = strings[index++];
			itemCount = Integer.parseInt(strings[index++]);
			price = POSUtil.parseDouble(strings[index++]);
			String total = strings[index++];
			taxrate = POSUtil.parseDouble(strings[index++]);
			bezahlt = Integer.parseInt(strings[index++]);
			storno = Integer.parseInt(strings[index++]);
			stornoReason = strings[index++];
			bezahltmit = Integer.parseInt(strings[index++]);
		} catch (Exception e) {
			return null;
		}


		if(bezahlt==1)
			ticket.setPaid(true);

		if(bezahltmit==1) {
			ticket.setCashPayment(false);
		}else {
			ticket.setCashPayment(true);
		}
		boolean nineteen=false;
		if(Math.abs(taxrate)>=19.0) {
			ticket.setTicketType(TicketType.DINE_IN.name());
			ticket.setType(TicketType.DINE_IN);
			nineteen = true;
		}else {
			ticket.setTicketType(TicketType.HOME_DELIVERY.name());
			ticket.setType(TicketType.HOME_DELIVERY);
		}
		TicketItem tItem = 	getTicketItem(nineteen, ticket);

		List<TicketItem> itemList = new ArrayList<TicketItem>();
		itemList.add(tItem);
		//				ticket.addToticketItems(tItem);
		ticket.setTicketItems(itemList);
		ticket.calculatePrice();
		ticket.setOwner(Application.getCurrentUser());
		Calendar calen = Calendar.getInstance();
		calen.setTime(date);
		calen.add(Calendar.MINUTE, 10);
		Terminal terminal = Application.getInstance().getTerminal();
		ticket.setDrawerResetted(false);
		ticket.setTerminal(terminal);
		ticket.setPaidAmount(ticket.getTotalAmount());
		ticket.setDueAmount(0.00);
		ticket.setPaid(true);
		ticket.setClosed(true);
		ticket.setClosingDate(calen.getTime());
		if(storno==1) {				
			ticket.setVoided(true);
			ticket.setVoidReason(stornoReason);
			ticket.setWasted(true);
			calen.add(Calendar.MINUTE, 30);
			ticket.setClosingDate(calen.getTime());				
		}else {
			ticket.setVoided(false);
		}
		return ticket;
	}

	public static String name;
	public static String stornoReason;
	public static int bezahlt;
	public static int storno;
	public static int bezahltmit;


	public static int salesId;
	public static int oldSalesId;

	public static Double price=0.0;
	public static Double taxrate =0.0;
	private static int itemCount;
	private static Date date;

	/*
		public static String getCategoryName(String name) {
			String category = "";
			boolean exist=false;
			List<MenuCategory> categoryList = MenuCategoryDAO.getInstance().findByName(name);
			if(categoryList!=null) {
				for(MenuCategory categ:categoryList) {
					if(categ.getName().compareTo(name)==0) {
						category = categ.getName();
						exist = true;
						break;
					}
				}
			}
			return category;
		}
		public static String getGroupname(String name) {
			String category = "";
			boolean exist=false;
			List<MenuGroup> GroupList =MenuGroupDAO.getInstance().findByName(name);
			if(GroupList!=null) {
				for(MenuGroup categ:GroupList) {
					if(categ.getName().compareTo(name)==0) {
						category = categ.getName();
						exist = true;
						break;
					}
				}
			}
			return category;
		}*/
	public static TicketItem getTicketItem(boolean ninteen, Ticket ticket) {
		TicketItem ticketItem = new TicketItem();
		ticketItem.setItemCount(itemCount);
		ticketItem.setUnitPrice(price/itemCount);
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		ticketItem.setId(ticket.getId());
		ticketItem.setTicket(ticket);
		ticketItem.setName(name);
		ticketItem.setCategoryName(ninteen ? com.floreantpos.POSConstants.MISC: "SONSTIGES_");
		ticketItem.setGroupName(ninteen ? com.floreantpos.POSConstants.MISC: "SONSTIGES_");
		ticketItem.setShouldPrintToKitchen(true);
		ticketItem.setPrintorder(1);
		ticketItem.setPrintedToKitchen(true);
		ticketItem.setBeverage(false);
		ticketItem.setItemId(997);		
		Double subTotal = price / (1 + (taxrate / 100));
		Double taxAmount = price - subTotal;

		ticketItem.setSubtotalAmount(subTotal);
		Double totalAmount = price;

		ticketItem.setTotalAmount(totalAmount);
		ticketItem.setTotalAmountWithoutModifiers(totalAmount);
		ticketItem.setTaxAmount(taxAmount);
		ticketItem.setTaxRate(taxrate);
		return ticketItem;
	}

	private static Date StartDate;
	private Date EndDate;

	public static Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public boolean doRefreshReport(boolean flag) {// GEN-FIRST:event_doRefreshReport
		int reportT = 0;
		if (flag == true)
			reportT = Report.REPORT_TYPE_2;
		Report report = new SalesReport();		   
		Date fromDate = getStartDate();
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		Random random = new Random();
		cal.set(Calendar.MINUTE,random.nextInt((59-20) +20));
		Date toDate = cal.getTime();

		if (fromDate.after(toDate)) {
			POSMessageDialog
			.showError(
					BackOfficeWindow.getInstance(),
					com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return false;
		}

		try {

			if (report != null) {
				int reportType = 0;
				if (flag == true)
					reportType = Report.REPORT_TYPE_2;

				report.setReportType(reportType);
				report.setStartDate(fromDate);
				report.setEndDate(toDate);
				/*
				 * report.setInput(tfInput.getText(),tfDineCashFood.getText(),
				 * tfDineCashDrink.getText(),
				 * tfDineCardFood.getText(),tfDineCardDrink.getText(),
				 * tfHomeCashFood.getText(), tfHomeCashDrink.getText(),
				 * tfHomeCardFood.getText(),tfHomeCardDrink.getText(),
				 * tfInvoices.getText(), tfItems.getText(), tfSalesId.getText(),
				 * tfTime.getText());
				 */
				report.refresh();


				if ((report != null) && (report.getType() == 1)
						&& (reportType != Report.REPORT_TYPE_1)) {
					Application.enabledGenerateReport();
				}
			}

		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
		return true;
	}



}
