package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.cotizaciones.dto.CategoriaDTO;
import cl.bbr.vte.bizdelegate.BizDelegate;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página para presentar el paso 2.
 * 
 * Presenta el listado de categorías.
 * 
 * @author BBR e-commerce & retail
 *  
 */
public class ViewNewCotizacionP2 extends Command {

	/**
	 * Despliega el listado de categorías
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
			String pag_form = rb.getString("conf.path.html") + ""
					+ getServletConfig().getInitParameter("pag_form");
			logger.debug("Template:" + pag_form);
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
			if (session.getAttribute("ses_com_nombre") != null)
				top.setVariable("{nombre_comprador}", session
						.getAttribute("ses_com_nombre"));
			else
				top.setVariable("{nombre_comprador}", "");
			
			//Id de la cotizacion
			top.setVariable("{cot_id}", session.getAttribute("ses_cot_id"));
			
			// Datos de productos fuera de mix
			if( session.getAttribute("ses_prod_mix") != null )
				top.setVariable("{prod_mix}", session.getAttribute("ses_prod_mix"));
			else
				top.setVariable("{prod_mix}", "");

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();			
			
			// Recupera lista de categorías
			List list_categoria = biz.getCategoriasList();

			List arr_categoria = new ArrayList();

			// Despliegue de categorías
			//this.getLogger().info("# cat padres:"+list_categoria.size());
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
						
						aux_fila.setVariable("{cat_id}",  cat2.getId()+"");
						aux_fila.setVariable("{cat_nombre}", cat2.getNombre() + "");						
						aux_fila.setVariable("{cat_tipo}", cat2.getTipo()+"");
						aux_fila.setVariable("{cat_padre}", cat2.getId_padre()+"");
						
						aux_lista.add(aux_fila);						
						
					}
				}
				//this.getLogger().info("ID categoria:"+data.getId()+" # subcategorias:"+aux_lista.size());				
				fila.setDynamicValueSets("LIST_SUBCAT", aux_lista);				
				
				arr_categoria.add(fila);
			}
			top.setDynamicValueSets("LIST_CATEGORIAS", arr_categoria);		
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new Exception(e);
		}

	}

}