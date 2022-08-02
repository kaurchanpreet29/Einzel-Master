package com.floreantpos.googlemaps;

import java.awt.Desktop;
import java.net.URI;
public class googleMap{
	public googleMap()
	{
		
	}
	public void getDirections(String src, String dest) {

		try {
        	
			String url = "https://www.google.com/maps?saddr="+src+"&daddr="+dest+"&ie=UTF8&directionsmode=driving&zoom=7&views=traffic";
			if (Desktop.isDesktopSupported())
        	    Desktop.getDesktop().browse(new URI(url));
			
			 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//"https://www.google.com/maps?saddr=India&daddr=Germany&directionsmode=driving&views=traffic&ie=UTF8&t=h&z=7&layer=t"
