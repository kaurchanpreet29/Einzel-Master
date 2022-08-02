/*
 * LoginScreen.java
 *
 * Created on August 14, 2006, 10:57 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.floreantpos.IconFactory;
import com.floreantpos.swing.ImageComponent;

/**
 *
 * @author  MShahriar
 */
public class LoginScreen extends JPanel{
	public final static String VIEW_NAME = "LOGIN_VIEW";
	Thread t;
	JDialog dialog;
	JPanel currentPanel;
	private PasswordScreen passwordScreen;

	/** Creates new form LoginScreen */
	public LoginScreen() {
		
		setLayout(new BorderLayout(0,0));
		currentPanel = this;
		
		JPanel centerPanel = new JPanel(new BorderLayout(0,0));
		centerPanel.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(20, 20, 0,0)));
		ImageIcon icon = IconFactory.getIcon("/", "logo.png");
		
		if(icon == null) {			
			icon = IconFactory.getIcon("floreant-pos.png");
		}
		
		centerPanel.add(new ImageComponent(icon.getImage()));
		centerPanel.setBackground(new Color(35,35,36));
		add(centerPanel,BorderLayout.CENTER);
		
		setBackground(new Color(35,35,36));
		passwordScreen = new PasswordScreen();
		passwordScreen.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(5, 5, 0,0)));
	}

	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
		if (aFlag) {
			passwordScreen.setFocus();
		}
	}

	public void setTerminalId(int terminalId) {
		add(passwordScreen, BorderLayout.EAST);
		passwordScreen.setFocus();
		passwordScreen.setTerminalId(terminalId);
	}
}
