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
import cl.bbr.vte.empresas.dto.ComprXSucDTO;
import cl.bbr.vte.empresas.dto.CompradoresDTO;

/**
 * AddComprador Comando Process
 * Agrega un comprador asociado a una sucursal
 * 
 * @author BBRI
 */

public class AddCompradorWizard extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del m�todo
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
     String UrlError            ="";
     String esAdm               ="0";
     String paramPreg			="";
     String paramResp			="";
     
     // 2. Procesa par�metros del request
     logger.debug("Procesando par�metros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("rut") == null ){throw new ParametroObligatorioException("rut es null");}
     if ( req.getParameter("dv") == null ){throw new ParametroObligatorioException("dv es null");}
     if ( req.getParameter("nombre") == null ){throw new ParametroObligatorioException("nombre es null");}
     if ( req.getParameter("paterno") == null ){throw new ParametroObligatorioException("paterno es null");}
     if ( req.getParameter("materno") == null ){throw new ParametroObligatorioException("materno es null");}
     if ( req.getParameter("sucursales[]") == null ){throw new ParametroObligatorioException("sucursales[] es null");}
     //if ( req.getParameter("sel_tip") == null ){throw new ParametroObligatorioException("sel_tip es null");}
     if ( req.getParameter("sel_gen") == null ){throw new ParametroObligatorioException("sel_gen es null");}
     if ( req.getParameter("telf_1") == null ){throw new ParametroObligatorioException("telf_1 es null");}
     if ( req.getParameter("telf_2") == null ){throw new ParametroObligatorioException("telf_2 es null");}
     if ( req.getParameter("telf_3") == null ){throw new ParametroObligatorioException("telf_3 es null");}
     if ( req.getParameter("email") == null ){throw new ParametroObligatorioException("email es null");}
     if ( req.getParameter("sel_est") == null ){throw new ParametroObligatorioException("sel_est es null");}
     if ( req.getParameter("clave") == null ){throw new ParametroObligatorioException("clave es null");}
     if ( req.getParameter("emp_id") == null ){throw new ParametroObligatorioException("emp_id es null");}
     if ( req.getParameter("UrlError") == null ){throw new ParametroObligatorioException("urlError es null");}
     if ( req.getParameter("preg_secreta") == null ){throw new ParametroObligatorioException("preg_secreta es null");}
     if ( req.getParameter("resp_secreta") == null ){throw new ParametroObligatorioException("resp_secreta es null");}
     
     String[] sucursales = req.getParameterValues("sucursales[]");
     
     logger.debug("n� sucursales: " + sucursales.length );
     // 2.2 obtiene parametros desde el request
     paramUrl 			= req.getParameter("url");
     paramRut			= Long.parseLong(req.getParameter("rut"));
     paramDv			= req.getParameter("dv");
     paramNombre		= req.getParameter("nombre");
     paramAp_paterno	= req.getParameter("paterno");
     paramAp_materno	= req.getParameter("materno");
    // sucursales[]        = req.getParameter("sucursales");
     //paramId_sucursal	= Long.parseLong(req.getParameter("sel_emp_suc"));
     //paramTipo			= req.getParameter("sel_tip");
     paramGenero		= req.getParameter("sel_gen");
     paramTelef1		= req.getParameter("telf_1");
     paramTelef2		= req.getParameter("telf_2");
     paramTelef3		= req.getParameter("telf_3");
     paramMailContacto	= req.getParameter("email");
     paramEstado		= req.getParameter("sel_est");
     paramClave		    = req.getParameter("clave");
     paramEmpId		    = Long.parseLong(req.getParameter("emp_id"));
     UrlError		    = req.getParameter("UrlError");
     paramPreg			= req.getParameter("preg_secreta");
     paramResp			= req.getParameter("resp_secreta");     
     
     
     if (req.getParameter("esAdm") != null){
    	 esAdm = req.getParameter("esAdm");
     }
     
     
     int cant_suc = 0;
     // 2.3 log de parametros y valores
     logger.debug("url: " + paramUrl);
     logger.debug("rut: " + paramRut);
     logger.debug("dv: " + paramDv);
     logger.debug("nombre: " + paramNombre);
     logger.debug("paterno: " + paramAp_paterno);
     logger.debug("materno: " + paramAp_materno);
    // logger.debug("sel_emp_suc: " + paramId_sucursal);
     logger.debug("sel_tip: " + paramTipo);
     logger.debug("sel_gen: " + paramGenero);
     logger.debug("telf_1: " + paramTelef1);
     logger.debug("telf_2: " + paramTelef2);
     logger.debug("telf_3: " + paramTelef3);
     logger.debug("email: " + paramMailContacto);
     logger.debug("sel_est: " + paramEstado);
     logger.debug("clave: " + paramClave);
     logger.debug("emp_id" + paramEmpId);
     logger.debug("esAdm" + esAdm);
     logger.debug("pregunta:"+ paramPreg);
     logger.debug("respuesta:"+ paramResp);     
     
 	 ForwardParameters fp = new ForwardParameters();
	 fp.add( req.getParameterMap() );
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
  		//prm.setId_sucursal(paramId_sucursal);
  		
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
  		prm.setId_empresa(paramEmpId);
  		prm.setCpr_pregunta(paramPreg);
  		prm.setCpr_respuesta(paramResp); 
  		
  		cant_suc = sucursales.length;
  		//Si vienen sucursales, asignamos la primera
  		if (cant_suc > 0){
  			prm.setId_sucursal(Long.parseLong(sucursales[0]));
  		}
  		
  		//Si es Administrador
  		if (esAdm.equals("1")){
  			prm.setCpr_tipo("A");
  		}else{
  			prm.setCpr_tipo(Constantes.CADENA_VACIA);
  		}
  		
  		
  		//obtiene mensajes
		String mensaje_exito = getServletConfig().getInitParameter("mensaje_exito"); 
 		String mensaje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");
  	     
  		boolean result = false;
  		
  		try{
  			long id = biz.setCreaComprador(prm);
  			logger.debug("result?"+result);

  			// Si creo el comprador
  			if(id > 0 ){
  				//Se verifica si es Administrador y se agrega relacion
  				/*int falloRelacionEmp= 0;
  				logger.debug("LLEGO A");
  				if (esAdm.equals("1")){
  					ComprXEmpDTO adm = new ComprXEmpDTO();
  					adm.setId_empresa(paramEmpId);
  					adm.setId_comprador(id);
  			 		boolean resultado = false;
  			 		
  			  		try{
  			  			resultado = biz.addRelEmpresaComprador(adm);
  			  			
  			  			if(!resultado){
  			  				falloRelacionEmp++;
  			  			}
  			  			logger.debug("LLEGO AKKKKKKKKKKKKK");
  			  		} catch(BocException e){
  			  	  		
  						if ( e.getMessage()!= null && !e.getMessage().equals("") ){
  							logger.debug("mensaje:"+e.getMessage());
  							fp.add( "msje" , e.getMessage() );
  							paramUrl = UrlError + fp.forward(); 
  						} else { 
  							logger.debug("Controlando excepci�n al agregar relacion empresa: " + e.getMessage());
  						}
  			     	}
  				}
  				logger.debug("LLEGO AQUIIIIIIIIII");*/
  				//Se agregan las sucursales para las cuales es comprador
  				
  				ComprXSucDTO cpr = new ComprXSucDTO();
  				int falloRelSuc = 0;
  				//logger.debug("id sucursal: " + sucursales[0]);
  				if (cant_suc > 1){
  					//El for parte en 1 porque la primera sucursal ya se agrego
	  				for (int i=1;i<cant_suc;i++){
	  					long id_suc = Long.parseLong(sucursales[i]);
	  					logger.debug("id sucursal: " + id_suc);
	  					logger.debug("id comprador: " + id);
	  					cpr.setId_sucursal(id_suc);
	  					cpr.setId_comprador(id);
	  					boolean resp = false;
	  					try{
	  						resp = biz.addRelSucursalComprador(cpr);
	  						if (!resp){
	  							falloRelSuc++;
	  	  					}
	  					}catch (BocException e){
	  						if ( e.getMessage()!= null && !e.getMessage().equals("") ){
	  							logger.debug("mensaje:"+e.getMessage());
	  							fp.add( "msje" , e.getMessage() );
	  							paramUrl = UrlError + fp.forward(); 
	  						} else { 
	  							logger.debug("Controlando excepci�n con sucursal: " + e.getMessage());
	  						}
	  					}
	  				}
  				}
  				if (falloRelSuc == 0 ){
	  				fp.add( "emp_id" , String.valueOf(paramEmpId));
	  				fp.add( "id_comprador" , String.valueOf(id));
	  				fp.add( "msje" , mensaje_exito);
					paramUrl = paramUrl + fp.forward();
  				}else{
  					fp.add( "msje" , mensaje_fracaso);
  	  				paramUrl = UrlError + fp.forward();
  				}
  				
  			}else{
  				fp.add( "msje" , mensaje_fracaso);
  				paramUrl = UrlError + fp.forward();
  			}
  			
  		} catch(BocException e){

  	  	    
     		logger.debug("Controlando excepci�n: " + e.getMessage());
			
			if ( e.getMessage()!=null &&  !e.getMessage().equals("")){
				logger.debug(e.getMessage());
				//fp.add( "rc" , Constantes._EX_EMP_ID_NO_CREADO );
				fp.add( "msje" , e.getMessage() );
				paramUrl = UrlError + fp.forward(); 
			} else { 
				logger.debug("Controlando excepci�n: " + e.getMessage());
			}
     	}
  		
  		logger.debug("result:"+result);

     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
