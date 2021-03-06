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
import cl.bbr.fo.util.Tracking_web;
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * Presenta el resumen de las promociones en el paso3
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ViewPromocionesP3 extends Command {

	/**
	 * Presenta el resumen de las promociones en el paso3
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
			
			// Se almacena tracking en este sector
			//Tracking_web.saveTracking("PROMOCIONES", arg0);
	
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
	
			IValueSet top = new ValueSet();
	
			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();

			// Recupera la sesi�n del usuario
			HttpSession session = arg0.getSession();
			
			// Recuperar los datos de las promociones TCP
			List lst_promociones_tcp = null;
				
			List datos_fila = new ArrayList();
			
			List l_tcp_ses = (List)session.getAttribute("ses_promo_tcp");
			if(l_tcp_ses != null) {
				lst_promociones_tcp = biz.getPromocionesByTCP(l_tcp_ses);
				for (int  j=0; lst_promociones_tcp != null && j< lst_promociones_tcp.size(); j++){
					
					PromocionDTO promo = (PromocionDTO)lst_promociones_tcp.get(j);
					
					IValueSet fila = new ValueSet();
					fila.setVariable("{descripcion}", promo.getDescr());
					datos_fila.add( fila );
				}
			}
				
			top.setDynamicValueSets("L_PROMOCIONES_TCP", datos_fila);

			/*
			if(arg0.getParameter("atrib") != null && arg0.getParameter("atrib").equals("TJM")){//CASO TARJETA JUMBO MAS
				top.setVariable("{tipotarjeta}", "TARJETA JUMBO MAS");
			}else if(arg0.getParameter("atrib") != null && arg0.getParameter("atrib").equals("TTC")){//CASO TARJETA CREDITO
				top.setVariable("{tipotarjeta}", "TARJETA CREDITO");
			}else if(arg0.getParameter("atrib") != null && arg0.getParameter("atrib").equals("TSP")){//CASO TOTAL SIN PROMOCION
				top.setVariable("{tipotarjeta}", "TOTAL SIN PROMOCION");
			}
			*/
			top.setVariable("{tipotarjeta}", arg0.getParameter("atrib").toString());
			
			String result = tem.toString(top);
	
			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			
			throw new CommandException(e);
		}
	}

}