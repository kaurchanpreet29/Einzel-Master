/*
 * MessageDialog.java
 *
 * Created on August 23, 2006, 10:45 PM
 */

package com.floreantpos.ui.dialog;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Timer;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

import com.floreantpos.isdnmonitor.VibrantDialog;
import com.floreantpos.main.Application;
import com.floreantpos.swing.ImageIcon;

/**
 * 
 * @author MShahriar
 */
public class POSMessageDialog extends javax.swing.JDialog{

	private static Logger logger = Logger.getLogger(Application.class);

	public static void showMessage(String message) {
		ErrorMessageDialog dialog = new ErrorMessageDialog(message);
		dialog.pack();
		dialog.open();
	}
	
	public static void showMessage(Component parent, String message) {
	
		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.INFORMATION_MESSAGE, null);
	}

	public static void showError(String message) {
		ErrorMessageDialog dialog = new ErrorMessageDialog(message);
		dialog.pack();
		dialog.open();
	} 

	public static boolean showYesNo(String message) {
		ErrorMessageDialog dialog = new ErrorMessageDialog(message, true, true);
		dialog.pack();
		dialog.open();
		boolean okOrNot =dialog.isOk();
		return okOrNot;
	}
	
	public static void showError(Component parent, String message) {
		
		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(String message, Throwable x) {
		x.printStackTrace();
		logger.error(message, x);
		
		JOptionPane.showMessageDialog(Application.getPosWindow(), message, com.floreantpos.POSConstants.ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE, null);
	}

	public static void showError(Component parent, String message, Throwable x) {
		x.printStackTrace();
		logger.error(message, x);
		
		JOptionPane.showMessageDialog(parent, message, com.floreantpos.POSConstants.MDS_POS, JOptionPane.ERROR_MESSAGE, null);
	}
	
	
}
