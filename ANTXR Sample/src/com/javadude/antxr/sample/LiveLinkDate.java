package com.javadude.antxr.sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * this class implements the format of Dates provided by LiveLink
 * created="2006-07-26T15:00:53"
 * @author Pastor Robert
 *
 */
public class LiveLinkDate {

	/**
	 * define the format of the date
	 */
	private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	private String llDate = "";
	private TimeZone tz = null;
	private GregorianCalendar calendar = null; 

	public LiveLinkDate (String strLLdate) {
		if (strLLdate.contains("T")) {
			this.llDate = strLLdate.replace('T', ' ');
			//System.out.println(this.llDate);
			// set time zone to use in interpreting the date
			this.tz = TimeZone.getDefault();
			// define the calendar
			this.calendar = new GregorianCalendar( this.tz );
			SDF_DATE.setCalendar( this.calendar );
			helper();
		}
	}

	private void helper () {

		Date date = null;
		try
		{
			// set time stamp
			date = SDF_DATE.parse(this.llDate);
			this.calendar.setTime(date);
			this.calendar.setTimeZone(this.tz);

		}
		catch ( ParseException e )
		{
			System.err.println( "LiveLinkDate: bad date format" );
			date = new Date();
			this.calendar.setTime(date);
		}
		//System.out.println("LiveLinkDate: "+this.calendar.getTime().toString());
	}

	@Override
	public String toString() {
		return this.calendar.getTime().toString();
	}

	public Date getDate() {
		return this.calendar.getTime();
	}

}
