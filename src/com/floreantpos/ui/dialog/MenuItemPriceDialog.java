package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.MenuItemPrice;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.NumberUtil;
public class MenuItemPriceDialog extends POSDialog implements ActionListener,WindowListener {
	private List<MenuItemPrice> menuItemPriceList;
	MenuItem menuItem;
	
	private TitlePanel titlePanel;
	
	public MenuItemPriceDialog(MenuItem item) {
		super(Application.getPosWindow(), true);
		this.menuItem = item;
		menuItemPriceList = item.getMenuitemprice();
		this.addWindowListener(this);
		init();
	}
	
	public MenuItemPriceDialog(Dialog parent) {
		super(parent, true);		
		init();
		this.addWindowListener(this);
	}

	private void init() {
		setResizable(false);
		MigLayout layout = new MigLayout();
		setLayout(layout);

		getContentPane().setBackground(new Color(35,35,36));
		titlePanel = new TitlePanel();
		getContentPane().add(titlePanel, "spanx ,growy,height 60,wrap");
		titlePanel.setBackground(new Color(35,35,36));
		titlePanel.setForeground(Color.WHITE);
		
		int index = 1;
		
		PosButton button = new PosButton();
    if(TerminalConfig.isNormalAuswalEnable()) {
		button.setText("Normal " + " (" + NumberUtil.formatNumber(menuItem.getPrice()) + " EUR)");
		button.setFont(new Font(null, Font.BOLD,14));
		button.addActionListener(this);
		button.setActionCommand("Normal " + "$"+menuItem.getPrice().toString());
		button.setBackground(new Color(2, 64, 2));
		button.setForeground(Color.WHITE);
		
		 
			getContentPane().add(button,"growx");
    }
		
		for (MenuItemPrice price: menuItemPriceList) {
			++index;
			button = new PosButton();
			button.setText(price.getName() + " (" + NumberUtil.formatNumber(price.getPrice()) + " EUR)");
			button.setFont(new Font(null, Font.BOLD,14));
			button.addActionListener(this);
			button.setActionCommand(price.getName() + "$"+price.getPrice().toString());
			button.setBackground(new Color(2, 64, 2));
	    button.setForeground(Color.WHITE);
			
			if (index%4 == 0)
				getContentPane().add(button,"growx,wrap");
			else
				getContentPane().add(button,"growx");
		}
	}
	
	private void doOk() {		
		setCanceled(false);
		dispose();
	}
	
	private void doCancel() {
		setCanceled(true);
		dispose();
	}
	
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		int dollarIndex = actionCommand.indexOf("$");
		String name = actionCommand.substring(0,dollarIndex);
		String itemPrice = actionCommand.substring(dollarIndex+1, actionCommand.length());
		
		if(POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if(POSConstants.OK.equalsIgnoreCase(actionCommand)) {
		}
		menuItem.setName(name);
		menuItem.setPrice(Double.parseDouble(itemPrice.replace(',','.')));
		this.setMenuItem(menuItem);
		doOk();
	}
	public void setTitle(String title) {
		titlePanel.setTitle("Waehlen Sie Gewunschte Artikel aus");
		super.setTitle(title);
	}
	
	public void setMenuItem(MenuItem item)
	{
		menuItem = item;
	}
	
	public MenuItem getMenuItem()
	{
		return menuItem;
	}
	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		doCancel();
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
