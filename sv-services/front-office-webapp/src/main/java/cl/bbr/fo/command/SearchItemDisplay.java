package cl.bbr.fo.command;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.productos.dto.CatMarcaComparator;
import cl.bbr.jumbocl.productos.dto.CategoriaDTO;

/**
 * Resultado búsqueda simple
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class SearchItemDisplay extends Command {

	/**
	 * Despliega resultado búsqueda simple
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
			
			// Recupera pagina desde web.xml y se inicia parser resultado_busqueda.html
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			
			IValueSet top = new ValueSet();		

			top.setVariable("{nombre_cliente}", session.getAttribute("ses_cli_nombre").toString());
			top.setVariable("{lug_desp}", session.getAttribute("ses_dir_alias").toString());

			String patron_original = Formatos.stringToSearchFO(arg0.getParameter("patron").toString());
			
			// Se obtiene el arreglo de patrones
			List list_ses_patron = new ArrayList();
			String  patronaux = "";
			String  patrontmp = "";
			int largo_patron = Integer.parseInt(rb.getString("searchitemdisplay.largopatron")); 
			if( arg0.getParameter("lu") == null && arg0.getParameter("patron") != null ) {
				String[] patrones = arg0.getParameter("patron").split("[\\n]+");
				for (int k=0; k < (patrones.length ); k++){
					if( patrones[k].trim().compareTo("") != 0  ){
						patronaux = "";
						String[] patronconespacio = patrones[k].toString().split("[\\s]+");
						for (int i=0; i < (patronconespacio.length ); i++){
							if (patronconespacio[i].length() > 1){//Valida que el largo del subpatron sea > 1
								if(patronconespacio[i].trim().toUpperCase().length() > largo_patron ){
									patronaux = patronaux +" "+Formatos.stringToSearchFO(patronconespacio[i].trim().substring(0,largo_patron));
								}else{
									patronaux = patronaux +" "+Formatos.stringToSearchFO(patronconespacio[i].trim());
								}
							}
						}
						if(patronaux.trim().length()>1)
							list_ses_patron.add(Formatos.stringToSearchFO(patronaux.trim()));
						patrontmp = patrontmp +"   "+patronaux.trim();
					}
				}
				session.setAttribute("ses_patron", list_ses_patron );
				 
			}
			else {
				list_ses_patron = (List)(session.getAttribute("ses_patron"));
			}
			
			// Búsquedas
			if( list_ses_patron != null && list_ses_patron.size() > 0 ) {
				
				List datos_patron = new ArrayList();
				
				for( int i = 0; i < list_ses_patron.size() ; i++ ) {
					 IValueSet fila_pat = new ValueSet();
					 String aux = (String)list_ses_patron.get(i);
					 fila_pat.setVariable("{patron}", aux.trim()+"");
					 
					 if(patrontmp != null && patrontmp.trim().length()>0){
						 if ( aux.trim().compareTo(list_ses_patron.get(0).toString().trim()) == 0){
							 fila_pat.setVariable("{class_menu_patron}",  "menuDesplegadoOnMouseOver");
						 }
						 else {
							 fila_pat.setVariable("{class_menu_patron}",  "menuDesplegado");
						 }
					 }else{
						 if ( aux.trim().compareTo(arg0.getParameter("patron").toString().trim()) == 0){
							 fila_pat.setVariable("{class_menu_patron}",  "menuDesplegadoOnMouseOver");
						 }
						 else {
							 fila_pat.setVariable("{class_menu_patron}",  "menuDesplegado");
						 }
					 }
					 
					 datos_patron.add(fila_pat);
				}
								
				List datos_p = new ArrayList(); 
				IValueSet fila = new ValueSet();
				fila.setDynamicValueSets("SESSION_PATRON",datos_patron);
				datos_p.add(fila);
				top.setDynamicValueSets("CON_PATRON", datos_p); 				
				
			}
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
			
			// Menú de categorías
			
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
			
			List list_patrones = new ArrayList();
			
			String[] patronesaux = arg0.getParameter("patron").toString().split("\\n");
			//String[] patrones = Formatos.stringToSearch(arg0.getParameter("patron").toString()).split("[\\s]+");
			String[] patrones = Formatos.stringToSearchFO(patronesaux[0].toString()).split("[\\s]+");
			
			for (int k=0; k < (patrones.length ); k++){
				if( patrones[k].trim().compareTo("") != 0 && patrones[k].length() > 1){
					//System.out.print("largoBD = "  +patrones[k].length());
					if(patrones[k].trim().toUpperCase().length() > largo_patron){
						list_patrones.add( patrones[k].trim().toUpperCase().substring(0,largo_patron) );
					}else{
						list_patrones.add( patrones[k].trim().toUpperCase() );
					}
				}
			}
			
			List datos_s = new ArrayList();
			List datos_m = new ArrayList();
			
			List listaSearch = new ArrayList();
			if( list_patrones.size() > 0 )
				listaSearch = biz.getSearch( list_patrones, Long.parseLong(session.getAttribute("ses_loc_id").toString()) );				
			
			//top.setVariable("{patronaux}",  Formatos.stringToSearch(arg0.getParameter("patron")) );
			
			long total_encuentros = 0;
			List cat_aux = new ArrayList();
			
			if(patrontmp != null && patrontmp.trim().length()>0){
				patron_original = (String) list_ses_patron.get(0);//patrontmp contiene el string sin letras de 1 caracter
				top.setVariable("{patronaux}",  list_ses_patron.get(0) );
			}else{
				patron_original = Formatos.stringToSearchFO(arg0.getParameter("patron").toString());
				top.setVariable("{patronaux}",  Formatos.stringToSearchFO(arg0.getParameter("patron").toString()) );				
			}
			
			for (int i = 0; i < listaSearch.size(); i++) {
				 CategoriaDTO cat = (CategoriaDTO) listaSearch.get(i);
				 
				 total_encuentros += cat.getCant_productos();
				 
				 if ( cat_aux.contains(cat.getNombre()+"-"+cat.getSubcat()) == false ){				 
					 IValueSet fila = new ValueSet();
					 fila.setVariable("{idcat}", cat.getId()+"");
					 fila.setVariable("{nombrecat}", cat.getNombre()+"");
					 fila.setVariable("{tipo}", cat.getTipo()+"");
					 fila.setVariable("{patron}", patron_original+"");
					 
					 if (cat.getSubcat() != null)
						 fila.setVariable("{subcat}", cat.getSubcat()+"\\");
					 else
						 fila.setVariable("{subcat}", "");
					 datos_s.add(fila);
					 
					 cat_aux.add(cat.getNombre()+"-"+cat.getSubcat());
					 
				 }
			}
			top.setDynamicValueSets("lista_categorias",datos_s);
			
			top.setVariable("{total}",  total_encuentros+"" ); // Muestra total de encuentros
			
			if (total_encuentros == 0){
				top.setVariable("{marcado}", "FO/busqueda/sin_resultado/" + patron_original);
			} else {
				top.setVariable("{marcado}", "FO/busqueda/con_resultado/" + patron_original);
			}
			
			List aux_marcas = new ArrayList();
			Collections.sort(listaSearch, new CatMarcaComparator());
			for (int i = 0; i < listaSearch.size(); i++) {
				 CategoriaDTO mar = (CategoriaDTO) listaSearch.get(i);
				 IValueSet fila = new ValueSet();
				 if ( aux_marcas.contains(mar.getNombre_marca()) == false ){
					 fila.setVariable("{nombremar}",  mar.getNombre_marca());
					 fila.setVariable("{cai}", mar.getId()+"");
					 fila.setVariable("{tip}", mar.getTipo()+"");
					 fila.setVariable("{caip}", mar.getSubcat()+"");
					 fila.setVariable("{idmar}", mar.getId_marca()+"");
					 fila.setVariable("{patron}", patron_original+"");
					 datos_m.add(fila);
					 aux_marcas.add(mar.getNombre_marca());
				 }
			}
			
			top.setDynamicValueSets("lista_marcas",datos_m);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}


	}

}