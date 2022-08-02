package com.orostock.inventory.ui;

import com.floreantpos.model.PackagingDimension;
import com.floreantpos.model.PackagingUnit;
import com.floreantpos.model.dao.PackagingUnitDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.DoubleTextField;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang.StringUtils;

public class PackagingUnitForm
  extends BeanEditor<PackagingUnit>
{
  private FixedLengthTextField tfName = new FixedLengthTextField(30);
  private FixedLengthTextField tfShortName = new FixedLengthTextField(10);
  private DoubleTextField tfFactor = new DoubleTextField(10);
  private JComboBox<PackagingDimension> cbDimension = new JComboBox(PackagingDimension.values());
  
  public PackagingUnitForm()
  {
    this(null);
  }
  
  public PackagingUnitForm(PackagingUnit pu)
  {
    createUI();
    
    setBean(pu);
  }
  
  private void createUI()
  {
    setLayout(new MigLayout("fill"));
    
    add(new JLabel("Name"));
    add(this.tfName, "grow, wrap");
    
    add(new JLabel("Short name"));
    add(this.tfShortName, "grow, wrap");
    
    add(new JLabel("Factor"));
    add(this.tfFactor, "wrap");
    
    add(new JLabel("Dimension"));
    add(this.cbDimension, "wrap");
  }
  
  public boolean save()
  {
    try
    {
      if (!updateModel()) {
        return false;
      }
      PackagingUnitDAO.getInstance().save((PackagingUnit)getBean());
      
      return true;
    }
    catch (IllegalModelStateException e)
    {
      POSMessageDialog.showError(this, e.getMessage());
    }
    return false;
  }
  
  protected void updateView()
  {
    PackagingUnit packagingUnit = (PackagingUnit)getBean();
    if (packagingUnit == null) {
      return;
    }
    this.tfName.setText(packagingUnit.getName());
    this.tfShortName.setText(packagingUnit.getShortName());
    this.tfFactor.setText(packagingUnit.getFactor() + "");
    this.cbDimension.setSelectedItem(packagingUnit.getPackagingDimension());
  }
  
  protected boolean updateModel()
    throws IllegalModelStateException
  {
    String name = this.tfName.getText();
    String shortName = this.tfShortName.getText();
    double factor = this.tfFactor.getDouble();
    PackagingDimension dimension = (PackagingDimension)this.cbDimension.getSelectedItem();
    if (StringUtils.isEmpty(name)) {
      throw new IllegalModelStateException("Bitte geben Sie unit name");
    }
    if (PackagingUnitDAO.getInstance().nameExists(name)) {
      throw new IllegalModelStateException("Verpackungseinheit mit diesem Namen existiert bereits");
    }
    if ((Double.isNaN(factor)) || (factor <= 0.0D)) {
      throw new IllegalModelStateException("Bitte stellen Sie sicher, dass Faktor> = 1");
    }
    if (dimension == null) {
      throw new IllegalModelStateException("Bitte wählen Sie eine Dimension");
    }
    PackagingUnit packagingUnit = (PackagingUnit)getBean();
    if (packagingUnit == null)
    {
      packagingUnit = new PackagingUnit();
      setBean(packagingUnit, false);
    }
    packagingUnit.setName(name);
    packagingUnit.setShortName(shortName);
    packagingUnit.setFactor(Double.valueOf(factor));
    packagingUnit.setPackagingDimension(dimension);
    
    return true;
  }
  
  public String getDisplayText()
  {
    return "Add/Edit packaing unit";
  }
}
