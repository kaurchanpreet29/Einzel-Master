package com.floreantpos.model;

import com.floreantpos.POSConstants;
import com.floreantpos.model.base.BaseCouponAndDiscount;



public class CouponAndDiscount extends BaseCouponAndDiscount {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public CouponAndDiscount () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public CouponAndDiscount (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public final static int FREE_AMOUNT = 0;
	public final static int FIXED_PER_CATEGORY = 1;
	public final static int FIXED_PER_ITEM = 2;
	public final static int FIXED_PER_ORDER = 3;
	public final static int PERCENTAGE_PER_CATEGORY = 4;
	public final static int PERCENTAGE_PER_ITEM = 5;
	public final static int PERCENTAGE_PER_ORDER = 6;
	public final static int GUTSCHEIN = 7;
	public final static int DIRECT_RABATT = 8;

	public final static String[] COUPON_TYPE_NAMES = { "Ohne Mindestbestellwert",  "Begrenzt auf eine Kategorie",
			 "Begrenzt auf einen Artikel", "Begrenzt auf eine Bestellung", "Prozente auf eine Kategorie",
			 "Prozente auf einen Artikel",  "Prozente auf eine Bestellung" };

	/*public final static String[] COUPON_TYPE_NAMES = { POSConstants.Coupn_type,  POSConstants.Coupn_type2,
			 POSConstants.Coupn_type3,  POSConstants.Coupn_type4,  POSConstants.Coupn_type5,
			 POSConstants.Coupn_type6,  POSConstants.Coupn_type7 };*/

	
	@Override
	public String toString() {
		return COUPON_TYPE_NAMES[getType()];
	}

}