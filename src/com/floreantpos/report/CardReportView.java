package com.floreantpos.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.CardDialog.CardPaymentType;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;
import com.floreantpos.util.NumberUtil;


public class CardReportView extends JPanel {
	private JXDatePicker fromDatePicker = new JXDatePicker();
	private JXDatePicker toDatePicker =  new JXDatePicker();
	private JButton btnPrint = new JButton("DRUCK");
	
	
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("ABBRECHEN");
	private JPanel reportContainer;

	JPanel topPanel;
	JPanel centerPanel;
	//JPanel eastPanel;
	boolean cancelled;
	JasperPrint jasperprint;
	private POSDialog dialog;
	public void setDialog(POSDialog dialog)
	{
		this.dialog = dialog;
	}
	public CardReportView() throws Exception {
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
		topPanel.add(btnOk);
		topPanel.add(btnPrint);
		topPanel.add(btnCancel);
		add(topPanel, BorderLayout.NORTH);

		//eastPanel = new JPanel();
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(new Color(209,222,235));
		centerPanel.setBorder(new EmptyBorder(0, 10,10,10));
		centerPanel.add(new JSeparator(), BorderLayout.NORTH);

		reportContainer = new JPanel(new BorderLayout());
		reportContainer.setBackground(new Color(209,222,235));
		centerPanel.add(reportContainer);

		add(centerPanel);
		//add(eastPanel,BorderLayout.EAST);
		//eastPanel.setBackground(new Color(209,222,235));
		//eastPanel.setPreferredSize(new Dimension(600,600));

		if(StringUtils.isNotEmpty(POSConstants.PRINT))
			btnPrint.setText(POSConstants.PRINT);
		
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
					POSMessageDialog.showError(CardReportView.this, POSConstants.ERROR_MESSAGE, e1);
				}
			}

		});		
		btnOk.setBackground(new Color(0,200,0));
		btnOk.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnOk.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					viewReport();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			btnCancel.setText(POSConstants.ABBRECHEN);
		
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(dialog != null)
					dialog.dispose();
			}

		});
		viewReport();
	}
	public Date getStartDate() {
		return new Date();
	}
	public boolean isCancelled()
	{
		return cancelled;
	}
	private void viewReport() throws Exception {
		Date fromDate = fromDatePicker.getDate();
		Date toDate = toDatePicker.getDate();

		if(fromDate.after(toDate)) {
			POSMessageDialog.showError(BackOfficeWindow.getInstance(), com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
			return;
		}

		fromDate = BusinessDateUtil.startOfOfficialDay(fromDate);
		toDate = BusinessDateUtil.endOfOfficialDay(toDate);


		//ChartServlet servlet = new ChartServlet();
		//ChartPanel panel = new ChartPanel(servlet.getChart(report));
		//eastPanel.add(panel);

		HashMap<String, String> map = new HashMap<String, String>();

		map.put("fromDate", ReportService.formatShortDate(fromDate));
		map.put("toDate", ReportService.formatShortDate(toDate));
		DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date date = new Date();
		String timeStamp = dateFormat1.format(date)+" Uhr";

		map.put("reportTime", timeStamp);
		map.put("reportTitle", "Zahlung Abschluss");
		Double americanexpress = 0.00;
		Double eccard = 0.00;
		Double gutschein = 0.00;
		Double karte = 0.00;
		Double khana = 0.00;
		Double master = 0.00;
		Double paypal = 0.00;
		Double visa = 0.00;
		Double total = 0.00;
		Double totalCash = 0.00;
		List<Ticket> ticketList = TicketDAO.getInstance().findDateTickets(fromDate, toDate);
		for(Iterator<Ticket> itr = ticketList.iterator(); itr.hasNext();)
		{
			Ticket ticket = itr.next();
			if(ticket.getSplitPayment() != null&&ticket.getSplitPayment())
			{
				if(ticket.getCardpaymenttype() == CardPaymentType.AMERICAN_EXPRESS.ordinal())
					americanexpress += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.EC_CARD.ordinal())
					eccard += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.GUTSCHEIN.ordinal())
					gutschein += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.KARTE.ordinal())
					karte += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.KHANA.ordinal())
					khana += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.MASTER.ordinal())
					master += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.PAYPAL.ordinal())
					paypal += ticket.getCardAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.VISA.ordinal())
					visa += ticket.getCardAmount();				
				totalCash += ticket.getTotalAmount()-ticket.getCardAmount();
			}else if(ticket.getCashPayment() != null && ticket.getCashPayment()){
				totalCash += ticket.getTotalAmount();
			}else if(ticket.getCardpaymenttype() != null){
				if(ticket.getCardpaymenttype() == CardPaymentType.AMERICAN_EXPRESS.ordinal())
					americanexpress += ticket.getTotalAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.EC_CARD.ordinal())				
					eccard += ticket.getTotalAmount();							
				else if(ticket.getCardpaymenttype() == CardPaymentType.GUTSCHEIN.ordinal())
					gutschein += ticket.getTotalAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.KARTE.ordinal())
					karte += ticket.getTotalAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.KHANA.ordinal())
					khana += ticket.getTotalAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.MASTER.ordinal())
					master += ticket.getTotalAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.PAYPAL.ordinal())
					paypal += ticket.getTotalAmount();
				else if(ticket.getCardpaymenttype() == CardPaymentType.VISA.ordinal())
					visa += ticket.getTotalAmount();
				else				
					eccard += ticket.getTotalAmount();	
			}else
				eccard += ticket.getTotalAmount();	

		}
		
		total = americanexpress + eccard + gutschein + karte + khana + master + paypal + visa;
		map.put("americanexpress", NumberUtil.formatNumber(americanexpress).toString() + " EUR");
		map.put("eccard",  NumberUtil.formatNumber(eccard).toString()+ " EUR");
		map.put("gutschein",  NumberUtil.formatNumber(gutschein).toString()+ " EUR");
		map.put("karte",  NumberUtil.formatNumber(karte).toString()+ " EUR");
		map.put("khana",  NumberUtil.formatNumber(khana).toString()+ " EUR");
		map.put("master",  NumberUtil.formatNumber(master).toString()+ " EUR");
		map.put("paypal",  NumberUtil.formatNumber(paypal).toString()+ " EUR");
		map.put("visa",  NumberUtil.formatNumber(visa).toString()+ " EUR");
		map.put("gesamt",  NumberUtil.formatNumber(total).toString()+ " EUR");
		map.put("bargesamt",  NumberUtil.formatNumber(totalCash).toString()+ " EUR");

		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResource("/com/floreantpos/report/template/card_report.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
		jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
		net.sf.jasperreports.swing.JRViewer viewer = new net.sf.jasperreports.swing.JRViewer(jasperPrint);
		viewer.getReportPanel().setBackground(new Color(209,222,235));
		this.jasperprint = jasperPrint;

		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
	}
}
