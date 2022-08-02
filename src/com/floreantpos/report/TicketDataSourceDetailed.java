package com.floreantpos.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.ui.ticket.TicketItemRowCreator;
import com.floreantpos.util.NumberUtil;

public class TicketDataSourceDetailed extends AbstractReportDataSource {

	public TicketDataSourceDetailed() {
		super(new String[] { "itemName", "itemQty", "itemSubtotal","ItemId", "unitPrice", "stoer" });
	}


	public TicketDataSourceDetailed(Ticket ticket) {		
		 super(new String[] { "itemName", "itemQty", "itemSubtotal","ItemId", "unitPrice", "stoer" });
		  
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

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) { 
		ITicketItem item = (ITicketItem) rows.get(rowIndex);
		if(item.getItemCode().compareTo("999") == 0)return null;
		switch (columnIndex) {
		case 0:
			String name=item.getNameDisplay();
			 
			if(item.getTaxExist()) {
				 //double txAmt=NumberUtil.roundToTwoDigit(item.getDisAmt());
				 double txAmt=item.getDisAmt();
				 name= name + "(Rabatt "+String.valueOf(txAmt)+")";
			}
			 
			if(TerminalConfig.isShortReceipt())
			{					
				if(TerminalConfig.isItemBarcode()&&item.getItemCountDisplay() != null&&item.getBarCode()!=null) {
					if(item.getItemCountDisplay()>1)
						return ( item.getItemCountDisplay()+ " X " + item.getNameDisplay()+ "\n***" +item.getBarCode()+"***");
					return (item.getNameDisplay()+ "(" +item.getBarCode()+")");
				}else if(item.getItemCountDisplay() != null)
					return ( item.getItemCountDisplay()+ " X " + name);
				else if(item.getNameDisplay().contains("mit"))
				{
					return ( item.getItemCountDisplay()+ " X " + name);
				}
				else
					return null;
			} else {

				if(TerminalConfig.isItemBarcode()&&item.getItemCountDisplay() != null&&item.getBarCode()!=null) {

					if(item.getItemCountDisplay()>1) {
						return ( item.getItemCountDisplay()+ "X " + name);
					} else {
						return ( "1X "+name);
					}

				} else if(item.getItemCountDisplay() != null) {
					if(item.getItemCountDisplay()>1) {
						return ( item.getItemCountDisplay()+ "X " +name);
					} else {
						return "1X " + name;
					}

				} else if(item.getNameDisplay().contains("mit")) {
					if(item.getItemCountDisplay()>1)
						return ( item.getItemCountDisplay()+ "X " +name);
					else
						return name;	
				}
				else
					return null;
			}

		case 1:
			Integer itemCountDisplay = item.getItemCountDisplay();
			if(itemCountDisplay == null) {
				return null;
			}

			//return String.valueOf(itemCountDisplay);
			return null;
		case 2:
			Double total = item.getTotalAmountWithoutModifiersDisplay()+item.getDiscountAmount();
			if(total == null) {
				return null;
			}
			if(!TerminalConfig.isShortReceipt()) {
				if(item.getItemCountDisplay()>1) {
					if(restaurant.isItemPriceIncludesTax())
						return	(String.valueOf(NumberUtil.formatNumber(total)));
					else
						return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay())));
				}
				else {
					if(restaurant.isItemPriceIncludesTax())
						return	(String.valueOf(NumberUtil.formatNumber(total)));
					else
						return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay())));
				}
			}else
				if(restaurant.isItemPriceIncludesTax())
					return	(String.valueOf(NumberUtil.formatNumber(total)));
				else
					return (String.valueOf(NumberUtil.formatNumber(total-item.getTaxAmountWithoutModifiersDisplay())));

		case 3:
			return ( item.getItemCode());
			
		case 4:
			return   Double.toString(item.getUnitPriceDisplay());
			
		case 5:
			int value = (int) Math.round( item.getTaxRate());
			return  Integer.toString(value)+"%";
			
		}

		return null;
	}
}