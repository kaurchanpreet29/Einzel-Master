package com.orostock.inventory.ui.recepie;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.inventory.InventoryItem;
import com.floreantpos.model.inventory.dao.InventoryItemDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.jidesoft.swing.AutoCompletionComboBox;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class InventoryItemSelector
  extends BeanEditorDialog
{
  AutoCompletionComboBox comboBox = new AutoCompletionComboBox();
  DoubleTextField tfPercentage = new DoubleTextField(5);
  InventoryItem selectedItem;
  double percentage;
  boolean dataLoaded;
  
  public InventoryItemSelector()
  {
    super(BackOfficeWindow.getInstance(), true);
    setBeanEditor(new InventoryItemView());
  }
  
  public InventoryItem getSelectedItem()
  {
    return this.selectedItem;
  }
  
  public double getPercentage()
  {
    return this.percentage;
  }
  
  public void loadData()
  {
    if (this.dataLoaded) {
      return;
    }
    List<InventoryItem> list = InventoryItemDAO.getInstance().findAll();
    this.comboBox.setModel(new DefaultComboBoxModel(list.toArray(new InventoryItem[0])));
    
    this.dataLoaded = true;
  }
  
  public void dispose()
  {
    BeanEditor editor = getBeanEditor();
    
    super.dispose();
    
    setBeanEditor(editor);
  }
  
  class InventoryItemView
    extends BeanEditor<InventoryItem>
  {
    public InventoryItemView()
    {
      setLayout(new BorderLayout());
      
      JPanel panel = new JPanel(new MigLayout());
      panel.add(new JLabel("Inventory item"));
      panel.add(InventoryItemSelector.this.comboBox, "w 250px,wrap");
      
      panel.add(new JLabel("Percentage"));
      panel.add(InventoryItemSelector.this.tfPercentage);
      
      add(panel);
    }
    
    public boolean save()
    {
      InventoryItemSelector.this.selectedItem = ((InventoryItem)InventoryItemSelector.this.comboBox.getSelectedItem());
      InventoryItemSelector.this.percentage = ((float)InventoryItemSelector.this.tfPercentage.getDouble());
      
      return true;
    }
    
    protected void updateView() {}
    
    protected boolean updateModel()
      throws IllegalModelStateException
    {
      return true;
    }
    
    public String getDisplayText()
    {
      return "Select inventory item";
    }
  }
}
