package com.floreantpos.config.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.model.ShopTable;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.FixedLengthDocument;
import com.floreantpos.swing.ImageIcon;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.dialog.NumberSelectionDialog2;
import com.floreantpos.ui.dialog.POSDialog;

public class TablePlanDesign2 extends POSDialog implements MouseListener{

	ImagePanel panel;
	JPanel buttonPanel;
	List<ShopTable> tableList;
	JButton moveButton;
	JButton floorPlan2;
	public TablePlanDesign2() throws IOException
	{
		super();
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		northPanel.setPreferredSize(new Dimension(800,20));
		setLayout(new BorderLayout());
		initComponents();
	}
	
	public void initComponents() throws IOException
	{
		
		setTitle("Tische Plan");
		tableList = new ArrayList();
		Image image;
		image = ImageIO.read(new File("resources/images/floorPlan1.png"));
		panel = new ImagePanel(image);
		this.setPreferredSize(new Dimension(800,700));
		panel.setPreferredSize(new Dimension(800,700));
		getContentPane().setBackground(new java.awt.Color(209,222,235));
		panel.setLayout(new BorderLayout());
		this.addMouseListener(this);
		panel.setOpaque(false);
		panel.setBackground(new java.awt.Color(209,222,235));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setPreferredSize(new Dimension(800,600));
		
		buttonPanel.setOpaque(false);
		buttonPanel.setBackground(new java.awt.Color(209,222,235));
		panel.add(buttonPanel,BorderLayout.NORTH);
		
		tableList = ShopTableDAO.getInstance().getByFloor(2);
		if(tableList != null)
		{
			for(Iterator<ShopTable> itr = tableList.iterator();itr.hasNext();)
				{
				ShopTable table = itr.next();
					final JButton button = new JButton();
					button.setText(table.getNumber()+"");
					
					if((table.getNumber().compareTo("99") == 0)||(table.getNumber().compareTo("98") == 0))
					{
						button.setBounds(table.getX()-30, table.getY()-60, 120,120);
						button.setBackground(new java.awt.Color(102,178,255));
					}
					else
					{
						button.setBackground(new java.awt.Color(102,255,102));
						button.setBounds(table.getX()-30, table.getY()-60, 60,60);
					}
					button.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							
							moveButton = button;
							removeButton(Integer.parseInt(button.getText()));
							buttonPanel.remove(button);
							buttonPanel.paintComponents(buttonPanel.getGraphics());
							buttonPanel.repaint();
						}
						
					});
					
					buttonPanel.add(button);
				}
			}
		
	    add(panel,BorderLayout.NORTH);
	    JPanel southPanel = new JPanel();
	    
	    JButton okButton = new JButton();
	    okButton.setText("OK");
	    okButton.setBackground(new java.awt.Color(102,255,102));
	    okButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
	    okButton.setSize(new Dimension(100,80));
	    okButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					save();
					dispose();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	    	
	    });
	    southPanel.add(okButton);
	    
	    JButton cancelButton = new JButton();
	    southPanel.setPreferredSize(new Dimension(800,100));
	    cancelButton.setText("ABBRECHEN");
	    cancelButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
	    cancelButton.setBackground(new java.awt.Color(255,153,153));
	    cancelButton.setSize(new Dimension(100,80));
	    cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dispose();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	    	
	    });
	    southPanel.add(cancelButton);
	    southPanel.setBackground(new java.awt.Color(209,222,235));
	    panel.add(southPanel,BorderLayout.SOUTH);
	}
	public class ImagePanel extends JPanel
	{
	private static final long serialVersionUID = 1L;
	private Image image = null;
	private int iWidth2;
	private int iHeight2;

	public ImagePanel(Image image)
	{
		
	    this.image = image;
	    this.iWidth2 = image.getWidth(this)/2;
	    this.iHeight2 = image.getHeight(this)/2;
	}


	public void paintComponent(Graphics g)
	{
	    super.paintComponent(g);
	    if (image != null)
	    {
	        int x = this.getParent().getWidth()/2 - iWidth2;
	        int y = this.getParent().getHeight()/2 - iHeight2;
	        g.drawImage(image,x,y,this);
	    }
	}
	}
	
	public boolean save() throws Exception {
		
		for(Iterator<ShopTable> itr = tableList.iterator(); itr.hasNext();)
		{
			ShopTable table = itr.next();
			String tableNr = table.getNumber()+"";
			ShopTable shopTable = ShopTableDAO.getInstance().getByNumber(tableNr.trim());
			if(shopTable == null)
			{
				shopTable = new ShopTable();
				shopTable.setNumber(tableNr);
				shopTable.setX(table.getX());
				shopTable.setY(table.getY());
				shopTable.setFloor(2);
				ShopTableDAO.getInstance().save(shopTable);
			}
			else
			{
				shopTable.setX(table.getX());
				shopTable.setY(table.getY());
				ShopTableDAO.getInstance().saveOrUpdate(shopTable);
			}
		}
		
		return true;
	}
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		final int x = evt.getX();
		final int y = evt.getY();
		
		if(SwingUtilities.isRightMouseButton(evt))
		{
			if(moveButton == null)return;
			int tableNumber = Integer.parseInt(moveButton.getText());
			if((tableNumber == 99)||(tableNumber == 98))
			{
				moveButton.setBounds(x-30, y-60, 100, 100);
				moveButton.setBackground(new java.awt.Color(102,178,255));
			}
			else
			{
				moveButton.setBounds(x-30, y-60, 60, 60);
				moveButton.setBackground(new java.awt.Color(102,255,102));
			}
			buttonPanel.add(moveButton);
			
			buttonPanel.paintComponents(buttonPanel.getGraphics());
			buttonPanel.repaint();
			ShopTable table = new ShopTable();
			table.setNumber(Integer.parseInt(moveButton.getText())+"");
			table.setX(x);
			table.setY(y);
			tableList.add(table);
			moveButton = null;
			return;
			
		}
		else if(moveButton != null)
		{
			moveButton = null;
		}

		
		int tableNumber = NumberSelectionDialog2.take(this,"Hinzufuegen Sie die Tische");
		if(tableNumber == 0)return;
		
		final PosButton button = new PosButton(tableNumber+"");
		
		if((tableNumber == 99)||(tableNumber == 98))
		{
			button.setBounds(x-30, y-60, 100, 100);
			button.setBackground(new java.awt.Color(102,178,255));
		}
		else
		{
			button.setBounds(x-30, y-60, 60, 60);
			button.setBackground(new java.awt.Color(102,255,102));
		}
		
		button.setBounds(x-30, y-60, 60, 60);
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeButton(Integer.parseInt(button.getText()));
				int tableNumber = NumberSelectionDialog2.take(BackOfficeWindow.getInstance(),"Hinzufuegen Sie die Tische");
				if(tableNumber == 0)return;
				
				button.setText(tableNumber+"");
				ShopTable table = new ShopTable();
				table.setNumber(tableNumber+"");
				table.setX(x);
				table.setY(y);
				tableList.add(table);
			}
			
		});
		ShopTable table = new ShopTable();
		table.setNumber(tableNumber+"");
		table.setX(x);
		table.setY(y);
		tableList.add(table);
		buttonPanel.add(button);
		
		buttonPanel.paintComponents(buttonPanel.getGraphics());
		buttonPanel.repaint();
		
		panel.repaint();
		repaint();
	}
	public void removeButton(int tableNr)
	{
		int index = 0;
		int found = 0;
		for(Iterator<ShopTable> itr = tableList.iterator(); itr.hasNext();)
		{
			ShopTable table = itr.next();
			if(table.getNumber().compareTo(tableNr+"") == 0)
			{
				found = 1;
				break;
			}
			index++;
		}
		if(found == 1)
			tableList.remove(index);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
