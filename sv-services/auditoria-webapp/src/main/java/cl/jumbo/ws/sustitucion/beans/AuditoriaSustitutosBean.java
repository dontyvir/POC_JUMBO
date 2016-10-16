package cl.jumbo.ws.sustitucion.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.JDOMException;

import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.ws.sustitucion.command.Controlador;
import cl.jumbo.ws.sustitucion.exceptions.WsException;
import cl.jumbo.ws.sustitucion.helperXML.GeneraDocumentXml;


public class AuditoriaSustitutosBean{	
	
    protected Logging  logger = new Logging(this);
	private   Document DocListaBarrasSustitucionXML;
    private   Document DocTablaAuditoria_SustitucionXML;
	private   String   retorno = "";    	
	
	/**
	 * Constructor
	 */
	public AuditoriaSustitutosBean(){
    
    }
	
    
    /** 
     * @param ListaBarrasSustitucionXML
     * @param id_local
     * @return XML con Detalle de Cod. Barra Sustitutos | codigo de error
     *  
     */
    public String getDetalleProdSustitutos(String ListaBarrasSustitucionXML, String id_ronda ) {
        
        List lstBarSust = new ArrayList();
        
           //Construccion de la clase GeneraDocumentXml.
        GeneraDocumentXml entrada = new GeneraDocumentXml();

        // Creamos instancia del controlador para efectuar la Descarga
        Controlador ctrlDescarga = new Controlador();

        logger.debug("Inicio Descarga Ronda...");
        
        logger.debug("ListaBarrasSustitucionXML: " + ListaBarrasSustitucionXML);
       
        
        //Llamada al metodo ManejoStringAXml el cual transforma todos los String a Document.
        try {
            DocListaBarrasSustitucionXML = entrada.ManejoStringAXml(ListaBarrasSustitucionXML);
        } catch (IOException e) {
            return "SysError";
        } catch (JDOMException e) {
            return "SysError";
        }
        
        //DTOBarraSustitutos dto = new DTOBarraSustitutos();
  
        //dto.setBarraSustitutosXML(DocListaBarrasSustitucionXML);
        
        try {
            lstBarSust = ctrlDescarga.getDetalleProdSustitutos(DocListaBarrasSustitucionXML, id_ronda);
        } catch (SystemException e) {
            logger.error("SystemException!!!");
            return "WSE006";
        } catch (WsException e) {
            logger.info(e.getMessage());
            return e.getMessage();
        }
        
        
        //Construccion de la clase GeneraDocumentXml.
        GeneraDocumentXml salida = new GeneraDocumentXml();
     
        //Llamada al metodo ManejoXML el cual transforma todos los Document´s en un solo Document retornado en String
        try {
            retorno = salida.ManejoListAStringXML(lstBarSust);
        } catch (IOException e) {
            logger.error("IOException al formar XML");
            return "WSE006";
        } catch (JDOMException e) {
            logger.error("JDOMException al formar XML");
            return "WSE006";
        } catch (Exception e){
            logger.error("Error al formar XML");
            return "WSE005";
        }
        
        logger.debug(retorno);

        return retorno;
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
    public String descargaAuditoriaSustitucion(String TablaAuditoria_SustitucionXML) {
           //Construccion de la clase GeneraDocumentXml.
        GeneraDocumentXml entrada = new GeneraDocumentXml();

        // Creamos instancia del controlador para efectuar la Descarga
        Controlador ctrlDescarga = new Controlador();

        logger.debug("Inicio Descarga Ronda...");
        
        logger.debug("TablaAuditoria_SustitucionXML:"+TablaAuditoria_SustitucionXML);
     
        //Llamada al metodo ManejoStringAXml el cual transforma todos los String a Document.
        try {
            DocTablaAuditoria_SustitucionXML = entrada.ManejoStringAXml(TablaAuditoria_SustitucionXML);
            
        } catch (IOException e) {
            return "SysError";
        } catch (JDOMException e) {
            return "SysError";
        }
        
        try {
            ctrlDescarga.doDescargaAuditoriaSustitucion(DocTablaAuditoria_SustitucionXML);
        } catch (SystemException e) {   
            logger.error("SystemException!!!");
            return "WSE006";
        } catch (WsException e) {
            logger.info(e.getMessage());
            return e.getMessage();
        }
        
        return "WSE000";
 
    }

}
		   


