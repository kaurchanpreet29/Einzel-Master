package com.floreantpos.ui.ticket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.util.NumberUtil;

public class TicketViewerTable extends JTable {
	
	private TicketViewerTableModel model;
	private DefaultListSelectionModel selectionModel;
	private TicketViewerTableCellRenderer cellRenderer;
	DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();	
	public TicketViewerTable() {
		this(null);
	}
   
	public TicketViewerTable(Ticket ticket) {
		model = new TicketViewerTableModel(this);
		setModel(model);		
		selectionModel = new DefaultListSelectionModel();
		selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellRenderer = new TicketViewerTableCellRenderer();
		leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		leftRenderer.setBackground(Color.BLACK);
		leftRenderer.setForeground(Color.WHITE);
		leftRenderer.setFont(new Font("Tahoma", Font.BOLD, 14));
		setSelectionModel(selectionModel);
		setAutoscrolls(true);
		setShowGrid(true);
		
		setBorder(null);
		setBorder(BorderFactory.createLineBorder(Color.WHITE));
		this.setBackground(Color.WHITE);
		resizeTableColumns();
		setTicket(ticket);	
	}
	
	
	public TicketItem getSelectedTicketItem() {
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			return (TicketItem) object;
		}
		return null;
	}
	
	public void setRabatPerItem(TicketItem ticketItem, int Value) {
		model.addRabat(getSelectedRow(), Value);
		repaint();
	}

 	public void updateTicketItem(String name, int count, Double unitPrice) {
 		model.updateTicketItem(getSelectedRow(), name, count, unitPrice);
 	}
 	
 	public void updateItems(int priceCategory) {
 		model.updateItems(priceCategory);
 	}
	
	private void resizeTableColumns() {
		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);		
		if(TerminalConfig.isCustomNumberDisplay()) {
			int Widht = 450;
        	try {
        		Widht = Integer.parseInt(TerminalConfig.getNumberDisplayWidth());
        	}catch(Exception ex) {
       	}
        	
        	setColumnWidth(0, (Widht/100)*18);
    		setColumnWidth(1, (Widht/100)*60);
    		setColumnWidth(2, (Widht/100)*22);
    		leftRenderer.setFont(new Font("Tahoma", Font.BOLD, 20));
        }else {
        	setColumnWidth(0, 60);
    		setColumnWidth(1, 210);
    		setColumnWidth(2, 70);
        }
		
	}
	
	public void resizeTableColumnsForSecondScreen() {
		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
		setColumnWidth(0, 140);
		setColumnWidth(1, 470);
		setColumnWidth(2, 180);
	}
	
	private void setColumnWidth(int columnNumber, int width) {
		TableColumn column = getColumnModel().getColumn(columnNumber);

		column.setPreferredWidth(width);
		column.setMaxWidth(width);
		column.setMinWidth(width);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if(column == 3)
			return leftRenderer;
		else 
			return cellRenderer;
	}
	
	public TicketViewerTableCellRenderer getRenderer() {
		return cellRenderer;
	}

	private boolean isTicketNull() {
		Ticket ticket = getTicket();
		if (ticket == null) {
			return true;
		}
		if (ticket.getTicketItems() == null) {
			return true;
		}
		return false;
	}

	public void scrollUp() {
		if (isTicketNull())
			return;

		int selectedRow = getSelectedRow();
		int rowCount = model.getItemCount();

		if (selectedRow <= 0) {
			selectedRow = rowCount - 1;
		}
		else if (selectedRow > (rowCount - 1)) {
			selectedRow = rowCount - 1;
		}
		else {
			--selectedRow;
		}

		selectionModel.addSelectionInterval(selectedRow, selectedRow);
		Rectangle cellRect = getCellRect(selectedRow, 0, false);
		scrollRectToVisible(cellRect);
	}

	public void scrollAbove()
	{
		Rectangle cellRect = getCellRect(0, 0, false);
		scrollRectToVisible(cellRect);
	}
	public void scrollDown() {
		if (isTicketNull())
			return;

		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			selectedRow = 0;
		}
		else if (selectedRow >= model.getItemCount() - 1) {
			selectedRow = 0;
		}
		else {
			++selectedRow;
		}

		selectionModel.addSelectionInterval(selectedRow, selectedRow);
		Rectangle cellRect = getCellRect(selectedRow, 0, false);
		scrollRectToVisible(cellRect);
	}

	public void increaseItemAmount(TicketItem ticketItem) {
		int itemCount = ticketItem.getItemCount();
		ticketItem.setItemCount(++itemCount);
		repaint();
	}
	public void setPrintOrder(int selectedRow, int order) {
		if (selectedRow < 0) {
			return;
		}
		else if (selectedRow >= model.getItemCount()) {
			return;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			ticketItem.setPrintorder(order);
			
			repaint();
		}
	}
	
	public boolean increaseAmountOnce()
	{
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			if(ticketItem.isPrintedToKitchen())return false;
			int itemCount = ticketItem.getItemCount();
			ticketItem.setItemCount(itemCount+1);
			repaint();

			return true;
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			if(modifier.isPrintedToKitchen())return false;
			int itemCount = modifier.getItemCount();
			modifier.setItemCount(itemCount+1);
			repaint();
			
			return true;
		}
		return false;

	}
	public boolean increaseItemAmount() {
		//int count = NumberSelectionDialog2.takeIntInput("Enter Number of times");
		int count = 1;
		if (count == 0)
			return true;
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int itemCount = ticketItem.getItemCount();
			ticketItem.setItemCount(itemCount+count);
			repaint();

			return true;
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			int itemCount = modifier.getItemCount();
			modifier.setItemCount(itemCount+count);
			repaint();
			
			return true;
		}
		return false;
	}
	public boolean decreaseAmountOnce()
	{
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			if(ticketItem.isPrintedToKitchen())return false;
			int itemCount = ticketItem.getItemCount();
			if (itemCount == 1)
				return false;
			if ((itemCount- 1) <= 0)
				return false;
			
			ticketItem.setItemCount(itemCount-1);
			repaint();

			return true;
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			if(modifier.isPrintedToKitchen())return false;
			int itemCount = modifier.getItemCount();
			if (itemCount == 1)
				return false;

			modifier.setItemCount(--itemCount);
			repaint();
			
			return true;
		}
		return false;

	}
	public boolean decreaseItemAmount() {
		int count = NumberSelectionDialog2.takeIntInput("Enter Number of times");
		if (count == 0)
			return true;
		int selectedRow = getSelectedRow();
		if (selectedRow < 0) {
			return false;
		}
		else if (selectedRow >= model.getItemCount()) {
			return false;
		}

		Object object = model.get(selectedRow);
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int itemCount = ticketItem.getItemCount();
			if (itemCount == 1)
				return false;
			if ((itemCount- count) <= 0)
				return false;
			
			ticketItem.setItemCount(itemCount-count);
			repaint();

			return true;
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier modifier = (TicketItemModifier) object;
			int itemCount = modifier.getItemCount();
			if (itemCount == 1)
				return false;

			modifier.setItemCount(--itemCount);
			repaint();
			
			return true;
		}
		return false;
	}

	public void setTicket(Ticket ticket) {
		model.setTicket(ticket);
	}

	public Ticket getTicket() {
		return model.getTicket();
	}

	public void addTicketItem(TicketItem ticketItem) {
		ticketItem.setTicket(getTicket());
		int addTicketItem = model.addTicketItem(ticketItem);
		
		int actualRowCount = addTicketItem;//getActualRowCount() - 1;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}
	
	public void addTicketItemOnly(TicketItem ticketItem) {
		ticketItem.setTicket(getTicket());
		int addTicketItem = model.addTicketItem(ticketItem);
		
		int actualRowCount = addTicketItem;//getActualRowCount() - 1;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}

	public Object deleteSelectedItem() {
		int selectedRow = getSelectedRow();
		return model.delete(selectedRow);
	}

	public boolean containsTicketItem(TicketItem ticketItem) {
		return model.containsTicketItem(ticketItem);
	}

	public void delete(int index) {
		model.delete(index);
	}

	public Object get(int index) {
		return model.get(index);
	}
	
	public Object getSelected() {
		int index = getSelectedRow();
		
		return model.get(index);
	}

	public void addAllTicketItem(TicketItem ticketItem) {
		model.addAllTicketItem(ticketItem);
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifier) {
		model.removeModifier(parent, modifier);
	}
	
	public void updateView() {
		int selectedRow = getSelectedRow();
		
		model.update();
		
		try {
			getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
		}catch (Exception e) {
			// do nothing
		}
	}
	
	public int getActualRowCount() {
		return model.getActualRowCount();
	}
	
	public void selectLast() {
		int actualRowCount = getActualRowCount() - 1;
		selectionModel.addSelectionInterval(actualRowCount, actualRowCount);
		Rectangle cellRect = getCellRect(actualRowCount, 0, false);
		scrollRectToVisible(cellRect);
	}
	
	public void selectRow(int index) {
		if(index < 0 || index >= getActualRowCount()) {
			index = 0;
		}
		selectionModel.addSelectionInterval(index, index);
		Rectangle cellRect = getCellRect(index, 0, false);
		scrollRectToVisible(cellRect);
	}

	public List<TicketItem> getAllTicketItems() {
		List<TicketItem> ticketItems = new ArrayList<>();
		for (int index = 0; index < getActualRowCount(); index++) {	
			Object object = model.get(index);
			if (object instanceof TicketItem) {
				ticketItems.add((TicketItem) object);
			}
		}
		return ticketItems;
	}
}
