package com.orostock.inventory.ui;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.Command;
import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.inventory.InventoryItem;
import com.floreantpos.model.inventory.InventoryTransaction;
import com.floreantpos.model.inventory.dao.InventoryItemDAO;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.IOUtils;
import org.jdesktop.swingx.JXTable;

public class InventoryItemBrowser
  extends ModelBrowser<InventoryItem>
{
  private JButton btnNewTransaction = new JButton("NEW TRANSACTION");
  
  public InventoryItemBrowser()
  {
    super(new InventoryItemEntryForm());
    
    JPanel buttonPanel = new JPanel();
    JButton btnExport = new JButton("Export Items");
    btnExport.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        InventoryItemBrowser.this.exportInventoryItems();
      }
    });
    buttonPanel.add(btnExport);
    
    this.browserPanel.add(buttonPanel, "South");
    
    this.btnNewTransaction.setActionCommand(Command.NEW_TRANSACTION.name());
    this.btnNewTransaction.setEnabled(false);
    
    init(new InventoryItemTableModel());
  }
  
  protected void exportInventoryItems()
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileFilter()
    {
      public String getDescription()
      {
        return "CSV file";
      }
      
      public boolean accept(File f)
      {
        return f.getName().toLowerCase().endsWith(".csv");
      }
    });
    fileChooser.setFileSelectionMode(0);
    int option = fileChooser.showSaveDialog(this);
    if (option != 0) {
      return;
    }
    File selectedFile = fileChooser.getSelectedFile();
    
    FileWriter out = null;
    try
    {
      out = new FileWriter(selectedFile);
      List<InventoryItem> inventoryItems = InventoryItemDAO.getInstance().findAll();
      
      String[] header = { "NAME", "UNIT_PER_PACKAGE", "TOTAL_PACKAGES", "AVERAGE_PACKAGE_PRICE", "TOTAL_RECEPIE_UNITS", "UNIT_PURCHASE_PRICE", "PACKAGE_BARCODE", "UNIT_BARCODE", "PACKAGE_DESC", "SORT_ORDER", "PACKAGE_REORDER_LEVEL", "PACKAGE_REPLENISH_LEVEL", "DESCRIPTION", "UNIT_SELLING_PRICE" };
      


      String line = "";
      for (String string : header) {
        line = line + string + ",";
      }
      out.write(line);
      out.write("\n");
      for (Iterator iter = inventoryItems.iterator();iter.hasNext();)
      {
        InventoryItem inventoryItem = (InventoryItem)iter.next();
        line = "";
        




        line = line + inventoryItem.getName() + ",";
        line = line + inventoryItem.getUnitPerPackage() + ",";
        line = line + inventoryItem.getTotalPackages() + ",";
        line = line + inventoryItem.getAveragePackagePrice() + ",";
        line = line + inventoryItem.getTotalRecepieUnits() + ",";
        line = line + inventoryItem.getUnitPurchasePrice() + ",";
        line = line + inventoryItem.getPackageBarcode() + ",";
        line = line + inventoryItem.getUnitBarcode() + ",";
        line = line + inventoryItem.getPackagingUnit() + ",";
        line = line + inventoryItem.getSortOrder() + ",";
        line = line + inventoryItem.getPackageReorderLevel() + ",";
        line = line + inventoryItem.getPackageReplenishLevel() + ",";
        line = line + inventoryItem.getDescription() + ",";
        line = line + inventoryItem.getUnitSellingPrice() + ",";
        
        out.write(line);
        out.write("\n");
      }
      JOptionPane.showMessageDialog(this, "Exported");
    }
    catch (Exception e)
    {
      POSMessageDialog.showError(BackOfficeWindow.getInstance(), e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      IOUtils.closeQuietly(out);
    }
  }
  
  public void loadData()
  {
    List<InventoryItem> inventoryItems = InventoryItemDAO.getInstance().findAll();
    InventoryItemTableModel tableModel = (InventoryItemTableModel)this.browserTable.getModel();
    tableModel.setRows(inventoryItems);
  }
  
  public void refreshTable()
  {
    loadData();
  }
  
  protected JButton getAdditionalButton()
  {
    return this.btnNewTransaction;
  }
  
  protected void handleAdditionaButtonActionIfApplicable(ActionEvent e)
  {
    if (e.getActionCommand().equalsIgnoreCase(Command.NEW_TRANSACTION.name()))
    {
      InventoryItem bean = (InventoryItem)this.beanEditor.getBean();
      InventoryTransactionEntryForm form = new InventoryTransactionEntryForm();
      form.setBean(new InventoryTransaction());
      form.setInventoryItem(bean);
      BeanEditorDialog dialog = new BeanEditorDialog(form, BackOfficeWindow.getInstance(), true);
      dialog.pack();
      dialog.open();
      
      refreshTable();
    }
    else
    {
      InventoryItem bean = (InventoryItem)this.beanEditor.getBean();
      if ((bean != null) && (bean.getId() != null)) {
        this.btnNewTransaction.setEnabled(true);
      } else {
        this.btnNewTransaction.setEnabled(false);
      }
    }
  }
  
  public void valueChanged(ListSelectionEvent e)
  {
    super.valueChanged(e);
    
    InventoryItem bean = (InventoryItem)this.beanEditor.getBean();
    if ((bean != null) && (bean.getId() != null)) {
      this.btnNewTransaction.setEnabled(true);
    } else {
      this.btnNewTransaction.setEnabled(false);
    }
  }
  
  protected void searchInventoryItem() {}
  
  static class InventoryItemTableModel
    extends ListTableModel<InventoryItem>
  {
    NumberFormat f = new DecimalFormat("0.##");
    
    public InventoryItemTableModel()
    {
      super();
    }
    
    public InventoryItemTableModel(List<InventoryItem> items)
    {
      super();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex)
    {
      InventoryItem row = (InventoryItem)getRowData(rowIndex);
      switch (columnIndex)
      {
      case 0: 
        return row.getName();
      case 1: 
        return row.getDescription();
      case 2: 
        return row.getTotalPackages();
      case 3: 
        return this.f.format(row.getUnitPurchasePrice());
      }
      return row.getName();
    }
  }
}
