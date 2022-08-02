package com.floreantpos.ui.dialog;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.commons.lang3.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.report.MonthReportViewer;

public class MonthReportViewerDialog extends POSDialog implements KeyListener{
	MonthReportViewer view;
	public MonthReportViewerDialog(final MonthReportViewer view) {
		this.view = view;
		
		if(StringUtils.isNotEmpty(POSConstants.Monatabschluss))
			setTitle(POSConstants.Monatabschluss);
		else
			setTitle("Monatabschluss");
			
		setBackground(new Color(209,222,235));
		createUI();
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
	    public boolean dispatchKeyEvent(KeyEvent e) {
	        boolean keyHandled = false;
	        try
	        {
	        if (e.getID() == KeyEvent.KEY_PRESSED) {
	           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	        	  dispose();
	            }
	        }
	        }
	        catch(Exception ex)
	        {
	        	System.out.println("Exception raised");
	        }
	        return keyHandled;
	    }
	});
	}
	private void createUI() {
		add(view);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println("key pressed");
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
