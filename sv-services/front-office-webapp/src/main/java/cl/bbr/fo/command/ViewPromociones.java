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
import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.promo.lib.dto.PromocionDTO;

/**
 * Presenta las promociones asociadas al producto
 *  
 * @author BBR e-commerce & retail
 *  
 */
public class ViewPromociones extends Command {

	/**
	 * Presenta las promociones asociadas al producto
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
			long idprod = 0;

			if( arg0.getParameter("idprod") != null ) {
				
				List datos_fila = new ArrayList();
				
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
				
				idprod = Long.parseLong(arg0.getParameter("idprod").toString());
				ProductoDTO productoDTO = new ProductoDTO();
				productoDTO.setPro_id(idprod);
				lst_promociones = biz.promocionesGetPromocionesByProductoId( productoDTO.getPro_id(), productoDTO.getPro_id_bo(), session.getAttribute("ses_loc_id").toString(), l_torec );
				
				this.getLogger().debug( "Size:" + lst_promociones.size() );
				
				for (int  j=0; j< lst_promociones.size(); j++){
					
					PromocionDTO promo = (PromocionDTO)lst_promociones.get(j);
					
					this.getLogger().debug( "Promociones " + j + "=" + promo.getDescr() );
					
					IValueSet fila = new ValueSet();
					fila.setVariable("{descripcion}", promo.getDescr());
					fila.setVariable("{descuento}", promo.getDescuento1() + "");
					datos_fila.add( fila );
				}
				
				top.setDynamicValueSets("L_PROMOCIONES", datos_fila);
				
			}

			String result = tem.toString(top);
			
			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}
	}

}