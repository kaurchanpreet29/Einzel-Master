package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryWarehouse;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.orostock.inventory.ui.InventoryWarehouseEntryForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class InventoryWarehouseEntryAction
  extends AbstractAction
{
  public InventoryWarehouseEntryAction()
  {
    super("New Inventory Warehouse");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    InventoryWarehouseEntryForm form = new InventoryWarehouseEntryForm(new InventoryWarehouse());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
  }
}
