package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.report.MenuUsageReportView;
import com.floreantpos.ui.dialog.MenuUsageDialog;

public class MenuUsageReportAction extends AbstractAction {

	public MenuUsageReportAction() {
		super(com.floreantpos.POSConstants.MENU_USAGE_REPORT);
	}

	public MenuUsageReportAction(String name) {
		super(name);
		init();
	}

	public MenuUsageReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		MenuUsageReportView reportView = null;
		int index = tabbedPane.indexOfTab(com.floreantpos.POSConstants.MENU_USAGE_REPORT);
		if (index == -1) {
			reportView = new MenuUsageReportView();
			tabbedPane.addTab(com.floreantpos.POSConstants.MENU_USAGE_REPORT, reportView);
		}
		else {
			reportView = (MenuUsageReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}
	public void init()
	{
		MenuUsageReportView reportView = null;
		reportView = new MenuUsageReportView();
		MenuUsageDialog dialog = new MenuUsageDialog(reportView);
		reportView.setDialog(dialog);
		dialog.open();
		dialog.pack();
		
	}
}
