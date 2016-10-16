/*
 * Created on 14-11-2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.jumbo.interfaces.kettle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import cl.jumbo.interfaces.ControladorProcesos;
import cl.jumbo.interfaces.Status;

/**
 * @author jvillalobos
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CargaVentasClientes extends KettleProcess {

	protected final static Logger logger = Logger
	.getLogger(CargaVentasClientes.class);

	/**
	 * @param controlador
	 * @param kettleScript
	 */
	public CargaVentasClientes(ControladorProcesos controlador) {
		super(controlador, KettleScripts.CARGA_VENTAS_CLIENTES);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		String comando = this.kettleScript + " bodba db06bo vthdba db05vth";
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
			
			//System.out.println("Fin proceso...");
			p.waitFor();
			if (p.exitValue() == 0) {
				setStatus(new Status(0, "Proceso ejecutado exitosamente"));
				this.controlador.setStatusHistory(this.statusHistory);
				logger.info("Proceso ejecutado exitosamente");
				//System.out.println("Proceso ejecutado exitosamente");
			} else {
				setStatus(new Status(-1, "Proceso finalizado con errores"));
				this.controlador.setStatusHistory(this.statusHistory);
				logger.error("Proceso finalizado con errores");
				//System.out.println("Proceso finalizado con errores");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void setStatus(Status status) {
		this.status = status;
		statusHistory.add(0, status);
	}

}
