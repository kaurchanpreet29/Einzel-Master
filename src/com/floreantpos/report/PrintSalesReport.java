package com.floreantpos.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
@XmlRootElement(name = "SalesReport")
public class PrintSalesReport{
	private SalesReportModel itemReportModel;

	String restaurantname;
	String addressline1;
	String addressline2;
	String addressline3;
	String phone;
	String taxnr;
	
	String id;
	String startdate;
	String enddate;                 
	String reporttime;
	
	Double speisen;
	Double speisentax;
	Double total;
	Double tax19;
	Double tax7;
	Double cash;
	Double cashtax;
	Double card;
	Double cardtax;

	public void setRestaurantname(String restaurantname)
	{
		this.restaurantname = restaurantname;
	}
	public String getRestaurantname()
	{
		return restaurantname;
	}
	public void setAddressline1(String addressline1)
	{
		this.addressline1 = addressline1;
	}
	public String getAddressline1()
	{
		return addressline1;
	}
	public void setAddressline2(String addressline2)
	{
		this.addressline2 = addressline2;
	}
	public String getAddressline2()
	{
		return addressline2;
	}
	public void setAddressline3(String addressline3)
	{
		this.addressline3 = addressline3;
	}
	public String getAddressline3()
	{
		return addressline3;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setTaxnr(String taxnr)
	{
		this.taxnr = taxnr;
	}
	public String getTaxnr()
	{
		return taxnr;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
	public void setStartdate(String startdate)
	{
		this.startdate = startdate;
	}
	public String getStartdate()
	{
		return startdate;
	}
	public void setEnddate(String enddate)
	{
		this.enddate = enddate;
	}
	public String getEnddate()
	{
		return enddate;
	}
	public void setReporttime(String reporttime)
	{
		this.reporttime = reporttime;
	}
	public String getReporttime()
	{
		return reporttime;
	}
	public void setSpeisen(Double speisen)
	{
		this.speisen = speisen;
	}
	public Double getSpeisen()
	{
		return speisen;
	}
	public void setSpeisentax(Double speisentax)
	{
		this.speisentax = speisentax;
	}
	public Double getSpeisentax()
	{
		return speisentax;
	}
	public void setTotal(Double total)
	{
		this.total = total;
	}
	public Double getTotal()
	{
		return total;
	}
	public void setTax19(Double tax19)
	{
		this.tax19 = tax19;
	}
	public Double getTax19()
	{
		return tax19;
	}
	public void setTax7(Double tax7)
	{
		this.tax7 = tax7;
	}
	public Double getTax7()
	{
		return tax7;
	}
	public void setCash(Double cash)
	{
		this.cash = cash;
	}
	public Double getCash()
	{
		return cash;
	}
	public void setCashtax(Double cashtax)
	{
		this.cashtax = cashtax;
	}
	public Double getCashtax()
	{
		return cashtax;
	}
	public void setCard(Double card)
	{
		this.card = card;
	}
	public Double getCard()
	{
		return card;
	}
	public void setCardtax(Double cardtax)
	{
		this.cardtax = cardtax;
	}
	public Double getCardtax()
	{
		return cardtax;
	}
	public void printData() {
		try
		{
		createModels();

		SalesReportModel itemReportModel = this.itemReportModel;
		JasperReport itemReport;
		itemReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/report/template/sales_sub_report_short.jasper"));
		
		HashMap map = new HashMap();
		map.put("reportTitle", "Z-ABSCHLAG Nr.: "+ id);
	    map.put("reportTime", this.reporttime);
        map.put("dateRange", "von " + this.startdate);
		map.put("dateRange1","bis " + this.enddate);
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("currencySymbol", "€");
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		
		map.put("restaurantName", restaurantname);
		map.put("addressLine1", addressline1);
		map.put("addressLine2", addressline2);
		map.put("addressLine3", addressline3);
		map.put("phone", "Tel.:"+phone);
		map.put("taxNr", "Steuer Nr.:"+taxnr);
		
		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/sales_report.jasper"));
		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		print.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		JReportPrintService.printQuitely(print);
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void createModels() {
		
		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
		ReportItem reportItem = new ReportItem();
		reportItem.setId("");
		reportItem.setAwt(this.total);
		reportItem.setAwt19(tax19);
		reportItem.setAwt7(tax7);
		reportItem.setCashPayment(cash);
		reportItem.setCashTax(cashtax);
		reportItem.setFood(speisen);		
		reportItem.setFoodTax(speisentax);
		reportItem.setCardPayment(card);
		reportItem.setCardTax(cardtax);
		itemMap.put("1", reportItem);
		itemReportModel = new SalesReportModel();
		itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
	}
	public static PrintSalesReport importKunden(File file) 
	{
		try {

			if (!file.exists()) {	
				return null;
			}

			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "iso-8859-1");
			JAXBContext jaxbContext = JAXBContext.newInstance(PrintSalesReport.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			PrintSalesReport printSalesReport = (PrintSalesReport) unmarshaller.unmarshal(reader);
			
			if(printSalesReport == null)
			{
				return null;
			}
			
			return printSalesReport;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void PrintDataFromDb(int id,Date date) throws JRException
	{
		System.out.println("Entered");
		
		System.out.println(date);
		
		Salesreportdb salesReport = null;
		Restaurant restaurant = RestaurantDAO.getRestaurant();
		List<Salesreportdb> saleslist;
		if(id != 0)
			saleslist = SalesReportDAO.getInstance().findById(id);
		else
			saleslist = SalesReportDAO.getInstance().findByDate(date);
		
		
		for(Iterator<Salesreportdb> itr = saleslist.iterator();itr.hasNext();)
		{
			salesReport = itr.next();
			System.out.println(salesReport.getStartdate());
			break;
		}
		if(salesReport == null)
		{
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), "Keine Informationen");
			return;
		}
		
		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
		ReportItem reportItem = new ReportItem();
		reportItem.setId("");
		if(salesReport.getAwt() != null)
			reportItem.setAwt(salesReport.getAwt());
		if(salesReport.getAwtn() != null)
			reportItem.setAwt19(salesReport.getAwtn());
		if(salesReport.getAwts() != null)
			reportItem.setAwt7(salesReport.getAwts());
		if(salesReport.getCashpayment() != null)
			reportItem.setCashPayment(salesReport.getCashpayment());
		if(salesReport.getCashtax() != null)
			reportItem.setCashTax(salesReport.getCashtax());
		
		Double food_ = salesReport.getFood() + salesReport.getBeverage();
		Double foodTax_ = salesReport.getFoodtax() + salesReport.getBeveragetax();
		
		reportItem.setFood(food_);		
		reportItem.setFoodTax(foodTax_);
		
		Double card_ = salesReport.getCardpayment() + salesReport.getOnline();
		Double cardTax_ = salesReport.getCardtax() + salesReport.getOnlinetax();
		reportItem.setCardPayment(card_);
		reportItem.setCardTax(cardTax_);
		itemMap.put("1", reportItem);
		SalesReportModel itemReportModel = new SalesReportModel();
		itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
		
		JasperReport itemReport;
		itemReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/report/template/sales_sub_report_short.jasper"));
		
		HashMap map = new HashMap();
		map.put("reportTitle", "Z-ABSCHLAG Nr.: "+ salesReport.getSalesid());
	    
		if(salesReport.getReporttime() != null)
			map.put("reportTime", salesReport.getReporttime());
		
		if(salesReport.getStartdate() != null)
		{
			map.put("dateRange", "von " + ReportService.formatShortDate(salesReport.getStartdate()));
		}
		
		if(salesReport.getEnddate() != null)
			map.put("dateRange1","bis " + ReportService.formatShortDate(salesReport.getEnddate()));
		
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("currencySymbol", "€");
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
	
		if(restaurant.getName() != null)
			map.put("restaurantName", restaurant.getName());
		
		if(restaurant.getAddressLine1() != null)
			map.put("addressLine1", restaurant.getAddressLine1());
		if(restaurant.getAddressLine2() != null)
			map.put("addressLine2", restaurant.getAddressLine2());
		if(restaurant.getAddressLine3() != null)
			map.put("addressLine3", restaurant.getAddressLine3());
		if(restaurant.getTelephone() != null)
			map.put("phone", "Tel.:"+ restaurant.getTelephone());
		if(restaurant.getTicketFooterMessage2() != null)
			map.put("taxNr", "Steuer Nr.:"+restaurant.getTicketFooterMessage2());
		
		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/sales_report.jasper"));
		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		print.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		JReportPrintService.printQuitely(print);
	}
}
