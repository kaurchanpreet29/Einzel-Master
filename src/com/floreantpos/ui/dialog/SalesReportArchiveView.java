package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.MenuUsageReport;
import com.floreantpos.report.MenuUsageReport.MenuUsageReportData;
import com.floreantpos.report.ReportItem;
import com.floreantpos.report.ReportUtil;
import com.floreantpos.report.SalesReport;
import com.floreantpos.report.SalesReportModel;
import com.floreantpos.report.SalesReportModelFactory;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.views.SalesReportView;
import com.floreantpos.util.NumberUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

public class SalesReportArchiveView extends POSDialog {

	private JTable table;
	private SalesReportTableModel tableModel;

	public SalesReportArchiveView() {
		initComponents();
	}
	private JComboBox dpMonth;
	private JComboBox dpYear;
	public void initComponents() {
		table = new JTable();
		table.setModel(tableModel = new SalesReportTableModel());
		List<Salesreportdb> salesList = SalesReportDAO.getInstance().findAll();
		salesList.sort((p1, p2) -> p1.getSalesid().compareTo(p2.getSalesid()));
		salesList = salesList.stream().collect(Collectors.toList());
		Collections.reverse(salesList);
		tableModel.setRows(salesList);
		table.setRowHeight(40);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.WHITE);
		table.setBackground(new Color(36, 36, 35));
		table.setForeground(Color.WHITE);
		table.setSelectionBackground(Color.GRAY);
		table.getTableHeader().setBackground(Color.BLACK);
		table.getTableHeader().setForeground(Color.WHITE);

		dpMonth = new JComboBox();
		dpMonth.setBackground(new Color(2, 64, 2));
		dpMonth.setPreferredSize(new Dimension(100,35));
		dpMonth.setFont(new Font("Times New Roman", Font.BOLD, 22));
		dpMonth.setBackground(Color.WHITE);
		DateUtil.populateMonth(dpMonth);

		dpYear = new JComboBox();
		dpYear.setBackground(new Color(2, 64, 2));
		dpYear.setPreferredSize(new Dimension(100,35));
		dpYear.setFont(new Font("Times New Roman", Font.BOLD, 22));
		dpYear.setBackground(Color.WHITE);
		DateUtil.populateYear(dpYear);

		Calendar c = Calendar.getInstance();
		int currentMonth = c.get(Calendar.MONTH);
		int currentYear = c.get(Calendar.YEAR);

		dpYear.setSelectedItem(String.valueOf(currentYear));
		dpMonth.setSelectedIndex(currentMonth);    

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(50);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(50);
		columnModel.getColumn(3).setPreferredWidth(100);
		JScrollPane scrollPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
		.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setPreferredSize(new Dimension(30, 60));
		scrollBar.setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		add(scrollPane);

		PosButton xReportButton = new PosButton("X-Abscluss");
		if(StringUtils.isNotEmpty(POSConstants.X_ABSCHLUSS))
			xReportButton.setText(POSConstants.X_ABSCHLUSS);
		
		JPanel bottomPanel = new JPanel();

		//added
		PosButton druckButton = new PosButton("T-Abschulss All");
		if(StringUtils.isNotEmpty(POSConstants.T_Abschulss))
			druckButton.setText(POSConstants.T_Abschulss);
		
		druckButton.setBackground(new Color(2, 64, 2));
		druckButton.setFont(new Font(null, Font.BOLD, 20));
		druckButton.setForeground(Color.WHITE);
		druckButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doPrint();			
			}
		});

		PosButton ZReportButton = new PosButton("Z-Abscluss");
		if(StringUtils.isNotEmpty(POSConstants.Z_ABSCHLUSS))
			ZReportButton.setText(POSConstants.Z_ABSCHLUSS);
		    ZReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printSelectedSalesReport(true);
			}
		});
		xReportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				printSelectedSalesReport(false);
			}
		});

		PosButton deletButton = new PosButton("DELETE");
		deletButton.setBackground(new Color(125, 6, 42));
		deletButton.setFont(new Font(null, Font.BOLD, 20));
		deletButton.setForeground(Color.WHITE);
		deletButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelected();
			}
		});

		if(Application.getCurrentUser().getFirstName().compareTo("Master")==0) {
			bottomPanel.add(dpMonth, "growx");
			bottomPanel.add(dpYear, "growx");
			bottomPanel.add(druckButton, "growx");
			bottomPanel.add(deletButton);
		}
		bottomPanel.add(xReportButton);
		bottomPanel.add(ZReportButton);

		PosButton closeButton = new PosButton("SCHLIESSEN");
		if(StringUtils.isNotEmpty(POSConstants.BACK_CLOSE))
			closeButton.setText(POSConstants.BACK_CLOSE);
		else
			closeButton.setText("SCHLIESSEN");
		
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		bottomPanel.add(closeButton);

		ZReportButton.setBackground(new Color(2, 64, 2));
		xReportButton.setBackground(new Color(2, 64, 2));

		xReportButton.setFont(new Font(null, Font.BOLD, 20));
		xReportButton.setForeground(Color.WHITE);

		ZReportButton.setFont(new Font(null, Font.BOLD, 20));
		ZReportButton.setForeground(Color.WHITE);

		closeButton.setBackground(new Color(125, 6, 42));
		closeButton.setFont(new Font(null, Font.BOLD, 20));
		closeButton.setForeground(Color.WHITE);

		bottomPanel.setBackground(new Color(35,35,36));

		add(bottomPanel,BorderLayout.SOUTH);
	}

	private void doPrint() {
		Calendar c = Calendar.getInstance();
		Calendar d = Calendar.getInstance();
		Calendar e = Calendar.getInstance();

		int year = Integer.parseInt(dpYear.getSelectedItem().toString());
		int month = DateUtil.getMonth(dpMonth.getSelectedItem().toString());
		e.set(year, month - 1, 1);
		c.set(year, month - 1, 1);
		c.set(Calendar.DAY_OF_MONTH, 0);  	
		Date fromDate = c.getTime();
		d.set(year, month - 1, 1); 
		d.set(Calendar.DAY_OF_MONTH, e.getActualMaximum(Calendar.DAY_OF_MONTH));
		d.add(Calendar.DAY_OF_MONTH, 1);
		Date toDate = d.getTime();

		List<Salesreportdb> salesList = SalesReportDAO.getInstance().findAllSaleReport(fromDate, toDate);

		System.out.println("nulll"+salesList.size()+fromDate+toDate);		  

		for(Salesreportdb sales:salesList) {
			try {
				printSalesReport(sales, true); 
			}catch(Exception ex) {
				POSMessageDialog.showError("Failed Zu Drucken");
			}

		}		
	}

	private void printSelectedSalesReport(boolean zReport) {
		try {
			int row = table.getSelectedRow();
			Salesreportdb salesReport = (Salesreportdb) tableModel.getRowData(row);
			SalesReportView view = new SalesReportView(salesReport, zReport,convertReport(salesReport));
			SalseReportDialog dialog = new SalseReportDialog(view);
			dialog.setSize(400, 600);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(Application.getPosWindow());
			dialog.setVisible(true);

			if(dialog.isPrint())
				printSalesReport(salesReport, zReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteSelected() {
		try {
			int row = table.getSelectedRow();
			Salesreportdb salesReport = (Salesreportdb) tableModel.getRowData(row);
			List<Ticket> tickets = TicketDAO.getInstance().findAllTickets(salesReport.getStartdate(), salesReport.getEnddate()); 
			if(tickets != null) {
				Session session = TicketDAO.getInstance().createNewSession();
				Transaction tx = session.beginTransaction();
				tickets.stream().forEach(ticket -> {
					ticket.setDrawerResetted(false);
					session.saveOrUpdate(ticket);
				});
				tx.commit();
				session.close();

			}
			SalesReportDAO.getInstance().delete(salesReport);
			List<Salesreportdb> salesList = SalesReportDAO.getInstance().findAll();
			salesList.sort((p1, p2) -> p1.getSalesid().compareTo(p2.getSalesid()));
			salesList = salesList.stream().collect(Collectors.toList());
			Collections.reverse(salesList);
			tableModel.setRows(salesList);
			table.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	String nineteen;
	String seven;
	private SalesReportModel convertReport(Salesreportdb salesReport) {
		try {
			nineteen = Double.toString(Application.getInstance().dineInTax)+"%";
			seven = Double.toString(Application.getInstance().homeDeleveryTax)+"%";
		}catch(Exception ex) {
			nineteen="";
			seven ="";
		}
		HashMap<String, ReportItem> itemMap = new HashMap<String, ReportItem>();
		ReportItem reportItem = new ReportItem();
		reportItem.setId(String.valueOf(salesReport.getSalesid()));
		reportItem.setAwt(salesReport.getAwt());
		reportItem.setAwt19(salesReport.getAwtn());
		reportItem.setAwt7(salesReport.getAwts());
		reportItem.setAwot((salesReport.getNetton() != null ? salesReport.getNetton() : 0.00)+(salesReport.getNettos() != null ? salesReport.getNettos() : 0.00)+(salesReport.getNettoz() != null ? salesReport.getNettoz() : 0.00));

		reportItem.setttd(salesReport.getTaxn());
		reportItem.setAwt0(salesReport.getNettoz() != null ? salesReport.getNettoz() : 0.00);

		reportItem.setPfand1(salesReport.getPfand1() != null ? salesReport.getPfand1() : 0.00);
		reportItem.setPfand2(salesReport.getPfand2() != null ? salesReport.getPfand2() : 0.00);
		reportItem.setPfand3(salesReport.getPfand3() != null ? salesReport.getPfand3() : 0.00 );
		reportItem.settts(salesReport.getTaxs());
		reportItem.setttz(salesReport.getTaxz());
		reportItem.setNetton(salesReport.getNetton() != null ? salesReport.getNetton() : 0.00);
		reportItem.setNettos(salesReport.getNettos() != null ? salesReport.getNettos() : 0.00);
		reportItem.setNettoz(salesReport.getNettoz() != null ? salesReport.getNettoz() : 0.00);
		 
		reportItem.setRabatt_19(salesReport.getRabatt_19());
		reportItem.setRabatt_7(salesReport.getRabatt_7());
		reportItem.setKtrinkgeld(salesReport.getKtrinkgeld());
		reportItem.setBtrinkgeld(salesReport.getBtrinkgeld());

		reportItem.setCashPayment(salesReport.getCashpayment());
		reportItem.setCashTax(salesReport.getCashtax());
		if (salesReport.getCashpaymentcount() != null) {
			reportItem.setCashPaymentCount(salesReport.getCashpaymentcount());
		}
		reportItem.setTotalInvoices(Math.round(salesReport.getTotalinvoices()));
		reportItem.setTotalSoldItems(salesReport.getSolditem());
		if (salesReport.getVoidtax() != null) {
			reportItem.setCancelledTax(salesReport.getVoidtax());
		}
		reportItem.setVoidArticles(salesReport.getVoidArticles());
		reportItem.setCancelledItems(Math.round(salesReport.getVoidticket()));
		reportItem.setCancelledTrans(salesReport.getVoidamount());
		try {
			reportItem.setTotalCash(salesReport.getCashpayment()-salesReport.getPfand1()+salesReport.getPfand2()+salesReport.getPfand3());
		}catch(Exception ex) {

		}    

		if(TerminalConfig.isWholeSale()||TerminalConfig.isPayTransfer()) {
			reportItem.setRechnugPament_anzahl((salesReport.getRechnugPament_anzahl()!=null?salesReport.getRechnugPament_anzahl():0)+"");
			reportItem.setRechnugPaymentAmount(String.valueOf((salesReport.getRechnugPaymentAmount()!=null?salesReport.getRechnugPaymentAmount():0.0))+" €");
			reportItem.setRechnugPayment_tax(String.valueOf((salesReport.getRechnugPayment_tax()!=null?salesReport.getRechnugPayment_tax():0.0))+" €");

			reportItem.setMwst_gesamt_text_rechnug(POSConstants.SALES_VAT_TOTAL);
			reportItem.setRechnugPayment_text(POSConstants.BILL+"-Zahlung");
			reportItem.setAnzahl_text_rechnug(POSConstants.SALES_COUNT);
			System.out.println("(POSConstants.BILL: " + POSConstants.BILL + ", getRechnugPament_anzahl()" + salesReport.getRechnugPament_anzahl() +" salesReport.getRechnugPaymentAmount(): " + salesReport.getRechnugPaymentAmount());

			reportItem.setOnlinePament_anzahl((salesReport.getOnlinePament_anzahl()!=null?salesReport.getOnlinePament_anzahl():0)+"");
			reportItem.setOnlinePayment_tax(String.valueOf(NumberUtil.roundToTwoDigit(salesReport.getOnlinetax()!=null?salesReport.getOnlinetax():0.0))+" €");
			reportItem.setOnlinePaymentAmount(String.valueOf((salesReport.getOnline()!=null?salesReport.getOnline():0.0))+" €");

			reportItem.setMwst_gesamt_text_online(POSConstants.SALES_VAT_TOTAL);
			reportItem.setOnlinePayment_text("Online-Zahlung");
			reportItem.setAnzahl_text_online(POSConstants.SALES_COUNT);	
		}	


		reportItem.setCardPayment(salesReport.getCardpayment());
		reportItem.setCardTax(salesReport.getCardtax());    
		
		reportItem.setDiscountAmount(salesReport.getDiscount());
		reportItem.setAnzahlRetour(salesReport.getAnzahlRetour()!=null ? salesReport.getAnzahlRetour() : 0);
		reportItem.setRetourGesamt(salesReport.getRetourGesamt()!=null ? salesReport.getRetourGesamt() : 0.00);
		reportItem.setRetourTax(salesReport.getRetourTax()!=null ? salesReport.getRetourTax() : 0.00);
		reportItem.setGesamtSumme(salesReport.getGesamtSumme()!=null ? salesReport.getGesamtSumme() : 0.00);
		if (salesReport.getCardpaymentcount() != null) {
			reportItem.setCardPaymentCount(salesReport.getCardpaymentcount());
		}
		reportItem.setCash19(salesReport.getCash19()!=null?salesReport.getCash19():0.00);
		reportItem.setCash7(salesReport.getCash7()!=null?salesReport.getCash7():0.00);
		reportItem.setCard19(salesReport.getCard19()!=null?salesReport.getCard19():0.00);    
		reportItem.setCard7(salesReport.getCard7()!=null?salesReport.getCard7():0.00); 
		//TextFields

		String cash19Text = "Bar-Zahlung "+nineteen+"%";
		String cash7Text =  "Bar-Zahlung "+seven+"%";
		String card19Text =  "Karte-Zahlung "+nineteen+"%";
		String card7Text = "Karte-Zahlung "+seven+"%";

		reportItem.setCash19Text(salesReport.getCash19Text()!=null?salesReport.getCash19Text():cash19Text);
		reportItem.setCash7Text(salesReport.getCash7Text()!=null?salesReport.getCash7Text():cash7Text);
		reportItem.setCard19Text(salesReport.getCard19Text()!=null?salesReport.getCard19Text():card19Text);
		reportItem.setCard7Text(salesReport.getCard7Text()!=null?salesReport.getCard7Text():card7Text);
		reportItem.setEinnahme_text(POSConstants.SALES_REVENUE);
		reportItem.setUmasat_gesamt_text(POSConstants.SALES_TOTAL_SUM);
		reportItem.setGesamt_19_text(salesReport.getGesamt_19_text()!=null?salesReport.getGesamt_19_text():POSConstants.SALES_TOTAL+" "+nineteen);
		reportItem.setGesamt_7_text(salesReport.getGesamt_7_text()!=null?salesReport.getGesamt_7_text():POSConstants.SALES_TOTAL+" "+seven);
		reportItem.setGesamt_0_text(salesReport.getGesamt_0_text()!=null?salesReport.getGesamt_0_text():POSConstants.SALES_TOTAL+" 0%");
		reportItem.setGesamt_netto_text(POSConstants.SALES_NET_TOTAL);
		reportItem.setNetto_19_text(salesReport.getNetto_19_text()!=null?salesReport.getNetto_19_text():POSConstants.SALES_NET+" "+nineteen);
		reportItem.setNetto_7_text(salesReport.getNetto_7_text()!=null?salesReport.getNetto_7_text():POSConstants.SALES_NET+" "+seven);
		reportItem.setNetto_0_text(salesReport.getNetto_0_text()!=null?salesReport.getNetto_0_text():POSConstants.SALES_NET+" 0%");
		reportItem.setMwst_19_text(salesReport.getMwst_19_text()!=null?salesReport.getMwst_19_text():POSConstants.SALES_VAT+" "+nineteen);
		reportItem.setMwst_7_text(salesReport.getMwst_7_text()!=null?salesReport.getMwst_7_text():POSConstants.SALES_VAT+" "+seven);
		reportItem.setMwst_0_text(salesReport.getMwst_0_text()!=null?salesReport.getMwst_0_text():POSConstants.SALES_VAT+" 0%");		
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
		reportItem.setVerkaufte_gutschein_text(POSConstants.SALES_COUPON);
		reportItem.setGutschein_eingelost_text(POSConstants.SALES_COUPON_REDEEMED);
		reportItem.setBar_tip_text(POSConstants.SALES_CASH_TIP);
		reportItem.setOther_tip_text(POSConstants.SALES_OTHER_TIP);
		reportItem.setRabatt_19text(POSConstants.SALES_RABATT_19);
		reportItem.setRabatt_7text(POSConstants.SALES_RABATT_7);
		System.out.println("POSConstants.SALES_RABATT_19: " + POSConstants.SALES_RABATT_19);

		itemMap.put("1", reportItem);
		SalesReportModel itemReportModel;
		itemReportModel = new SalesReportModel();
		itemReportModel.setItems(new ArrayList<ReportItem>(itemMap.values()));
		return itemReportModel;
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

	private void printSalesReport(Salesreportdb salesReport, boolean zReport) throws JRException,
	IOException {
		ReportService reportService = new ReportService();

		SalesReportModel itemReportModel = convertReport(salesReport);
		JasperReport itemReport;
		Restaurant rest = RestaurantDAO.getRestaurant();

		if(TerminalConfig.isZahlungSteur()) {
			System.out.println("ik");
			itemReport = (JasperReport) JRLoader.loadObject(
					SalesReportModelFactory.class.getResource("/com/floreantpos/report/template/sales_sub_report_zahlung.jasper")); 
		}else if(TerminalConfig.isWholeSale()) {
			System.out.println("Second");
			if(!rest.isWithWarengroup())
				itemReport = (JasperReport) JRLoader
				.loadObject(SalesReportModelFactory.class
						.getResource("/com/floreantpos/report/template/sales_sub_report_W_OhneW.jasper"));
			else
				itemReport = (JasperReport) JRLoader
				.loadObject(SalesReportModelFactory.class
						.getResource("/com/floreantpos/report/template/sales_sub_report_W.jasper"));
		}else if(TerminalConfig.isPayTransfer()) {
			System.out.println("third");
			if(!rest.isWithWarengroup()) {		
				 
				itemReport = (JasperReport) JRLoader
				.loadObject(SalesReportModelFactory.class
						.getResource("/com/floreantpos/report/template/sales_sub_report_OnlineOhneW.jasper")); 
			}	else {	
				 System.out.println("sales_sub_report_Online: ");
				itemReport = (JasperReport) JRLoader
				.loadObject(SalesReportModelFactory.class
						.getResource("/com/floreantpos/report/template/sales_sub_report_Online.jasper"));
				 
		    	}
			} else {
				System.out.println("fourth");
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
		long id;
		ReportUtil.populateRestaurantProperties(map);
		int salesId = salesReport.getSalesid();
		if (zReport) {
			map.put("reportTitle", "Z-ABSCHLAG Nr.: " + salesId);
		} else {
			map.put("reportTitle", "X-ABSCHLAG Nr.: " + salesId);
		}
		Calendar c = Calendar.getInstance();
		map.put("reportTime", salesReport.getReporttime());

		map.put("dateRange",
				"von " + ReportService.formatShortDate(salesReport.getStartdate()));
		map.put("dateRange1",
				"bis " + ReportService.formatShortDate(salesReport.getEnddate()));
		map.put("itemDataSource", new JRTableModelDataSource(itemReportModel));
		map.put("currencySymbol", Application.getCurrencySymbol());
		map.put("itemGrandTotal", itemReportModel.getGrandTotalAsString());
		map.put("itemReport", itemReport);
		map.put("datum", "Datum");
		map.put("warenTitle", "Warengruppen-Abrechnung");
		map.put("warenGruppen", "WArengruppen");
		map.put("Anz", "Anz.");
		map.put("total", "Gesamt");
		map.put("date", POSConstants.DATE);
		JasperReport masterReport = (JasperReport) JRLoader
				.loadObject(SalesReport.class
						.getResource("/com/floreantpos/report/template/sales_report.jasper"));
		if(!rest.isWithWarengroup())
			masterReport = (JasperReport) JRLoader
			.loadObject(SalesReport.class
					.getResource("/com/floreantpos/report/template/sales_report_OhneW.jasper"));
		MenuUsageReport report = null;
		if(rest.isWithWarengroup())
			report = getMenuUsageReport(salesReport);
		else {
			report = new MenuUsageReport();
			report.addReportData(new MenuUsageReportData());
		}
		JasperPrint print = JasperFillManager.fillReport(masterReport, map,
				new JRTableModelDataSource(report
						.getTableModel()));
		JRViewer viewer = new net.sf.jasperreports.swing.JRViewer(print);
		viewer.getReportPanel().setBackground(new Color(209, 222, 235));
		print.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		JReportPrintService.printQuitely(print);
	}

	private class SalesReportTableModel extends ListTableModel {
		 
		public SalesReportTableModel() {
			super(new String[] { "Sales-Id", POSConstants.DATE,
					POSConstants.Druck_Zeit, POSConstants.TOTAL });
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {

			if (columnIndex == 0 && Application.getCurrentUser().getFirstName().compareTo("Master")==0||
					columnIndex == 0 && Application.getCurrentUser().getFirstName().compareTo("Super-User")==0||
					columnIndex == 2 && Application.getCurrentUser().getFirstName().compareTo("Super-User")==0||
					columnIndex == 2 && Application.getCurrentUser().getFirstName().compareTo("Master")==0) {
				return true;
			}
			return false;
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				Salesreportdb salesReport = (Salesreportdb) rows.get(rowIndex);

				if (columnIndex == 0) {
					try {
						salesReport.setSalesid(Integer.parseInt(value.toString()));
					} catch (Exception e) {
					}
				} else if (columnIndex == 2) {
					salesReport.setReporttime(value.toString());
				}

				SalesReportDAO.getInstance().saveOrUpdate(salesReport);
				this.fireTableDataChanged();
				table.repaint();
			} catch (Exception e) {
			}
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Salesreportdb salesReport = (Salesreportdb) rows.get(rowIndex);
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yy");
			switch (columnIndex) {
			case 0:
				return salesReport.getSalesid();
			case 1:
				return df.format(salesReport.getStartdate());
				//      case 2:
				//        return df.format(salesReport.getEnddate());
			case 2:
				return salesReport.getReporttime();
			case 3:
				return NumberUtil.formatNumber(salesReport.getAwt());
			}
			return null;
		}
	}
}