package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.orostock.inventory.ui.InventoryItemBrowser;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

public class InventoryItemBrowserAction
  extends AbstractAction
{
  public InventoryItemBrowserAction()
  {
    super("Inventory Item Browser");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    BackOfficeWindow window = BackOfficeWindow.getInstance();
    JTabbedPane tabbedPane = window.getTabbedPane();
    
    InventoryItemBrowser browser = null;
    
    int index = tabbedPane.indexOfTab("Inventory Item Browser");
    if (index == -1)
    {
      browser = new InventoryItemBrowser();
      tabbedPane.addTab("Inventory Item Browser", browser);
      browser.loadData();
    }
    else
    {
      browser = (InventoryItemBrowser)tabbedPane.getComponentAt(index);
    }
    tabbedPane.setSelectedComponent(browser);
  }
}
