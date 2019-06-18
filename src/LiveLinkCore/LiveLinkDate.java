package LiveLinkCore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class implements the format of Dates provided by LiveLink
 * created="2006-07-26T15:00:53"
 * @author Pastor Robert
 * @since June 2009
 *
 */
public class LiveLinkDate {

	private static final Logger logger = Logger.getLogger(LiveLinkDate.class.getName()); 

	/**
	 * define the format of the date
	 */
	private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	
	private String llDate = "";
	private TimeZone tz = null;
	private GregorianCalendar calendar = null; 
	
	public LiveLinkDate (String strLLdate) {
		
		this.llDate = strLLdate.replace('T', ' ');
		//System.out.println(this.llDate);
		// set time zone to use in interpreting the date
		this.tz = TimeZone.getDefault();
		// define the calendar
		this.calendar = new GregorianCalendar( this.tz );
		SDF_DATE.setCalendar( this.calendar );
		helper();
	}
	
	public LiveLinkDate() {
		this.llDate = "";
		// set time zone to use in interpreting the date
		this.tz = TimeZone.getDefault();
		// define the calendar
		this.calendar = new GregorianCalendar( this.tz );
		SDF_DATE.setCalendar( this.calendar );
		helper();
		
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
			logger.log(Level.SEVERE,"bad date format: "+e.getLocalizedMessage() );
			date = new Date();
			this.calendar.setTime(date);
			this.calendar.setTimeZone(this.tz);

		}
	}
	
	@Override
	public String toString() {
		return this.calendar.getTime().toString();
	}
	
	public Date getDate() {
		return this.calendar.getTime();
	}
	
}
