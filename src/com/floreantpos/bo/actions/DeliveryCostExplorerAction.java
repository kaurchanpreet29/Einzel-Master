package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.DeliveryCostExplorer;
import com.floreantpos.bo.ui.explorer.MenuItemExplorer;

public class DeliveryCostExplorerAction extends AbstractAction {

	public DeliveryCostExplorerAction() {
		super("Lieferkostenverwaltung");
		
	}

	public DeliveryCostExplorerAction(String name) {
		super(name);
		init();
	}

	public DeliveryCostExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane;
		DeliveryCostExplorer item;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Lieferkostenverwaltung");
		if (index == -1) {
			item = new DeliveryCostExplorer();
			tabbedPane.addTab("Lieferkostenverwaltung", item);
		}
		else {
			item = (DeliveryCostExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(item);
	}
	public void init() {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane;
		DeliveryCostExplorer item;
		tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Lieferkostenverwaltung");
		if (index == -1) {
			item = new DeliveryCostExplorer();
			tabbedPane.addTab("Lieferkostenverwaltung", item);
		}
		else {
			item = (DeliveryCostExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(item);
	}
}