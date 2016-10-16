/*
 * Created on 27-08-2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces.kettle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;

import cl.jumbo.interfaces.ControladorProcesos;
import cl.jumbo.interfaces.ProcesoInterfaz;
import cl.jumbo.interfaces.Status;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class KettleProcess  extends Thread implements ProcesoInterfaz{

	protected ControladorProcesos controlador = null;
	protected Status status = null;
	protected Vector statusHistory = new Vector();
	protected boolean stopFlag = false;
	protected boolean sleepFlag = false;
	protected boolean pauseFlag = false;
	protected long timeSleep = 0;
	protected String kettleScript = "";
	private Logger logger;

	public KettleProcess(ControladorProcesos controlador, String kettleScript) {
		super();
		this.controlador = controlador;
		this.kettleScript = kettleScript;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#runProcess()
	 */
	public void runProcess() {
		start();
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toStop()
	 */
	public void toStop() {
		stopFlag = true;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#getStatusHistory()
	 */
	public Vector getStatusHistory() {
		return statusHistory;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#getStatus()
	 */
	public Status getStatus() {
		return this.status;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toPause()
	 */
	public void toPause() {
		pauseFlag = true;
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toResume()
	 */
	public void toResume() {
		pauseFlag = false;
		notify();
	}

	/* (non-Javadoc)
	 * @see cl.jumbo.interfaces.ProcesoInterfaz#toSleep(long)
	 */
	public void toSleep(long time) {
		timeSleep = time;
		sleepFlag = true;
		if (time == 0)
			sleepFlag = false;
	}

	private void setStatus(Status status) {
		this.status = status;
		statusHistory.add(0, status);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String comando = this.kettleScript;
		String command[] = comando.split("\\s");
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			String s;
			for (s = null; (s = stdInput.readLine()) != null;) {
				if (stopFlag) {
					p.destroy();
					return;
				}
			}

			while ((s = stdError.readLine()) != null) {
			}
			
			logger.debug("Fin proceso...");
			p.waitFor();
			if (p.exitValue() == 0) {
				setStatus(new Status(0, "Proceso ejecutado exitosamente"));
				this.controlador.setStatusHistory(this.statusHistory);
				logger.debug("Proceso ejecutado exitosamente");
			} else {
				setStatus(new Status(-1, "Proceso finalizado con errores"));
				this.controlador.setStatusHistory(this.statusHistory);
				logger.debug("Proceso finalizado con errores");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
