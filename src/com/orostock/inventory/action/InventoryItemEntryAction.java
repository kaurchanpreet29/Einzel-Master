package com.orostock.inventory.action;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryItem;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.orostock.inventory.ui.InventoryItemEntryForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class InventoryItemEntryAction
  extends AbstractAction
{
  public InventoryItemEntryAction()
  {
    super("New Inventory Item");
  }
  
  public void actionPerformed(ActionEvent e)
  {
    InventoryItemEntryForm form = new InventoryItemEntryForm();
    form.setBean(new InventoryItem());
    BeanEditorDialog dialog = new BeanEditorDialog(BackOfficeWindow.getInstance(), true);
    dialog.setBeanEditor(form);
    dialog.pack();
    dialog.setLocationRelativeTo(BackOfficeWindow.getInstance());
    dialog.setVisible(true);
  }
}
