package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.util.NumberUtil;

public class ZVTCardConfirmationDialog extends POSDialog {
  
  private Double totalAmount;  
  private Double inputAmount;
  
  public ZVTCardConfirmationDialog(Double totalAmount, Double inputAmount){
    this.totalAmount = totalAmount;
    this.inputAmount = inputAmount;
    initComponents();
  }
  
  public void initComponents() {
    getContentPane().setBackground(new Color(209, 222, 235));
    getContentPane().setLayout(new BorderLayout(5,5));
    
    JPanel topPanel = new JPanel();
    topPanel.setBackground(new Color(209, 222, 235));
    topPanel.setLayout(new MigLayout());
    
    JPanel centerPanel =new JPanel();
    centerPanel.setBackground(new Color(209, 222, 235));
    centerPanel.setLayout(new BorderLayout(5,5));
    
    JPanel middlePanel = new JPanel();
    middlePanel.setBackground(new Color(209, 222, 235));
    middlePanel.setLayout(new MigLayout());
    
    JPanel bottomPanel = new JPanel();
    bottomPanel.setBackground(new Color(209, 222, 235));
    bottomPanel.setLayout(new MigLayout());
    
    JLabel lblTotalAmount = new JLabel("Gesamt: ");
    lblTotalAmount.setFont(new Font(null, Font.BOLD, 20));
    topPanel.add(lblTotalAmount);
    JLabel lblTotalAmountValue = new JLabel(NumberUtil.formatNumber(this.totalAmount)  + " " + Application.getCurrencySymbol());
    lblTotalAmountValue.setFont(new Font(null, Font.PLAIN, 20));
    topPanel.add(lblTotalAmountValue,"growx, wrap");
    
    JLabel lblPaidAmount = new JLabel("Bezahlt: ");
    lblPaidAmount.setFont(new Font(null, Font.BOLD, 20));
    topPanel.add(lblPaidAmount);

    JLabel lblPaidAmountValue = new JLabel(NumberUtil.formatNumber(this.inputAmount)  + " " + Application.getCurrencySymbol());
    lblPaidAmountValue.setFont(new Font(null, Font.PLAIN, 20));
    topPanel.add(lblPaidAmountValue,"growx, wrap");
    
    JLabel lblConfirmationMessage = new JLabel("Zahlung Erfoeglich ?");
    lblConfirmationMessage.setFont(new Font(null,Font.BOLD, 20));
    middlePanel.add(lblConfirmationMessage);
    
    PosButton okButton = new PosButton("   Ja   ");
    okButton.setBackground(new Color(0, 200, 0));
    okButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        setCanceled(false);
        dispose();
      }
    });
    
    PosButton cancelButton = new PosButton("  Nein  ");
    cancelButton.setBackground(new Color(255, 102, 102));
    cancelButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        setCanceled(true);
        dispose();
      }
    });
    
    bottomPanel.add(okButton);
    bottomPanel.add(cancelButton);
    
    centerPanel.add(middlePanel, BorderLayout.CENTER);
    centerPanel.add(bottomPanel, BorderLayout.SOUTH);
    
    getContentPane().add(topPanel, BorderLayout.NORTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    
  }
}
