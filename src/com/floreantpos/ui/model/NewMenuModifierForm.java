package com.floreantpos.ui.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;



import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.MenuModifierGroup;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.model.dao.ModifierGroupDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.ui.BeanEditor;

public class NewMenuModifierForm extends BeanEditor{

	public NewMenuModifierForm() throws Exception {
		initComponents();
	}
	
	public void initComponents()
	{
		setLayout(new MigLayout());
		lblCopyModifierGroup = new JLabel("Kopieren Zutaten Gruppen");
		
		lblNewGroup = new JLabel("Neu Zutaten Gruppen");
		tfNewGroup = new JTextField(20);
		
		lblPrice = new JLabel("Preis");
		tfPrice = new JTextField(20);
		
		lblExtraPrice = new JLabel("Extra Preis");
		tfExtraPrice = new JTextField(20);
		
		ModifierGroupDAO modifierGroupDAO = new ModifierGroupDAO();
		List<MenuModifierGroup> groups = modifierGroupDAO.findAll();
		cbModifierGroup = new javax.swing.JComboBox();
		cbModifierGroup.setModel(new DefaultComboBoxModel(new Vector<MenuModifierGroup>(groups)));
		
		add(lblNewGroup);
		add(tfNewGroup,"wrap");
		add(lblCopyModifierGroup);
		add(cbModifierGroup,"wrap");
		add(lblPrice);
		add(tfPrice,"wrap");
		add(lblExtraPrice);
		add(tfExtraPrice,"wrap");
		
		
	}
	@Override
	public boolean save() {
		ModifierGroupDAO modifierGroupDAO = new ModifierGroupDAO();
		MenuModifierGroup menuModifierGroup = null;
		List<MenuModifierGroup> menuModifierGroupList = modifierGroupDAO.findAll();
		for(Iterator<MenuModifierGroup> itr = menuModifierGroupList.iterator(); itr.hasNext();)
		{
			MenuModifierGroup temp =  itr.next();
			if(temp.getName().compareTo(tfNewGroup.getText()) == 0)
			{
				menuModifierGroup = temp;
				break;
			}
		}
		
		if(menuModifierGroup == null)
		{
			menuModifierGroup = new MenuModifierGroup();
			menuModifierGroup.setName(tfNewGroup.getText());
			modifierGroupDAO.save(menuModifierGroup);
		}
		
		ModifierDAO modifierDao = new ModifierDAO();
		List<MenuModifier> menuModifierList = modifierDao.findAll();
		List<MenuModifier> tempModifierList = new ArrayList();
		
		for(Iterator<MenuModifier> itr = menuModifierList.iterator();itr.hasNext();)
		{
			MenuModifier menuModifier = itr.next();
			if(menuModifier.getModifierGroup().getName().compareTo(cbModifierGroup.getSelectedItem().toString()) == 0)
			{
				tempModifierList.add(menuModifier);
				
			}
		}
		
		System.out.println(tempModifierList.size());
		if(tempModifierList.size() > 0)
		{
			for(Iterator<MenuModifier> itr = tempModifierList.iterator();itr.hasNext();)
			{
				MenuModifier menuModifier = itr.next();
				MenuModifier newMenuModifier = new MenuModifier();
				String price = tfPrice.getText().replace(',', '.');
				String extraPrice = tfExtraPrice.getText().replace(',', '.');

				newMenuModifier.setExtraPrice(new Double(extraPrice));
				newMenuModifier.setPrice(new Double(price));
				newMenuModifier.setModifierGroup(menuModifierGroup);
				newMenuModifier.setTax(menuModifier.getTax());
				newMenuModifier.setShouldPrintToKitchen(menuModifier.isShouldPrintToKitchen());
				newMenuModifier.setName(menuModifier.getName());
				modifierDao.saveOrUpdate(newMenuModifier);
			}
		}	
		return true;
	}

	@Override
	protected void updateView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getDisplayText() {
		return "Kopieren Zutaten Gruppen";
	}
	private javax.swing.JLabel lblNewGroup;
	private javax.swing.JTextField tfNewGroup;
	private javax.swing.JLabel lblCopyModifierGroup;
	private javax.swing.JComboBox cbModifierGroup;
	private javax.swing.JLabel lblPrice;
	private javax.swing.JTextField tfPrice;
	private javax.swing.JLabel lblExtraPrice;
	private javax.swing.JTextField tfExtraPrice;

}
