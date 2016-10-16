package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.PoligonoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que modifica a los Usuarios
 * @author bbr
 *
 */
public class ModPoligono extends Command {
	private final static long serialVersionUID = 1;
 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String Url = "";
     long   id_poligono = 0L;
     long   id_comuna = 0L;
     long   num_pol = 0L;
     String desc_pol = "";

     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_poligono") == null ){throw new ParametroObligatorioException("id_poligono es nulo");}
     if ( req.getParameter("id_comuna") == null ){throw new ParametroObligatorioException("id_comuna es nulo");}
     if ( req.getParameter("num_pol") == null ){throw new ParametroObligatorioException("num_pol es nulo");}
     if ( req.getParameter("desc_pol") == null ){throw new ParametroObligatorioException("desc_pol es nulo");}

     // 2.2 obtiene parametros desde el request
     id_poligono = Long.parseLong(req.getParameter("id_poligono")); //string:obligatorio:si
     id_comuna   = Long.parseLong(req.getParameter("id_comuna")); //string:obligatorio:no
     num_pol     = Long.parseLong(req.getParameter("num_pol")); //string:obligatorio:no
     desc_pol    = req.getParameter("desc_pol"); //string:obligatorio:no
     Url         = req.getParameter("url") + "?id_comuna=" + id_comuna;

     // 2.3 log de parametros y valores
     logger.debug("url: " + Url);
     logger.debug("id_poligono: " + id_poligono);
     logger.debug("id_comuna: " + id_comuna);
     logger.debug("num_pol: " + num_pol);
     logger.debug("desc_pol: " + desc_pol);

     
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	PoligonoDTO pol = new PoligonoDTO();

     	pol.setId_poligono(id_poligono);
     	pol.setNum_poligono(num_pol);     	
     	pol.setDescripcion(desc_pol);


		bizDelegate.ModPoligono(pol);

     // 4. Redirecciona salida
     res.sendRedirect(Url);

 }//execute

}
