package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * Comando que permite modificar la información de la empresa, segun los datos 
 * ingresados en el formulario
 * @author BBR
 *
 */
public class MarcaEliminadaSucursal extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl 			="";
     long	paramId_sucursal	=-1;
     long	paramId_empresa		= 0;
     String UrlError="";
     
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("UrlError") == null ){throw new ParametroObligatorioException("UrlError es null");}
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id es null");}
     
     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_sucursal	= Long.parseLong(req.getParameter("id_sucursal"));
     paramId_empresa	= Long.parseLong(req.getParameter("emp_id"));
     UrlError = req.getParameter("UrlError");
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_sucursal: " + paramId_sucursal);
     logger.debug("emp_id: " + paramId_empresa);
    
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
		SucursalesDTO prm = new SucursalesDTO();
		prm.setSuc_id(paramId_sucursal);
		prm.setSuc_estado(Constantes.ESTADO_ELIMINADO);
  		
		
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate biz = new BizDelegate();
     	
     	try{
     		
     		boolean result = biz.setDelSucursal(prm);
    		logger.debug("result?"+result);
    		if(result){
    			fp.add( "emp_id" , String.valueOf(paramId_empresa));
  				fp.add( "id_sucursal" , String.valueOf(paramId_sucursal));
  				fp.add( "msje" , mensaje_exito);
  				paramUrl = paramUrl + fp.forward();
  			}else{
  				fp.add( "emp_id" , String.valueOf(paramId_empresa));
  				fp.add( "msje" , mensaje_fracaso);
  				paramUrl = UrlError + fp.forward();
  			}
    		
     	}catch(BocException e){
     		logger.debug("Controlando excepción: " + e.getMessage());
			
			if ( e.getMessage()!= null && !e.getMessage().equals("") ){
				logger.debug(e.getMessage());
				fp.add( "msje" , e.getMessage() );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
