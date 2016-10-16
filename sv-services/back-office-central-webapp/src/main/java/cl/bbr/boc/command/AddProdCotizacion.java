package cl.bbr.boc.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.dto.ProductosDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;

/**
 * AddCatWeb Comando Process
 * agrega un producto a una categoria web
 * @author BBRI
 */

public class AddProdCotizacion extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	      // 1. seteo de Variables del método
	 String paramUrl = "";
     long paramIdCot= -1;//obligatorio
     long paramIdProd = -1;//obligatorio
     double paramCant = 0;//obligatorio
     double paramDesc = 0;//obligatorio
     long paramIdLoc = -1;//obligatorio
     String paramObs = "";
     
     // 1.1 Parámetros de inicialización servlet
     // Recupera pagina desde web.xml
 	
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...AddProdCotizacion");

     // 2.1 revision de parametros obligatorios
		if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null");	}
		if ( req.getParameter("cot_id") == null){ throw new ParametroObligatorioException("id_cat es null"); }
		if ( req.getParameter("id_prod") == null){ throw new ParametroObligatorioException("id_prod es null"); }
		if ( req.getParameter("id_loc") == null){ throw new ParametroObligatorioException("id_loc es null"); }
		if ( req.getParameter("cant") == null){ throw new ParametroObligatorioException("cantidad es null"); }
		
		
     // 2.2 obtiene parametros desde el request
		paramUrl = req.getParameter("url");
		paramIdCot = Long.parseLong(req.getParameter("cot_id"));
		paramCant = Double.parseDouble(req.getParameter("cant"));
		paramIdProd = Long.parseLong(req.getParameter("id_prod"));
		paramDesc = Double.parseDouble(req.getParameter("desc"));
		paramObs = req.getParameter("obs");
		paramIdLoc = Long.parseLong(req.getParameter("id_loc"));
		
     // 2.3 log de parametros y valores
		logger.debug("url: " + paramUrl);
		logger.debug("paramIdCot: " + paramIdCot);
		logger.debug("id_prod: " + paramIdProd);
		logger.debug("paramCant: " + paramCant);
		logger.debug("paramDesc: " + paramDesc);
		logger.debug("paramIdLoc: " + paramIdLoc);
		logger.debug("obs : " + paramObs);
	
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		
     /*
      * 3. Procesamiento Principal
      */
		BizDelegate bizDelegate = new BizDelegate();
		boolean result = false;
		try{
			ProductosDTO prod = new ProductosDTO();
			logger.debug("antes de prod " );
			prod = bizDelegate.getProductosById(paramIdProd);
			
			
			ProductosCotizacionesDTO prodCot = new ProductosCotizacionesDTO();
			double precio = bizDelegate.getProductosPrecios(paramIdProd, paramIdLoc); 
			prodCot.setDetcot_cot_id(paramIdCot);
			prodCot.setDetcot_proId(paramIdProd);
			prodCot.setDetcot_cantidad(paramCant);
			prodCot.setDetcot_pro_id_bo(prod.getId_bo());
			prodCot.setDetcot_codSap(prod.getCod_sap());
			prodCot.setDetcot_umed(prod.getTipre());
			prodCot.setDetcot_desc(prod.getDesc_corta());
			prodCot.setDetcot_obs(paramObs);
			prodCot.setDetcot_preparable(prod.getEs_prep());
			prodCot.setDetcot_pesable(prod.getEs_pesable());
			prodCot.setDetcot_con_nota(prod.getAdm_coment());
			prodCot.setDetcot_dscto_item(paramDesc);
			prodCot.setDetcot_precio_lista(precio);
			double precioUnit = precio - (precio * (paramDesc/100));
			prodCot.setDetcot_precio(precioUnit);		
			
			
		 
			result = bizDelegate.addProductoCotizacion(prodCot);
			
			if (result){
				logger.debug("id: " + paramIdCot + " usuario " + usr.getLogin());
				LogsCotizacionesDTO log = new LogsCotizacionesDTO();
				String mjelog = "Se agrego un producto a la cotización.";
				log.setCot_id(paramIdCot);
				log.setDescripcion(mjelog);
				log.setUsuario(usr.getLogin());
				bizDelegate.addLogCotizacion(log);
				
			}
		}catch(BocException e){
	    	 logger.debug("Error:"+e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("url_fracaso");
	    	 if (  e.getMessage().equals(Constantes._EX_COT_PROD_PRECIO_CERO) ){
					logger.debug("El producto tiene precio 0.");
					fp.add( "rc" , Constantes._EX_COT_PROD_PRECIO_CERO );
					
					paramUrl = UrlError + fp.forward();
					logger.debug("paramUrl: " +paramUrl);
				}
	    	 if (  e.getMessage().equals(Constantes._EX_COT_CODBARRA_NO_EXISTE) ){
					logger.debug("Código de barras no existe.");
					fp.add( "rc" , Constantes._EX_COT_CODBARRA_NO_EXISTE );
					
					paramUrl = UrlError + fp.forward();
					logger.debug("paramUrl: " +paramUrl);
				}
	    	 if (  e.getMessage().equals(Constantes._EX_COT_PROD_DESPUBLICADO) ){
					logger.debug("El producto despublicado.");
					fp.add( "rc" , Constantes._EX_COT_PROD_DESPUBLICADO);
					
					paramUrl = UrlError + fp.forward();
					logger.debug("paramUrl: " +paramUrl);
				}
	    	 if (  e.getMessage().equals(Constantes._EX_COT_PROD_ID_NO_EXISTE) ){
					logger.debug("El producto no existe.");
					fp.add( "rc" , Constantes._EX_COT_PROD_ID_NO_EXISTE);
					
					paramUrl = UrlError + fp.forward();
					logger.debug("paramUrl: " +paramUrl);
				}
	    	 if (e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El producto no existe.");
					fp.add( "rc" , Constantes._EX_COT_PROD_ID_NO_EXISTE);
					
					paramUrl = UrlError + fp.forward();
					logger.debug("paramUrl: " +paramUrl);
				}
			
		}
		
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
