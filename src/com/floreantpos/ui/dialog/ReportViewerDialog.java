package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.commons.lang3.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.report.ReportViewer;
import com.floreantpos.ui.views.OrderInfoView;

public class ReportViewerDialog extends POSDialog implements KeyListener{
	ReportViewer view;
	public ReportViewerDialog(final ReportViewer view) {
		this.view = view;
		
		if(StringUtils.isNotEmpty(POSConstants.Tages_Abschluss))
			setTitle(POSConstants.Tages_Abschluss+"*");
		else 
			setTitle("Tages Abschluss*");
			
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
