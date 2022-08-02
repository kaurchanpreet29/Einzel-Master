package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;



import javax.swing.AbstractAction;
import javax.swing.JFileChooser;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.Street;
import com.floreantpos.model.StreetDB;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.StreetDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.datamigrate.Elements;


public class StasseImportAction extends AbstractAction {

	public StasseImportAction() {
		super("Import Strasse");
	}
	public StasseImportAction(String name) {
		super(name);
		init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = DataExportAction.getFileChooser();
		int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();
		try {

			importStreetFromFile(file);
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");

		} catch (Exception e1) {
			e1.printStackTrace();

			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}

	}

	public void init() {
		JFileChooser fileChooser = DataExportAction.getFileChooser();
		int option = fileChooser.showOpenDialog(BackOfficeWindow.getInstance());
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();
		try {

			importStreetFromFile(file);
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");

		} catch (Exception e1) {
			e1.printStackTrace();

			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}

	}

	public static void importStreetFromFile(File file) throws Exception {
		if (file == null)
			return;

		FileInputStream	inputStream = new FileInputStream(file);
		importStreet(inputStream);
	}

	public static void importStreet(InputStream inputStream) throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();
		try {
			session = dao.createNewSession();
		
			transaction = session.beginTransaction();
			
			JAXBContext jaxbContext = JAXBContext.newInstance(StreetDB.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StreetDB streetDb = (StreetDB) unmarshaller.unmarshal(inputStream);
			
			for(Iterator<Street> itr = streetDb.getStreet().iterator();itr.hasNext();)
			{
				Street street = itr.next();
				StreetDAO.getInstance().save(street,session);
			}
			if(transaction.wasCommitted())
				System.out.println("Transaction committed");
			transaction.commit();
			dao.closeSession(session);
			
		} catch (Exception e1) {

			if (transaction != null)
				transaction.rollback();
			throw e1;

		} finally {
			dao.closeSession(session);
			IOUtils.closeQuietly(inputStream);
		}
	}
}
