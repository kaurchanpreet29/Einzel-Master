package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.KundenDB;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuModifierDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.datamigrate.Elements;

public class KundenExportAction extends AbstractAction{
	public KundenExportAction() {
		super("Kundendaten Export");
	}
	public KundenExportAction(String name) {
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

			boolean success = KundenDB.exportKunden(file);

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

			boolean success = KundenDB.exportKunden(file);

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
		fileChooser.setSelectedFile(new File("Khana-Kunden-Daten.xml"));
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
