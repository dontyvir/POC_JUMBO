package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;
import cl.bbr.jumbocl.productos.dto.ProductoDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Comando que despliega los productos que cumplen con la búsqueda realizada para la marca seleccionada.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SearchItemList extends Command {

	/**
	 * Comando que despliega los productos que cumplen con la búsqueda realizada para la marca seleccionada.
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
			
			// Recupera pagina desde web.xml
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			
			// Carga el template html
			TemplateLoader load = new TemplateLoader(pag_form);
			
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();		

			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias").toString());

			// Búsquedas
			
			if (session.getAttribute("ses_patron") != null && session.getAttribute("ses_patron") != ""){
				
				List datos_patron = new ArrayList();
				List list_ses_patron = (List)(session.getAttribute("ses_patron"));
				
				for( int i = 0; i < list_ses_patron.size() ; i++ ) {
					 IValueSet fila_pat = new ValueSet();
					 String aux = (String)list_ses_patron.get(i);
					 fila_pat.setVariable("{patron}", aux.trim()+"");
					 if ( aux.trim().compareTo(arg0.getParameter("patron").toString().trim()) == 0){
						 fila_pat.setVariable("{class_menu_patron}",  "menuDesplegadoOnMouseOver");
					 }
					 else {
						 fila_pat.setVariable("{class_menu_patron}",  "menuDesplegado");
					 }
					 datos_patron.add(fila_pat);
				}
								
				List datos_p = new ArrayList(); 
				IValueSet fila = new ValueSet();
				fila.setDynamicValueSets("SESSION_PATRON",datos_patron);
				datos_p.add(fila);
				top.setDynamicValueSets("CON_PATRON", datos_p); 				

			}	
			
			long idcategoria = 0;
			if ( arg0.getParameter("cai") != null  ) {
				idcategoria = Long.parseLong(arg0.getParameter("cai"));
				top.setVariable("{categoria_id}", idcategoria + "");
			}
			top.setVariable("{marca_id}", arg0.getParameter("idmar") + "");
			top.setVariable("{patron}", arg0.getParameter("patron") + "");
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			long cliente_id = Long.parseLong(session.getAttribute("ses_cli_id").toString());
			
			List arr_categoria = new ArrayList();			
			List list_categoria = biz.productosGetCategorias( cliente_id );
			
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

			// Recuperar datos de la categoría
			if ( arg0.getParameter("cai") != null  ) {
				CategoriaDTO cat = biz.getCategoria( Long.parseLong(arg0.getParameter("cai")) );
				CategoriaDTO cat_padre = null;
				if( cat.getId_padre() != 0 ) {
					cat_padre = biz.getCategoria( cat.getId_padre() );
				}
				if( cat_padre != null ) { 
					top.setVariable("{cat_nombre}", cat_padre.getNombre() + "/" + cat.getNombre()+"");
				}
				else {
					top.setVariable("{cat_nombre}", cat.getNombre()+"");
				}				
			}
			else {
				top.setVariable("{cat_nombre}", "Búsqueda Avanzada");
			}
			
			// Marcas para las categoria
			List lista_marcas = new ArrayList();
			List list_patrones = new ArrayList();			
			if( arg0.getParameter("patron")!=null  ) {
				String[] patrones = arg0.getParameter("patron").split("[\\s]+");
				for (int j=0; j < (patrones.length ); j++){
					list_patrones.add(patrones[j].trim().toUpperCase());
				}
			}		
		
			// Recuperar los cupones y los tcp de la sesión
			List l_torec = new ArrayList();
			List l_tcp = null;
			if( session.getAttribute("ses_promo_tcp") != null ) {
				l_tcp = (List)session.getAttribute("ses_promo_tcp");
				l_torec.addAll(l_tcp);
			}
			if( session.getAttribute("ses_cupones") != null ) {
				List l_cupones = (List)session.getAttribute("ses_cupones");
				l_torec.addAll(l_cupones);
			}
			
			List list_prod = biz.getSearchMarca( session.getAttribute("ses_loc_id").toString(), Long.parseLong(arg0.getParameter("idmar")), cliente_id, "", list_patrones, l_torec );

			SortedSet marca_old = new TreeSet();
			for( int k = 0; k < list_prod.size(); k++ ) {
				ProductoDTO data = (ProductoDTO) list_prod.get(k);
				marca_old.add(data.getMarca()+"--"+data.getMarca_id());
			}
		    Iterator it = marca_old.iterator();
		    while (it.hasNext()) {
		        String aux1 = (String)it.next();
				String []aux = aux1.split("--");
				IValueSet valueset_marcas = new ValueSet();	
				int largo_pro = Integer.parseInt(rb.getString("search.largo.marca"));
				if( aux[0].length() < largo_pro )
					largo_pro = aux[0].length();						
				valueset_marcas.setVariable("{nombre}", aux[0].substring(0,largo_pro)+"");
				valueset_marcas.setVariable("{valor}", aux[1]+"");
				lista_marcas.add(valueset_marcas);		
			}
			top.setDynamicValueSets("FIL_MARCAS",lista_marcas);				
			
			String result = tem.toString(top);

			out.print(result);			
			
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

}