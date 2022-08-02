package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JSeparator;

import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

import net.miginfocom.swing.MigLayout;

public class NumberSelectionPasswordDialog extends POSDialog implements
    ActionListener, WindowListener{
  private int defaultValue;

  private TitlePanel titlePanel;
  private static POSPasswordField tfNumber;

  private boolean floatingPoint;
  private PosButton posButton_1;
  private static int count;
  boolean keyHandled;

  private User user;

  public NumberSelectionPasswordDialog() {
    this(Application.getPosWindow()); 
  }

  public NumberSelectionPasswordDialog(Frame parent) {
    super(parent, true);
    init();
  }

  public void setFocus() {
    tfNumber.setText("");
    tfNumber.requestFocus();
  }

  
  public NumberSelectionPasswordDialog(Dialog parent) {
    super(parent, true);

    init();
    this.addWindowListener(this);
  }

  public User getUser() {
    return user;
  }

  private void init() {
    setResizable(false);
    //for key listner
	//this.setFocusable(true);
	startKeyboardListener();
	keyHandled = false;
	count =0;
    Container contentPane = getContentPane();

    MigLayout layout = new MigLayout("fillx",
        "[60px,fill][60px,fill][60px,fill]", "[][][][][]");
    contentPane.setLayout(layout);

    titlePanel = new TitlePanel();
    contentPane.add(titlePanel, "spanx ,growy,height 60,wrap");
    titlePanel.setBackground(new Color(35, 35, 36));
    titlePanel.setForeground(Color.WHITE);
    contentPane.setBackground(new Color(35, 35, 36));
    tfNumber = new POSPasswordField();

    tfNumber.setFont(tfNumber.getFont().deriveFont(Font.BOLD, 24));
    tfNumber.setFocusable(true);
    tfNumber.requestFocus();
    tfNumber.setBackground(Color.WHITE);
    tfNumber.setText("");
    contentPane.add(tfNumber, "span 2, grow");

    PosButton posButton = new PosButton(POSConstants.CLEAR_ALL);
    posButton.setFocusable(false);
    posButton.setBackground(Color.BLACK);
    posButton.setForeground(Color.WHITE);
    posButton.setMinimumSize(new Dimension(25, 23));
    posButton.setFont(new Font("Times New Roman", Font.BOLD, 28));
    posButton.addActionListener(this);
    contentPane.add(posButton, "growy,height 55,wrap");

    String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" },
        { "1", "2", "3" }, { "00", "0", POSConstants.CLEAR } };
    String[][] iconNames = new String[][] {
        { "7_32.png", "8_32.png", "9_32.png" },
        { "4_32.png", "5_32.png", "6_32.png" },
        { "1_32.png", "2_32.png", "3_32.png" },
        { "00_32.png", "0_32.png", "clear_32.png" } };

    for (int i = 0; i < numbers.length; i++) {
      for (int j = 0; j < numbers[i].length; j++) {
        posButton = new PosButton();
        posButton.setFocusable(false);
        String buttonText = String.valueOf(numbers[i][j]);
        posButton.setText(buttonText);
        posButton.setBackground(new Color(102, 51, 0));
        posButton.setForeground(Color.WHITE);
        posButton.setActionCommand(buttonText);
        if(buttonText.equals(POSConstants.CLEAR)) {
          posButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_32.png"))); // NOI18N  
        }
        
        posButton.setFont(new Font(null, Font.BOLD, 18));
        posButton.addActionListener(this);
        String constraints = "grow, height 55";
        if (j == numbers[i].length - 1) {
          constraints += ", wrap";
        }
        contentPane.add(posButton, constraints);
      }
    }
    contentPane.add(new JSeparator(), "newline,spanx ,growy,gapy 20");

    posButton = new PosButton(POSConstants.OK);
    posButton.setFocusable(false);
    posButton.addActionListener(this);
    posButton.setForeground(Color.WHITE);
    posButton.setBackground(new Color(2, 64, 2));
    contentPane.add(posButton, "skip 1,grow");

    posButton_1 = new PosButton(POSConstants.CANCEL);
    posButton_1.setBackground(new Color(125, 6, 42));
    posButton_1.setForeground(Color.WHITE);
    posButton_1.setFocusable(false);
    posButton_1.addActionListener(this);
    contentPane.add(posButton_1, "grow");
  }

  private void startKeyboardListener() {
	 //KeyboardFocusManager.setCurrentKeyboardFocusManager(null);
	  KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						dispose();
					}else if (e.getKeyCode() == KeyEvent.VK_NUMPAD0 || e.getKeyCode() == KeyEvent.VK_0) {
						doInsertNumber("0");
					}else if (e.getKeyCode() == KeyEvent.VK_NUMPAD1 || e.getKeyCode() == KeyEvent.VK_1) {
						doInsertNumber("1");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD2 || e.getKeyCode() == KeyEvent.VK_2) {
						doInsertNumber("2");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD3 || e.getKeyCode() == KeyEvent.VK_3) {
						doInsertNumber("3");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD4 || e.getKeyCode() == KeyEvent.VK_4) {
						doInsertNumber("4");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD5 || e.getKeyCode() == KeyEvent.VK_5) {
						doInsertNumber("5");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD6 || e.getKeyCode() == KeyEvent.VK_6) {
						doInsertNumber("6");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD7 || e.getKeyCode() == KeyEvent.VK_7) {
						doInsertNumber("7");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD8 || e.getKeyCode() == KeyEvent.VK_8) {
						doInsertNumber("8");
					} else if (e.getKeyCode() == KeyEvent.VK_NUMPAD9 || e.getKeyCode() == KeyEvent.VK_9) {
						doInsertNumber("9");
					}
				}
				return false;
			}
			
		});
	  
	 
  }
  private void doOk() {
    char[] key = tfNumber.getPassword();
    String password = new String(key);
    if (!validate(password)) {
      //POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
      return;
    }

    if (performUserLogin(password)) {
      setCanceled(false);
      dispose();
      tfNumber.setText(null);
    } else {
      tfNumber.setText(null);
    }
  }
  
  
  
  private void doCancel() {
    setCanceled(true);
    dispose();
  }

  private void doClearAll() {
    tfNumber.setText("");
  }

  private void doClear() {
    String s = tfNumber.getText();
    if (s.length() > 1) {
      s = s.substring(0, s.length() - 1);
    } else {
      s = String.valueOf("");
    }
    tfNumber.setText(s);
  }

  private void doInsertNumber(String number) {
    String s = tfNumber.getText();
    double d = 0;

    try {
      d = Double.parseDouble(s);
    } catch (Exception x) {
    }

    if (d == 0) {
      tfNumber.setText(number);
      return;
    }

    s = s + number;
    if (!validate(s)) {
      //POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
      return;
    }
    tfNumber.setText(s);
    
  }
  
 
  private void doInsertDot() {
    String string = tfNumber.getText() + ".";
    if (!validate(string)) {
      POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
      return;
    }
    tfNumber.setText(string);   
  }

  public void actionPerformed(ActionEvent e) {	  
    String actionCommand = e.getActionCommand();
    if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
      doCancel();
    } else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
      doOk();
    } else if (actionCommand.equals(POSConstants.CLEAR_ALL)) {
      doClearAll();
    } else if (actionCommand.equals(POSConstants.CLEAR)) {
      doClear();
    } else if (actionCommand.equals(".")) {
      doInsertDot();
    } else {
      doInsertNumber(actionCommand);
    }

  }

  private boolean validate(String str) {
    try {
      Integer.parseInt(str);
    } catch (Exception x) {
      return false;
    }
    return true;
  }

  public void setTitle(String title) {
    titlePanel.setTitle(title);

    super.setTitle(title);
  }

  public void setDialogTitle(String title) {
    super.setTitle(title);
  }

  public void setValue(double value) {
    if (value == 0) {
      tfNumber.setText("");
    } else if (isFloatingPoint()) {
      tfNumber.setText(String.valueOf(value));
    } else {
      tfNumber.setText(String.valueOf((int) value));
    }
  }

  public boolean isFloatingPoint() {
    return floatingPoint;
  }

  public void setFloatingPoint(boolean decimalAllowed) {
    this.floatingPoint = decimalAllowed;
  }

  public int getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(int defaultValue) {
    this.defaultValue = defaultValue;
    tfNumber.setText("");
  }

  public boolean performUserLogin(String key) {

    UserDAO dao = new UserDAO();
    user = dao.findUserBySecretKey(key);

    if (user == null) {
      POSMessageDialog.showError("Ungueltige Pin");
      return false;
    }

    return true;
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
    System.out.println("Canceling");
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
