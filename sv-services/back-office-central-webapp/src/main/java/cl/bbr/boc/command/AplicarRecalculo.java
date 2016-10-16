package cl.bbr.boc.command;



import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.pedidos.dto.DetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetallePedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.PromocionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;


/**
 * AddCampana Comando Process
 * Agrega una Campaña
 * @author BBRI
 */

public class AplicarRecalculo extends Command {
	private final static long serialVersionUID = 1;
 
 protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

     // 1. seteo de Variables del método
     String paramUrl = "";
     long id_pedido = -1;
     long id_local = -1;
     long total_reg=0;
     long id_promo = -1;
     String prod_rel = "";
     String mje_exito = "";
     String mje_fracaso = "";
     String UrlError ="";
     
     ArrayList lst_dp = new ArrayList();
     ArrayList lst_promo = new ArrayList();

     // 2. Procesa parámetros del request
     logger.debug("Procesando parámetros...");

     // 2.1 revision de parametros obligatorios
     if ( req.getParameter("url") == null ){throw new ParametroObligatorioException("url es null");}
     if ( req.getParameter("total_reg") == null ){throw new ParametroObligatorioException("total_reg es null");}
     if ( req.getParameter("id_pedido") == null ){throw new ParametroObligatorioException("id_pedido es null");}
     if ( req.getParameter("id_local") == null ){throw new ParametroObligatorioException("id_local es null");}
     
     // 2.2 obtiene parametros desde el request
     paramUrl = req.getParameter("url");
     total_reg = Long.parseLong(req.getParameter("total_reg"));
     id_pedido = Long.parseLong(req.getParameter("id_pedido"));
     id_local = Long.parseLong(req.getParameter("id_local"));
     
	 mje_fracaso = getServletConfig().getInitParameter("mensaje_fracaso");		
	 logger.debug("mensaje_fracaso: " + mje_fracaso);
	 mje_exito = getServletConfig().getInitParameter("mensaje_exito");		
	 logger.debug("mensaje_exito: " + mje_exito);
	 UrlError = getServletConfig().getInitParameter("UrlError");		
	 logger.debug("UrlError: " + UrlError);			
     
     // 2.3 log de parametros y valores
     logger.debug("paramUrl: " + paramUrl);
     logger.debug("total_reg: " + total_reg);
     logger.debug("id_pedido: " + id_pedido);
     logger.debug("id_local: " + id_local);

     ForwardParameters fp = new ForwardParameters();
	 fp.add( req.getParameterMap() );
  	  	 
     /*
      * 3. Procesamiento Principal
      */
  		BizDelegate biz = new BizDelegate();
  		

  		try{
	  		for (int i = 0 ; i< total_reg;i++){
	  			
	  			
	  			 if ( req.getParameter("id_promo_" + i) == null ){throw new ParametroObligatorioException("id_promo_"+i+" es null");}
	  			 if ( req.getParameter("prod_rel_" + i) == null ){throw new ParametroObligatorioException("prod_rel_"+i+" es null");}
	  			 
	  			 id_promo =  Long.parseLong(req.getParameter("id_promo_" + i));
	 			 prod_rel = req.getParameter("prod_rel_" + i);
	 			 logger.debug("id_promo: " + id_promo);
				 logger.debug("prod_rel: " + prod_rel);
	  			 
	  			 PromocionDTO promo = new PromocionDTO();
	  			 promo = biz.getPromocionById(id_promo);
	  			 
	  			 // Obtenemos los productos relacionados de la promoción
	  			 
	  			 String [] productos = prod_rel.split("#");
	  			 logger.debug("Tamaño lista de productos: " + productos.length);

	  			 for (int j=0;j<productos.length;j++){
	  				String[] prods = productos[j].split(",");	 
		  	  		
	  				double precio_lista = 0;
	  				double desc_unitario = 0;
	  				double precio = 0;
	  				double tasa_descuento = 0.0;
	  				precio_lista = Double.parseDouble(prods[2]);
	  				desc_unitario = Double.parseDouble(prods[3]);	 
	  				

	  				precio = precio_lista - desc_unitario;
	  				tasa_descuento = (1-(precio/precio_lista))*100;
	  				
	  				//logger.debug("LO QUE TRAE PRODUCTOS["+j+"]: " + productos[j] );
	  				//logger.debug("LO QUE SE ESTA INGRESANDO COMO DETALLE: " + Long.parseLong(prods[0]));
	  				
	  				PromoDetallePedidoDTO promodp = new PromoDetallePedidoDTO();
		  			promodp.setId_pedido(id_pedido);
		  			
		  			promodp.setId_promocion(id_promo);
		  			promodp.setPromo_codigo(promo.getCod_promo());
		  			promodp.setPromo_desc(promo.getDescr());
		  			promodp.setPromo_fini(promo.getFini());
		  			promodp.setPromo_ffin(promo.getFini());
		  			promodp.setPromo_tipo(promo.getTipo_promo()); 					
		  			 
	  				promodp.setId_detalle(Long.parseLong(prods[0]));
	  	  			promodp.setId_producto(Long.parseLong(prods[1]));
	  	  			logger.debug("la tasa de descuento que se ingresa es: " + tasa_descuento);
	  	  			promodp.setPromo_dscto_porc(tasa_descuento);
	  	  			logger.debug("Esto es lo que trae prods[0]" + prods[0]);
	  	  			
	  	  		

	  				
	  				DetallePedidoDTO dp = new DetallePedidoDTO();
					dp.setId_detalle(Long.parseLong(prods[0]));
					dp.setPrecio(precio);
					dp.setDscto_item(tasa_descuento);
					dp.setDesc_pesos_item(desc_unitario);
					dp.setPrecio_lista(precio_lista);
					logger.debug("PROMOCION: " + promo.getCod_promo());
					logger.debug("ID DETALLE: " + prods[0]);
					logger.debug("PRECIO DE LISTA: " + precio_lista);
					logger.debug("DESCUENTO UNITARIO: " + desc_unitario);
					logger.debug("TASA DESCUENTO_ITEM: " + tasa_descuento);
					
					// Agregamos a la lista el detalle de la promocion
	  	  			lst_promo.add(promodp);
					
	  				//Agregamos a la lista el detalle del pedido
					lst_dp.add(dp);
					
	  			 }
	  			 
	  			 
  			
	  		}
	  		
	  		
	  		// Aplicamos el recalculo
	  		biz.setAplicaRecalculo( lst_promo, lst_dp,id_pedido);
	  		fp.add( "mensaje" , mje_exito );
	  		paramUrl += "?id_pedido="+id_pedido+"&mensaje="+mje_exito;
  		}catch (BocException e){
  			logger.debug("Error:"+e.getMessage());
	    	 
	  			logger.debug("Controlando excepción: " + e.getMessage());
				paramUrl = UrlError+"?id_pedido="+id_pedido+"&mensaje="+mje_fracaso;
	  			logger.debug("paramUrl:"+paramUrl);
  		}

  		
     // 4. Redirecciona salida
     res.sendRedirect(paramUrl);

 }//execute

}
