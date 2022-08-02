package com.floreantpos.ui.dialog;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Calendar;
import java.util.Date;

public class CallerDialogMain implements Runnable{

	String phone;
	String name;
	String address1;
	String address2;
	boolean cancelled;
	public CallerDialogMain(String phone,String name,String address1,String address2)
	{
		this.phone = phone;
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run()
	{
		CallerDialog dialog = new CallerDialog(phone,name,address1,address2);
		dialog.setPreferredSize(new Dimension(450,240));
		Date date = new Date();
		dialog.setTitle("Anruf Info: " + Calendar.getInstance().getTime() );
		
		int randomNum = (int)(Math.random()*400); 
		
		Point p = new Point(randomNum,randomNum);
		
		dialog.setLocation(p.x,p.y);
		dialog.pack();
		dialog.setVisible(true);
		if(dialog.isCancelled())
		{
			cancelled = true;
			dialog.dispose();
		}
		
		
    }
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
}
