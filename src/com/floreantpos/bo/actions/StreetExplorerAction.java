package com.floreantpos.bo.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.MenuItemExplorer;
import com.floreantpos.bo.ui.explorer.StreetExplorer;

public class StreetExplorerAction extends AbstractAction {

	public StreetExplorerAction() {
		super("Strasse");
	}

	public StreetExplorerAction(String name) {
		super(name);
		init();
	}

	public StreetExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane;
		StreetExplorer item;
		tabbedPane = backOfficeWindow.getTabbedPane();
		tabbedPane.setBackground(new Color(209,222,235));
		int index = tabbedPane.indexOfTab("Strassenverzeichnis");
		if (index == -1) {
			item = new StreetExplorer();
			tabbedPane.addTab("Strassenverzeichnis", item);
		}
		else {
			item = (StreetExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(item);
	}
	public void init() {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane;
		StreetExplorer item;
		tabbedPane = backOfficeWindow.getTabbedPane();
		tabbedPane.setBackground(new Color(209,222,235));
		int index = tabbedPane.indexOfTab("Strassenverzeichnis");
		if (index == -1) {
			item = new StreetExplorer();
			tabbedPane.addTab("Strassenverzeichnis", item);
		}
		else {
			item = (StreetExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(item);
	}
}
