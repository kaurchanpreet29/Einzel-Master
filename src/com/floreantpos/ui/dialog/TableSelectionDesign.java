package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.ShopTable;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.ShopTableDAO;
import com.floreantpos.swing.PosButton;
public class TableSelectionDesign extends POSDialog{
	ImagePanel panel;
	JPanel buttonPanel;
	Ticket ticket;
	private int selectedTable;
	boolean change;
	private DefaultListModel<ShopTable> addedTableListModel = new DefaultListModel<ShopTable>();
	private JList<ShopTable> addedTableList = new JList<ShopTable>(addedTableListModel);
	public TableSelectionDesign(boolean change) throws IOException
	{
		initComponents();
		this.change = change;
	}
	public void selectTable(int number)
	{
		int tableNr = number;
		setSelectedTable(tableNr);
		
		if (addTable(tableNr)) {
			doOk();
		}
		
	}
	private void doOk() {
		setCanceled(false);
		dispose();
	}

	public void setTicket(Ticket ticket)
	{
		this.ticket = ticket;
	}
	
	public int getSelectedTable()
	{
		return selectedTable;
	}
	public void setSelectedTable(int selectedTable)
	{
		this.selectedTable = selectedTable;
	}
	
	public void initComponents() throws IOException
	{
		setTitle("Waehlen Sie den Tisch");
		this.setResizable(false);
		Image image = ImageIO.read(new File("resources/images/floorPlan.png"));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800,600));
		panel = new ImagePanel(image);
		panel.setPreferredSize(new Dimension(800,600));
		getContentPane().setBackground(new Color(209,222,235));
		setBackground(new Color(209,222,235));
		panel.setBackground(new Color(209,222,235));
		panel.setOpaque(false);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(null);
		buttonPanel.setPreferredSize(new Dimension(800,600));
		buttonPanel.setBackground(new Color(209,222,235));
	
		
		List<ShopTable> tableList = ShopTableDAO.getInstance().getByFloor(1);
			if(tableList != null)
			{
				for(Iterator<ShopTable> itr = tableList.iterator();itr.hasNext();)
				{
					ShopTable table = itr.next();
					JButton button = new RoundButton();
					button.setText(table.getNumber()+"");
					button.setFont(new Font("Times New Roman", Font.BOLD, 20));
					
					if((table.getNumber().compareTo("99") == 0))
					{
						button.setBounds(table.getX()-28, table.getY()-115, 100,100);
						button.setBackground(new Color(102,178,255));
					}
					else if(table.getNumber().compareTo("98") == 0)
					{
						button.setBounds(table.getX()-28, table.getY()-115, 100,100);
						button.setBackground(new Color(233,245,185));
					}
					else if(table.getNumber().compareTo("999") == 0)
					{
						button.setBackground(new Color(202,164,245));
						button.setBounds(table.getX()-28, table.getY()-115, 60,60);
					}
					else
					{
						button.setBackground(new Color(102,255,102));
						button.setBounds(table.getX()-28, table.getY()-115, 60,60);
					}
					button.setBorderPainted(false);
					button.setContentAreaFilled(false);
					buttonPanel.add(button);
					
					final String tableNumber = table.getNumber()+"";
					if(table.isOccupied())
							button.setBackground(new Color(255,153,153));
					
					button.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent arg0) {
							
							int tableNr = Integer.parseInt(tableNumber.trim());
							setSelectedTable(tableNr);
							
							if (addTable(tableNr)) {
								doOk();
							}
							
						}
						
					});
				}
			}
		
		buttonPanel.setOpaque(false);
		panel.add(buttonPanel);
	    add(panel,BorderLayout.CENTER);
	}
	public List<ShopTable> getTables() {
		Enumeration<ShopTable> elements = this.addedTableListModel.elements();
		
		List<ShopTable> tables = new ArrayList<ShopTable>();

		while (elements.hasMoreElements()) {
			
			tables.add(elements.nextElement());
		}

		return tables;
	}

	private boolean addTable(int tableNr) {
		String tableNumber = tableNr+"";
		tableNumber = tableNumber.trim();
		
		if (StringUtils.isEmpty(tableNumber)) {
			POSMessageDialog.showError(this, "Bitte Tisch nummer einfügen");
			return false;
		}
		
		if(change)
		{
			if((tableNr == 99) || (tableNr == 98))
			{
				return false;
			}
				
		}

		Enumeration<ShopTable> elements = this.addedTableListModel.elements();
		
		String oldTableNumber ="";
		while (elements.hasMoreElements()) {
			 oldTableNumber = elements.nextElement().getNumber();
			 break;
		}
		ShopTable shopTable = ShopTableDAO.getInstance().getByNumber(tableNumber);
		
		if (shopTable == null) {
			shopTable = new ShopTable();
			shopTable.setNumber(tableNumber);
		}
		
		//Check for Edit/New ticket
		if(!change)
		{
			if (shopTable.isOccupied())//Its a Edit ticket 
			{
				change = false;
			    return true;
			}
			else
			{
				addedTableListModel.addElement(shopTable);//Create a new ticket
				change = false;
				return true;
			}
			
		}
		else
		{
			if (shopTable.isOccupied()) {
				POSMessageDialog.showError(this, "Tisch " + tableNumber + " belegt");
				return false;
			}
			ShopTable oldShopTable = ShopTableDAO.getInstance().getByNumber(oldTableNumber);
			if (oldShopTable != null)//Table change in a old ticket 
			{
				oldShopTable.setOccupied(false);
				addedTableListModel.removeElement(oldShopTable);
			}
			addedTableListModel.addElement(shopTable);
			change = false;
			return true;
		}
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
}
