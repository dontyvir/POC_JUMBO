package cl.cencosud.jumbo.iterator;

import cl.cencosud.jumbo.scheduler.ScheduleIterator;
import java.util.Calendar;
import java.util.Date;

public class DailyIterator implements ScheduleIterator {

    private final Calendar calendar = Calendar.getInstance();

    public DailyIterator(int hourOfDay, int minute, int second) {

        this(hourOfDay, minute, second, new Date());
    }

    public DailyIterator(int hourOfDay, int minute, int second, Date date) {

        this.calendar.setTime(date);
        this.calendar.set(11, hourOfDay);
        this.calendar.set(12, minute);
        this.calendar.set(13, second);
        this.calendar.set(14, 0);
        if ( !this.calendar.getTime().before(date) )
            this.calendar.add(5, -1);
    }

    public Date next() {

        this.calendar.add(5, 1);
        return this.calendar.getTime();
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.iterator.DailyIterator JD-Core Version: 0.6.0
 */