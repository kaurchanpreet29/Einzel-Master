package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class NumberSelectionDialog2 extends POSDialog implements ActionListener,WindowListener {
	private int defaultValue;
	
	private TitlePanel titlePanel;
	private JTextField tfNumber;

	private boolean floatingPoint;
	private PosButton posButton_1;
	
	public NumberSelectionDialog2() {
		this(Application.getPosWindow());
	}

	public NumberSelectionDialog2(Frame parent) {
		super(parent, true);
		init();
	}
	
	public NumberSelectionDialog2(Dialog parent) {
		super(parent, true);
		
		init();
		this.addWindowListener(this);
	}

	private void init() {
		setResizable(false);
		
		Container contentPane = getContentPane();

		MigLayout layout = new MigLayout("fillx", "[60px,fill][60px,fill][60px,fill]", "[][][][][]");
		contentPane.setLayout(layout);

		titlePanel = new TitlePanel();
		contentPane.add(titlePanel, "spanx ,growy,height 60,wrap");
		titlePanel.setBackground(new Color(209,222,235));
		contentPane.setBackground(new Color(209,222,235));
		tfNumber = new JTextField();
		
		tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
		//tfNumber.setEditable(false);
		tfNumber.setFocusable(true);
		tfNumber.requestFocus();
		tfNumber.setBackground(Color.WHITE);
		//tfNumber.setHorizontalAlignment(JTextField.RIGHT);
		contentPane.add(tfNumber, "span 2, grow");

		PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
		posButton.setFocusable(false);
		posButton.setMinimumSize(new Dimension(25, 23));
		posButton.setFont(new Font("Times New Roman", Font.BOLD, 28));
		posButton.addActionListener(this);
		contentPane.add(posButton, "growy,height 55,wrap");

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { ".", "0", "CLEAR" } };
		String[][] iconNames = new String[][] { { "7_32.png", "8_32.png", "9_32.png" }, { "4_32.png", "5_32.png", "6_32.png" }, { "1_32.png", "2_32.png", "3_32.png" },
				{ "dot_32.png", "0_32.png", "clear_32.png" } };

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				posButton = new PosButton();
				posButton.setFocusable(false);
				ImageIcon icon = IconFactory.getIcon(iconNames[i][j]);
				String buttonText = String.valueOf(numbers[i][j]);

				if (icon == null) {
					posButton.setText(buttonText);
				}
				else {
					posButton.setIcon(icon);
					if (POSConstants.CLEAR.equals(buttonText)) {
						posButton.setText(buttonText);
					}
				}

				posButton.setActionCommand(buttonText);
				posButton.addActionListener(this);
				String constraints = "grow, height 55";
				if (j == numbers[i].length - 1) {
					constraints += ", wrap";
				}
				contentPane.add(posButton, constraints);
			}
		}
		contentPane.add(new JSeparator(), "newline,spanx ,growy,gapy 20");

		posButton = new PosButton(POSConstants.OK);
		posButton.setFocusable(false);
		posButton.setBackground(new Color(102,255,102));
		posButton.addActionListener(this);
		contentPane.add(posButton, "skip 1,grow");

		posButton_1 = new PosButton(POSConstants.CANCEL);
		posButton_1.setBackground(new Color(255,153,153));
		posButton_1.setFocusable(false);
		posButton_1.addActionListener(this);
		contentPane.add(posButton_1, "grow");
	}
	
	private void doOk() {
		if (!validate(tfNumber.getText())) {
			tfNumber.setText("0");
			}
		setCanceled(false);
		dispose();
	}
	
	private void doCancel() {
		setCanceled(true);
		dispose();
	}
	
	private void doClearAll() {
		tfNumber.setText("");
	}
	
	private void doClear() {
		String s = tfNumber.getText();
		if (s.length() > 1) {
			s = s.substring(0, s.length() - 1);
		}
		else {
			s = String.valueOf(defaultValue);
		}
		tfNumber.setText(s);
	}
	
	private void doInsertNumber(String number) {
		String s = tfNumber.getText();
		double d = 0;
		
		s = s + number;
		if (!validate(s)) {
			POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
			return;
		}
		tfNumber.setText(s);
	}
	
	private void doInsertDot() {
		//if (isFloatingPoint() && tfNumber.getText().indexOf('.') < 0) {
			String string = tfNumber.getText() + ".";
			if (!validate(string)) {
				POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
				return;
			}
			tfNumber.setText(string);
		//}
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		
		if(POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if(POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
			doClear();
		}
		else if (actionCommand.toString().compareTo("CLEAR")==0) {
			
			doClearAll();
		}
		else if (actionCommand.equals(".")) {
			doInsertDot();
		}
		else {
			doInsertNumber(actionCommand);
		}

	}

	private boolean validate(String str) {
		if (isFloatingPoint()) {
			try {
				Double.parseDouble(str);
			} catch (Exception x) {
				return false;
			}
		}
		else {
			try {
				Integer.parseInt(str);
			} catch (Exception x) {
				return false;
			}
		}
		return true;
	}

	public void setTitle(String title) {
		titlePanel.setTitle(title);
		
		super.setTitle(title);
	}
	
	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	public double getValue() {
		if(tfNumber.getText().length() > 0)
			return Double.parseDouble(tfNumber.getText());
		else
			return 0;
	}

	public void setValue(double value) {
		if(value == 0) {
			tfNumber.setText("");
		}
		else if (isFloatingPoint()) {
			tfNumber.setText(String.valueOf(value));
		}
		else {
			tfNumber.setText(String.valueOf((int) value));
		}
	}

	public boolean isFloatingPoint() {
		return floatingPoint;
	}

	public void setFloatingPoint(boolean decimalAllowed) {
		this.floatingPoint = decimalAllowed;
	}

	public static void main(String[] args) {
		NumberSelectionDialog2 dialog2 = new NumberSelectionDialog2();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	public int getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
		tfNumber.setText("");
	}

	public static int takeIntInput(String title) {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setTitle(title);
		dialog.pack();
		dialog.open();
		if (dialog.isCanceled()) {
			return 0;
		}
		
		return (int) dialog.getValue();
	}
	
	public static double takeDoubleInput(String title, String dialogTitle, double initialAmount) {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setFloatingPoint(true);
		dialog.setValue(initialAmount);
		dialog.setTitle(title);
		dialog.setDialogTitle(dialogTitle);
		dialog.pack();
		dialog.open();
		
		if (dialog.isCanceled()) {
			return Double.NaN;
		}
		
		return dialog.getValue();
	}
	public static double takeDoubleInput(String title, String dialogTitle) {
		NumberSelectionDialog2 dialog = new NumberSelectionDialog2();
		dialog.setFloatingPoint(true);
		dialog.setTitle(title);
		dialog.setDialogTitle(dialogTitle);
		dialog.pack();
		dialog.open();
		
		if (dialog.isCanceled()) {
			return Double.NaN;
		}
		
		return dialog.getValue();
	}
	public static double show(Component parent, String title, double initialAmount) {
		NumberSelectionDialog2 dialog2 = new NumberSelectionDialog2();
		dialog2.setFloatingPoint(true);
		dialog2.setTitle(title);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setValue(initialAmount);
		dialog2.setVisible(true);
		
		if(dialog2.isCanceled()) {
			return Double.NaN;
		}
		
		return dialog2.getValue();
	}

	public static int take(Component parent, String title) {
		NumberSelectionDialog2 dialog2 = new NumberSelectionDialog2();
		dialog2.setTitle(title);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setVisible(true);
		
		return (int)dialog2.getValue();
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
		System.out.println("Canceling");
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
