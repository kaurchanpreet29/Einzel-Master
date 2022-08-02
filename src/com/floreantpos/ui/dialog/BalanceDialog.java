package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRProperties;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.ui.views.order.TicketView;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.model.dao.TseDataDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.TicketDetailView.PrintType;
import com.floreantpos.util.NumberUtil;
import com.khana.tse.fiskaly.FiskalyKeyParameter;
import com.khana.tse.fiskaly.PosFiskalyController;
import com.khana.tse.fiskaly.transaction.FiskalyPaymentType;
import com.khana.tse.fiskaly.transaction.FiskalyReceiptType;
import com.floreantpos.model.TSEReceiptData;
import com.floreantpos.model.dao.TSEReceiptDataDAO;


public class BalanceDialog extends POSDialog{
	private List<Ticket> tickets;
	private Ticket ticket;
	 
	JLabel lbltotal = new JLabel();
	JLabel lblpaidAmount = new JLabel();
	JLabel lblbalance = new JLabel();
	JLabel lblLine = new JLabel();
	PosButton btnok = new PosButton("SCHLIESSEN");
	
	
	PosButton btndruck = new PosButton("DRUCK");
	
	public BalanceDialog(Ticket ticket,Double total, Double paidAmount,Double balance,boolean print)
	{
		System.out.println("TicketPaymentType: " + ticket.getCashPayment() +"" );
		setLayout(new BorderLayout());
		getContentPane().setBackground(new Color(209,222,235));
		setTitle("Zurueck Geld");
		JPanel panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new MigLayout());
		
		
		if(StringUtils.isNotEmpty(POSConstants.TOTAL))
			lbltotal.setText(POSConstants.TOTAL+":    "+NumberUtil.formatNumber(total) + " €");
		else 
			lbltotal.setText("GESAMT:    "+NumberUtil.formatNumber(total) + " €");
			
		setFont(lbltotal);
		if(StringUtils.isNotEmpty(POSConstants.BAR))
			lblpaidAmount.setText(POSConstants.BAR+":    "+NumberUtil.formatNumber(paidAmount) + " €");
		else
		    lblpaidAmount.setText("BAR:            "+ NumberUtil.formatNumber(paidAmount) + " €");
		
		setFont(lblpaidAmount);
		if(StringUtils.isNotEmpty(POSConstants.BAR))
			lblbalance.setText(POSConstants.Zuruck+":    "+NumberUtil.formatNumber(balance) + " €");
		else
		    lblbalance.setText("ZURUECK: "+ NumberUtil.formatNumber(balance) + "  €");
		
		lblbalance.setFont(new Font("Times New Roman", Font.BOLD, 25));
		setBackground(new Color(209,222,235));
		btnok.setBackground(new Color(102,255,102));
		btnok.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		if(StringUtils.isNotEmpty(POSConstants.PRINT))
			btndruck.setText(POSConstants.PRINT);
		
		btndruck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {					 
					printReport(ticket,0,true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}				 
								 
			}
		});
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
		    public boolean dispatchKeyEvent(KeyEvent e) {
		        boolean keyHandled = false;
		        try
		        {
		        if (e.getID() == KeyEvent.KEY_PRESSED) {
		           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        	  dispose();
		            }
		           else if (e.getKeyCode() == KeyEvent.VK_ENTER)dispose();
		        }
		        }
		        catch(Exception ex)
		        {
		        	System.out.println("Exception raised");
		        }
		        return keyHandled;
		}});
		panel.add(lbltotal,"wrap");
		panel.add(lblpaidAmount,"wrap");
		panel.add(lblbalance,"wrap");
		JLabel lblImage = new JLabel();
		lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/euro.png")));
		
		getContentPane().add(panel, BorderLayout.WEST);
		getContentPane().add(lblImage, BorderLayout.EAST);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		 
		if(StringUtils.isNotEmpty(POSConstants.SCHLIESSEN))
			btnok.setText(POSConstants.SCHLIESSEN);
		
		buttonPanel.add(btnok, BorderLayout.EAST);
		
		if(!print)
		buttonPanel.add(btndruck, BorderLayout.WEST);
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
	}
	

	private PosFiskalyController posTseController;

	public PosFiskalyController getPosTseController() {
		return posTseController;
	}
	
	public void tse(Ticket ticket) {
		try {
			List<TicketItem> itemList = ticket.getTicketItems();
			Collections.sort(itemList, new TicketItem.ItemComparator());
			 
			    TSEReceiptData receiptData = null;
		    	TseDataDAO tseDataDAO = new  TseDataDAO();
		    	
		    	receiptData = tseDataDAO.findById();
			    if (receiptData != null) {
			    	
					ticket.setTseReceiptDataId(receiptData.getId());
					ticket.setTseReceiptTxRevisionNr(receiptData.getLatestRevision());
				}
			   
		} catch (Exception e) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.ERROR_MESSAGE, e);
		}
	}
	
	public void printReport(Ticket ticket, int arg, boolean angebot) throws Exception
	{
		
		tse(ticket);
		//reportPanel.removeAll();	
		 
		JRProperties.setProperty("net.sf.jasperreports.xpath.executer.factory",
			    "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
		 
			System.out.println("ticket vorschau print: " + ticket.getTicketItems().size());
			final Restaurant restaurant = RestaurantDAO.getRestaurant();
			if (ticket.getTicketid() == null&&!angebot|| ticket.getTicketid() != null&&ticket.getTicketid() == 0&&!angebot) {
				int restaurantTicketId = restaurant.getTicketid() != null ? restaurant.getTicketid() : 1;
				ticket.setTicketid(restaurantTicketId);					
				restaurant.setTicketid(++restaurantTicketId);
				RestaurantDAO.getInstance().saveOrUpdate(restaurant);
				TicketDAO.getInstance().saveOrUpdate(ticket);
			}
			TicketPrintProperties printProperties = new TicketPrintProperties("*** RECHNUNG ***", true, true, true,false);
			HashMap map;
			 
			  if(TerminalConfig.isLogoEnabled()) {
			    map =JReportPrintService.populateTicketPropertiesZWS(ticket, printProperties, null,PrintType.REGULAR,true, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
			  } else {
			    map =JReportPrintService.populateTicketPropertiesZWS(ticket, printProperties, null,PrintType.REGULAR,false, ticket.getPaidAmount()-ticket.getTotalAmountWithoutPfand());
			  }
			 
			JasperPrint jasperPrint;
		
				jasperPrint = JReportPrintService.createPrint(ticket, map, null,false);
				jasperPrint.setProperty("printerName", Application.getPrinters().getReceiptPrinter());
				jasperPrint.setName(POSConstants.RECEIPT_NR+"_" + (ticket.getTicketid()!=null?ticket.getTicketid():0));
				JReportPrintService.printQuitely(jasperPrint);
			 
		repaint();
		revalidate();
		
	}
	
	public void setFont(JLabel label){
		label.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	}
}
