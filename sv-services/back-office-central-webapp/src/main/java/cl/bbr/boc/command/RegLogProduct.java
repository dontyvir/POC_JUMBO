package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que registra un log en productos
 * @author bbr
 *
 */
public class RegLogProduct extends Command {
	private final static long serialVersionUID = 1;
 
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     
     // 1. seteo de Variables del método
     String paramUrl = "";
     String paramTexto="";
     long paramId_producto=0;
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_prod") == null ){throw new ParametroObligatorioException("id_prod es null");}
     if ( req.getParameter("texto") == null ){throw new ParametroObligatorioException("texto es nulo");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramId_producto = Long.parseLong(req.getParameter("id_prod"));
     paramTexto = req.getParameter("texto"); //string:obligatorio:si

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_prod: " + paramId_producto);
     logger.debug("texto: " + paramTexto);
     
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate biz = new BizDelegate();
     	
		String fec_crea = Formatos.getFecHoraActual();
		ProductoLogDTO log = new ProductoLogDTO(); 
		log.setCod_prod(paramId_producto);
		log.setFec_crea(fec_crea);
		log.setUsuario(usr.getApe_paterno());
		log.setTexto(paramTexto);
		int resLog = biz.setLogProduct(log);
		logger.debug("se guardo log con id:"+resLog);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
