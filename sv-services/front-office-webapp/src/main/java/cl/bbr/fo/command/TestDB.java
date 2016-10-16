package cl.bbr.fo.command;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.exception.CommandException;
import cl.bbr.jumbocl.common.utils.Utils;

/**
 * Servlet que permite realizar TEST
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class TestDB extends Command {

	/**
	 * Permite realizar TEST
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws CommandException 
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws CommandException {

		// Se recupera la salida para el servlet
		PrintWriter out = null;
		try {
			out = arg1.getWriter();
		} catch (IOException e) {
			this.getLogger().error(e);
		}

		out.print("<p>inicio prueba</p>");	
		
		/*
		BizDelegate biz = new BizDelegate();
		try {
			List list_prod = biz.productosGetProductos( "1", 4, 100, "", "" );
			this.getLogger().debug("Size:" + list_prod.size());
		} catch (SystemException e) {
			throw new CommandException(e);
		}
		*/
		
		out.println( "-->" + Utils.redondearFO(0.33) + "<---<br>" );
		out.println( "-->" + Utils.redondearFO(0.63) + "<---<br>" );
		out.println( "-->" + Utils.redondearFO(1.33) + "<---<br>" );
		out.println( "-->" + Utils.redondearFO(1.63) + "<---<br>" );
		out.println( "-->" + Utils.redondearFO(4.77) + "<---<br>" );
		
		out.print("<p>fin prueba</p>");

	}
	
}