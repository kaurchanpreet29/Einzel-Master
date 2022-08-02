package com.floreantpos.report;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.jdesktop.swingx.calendar.DateUtils;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;

public class ServerReport extends Report{
	private ServerReportModel serverReportModel;
	private String currencySymbol;
	private int finalOrders = 0;
	private User user = Application.getCurrentUser();
	private Double finalAmount = 0.0;
	private Double cashAmount = 0.00;
	private int cashCount = 0;
	private int cardCount = 0;
	private Double cardAmount = 0.00;
	private int openCount=0;
	private Double openAmountTotal =0.00;
	@SuppressWarnings("deprecation")
	@Override
	public void refresh() throws Exception {
		HashMap map = new HashMap();
//		finalOrders = 0;
//		finalAmount = 0.0;
		java.util.List<ServerData> dataList = createModels(user);		
		serverReportModel = new ServerReportModel();
		serverReportModel.setList(dataList);
		if(user.isAdministrator())
		map.put("reportTitle", "Mitarbeiter Abschluss" );
		else
		map.put("reportTitle", "Kellner Abschluss" );
	
		ReportUtil.populateRestaurantProperties(map);
		Calendar c = Calendar.getInstance();
        Date date = new Date();
        int month = c.get(Calendar.MONTH )+1;
        
        map.put("dateRange", "Von " + ReportService.formatShortDate(getStartDate()));
		map.put("dateRange1","bis " + ReportService.formatShortDate(getEndDate()));
		map.put("reportTime", c.get(Calendar.DATE) + "." + month + "." + c.get(Calendar.YEAR) +"," + date.getHours() + ":" + date.getMinutes() + " Uhr");
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("bGesamt",finalOrders+"");
		map.put("tGesamt",NumberUtil.formatNumber(finalAmount)+ " " + currencySymbol);
		map.put("cashPayment", NumberUtil.formatNumber(cashAmount) + " " + Application.getCurrencySymbol());
		map.put("cardPayment", NumberUtil.formatNumber(cardAmount) + " " + Application.getCurrencySymbol());
		map.put("cashCount", cashCount + "");
		map.put("cardCount", cardCount + "");
		
		JasperReport masterReport = (JasperReport) JRLoader.loadObject(SalesReport.class.getResource("/com/floreantpos/report/template/server_report.jasper"));

		JasperPrint print = JasperFillManager.fillReport(masterReport, map, new JRTableModelDataSource(serverReportModel));
		this.setJasperPrint(print);
		viewer = new net.sf.jasperreports.swing.JRViewer(print);
		viewer.getReportPanel().setBackground(new Color(209,222,235));
		viewer.setBackground(new Color(209,222,235));
		
	}
	public java.util.List<ServerData> createModels(User currentUser) {
		cashAmount = 0.00;
		cashCount = 0;
		cardCount = 0;
		cardAmount = 0.00;		
		cardAmount = 0.00;
		Date date1 = BusinessDateUtil.startOfOfficialDay(getStartDate());
		Date date2 = BusinessDateUtil.endOfOfficialDay(getEndDate());
		currencySymbol = Application.getCurrencySymbol();		
		java.util.List<ServerData> serverList = new ArrayList();
		if(currentUser.isAdministrator()) {
			java.util.List<User> userList = UserDAO.getInstance().findAll();
			
			for(Iterator<User> itr = userList.iterator(); itr.hasNext();)
			{
				User user = itr.next();
				Double totalAmount = 0.0;
				int totalOrders = 0;
				java.util.List<Ticket> ticketList = TicketDAO.getInstance().findByOwner(date1, date2,false);
				for(Iterator<Ticket> tcktItr = ticketList.iterator(); tcktItr.hasNext();)
				{
					Ticket ticket = tcktItr.next();
					if(ticket.getOwner().getFirstName().compareTo(user.getFirstName()) == 0)
					{
						totalAmount += ticket.getTotalAmount();
						totalOrders++;						
						if(ticket.getSplitPayment() != null &&ticket.getSplitPayment()){
							if(ticket.getCardAmount()!=null) {
								cardAmount += ticket.getCardAmount();
								++cardCount;
								cashAmount += ticket.getTotalAmount()-ticket.getCardAmount();
								++cashCount;
							}    	  
						}else if(ticket.getCashPayment() != null && ticket.getCashPayment()) {
							cashAmount += ticket.getTotalAmount();
							++cashCount;
						} else if (ticket.getCashPayment() != null && !ticket.getCashPayment()){
							cardAmount += ticket.getTotalAmount();
							++cardCount;
						}
					}
				}
				
				finalAmount += totalAmount;
				finalOrders += totalOrders;
				ServerData serverData = new ServerData();
				serverData.setName(user.getFirstName());
				serverData.setTotalAmount(totalAmount);
				serverData.setTotalOrders(totalOrders);
				
				if(totalAmount>0.00)
				serverList.add(serverData);
			}
		}else {
			Double totalAmount = 0.0;
			int totalOrders = 0;
			java.util.List<Ticket> ticketList = TicketDAO.getInstance().findByOwner(date1, date2,currentUser);
			for(Iterator<Ticket> tcktItr = ticketList.iterator(); tcktItr.hasNext();)
			{
				Ticket ticket = tcktItr.next();
					totalAmount += ticket.getTotalAmount();
					totalOrders++;
					
					if(ticket.getSplitPayment() != null &&ticket.getSplitPayment()){
						if(ticket.getCardAmount()!=null) {
							cardAmount += ticket.getCardAmount();
							++cardCount;
							cashAmount += ticket.getTotalAmount()-ticket.getCardAmount();
							++cashCount;
						}    	  
					}else if(ticket.getCashPayment() != null && ticket.getCashPayment()) {
						cashAmount += ticket.getTotalAmount();
						++cashCount;
					} else if (ticket.getCashPayment() != null && !ticket.getCashPayment()){
						cardAmount += ticket.getTotalAmount();
						++cardCount;
					}
			
			}
			
			finalAmount += totalAmount;
			finalOrders += totalOrders;
			ServerData serverData = new ServerData();
			serverData.setName(user.getFirstName());
			serverData.setTotalAmount(totalAmount);
			serverData.setTotalOrders(totalOrders);			
			serverList.add(serverData);
		}
		
		return serverList;
	}
	@Override
	public boolean isDateRangeSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTypeSupported() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void refresh(boolean print) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
