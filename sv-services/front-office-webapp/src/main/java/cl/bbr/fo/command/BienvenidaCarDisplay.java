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
import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.common.utils.Formatos;

/**
 * Despliega la lista de productos guardados en el carro para la bienvenida.
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class BienvenidaCarDisplay extends Command {

	/**
	 * Despliega productos en el carro
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
			
			// Se recupera la salida para el servlet
			PrintWriter out = arg1.getWriter();		
			
			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

			IValueSet top = new ValueSet();

			// Instacia del bizdelegate
			BizDelegate biz = new BizDelegate();
			
			Long cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
			String local = "-1";
			 
			List listaCarro = biz.carroComprasGetProductos( cliente_id.longValue(), local, null ); 
			List datos_e = new ArrayList();
			for (int i = 0; i < listaCarro.size(); i++) {
				 CarroCompraDTO car = (CarroCompraDTO) listaCarro.get(i); 
				 IValueSet fila = new ValueSet();
				 fila.setVariable("{cantidad}", Formatos.formatoIntervaloFO(car.getCantidad())+""); 
				 fila.setVariable("{nota}", car.getTipo_producto() + " " + car.getNombre()+"");
				 datos_e.add(fila);
			}
			top.setDynamicValueSets("lista_carro",datos_e);
			String result = tem.toString(top);

			out.print(result);

		} catch (Exception e) {
			this.getLogger().error(e);
			e.printStackTrace();
			throw new CommandException(e);
		}

	}

}