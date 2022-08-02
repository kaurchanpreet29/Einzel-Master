package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.floreantpos.swing.ImageIcon;

public class WarningMessageDialog extends POSDialog{

	JLabel lblImage;
	JLabel lblText;
	JButton bckButton;
	JButton okButton;
	JButton cancelButton;
	boolean cancelled;
	boolean back;
	boolean btncancelled;
	boolean save;
	public WarningMessageDialog(String message)
	{
		setTitle("WARNUNG");
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		panel.setBackground(new Color(209,222,235));
		lblImage = new JLabel();
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/images/warnung.png"));
		lblImage.setIcon(imageIcon);
		lblText = new JLabel(message);
		lblText.setFont(new Font("Times New Roman", Font.BOLD, 24));
		panel.add(lblImage);
		panel.add(lblText, "wrap");
		
		
		JPanel buttonPanel = new JPanel();
		
		bckButton = new JButton("ZUERUCK");
		bckButton.setFont(new Font("Times New Roman", Font.BOLD, 28));
		bckButton.setBackground(new Color(102,178,255));
		bckButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setCanceled(false);
				setBack(true);
				setSave(false);
				dispose();
			}
			
		});
		okButton = new JButton("SPEICHERN");
		okButton.setFont(new Font("Times New Roman", Font.BOLD, 28));
		okButton.setBackground(new Color(102,255,102));
		okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setBack(false);
				setBtncancelled(false);
				setSave(true);
				dispose();
			}
			
		});
		cancelButton = new JButton("NICHT SPEICHERN");
		cancelButton.setBackground(new Color(255,153,153));
		cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 28));
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setBtncancelled(true);
				setBack(false);
				setSave(false);
				dispose();
			}
		});
		buttonPanel.add(bckButton);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(panel, BorderLayout.NORTH);
		buttonPanel.setBackground(new Color(209,222,235));
		add(buttonPanel, BorderLayout.SOUTH);
	}
	public void setBack(boolean bck)
	{
		this.back = bck;
	}
	public boolean isBack()
	{
		return back;
	}
	public boolean isSave()
	{
		return save;
	}
	public void setSave(boolean save)
	{
		this.save = save;
	}
	public void setBtncancelled(boolean cancel)
	{
		this.btncancelled = cancel;
	}
	public boolean getBtnCancelled()
	{
		return this.btncancelled;
	}
	/*public void setCancelled(boolean flag)
	{
		cancelled = flag;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}*/
}
