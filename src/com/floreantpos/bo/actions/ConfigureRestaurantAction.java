package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.ui.ConfigurationDialog;

public class ConfigureRestaurantAction extends AbstractAction {

	public ConfigureRestaurantAction() {
		super("Geschaeft");
	}

	public ConfigureRestaurantAction(String name) {
		super("Geschaeft");
		init();
	}

	public ConfigureRestaurantAction(String name, Icon icon) {
		super("Geschaeft", icon);
	}

	public void actionPerformed(ActionEvent e) {
		ConfigurationDialog dialog = null;
		try {
			dialog = new ConfigurationDialog(BackOfficeWindow.getInstance());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dialog.pack();
		dialog.open();
	}
	public void init() {
		ConfigurationDialog dialog = null;
		try {
			dialog = new ConfigurationDialog(BackOfficeWindow.getInstance());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		dialog.pack();
		dialog.open();
	}

}
