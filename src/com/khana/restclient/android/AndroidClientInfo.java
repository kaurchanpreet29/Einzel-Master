package com.khana.restclient.android;

/**
 * @author Jyoti Rai
 *
 */

public class AndroidClientInfo extends BaseAndroidClientInfo{

	
		private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
		public AndroidClientInfo () {
			super();
		}

		/**
		 * Constructor for primary key
		 */
		public AndroidClientInfo (java.lang.Integer id) {
			super(id);
		}

	/*[CONSTRUCTOR MARKER END]*/

		public final static String CLIENT_ID = "clientId";//added
}
