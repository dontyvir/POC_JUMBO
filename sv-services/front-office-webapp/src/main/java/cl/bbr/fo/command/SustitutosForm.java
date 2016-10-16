package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.exception.SystemException;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.jumbocl.productos.dto.ProductosSustitutosCategoriasDTO;

/**
 * Página de sustitutos de los clientes
 * 
 * @author imoyano
 * 
 */
public class SustitutosForm extends Command {
	
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
            int nroProdPorCatSinCabecera = 3;
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Sustitutos", arg0);

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
            
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			Long idCliente = new Long(session.getAttribute("ses_cli_id").toString());
			
			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
            
			// Seteamos una variable para desplegar el mensaje de exito
            String save = "NO";
            if ( arg0.getParameter("save") != null ) {
                save = arg0.getParameter("save").toString();
            }
            if ( save.equalsIgnoreCase("SI") ) {
                top.setVariable("{save}", "SI");
            } else {
                top.setVariable("{save}", "NO");
            }
            
            List sustitutosPorCategorias = new ArrayList();
            
            // Seteamos la variable del retorno, si llegamos desde el resumen debemos mostrar un TERMINAR
            String volver = "javascript:history.back();";
            if ( arg0.getParameter("from") != null ) {
                volver = arg0.getParameter("from").toString();
            }
            top.setVariable("{from}", volver);
            List mostrarBoton = new ArrayList(); 
            IValueSet fila = new ValueSet();
            fila.setVariable("{from}", volver);
            mostrarBoton.add(fila);
            
            if ( volver.equalsIgnoreCase("RESUMEN") ) {
                // Guardamos la lista si venimos desde el resumen
                if ( !save.equalsIgnoreCase("SI") ) {
                    guardamosNombreLista(idCliente, arg0);
                }
                top.setDynamicValueSets("BTN_TERMINAR", mostrarBoton);
                sustitutosPorCategorias = biz.productosSustitutosPorCategoria(idCliente.longValue(), true);
            } else {
                top.setDynamicValueSets("BTN_VOLVER", mostrarBoton);
                sustitutosPorCategorias = biz.productosSustitutosPorCategoria(idCliente.longValue(), false);
            }
            
            List fmCate = new ArrayList();
            String sepa = "";
            String cats = "";
            String mostrarAcordeon = "";
            String sepMostrar = "";
            for ( int i = 0; i < sustitutosPorCategorias.size(); i++ ) {
            	ProductosSustitutosCategoriasDTO cat = (ProductosSustitutosCategoriasDTO) sustitutosPorCategorias.get(i);
                List fmProd = new ArrayList();
                IValueSet filaCat = new ValueSet();
                filaCat.setVariable("{categoria}", cat.getCategoria());
                filaCat.setVariable("{id_categoria}", ""+cat.getId());                
                
                String sep   = "";
                String prods = "";
                boolean abrir = false;
                for ( int j = 0; j < cat.getSustitutos().size(); j++) {
                    ProductoDTO prod = (ProductoDTO) cat.getSustitutos().get(j);
                    IValueSet filaPro = new ValueSet();
                    filaPro.setVariable("{descripcion}", prod.getDescripcion()+ "");
                    filaPro.setVariable("{marca}", prod.getMarca()+ "");
                    filaPro.setVariable("{id_producto}", ""+prod.getPro_id());
                    filaPro.setVariable("{id_categoria}", ""+cat.getId());
                    
                    for ( int h = 1; h <= 5; h++ ) {
                        if ( h == prod.getCriterio().getIdCriterio() ) {
                            filaPro.setVariable("{checked" + h + "}", "checked=\"checked\"");
                        } else {
                            filaPro.setVariable("{checked" + h + "}", "");
                        }                       
                    }
                    if ( prod.getCriterio().getAsignoCliente().equalsIgnoreCase("S")) {
                        filaPro.setVariable("{color_fondo_prod}", "#ffffff");
                        filaPro.setVariable("{img_globo_cri}", "");
                        
                    } else {
                        abrir = true;
                        filaPro.setVariable("{color_fondo_prod}", "#fbfad0");
                        filaPro.setVariable("{img_globo_cri}", "<img src=\"/FO_IMGS/img/estructura/sustitutos/globo_paso4.gif\" name=\"globo_sustituto\" width=\"233\" height=\"34\" border=\"0\" id=\"globo_sustituto\" alt=\"\" />");
                    }
                    if ( prod.getCriterio().getIdCriterio() == 4 ) {
                        if ( prod.getCriterio().getSustitutoCliente().length() > 0 ) {
                            filaPro.setVariable("{sustituto_cliente}", prod.getCriterio().getSustitutoCliente());
                        } else {
                            filaPro.setVariable("{sustituto_cliente}", "Ej: marca, sabor");
                        }
                    } else {
                        filaPro.setVariable("{sustituto_cliente}", "Ej: marca, sabor");
                    }
                    
                    
                    fmProd.add(filaPro);
                    prods += sep + prod.getPro_id();
                    sep = ",";
                }
                filaCat.setVariable( "{productos}", prods ); 
                filaCat.setDynamicValueSets("PRODUCTOS", fmProd);
                
                if ( fmProd.size() > nroProdPorCatSinCabecera ) {
                    List mostrarCabecera = new ArrayList(); 
                    IValueSet filaCab = new ValueSet();
                    filaCab.setVariable("{id_categoria}", ""+cat.getId()); 
                    mostrarCabecera.add(filaCab);
                    filaCat.setDynamicValueSets("MOSTRAR_CABECERA", mostrarCabecera);
                }
                
                fmCate.add(filaCat);
                
                cats += sepa + cat.getId();
                sepa = ",";
                
                if ( abrir ) {
                    mostrarAcordeon += sepMostrar + cat.getId(); //Ingresamos todos los correlativos para abrir el acordeon
                    sepMostrar = ",";
                }
            }
            top.setVariable("{categorias}", cats);
            top.setVariable("{abrir_acordeon}", mostrarAcordeon);            
            top.setDynamicValueSets("CATEGORIAS", fmCate);
            
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException(e);
		}
	}

    /**
     * @param idCliente
     * @param arg0
     * @throws SystemException
     */
    private void guardamosNombreLista(Long idCliente, HttpServletRequest arg0) {
        BizDelegate biz = new BizDelegate();
        try {
            long opcionradio = Long.parseLong(arg0.getParameter("opcionradio"));
            String txtNewNombre =  arg0.getParameter("txtNewNombre");            
            if ( opcionradio == 1 && txtNewNombre != "" ) {
                biz.updateNombreCompraHistorica(txtNewNombre, idCliente.longValue() );                
            }
        } catch (SystemException e) {
            this.getLogger().debug( e.getMessage() );
        }
    }
}