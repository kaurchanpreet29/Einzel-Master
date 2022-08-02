package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.swing.PosButton;

public class ColorPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  private PosButton btnColor1;
  private PosButton btnColor2;
  private PosButton btnColor3;
  private PosButton btnColor4;
  private PosButton btnColor5;
  private PosButton btnColor6;
  private PosButton btnColor7;
  private PosButton btnColor8;

  private Color color1;
  private Color color2;
  private Color color3;
  private Color color4;
  private Color color5;
  private Color color6;
  private Color color7;
  private Color color8;

  private IntegerTextField tfRed = new IntegerTextField();
  private IntegerTextField tfGreen = new IntegerTextField();
  private IntegerTextField tfBlue = new IntegerTextField();

  public ColorPanel() {
    initComponents();
  }

  public void initComponents() {

    JPanel colorButtonPanel = new JPanel();
    colorButtonPanel.setLayout(new GridLayout(4, 4, 2, 2));
    colorButtonPanel.setBackground(new Color(209, 222, 235));

    // Blue
    color1 = new Color(14,9,166);
    btnColor1 = new PosButton();
    btnColor1.setBackground(color1);
    btnColor1.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(14 + "");
        tfGreen.setText(9 + "");
        tfBlue.setText(166 + "");
      }
    });

    // Purple
    color2 = new Color(138, 9, 74);
    btnColor2 = new PosButton();
    btnColor2.setBackground(color2);
    btnColor2.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(138 + "");
        tfGreen.setText(9 + "");
        tfBlue.setText(74 + "");
      }
    });

    // Dark navy blue
    color3 = new Color(61, 9, 138);
    btnColor3 = new PosButton();
    btnColor3.setBackground(color3);
    btnColor3.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(61 + "");
        tfGreen.setText(9 + "");
        tfBlue.setText(138 + "");
      }
    });

    // Dark Brown
    color4 = new Color(102,51,0);
    btnColor4 = new PosButton();
    btnColor4.setBackground(color4);
    btnColor4.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(102 + "");
        tfGreen.setText(51 + "");
        tfBlue.setText(0 + "");
      }
    });

    // Magenta
    color5 = new Color(3, 113, 117);
    btnColor5 = new PosButton();
    btnColor5.setBackground(color5);
    btnColor5.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(3 + "");
        tfGreen.setText(113 + "");
        tfBlue.setText(117 + "");
      }
    });

    // Dark yellow
    color6 = new Color(168,162,3);
    btnColor6 = new PosButton();
    btnColor6.setBackground(color6);
    btnColor6.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(168+ "");
        tfGreen.setText(162 + "");
        tfBlue.setText(3 + "");
      }
    });

    // Black
    color7 = new Color(1,1,1);
    btnColor7 = new PosButton();
    btnColor7.setBackground(color7);
    btnColor7.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(1 + "");
        tfGreen.setText(1 + "");
        tfBlue.setText(1 + "");
      }
    });

    // RED
    color8 = new Color(252,8,33);
    btnColor8 = new PosButton();
    btnColor8.setBackground(color8);
    btnColor8.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        tfRed.setText(252 + "");
        tfGreen.setText(8 + "");
        tfBlue.setText(33 + "");
      }
    });

    colorButtonPanel.add(btnColor1);
    colorButtonPanel.add(btnColor2);
    colorButtonPanel.add(btnColor3);
    colorButtonPanel.add(btnColor4);
    colorButtonPanel.add(btnColor5);
    colorButtonPanel.add(btnColor6);
    colorButtonPanel.add(btnColor7);
    colorButtonPanel.add(btnColor8);

    JPanel southButtonPanel = new JPanel();
    southButtonPanel.setLayout(new MigLayout("insets 0", "[][grow]", ""));
    southButtonPanel.setBackground(new Color(209, 222, 235));
    southButtonPanel.add(new JLabel("Rot"));
    southButtonPanel.add(tfRed, "growx,wrap");
    southButtonPanel.add(new JLabel("Gruen"));
    southButtonPanel.add(tfGreen, "growx,wrap");
    southButtonPanel.add(new JLabel("Blaue"));
    southButtonPanel.add(tfBlue, "growx,wrap");

    setLayout(new BorderLayout(0, 0));
    setBackground(new Color(209, 222, 235));
    add(colorButtonPanel, BorderLayout.CENTER);
    add(southButtonPanel, BorderLayout.SOUTH);
  }

  public void setFont(JLabel lbl) {
    lbl.setPreferredSize(new Dimension(20, 20));
  }

  public Integer getRed() {
    if (tfRed.getText() != null && tfRed.getText().length() > 0) {
      int red = Integer.parseInt(tfRed.getText());
      if(red > 255) {
        return 255;
      }
      return red;
    }
     
    return 0;
  }

  public Integer getGreen() {
    if (tfGreen.getText() != null && tfGreen.getText().length() > 0) {
      int green = Integer.parseInt(tfGreen.getText());
      if(green > 255) {
        return 255;
      }
      return green;
    }
    return 0;
  }

  public Integer getBlue() {
    if (tfBlue.getText() != null && tfBlue.getText().length() > 0) {
      int blue = Integer.parseInt(tfBlue.getText());
      if(blue > 255) {
        return 255;
      }
      return blue;
    }
    
    return 0;
  }
  
  public void setRed(int red) {
    tfRed.setText(red+"");
  }
  
  public void setGreen(int green) {
    tfGreen.setText(green+"");
  }
  
  public void setBlue(int blue) {
    tfBlue.setText(blue+"");
  }
}
