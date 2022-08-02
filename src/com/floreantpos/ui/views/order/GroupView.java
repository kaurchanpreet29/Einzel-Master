/*
 * GroupView.java
 *
 * Created on August 5, 2006, 9:29 PM
 */

package com.floreantpos.ui.views.order;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.MessageDialog;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.views.order.actions.GroupSelectionListener;

/**
 *
 * @author  MShahriar
 */
public class GroupView extends SelectionView {
	private Vector<GroupSelectionListener> listenerList = new Vector<GroupSelectionListener>();
    
	private MenuCategory menuCategory;
	private Ticket currentTicket;
    public static final String VIEW_NAME = "GROUP_VIEW";
    
    /** Creates new form GroupView */
    public GroupView() {
        super(com.floreantpos.POSConstants.GROUPS,TerminalConfig.getTouchScreenItemButtonWidth(), TerminalConfig.getTouchScreenItemButtonHeight());
        setBackground(new Color(35,35,36));
        setBackEnable(false);
    }

	public MenuCategory getMenuCategory() {
		return menuCategory;
	}
	public Ticket getCurrentTicket()
	{
		return this.currentTicket;
	}
	public void setCurrentTicket(Ticket CurrentTicket)
	{
		this.currentTicket = CurrentTicket;
	}

	public void setMenuCategory(MenuCategory foodCategory) {
		this.menuCategory = foodCategory;

		//reset();
		
		if (foodCategory == null) {
			return;
		}
		
		try {
			MenuGroupDAO dao = new MenuGroupDAO();
			List<MenuGroup> groups = dao.findEnabledByParent(foodCategory);
			
			if(groups.size() == 1) {
				MenuGroup menuGroup = groups.get(0);
				fireGroupSelected(menuGroup);
				return;
			}
			setItems(groups);
		} catch (Exception e) {
			MessageDialog.showError(e);
		}
	}
	
	public void addGroupSelectionListener(GroupSelectionListener listener) {
		listenerList.add(listener);
	}
	
	public void removeGroupSelectionListener(GroupSelectionListener listener) {
		listenerList.remove(listener);
	}
	
	private void fireGroupSelected(MenuGroup foodGroup) {
		for (GroupSelectionListener listener : listenerList) {
			listener.groupSelected(foodGroup);
		}
	}
	MenuGroup menuGroup;
	@Override
	protected JButton createItemButton(Object item) {
		MenuGroup menuGroup = (MenuGroup) item;
		GroupButton button = new GroupButton(menuGroup);
		button.setFont(new Font(null, Font.BOLD,TerminalConfig.getTouchScreenFontSize()));
		button.setBackground(new Color(6,54,104));
		button.setForeground(Color.WHITE);
		return button;
	}
	
	
	private class GroupButton extends PosButton implements ActionListener {
		MenuGroup foodGroup;
		private final int BUTTON_SIZE = TerminalConfig.getTouchScreenItemButtonHeight();
		GroupButton(MenuGroup foodGroup) {
			int w = BUTTON_SIZE - 20;
			int h = BUTTON_SIZE - 20;
			
			this.foodGroup = foodGroup;
		
			setText("<html><body><center><br>" + foodGroup.getName() + "</br></center></body></html>");
			if(foodGroup.getImage() != null) {
				ImageIcon imageIcon = new ImageIcon(new ImageIcon(foodGroup.getImage()).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
				setIcon(imageIcon);
				setVerticalTextPosition(SwingConstants.BOTTOM);
			    setHorizontalTextPosition(SwingConstants.CENTER);
				}
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			fireGroupSelected(foodGroup);
		}
	}

	@Override
	public void doGoBack() {
	}
}
