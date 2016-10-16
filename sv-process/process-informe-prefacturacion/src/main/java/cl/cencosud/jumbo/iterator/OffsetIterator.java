package cl.cencosud.jumbo.iterator;

import cl.cencosud.jumbo.scheduler.ScheduleIterator;
import java.util.Calendar;
import java.util.Date;

public class OffsetIterator implements ScheduleIterator {

    private final ScheduleIterator scheduleIterator;
    private final int field;
    private final int offset;
    private final Calendar calendar = Calendar.getInstance();

    public OffsetIterator(ScheduleIterator scheduleIterator, int field, int offset) {

        this.scheduleIterator = scheduleIterator;
        this.field = field;
        this.offset = offset;
    }

    public Date next() {

        Date date = scheduleIterator.next();
        if ( date == null ) {
            return null;
        }
        calendar.setTime(date);
        calendar.add(this.field, this.offset);
        return calendar.getTime();
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.iterator.OffsetIterator JD-Core Version: 0.6.0
 */