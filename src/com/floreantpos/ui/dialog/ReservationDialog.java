package com.floreantpos.ui.dialog;

import java.awt.Color;

import com.floreantpos.ui.views.ReservationView;

public class ReservationDialog extends POSDialog{
	ReservationView view;
	public ReservationDialog(ReservationView view) {
		this.view = view;
		setTitle("TERMINE");
		setBackground(new Color(209,222,235));
		createUI();
	}
	private void createUI() {
		add(view);
	}
}