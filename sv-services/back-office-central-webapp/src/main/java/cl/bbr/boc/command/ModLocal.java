package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.ctrl.LocalCtrl;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Comando que permite modificar la marca
 * @author bbr
 *
 */
public class ModLocal extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl = "";
     long id_local = 0L;
     String cod_local = "";
     String nom_local = "";
     int orden =0;
     int cod_local_pos = 0;
     String tipo_flujo ="";
     String cod_local_promo="";
     String tipo_picking = "";
     String retirolocal="N";//por defecto no es retiro local
     int id_zona_retiro=0;
     String direccion="";
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     
     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_local") == null ){throw new ParametroObligatorioException("id_local es nulo");}
     if ( req.getParameter("cod_local") == null ){throw new ParametroObligatorioException("cod_local es nulo");}
     if ( req.getParameter("nom_local") == null ){throw new ParametroObligatorioException("nom_local es nulo");}
     if ( req.getParameter("orden") == null ){throw new ParametroObligatorioException("orden es nulo");}
     if ( req.getParameter("cod_local_pos") == null ){throw new ParametroObligatorioException("cod_local_pos es nulo");}
     if ( req.getParameter("tipo_flujo") == null ){throw new ParametroObligatorioException("tipo_flujo es nulo");}
     if ( req.getParameter("cod_local_promo") == null ){throw new ParametroObligatorioException("cod_local_promo es nulo");}     
     if ( req.getParameter("tipo_picking") == null ){throw new ParametroObligatorioException("tipo_picking es nulo");}     
     if ( req.getParameter("direccion") == null ){throw new ParametroObligatorioException("direccion");}     
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     id_local = Long.parseLong(req.getParameter("id_local"));
     cod_local = req.getParameter("cod_local"); //string:obligatorio:no
     nom_local = req.getParameter("nom_local"); //string:obligatorio:no
     orden = Integer.parseInt(req.getParameter("orden")); //string:obligatorio:no
     cod_local_pos = Integer.parseInt(req.getParameter("cod_local_pos")); //string:obligatorio:no
     tipo_flujo = req.getParameter("tipo_flujo"); //string:obligatorio:no
     cod_local_promo = req.getParameter("cod_local_promo"); //string:obligatorio:no
     tipo_picking = req.getParameter("tipo_picking"); //string:obligatorio:no
     direccion=req.getParameter("direccion");
     if (req.getParameter("retirolocal")==null){
    	 retirolocal="N";
     }else{
    	 retirolocal=req.getParameter("retirolocal");//string:obligatorio:no
    	 id_zona_retiro=Integer.parseInt(req.getParameter("zona"));
    	//modificamos la zona para que tenga orden 1000 (muestre al final la lista de zonas)      	
      	LocalCtrl local = new LocalCtrl();
      	local.doModZonaDespachoOrden(id_zona_retiro, 1000);
     }

     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_local: " + id_local);
     logger.debug("cod_local: " + cod_local);
     logger.debug("nom_local: " + nom_local);
     logger.debug("orden: " + orden);
     logger.debug("cod_local_pos: " + cod_local_pos);
     logger.debug("tipo_flujo: " + tipo_flujo);
     logger.debug("cod_local_promo: " + cod_local_promo);
     logger.debug("tipo_picking: " + tipo_picking);
     logger.debug("retirolocal: " + retirolocal);
     logger.debug("id_zona_retiro: " + id_zona_retiro);
     logger.debug("direccion: " + direccion);

     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );

     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate biz = new BizDelegate();
     	
     	LocalDTO dto = new LocalDTO();
     	
     	dto.setId_local(id_local);
     	dto.setCod_local(cod_local);
     	dto.setNom_local(nom_local);
     	dto.setOrden(orden);
     	dto.setCod_local_pos(cod_local_pos);
     	dto.setTipo_flujo(tipo_flujo);
     	dto.setCod_local_promocion(cod_local_promo);
     	dto.setTipo_picking(tipo_picking);
     	dto.setRetirolocal(retirolocal);
     	dto.setId_zona_retiro(id_zona_retiro);
     	dto.setDireccion(direccion);
     	try{
			// Ejecuta Operación
	     	boolean result = biz.doModLocal( dto );
	     	logger.debug("result?"+result);
    		if(result){
  				paramUrl += "&msje=" +  mensaje_exito;
  			}else{
  				paramUrl += "&msje=" +  mensaje_fracaso;
  			}
     	}catch(BocException e){
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage()!= null && !e.getMessage().equals("") ){
				logger.debug(e.getMessage());
				fp.add( "msje" , e.getMessage() );
				fp.add( "id_local" , String.valueOf(id_local) );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
     	
	
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
