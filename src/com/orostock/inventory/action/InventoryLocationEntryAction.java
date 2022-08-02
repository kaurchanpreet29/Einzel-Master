package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryLocation;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.orostock.inventory.ui.InventoryLocationEntryForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class InventoryLocationEntryAction
  extends AbstractAction
{
  public InventoryLocationEntryAction()
  {
    super("New Inventory Location");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    InventoryLocationEntryForm form = new InventoryLocationEntryForm(new InventoryLocation());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
  }
}
