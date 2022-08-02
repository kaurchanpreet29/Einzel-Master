package com.floreantpos.bo.actions;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.ui.ConfigurationDialog;
import com.floreantpos.config.ui.OnlineConfigurationDialog;
import com.floreantpos.config.ui.OnlineConfigurationDialog2;

public class ConfigureOnlineRestaurantAction extends AbstractAction {

	int shop = 0;
	public ConfigureOnlineRestaurantAction() {
		super(com.floreantpos.POSConstants.CONFIGURATION);
	}

	public ConfigureOnlineRestaurantAction(String name, int shop) {
		super(name);
		this.shop = shop;
		init();
	}

	public ConfigureOnlineRestaurantAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		
		if(shop == 1)
		{
			OnlineConfigurationDialog dialog = null;
			try {
				dialog = new OnlineConfigurationDialog(BackOfficeWindow.getInstance());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dialog.pack();
			dialog.open();
		}
		else if(shop == 2)
		{
			OnlineConfigurationDialog2 dialog = null;
			try {
				dialog = new OnlineConfigurationDialog2(BackOfficeWindow.getInstance());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dialog.pack();
			dialog.open();
		}
	}
	public void init() {
		
		if(shop == 1)
		{
			OnlineConfigurationDialog dialog = null;
			try {
				dialog = new OnlineConfigurationDialog(BackOfficeWindow.getInstance());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dialog.setPreferredSize(new Dimension(800,600));
			dialog.pack();
			dialog.open();
		}
		else if(shop == 2)
		{
			OnlineConfigurationDialog2 dialog = null;
			try {
				dialog = new OnlineConfigurationDialog2(BackOfficeWindow.getInstance());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			dialog.setPreferredSize(new Dimension(800,600));
			dialog.pack();
			dialog.open();
		}
		
	}

}
