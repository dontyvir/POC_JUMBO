package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.utils.vteFormatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.CotizacionesDTO;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * <p>Página que muestra el listado de productos que pertenecen a una cotización.</p>  
 * <p>El parámetro obligatorio que se necesita es el identificador unico de la cotización.</p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewProdCotiAnt extends Command {

	/**
	 * Despliega el listado de productos de una cotizacion
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		
			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");			
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.path.html") + "" + getServletConfig().getInitParameter("pag_form");
			logger.debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			if( arg0.getParameter("cot_id") == null )
				throw new ParametroObligatorioException( "Falta parámetro obligatorio. (cot_id)" );

			
			long cot_id = 0;
			cot_id = Long.parseLong(arg0.getParameter("cot_id"));
			
			if(cot_id > 0){
				// Instacia del bizdelegate
				BizDelegate biz = new BizDelegate();
	
				//Recupera el encabezado de de la cotizacion
				CotizacionesDTO detcoti = biz.getCotizacionesById(cot_id);
				top.setVariable("{cot_id}", detcoti.getCot_id()+"");
				top.setVariable("{nom_suc}", detcoti.getCot_nom_suc()+"");
				top.setVariable("{nom_emp}", detcoti.getCot_nom_emp());
				
				
				//Recupera el listado de detalles de la cotizacio
				List lista = biz.getProductosDetCotiz(cot_id);
				List datos = new ArrayList();	
				for (int i = 0; i < lista.size(); i++) {
					ProductosCotizacionesDTO proXcoti = (ProductosCotizacionesDTO) lista.get(i);  
					IValueSet fila = new ValueSet();
					fila.setVariable("{codigo}", proXcoti.getDetcot_proId() + "");
					fila.setVariable("{descrip}", proXcoti.getPro_tipo_producto() + " " + proXcoti.getMar_nombre() + " " + proXcoti.getDetcot_desc());
					fila.setVariable("{precio}", vteFormatos.formatoPrecio(Utils.redondear(proXcoti.getDetcot_precio())) + "");
					fila.setVariable("{cantidad}", proXcoti.getDetcot_cantidad() + "");
					fila.setVariable("{ite}", i + "");
					
					datos.add(fila);				
				}
				top.setVariable("{tot_reg}", lista.size()+"");
			
				top.setDynamicValueSets("ListaProductos", datos);
				
			}
			
			String result = tem.toString(top);

			out.print(result);

	}

}