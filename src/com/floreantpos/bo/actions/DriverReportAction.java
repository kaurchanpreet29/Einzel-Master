package com.floreantpos.bo.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.DriverReport;
import com.floreantpos.report.ReportViewer;
import com.floreantpos.report.SalesReport;

public class DriverReportAction extends AbstractAction{
	public DriverReportAction(String name) {
		super(name);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		ReportViewer viewer = null;

		int index = tabbedPane.indexOfTab("Fahrerabschluss");
		tabbedPane.setBackground(new Color(209,222,235));
		if (index == -1) {
			viewer = new ReportViewer(new DriverReport());
			tabbedPane.addTab("Fahrerabschluss", viewer);
		}
		else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		viewer.setBackground(new Color(209,222,235));
		tabbedPane.setSelectedComponent(viewer);
		
	}

}
