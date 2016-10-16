package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.vte.utils.vteFormatos;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.cotizaciones.dto.ProductosCotizacionesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * Página que muestra el carro de compra que pertenecen a una cotización 
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewListCotProd extends Command {

	/**
	 * Despliega el listado del carro de compra
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

		// Instacia del bizdelegate
		BizDelegate biz = new BizDelegate();

		//Recupera el listado de detalles de la cotizacio
		List lista = biz.getProductosDetCotiz(Long.parseLong(session.getAttribute("ses_cot_id").toString()));
		List datos = new ArrayList();	
		long total = 0;
		long precio_total = 0;
		long contador = 0;
		for (int i = 0; i < lista.size(); i++) {
			ProductosCotizacionesDTO proXcoti = (ProductosCotizacionesDTO) lista.get(i);  
			IValueSet fila = new ValueSet();
			fila.setVariable("{id}", proXcoti.getDetcot_id()+"" );
			String nombre_pro = (proXcoti.getPro_tipo_producto()+" "+proXcoti.getMar_nombre()).trim();
			int largo_pro = Integer.parseInt(rb.getString("orderitemdisplay.largonombreproducto"));
			if( nombre_pro.length() < largo_pro )
				largo_pro = nombre_pro.length();
			fila.setVariable("{descrip}", nombre_pro.substring(0,largo_pro));
			fila.setVariable("{nommarca}", proXcoti.getPro_tipo_producto()+"\n"+proXcoti.getDetcot_desc()+"\n"+proXcoti.getMar_nombre()+"");
			precio_total = Utils.redondear(proXcoti.getDetcot_cantidad() * Utils.redondear(proXcoti.getDetcot_precio()));
			total += precio_total;
			//fila.setVariable("{precio}", vteFormatos.formatoPrecio(Utils.redondear(proXcoti.getDetcot_precio())) + "");
			fila.setVariable("{precio}", vteFormatos.formatoPrecio(Utils.redondear(proXcoti.getDetcot_precio()*proXcoti.getDetcot_cantidad())) + "");
			fila.setVariable("{cantidad}", proXcoti.getDetcot_cantidad() + "");
			fila.setVariable("{unidad}", vteFormatos.formatoUnidad(proXcoti.getPro_tipre()));
			fila.setVariable("{pro_id}", proXcoti.getDetcot_proId()+"");
			fila.setVariable("{intervalo}", proXcoti.getIntervalo()+"");
			
			fila.setVariable("{contador}", contador+"");
			contador++;
			
			datos.add(fila);				
		}
		
		top.setDynamicValueSets("ListaProductos", datos);
		top.setVariable("{lista_carro}",lista.size()+"");
		top.setVariable("{total}", vteFormatos.formatoPrecio(total) +"" );
		
		String result = tem.toString(top);

		out.print(result);

	}

}