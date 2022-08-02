package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Customer;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.CustomerDAO;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.ComboBoxModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.model.MenuCategoryForm;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.ui.views.order.OrderView;
import net.miginfocom.swing.MigLayout;

public class CategoryExplorer extends TransparentPanel {
	private List<MenuCategory> categoryList;

	private JTable table;
	JComboBox<String> cbType;
	private CategoryExplorerTableModel tableModel;

	public CategoryExplorer() {
		this.setPreferredSize(new Dimension(800,480));
		MenuCategoryDAO dao = new MenuCategoryDAO();
		categoryList = dao.findAll();
		tableModel = new CategoryExplorerTableModel();
		table = new JTable(tableModel);
		table.getTableHeader().setBackground(new Color(209,222,235));
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		setLayout(new BorderLayout(5,5));
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
		add(jScrollPane);
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();

		JButton addButton = explorerButton.getAddButton();
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					while(true)
					{
						MenuCategoryForm editor = MenuCategoryForm.getmForm();
						BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
						dialog.open();
						if (dialog.isCanceled())
							return;
						MenuCategory foodCategory = (MenuCategory) editor.getBean();
						tableModel.addCategory(foodCategory);
					}
				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		JButton editButton = explorerButton.getEditButton();
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					MenuCategory category = categoryList.get(index);
					MenuCategoryForm editor = new MenuCategoryForm(category);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;

					table.repaint();
				} catch (Throwable x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		JButton deleteButton = explorerButton.getDeleteButton();
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MenuCategoryDAO dao = new MenuCategoryDAO();
					int rows[] = table.getSelectedRows();
					if (ConfirmDeleteDialog.showMessage(CategoryExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						for(int i = 0; i < rows.length; i++)
						{MenuCategory category = categoryList.get(rows[i]);
						if(Application.getCurrentUser().getFirstName().equals("Super-User")) {								
							List<MenuGroup> menuGroup = MenuGroupDAO.getInstance().findByParent(category);
							if(menuGroup!=null&&menuGroup.size()>0&&ConfirmDeleteDialog.showMessage(CategoryExplorer.this, "es gibt Menugroup, Wollen Sie Fortfahren?",
									com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
								for (MenuGroup grp : menuGroup) {
									List<MenuItem> menuList = MenuItemDAO.getInstance().findByParent(grp, true);
									for (MenuItem item : menuList) {
										MenuItemDAO.getInstance().delete(item);
									}
									MenuGroupDAO.getInstance().delete(grp);
								}
							}
							try {
								dao.delete(category);
							}catch(Exception ex) {
								
							}

						} else {
							try {
								dao.delete(category);
							}catch(Exception ex) {
								
							}
						}

						}
					}

				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
				categoryList = MenuCategoryDAO.getInstance().findAll();
				table.repaint();
				table.revalidate();		
			}

		});		

		cbType = new javax.swing.JComboBox();
		cbType.setBackground(Color.WHITE);  
		cbType.addItem(String.valueOf(1));
		if(TerminalConfig.isPriceCategoryKunden()) {
			List<Customer> CustList = CustomerDAO.getInstance().findAll();
			if(CustList!=null) {
				for (Customer cust:CustList) {
					try {
						if(cust.getLoyaltyNo()!=null)
							cbType.addItem(cust.getLoyaltyNo());
					} catch(Exception ex) {

				    }
				}	
			}
		} else if(TerminalConfig.isPriceCategory()) {			
			for (int i=2; i<=20;i++) {
				try {
					cbType.addItem(String.valueOf(i));
				}catch(Exception ex) {

				}	
			}
		}	

		POSDialog dialog = new POSDialog();
		JButton select = new JButton(" OK ");
		select.setFont(new Font(null, Font.BOLD, 20));
		select.setBackground(Color.BLUE);
		select.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setCanceled(true);
				dialog.dispose();
			}
		});
		JLabel lblSelect = new JLabel("Select Price Category");
		lblSelect.setFont(new Font(null, Font.BOLD, 20));
		lblSelect.setForeground(Color.BLUE);
		TransparentPanel selectPanel = new TransparentPanel();
		selectPanel.setLayout(new MigLayout());
		selectPanel.setPreferredSize(new Dimension(300, 100));
		selectPanel.add(lblSelect, "cell 0 0");
		selectPanel.add(cbType, "cell 1 0");
		selectPanel.add(select, "cell 2 0");
		dialog.add(selectPanel);		
		JButton duplicateButton = explorerButton.getDuplicateButton();
		duplicateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int rows[] = table.getSelectedRows();
					if(TerminalConfig.isPriceCategory()||TerminalConfig.isPriceCategoryKunden()) {
						dialog.pack();
						dialog.open();
					}					
					for (int i = 0; i < rows.length; i++) {
						MenuCategory category = categoryList.get(rows[i]);
						List<MenuGroup> groupList = MenuGroupDAO.getInstance().findByParent(category);
						if(TerminalConfig.isPriceCategory()||TerminalConfig.isPriceCategoryKunden()) {
							category.setPriceCategory(Integer.parseInt(cbType.getSelectedItem().toString()));
							category.setName(category.getName()+"_"+cbType.getSelectedItem().toString());
						}

						else
							category.setPriceCategory(1);
						MenuCategoryDAO.getInstance().save(category);
						for (Iterator<MenuGroup> itr = groupList.iterator(); itr.hasNext();) {
							MenuGroup menuGroup = itr.next();
							menuGroup.setParent(category);
							List<MenuItem> itemList = MenuItemDAO.getInstance().findByParent(menuGroup, false);
							MenuGroupDAO.getInstance().save(menuGroup);

							for (MenuItem item : itemList) {
								item = MenuItemDAO.getInstance().loadMenuItem(item.getId());
								item.setParent(menuGroup);
								if(TerminalConfig.isPriceCategory()||TerminalConfig.isPriceCategoryKunden())
									item.setPriceCategory(Integer.parseInt(cbType.getSelectedItem().toString()));
								else
									item.setPriceCategory(1);
								MenuItemDAO.getInstance().save(item);
							}

						}
					}
					tableModel.fireTableDataChanged();
					table.repaint();

				} catch (Exception x) {
					BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});	


		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(duplicateButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}



	class CategoryExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.VISIBLE, "Steuer"};

		public int getRowCount() {
			if(categoryList == null) {
				return 0;
			}
			return categoryList.size();
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(columnIndex == 1) {
				return true;
			}
			return false;
		}
		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			try
			{
				MenuCategory menuCategory = categoryList.get(rowIndex);

				if (columnIndex == 1)
				{
					menuCategory.setName(value.toString());
				}
				MenuCategoryDAO.getInstance().saveOrUpdate(menuCategory);
				this.fireTableDataChanged();
				table.repaint();
			}
			catch(Exception e){}
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (categoryList == null)
				return ""; //$NON-NLS-1$

			MenuCategory category = categoryList.get(rowIndex);




			switch (columnIndex) {
			case 0:
				return String.valueOf(category.getCategoryid());
			case 1:
				return category.getName();
			case 2:
				return Boolean.valueOf(category.isVisible());
			case 3:
				if (category.getType().compareTo(POSConstants.DINE_IN) == 0) {
					return OrderView.taxDineIn+" %";
				} else if (category.getType().compareTo(POSConstants.HOME_DELIVERY) == 0) {
					return OrderView.taxHomeDelivery+" %";
				} else {
					return "0,00 %";
				}
			}
			return null;
		}

		public void addCategory(MenuCategory category) {
			int size = categoryList.size();
			categoryList.add(category);
			fireTableRowsInserted(size, size);
		}

		public void deleteCategory(MenuCategory category, int index) {
			categoryList.remove(category);
			fireTableRowsDeleted(index, index);
		}
	}
}
