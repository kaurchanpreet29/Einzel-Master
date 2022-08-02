package com.floreantpos.add.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.floreantpos.model.Restaurant;
import com.floreantpos.model.dao.RestaurantDAO;

public class ModifyIndexXml {


	public  void ModifyIndexXml() {
	
	}
		
		
		public void init(String path, String von, String bis) {
			
		
	   try {
		   ClassLoader classLoader = getClass().getClassLoader();
		   String path1 = new File(System.getProperty("user.dir"))+"/resources/index.xml";
		   File file = new File(path1);
		   
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(file.getPath());
		doc.getDocumentElement().normalize();	

		Restaurant rest = RestaurantDAO.getInstance().getRestaurant();
		
		// Get the root element
		Node company = doc.getFirstChild();

		Node dataSupplier = doc.getElementsByTagName("DataSupplier").item(0);

		NodeList listSup = dataSupplier.getChildNodes();
		for (int sup = 0; sup < listSup.getLength(); sup++) {
			 Node nodeSup = listSup.item(sup);
			   if ("Name".equals(nodeSup.getNodeName())) {
				   nodeSup.setTextContent(rest.getName());
			   }
			   if ("Location".equals(nodeSup.getNodeName())) {
				   nodeSup.setTextContent(rest.getAddressLine3());
			   }
		}
			



		Node media = doc.getElementsByTagName("Media").item(0);
		
		NodeList list = media.getChildNodes();

		for (int i = 0; i < list.getLength(); i++) {
			
                   Node node = list.item(i);

		   if ("Table".equals(node.getNodeName())) {
			   NodeList list1 = node.getChildNodes();
			   for (int ii = 0; ii < list1.getLength(); ii++) {					
                   Node node1 = list1.item(ii);
                   if ("Validity".equals(node1.getNodeName())) {
                	   NodeList list2 = node1.getChildNodes();
                	   for (int i1 = 0; i1 < list1.getLength(); i1++) {
                		   Node node5 = list2.item(i1);
                		   if(node5!=null) {
                			   if ("Range".equals(node5.getNodeName())) {
                            	   NodeList listSupw = node5.getChildNodes();
                       			for (int sup = 0; sup < listSupw.getLength(); sup++) {
                       				 Node nodeSup = listSupw.item(sup);
                       				   if ("From".equals(nodeSup.getNodeName())) {
                       					   nodeSup.setTextContent(von);
                       				   }
                       				   if ("To".equals(nodeSup.getNodeName())) {
                       					   nodeSup.setTextContent(bis);
                       				   }

                       			}   
                               }   
                		   }
                                         		                		   
                		  
                	   }
           		   }
			   }
		   }

		   
		}

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file.getPath()));
		transformer.transform(source, result);
		
		File fileToMove = new File(file.getPath());
		
		File destinationFolder = new File(path);
		System.out.println(destinationFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdirs();
		}
		
		copyFile(fileToMove, new File(destinationFolder + "\\" +fileToMove.getName()));
		 String path2 = new File(System.getProperty("user.dir"))+"/resources/gdpdu-01-08-2002.dtd";
		 Path sourceDirectory = Paths.get(path2);
	        Path targetDirectory = Paths.get(destinationFolder.getPath());
	        try {
	        	File fileToCopy = new File(path2);
	            File newFile = new File(destinationFolder.getPath()+"\\gdpdu-01-08-2002.dtd");	         
	            FileUtils.copyFile(fileToCopy, newFile);	         
	        }catch(Exception ex) {
	        	ex.printStackTrace();
	        }
	    
		System.out.println("Done");
		
		

	   } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	   } catch (TransformerException tfe) {
		tfe.printStackTrace();
	   } catch (IOException ioe) {
		ioe.printStackTrace();
	   } catch (SAXException sae) {
		sae.printStackTrace();
	   }
	}
		
		
		
		public static void copyFile(File source, File dest) throws IOException {
			 if(!dest.exists()) {
			  dest.createNewFile();
			 }
			 InputStream in = null;
			 OutputStream out = null;
			 try {
			  in = new FileInputStream(source);
			  out = new FileOutputStream(dest);
			  String header1 = "<!DOCTYPE DataSet SYSTEM \"gdpdu-01-08-2002.dtd\">\n";
			  out.write(header1.getBytes());
			    
			  // Transfer bytes from in to out
			  byte[] buf = new byte[1024];
			  int len;
			  int count = 0;
			  while ((len = in.read(buf)) > 0) {				
				out.write(buf, 0, len);
			  }
			 }
			 finally {
			  if(in != null) {
			   in.close();
			  }
			  if(out != null) {
			   out.close();
			  }
			 }
			}
}


