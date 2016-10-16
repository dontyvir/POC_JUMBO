package cl.cencosud.jumbo.scheduler;

import java.util.TimerTask;

public abstract class SchedulerTask implements Runnable {

    final Object lock = new Object();

    int state = 0;
    static final int VIRGIN = 0;
    static final int SCHEDULED = 1;
    static final int CANCELLED = 2;
    TimerTask timerTask;

    public abstract void run();

    public boolean cancel() {

        synchronized ( this.lock ) {
            if ( this.timerTask != null ) {
                this.timerTask.cancel();
            }
            boolean result = this.state == 1;
            this.state = 2;
            return result;
        }
    }

    public long scheduledExecutionTime() {

        synchronized ( this.lock ) {
            return this.timerTask == null ? 0L : this.timerTask.scheduledExecutionTime();
        }
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.scheduler.SchedulerTask JD-Core Version: 0.6.0
 */