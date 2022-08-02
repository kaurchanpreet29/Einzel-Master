package com.floreantpos.model;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

@XmlRootElement(name = "Reservations")
public class ReservationDB {
	private List<Reservation> reservation;

	public void setReservation(List<Reservation> reservation) {
		this.reservation = reservation;
	}
	
	public List<Reservation> getReservation()
	{
		return this.reservation;
	}
	public static void save(ReservationDB reservationDb) {
	try {

		System.out.println("Saving data");
		File file = new File("config", "Reservations.xml");
		
		JAXBContext jaxbContext = JAXBContext.newInstance(ReservationDB.class);
		Marshaller m = jaxbContext.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		StringWriter writer = new StringWriter();
		m.marshal(reservationDb, writer);

		FileUtils.write(file, writer.toString());

	} catch (Exception e) {
		e.printStackTrace();
	}
	}

	public static ReservationDB loadReservationList() 
	{
		try {

			File file = new File("config", "Reservations.xml");
				
			if (!file.exists()) {	
				return null;
			}

			FileReader reader = new FileReader(file);

			JAXBContext jaxbContext = JAXBContext.newInstance(ReservationDB.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			ReservationDB reservationDB = (ReservationDB) unmarshaller.unmarshal(reader);
			
			return reservationDB;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void deleteReservations(Date date)
	{
		ReservationDB reservationDb = loadReservationList();
		if(reservationDb == null)
			return;
		List<Reservation> reservationList = reservationDb.getReservation();
		if(reservationList == null)return;
		List<Reservation> tempList = new ArrayList();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		for(Iterator<Reservation> itr = reservationList.iterator();itr.hasNext();)
		{
			Reservation reservation = (Reservation)itr.next();
			
			if(reservation.getDate().compareTo(dateFormat.format(date)) == 0)
			{}
			else
				tempList.add(reservation);
		}
		reservationDb.setReservation(tempList);
		save(reservationDb);
		
	}
}
