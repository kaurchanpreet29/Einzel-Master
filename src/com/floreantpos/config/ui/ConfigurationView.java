package com.floreantpos.config.ui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public abstract class ConfigurationView extends JPanel{
	private boolean initialized = false;

	public ConfigurationView() {
		setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10, 10, 10, 10)));
		setBackground(new Color(209,222,235));
		
	}
	
	public void setTabbedPane(JTabbedPane panel, int i, Color color) {
		setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(10, 10, 10, 10)));
		setBackground(new Color(209,222,235));
		  panel.setUI(new BasicTabbedPaneUI()
          {
              @Override
              protected void installDefaults()
              {
                  super.installDefaults();

                  highlight       = Color.BLACK;
                  lightHighlight  = Color.BLACK;

                  shadow          = Color.BLACK;
                  darkShadow      = Color.BLACK;
                  focus           = Color.BLACK;
              }
          });

          //Set the active tab background to white
          UIManager.put("TabbedPane.selected", Color.WHITE);
          UIManager.put("TabbedPane.unselectedTabBackground", Color.BLACK);

          //Remove borders
          UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));

          setOpaque(true);

          setBackground(color);
          setForeground(Color.white);
	}
	protected JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setHorizontalAlignment(JLabel.RIGHT);
		return label;
	}

	protected void addRow(String title, JTextField textField) {
		add(createLabel(title), "newline, grow");
		add(textField, "w 250,height pref");
	}
	
	protected void addRow(String title, JTextField textField, String constraints) {
		add(createLabel(title), "newline, grow");
		add(textField, constraints);
	}
	
	public abstract boolean save() throws Exception;
	public abstract void initialize() throws Exception;
	public abstract String getName();

	public boolean isInitialized() {
		return initialized;
	}


	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
}
