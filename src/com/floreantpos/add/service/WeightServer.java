package com.floreantpos.add.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.khana.weight.management.ScaleWeight;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class WeightServer {
public static boolean isWeightAvailabel = false;
public static String weight;

public static String getWeight() {
	return weight;
}


public static void setWeight(String weightt) {
	weight = weightt;
}

public static ScaleWeight serial;
public static int count=1;

   

  public static WeightServer instance;
public static WeightServer getInstance() {
	if(instance==null)
	return instance = new WeightServer();
	else
		return instance;
}


public static void setWeightAvailabel(boolean isWeightAvailabel) {
	WeightServer.isWeightAvailabel = isWeightAvailabel;
}

	public WeightServer() {
        init();
    }
    
    public void init() {
    	try {
			HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/getWeight", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
    	}catch(Exception ex) {
    		System.out.println("Unable to start weight server");
    	}
    }

    static class MyHandler implements HttpHandler { 
    	
    	public void handle(HttpExchange t) throws IOException {
    		Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					serial = new ScaleWeight().getInstance();
					serial.StartScaleWeight();
		    		serial.reqImmediateWeights();					
				}
			});
    		thread.start();
    		try {
				thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
    		
        	String response = serial.getWeight();
        	System.out.println("Waiting "+response);
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
            count = 1;
        }
    }
    
    public static void main(String[] args) throws Exception {
    	instance = new WeightServer();
    }


}