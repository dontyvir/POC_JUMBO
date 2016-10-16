package cl.cencosud.jumbo.home;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TestCalendar {

	public TestCalendar() {
	}

	public static void main(String args[]) {
		System.out
				.println("******************************************************************");
		Date now = new Date();
		System.out.println("Ahora : " + now);
		long l = System.currentTimeMillis();
		Date now1 = new Date(l);
		System.out.println("Ahora currentTimeMillis : " + now1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		System.out.println("[Dia] : " + cal.get(5));
		System.out.println("TIME : " + cal.get(10) + " : " + cal.get(12)
				+ " : " + cal.get(13));
		System.out
				.println("********************** (java.sql.Date) *************************");
		java.sql.Date sql = new java.sql.Date(now.getTime());
		System.out.println("Date dd/mm/YYYY (sql) => " + sql);
		Time time = new Time(now.getTime());
		System.out.println("Time HH:mm:ss (sql) =>  " + time);
		Timestamp timestamp = new Timestamp(now.getTime());
		System.out.println("TimesTamp dd/mm/YYYY HH:mm:ss (sql) =>  "
				+ timestamp);
		System.out
				.println("********************** TimeZone *************************");
		Calendar fechaxTZ = Calendar.getInstance();
		System.out.println("GET Time ZONE :" + fechaxTZ.getTimeZone());
		TimeZone tz = TimeZone.getTimeZone("AST");
		fechaxTZ.setTimeZone(tz);
		System.out.println("Time ZONE :" + fechaxTZ.getTimeZone());
		TimeZone timeZone1 = TimeZone.getTimeZone("America/Los_Angeles");
		TimeZone timeZone2 = TimeZone.getTimeZone("AST");
		TimeZone timeZone3 = TimeZone.getTimeZone("America/Santiago");
		Calendar calendar1 = new GregorianCalendar();
		Calendar calendar2 = new GregorianCalendar();
		Calendar calendar3 = new GregorianCalendar();
		calendar1.setTimeZone(timeZone1);
		calendar2.setTimeZone(timeZone2);
		calendar2.setTimeZone(timeZone3);
		System.out.println("__________________________________________");
		long timeLA = calendar1.getTimeInMillis();
		System.out.println("timeLA   = " + timeLA);
		System.out.println("hour     = " + calendar1.get(11) + ":"
				+ calendar1.get(12));
		System.out.println("__________________________________________");
		long timeCPH = calendar2.getTimeInMillis();
		System.out.println("timeAST  = " + timeCPH);
		System.out.println("hour     = " + calendar2.get(11) + ":"
				+ calendar2.get(12));
		System.out.println("__________________________________________");
		long timeCL = calendar3.getTimeInMillis();
		System.out.println("timeCL  = " + timeCL);
		System.out.println("hour     = " + calendar3.get(11) + ":"
				+ calendar3.get(12));
		System.out.println("__________________________________________");
		System.out
				.println("********************** Calendar *************************");
		Calendar fechax = Calendar.getInstance();
		System.out.println(fechax.getTime());
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf2.format(fechax.getTime()));
	}
}
