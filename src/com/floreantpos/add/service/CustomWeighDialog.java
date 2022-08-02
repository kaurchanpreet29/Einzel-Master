package com.floreantpos.add.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import com.floreantpos.main.Application;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.util.NumberUtil;

import bsh.StringUtil;



public class CustomWeighDialog extends JDialog {

	public CustomWeighDialog() {
		super(Application.getPosWindow(), true);
		initComponents();
	}
	public boolean canceled = false;
	public boolean isCanceled() {
		return canceled;
	}
	public JCheckBox sons19 = new JCheckBox("  19% ");
	public void initComponents()
	{
		this.setPreferredSize(new Dimension(800,500));
		this.setTitle("Khana Kassensysteme - Custom Weight Calculator");
		JPanel textPanel = new JPanel();
		resultJText = new JTextField(18);
		resultJText.setDocument(new FixedLengthDocument(18));
		resultJText.setPreferredSize(new Dimension(300,50));
		resultJText.setFont(new Font("Times New Roman", Font.BOLD, 24));
		value = 0.0;
		sons19.setSelected(false);
		sons19.setFocusable(false);		
		JButton btnEnter = new JButton(" OK ");
		btnEnter.setBackground(new Color(102,255,102));
		btnEnter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(resultJText.getText().contains("*")){
						tempNumbers1 = Double.valueOf(resultJText.getText().substring(0, resultJText.getText().indexOf("*")).replace(',','.'));
						tempNumbers2 = Double.valueOf(resultJText.getText().substring(resultJText.getText().indexOf("*")+1, resultJText.getText().length()).replace(',','.'));
						value = NumberUtil.roundToTwoDigit(tempNumbers1 * tempNumbers2);
						if(value>500) {
							int option = JOptionPane.showOptionDialog(null, value+" € ist hoch dann 500 €", "Sind si Sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);
							if(option != JOptionPane.YES_OPTION) {
								return;
							}
						}
					}else {						
						value = Double.valueOf(resultJText.getText().replace(',','.'));
						if(value>500) {
							int option = JOptionPane.showOptionDialog(null, value+" € ist hoch dann 500 €", "Sind si Sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null,null);
							if(option != JOptionPane.YES_OPTION) {
								return;
							}
						}
					}
					dispose();
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "Multiple Commas Error!!!");
					resultJText.setText("");
				}
				
			}

		});
		btnEnter.setFocusable(false);
		btnEnter.setFont(new Font("Tahoma", Font.BOLD, 30));

		JButton btnClear = new JButton("    C    ");
		btnClear.setBackground(new Color(255,153,153));
		btnClear.setFocusable(false);
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnClear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				resultJText.setText("");
				tempNumbers1 = 0;
				tempNumbers2 = 0;
				function = -1;				
			}
		});
		JButton btnCancel = new JButton("ABRECHEN");
		btnCancel.setBackground(new Color(255,153,153));
		btnCancel.setFocusable(false);
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 30));
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				canceled = true;
				dispose();	
			}
		});

		textPanel.setLayout(new FlowLayout());
		textPanel.add(resultJText);
		textPanel.add(sons19);
		textPanel.add(btnEnter);
		textPanel.add(btnClear);
		textPanel.add(btnCancel);
		textPanel.setBackground(new Color(209,222,235)); 
		keypadPanel = new javax.swing.JPanel();
		posButton7 = new com.floreantpos.swing.PosButton();
		setColorButton(posButton7);
		posButton7.setFocusable(false);
		posButton8 = new JButton();
		setColorButton(posButton8);
		posButton8.setFocusable(false);
		posButton9 = new JButton();
		setColorButton(posButton9);
		posButton9.setFocusable(false);
		posButton4 = new JButton();
		setColorButton(posButton4);
		posButton4.setFocusable(false);
		posButton5 = new JButton();
		setColorButton(posButton5);
		posButton5.setFocusable(false);
		posButton6 = new JButton();
		setColorButton(posButton6);
		posButton6.setFocusable(false);
		posButton1 = new JButton();
		setColorButton(posButton1);
		posButton1.setFocusable(false);
		posButton2 = new JButton();
		setColorButton(posButton2);
		posButton2.setFocusable(false);
		posButton3 = new JButton();
		setColorButton(posButton3);
		posButton3.setFocusable(false);
		posButton0 = new JButton();
		setColorButton(posButton0);
		posButton0.setFocusable(false);

		posButtonMultiply = new JButton();
		posButtonMultiply.setFocusable(false);

		keypadPanel.setLayout(new java.awt.GridLayout(4, 4, 1, 1));
		keypadPanel.setPreferredSize(new Dimension(200,350));
		posButton7.setAction(goAction);
		posButton7.setIcon(com.floreantpos.IconFactory.getIcon("7_32.png"));
		posButton7.setActionCommand("7");
		keypadPanel.add(posButton7);

		posButton8.setAction(goAction);
		posButton8.setIcon(com.floreantpos.IconFactory.getIcon("8_32.png"));
		posButton8.setActionCommand("8");
		keypadPanel.add(posButton8);

		posButton9.setAction(goAction);
		posButton9.setIcon(com.floreantpos.IconFactory.getIcon("9_32.png"));
		posButton9.setActionCommand("9");
		keypadPanel.add(posButton9);	


		posButton4.setAction(goAction);
		posButton4.setIcon(com.floreantpos.IconFactory.getIcon("4_32.png"));
		posButton4.setActionCommand("4");
		keypadPanel.add(posButton4);

		posButton5.setAction(goAction);
		posButton5.setIcon(com.floreantpos.IconFactory.getIcon("5_32.png"));
		posButton5.setActionCommand("5");
		keypadPanel.add(posButton5);

		posButton6.setAction(goAction);
		posButton6.setIcon(com.floreantpos.IconFactory.getIcon("6_32.png"));
		posButton6.setActionCommand("6");
		keypadPanel.add(posButton6);


		posButton1.setAction(goAction);
		posButton1.setIcon(com.floreantpos.IconFactory.getIcon("1_32.png"));
		posButton1.setActionCommand("1");
		keypadPanel.add(posButton1);

		posButton2.setAction(goAction);
		posButton2.setIcon(com.floreantpos.IconFactory.getIcon("2_32.png"));
		posButton2.setActionCommand("2");
		keypadPanel.add(posButton2);

		posButton3.setAction(goAction);
		posButton3.setIcon(com.floreantpos.IconFactory.getIcon("3_32.png"));
		posButton3.setActionCommand("3");
		keypadPanel.add(posButton3);		

		btnDot = new JButton();
		setColorButton(btnDot);
		btnDot.setFocusable(false);
		btnDot.setAction(goAction);
		btnDot.setActionCommand(",");
		btnDot.setIcon(com.floreantpos.IconFactory.getIcon("comma_32.png"));
		keypadPanel.add(btnDot);

		posButton0.setAction(goAction);
		posButton0.setIcon(com.floreantpos.IconFactory.getIcon("0_32.png"));
		posButton0.setActionCommand("0");
		keypadPanel.add(posButton0);
		setLayout(new BorderLayout(0, 0));

		posButtonMultiply.setAction(goAction);
		posButtonMultiply.setActionCommand("*");
		posButtonMultiply.setIcon(com.floreantpos.IconFactory.getIcon("delete_32.png"));
		setColorFnButton(posButtonMultiply);
		keypadPanel.add(posButtonMultiply);		

		keypadPanel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		add(textPanel, BorderLayout.NORTH);
		add(keypadPanel, BorderLayout.CENTER);

	}
	public Double value;

	public Double getValue() {
		return value;
	}
	public void setColorButton(JButton button)
	{
		button.setBackground(new Color(204,204,255));
	}
	public void setColorFnButton(JButton button)
	{
		button.setBackground(new Color(204,204,255));
	}
	Action goAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			JTextComponent focusedTextComponent = null;

			if (!(focusOwner instanceof JTextComponent)) {
				return;
			}

			focusedTextComponent = (JTextComponent) focusOwner;

			String command = e.getActionCommand();

			if (command.equals("CLEAR")) {
				focusedTextComponent.setText("");
			}else {
				focusedTextComponent.setText(focusedTextComponent.getText() + command);				
			}
		}
	};
	private double tempNumbers1 = 0.00;
	private double tempNumbers2 = 0.00;

	private byte function = -1;
	private javax.swing.JPanel keypadPanel;
	private JButton posButton0;
	private JButton posButton1;
	private JButton posButton10;
	private JButton posButton2;
	private JButton posButton3;
	private JButton posButton4;
	private JButton posButton5;
	private JButton posButton6;
	private JButton posButton7;
	private JButton posButton8;
	private JButton posButton9;
	private JButton posButtonPlus;
	private JButton posButtonMinus;
	private JButton posButtonMultiply;
	private JButton posButtonDivide;
	private JButton btnDot;
	private JTextField resultJText;
}