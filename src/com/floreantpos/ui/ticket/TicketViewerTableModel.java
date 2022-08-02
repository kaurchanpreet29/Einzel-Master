package com.floreantpos.ui.ticket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.TicketItemCookingInstruction;
import com.floreantpos.model.TicketItemModifier;
import com.floreantpos.model.TicketItemModifierGroup;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.util.NumberUtil;

public class TicketViewerTableModel extends AbstractTableModel {
	private JTable table;
	protected Ticket ticket;
	protected final HashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();

	private boolean priceIncludesTax = false;
	
	protected String[] excludingTaxColumnNames = { POSConstants.Anz,POSConstants.Artikel, POSConstants.PRICE};
	//protected String[] includingTaxColumnNames = { "ItemId","Item", "U/Price", "Unit", "Price" };

	private boolean forReciptPrint;
	private boolean printCookingInstructions;

	public TicketViewerTableModel(JTable table) {	
		this(table, null);
	}

	public TicketViewerTableModel(JTable table, Ticket ticket) {
		this.table = table;
		setTicket(ticket);
		
	}

	public int getItemCount() {
		return tableRows.size();
	}

	@Override
	public int getRowCount() {
		int size = tableRows.size();
		
		return size;
	}

	public int getActualRowCount() {
		return tableRows.size();
	}

	@Override
	public int getColumnCount() {
		/*if(priceIncludesTax) {
			return includingTaxColumnNames.length;
		}*/
		
		return excludingTaxColumnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		/*if(priceIncludesTax) {
			return includingTaxColumnNames[column];
		}*/
		
		return excludingTaxColumnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {	   
	    return getValueAt(0, columnIndex).getClass();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem ticketItem = tableRows.get(String.valueOf(rowIndex));
		
		if(ticketItem == null) {
			return null;
		}
		switch (columnIndex) {
			case 0:
				return ticketItem.getItemCountDisplay();
				
			case 1:
				return ticketItem.getNameDisplay();

			case 2:
//				return ticketItem.getItemCountDisplay();
//
//			case 3:
				if(Application.getInstance().isPriceIncludesTax())
				return NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiersDisplay()+ticketItem.getDiscountAmount());
				else
					return NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiersDisplay()-ticketItem.getTaxAmountWithoutModifiersDisplay()+ticketItem.getDiscountAmount());
		}

		return ticketItem.getItemCode();
	}

	private void calculateRows() {
		TicketItemRowCreator.calculateTicketRows(ticket, tableRows);
	}

	public int addTicketItem(TicketItem ticketItem) {

		int count = 1;
		try {
			if(TerminalConfig.isTakeItemCount())
			count = Integer.parseInt(OrderView.getInstance().getTicketView().getItemZahl());
		} catch(Exception ex) {
			
		}
		if (ticketItem.isHasModifiers()) {
			return addTicketItemToTicket(ticketItem);
		}

		Set<Entry<String, ITicketItem>> entries = tableRows.entrySet();
		for (Entry<String, ITicketItem> entry : entries) {

			if (!(entry.getValue() instanceof TicketItem)) {
				continue;
			}

			TicketItem t = (TicketItem) entry.getValue();
			if (ticketItem.getName().equals(t.getName()) && !t.isPrintedToKitchen() && (ticketItem.getUnitPrice().toString().compareTo(t.getUnitPrice().toString())== 0)) {
				
				if(t.getDiscountAmount()!=null&&t.getDiscountAmount()>0) {					
					t.setDiscountAmount((t.getDiscountAmount()/t.getItemCount())*(t.getItemCount()+ticketItem.getItemCount()));					 
				}
				
				t.setItemCount(t.getItemCount() + count);

				table.repaint();

				return Integer.parseInt(entry.getKey());
			}
		}

		return addTicketItemToTicket(ticketItem);
	}

	private int addTicketItemToTicket(TicketItem ticketItem) {
		ticket.addToticketItems(ticketItem);
		calculateRows();
		fireTableDataChanged();

		return tableRows.size() - 1;
	}

	public void addAllTicketItem(TicketItem ticketItem) {
		if (ticketItem.isHasModifiers()) {
			List<TicketItem> ticketItems = ticket.getTicketItems();
			ticketItems.add(ticketItem);

			calculateRows();
			fireTableDataChanged();
		}
		else {
			List<TicketItem> ticketItems = ticket.getTicketItems();
			boolean exists = false;
			for (TicketItem item : ticketItems) {
				if (item.getName().equals(ticketItem.getName())) {
					int itemCount = item.getItemCount();
					itemCount += ticketItem.getItemCount();
					item.setItemCount(itemCount);
					exists = true;
					table.repaint();
					return;
				}
			}
			if (!exists) {
				ticket.addToticketItems(ticketItem);
				calculateRows();
				fireTableDataChanged();
			}
		}
	}

	public boolean containsTicketItem(TicketItem ticketItem) {
		if (ticketItem.isHasModifiers())
			return false;

		List<TicketItem> ticketItems = ticket.getTicketItems();
		for (TicketItem item : ticketItems) {
			if (item.getName().equals(ticketItem.getName())) {
				return true;
			}
		}
		return false;
	}

	public void removeModifier(TicketItem parent, TicketItemModifier modifierToDelete) {
		TicketItemModifierGroup ticketItemModifierGroup = modifierToDelete.getParent();
		List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();

		for (Iterator iter = ticketItemModifiers.iterator(); iter.hasNext();) {
			TicketItemModifier modifier = (TicketItemModifier) iter.next();
			if (modifier.getItemId() == modifierToDelete.getItemId()) {
				iter.remove();

				if (modifier.isPrintedToKitchen()) {
					ticket.addDeletedItems(modifier);
				}

				calculateRows();
				fireTableDataChanged();
				return;
			}
		}
	}

	public Object delete(int index) {
		if (index < 0 || index >= tableRows.size())
			return null;

		Object object = tableRows.get(String.valueOf(index));
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int rowNum = ticketItem.getTableRowNum();

			List<TicketItem> ticketItems = ticket.getTicketItems();
			for (Iterator iter = ticketItems.iterator(); iter.hasNext();) {
				TicketItem item = (TicketItem) iter.next();
				if (item.getTableRowNum() == rowNum) {
					int count = item.getItemCount();
					int newCount = 0;
					Double newAmount = 0.00;
					if(item.getItemCount() == 1)
						iter.remove();
					else
					{
						newCount = item.getItemCount()-1;
						item.setItemCount(newCount);
					}
					if(count != 1)
					{
						TicketItem item_new = new TicketItem();
						newAmount = item.getTotalAmountWithoutModifiers();
						item_new.setId(item.getId());
						item_new.setItemId(item.getItemId());
						item_new.setTotalAmountWithoutModifiers(newAmount);
						item_new.setItemCount(1);
						item_new.setBeverage(item.isBeverage());
						item_new.setCategoryName(item.getCategoryName());
						item_new.setGroupName(item.getGroupName());
						item_new.setHasModifiers(item.isHasModifiers());
						item_new.setCookingInstructions(item.getCookingInstructions());
						item_new.setShouldPrintToKitchen(item.isShouldPrintToKitchen());
						item_new.setUnitPrice(item.getUnitPrice());
						item_new.setItemCount(1);
						item_new.setName(item.getName());
						item_new.setTotalAmountWithoutModifiers(item.getUnitPriceDisplay());
						if (item.isPrintedToKitchen()&&Application.getInstance().getCurrentUser().getFirstName().compareTo("Master")!=0
								||item.isPrintedToKitchen()&&TerminalConfig.isTseEnable()) {
							ticket.addDeletedItems(item_new);
						}
					}
					else
					{
						if (item.isPrintedToKitchen()&&Application.getInstance().getCurrentUser().getFirstName().compareTo("Master")!=0
								||item.isPrintedToKitchen()&&TerminalConfig.isTseEnable()) {
						    ticket.addDeletedItems(item);
						}
					}
					break;
				}
			}
		}
		else if (object instanceof TicketItemModifier) {
			TicketItemModifier itemModifier = (TicketItemModifier) object;
			TicketItemModifierGroup ticketItemModifierGroup = itemModifier.getParent();
			List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();

			if (ticketItemModifiers != null) {
				for (Iterator iterator = ticketItemModifiers.iterator(); iterator.hasNext();) {
					TicketItemModifier element = (TicketItemModifier) iterator.next();
					if (itemModifier.getTableRowNum() == element.getTableRowNum()) {
						iterator.remove();
						if (element.isPrintedToKitchen()&&Application.getInstance().getCurrentUser().getFirstName().compareTo("Master")!=0
								||element.isPrintedToKitchen()&&TerminalConfig.isTseEnable()) {
							ticket.addDeletedItems(element);
						}
					}
				}
			}
		}
		else if (object instanceof TicketItemCookingInstruction) {
			TicketItemCookingInstruction cookingInstruction = (TicketItemCookingInstruction) object;
			int tableRowNum = cookingInstruction.getTableRowNum();

			TicketItem ticketItem = null;
			while (tableRowNum > 0) {
				Object object2 = tableRows.get(String.valueOf(--tableRowNum));
				if (object2 instanceof TicketItem) {
					ticketItem = (TicketItem) object2;
					break;
				}
			}

			if (ticketItem != null) {
				ticketItem.removeCookingInstruction(cookingInstruction);
			}
		}

		calculateRows();
		fireTableDataChanged();
		return object;
	}
	
	public Object addRabat(int index, int Value) {
		if (index < 0 || index >= tableRows.size())
			return null;

		Object object = tableRows.get(String.valueOf(index));
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int rowNum = ticketItem.getTableRowNum();

			List<TicketItem> ticketItems = ticket.getTicketItems();
			for (Iterator iter = ticketItems.iterator(); iter.hasNext();) {
				TicketItem item = (TicketItem) iter.next();
				if (item.getTableRowNum() == rowNum) {
										
					String itemName = item.getName();

					if (itemName.length() > 15) {
						itemName = itemName.substring(0, 15);
					}
					
					Double value = (NumberUtil.roundToTwoDigit(item.getTotalAmount())/100)*Value;
					
					if (item.getTotalAmount() > 0.00) {
						ticketItem.setName(itemName + "(R - " + NumberUtil.formatNumber(value) + "");
					}
				

					item.setDiscountAmount(value);
					item.setDiscountRate(new Double(Value));
					break;
				}
			}
		}
		calculateRows();
		fireTableDataChanged();
		return object;
	}
	
	public Object updateTicketItem(int index, String name, int count, double unitPrice) {
		if (index < 0 || index >= tableRows.size())
			return null;

		Object object = tableRows.get(String.valueOf(index));
		if (object instanceof TicketItem) {
			TicketItem ticketItem = (TicketItem) object;
			int rowNum = ticketItem.getTableRowNum();
			List<TicketItem> ticketItems = ticket.getTicketItems();
			for (Iterator iter = ticketItems.iterator(); iter.hasNext();) {
				TicketItem item = (TicketItem) iter.next();
				if (item.getTableRowNum() == rowNum) {										
					item.setName(name);
					item.setUnitPrice(unitPrice);
					if(unitPrice<0)
						item.setPfand(true);
					item.setDiscountAmount(item.getDiscountAmount()*count);
					item.setItemCount(count);
					item.setTotalAmount(unitPrice*count);
					item.setTotalAmountWithoutModifiers(unitPrice*count);
					break;
				}
			}
		}
		calculateRows();
		fireTableDataChanged();
		return object;
	}
	
	
	public void updateItems(int priceCategory) {	
			List<TicketItem> ticketItems = ticket.getTicketItems();
			for (Iterator iter = ticketItems.iterator(); iter.hasNext();) {
				TicketItem item = (TicketItem) iter.next();
							String barcode = item.getBarcode();
							if(barcode.isEmpty()||!barcode.isEmpty()&&Double.valueOf(barcode)==0)
								continue;
							barcode = barcode.trim();
							List<MenuItem> items = MenuItemDAO.getInstance().findByBarcodePrice(barcode, priceCategory);
							if(items.isEmpty())
								items = MenuItemDAO.getInstance().findByBarcode1Price(barcode, priceCategory);
							if(items.isEmpty())
								items = MenuItemDAO.getInstance().findByBarcode2Price(barcode, priceCategory);
							if(items.isEmpty())
								continue;
							MenuItem menuItem = null;
							for (int i = 0; i < items.size(); i++) {
								menuItem = items.get(i);								
							}
							if(menuItem==null)
								continue;
							MenuItemDAO dao = new MenuItemDAO();
							menuItem = dao.initialize(menuItem);
					TicketItem conItem = menuItem.convertToTicketItem(ticket, item.getItemCount());
					item.setUnitPrice(conItem.getUnitPrice()*(item.getWeightgrams()/1000));
					item.setTotalAmountWithoutModifiers(conItem.getUnitPrice()*(item.getWeightgrams()/1000)*item.getItemCount());
					System.out.println(barcode+" Price "+conItem.getUnitPrice()+item.getWeightgrams());
				
		}
		calculateRows();
		fireTableDataChanged();
	}
	

	public Object get(int index) {
		if (index < 0 || index >= tableRows.size())
			return null;

		return tableRows.get(String.valueOf(index));
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;

		update();
	}

	public void update() {
		calculateRows();
		fireTableDataChanged();
	}
	
	public boolean isForReciptPrint() {
		return forReciptPrint;
	}

	public void setForReciptPrint(boolean forReciptPrint) {
		this.forReciptPrint = forReciptPrint;
	}

	public boolean isPrintCookingInstructions() {
		return printCookingInstructions;
	}

	public void setPrintCookingInstructions(boolean printCookingInstructions) {
		this.printCookingInstructions = printCookingInstructions;
	}

	public boolean isPriceIncludesTax() {
		return priceIncludesTax;
	}

	public void setPriceIncludesTax(boolean priceIncludesTax) {
		this.priceIncludesTax = priceIncludesTax;
	}
}
