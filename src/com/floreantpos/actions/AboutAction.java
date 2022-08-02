package com.floreantpos.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.floreantpos.ui.dialog.AboutDialog;

public class AboutAction extends AbstractAction {
	
	public AboutAction() {
		super("Ueber uns");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		AboutDialog dialog = new AboutDialog();
		dialog.pack();
		dialog.open();
	}

}
