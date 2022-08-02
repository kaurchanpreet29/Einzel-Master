package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryMetaCode;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.orostock.inventory.ui.InventoryMetacodeEntryForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class InventoryMetaCodeEntryAction
  extends AbstractAction
{
  public InventoryMetaCodeEntryAction()
  {
    super("New Inventory Meta code");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    InventoryMetacodeEntryForm form = new InventoryMetacodeEntryForm(new InventoryMetaCode());
    BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
    dialog.pack();
    dialog.open();
  }
}
