package com.floreantpos.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.config.UIConfig;

public class POSToggleButton extends JToggleButton {
  public static Border border = new LineBorder(Color.BLACK, 1);
  static Insets margin = new Insets(0, 0, 0, 0);

  static {
    UIManager.put("POSToggleButtonUI",
        "com.floreantpos.swing.POSToggleButtonUI");
  }

  public POSToggleButton() {
    this(null);
  }

  public POSToggleButton(String text) {
    super(text);
    setFont(UIConfig.getButtonFont());
    // setFocusPainted(false);
    // setContentAreaFilled(false);
    // setOpaque(true);
    setMargin(margin);
  }

  @Override
  public String getUIClassID() {
    return "POSToggleButtonUI";
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();

    if (isPreferredSizeSet()) {
      return size;
    }

    if (ui != null) {
      size = ui.getPreferredSize(this);
    }

    if (size != null) {
      size.setSize(size.width, 20);
    }

    return (size != null) ? size : super.getPreferredSize();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (this.isSelected()) {
      int w = getWidth();
      int h = getHeight();
      String s = getText();

      g.setColor(new Color(101, 70, 162));
      g.setFont(new Font("Tahoma", Font.BOLD, TerminalConfig
          .getTouchScreenFontSize()));
      g.fillRect(0, 0, w, h);
      g.setColor(Color.WHITE);
      g.drawString(s, (w - g.getFontMetrics().stringWidth(s)) / 2 + 1, (h + g
          .getFontMetrics().getAscent()) / 2 - 1);
    } else {
      int w = getWidth();
      int h = getHeight();
      String s = getText();
      g.setColor(new Color(57, 46, 46));
      g.fillRect(0, 0, w, h);
      g.setColor(Color.WHITE);
      g.drawString(s, (w - g.getFontMetrics().stringWidth(s)) / 2 + 1, (h + g
          .getFontMetrics().getAscent()) / 2 - 1);
    }
  }
}
