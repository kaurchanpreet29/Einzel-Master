package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.CardReportView;
import com.floreantpos.report.TableReportView;
import com.floreantpos.ui.dialog.CardReportDialog;
import com.floreantpos.ui.dialog.TableReportDialog;

import net.sf.jasperreports.engine.JRException;


public class TableReportAction extends AbstractAction {

	public TableReportAction(String name) throws Exception {
		super(name);
//		init();
	}
//	public void init() throws Exception
//	{
//		TableReportView reportView = null;
//		reportView = new TableReportView();
//		TableReportDialog dialog = new TableReportDialog(reportView);
//		dialog.open();
//		dialog.pack();
//		
//	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		TableReportView reportView = null;
		try {
			reportView = new TableReportView();
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TableReportDialog dialog = new TableReportDialog(reportView);
		dialog.open();
		dialog.pack();
		
	}
}
