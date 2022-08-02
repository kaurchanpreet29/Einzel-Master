/*
 * NumberSelectionView.java
 *
 * Created on August 25, 2006, 7:56 PM
 */

package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.floreantpos.IconFactory;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;

/**
 *
 * @author  MShahriar
 */
public class NumberSelectionView extends TransparentPanel implements ActionListener {
  private TitledBorder titledBorder;
  
  private boolean decimalAllowed;
  private JTextField tfNumber;
  
  /** Creates new form NumberSelectionView */
  public NumberSelectionView() {
    initComponents();
  }

  private void initComponents() {
    setLayout(new BorderLayout(5,5));
    
    tfNumber = new JTextField();
    tfNumber.setText("0");
    tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
    tfNumber.setEditable(false);
    tfNumber.setBackground(Color.WHITE);
    tfNumber.setHorizontalAlignment(JTextField.RIGHT);
    
    JPanel northPanel = new JPanel(new BorderLayout(5,5));
    northPanel.add(tfNumber, BorderLayout.CENTER);
    northPanel.setBackground(new Color(5,29,53));
    PosButton btnClearAll = new PosButton();
    btnClearAll.setText(com.floreantpos.POSConstants.CLEAR_ALL);
    btnClearAll.setActionCommand(com.floreantpos.POSConstants.CLEAR_ALL);
    btnClearAll.setPreferredSize(new Dimension(90, 50));
    
    setFont(btnClearAll);
    ImageIcon iconC = IconFactory.getIcon("clear_32.png");
    btnClearAll.setIcon(iconC);
    
    btnClearAll.addActionListener(this);
    northPanel.add(btnClearAll, BorderLayout.EAST);
    
    add(northPanel, BorderLayout.NORTH);
    
    String[][] numbers = {
            {"7","8","9"},
            {"4","5","6"},
            {"1","2","3"},
            {"X","0","00"}
        }; 
        
        JPanel centerPanel = new JPanel(new GridLayout(4,3,5,5));
        Dimension preferredSize = new Dimension(90,80);
        
        for (int i = 0; i < numbers.length; i++) {
      for (int j = 0; j < numbers[i].length; j++) {
        PosButton posButton = new PosButton();
        String buttonText = String.valueOf(numbers[i][j]);
        posButton.setText(buttonText);
        posButton.setActionCommand(buttonText);
        posButton.setPreferredSize(preferredSize);
        setFont(posButton);
        posButton.addActionListener(this);
        centerPanel.add(posButton);
      }
    }
    centerPanel.setBackground(new Color(5,29,53));
    add(centerPanel, BorderLayout.CENTER);
    
    setBackground(new Color(5,29,53));
    titledBorder = new TitledBorder("");
    titledBorder.setTitleJustification(TitledBorder.CENTER);
    
    setBorder(titledBorder);
  }

  public void setFont(PosButton posButton) {
    posButton.setForeground(Color.WHITE);
    posButton.setBackground(new Color(102,51,0));
    posButton.setFont(new Font(null, Font.BOLD, 16));
  }
  
  public void setNumber(int cents) {
    tfNumber.setText(cents+"");  
  }

  public void actionPerformed(ActionEvent e) {
    String actionCommand = e.getActionCommand();
    if(actionCommand.equals(com.floreantpos.POSConstants.CLEAR_ALL)) {
      tfNumber.setText("0");
    }
    else if(actionCommand.equals("X")) {
       String s = "*";
       tfNumber.setText(tfNumber.getText()+s);
    } 
    else if (actionCommand.equals(".")) {
      if (isDecimalAllowed() && tfNumber.getText().indexOf('.') < 0) {
        String string = tfNumber.getText() + ".";
        tfNumber.setText(string);
      }
    }
    else {
      String s = tfNumber.getText();
      if(s.equals("0")) {
        tfNumber.setText(actionCommand);
        return;
      }
      
      s = s + actionCommand;
      tfNumber.setText(s);
    }
    
  }
  public void setTitle(String title) {
      titledBorder.setTitle(title);
    }
  
  public String getValue() {
    return tfNumber.getText();
  }
  
  public String getText() {
    return tfNumber.getText();
  }

  public void setValue(double value) {
    if(isDecimalAllowed()) {
      tfNumber.setText(String.valueOf(value));
    }
    else {
      tfNumber.setText(String.valueOf( (int) value));
    }
  }


  public boolean isDecimalAllowed() {
    return decimalAllowed;
  }

  public void setDecimalAllowed(boolean decimalAllowed) {
    this.decimalAllowed = decimalAllowed;
  }
}
