package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.ModCotizacionDTO;

/**
 * Comando que permite modificar la información de la empresa, segun los datos 
 * ingresados en el formulario
 * @author BBR
 *
 */
public class ModCotizacion extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long	paramId_cot  		= -1;
     double paramCosto_desp 	= -1;
     String paramObs 			= "";
     String paramFueraMix  		= "";
     String paramTipoDoc        = "";
     long   paramId_dirfact     = -1;
     long   paramId_dir         = -1;
     String paramFVenc 			="";
     String paramAut_margen		="";
     String paramAut_dscto		="";
     String paramFDesp  		="";
     long   paramIdJornRef		=-1;
   
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("cot_id") == null ){throw new ParametroObligatorioException("id_cot es null");}
     if ( req.getParameter("tipo_doc") == null ){throw new ParametroObligatorioException("tipo_doc es null");}
     if ( req.getParameter("sel_dirdes") == null ){throw new ParametroObligatorioException("tipo_doc es null");}
          // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_cot	    = Long.parseLong(req.getParameter("cot_id"));
     paramCosto_desp    = Double.parseDouble(req.getParameter("costo_desp"));
     paramObs           = req.getParameter("obs");
     paramFueraMix      = req.getParameter("fuera_mix");
     paramTipoDoc       = req.getParameter("tipo_doc");
     paramId_dir        = Long.parseLong(req.getParameter("sel_dirdes"));
     paramFVenc       	= req.getParameter("fecha_venc");
     
     
     if (req.getParameter("aut_margen")==null){ paramAut_margen=""; }
     else { paramAut_margen 	= req.getParameter("aut_margen");}
     
     
     if (req.getParameter("aut_dscto")==null){    	 paramAut_dscto="";     }
     else { paramAut_dscto		= req.getParameter("aut_dscto");}
     
     if ( req.getParameter("sel_dirfac") != null){
    	 paramId_dirfact    = Long.parseLong(req.getParameter("sel_dirfac"));
     } 
     paramIdJornRef = Long.parseLong(req.getParameter("id_jornada"));
     paramFDesp = req.getParameter("fec_desp");
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_cot: " + paramId_cot);
     logger.debug("obs: " + paramObs);
     logger.debug("Fuera mix: " + paramFueraMix);
     logger.debug("costo despacho: " + paramCosto_desp);
     logger.debug("Tipo Documento: " + paramTipoDoc);
     logger.debug("Direccion facturación: " + paramId_dirfact);
     logger.debug("Direccion despacho: " + paramId_dir);
     logger.debug("Fecha de Vencimiento: " + paramFVenc);
     logger.debug("autorizacion margen: " + paramAut_margen);
 	 logger.debug("autorizacion dscto: " + paramAut_dscto);
 	 logger.debug("id jornada: " + paramIdJornRef);
 	 logger.debug("fecha despacho: " + paramFDesp);
     
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
	//obtiene mensajes
	String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
	String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     /*
      * 3. Procesamiento Principal
      */
   	BizDelegate bizDelegate = new BizDelegate();
     	
 	try{
 		ModCotizacionDTO cot = new ModCotizacionDTO();
 		cot.setId_cot(paramId_cot);
 		cot.setObs(paramObs);
 		cot.setFueraMix(paramFueraMix);
 		cot.setCosto_despacho(paramCosto_desp);
 		cot.setUsuario(usr.getLogin());
 		cot.setId_dirfact(paramId_dirfact);
 		cot.setTipo_doc(paramTipoDoc);
 		cot.setId_dirdesp(paramId_dir);
 		cot.setFecha_venc(paramFVenc);
 		cot.setAut_margen(paramAut_margen);
 		cot.setAut_dscto(paramAut_dscto); 
 		cot.setFec_desp(paramFDesp);
 		cot.setId_jorn_ref(paramIdJornRef);
 		
 		boolean result = bizDelegate.setModCotizacion(cot);
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
			fp.add( "id_cot" , String.valueOf(paramId_cot) );
			paramUrl = UrlError + fp.forward(); 
		} else { 
			logger.debug("Controlando excepción: " + e.getMessage());
		}
 	}
 

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
