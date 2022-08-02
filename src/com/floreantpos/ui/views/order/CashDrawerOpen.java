package com.floreantpos.ui.views.order;

public class CashDrawerOpen {
	
	static CashDrawerOpen cashDrawer;
	CashDrawerOpen()
	{}
	
	public void openCashDrawer()
	{
		try{
			
//			CashDrawer drawer = Application.getDrawer();
//			if(drawer == null)return;
//			drawer.open("CashDrawer");
//			drawer.claim(10000);
//			drawer.setDeviceEnabled(true);
//			if (drawer.getDrawerOpened() == true)
//			{
//				System.out.println("cashDrawer.getDrawerOpened() == true");
//				drawer.close();
//				return;
//			}
//			else
//			{
//				System.out.println("cashDrawer.getDrawerOpened() == false");
//			}
//			drawer.openDrawer();
//			drawer.close();
        }
		catch(Exception e){e.printStackTrace();}
	}
	public static CashDrawerOpen getInstance()
	{
		if(cashDrawer == null)
		{
			cashDrawer = new CashDrawerOpen();
			return cashDrawer;
		}
		return cashDrawer;
	}
}
