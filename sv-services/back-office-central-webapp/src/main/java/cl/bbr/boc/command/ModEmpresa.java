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
import cl.bbr.vte.empresas.dto.EmpresasDTO;

/**
 * Comando que permite modificar la información de la empresa, segun los datos 
 * ingresados en el formulario
 * @author BBR
 *
 */
public class ModEmpresa extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl 			="";
     long	paramId_empresa		=-1;
     long 	paramRut			=-1;
     String paramDv				="";
     String paramRaz_social		="";
     String paramNombre			="";
     String paramRubro			="";
     String paramDescripcion	="";
     int 	paramCantEmpl		= 0;
     String paramNomContacto	="";
     String paramCargoContacto	="";
     String paramTel1Contacto	="";
     String paramTel2Contacto	="";
     String paramTel3Contacto	="";
     String paramMailContacto	="";
     String paramEstado			="";
     double	paramMrgMinimo		= 0;
     double	paramDsctoMaximo	= 0;
     String paramCodtel1 		="";
     String paramCodtel2 		= "";
     String paramCodtel3 		= "";
     
     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("id_empresa") == null ){throw new ParametroObligatorioException("id_empresa es null");}
     if ( req.getParameter("rut") == null ){throw new ParametroObligatorioException("rut es null");}
     if ( req.getParameter("dv") == null ){throw new ParametroObligatorioException("dv es null");}
     if ( req.getParameter("raz_social") == null ){throw new ParametroObligatorioException("raz_social es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("rubro") == null ){throw new ParametroObligatorioException("rubro es null");}
     if ( req.getParameter("descrip") == null ){throw new ParametroObligatorioException("descrip es null");}
     if ( req.getParameter("cant_emp") == null ){throw new ParametroObligatorioException("cant_emp es null");}
     if ( req.getParameter("nom_cont") == null ){throw new ParametroObligatorioException("nom_cont es null");}
     if ( req.getParameter("cargo") == null ){throw new ParametroObligatorioException("cargo es null");}
     if ( req.getParameter("fon_num_1") == null ){throw new ParametroObligatorioException("fon_num_1 es null");}
     if ( req.getParameter("fon_num_2") == null ){throw new ParametroObligatorioException("fon_num_2 es null");}
     if ( req.getParameter("fon_num_3") == null ){throw new ParametroObligatorioException("fon_num_3 es null");}
     if ( req.getParameter("email") == null ){throw new ParametroObligatorioException("email es null");}
     if ( req.getParameter("sel_est") == null ){throw new ParametroObligatorioException("sel_est es null");}
     if ( req.getParameter("margen") == null ){throw new ParametroObligatorioException("margen es null");}
     if ( req.getParameter("dscto") == null ){throw new ParametroObligatorioException("dscto es null");}
     if ( req.getParameter("fon_cod_1") == null ){throw new ParametroObligatorioException("fon_cod_1 es null");}
     if ( req.getParameter("fon_cod_2") == null ){throw new ParametroObligatorioException("fon_cod_2 es null");}
     if ( req.getParameter("fon_cod_3") == null ){throw new ParametroObligatorioException("fon_cod_3 es null");}
    
     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramId_empresa	= Long.parseLong(req.getParameter("id_empresa"));
     paramRut			= Long.parseLong(req.getParameter("rut"));
     paramDv			= req.getParameter("dv");
     paramRaz_social	= req.getParameter("raz_social");
     paramNombre		= req.getParameter("nombre");
     paramRubro			= req.getParameter("rubro");
     paramDescripcion	= req.getParameter("descrip");
     paramCantEmpl		= Integer.parseInt(req.getParameter("cant_emp"));
     paramNomContacto	= req.getParameter("nom_cont");
     paramCargoContacto	= req.getParameter("cargo");
     paramTel1Contacto	= req.getParameter("fon_num_1");
     paramTel2Contacto	= req.getParameter("fon_num_2");
     paramTel3Contacto	= req.getParameter("fon_num_3");
     paramCodtel1		= req.getParameter("fon_cod_1");
     paramCodtel2		= req.getParameter("fon_cod_2");
     paramCodtel3		= req.getParameter("fon_cod_3");
     paramMailContacto	= req.getParameter("email");
     paramEstado		= req.getParameter("sel_est");
     if(req.getParameter("margen").indexOf(",")>-1){
         //cambiar coma por punto
    	 paramMrgMinimo		= Double.parseDouble(req.getParameter("margen").replace(',','.'));
     }else
    	 paramMrgMinimo		= Double.parseDouble(req.getParameter("margen"));
     paramDsctoMaximo		= Double.parseDouble(req.getParameter("dscto"));
     
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("id_empresa: " + paramId_empresa);
     logger.debug("rut: " + paramRut);
     logger.debug("dv: " + paramDv);
     logger.debug("raz_social: " + paramRaz_social);
     logger.debug("nombre: " + paramNombre);
     logger.debug("rubro: " + paramRubro);
     logger.debug("descrip: " + paramDescripcion);
     logger.debug("cant_emp: " + paramCantEmpl);
     logger.debug("nom_cont: " + paramNomContacto);
     logger.debug("cargo: " + paramCargoContacto);
     logger.debug("cod_tel_1: " + paramCodtel1);
     logger.debug("telf_1: " + paramTel1Contacto);
     logger.debug("cod_tel_2: " + paramCodtel2);
     logger.debug("telf_2: " + paramTel2Contacto);
     logger.debug("cod_tel_3: " + paramCodtel3);
     logger.debug("telf_3: " + paramTel3Contacto);
     logger.debug("email: " + paramMailContacto);
     logger.debug("sel_est: " + paramEstado);
     logger.debug("margen: " + paramMrgMinimo);
     logger.debug("dscto: " + paramDsctoMaximo);
     
     ForwardParameters fp = new ForwardParameters();
     fp.add( req.getParameterMap() );
     
		EmpresasDTO prm = new EmpresasDTO();
 		prm.setEmp_id(paramId_empresa);
  		prm.setEmp_rut(paramRut);
  		prm.setEmp_dv(paramDv);
  		prm.setEmp_rzsocial(paramRaz_social);
  		prm.setEmp_nom(paramNombre);
  		prm.setEmp_rubro(paramRubro);
  		prm.setEmp_descr(paramDescripcion);
  		prm.setEmp_tamano("");
  		prm.setEmp_qtyemp(paramCantEmpl);
  		prm.setEmp_nom_contacto(paramNomContacto);
  		prm.setEmp_cargo_contacto(paramCargoContacto);
  		prm.setEmp_fono1_contacto(paramTel1Contacto);
  		prm.setEmp_fono2_contacto(paramTel2Contacto);
  		prm.setEmp_fono3_contacto(paramTel3Contacto);
  		prm.setEmp_mail_contacto(paramMailContacto);
  		prm.setEmp_estado(paramEstado);
  		prm.setEmp_mrg_minimo(paramMrgMinimo);
  		prm.setEmp_dscto_max(paramDsctoMaximo);
  		prm.setEmp_fmod(Formatos.getFecHoraActual());
  		prm.setEmp_cod_fon1(paramCodtel1);
  		prm.setEmp_cod_fon2(paramCodtel2);
  		prm.setEmp_cod_fon3(paramCodtel3);
		
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
     /*
      * 3. Procesamiento Principal
      */
     	BizDelegate bizDelegate = new BizDelegate();
     	
     	try{
     		
     		boolean result = bizDelegate.setModEmpresa(prm);
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
				fp.add( "id_empresa" , String.valueOf(paramId_empresa) );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepción: " + e.getMessage());
			}
     	}
	 

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
