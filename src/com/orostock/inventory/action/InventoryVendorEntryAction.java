package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryVendor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.orostock.inventory.ui.InventoryVendorEntryForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class InventoryVendorEntryAction
  extends AbstractAction
{
  public InventoryVendorEntryAction()
  {
    super("New Inventory Vendor");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    InventoryVendorEntryForm form = new InventoryVendorEntryForm(new InventoryVendor());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
  }
}
