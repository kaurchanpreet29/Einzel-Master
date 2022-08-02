package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.PrintSalesReport;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class SalesReportImport extends AbstractAction{

	public SalesReportImport() {
		super("Datum Import");
	}
	public SalesReportImport(String name) {
		super(name);
		init();
	}
	
	public void init() {
		File folder = new File("SalesReport");
		try {
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile() && file.getName().contains(".xml")) {
			    	PrintSalesReport printSalesReport = PrintSalesReport.importKunden(file);
					if(printSalesReport != null)
					{
						printSalesReport.printData();
					}
					else
					{
						POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), "Fehler!");
						continue;
					}
					file.delete();
			    }
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			POSMessageDialog.showMessage(BackOfficeWindow.getInstance(), e1.getMessage());
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
	}
}
