package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;

public class OpenDialog extends JDialog {

  public OpenDialog() {
    
    setBackground(new Color(255,255,255));
    setLayout(new BorderLayout());
    
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new MigLayout("alignx 50%"));
   
    ImageIcon loading = IconFactory.getIcon("Khana_Kassensysteme.gif");
    JLabel loadingLabel = new JLabel();
    loadingLabel.setOpaque(true);
    loadingLabel.setIcon(loading);
    
    panel.setOpaque(false);
    getContentPane().add(panel, BorderLayout.CENTER);
    getContentPane().add(loadingLabel, BorderLayout.SOUTH);
  }
  
}
