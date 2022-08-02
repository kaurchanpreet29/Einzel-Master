package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.model.CookingInstruction;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.ui.BeanEditor;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class CookingInstructionSelectionView extends BeanEditor {
	private JTable table;
	
	private List<TicketItemCookingInstruction> ticketItemCookingInstructions;
	
	private JPanel textPanel;
	private JPanel qwertyPanel;
	private JPanel westPanel;
	JTextArea taMsg;
	public CookingInstructionSelectionView() {
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout());

		westPanel = new JPanel();
		westPanel.setBackground(new Color(209,222,235));
		table = new JTable();
		List<CookingInstruction> cookingInstructions = (List<CookingInstruction>) getBean();
		table.setModel(new CookingInstructionTableModel(cookingInstructions));
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.setRowHeight(35);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30,20));
		scrollPane.getVerticalScrollBar().setBackground(Color.WHITE);
		westPanel.add(scrollPane);


		add(westPanel,BorderLayout.WEST);
		table.getTableHeader().setBackground(new Color(209,222,235));
		scrollPane.getViewport().setBackground(new Color(209,222,235));
		
		textPanel = new JPanel();
		textPanel.setBackground(new Color(209,222,235));
		taMsg = new JTextArea(9,25);
		taMsg.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		taMsg.setLineWrap(true);
	   	textPanel.add(taMsg);
		
	   add(textPanel,BorderLayout.CENTER);
	   	
		qwertyPanel = new JPanel();
		qwertyPanel.setBackground(new Color(209,222,235));
		com.floreantpos.swing.QwertyKeyPad qwertyKeyPad = new com.floreantpos.swing.QwertyKeyPad();
		qwertyPanel.add(qwertyKeyPad);
		
		setBackground(new Color(128,128,128));
		add(qwertyPanel,BorderLayout.SOUTH);
		//setBorder(new EmptyBorder(10, 10, 10, 10));
	}

	@Override
	public boolean save() {
		int[] selectedRows = table.getSelectedRows();
		
		
		if(taMsg.getText().length() > 0)
		{
			if(ticketItemCookingInstructions == null) {
				ticketItemCookingInstructions = new ArrayList<TicketItemCookingInstruction>(selectedRows.length);
			}
			TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
			cookingInstruction.setDescription(taMsg.getText());
			ticketItemCookingInstructions.add(cookingInstruction);
			return true;
		}
		if(selectedRows.length == 0) {
			POSMessageDialog.showError("No cooking instruction selected");
			return false;
		}
		
		if(ticketItemCookingInstructions == null) {
			ticketItemCookingInstructions = new ArrayList<TicketItemCookingInstruction>(selectedRows.length);
		}
		
		CookingInstructionTableModel model = (CookingInstructionTableModel) table.getModel();
		for (int i = 0; i < selectedRows.length; i++) {
			CookingInstruction ci = model.rowsList.get(selectedRows[i]);
			TicketItemCookingInstruction cookingInstruction = new TicketItemCookingInstruction();
			cookingInstruction.setDescription(ci.getDescription());
			ticketItemCookingInstructions.add(cookingInstruction);
		}
		
		return true;
	}

	@Override
	protected void updateView() {
		List<CookingInstruction> cookingInstructions = (List<CookingInstruction>) getBean();
		table.setModel(new CookingInstructionTableModel(cookingInstructions));
		table.repaint();
		table.revalidate();
		repaint();
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		return true;
	}

	@Override
	public String getDisplayText() {
		return "Select cooking instructions";
	}
	
	public List<TicketItemCookingInstruction> getTicketItemCookingInstructions() {
		return ticketItemCookingInstructions;
	}

	class CookingInstructionTableModel extends AbstractTableModel {
		private final String[] columns = { "COOKING INSTRUCTIONS" };

		private List<CookingInstruction> rowsList;

		public CookingInstructionTableModel() {
		}

		public CookingInstructionTableModel(List<CookingInstruction> rows) {
			this.rowsList = rows;
		}

		@Override
		public int getRowCount() {
			if (rowsList == null) {
				return 0;
			}

			return rowsList.size();
		}

		@Override
		public int getColumnCount() {
			return columns.length;
		}

		@Override
		public String getColumnName(int column) {
			return columns[column];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowsList == null) {
				return null;
			}

			CookingInstruction row = rowsList.get(rowIndex);

			switch (columnIndex) {
				case 0:
					return row.getDescription();
			}
			return null;
		}
	}
}
