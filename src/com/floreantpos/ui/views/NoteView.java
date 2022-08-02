package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.TransparentPanel;

public class NoteView extends JPanel implements ActionListener, ChangeListener {
  Font buttonFont = getFont().deriveFont(Font.BOLD, 24);

  String[] s1 = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" };
  String[] s2 = { "q", "w", "e", "r", "t", "z", "u", "i", "o", "p" };
  String[] s3 = { "a", "s", "d", "f", "g", "h", "j", "k", "l", ";" };
  String[] s4 = { "y", "x", "c", "v", "b", "n", "m", "-", ",", "." };

  public JTextField note = new JTextField();

  private ArrayList<PosButton> buttons = new ArrayList<PosButton>();
  Dimension pSize = new Dimension(50, 50);

  public NoteView() {
    setLayout(new BorderLayout(5, 5));

    note.setFont(note.getFont().deriveFont(Font.BOLD, 18));
    note.setDocument(new FixedLengthDocument(1000));

    TransparentPanel northPanel = new TransparentPanel(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(note);
    scrollPane.setMinimumSize(new Dimension(600,100));
    northPanel.add(scrollPane);
    northPanel.setBackground(new Color(5,29,53));
    

    TransparentPanel centerPanel = new TransparentPanel(new GridLayout(0,1,2,2));
    centerPanel.setBackground(new Color(5,29,53));
    centerPanel.add(addButtonsToPanel(s1));
    centerPanel.add(addButtonsToPanel(s2));
    centerPanel.add(addButtonsToPanel(s3));
    centerPanel.add(addButtonsToPanel(s4));
    
    JPanel eastPanel = new JPanel(new GridLayout(0, 1, 2, 2));
    PosButton button = new PosButton();
    button.setBackground(Color.BLACK);
    button.setForeground(Color.WHITE);
    button.setText("SPACE");
    button.addActionListener(this);
    eastPanel.add(button);
    
    POSToggleButton toggleButton = new POSToggleButton();
    toggleButton.setContentAreaFilled(false);
    toggleButton.setOpaque(true);
    toggleButton.setBackground(new Color(102,178,255));
    toggleButton.setText("CAPS");
    toggleButton.addChangeListener(this);
    eastPanel.setBackground(new Color(5,29,53));
    eastPanel.add(toggleButton);

    button = new PosButton();
    button.setText(com.floreantpos.POSConstants.CLEAR);
    button.setIcon(com.floreantpos.IconFactory.getIcon("clear_32.png"));
    button.setBackground(new Color(125,6,42));
    button.setForeground(Color.WHITE);
    button.addActionListener(this);
    eastPanel.add(button);

    button = new PosButton();
    button.setText(com.floreantpos.POSConstants.CLEAR_ALL);
    button.setBackground(new Color(125,6,42));
    button.setForeground(Color.WHITE);
    button.addActionListener(this);
    eastPanel.add(button);

    add(northPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
    add(eastPanel, BorderLayout.EAST);
    
    setBackground(new Color(5,29,53));
    note.setText("Sonstiges Speisen");
  }

  private TransparentPanel addButtonsToPanel(String[] buttonText) {
    TransparentPanel panel = new TransparentPanel(new GridLayout(0,s1.length,2,2));
    panel.setBackground(new Color(5,29,53));
    for (int i = 0; i < buttonText.length; i++) {
      String s = buttonText[i];
      PosButton button = new PosButton();
      button.setBackground(Color.BLACK);
      button.setForeground(Color.WHITE);
      button.setText(s);
      button.setPreferredSize(pSize);
      button.addActionListener(this);
      button.setFont(buttonFont);
      buttons.add(button);
      panel.add(button);
    }
    return panel;
  }

  public void actionPerformed(ActionEvent e) {
    String s = e.getActionCommand();
    if (s.equals(com.floreantpos.POSConstants.OK)) {
      //canceled = false;
      //dispose();
    }
    else if (s.equals(com.floreantpos.POSConstants.CANCEL)) {
      //canceled = true;
      //dispose();
    }
    else if (s.equals(com.floreantpos.POSConstants.CLEAR)) {
      String str = note.getText();
      if (str.length() > 0) {
        str = str.substring(0, str.length() - 1);
      }
      note.setText(str);
    }
    else if (s.equals(com.floreantpos.POSConstants.CLEAR_ALL)) {
      note.setText("");
    }
    else if (s.equals("SPACE")) {
      String str = note.getText();
      if (str == null) {
        str = "";
      }
      note.setText(str + " ");
    }
    else {
      String str = note.getText();
      if (str == null) {
        str = "";
      }
      note.setText(str + s);
    }
  }

  public void stateChanged(ChangeEvent e) {
    JToggleButton b = (JToggleButton) e.getSource();

    if (b.isSelected()) {
      for (PosButton button : buttons) {
        button.setText(button.getText().toUpperCase());
      }
    }
    else {
      for (PosButton button : buttons) {
        button.setText(button.getText().toLowerCase());
      }
    }
  }

  public JTextField getNote() {
    return note;
  }
  
  public void setNoteLength(int length) {
    note.setDocument(new FixedLengthDocument(length));
  }
  
  public void setText(String text){
    note.setText(text);
  }
}
