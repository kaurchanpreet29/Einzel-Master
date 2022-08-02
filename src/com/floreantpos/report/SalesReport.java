package com.floreantpos.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.add.service.TseTicketService;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.isdnmonitor.CallMon;
import com.floreantpos.main.Application;
import com.floreantpos.model.Cashbook;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.MenuUsage;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.ReservationDB;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketCouponAndDiscount;
import com.floreantpos.model.TicketCriteria;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.CashbookDAO;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.model.dao.KitchenTicketItemDAO;
import com.floreantpos.model.dao.MenuUsageDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TSEReceiptDataDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class SalesReport extends Report implements Runnable {

	//	private Thread t;
	private SalesReportModel itemReportModel;
	String reportTime;
	Integer salesId;
	Restaurant rest;
	String nineteen;
	String seven;
	boolean refreshed = false;
	private List<Ticket> tickets = new ArrayList<Ticket>();
	public SalesReport() {		
		super();
	}
	Salesreportdb sales;
	MenuUsageReport report;
	boolean isTseActivated = false;

	@Override
	public void refresh() throws Exception {
		try {
			gen =false;
			rest = RestaurantDAO.getRestaurant();
			this.isTseActivated = ReportUtil.isTseActiv(getStartDate());
			if(getReportType() == Report.REPORT_TYPE_1||!refreshed) {
				salesId = rest.getSalesid()!=null?rest.getSalesid():Integer.parseInt(TerminalConfig.getSalesId());
				createModels();
				refreshed = true;				
			}

			SalesReportModel itemReportModel = this.itemReportModel;
			JasperReport itemReport;

			if(TerminalConfig.isZahlungSteur()) {
				System.out.println("First");
				itemReport = (JasperReport) JRLoader.loadObject(
						SalesReportModelFactory.class.getResource("/com/floreantpos/report/template/sales_sub_report_zahlung.jasper")); 
			} else if(TerminalConfig.isWholeSale()) {
				System.out.println("second");
				if(!rest.isWithWarengroup())
					
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_W_OhneW.jasper"));
				else
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_W.jasper"));
			} else if(TerminalConfig.isPayTransfer()) {
				System.out.println("third");
				if(!rest.isWithWarengroup()) {	
					System.out.println("third_1");
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_OnlineOhneW.jasper"));				 
				} else if(TerminalConfig.isRabattDirektEnable() || TerminalConfig.isTRINKGELD()) {		
					System.out.println("third_2");
					itemReport = (JasperReport) JRLoader
							.loadObject(SalesReportModelFactory.class
									.getResource("/com/floreantpos/report/template/sales_sub_report_Online_Gud_Tip.jasper"));					
				} else {		
					System.out.println("third_3");
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_Online.jasper"));
				}
			} else if(TerminalConfig.isRabattDirektEnable() || TerminalConfig.isTRINKGELD()) {
				itemReport = (JasperReport) JRLoader
						.loadObject(SalesReportModelFactory.class
								.getResource("/com/floreantpos/report/template/sales_sub_report_gudschein_tip.jasper"));
			} else {
				System.out.println("fourth");
				if(!rest.isWithWarengroup()) {
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_OhneW.jasper"));
				} else {
					System.out.println("fifth");
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report.jasper"));
				}
			}

			HashMap map = new HashMap();
			ReportUtil.populateRestaurantProperties(map);     
			if (getReportType() == Report.REPORT_TYPE_2) {
				rest.setSalesid(salesId+1);
				rest.setStartToday(null);
				RestaurantDAO.getInstance().saveOrUpdate(rest);
				TerminalConfig.setSalesId((salesId + 1) + "");
			}

			if (getReportType() == Report.REPORT_TYPE_2) {
				try {
					SalesReportDAO.getInstance().saveOrUpdate(sales);
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				map.put("reportTitle", "Z-ABSCHLAG Nr.: " + salesId);
			} else
				map.put("reportTitle", "X-ABSCHLAG Nr.: " + salesId);
			Calendar c = Calendar.getInstance();

			map.put("reportTime",  reportTime);
			map.put("dateRange",
					"von " + ReportService.formatShortDate(getStartDate()));
			map.put("dateRange1",
					"bis " + ReportService.formatShortDate(dateNow));
			map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
			map.put("currencySymbol", Application.getCurrencySymbol());
			map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
			map.put("itemReport", itemReport);
			map.put("date", POSConstants.DATE);
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
			setJasperPrint(print);  
			final Restaurant restaurant = RestaurantDAO.getRestaurant();
			if (getReportType() != Report.REPORT_TYPE_1) {

				if(!gen) {
					if (TerminalConfig.isIsdnEnabled()) {
						CallMon callMon = Application.getInstance().getCallMonitorInstance();
						callMon.deleteAll();
					}
					if(TerminalConfig.isReservation()) {
						ReservationDB.deleteReservations(getStartDate());
						restaurant.setKitchenSerialNo(1);
						restaurant.setBarSerialNo(1);
					}
				}

				Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
				Date date2 = BusinessDateUtil.endOfOfficialDay(getStartDate());
				System.out.println(" here  "+ date1+date2);
				tickets = TicketDAO.getInstance().findDateTickets(date1, date2, false);
				int ticketId = restaurant.getTicketid() != null ? restaurant.getTicketid() : 0;
				
				
				for (Ticket ticket:tickets) {					
					ticket.setDrawerResetted(true);
					if ((ticket.getTicketid() == null || ticket.getTicketid() == 0)) {
						ticket.setTicketid(ticketId);
						ticketId = ticketId+1;						
					}					
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}
				
				
				if (yesterday) {
					for (Iterator<Ticket> itr = leftOverYesterday.iterator(); itr.hasNext();) {
						Ticket ticket = itr.next();
						//tickets.add(ticket);
						ticket.setDrawerResetted(true);
						TicketDAO.getInstance().saveOrUpdate(ticket);
					}					
					yesterday = false;
				}
				//				if(TerminalConfig.isAllTickets()&&!TerminalConfig.isBuildMode())
				//					tickets = TicketDAO.getInstance().findAllCurrentTickets();

				for (Ticket ticket:tickets) {
					if(tseEnable&&isTseActivated&&tseTier3) {
						
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
						tseForceFinish(ticket);
					}
					System.out.println("ticket.setDrawerResetted1__");
					ticket.setDrawerResetted(true);
					if ((ticket.getTicketid() == null || ticket.getTicketid() == 0)) {
						ticket.setTicketid(ticketId);
						ticketId = ticketId+1;						
					}					
					TicketDAO.getInstance().saveOrUpdate(ticket);
				}
				restaurant.setTicketid(ticketId);
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);			

				try {
					if(TerminalConfig.isCashBookEnable())					
						doAutoKassenBook(restaurant);
				} catch(Exception ex) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	boolean gen;
	boolean tseCheck = false;
	@Override
	public void refresh(boolean gen) throws Exception {
		try {
			/*
			 * if(this.dineincashfood.length() != 0) createModelsDup(getInput()); else
			 */
			this.gen = gen;
			rest = RestaurantDAO.getRestaurant();
			salesId = rest.getSalesid()!=null?rest.getSalesid():Integer.parseInt(TerminalConfig.getSalesId());
			if(!tseCheck) {
				this.isTseActivated = ReportUtil.isTseActiv(getStartDate());
				tseCheck = true;
			}

			if(createModels()) {
				rest.setSalesid(salesId+1);
				RestaurantDAO.getInstance().saveOrUpdate(rest);
				TerminalConfig.setSalesId((salesId + 1) + "");
				SalesReportDAO.getInstance().saveOrUpdate(sales);
				List<Ticket> tickets = null;
				Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
				Date date2 = BusinessDateUtil.endOfOfficialDay(getStartDate());

				tickets = TicketDAO.getInstance().findDateTickets(date1, date2, false);
				for (Ticket ticket:tickets) {
					ticket.setDrawerResetted(true);
					TicketDAO.getInstance().saveOrUpdate(ticket);
					
					if(tseEnable&&isTseActivated&&tseTier3) {
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getTicketid());	
						tseForceFinish(ticket);
						//TicketDAO.getInstance().saveOrUpdate(ticket);
					}  
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		if(!gen) {
			try {
				if(Application.getCurrentUser().getFirstName().compareTo("Master")!=0)
					Application.getInstance().shutdownPOS(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	Date dateNow;
	boolean yesterday = false;
	List<Ticket> leftOverYesterday = null;
	//	List<Ticket> ticketsCache;
	Calendar cal = Calendar.getInstance();
	Double BAR_TRINKGELD;
	Double KARTE_TRINKGELD;
	Double GUTS_TRINKGELD;
	Double dineIn = 19.00;
	Double homeDelevery = 7.00;
	
	Double RABATT_19;
	Double RABATT_7;
	
	
	@SuppressWarnings("deprecation")
	public boolean createModels() {
		System.out.println("Create Model....");
		Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
		Date date2 = BusinessDateUtil.endOfOfficialDay(getStartDate());

		if(StringUtils.isNotEmpty(POSConstants.Tax_drinks))
			dineIn = Double.parseDouble(POSConstants.Tax_drinks);
		 
		
		if(StringUtils.isNotEmpty(POSConstants.Tax_drinks))
			homeDelevery = Double.parseDouble(POSConstants.Tax_food);
		
		if(ReportUtil.isSpecial(date1)) {
			dineIn = 16.00;
			homeDelevery = 5.00;
		}else {
			dineIn = Application.getInstance().getDineInTax();
			homeDelevery = Application.getInstance().getHomeDeleveryTax();
		}	
		
		nineteen = dineIn+"%";
		seven = homeDelevery+"%";

		dateNow = new Date();
		cal.setTime(getStartDate());
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date dateS = cal.getTime();
		Date dateE = getStartDate();
		boolean yesterdayTaken = false;
		List<Salesreportdb> salesReportss = SalesReportDAO.getInstance().findByDate(dateS);
		if (salesReportss != null && !salesReportss.isEmpty() && salesReportss.size()  == 1) {
			yesterdayTaken = true;
		} else {
			cal.add(Calendar.DATE, -1);
			dateS = cal.getTime();
			dateE = BusinessDateUtil.endOfOfficialDay(dateS);
		}
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy,HH:mm");

		if(yesterdayTaken) {
			leftOverYesterday = TicketDAO.getInstance().findTickets(dateS, dateE, false);
			for(Ticket tkt: leftOverYesterday) {
				if(!tkt.isDrawerResetted()) {
					
					yesterday = true;
					break;
				}
			}	
		}
		
		TicketCriteria criteria = new TicketCriteria();
		criteria.setStartDate(date1);
		criteria.setEndDate(date2);
		criteria.setDrawerResseted(false);
		//					if (getReportType() == Report.REPORT_TYPE_1) {
		//						
		//						tickets = TicketDAO.getInstance().findTicketsToday(date1, date2);
		//					}else {
		//						tickets = TicketDAO.getInstance().findTickets(date1, date2, false);
		//					}

		if (getReportType() == Report.REPORT_TYPE_1) {				
			tickets = TicketDAO.getInstance().findTicketsWithIDOnly(criteria);
		} else {
			tickets = TicketDAO.getInstance().findTicketsWithIDOnly(criteria);
		}

		//		tickets = TicketDAO.getInstance().findTickets(date1, date2, false);

		if(gen&&tickets.size()==0)
			return false;
	

		if(TerminalConfig.isAutoTagesAbs()) {
			dateNow = getStartDate();
			Calendar c = Calendar.getInstance();
			c.setTime(dateNow);
			dateNow = c.getTime(); 
			int month = c.get(Calendar.MONTH) + 1;
			int day =c.get(Calendar.DAY_OF_MONTH);
			String days = ReportUtil.twoDigitPadding(day + "");
			String months = ReportUtil.twoDigitPadding(month + "");
			reportTime = days + "." + months + "."
					+ c.get(Calendar.YEAR) + ", um " +  Application.getInstance().getRestaurant().getAutoSalesHour() + ": "+ReportUtil.twoDigitPadding(ReportUtil.generateRandom(35, 7)+"") + 
					" Uhr";
		} else if(Application.getCurrentUser()!=null&&Application.getCurrentUser().getFirstName().compareTo("Master")==0) {
			reportTime = format.format(date2);
			Date dateNow = new Date();
			if(tickets.size()>1&&tickets.get(tickets.size()-1).getClosingDate()!=null)
				dateNow = tickets.get(tickets.size()-1).getClosingDate();
			else if(tickets.size()>1&&tickets.get(tickets.size()-1).getCreateDate()!=null)
				dateNow = tickets.get(tickets.size()-1).getCreateDate();
			Calendar c = Calendar.getInstance();
			System.out.println("M "+dateNow);
			c.setTime(dateNow);
			//c.add(Calendar.MINUTE, ReportUtil.generateRandom(50, 30)); // adds one hour
			dateNow = c.getTime(); // returns new date object, one hour in the future
			
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
			 
			Date date = new Date();
			String timeStamp = timeFormat.format(date);
			System.out.println("dateNow: " + dateNow.getTime()+ ", " + timeStamp);
			
			int month = c.get(Calendar.MONTH)+1;
			int min = dateNow.getMinutes();
			int hours = dateNow.getHours();
			int day =c.get(Calendar.DAY_OF_MONTH);
			String minute = ReportUtil.twoDigitPadding(min + "");
			String hourss = ReportUtil.twoDigitPadding(hours + "");
			String days = ReportUtil.twoDigitPadding(day + "");
			String months = ReportUtil.twoDigitPadding(month + "");
			reportTime = days + "." + months + "."
					+ c.get(Calendar.YEAR) + ", um " + hourss + ":" + minute
					+ " Uhr";
			
            LocalDateTime localDateTime = LocalDateTime.now();			
			System.out.println("localDateTime: " + localDateTime );
			
			System.out.println("M "+reportTime);
		} else {
			
			Calendar c = Calendar.getInstance();
			int month = c.get(Calendar.MONTH) + 1;
			int min = dateNow.getMinutes();
			int hours = dateNow.getHours();
			int day =c.get(Calendar.DAY_OF_MONTH);
			String minute = min + "";
			if (minute.length() == 1) {
				minute = "0" + minute;
			}

			String hourss = ReportUtil.twoDigitPadding(hours + "");			
			String days = ReportUtil.twoDigitPadding(day + "");
			String months = ReportUtil.twoDigitPadding(month + "");

			reportTime = days + "." + months + "."
					+ c.get(Calendar.YEAR) + ", um " + hourss + ":" + minute
					+ " Uhr";

			System.out.println("T "+reportTime);
		}	

		if(yesterdayTaken) {
			criteria.setStartDate(dateS);
			criteria.setEndDate(date1);
			leftOverYesterday = TicketDAO.getInstance().findTicketsWithIDOnly(criteria);
			//			leftOverYesterday = TicketDAO.getInstance().findTickets(dateS, date1, false);
			for(Ticket tkt: leftOverYesterday) {
				tickets.add(tkt);				
			}	
		}
		if(TerminalConfig.isCashBookEnable()||TerminalConfig.isZahlungSteur())
			calculateZahlung(tickets);

		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
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
		BAR_TRINKGELD = 0.00;
		KARTE_TRINKGELD = 0.00;
		GUTS_TRINKGELD = 0.00;
		RABATT_19 = 0.00;
		RABATT_7=0.00;
		Double SOLD_GUTSCHEIN = 0.00;
		Double GUTSCHEIN_EINGELOST = 0.00;
		Double TICKET_GUTSCHEIN_AMOUNT = 0.00;
		Double TOTAL_CASH_PAYMENT = 0.00;
		Double TOTAL_CASH_TAX = 0.00;
		int TOTAL_CASH_COUNT = 0;
		int TOTAL_CARD_COUNT = 0;
		int TOTAL_GUDSCHEIN_CARD_COUNT = 0;
		int TOTAL_RECHNUG_PAYMENT_COUNT = 0;
		Double TOTAL_RECHNUG_PAYMENT = 0.00;
		Double TOTAL_RECHNUG_PAYMENT_TAX = 0.00;
		Double TOTAL_CARD_PAYMENT = 0.00;
		Double TOTAL_CARD_PAYMENT_GUDSCHEIN = 0.00;
		Double TOTAL_CARD_TAX = 0.00;	
		Double TOTAL_GUDSCHEIN_CARD_TAX = 0.00;
		Double TOTAL_ONLINE_PAYMENT = 0.00;
		Double TOTAL_ONLINE_TAX = 0.00;
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
		long VOIDTICKETS = 0;
		Double VOIDAMT = 0.00;

		int TOTAL_ONLINE_PAYMENT_COUNT = 0;	

		long VOIDARTICLE = 0;
		List<TicketItem> menuItems = new ArrayList<TicketItem>();
		if(tickets!=null&&tickets.size()>0) {

			for (Iterator iter = tickets.iterator(); iter.hasNext();) {
				Ticket t = (Ticket) iter.next();
				Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());      
				List<TicketItem> ticketItems = ticket.getTicketItems();		

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
				//				boolean deducted = false;
				double tax19 =0.0;
				double tax7=0.0;
				//				boolean deducted1 = false;
				double totalItems = 0.0;
				for (Iterator iterItem = ticketItems.iterator(); iterItem.hasNext();) {
					TicketItem item = (TicketItem) iterItem.next();	
					menuItems.add(item);

					System.out.println("item.getTaxRate(): "+item.getTaxRate()+" , dineIn: "+ dineIn+", TicktId: "+ticket.getId());
					if (item.getTaxRate().compareTo(dineIn) == 0) {
						TOTAL_WTAX19 += item.getTotalAmount();
						
						//						if(found==1&&!deducted1) {
						//							Double discount = ticket.getDiscountAmount();
						//							TOTAL_DISCOUNT += discount;
						//							TOTAL_WTAX19 -= discount;
						//							deducted1 = true;
						//						}
						tax19 +=item.getTaxAmount();
						
						if(item.getExistDiscount()!=null&&item.getExistDiscount()) {
							 
							RABATT_19 += item.getDiscountAmt();
						}
						 
					} else if (item.getTaxRate().compareTo(homeDelevery) == 0) {	
						TOTAL_WTAX7 += item.getTotalAmount();
						//						if(found==1&&!deducted1) {
						//							Double discount = ticket.getDiscountAmount();
						//							TOTAL_DISCOUNT += discount;
						//							TOTAL_WTAX7 -= discount;
						//							deducted1 = true;
						//						}
						tax7 +=NumberUtil.roundToTwoDigit(item.getTaxAmount());
						
						if(item.getExistDiscount()!=null&&item.getExistDiscount()) {
							RABATT_7 += item.getDiscountAmt();
						}
						 
					} else if(item.getTaxRate().compareTo(0.0) == 0){	
						System.out.println("(item.getTaxRate().compareTo(0.0): " + item.getTaxRate().compareTo(0.0));
						TOTAL_WTAX0 += item.getTotalAmount();
						TOTAL_TAX0 += item.getTaxAmount();
						 
					} else {
						System.out.println("bla bla  "+item.getTotalAmount()+" Rat"+item.getTaxRate());
					}

					TOTAL_ITEMS +=item.getItemCount();
					if(item.isPfand()||item.getTotalAmount()<0) {
						if(item.getTaxRate().compareTo(dineIn)==0)
							REFUNDETAX19 += item.getTaxAmount();
						else if(item.getTaxRate().compareTo(homeDelevery)==0)
							REFUNDETAX7 += item.getTaxAmount();
						REFUNDETICKETS+= item.getItemCount();
						REFUNDEAMT += item.getTotalAmount();
						TOTAL_ITEMS -=item.getItemCount();
						
					}

					totalItems += item.getTotalAmount();
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
				TOTAL_PFAND1 += ticket.getPfand();
				TOTAL_PFAND2 += ticket.getPfand2();
				TOTAL_PFAND3 += ticket.getPfand3();

				SOLD_GUTSCHEIN += ticket.getSoldGutschein() ? ticket.getTotalAmount() : 0.00;
				GUTSCHEIN_EINGELOST = ReportUtil.getDiscountByType(ticket.getCouponAndDiscounts(), true, false, false);
				
				if (ticket.getSplitPayment() == Boolean.TRUE) {
					//TOTAL_CARD_PAYMENT += ticket.getCardAmount();
					//TOTAL_CASH_PAYMENT += ticket.getTotalAmount()-ticket.getCardAmount();
					TOTAL_CASH_COUNT++;
					TOTAL_CASH_TAX += totalTax/2;					
					TOTAL_CARD_COUNT++;
					TOTAL_CARD_TAX += totalTax/2;
					
					if(ticket.getCardAmount()!=null) {
						TOTAL_CARD_PAYMENT += ticket.getCardAmount();
						TOTAL_CASH_PAYMENT += ticket.getTotalAmount()-ticket.getCardAmount();  
					}
					if(ticket.getTipAmount()!=null) {
						KARTE_TRINKGELD += ticket.getTipAmount();
					}
					
				}else if (TerminalConfig.isPayTransfer()&&ticket.getRechnugPayemnt() == Boolean.TRUE) {
					TOTAL_RECHNUG_PAYMENT += ticket.getTotalAmount();
					TOTAL_RECHNUG_PAYMENT_COUNT++;
					if(ticket.getTipAmount()!=null) {
						TOTAL_RECHNUG_PAYMENT += ticket.getTipAmount();	
						KARTE_TRINKGELD += ticket.getTipAmount();
					}
					TOTAL_RECHNUG_PAYMENT_TAX += totalTax;
				}else if (ticket.getOnlinePayment() == Boolean.TRUE) {
					TOTAL_ONLINE_PAYMENT += ticket.getTotalAmount();										
					TOTAL_ONLINE_PAYMENT_COUNT++;
					if(ticket.getTipAmount()!=null) {
						TOTAL_ONLINE_PAYMENT += ticket.getTipAmount();	
						KARTE_TRINKGELD += ticket.getTipAmount();
					}
					TOTAL_ONLINE_TAX += totalTax;
					
				/*} else if (ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment()) {
					if(ticket.getTipAmount()!=null) {
						GUTS_TRINKGELD += ticket.getTipAmount();
					}*/

				}  else if (ticket.getCashPayment() == Boolean.TRUE) {
					TOTAL_CASH_PAYMENT += ticket.getTotalAmount();
					TOTAL_CASH_COUNT++;
					if(ticket.getTipAmount()!=null) {
						TOTAL_CASH_PAYMENT += ticket.getTipAmount();
						BAR_TRINKGELD += ticket.getTipAmount();
					}
					TOTAL_CASH_TAX += totalTax;
				} else {					
					TOTAL_CARD_PAYMENT += ticket.getTotalAmount();
					if(ticket.getTipAmount()!=null) {
						TOTAL_CARD_PAYMENT += ticket.getTipAmount();
						KARTE_TRINKGELD += ticket.getTipAmount();
					}
					TOTAL_CARD_TAX += totalTax;					 
					TOTAL_CARD_COUNT++;
					 
				}

				TICKET_GUTSCHEIN_AMOUNT += GUTSCHEIN_EINGELOST;
				TAX_TOT += totalTax;
				
				
				//delete master storno article start
				List<KitchenTicket> kVoidListMaster_ = KitchenTicketDAO.getInstance().deletMasterVoidArt(ticket.getId());				
				if(kVoidListMaster_!=null) {
					for (KitchenTicket ticket11:kVoidListMaster_) {	
						KitchenTicketItemDAO.getInstance().delete(ticket11);						 
					}					 
				}
				//delete master storno article finish
				
				List<KitchenTicket> kVoidList_ = KitchenTicketDAO.getInstance().findVoided(ticket.getId());
				if(kVoidList_!=null) {
					for (KitchenTicket ticket1:kVoidList_) {	
						 
						VOIDARTICLE += ticket1.getItemCount();
						VOIDAMT += ticket1.getTotalAmount();	
						System.out.println();
					}
				}
				
				ticket = null;
				iter.remove();
			}
			
			BAR_TRINKGELD = NumberUtil.roundToTwoDigit(BAR_TRINKGELD);
			KARTE_TRINKGELD = NumberUtil.roundToTwoDigit(KARTE_TRINKGELD);
			SOLD_GUTSCHEIN = NumberUtil.roundToTwoDigit(SOLD_GUTSCHEIN);
			TICKET_GUTSCHEIN_AMOUNT = NumberUtil.roundToTwoDigit(TICKET_GUTSCHEIN_AMOUNT);
			
			ReportService reportService = new ReportService();
			//MenuUsageReport report1;
			if(rest.isWithWarengroup())
				report = reportService.getMenuUsageReportTicketIts(
						BusinessDateUtil.startOfOfficialDay(getStartDate()),
						BusinessDateUtil.endOfOfficialDay(getStartDate()), 1, false, menuItems);
			else {
				report = new MenuUsageReport();
				MenuUsageReportData data = new MenuUsageReportData(); 
				data.setGrossSales(0.0);
				data.setDiscount(0.0);
				data.setCount(0);
				data.setNetSales(0.0);
				data.setProfit(0.0);
				report.addReportData(data);
			}

		}else {
			report = new MenuUsageReport();
			MenuUsageReportData data = new MenuUsageReportData(); 
			data.setGrossSales(0.0);
			data.setDiscount(0.0);
			data.setCount(0);
			data.setNetSales(0.0);
			data.setProfit(0.0);
			data.setCategoryName("");
			report.addReportData(data);			
		}

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
			VOIDTICKETS++;
			VOIDAMT += ticket.getTotalAmount();
		} 


		/*List<KitchenTicket> kVoidList = KitchenTicketDAO.getInstance().findAllVoided(date1, date2);
		if(kVoidList!=null) {
			for (KitchenTicket ticket1:kVoidList) {	
				 
				VOIDARTICLE += ticket1.getItemCount();
				VOIDAMT += ticket1.getTotalAmount();				
			}
		} */

		
		String cash19Text = "Bar-Zahlung "+nineteen+"%";
		String cash7Text =  "Bar-Zahlung "+seven+"%";
		String card19Text =  "Karte-Zahlung "+nineteen+"%";
		String card7Text = "Karte-Zahlung "+seven+"%";
		//
		//		List<Ticket> Refundtickets = TicketDAO.getInstance().findRefundedTickets(date1, date2);
		//		for (Iterator refunded = Refundtickets.iterator(); refunded.hasNext();) {
		//			Ticket t = (Ticket) refunded.next();
		//			Ticket ticket = TicketDAO.getInstance().loadFullTicket(t.getId());
		//
		//			REFUNDETAX19 += ticket.getRefunTax19();
		//			REFUNDETAX7 += ticket.getRefunTax7();
		//			REFUNDETICKETS+= ticket.getRefundCount();
		//			REFUNDEAMT += ticket.getRefunAmount();
		//		}
		if(!gen) {
			ReportItem reportItem = new ReportItem();
			reportItem.setId("");
			//reportItem.setAwt(TOTAL_WTAX);
			//gutschein 
			reportItem.setAwt(TOTAL_WTAX+SOLD_GUTSCHEIN+TOTAL_DISCOUNT);
			//gutschein
			//reportItem.setAwt(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0);
			reportItem.setAwt19(TOTAL_WTAX19);
			reportItem.setAwt0(TOTAL_WTAX0);
			//	if (TerminalConfig.isSupermarket()) {
			reportItem.setAwt7(TOTAL_WTAX7);
			reportItem.setAwt0(TOTAL_WTAX0);
			//	}

			reportItem.setttd(TOTAL_TAX19);
			NETTO_19 = TOTAL_WTAX19 - TOTAL_TAX19;
			NETTO_7 = TOTAL_WTAX7 - TOTAL_TAX7;
			NETTO_0 = TOTAL_WTAX0;
			reportItem.setAwot(NETTO_19+NETTO_7+NETTO_0);
			//	if (TerminalConfig.isSupermarket()) {
			reportItem.settts(TOTAL_TAX7);
			reportItem.setttz(TOTAL_TAX0);
			reportItem.setNettos(NETTO_7);
			reportItem.setNettoz(NETTO_0);
			//	}   

			reportItem.setCashPayment(TOTAL_CASH_PAYMENT);
			reportItem.setCashTax(TOTAL_CASH_TAX);
			reportItem.setCashPaymentCount(TOTAL_CASH_COUNT);
			reportItem.setTotalInvoices(TOTAL_INVOICES);
			reportItem.setTotalSoldItems(TOTAL_ITEMS);
			reportItem.setBtrinkgeld(BAR_TRINKGELD);
			reportItem.setRabatt_19(RABATT_19);
			reportItem.setRabatt_7(RABATT_7);
			reportItem.setKtrinkgeld(KARTE_TRINKGELD+GUTS_TRINKGELD);
			reportItem.setSoldGutschein(SOLD_GUTSCHEIN);
			reportItem.setTicketGutscheinAmount(TICKET_GUTSCHEIN_AMOUNT); 
			 
			reportItem.setVoidArticles(VOIDARTICLE);
			//reportItem.setVoidArticles(VOIDARTICLE);
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
			reportItem.setGesamtSumme(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0-TOTAL_PFAND1-TOTAL_PFAND2-TOTAL_PFAND3-TICKET_GUTSCHEIN_AMOUNT-TOTAL_DISCOUNT);
			reportItem.setCardPayment(TOTAL_CARD_PAYMENT);
			reportItem.setGoodscheinCardPayment_text(card7Text);
			 
			
			reportItem.setGoodscheinCardPayment(TOTAL_CARD_PAYMENT_GUDSCHEIN);
			reportItem.setGoodscheinCardTax(TOTAL_GUDSCHEIN_CARD_TAX);
			reportItem.setGoodscheinCardPaymentCount(TOTAL_GUDSCHEIN_CARD_COUNT);
			
			reportItem.setCardTax(TOTAL_CARD_TAX);
			reportItem.setCardPaymentCount(TOTAL_CARD_COUNT);
			reportItem.setCash19(totalBar19);
			reportItem.setCash7(totalBar7);
			reportItem.setCard19(totalCard19);
			reportItem.setCard7(totalCard7);

			if(TerminalConfig.isWholeSale()||TerminalConfig.isPayTransfer()) {
				reportItem.setRechnugPament_anzahl(TOTAL_RECHNUG_PAYMENT_COUNT+"");
				reportItem.setRechnugPayment_tax(String.valueOf(NumberUtil.roundToTwoDigit(TOTAL_RECHNUG_PAYMENT_TAX))+" €");
				reportItem.setRechnugPaymentAmount(String.valueOf(NumberUtil.roundToTwoDigit(TOTAL_RECHNUG_PAYMENT))+" €");

				reportItem.setMwst_gesamt_text_rechnug(POSConstants.SALES_VAT_TOTAL);
				reportItem.setRechnugPayment_text(POSConstants.BILL+"-Zahlung");			
				reportItem.setAnzahl_text_rechnug(POSConstants.SALES_COUNT);

				reportItem.setOnlinePament_anzahl(TOTAL_ONLINE_PAYMENT_COUNT+"");
				reportItem.setOnlinePayment_tax(String.valueOf(NumberUtil.roundToTwoDigit(TOTAL_ONLINE_TAX))+" €");
				reportItem.setOnlinePaymentAmount(String.valueOf(NumberUtil.roundToTwoDigit(TOTAL_ONLINE_PAYMENT))+" €");

				reportItem.setMwst_gesamt_text_online(POSConstants.SALES_VAT_TOTAL);
				reportItem.setOnlinePayment_text("Online-Zahlung");
				reportItem.setAnzahl_text_online(POSConstants.SALES_COUNT);

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
			reportItem.setVerkaufte_gutschein_text(POSConstants.SALES_COUPON);
			reportItem.setGutschein_eingelost_text(POSConstants.SALES_COUPON_REDEEMED);
			reportItem.setBar_tip_text(POSConstants.SALES_CASH_TIP);
			reportItem.setOther_tip_text(POSConstants.SALES_OTHER_TIP);
			reportItem.setRabatt_19text(POSConstants.SALES_RABATT_19);
			reportItem.setRabatt_7text(POSConstants.SALES_RABATT_7);
			
			
			reportItem.setCardpayment_text(POSConstants.SALES_CARD_PAYMENT);
			reportItem.setKunden_rabatt_text(POSConstants.SALES_DISCOUNT);
			reportItem.setCash_in_cashdrawer_text(POSConstants.SALES_CASH_CASDRAWER);
			reportItem.setGesamt_summe_text(POSConstants.SALES_SUM_TOTAL);
			reportItem.setWarengroup_abs_text(POSConstants.SALES_GOODS_ACOUNTING);
			reportItem.setWarengroup_text(POSConstants.SALES_GOODS);
			reportItem.setGesamt_text(POSConstants.SALES_TOTAL);
			reportItem.setCash19Text(cash19Text);
			reportItem.setCash7Text(cash7Text);
			reportItem.setCard19Text(card19Text);
			reportItem.setCard7Text(card7Text);
			System.out.println(TOTAL_TAX7+" "+ date1+date2+tickets.size()+TOTAL_WTAX7);
			itemMap.put("1", reportItem);
			itemReportModel = new SalesReportModel();
			itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
		}

		List<Salesreportdb> salesList = null;
		System.out.println("Sales id  "+salesId);
		try {
			salesList = SalesReportDAO.getInstance().findById(salesId);
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		sales = salesList != null && !salesList.isEmpty() ? salesList.get(0) : new Salesreportdb();
		if (sales.getId() == null || sales.getId() == 0) {
			sales.setId(salesId);
		}
		sales.setSalesid(salesId);
		sales.setStartdate(getStartDate());
		sales.setEnddate(dateNow);
		sales.setReporttime(reportTime);
		sales.setAwt(TOTAL_WTAX+SOLD_GUTSCHEIN);
		sales.setAwt(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0+SOLD_GUTSCHEIN);
		sales.setAwtn(TOTAL_WTAX19);
		sales.setAwtz(SOLD_GUTSCHEIN);
		sales.setAwts(TOTAL_WTAX7);
		sales.setSoldGutschein(SOLD_GUTSCHEIN);
		sales.setCashpayment(TOTAL_CASH_PAYMENT);
		sales.setCashpaymentcount(TOTAL_CASH_COUNT);
		sales.setCashtax(TOTAL_CASH_TAX);
		sales.setBtrinkgeld(BAR_TRINKGELD);
		sales.setKtrinkgeld(KARTE_TRINKGELD+GUTS_TRINKGELD); 
		sales.setTicketgutscheinamount(TICKET_GUTSCHEIN_AMOUNT);
		sales.setCardpayment(TOTAL_CARD_PAYMENT);
		sales.setCardpaymentcount(TOTAL_CARD_COUNT);
		sales.setCardtax(TOTAL_CARD_TAX);
		sales.setVoidticket((double) VOIDTICKETS);
		sales.setVoidamount(VOIDAMT);
		sales.setVoidArticles(VOIDARTICLE);
		//		sales.setFood(FOOD_TAX);
		//		sales.setFoodtax(FOOD_TAX);
		//		sales.setBeverage(BEVERAGE);
		//		sales.setBeveragetax(BEVERAGE_TAX);
		sales.setTotalwotax(TOTAL_WOTAX);
		sales.setTaxtotal(TAX_TOT);
		sales.setTaxn(TOTAL_TAX19);
		sales.setTaxs(TOTAL_TAX7);
		sales.setTaxz(TOTAL_TAX0);
		sales.setCancelledtrans(VOIDAMT);
		sales.setCancelledtax(0.00);
		sales.setDiscount(TOTAL_DISCOUNT);
		sales.setRabatt_19(RABATT_19);
		sales.setRabatt_7(RABATT_7);
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
		sales.setAnzahlRetour(REFUNDETICKETS);
		sales.setRetourGesamt(REFUNDEAMT);
		sales.setRetourTax(REFUNDETAX19+REFUNDETAX7);
		sales.setGesamtMwst19(TOTAL_WTAX19+REFUNDETAX19);
		sales.setGesamtMwst7(TOTAL_TAX7+REFUNDETAX7);
		sales.setGesamtSumme(TOTAL_WTAX19+TOTAL_WTAX7+TOTAL_WTAX0-TOTAL_PFAND1-TOTAL_PFAND2-TOTAL_PFAND3-TOTAL_DISCOUNT);
		if(TerminalConfig.isWholeSale()||TerminalConfig.isPayTransfer()) {
			sales.setRechnugPament_anzahl(TOTAL_RECHNUG_PAYMENT_COUNT);
			sales.setRechnugPayment_tax(NumberUtil.roundToTwoDigit(TOTAL_RECHNUG_PAYMENT_TAX));
			sales.setRechnugPaymentAmount(NumberUtil.roundToTwoDigit(TOTAL_RECHNUG_PAYMENT));
			sales.setOnline(TOTAL_ONLINE_PAYMENT);
			sales.setOnlinePament_anzahl(TOTAL_ONLINE_PAYMENT_COUNT);
			sales.setOnlinetax(TOTAL_ONLINE_TAX);
		}
		sales.setGesamt_19_text(POSConstants.SALES_TOTAL+" "+nineteen);
		sales.setGesamt_7_text(POSConstants.SALES_TOTAL+" "+seven);
		sales.setGesamt_0_text(POSConstants.SALES_TOTAL+" 0%");
		sales.setNetto_19_text(POSConstants.SALES_NET+" "+nineteen);
		sales.setNetto_7_text(POSConstants.SALES_NET+" "+seven);
		sales.setNetto_0_text(POSConstants.SALES_NET+" 0%");
		sales.setMwst_19_text(POSConstants.SALES_VAT+" "+nineteen);
		sales.setMwst_7_text(POSConstants.SALES_VAT+" "+seven);
		sales.setMwst_0_text(POSConstants.SALES_VAT+" 0%");
		sales.setCash19Text(cash19Text);
		sales.setCash7Text(cash7Text);
		sales.setCard19Text(card19Text);
		sales.setCard7Text(card7Text);
		sales.setCash19(totalBar19);
		sales.setCash7(totalBar7);
		sales.setCard19(totalCard19);
		sales.setCard7(totalCard7);	

		List<MenuUsage> menuUsages = new ArrayList<>();
		try {
			report.getReportDatas().stream().forEach(data -> {
				MenuUsage menuUsage = new MenuUsage();
				menuUsage.setCategory(data.getCategoryName());
				menuUsage.setCount(data.getCount());
				menuUsage.setTax(data.getTax());
				menuUsage.setGrossSales(data.getGrossSales());
				menuUsages.add(menuUsage);
				MenuUsageDAO.getInstance().saveOrUpdate(menuUsage);				
			});
		}catch(Exception ex) {

		}
		sales.setMenuUsages(menuUsages);		

		return true;
	}

	Double totalBar19 = 0.00;
	Double totalBar7 = 0.00;
	Double totalCard19 = 0.00;
	Double totalCard7 = 0.00;
	private void calculateZahlungOld(List<Ticket> ticketList) {
		
		totalCard7 = 0.00;
		totalBar7 = 0.00;
		totalBar19 = 0.00;
		totalCard19 = 0.00;
		for(Ticket ticket: ticketList) {			
			if(ticket.getTicketType()!=null&&!ticket.isVoided()) {				
				if(ticket.getSplitPayment() != null &&ticket.getSplitPayment()) {
					if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {
						totalBar19 += (ticket.getTotalAmount()-ticket.getCardAmount());
						totalCard19 += ticket.getCardAmount();

					} else {
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
						double Nineteen =0.0;
						double seven =0.0;
						double cardAmnt =ticket.getCardAmount();
						double barAmnt =ticket.getTotalAmount()-ticket.getCardAmount();
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.isBeverage()) {
								Nineteen += item.getSubtotalAmount(); 
							} else {
								seven += item.getSubtotalAmount();
							}
						}
						
						if(cardAmnt<Nineteen) {
							totalCard19 += cardAmnt;
							totalBar19 += Nineteen-cardAmnt;
							totalBar7 += seven;
						}else if(cardAmnt<seven) {
							totalCard7 += cardAmnt;
							totalBar7 += seven-cardAmnt;
							totalBar19 += Nineteen;
						}else if(barAmnt<Nineteen) {
							totalBar19 += barAmnt;
							totalCard19 += Nineteen-barAmnt;
							totalCard7 += seven;
						}else if(barAmnt<seven) {
							totalBar7 += barAmnt;
							totalCard7 += seven-barAmnt;
							totalCard19 += Nineteen;
						}else {
							totalBar7 += seven/2;
							totalBar19 +=  Nineteen/2;
							totalCard7 += seven/2;
							totalCard19 += Nineteen/2;
						}
						//						
						//							if(ticket.getTipAmount()!=null&&ticket.getTipAmount()>0) {
						//								totalBar19 -= ticket.getTipAmount();
						//							}
					}

				}else if(ticket.getCashPayment()!=null&&ticket.getCashPayment()) {
					if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {
						if (ticket.getCashPayment() != null && ticket.getCashPayment()) { 
							totalBar19 += ticket.getTotalAmount();	
						}
					} else {						
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.isBeverage()) {
								totalBar19 += item.getSubtotalAmount(); 
							} else {
								totalBar7 += item.getSubtotalAmount();
							}
						}						
					}

				} else {
					if (ticket.getTicketType().compareTo(TicketType.DINE_IN.name()) == 0) {					
						totalCard19 += ticket.getTotalAmount();							
					} else {
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.isBeverage()) {
								totalCard19 += item.getSubtotalAmount(); 
							} else {
								totalCard7 += item.getSubtotalAmount();
							}
						}
					}
					//					if(ticket.getTipAmount()!=null&&ticket.getTipAmount()>0) {
					//						totalBar19 -= ticket.getTipAmount();
					//					}
				}
			}
		}
	}


	private void calculateZahlung(List<Ticket> ticketList) {
		totalCard7 = 0.00;
		totalBar7 = 0.00;
		totalBar19 = 0.00;
		totalCard19 = 0.00;

		for(Ticket ticket: ticketList) {
			if(ticket.getType()!=null&&!ticket.isVoided()) {
				ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
				
				if(ticket.getSplitPayment() != null &&ticket.getSplitPayment()) {
											double Nineteen =0.0;
						double seven =0.0;
						double cardAmnt =ticket.getCardAmount();
						double barAmnt =ticket.getTotalAmount()-ticket.getCardAmount();
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.getTaxRate().compareTo(dineIn)==0) {
								Nineteen += item.getTotalAmount(); 
							} else {
								seven += item.getTotalAmount();
							}
						}

						if(cardAmnt<Nineteen) {
							totalCard19 += cardAmnt;
							totalBar19 += Nineteen-cardAmnt;
							totalBar7 += seven;
						} else if(cardAmnt<seven) {
							totalCard7 += cardAmnt;
							totalBar7 += seven-cardAmnt;
							totalBar19 += Nineteen;
						} else if(barAmnt<Nineteen) {
							totalBar19 += barAmnt;
							totalCard19 += Nineteen-barAmnt;
							totalCard7 += seven;
						} else if(barAmnt<seven) {
							totalBar7 += barAmnt;
							totalCard7 += seven-barAmnt;
							totalCard19 += Nineteen;
						} else {
							totalBar7 += seven/2;
							totalBar19 +=  Nineteen/2;
							totalCard7 += seven/2;
							totalCard19 += Nineteen/2;
						}

				}else if(ticket.getCashPayment()!=null&&ticket.getCashPayment()) {
					if(TerminalConfig.isSpecial()) {
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.getTaxRate().compareTo(homeDelevery)==0) {
								totalBar7 += item.getTotalAmount();
							} else {
								totalBar19 += item.getTotalAmount();
							}
						}
					} else {						
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.getTaxRate().compareTo(dineIn)==0) {
								totalBar19 += item.getTotalAmount(); 
							} else {
								totalBar7 += item.getTotalAmount();
							}
						}						
					}
					

				} else {
					if(TerminalConfig.isSpecial()) {
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.getTaxRate().compareTo(homeDelevery)==0) {
								totalCard7 += item.getTotalAmount();
							} else {
								totalCard19 += item.getTotalAmount();
							}
						}
					}else {
						ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
						for(TicketItem item:ticket.getTicketItems()) {
							if (item.isBeverage()) {
								totalCard19 += item.getTotalAmount(); 
							} else {
								totalCard7 += item.getTotalAmount();
							}
						}
					}
					//					if(ticket.getTipAmount()!=null&&ticket.getTipAmount()>0) {
					//						totalBar19 -= ticket.getTipAmount();
					//					}
				}
			}
		}
		System.out.println("calucated "+totalCard19+" "+totalBar19+" "+totalBar7+" "+totalCard7);

	}

	public void doAutoKassenBook(Restaurant rest) {
		SimpleDateFormat format = new SimpleDateFormat("MMMM, yyyy");
		Calendar calll = Calendar.getInstance();
		calll.setTime(DateUtils.startOfDay(getStartDate()));
		calll.set(Calendar.HOUR_OF_DAY, new Date().getHours());
		String formatdate = format.format(calll.getTime());

		if(totalBar19>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Bar "+dineIn+"% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8400);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalBar19));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		if(totalBar7>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Bar "+homeDelevery+"% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8300);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalBar7));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}

		if(totalCard19>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Karte "+dineIn+"% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8400);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalCard19));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
		if(totalCard7>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Karte "+homeDelevery+"% - 1");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(true);
			cashbookInf.setKonto(8300);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalCard7));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}

		if(totalCard19>0.00||totalCard7>0.00) {
			Cashbook cashbookInf = new Cashbook();
			cashbookInf.setBeschreibung("Karte Zahlung");
			cashbookInf.setBezeichnung(formatdate);
			cashbookInf.setAdddata(false);
			cashbookInf.setKonto(2000);
			cashbookInf.setSalesId(rest.getSalesid());
			cashbookInf.setDate(calll.getTime());
			cashbookInf.setBetrag(Math.abs(totalCard7+totalCard19));			
			CashbookDAO.getInstance().saveOrUpdate(cashbookInf);
		}
	}

	private final boolean tseTier3 = TerminalConfig.isTseTier3();	
	private final boolean  tseEnable = TerminalConfig.isTseEnable();


	public void tseForceFinish(Ticket ticket) {
		if(tseEnable&&tseTier3&&ticket.getTseReceiptDataId()==null) {
			try {				
				int paymentType = 0;
				if(ticket.getCashPayment())
					paymentType = PaymentType.CASH.ordinal();
				else
					paymentType = PaymentType.CARD.ordinal();

				TseTicketService.getTseService().forceRestartOrder(ticket);
				TSEReceiptData receiptData = TseTicketService.getTseService().initFinishTseOrder(ticket, paymentType);
				TSEReceiptDataDAO.getInstance().saveOrUpdate(receiptData);					
				ticket.setTseReceiptDataId(receiptData.getId());
				ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
				ticket.setDrawerResetted(true);
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}catch (Exception e) {

			}finally {

			}
		}
	}
}
