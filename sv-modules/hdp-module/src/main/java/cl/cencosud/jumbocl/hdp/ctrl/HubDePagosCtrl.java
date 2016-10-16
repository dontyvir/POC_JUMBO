package cl.cencosud.jumbocl.hdp.ctrl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import cencosud.cl.FopServiceProxy;
import cl.cencosud.jumbocl.hdp.dto.OutputFop;
import cl.cencosud.jumbocl.hdp.exceptions.HubDePagosException;


public class HubDePagosCtrl {
	
	 /**
     * Permite generar los eventos en un archivo log.
     */
   // Logging logger = new Logging(this); 
		
	public List getFops() throws HubDePagosException {
		
		List fops = new ArrayList();
			
		try {
								
			String endpoint = "http://pagos.qa.cencosud.com:80/payment_hub/services/soap/v2/FopService";
			
			Document document = new Document();								
			Element root = new Element("message");
			
			Element header = new Element("header");
			header.addContent(new Element("action").setText("/FOP/Get"));
			header.addContent(new Element("dateTime").setText("2016.03.03 18:02:SS.SSSSSS"));
			header.addContent(new Element("messageId").setText("fdde-13dr-ewe2-jjde-lvac"));
			
			Element body = new Element("body");
			body.setAttribute(new Attribute("action", "request"));
			body.addContent(new Element("applicationId").setText("4001"));
			body.addContent(new Element("currency").setText("CLP"));
			
			root.addContent(header);
			root.addContent(body);
	        document.setContent(root);
	        
	        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("UTF-8"));
	        String xmlString =outputter.outputString(document);

	        FopServiceProxy wsFop = new FopServiceProxy();
	        wsFop.setEndpoint(endpoint);
	        String xmlcontent = wsFop.get(xmlString);
	        StringReader reader = new StringReader(xmlcontent);
	        
	        if(reader != null){
		        SAXBuilder saxBuilder = new SAXBuilder();
				Document documentResponseXML = saxBuilder.build(reader);
				
				Element rootNode = documentResponseXML.getRootElement();
				if("message".equals(rootNode.getName())){
					List list = rootNode.getChildren("body");			
		
		            Element nodeFops = (Element) (((Element) list.get(0)).getChildren("fops")).get(0);
		            List liFop = nodeFops.getChildren("fop");  
		            for ( int f = 0; f < liFop.size(); f++ ){
		            	Element nodeFop = (Element) liFop.get(f);
		            	OutputFop oFops = new OutputFop();
		            	oFops.setId(nodeFop.getChildTextTrim("id"));
		            	oFops.setName(nodeFop.getChildTextTrim("name"));
		            	oFops.setDescriptionHtml(nodeFop.getChildTextTrim("descriptionHtml"));
		            	oFops.setLogoUrl(nodeFop.getChildTextTrim("logoUrl"));
		            	oFops.setUseDiscountAmount(nodeFop.getChildTextTrim("useDiscountAmount"));
		            	oFops.setIsLoyalty(nodeFop.getChildTextTrim("isLoyalty"));	            	
		            	fops.add(oFops);
		            }
				}
	        }
		
		}catch (Exception e) {
			 throw new HubDePagosException(e);
		}
		
		return fops;		
	}

}
