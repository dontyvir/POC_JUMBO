package cl.jumbo.ventaondemand.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

//import cl.jumbo.ventaondemand.log.Logging;
import cl.jumbo.ventaondemand.web.command.Command;

public class Client extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private static final Logger logger = Logger.getLogger(Client.class);
	
	protected void execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {		
	}
}
