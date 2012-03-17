package com.testerschoice.moneybook;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MoneyBook {

	public static final String AUTHORITY = "com.testerschoice.provider.MoneyBook";
	
	public static final class MoneyBookColumns implements BaseColumns{
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/lists");
		
		public static final String ITEM = "item";
		
		public static final String ITEM_PRICE = "price";
		
		public static final String PURCHASE_DATE_YEAR = "year";
		
		public static final String PURCHASE_DATE_MONTH = "month";
		
		public static final String PURCHASE_DATE_DAY = "day";
		
		public static final String DEFAULT_SORT_ORDER = "year DESC";
	}
}
