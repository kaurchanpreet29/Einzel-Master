package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;

public class CustomerDialog extends POSDialog{

	TransparentPanel panel;
	
	public CustomerDialog(TransparentPanel panel)
	{
		setTitle("Kundendatum");
		this.panel = panel;
		initComponents();
	}
	
	public void initComponents()
	{
		getContentPane().setBackground(new Color(209,222,235));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel,BorderLayout.CENTER);
		
		PosButton btnCancel = new PosButton("ABBRECHEN");
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		JPanel panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new BorderLayout());
		panel.add(btnCancel,BorderLayout.EAST);
		getContentPane().add(panel,BorderLayout.NORTH);
	}
}
