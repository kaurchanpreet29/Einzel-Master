/*
 * CategoryView.java
 *
 * Created on August 5, 2006, 2:21 AM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.apache.log4j.Logger;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketType;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;

/**
 *
 * @author  MShahriar
 */
public class CategoryView extends SelectionView implements ActionListener {
	private Vector<CategorySelectionListener> listenerList = new Vector<CategorySelectionListener>();
	//private CardLayout cardLayout = new CardLayout();

	private ButtonGroup categoryButtonGroup;
	private Map<String, CategoryButton> buttonMap = new HashMap<String, CategoryButton>();
	private Ticket currentTicket;
	public static final String VIEW_NAME = "CATEGORY_VIEW";
	
	//private int panelCount = 0;
	
	/** Creates new form CategoryView */
	public CategoryView() {
		super(com.floreantpos.POSConstants.CATEGORIES, Integer.parseInt(TerminalConfig.getCategoryWidth()),Integer.parseInt(TerminalConfig.getCategoryHeight()));
		
		setBackVisible(false);
		setBackground(new Color(35,35,36));
		categoryButtonGroup = new ButtonGroup();
	}

	public void initialize() {
		reset();
		MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
		List<MenuCategory> categories = null;
		
		categories = categoryDAO.findAll();
		if(TerminalConfig.isPriceCategory()&&TerminalConfig.isUpdatePriceCategory()||TerminalConfig.isPriceCategoryKunden()&&TerminalConfig.isUpdatePriceCategory()) {
			categories = categories.stream()
					.filter(cat -> cat.getPriceCategory()==OrderView.getInstance().getTicketView().getPriceCategory())
					.collect(Collectors.toList());
		}else if(TerminalConfig.isPriceCategory()||TerminalConfig.isPriceCategoryKunden()) {
			categories = categories.stream()
					.filter(cat -> cat.getPriceCategory()==0)
					.collect(Collectors.toList());
		}
		
		if(categories.size() == 0) return;
		
		setItems(categories);
		CategoryButton categoryButton = (CategoryButton) buttonsPanel.getComponent(0);
		if(categoryButton != null) {
			categoryButton.setSelected(true);
			fireCategorySelected(categoryButton.foodCategory);
		}
	}
	public void reinitialize(boolean flag) {
		reset();
		MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
		List<MenuCategory> categories; 
		MenuCategory priorityCategory = null;
			
		if(flag)
		{
			categories = categoryDAO.findPriority(TicketType.HOME_DELIVERY);
			for(Iterator<MenuCategory> itr = categories.iterator();itr.hasNext();)
			{
				priorityCategory = itr.next();
				break;
			}
			categories.addAll(categoryDAO.findBevegares(TicketType.HOME_DELIVERY));
			categories.addAll(categoryDAO.findNonBevegares(TicketType.HOME_DELIVERY));
			
		}
		else
		{
			categories = categoryDAO.findPriority(TicketType.DINE_IN);
			for(Iterator<MenuCategory> itr = categories.iterator();itr.hasNext();)
			{
				priorityCategory = itr.next();
				break;
			}
			categories.addAll(categoryDAO.findBevegares(TicketType.DINE_IN));
			categories.addAll(categoryDAO.findNonBevegares(TicketType.DINE_IN));
		}
		
		
		if(TerminalConfig.isPriceCategory()&&TerminalConfig.isUpdatePriceCategory()||TerminalConfig.isPriceCategoryKunden()&&TerminalConfig.isUpdatePriceCategory()) {
			categories = categories.stream()
					.filter(cat -> cat.getPriceCategory()==OrderView.getInstance().getTicketView().getPriceCategory())
					.collect(Collectors.toList());
		}else if(TerminalConfig.isPriceCategory()||TerminalConfig.isPriceCategoryKunden()) {
			categories = categories.stream()
					.filter(cat -> cat.getPriceCategory()==0)
					.collect(Collectors.toList());
		}


		if(categories.size() == 0) return;
		
		setItems(categories);
		CategoryButton categoryButton = (CategoryButton) buttonsPanel.getComponent(0);
		if(categoryButton != null) {
			categoryButton.setSelected(true);
			fireCategorySelected(categoryButton.foodCategory);
		}
		if(priorityCategory != null)
			fireCategorySelected(priorityCategory);
		
	}

	public void reOrder() {
    removeAll();
    repaint();
    revalidate();
    add(buttonsPanel, BorderLayout.CENTER);
    repaint();
    revalidate();
  }
	
	public Ticket getCurrentTicket()
	{
		return this.currentTicket;
	}
	public void setCurrentTicket(Ticket CurrentTicket)
	{
		this.currentTicket = CurrentTicket;
	}
	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuCategory menuCategory = (MenuCategory) item;
		
		CategoryButton button = new CategoryButton(this,menuCategory);
		categoryButtonGroup.add(button);
		
		buttonMap.put(String.valueOf(menuCategory.getId()), button);
		
		return button;
	}
	
	public void addCategorySelectionListener(CategorySelectionListener listener) {
		listenerList.add(listener);
	}
	
	public void removeCategorySelectionListener(CategorySelectionListener listener) {
		listenerList.remove(listener);
	}
	
	private void fireCategorySelected(MenuCategory foodCategory) {
		for (CategorySelectionListener listener : listenerList) {
			listener.categorySelected(foodCategory);
		}
	}
	
	public void setSelectedCategory(MenuCategory category) {
		CategoryButton button = buttonMap.get(String.valueOf(category.getId()));
		if(button != null) {
			button.setSelected(true);
		}
	}
	
	private static class CategoryButton extends POSToggleButton {
		MenuCategory foodCategory;
		
		CategoryButton(CategoryView view, MenuCategory foodCategory) {
			this.foodCategory = foodCategory;
			//setText("<html><body><center>" + foodCategory.getName() + "</center></body></html>");
			setText(foodCategory.getName());
			setOpaque(true);
			this.setBackground(Color.WHITE);
			addActionListener(view);
			this.setPreferredSize(this.getPreferredSize());
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		CategoryButton button = (CategoryButton) e.getSource();
		button.setFont(new Font(null, Font.BOLD,TerminalConfig.getTouchScreenFontSize()));
		button.setBackground(new Color(255,128,0));
		if(button.isSelected()) {
			fireCategorySelected(button.foodCategory);
		}
		OrderView.getInstance().getTicketView().focusToScanid();
	}
	
	public void cleanup() {
		Collection<CategoryButton> buttons = buttonMap.values();
		
		for (CategoryButton button : buttons) {
			button.removeActionListener(this);
		}
		buttonMap.clear();
		
		logger.debug("Cleared category buttons");		
	}
	
	private static Logger logger = Logger.getLogger(MenuItemView.class);

	@Override
	public void doGoBack() {
		
	}
}
