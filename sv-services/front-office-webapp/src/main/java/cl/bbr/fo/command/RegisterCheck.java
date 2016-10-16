package cl.bbr.fo.command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.clientes.dto.ClienteDTO;

/**
 * Comando que revisa si existe el cliente.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class RegisterCheck extends Command {

	/**
	 * Revisa si existe el cliente. Retorna True o False.
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
		// Se recupera la salida para el servlet
		PrintWriter out = arg1.getWriter();

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		// recuperar identificador de la región de consulta		
		long reg_id  = Long.parseLong(arg0.getParameter("regRut").replaceAll("\\.",""));
		
		ClienteDTO rutaux = (ClienteDTO)biz.clienteGetByRut( reg_id );
		
		if ( rutaux != null ) {
			out.print( "true" );
		} else {
			out.print( "false" );
		}
	}

}