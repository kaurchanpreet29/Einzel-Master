package com.floreantpos.ui.views;

import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import com.floreantpos.swing.PosButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class OrderInfoDialogZWS extends POSDialog {
	
	boolean preview;
	
	OrderInfoViewZWS view;
	
	public OrderInfoDialogZWS(OrderInfoViewZWS view, boolean preview) {
		this.view = view;
		this.preview = preview;
		setTitle("DRUCK VORSCHAU ZWS");
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
				doPrint();
				System.out.println("DRUCK VORSCHAU");
			}
		});
		btnPrint.setText("DRUCK");
		btnPrint.setBackground(new Color(102,255,102));
		panel.add(btnPrint);
		
		PosButton btnClose = new PosButton();
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setText("ABBRECHEN");
		btnClose.setBackground(new Color(255,153,153));
		panel.add(btnClose);
		panel.setBackground(new Color(209,222,235));
	}

	protected void doPrint() {
		try {
			view.print(preview);
			this.dispose();
		} catch (Exception e) {
			e.printStackTrace();
			POSMessageDialog.showError(e.getMessage());
		}
	}

}
