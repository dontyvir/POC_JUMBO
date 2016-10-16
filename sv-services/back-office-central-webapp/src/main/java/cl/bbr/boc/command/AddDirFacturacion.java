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
import cl.bbr.vte.empresas.dto.DirFacturacionDTO;

/**
 * AddDirDespacho Comando Process
 * Agrega una direccion de despacho
 * 
 * @author BBRI
 */

public class AddDirFacturacion extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long 	paramId_sucursal	=-1;
     long	paramComuna			=-1;
     long	paramTipo_calle		=-1;
     String paramAlias			="";
     String paramCalle			="";
     String	paramNumero			="";
     String paramDepto			="";
     String paramComentarios	="";
     String paramCiudad			="";
     String paramFax			="";
     String paramNom_cont		="";
     String paramCargo			="";
     String paramMail			="";
     String paramFono1			="";
     String paramFono2			="";
     String paramFono3			="";
     String UrlError            ="";
     long emp_id = -1;
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("comuna") == null ){throw new ParametroObligatorioException("comuna es null");}
     if ( req.getParameter("tipo_calle") == null ){throw new ParametroObligatorioException("tipo_calle es null");}
     if ( req.getParameter("alias") == null ){throw new ParametroObligatorioException("alias es null");}
     if ( req.getParameter("calle") == null ){throw new ParametroObligatorioException("calle es null");}
     if ( req.getParameter("numero") == null ){throw new ParametroObligatorioException("numero es null");}
     if ( req.getParameter("depto") == null ){throw new ParametroObligatorioException("depto es null");}
     if ( req.getParameter("comentario") == null ){throw new ParametroObligatorioException("comentario es null");}
     if ( req.getParameter("ciudad") == null ){throw new ParametroObligatorioException("ciudad es null");}
     if ( req.getParameter("fax") == null ){throw new ParametroObligatorioException("fax es null");}
     if ( req.getParameter("nom_cont") == null ){throw new ParametroObligatorioException("nom_cont es null");}
     if ( req.getParameter("cargo") == null ){throw new ParametroObligatorioException("cargo es null");}
     if ( req.getParameter("mail") == null ){throw new ParametroObligatorioException("mail es null");}
     if ( req.getParameter("fono1") == null ){throw new ParametroObligatorioException("fono1 es null");}
     if ( req.getParameter("fono3") == null ){throw new ParametroObligatorioException("fono3 es null");}
     if ( req.getParameter("fono3") == null ){throw new ParametroObligatorioException("fono3 es null");}
     if ( req.getParameter("UrlError") == null ){throw new ParametroObligatorioException("UrlError es null");}
     
     
     
     

     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_sucursal	= Long.parseLong(req.getParameter("id_sucursal"));
     paramComuna		= Long.parseLong(req.getParameter("comuna"));
     paramTipo_calle	= Long.parseLong(req.getParameter("tipo_calle"));
     paramAlias 		= req.getParameter("alias");
     paramCalle 		= req.getParameter("calle");
     paramNumero 		= req.getParameter("numero");
     paramDepto 		= req.getParameter("depto");
     paramComentarios 	= req.getParameter("comentario");
     paramCiudad 		= req.getParameter("ciudad");
     paramFax 			= req.getParameter("fax");
     paramNom_cont 		= req.getParameter("nom_cont");
     paramCargo 		= req.getParameter("cargo");
     paramMail 			= req.getParameter("mail");
     paramFono1 		= req.getParameter("fono1");
     paramFono2 		= req.getParameter("fono2");
     paramFono3 		= req.getParameter("fono3");
     UrlError    		= req.getParameter("UrlError");
     if (req.getParameter("emp_id")!= null && !req.getParameter("emp_id").equals(""))
    	 emp_id = Long.parseLong(req.getParameter("emp_id"));
     // 2.3 log de parametros y valores
     logger.debug("url			: " + paramUrl);
     logger.debug("id_sucursal	: " + paramId_sucursal);
     logger.debug("comuna		: " + paramComuna);
     logger.debug("tipo_calle	: " + paramTipo_calle);
     logger.debug("alias		: " + paramAlias);
     logger.debug("calle		: " + paramCalle);
     logger.debug("numero		: " + paramNumero);
     logger.debug("depto		: " + paramDepto);
     logger.debug("comentario	: " + paramComentarios);
     logger.debug("ciudad		: " + paramCiudad);
     logger.debug("fax			: " + paramFax);
     logger.debug("nom_cont		: " + paramNom_cont);
     logger.debug("cargo		: " + paramCargo);
     logger.debug("mail			: " + paramMail);
     logger.debug("fono1		: " + paramFono1);
     logger.debug("fono2		: " + paramFono2);
     logger.debug("fono3		: " + paramFono3);
     logger.debug("urlerror		: " + UrlError);
     logger.debug("emp_id		: " + emp_id);
     
 		ForwardParameters fp = new ForwardParameters();
	  	fp.add( req.getParameterMap() );
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		DirFacturacionDTO prm = new DirFacturacionDTO ();
  		prm.setDfac_cli_id(paramId_sucursal);
  		prm.setDfac_com_id(paramComuna);
  		prm.setDfac_tip_id(paramTipo_calle);
  		prm.setDfac_alias(paramAlias);
  		prm.setDfac_calle(paramCalle);
  		prm.setDfac_numero(paramNumero);
  		prm.setDfac_depto(paramDepto);
  		prm.setDfac_comentarios(paramComentarios);
  		prm.setDfac_ciudad(paramCiudad);
  		prm.setDfac_fax(paramFax);
  		prm.setDfac_nom_contacto(paramNom_cont);
  		prm.setDfac_cargo(paramCargo);
  		prm.setDfac_email(paramMail);
  		prm.setDfac_fono1(paramFono1);
  		prm.setDfac_fono2(paramFono2);
  		prm.setDfac_fono3(paramFono3);
  		prm.setDfac_estado(Constantes.ESTADO_ACTIVADO);
     
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		long id = -1;
  		try{
  			id = biz.insDirFacturacion(prm);
  			logger.debug("id?"+id);

  			if(id > 0 ){
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
			logger.debug("La direccion de facturacion no pudo ser creada...");
			fp.add( "msje" , mensaje_fracaso );
			paramUrl = UrlError + fp.forward(); 

     	}
  		
  		logger.debug("result:"+id);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
