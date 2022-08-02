package com.floreantpos.config.ui;

import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.util.POSUtil;

import net.miginfocom.swing.MigLayout;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;

public class PickColorView extends ConfigurationView {
	
	private Restaurant restaurant;
	private JColorChooser chooser;
	
	public PickColorView() {
		setLayout(new MigLayout("", "[]", "[]"));

		chooser = new JColorChooser();
		add(chooser, "cell 0 0");
	}

	@Override
	public boolean save() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initialize() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "Color Picker";
	}

}
