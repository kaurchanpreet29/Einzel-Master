package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.report.MenuUsageReport;
import com.floreantpos.report.ReportItem;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.SalesReport;
import com.floreantpos.report.SalesReportModel;
import com.floreantpos.report.SalesReportModelFactory;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
import com.floreantpos.report.service.ReportService;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.swing.JRViewer;

public class SalesReportView extends JPanel {
	private Salesreportdb salesReport;
	JPanel reportPanel;
	SalesReportModel model;
	
	private boolean zreport;
	public SalesReportView(Salesreportdb salesDb, boolean reportType, SalesReportModel model ) throws Exception {
		super();
		this.salesReport = salesDb;
		this.zreport = reportType;
		this.model = model;
		setBackground(new Color(209,222,235));
		createUI();
	}
	
	private void createUI() throws Exception {
		reportPanel = new JPanel(new MigLayout());
		setBackground(new Color(128,128,128));	
		reportPanel.setBackground(new Color(209,222,235));
		JScrollPane scrollPane = new JScrollPane(reportPanel);
		scrollPane.setBackground(new Color(209,222,235));
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		setLayout(new BorderLayout());		
		add(scrollPane);
		showReportView(0);
	}	
	
	public void showReportView(int arg) throws Exception
	{
		
		System.out.println("showReportView: ");
		reportPanel.removeAll();
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
				"net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");

	
			Restaurant rest = RestaurantDAO.getRestaurant();
		    HashMap map = new HashMap();
		    long id;
		    JasperReport itemReport;
		    if(TerminalConfig.isWholeSale()) {
		    	System.out.println("first_");
				if(!rest.isWithWarengroup())
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_W_OhneW.jasper"));
				else
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_W.jasper"));
			} else if(TerminalConfig.isPayTransfer()) {		
				System.out.println("second_");
				if(!rest.isWithWarengroup()) {
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_OnlineOhneW.jasper"));
				}	else {
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_Online.jasper"));
				}
			} else if(TerminalConfig.isRabattDirektEnable() || TerminalConfig.isTRINKGELD()) {
				itemReport = (JasperReport) JRLoader
						.loadObject(SalesReportModelFactory.class
								.getResource("/com/floreantpos/report/template/sales_sub_report_gudschein_tip.jasper"));
			} else {
				System.out.println("third_");
				if(!rest.isWithWarengroup()) {
				 
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							.getResource("/com/floreantpos/report/template/sales_sub_report_Online.jasper"));
					//sales_sub_report_OhneW
				} else {
					 
				System.out.println("sales_sub_report: ");
					itemReport = (JasperReport) JRLoader
					.loadObject(SalesReportModelFactory.class
							 .getResource("/com/floreantpos/report/template/sales_sub_report_Online.jasper"));
					//sales_sub_report
					}
			}
	
		    ReportUtil.populateRestaurantProperties(map);
		    int salesId = salesReport.getSalesid();
		    if (zreport) {
		        map.put("reportTitle", "Z-ABSCHLAG Nr. (Z-1): " + salesId);
		    }  else {
		        map.put("reportTitle", "X-ABSCHLAG Nr. (X-1): " + salesId);
		    }

		    Calendar c = Calendar.getInstance();
		    map.put("reportTime", salesReport.getReporttime());
		    map.put("itemReport", itemReport);
		    map.put("dateRange", ReportService.formatShortDate(salesReport.getStartdate()));
		    map.put("dateRange1", "bis " + ReportService.formatShortDate(salesReport.getEnddate()));
		    map.put("itemDataSource", new JRTableModelDataSource(model));
		    map.put("currencySymbol", Application.getCurrencySymbol());
		    map.put("itemGrandTotal", model.getGrandTotalAsString());
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
		            new JRTableModelDataSource(getMenuUsageReport(salesReport)
		                .getTableModel()));
		        JRViewer viewer = new net.sf.jasperreports.swing.JRViewer(print);
		        viewer.getReportPanel().setBackground(new Color(209, 222, 235));
		        print.setProperty("printerName", Application.getPrinters()
		            .getReceiptPrinter());		    

			TicketReceiptView receiptView = new TicketReceiptView(print);
			reportPanel.add(receiptView.getReportPanel());
		
		repaint();
		revalidate();
	}	
	
	  private MenuUsageReport getMenuUsageReport(Salesreportdb salesReport) {
		    List<MenuUsageReportData> dataList = new ArrayList<>();
		    salesReport = SalesReportDAO.getInstance().loadFull(salesReport.getId());
		    salesReport.getMenuUsages().stream().forEach(menuUsage -> {
		      MenuUsageReportData data = new MenuUsageReportData();
		      data.setCategoryName(menuUsage.getCategory());
		      data.setGrossSales(menuUsage.getGrossSales());
		      data.setTax(menuUsage.getTax());
		      data.setCount(menuUsage.getCount());
		      dataList.add(data);
		    });
		    MenuUsageReport report = new MenuUsageReport();
		    report.setReportData(dataList);
		    return report;
		  }	  
}
