package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class CardReportDialog extends POSDialog{

	JPanel panel;
	public CardReportDialog(JPanel panel)
	{
		this.panel = panel;
		setTitle("Kartenabschluss");
		initComponents();
	}
	
	public void initComponents()
	{
		setSize(new Dimension(1000,600));
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		panel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
	}
}
