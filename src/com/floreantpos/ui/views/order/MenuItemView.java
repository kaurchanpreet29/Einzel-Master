/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.floreantpos.PosException;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.views.order.actions.ItemSelectionListener;
import com.floreantpos.util.NumberUtil;

/**
 * 
 * @author MShahriar
 */
public class MenuItemView extends SelectionView {
	public final static String VIEW_NAME = "ITEM_VIEW";

	private Vector<ItemSelectionListener> listenerList = new Vector<ItemSelectionListener>();
	private Ticket currentTicket;
	private MenuGroup menuGroup;

	/** Creates new form GroupView */
	public MenuItemView() {
		super(com.floreantpos.POSConstants.ITEMS, TerminalConfig
				.getTouchScreenItemButtonWidth(), TerminalConfig
				.getTouchScreenItemButtonHeight());
		setBackground(new Color(35, 35, 36));
		setBackEnable(false);

	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

	public Ticket getCurrentTicket() {
		return this.currentTicket;
	}

	public void setCurrentTicket(Ticket CurrentTicket) {
		this.currentTicket = CurrentTicket;
	}

	public void setMenuGroup(MenuGroup menuGroup) {
		this.menuGroup = menuGroup;

		reset();

		if (menuGroup == null) {
			return;
		}

		MenuItemDAO dao = new MenuItemDAO();
		try {
			List<MenuItem> items = dao.findByParent(menuGroup, false);
			setBackEnable(items.size() > 0);
			if(TerminalConfig.isItemSorting()) {
				try {
				Collections.sort(items, new ItemIdComparator());
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(TerminalConfig.isItemPriceSorting()) {
				try {
					Collections.sort(items, new ItemPriceComparator());
					}catch (Exception e) {
						// TODO: handle exception
					}
			}
			setItems(items);
			revalidate();
			repaint();
		} catch (PosException e) {
			e.printStackTrace();
		}
	}


	public static class ItemIdComparator implements Comparator {
		public ItemIdComparator() {
		}
		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof MenuItem) || !(o2 instanceof MenuItem))
				throw new ClassCastException();

			MenuItem e1 = (MenuItem) o1;
			MenuItem e2 = (MenuItem) o2;

			if (e1.getItemId() == null)
				return 0;
			try {
			return Integer.parseInt(e1.getItemId()) - Integer.parseInt(e2.getItemId());
			}catch(Exception ex){
				
			}
			return 0;
		}
	}	
	
	public static class ItemPriceComparator implements Comparator {
		public ItemPriceComparator() {
		}

		@Override
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof MenuItem) || !(o2 instanceof MenuItem))
				throw new ClassCastException();

			MenuItem e1 = (MenuItem) o1;
			MenuItem e2 = (MenuItem) o2;

			if (e1.getPrice() == null)
				return 0;
			return e1.getPrice().compareTo(e2.getPrice());
		}
	}



	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuItem menuItem = (MenuItem) item;
		ItemButton itemButton = new ItemButton(menuItem);
		itemButton.setFont(new Font(null, Font.BOLD, TerminalConfig
				.getTouchScreenFontSize()));

		return itemButton;
	}

	public void addItemSelectionListener(ItemSelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeItemSelectionListener(ItemSelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireItemSelected(MenuItem foodItem) {
		for (ItemSelectionListener listener : listenerList) {
//			listener.itemSelected(foodItem, currentTicket);
//			if (foodItem.getSubitemid() != null) {
//				List<MenuItem> itemlist = MenuItemDAO.getInstance().findById(true,
//						foodItem.getSubitemid());
//				for (Iterator<MenuItem> itr = itemlist.iterator(); itr.hasNext();) {
//					MenuItem item = itr.next();
//					if (item == null)
//						continue;
//				listener.itemSelected(item, currentTicket);				
//					break;
//				}
//			}
			
				if(foodItem.isPfand())
					listener.pfandSelected(foodItem, currentTicket, true);
				else {
					listener.itemSelected(foodItem, currentTicket);		
					 
					if (foodItem.getSubitemid() != null) {
						List<MenuItem> itemlist = MenuItemDAO.getInstance().findById(true, foodItem.getSubitemid());
						for (Iterator<MenuItem> itr = itemlist.iterator(); itr.hasNext();) {
							MenuItem item = itr.next();
							if (item == null)
								continue;
							listener.pfandSelected(item, currentTicket, false);
							break;
						}
					}
				}
				
			
			OrderView.getInstance().getTicketView().resetTextField();
		}
	}

	private void fireBackFromItemSelected() {
		for (ItemSelectionListener listener : listenerList) {
			listener.itemSelectionFinished(menuGroup);
		}
	}

	private class ItemButton extends PosButton implements ActionListener {
		private final int BUTTON_SIZE = TerminalConfig
				.getTouchScreenItemButtonHeight();
		MenuItem foodItem;
		com.floreantpos.swing.TransparentPanel panel;
		com.floreantpos.swing.TransparentPanel pricePanel;
		com.floreantpos.swing.TransparentPanel weightPanel;
		com.floreantpos.swing.TransparentPanel namePanel;
		JLabel lblId;
		JLabel lblPrice;
		JLabel lblText;
		JLabel lblWeight;
		JLabel lblChangePrice;

		ItemButton(MenuItem foodItem) {
			Restaurant rest = RestaurantDAO.getRestaurant();	
			panel = new TransparentPanel();
			panel.setLayout(new BorderLayout());

			pricePanel = new TransparentPanel();;
			pricePanel.setLayout(new BorderLayout());
			lblId = new JLabel();
			lblPrice = new JLabel();
			
			int red = 2;
			int green = 48;
			int blue = 20;
			boolean change = false;
			if (foodItem.getRed() != null && foodItem.getRed() > 0) {
				red = foodItem.getRed();
				change = true;
			}
			if (foodItem.getGreen() != null && foodItem.getGreen() > 0) {
				green = foodItem.getGreen();
				change = true;
			}
			if (foodItem.getBlue() != null && foodItem.getBlue() > 0) {
				blue = foodItem.getBlue();
				change = true;
			}
			
			int fred = foodItem.getFred();		
			int fgreen = foodItem.getFgreen();	
			int	fblue = foodItem.getFblue();		
			
			if(fred==255&&fgreen==255&&fblue==255) {
				fred = rest.getItemTexColor().getRed();
				fgreen = rest.getItemTexColor().getGreen();
				fblue = rest.getItemTexColor().getBlue();
			}
			
			if(!change) {
				red = rest.getBackButtoColor().getRed();
				green = rest.getBackButtoColor().getGreen();
				blue = rest.getBackButtoColor().getBlue();
			}

			weightPanel = new TransparentPanel();;
			weightPanel.setLayout(new BorderLayout());

			lblWeight = new JLabel();
		
			//weightPanel.add(lblWeight, BorderLayout.EAST);

			lblChangePrice = new JLabel();      

			if(!TerminalConfig.isHideItemPrice()) {
				pricePanel.add(lblPrice, BorderLayout.EAST);
			}
			pricePanel.add(lblWeight, BorderLayout.WEST);

			weightPanel.add(lblChangePrice, BorderLayout.WEST);
			namePanel = new TransparentPanel();;
			namePanel.setLayout(new BorderLayout());

			lblText = new JLabel();
			
			weightPanel.add(lblText, BorderLayout.CENTER);

			if(!TerminalConfig.isHideItemId() || !TerminalConfig.isHideItemPrice()) {
				panel.add(pricePanel, BorderLayout.NORTH);
			}
			panel.add(namePanel, BorderLayout.CENTER);
			panel.add(weightPanel, BorderLayout.SOUTH);
			
			
			lblText.setForeground(new Color(fred, fgreen, fblue));
			lblWeight.setForeground(new Color(fred, fgreen, fblue));
			lblId.setForeground(new Color(fred, fgreen, fblue));
			lblPrice.setForeground(new Color(fred, fgreen, fblue));
			
			
			namePanel.setBackground(new Color(red, green, blue));
			pricePanel.setBackground(new Color(red, green, blue));
			panel.setBackground(new Color(red, green, blue));
			setBackground(new Color(red, green, blue));
			weightPanel.setBackground(new Color(red, green, blue));

			this.foodItem = foodItem;
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setFont(new Font("Tahoma", Font.BOLD, 16));

			String itemName = foodItem.getName();
			lblText.setFont(new Font("Tahoma", Font.BOLD, 16));


			lblText.setHorizontalAlignment(SwingConstants.CENTER);
			lblId.setText(" " + foodItem.getItemId());
			lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));

			lblPrice.setText(NumberUtil.formatNumber(foodItem.getPrice()) + " € ");
			lblPrice.setFont(new Font("Tahoma", Font.BOLD, 16));
			if (foodItem.getWeightgrams() != null
					&& foodItem.getWeightgrams().compareTo(0.00) != 0) {
				lblWeight.setText(foodItem.getWeightgrams().intValue() + " g ");
				lblWeight.setFont(new Font("Tahoma", Font.BOLD, 16));
			}
			if (foodItem.isChangeprice() != null && foodItem.isChangeprice() == true) {
				lblChangePrice.setFont(new Font("Tahoma", Font.BOLD, 13));
				lblChangePrice.setText("*P");
				lblChangePrice.setForeground(Color.WHITE);
			}
			if(foodItem.getImage() != null) {    	  
				ImageIcon imageIcon = new ImageIcon(new ImageIcon(foodItem.getImage()).getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH));
				JLabel picLabel = new JLabel(imageIcon);
				namePanel.add(picLabel);
				if(itemName.length() > 13) {
					lblText.setFont(new Font("Tahoma", Font.BOLD, 14));
					lblText.setText(itemName);
				}  
			} else {
				namePanel.add(lblText);
				lblText.setText("<html><body><center>" + itemName 
						+ "</center></body></html>");
			}
			if(!foodItem.isShowImageOnly())
				add(panel);
			else {
				ImageIcon imageIcon = new ImageIcon(new ImageIcon(foodItem.getImage()).getImage().getScaledInstance(BUTTON_SIZE, BUTTON_SIZE, Image.SCALE_SMOOTH));
				JLabel picLabel = new JLabel(imageIcon);
				add(picLabel);
			}

			setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			fireItemSelected(foodItem);
		}
	}

	@Override
	public void doGoBack() {
		fireBackFromItemSelected();
	}
}
