package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.casos.dto.CasoDTO;
import cl.bbr.jumbocl.casos.dto.CasosDocBolDTO;
import cl.bbr.jumbocl.casos.dto.ProductoCasoDTO;
import cl.bbr.jumbocl.casos.utils.CasosUtil;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Genera documento con informacion de un caso para el BOL
 * 
 * @author imoyano
 *
 */
public class ViewDocumentoCaso extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
	    logger.debug("Comienzo ViewDocumentoCaso Execute");
	    
	    //Variables
	    long idCaso		 	= 0;
	    long idDocBolCaso	= 0;
	    int cantProdEnviar	= 0;
	    int cantProdRetirar	= 0;
        int cantEnvioDinero = 0;
	    int cantDocumentos	= 0;
	    
	    View salida = new View(res);
		String html = getServletConfig().getInitParameter("TplFile");
		html = path_html + html;
		logger.debug("Template: " + html);
		
	    TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		//Sacamos la info de la pagina
		if (req.getParameter("id_caso") != null) {
		    idCaso = Long.parseLong(req.getParameter("id_caso").toString());
		}
		if (req.getParameter("id_doc_bol") != null) {
		    idDocBolCaso = Long.parseLong(req.getParameter("id_doc_bol").toString());
		}
		
		BizDelegate bizDelegate = new BizDelegate();
		// ----- Datos del caso ----------
		CasoDTO caso = bizDelegate.getCasoByIdCaso(idCaso);
		
		// ---- Productos a enviar ----
		List prodEnviar = bizDelegate.getProductosByCasoAndTipo(idCaso,"E");
		ArrayList pEnviar = new ArrayList();
		
		long precioTotalEnviar = 0;
		for (int i = 0; i < prodEnviar.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodEnviar.get(i);
			
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			// En los productos a enviar se muestran los productos alternativos con su precio respectivo, en caso de no tener 
			// producto alternativo, se muestra el producto solicitado (original) con su precio
			if (prod.getPsDescripcion().length() > 0) {
			    fila.setVariable("{producto}",prod.getPsDescripcion());
				fila.setVariable("{cantidad}",prod.getPsCantidad());			
				fila.setVariable("{unidad}",prod.getPsUnidad()); 
			} else {
			    fila.setVariable("{producto}",prod.getPpDescripcion());
				fila.setVariable("{cantidad}",prod.getPpCantidad());			
				fila.setVariable("{unidad}",prod.getPpUnidad());   
			}			
			fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
			precioTotalEnviar += obtienePrecioTotal(prod);
			fila.setVariable("{precio_total}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(obtienePrecioTotal(prod)))));
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());			
			pEnviar.add(fila);
		}
		cantProdEnviar = pEnviar.size();
		
		// ---- Productos a Retirar ----
		List prodRetirar = bizDelegate.getProductosByCasoAndTipo(idCaso,"R");
		ArrayList pRetirar = new ArrayList();
		
		long precioTotalRetirar = 0;
		for (int i = 0; i < prodRetirar.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodRetirar.get(i);
			
			// En los productos a retirar se muestran los productos solicitados con su precio respectivo
			
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
			fila.setVariable("{cantidad}",prod.getPpCantidad());			
			fila.setVariable("{unidad}",prod.getPpUnidad());
			
			fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
			precioTotalRetirar += obtienePrecioTotal(prod);
			fila.setVariable("{precio_total}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(obtienePrecioTotal(prod)))));
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
			
			pRetirar.add(fila);
		}
		cantProdRetirar = pRetirar.size();
        

        // ---- Envio de dinero ----
        List prodEnvioDinero = bizDelegate.getProductosByCasoAndTipo(idCaso,"P");
        ArrayList pEnvioDinero = new ArrayList();
        
        long precioTotalEnvioDinero = 0;
        for (int i = 0; i < prodEnvioDinero.size(); i++) {          
            IValueSet fila = new ValueSet();
            ProductoCasoDTO prod = (ProductoCasoDTO)prodEnvioDinero.get(i);
            
            // En los productos a retirar se muestran los productos solicitados con su precio respectivo
            
            fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
            
            fila.setVariable("{producto}",prod.getPpDescripcion());
            fila.setVariable("{cantidad}",prod.getPpCantidad());            
            fila.setVariable("{unidad}",prod.getPpUnidad());
            
            fila.setVariable("{precio}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(prod.getPrecio()))));
            precioTotalEnvioDinero += obtienePrecioTotal(prod);
            fila.setVariable("{precio_total}", Formatos.formatoPrecio(Double.parseDouble(String.valueOf(obtienePrecioTotal(prod)))));
            
            fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
            fila.setVariable("{responsable}",prod.getResponsable().getNombre());
            
            pEnvioDinero.add(fila);
        }
        cantEnvioDinero = pEnvioDinero.size();
		
		// ---- Productos a Especiales (Documentos) ----
		List prodDocumentos = bizDelegate.getProductosByCasoAndTipo(idCaso,"D");
		ArrayList pDocumentos = new ArrayList();
		
		for (int i = 0; i < prodDocumentos.size(); i++) {			
			IValueSet fila = new ValueSet();
			ProductoCasoDTO prod = (ProductoCasoDTO)prodDocumentos.get(i);
			fila.setVariable("{id_producto}",String.valueOf(prod.getIdProducto()));
			
			fila.setVariable("{producto}",prod.getPpDescripcion());
			fila.setVariable("{comentario_boc}",prod.getComentarioBOC());
			
			fila.setVariable("{comentario_bol}",prod.getComentarioBOL());
			
			fila.setVariable("{quiebre}",prod.getQuiebre().getNombre());
			fila.setVariable("{responsable}",prod.getResponsable().getNombre());
			pDocumentos.add(fila);
		}
		cantDocumentos = pDocumentos.size();
		
		//Info del documento
		CasosDocBolDTO doc = new CasosDocBolDTO();
		
		if (idDocBolCaso == 0) {
			List docCaso = bizDelegate.getDocBolCasoByCaso(idCaso);
			if (docCaso.size() > 0) {			
				doc = (CasosDocBolDTO)docCaso.get(0);			
			}
		} else {
		    doc = bizDelegate.getDocBolCasoById(idDocBolCaso);
		}
		
		top.setVariable("{comentario}", CasosUtil.frmTexto(doc.getComentario()).replaceAll("<br>","\r\n"));
		top.setVariable("{cooler1}", CasosUtil.frmTexto(doc.getCooler1()));
		top.setVariable("{cooler2}", CasosUtil.frmTexto(doc.getCooler2()));
		top.setVariable("{cooler3}", CasosUtil.frmTexto(doc.getCooler3()));
		top.setVariable("{cooler4}", CasosUtil.frmTexto(doc.getCooler4()));
		top.setVariable("{cooler5}", CasosUtil.frmTexto(doc.getCooler5()));
		top.setVariable("{cooler6}", CasosUtil.frmTexto(doc.getCooler6()));
		
		top.setVariable("{bin1}", CasosUtil.frmTexto(doc.getBin1()));
		top.setVariable("{bin2}", CasosUtil.frmTexto(doc.getBin2()));
		top.setVariable("{bin3}", CasosUtil.frmTexto(doc.getBin3()));
		top.setVariable("{bin4}", CasosUtil.frmTexto(doc.getBin4()));
		top.setVariable("{bin5}", CasosUtil.frmTexto(doc.getBin5()));
		top.setVariable("{bin6}", CasosUtil.frmTexto(doc.getBin6()));		
		
		//Informacion para la pagina
	    top.setVariable("{cliente}",	caso.getCliNombre());
	    top.setVariable("{rut}",		String.valueOf(caso.getCliRut()));
	    top.setVariable("{dv}", 		caso.getCliDv());
	    top.setVariable("{direccion}", 	caso.getDireccion());
	    top.setVariable("{comuna}", 	caso.getComuna());
	    top.setVariable("{telefono}", 	caso.getCliFonoCod1() + " " + caso.getCliFonoNum1());
	    top.setVariable("{nro_op}", 	String.valueOf(caso.getIdPedido()));
	    top.setVariable("{nro_caso}",	String.valueOf(caso.getIdCaso()));
	    top.setVariable("{fecha}", 		Formatos.frmFecha(caso.getFcCreacionCaso()) );
	    top.setVariable("{indicaciones}", 	caso.getIndicaciones());
	    
	    top.setVariable("{total_retirar}",	Formatos.formatoPrecio(Double.parseDouble(String.valueOf(precioTotalRetirar))));
	    top.setVariable("{total_enviar}", 	Formatos.formatoPrecio(Double.parseDouble(String.valueOf(precioTotalEnviar))));
        top.setVariable("{total_envio_dinero}",   Formatos.formatoPrecio(Double.parseDouble(String.valueOf(precioTotalEnvioDinero))));
	    
	    long totalResumen 	= precioTotalEnviar + precioTotalEnvioDinero - precioTotalRetirar;
	    String mensaje 		= "";
	    
	    if (totalResumen > 0) {
	        mensaje = "Cantidad a Cobrar";
	    } else if (totalResumen < 0) {
	        mensaje = "Cantidad a Devolver";
	        totalResumen = totalResumen * -1;
	    }
	    top.setVariable("{mensaje_resumen}", mensaje);
	    top.setVariable("{total_resumen}", 	 Formatos.formatoPrecio(Double.parseDouble(String.valueOf(totalResumen))));
	    
		top.setDynamicValueSets("PRDS_ENVIAR",	pEnviar);
		top.setDynamicValueSets("PRDS_RETIRAR", pRetirar);
		top.setDynamicValueSets("PRDS_DOCS", 	pDocumentos);
        top.setDynamicValueSets("ENVIO_DINERO",    pEnvioDinero);
		
		top.setVariable("{cantProdEnviar}",	String.valueOf( cantProdEnviar) );
		top.setVariable("{cantProdRetirar}", 	String.valueOf( cantProdRetirar) );
		top.setVariable("{cantDocumentos}", 	String.valueOf( cantDocumentos) );
        top.setVariable("{cantEnvioDinero}",     String.valueOf( cantEnvioDinero) );
		
		logger.debug("Fin ViewDocumentoCaso Execute");
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
	}

    /**
     * @param prod
     * @return
     */
    private long obtienePrecioTotal(ProductoCasoDTO prod) {
        double cantidad = 0;
        if (prod.getTipoAccion().equalsIgnoreCase("R") || prod.getTipoAccion().equalsIgnoreCase("P")) {
            if (prod.getPpCantidad().length() > 0) {
                cantidad = Double.parseDouble(prod.getPpCantidad().replaceAll(",","."));
            }
            
        } else if (prod.getTipoAccion().equalsIgnoreCase("E")) {
            if (prod.getPsDescripcion().length() > 0) {
                if (prod.getPsCantidad().length() > 0) {
                    cantidad = Double.parseDouble(prod.getPsCantidad().replaceAll(",","."));
                }
    		} else {
    		    if (prod.getPpCantidad().length() > 0) {
                    cantidad = Double.parseDouble(prod.getPpCantidad().replaceAll(",","."));
                } 
    		}
        }
        return Math.round( cantidad * Double.parseDouble(String.valueOf(prod.getPrecio())) );
    }
}
