package com.floreantpos.ui.dialog;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

public class RoundButton extends JButton {
	  public RoundButton(String label) {
	    super(label);
	  }

	 public RoundButton() {
		// TODO Auto-generated constructor stub
	}

	protected void paintComponent(Graphics g) {
	    
		g.setColor(getBackground());
	    g.fillOval(10, 10, getSize().width-20, 
	      getSize().height-20);
	    
	    super.paintComponent(g);
	 }
	  protected void paintBorder(Graphics g) {
		
		 g.setColor(new Color(51,25,0)); 
		 //g.drawOval(0, 0, getSize().width+10, 
	     // getSize().height+10);
	  }
	  Shape shape;
	  public boolean contains(int x, int y) {
	
	    if (shape == null || 
	      !shape.getBounds().equals(getBounds())) {
	      shape = new Ellipse2D.Float(0, 0, 
	        getWidth(), getHeight());
	    }
	    
	    return shape.contains(x, y);
	  }

	  
	  
}
