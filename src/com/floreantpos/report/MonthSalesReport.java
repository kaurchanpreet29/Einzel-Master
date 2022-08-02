package com.floreantpos.report;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;


public class MonthSalesReport extends Report implements Runnable{
	private Thread t;
	private SalesReportModel itemReportModel;
	String reportTime;
	Integer salesId;
	Restaurant rest;
	boolean refreshed = false;
	public MonthSalesReport() {
		super();
	} 
	@Override
	public void refresh() throws Exception {
		try
		{
			rest = RestaurantDAO.getRestaurant();
			if(getReportType() == Report.REPORT_TYPE_1||!refreshed) {
				createModels();
			}

			SalesReportModel itemReportModel = this.itemReportModel;
			JasperReport itemReport;	
			if(TerminalConfig.isWholeSale()) {
				if(!rest.isWithWarengroup())
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_W_OhneW.jasper"));
				else
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_W.jasper"));
			}else {
				if(!rest.isWithWarengroup())
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_OhneW.jasper"));
				else
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report.jasper"));
			}
			
			HashMap map = new HashMap();
			int id;
			ReportUtil.populateRestaurantProperties(map);
			if(isJaherAbs())
				id = Integer.parseInt(TerminalConfig.getYearId());
			else
				id = Integer.parseInt(TerminalConfig.getMonthSalesId());

			salesId = id;

			if (getReportType() == Report.REPORT_TYPE_2) {
				if(isJaherAbs()) {
					map.put("reportTitle", "Jahressabschluss Nr. (Z-2): " + salesId);
					TerminalConfig.setYearId((salesId+1)+"");
				}else {
					map.put("reportTitle", "Monatsabschluss Nr. (Z-2): " + salesId);
					TerminalConfig.setMonthSalesId((salesId+1)+"");
				}

			}else {
				if(isJaherAbs()) {
					map.put("reportTitle", "Jahersabschluss (X-2) Nr.: " + salesId);
				}else {
					map.put("reportTitle", "Monatsabschluss (X-2) Nr.: " + salesId);
				}

			}


			Calendar c = Calendar.getInstance();
			Date date = new Date();
			int month = c.get(Calendar.MONTH )+1;
			/*if(this.dineincashfood.length() != 0)
        	map.put("reportTime", this.time);
        else
			 */ 
			SimpleDateFormat format =  new SimpleDateFormat("MMM yyyy ");
			int min = date.getMinutes();
			String minute = min + "";
			if(minute.length() == 1)
			{
				minute = "0"+minute; 
			}
			reportTime = c.get(Calendar.DATE) + "." + month + "." + c.get(Calendar.YEAR) +", um " + date.getHours() + ":" + minute + " Uhr";
			map.put("reportTime", c.get(Calendar.DATE) + "." + month + "." + c.get(Calendar.YEAR) +", um " + date.getHours() + ":" + minute + " Uhr");
			if(isJaherAbs()) {
				Calendar D = Calendar.getInstance();
				D.setTime(getStartDate());
				map.put("dateRange", "von " + D.get(Calendar.YEAR));
			}else
				map.put("dateRange", "von " + format.format(getStartDate()));
			
			 if(Application.getInstance().getCurrentUser().getFirstName().compareTo("Master")==0) {
					Calendar cal = Calendar.getInstance(); // creates calendar
				cal.setTime(getEndDate());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.HOUR_OF_DAY, 1);// sets calendar time/date
				cal.add(Calendar.MINUTE, 20); // adds one hour
				date = cal.getTime(); // returns new date object, one hour in the future
				int min1 = date.getMinutes();
				month = date.getMonth()+1;
				String minute1 = min + "";
				if (minute1.length() == 1) {
					minute1 = "0" + minute1;
				}
				reportTime = cal.get(Calendar.DATE) + "." + month + "." + cal.get(Calendar.YEAR) + ", um " + date.getHours() + ":"
						+ minute1 + " Uhr";
				map.put("reportTime", reportTime);
			}
			map.put("dateRange1","bis " + ReportService.formatShortDate(getEndDate()));
			map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
			map.put("currencySymbol", Application.getCurrencySymbol());
			map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
			map.put("itemReport", itemReport);
			map.put("date", POSConstants.DATE);
			map.put("warenTitle", "Warengruppen-Abrechnung");
			map.put("warenGruppen", "WArengruppen");
			map.put("Anz", "Anz.");
			map.put("total", "Gesamt");
			JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/sales_report.jasper"));
			if(!rest.isWithWarengroup())
				masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/sales_report_OhneW.jasper"));
			ReportService reportService = new ReportService();
			MenuUsageReport report;
			if(rest.isWithWarengroup())
				report = reportService.getMenuUsageReport(
						BusinessDateUtil.startOfOfficialDay(getStartDate()),
						BusinessDateUtil.endOfOfficialDay(getEndDate()), 1, false);
			else{
				report = new MenuUsageReport();
				MenuUsageReportData data = new MenuUsageReportData(); 
				data.setGrossSales(0.0);
				data.setDiscount(0.0);
				data.setCount(0);
				data.setNetSales(0.0);
				data.setProfit(0.0);
				report.addReportData(data);
			}
			if(report.getReportDatas()!=null&&report.getReportDatas().size()<1) {
				report = new MenuUsageReport();
				MenuUsageReportData data = new MenuUsageReportData(); 
				data.setGrossSales(0.0);
				data.setDiscount(0.0);
				data.setCount(0);
				data.setNetSales(0.0);
				data.setProfit(0.0);
				report.addReportData(data);
			}
//			MenuUsageReport report = reportService.getMenuUsageReport(
//					BusinessDateUtil.startOfOfficialDay(getStartDate()),
//					BusinessDateUtil.endOfOfficialDay(getStartDate()), 1, false);

			JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JRTableModelDataSource(report.getTableModel()));
			viewer = new net.sf.jasperreports.swing.JRViewer(print);
			viewer.getReportPanel().setBackground(new Color(209,222,235));
			this.setJasperPrint(print);
			if (getReportType() == Report.REPORT_TYPE_2)
			{
				print.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				JReportPrintService.printQuitely(print);
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try {
			Application.getInstance().shutdownPOS(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
	String nineteen;
	String seven;
	Double dineIn = 19.00;
	Double homeDelevery = 7.0;
	public void createModels() {
				
		Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
		Date date2 = BusinessDateUtil.endOfOfficialDay(getEndDate());
		
		
		System.out.println("check Date "+date1+ReportUtil.isSpecial(date1));
		if(ReportUtil.isSpecial(date1)) {
			dineIn = 16.00;
			homeDelevery = 5.00;
		}else {
			dineIn = Application.getInstance().getDineInTax();
			homeDelevery = Application.getInstance().getHomeDeleveryTax();
		}	
		
		nineteen = dineIn+"%";
		seven = homeDelevery+"%";
		
		List<Ticket> tickets = null;
		//		if(isJaherAbs())
		//			tickets = TicketDAO.getInstance().findYearTickets(date1, date2);
		//		else
		tickets = TicketDAO.getInstance().findMonthTickets(date1, date2);
		//List<Ticket> tickets = null;

		//tickets = TicketDAO.getInstance().findTickets(date1, date2, false);
		System.out.println("List of Ticket = "+tickets.size());
		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
//		Tax dineIn = TaxDAO.getInstance().findByName(POSConstants.DINE_IN);
//		Double dineInTax = dineIn.getRate();
//		Tax homeDelevery = TaxDAO.getInstance().findByName(POSConstants.HOME_DELIVERY);
//		Double homeDeleveryTax = homeDelevery.getRate();
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
		int TOTAL_RECHNUG_PAYMENT_COUNT = 0;
		Double TOTAL_RECHNUG_PAYMENT = 0.00;
		Double TOTAL_RECHNUG_PAYMENT_TAX = 0.00;
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
		int REFUNDETICKETS = 0;
		Double REFUNDEAMT = 0.00;
		Double REFUNDETAX19 = 0.00;
		Double REFUNDETAX7 = 0.00;
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket t = (Ticket) iter.next();
			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());      
			List<TicketItem> ticketItems = ticket.getTicketItems();
			if (ticketItems == null)
				continue;			

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


//			boolean deducted = false;
			double tax19 =0.0;
			double tax7=0.0;
			for (Iterator iterItem = ticketItems.iterator(); iterItem.hasNext();) {
				TicketItem item = (TicketItem) iterItem.next();
//				if (item.isBeverage()) {
//					BEVERAGE += item.getTotalAmount();
//					if(found==1&&!deducted) {
//						BEVERAGE -= ticket.getDiscountAmount();
//						deducted = true;
//					}
//				} else {
//					FOOD += item.getTotalAmount();
//					if(found==1&&!deducted) {
//						FOOD -= ticket.getDiscountAmount();
//						deducted = true;
//					}
//				}
//				boolean deducted1 = false;
				if (item.getTaxRate().compareTo(dineIn) == 0) {
					TOTAL_WTAX19 += item.getTotalAmount();
//					if(found==1&&!deducted1) {
//						Double discount = ticket.getDiscountAmount();
//						TOTAL_DISCOUNT += discount;
//						TOTAL_WTAX19 -= discount;
//						deducted1 = true;
//					}
					tax19 +=item.getTaxAmount();
					
				} else if (item.getTaxRate().compareTo(homeDelevery) == 0) {
					TOTAL_WTAX7 += item.getTotalAmount();
//					if(found==1&&!deducted1) {
//						Double discount = ticket.getDiscountAmount();
//						TOTAL_DISCOUNT += discount;
//						TOTAL_WTAX7 -= discount;
//						deducted1 = true;
//					}
					tax7 +=item.getTaxAmount();				
				}else if(item.getTaxRate().compareTo(0.00)==0){
//					if(found==1&&!deducted1) {
//						Double discount = ticket.getDiscountAmount();
//						TOTAL_DISCOUNT += discount;
//						TOTAL_WTAX0 -= discount;
//						deducted1 = true;
//					}
					TOTAL_WTAX0 += item.getTotalAmount();
					TOTAL_TAX0 += item.getTaxAmount();
				}
				TOTAL_ITEMS +=item.getItemCount();
				if(item.isPfand()) {
					if(item.getTaxRate().compareTo(dineIn)==0)
						REFUNDETAX19 += item.getTaxAmount();
					else if(item.getTaxRate().compareTo(homeDelevery)==0)
						REFUNDETAX7 += item.getTaxAmount();
					REFUNDETICKETS+= item.getItemCount();
					REFUNDEAMT += item.getTotalAmount();
					TOTAL_ITEMS -=item.getItemCount();
				}
										
			}
			if(found==1) {
				TOTAL_DISCOUNT += ticket.getDiscountAmount();
			}
			TOTAL_TAX19 += tax19;
			TOTAL_TAX7 += tax7;

			TOTAL_INVOICES++;
			TOTAL_WTAX += ticket.getTotalAmountWithoutPfand();
			Double totalTax = 0.00;

			totalTax = tax19 + tax7;      

//			TOTAL_TAX19 += ticket.getTax19();
//			TOTAL_TAX7 += ticket.getTax7();
//			TOTAL_TAX0 += ticket.getTax0();


//			BEVERAGE_TAX += ticket.getBeverageTax();
//			FOOD_TAX += ticket.getNonBeverageTax();

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
			}else if (TerminalConfig.isPayTransfer()&&ticket.getRechnugPayemnt() == Boolean.TRUE) {
				TOTAL_RECHNUG_PAYMENT += ticket.getTotalAmount();
				TOTAL_RECHNUG_PAYMENT_COUNT++;
				TOTAL_RECHNUG_PAYMENT_TAX += totalTax;
			} else if (ticket.getCashPayment() == Boolean.TRUE) {
				TOTAL_CASH_PAYMENT += ticket.getTotalAmount();
				TOTAL_CASH_COUNT++;
				TOTAL_CASH_TAX += totalTax;
			} else {
				TOTAL_CARD_PAYMENT += ticket.getTotalAmount();
				TOTAL_CARD_COUNT++;
				TOTAL_CARD_TAX += totalTax;
			}

			//			if (found == 1)
			//				TOTAL_WOTAX += ticket.getTotalAmount() - totalTax;
			//			else
			//				TOTAL_WOTAX += ticket.getTotalAmount() + ticket.getDiscountAmount()
			//				- totalTax;

			TAX_TOT += totalTax;
			ticket = null;
			iter.remove();
		}
		long VOIDTICKETS = 0;
		Double VOIDAMT = 0.00;
		Double VOIDTAX = 0.00;
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



		ReportItem reportItem = new ReportItem();
		reportItem.setId("");
		//reportItem.setAwt(TOTAL_WTAX);
		System.out.println("tax7 = "+TOTAL_WTAX7);

		reportItem.setAwt(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0+TOTAL_DISCOUNT);
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
		
		reportItem.setCardPayment(TOTAL_CARD_PAYMENT);
		reportItem.setCardTax(TOTAL_CARD_TAX);
		reportItem.setCardPaymentCount(TOTAL_CARD_COUNT);
		if(TerminalConfig.isWholeSale()) {
			reportItem.setRechnugPament_anzahl(TOTAL_RECHNUG_PAYMENT_COUNT+"");
			reportItem.setRechnugPayment_tax(String.valueOf(NumberUtil.roundToTwoDigit(TOTAL_RECHNUG_PAYMENT_TAX))+" €");
			reportItem.setRechnugPaymentAmount(String.valueOf(NumberUtil.roundToTwoDigit(TOTAL_RECHNUG_PAYMENT))+" €");
			reportItem.setMwst_gesamt_text_rechnug(POSConstants.SALES_VAT_TOTAL);
			reportItem.setRechnugPayment_text(POSConstants.BILL+"-Zahlung");
			reportItem.setAnzahl_text_rechnug(POSConstants.SALES_COUNT);
			
		}	
		
		//TextFields
		reportItem.setEinnahme_text(POSConstants.SALES_REVENUE);
		reportItem.setUmasat_gesamt_text(POSConstants.SALES_TOTAL_SUM);
		reportItem.setGesamt_19_text(POSConstants.SALES_TOTAL+" "+nineteen);
		reportItem.setGesamt_7_text(POSConstants.SALES_TOTAL+" "+seven);
		reportItem.setGesamt_0_text(POSConstants.SALES_TOTAL+" 0%");
		reportItem.setGesamt_netto_text(POSConstants.SALES_NET_TOTAL);
		reportItem.setNetto_19_text(POSConstants.SALES_NET+" "+nineteen);
		reportItem.setNetto_7_text(POSConstants.SALES_NET+" "+seven);
		reportItem.setNetto_0_text(POSConstants.SALES_NET+" 0%");
		reportItem.setMwst_19_text(POSConstants.SALES_VAT+" "+nineteen);
		reportItem.setMwst_7_text(POSConstants.SALES_VAT+" "+seven);
		reportItem.setMwst_0_text(POSConstants.SALES_VAT+" 0%");		
		reportItem.setAnzahl_retour_text(POSConstants.SALES_TOTAL_RETURN_COUNT);
		reportItem.setRetour_mwst_text(POSConstants.SALES_TOTAL_RETURN_VAT);
		reportItem.setRetour_gesamt_text(POSConstants.SALES_TOTAL_RETURN_AMOUNT);
		reportItem.setAnzahl_storno_text(POSConstants.SALES_TOTAL_CANCELED);
		reportItem.setStorno_mwst_text(POSConstants.SALES_TOTAL_CANCEL_VAT);
		reportItem.setAnzahl_storno_gesamt_text(POSConstants.SALES_TOTAL_CANCELED_AMOUNT);
		reportItem.setAnzahl_rechnugen_text(POSConstants.SALES_TOTAL_BILLS);
		reportItem.setAnzahl_sold_items_text(POSConstants.SALES_TOTAL_SOLD_PRODUCT);
		reportItem.setCashpayment_text(POSConstants.SALES_CASH_PAYMENT);
		reportItem.setMwst_gesamt_text(POSConstants.SALES_VAT_TOTAL);
		reportItem.setMwst_gesamt_text1(POSConstants.SALES_VAT_TOTAL);
		reportItem.setAnzahl_text(POSConstants.SALES_COUNT);
		reportItem.setAnzahl_text1(POSConstants.SALES_COUNT);
		reportItem.setAnzahl_text2(POSConstants.SALES_COUNT);
		reportItem.setCardpayment_text(POSConstants.SALES_CARD_PAYMENT);
		reportItem.setKunden_rabatt_text(POSConstants.SALES_DISCOUNT);
		reportItem.setCash_in_cashdrawer_text(POSConstants.SALES_CASH_CASDRAWER);
		reportItem.setGesamt_summe_text(POSConstants.SALES_SUM_TOTAL);
		reportItem.setWarengroup_abs_text(POSConstants.SALES_GOODS_ACOUNTING);
		reportItem.setWarengroup_text(POSConstants.SALES_GOODS);
  		reportItem.setGesamt_text(POSConstants.SALES_TOTAL);
		itemMap.put("1", reportItem);
		itemReportModel = new SalesReportModel();
		itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
	}
	@Override
	public void refresh(boolean print) throws Exception {
		// TODO Auto-generated method stub

	}
}
