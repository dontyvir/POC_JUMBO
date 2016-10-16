package cl.bbr.ws.beans;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.ws.bizdelegate.BizDelegate;
import cl.bbr.ws.command.Controlador;
import cl.bbr.ws.dto.DTOPicking;
import cl.bbr.ws.exceptions.WsException;
import cl.bbr.ws.helperXML.GeneraDocumentXml;
/**
 * Contiene los metodos de carga y descarga para las rondas de picking que son llamados
 * desde el WebService.
 *
 */
public class RondaBean{
	
	protected Logging  logger = new Logging(this);
	private   Document DocTablaDetalle_PedidoXML; 
	private   Document DocTablaDetalle_PickingXML;
	private   Document DocTablaBin_OpXML;
	private   Document DoctablaRegistro_PickingXML;
	private   String retornoCarga = "";    	
	
	/**
	 * Constructor
	 */
	public RondaBean(){}
	
	/** 
	 * Metodo ws que extrae ronda de carga hacia la PDA para ser pickeados 
	 * @param NRonda
	 * @param login
	 * @param password
	 * @return XML de la carga de rondas | codigo de error
	 * 
	 */
	public String getCargaRonda( String NRonda, String login, String password) {
	    return getCargaRondaLdap( NRonda, login, password, Constantes.PDA_MODO_AUTENTICACION_BD);
	}
	
    public String getCargaRondaLdap( String NRonda, String login, String password, int tipo_aut) {    	
    	
    	logger.debug("Inicio Carga Ronda:"+NRonda+"...");
		
		// Creamos instancia del controlador para efectuar la Carga
		Controlador ctrlCarga = new Controlador();
		
		long id_ronda;
		
		// cambiamos tipo de dato a NRonda
		try {
			id_ronda = Long.parseLong(NRonda);
		}catch (Exception e){
			e.printStackTrace();
			return "WSE005";
		}
		
		
		if(tipo_aut==Constantes.PDA_MODO_AUTENTICACION_BD){
		    logger.debug("... antes doCargaRonda autenticación vía bd");
		}
		else if (tipo_aut==Constantes.PDA_MODO_AUTENTICACION_LDAP){
		    logger.debug("... antes doCargaRonda autenticación vía ldap");
		}
		else{
		    logger.error("tipo de autorizacion recibida erronea: tipo_aut="+tipo_aut);
		    return "WSE005";
		}
		    
		
		
		if ( id_ronda > 2000000000 )
			return "WSE005";
		
		try {
			ctrlCarga.doCargaRonda(id_ronda,login,password, tipo_aut);
		} catch (SystemException e) {	
			logger.error("SystemException: " + e.getMessage());
			return "ERR001";
		} catch (WsException e) {
			e.printStackTrace();
			if (e.getMessage().equals(Constantes._EX_RON_ESTADO_INADECUADO))
				return "WSE002";
			else
			logger.info(e.getMessage());
			return e.getMessage();
		}
		logger.debug("... despues docargaronda");
		// Obtenemos los usuarios
		logger.debug("Usuarios");
		List usuarios = new ArrayList();
		usuarios = ctrlCarga.getUsuarios();
				
		// Obtenemos Productos Ronda
		logger.debug("Productos Ronda");		
		Hashtable productos = new Hashtable();
		productos = ctrlCarga.getProductos_ronda();
		//	  ---------- mod_ene09 - ini------------------------
		
		//Obtenemos el tipo de Picking Normal / light;
		String tipo_picking = ctrlCarga.getTipo_picking();
		

		logger.debug("Orden de Productos");
		//List orden_prods = new ArrayList();
		//agregar aca la llamada a la funcion de Richard
		//entrada listado de productos
		//salida listado de ordenes de productos
		if (tipo_picking.equals("N")){
			try{
				BizDelegate biz = new BizDelegate();
				Hashtable ordenRonda = biz.setOrdenProductosPDA(id_ronda); 

				Iterator it = ordenRonda.keySet().iterator();
			    while (it.hasNext()) {
			        // Get key
			        String key = it.next().toString();
			        //ordenRonda.get(key);
			        
			        ProductosPedidoDTO prod = new ProductosPedidoDTO();
			        prod = (ProductosPedidoDTO)productos.get(key);
			        prod.setOrden(ordenRonda.get(key).toString());
			    }
	        }catch (WsException e){
			    logger.error("Error al OrdenProductosPDA");
			    e.printStackTrace();
			}catch (SystemException e){
			    logger.error("Error al OrdenProductosPDA");
			    e.printStackTrace();
			}
		}
		

		//for(int i=0;i<productos.size();i++){		
		//	OrdenProdDTO nwo = new OrdenProdDTO();
			//nwo.setCod_prod();
			//nwo.setId_detalle();
/*			ProductosPedidoDTO prod = null;
			prod = (ProductosPedidoDTO)productos.get(i);
			String seccion = prod.getId_catprod().substring(0, 2);
			String rubro   = prod.getId_catprod().substring(2, 4);
*/			
			
			
			
		//	nwo.setOrden(i*10);
		//	orden_prods.add(nwo);
		//}
		 
		//	  ---------- mod_ene09 - fin------------------------
	
		//Obtenemos Sustitutos criterios
		logger.debug("Sustitutos Criterios");
		List sust_criterios = new ArrayList();
		sust_criterios = ctrlCarga.getSust_criterios();
	
		// Obtenemos Codigos de Barra
		logger.debug("Codigos Barra");		
		List codigos = new ArrayList();
		codigos = ctrlCarga.getCodigos_barra();
		
		// Obtenemos Tipo_Flujo Parcial / Normal
		String tipo_flujo = ctrlCarga.getTipo_flujo();
		
		//Obtenemos el tipo de Picking Normal / light;
		//String tipo_picking = ctrlCarga.getTipo_picking();
		
		//Obtenemos las promociones
		logger.debug("Productos con promociones.");
		List promociones = new ArrayList();
		promociones = ctrlCarga.getPromo_producto();
		
        //Construccion de la clase GeneraDocumentXml.
        GeneraDocumentXml salida = new GeneraDocumentXml();
     
        //Llamada al metodo ManejoXML el cual transforma todos los Document´s en un solo Document retornado en String
        try {
			retornoCarga = salida.ManejoListAStringXML(usuarios, productos, codigos, tipo_flujo, promociones, sust_criterios, tipo_picking);
			logger.debug("despues de ManejoListAStringXML");
		} catch (IOException e) {
			logger.error("IOException al formar XML");
			e.printStackTrace();
			return "WSE006";
		} catch (JDOMException e) {
			logger.error("JDOMException al formar XML");
			e.printStackTrace();
			return "WSE006";
		} catch (WsException e){
		    logger.error("WsException al formar XML");
		    e.printStackTrace();
		    return "WSE005";
		} catch (Exception e){
			logger.error("Error al formar XML");
			e.printStackTrace();
			return "WSE005";
		}
		
		logger.debug("XML Carga Ronda:\n================\n" + retornoCarga);
		
		return retornoCarga;
       
    }

    private List OrdenaProductosBySeccionRubro(List productos){
        for(int i=0;i<productos.size();i++){
            ProductosPedidoDTO prod = null;
    		prod = (ProductosPedidoDTO)productos.get(i);
    		String seccion = prod.getId_catprod().substring(0, 2);
    		String rubro   = prod.getId_catprod().substring(2, 4);
        }
        
        return null;
    }
    
	/** 
	 * Metodo ws que extrae ronda ya pickeada hacia el servidor
	 *  
	 * @param TablaDetalle_PedidoXML
	 * @param TablaDetalle_PickingXML
	 * @param TablaBin_OpXML
	 * @param TablaRegistro_PickingXML
	 * @return XML de la descarga de rondas | codigo de error
	 *  
	 */
    public String getDescargaRonda(String TablaDetalle_PedidoXML,
                                   String TablaDetalle_PickingXML,
                                   String TablaBin_OpXML,
                                   String TablaRegistro_PickingXML) {
    	Controlador ctrlDescarga = null;
        try {

    	
    	   //Construccion de la clase GeneraDocumentXml.
        GeneraDocumentXml entrada = new GeneraDocumentXml();

		// Creamos instancia del controlador para efectuar la Descarga
		 ctrlDescarga = new Controlador();
    	logger.debug("Inicio Descarga Ronda...");
    	
    	logger.info("TablaDetalle_PedidoXML:"+TablaDetalle_PedidoXML);
    	logger.info("TablaDetalle_PickingXML:"+TablaDetalle_PickingXML);
    	logger.info("TablaBin_OpXML:"+TablaBin_OpXML);
    	logger.info("TablaRegistro_PickingXML:"+TablaRegistro_PickingXML);
    	
        //Llamada al metodo ManejoStringAXml el cual transforma todos los String a Document.
        	DocTablaDetalle_PedidoXML   = entrada.ManejoStringAXml(TablaDetalle_PedidoXML);
        	DocTablaDetalle_PickingXML  = entrada.ManejoStringAXml(TablaDetalle_PickingXML);
        	DocTablaBin_OpXML           = entrada.ManejoStringAXml(TablaBin_OpXML);
        	DoctablaRegistro_PickingXML = entrada.ManejoStringAXml(TablaRegistro_PickingXML);
		} catch (IOException e) {
			e.printStackTrace();
			return "SysError";
		} catch (JDOMException e) {
			e.printStackTrace();
			return "SysError";
		}
	    
        DTOPicking dtoDescarga = new DTOPicking();
  
        dtoDescarga.setTablaDetallePedidoXML(DocTablaDetalle_PedidoXML);
	    dtoDescarga.setTablaDetalle_PickingXML(DocTablaDetalle_PickingXML);
	    dtoDescarga.setTablaBin_OpXML(DocTablaBin_OpXML);
	    dtoDescarga.setTablaRegistro_PickingXML(DoctablaRegistro_PickingXML);
	    
	    try {
		    ctrlDescarga.doDescargaRonda(dtoDescarga);
		} catch (SystemException e) {
			logger.error("SystemException!!!");
			e.printStackTrace();
			return "WSE006";
		} catch (WsException e) {
			logger.info(e.getMessage());
			return e.getMessage();
		}
		
        return "WSE000";
	}
	
}
		   


