package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.DireccionesDTO;

/**
 * ModDirDespacho Comando Process
 * Modifica informacion de una direccion de despacho de un sucursal
 * 
 * @author BBRI
 */

public class ModDirDespacho extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long 	paramId_dir			=-1;
     long 	paramId_sucursal	=-1;
     long	paramComuna			=-1;
     int 	paramPoligono		=-1;
     long	paramTipo_calle		=-1;
     String paramAlias			="";
     String paramCalle			="";
     String	paramNumero			="";
     String paramDepto			="";
     String paramComentarios	="";
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_dir") == null ){throw new ParametroObligatorioException("id_dir es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("comuna") == null ){throw new ParametroObligatorioException("comuna es null");}
     if ( req.getParameter("poligono") == null ){throw new ParametroObligatorioException("poligono es null");}
     if ( req.getParameter("tipo_calle") == null ){throw new ParametroObligatorioException("tipo_calle es null");}
     if ( req.getParameter("alias") == null ){throw new ParametroObligatorioException("alias es null");}
     if ( req.getParameter("calle") == null ){throw new ParametroObligatorioException("calle es null");}
     if ( req.getParameter("numero") == null ){throw new ParametroObligatorioException("numero es null");}
     if ( req.getParameter("depto") == null ){throw new ParametroObligatorioException("depto es null");}
     if ( req.getParameter("comentario") == null ){throw new ParametroObligatorioException("comentario es null");}
     

     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_dir		= Long.parseLong(req.getParameter("id_dir"));
     paramId_sucursal	= Long.parseLong(req.getParameter("id_sucursal"));
     paramComuna		= Long.parseLong(req.getParameter("comuna"));
     String [] pol = req.getParameter("poligono").split("~");
     paramPoligono		= Integer.parseInt(pol[0]);
     paramTipo_calle	= Long.parseLong(req.getParameter("tipo_calle"));
     paramAlias 		= req.getParameter("alias");
     paramCalle 		= req.getParameter("calle");
     paramNumero 		= req.getParameter("numero");
     paramDepto 		= req.getParameter("depto");
     paramComentarios 	= req.getParameter("comentario");
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_dir: " + paramId_dir);
     logger.debug("id_sucursal: " + paramId_sucursal);
     logger.debug("comuna: " + paramComuna);
     logger.debug("zona: " + paramPoligono);
     logger.debug("tipo_calle: " + paramTipo_calle);
     logger.debug("alias: " + paramAlias);
     logger.debug("calle: " + paramCalle);
     logger.debug("numero: " + paramNumero);
     logger.debug("depto: " + paramDepto);
     logger.debug("comentario: " + paramComentarios);
     
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		DireccionesDTO prm = new DireccionesDTO ();
  		prm.setId(paramId_dir);
  		prm.setCli_id(paramId_sucursal);
  		prm.setCom_id(paramComuna);
  		prm.setId_poligono(paramPoligono);
  		prm.setTip_id(paramTipo_calle);
  		prm.setTipo_calle(paramTipo_calle);
  		prm.setAlias(paramAlias);
  		prm.setCalle(paramCalle);
  		prm.setNumero(paramNumero);
  		prm.setDepto(paramDepto);
  		prm.setComentarios(paramComentarios);
  		prm.setFec_crea(Formatos.getFecHoraActual());
  		//prm.setLoc_cod(-1);
     
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		boolean result = false;
  		try{
  			result = biz.modDirDespacho(prm);
  			logger.debug("result?"+result);
  			if(result){
  				paramUrl += "?id_dir=" + paramId_dir +"&id_sucursal=" + paramId_sucursal + "&msje=" +  mensaje_exito;
  			}else{
  				paramUrl += "?msje=" +  mensaje_fracaso;
  			}
  			
  		} catch(BocException e){
  	  		ForwardParameters fp = new ForwardParameters();
  	  	    fp.add( req.getParameterMap() );
  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage().equals(Constantes._EX_EMP_ID_NO_CREADO) ){
				logger.debug("La empresa no se puedo crear...");
				fp.add( "rc" , Constantes._EX_EMP_ID_NO_CREADO );
				fp.add( "msje" , mensaje_fracaso );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
