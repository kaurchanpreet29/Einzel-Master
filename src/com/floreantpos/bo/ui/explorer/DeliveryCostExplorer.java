package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.main.Application;
import com.floreantpos.model.Gebiet;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.DeliveryCostDAO;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.dialog.DeliveryCostDialog;
import com.floreantpos.util.NumberUtil;

public class DeliveryCostExplorer extends TransparentPanel {
	List<Gebiet> deliveryCostList;

	private JTable table;
	private DeliveryCostExplorerTableModel tableModel;
	JButton saveButton;
	JButton addButton;
	JButton deleteButton;
	
	DefaultTableCellRenderer otherRenderer = new DefaultTableCellRenderer() {
	    Font font = new Font("Times New Roman", Font.BOLD, 16);

	    @Override
	    public Component getTableCellRendererComponent(JTable table,
	            Object value, boolean isSelected, boolean hasFocus,
	            int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
	                row, column);
	        setFont(font);
	        this.setHorizontalAlignment(SwingConstants.CENTER);
	        return this;
	    }

	};
	public DeliveryCostExplorer()
	{
		this.setPreferredSize(new Dimension(800,480));
		tableModel = new DeliveryCostExplorerTableModel();
		deliveryCostList = DeliveryCostDAO.getInstance().findAll();
		if(deliveryCostList == null)
		{
			deliveryCostList = new ArrayList();
		}
		tableModel.setRows(deliveryCostList);
		
		table = new JTable(tableModel);
	
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		
		table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(leftRenderer);
		
		table.setBorder(null);
		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
		addButton = explorerButton.getAddButton();
		addButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DeliveryCostDialog dialog = new DeliveryCostDialog(getNextId());
				dialog.setSize(800, 600);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
				if(dialog.getGebiet() != null)
				{
					deliveryCostList.add(dialog.getGebiet());
					DeliveryCostDAO.getInstance().save(dialog.getGebiet());
					table.revalidate();
					table.repaint();
				}
			}
			
		});
		deleteButton = explorerButton.getDeleteButton();
		deleteButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				Gebiet gebiet = deliveryCostList.get(index);
				tableModel.deleteItem(index);
				DeliveryCostDAO.getInstance().delete(gebiet);
				
		    }
		});
		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(deleteButton);
		
		
		setLayout(new BorderLayout(5, 5));
		JScrollPane jScrollPane = new JScrollPane(table);
		jScrollPane.getViewport().setBackground(new Color(209,222,235));
		jScrollPane.setBorder(null);
		setBackground(new Color(209,222,235));
		table.getTableHeader().setBackground(new Color(209,222,235));
		add(jScrollPane,BorderLayout.NORTH);
		add(panel, BorderLayout.SOUTH);
	
	}
	
	public int getNextId()
	{
		int index = 0;
		if(deliveryCostList != null && deliveryCostList.size() > 0)
		{
			for(Iterator<Gebiet> itr = deliveryCostList.iterator();itr.hasNext();)
			{
				Gebiet gebiet = itr.next();
				index = gebiet.getId();
			}
		}
		return index+1;
	}
	class DeliveryCostExplorerTableModel extends ListTableModel {
		String[] columnNames = {"Id","Open", "Plz", "Bezirk", "Ort", "Mindestbestellwert", "Lieferkosten", "lieferzeit" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		DeliveryCostExplorerTableModel(){
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
			Gebiet gebiet = deliveryCostList.get(rowIndex);
			
			if (columnIndex == 0)
			{
				gebiet.setId(Integer.valueOf(value.toString()));
			}
			else if (columnIndex == 1)
			{
				gebiet.setIsopen(Boolean.valueOf(value.toString()));
			}
			else if (columnIndex == 2)
			{
				gebiet.setPlz(Integer.valueOf((String) value));
			}
			else if (columnIndex == 3)
			{
				gebiet.setBezirk(value.toString());
			}
			else if (columnIndex == 4)
			{
				gebiet.setOrt(value.toString());
			}
			else if (columnIndex == 5)
			{
				String data = value.toString().replace(',', '.');
				gebiet.setMindest(Double.valueOf(data));
			}
			else if (columnIndex == 6)
			{
				String data = value.toString().replace(',', '.');
				gebiet.setLieferkosten(Double.valueOf(data));
			}
			else if (columnIndex == 7)
			{
				gebiet.setLieferzeit(Integer.valueOf(value.toString()));
			}
			DeliveryCostDAO.getInstance().saveOrUpdate(gebiet);
			fireTableDataChanged();
			table.repaint();
			}
			catch(Exception e){}
		}
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Gebiet gebiet  = (Gebiet) rows.get(rowIndex);
		
			switch (columnIndex) {
			case 0:
				return gebiet.getId();
			case 1:
				return gebiet.getIsopen();
			case 2:
				return gebiet.getPlz();
			case 3:
				return gebiet.getBezirk();
			case 4:
				return gebiet.getOrt();
			case 5:
				return NumberUtil.formatNumber(gebiet.getMindest());
			case 6:
				return NumberUtil.formatNumber(gebiet.getLieferkosten());
			case 7:
				return gebiet.getLieferzeit();
			}
			return null;
		}
		
	}
}