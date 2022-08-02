package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.floreantpos.IconFactory;
public class TableDialog extends POSDialog{

	JButton plan1;
	JButton plan2;
	JPanel panel;
	int selectedPlan;
	public TableDialog() throws IOException
	{
		initComponents();
		
	}
	
	public void initComponents()
	{
		setTitle("Waehlen Sie die Raum");
		setBackground(new Color(209,222,235));
		setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(660,350));
		panel = new JPanel();
		panel.setBackground(new Color(209,222,235));
		panel.setLayout(new MigLayout());
		plan1 = new JButton();
		plan1.setContentAreaFilled(false);

		plan1.setFont(new Font("Times New Roman", Font.BOLD, 40));
		panel.add(plan1);
		ImageIcon icon = IconFactory.getIcon("floorplan_small.png");
		plan1.setIcon(icon);
		plan1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedPlan =1 ;
				setCanceled(false);
				dispose();
			}
			
		});
		plan2 = new JButton();
		
		ImageIcon icon1 = IconFactory.getIcon("floorplan1_small.png");
		plan2.setContentAreaFilled(false);
		plan2.setIcon(icon1);
		plan2.setFont(new Font("Times New Roman", Font.BOLD, 40));
		plan2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedPlan =2 ;
				setCanceled(false);
				dispose();
				
			}
			
		});

		panel.add(plan2);
		
		add(panel,BorderLayout.CENTER);
		
	}
	
	public int getSelectedPlan()
	{
		return selectedPlan;
	}
}
