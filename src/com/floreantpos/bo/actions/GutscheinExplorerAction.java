package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.GutscheinExplorer;

public class GutscheinExplorerAction extends AbstractAction {

	public GutscheinExplorerAction() {
		super("Gutschein");
	}

	public GutscheinExplorerAction(String name) {
		super(name);
		init();
	}

	public GutscheinExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		
		GutscheinExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("GutscheinExplorer");
		if (index == -1) { 	
			explorer = new GutscheinExplorer();
			tabbedPane.addTab("GutscheinExplorer", explorer);
		}
		else {
			explorer = (GutscheinExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}

	public void init() {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		
		GutscheinExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("GutscheinExplorer");
		if (index == -1) {
			explorer = new GutscheinExplorer();
			tabbedPane.addTab("GutscheinExplorer", explorer);
		}
		else {
			explorer = (GutscheinExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
}
