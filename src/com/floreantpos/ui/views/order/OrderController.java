package com.floreantpos.ui.views.order;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.floreantpos.POSConstants;
import com.floreantpos.actions.SettleTicketAction;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ActionHistory;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuModifier;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.ActionHistoryDAO;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.ui.dialog.ErrorMessageDialog;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.WeightDialog;
import com.floreantpos.ui.views.SwitchboardView;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;
import com.floreantpos.ui.views.order.actions.GroupSelectionListener;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.ui.views.order.actions.ModifierSelectionListener;
import com.floreantpos.ui.views.order.actions.OrderListener;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.ui.dialog.MenuItemPriceDialog;

public class OrderController implements OrderListener, CategorySelectionListener, GroupSelectionListener, ItemSelectionListener, ModifierSelectionListener {
	private OrderView orderView;

	public OrderController(OrderView orderView) {
		this.orderView = orderView;

		orderView.getCategoryView().addCategorySelectionListener(this);
		orderView.getGroupView().addGroupSelectionListener(this);
		orderView.getItemView().addItemSelectionListener(this);
		orderView.getOthersView().setItemSelectionListener(this);
		orderView.getModifierView().addModifierSelectionListener(this);
		orderView.getTicketView().addOrderListener(this);
		orderView.getTicketView().setItemSelectionListener(this);
	}

	@Override
	public void categorySelected(MenuCategory foodCategory) {
		orderView.showView(GroupView.VIEW_NAME);
		orderView.getGroupView().setMenuCategory(foodCategory);
	}

	@Override
	public void groupSelected(MenuGroup foodGroup) {
		orderView.showView(MenuItemView.VIEW_NAME);
		orderView.getItemView().setMenuGroup(foodGroup);
	}

	@Override
	public void itemSelected(MenuItem menuItem) {
		MenuItemDAO dao = new MenuItemDAO();
		menuItem = dao.initialize(menuItem);	
		String menuname = menuItem.getName();
		boolean changeMenuItem = false;
		
		System.out.println("menuItem.getMenuitemprice().size() : "+ menuItem.getMenuitemprice().size() );
		
		if(menuItem.isChangeprice()!= null && menuItem.isChangeprice())
		{
			Double newTotal = 0.00;
			NumberSelectionDialog2.takeDoubleInput("Neuer Gesamtbetrag","Neuer Gesamtbetrag", newTotal);
			if(newTotal > 0.00)
				menuItem.setPrice(newTotal);
			if(menuItem.isPfand()) {
				if(newTotal>0.00)
					menuItem.setPrice(0-newTotal);
			}
		} else if(menuItem.getMenuitemprice() != null && menuItem.getMenuitemprice().size() > 0){
				MenuItemPriceDialog dialog = new MenuItemPriceDialog(menuItem);
				dialog.setTitle("Waehlen Sie die Artikel");
				dialog.pack();
				dialog.open();

				if(!dialog.isCanceled())
				{
					menuItem = dialog.getMenuItem();
					changeMenuItem = true;
				}
			} 
		if(menuItem.getWeightgrams() != null && menuItem.getWeightgrams().compareTo(0.00) > 0 )
		{
			WeightDialog dialog = new WeightDialog(menuItem.getWeightgrams(), menuItem.getPrice(), menuItem.getName(),menuItem.getItemId());
			dialog.setPreferredSize(new Dimension(750,600));
			dialog.setTitle("Gewicht");
			dialog.pack();
			dialog.open();			
			if(dialog.isCanceled())
			{
				return;
			}
			else if(dialog.getNetPrice().compareTo(0.00) != 0)
			{
				menuItem.setPrice(dialog.getNetPrice());
				menuItem.setName(menuItem.getName() + "-"+ dialog.getGivenWeight().intValue()+ "g");
				Double weight = (Double.valueOf(dialog.getGivenWeight().intValue()));
				menuItem.setWeightgrams(weight);
			}
		}
		
		 TicketItem ticketItem = menuItem.convertToTicketItem();
			if(changeMenuItem)
			{
				menuname = menuname.concat(" "+ticketItem.getName());
				if(menuname.length() > 30)
					ticketItem.setName(menuname.substring(0, 30));
				else
					ticketItem.setName(menuname);				
			}

		int count = 1;
		if(TerminalConfig.isTakeItemCount()) {
			try {			
				count = Integer.parseInt(orderView.getTicketView().getItemZahl());
				if (count > 10) {
					ErrorMessageDialog dialog = new ErrorMessageDialog("Art. Anzahl " + count + " ist zu hoch",
							true);
					dialog.pack();
					dialog.open();
					if (dialog.isCancelled())
						return;
				}
			} catch(Exception ex) {

			}
		}

		orderView.getTicketView().addTseItem(menuItem, count, null);
		
		TicketItem ticketItem_ = menuItem.convertToTicketItem(count);
		if(ticketItem_.isPfand())
			orderView.getTicketView().getTicket().setRefunded(true);
		orderView.getTicketView().addTicketItem(ticketItem_);

		if (menuItem.hasModifiers()) {
			ModifierView modifierView = orderView.getModifierView();
			modifierView.setMenuItem(menuItem, ticketItem_);
			orderView.showView(ModifierView.VIEW_NAME); 
		}
	}

	@Override
	public void modifierSelected(MenuItem parent, MenuModifier modifier) {
		 
	}

	@Override
	public void itemSelectionFinished(MenuGroup parent) {
		MenuCategory menuCategory = parent.getParent();
		GroupView groupView = orderView.getGroupView();
		if (!menuCategory.equals(groupView.getMenuCategory())) {
			groupView.setMenuCategory(menuCategory);
		}
		orderView.showView(GroupView.VIEW_NAME);
	}

	@Override
	public void modifierSelectionFiniched(MenuItem parent) {
		MenuGroup menuGroup = parent.getParent();
		MenuItemView itemView = orderView.getItemView();
		if (!menuGroup.equals(itemView.getMenuGroup())) {
			itemView.setMenuGroup(menuGroup);
		}
		orderView.showView(MenuItemView.VIEW_NAME);
	}

	@Override
	public void payOrderSelected(Ticket ticket) {
		if(!TerminalConfig.isTabVersion())
			RootView.getInstance().showView(SwitchboardView.VIEW_NAME);
		List<TicketItem> itemList = ticket.getTicketItems();
		List<TicketItem> newItemList = new ArrayList();
		for(Iterator<TicketItem> itr = itemList.iterator();itr.hasNext();)
		{
			TicketItem item = itr.next();
			if(item.isHasModifiers())
			{
				newItemList.add(item);
			}
			else if(!isFound(newItemList, item.getItemCode(), item.getName(),item.getUnitPriceDisplay())) 
			{
				int count = 0;
				count = checkTicketItem(ticket, item.getItemCode(), item.getName(),item.getUnitPrice());
				Double price = item.getUnitPrice();
				item.setTotalAmountWithoutModifiers(count * price);
				item.setItemCount(count);
				newItemList.add(item);
			}
		}
		ticket.setTicketItems(newItemList);
		new SettleTicketAction(ticket.getId()).execute();
		SwitchboardView.getInstance().updateTicketList();
	}
	public int checkTicketItem(Ticket ticket, String id, String name, Double unitPrice)
	{
		int index = 0;
		List<TicketItem> itemList = ticket.getTicketItems();
		for(Iterator<TicketItem> itr = itemList.iterator();itr.hasNext();)
		{
			TicketItem item = itr.next();
			if((item.getItemCode().compareTo(id)==0) && (item.getName().compareTo(name) == 0) && (Double.compare(item.getUnitPrice(),unitPrice)== 0))
			{
				index = index + item.getItemCount();
			}
		}
		return index;
	}

	public boolean isFound(List<TicketItem> itemList, String id, String name, Double unitPrice)
	{
		for(Iterator<TicketItem> itr = itemList.iterator(); itr.hasNext();)
		{
			TicketItem item = itr.next();

			if((item.getItemCode().compareTo(id) == 0 )&& (item.getName().compareTo(name) == 0) && (Double.compare(item.getUnitPriceDisplay(),unitPrice) == 0))
			{
				return true;
			}
		}
		return false;
	}
	//VERIFIED
	public synchronized static void saveOrder(Ticket ticket) {
		if (ticket == null)
			return;
		GenericDAO dao = new GenericDAO();
		Session session = dao.getSession();
		Transaction tx = session.beginTransaction();
		boolean newTicket = ticket.getId() == null;
		session.saveOrUpdate(ticket);
		tx.commit();
		dao.closeSession(session);

		//	save the action
		ActionHistoryDAO actionHistoryDAO = ActionHistoryDAO.getInstance();
		User user = Application.getCurrentUser();

		if (newTicket) {
			actionHistoryDAO.saveHistory(user, ActionHistory.NEW_CHECK, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId());
		}
		else {
			actionHistoryDAO.saveHistory(user, ActionHistory.EDIT_CHECK, POSConstants.RECEIPT_REPORT_TICKET_NO_LABEL + ":" + ticket.getId());
		}
	}

	@Override
	public void itemSelected(MenuItem menuItem, Ticket ticket) {
		
		String menuname = menuItem.getName();
		boolean changeMenuItem = false;
		MenuItemDAO dao = new MenuItemDAO();
		menuItem = dao.initialize(menuItem);

		if(menuItem.isChangeprice()!= null && menuItem.isChangeprice())
		{
			Double newTotal = 0.00;
			NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
			dialog.setFloatingPoint(true);
			dialog.setTitle("Geben Sie die Gesamt: "+ NumberUtil.formatNumber(menuItem.getPrice()) + " €");
			dialog.setDialogTitle("Aendern Preis");
			dialog.pack();
			dialog.open();
			
			if (dialog.isCanceled()) {
				return ;
			}
			newTotal = dialog.getValue();
			if(newTotal > 0.00)
				menuItem.setPrice(newTotal);
			else
			if(menuItem.isPfand()) {
				if(newTotal>0.00)
					menuItem.setPrice(0-newTotal);
			}
		} else if(menuItem.getMenuitemprice() != null && menuItem.getMenuitemprice().size() > 0) {		  
			 
				MenuItemPriceDialog dialog = new MenuItemPriceDialog(menuItem);
				dialog.setTitle("Waehlen Sie die Artikel");
				dialog.pack();
				dialog.open();
				if(dialog.isCanceled())
				{
					return;
				}
				menuItem = dialog.getMenuItem();
				changeMenuItem = true;
			}	
		if(menuItem.getWeightgrams() != null && menuItem.getWeightgrams().compareTo(0.00) > 0 )
		{
			WeightDialog dialog = new WeightDialog(menuItem.getWeightgrams(), menuItem.getPrice(), menuItem.getName(),menuItem.getItemId());
			dialog.setPreferredSize(new Dimension(750,600));
			dialog.setTitle("Gewicht");
			dialog.pack();
			dialog.open();

			if(dialog.isCanceled())
				return;
			else if(dialog.getNetPrice().compareTo(0.00) != 0)
			{
				menuItem.setPrice(dialog.getNetPrice());
				menuItem.setName(menuItem.getName() + "-"+ dialog.getGivenWeight().intValue()+ "g");
				Double weight = (Double.valueOf(dialog.getGivenWeight().intValue()));
				menuItem.setWeightgrams(weight);
			}
		}


		try {
			if(TerminalConfig.isInventurAlert()&&menuItem.getInstock() != null){
				int stock = menuItem.getInstock();			
				if(stock<10) {
					JOptionPane.showMessageDialog(null, "Achtung !!!  "+"Übrig gebliebende Menge ist "+stock);
				}
			}
		} catch(Exception ex) {   	}

		int count = 1;
		if(TerminalConfig.isTakeItemCount()) {
			try {			
				count = Integer.parseInt(orderView.getTicketView().getItemZahl());
				if (count > 10) {
					ErrorMessageDialog dialog = new ErrorMessageDialog("Art. Anzahl " + count + " ist zu hoch",
							true);
					dialog.pack();
					dialog.open();
					if (dialog.isCancelled())
						return;
				}
			} catch(Exception ex) {   }
		}
		orderView.getTicketView().addTseItem(menuItem, count, ticket);


		TicketItem ticketItem = menuItem.convertToTicketItem(ticket, count);	
		
		if(changeMenuItem)
		{
			menuname = menuname.concat(" "+ticketItem.getName());
			
			if(menuname.length() > 30)
				ticketItem.setName(menuname.substring(0, 30));
			else
				ticketItem.setName(menuname);			  
			 
		}
		
		orderView.getTicketView().addTicketItem(ticketItem);

		if (menuItem.hasModifiers()) {
			ModifierView modifierView = orderView.getModifierView();
			modifierView.setMenuItem(menuItem, ticketItem);
			orderView.showView(ModifierView.VIEW_NAME);
		}

	}

	@Override
	public void pfandSelected(MenuItem menuItem, Ticket ticket, boolean retour) {
		MenuItemDAO dao = new MenuItemDAO();
		menuItem = dao.initialize(menuItem);
		
		if(menuItem.isChangeprice()!= null && menuItem.isChangeprice())
		{
			Double newTotal = 0.00;
			NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
			dialog.setFloatingPoint(true);
			dialog.setTitle("Geben Sie die Gesamt: "+ NumberUtil.formatNumber(menuItem.getPrice()) + " €");
			dialog.setDialogTitle("Aendern Preis");
			dialog.pack();
			dialog.open();
			
			if (dialog.isCanceled()) {
				return ;
			}
			newTotal = dialog.getValue();
			if(newTotal > 0.00)
				menuItem.setPrice(newTotal);
			else
			if(menuItem.isPfand()) {
				if(newTotal>0.00)
					menuItem.setPrice(0-newTotal);
			}

		}	
		if(menuItem.getWeightgrams() != null && menuItem.getWeightgrams().compareTo(0.00) > 0 )
		{
			WeightDialog dialog = new WeightDialog(menuItem.getWeightgrams(), menuItem.getPrice(), menuItem.getName(),menuItem.getItemId());
			dialog.setPreferredSize(new Dimension(750,600));
			dialog.setTitle("Gewicht");
			dialog.pack();
			dialog.open();

			if(dialog.isCanceled())
				return;
			else if(dialog.getNetPrice().compareTo(0.00) != 0)
			{
				menuItem.setPrice(dialog.getNetPrice());
				menuItem.setName(menuItem.getName() + "-"+ dialog.getGivenWeight().intValue()+ "g");
				Double weight = (Double.valueOf(dialog.getGivenWeight().intValue()));
				menuItem.setWeightgrams(weight);
			}
		}

		
		int count = 1;
		if(TerminalConfig.isTakeItemCount()) {
			try {			
				count = Integer.parseInt(orderView.getTicketView().getItemZahl());				
			}catch(Exception ex) {

			}
		}
		if(!retour)
		orderView.getTicketView().addTseItem(menuItem, count, ticket);
		if(retour)
			ticket.setRefunded(true);
		
		TicketItem ticketItem = menuItem.convertToTicketItem(ticket, count,  retour);
		if(retour)
			orderView.getTicketView().addTseItem(ticketItem, ticket);
		System.out.println(ticketItem.getWeightgrams()+" Weight ");
		orderView.getTicketView().addTicketItem(ticketItem);

	}
}
