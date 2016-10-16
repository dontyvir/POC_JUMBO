package cl.bbr.boc.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcPubDespProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que publica o despublica productos
 * @author bbr
 *
 */
public class PubDespProduct extends Command {
	
	private final static long serialVersionUID = 1;
 
	
	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
     
	     // 1. seteo de Variables del método
		 String motivo ="";
		 long sel_mot =0L;
	     String paramUrl = "";
	     long paramId_producto=0L;
	     int paramAction=0;
	     String paramGenerico = "";
	     String paramTipo = "";
	     String paramIdMarca = "";
	     String paramDesCor = "";
	     String paramDesLar = "";
	     String paramUniMed = "";
	     double paramConten = 0;
	     String paramAdmCom = "";
	     String paramEsPrep = "";
	     double paramIntVal = 0;
	     double paramIntMax = 0;
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     if ( req.getParameter("id_producto") == null ){throw new ParametroObligatorioException("id_producto es nulo");}
	     if ( req.getParameter("action") == null ){throw new ParametroObligatorioException("action es nulo");}
	     if ( req.getParameter("tipo_prod") == null ){throw new ParametroObligatorioException("Tipo_prod es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_producto = Long.parseLong(req.getParameter("id_producto")); //long:obligatorio:si
	     
	     paramAction = Integer.parseInt(req.getParameter("action")); //int:obligatorio:si
	     if (paramAction == 2){
	    	 if ( req.getParameter("motivo") == null ){throw new ParametroObligatorioException("Observación es nulo");}	    		
	    	 if ( req.getParameter("sel_mot") == null ){throw new ParametroObligatorioException("motivo es nulo");}
	     }
	     
	     motivo = req.getParameter("sel_mot")+" : "+req.getParameter("motivo");
	     if ( paramAction == 2  )
	    	 sel_mot = Long.parseLong(req.getParameter("sel_mot"));
	     
	     if (paramAction == 1){
		     paramGenerico 	= req.getParameter("tipo_prod"); //String:obligatorio:si
		     paramTipo 		= req.getParameter("tipo");
		     paramIdMarca 	= req.getParameter("id_marca");
		     paramDesCor 	= req.getParameter("desc_corta");
		     paramDesLar 	= req.getParameter("desc_larga");
		     paramUniMed 	= req.getParameter("uni_med");
		     paramConten 	= Double.parseDouble(req.getParameter("contenido"));
		     paramAdmCom 	= req.getParameter("adm_com");
		     paramEsPrep 	= req.getParameter("es_prep");
		     paramIntVal 	= Double.parseDouble(req.getParameter("int_val"));
		     paramIntMax 	= Double.parseDouble(req.getParameter("int_max"));	     
	     }
	     
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_producto: " + paramId_producto);
	     logger.debug("action: " + paramAction);
	     logger.debug("tipo_prod: " + paramGenerico);
	     logger.debug("tipo: " + paramTipo);
	     logger.debug("id_marca: " + paramIdMarca);
	     logger.debug("desc_corta: " + paramDesCor);
	     logger.debug("desc_larga: " + paramDesLar);
	     logger.debug("uni_med: " + paramUniMed);
	     logger.debug("contenido: " + paramConten);
	     logger.debug("adm_com: " + paramAdmCom);
	     logger.debug("es_prep: " + paramEsPrep);
	     logger.debug("int_val: " + paramIntVal);
	     logger.debug("int_max: " + paramIntMax);
	     logger.debug("motivo: " + motivo);
	     logger.debug("sel_motivo: " + sel_mot);
	     logger.debug("Tipo "+ paramTipo);
	     
	     /*
	      * 3. Procesamiento Principal
	      */
	     ForwardParameters fp = new ForwardParameters();
	 	 fp.add( req.getParameterMap() );
	     try{
	     	BizDelegate biz = new BizDelegate();
			String mensPubl = getServletConfig().getInitParameter("MensPubl");
			String mensDesp = getServletConfig().getInitParameter("MensDesp");
			ProcPubDespProductDTO param = new ProcPubDespProductDTO ();
			param.setId_producto(paramId_producto);
			param.setAction(paramAction);
			param.setGenerico(paramGenerico);
			param.setTipo(paramTipo);
			param.setIdMarca(paramIdMarca);			
			param.setDesCor(paramDesCor);
			param.setDesLar(paramDesLar);
			param.setUniMed(paramUniMed);
			param.setConten(paramConten);
			param.setAdmCom(paramAdmCom);
			param.setEsPrep(paramEsPrep);
			param.setIntVal(paramIntVal);
			param.setIntMax(paramIntMax);
			param.setId_usr(usr.getId_usuario());
			param.setUsr_login(usr.getLogin());
			param.setMensPubl(mensPubl);
			param.setMensDesp(mensDesp);
			param.setMotivo(motivo);
			param.setId_motivo(sel_mot);
			biz.setPubDespProduct(param);
	     }catch(BocException e){
	    	 logger.debug("Controlando excepción: " + e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("UrlError");
	    	 if ( e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El código de producto ingresado no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } if ( e.getMessage().equals(Constantes._EX_PROD_CAT_NO_TIENE) ){
					logger.debug("El producto no tiene categoria relacionada. No puede ser publicada");
					fp.add( "rc" , Constantes._EX_PROD_CAT_NO_TIENE );
					paramUrl = UrlError + fp.forward(); 
			 } else {
					logger.debug("Controlando excepción: " + e.getMessage());
			 }
	     }
	     	/*
			// agregar la accion en el log del producto
			String fec_crea = Formato.getFecHoraActual();
			ProductoLogDTO log = new ProductoLogDTO(); 
			log.setCod_prod(paramId_producto);
			log.setFec_crea(fec_crea);
			log.setUsuario(usr.getApe_paterno());
			//accion 1 : Publicar
			if(paramAction==1)
				log.setTexto(mensPubl);
			//accion 2 : DesPublicar
			else if (paramAction==2)
				log.setTexto(mensDesp);
			int resLog = biz.setLogProduct(log);
			logger.debug("se guardo log con id:"+resLog);
			
				//es un producto item y se despublico
				//obtener prod generico donde el item despublicado fuera el unico item 
				//List lst_prod = biz.getProductGenericos(paramId_producto);
				//en el caso q exista, enviar mensaje. "Los siguientes productos genéricos procederan a ser despublicados"
				//con botones Aceptar y Cancelar
				//en caso q acepte, despublicar para cada producto generico:
				//*
				for (int i = 0; i < lst_prod.size(); i++) {
					ProductosDTO pro = (ProductosDTO) lst_prod.get(i); 
					ProcPubDespProductDTO paramPadre = new ProcPubDespProductDTO (pro.getId(), paramAction, paramGenerico);
					biz.setPubDespProduct(paramPadre);
				}
				//*
			//}
	
			
			if(paramAction==2){			
				ProductoLogDTO log1 = new ProductoLogDTO();
				log1.setCod_prod(id_producto);
				log1.setFec_crea(Formato.getFecHoraActual());
				log1.setUsuario(usr.getLogin());
				log1.setTexto("El motivo de Despublicación es: "+ motivo);
				biz.setLogProduct(log1);
			}
			*/
			
				
	     // 4. Redirecciona salida
	//		res.
			
	     res.sendRedirect(paramUrl);

	}//execute

}
