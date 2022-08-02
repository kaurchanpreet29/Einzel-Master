package com.floreantpos.add.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.floreantpos.IconFactory;
import com.floreantpos.swing.TransparentPanel;

import net.miginfocom.swing.MigLayout;

public class AnimationLoader extends JDialog{
	private TransparentPanel loderPane;
	private static AnimationLoader loader;
	
	public static AnimationLoader getLoader() {
		if(loader==null)
			loader = new AnimationLoader("Loading...");
		return loader;
	}
	
	public void closeInsertDialog() {
		try {
			if(busyDialog.isVisible())
				busyDialog.dispose();
		}catch(Exception ex) {

		}
	}
	
	public void CloseBusiDialog() {
		try {
			if(busyDialog.isVisible())
				busyDialog.dispose();
		}catch(Exception ex) {
		}
	}

	public void showInsertDialog() {		
		busyDialog = new AnimationLoader(false);
		busyDialog.setUndecorated(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width / 2) - (busyDialog.getPreferredSize().width / 2);
		int y = (screenSize.height / 2) - (busyDialog.getPreferredSize().height / 2);
		busyDialog.setLocation(x, y);
		busyDialog.pack();
		busyDialog.setVisible(true);
	}
	
	public AnimationLoader(boolean noConnection) {
		setBackground(new Color(255,255,255));
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new MigLayout("alignx 50%"));
		Icon icon = null;
		if(noConnection) 
			icon = new IconFactory().getIconFromImage("noconnection.gif");			 
		else 
			icon = new IconFactory().getIconFromImage("transfer.gif");
		
		JLabel loadingLabel = new JLabel();
		loadingLabel.setOpaque(true);
		loadingLabel.setIcon(icon);	    
		panel.setOpaque(false);
		if(noConnection)
			getContentPane().add(new JLabel("Kein verbindung zum server bitte warten..."), BorderLayout.CENTER);
		else
			getContentPane().add(new JLabel(""), BorderLayout.CENTER);	
		getContentPane().add(loadingLabel, BorderLayout.SOUTH);	
	}
	
	public void ShowBusyDialog(boolean noData) {		
		if(noData) {
			busyDialog = new AnimationLoader("Kein verbindung zum server bitte warten...");
		}else {
			busyDialog = new AnimationLoader("");
		}
		busyDialog.setUndecorated(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width / 2) - (busyDialog.getPreferredSize().width / 2);
		int y = (screenSize.height / 2) - (busyDialog.getPreferredSize().height / 2);
		busyDialog.setLocation(x, y);
		busyDialog.pack();
		busyDialog.setVisible(true);
	}
	
	public AnimationLoader(String text) {
		setBackground(new Color(255,255,255));
	    setLayout(new BorderLayout());	    
	    JPanel panel = new JPanel();
	    panel.setOpaque(false);
	    panel.setLayout(new MigLayout("alignx 50%"));
	   
		Icon icon = new IconFactory().getIcon("loading.gif");
	    JLabel loadingLabel = new JLabel();
	    loadingLabel.setOpaque(true);
	    loadingLabel.setIcon(icon);
	    
	    panel.setOpaque(false);
	    JLabel lblMessage = new JLabel(text);
	    lblMessage.setFont(new Font("Times New Roman", Font.PLAIN, 24));
	    lblMessage.setForeground(Color.RED);
	    lblMessage.setBackground(Color.WHITE);
	    getContentPane().add(lblMessage, BorderLayout.CENTER);
	    getContentPane().add(loadingLabel, BorderLayout.SOUTH);	
	}
	public AnimationLoader busyDialog;
	
	public void showCardDialog() {		
		busyDialog = new AnimationLoader(true, "");
		busyDialog.setAlwaysOnTop(true);		
		busyDialog.setUndecorated(true);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (screenSize.width / 2) - (busyDialog.getPreferredSize().width / 2);
	    int y = (screenSize.height / 2) - (busyDialog.getPreferredSize().height / 2);
	    busyDialog.setLocation(x, y);
	    busyDialog.pack();
	    busyDialog.setVisible(true);
	}
	
	
	public AnimationLoader(boolean card, String command) {
		setBackground(new Color(255,255,255));
	    setLayout(new BorderLayout());	    
	    JPanel panel = new JPanel();
	    panel.setOpaque(false);
	    panel.setLayout(new MigLayout("alignx 50%"));	   
		Icon icon = new IconFactory().getIcon("zvt.gif");
	    JLabel loadingLabel = new JLabel();
	    loadingLabel.setOpaque(true);
	    loadingLabel.setIcon(icon);
	    panel.setOpaque(false);
	    JLabel lblMessage = new JLabel("Bitte Warten...", SwingUtilities.CENTER);
	    lblMessage.setFont(new Font("Times New Roman", Font.PLAIN, 24));
	    lblMessage.setForeground(Color.BLUE);
	    lblMessage.setBackground(Color.WHITE);

	    add(lblMessage, BorderLayout.NORTH);
	    add(loadingLabel, BorderLayout.CENTER);
	}
	
	public void CloseCardDialog() {
		try {
			if(busyDialog.isVisible())
				busyDialog.dispose();
		}catch(Exception ex) {
			
		}
		
	}
	
	
public TransparentPanel getLoder(Icon icon, String text) {
		loderPane.removeAll();
	    BoxLayout layoutMgr = new BoxLayout(loderPane, BoxLayout.PAGE_AXIS);
	    loderPane.setLayout(layoutMgr);	   
	    JLabel iconLabel = new JLabel();
	    iconLabel.setIcon(icon);
	    JLabel label = new JLabel(text);
	    loderPane.add(iconLabel);
	    loderPane.add(label);	
	return loderPane;
}
}
