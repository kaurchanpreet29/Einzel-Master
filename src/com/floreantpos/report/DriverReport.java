package com.floreantpos.report;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import org.hibernate.Query;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.model.SalesId;
import com.floreantpos.main.Application;
import com.floreantpos.model.Tax;
import com.floreantpos.model.Terminal;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TerminalDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

public class DriverReport extends Report {
	private DriverReportModel driverReportModel;
	private String currencySymbol;

	public DriverReport() {
		super();
	}

	@Override
	public void refresh() throws Exception {
		
		HashMap map = new HashMap();
		createModels();

		DriverReportModel driverReportModel = this.driverReportModel;
		JasperReport itemReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/report/template/driver_sub_report.jasper"));
		map.put("reportTitle", "Fahrer Abschluss" );
		ReportUtil.populateRestaurantProperties(map);
		
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        
        Date date = new Date();
        int month = c.get(Calendar.MONTH )+1;
        map.put("dateRange", "von " + ReportService.formatShortDate(getStartDate()));
		map.put("dateRange1","bis " + ReportService.formatShortDate(getEndDate()));
		map.put("reportTime", c.get(Calendar.DATE) + "." + month + "." + c.get(Calendar.YEAR) +"," + date.getHours() + ":" + date.getMinutes() + " Uhr");
		map.put("itemDataSource", new JRTableModelDataSource(driverReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemReport", itemReport);
		
		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/driver_report.jasper"));

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JREmptyDataSource());
		viewer = new net.sf.jasperreports.swing.JRViewer(print);
		viewer.getReportPanel().setBackground(new Color(209,222,235));
		
	}

	@Override
	public boolean isDateRangeSupported() {
		return true;
	}

	@Override
	public boolean isTypeSupported() {
		return true;
	}

	public void createModels() {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());
		currencySymbol = Application.getCurrencySymbol();
		Double TotalDriverAmount = 0.00;
		int totalDelivery = 0;
		int totalNr = 0;
		int totalNrOnline = 0;
		HashMap<String, DriverData> map = new HashMap();
		String driverName1 = "",driverName2 = "",driverName3 ="",driverName4="",driverName5="",driverName6="",driverName7="",driverName8="",driverName9 = "";
		Double driverAmount1 = null,driverAmount2=null,driverAmount3=null,driverAmount4=null,driverAmount5=null,driverAmount6=null,driverAmount7=null,driverAmount8=null,driverAmount9 = null;
		long driverDelivery1 = 0,driverDelivery2=0,driverDelivery3=0,driverDelivery4=0,driverDelivery5=0,driverDelivery6=0,driverDelivery7=0,driverDelivery8=0,driverDelivery9 = 0;
		List<User> driversList = UserDAO.getInstance().findDrivers();
		int nameIndex = 1;
		Double totalAmount = 0.00;
		for (Iterator itr = driversList.iterator();itr.hasNext();)
		{
			User user = (User)itr.next();
			
			TotalDriverAmount = 0.00;
			totalDelivery = 0;
			
			List<Ticket> tickets = TicketDAO.getInstance().findTicketsOpen(date1, date2,false);
		
			for (Iterator ticketItr = tickets.iterator(); ticketItr.hasNext();)
			{
				Ticket ticket = (Ticket)ticketItr.next();
				if (ticket.getAssignedDriver() != null && ((ticket.getCashPayment() == null) || (ticket.getCashPayment() == false)))
				{
					if (ticket.getAssignedDriver().getFirstName().compareTo(user.getFirstName()) == 0)
					{
						totalNrOnline++;
					}
				}
				if (ticket.getAssignedDriver() != null && ticket.getCashPayment()!= null && ticket.getCashPayment() == true)
				{
				    if (ticket.getAssignedDriver().getFirstName().compareTo(user.getFirstName()) == 0)
				    {
				    	TotalDriverAmount += ticket.getTotalAmount();
				    	totalAmount += ticket.getTotalAmount();
				    	totalNr++;
				    	totalDelivery++;
				    }
				    
				}
				
				
			}
			if (nameIndex == 1){driverName1 = user.getFirstName(); driverAmount1 = TotalDriverAmount; driverDelivery1 = totalDelivery;}
			if (nameIndex == 2){driverName2 = user.getFirstName(); driverAmount2 = TotalDriverAmount;driverDelivery2 = totalDelivery;}
			if (nameIndex == 3){driverName3 = user.getFirstName(); driverAmount3 = TotalDriverAmount;driverDelivery3 = totalDelivery;}
			if (nameIndex == 4){driverName4 = user.getFirstName(); driverAmount4 = TotalDriverAmount;driverDelivery4 = totalDelivery;}
			if (nameIndex == 5){driverName5 = user.getFirstName(); driverAmount5 = TotalDriverAmount;driverDelivery5 = totalDelivery;}
			if (nameIndex == 6){driverName6 = user.getFirstName(); driverAmount6 = TotalDriverAmount;driverDelivery6 = totalDelivery;}
			if (nameIndex == 7){driverName7 = user.getFirstName(); driverAmount7 = TotalDriverAmount;driverDelivery7 = totalDelivery;}
			if (nameIndex == 8){driverName8 = user.getFirstName(); driverAmount8 = TotalDriverAmount;driverDelivery8 = totalDelivery;}
			if (nameIndex == 9){driverName9 = user.getFirstName(); driverAmount9 = TotalDriverAmount;driverDelivery9 = totalDelivery;}
						
			nameIndex++;
		}
		DriverData data = new DriverData();
		
		data.setDriverName1(driverName1);
		data.setDriverName2(driverName2);
		data.setDriverName3(driverName3);
		data.setDriverName4(driverName4);
		data.setDriverName5(driverName5);
		data.setDriverName6(driverName6);
		data.setDriverName7(driverName7);
		data.setDriverName8(driverName8);
		data.setDriverName9(driverName9);
		
		if (driverName1 != "")
		{
			data.setDriverAmount1(new String(NumberUtil.formatNumber(driverAmount1))+ " " + currencySymbol);
			data.setDriverDelivery1(driverDelivery1+"");
		}
		if (driverName2 != "")
		{
			data.setDriverAmount2(new String(NumberUtil.formatNumber(driverAmount2))+" " + currencySymbol);
			data.setDriverDelivery2(driverDelivery2+"");
		}
		if (driverName3 != "")
		{
			data.setDriverAmount3(new String(NumberUtil.formatNumber(driverAmount3)+" " + currencySymbol));
			data.setDriverDelivery3(driverDelivery3+"");
		}
		if (driverName4 != "")
		{
			data.setDriverAmount4(new String(NumberUtil.formatNumber(driverAmount4)+" " + currencySymbol));
			data.setDriverDelivery4(driverDelivery4+"");
		}
		if (driverName5 != "")
		{
			data.setDriverAmount5(new String(NumberUtil.formatNumber(driverAmount5)+" " + currencySymbol));
			data.setDriverDelivery5(driverDelivery5+"");
		}
		if (driverName6 != "")
		{
			data.setDriverAmount6(new String(NumberUtil.formatNumber(driverAmount6)+" " + currencySymbol));
			data.setDriverDelivery6(driverDelivery6+"");
		}
		if (driverName7 != "")
		{
			data.setDriverAmount7(new String(NumberUtil.formatNumber(driverAmount7)+" " + currencySymbol));
			data.setDriverDelivery7(driverDelivery7+"");
		}
		if (driverName8 != "")
		{
			data.setDriverAmount8(new String(NumberUtil.formatNumber(driverAmount8)+" " + currencySymbol));
			data.setDriverDelivery8(driverDelivery8+"");
		}
		if (driverName9 != "")
		{
			data.setDriverAmount9(new String(NumberUtil.formatNumber(driverAmount9)+" " + currencySymbol));
			data.setDriverDelivery9(driverDelivery9+"");
		}
		data.setTotalAmount(NumberUtil.formatNumber(totalAmount)+ " "+currencySymbol);
		data.setTotalNr(totalNr + "");
		map.put("1", data);
		driverReportModel = new DriverReportModel();
		driverReportModel.setItems(new ArrayList<DriverData>(map.values()));
	
		
	}

	@Override
	public void refresh(boolean print) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
