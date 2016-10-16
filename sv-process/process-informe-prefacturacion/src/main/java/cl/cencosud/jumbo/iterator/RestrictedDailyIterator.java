package cl.cencosud.jumbo.iterator;

import cl.cencosud.jumbo.scheduler.ScheduleIterator;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class RestrictedDailyIterator implements ScheduleIterator {

    
    private final int[] days;
    private final Calendar calendar = Calendar.getInstance();

    public RestrictedDailyIterator(int hourOfDay, int minute, int second, int[] days) {

        this(hourOfDay, minute, second, days, new Date());
    }

    public RestrictedDailyIterator(int hourOfDay, int minute, int second, int[] days, Date date) {

        
        this.days = ( (int[]) days.clone() );
        Arrays.sort(this.days);

        this.calendar.setTime(date);
        this.calendar.set(11, hourOfDay);
        this.calendar.set(12, minute);
        this.calendar.set(13, second);
        this.calendar.set(14, 0);
        if ( !this.calendar.getTime().before(date) )
            this.calendar.add(5, -1);
    }

    public Date next() {

        do
            this.calendar.add(5, 1);
        while ( Arrays.binarySearch(this.days, this.calendar.get(7)) < 0 );
        return this.calendar.getTime();
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.iterator.RestrictedDailyIterator JD-Core Version: 0.6.0
 */