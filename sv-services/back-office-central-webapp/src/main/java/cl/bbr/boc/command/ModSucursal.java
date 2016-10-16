package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;

/**
 * Comando que permite modificar la información de la empresa, segun los datos 
 * ingresados en el formulario
 * @author BBR
 *
 */
public class ModSucursal extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	 
     // 1. seteo de Variables del método
     String paramUrl 			="";
     long	paramId_sucursal	=-1;
     long 	paramRut			=-1;
     String paramDv				="";
     String paramRaz_social		="";
     String paramNombre			="";
     String paramDescripcion	="";
     long	paramId_empresa		= 0;
     String paramTel1Contacto	="";
     String paramTel2Contacto	="";
     String paramMailContacto	="";
     String paramEstado			="";
     String paramEncRecep       ="";
     String paramCod1Telefono   ="";
     String paramCod2Telefono   ="";
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_sucursal") == null ){throw new ParametroObligatorioException("id_sucursal es null");}
     if ( req.getParameter("rut") == null ){throw new ParametroObligatorioException("rut es null");}
     if ( req.getParameter("dv") == null ){throw new ParametroObligatorioException("dv es null");}
     if ( req.getParameter("raz_social") == null ){throw new ParametroObligatorioException("raz_social es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("descrip") == null ){throw new ParametroObligatorioException("descrip es null");}
     if ( req.getParameter("sel_emp") == null ){throw new ParametroObligatorioException("sel_emp es null");}
     if ( req.getParameter("fon_num_1") == null ){throw new ParametroObligatorioException("telf_1 es null");}
     if ( req.getParameter("fon_num_2") == null ){throw new ParametroObligatorioException("telf_2 es null");}
     if ( req.getParameter("email") == null ){throw new ParametroObligatorioException("email es null");}
     if ( req.getParameter("sel_est") == null ){throw new ParametroObligatorioException("sel_est es null");}
     if ( req.getParameter("enc_recep") == null ){throw new ParametroObligatorioException("enc_recep es null");}
     if ( req.getParameter("fon_cod_1") == null ){throw new ParametroObligatorioException("cod_tel_1 es null");}
     if ( req.getParameter("fon_cod_2") == null ){throw new ParametroObligatorioException("cod_tel_2 es null");}
 
     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_sucursal	= Long.parseLong(req.getParameter("id_sucursal"));
     paramRut			= Long.parseLong(req.getParameter("rut"));
     paramDv			= req.getParameter("dv");
     paramRaz_social	= req.getParameter("raz_social");
     paramNombre		= req.getParameter("nombre");
     paramDescripcion	= req.getParameter("descrip");
     paramId_empresa	= Long.parseLong(req.getParameter("sel_emp"));
     paramTel1Contacto	= req.getParameter("fon_num_1");
     paramTel2Contacto	= req.getParameter("fon_num_2");
     paramMailContacto	= req.getParameter("email");
     paramEstado		= req.getParameter("sel_est");
     paramEncRecep		= req.getParameter("enc_recep");
     paramCod1Telefono   =req.getParameter("fon_cod_1");
     paramCod2Telefono   =req.getParameter("fon_cod_2");
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_sucursal: " + paramId_sucursal);
     logger.debug("rut: " + paramRut);
     logger.debug("dv: " + paramDv);
     logger.debug("raz_social: " + paramRaz_social);
     logger.debug("nombre: " + paramNombre);
     logger.debug("descrip: " + paramDescripcion);
     logger.debug("sel_emp: " + paramId_empresa);
     logger.debug("cod_tel_1: " + paramCod1Telefono);     
     logger.debug("telf_1: " + paramTel1Contacto);
     logger.debug("cod_tel_2: " + paramCod2Telefono);     
     logger.debug("telf_2: " + paramTel2Contacto);
     logger.debug("email: " + paramMailContacto);
     logger.debug("sel_est: " + paramEstado);
     logger.debug("enc_recep: " + paramEncRecep);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
		SucursalesDTO prm = new SucursalesDTO();
		prm.setSuc_id(paramId_sucursal);
		prm.setSuc_rut(paramRut);
		prm.setSuc_dv(paramDv);
		prm.setSuc_razon(paramRaz_social);
		prm.setSuc_nombre(paramNombre);
		prm.setSuc_descr(paramDescripcion);
		prm.setSuc_emp_id(paramId_empresa);
		prm.setSuc_fono_num1(paramTel1Contacto);
		prm.setSuc_fono_num2(paramTel2Contacto);
		prm.setSuc_email(paramMailContacto);
		prm.setSuc_estado(paramEstado);
  		prm.setSuc_fec_act(Formatos.getFecHoraActual());
  		prm.setSuc_pregunta(paramEncRecep);
  		prm.setSuc_fono_cod1(paramCod1Telefono);
  		prm.setSuc_fono_cod2(paramCod2Telefono);
		
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	
     	try{
     		
     		boolean result = bizDelegate.setModSucursal(prm);
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
				fp.add( "id_sucursal" , paramId_sucursal+"");
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
