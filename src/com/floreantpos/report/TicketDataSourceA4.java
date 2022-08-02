package com.floreantpos.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.ui.ticket.TicketItemRowCreator;
import com.floreantpos.util.NumberUtil;

public class TicketDataSourceA4 extends AbstractReportDataSource {
	
	public TicketDataSourceA4() {
		super(new String[] { "itemName", "itemQty", "itemSubtotal","itemPrice","ItemId", "itemTax" });
	}

	public TicketDataSourceA4(Ticket ticket) {
		super(new String[] { "itemName", "itemQty", "itemSubtotal","itemPrice","ItemId", "itemTax"});

		setTicket(ticket);
	}
	
	private void setTicket(Ticket ticket) {
		ArrayList<ITicketItem> rows = new ArrayList<ITicketItem>();

		LinkedHashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();
		TicketItemRowCreator.calculateTicketRows(ticket, tableRows);

		rows.addAll(tableRows.values());
		setRows(rows);
	}
Restaurant restaurant = RestaurantDAO.getRestaurant();
	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem item = (ITicketItem) rows.get(rowIndex);
		
		switch (columnIndex) {
			case 0:
				return " "+item.getNameDisplay();

			case 1:
				Integer itemCountDisplay = item.getItemCountDisplay();
				if(itemCountDisplay == null) {
					return null;
				}
				String inhalt = item.getUnitType();
			if(inhalt.length()>1)
				return " "+NumberUtil.paddingThreeDigit(itemCountDisplay)+" | "+inhalt;
			else
				return " "+NumberUtil.paddingThreeDigit(itemCountDisplay);
			case 2:
				Double total = item.getTotalAmountWithoutModifiersDisplay();
				if(total == null) {
					return null;
				}
				if(restaurant.isItemPriceIncludesTax())
					return	(String.valueOf(NumberUtil.formatNumber(total))+" "+Application.getCurrencySymbol()+" ");
				else
				return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay()))+" "+Application.getCurrencySymbol()+" ");

			case 3:
				if(restaurant.isItemPriceIncludesTax())
				return (String.valueOf(NumberUtil.formatNumber(item.getUnitPriceDisplay()-(item.getTaxAmountWithoutModifiersDisplay()/item.getItemCountDisplay())))+" ");
				else
					return (String.valueOf(NumberUtil.formatNumber(item.getUnitPriceDisplay()))+" ");	
			case 4:
				return " "+(rowIndex+1)+"";
			case 5:
//				return String.valueOf(NumberUtil.formatNumber(item.getTaxAmountWithoutModifiersDisplay()));
				return (String.valueOf(NumberUtil.formatNumber(item.getTaxRate()))+" %");	
		}

		return null;
	}
}