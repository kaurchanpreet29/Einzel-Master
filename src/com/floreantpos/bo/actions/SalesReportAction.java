package com.floreantpos.bo.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.report.ReportViewer;
import com.floreantpos.report.SalesReport;

public class SalesReportAction extends AbstractAction {

	public SalesReportAction() {
		super(com.floreantpos.POSConstants.SALES_REPORT);
	}

	public SalesReportAction(String name) {
		super(name);
	}

	public SalesReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		tabbedPane.setBackground(new Color(209,222,235));
		ReportViewer viewer = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.SALES_REPORT);
		if (index == -1) {
			viewer = new ReportViewer(new SalesReport());
			tabbedPane.addTab(com.floreantpos.POSConstants.SALES_REPORT, viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		viewer.setBackground(new Color(209,222,235));
		tabbedPane.setSelectedComponent(viewer);
	}

}
