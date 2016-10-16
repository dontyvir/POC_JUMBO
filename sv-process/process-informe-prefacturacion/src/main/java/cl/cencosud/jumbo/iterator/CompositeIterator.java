package cl.cencosud.jumbo.iterator;

import cl.cencosud.jumbo.scheduler.ScheduleIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CompositeIterator implements ScheduleIterator {

    private List orderedTimes = new ArrayList();
    private List orderedIterators = new ArrayList();

    public CompositeIterator(ScheduleIterator[] scheduleIterators) {

        for ( int i = 0; i < scheduleIterators.length; i++ )
            insert(scheduleIterators[i]);
    }

    private void insert( ScheduleIterator scheduleIterator ) {

        Date time = scheduleIterator.next();
        if ( time == null ) {
            return;
        }
        int index = Collections.binarySearch(this.orderedTimes, time);
        if ( index < 0 ) {
            index = -index - 1;
        }
        orderedTimes.add(index, time);
        orderedIterators.add(index, scheduleIterator);
    }

    public synchronized Date next() {

        Date next = null;
        while ( ( !orderedTimes.isEmpty() ) && ( ( next == null ) || ( next.equals(this.orderedTimes.get(0)) ) ) ) {
            next = (Date) orderedTimes.remove(0);
            insert( (ScheduleIterator) orderedIterators.remove(0) );
        }
        return next;
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.iterator.CompositeIterator JD-Core Version: 0.6.0
 */