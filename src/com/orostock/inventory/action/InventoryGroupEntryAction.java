package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryGroup;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.orostock.inventory.ui.InventoryGroupEntryForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class InventoryGroupEntryAction
  extends AbstractAction
{
  public InventoryGroupEntryAction()
  {
    super("New Inventory Group");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    InventoryGroupEntryForm form = new InventoryGroupEntryForm(new InventoryGroup());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
  }
}
