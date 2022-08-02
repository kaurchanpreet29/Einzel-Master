package com.orostock.inventory.ui.recepie;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.inventory.InventoryItem;
import com.floreantpos.model.inventory.Recepie;
import com.floreantpos.model.inventory.RecepieItem;
import com.floreantpos.swing.IUpdatebleView;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class RecepieView
  extends JPanel
  implements IUpdatebleView<MenuItem>
{
  JTable recepieItemTable = new JTable(new RecepieItemTableModel());
  JButton btnAddItem = new JButton("Add Inventory Item");
  JButton btnDeleteItem = new JButton("Delete Selected Item");
  InventoryItemSelector itemSelector = new InventoryItemSelector();
  boolean inited = false;
  
  public RecepieView()
  {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    add(new JScrollPane(this.recepieItemTable));
    
    this.btnAddItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        RecepieView.this.addInventoryItem();
      }
    });
    this.btnDeleteItem.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        int selectedRow = RecepieView.this.recepieItemTable.getSelectedRow();
        if (selectedRow < 0) {
          return;
        }
        RecepieView.RecepieItemTableModel model = (RecepieView.RecepieItemTableModel)RecepieView.this.recepieItemTable.getModel();
        model.deleteItem(selectedRow);
      }
    });
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(this.btnAddItem);
    buttonPanel.add(this.btnDeleteItem);
    
    add(buttonPanel, "South");
  }
  
  protected void addInventoryItem()
  {
    this.itemSelector.loadData();
    this.itemSelector.open();
    if (this.itemSelector.isCanceled()) {
      return;
    }
    RecepieItem item = new RecepieItem();
    item.setPercentage(Double.valueOf(this.itemSelector.getPercentage()));
    item.setInventoryItem(this.itemSelector.getSelectedItem());
    
    RecepieItemTableModel model = (RecepieItemTableModel)this.recepieItemTable.getModel();
    model.addItem(item);
  }
  
  public boolean updateModel(MenuItem e)
  {
    RecepieItemTableModel model = (RecepieItemTableModel)this.recepieItemTable.getModel();
    if (model.getRowCount() == 0) {
      return true;
    }
    Recepie recepie = e.getRecepie();
    if (recepie == null)
    {
      recepie = new Recepie();
      recepie.setMenuItem(e);
      e.setRecepie(recepie);
    }
    recepie.getRecepieItems().clear();
    
    List<RecepieItem> rows = model.getRows();
    for (RecepieItem recepieItem : rows) {
      recepie.addRecepieItem(recepieItem);
    }
    return true;
  }
  
  public void initView(MenuItem e)
  {
    if (this.inited) {
      return;
    }
    Recepie recepie = e.getRecepie();
    if (recepie == null) {
      return;
    }
    List<RecepieItem> items = new ArrayList(recepie.getRecepieItems());
    RecepieItemTableModel model = (RecepieItemTableModel)this.recepieItemTable.getModel();
    model.setRows(items);
    
    this.inited = true;
  }
  
  class RecepieItemTableModel
    extends ListTableModel<RecepieItem>
  {
    RecepieItemTableModel()
    {
      super();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      RecepieItem item = (RecepieItem)getRowData(rowIndex);
      switch (columnIndex)
      {
      case 0: 
        return item.getInventoryItem().getName();
      case 1: 
        return item.getPercentage();
      }
      return null;
    }
  }
}
