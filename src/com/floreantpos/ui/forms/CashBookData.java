package com.floreantpos.ui.forms;

import java.util.HashMap;
import java.util.Map;

public class CashBookData {
	
	static Map<String, CashBookIndex> data;

	public static Map<String, CashBookIndex> getData() {
		
		if(data == null)
		{
			data = new HashMap<String, CashBookIndex>();
			setData(data);
			populateData();
		}
		
		return data;
	}

	public static void setData(Map<String, CashBookIndex> data) {
		CashBookData.data = data;
	}
	
	public static void populateData()
	{
		String beschreibung = "0% Artikel +";
		addData(beschreibung, 0.00, 8200);
		beschreibung = "7% Artikel +";
		addData(beschreibung, 7.00, 8300);
		beschreibung = "Anfangbestand +";
		addData(beschreibung, 0.00, 9000);
		beschreibung = "Artikel +";
		addData(beschreibung, 19.00, 8402);
		beschreibung = "Aushilfslöhne -";
		addData(beschreibung, 0.00,4190);
		beschreibung = "Bankeinzahlung -";
		addData(beschreibung, 0.00,1360);
		beschreibung = "Betriebsbedarf -";
		addData(beschreibung, 19.00, 4980);
		beschreibung = "Bewirtungskosten -";
		addData(beschreibung, 19.00,4650);
		beschreibung = "Bewirtungskosten 7% +";
		addData(beschreibung, 7.00,4650);
		beschreibung = "Bezahlte kundenrechnungen +";
		addData(beschreibung, 0.00,1410);
		beschreibung = "Blumengeschenke bis 35, 7%  -";
		addData(beschreibung, 7.00, 4630);
		beschreibung = "Blumengeschenke über 35, 7%  -";
		addData(beschreibung, 7.00,4635);
		beschreibung = "Bürobedarf -";
		addData(beschreibung, 19.00, 4930);
		beschreibung = "Dekoration 19% -";
		addData(beschreibung, 19.00, 4611);
		beschreibung = "Dekoration 7% -";
		addData(beschreibung, 7.00, 4611);
		beschreibung = "Devisen -";
		addData(beschreibung, 0.00, 1362);
		beschreibung = "Einnahmen Leistungen Damen +";
		addData(beschreibung, 19.00, 8400);
		beschreibung = "Einnahmen Leistungen Herren +";
		addData(beschreibung, 19.00, 8401);
		beschreibung = "Fachzeitschriften/Büche -";
		addData(beschreibung, 19.00, 4940);
		beschreibung = "Fahrtkostenerstattung AN (Bus, Bahn) -";
		addData(beschreibung, 19.00, 4175);
		beschreibung = "Freiwillige soziale Aufwendungen -";
		addData(beschreibung, 0.00, 4141);
		beschreibung = "Gehälter -";
		addData(beschreibung, 0.00, 4120);
		beschreibung = "Geringweige wirtschaftsgüter -";
		addData(beschreibung, 19.00, 460);
		beschreibung = "Geschenke bis 35 -";
		addData(beschreibung, 19.00, 4630);
		beschreibung = "Geschenke über 35 -";
		addData(beschreibung, 19.00, 4635);
		beschreibung = "Gutscheineinlösung -";
		addData(beschreibung, 0.00, 8201);
		beschreibung = "Gutscheineinlösung Bonusartikel -";
		addData(beschreibung, 19.00, 8791);
		beschreibung = "Gutscheineinlösung verschenkt -";
		addData(beschreibung, 19.00, 8790);
		beschreibung = "Gutscheinverkauf +";
		addData(beschreibung, 0.00, 8201);
		beschreibung = "Instandhaltung betriebl. Räume -";
		addData(beschreibung, 19.00, 4260);
		beschreibung = "KFZ Repaturen -";
		addData(beschreibung, 19.00, 4540);
		beschreibung = "Kasse einlage +";
		addData(beschreibung, 0.00, 1360);
		beschreibung = "Kreditkartenbelege -";
		addData(beschreibung, 0.00, 1361);
		beschreibung = "Kreditkartengebühren -";
		addData(beschreibung, 19.00, 4970);
		beschreibung = "Kundenkonto -";
		addData(beschreibung, 0.00, 8405);
		beschreibung = "Kundenkonto +";
		addData(beschreibung, 0.00, 8405);
		beschreibung = "Kundenrechnungen -";
		addData(beschreibung, 0.00, 1410);
		beschreibung = "Laufene KFZ Betriebkosten -";
		addData(beschreibung, 19.00, 4530);
		beschreibung = "Löhne -";
		addData(beschreibung, 0.00, 4110);
		beschreibung = "Miete 0% -";
		addData(beschreibung, 0.00, 4210);
		beschreibung = "Miete 19% -";
		addData(beschreibung, 19.00, 4210);
		beschreibung = "Porto -";
		addData(beschreibung, 0.00, 4910);
		beschreibung = "Privateinlage +";
		addData(beschreibung, 0.00, 1890);
		beschreibung = "Privateinnahme -";
		addData(beschreibung, 0.00, 1800);
		beschreibung = "Reinigungsmittel -";
		addData(beschreibung, 19.00, 4250);
		beschreibung = "Reisekosten AN -";
		addData(beschreibung, 19.00, 4660);
		beschreibung = "Reisekosten unternehmer -";
		addData(beschreibung, 19.00, 4670);
		beschreibung = "Reparutaren Geschäftsausstattung -";
		addData(beschreibung, 19.00, 4805);
		beschreibung = "Reparutaren Machinen/Anlagen -";
		addData(beschreibung, 19.00, 4805);
		beschreibung = "Scheckeinreichnung -";
		addData(beschreibung, 0.00, 1362);
		beschreibung = "Sonstige Erlöse 19% +";
		addData(beschreibung, 19.00, 8640);
		beschreibung = "Sonstige Erlöse 7% +";
		addData(beschreibung, 7.00, 8630);
		beschreibung = "Sonstige kosten -";
		addData(beschreibung, 19.00, 4900);
		beschreibung = "Terminalzahlung -";
		addData(beschreibung, 0.00, 1361);
		beschreibung = "VWL -";
		addData(beschreibung, 0.00, 4170);
		beschreibung = "Wareneinkauf -";
		addData(beschreibung, 19.00, 3200);
		beschreibung = "Weiterbildung, AN -";
		addData(beschreibung, 19.00, 4140);
		beschreibung = "Weiterbildung unternehmer -";
		addData(beschreibung, 19.00, 4900);
		beschreibung = "Zeitung/Lesezirkel -";
		addData(beschreibung, 7.00, 4940);
		beschreibung = "Km Geld Erstattung -";
		addData(beschreibung, 0.00, 4668);
		beschreibung = "Werkzeug/Kleingeräte -";
		addData(beschreibung, 19.00, 4985);
	}
	
	public static void addData(String beschreibung, Double tax, int nr)
	{
		CashBookIndex index = new CashBookIndex();
		index.setNumber(nr);
		index.setTax(tax);
		data.put(beschreibung, index);
	}

	public static CashBookIndex getCashBookIndex(String beschreibung)
	{
		if(data == null)
		{
			data = new HashMap<String, CashBookIndex>();
			setData(data);
			populateData();
		}
		return data.get(beschreibung);
	}
}
