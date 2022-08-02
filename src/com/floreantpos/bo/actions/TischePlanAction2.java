package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.config.ui.TablePlanDeisgn;
import com.floreantpos.config.ui.TablePlanDesign2;
import com.floreantpos.model.KundenDB;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TableSelectionDesign;

public class TischePlanAction2 extends AbstractAction{

	public TischePlanAction2(String name) throws IOException {
		super(name);
		init();
	}
	
	public void init() throws IOException {
		TablePlanDesign2 dialog = new TablePlanDesign2();
		dialog.pack();
		dialog.open();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
