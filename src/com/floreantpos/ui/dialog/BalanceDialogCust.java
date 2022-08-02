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


public class BalanceDialogCust extends POSDialog{
	private List<Ticket> tickets;
	private Ticket ticket;
	
	JLabel lbltotal = new JLabel();
	JLabel lblpaidAmount = new JLabel();
	JLabel lblbalance = new JLabel();
	JLabel lblLine = new JLabel();
	PosButton btnok = new PosButton("SCHLIESSEN");
	PosButton btndruck = new PosButton("DRUCK");
	
	public BalanceDialogCust(Double total, Double paidAmount,Double balance)
	{
		System.out.println("TicketPaymentType: " + ticket.getCashPayment() +"" );
		setLayout(new BorderLayout());
		getContentPane().setBackground(new Color(209,222,235));
		setTitle("Zurueck Geld");
		JPanel panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new MigLayout());
		lbltotal.setText("GESAMT:    "+NumberUtil.formatNumber(total) + " €");
		setFont(lbltotal);
		lblpaidAmount.setText("BAR:            "+ NumberUtil.formatNumber(paidAmount) + " €");
		setFont(lblpaidAmount);
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
		
		btndruck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				 
				
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
		 
		buttonPanel.add(btnok, BorderLayout.EAST);
		
		 
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
	}
	

	private PosFiskalyController posTseController;

	public PosFiskalyController getPosTseController() {
		return posTseController;
	}
	
	public void setFont(JLabel label){
		label.setFont(new Font("Times New Roman", Font.PLAIN, 25));
	}
}
