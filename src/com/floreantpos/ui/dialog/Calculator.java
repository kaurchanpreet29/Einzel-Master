package com.floreantpos.ui.dialog;

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
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import com.floreantpos.main.Application;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.util.NumberUtil;



public class Calculator extends JDialog {
	
	public Calculator() {
		super(Application.getPosWindow(), true);
		initComponents();
	}
	public void initComponents()
	{
		this.setPreferredSize(new Dimension(800,500));
		this.setTitle("Khana Kassensysteme - Calculator");
		JPanel textPanel = new JPanel();
		resultJText = new JTextField(18);
		resultJText.setDocument(new FixedLengthDocument(18));
		resultJText.setPreferredSize(new Dimension(300,50));
		resultJText.setFont(new Font("Times New Roman", Font.BOLD, 24));
	
		resultJText.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10)
				{
					
					try{
						tempNumbers2 = Double.valueOf(resultJText.getText().replace(',','.'));
					}
					catch(Exception e){tempNumbers2 = 0;}
					
					    if (function == 0) {
					    	
					    	Double value = ((double)tempNumbers1 /  (double)tempNumbers2);
					    	resultJText.setText(value.toString());
				        } else if (function == 1) {
				        	Double value = tempNumbers1 * tempNumbers2;
				            resultJText.setText(value+"");
				        } else if (function == 2) {
				        	Double value = tempNumbers2 + tempNumbers1;
				            resultJText.setText(value+"");
				        } else if (function == 3) {
				        	Double value = tempNumbers1 - tempNumbers2;
				            resultJText.setText(value+"");
				        } else {
				            resultJText.setText(String.valueOf(tempNumbers1));
				        }
					    
					   tempNumbers1 = Double.valueOf(resultJText.getText().replace(',', '.'));
				}
				else if(resultJText.getText().contains("*")){
					
					int index = resultJText.getText().indexOf('*');
					String value = resultJText.getText().substring(0, index);
					value = value.replace(',','.');
					Double number = Double.parseDouble(value);
					if (tempNumbers1 == 0) {
			            tempNumbers1 = number;
			            resultJText.setText("");
			        } else {
			            tempNumbers2 = number;
			            resultJText.setText("");
			        }
			        function = 1;
				}
				else if(resultJText.getText().contains("+"))
				{
					int index = resultJText.getText().indexOf('+');
					String value = resultJText.getText().substring(0, index);
					value = value.replace(',','.');
					if (tempNumbers1 == 0) {
						Double number;
						try{
						 number = Double.parseDouble(value);
						}
						catch(Exception e){number = 0.00;}
			            tempNumbers1 = number;
			            resultJText.setText("");
			        } else {
			        	Double number;
						try{
						 number = Double.parseDouble(value);
						}
						catch(Exception e){number = 0.00;}
			            tempNumbers2 = number;
			            resultJText.setText("");
			        }
			        function = 2;
				}
				else if(resultJText.getText().contains("-"))
				{
					int index = resultJText.getText().indexOf('-');
					String value = resultJText.getText().substring(0, index);
					value = value.replace(',','.');
					 if (tempNumbers1 == 0) {
						 Double number;
							try{
							 number = Double.parseDouble(value);
							}
							catch(Exception e){number = 0.00;}
				            tempNumbers1 = number;
				            resultJText.setText("");
				        } else {
				        	Double number;
							try{
							 number = Double.parseDouble(value);
							}
							catch(Exception e){number = 0.00;}
				            tempNumbers2 = number;
				            resultJText.setText("");
				        }
				        function = 3;
				}
				else if(resultJText.getText().contains("/"))
				{
					int index = resultJText.getText().indexOf('/');
					String value = resultJText.getText().substring(0, index);
					value = value.replace(',','.');
					  if (tempNumbers1 == 0) {
						  Double number;
							try{
							 number = Double.parseDouble(value);
							}
							catch(Exception e){number = 0.00;}
				            tempNumbers1 = number;
				            resultJText.setText("");
				        } else {
				        	Double number;
							try{
							 number = Double.parseDouble(value);
							}
							catch(Exception e){number = 0.00;}
				            tempNumbers2 = number;
				            resultJText.setText("");
				        }
				        function = 0;
				}
				
		    }

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		JButton btnEnter = new JButton("OK");
		btnEnter.setBackground(new Color(102,255,102));
		btnEnter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			
				tempNumbers2 = Double.valueOf(resultJText.getText().replace(',','.'));
				
				    if (function == 0) {
				    	Double value = Double.valueOf((((tempNumbers1 / tempNumbers2) * 100)) / 100);
			            resultJText.setText(value+"");
			        } else if (function == 1) {
			        	Double value = tempNumbers1 * tempNumbers2;
			            resultJText.setText(value+"");
			        } else if (function == 2) {
			        	Double value = tempNumbers2 + tempNumbers1;
			            resultJText.setText(value+"");
			        } else if (function == 3) {
			        	Double value = tempNumbers1 - tempNumbers2;
			            resultJText.setText(value+"");
			        } else {
			            resultJText.setText(String.valueOf(tempNumbers1));
			        }
				    
				
				    tempNumbers1 = Double.valueOf(resultJText.getText().replace(',','.'));
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
				 dispose();	
			}
	   });
		
		textPanel.setLayout(new FlowLayout());
		textPanel.add(resultJText);
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

		posButtonPlus = new JButton();
		posButtonPlus.setFocusable(false);
		posButtonMinus = new JButton();
		posButtonMinus.setFocusable(false);
		posButtonMultiply = new JButton();
		posButtonMultiply.setFocusable(false);
		posButtonDivide = new JButton();
		posButtonDivide.setFocusable(false);
		
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

		posButtonDivide.setAction(goAction);
		posButtonDivide.setFont(new Font("Tahoma", Font.PLAIN, 45));
		posButtonDivide.setForeground(new Color(102,178,255));
		posButtonDivide.setText("/");
		posButtonDivide.setActionCommand("Divide");
		setColorFnButton(posButtonDivide);
		keypadPanel.add(posButtonDivide);
		

		
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

		posButtonMultiply.setAction(goAction);
		posButtonMultiply.setIcon(com.floreantpos.IconFactory.getIcon("delete_32.png"));
		posButtonMultiply.setActionCommand("Multiply");
		setColorFnButton(posButtonMultiply);
		keypadPanel.add(posButtonMultiply);
		
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

		posButtonMinus.setAction(goAction);
		posButtonMinus.setIcon(com.floreantpos.IconFactory.getIcon("minus_32.png"));
		posButtonMinus.setActionCommand("Minus");
		setColorFnButton(posButtonMinus);
		keypadPanel.add(posButtonMinus);
		
		
		btnDot = new JButton();
		setColorButton(btnDot);
		btnDot.setFocusable(false);
		btnDot.setAction(goAction);
		btnDot.setActionCommand(".");
		btnDot.setIcon(com.floreantpos.IconFactory.getIcon("dot_32.png"));
		keypadPanel.add(btnDot);

		posButton0.setAction(goAction);
		posButton0.setIcon(com.floreantpos.IconFactory.getIcon("0_32.png"));
		posButton0.setActionCommand("0");
		keypadPanel.add(posButton0);
		setLayout(new BorderLayout(0, 0));
		posButton10 = new com.floreantpos.swing.PosButton();
		posButton10.setFocusable(false);
		keypadPanel.add(posButton10);

		posButton10.setAction(goAction);
		setColorButton(posButton10);
		posButton10.setIcon(com.floreantpos.IconFactory.getIcon("clear_32.png"));
		posButton10.setText("CLEAR");
		
		posButtonPlus.setAction(goAction);
		posButtonPlus.setIcon(com.floreantpos.IconFactory.getIcon("add_user_32.png"));
		posButtonPlus.setActionCommand("Plus");
		setColorFnButton(posButtonPlus);
		keypadPanel.add(posButtonPlus);
		
		keypadPanel.setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		add(textPanel, BorderLayout.NORTH);
		add(keypadPanel, BorderLayout.CENTER);
		
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
			}
			else if (command.equals("Plus")) {
				if (tempNumbers1 == 0) {
		            tempNumbers1 = Double.parseDouble(resultJText.getText());
		            resultJText.setText("");
		        } else {
		            tempNumbers2 = Double.parseDouble(resultJText.getText());
		            resultJText.setText("");
		        }
		        function = 2;
			}
			else if(command.equals("Divide"))
			{
				  if (tempNumbers1 == 0) {
			            tempNumbers1 = Double.parseDouble(resultJText.getText());
			            resultJText.setText("");
			        } else {
			            tempNumbers2 = Double.parseDouble(resultJText.getText());
			            resultJText.setText("");
			        }
			        function = 0;
			}
			else if(command.equals("Minus"))
			{
				 if (tempNumbers1 == 0) {
			            tempNumbers1 = Double.parseDouble(resultJText.getText());
			            resultJText.setText("");
			        } else {
			            tempNumbers2 = Double.parseDouble(resultJText.getText());
			            resultJText.setText("");
			        }
			        function = 3;
			}
			else if(command.equals("Multiply"))
			{
				if (tempNumbers1 == 0) {
		            tempNumbers1 = Double.parseDouble(resultJText.getText());
		            resultJText.setText("");
		        } else {
		            tempNumbers2 = Double.parseDouble(resultJText.getText());
		            resultJText.setText("");
		        }
		        function = 1;
			}
			else {
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