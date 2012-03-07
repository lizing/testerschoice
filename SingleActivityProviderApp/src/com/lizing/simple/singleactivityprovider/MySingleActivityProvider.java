package com.lizing.simple.singleactivityprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class MySingleActivityProvider {

	public static final String AUTHORITY = "com.lizing.simple.singleactivityprovider";
	
	public static final class NoteColumns implements BaseColumns{
		
		//public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notes");
		
		public static final String NOTE = "NOTE";
	}
}
