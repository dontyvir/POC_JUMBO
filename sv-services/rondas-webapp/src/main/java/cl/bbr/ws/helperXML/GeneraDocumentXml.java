package cl.bbr.ws.helperXML;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.BarraDetallePedidosRondaDTO;
import cl.bbr.jumbocl.pedidos.dto.FOSustitutosCriteriosDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PromoDetPedRondaDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.ws.bizdelegate.BizDelegate;
import cl.bbr.ws.exceptions.WsException;
/**
 * Genera el documento XML de rondas 
 */
public class GeneraDocumentXml {
	private String documentoxml;
	protected Logging logger = new Logging(this);

	/** Constructor GeneraDocumentXml*/
	public GeneraDocumentXml() {
	}
	
	/**
	 * Valida y formate un documento XML de ronda
	 * @param lst_usuarios
	 * @param lst_productos
	 * @param lst_cbarra
	 * @return Documento XML formateado de una ronda
	 * @throws IOException
	 * @throws JDOMException
	 */
    public String ManejoListAStringXML(List lst_usuarios, Hashtable lst_productos, List lst_cbarra, 
  		String tipo_flujo_local, List lst_promociones, List lst_sust_crit , String tipo_picking) 
        throws IOException, JDOMException, SystemException, WsException{

        BizDelegate biz = new BizDelegate();
    	//Se crea la raiz del Documento Xml.
        Element root = new Element("tablaronda");

        //Crear el Elemento hijo usuarios para el root TablaRonda
        Element usuarios = new Element("usuarios");
   		// iteramos sobre la listas de usuarios
   		for (int i=0; i<lst_usuarios.size(); i++){
   			UserDTO usr = new UserDTO();
   			usr = (UserDTO)lst_usuarios.get(i);
   			logger.debug("->" + usr.getId_usuario() + usr.getPassword() + usr.getLogin());

   			// Armar Doc XML
            Usuario user = new Usuario(usr.getId_usuario()+"",usr.getLogin(),usr.getPassword(), usr.getId_perfil()+"");
            usuarios.addContent(user);	
   		}
	    //agregamos el Elemento usuarios al Elemento raiz tablaronda.
   		root.addContent(usuarios); 
           //Fin Crear Elemento Usuario
             
                
        //Creamos el Elemento hijo CBarras para el root TablaRonda     
        Element cbarras = new Element("cbarras");                 
   		// iteramos sobre la listas de detalles de pedidos.   		
   		for (int i=0; i<lst_cbarra.size(); i++){
   			BarraDetallePedidosRondaDTO barra = new BarraDetallePedidosRondaDTO();
   			barra = (BarraDetallePedidosRondaDTO)lst_cbarra.get(i);
   			logger.debug("->" + barra.getCod_barra() + barra.getTip_codbar() + barra.getId_detalle());
           
                CBarra cbar = new CBarra(barra.getCod_barra(), barra.getTip_codbar(), barra.getId_detalle()+"");                    
                cbarras.addContent(cbar);
        }
   		//agregamos el Elemento cbarras al Elemento raiz tablaronda.
        root.addContent(cbarras);
        //Fin Crear Elemento CBarras
        
        
        long id_sector = 0L;
        
        //Creamos el Elemento hijo detalles_pedido para el root TablaRonda     
        Element detalles_pedido = new Element("detalles_pedido");
        //iteramos sobre la listas de usuarios
        Iterator it = lst_productos.keySet().iterator();
	    while (it.hasNext()) {
	        // Get key
	        String key = it.next().toString();
			ProductosPedidoDTO prod = new ProductosPedidoDTO();
			prod = (ProductosPedidoDTO)lst_productos.get(key);
			//---------- mod_ene09 - ini------------------------
			logger.debug("->id_prod: " + prod.getId_producto() 
                        +" - sector: " + prod.getSector()
			  		    +" - descr : " + prod.getDescripcion() 
	  				    +" - pesab : " + prod.getPesable() 
					    +" - orden : " + prod.getOrden()
					    +" - sust  : " + prod.getPol_sustitucion());
		
			//PONER BAYPASS DE SECTOR
			//************************
			//YA NO ES NECESARIO DEBIDO A LA REESTRUCTURACIÓN DE LA TABLA DE SECTORES
			//***********************************************************************
		    /*if (tipo_picking.equals("N")){
		        if (id_sector==0){
		            id_sector = biz.getIdSectorByNombre(prod.getSector());
		        }
		    }else if (tipo_picking.equals("L")){
		        id_sector = biz.getIdSectorByNombre(prod.getSector());
		    }*/
			//************************
			
            DetallePedido detPed = new DetallePedido(
              prod.getId_detalle()+"", prod.getId_pedido()+"", prod.getCod_producto(), prod.getUnid_medida(),
              prod.getDescripcion(), prod.getCant_solic()+"", "0", "0", "0", prod.getPrecio()+"", 
              prod.getObservacion(), prod.getPesable(), prod.getId_sector()+"",prod.getOrden(), prod.getSector(), "0", prod.getId_dronda()+"",
              prod.getPol_sustitucion()+"", prod.getIdCriterio()+"", prod.getDescCriterio());
              //---------- mod_ene09 - fin------------------------
            detalles_pedido.addContent(detPed);
        }
		//agregamos el Elemento detalles_pedido al Elemento raiz tablaronda.
        root.addContent(detalles_pedido);
    
        //se agrega el elemento TIPO_FLUJO, C 
        Element globales = new Element("globales");
        Element global = new Element("global");
        Element tipo_flujo = new Element("tipo_flujo");

        tipo_flujo.setText(tipo_flujo_local);//normal = con bodega , parcial = sin bodega
        
        //---------- mod_ene09 - ini------------------------
        Element tipo_pick = new Element("tipo_pick");
        tipo_pick.setText(tipo_picking);
        global.addContent(tipo_pick);
        //---------- mod_ene09 - fin------------------------
            
        global.addContent(tipo_flujo);
        globales.addContent(global);
        root.addContent(globales);
        
        //se agrega la tabla de sustitutos criterios
        Element scriterios = new Element("criterios");                 
       	// iteramos sobre la listas de detalles de pedidos.   		
       	for (int i=0; i<lst_sust_crit.size(); i++){
       		FOSustitutosCriteriosDTO criterio = (FOSustitutosCriteriosDTO)lst_sust_crit.get(i);
       		logger.debug("->" + criterio.getId_criterio()+", "+criterio.getNombre_criterio());
                             
            SustitutosCriterio scrit = new SustitutosCriterio(criterio.getId_criterio()+"", criterio.getNombre_criterio());
            scriterios.addContent(scrit);
        }
       	//agregamos el Elemento cbarras al Elemento raiz tablaronda.
        root.addContent(scriterios);
            
        
        //Creamos el Elemento hijo promociones para el root TablaRonda     
        Element promociones = new Element("promociones");                 
       	// iteramos sobre la listas de promociones.   		
       	for (int i=0; i<lst_promociones.size(); i++){
       		PromoDetPedRondaDTO promo = new PromoDetPedRondaDTO();
       		promo = (PromoDetPedRondaDTO)lst_promociones.get(i);
       		logger.debug("->" + promo.getId_detalle()+" - " + promo.getId_promocion()+" - " + promo.getSustituible()+" - "+promo.getFaltante());               
            Promocion pro = new Promocion(promo.getId_detalle()+"", promo.getId_promocion()+"", promo.getSustituible(),promo.getFaltante());                   
            promociones.addContent(pro);
        }
       	//agregamos el Elemento promociones al Elemento raiz tablaronda.
        root.addContent(promociones);
        //Fin Crear Elemento promociones
        logger.debug("1");
        
        //Creamos el documento xml
        Document docu = new Document(root);

        documentoxml = getNiceXML8(docu);
        //System.out.println("\n"+documentoxml);
 
       	return documentoxml;
    }
  
  
     /**
      * Se convierte el String recibido desde la PDA y se convierte a Document
      * @param tablaRecibida
      * @return Document
      * @throws IOException
      * @throws JDOMException
      */
	 public Document ManejoStringAXml(String tablaRecibida) 
	  throws IOException, JDOMException{
		    //String xmlStringFinal = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
		    //xmlStringFinal = xmlStringFinal + tablaRecibida;
		    //Reader stringReader = new StringReader(xmlStringFinal);
		    Reader stringReader = new StringReader(tablaRecibida);
		    SAXBuilder saxBuilder=new SAXBuilder(false);				    
			org.jdom.Document jdomDocument = saxBuilder.build(stringReader);
			String tabla = getNiceXML8(jdomDocument); 
			System.out.println(tabla);
			return jdomDocument;
	}
  
	 
   /**
    * Metodo que retorna un String con todo el formato del documento XML ingresado como parametro
    * @param doc
    * @return header del XML Formateado como String
    */ 
	public static String getNiceXML8(Document doc) {
		XMLOutputter dout = new XMLOutputter();
		dout.setEncoding("ISO-8859-1");
		dout.setNewlines(false);
		return (dout.outputString(doc));
	}
}
