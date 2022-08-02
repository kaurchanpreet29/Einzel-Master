package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.Customer;
import com.floreantpos.model.KundenDB;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.order.OrderView;

public class KundenImportAction extends AbstractAction{

	public KundenImportAction() {
		super("Kundendaten Import");
	}
	public KundenImportAction(String name) {
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

			boolean success = KundenDB.importKunden(file);
			if(success == true)
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");
			else
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Fehler!");

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

//			KundenDB.importKunden(file);		
			importCustomer(file);	
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Erfolg!");

		} catch (Exception e1) {
			e1.printStackTrace();
			try {
				importCustomer(file);		
				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Success!");
			} catch (Exception e11) {

				POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e11.getMessage());

			}
			
		}

	}
	
	public void importCustomer(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook fs = new XSSFWorkbook(fis);
			XSSFSheet sheet = fs.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();

			int cols = 0; // No of columns
			int tmp = 0;

			// This trick ensures that we get the data properly even if it doesn't start from first few rows

			row = sheet.getRow(0);
			if(row != null) {
				tmp = sheet.getRow(0).getPhysicalNumberOfCells();
				if(tmp > cols) cols = tmp;
			}
			int start = 0;
			for(int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				String Loyaltynumber ="";
				String firma = "";
				String saluation ="";
				String telephone ="";
				String strasse ="";
				String nr = "";
				String name = "";
				String city = "";
				String plz = "";
				if(row != null) {
					int iteration =0;
					for(int c = 0; c < cols; c++) {
						cell = row.getCell((short)c);
						if(cell != null&&cell.toString().compareTo("")!=0) {
							if(iteration==0) {
								String temp = cell.toString();
								if(temp.contains(".")) 
									temp = temp.substring(0,temp.indexOf("."));
								Loyaltynumber = temp;
							}
							if(iteration==1) {
								saluation = cell.toString();
							}
							if(iteration==2) {
								name = cell.toString();
							}
							if(iteration==3) {
								firma = cell.toString();
							}
							
							if(iteration==4) {
								telephone = cell.toString();
							}
							if(iteration==5) {									
								strasse = cell.toString();								
							}
							if(iteration==6) {
								nr = cell.toString();
								if(nr.contains("."))
									nr = nr.substring(0, nr.indexOf("."));
								
							}
							if(iteration==7) {
								plz =cell.toString();
								if(plz.contains("."))
									plz = plz.substring(0, plz.indexOf("."));
							}
							if(iteration==8) {	
								city = cell.toString();	
							}
							
						}
						++iteration;
					}
				}
				Customer customer =new Customer();
				customer.setLoyaltyNo(Loyaltynumber);
				customer.setAddress(strasse);
				customer.setSalutation(saluation);
				customer.setName(name);
				customer.setFirmName(firma);
				customer.setTelephoneNo(telephone);
				customer.setDoorNo(nr);
				customer.setZipCode(plz);
				customer.setCity(city);
				try {
					CustomerDAO.getInstance().saveOrUpdate(customer);
				}catch(Exception ex) {
					
				}

				++start;
			}
			fs.close();
		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}

}
