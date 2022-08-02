package com.floreantpos.add.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Salesreportdb;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.SalesReportDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TicketItemDAO;
import com.floreantpos.model.util.DateUtil;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.Report;
import com.floreantpos.report.SalesReport;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.BusinessDateUtil;

import net.miginfocom.swing.MigLayout;

public class ReprintSaleWRT extends POSDialog{


	private static final long serialVersionUID = 1L;
	JXDatePicker dpStartDate;
	JXDatePicker dpEndDate;
	private Report report;
	private static boolean val = false;
	private static int RehnungNummer;

	Date startDate;
	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	PosButton resetButton;
	PosButton setTButton;
	PosButton zuruckButton;

	boolean  check;

	public ReprintSaleWRT()
	{
		setTitle("Sales-Reset-Print");
		initComponents();
	}

	public void initComponents()
	{
		JPanel panel = new JPanel();
		dpStartDate = new JXDatePicker();
		dpStartDate.getMonthView().setFont(
				new Font("Times New Roman", Font.PLAIN, 28));
		dpStartDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));

		dpEndDate = new JXDatePicker();
		dpEndDate.getMonthView().setFont(
				new Font("Times New Roman", Font.PLAIN, 28));
		dpEndDate.setFont(new Font("Times New Roman", Font.PLAIN, 22));


		dpStartDate.setDate(DateUtils.startOfDay(new Date()));
		dpEndDate.setDate(DateUtils.endOfDay(new Date()));

		resetButton = new PosButton();
		resetButton.setText("ZURÜCKSETZEN");
		resetButton.setBackground(new Color(102, 255, 102));
		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final Date startDate = BusinessDateUtil.startOfOfficialDay(dpStartDate.getDate());
				final Date endDate = BusinessDateUtil.endOfOfficialDay(dpEndDate.getDate());
				List<Ticket> tickets = TicketDAO.getInstance().findAllTickets(startDate, endDate); 
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


				List<Salesreportdb> reports = SalesReportDAO.getInstance().findAllSaleReport(dpStartDate.getDate(), dpEndDate.getDate());
				if(reports != null) {
					Session sesion = SalesReportDAO.getInstance().createNewSession();
					Transaction tx = sesion.beginTransaction();
					reports.stream().forEach(report -> {						
						sesion.delete(report);						
					});
					tx.commit();
					sesion.close();
					POSMessageDialog.showError("Erfolg");
				}

			}			
		});

		setTButton = new PosButton();
		setTButton.setText("GENERIEREN");
		setTButton.setBackground(new Color(102, 255, 102));
		setTButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ImageIcon busy = IconFactory.getIcon("/icons/", "busy.gif");
					setTButton.setBackground(new Color(252, 252, 169));
					setTButton.setIcon(busy);
					startDate = DateUtil.startOfDay(dpStartDate.getDate());
					//					System.out.println("StartDate   "+startDate);
					final Date endDate = DateUtil.endOfDay(dpEndDate.getDate());					

					Calendar cal = Calendar.getInstance();
					Calendar cal1 = Calendar.getInstance(); // creates calendar
					
					if(report==null)
						report = new SalesReport();
					cal.setTime(startDate);
					cal1.setTime(endDate);
					long loop = daysBetween(startDate, endDate);
					//					System.out.println("Loop Value"+loop);
					for(int i=1; i<=(loop+1);i++) {
						showSalesReport(startDate);
						cal.set(Calendar.DATE, cal.get(Calendar.DATE)+1);
						startDate = cal.getTime();
					}

					POSMessageDialog.showError("Erfolg");
					setTButton.setIcon(null);
					setTButton.setBackground(new Color(102, 255, 102));
				}catch(Exception ex) {

				}
			}
		});

		zuruckButton = new PosButton();
		zuruckButton.setText("ABRECHEN");
		zuruckButton.setBackground(new Color(237, 113, 99));
		zuruckButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setTButton.setBackground(new Color(102, 255, 102));
				setTButton.setIcon(null);
				dispose();
			}
		});

		panel.setLayout(new MigLayout());
		panel.add(dpStartDate);
		panel.add(dpEndDate);
		panel.add(resetButton);
		panel.add(setTButton);
		panel.add(zuruckButton);

		setSize(new Dimension(1000,600));
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		panel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
	}
	//added

	public void showSalesReport(Date date) {
		//User usr =Application.getCurrentUser();
		try {
//			Report report = new SalesReport();
//			report.setType(1);			
//			setReport(report);
//			List<TicketItem> ticketItem = TicketItemDAO.getInstance().findByDate(date);
//
//			if(!ticketItem.isEmpty()) {				
				doRefreshReport(true, date);
//			}
			return;

		}catch (Exception e) {
			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
		}
	}
	int salesId;
//	public void resetArcheive(Date date) {	
//		Date from = BusinessDateUtil.startOfOfficialDay(date);
//		Date toDate = BusinessDateUtil.endOfOfficialDay(date);
//		System.out.println("Resetted von: "+from+" bis: "+toDate);
//		try {
//			Report report = new SalesReport();
//			final Restaurant restaurant1 = RestaurantDAO.getRestaurant();
//			int id1 = TerminalConfig.getSalesId() != null ? Integer.parseInt(TerminalConfig.getSalesId()) : 1;
//			if(id1==1) {
//				try {
//					id1 = Integer.parseInt(TerminalConfig.getSalesId());
//				}catch(Exception ex) {
//
//				}
//			}
//			List<Ticket> tickets = TicketDAO.getInstance().findAllTickets(from, toDate); 
//			if(tickets != null) {
//				Session session = TicketDAO.getInstance().createNewSession();
//				Transaction tx = session.beginTransaction();
//				tickets.stream().forEach(ticket -> {
//					ticket.setDrawerResetted(false);
//					session.saveOrUpdate(ticket);
//				});
//				tx.commit();
//				session.close();
//
//			}
//
//
//			List<Salesreportdb> reports = SalesReportDAO.getInstance().findByDate(from);	  	
//			if(reports != null) {
//				Session sesion = SalesReportDAO.getInstance().createNewSession();
//				Transaction tx = sesion.beginTransaction();
//				reports.stream().forEach(report1 -> {					
//					salesId = report1.getSalesid();
//					sesion.delete(report1);						
//				});
//				tx.commit();
//				sesion.close();
//
//
//				TerminalConfig.setSalesId(salesId+"");
//				RestaurantDAO.instance.saveOrUpdate(restaurant1);
//
//				report.setType(1);			
//				setReport(report);
//
//				List<TicketItem> ticketItem = TicketItemDAO.getInstance().findByDate(date);
//
//				if(!ticketItem.isEmpty()) {				
//					doRefreshReport(true, date);
//				}
//				TerminalConfig.setSalesId(id1+"");
//				RestaurantDAO.instance.saveOrUpdate(restaurant1);
//				return;
//			}
//
//		}catch (Exception e) {
//			POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
//		}
//	}



	private static long daysBetween(Date start, Date end) {
		long difference =  (end.getTime()-start.getTime())/86400000;
		return Math.abs(difference);
	}

	public boolean doRefreshReport(boolean flag, Date date) {// GEN-FIRST:event_doRefreshReport

		Date fromDate = date;
		Calendar c = Calendar.getInstance();
		c.setTime(fromDate);
		c.add(Calendar.DATE, 1);

		Date toDate = c.getTime();
//		if (fromDate.after(toDate)) {
//			POSMessageDialog
//			.showError(
//					BackOfficeWindow.getInstance(),
//					com.floreantpos.POSConstants.FROM_DATE_CANNOT_BE_GREATER_THAN_TO_DATE_);
//			return false;
//		}


		try {
			if (report != null) {
				int reportType = 0;
				if (flag == true)
					reportType = Report.REPORT_TYPE_2;
				report.setReportType(reportType);
				report.setStartDate(fromDate);
				report.setEndDate(toDate);	        
				report.refresh(true);
			}

		} catch (Exception e) {
			MessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, e);
		}
		return true;
	}


	public void doPrint() {
		try {
			if (report.getJasperPrint() != null) {
				report.getJasperPrint().setProperty("printerName",
						Application.getPrinters().getReceiptPrinter());
				JReportPrintService.printQuitely(report.getJasperPrint());
			}
		} catch (Exception e) {
		}
	}

	private Date getDate(Date date, int hours, int mins) {
		Calendar calendar = Calendar.getInstance();
		synchronized(calendar) {
			calendar.setTime(date);
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MINUTE, mins);
			return calendar.getTime();
		}  
	}


}