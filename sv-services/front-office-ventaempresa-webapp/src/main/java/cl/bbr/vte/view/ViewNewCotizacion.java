package cl.bbr.vte.view;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cl.bbr.common.framework.Command;
import cl.bbr.vte.bizdelegate.BizDelegate;
import cl.bbr.vte.empresas.dto.DireccionesDTO;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

/**
 * P�gina que muestra Formulario para una nueva cotizaci�n
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class ViewNewCotizacion extends Command {

	/**
	 * Despliega Formulario para una nueva cotizaci�n 
	 * 
	 * @param arg0
	 *            Request recibido desde el navegador
	 * @param arg1
	 *            Response recibido desde el navegador
	 * @throws Exception
	 */
	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {

		try {

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("vte");			
			
			// Recupera la sesi�n del usuario
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

			// Nombre del comprador para header
			if( session.getAttribute("ses_com_nombre") != null )
				top.setVariable( "{nombre_comprador}", session.getAttribute("ses_com_nombre") );
			else
				top.setVariable( "{nombre_comprador}", "" );		
			
			
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			List lista = biz.getListDireccionDespBySucursal(Long.parseLong(session.getAttribute("ses_suc_id")+""));
			List datos = new ArrayList();
			for (int i = 0; i < lista.size(); i++) {
				DireccionesDTO dire = (DireccionesDTO) lista.get(i);
				IValueSet fila = new ValueSet();
				fila.setVariable("{id_dire}", dire.getId()+"");
				fila.setVariable("{direccion}", dire.getCalle()+" "+dire.getNumero()+", "+dire.getNom_comuna());
				datos.add(fila);
			}
			top.setDynamicValueSets("ListaDirecciones",datos);
			
			
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}

	}

}