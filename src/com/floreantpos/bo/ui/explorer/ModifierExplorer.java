package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.dao.ModifierDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.model.MenuModifierForm;
import com.floreantpos.ui.model.NewMenuModifierForm;

public class ModifierExplorer extends TransparentPanel {
	private List<MenuModifier> modifierList;
	ModifierExplorerTableModel tableModel;
	private String currencySymbol;
	private JTable table;

	public ModifierExplorer() {
		currencySymbol = Application.getCurrencySymbol();
		this.setPreferredSize(new Dimension(800,480));
		ModifierDAO dao = new ModifierDAO();
		modifierList = dao.findAll();

		tableModel = new ModifierExplorerTableModel();
		table = new JTable(tableModel);
		table.getTableHeader().setBackground(new Color(209,222,235));
		table.setDefaultRenderer(Object.class, new PosTableRenderer());
		//table.packAll();
		
		setLayout(new BorderLayout(5, 5));
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
		add(jScrollPane);

		TransparentPanel panel = new TransparentPanel();
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		JButton editButton = explorerButton.getEditButton();
		JButton addButton = explorerButton.getAddButton();
		JButton deleteButton = explorerButton.getDeleteButton();
		JButton copyButton = explorerButton.getCopyButton();

		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					MenuModifier modifier = modifierList.get(index);

					MenuModifierForm editor = new MenuModifierForm(modifier);
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

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					
					while(true)
					{
						MenuModifierForm editor = new MenuModifierForm();
						BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
						dialog.open();
						if (dialog.isCanceled())
							return;
						MenuModifier modifier = (MenuModifier) editor.getBean();
						tableModel.addModifier(modifier);
					}
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}
			}

		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;
					if (ConfirmDeleteDialog.showMessage(ModifierExplorer.this, com.floreantpos.POSConstants.CONFIRM_DELETE, com.floreantpos.POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
						MenuModifier category = modifierList.get(index);
						ModifierDAO modifierDAO = new ModifierDAO();
						modifierDAO.delete(category);
						tableModel.deleteModifier(category, index);
					}
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}

			}

		});

		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					NewMenuModifierForm editor = new NewMenuModifierForm();
					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
					dialog.open();
					if (dialog.isCanceled())
						return;
					
				} catch (Throwable x) {
				BOMessageDialog.showError(com.floreantpos.POSConstants.ERROR_MESSAGE, x);
				}

			}

		});

		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(copyButton);
		add(panel, BorderLayout.SOUTH);
	}

	class ModifierExplorerTableModel extends AbstractTableModel {
		String[] columnNames = {com.floreantpos.POSConstants.ID, com.floreantpos.POSConstants.NAME, com.floreantpos.POSConstants.PRICE + " (" + currencySymbol + ")", com.floreantpos.POSConstants.EXTRA_PRICE, com.floreantpos.POSConstants.TAX + "(%)", com.floreantpos.POSConstants.MODIFIER_GROUP }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		public int getRowCount() {
			if (modifierList == null) {
				return 0;
			}
			return modifierList.size();
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
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (modifierList == null)
				return ""; //$NON-NLS-1$

			MenuModifier modifier = modifierList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return String.valueOf(modifier.getId());

				case 1:
					return String.valueOf(modifier.getName());

				case 2:
					return Double.valueOf(modifier.getPrice());
					
				case 3:
					return Double.valueOf(modifier.getExtraPrice());
					
				case 4:
					if(modifier.getTax() == null) {
						return ""; //$NON-NLS-1$
					}
					return Double.valueOf(modifier.getTax().getRate());
					
				case 5:
					if(modifier.getModifierGroup() == null) {
						return ""; //$NON-NLS-1$
					}
					return modifier.getModifierGroup().getName();
			}
			return null;
		}

		public void addModifier(MenuModifier category) {
			int size = modifierList.size();
			modifierList.add(category);
			fireTableRowsInserted(size, size);

		}

		public void deleteModifier(MenuModifier category, int index) {
			modifierList.remove(category);
			fireTableRowsDeleted(index, index);
		}
	}
}
