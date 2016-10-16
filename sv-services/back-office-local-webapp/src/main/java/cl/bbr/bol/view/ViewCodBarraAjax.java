package cl.bbr.bol.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Despliega los codigos de barra
 * @author BBR
 *
 */
public class ViewCodBarraAjax extends Command {

 

 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr)
         throws Exception {
	 
		
		View salida = new View(res);
		 String codBarra = "";
		 String op = "";
			if (req.getParameter("cod_barra") != null && !req.getParameter("cod_barra").equals("")){
				codBarra= req.getParameter("cod_barra");
			}	
			if (req.getParameter("op") != null && !req.getParameter("op").equals("")){
				op= req.getParameter("op");
			}		
 			String result = "";
			
			 result = codBarra+"-"+op;
		
		salida.setHtmlOut(result);
		salida.Output();
		
	}


}
