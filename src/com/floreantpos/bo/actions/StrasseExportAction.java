package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.StreetDB;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class StrasseExportAction extends AbstractAction{
	public StrasseExportAction() {
		super("Export Strasse");
	}
	public StrasseExportAction(String name) {
		super(name);
		init();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Session session = null;
		Transaction transaction = null;
		FileWriter fileWriter = null;
		GenericDAO dao = new GenericDAO();

		try {
			JFileChooser fileChooser = getFileChooser();
			int option = fileChooser.showSaveDialog(BackOfficeWindow.getInstance());
			if (option != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();
			if (file.exists()) {
				option = JOptionPane.showConfirmDialog(BackOfficeWindow.getInstance(), "Überschreiben von Dateien " + file.getName() + "?", "Bestätigen",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			boolean success = StreetDB.exportStreet(file);

			if(success == true)
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");
			else
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Fehler!");
			

		} catch (Exception e1) {
			transaction.rollback();
			e1.printStackTrace();
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		} finally {
			IOUtils.closeQuietly(fileWriter);
			dao.closeSession(session);
		}
	}
	public void init() {
		Session session = null;
		Transaction transaction = null;
		FileWriter fileWriter = null;
		GenericDAO dao = new GenericDAO();

		try {
			JFileChooser fileChooser = getFileChooser();
			int option = fileChooser.showSaveDialog(BackOfficeWindow.getInstance());
			if (option != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = fileChooser.getSelectedFile();
			if (file.exists()) {
				option = JOptionPane.showConfirmDialog(BackOfficeWindow.getInstance(), "Überschreiben von Dateien " + file.getName() + "?", "Bestätigen",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			}

			boolean success = StreetDB.exportStreet(file);

			if(success == true)
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");
			else
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Fehler!");
			

		} catch (Exception e1) {
			transaction.rollback();
			e1.printStackTrace();
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		} finally {
			IOUtils.closeQuietly(fileWriter);
			dao.closeSession(session);
		}
	}
	public static JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setSelectedFile(new File("Khana-Street-Daten.xml"));
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "XML File";
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".xml")) {
					return true;
				}

				return false;
			}
		});
		return fileChooser;
	}
}
