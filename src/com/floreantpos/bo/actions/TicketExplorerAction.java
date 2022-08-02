package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.TicketExplorer;

public class TicketExplorerAction extends AbstractAction {

	public TicketExplorerAction() {
		super("Rechnung");
	}

	public TicketExplorerAction(String name) {
		super(name);
		init();
	}

	public TicketExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		
		TicketExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Rechnung verwaltung");
		if (index == -1) {
			explorer = new TicketExplorer();
			tabbedPane.addTab("Rechnung verwaltung", explorer);
		}
		else {
			explorer = (TicketExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
	public void init() {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		
		TicketExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Rechnung verwaltung");
		if (index == -1) {
			explorer = new TicketExplorer();
			tabbedPane.addTab("Rechnung verwaltung", explorer);
		} else {
			explorer = (TicketExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
}
