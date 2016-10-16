package cl.cencosud.jumbo.iterator;

import java.util.Calendar;
import java.util.Date;

import calendrica.Gregorian;
import calendrica.ProtoDate;
import calendrica.Time;
import cl.cencosud.jumbo.scheduler.ScheduleIterator;

public class SunsetIterator implements ScheduleIterator {

    private final double latitude;
    private final double longitude;
    private final Calendar calendar = Calendar.getInstance();

    public SunsetIterator(double latitude, double longitude) {

        this(latitude, longitude, new Date());
    }

    public SunsetIterator(double latitude, double longitude, Date date) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.calendar.setTime(date);
        Date sunset = calculateSunset(latitude, longitude, date);
        if ( !sunset.before(date) )
            this.calendar.add(5, -1);
    }

    private static Date calculateSunset( double latitude, double longitude, Date date ) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int fixedDate = Gregorian.toFixed(calendar.get(2) + 1, calendar.get(5), calendar.get(1));

        int offset = -calendar.getTimeZone().getOffset(calendar.get(0), calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(7),
                calendar.get(11) * 60 * 60 * 1000 + calendar.get(12) * 60 * 1000 + calendar.get(13) * 1000 + calendar.get(14)) / 60000;

        Time time = new Time(ProtoDate.standardFromLocal(ProtoDate.sunset(fixedDate, latitude, longitude), offset));
        calendar.set(11, time.hour);
        calendar.set(12, time.minute);
        calendar.set(13, (int) time.second);
        calendar.set(14, (int) Math.round(time.second * 1000.0D % 1000.0D));
        return calendar.getTime();
    }

    public Date next() {

        this.calendar.add(5, 1);
        return calculateSunset(this.latitude, this.longitude, this.calendar.getTime());
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.iterator.SunsetIterator JD-Core Version: 0.6.0
 */