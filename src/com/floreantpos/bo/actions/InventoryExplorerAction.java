package com.floreantpos.bo.actions;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.InventoryExplorer;


public class InventoryExplorerAction extends AbstractAction {

	public InventoryExplorerAction() {
		super(com.floreantpos.POSConstants.TAX);
	}

	public InventoryExplorerAction(String name) {
		super(name);
		init();
	}

	public InventoryExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
		
		InventoryExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(POSConstants.TAX_EXPLORER);
		if (index == -1) {
			explorer = new InventoryExplorer();
			tabbedPane.addTab(POSConstants.TAX_EXPLORER, explorer);
		}
		else {
			explorer = (InventoryExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
	
	public void init() {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();		
		InventoryExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(POSConstants.TAX_EXPLORER);
		if (index == -1) {
			explorer = new InventoryExplorer();
			tabbedPane.addTab(POSConstants.TAX_EXPLORER, explorer);
		}
		else {
			explorer = (InventoryExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
}
