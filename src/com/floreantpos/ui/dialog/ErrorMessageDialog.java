package com.floreantpos.ui.dialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;
import org.jdom.Parent;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;

public class ErrorMessageDialog extends POSDialog{

	JLabel lblImage;
	JLabel lblText;
	PosButton okButton;
	PosButton cancelButton;
	String message;
	boolean cancel;
	boolean cancelled;
	boolean jaNein = false;
	static boolean ok;
	
	public static boolean isOk() {
		return ok;
	}
	public boolean isCancelled()
	{
		return cancelled;
	}
	public void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	public ErrorMessageDialog(String message, boolean cancel)
	{
		this.cancel = cancel;
		this.message = message;
		initComponents();
	}
	
	public ErrorMessageDialog(String message, boolean cancel, boolean ja)
	{
		this.cancel = cancel;
		this.message = message;
		this.jaNein = true;
		initComponents();
	}
	public ErrorMessageDialog(String message, boolean cancel, Component parent)
	{
		super();
		this.cancel = cancel;
		this.message = message;
		initComponents();
	}
	public ErrorMessageDialog(String message)
	{
		cancel = false;
		this.message = message;
		initComponents();
	}
	
	public void initComponents()
	{
		
		if(StringUtils.isNotEmpty(POSConstants.WARNUNG))
			setTitle(POSConstants.WARNUNG);
		else
			setTitle("WARNUNG");
			
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setBackground(new Color(209,222,235));
		lblImage = new JLabel();
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/warnung.png"));
		lblImage.setIcon(imageIcon);
		lblText = new JLabel(message);
		lblText.setFont(new Font("Times New Roman", Font.BOLD, 24));
		panel.add(lblImage);
		panel.add(lblText, "wrap");
		
		
		JPanel buttonPanel = new JPanel();
		
		okButton = new PosButton("OK");
		
		okButton.setPreferredSize(new Dimension(100,60));
		okButton.setBackground(new Color(102,255,102));
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCancelled(false);
				dispose();
			}
			
		});
		if(jaNein)
			okButton.setText(" JA ");
		buttonPanel.add(okButton);
		
		cancelButton = new PosButton("ABBRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		 
		cancelButton.setPreferredSize(new Dimension(125,60));
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCancelled(true);
				dispose();
			}			
		});
		if(cancel) {
			if(jaNein)
				cancelButton.setText("  NEIN  ");
			buttonPanel.add(cancelButton);		
		}			
		 
		add(panel, BorderLayout.NORTH);
		buttonPanel.setBackground(new Color(209,222,235));
		add(buttonPanel, BorderLayout.SOUTH);

	}
}
