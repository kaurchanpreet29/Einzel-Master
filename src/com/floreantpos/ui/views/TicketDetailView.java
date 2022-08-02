/*
 * TicketInfoView.java
 *
 * Created on August 13, 2006, 11:17 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.floreantpos.model.Ticket;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.ui.dialog.DiscountDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.payment.SettleTicketDialog;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author  MShahriar
 */
public class TicketDetailView extends JPanel {

	public enum PrintType
	{
		REGULAR,
		REGULAR2,
		REGULAR3,		
		REGULAR4,
		COPY,
		ZWS,
		A4
	}
	public final static String VIEW_NAME = "TICKET_DETAIL";

	private JPanel topPanel;
	private JPanel bottomPanel;
	private List<Ticket> tickets;
	private com.floreantpos.swing.PosButton rabatt1;
	private boolean noDiscount = false;
	public PrintType printType;
	public DiscountDialog dialog;
	public SettleTicketDialog dialog2;
	/** Creates new form TicketInfoView */
	public TicketDetailView(DiscountDialog dialog) {
		this.dialog = dialog;
		init();
	}
	public TicketDetailView(boolean noDiscount) {
		this.noDiscount = noDiscount;
		init();
	}
	public TicketDetailView(SettleTicketDialog dialog) {
	}
	public TicketDetailView() {
		init();
	}
	public void init()
	{
		setLayout(new BorderLayout(5, 5));
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setBackground(new Color(209,222,235));
		topPanel = new JPanel(new GridLayout());
		topPanel.setBackground(new Color(209,222,235));
		add(topPanel, BorderLayout.CENTER);

		rabatt1 = new com.floreantpos.swing.PosButton();
		rabatt1.setBackground(null);
		rabatt1.setText("RABATT");

		bottomPanel = new JPanel(new GridLayout());
		add(bottomPanel,BorderLayout.NORTH);

		rabatt1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if(rabatt1.getBackground() == Color.GREEN)
				{
					dialog.doViewDiscounts();
					if(dialog.getTicket().getCouponAndDiscounts()==null)
						rabatt1.setBackground(null);
				}else{
					dialog.doApplyCoupon();
					if (dialog.getTicket().getCouponAndDiscounts().size() > 0) {
						rabatt1.setBackground(Color.GREEN);						
					}
				}
				dialog.setCancelText();

			}

		});
		if(dialog != null && dialog.getTicket()!= null && dialog.getTicket().getCouponAndDiscounts() != null)
		{
			if(dialog.getTicket().getCouponAndDiscounts().size() > 0)
				rabatt1.setBackground(Color.GREEN);
		}
		bottomPanel.setBackground(new Color(209,222,235));
		if(!noDiscount)
			bottomPanel.add(rabatt1);
		bottomPanel.setBackground(new Color(209,222,235));
		setOpaque(false);
	}

	public void setPrintType(PrintType type)
	{
		printType = type;
	}
	public PrintType getPrintType()
	{
		return printType;
	}
	public void clearView() {
		try {
			topPanel.removeAll();
		}catch(Exception ex) {

		}
	}

	public void updateView(boolean isOfficial) {
		try {
			clearView();
			List<Ticket> tickets = getTickets();

			int totalTicket = tickets.size();
			if (totalTicket <= 0) {
				return;
			}

			JPanel reportPanel = new JPanel(new MigLayout());
			reportPanel.setBackground(new Color(209,222,235));
			JScrollPane scrollPane = new JScrollPane(reportPanel);
			scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
			scrollPane.getVerticalScrollBar().setUnitIncrement(10);

			for (Iterator iter = tickets.iterator(); iter.hasNext();) {
				Ticket ticket = (Ticket) iter.next();

				TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
				HashMap map = JReportPrintService.populateTicketProperties(ticket, printProperties, null,PrintType.REGULAR,false, 0.00);
				JasperPrint jasperPrint = JReportPrintService.createPrint(ticket, map, null,isOfficial);

				TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
				reportPanel.add(receiptView.getReportPanel());
			}

			scrollPane.setBackground(new Color(209,222,235));
			try {
				topPanel.add(scrollPane, BorderLayout.CENTER);
			}catch(Exception ex) {

			}

			revalidate();
			repaint();

		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
		try {
			updateView(false);
		}catch(Exception ex) {

		}

	}

	public void setTicket(Ticket ticket) {
		tickets = new ArrayList<Ticket>(1);
		tickets.add(ticket);
		updateView(false);
	}

	public void cleanup() {
		tickets = null;
	}
}

