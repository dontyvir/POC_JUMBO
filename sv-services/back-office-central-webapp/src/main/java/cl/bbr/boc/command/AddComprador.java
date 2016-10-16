package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;

/**
 * AddComprador Comando Process
 * Agrega un comprador asociado a una sucursal
 * 
 * @author BBRI
 */

public class AddComprador extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long 	paramRut			=-1;
     String paramDv				="";
     String paramNombre			="";
     String paramAp_paterno		="";
     String paramAp_materno		="";
     long	paramId_sucursal	=-1;
     String paramTipo			="";
     String paramGenero			="";
     String paramTelef1			="";
     String paramTelef2			="";
     String paramTelef3			="";
     String paramMailContacto	="";
     String paramEstado			="";
     String paramClave          ="";
     long   paramEmpId          =-1; 
     String paramPreg			="";
     String paramResp			="";
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("rut") == null ){throw new ParametroObligatorioException("rut es null");}
     if ( req.getParameter("dv") == null ){throw new ParametroObligatorioException("dv es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("paterno") == null ){throw new ParametroObligatorioException("paterno es null");}
     if ( req.getParameter("materno") == null ){throw new ParametroObligatorioException("materno es null");}
     if ( req.getParameter("sel_emp_suc") == null ){throw new ParametroObligatorioException("sel_emp_suc es null");}
     //if ( req.getParameter("sel_tip") == null ){throw new ParametroObligatorioException("sel_tip es null");}
     if ( req.getParameter("sel_gen") == null ){throw new ParametroObligatorioException("sel_gen es null");}
     if ( req.getParameter("fon_num_1") == null ){throw new ParametroObligatorioException("telf_1 es null");}
     if ( req.getParameter("fon_num_2") == null ){throw new ParametroObligatorioException("telf_2 es null");}
     if ( req.getParameter("fon_num_3") == null ){throw new ParametroObligatorioException("telf_3 es null");}
     if ( req.getParameter("email") == null ){throw new ParametroObligatorioException("email es null");}
     if ( req.getParameter("sel_est") == null ){throw new ParametroObligatorioException("sel_est es null");}
     if ( req.getParameter("clave") == null ){throw new ParametroObligatorioException("clave es null");}
     if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id es null");}
     if ( req.getParameter("preg_secreta") == null ){throw new ParametroObligatorioException("preg_secreta es null");}
     if ( req.getParameter("resp_secreta") == null ){throw new ParametroObligatorioException("resp_secreta es null");}
     if ( req.getParameter("fon_cod_1") == null ){throw new ParametroObligatorioException("cod_1 es null");}
     if ( req.getParameter("fon_cod_2") == null ){throw new ParametroObligatorioException("cod_2 es null");}
     if ( req.getParameter("fon_cod_3") == null ){throw new ParametroObligatorioException("cod_3 es null");}

     

     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramRut			= Long.parseLong(req.getParameter("rut"));
     paramDv			= req.getParameter("dv");
     paramNombre		= req.getParameter("nombre");
     paramAp_paterno	= req.getParameter("paterno");
     paramAp_materno	= req.getParameter("materno");
     paramId_sucursal	= Long.parseLong(req.getParameter("sel_emp_suc"));
     //paramTipo			= req.getParameter("sel_tip");
     paramGenero		= req.getParameter("sel_gen");
     paramTelef1		= req.getParameter("fon_cod_1")+req.getParameter("fon_num_1");
     paramTelef2		= req.getParameter("fon_cod_2")+req.getParameter("fon_num_2");
     paramTelef3		= req.getParameter("fon_cod_3")+req.getParameter("fon_num_3");
     paramMailContacto	= req.getParameter("email");
     paramEstado		= req.getParameter("sel_est");
     paramClave		    = req.getParameter("clave");
     paramPreg			= req.getParameter("preg_secreta");
     paramResp			= req.getParameter("resp_secreta");
     paramEmpId		    = Long.parseLong(req.getParameter("emp_id"));
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("rut: " + paramRut);
     logger.debug("dv: " + paramDv);
     logger.debug("nombre: " + paramNombre);
     logger.debug("paterno: " + paramAp_paterno);
     logger.debug("materno: " + paramAp_materno);
     logger.debug("sel_emp_suc: " + paramId_sucursal);
     logger.debug("sel_tip: " + paramTipo);
     logger.debug("sel_gen: " + paramGenero);
     logger.debug("telf_1: " + paramTelef1);
     logger.debug("telf_2: " + paramTelef2);
     logger.debug("telf_3: " + paramTelef3);
     logger.debug("email: " + paramMailContacto);
     logger.debug("sel_est: " + paramEstado);
     logger.debug("clave: " + paramClave);
     logger.debug("pregunta:"+ paramPreg);
     logger.debug("respuesta:"+ paramResp);
     logger.debug("emp_id" + paramEmpId);
     
     //ForwardParameters fp = new ForwardParameters();
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		CompradoresDTO prm = new CompradoresDTO();
  		prm.setCpr_rut(paramRut);
  		prm.setCpr_dv(paramDv);
  		prm.setCpr_nombres(paramNombre);
  		prm.setCpr_ape_pat(paramAp_paterno);
  		prm.setCpr_ape_mat(paramAp_materno);
  		prm.setId_sucursal(paramId_sucursal);
  		prm.setCpr_tipo(Constantes.CADENA_VACIA);
  		prm.setCpr_genero(paramGenero);
  		prm.setCpr_fono1(paramTelef1);
  		prm.setCpr_fono2(paramTelef2);
  		prm.setCpr_fono3(paramTelef3);
  		prm.setCpr_email(paramMailContacto);
  		prm.setCpr_estado(paramEstado);
  		prm.setCpr_fec_crea(Formatos.getFecHoraActual());
//  	 encripta el password en MD5
		String passwd = Cifrador.toMD5(paramClave);
  		prm.setCpr_pass(passwd);
  		prm.setCpr_pregunta(paramPreg);
  		prm.setCpr_respuesta(paramResp);  		
  		prm.setId_empresa(paramEmpId);
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		boolean result = false;
  		try{
  			long id = biz.setCreaComprador(prm);
  			logger.debug("result?"+result);
  			if(id>0){
  				paramUrl += "?id_comprador=" + id + "&msje=" +  mensaje_exito;
  			}else{
  				paramUrl += "?msje=" +  mensaje_fracaso;
  			}
  			
  		} catch(BocException e){
  	  		ForwardParameters fp = new ForwardParameters();
  	  	    fp.add( req.getParameterMap() );
  	  	    
     		logger.debug("Controlando excepción: " + e.getMessage());
			String UrlError = getServletConfig().getInitParameter("UrlError");
			if ( e.getMessage()!=null &&  !e.getMessage().equals("")){
				logger.debug(e.getMessage());
				//fp.add( "rc" , Constantes._EX_EMP_ID_NO_CREADO );
				fp.add( "msje" , e.getMessage() );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
