package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.CardReportView;
import com.floreantpos.ui.dialog.CardReportDialog;


public class CardPaymentAction extends AbstractAction {

	public CardPaymentAction(String name) throws Exception {
		super(name);
		init();
	}
	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();
		
		CardReportView reportView = null;
		int index = tabbedPane.indexOfTab("Karten Abscluss");
		if (index == -1) {
			try {
				reportView = new CardReportView();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			tabbedPane.addTab("Karten Abscluss", reportView);
		}
		else {
			reportView = (CardReportView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}
	public void init() throws Exception
	{
		CardReportView reportView = null;
		reportView = new CardReportView();
		CardReportDialog dialog = new CardReportDialog(reportView);
		reportView.setDialog(dialog);
		dialog.open();
		dialog.pack();
		
	}
}
