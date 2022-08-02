package com.floreantpos.model;

import java.util.Comparator;

import com.floreantpos.model.base.BaseCashbook;

public class Cashbook extends BaseCashbook{
	/*[CONSTRUCTOR MARKER BEGIN]*/

	public static String PROP_ID = "id";
	public static String PROP_DATE = "date";
	public static String PROP_BESCHREIBUNG = "beschreibung";
	public static String PROP_KONTO = "konto";

	public Cashbook () {
		super();
	}
	public static class CashbookComparator implements Comparator {
		public CashbookComparator(){}
		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof Cashbook) || !(o2 instanceof Cashbook))
				throw new ClassCastException();

			Cashbook e1 = (Cashbook) o1;
			Cashbook e2 = (Cashbook) o2;
			if(e1.getDate() != null && e2.getDate() != null)
				return (int) (e1.getDate().compareTo(e2.getDate()));
			else
				return 0;
		}
	}
	/**
	 * Constructor for primary key
	 */
	public Cashbook (java.lang.Integer autoId) {
		super(autoId);
	}
	/**
	 * Constructor for required fields
	 */
	public Cashbook (
			java.lang.Integer autoId,
			java.util.Date date,
			java.lang.Double betrag,
			java.lang.String beschreibung,
			java.lang.String bezeichnung,
			java.lang.Boolean add
			) {
		super (
				autoId,
				date,
				betrag,
				beschreibung,
				bezeichnung,
				add
				);
	}	
}
