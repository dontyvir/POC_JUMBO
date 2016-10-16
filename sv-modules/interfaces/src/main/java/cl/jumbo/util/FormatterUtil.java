package cl.jumbo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatterUtil {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437208809707237054L;
	private String datePattern = "dd/MM/yyyy";
	private String timePattern = "HH:mm:ss";
	
	public FormatterUtil () {
	}
	
	public FormatterUtil (String datePattern) {
		this.datePattern = datePattern;
	}

	public FormatterUtil (String datePattern, String timePattern) {
		this.datePattern = datePattern;
		this.timePattern = timePattern;
	}

	public String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.format(date);
	}
	
	public String formatDate(java.sql.Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return sdf.format(new Date(date.getTime()));
	}
	
	public String formatTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
		return sdf.format(date);
	}

	public String formatFullDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern + " " + timePattern);
		return sdf.format(date);
	}
	
	public Date getDate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		return new Date(sdf.parse(date).getTime());
	}
	
	public boolean isValidDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		    sdf.setLenient(false);
		    Date dt = sdf.parse(date);
		} catch (ParseException e1) {
			return false;
		}
		
		return true;
	}

	public boolean isValidDateAndTime(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern + " " + timePattern);
		    sdf.setLenient(false);
		    Date dt = sdf.parse(date);
		} catch (ParseException e1) {
			return false;
		}
		
		return true;
	}
}
	
