package com.floreantpos.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.MenuItemModifierGroupDAO;
import com.floreantpos.model.dao.MenuModifierGroupDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.ui.BeanEditor;

public class ZutatenForm  extends BeanEditor{

	public ZutatenForm(int selectedRows[],MenuItem menuItem)
	{
		this.selectedRows = selectedRows;
		this.menuItem = menuItem;
		initComponents();
		
	}
	
	public void initComponents()
	{
		setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setLayout(new MigLayout());
		setBackground(new Color(209,222,235));
		panel.setBackground(new Color(209,222,235));
		selectedMenuGroupList = new ArrayList();
		List<MenuModifierGroup> modifierList = MenuModifierGroupDAO.getInstance().findAll();
		groupList = new ArrayList();
		for(Iterator<MenuModifierGroup> itr = modifierList.iterator();itr.hasNext();)
		{
			MenuModifierGroup modifierGroup = itr.next();
			
			final JCheckBox cb = new JCheckBox();
			cb.setText(modifierGroup.getName());
			cb.setBackground(new Color(209,222,235));
			cb.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(cb.isSelected())
					{
						groupList.add(cb.getText());
					}
					else
					{
						groupList.remove(cb.getText());
					}
				}
				
			});
			panel.add(cb);
			
		}
		add(panel);
	}
	@Override
	public boolean save() {
		Transaction transaction = null;
		GenericDAO dao = new GenericDAO();
		Session s = dao.createNewSession();
		transaction = s.beginTransaction();
		try{
		
		for(Iterator<String> itr = groupList.iterator();itr.hasNext();)
		{
			MenuModifierGroup menuModifierGroup = getMenuModifierGroup(itr.next());
			if(menuModifierGroup != null)
			{
				MenuItemModifierGroup group = new MenuItemModifierGroup();
				group.setModifierGroup(menuModifierGroup);
				group.setMaxQuantity(0);
				group.setMinQuantity(0);
				selectedMenuGroupList.add(group);
				System.out.println("Size: " + MenuItemModifierGroupDAO.getInstance().findAll().size());
				MenuItemModifierGroupDAO.getInstance().saveOrUpdate(group);
			}
		}
		
		System.out.println("Selected Rows");
		
		
		
		menuItem.setMenuItemModiferGroups(selectedMenuGroupList);
		System.out.println("Saving data: " + selectedMenuGroupList.size());
		MenuItemDAO.getInstance().saveOrUpdate(menuItem);
		
	    //}
		transaction.commit();
		dao.closeSession(s);
		}catch(Exception e){
			if (transaction != null)
				transaction.rollback();
		}
		
		return true;
	}

	
	public List<String> getSelectedList()
	{
		return groupList;
	}
	@Override
	protected void updateView() {
		// TODO Auto-generated method stub
		
	}

	public MenuModifierGroup getMenuModifierGroup(String name)
	{
		List<MenuModifierGroup> modifierList = MenuModifierGroupDAO.getInstance().findAll();
		for(Iterator<MenuModifierGroup> itr = modifierList.iterator();itr.hasNext();)
		{
			MenuModifierGroup modifierGroup = itr.next();
			if(modifierGroup.getName().compareTo(name) == 0)
				return modifierGroup;
			
		}
		return null;
	}
	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		
		return true;
	}

	@Override
	public String getDisplayText() {
		return "Waehlen Sie die Extra Gruppe";
	}
	private JPanel panel;
	List<String> groupList;
	
	List<MenuItemModifierGroup> selectedMenuGroupList;
	MenuItem menuItem;
	int selectedRows[];
}
