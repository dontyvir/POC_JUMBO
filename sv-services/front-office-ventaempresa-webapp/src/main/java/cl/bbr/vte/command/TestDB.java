package cl.bbr.vte.command;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.EmpresasDTO;

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
	 * @throws SystemException 
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1) throws SystemException {

		// Se recupera la salida para el servlet
		PrintWriter out = null;
		try {
			out = arg1.getWriter();
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}

		out.print("<p>inicio prueba</p>");	
		
		
		BizDelegate biz = new BizDelegate();
		EmpresasDTO emp = new EmpresasDTO();
		long rut = Long.parseLong("12");
		emp = null;//biz.EmpresasGetById(rut);
		if(emp!=null){
			out.println("RAZON  = " + emp.getEmp_rzsocial());
			out.println("<br>CALLE  = " + emp.getEmp_rut());
			System.out.print("RAZON  = " + emp.getEmp_rzsocial());
			System.out.print("CALLE  = " + emp.getEmp_rut());
		}
		//arg1.sendRedirect(mar.getUrl());
		/*try {
			List list_prod = biz.EmpresasGetByRut("12");
			this.getLogger().debug("Size:" + list_prod.size());
		} catch (SystemException e) {
			throw new CommandException(e);
		}
		
		/*
		try {
			List list_prod = biz.productosGetProductos( "1", 4, 100, "", "" );
			this.getLogger().debug("Size:" + list_prod.size());
		} catch (SystemException e) {
			throw new CommandException(e);
		}
		
		
		
		out.println( "-->" + Utils.redondear(0.33) + "<---<br>" );
		out.println( "-->" + Utils.redondear(0.63) + "<---<br>" );
		out.println( "-->" + Utils.redondear(1.33) + "<---<br>" );
		out.println( "-->" + Utils.redondear(1.63) + "<---<br>" );
		out.println( "-->" + Utils.redondear(4.77) + "<---<br>" );
		*/
		out.print("<p>fin prueba</p>");

	}
	
}