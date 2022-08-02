package com.floreantpos.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseKitchenTicket;
import com.floreantpos.model.dao.KitchenTicketDAO;
import com.floreantpos.util.NumberUtil;



public class KitchenTicket extends BaseKitchenTicket {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public KitchenTicket () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public KitchenTicket (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/
	
	public Printer getPrinter() {
		PosPrinters printers = Application.getPrinters();
		//VirtualPrinter virtualPrinter = getVirtualPrinter();
		
		//if(virtualPrinter == null) {
		return printers.getDefaultKitchenPrinter();
		//}
		//System.out.println("Virtual printer not null");
		//return printers.getKitchenPrinterFor(virtualPrinter);
	}
	
	public String getBarPrinter() {
		PosPrinters printers = Application.getPrinters();
		return printers.getBarPrinter();
	}
	
	public static List<KitchenTicket> fromTicket(Ticket ticket) {
		Map<Printer, KitchenTicket> itemMap = new HashMap<Printer, KitchenTicket>();
		List<KitchenTicket> kitchenTickets = new ArrayList<KitchenTicket>(2);
		
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if(ticketItems == null) {
			return kitchenTickets;
		}
		
		for(int i = 1; i < 6;i++)
		{
			itemMap = addKitchenItems(ticketItems, i,itemMap,ticket);
			Collection<KitchenTicket> values = itemMap.values();
			for (KitchenTicket kitchenTicket : values) {
				kitchenTickets.add(kitchenTicket);
			}
			itemMap.clear();
		}
		return kitchenTickets;
	}
	
	public static Map<Printer,KitchenTicket> addKitchenItems(List<TicketItem> ticketItems, int index, Map<Printer, KitchenTicket> itemMap, Ticket ticket)
	{
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isPrintedToKitchen() || !ticketItem.isShouldPrintToKitchen()) {
				continue;
			}
			if (ticketItem.isBeverage()){continue;}
					
			Printer printer = ticketItem.getPrinter();
			if(printer == null) {
				continue;
			}
			
			if(ticketItem.getPrintorder() != index)
				continue;
			KitchenTicket kitchenTicket = itemMap.get(printer);
			
			if(kitchenTicket == null) {
				kitchenTicket = new KitchenTicket();
				kitchenTicket.setVirtualPrinter(ticketItem.getVirtualPrinter());
				kitchenTicket.setTicketId(ticket.getId());
				kitchenTicket.setCreateDate(new Date());
				
				kitchenTicket.setTableNumbers(ticket.getTableNumbers());
				kitchenTicket.setServerName(ticket.getOwner().getFirstName());
				kitchenTicket.setStatus(KitchenTicketStatus.WAITING.name());
				kitchenTicket.setVirtualPrinter(printer.getVirtualPrinter());
				
				KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);
				
				itemMap.put(printer, kitchenTicket);
			 }
			KitchenTicketItem item = new KitchenTicketItem();
			
			item.setMenuItemCode(ticketItem.getItemCode());
			item.setMenuItemPrice(NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiers()).toString());
			item.setMenuItemName(ticketItem.getNameDisplay());
			item.setQuantity(ticketItem.getItemCountDisplay());
			item.setStatus(KitchenTicketStatus.WAITING.name());
			kitchenTicket.addToticketItems(item);
			
			ticketItem.setPrintedToKitchen(true);
			
			includeModifiers(ticketItem, kitchenTicket); 
			includeCookintInstructions(ticketItem, kitchenTicket);
		}
		return itemMap;
	}
	public static List<KitchenTicket> deletefromTicket(Ticket ticket) {
		Map<Printer, KitchenTicket> itemMap = new HashMap<Printer, KitchenTicket>();
		List<KitchenTicket> kitchenTickets = new ArrayList<KitchenTicket>(2);
		System.out.println("deletefromTicket: ");
		List<TicketItem> ticketItems = ticket.getDeletedItems();
		if(ticketItems == null) {
			return kitchenTickets;
		}
		double total =0.0;
		int count = 0;
		for (TicketItem ticketItem : ticketItems) {
			
//			if (ticketItem.isBeverage()){continue;}
					
			Printer printer = ticketItem.getPrinter();
			if(printer == null) {
				continue;
			}
		
			KitchenTicket kitchenTicket = itemMap.get(printer);
			
			if(kitchenTicket == null) {
				kitchenTicket = new KitchenTicket();
				kitchenTicket.setVirtualPrinter(ticketItem.getVirtualPrinter());
				kitchenTicket.setTicketId(ticket.getId());
				kitchenTicket.setCreateDate(new Date());
				kitchenTicket.setTableNumbers(ticket.getTableNumbers());
				kitchenTicket.setServerName(ticket.getOwner().getFirstName());
				kitchenTicket.setStatus(KitchenTicketStatus.WAITING.name());
				kitchenTicket.setVirtualPrinter(printer.getVirtualPrinter());
				kitchenTicket.setVoided(true);						
				itemMap.put(printer, kitchenTicket);
			 }
			count += ticketItem.getItemCount();
			total += ticketItem.getTotalAmountWithoutModifiers();
			KitchenTicketItem item = new KitchenTicketItem();
			
			item.setMenuItemCode(ticketItem.getItemCode());
			item.setMenuItemPrice(NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiers()).toString());
			item.setMenuItemName(ticketItem.getNameDisplay());
			item.setQuantity(ticketItem.getItemCountDisplay());
			item.setStatus(KitchenTicketStatus.WAITING.name());
			
			kitchenTicket.addToticketItems(item);
			kitchenTicket.setTotalAmount(total);
			kitchenTicket.setItemCount(count);
			ticketItem.setPrintedToKitchen(true);
			
			includeModifiers(ticketItem, kitchenTicket);
			includeCookintInstructions(ticketItem, kitchenTicket);
			KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);		
		}
		
		Collection<KitchenTicket> values = itemMap.values();
		for (KitchenTicket kitchenTicket : values) {
			kitchenTickets.add(kitchenTicket);
			
		}
		
			return kitchenTickets;
	}
	public static List<KitchenTicket> deletefromBarTicket(Ticket ticket) {
		Map<Printer, KitchenTicket> itemMap = new HashMap<Printer, KitchenTicket>();
		List<KitchenTicket> kitchenTickets = new ArrayList<KitchenTicket>(2);
		
		List<TicketItem> ticketItems = ticket.getDeletedItems();
		if(ticketItems == null) {
			return kitchenTickets;
		}
		
		for (TicketItem ticketItem : ticketItems) {
			
			if (!ticketItem.isBeverage()){continue;}
					
			Printer printer = ticketItem.getPrinter();
			if(printer == null) {
				continue;
			}
			
			KitchenTicket kitchenTicket = itemMap.get(printer);
			
			if(kitchenTicket == null) {
				kitchenTicket = new KitchenTicket();
				kitchenTicket.setVirtualPrinter(ticketItem.getVirtualPrinter());
				kitchenTicket.setTicketId(ticket.getId());
				kitchenTicket.setCreateDate(new Date());
				kitchenTicket.setTableNumbers(ticket.getTableNumbers());
				kitchenTicket.setServerName(ticket.getOwner().getFirstName());
				kitchenTicket.setStatus(KitchenTicketStatus.WAITING.name());
				kitchenTicket.setVirtualPrinter(printer.getVirtualPrinter());
				kitchenTicket.setTotalAmount(ticketItem.getTotalAmountWithoutModifiers());
				kitchenTicket.setItemCount(ticketItem.getItemCount());
				KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);
				
				itemMap.put(printer, kitchenTicket);
			 }
			KitchenTicketItem item = new KitchenTicketItem();
			
			item.setMenuItemCode(ticketItem.getItemCode());
			item.setMenuItemPrice(NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiers()).toString());
			item.setMenuItemName(ticketItem.getNameDisplay());
			item.setQuantity(ticketItem.getItemCountDisplay());
			item.setStatus(KitchenTicketStatus.WAITING.name());
			
			kitchenTicket.addToticketItems(item);
			
			ticketItem.setPrintedToKitchen(true);
			
			includeModifiers(ticketItem, kitchenTicket);
			includeCookintInstructions(ticketItem, kitchenTicket);
		}
		
		Collection<KitchenTicket> values = itemMap.values();
		for (KitchenTicket kitchenTicket : values) {
			kitchenTickets.add(kitchenTicket);
			
		}
		
			return kitchenTickets;
	}
	public static List<KitchenTicket> fromBarTicket(Ticket ticket) {
		Map<String, KitchenTicket> itemMap = new HashMap<String, KitchenTicket>();
		List<KitchenTicket> kitchenTickets = new ArrayList<KitchenTicket>(2);
		
		List<TicketItem> ticketItems = ticket.getTicketItems();
		if(ticketItems == null) {
			return kitchenTickets;
		}
		
		for (TicketItem ticketItem : ticketItems) {
			if(ticketItem.isPrintedToKitchen() || !ticketItem.isShouldPrintToKitchen()) {
				continue;
		}
			
		if (!ticketItem.isBeverage()){continue;}
					
		String printer = ticketItem.getBarPrinter();
		if(printer == null) {
			continue;
		}
		
		KitchenTicket kitchenTicket = itemMap.get(printer);
		
		if(kitchenTicket == null) {
			kitchenTicket = new KitchenTicket();
			kitchenTicket.setVirtualPrinter(ticketItem.getVirtualPrinter());
			kitchenTicket.setTicketId(ticket.getId());
			kitchenTicket.setCreateDate(new Date());
			kitchenTicket.setTableNumbers(ticket.getTableNumbers());
			kitchenTicket.setServerName(ticket.getOwner().getFirstName());
			kitchenTicket.setStatus(KitchenTicketStatus.WAITING.name());
						
			KitchenTicketDAO.getInstance().saveOrUpdate(kitchenTicket);
			
			itemMap.put(printer, kitchenTicket);
		 }
		KitchenTicketItem item = new KitchenTicketItem();
		
		item.setMenuItemCode(ticketItem.getItemCode());
		item.setMenuItemPrice(NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiers()).toString());
		item.setMenuItemName(ticketItem.getNameDisplay());
		item.setQuantity(ticketItem.getItemCountDisplay());
		item.setStatus(KitchenTicketStatus.WAITING.name());
		
		kitchenTicket.addToticketItems(item);
		
		ticketItem.setPrintedToKitchen(true);
			
		includeModifiers(ticketItem, kitchenTicket);
		includeCookintInstructions(ticketItem, kitchenTicket);
		}
		
		Collection<KitchenTicket> values = itemMap.values();
		for (KitchenTicket kitchenTicket : values) {
			kitchenTickets.add(kitchenTicket);
			
		}
		
		return kitchenTickets;
	}
	
	private static void includeCookintInstructions(TicketItem ticketItem, KitchenTicket kitchenTicket) {
		List<TicketItemCookingInstruction> cookingInstructions = ticketItem.getCookingInstructions();
		if (cookingInstructions != null) {
			for (TicketItemCookingInstruction ticketItemCookingInstruction : cookingInstructions) {
				KitchenTicketItem item = new KitchenTicketItem();
				item.setMenuItemCode("");
				item.setMenuItemName(ticketItemCookingInstruction.getNameDisplay());
				item.setMenuItemPrice(NumberUtil.formatNumber(ticketItem.getTotalAmountWithoutModifiers()).toString());
				item.setQuantity(ticketItemCookingInstruction.getItemCountDisplay());
				kitchenTicket.addToticketItems(item);
			}
		}
	}

	private static void includeModifiers(TicketItem ticketItem, KitchenTicket kitchenTicket) {
		List<TicketItemModifierGroup> ticketItemModifierGroups = ticketItem.getTicketItemModifierGroups();
		if (ticketItemModifierGroups != null) {
			for (TicketItemModifierGroup ticketItemModifierGroup : ticketItemModifierGroups) {
				List<TicketItemModifier> ticketItemModifiers = ticketItemModifierGroup.getTicketItemModifiers();
				if (ticketItemModifiers != null) {
					for (TicketItemModifier itemModifier : ticketItemModifiers) {

						if (itemModifier.isPrintedToKitchen() || !itemModifier.isShouldPrintToKitchen()) {
							continue;
						}

						KitchenTicketItem item = new KitchenTicketItem();
						item.setMenuItemCode("");
						item.setMenuItemName(itemModifier.getNameDisplay());
						item.setMenuItemPrice(NumberUtil.formatNumber(itemModifier.getTotalAmountWithoutModifiersDisplay()).toString());
						item.setQuantity(itemModifier.getItemCountDisplay());
						item.setStatus(KitchenTicketStatus.WAITING.name());
						kitchenTicket.addToticketItems(item);
						
						itemModifier.setPrintedToKitchen(true);
					}
				}
			}
		}
	}
	
	public static enum KitchenTicketStatus {
		WAITING, VOID, DONE;
	}
}