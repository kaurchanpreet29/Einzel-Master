package com.floreantpos.ui.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXDatePicker;

import com.floreantpos.POSConstants;
import com.floreantpos.model.CouponAndDiscount;
import com.floreantpos.model.dao.CouponAndDiscountDAO;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.ui.BeanEditor;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Created by IntelliJ IDEA.
 * User: mshahriar
 * Date: Oct 5, 2006
 * Time: 1:18:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CouponForm extends BeanEditor {
    private JPanel contentPane;
    private JTextField tfCouponName;
    //private JComboBox cbCouponType;
    private JFormattedTextField tfCouponValue;
    private JCheckBox chkDisabled;
    private JCheckBox chkFreiWillig;
    private JCheckBox chkNeverExpire;
    private JXDatePicker dpExperation;

    public CouponForm() {
        this(new CouponAndDiscount());
    }

    public CouponForm(CouponAndDiscount coupon) {
        this.setLayout(new BorderLayout());
        add(contentPane);

        tfCouponName.setDocument(new FixedLengthDocument(30));
        //cbCouponType.setModel(new DefaultComboBoxModel(CouponAndDiscount.COUPON_TYPE_NAMES));

        setBean(coupon);
    }

    @Override
    public boolean save() {
        try {
            if (!updateModel()) return false;

            CouponAndDiscount coupon = (CouponAndDiscount) getBean();
            CouponAndDiscountDAO dao = new CouponAndDiscountDAO();
            dao.saveOrUpdate(coupon);
            List<CouponAndDiscount> couponList = CouponAndDiscountDAO.getInstance().findAll();
            for(Iterator<CouponAndDiscount> itr = couponList.iterator();itr.hasNext();)
            {
            	CouponAndDiscount c = itr.next();
            	System.out.println(c.getName());
            	System.out.println(c.getExpiryDate());
            	System.out.println(c.getValue());
            	System.out.println(c.getId());
            	
            }
            
        } catch (Exception e) {
            MessageDialog.showError(com.floreantpos.POSConstants.SAVE_ERROR, e);
            return false;
        }
        return true;
    }

    @Override
    protected void updateView() {
        CouponAndDiscount coupon = (CouponAndDiscount) getBean();
        if (coupon == null) return;

        tfCouponName.setText(coupon.getName());
        tfCouponValue.setValue(Double.valueOf(coupon.getValue()));
        //cbCouponType.setSelectedIndex(coupon.getType());
        dpExperation.setDate(coupon.getExpiryDate());
        chkDisabled.setSelected(coupon.isDisabled());
        chkNeverExpire.setSelected(coupon.isNeverExpire());
        try {
        	 if(coupon.getName().toLowerCase().contains("frei"))
             	chkFreiWillig.setSelected(true);
        }catch(Exception ex) {
        	
        }
       
    }

    @Override
    protected boolean updateModel() {
        String name = tfCouponName.getText();
        double couponValue = 0;
        couponValue = ((Double) tfCouponValue.getValue()).doubleValue();
        int couponType = CouponAndDiscount.PERCENTAGE_PER_ITEM;
        Date expiryDate = dpExperation.getDate();
        boolean disabled = chkDisabled.isSelected();
        boolean neverExpire = chkNeverExpire.isSelected();
        boolean freiwillig = chkFreiWillig.isSelected();
        
        if (name == null || name.trim().equals("")) {
            MessageDialog.showError("Name cannot be empty");
            return false;
        }
        if (!freiwillig && couponValue <= 0) {
            MessageDialog.showError("Value must be greater than 0");
            return false;
        }

        CouponAndDiscount coupon = (CouponAndDiscount) getBean();
        coupon.setName(name);
        coupon.setValue(couponValue);
        coupon.setExpiryDate(expiryDate);
        coupon.setType(couponType);
        coupon.setDisabled(disabled);
        coupon.setNeverExpire(neverExpire);

        return true;
    }

    @Override
    public String getDisplayText() {
        CouponAndDiscount coupon = (CouponAndDiscount) getBean();
        if (coupon.getId() == null) {
        	if(StringUtils.isNotEmpty(POSConstants.Hinzufügen_Gutschein))
    			return POSConstants.Hinzufügen_Gutschein;
        	else
                return "Hinzufügen von Gutschein/Rabatt";
        }
        if(StringUtils.isNotEmpty(POSConstants.Gutschein_Bearbeiten))
			return POSConstants.Gutschein_Bearbeiten;
        else
           return "Gutschein/Rabatt Bearbeiten";
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:100px:grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        contentPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JLabel label1 = new JLabel();
        label1.setText("Gutscheinname" + ":");
        if(StringUtils.isNotEmpty(POSConstants.Gutscheinname))
        	label1.setText(POSConstants.Gutscheinname + ":");
        
        CellConstraints cc = new CellConstraints();
        contentPane.add(label1, cc.xy(1, 1));
        final JLabel label2 = new JLabel();
        label2.setText("Ablaufdatum" + ":");
        if(StringUtils.isNotEmpty(POSConstants.Ablaufdatum))
        	label2.setText(POSConstants.Ablaufdatum + ":");
        
        contentPane.add(label2, cc.xy(1, 5));
        tfCouponName = new JTextField();
        contentPane.add(tfCouponName, cc.xyw(3, 1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        dpExperation = new JXDatePicker();
        contentPane.add(dpExperation, cc.xy(3, 5));
        final JLabel label3 = new JLabel();
        label3.setText("Gutscheintyp" + ":");
        if(StringUtils.isNotEmpty(POSConstants.Gutscheintyp))
        	label3.setText(POSConstants.Gutscheintyp + ":");
        
        	
        contentPane.add(label3, cc.xy(1, 3));
        /*cbCouponType = new JComboBox();
        cbCouponType.setBackground(Color.WHITE);
        contentPane.add(cbCouponType, cc.xy(3, 3));*/
        
        if(StringUtils.isNotEmpty(POSConstants.Prozente_Artikel))        	
        contentPane.add(new JLabel(POSConstants.Prozente_Artikel), cc.xy(3, 3));
        else {
        	contentPane.add(new JLabel("Prozente auf einen Artikel"), cc.xy(3, 3));
        }
        
        final JLabel label4 = new JLabel();
        label4.setText("Gutscheinwert(%)" + ":");
        if(StringUtils.isNotEmpty(POSConstants.Gutscheinwert))
        	label4.setText(POSConstants.Gutscheinwert);
        
        contentPane.add(label4, cc.xy(1, 7));
        tfCouponValue = new JFormattedTextField();
        contentPane.add(tfCouponValue, cc.xy(3, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        chkDisabled = new JCheckBox();
        chkDisabled.setBackground(new Color(209,222,235));
        chkDisabled.setText("ungueltig");
        if(StringUtils.isNotEmpty(POSConstants.ungueltig))
        	chkDisabled.setText(POSConstants.ungueltig);
        
        contentPane.add(chkDisabled, cc.xy(3, 9));
        chkNeverExpire = new JCheckBox();
        chkNeverExpire.setBackground(new Color(209,222,235));
        chkNeverExpire.setText("Laeuft nie aus");
        if(StringUtils.isNotEmpty(POSConstants.Laeuft_nie_aus))
        	chkNeverExpire.setText(POSConstants.Laeuft_nie_aus);
        
        chkFreiWillig = new JCheckBox();
        chkFreiWillig.setBackground(new Color(209,222,235));
        chkFreiWillig.setText("Freiwilliges");
        if(StringUtils.isNotEmpty(POSConstants.Freiwilliges))
        	chkFreiWillig.setText(POSConstants.Freiwilliges);
        
        chkFreiWillig.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(chkFreiWillig.isSelected()) {
						tfCouponName.setText("Freiwilliges");
						if(StringUtils.isNotEmpty(POSConstants.Freiwilliges))
				        	chkFreiWillig.setText(POSConstants.Freiwilliges);
						
						chkNeverExpire.setSelected(true);
					}else {
						tfCouponName.setText("");
						chkNeverExpire.setSelected(false);
					}
						
				}catch(Exception ex) {
					
				}
				
			}
		});
        contentPane.add(chkNeverExpire, cc.xy(3, 11));
        contentPane.add(chkFreiWillig, cc.xy(1, 9));
        contentPane.setBackground(new Color(209,222,235));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
