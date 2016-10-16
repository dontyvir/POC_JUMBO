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
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * Presenta las promociones asociadas al grupo (tcp) del cliente
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ViewTcpPromociones extends Command {

	/**
	 * Presenta las promociones asociadas al grupo (tcp) del cliente
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
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
	
			IValueSet top = new ValueSet();
	
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			// Recuperar los datos de las promociones para el producto
			List lst_promociones = null;
				
			List datos_fila = new ArrayList();
			
			List l_tcp_ses = (List)session.getAttribute("ses_promo_tcp");
			if(l_tcp_ses != null) {
				lst_promociones = biz.getPromocionesByTCP(l_tcp_ses);
				for (int  j=0; lst_promociones != null && j< lst_promociones.size(); j++){
					
					PromocionDTO promo = (PromocionDTO)lst_promociones.get(j);
					
					IValueSet fila = new ValueSet();
					fila.setVariable("{descripcion}", promo.getDescr());
					datos_fila.add( fila );
				}
			}
				
			top.setDynamicValueSets("L_PROMOCIONES", datos_fila);
				
			String result = tem.toString(top);
			
			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}
	}

}