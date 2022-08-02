package com.orostock.inventory.ui;

import com.floreantpos.model.inventory.InOutEnum;
import com.floreantpos.model.inventory.InventoryItem;
import com.floreantpos.model.inventory.InventoryTransaction;
import com.floreantpos.model.inventory.InventoryTransactionType;
import com.floreantpos.model.inventory.InventoryVendor;
import com.floreantpos.model.inventory.PurchaseOrder;
import com.floreantpos.model.inventory.dao.InventoryItemDAO;
import com.floreantpos.model.inventory.dao.InventoryTransactionDAO;
import com.floreantpos.model.inventory.dao.InventoryTransactionTypeDAO;
import com.floreantpos.model.inventory.dao.InventoryVendorDAO;
import com.floreantpos.model.inventory.dao.PurchaseOrderDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXDatePicker;

public class InventoryTransactionEntryForm
  extends BeanEditor<InventoryTransaction>
{
  private JTextField tfItem;
  private DoubleTextField tfUnitPrice;
  private JXComboBox cbTransactionType;
  private JXComboBox cbVendor;
  private JXDatePicker datePicker;
  private JTextArea taNote;
  private IntegerTextField tfUnit;
  private JTextField tfPO;
  private InventoryItem inventoryItem;
  
  public InventoryTransactionEntryForm()
  {
    createUI();
    
    List<InventoryVendor> vendors = InventoryVendorDAO.getInstance().findAll();
    
    List<InventoryTransactionType> transactionTypes = InventoryTransactionTypeDAO.getInstance().findAll();
    if (transactionTypes.size() == 0)
    {
      InventoryTransactionType transactionType = new InventoryTransactionType();
      transactionType.setName("IN");
      transactionType.setInOrOutEnum(InOutEnum.IN);
      InventoryTransactionTypeDAO.getInstance().save(transactionType);
      
      transactionType = new InventoryTransactionType();
      transactionType.setName("OUT");
      transactionType.setInOrOutEnum(InOutEnum.OUT);
      InventoryTransactionTypeDAO.getInstance().save(transactionType);
      
      transactionType = new InventoryTransactionType();
      transactionType.setName("UNCHANGED");
      transactionType.setInOrOutEnum(InOutEnum.UNCHANGED);
      InventoryTransactionTypeDAO.getInstance().save(transactionType);
      
      transactionTypes = InventoryTransactionTypeDAO.getInstance().findAll();
    }
    this.cbTransactionType.setModel(new DefaultComboBoxModel(transactionTypes.toArray(new InventoryTransactionType[0])));
    this.cbVendor.setModel(new DefaultComboBoxModel(vendors.toArray(new InventoryVendor[0])));
  }
  
  private void createUI()
  {
    setLayout(new MigLayout());
    
    add(new JLabel("Reference#"));
    this.tfPO = new JTextField(10);
    add(this.tfPO, "wrap, w 150px");
    
    add(new JLabel("Transaction Type"));
    this.cbTransactionType = new JXComboBox();
    add(this.cbTransactionType, "wrap, w 150px");
    
    add(new JLabel("Item"));
    this.tfItem = new JTextField(20);
    this.tfItem.setEnabled(false);
    add(this.tfItem, "grow, wrap");
    
    add(new JLabel("Unit Price"));
    this.tfUnitPrice = new DoubleTextField(20);
    add(this.tfUnitPrice, "grow, wrap");
    
    add(new JLabel("Unit"));
    this.tfUnit = new IntegerTextField(20);
    add(this.tfUnit, "grow, wrap");
    
    add(new JLabel("Date"));
    this.datePicker = new JXDatePicker(new Date());
    add(this.datePicker, "wrap, w 200px");
    
    add(new JLabel("Vendor"));
    this.cbVendor = new JXComboBox();
    add(this.cbVendor, "wrap, w 200px");
    




    add(new JLabel("Note"));
    this.taNote = new JTextArea();
    add(new JScrollPane(this.taNote), "grow, h 100px, wrap");
  }
  
  public void setInventoryItem(InventoryItem item)
  {
    this.inventoryItem = item;
    this.tfItem.setText(item.getName());
  }
  
  public boolean save()
  {
    Session session = null;
    Transaction tx = null;
    try
    {
      if (!updateModel()) {
        return false;
      }
      InventoryTransaction inventoryTransaction = (InventoryTransaction)getBean();
      
      InOutEnum inOutEnum = InOutEnum.fromInt(inventoryTransaction.getTransactionType().getInOrOut().intValue());
      switch (inOutEnum.ordinal())
      {
      case 1: 
        this.inventoryItem.setTotalPackages(Integer.valueOf(this.inventoryItem.getTotalPackages().intValue() + inventoryTransaction.getQuantity().intValue()));
        


        break;
      case 2: 
        this.inventoryItem.setTotalPackages(Integer.valueOf(this.inventoryItem.getTotalPackages().intValue() - inventoryTransaction.getQuantity().intValue()));
        

        break;
      }
      session = InventoryTransactionDAO.getInstance().createNewSession();
      tx = session.beginTransaction();
      
      PurchaseOrder purchaseOrder = inventoryTransaction.getReferenceNo();
      
      PurchaseOrderDAO.getInstance().saveOrUpdate(purchaseOrder, session);
      InventoryTransactionDAO.getInstance().saveOrUpdate(inventoryTransaction, session);
      InventoryItemDAO.getInstance().saveOrUpdate(this.inventoryItem);
      
      tx.commit();
    }
    catch (Exception e)
    {
      if (tx != null) {
        tx.rollback();
      }
      if (session != null) {
        session.close();
      }
      POSMessageDialog.showError(e.getMessage(), e);
      return false;
    }
    return true;
  }
  
  protected void updateView() {}
  
  protected boolean updateModel()
    throws IllegalModelStateException
  {
    InventoryTransaction transaction = (InventoryTransaction)getBean();
    
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setOrderId(this.tfPO.getText());
    
    transaction.setTransactionType((InventoryTransactionType)this.cbTransactionType.getSelectedItem());
    transaction.setReferenceNo(purchaseOrder);
    transaction.setTransactionType((InventoryTransactionType)this.cbTransactionType.getSelectedItem());
    transaction.setInventoryItem(this.inventoryItem);
    transaction.setUnitPrice(Double.valueOf(this.tfUnitPrice.getDouble()));
    transaction.setQuantity(Integer.valueOf(this.tfUnit.getInteger()));
    transaction.setTransactionDate(this.datePicker.getDate());
    transaction.setVendor((InventoryVendor)this.cbVendor.getSelectedItem());
    

    return true;
  }
  
  public String getDisplayText()
  {
    return "New transaction";
  }
}
