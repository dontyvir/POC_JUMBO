package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.exceptions.BocException;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcAddCategoryProductDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoCategDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * AddCatWeb Comando Process
 * agrega un producto a una categoria web
 * @author BBRI
 */

public class AddProdCatWeb extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	      // 1. seteo de Variables del método
	 String paramUrl = "";
     String paramIdCateg = "";//obligatorio
     String paramIdProd = "";//obligatorio
     String paramOrden = "";//obligatorio
     String paramNomCateg = "";
     String paramOrigen = "";
     String paramConPago = "";
     String action = "";
     
     long id_prca = -1;
     long id_cat = -1;
     long id_prod = -1;
     String estado = "";
     int orden = -1;
     
     // 1.1 Parámetros de inicialización servlet
     // Recupera pagina desde web.xml
 	
     // 2. Procesa parámetros del request

     logger.debug("Procesando parámetros...AddProdCatWeb");

     // 2.1 revision de parametros obligatorios
		if ( req.getParameter("url") == null ){ throw new ParametroObligatorioException("url es null");	}
		if ( req.getParameter("id_cat") == null){ throw new ParametroObligatorioException("id_cat es null"); }
		if ( req.getParameter("id_prod") == null){ throw new ParametroObligatorioException("id_prod es null"); }
		if ( req.getParameter("orden") == null){ throw new ParametroObligatorioException("orden es null"); }
		if ( req.getParameter("nom_cat") == null){ throw new ParametroObligatorioException("nom_cat es null"); }
		if ( req.getParameter("origen") == null){ throw new ParametroObligatorioException("origen es null"); }
		if ( req.getParameter("rad_pago") == null){ throw new ParametroObligatorioException("rad_pago es null"); }
		if ( req.getParameter("action") != null){
			action = req.getParameter("action");
			logger.debug("action: "+action);
		}else
		{
			action ="AddProdCatWeb";
			logger.debug("action: "+action);
		}
		if(action.equals("ModProdCatWeb")){
			id_prca = Long.parseLong(req.getParameter("procat_id"));
		}
     // 2.2 obtiene parametros desde el request
		paramUrl = req.getParameter("url");
		paramIdCateg = req.getParameter("id_cat");
		id_cat = Long.parseLong(paramIdCateg);
		paramIdProd = req.getParameter("id_prod");
		id_prod = Long.parseLong(paramIdProd);
		//paramEstado = "A";
		estado = "A";
		paramOrden = req.getParameter("orden");
		orden = Integer.parseInt(paramOrden);
		paramNomCateg = req.getParameter("nom_cat");
		paramOrigen = req.getParameter("origen");
		paramConPago = req.getParameter("rad_pago");

     // 2.3 log de parametros y valores
		logger.debug("url: " + paramUrl);
		logger.debug("id_cat: " + paramIdCateg);
		logger.debug("id_prod: " + paramIdProd);
		logger.debug("orden: " + paramOrden);
		logger.debug("nom_cat: " + paramNomCateg);
		logger.debug("origen: " + paramOrigen);
		logger.debug("con pago: " + paramConPago);
     
		ForwardParameters fp = new ForwardParameters();
		fp.add( req.getParameterMap() );
		
     /*
      * 3. Procesamiento Principal
      */
		BizDelegate bizDelegate = new BizDelegate();
		List lista = bizDelegate.getProductosByCategId(id_cat);
		long id_procat;
		for (int i = 0; i < lista.size(); i++) {
			ProductoCategDTO prod = (ProductoCategDTO)lista.get(i);
			if(prod.getId_prod()==id_prod){
				id_procat=prod.getId();
			}
		}	
		try{
			String mensAsoc = getServletConfig().getInitParameter("MensAsoc");
			boolean result; 
			if(action.equals("AddProdCatWeb")){
				ProcAddCategoryProductDTO param = new ProcAddCategoryProductDTO(id_cat, id_prod, estado, orden, mensAsoc, 
						usr.getLogin()); 
				param.setCon_pago(paramConPago);				
				result = bizDelegate.setAddCategoryProduct(param);
			}else{			
				ProductoCategDTO param = new ProductoCategDTO();
				param.setId(id_prca);
				param.setId_prod(id_prod);
				param.setOrden(orden);
				param.setCon_pago(paramConPago);
				result = bizDelegate.setModCategoryProduct(param);
			}
			logger.debug("resultado final:"+result);
			
		}catch(BocException e) {
			logger.debug("Controlando excepción del AddProdcatweb: " + e.getMessage());
			String UrlError = "";
		    if(paramOrigen.equals("CAT")) UrlError = getServletConfig().getInitParameter("UrlErrorCat");
		    if(paramOrigen.equals("PROD")) UrlError = getServletConfig().getInitParameter("UrlErrorProd");
			if (  e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
				logger.debug("El código de la categoría ingresado no existe");
				fp.add( "rc" , Constantes._EX_CAT_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			} else if ( e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
				logger.debug("El código de producto ingresado no existe");
				fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
				paramUrl = UrlError + fp.forward();
			} else if ( e.getMessage().equals(Constantes._EX_CAT_PROD_REL_EXISTE) ){
				logger.debug("La relacion entre categoria y producto ya existe.");
				fp.add( "rc" , Constantes._EX_CAT_PROD_REL_EXISTE );
				paramUrl = UrlError + fp.forward();
			} else if ( e.getMessage().equals(Constantes._EX_CAT_NO_ES_TERMINAL) ){
				logger.debug("La categoria no es terminal y no se puede asociar al producto.");
				fp.add( "rc" , Constantes._EX_CAT_NO_ES_TERMINAL );
				paramUrl = UrlError + fp.forward();
			} else {
				logger.debug("Controlando excepción: " + e.getMessage());
			}
		} catch(Exception e){
			logger.debug("Controlando excepción desconocida: " + e.getMessage());
			
		}
		
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
