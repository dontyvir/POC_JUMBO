package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que entrega los productos para una categoría
 * 
 * @author rbelmar it4b
 * 
 */
public class AjaxCategoriasLeft extends Command {
    
    static final long   ID_LOCAL = 1;
    static final long   ID_ZONA  = 1;

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {
		try {
            arg1.setCharacterEncoding("UTF-8");
            arg1.setContentType("text/xml; charset=iso-8859-15");

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
            
            BizDelegate biz = new BizDelegate();
            
            if ( !"1".equalsIgnoreCase( session.getAttribute("ses_cli_id").toString() )) {
                //Se almacena tracking en este sector
                //Tracking_web.saveTracking("Categorias", arg0);    
            }
            if( session.getAttribute("cod_error") != null ) {
                session.removeAttribute("cod_error");
            }
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();
			
			String pag_form = "";
			if ( arg0.getParameter("cai") == null || arg0.getParameter("tip").compareTo("I") == 0 ){
				// Recupera pagina desde web.xml y se inicia parser
				pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			} else { 
				// Recupera pagina desde web.xml y se inicia parser
				pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form_cat");
			}
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			// Recepción de variables de página
            long idCabecera = 0;
			long idIntermedia = 0;
			long idTerminal = 0;
            if ( arg0.getParameter("cab") != null ){
                idCabecera = Long.parseLong(arg0.getParameter("cab"));
                top.setVariable("{cabecera_id}", arg0.getParameter("cab") + "");
            }
			if ( arg0.getParameter("int") != null ){
				idIntermedia = Long.parseLong(arg0.getParameter("int"));
				top.setVariable("{categoria_id}", arg0.getParameter("int") + "");
			}
			if ( arg0.getParameter("ter") != null  ) {
				idTerminal = Long.parseLong(arg0.getParameter("ter"));
				top.setVariable("{subcategoria_id}", arg0.getParameter("ter") + "");
			}
			
			this.getLogger().debug( "Cabecera:" + idCabecera + " Intermedia:" + idIntermedia + " Terminal:" + idTerminal );
			
			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
            
            /*************************************************/
            /************   C A T E G O R I A S   ************/
            /*************************************************/
			List categorias = new ArrayList();
            
            CategoriaDTO cat1 = null;
            
            List list_categoria = biz.productosGetCategorias( cliente_id.longValue() );
			
			this.getLogger().debug("[AjaxCategoriasLeft - Categoria] : "+list_categoria.size());
			
			for( int i = 0; i < list_categoria.size() ; i++ ) {
				cat1 = (CategoriaDTO) list_categoria.get(i);
				IValueSet nivel1 = new ValueSet();
				
                //Cabeceras
				nivel1.setVariable("{cat_id}",  cat1.getId()+"");
				nivel1.setVariable("{cat_nombre}", cat1.getNombre()+"");
				nivel1.setVariable("{cat_tipo}", cat1.getTipo()+"");
				nivel1.setVariable("{cat_padre}", "0");
                nivel1.setVariable("{numero}", i + "");

				// Buscar Intermedias
				CategoriaDTO cat2 = null;
				List intermedias = cat1.getCategorias();
				List aux_intermedias = new ArrayList();
				
				IValueSet nivel2 = null;
				for (int  j=0; j< intermedias.size(); j++){
					nivel2 = new ValueSet();
					cat2 = (CategoriaDTO)intermedias.get(j);
					if (cat1.getId() == cat2.getId_padre()){
						nivel2.setVariable("{nombre_subcat}", cat2.getNombre() + "");
						nivel2.setVariable("{cat_id}",  cat2.getId()+"");
						nivel2.setVariable("{cat_tipo}", cat2.getTipo()+"");
						nivel2.setVariable("{cat_padre}", cat2.getId_padre()+"");
                        nivel2.setVariable("{cat_cab}", cat1.getId()+"");
                        nivel2.setVariable("{nombre_padre}", cat1.getNombre()+""); // se agrega

						if ( idIntermedia == cat2.getId()){
							nivel2.setVariable("{class_menu}",  "menuDesplegadoOnMouseOver");
						}else{
							nivel2.setVariable("{class_menu}",  "menuDesplegado");
						}
                        
                        //Buscar Terminales
                        CategoriaDTO cat3 = null;
                        List terminales = cat2.getCategorias();
                        List aux_terminales = new ArrayList();
                        
                        IValueSet aux_fila2 = null;
                        for (int  k=0; k< terminales.size(); k++){
                            aux_fila2 = new ValueSet();
                            cat3 = (CategoriaDTO)terminales.get(k);
                            if (cat2.getId() == cat3.getId_padre()){
                                aux_fila2.setVariable("{nombre_subcat}", cat3.getNombre() + "");
                                aux_fila2.setVariable("{cat_id}",  cat3.getId()+"");
                                aux_fila2.setVariable("{cat_tipo}", cat3.getTipo()+"");
                                aux_fila2.setVariable("{cat_padre}", cat3.getId_padre()+"");
                                aux_fila2.setVariable("{cat_cab}", cat1.getId()+"");
                                aux_fila2.setVariable("{banner_subcat}", cat3.getBanner()+"");
                                aux_fila2.setVariable("{nombre_padre}", cat2.getNombre()+""); // se agrega
                                aux_fila2.setVariable("{nombre_abuelo}", cat1.getNombre()+""); // se agrega                                aux_fila2.setVariable("{numero}", i + "");
								aux_fila2.setVariable("{numero}", i + "");
                                aux_terminales.add(aux_fila2);
                            }
                        }
                        nivel2.setDynamicValueSets("TERMINALES", aux_terminales);
						aux_intermedias.add(nivel2);
					}
				}
				this.getLogger().debug("ID categoria:" + cat1.getId() + " Cantidad subcategorias:" + aux_intermedias.size());				
				nivel1.setDynamicValueSets("INTERMEDIAS", aux_intermedias);
				
				if ( idCabecera == cat1.getId()){
					nivel1.setVariable("{menu_cerrado}",  "");
				} else {
					nivel1.setVariable("{menu_cerrado}",  "display:none;");
				}
				if ((i+1) % 2 == 0){
					nivel1.setVariable("{class_menu}",  "menuactivob");
				} else {
					nivel1.setVariable("{class_menu}",  "menuactivo");
				}

				categorias.add(nivel1);
			}
			top.setDynamicValueSets("CATEGORIAS", categorias);
            /*************************************************/
            /************   C A T E G O R I A S   ************/
            /*************************************************/

			String result = tem.toString(top);
			out.print(result);
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}
	}
}