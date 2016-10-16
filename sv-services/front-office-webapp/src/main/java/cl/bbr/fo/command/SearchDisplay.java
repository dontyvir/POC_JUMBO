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
 * Página form búsqueda avanzada
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SearchDisplay extends Command {

	/**
	 * Form búsqueda avanzada
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("Busqueda", arg0);				
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml y se inicia parser busqueda_avanzada.html
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();		

			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias").toString());
			top.setVariable("{del_data}", "1");
			top.setVariable("{valor_data}", rb.getString("searchdisplay.texto.ejemplo") );
			
			// Búsquedas
			
			// Se busca patrón desde la session
			if (session.getAttribute("ses_patron") != null && session.getAttribute("ses_patron") != ""){
				String aux_listado_data = "";
				 
				List datos_patron = new ArrayList();
				List list_ses_patron = (List)(session.getAttribute("ses_patron"));
				
				for( int i = 0; i < list_ses_patron.size() ; i++ ) {
					 IValueSet fila_pat = new ValueSet();
					 String aux = (String)list_ses_patron.get(i);
					 fila_pat.setVariable("{patron}", aux.trim()+"");
					 datos_patron.add(fila_pat);
					 aux_listado_data += list_ses_patron.get(i) + "\n";
				}
								
				List datos_p = new ArrayList(); 
				IValueSet fila = new ValueSet();
				fila.setDynamicValueSets("SESSION_PATRON",datos_patron);
				datos_p.add(fila);
				top.setDynamicValueSets("CON_PATRON", datos_p); 				 
				 
				// Variables auxiliares para página
				top.setVariable("{del_data}", "0");
				top.setVariable("{valor_data}", aux_listado_data );
				
			}
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
			
			List arr_categoria = new ArrayList();			
			List list_categoria = biz.productosGetCategorias( cliente_id.longValue() );
					
			for( int i = 0; i < list_categoria.size() ; i++ ) {
				CategoriaDTO data = (CategoriaDTO) list_categoria.get(i);
				IValueSet fila = new ValueSet();
		
				fila.setVariable("{cat_id}",  data.getId()+"");	
				fila.setVariable("{cat_nombre}", data.getNombre()+"");
				fila.setVariable("{cat_tipo}", data.getTipo()+"");
				fila.setVariable("{cat_padre}", "0");				

				// Buscar subcategorias
				CategoriaDTO cat2 = null;
				List lista = data.getCategorias();
				List aux_lista = new ArrayList();
				for (int  j=0; j< lista.size(); j++){
					IValueSet aux_fila = new ValueSet();
					cat2 = (CategoriaDTO)lista.get(j);
					if (data.getId() == cat2.getId_padre()){
						aux_fila.setVariable("{nombre_subcat}", cat2.getNombre() + "");
						aux_fila.setVariable("{cat_id}",  cat2.getId()+"");	
						aux_fila.setVariable("{cat_tipo}", cat2.getTipo()+"");
						aux_fila.setVariable("{cat_padre}", cat2.getId_padre()+"");
						aux_lista.add(aux_fila);
					}
				}
				fila.setDynamicValueSets("SUBCAT", aux_lista);				
				
				if ((i+1) % 2 == 0){
					fila.setVariable("{class_menu}",  "menuactivob");
				}else{
					fila.setVariable("{class_menu}",  "menuactivo");
				}						
				arr_categoria.add(fila);
			}
			top.setDynamicValueSets("CATEGORIAS", arr_categoria);
			
			////////////////////////////////////////////////////////////////////////////////////////////////////
			String result = tem.toString(top);

			out.print(result);

			
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

}