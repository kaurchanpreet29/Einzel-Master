package com.floreantpos.util;

import java.awt.Dialog;
import java.awt.Window;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TableDialog;
import com.floreantpos.ui.dialog.TableSelectionDesign;
import com.floreantpos.ui.dialog.TableSelectionDesign2;
import com.floreantpos.ui.dialog.TableSelectionDialog;

public class PosGuiUtil {
	static int selectedTable;
	
	public static List<ShopTable> captureTable(Ticket ticket,boolean change) throws IOException {
		selectedTable = 0;
		boolean plan1 = TerminalConfig.isPlan1();
		boolean plan2 = TerminalConfig.isPlan2();
		if( plan1 == true || plan2 == true)
		{
			if(plan1 == true && plan2 == false)
				return selectPlan1(ticket, change);
			else if (plan1 == false && plan2 == true)
				return selectPlan2(ticket, change);
			else if(plan1 == true && plan2 == true)
			{
				TableDialog selDialog = new TableDialog();
				selDialog.pack();
				selDialog.open();
				if(selDialog.getSelectedPlan() == 1)
				{
					return selectPlan1(ticket, change);
				}
				else if(selDialog.getSelectedPlan() == 2)
				{
					return selectPlan2(ticket, change);
				}
			}
		}
		else
		{
			TableSelectionDialog dialog = new TableSelectionDialog(change);
			dialog.setTicket(ticket);
			dialog.pack();
			dialog.open();

			if (dialog.isCanceled()) {
				return null;
			}
			selectedTable = dialog.getSelectedTable();
			return dialog.getTables();
		}
		return null;
		
		
	}
	
	public static List<ShopTable> selectPlan1(Ticket ticket, boolean change) throws IOException
	{
		TableSelectionDesign dialog = new TableSelectionDesign(change);
		dialog.setTicket(ticket);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return null;
		}
		selectedTable = dialog.getSelectedTable();
		return dialog.getTables();
	}
	
	public static List<ShopTable> selectPlan2(Ticket ticket, boolean change) throws IOException
	{
		TableSelectionDesign2 dialog = new TableSelectionDesign2(change);
		dialog.setTicket(ticket);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return null;
		}
		selectedTable = dialog.getSelectedTable();
		return dialog.getTables();
	}
	
	
	public static int getSelectedTable()
	{
		return selectedTable;
	}
	public static int captureGuestNumber() {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(POSConstants.ENTER_NUMBER_OF_GUEST);
		dialog.pack();
		dialog.open();

		if (dialog.isCanceled()) {
			return -1;
		}

		int numberOfGuests = (int) dialog.getValue();
		if (numberOfGuests == 0) {
			POSMessageDialog.showError(Application.getPosWindow(), POSConstants.GUEST_NUMBER_CANNOT_BE_0);
			return -1;
		}

		return numberOfGuests;
	}

	public static Double parseDouble(JTextComponent textComponent) {
		String text = textComponent.getText();
		try {
			return Double.parseDouble(text);
		} catch (Exception e) {
			return 0.0;
		}
	}

	public static boolean isModalDialogShowing() {
		Window[] windows = Window.getWindows();
		if (windows != null) { // don't rely on current implementation, which at least returns [0].
			for (Window w : windows) {
				if (w.isShowing() && w instanceof Dialog && ((Dialog) w).isModal())
					return true;
			}
		}
		return false;
	}
	
	public static void setColumnWidth(JTable table, int columnNumber, int width) {
		TableColumn column = table.getColumnModel().getColumn(columnNumber);

		column.setPreferredWidth(width);
		column.setWidth(width);
//		column.setMaxWidth(width);
//		column.setMinWidth(width);
	}
}
