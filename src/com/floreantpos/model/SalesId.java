package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.floreantpos.model.dao.VirtualPrinterDAO;

@XmlRootElement(name = "Sales")
public class SalesId {
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	/*public void save() {
		try {

			File file = new File("config", "Sales.xml");
			
			JAXBContext jaxbContext = JAXBContext.newInstance(SalesReport.class);
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			StringWriter writer = new StringWriter();
			m.marshal(this, writer);

			FileUtils.write(file, writer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static long getNewId() {
		try {

			File file = new File("config", "Sales.xml");
					
			if (!file.exists()) {
				return 0;
			}

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(SalesId.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			SalesId report = (SalesId) unmarshaller.unmarshal(reader);
			long value = report.id;
			
			report.id++;
			
			//Writing new value
			Marshaller m = jaxbContext.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();
			m.marshal(report, writer);

			FileUtils.write(file, writer.toString());
			
			return value;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}
}
