package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
public class PrintDelayDialog extends POSDialog{
	public PrintDelayDialog()
	{
		super(); 
		setLayout(new MigLayout());
		getContentPane().setBackground(new Color(128,128,128));
		setPreferredSize(new Dimension(200,200));
		this.setUndecorated(true);
	
		JLabel lblImage = new JLabel();
		lblImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/printer_wait.gif")));
		
		JLabel lblImage_printer = new JLabel("Bon wird gedruckt...");
		lblImage_printer.setFont(new Font("Times New Roman", Font.PLAIN,18));
		lblImage_printer.setForeground(Color.WHITE);
		add(lblImage,"wrap");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(lblImage_printer,BorderLayout.WEST);
		panel.setBackground(new Color(128,128,128));
		add(panel);
	}
}
