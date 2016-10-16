package cl.jumbo.ws.sustitucion.helperXML;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import cl.bbr.jumbocl.pedidos.dto.BarraAuditoriaSustitucionDTO;
import cl.bbr.jumbocl.shared.log.Logging;
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
	 * Valida y formate un documento XML Codigos de Barra Sustitutos
	 * @param lst_BarSust
	 * @return Documento XML formateado
	 * @throws IOException
	 * @throws JDOMException
	 */
    public String ManejoListAStringXML(List lst_BarSust) 
        throws IOException, JDOMException{

    	//Se crea la raiz del Documento Xml. 
        Element root = new Element("CBarrasSustitutos");

   
        //Crear el Elemento hijo usuarios para el root TablaRonda
        //Element usuarios = new Element("cbarras");
   		// iteramos sobre la listas de usuarios
   		for (int i=0; i<lst_BarSust.size(); i++){
            BarraAuditoriaSustitucionDTO barSust = new BarraAuditoriaSustitucionDTO();
            barSust = (BarraAuditoriaSustitucionDTO)lst_BarSust.get(i);
   			logger.debug("->" + barSust.getCod_barra() + ", " + barSust.getTip_codbar() + ", " + barSust.getId_producto() + ", " +
                    barSust.getDescripcion() + ", " + barSust.getPrecio() + ", " + barSust.getUnid_med());
   			// Armar Doc XML
            CBarraSustituto CBarSust = new CBarraSustituto(barSust);
            root.addContent(CBarSust);	
   		}
        //Fin Crear Elemento CBarras
                
        //Creamos el documento xml
        Document docu = new Document(root);
        //System.out.println(docu.toString());
            
        documentoxml = getNiceXML8(docu);
        System.out.println("XML: " + documentoxml);
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
