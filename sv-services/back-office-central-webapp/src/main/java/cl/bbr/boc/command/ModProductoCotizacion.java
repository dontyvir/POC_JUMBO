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
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.cotizaciones.dto.LogsCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ModProdCotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;


/**
 * Comando que permite modificar los productos
 * @author bbr
 *
 */
public class ModProductoCotizacion extends Command {
	private final static long serialVersionUID = 1;
 
	 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

	     // 1. seteo de Variables del método
	     String paramUrl = "";
	     long   paramId_cot=-1;
	     long   paramId_detcot=-1;
	     double paramCantidad=0;
	     double paramDescuento=0;
	     String paramAccion="";
	     double precio=0;
	     double descuento=0;
	     boolean result=false;
	     // 2. Procesa parámetros del request
	
	     logger.debug("Procesando parámetros... ModProductoCotizacion");
	
	     // 2.1 revision de parametros obligatorios
	     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
	     paramUrl    = req.getParameter("url");
	     logger.debug("url: " + paramUrl);
	     
   	     if ( req.getParameter("cot_id") == null ){throw new ParametroObligatorioException("cot_id es nulo");}
   	     logger.debug("cot_id: " + req.getParameter("cot_id"));
   	     paramId_cot = Long.parseLong(req.getParameter("cot_id")); //long:obligatorio:si
   	     logger.debug("cot_id: " + paramId_cot);
   	  
	     if ( req.getParameter("accion") == null ){throw new ParametroObligatorioException("accion es nulo");}
	     paramAccion = req.getParameter("accion");
	     if (paramAccion.equals("aplica")){
	    	 paramDescuento = Double.parseDouble(req.getParameter("descto"));
	     }
	     
	     // 2.3 log de parametros y valores
	     
	     
	     logger.debug("id_detcot: " + paramId_detcot);

	     
	     
	     ForwardParameters fp = new ForwardParameters();
		 fp.add( req.getParameterMap() );
	     /*
	      * 3. Procesamiento Principal
	      */
	     BizDelegate biz = new BizDelegate();
	     String mensActual = getServletConfig().getInitParameter("MensActual");
	     
	     try{
	    	 
	    	 List listaProductos = null;

	 		listaProductos =  biz.getProductosCotiz(paramId_cot);
	 		
	 		for (int i=0; i<listaProductos.size(); i++){			
				ProductosCotizacionesDTO prod = new ProductosCotizacionesDTO();
				ModProdCotizacionesDTO prodCambio = new ModProdCotizacionesDTO();
				prod = (ProductosCotizacionesDTO)listaProductos.get(i);
				
				prodCambio.setDetcot_id(prod.getDetcot_id());
				prodCambio.setDetcot_cot_id(prod.getDetcot_cot_id());
				
				
				if (paramAccion.equals("guarda")){
					paramDescuento = Double.parseDouble(req.getParameter("descuento_"+i));	
					paramCantidad = Double.parseDouble(req.getParameter("cantidad_"+i));
				}else{
					paramCantidad = prod.getDetcot_cantidad();	
				}
				prodCambio.setDetcot_cantidad(paramCantidad);
				prodCambio.setDetcot_dscto_item(paramDescuento);
				precio =prod.getDetcot_precio_lista() - (prod.getDetcot_precio_lista() * (paramDescuento/100));				
				prodCambio.setDetcot_precio(precio);
				
				result = biz.updProductoCotizacion(prodCambio);
			}
	     if (result){
				LogsCotizacionesDTO log = new LogsCotizacionesDTO();
				String mjelog = "Se modificaron productos de la cotización.";
				log.setCot_id(paramId_cot);
				log.setDescripcion(mjelog);
				log.setUsuario(usr.getLogin());
				biz.addLogCotizacion(log);
				logger.debug("Se modifico producto cotización");
			}
	     }catch(BocException e) {
	    	 logger.debug("Controlando excepción del AddProdcatweb: " + e.getMessage());
	    	 String UrlError = getServletConfig().getInitParameter("UrlError");
	    	 if ( e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE) ){
					logger.debug("El código de producto ingresado no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } 
	    	 if ( e.getMessage().equals(Constantes._EX_PROD_ID_MAR_NO_EXISTE) ){
					logger.debug("El código de marca no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_MAR_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 } 
			 if ( e.getMessage().equals(Constantes._EX_PROD_ID_UME_NO_EXISTE) ){
					logger.debug("El código de unidad de medida no existe");
					fp.add( "rc" , Constantes._EX_PROD_ID_UME_NO_EXISTE );
					paramUrl = UrlError + fp.forward(); 
			 }else {
					logger.debug("Controlando excepción: " + e.getMessage());
			 }
	     }
	
	     // 4. Redirecciona salida
	     res.sendRedirect(paramUrl);

	 }//execute

}
