package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.bo.ui.explorer.MenuItemExplorer.MenuItemExplorerTableModel;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Street;
import com.floreantpos.model.dao.StreetDAO;
import com.floreantpos.swing.TransparentPanel;


public class StreetExplorer extends TransparentPanel{
	
	private JTable table;
	private StreetExplorerTableModel tableModel;
	private List<Street> streetList;
	JButton addButton;
	JButton deleteButton;
	
	public StreetExplorer()
	{
		initComponents();
	}
	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
	    Font font = new Font("Times New Roman", Font.PLAIN, 16);

	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
	                row, column);
	        setFont(font);
	        this.setHorizontalAlignment(SwingConstants.LEFT);
	        return this;
	    }

	};
	public void initComponents()
	{
		setLayout(new BorderLayout());
		streetList = StreetDAO.getInstance().findAll();
		if(streetList == null)
			streetList = new ArrayList();
		tableModel = new StreetExplorerTableModel();
		
		tableModel.setRows(streetList);
		
		table = new JTable(tableModel);
		table.getTableHeader().setBackground(new Color(209,222,235));
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		
		table.getColumnModel().getColumn(0).setCellRenderer(otherRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(300);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(250);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		final JScrollPane jScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane, BorderLayout.CENTER);
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		
		addButton = explorerButton.getAddButton();
		addButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
			Street street = null;
			if(streetList.size() == 0){
				street = new Street();
				street.setId(1);
				street.setName("Muster Str.");
				street.setOrt("Frankfurt am Main");
				street.setPlz("65934");
				street.setBezirk("Hbf");
				streetList.add(0,street);
				StreetDAO.getInstance().save(street);
			}
			else
			{
				street = new Street();
				streetList.add(0,street);
				
			}
			table.repaint();
			JScrollBar vertical = jScrollPane.getVerticalScrollBar();
			vertical.setValue( vertical.getMinimum() );
			}
		});
		deleteButton = explorerButton.getDeleteButton();
		deleteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				Street street = streetList.get(index);  
				tableModel.deleteItem(index);
				StreetDAO.getInstance().delete(street);
		    }
		});
		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(deleteButton);
		
		add(panel, BorderLayout.SOUTH);
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
	}
	class StreetExplorerTableModel extends ListTableModel {
		String[] columnNames = {"Name","Bezirk","Ort", "Plz"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		StreetExplorerTableModel(){
			setColumnNames(columnNames);
			
			
		}
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		public void setValueAt(Object value, int rowIndex, int columnIndex)
		{
			try
			{
			Street street = streetList.get(rowIndex);
			
			if (columnIndex == 0)
			{
				street.setName(value.toString());
			}
			else if (columnIndex == 1)
			{
				street.setBezirk(value.toString());
			}
			else if (columnIndex == 2)
			{
				street.setOrt(value.toString());				
			}
			else if (columnIndex == 3)
			{
				street.setPlz(value.toString());	
			}
			StreetDAO.getInstance().saveOrUpdate(street);
			this.fireTableDataChanged();
			table.repaint();
			}
			catch(Exception e){}
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			Street street = (Street) rows.get(rowIndex);
			
			switch (columnIndex) {
				case 0:
						return street.getName();
				case 1:
						return street.getBezirk();
				case 2:
						return street.getOrt();
				
				case 3:
						return street.getPlz();
			}
			return null;
		}
	}
}
