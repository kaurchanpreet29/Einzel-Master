package com.oro.orderextension;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.customer.CustomerExplorer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

public class CustomerExplorerAction
  extends AbstractAction
{
  public CustomerExplorerAction()
  {
    super("Kunden");
  }
  
  public CustomerExplorerAction(String name)
  {
    super(name);
    init();
  }
  
  public CustomerExplorerAction(String name, Icon icon)
  {
    super(name, icon);
  }
  
  public void actionPerformed(ActionEvent e)
  {
    BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
    
    CustomerExplorer explorer = null;
    JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
    int index = tabbedPane.indexOfTab("Customer");
    if (index == -1)
    {
      explorer = new CustomerExplorer();
      tabbedPane.addTab("Customer", explorer);
    }
    else
    {
      explorer = (CustomerExplorer)tabbedPane.getComponentAt(index);
    }
    tabbedPane.setSelectedComponent(explorer);
  }
  public void init()
  {
    BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();
    
    CustomerExplorer explorer = null;
    JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
    int index = tabbedPane.indexOfTab("Customer");
    if (index == -1)
    {
      explorer = new CustomerExplorer();
      tabbedPane.addTab("Customer", explorer);
    }
    else
    {
      explorer = (CustomerExplorer)tabbedPane.getComponentAt(index);
    }
    tabbedPane.setSelectedComponent(explorer);
  }
}
