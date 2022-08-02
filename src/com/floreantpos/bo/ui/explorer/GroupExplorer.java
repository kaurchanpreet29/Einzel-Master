package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Tax;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.TaxDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuGroupForm;
import com.floreantpos.ui.model.MenuItemForm;
import com.floreantpos.util.NumberUtil;

public class GroupExplorer extends TransparentPanel {
	private List<MenuGroup> groupList;
	private JTable table;
	private List<MenuGroup> tempList;
	private GroupExplorerTableModel tableModel;          
	
	
	public GroupExplorer() {
		this.setPreferredSize(new Dimension(800,480));
		MenuGroupDAO dao = new MenuGroupDAO();
		groupList = dao.findAll();

		tableModel = new GroupExplorerTableModel();
		table = new JTable(tableModel);
		table.getTableHeader().setBackground(new Color(209,222,235));
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		setLayout(new BorderLayout(5, 5));
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
		add(jScrollPane);

		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();
		JButton fitButton = explorerButton.getFitButton();
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					MenuGroup category = groupList.get(index);

					MenuGroupForm editor = new MenuGroupForm(category);
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					table.repaint();
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					while(true)
					{
						MenuGroupForm editor = new MenuGroupForm();
						BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
						dialog.open();
						if (dialog.isCanceled())
							return;
						MenuGroup foodGroup = (MenuGroup) editor.getBean();
						tableModel.addGroup(foodGroup);
					}
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int rows[] = table.getSelectedRows();
					
					if (ConfirmDeleteDialog.showMessage(GroupExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						for(int i = 0; i < rows.length; i++)
						{
							MenuGroup group = groupList.get(i);
							MenuGroupDAO foodGroupDAO = new MenuGroupDAO();
//							List<MenuItem> itemList = MenuItemDAO.getInstance().findByParent(group, false);		
							//								for(MenuItem item: itemList)
							//								{
							//									MenuItemDAO.getInstance().delete(item);
							//								}//								
							foodGroupDAO.delete(group);
						}

					}
					groupList = MenuGroupDAO.getInstance().findAll();
					Collections.sort(groupList);
					table.repaint();
					table.revalidate();
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		fitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<MenuGroup> tempList;
					MenuGroupDAO dao = new MenuGroupDAO();
					tempList = dao.findAll();
					
					for (Iterator itr=tempList.iterator();itr.hasNext();)
					{
						MenuGroupForm editor;
						MenuGroup group = (MenuGroup)itr.next();
						group.setGaeng(1);
						editor = new MenuGroupForm(group);
						editor.save();
					}
					table.repaint();
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		
		
		JButton btnMerge = new JButton("Merger");
		btnMerge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<MenuGroup> tempList;
					MenuGroupDAO dao = new MenuGroupDAO();
					tempList = dao.findAll();
					String name ="";
					MenuGroup mainGroup;
					for(MenuGroup group:tempList) {
						
						if(name.isEmpty()||name.compareTo(group.getName())!=0) {
							mainGroup = group;
						}
					}
					table.repaint();
				} catch (Exception x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});
		
		

		TransparentPanel panel = new TransparentPanel();

		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(fitButton);
		add(panel, BorderLayout.SOUTH);
	}

	class GroupExplorerTableModel extends AbstractTableModel {
		String[] columnNames = { com.floreantpos.POSConstants.ID, "Gaenge", com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.VISIBLE, com.floreantpos.POSConstants.MENU_CATEGORY, "Rabatt" };

		public int getRowCount() {
			if (groupList == null) {
				return 0;
			}
			return groupList.size();
		}

		public int getColumnCount() {
			return 6;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (groupList == null)
				return ""; //$NON-NLS-1$

			MenuGroup category = groupList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					if(category.getGroupid() == null)return 0;
					return Integer.parseInt((category.getGroupid().toString()));

				case 1:
					return category.getGaeng();
				case 2:
					return category.getName();

				case 3:
					return Boolean.valueOf(category.isVisible());
				case 4:
					return category.getParent().getName();
					
				case 5:
				  return category.getDiscount() != null ? NumberUtil.formatNumber(category.getDiscount()) : StringUtils.EMPTY;
			}
			return null;
		}

		public void addGroup(MenuGroup category) {
			int size = groupList.size();
			groupList.add(category);
			fireTableRowsInserted(size, size);

		}

		public void deleteGroup(MenuGroup category, int index) {
			groupList.remove(category);
			fireTableRowsDeleted(index, index);
		}
	}
}
