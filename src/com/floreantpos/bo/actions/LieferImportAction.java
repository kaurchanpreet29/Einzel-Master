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
import com.floreantpos.model.DeliveryCostDB;
import com.floreantpos.model.Gebiet;
import com.floreantpos.model.dao.DeliveryCostDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class LieferImportAction extends AbstractAction {

	public LieferImportAction() {
		super("Import Liefergebiet");
	}
	public LieferImportAction(String name) {
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

			importLieferFromFile(file);
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

			importLieferFromFile(file);
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");

		} catch (Exception e1) {
			e1.printStackTrace();

			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}

	}

	public static void importLieferFromFile(File file) throws Exception {
		if (file == null)
			return;
		FileInputStream	inputStream = new FileInputStream(file);
		importLiefer(inputStream);
	}

	public static void importLiefer(InputStream inputStream) throws Exception {
		
		Session session = null;
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();
		try {
			session = dao.createNewSession();
		
			transaction = session.beginTransaction();
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryCostDB.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			DeliveryCostDB lieferDb = (DeliveryCostDB) unmarshaller.unmarshal(inputStream);
			
			for(Iterator<Gebiet> itr = lieferDb.getGebiet().iterator();itr.hasNext();)
			{
				Gebiet gebiet = itr.next();
				DeliveryCostDAO.getInstance().save(gebiet,session);
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
