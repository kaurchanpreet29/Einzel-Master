package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.main.Application;
import com.floreantpos.model.Restaurant;
import com.floreantpos.model.TicketItem;
import com.floreantpos.model.User;
import com.floreantpos.model.dao.RestaurantDAO;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.POSPasswordField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class PasswordDialog extends POSDialog implements
    ActionListener, WindowListener {
  private int defaultValue;
  private String stornoText;
 
private boolean isManual =false;
  private TitlePanel titlePanel;
  private static POSPasswordField tfNumber;

  private boolean floatingPoint;
  private PosButton posButton_1;
  private JComboBox<String> cbStornoOption = new JComboBox<>();

  private User user;

  private int pin;
  
  public PasswordDialog() {
    this(Application.getPosWindow());
  }

  public PasswordDialog(Frame parent) {
    super(parent, true);
    init();
  }

  public void setFocus() {
    tfNumber.setText("");
    tfNumber.requestFocus();
  }

  
  public PasswordDialog(int pin, String title) {
    init();
    this.pin = pin;
    this.setTitle(title);
    this.addWindowListener(this);
  }
  
  public PasswordDialog(int pin, String title, String reason) {
	  	stornoText = reason;
	    init();
	    this.pin = pin;
	    this.setTitle(title);
	    this.addWindowListener(this);
	  }

  public PasswordDialog(String pin, String title) {
    init();
    try {
      this.pin = Integer.parseInt(pin);
    } catch(Exception e) {
      this.pin = 1005;
    }
    this.setTitle(title);
    this.addWindowListener(this);
  }
  
  public User getUser() {
    return user;
  }
  
  public String getStornoText() {
		return stornoText;
	}

	public void setStornoText(String stornoText) {
		this.stornoText = stornoText;
	}


  private void init() {
    setResizable(false);

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
    tfNumber.requestFocus();
    tfNumber.setFocusable(true);
    tfNumber.requestFocus();
    tfNumber.setBackground(Color.WHITE);
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
    //added
    cbStornoOption.setSize(70, 100);
    cbStornoOption.setBackground(new Color(138, 193, 98));
    cbStornoOption.addItem("Manual Grund");
    
    cbStornoOption.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
if(cbStornoOption.getSelectedItem().toString().compareTo("Manual Grund")==0) {
	showItemPopUp(e);
}else {
	setStornoText(cbStornoOption.getSelectedItem().toString());
}
		}
	});
    
    
   /* cbStornoOption.addMouseListener(new MouseAdapter() {
		public void mouseClicked(MouseEvent event) {
			if(event.getClickCount() == 1) {

				if(cbStornoOption.getSelectedItem().toString().compareTo("Manual Grund")==0) {
					showItemPopUp(event);
				}
			}			
					}
				});
    
    */
    
    //till here
    
    JLabel grund = new JLabel("Grund Der Storno");
    grund.setForeground(new Color(255, 255, 255));
    JPanel storno = new JPanel();
    storno.add(grund, "growx, wrap");
    storno.add(cbStornoOption, "growx, wrap");
    
    //contentPane.add(new JSeparator(), "newline,spanx ,growy,gapy 20");
//    if(stornoText!=null&&!stornoText.isEmpty()) {
//    	//contentPane
//        contentPane.add(grund, "wrap");
////    	contentPane.add(cbStornoOption, "growx");
//    }

    posButton = new PosButton(POSConstants.OK);	
    posButton.setFocusable(false);
    posButton.addActionListener(this);
    posButton.setForeground(Color.WHITE);
    posButton.setBackground(new Color(2, 64, 2));
    contentPane.add(posButton, "growx");

    posButton_1 = new PosButton(POSConstants.CANCEL);
    posButton_1.setBackground(new Color(125, 6, 42));
    posButton_1.setForeground(Color.WHITE);
    posButton_1.setFocusable(false);
    posButton_1.addActionListener(this);
    contentPane.add(posButton_1, "growx");
    
    //add combobox for storno reason    
  }

  private void doOk() {
    char[] key = tfNumber.getPassword();
    String password = new String(key);
    if (!validate(password)) {
      POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
      return;
    }

    if (this.pin == Integer.parseInt(password)) {
      setCanceled(false);
      dispose();
    } else {
      POSMessageDialog.showError("Ungueltig pin");
      tfNumber.setText("");
    }
  }
  
	protected void showItemPopUp(ActionEvent event) {

		JPopupMenu popup = new JPopupMenu();
		JTextField manual = new JTextField();	
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			setStornoText(manual.getText());
			isManual=true;
			popup.hide();
			}
		});
		popup.add(manual);
		popup.add(btnOk);
		popup.setPreferredSize(new Dimension(150, 50));
//		popup.show(cbStornoOption, 10, 10);

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
      POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
      return;
    }
    tfNumber.setText(s);
  }

  private void doInsertDot() {
    // if (isFloatingPoint() && tfNumber.getText().indexOf('.') < 0) {
    String string = tfNumber.getText() + ".";
    if (!validate(string)) {
      POSMessageDialog.showError(this, POSConstants.INVALID_NUMBER);
      return;
    }
    tfNumber.setText(string);
    // }
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
