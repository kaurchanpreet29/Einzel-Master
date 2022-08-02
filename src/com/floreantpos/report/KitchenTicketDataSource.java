package com.floreantpos.report;

import com.floreantpos.model.KitchenTicket;
import com.floreantpos.model.KitchenTicketItem;

public class KitchenTicketDataSource extends AbstractReportDataSource {

	public KitchenTicketDataSource() {
		super(new String[] { "itemNo", "itemName", "itemPrice"});
	}

	public KitchenTicketDataSource(KitchenTicket ticket) {
		super(new String[] { "itemNo", "itemName", "itemPrice"});

		setTicket(ticket);
	}

	private void setTicket(KitchenTicket ticket) {
		setRows(ticket.getTicketItems());
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		KitchenTicketItem item = (KitchenTicketItem) rows.get(rowIndex);

		switch (columnIndex) {
			case 0:				
				if (item.getMenuItemCode() == null || item.getMenuItemCode().length() == 0)
				{
					return "*000";
				}
				
				return ( "#"+item.getMenuItemCode());
				
			case 1:
				Integer itemCountDisplay = item.getQuantity();
				System.out.println(item.getMenuItemName() + ":"+item.getMenuItemPrice());
				if (item.getMenuItemCode().length() == 0)
				{
					if(item.getMenuItemName().contains("mit"))
						return ("  "+ item.getMenuItemName() + " X " + itemCountDisplay);
					else
						return item.getMenuItemName();
				}
				return (itemCountDisplay + " X " + item.getMenuItemName());
			case 2:
				if (item.getMenuItemCode().length() == 0)
				{
					if(item.getMenuItemName().contains("mit"))
						return (item.getMenuItemPrice());
					else
						return "0,00";
				}
				else
					return (item.getMenuItemPrice());
			}

		return null;
	}
}
