package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelCategoryProductDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * Comando que permite suprimir una Categoria para los productos
 * @author bbr
 *
 */
public class ModSupCatProduct extends Command {
	private final static long serialVersionUID = 1;
 

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	     
	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long paramId_producto=0L;
	     long paramId_categoria=0L;
	     String paramOrigen="";
	     
	     // 2. Procesa parámetros del request
	     logger.debug("Procesando parámetros...");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es nulo");}
	     if ( req.getParameter("id_prod") == null ){throw new ParametroObligatorioException("id_prod es nulo");}
	     if ( req.getParameter("id_cat") == null ){throw new ParametroObligatorioException("id_cat es nulo");}
	     if ( req.getParameter("origen") == null ){throw new ParametroObligatorioException("origen es nulo");}
	
	     // 2.2 obtiene parametros desde el request
	     paramUrl = req.getParameter("url");
	     paramId_producto = Long.parseLong(req.getParameter("id_prod")); //long:obligatorio:si
	     paramId_categoria = Long.parseLong(req.getParameter("id_cat"));
	     paramOrigen = req.getParameter("origen");
	
	     // 2.3 log de parametros y valores
	     logger.debug("url: " + paramUrl);
	     logger.debug("id_prod: " + paramId_producto);
	     logger.debug("id_cat: " + paramId_categoria);
	     logger.debug("origen: " + paramOrigen);

	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
		 
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
    	 String UrlError = "";
    	 if(paramOrigen.equals("CAT")) UrlError = getServletConfig().getInitParameter("UrlErrorCat");
    	 if(paramOrigen.equals("PROD")) UrlError = getServletConfig().getInitParameter("UrlErrorProd");

	     try{
			 String mensDesa = getServletConfig().getInitParameter("MensDesa");
			 ProcDelCategoryProductDTO param = new ProcDelCategoryProductDTO(paramId_categoria, paramId_producto, 
					 usr.getLogin(), mensDesa); 
			 boolean result = biz.setDelCategoryProduct(param);
			 logger.debug("desasociar:"+result);
			 List lst_cat = biz.getCategoriasByProductId(paramId_producto);
			 logger.debug("categorias relacionadas:"+lst_cat.size());
			 if(lst_cat.size()==0){
				 	logger.debug("Se eliminaron todas las categorias.");
					fp.add( "rc" , Constantes._EX_PROD_SIN_CAT );
					paramUrl = UrlError + fp.forward(); 
					logger.debug("paramUrl:"+paramUrl);
			 }
	     }catch(BocException e) {
	    	 logger.debug("Controlando excepción del AddProdcatweb: " + e.getMessage());
	    	 if ( e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El código de producto ingresado no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } 
	    	 if ( e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE) ){
					logger.debug("El código de la categoria no existe");
					fp.add( "rc" , Constantes._EX_CAT_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } 
			 if ( e.getMessage().equals(Constantes._EX_CAT_PROD_REL_NO_EXISTE) ){
					logger.debug("La relación entre categoría y producto no existen.No se puede eliminar.");
					fp.add( "rc" , Constantes._EX_CAT_PROD_REL_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 }
			 /*if ( e.getMessage().equals(Constantes._EX_PROD_CAT_NO_TIENE) ){
					logger.debug("Se eliminaron todas las categorias.");
					fp.add( "rc" , Constantes._EX_PROD_CAT_NO_TIENE );
					paramUrl = UrlError + fp.forward(); 
			 }	*/
    		 else {
					logger.debug("Controlando excepción: " + e.getMessage());
			 }
	    	 
	     }
			
		 /*
		 ProcDelCategoryProductDTO param = null;
		 String fec_crea = "";
		 ProductoLogDTO log = null;
		 String nom_categ = "";
			param = new ProcDelCategoryProductDTO(paramId_categoria, paramId_producto); 
			result = biz.setDelCategoryProduct(param);
			logger.debug("desasociar:"+result);
		     if(result){
				 // agregar la accion en el log del producto
				 fec_crea = Formato.getFecHoraActual();
				 log = new ProductoLogDTO(); 
				 log.setCod_prod(paramId_producto);
				 log.setFec_crea(fec_crea);
				 log.setUsuario(usr.getApe_paterno());
				 nom_categ = biz.getCategoriasById(paramId_categoria).getNombre();
				 log.setTexto(nom_categ+":"+mensDesa);
				 int resLog = biz.setLogProduct(log);
				 logger.debug("se guardo log con id:"+resLog);
		     }
	     */
	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);
	
	}//execute

}
