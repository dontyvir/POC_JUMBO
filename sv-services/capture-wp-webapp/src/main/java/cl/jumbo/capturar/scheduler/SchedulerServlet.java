/*
 * Creado el 05-11-2010
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.jumbo.capturar.scheduler;

import java.io.IOException;
//import java.util.Timer;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.jumbo.capturar.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.capturar.command.CapturarReserva;
import cl.jumbo.capturar.exceptions.FuncionalException;

public class SchedulerServlet extends HttpServlet implements Servlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Logging logger = new Logging(this);
	
	protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {						
		BizDelegate bizDelegate = null;
		try {
			logger.debug("Entro a ejecutar Servlet de captura");
			bizDelegate = new BizDelegate();
			ParametroDTO parametroDTO  = bizDelegate.getParametroByName("CAPTURA_WEB");
			//long interval = Long.parseLong(config.getInitParameter("interval")) * 1000;
			if(parametroDTO.getValor().equalsIgnoreCase("TRUE")){
				CapturarReserva action = new CapturarReserva();
				action.run();			
				Thread.sleep(2000);					
			}				
		} catch (FuncionalException e) {
			logger.error("Error Funcional Servlet de captura, " + e);
			e.printStackTrace();			
		} catch (Exception e) {
			logger.error("Error Servlet de captura, " + e);
			e.printStackTrace();
		}finally{
		}
		//arg1.sendRedirect("fin_ejecucion.html");
		arg1.sendRedirect(arg0.getContextPath()+"/fin_ejecucion.html");
	}

	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		doGet(arg0, arg1);
	}

	public void init(ServletConfig config) throws ServletException {				
		/*
		long interval = Long.parseLong(config.getInitParameter("interval")) * 1000;
		CapturarReserva action = new CapturarReserva();
		action.run();
		Timer timer = new Timer();
		timer.schedule(action, 10000, interval);
		*/
		//timer.scheduleAtFixedRate(action, 7000, interval);
	}
}