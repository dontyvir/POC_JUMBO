package cl.bbr.bol.command;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 *   Clase que se activa solamente si el pedido esta en Revision Faltante
 * 	 hay 2 botones , Pasar a En bodega que su valor de boton en B y
 * 	 Repickear , su valor es R
 * 		
 * 
 * @author RMI -DNT
 */

public class RevisionFaltante extends Command {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }

    protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
    	logger.debug("-----Nombre de la clase Actual-------- " + getClass());
        String paramUrl			= "";
        String msg = "OK";
        BizDelegate biz = new BizDelegate();
      	paramUrl = getServletConfig().getInitParameter("Url");
      	//Genera un Objeto de el tipo pedidoLog , que crea un log nuevo
      	LogPedidoDTO log = new LogPedidoDTO();
      	//    	 Recupera pagina desde web.xml
		String UrlError = getServletConfig().getInitParameter("UrlError");
		long id_jornada  = Long.parseLong(req.getParameter("id_jornada"));
        long id_pedido  = Long.parseLong(req.getParameter("id_pedido"));
        // Se   le pasa los valores de usuario al logPedido y el id del pedido
    	log.setUsuario(usr.getLogin());
		log.setId_pedido(id_pedido);
        //Valor que se pasa desde el html para saber de que boton viene para realizar los cambios
        String valor = (req.getParameter("btn"));
       
               
        //Recibo los valores de cant faltante y sin pickear para cuando repickeemos se deja en 0 faltante y se suma el total a sinpickear
    
        logger.debug("-----Inicio Logger---------");
        logger.debug("Id del pedido" + id_pedido);
        logger.debug("Valor del Boton que llega " + valor);
        logger.debug("Id de la Jornada que viene del pedido  "+id_jornada);
        logger.debug("----- Fin  Logger---------");         
        
        
        /**
         * Si el boton presionadao es en Bodega
         *  se agrega el log  correspondiente
         * 
         */
                
        if ( valor.equals("B")) {
        	try {
        		
        		biz.setModEstadoPedido(id_pedido,Constantes.ID_ESTAD_PEDIDO_EN_BODEGA);
        		log.setLog("Se ha inciado proceso en Bodega");
        		biz.addLogPedido(log);
        	}catch (Exception e3) {
        		paramUrl = UrlError;
        		
        		}   
			
        	/**
             * Si el boton presionadao es Repickear
             *  se agrega el log  correspondiente
             * 
             */}
        else 
        	if ( valor.equals("R")) {
        		String prodPick = req.getParameter("prodPick");
        		if(null != prodPick && !"".equals(prodPick.trim())){
	        		String productosSeleccionados[]= prodPick.split(",");
        		List listadoProductos = biz.getListadoProductosxPedido(id_pedido);
	    			if(productosSeleccionados != null){
		    			for (int i = 0; i < productosSeleccionados.length; i++) {
    			
		    				int j=0;
		    				while(j<listadoProductos.size() 
		    						&& !((ProductosPedidoDTO)listadoProductos.get(j)).getCod_producto().equals(productosSeleccionados[i])){
		    					j++;
		    				}
		    				if(j<listadoProductos.size()){
    				ProductosPedidoDTO mon = new ProductosPedidoDTO();
			    				mon = (ProductosPedidoDTO)listadoProductos.get(j); 
       				long id_producto = mon.getId_producto();
    				double cant_falt = mon.getCant_faltan();
    				double cant_spick = mon.getCant_spick();
    				double cantidad = cant_falt + cant_spick;
    				
    				try {
                   		biz.setModDetallePedido(id_pedido, cantidad, id_producto);
                		log.setLog("Se ha inciado proceso en Picking");
                		
                      	}
                		catch (Exception e1) {
                		
                		}   
    			}
		    			}
	    			}
    			
        	
           		biz.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_EN_PICKING);
           		biz.addLogPedido(log);
        		log.setLog("Se ha inciado proceso en Picking");
        		}	  
        	  
			
        }
    	// 4. Redirecciona salida
       
        paramUrl ="ViewMonitorJornadas?id_jornada="+id_jornada;
   		logger.debug ("Param URl  "+paramUrl);
   		logger.debug("-----FIN -----  " + getClass());
		res.sendRedirect(paramUrl);
		
    }
    }
    

