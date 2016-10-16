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
 * AddDirDespacho Comando Process
 * Agrega una direccion de despacho
 * 
 * @author BBRI
 */

public class AddDirDespacho extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long 	paramId_sucursal	=-1;
     long	paramComuna			=-1;
     int    paramPoligono		=-1;
     long	paramTipo_calle		=-1;
     String paramAlias			="";
     String paramCalle			="";
     String	paramNumero			="";
     String paramDepto			="";
     String paramComentarios	="";
     String UrlError            ="";
     long emp_id =-1;

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("comuna") == null ){throw new ParametroObligatorioException("comuna es null");}
     if ( req.getParameter("poligono") == null ){throw new ParametroObligatorioException("poligono es null");}
     if ( req.getParameter("tipo_calle") == null ){throw new ParametroObligatorioException("tipo_calle es null");}
     if ( req.getParameter("alias") == null ){throw new ParametroObligatorioException("alias es null");}
     if ( req.getParameter("calle") == null ){throw new ParametroObligatorioException("calle es null");}
     if ( req.getParameter("numero") == null ){throw new ParametroObligatorioException("numero es null");}
     if ( req.getParameter("depto") == null ){throw new ParametroObligatorioException("depto es null");}
     if ( req.getParameter("comentario") == null ){throw new ParametroObligatorioException("comentario es null");}
     if ( req.getParameter("UrlError") == null ){throw new ParametroObligatorioException("UrlError es null");}
     
     

     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
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
     UrlError    		= req.getParameter("UrlError");
     if ( req.getParameter("emp_id") != null && !req.getParameter("emp_id").equals(""))
    	 emp_id 	= Long.parseLong(req.getParameter("emp_id"));
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_sucursal: " + paramId_sucursal);
     logger.debug("comuna: " + paramComuna);
     logger.debug("Poligono: " + paramPoligono);
     logger.debug("tipo_calle: " + paramTipo_calle);
     logger.debug("alias: " + paramAlias);
     logger.debug("calle: " + paramCalle);
     logger.debug("numero: " + paramNumero);
     logger.debug("depto: " + paramDepto);
     logger.debug("comentario: " + paramComentarios);
     logger.debug("urlerror		: " + UrlError);
     logger.debug("emp_id		: " + emp_id);
     
	ForwardParameters fp = new ForwardParameters();
  	fp.add( req.getParameterMap() );   
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		DireccionesDTO prm = new DireccionesDTO ();
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
  		prm.setEstado(Constantes.ESTADO_ACTIVADO);
  		//prm.setLoc_cod(-1);
     
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		long id = -1;
  		try{
  			id = biz.insDirDespacho(prm);
  			logger.debug("id?"+id);
  			if(id>0){
  				//paramUrl += "?id_dir=" + id + "&id_sucursal=" + paramId_sucursal + "&msje=" +  mensaje_exito;
  				fp.add( "emp_id" , String.valueOf(emp_id));
  				fp.add( "id_dir" , String.valueOf(id));
  				fp.add( "msje" , mensaje_exito);
				paramUrl = paramUrl + fp.forward(); 
  			}else{
  				fp.add( "msje" , mensaje_fracaso);
  				paramUrl = UrlError + fp.forward();
  			}
  			
  		} catch(BocException e){

  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			logger.debug("La direccion de despacho no pudo ser creada...");
			fp.add( "msje" , mensaje_fracaso );
			paramUrl = UrlError + fp.forward(); 
     	}
  		
  		logger.debug("result:"+id);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
