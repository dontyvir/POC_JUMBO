package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AddMarca Comando Process
 * Agrega marca 
 * @author BBRI
 */

public class AddMarca extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del m�todo
     String paramUrl = "";
     String paramNombre="";
     String paramEstado="";
     
     // 2. Procesa par�metros del request
     logger.debug("Procesando par�metros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("estado") == null ){throw new ParametroObligatorioException("estado es null");}

     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     paramNombre = req.getParameter("nombre"); //string:obligatorio:no
     paramEstado = req.getParameter("estado"); //string:obligatorio:no

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("nombre: " + paramNombre);
     logger.debug("estado: " + paramEstado);
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		MarcasDTO prm = new MarcasDTO();
  		
  		prm.setNombre(paramNombre);
  		prm.setEstado(paramEstado);
     
  	 try{
  		boolean result = biz.addMarca(prm);
  		logger.debug("result:"+result);
  	 }catch(BocException e){
			logger.debug("Controlando excepci�n del AddProdcatweb: " + e.getMessage());
			if (  e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
				logger.debug("El c�digo de la categor�a ingresado no existe");
				//fp.add( "rc" , Constantes._EX_CAT_ID_NO_EXISTE );
				//paramUrl = UrlError + fp.forward();
			}
  	 }
  	 

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
