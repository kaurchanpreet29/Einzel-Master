package com.orostock.inventory.ui;

import com.floreantpos.PosException;
import com.floreantpos.model.inventory.InventoryVendor;
import com.floreantpos.model.inventory.dao.InventoryVendorDAO;
import com.floreantpos.swing.POSTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.POSUtil;
import java.awt.BorderLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

public class InventoryVendorEntryForm
  extends BeanEditor<InventoryVendor>
{
  private JCheckBox chkVisible;
  private POSTextField tfName;
  
  public InventoryVendorEntryForm(InventoryVendor ig)
  {
    setLayout(new BorderLayout());
    
    createUI();
    
    setBean(ig);
  }
  
  private void createUI()
  {
    JPanel panel = new JPanel();
    add(panel, "Center");
    panel.setLayout(new MigLayout("", "[][grow]", "[][]"));
    
    JLabel lblName = new JLabel("Name");
    panel.add(lblName, "cell 0 0,alignx trailing");
    
    this.tfName = new POSTextField();
    panel.add(this.tfName, "cell 1 0,growx");
    
    this.chkVisible = new JCheckBox("Visible", true);
    panel.add(this.chkVisible, "cell 1 1");
  }
  
  public void updateView()
  {
    InventoryVendor model = (InventoryVendor)getBean();
    if (model == null) {
      return;
    }
    this.tfName.setText(model.getName());
    if (model.getId() != null) {
      this.chkVisible.setSelected(POSUtil.getBoolean(model.isVisible()));
    }
  }
  
  public boolean updateModel()
  {
    InventoryVendor model = (InventoryVendor)getBean();
    if (model == null) {
      model = new InventoryVendor();
    }
    String nameString = this.tfName.getText();
    if (StringUtils.isEmpty(nameString)) {
      throw new PosException("Name cannot be empty");
    }
    model.setName(nameString);
    model.setVisible(Boolean.valueOf(this.chkVisible.isSelected()));
    
    return true;
  }
  
  public String getDisplayText()
  {
    return "Add inventory Entry";
  }
  
  public boolean save()
  {
    try
    {
      if (!updateModel()) {
        return false;
      }
      InventoryVendor model = (InventoryVendor)getBean();
      InventoryVendorDAO.getInstance().saveOrUpdate(model);
      
      return true;
    }
    catch (Exception e)
    {
      POSMessageDialog.showError(e.getMessage());
    }
    return false;
  }
}
