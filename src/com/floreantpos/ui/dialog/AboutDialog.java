package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.floreantpos.IconFactory;
import com.floreantpos.bo.ui.BackOfficeWindow;


public class AboutDialog extends POSDialog {
	
	public AboutDialog() {
		super(BackOfficeWindow.getInstance(), true);
		setTitle("About");
	}
	
	@Override
	protected void initUI() {
		JPanel contentPanel = new JPanel(new BorderLayout(20,20));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JLabel logoLabel = new JLabel(IconFactory.getIcon("/icons/", "fp_logo128x128.png"));
		contentPanel.add(logoLabel, BorderLayout.WEST);
		
		JLabel l = new JLabel("<html><center><h1>Khana Kassensysteme</h1><br/></center></html>");
		contentPanel.add(l);
		
		JPanel buttonPanel = new JPanel();
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		buttonPanel.add(btnOk);
		contentPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		add(contentPanel);
	}
}
