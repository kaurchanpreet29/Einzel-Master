package com.floreantpos.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JRViewer;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateUtils;
import org.jfree.chart.ChartPanel;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.main.ChartServlet;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.UiUtil;

public class MenuUsageReportView extends JPanel {
	private JXDatePicker fromDatePicker = new JXDatePicker();
	private JXDatePicker toDatePicker =  new JXDatePicker();
	private JButton btnGroup = new JButton("GRUPPEN");
	private JButton btnCategory = new JButton("KATEGORIEN");
	private JButton btnItem = new JButton("ARTIKEL");
	private JButton btnPrint = new JButton("DRUCK");
	private JButton btnCancel = new JButton("ABBRECHEN");
	private JPanel reportContainer;
	JPanel topPanel;
	JPanel centerPanel;
	JPanel eastPanel;
	boolean cancelled;
	JasperPrint jasperprint;
	POSDialog dialog;
	
	public void setDialog(POSDialog dialog)
	{
		this.dialog = dialog;
	}
	public MenuUsageReportView() {
		super(new BorderLayout());
		fromDatePicker.setDate(DateUtils.startOfDay(getStartDate()));
		toDatePicker.setDate(DateUtils.endOfDay(getStartDate()));
		
		setBackground(new Color(209,222,235));
		topPanel = new JPanel(new MigLayout());
		topPanel.setBackground(new Color(209,222,235));
		topPanel.add(new JLabel("Von :"), "grow");
		topPanel.add(fromDatePicker,"wrap");
		topPanel.add(new JLabel("Bis:"), "grow");
		topPanel.add(toDatePicker);
		topPanel.add(btnCategory);
		topPanel.add(btnGroup);
		topPanel.add(btnItem);
		topPanel.add(btnPrint);
		topPanel.add(btnCancel);
		add(topPanel, BorderLayout.NORTH);
		
		eastPanel = new JPanel();
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(new Color(209,222,235));
		centerPanel.setBorder(new EmptyBorder(0, 10,10,10));
		centerPanel.add(new JSeparator(), BorderLayout.NORTH);
		
		reportContainer = new JPanel(new BorderLayout());
		reportContainer.setBackground(new Color(209,222,235));
		centerPanel.add(reportContainer);
		
		add(centerPanel);
		add(eastPanel,BorderLayout.EAST);
		eastPanel.setBackground(new Color(209,222,235));
		eastPanel.setPreferredSize(new Dimension(600,600));
		btnCategory.setBackground(new Color(102,255,102));
		btnCategory.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnCategory.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					eastPanel.removeAll();
					eastPanel.revalidate();
					viewReport(1);
				} catch (Exception e1) {
					POSMessageDialog.showError(MenuUsageReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});
		btnGroup.setBackground(new Color(102,255,102));
		btnGroup.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnGroup.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					eastPanel.removeAll();
					eastPanel.revalidate();
					viewReport(2);
				} catch (Exception e1) {
					POSMessageDialog.showError(MenuUsageReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});
		btnItem.setBackground(new Color(102,255,102));
		btnItem.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					eastPanel.removeAll();
					eastPanel.revalidate();
					viewReport(3);
				} catch (Exception e1) {
					POSMessageDialog.showError(MenuUsageReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});		
		btnPrint.setBackground(new Color(102,178,255));
		btnPrint.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					if(jasperprint != null)
            		{
						jasperprint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
            			JReportPrintService.printQuitely(jasperprint);
            		}
				} catch (Exception e1) {
					POSMessageDialog.showError(MenuUsageReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}
			
		});
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(dialog != null)
					dialog.dispose();
			}
			
		});		
	}
	public Date getStartDate() {
			return new Date();
	}
	public boolean isCancelled()
	{
		return cancelled;
	}
	private void viewReport(int type) throws Exception {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();
		
		if(fromDate.after(toDate)) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}
		
		fromDate = DateUtils.startOfDay(fromDate);
		toDate = DateUtils.endOfDay(toDate);
		
		ReportService reportService = new ReportService();
		MenuUsageReport report = reportService.getMenuUsageReport(fromDate, toDate,type,true);
		
		ChartServlet servlet = new ChartServlet();
		ChartPanel panel = new ChartPanel(servlet.getChart(report));
		eastPanel.add(panel);
		HashMap<String, String> map = new HashMap<String, String>();
		
		if(type == 1)
		{
			map.put("reportTitle", "MENUSTATISTIK-KATEGORIEN");
		}
		else if(type == 2)
		{
			map.put("reportTitle", "MENUSTATISTIK-GRUPPEN");
		}
		else if(type == 3) 
		{
			map.put("reportTitle", "MENUSTATISTIK-ARTIKEL");
		}
		map.put("fromDate", ReportService.formatShortDate(fromDate));
		map.put("toDate", ReportService.formatShortDate(toDate));
		map.put("reportTime", ReportService.formatFullDate(new Date()));
		
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/report/template/menu_usage_report.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRTableModelDataSource(report.getTableModel()));
		jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		JRViewer viewer = new JRViewer(jasperPrint);
		this.jasperprint = jasperPrint;
		
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
		
	}
}
