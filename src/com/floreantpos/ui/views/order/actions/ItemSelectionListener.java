package com.floreantpos.ui.views.order.actions;

import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.Ticket;

public interface ItemSelectionListener {
	void itemSelected(MenuItem menuItem);
	void itemSelected(MenuItem menuItem, Ticket ticket);
	void itemSelectionFinished(MenuGroup parent);
	void pfandSelected(MenuItem menuItem, Ticket ticket, boolean retour);
}
