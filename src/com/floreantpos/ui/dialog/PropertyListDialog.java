package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;

import com.floreantpos.model.PropertyData;
import com.floreantpos.model.dao.PropertyDataDAO;

import com.floreantpos.swing.POSTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.QwertyKeyPad;
import com.floreantpos.ui.PosTableRenderer;
import net.miginfocom.swing.MigLayout;
public class PropertyListDialog extends POSDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	JLabel lbl1;


	private JTextField tfNewProperty;

	public PosButton btnAdd;
	public PosButton btnDelete;

	public PosButton cancelButton;
	public JPanel toppanel;
	public JPanel buttonPanel;
	public JPanel centerPanel;
	public QwertyKeyPad qwerty;
	boolean cancelled;
	public JTable jTable;
	private List<PropertyData> titems;
	ItemExplorerModel model;
	JTable explorerTable;

	public void init() {
		setTitle("Auswahl Hinzufugen!!!");
		if(StringUtils.isNotEmpty(POSConstants.Auswahl_Hinzufugen))
			setTitle(POSConstants.Auswahl_Hinzufugen);
		
		setLayout(new BorderLayout());
		toppanel= new JPanel();		
		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,4));
		centerPanel.setBackground(new Color(209,222,235));	
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout());
		buttonPanel.setBackground(new Color(209,222,235));		

		btnAdd = new PosButton("HINZUFUGEN");
		if(StringUtils.isNotEmpty(POSConstants.HINZUFUGEN))
			btnAdd.setText(POSConstants.HINZUFUGEN);
		
		btnAdd.setFont(new Font("Times New Roman", Font.BOLD, 28));
		btnAdd.setBackground(new Color(102,255,102));

		cancelButton = new PosButton("ABRECHEN");
		if(StringUtils.isNotEmpty(POSConstants.ABBRECHEN))
			cancelButton.setText(POSConstants.ABBRECHEN);
		
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 28));

		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCanceled(false);
				doAdd();
				tfNewProperty.setText("");
			}

		});

		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setCancelled(true);				
				dispose();
			}
		});

		btnDelete = new PosButton("DELETE");
		btnDelete.setBackground(new Color(255,153,153));
		btnDelete.setFont(new Font("Times New Roman", Font.BOLD, 28));
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doDelet();				
			}
		});	 

	}

	public PropertyListDialog() {
		init();		
		titems = PropertyDataDAO.getInstance().findAll();
		model = new ItemExplorerModel();
		explorerTable = new JTable(model);
		explorerTable.setRowHeight(35);
		explorerTable.setDefaultRenderer(Object.class, new PosTableRenderer());
		explorerTable.getTableHeader().setBackground(new Color(209, 222, 235));		
		model.setRows(titems);
		explorerTable.repaint();
		explorerTable.revalidate();		
		JScrollPane jScrollPane = new JScrollPane(explorerTable);
		jScrollPane.getViewport().setBackground(new Color(209, 222, 235));
		jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 20));
		jScrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		add(jScrollPane, BorderLayout.CENTER);

		tfNewProperty = new POSTextField(20);   
		tfNewProperty.setFont(new Font(null, Font.PLAIN, 20));
		tfNewProperty.setPreferredSize(new Dimension(50, 40));
		tfNewProperty.requestFocus();

		buttonPanel.add(tfNewProperty);
		buttonPanel.add(btnAdd);
		buttonPanel.add(btnDelete);
		buttonPanel.add(cancelButton);
		buttonPanel.setBackground(new Color(209,222,235));
		add(buttonPanel, BorderLayout.SOUTH);		
	}

	public void doDelet() {		
		int index = explorerTable.getSelectedRow();
		if (index < 0)
			return;

		try {
			PropertyData data = titems.get(index);
			titems.remove(index);
			PropertyDataDAO.getInstance().delete(data);
			updateView();
		}catch(Exception ex) {

		}
		updateView();		
	}

	protected void updateView() {		 
		titems = PropertyDataDAO.getInstance().findAll();			
		model.setRows(titems);
		explorerTable.repaint();
		explorerTable.revalidate();
	}	

	class ItemExplorerModel extends ListTableModel {
		String[] columnNames = { "ID", "PROPERTY-NAME"};

		public int getRowCount() {
			if (titems == null) {
				return 0;
			}
			return titems.size();
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
			if(columnIndex==1)
				return true;
			return false;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			if (titems == null)
				return ""; //$NON-NLS-1$

			PropertyData ticketItem = titems.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return ticketItem.getId();
			case 1:
				return ticketItem.getPropertytext();		

			}
			return null;
		}
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			try {
				PropertyData data = titems.get(rowIndex);
				if (columnIndex == 1) {
					data.setPropertytext(value.toString());
				} 
				PropertyDataDAO.getInstance().saveOrUpdate(data);
				this.fireTableDataChanged();
				explorerTable.repaint();
			} catch (Exception e) {
			}
		}
	}

	public  void doAdd() {

		try {
			PropertyData property = new PropertyData();
			property.setPropertytext(tfNewProperty.getText());
			PropertyDataDAO.getInstance().saveOrUpdate(property);

		}catch(NumberFormatException ex) {
			POSMessageDialog.showError("Nummer muss ohne \",\" geben");
			return;
		}
		updateView();
		tfNewProperty.setText("");
	}

	public void setCancelled(boolean flag)
	{
		cancelled = flag;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}

}