package com.khan.online.sales;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.isdnmonitor.CallMon;
import com.floreantpos.main.Application;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.MenuUsage;
import com.floreantpos.model.ReservationDB;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.MenuUsageDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.MenuUsageReport;
import com.floreantpos.report.Report;
import com.floreantpos.report.ReportItem;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.SalesReport;
import com.floreantpos.report.SalesReportModel;
import com.floreantpos.report.SalesReportModelFactory;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.BusinessDateUtil;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;


public class AutoSalesReport extends Report implements Runnable{

	  private SalesReportModel itemReportModel;
	  String reportTime;
	  Integer salesId;
	  private Thread t;
	  public AutoSalesReport() {
	    super();
	  }

	  @Override
	  public void refresh() throws Exception {
	    try {
	      /*
	       * if(this.dineincashfood.length() != 0) createModelsDup(getInput()); else
	       */
	    	 ReportService reportService = new ReportService();
	         MenuUsageReport report = reportService.getMenuUsageReport(
	        		 BusinessDateUtil.startOfOfficialDay(getStartDate()),
	        		 BusinessDateUtil.endOfOfficialDay(getEndDate()), 1, true);
	         createModels(report);

	         SalesReportModel itemReportModel = this.itemReportModel;
	         JasperReport itemReport;
	         Restaurant rest = Application.getInstance().getRestaurant();
	         if(!rest.isWithWarengroup())
	             itemReport = (JasperReport) JRLoader
	                 .loadObject(SalesReportModelFactory.class
	                     .getResource("/com/floreantpos/report/template/sales_sub_report_OhneW.jasper"));
	             else
	           	  itemReport = (JasperReport) JRLoader
	                 .loadObject(SalesReportModelFactory.class
	                     .getResource("/com/floreantpos/report/template/sales_sub_report.jasper"));

	         HashMap map = new HashMap();
	         long id;
	         ReportUtil.populateRestaurantProperties(map);
	         id = Integer.parseInt(TerminalConfig.getSalesId());
	         salesId = (int) id;
	         if (getReportType() == Report.REPORT_TYPE_2)
	           TerminalConfig.setSalesId((id + 1) + "");
	         /*
	          * if(this.dineincashfood.length() != 0) map.put("reportTitle",
	          * "Z-ABSCHLAG Nr.: "+ this.salesid); else
	          */
	         if (getReportType() == Report.REPORT_TYPE_2)
	           map.put("reportTitle", "Z-ABSCHLAG Nr.: " + id);
	         else
	           map.put("reportTitle", "X-ABSCHLAG Nr.: " + id);
	         Calendar c = Calendar.getInstance();

	         Date date = getEndDate();
	         c.setTime(date);
	         int month = c.get(Calendar.MONTH) + 1;
	         /*
	          * if(this.dineincashfood.length() != 0) map.put("reportTime", this.time);
	          * else
	          */
	         int min = date.getMinutes();
	         String minute = min + "";
	         if (minute.length() == 1) {
	           minute = "0" + minute;
	         }
	         reportTime = c.get(Calendar.DATE) + "." + month + "."
	             + c.get(Calendar.YEAR) + ", um " + date.getHours() + ":" + minute
	             + " Uhr";
	         map.put("reportTime",
	             c.get(Calendar.DATE) + "." + month + "." + c.get(Calendar.YEAR)
	                 + ", um " + date.getHours() + ":" + minute + " Uhr");

	         map.put("dateRange",
	             "von " + ReportService.formatShortDate(getStartDate()));
	         map.put("dateRange1",
	             "bis " + ReportService.formatShortDate(getEndDate()));
	         map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
	         map.put("currencySymbol", Application.getCurrencySymbol());
	         map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
	         map.put("itemReport", itemReport);
	         map.put("datum", "Datum");
	         map.put("warenTitle", "Warengruppen-Abrechnung");
	         map.put("warenGruppen", "WArengruppen");
	         map.put("Anz", "Anz.");
	         map.put("total", "Gesamt");
	         JasperReport masterReport = (JasperReport) JRLoader
	             .loadObject(SalesReport.class
	                 .getResource("/com/floreantpos/report/template/sales_report.jasper"));
	         if(!rest.isWithWarengroup())
	       	  masterReport = (JasperReport) JRLoader
	             .loadObject(SalesReport.class
	                 .getResource("/com/floreantpos/report/template/sales_report_OhneW.jasper"));
	         JasperPrint print = JasperFillManager.fillReport(masterReport, map,
	             new JRTableModelDataSource(report.getTableModel()));
	         viewer = new net.sf.jasperreports.swing.JRViewer(print);
	         viewer.getReportPanel().setBackground(new Color(209, 222, 235));
	         this.setJasperPrint(print);

	         if (getReportType() != Report.REPORT_TYPE_1) {
	           List<Ticket> tickets = null;
	           Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
	           Date date2 = BusinessDateUtil.endOfOfficialDay(getEndDate());
	           tickets = TicketDAO.getInstance().findDateTickets(date1, date2, false);
	           for (Iterator<Ticket> itr = tickets.iterator(); itr.hasNext();) {
	             Ticket ticket = itr.next();
	             ticket.setDrawerResetted(true);
	             TicketDAO.getInstance().saveOrUpdate(ticket);
	           }
	         }
	         if (getReportType() != Report.REPORT_TYPE_1) {
	           if (TerminalConfig.isIsdnEnabled()) {
	             CallMon callMon = Application.getInstance().getCallMonitorInstance();
	             callMon.deleteAll();
	           }
	         }

	         if (getReportType() != Report.REPORT_TYPE_1) {
	           ReservationDB.deleteReservations(getStartDate());
	           Restaurant restaurant = RestaurantDAO.getRestaurant();
	           restaurant.setKitchenSerialNo(1);
	           restaurant.setBarSerialNo(1);
	           RestaurantDAO.getInstance().saveOrUpdate(restaurant);
	           t = new Thread(this);
	           t.start();
	         }
	      if (getReportType() != Report.REPORT_TYPE_1) {
		      Salesreportdb sales;
		      List<Salesreportdb> salesList = SalesReportDAO.getInstance().findById(salesId);
		      sales = salesList != null && !salesList.isEmpty() ? salesList.get(0) : new Salesreportdb();
		      if (sales.getId() == null || sales.getId() == 0) {
		        sales.setId(salesId);
		      }
		      sales.setSalesid(salesId);
		      sales.setStartdate(getStartDate());
		      sales.setEnddate(getEndDate());
		      sales.setReporttime(reportTime);
		      sales.setAwt(TOTAL_WTAX);
		      sales.setAwtn(TOTAL_WTAX19);
		      sales.setAwts(TOTAL_WTAX7);
		      sales.setCashpayment(TOTAL_CASH_PAYMENT);
		      sales.setCashpaymentcount(TOTAL_CASH_COUNT);
		      sales.setCashtax(TOTAL_CASH_TAX);
		      sales.setCardpayment(TOTAL_CARD_PAYMENT);
		      sales.setOnline(TOTAL_ONLINE_PAYMENT);
		      sales.setCardpaymentcount(TOTAL_CARD_COUNT);
		      sales.setOnlinetax(TOTAL_ONLINE_TAX);
		      sales.setCardtax(TOTAL_CARD_TAX);
		      sales.setVoidticket((double) VOIDTICKETS);
		      sales.setVoidamount(VOIDAMT);
		      sales.setVoidtax(VOIDTAX);
		      sales.setFood(FOOD_TAX);
		      sales.setFoodtax(FOOD_TAX);
		      sales.setBeverage(BEVERAGE);
		      sales.setBeveragetax(BEVERAGE_TAX);
		      sales.setTotalwotax(TOTAL_WOTAX);
		      sales.setTaxtotal(TAX_TOT);
		      sales.setTaxn(TOTAL_TAX19);
		      sales.setTaxs(TOTAL_TAX7);
		      sales.setTaxz(TOTAL_TAX0);
		      sales.setCancelledtrans(0.00);
		      sales.setCancelledtax(0.00);
		      sales.setDiscount(TOTAL_DISCOUNT);
		      sales.setInsertamount(0.00);
		      sales.setInsertticket(0);
		      sales.setSolditem((int) TOTAL_ITEMS);
		      sales.setVoidticket((double) VOIDTICKETS);
		      sales.setVoidamount(VOIDAMT);
		      sales.setTotalinvoices((double) TOTAL_INVOICES);
		      sales.setPfand1(TOTAL_PFAND1);
		      sales.setPfand2(TOTAL_PFAND2);
		      sales.setPfand3(TOTAL_PFAND3);
		      sales.setNetton(NETTO_19);
		      sales.setNettos(NETTO_7);
		      sales.setNettoz(NETTO_0);
		     
		      List<MenuUsage> menuUsages = new ArrayList<>();		      
		      report.getReportDatas().stream().forEach(data -> {
		        MenuUsage menuUsage = new MenuUsage();
		        menuUsage.setCategory(data.getCategoryName());
		        menuUsage.setCount(data.getCount());
		        menuUsage.setTax(data.getTax());
		        menuUsage.setGrossSales(data.getGrossSales());
		        menuUsages.add(menuUsage);
		        MenuUsageDAO.getInstance().saveOrUpdate(menuUsage);
		      });
		      sales.setMenuUsages(menuUsages);
		      SalesReportDAO.getInstance().saveOrUpdate(sales);
		      try {
		  		Thread.sleep(300);
		  	} catch (InterruptedException e) {
		  		// TODO Auto-generated catch block
		  		e.printStackTrace();
		  	}
		      
		    }

	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
	 
	  @Override
	  public boolean isDateRangeSupported() {
	    return true;
	  }

	  @Override
	  public boolean isTypeSupported() {
	    return true;
	  }

	  	Double homeTax = 0.00;
	    Double TOTAL_WTAX = 0.00;
	    Double TOTAL_WTAX19 = 0.00;
	    Double TOTAL_WTAX7 = 0.00;
	    Double TOTAL_WTAX0 = 0.00;
	    Double TAX_TOT = 0.00;
	    Double TOTAL_WOTAX = 0.00;
	    Double TOTAL_TAX19 = 0.00;
	    Double TOTAL_TAX7 = 0.00;
	    Double TOTAL_TAX0 = 0.00;
	    long TOTAL_INVOICES = 0;
	    long TOTAL_ITEMS = 0;
	    Double TOTAL_CASH_PAYMENT = 0.00;
	    Double TOTAL_CASH_TAX = 0.00;
	    int TOTAL_CASH_COUNT = 0;
	    int TOTAL_CARD_COUNT = 0;
	    Double TOTAL_CARD_PAYMENT = 0.00;
	    Double TOTAL_CARD_TAX = 0.00;
	    Double TOTAL_ONLINE_PAYMENT = 0.00;
	    Double TOTAL_ONLINE_TAX = 0.00;
	    Double FOOD = 0.00;
	    Double FOOD_TAX = 0.00;
	    Double BEVERAGE = 0.00;
	    Double BEVERAGE_TAX = 0.00;
	    Double TOTAL_DISCOUNT = 0.00;
	    Double TOTAL_PFAND1 = 0.00;
	    Double TOTAL_PFAND2 = 0.00;
	    Double TOTAL_PFAND3 = 0.00;
	    Double NETTO_19 = 0.00;
	    Double NETTO_7 = 0.00;
	    Double NETTO_0 = 0.00;
	    long VOIDTICKETS = 0;
	    Double VOIDTAX = 0.00;
	    Double VOIDAMT = 0.00;
	    int REFUNDETICKETS = 0;
	    Double REFUNDEAMT = 0.00;
	    Double REFUNDETAX19 = 0.00;
	    Double REFUNDETAX7 = 0.00;

	  public void createModels(MenuUsageReport report) {
	    Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
	    Date date2 =BusinessDateUtil.endOfOfficialDay(getEndDate());
	    Calendar c = Calendar.getInstance();	     
	    List<Ticket> tickets = TicketDAO.getInstance().findTickets(date1, date2, false);	    
	    
	    if(TerminalConfig.isAllTickets())
	    	tickets = TicketDAO.getInstance().findAllCurrentTickets();
	    System.out.println(" "+ date1+date2+tickets.size());
	    HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
	    
	    Double dineInTax = Application.getInstance().dineInTax;
	    homeTax = Application.getInstance().homeDeleveryTax;
	    
	    for (Iterator iter = tickets.iterator(); iter.hasNext();) {
	      Ticket t = (Ticket) iter.next();
	      Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());      
	      List<TicketItem> ticketItems = ticket.getTicketItems();
	      if (ticketItems == null)
	        continue;      
	      
	      Double taxRate = 0.00;
	      if (ticket.getTicketType() != null) {
	        if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {
	          taxRate = dineInTax;
	        } else {
	          taxRate = homeTax;
	        }
	      }
	      for (Iterator iterItem = ticketItems.iterator(); iterItem.hasNext();) {
	        TicketItem item = (TicketItem) iterItem.next();

	        if (item.isBeverage()) {
	          BEVERAGE += item.getSubtotalAmount();
	        } else {
	          FOOD += item.getSubtotalAmount();
	        }
	        if (item.getTaxRate().compareTo(19.00) == 0) {
	          TOTAL_WTAX19 += item.getSubtotalAmount();
	        } else if (item.getTaxRate().compareTo(7.00) == 0) {
	          TOTAL_WTAX7 += item.getSubtotalAmount();
	        }else {
	        	TOTAL_WTAX0+= item.getSubtotalAmount();
	        }
	      }
	      long itemsCount = 0;
	      for (TicketItem item : ticketItems) {
	        itemsCount += item.getItemCount();
	      }
	      TOTAL_ITEMS += itemsCount;
	      TOTAL_INVOICES++;
	      TOTAL_WTAX += ticket.getTotalAmountWithoutPfand();
	      TOTAL_DISCOUNT += ticket.getDiscountAmount();
	      Double totalTax = 0.00;

	      totalTax = ticket.getTax19() + ticket.getTax7();      

	      TOTAL_TAX19 += ticket.getTax19();
	      TOTAL_TAX7 += ticket.getTax7();
	      TOTAL_TAX0 += ticket.getTax0();
	      
	      BEVERAGE_TAX += ticket.getBeverageTax();
	      FOOD_TAX += ticket.getNonBeverageTax();

	      TOTAL_PFAND1 += ticket.getPfand();
	      TOTAL_PFAND2 += ticket.getPfand2();
	      TOTAL_PFAND3 += ticket.getPfand3();
	      
	      if (ticket.getSplitPayment() == Boolean.TRUE) {
	          TOTAL_CASH_PAYMENT += ticket.getTotalAmount()-ticket.getCardAmount();
	          TOTAL_CASH_COUNT++;
	          TOTAL_CASH_TAX += totalTax/2;
	          TOTAL_CARD_PAYMENT += ticket.getCardAmount();
	          TOTAL_CARD_COUNT++;
	          TOTAL_CARD_TAX += totalTax/2;
	        } else if (ticket.getCashPayment() == Boolean.TRUE) {
	        TOTAL_CASH_PAYMENT += ticket.getTotalAmount();
	        TOTAL_CASH_COUNT++;
	        TOTAL_CASH_TAX += totalTax;
	      } else {
	        TOTAL_CARD_PAYMENT += ticket.getTotalAmount();
	        TOTAL_CARD_COUNT++;
	        TOTAL_CARD_TAX += totalTax;
	      }

	      int found = 0;
	      List<TicketCouponAndDiscount> couponList = ticket.getCouponAndDiscounts();
	      for (Iterator<TicketCouponAndDiscount> itr = couponList.iterator(); itr
	          .hasNext();) {
	        TicketCouponAndDiscount coupon = itr.next();
	        if (coupon.getType() == CouponAndDiscount.PERCENTAGE_PER_ITEM) {
	          found = 1;
	          break;
	        }

	      }
	      if (found == 1)
	        TOTAL_WOTAX += ticket.getTotalAmount() - totalTax;
	      else
	        TOTAL_WOTAX += ticket.getTotalAmount() + ticket.getDiscountAmount()
	            - totalTax;

	      TAX_TOT += totalTax;
	      ticket = null;
	      iter.remove();
	    }
	    long VOIDTICKETS = 0;
	    
	    String terminalNumber = TerminalConfig.getTerminalId() + "";
	    List<Ticket> Voidedtickets = TicketDAO.getInstance().findVoidedTickets(
	        date1, date2, false);
	    for (Iterator voidIter = Voidedtickets.iterator(); voidIter.hasNext();) {
	      Ticket t = (Ticket) voidIter.next();
	      Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());
	      if (t.getVoidReason() != null) {
	        if (t.getVoidReason().toString().compareTo(terminalNumber) == 0)
	          continue;
	      }
	      VOIDTAX += ticket.getTax19() + ticket.getTax7();
	      VOIDTICKETS++;
	      VOIDAMT += ticket.getTotalAmount();
	    }
	    
	   

	    List<Ticket> Refundtickets = TicketDAO.getInstance().findRefundedTickets(date1, date2);
	    for (Iterator refunded = Refundtickets.iterator(); refunded.hasNext();) {
	      Ticket t = (Ticket) refunded.next();
	      Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());      
	      REFUNDETAX19 += ticket.getTax19();
	      REFUNDETAX7 += ticket.getTax7();
	      REFUNDETICKETS++;
	      REFUNDEAMT += ticket.getTotalAmount();
	    }
	  

	    ReportItem reportItem = new ReportItem();
	    reportItem.setId("");
	    //reportItem.setAwt(TOTAL_WTAX);

	    reportItem.setAwt(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0);
	    reportItem.setAwt19(TOTAL_WTAX19);
	    reportItem.setAwt0(TOTAL_WTAX0);
	    if (TerminalConfig.isSupermarket()) {
	      reportItem.setAwt7(TOTAL_WTAX7);
	      reportItem.setAwt0(TOTAL_WTAX0);
	    }
	    reportItem.setAwot(TOTAL_WOTAX);
	    reportItem.setttd(TOTAL_TAX19);
	    NETTO_19 = TOTAL_WTAX19 - TOTAL_TAX19;
	    NETTO_7 = TOTAL_WTAX7 - TOTAL_TAX7;
	    NETTO_0 = TOTAL_WTAX0;
	    
	    if (TerminalConfig.isSupermarket()) {
	      reportItem.settts(TOTAL_TAX7);
	      reportItem.setttz(TOTAL_TAX0);
	      reportItem.setNettos(NETTO_7);
	      reportItem.setNettoz(NETTO_0);
	    }    
	    
	    reportItem.setCashPayment(TOTAL_CASH_PAYMENT);
	    reportItem.setCashTax(TOTAL_CASH_TAX);
	    reportItem.setCashPaymentCount(TOTAL_CASH_COUNT);
	    reportItem.setTotalInvoices(TOTAL_INVOICES);
	    reportItem.setTotalSoldItems(TOTAL_ITEMS);
	    reportItem.setCancelledTax(VOIDTAX);
	    reportItem.setCancelledItems(VOIDTICKETS);
	    reportItem.setCancelledTrans(VOIDAMT);
	    reportItem.setTotalCash(TOTAL_CASH_PAYMENT-TOTAL_PFAND1-TOTAL_PFAND2-TOTAL_PFAND3);
	    reportItem.setPfand1(TOTAL_PFAND1);
	    reportItem.setPfand2(TOTAL_PFAND2);
	    reportItem.setPfand3(TOTAL_PFAND3);
	    reportItem.setNetton(NETTO_19);
	    reportItem.setAnzahlRetour(REFUNDETICKETS);
	    reportItem.setRetourGesamt(REFUNDEAMT);
	    reportItem.setRetourTax(REFUNDETAX19+REFUNDETAX7);
	    reportItem.setGesamtMwst19(TOTAL_WTAX19+REFUNDETAX19);
	    reportItem.setGesamtMwst7(TOTAL_TAX7+REFUNDETAX7);
	    reportItem.setDiscountAmount(TOTAL_DISCOUNT);
	    reportItem.setGesamtSumme(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0-TOTAL_PFAND1-TOTAL_PFAND2-TOTAL_PFAND3);

//	    if (getReportType() != Report.REPORT_TYPE_1) {
//	        System.out.println(TOTAL_TAX7+" "+ date1+date2+tickets.size()+TOTAL_WTAX7);
//	      Salesreportdb sales;
//	      List<Salesreportdb> salesList = null;
//	      System.out.println("Sales id  "+salesId);
//	      try {
//	    	  
//	    	  salesList = SalesReportDAO.getInstance().findById(salesId);
//	    	  
//	      }catch(Exception ex) {
//	    	  ex.printStackTrace();
//	      }
//	      sales = salesList != null && !salesList.isEmpty() ? salesList.get(0) : new Salesreportdb();
//	      if (sales.getId() == null || sales.getId() == 0) {
//	        sales.setId(salesId);
//	      }
//	      sales.setSalesid(salesId);
//	      sales.setStartdate(getStartDate());
//	      sales.setEnddate(new Date());
//	      sales.setReporttime(reportTime);
//	      //sales.setAwt(TOTAL_WTAX);
//	      sales.setAwt(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0);
//	      sales.setAwtn(TOTAL_WTAX19);
//	      sales.setAwts(TOTAL_WTAX7);
//	      sales.setCashpayment(TOTAL_CASH_PAYMENT);
//	      sales.setCashpaymentcount(TOTAL_CASH_COUNT);
//	      sales.setCashtax(TOTAL_CASH_TAX);
//	      sales.setCardpayment(TOTAL_CARD_PAYMENT);
//	      sales.setOnline(TOTAL_ONLINE_PAYMENT);
//	      sales.setCardpaymentcount(TOTAL_CARD_COUNT);
//	      sales.setOnlinetax(TOTAL_ONLINE_TAX);
//	      sales.setCardtax(TOTAL_CARD_TAX);
//	      sales.setVoidticket((double) VOIDTICKETS);
//	      sales.setVoidamount(VOIDAMT);
//	      sales.setVoidtax(VOIDTAX);
//	      sales.setFood(FOOD_TAX);
//	      sales.setFoodtax(FOOD_TAX);
//	      sales.setBeverage(BEVERAGE);
//	      sales.setBeveragetax(BEVERAGE_TAX);
//	      sales.setTotalwotax(TOTAL_WOTAX);
//	      sales.setTaxtotal(TAX_TOT);
//	      sales.setTaxn(TOTAL_TAX19);
//	      sales.setTaxs(TOTAL_TAX7);
//	      sales.setTaxz(TOTAL_TAX0);
//	      sales.setCancelledtrans(0.00);
//	      sales.setCancelledtax(0.00);
//	      sales.setDiscount(TOTAL_DISCOUNT);
//	      sales.setInsertamount(0.00);
//	      sales.setInsertticket(0);
//	      sales.setSolditem((int) TOTAL_ITEMS);
//	      sales.setVoidticket((double) VOIDTICKETS);
//	      sales.setVoidamount(VOIDAMT);
//	      sales.setTotalinvoices((double) TOTAL_INVOICES);
//	      sales.setPfand1(TOTAL_PFAND1);
//	      sales.setPfand2(TOTAL_PFAND2);
//	      sales.setPfand3(TOTAL_PFAND3);
//	      sales.setNetton(NETTO_19);
//	      sales.setNettos(NETTO_7);
//	      sales.setNettoz(NETTO_0);
//	      sales.setAnzahlRetour(REFUNDETICKETS);
//	      sales.setRetourGesamt(REFUNDEAMT);
//	      sales.setRetourTax(REFUNDETAX19+REFUNDETAX7);
//	      sales.setGesamtMwst19(TOTAL_WTAX19-REFUNDETAX19);
//	      sales.setGesamtMwst7(TOTAL_TAX7-REFUNDETAX7);
//	      sales.setGesamtSumme(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0-TOTAL_PFAND1-TOTAL_PFAND2-TOTAL_PFAND3+REFUNDEAMT);
//	      List<MenuUsage> menuUsages = new ArrayList<>();
//
//	      int count = 0;
//	      report.getReportDatas().stream().forEach(data -> {
//	        MenuUsage menuUsage = new MenuUsage();
//	        menuUsage.setCategory(data.getCategoryName());
//	        menuUsage.setCount(data.getCount());
//	        menuUsage.setTax(data.getTax());
//	        menuUsage.setGrossSales(data.getGrossSales());
//	        menuUsages.add(menuUsage);
//	        MenuUsageDAO.getInstance().saveOrUpdate(menuUsage);
//	      });
//	      sales.setMenuUsages(menuUsages);
//	      SalesReportDAO.getInstance().saveOrUpdate(sales);
//	      try {
//			Thread.sleep(300);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    }
	    reportItem.setCardPayment(TOTAL_CARD_PAYMENT);
	    reportItem.setCardTax(TOTAL_CARD_TAX);
	    reportItem.setCardPaymentCount(TOTAL_CARD_COUNT);

	    itemMap.put("1", reportItem);
	    // itemMap.put("reportTime", ReportService.formatFullDate(new Date()));

	    itemReportModel = new SalesReportModel();
	    itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
	  }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(boolean print) throws Exception {
		// TODO Auto-generated method stub
		
	}
	}
