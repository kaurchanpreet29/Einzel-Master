package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
















import com.floreantpos.main.Application;
import com.floreantpos.model.PosPrinters;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.report.JReportPrintService;
import com.floreantpos.swing.PosButton;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

public class KitchenMessageDialog extends POSDialog{

	public KitchenMessageDialog() 
	{
		setLayout(new MigLayout());
		messagePanel = new JPanel();
		messagePanel.setLayout(new FlowLayout());
		JLabel lblMsg = new JLabel("Nachricht: ");
		messagePanel.add(lblMsg);
		
		taMsg = new JTextArea(7,30);
		taMsg.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		taMsg.setLineWrap(true);
	   	messagePanel.add(taMsg);
		
	   	sendButton = new PosButton();
	   	sendButton.setBackground(new Color(102,255,102));
	   	sendButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
	   	sendButton.setText("Senden");
	   	messagePanel.add(sendButton);
	   	
	   	sendButton.addActionListener(new ActionListener(){
	   		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HashMap map = new HashMap();
				DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				Date date = new Date();
				String timeStamp = dateFormat1.format(date);
				map.put("reportTime", timeStamp);
				map.put("kitchenMessage", taMsg.getText().toString());
				map.put("user", "Von: "+ Application.getCurrentUser());
				try
				{
					JasperPrint jasperPrint = JReportPrintService.createKitchenMessagePrint(map);
					jasperPrint.setName("KitchenMessage");
					
					PosPrinters printers = Application.getPrinters(); 
					
					jasperPrint.setProperty("printerName", printers.getDefaultKitchenPrinter().getDeviceName());
			
					JReportPrintService.printQuitely(jasperPrint);
					taMsg.setText("");
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
	   	});
	   	
	   	cancelButton = new PosButton();
	   	cancelButton.setBackground(new Color(255,153,153));
	   	cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
	   	cancelButton.setText("Abbrechen");
	   	cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				return;
				
			}
	   		
	   	});
	   	messagePanel.add(cancelButton);
	   	
	   	
		add(messagePanel,"wrap");
		keyPadPanel = new JPanel();
		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		keyPadPanel.add(qwertyKeyPad);
		add(keyPadPanel);
		messagePanel.setBackground(new Color(209,222,235));
		keyPadPanel.setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
	}
	
	JPanel messagePanel;
	JPanel keyPadPanel;
	JTextArea taMsg;
	PosButton sendButton;
	PosButton cancelButton;
}