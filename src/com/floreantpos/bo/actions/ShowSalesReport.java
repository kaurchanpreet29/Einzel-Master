package com.floreantpos.bo.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.floreantpos.ui.dialog.ShowSalesReportDialog;

public class ShowSalesReport extends AbstractAction{

	public ShowSalesReport() {
		super("Datum Import");
	}
	public ShowSalesReport(String name) {
		super(name);
		init();
	}
	
	public void init() {
		ShowSalesReportDialog dialog = new ShowSalesReportDialog();
		dialog.pack();
		dialog.setPreferredSize(new Dimension(800,600));
		dialog.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
	}
}
