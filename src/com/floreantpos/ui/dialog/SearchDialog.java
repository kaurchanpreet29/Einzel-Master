package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.TicketType;

public class SearchDialog extends POSDialog implements ActionListener,
    WindowListener {
  private List<MenuItem> menuItems;
  MenuItem menuItem;
  private TicketType ticketType;

  private TitlePanel titlePanel;

  public SearchDialog(List<MenuItem> item, TicketType type) {
    this(Application.getPosWindow(), item, type);

  }

  public SearchDialog(Frame parent, List<MenuItem> item, TicketType type) {
    super(parent, true);
    menuItems = item;
    ticketType = type;
    this.addWindowListener(this);
    init();
  }

  public SearchDialog(Dialog parent) {
    super(parent, true);

    init();
    this.addWindowListener(this);
  }

  private void init() {
    setResizable(false);
    MigLayout layout = new MigLayout();
    setLayout(layout);

    getContentPane().setBackground(new Color(209, 222, 235));
    titlePanel = new TitlePanel();
    getContentPane().add(titlePanel, "spanx ,growy,height 60,wrap");
    titlePanel.setBackground(new Color(209, 222, 235));

    int index = 1;
    for (MenuItem menuItem: menuItems) {
      PosButton button = new PosButton();
      button.setText(menuItem.getName() + " ("
          + NumberUtil.formatNumber(menuItem.getPrice()) + " EUR)");
      button.setFont(new Font("Times New Roman", Font.BOLD, 12));
      button.addActionListener(this);
      button.setActionCommand(menuItem.getName() + "$"
          + menuItem.getPrice().toString());
      button.setBackground(new Color(102, 255, 102));

      if (index % 4 == 0)
        getContentPane().add(button, "growx,wrap");
      else
        getContentPane().add(button, "growx");

      ++index;
    }
  }

  private void doOk() {

    setCanceled(false);
    dispose();
  }

  private void doCancel() {
    setCanceled(true);
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    String actionCommand = e.getActionCommand();

    int dollarIndex = actionCommand.indexOf("$");
    String name = actionCommand.substring(0, dollarIndex);
    String itemPrice = actionCommand.substring(dollarIndex + 1,
        actionCommand.length());

    if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
      doCancel();
    } else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
      doOk();
    }
    for (MenuItem menuItem: menuItems) {
      if ((menuItem.getPrice().toString().compareTo(itemPrice) == 0)
          && (menuItem.getName().toString().compareTo(name) == 0)) {
        setMenuItem(menuItem);
        doOk();
        return;
      }
    }
    doCancel();
  }

  public void setTitle(String title) {
    titlePanel.setTitle("Waehlen Sie Gewunschte Artikel aus");
    super.setTitle(title);
  }

  public void setMenuItem(MenuItem item) {
    menuItem = item;
  }

  public MenuItem getMenuItem() {
    return menuItem;
  }

  public void setDialogTitle(String title) {
    super.setTitle(title);
  }

  @Override
  public void windowActivated(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowClosed(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowClosing(WindowEvent arg0) {
    doCancel();

  }

  @Override
  public void windowDeactivated(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowDeiconified(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowIconified(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void windowOpened(WindowEvent arg0) {
    // TODO Auto-generated method stub

  }
}
