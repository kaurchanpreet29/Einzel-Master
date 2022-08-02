package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import com.floreantpos.report.SalesReportModel;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.SalesReportView;

public class SalseReportDialog extends POSDialog {
	SalesReportView view;
	boolean print;
	
	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	public SalseReportDialog(SalesReportView view) {
		this.view = view;
		setTitle("VORSCHAU");
		setBackground(new Color(209,222,235));
		createUI();
	}	

	private void createUI() {
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
		    public boolean dispatchKeyEvent(KeyEvent e) {
			       
		        if (e.getID() == KeyEvent.KEY_PRESSED) {
		           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        	  dispose();
		        	  
		           }
		        }
		        return false;
		}});
		add(view);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
			
		PosButton btnPrint = new PosButton();
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPrint(true);
				dispose();
			}
		});
		btnPrint.setText("DRUCK");
		btnPrint.setBackground(new Color(102,255,102));
		panel.add(btnPrint);
		
		
		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				setPrint(false);
				dispose();
			}
		});
		btnClose.setText("ABBRECHEN");
		btnClose.setBackground(new Color(255,153,153));
		panel.add(btnClose);
		panel.setBackground(new Color(209,222,235));
	}

	
}
