/*package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.add.service.GetWeightClient;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;
import com.khana.weight.management.ScaleWeight;

import net.miginfocom.swing.MigLayout;

public class NumberSelectionStandardDialg extends POSDialog implements ActionListener{
	
	String name;
	String id;
	PosButton okButton;
	PosButton cancelButton;
	Double netPrice = 0.00;
	PosButton btnClear;
	static JLabel lblDisplay;

	public NumberSelectionStandardDialg(Double weight, Double price,String name,String id)
	{		
		this.name = name;
		this.id = id;
		init();
	}
	public Double getNetPrice()
	{
		return netPrice;
	}
	
	
	public void init()
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
		    private JLabel tfWeight;

			public boolean dispatchKeyEvent(KeyEvent e) {
			       
		        if (e.getID() == KeyEvent.KEY_PRESSED) {
		           if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        	  dispose();		        	  
		           }
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD0){doInsertNumber("0");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD1){doInsertNumber("1");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2){doInsertNumber("2");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3){doInsertNumber("3");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4){doInsertNumber("4");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5){doInsertNumber("5");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6){doInsertNumber("6");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7){doInsertNumber("7");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8){doInsertNumber("8");}
		           else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9){doInsertNumber("9");}
		           else if (e.getKeyCode() == KeyEvent.VK_F5){clearAll();}
		           if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		        	   	 try {
		        	   		if(tfWeight.getText() == null || tfWeight.getText().length() == 0)
							{
								setCanceled(false);
								dispose();
								return false;
							}
		        	   		weight_given = Double.parseDouble(tfWeight.getText());
							netPrice = (weight_given/weight) * price;
							setCanceled(false);
							dispose();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        	  dispose();
			           }
		        }
		        
		        return false;
		}});
		
		

		okButton = new PosButton("OK");
		okButton.setBackground(new Color(102,255,102));
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				weight_given = 0.00;
				try{
					
					setCanceled(false);
					dispose();
				}
				catch(NumberFormatException e)
				{
					POSMessageDialog.showError("Bitte geben Sie einen richtige datei");
					clearAll();
					return;
				}
				
			}
			
		});
		
		cancelButton = new PosButton("ABRECHEN");
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);				
				dispose();
			}
			
		});
		
		btnClear = new PosButton("    CLR   ");
		btnClear.setBackground(new Color(255,153,153));
		btnClear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				clearAll();				
			}
			
		});
		
		serial = new ScaleWeight().getInstance();
		
		btnWeight = new PosButton("   OK  ");
		btnWeight.setFocusable(false);
		btnWeight.setMinimumSize(new Dimension(25, 20));
		btnWeight.setFont(new Font("Times New Roman", Font.BOLD, 28));
		btnWeight.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		tfWeight.setMaximumSize(new Dimension(300, btnWeight.getPreferredSize().height));
		tfWeight.setPreferredSize(new Dimension(300, btnWeight.getPreferredSize().height));
		
		getContentPane().setLayout(new BorderLayout());
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new MigLayout());
		lblKg = new JLabel("");
		lblKg.setBackground(new Color(128,207,226));
		lblKg.setOpaque(true);
		lblKg.setBorder(null);
		lblKg.setFont(new Font("Liberation Sans Narrow", Font.PLAIN, 30));
		lblKg.setMaximumSize(new Dimension(80, btnWeight.getPreferredSize().height));
		lblKg.setPreferredSize(new Dimension(80, btnWeight.getPreferredSize().height));
		//lblKg.setMinimumSize(new Dimension(50, btnWeight.getPreferredSize().height));
		
		JLabel lbl1 = new JLabel();
		lbl1.setText(id + ". "+ name);
		lbl1.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		
		textPanel.add(lbl1);
		lblDisplay = new JLabel();
		lblDisplay.setText("0,00");
		lblDisplay.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblDisplay.setForeground(Color.RED);
		textPanel.add(lblDisplay, "wrap");
		
		tfWeight.setFont(new Font("Times New Roman", Font.BOLD, 42));
		tfWeight.setHorizontalAlignment(SwingConstants.RIGHT);
		textPanel.add(tfWeight,"growx");
		textPanel.add(lblKg, "grow");
		textPanel.add(btnWeight, "growx");
		textPanel.add(btnClear,"growx,wrap");
		textPanel.setBackground(new Color(209,222,235));
		getContentPane().add(textPanel,BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,3));
		
		PosButton posButton7 = new PosButton();
		posButton7.setFocusable(false);
		ImageIcon icon = IconFactory.getIcon("7_32.png");
		panel.add(posButton7);
		posButton7.setIcon(icon);
		posButton7.setActionCommand("7");
		posButton7.addActionListener(this);
		
		PosButton posButton8 = new PosButton();
		posButton8.setFocusable(false);
		ImageIcon icon8 = IconFactory.getIcon("8_32.png");
		panel.add(posButton8);
		posButton8.setIcon(icon8);
		posButton8.setActionCommand("8");
		posButton8.addActionListener(this);
		
		PosButton posButton9 = new PosButton();
		posButton9.setFocusable(false);
		ImageIcon icon9 = IconFactory.getIcon("9_32.png");
		posButton9.setIcon(icon9);
		panel.add(posButton9);
		posButton9.setActionCommand("9");
		posButton9.addActionListener(this);
		
		
		PosButton posButton4 = new PosButton();
		posButton4.setFocusable(false);
		ImageIcon icon4 = IconFactory.getIcon("4_32.png");
		posButton4.setIcon(icon4);
		panel.add(posButton4);
		posButton4.setActionCommand("4");
		posButton4.addActionListener(this);
		
		
		PosButton posButton5 = new PosButton();
		posButton5.setFocusable(false);
		ImageIcon icon5 = IconFactory.getIcon("5_32.png");
		posButton5.setIcon(icon5);
		panel.add(posButton5);
		posButton5.setActionCommand("5");
		posButton5.addActionListener(this);
		
		
		PosButton posButton6 = new PosButton();
		posButton6.setFocusable(false);
		ImageIcon icon6 = IconFactory.getIcon("6_32.png");
		posButton6.setIcon(icon6);
		panel.add(posButton6);
		posButton6.setActionCommand("6");
		posButton6.addActionListener(this);
		
		PosButton posButton1 = new PosButton();
		posButton1.setFocusable(false);
		ImageIcon icon1 = IconFactory.getIcon("1_32.png");
		posButton1.setIcon(icon1);
		panel.add(posButton1);
		posButton1.setActionCommand("1");
		posButton1.addActionListener(this);
		
		PosButton posButton2 = new PosButton();
		posButton2.setFocusable(false);
		ImageIcon icon2 = IconFactory.getIcon("2_32.png");
		posButton2.setIcon(icon2);
		panel.add(posButton2);
		posButton2.setActionCommand("2");
		posButton2.addActionListener(this);
		
		PosButton posButton3 = new PosButton();
		posButton3.setFocusable(false);
		ImageIcon icon3 = IconFactory.getIcon("3_32.png");
		panel.add(posButton3);
		posButton3.setIcon(icon3);
		posButton3.setActionCommand("3");
		posButton3.addActionListener(this);
		
		PosButton posButtonM = new PosButton();
		posButtonM.setFocusable(false);
		panel.add(posButtonM);
		posButtonM.setText("00");
		posButtonM.setFont(new Font(null, Font.BOLD, 50));
		posButtonM.setForeground(new Color(116, 210, 242));
		posButtonM.setActionCommand("00");
		posButtonM.addActionListener(this);
		
		PosButton posButton0 = new PosButton();
		posButton0.setFocusable(false);
		ImageIcon icon0 = IconFactory.getIcon("0_32.png");
		panel.add(posButton0);
		posButton0.setIcon(icon0);
		posButton0.setActionCommand("0");
		posButton0.addActionListener(this);
		
		PosButton posButtonC = new PosButton("C");
		posButtonC.setFocusable(false);
		//ImageIcon iconC = IconFactory.getIcon("clear_32.png");
		panel.add(posButtonC);
		//posButtonC.setIcon(iconC);
		posButtonC.setActionCommand("clc");
		posButtonC.addActionListener(this);
		posButtonC.setFont(new Font(null, Font.BOLD, 50));
		posButtonC.setForeground(new Color(116, 210, 242));
		panel.setBackground(new Color(209,222,235));
		getContentPane().setBackground(new Color(209,222,235));
		getContentPane().add(panel,BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,2));
		
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(POSConstants.CLEAR)) {
			clearAll();
		}
		else if (actionCommand.toString().compareTo("clc")==0){
			doClear();
		}
		else
		{
			doInsertNumber(actionCommand);
		}
		
	}
	
	public void clearAll()
	{
		tfWeight.setText("");
		lblDisplay.setText("0,00");
		lblKg.setText("   ");
	}
	
	public static void setWeight(String value) {
		System.out.println("Value  "+ value);

		// value1 = value.trim();
		String value1 = value.replace(".", ",");
		value1 = value1.substring(0, value1.indexOf(",")+4);
		System.out.println("Value  "+ value1);
		tfWeight.setText(value1);
		updatePriceView();
		btnWeight.setBackground(null);
	}
	
	
	private void doClear() {
		String s = tfWeight.getText();
		if (s.length() > 1) {
			
			s = s.substring(0, s.length() - 1);
		}
		
		tfWeight.setText(s);
	}
	
	private static void doInsertNumber(String number) {
		
		String s = tfWeight.getText();
		s = s + number;
		if(s.length()<=7) {
			if(s.length()<=3)
				s = "0,"+org.apache.commons.lang.StringUtils.leftPad(s, 3, "0");
			else {
				if(s.substring(0, 1).compareTo("0")==0)
					s = s.substring(1, s.length());
			}
				
			if (number.compareTo("CLEAR") != 0)
				tfWeight.setText(format(s.replace(",", "")));
			System.out.println(format(s.replaceAll(",", "")));
			updatePriceView();
		}		
	}
	
	private static void updatePriceView() {
		Double weightgn = 0.00;
		try {
			weightgn = Double.parseDouble(tfWeight.getText().replace(",", "."))*1000;
		}catch(Exception ex) {
			
		}
		Double net = (weightgn/weight) * price;
		lblDisplay.setText(NumberUtil.formatNumber(net) + " €");
		lblKg.setText(" kg");
	}
	
	public static String format(String value) {
		System.out.println("BVa "+value);		
		String commafiedNum="";
        Character firstChar= value.charAt(0);
         
        //If there is a positive or negative number sign,
        //then put the number sign to the commafiedNum and remove the sign from inputNum.
        if(firstChar=='+' || firstChar=='-')
        {
            commafiedNum = commafiedNum + Character.toString(firstChar);
            value=value.replaceAll("[-\\+]", "");
        }
         
        //If the input number has decimal places,
        //then split it into two, save the first part to inputNum
        //and save the second part to decimalNum which will be appended to the final result at the end.
        String [] splittedNum = value.split("\\.");
        String decimalNum="";
        if(splittedNum.length==2)
        {
        	value=splittedNum[0];
            decimalNum="."+splittedNum[1];
        }
         
        //The main logic for adding commas to the number.
        int numLength = value.length();
        for (int i=0; i<numLength; i++) { 
            if ((numLength-i)%3 == 0 && i != 0) {
                commafiedNum += ",";
            }
            commafiedNum += value.charAt(i);
        }
 
        return commafiedNum+decimalNum;		
	}
}
*/