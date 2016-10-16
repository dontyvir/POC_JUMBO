package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que despliega el resultado de busqueda de productos, agrupados por sección y por marca 
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewSearchProductos extends Command {

	/**
	 * Despliega el resultado de busqueda de productos
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
			//throws CommandException {

		try {

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");			
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
			logger.debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();
			
	        //Despliega el Banner de inicio dependiendo del lugar desde donde se loguea
			top.setVariable("{bt_inicio}", session.getAttribute("bt_inicio").toString());

			//Se setea la variable tipo usuario
			if(session.getAttribute("ses_tipo_usuario") != null ){
				top.setVariable("{tipo_usuario}", session.getAttribute("ses_tipo_usuario").toString());
			}else{
				top.setVariable("{tipo_usuario}", "0");
			}
		

			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		
			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Recuperara identificador único para el local de la dirección de despacho de la cotización
			long local_id = 0;
			try {
				//CotizacionesDTO cotizacion = biz.getCotizacionesById( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
			    long id_dir = biz.getDireccionByCotizacion( Long.parseLong(session.getAttribute("ses_cot_id").toString()) );
				local_id = biz.getLocalDireccion( id_dir );
			} catch (Exception e) {
				logger.error("Problemas con la recuperación del ID del local.");
				logger.debug(e.getMessage());
				throw new Exception(e);
			}			

			

			List list_patrones = new ArrayList();
			int largo_patron = Integer.parseInt(rb.getString("searchitemdisplay.largopatron"));
			
			//int criterio = Integer.parseInt(arg0.getParameter("tipobus").toString());
			
			String[] patrones = Formatos.stringToSearch(arg0.getParameter("patron").toString()).split("[\\s]+");
			
			top.setVariable("{patron}",  arg0.getParameter("patron").toString() );
			
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
			
			List listaSearch = new ArrayList();
			
			if( list_patrones.size() > 0 )
				listaSearch = biz.getSearch( list_patrones, local_id, 0 );
			
			List datos_s = new ArrayList();
			List datos_m = new ArrayList();
			long total_encuentros = 0;
			List cat_aux = new ArrayList();
			
			for (int i = 0; i < listaSearch.size(); i++) {
				 CategoriaDTO cat = (CategoriaDTO) listaSearch.get(i);
				 
				 total_encuentros += cat.getCant_productos();
				 
				 if ( cat_aux.contains(cat.getNombre()+"-"+cat.getSubcat()) == false ){				 
					 IValueSet fila = new ValueSet();
					 fila.setVariable("{idcat}", cat.getId()+"");
					 fila.setVariable("{idcatpadre}", cat.getId_padre()+"");
					 fila.setVariable("{nombrecat}", cat.getNombre()+"");
					 fila.setVariable("{tipo}", cat.getTipo()+"");
					 fila.setVariable("{patron}", arg0.getParameter("patron").toString()+"");
					 
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

			List aux_marcas = new ArrayList();
			Collections.sort(listaSearch, new CatMarcaComparator());
			for (int i = 0; i < listaSearch.size(); i++) {
				 CategoriaDTO mar = (CategoriaDTO) listaSearch.get(i);
				 IValueSet fila = new ValueSet();
				 if ( aux_marcas.contains(mar.getNombre_marca()) == false ){
					 fila.setVariable("{nombremar}",  mar.getNombre_marca());
					 fila.setVariable("{cai}", mar.getId()+"");
					 fila.setVariable("{idcatpadre}", mar.getId_padre()+"");
					 fila.setVariable("{tip}", mar.getTipo()+"");
					 fila.setVariable("{caip}", mar.getSubcat()+"");
					 fila.setVariable("{idmar}", mar.getId_marca()+"");
					 fila.setVariable("{patron}", arg0.getParameter("patron").toString());
					 datos_m.add(fila);
					 aux_marcas.add(mar.getNombre_marca());
				 }
			}
			
			top.setDynamicValueSets("lista_marcas",datos_m);
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			//throw new CommandException(e);
		}

	}
	private class CatMarcaComparator implements Comparator  {

		public int compare(Object arg0, Object arg1) {
			CategoriaDTO cat1 = (CategoriaDTO) arg0;
			CategoriaDTO cat2 = (CategoriaDTO) arg1;
			return cat1.getNombre_marca().compareTo(cat2.getNombre_marca());
		}
		
	}

}