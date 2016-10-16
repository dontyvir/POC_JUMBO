package cl.bbr.bol.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.switchTBK.ClienteSwitch;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * AddDirDespacho Comando Process
 * Agrega una direccion de despacho
 * 
 * @author BBRI
 */

public class Switch extends Command {
	private final static long serialVersionUID = 1;
 
	long id_pedido = -1;


    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

        // 2. Procesa parámetros del request
        logger.debug("Procesando parámetros...");

        // 2.1 revision de parametros obligatorios
        if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}

        // 2.2 obtiene parametros desde el request
        id_pedido = Long.parseLong(req.getParameter("id_pedido"));

        // 2.3 log de parametros y valores
        logger.debug("id_pedido: " + id_pedido);

     
 		ForwardParameters fp = new ForwardParameters();
	  	fp.add( req.getParameterMap() );
	  	
        /*
         * 3. Procesamiento Principal
         */
	  	
	  	System.out.println("getRemoteHost: " + req.getRemoteHost());
	  	System.out.println("getServerName: " + req.getServerName());
	  	
	  	
	  	if (req.getRemoteHost().equals("localhost") || 
	  	      req.getRemoteHost().equals("g500603ws189.cencosud.corp") || // PC Richard
	  	        req.getRemoteHost().equals("p500603ws075.cencosud.corp")){ //PC Ivan
	  	    
		  	ClienteSwitch cliSwitch = new ClienteSwitch();
	        if (cliSwitch.insertaTrxSwitch(id_pedido)){
	            System.out.println("Registro Insertado Correctamente");
	        }else{
	            System.out.println("Error en la Inserción del Registro");
	        }
	  	}
    }
}
