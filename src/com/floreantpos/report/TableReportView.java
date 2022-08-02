package com.floreantpos.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketStatus;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.report.service.ReportService;
import com.floreantpos.ui.dialog.CardDialog.CardPaymentType;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.NumberUtil;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class TableReportView extends JPanel{
	private TicketListTableModel ticketListTableModel;
	private JPanel reportContainer;
	private JXDatePicker fromDatePicker = new JXDatePicker();
	private JXDatePicker toDatePicker =  new JXDatePicker();
	private JButton btnPrint = new JButton("DRUCK");
	private JButton btnOk = new JButton("OK");
	JPanel topPanel;
	JPanel centerPanel;
	JasperPrint jasperprint;
	private List<Ticket> tempList = new ArrayList();
	public TableReportView() throws JRException
	{
		super(new BorderLayout());
		initComponents();
	}
	public Date getStartDate() {
		return new Date();
	}
	public void initComponents() throws JRException
	{
		
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
		
		btnPrint.setBackground(new Color(102,178,255));
		btnPrint.setFont(new Font("Times New Roman", Font.BOLD, 18));
		btnPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(jasperprint != null)
            		{
						jasperprint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
            			JReportPrintService.printQuitely(jasperprint);
            		}
				} catch (Exception e1) {
					POSMessageDialog.showError(TableReportView.this, POSConstants.ERROR_MESSAGE, e1);
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
//		viewReport();
	}
	
	public void viewReport() throws JRException
	{
		ticketListTableModel = new TicketListTableModel();
		Date fromDate = fromDatePicker.getDate();
		Date toDate = DateUtils.endOfDay(toDatePicker.getDate());
		
		HashMap map = new HashMap();
		
		JasperReport tableReport = (JasperReport) JRLoader.loadObject(SalesReportModelFactory.class.getResource("/com/floreantpos/report/template/zwisen_report.jasper"));
		map.put("reportTitle", "Zwisenabschluss" );
		
		
		
		tempList = TicketDAO.getInstance().findTicketsToday(fromDate, toDate);
		System.out.println("Start date: "+ fromDate);
		System.out.println("End date: "+ toDate);
		if(tempList == null)
			tempList = new ArrayList();
		
		Double totalAmount = 0.00;
		boolean first = true;
		for(Iterator<Ticket> itr = tempList.iterator();itr.hasNext();)
		{
			Ticket ticket = itr.next();
			totalAmount += ticket.getTotalAmount();
			if(first) {
				fromDate = ticket.getCreateDate();
				first = false;
			}else {
				toDate = ticket.getCreateDate();
			}
		}
		
		map.put("fromDate", ReportService.formatShortDateTime(fromDate));
		map.put("toDate", ReportService.formatShortDateTime(toDate));
		
		map.put("alltotal", NumberUtil.formatNumber(totalAmount) +" €");
		ticketListTableModel.setRows(tempList);
		jasperprint = JasperFillManager.fillReport(tableReport, map, new JRTableModelDataSource(ticketListTableModel));
		net.sf.jasperreports.swing.JRViewer viewer = new net.sf.jasperreports.swing.JRViewer(jasperprint);
		viewer.getReportPanel().setBackground(new Color(209,222,235));
		
		reportContainer.removeAll();
		reportContainer.add(viewer);
		reportContainer.revalidate();
	}

	private class TicketListTableModel extends ListTableModel {
		
		public TicketListTableModel() {
			
			super(new String[] { "tisch", "status", "total", "uhr"  });
		
			
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);
			
			switch (columnIndex) {
			case 0:
				String tableNumber = ticket.getTableNumbers(); 
				if(ticket.getSplit()!= null && ticket.getSplit())
					return tableNumber + "*";
				else
					return tableNumber;
			
			case 1:
				String customerName = ticket.getProperty(Ticket.CUSTOMER_NAME);
				String paymentType = "";
				String paymentCard = "";
				if(ticket.getOnlinePayment() != null && ticket.getOnlinePayment() == true)
				{
					paymentType = "Online";
				}
				else if (ticket.getCashPayment() != null && ticket.getCashPayment() == true)
				{
					paymentType = "BAR";
				}
				else if (ticket.getGutscheinPayment() != null && ticket.getGutscheinPayment() == true)
				{
					paymentType = "GUTSCHEIN";
				}
				else if (ticket.getCashPayment() != null && ticket.getCashPayment() == false)
				{
					paymentType = "KARTE";
					
					if(ticket.getCardpaymenttype() == CardPaymentType.AMERICAN_EXPRESS.ordinal())
						paymentCard = "AMEX";
					else if(ticket.getCardpaymenttype() == CardPaymentType.EC_CARD.ordinal())
						paymentCard = "EC-Karte";
					else if(ticket.getCardpaymenttype() == CardPaymentType.GUTSCHEIN.ordinal())
						paymentCard = "Gutschein";
					else if(ticket.getCardpaymenttype() == CardPaymentType.KHANA.ordinal())
						paymentCard = "Khana";
					else if(ticket.getCardpaymenttype() == CardPaymentType.MASTER.ordinal())
						paymentCard = "MASTER";
					else if(ticket.getCardpaymenttype() == CardPaymentType.PAYPAL.ordinal())
						paymentCard = "PayPal";
					else if(ticket.getCardpaymenttype() == CardPaymentType.VISA.ordinal())
						paymentCard = "VISA";
					else if(ticket.getCardpaymenttype() == CardPaymentType.KARTE.ordinal())
						paymentCard = "Karte";
					else
						paymentCard = "Karte";
				}
								
				if(ticket.isPaid()) {
					if(ticket.getStatus() != null) {
						return TicketStatus.valueOf(ticket.getStatus()).toString();
					}
					if(paymentCard.length() != 0)
						return paymentCard;
					else
						return paymentType;
				}
				
				return "--Offen--";
			case 2:
				return NumberUtil.formatNumber(ticket.getTotalAmount()) + " €";
			case 3:
			{
				SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("HH:MM");
			
				String date = dateTimeFormatter.format(ticket.getCreateDate()).toString();
				return date;
			}
								
			}

			return null;
		}
	}
}
