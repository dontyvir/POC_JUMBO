package cl.cencosud.jumbo.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Scheduler {

    private final Timer timer = new Timer();

    public void cancel() {

        this.timer.cancel();
    }

    public void schedule( SchedulerTask schedulerTask, ScheduleIterator iterator ) {

        Date time = iterator.next();
        if ( time == null )
            schedulerTask.cancel();
        else
            synchronized ( schedulerTask.lock ) {
                if ( schedulerTask.state != 0 ) {
                    throw new IllegalStateException("Task already scheduled or cancelled");
                }

                schedulerTask.state = 1;
                schedulerTask.timerTask = new SchedulerTimerTask(schedulerTask, iterator);
                this.timer.schedule(schedulerTask.timerTask, time);
            }
    }

    private void reschedule( SchedulerTask schedulerTask, ScheduleIterator iterator ) {

        Date time = iterator.next();
        if ( time == null )
            schedulerTask.cancel();
        else
            synchronized ( schedulerTask.lock ) {
                if ( schedulerTask.state != 2 ) {
                    schedulerTask.timerTask = new SchedulerTimerTask(schedulerTask, iterator);
                    this.timer.schedule(schedulerTask.timerTask, time);
                }
            }
    }

    class SchedulerTimerTask extends TimerTask {

        private SchedulerTask schedulerTask;
        private ScheduleIterator iterator;

        public SchedulerTimerTask(SchedulerTask schedulerTask, ScheduleIterator iterator) {

            this.schedulerTask = schedulerTask;
            this.iterator = iterator;
        }

        public void run() {

            this.schedulerTask.run();
            Scheduler.this.reschedule(this.schedulerTask, this.iterator);
        }
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.scheduler.Scheduler JD-Core Version: 0.6.0
 */