package cl.bbr.fo.command;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;
import cl.bbr.fo.util.UtilsCarroCompra;
import cl.bbr.jumbocl.clientes.dto.MiCarroDTO;
import cl.bbr.log.Logging;

/**
 * Página que entrega los productos que tiene el cliente en el carro
 * <p>
 * Muestra un listado de los productos que se encuentran guardados en el carro
 * <p>
 * 
 * @author BBR e-commerce & retail
 * 
 */
public class OrderItemDisplay extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws CommandException {

		try {
			Logging logger = new Logging(this);
			long idProductoAgregado = 0;
			boolean primerProdNuevo = true;
			boolean primera = true;
			String myContextParam = null;
			IValueSet top = new ValueSet();
			BizDelegate biz = new BizDelegate();
			String idSession = null;
			List dto = null;

			// Carga properties
			ResourceBundle rb = ResourceBundle.getBundle("fo");

			// Recupera la sesión del usuario
			HttpSession session = arg0.getSession();
			arg1.setHeader("Cache-Control", "no-cache");
			arg0.setCharacterEncoding("UTF-8");
			arg1.setContentType("text/html; charset=iso-8859-1");
			// Recuperar los cupones y los tcp de la sesión
			List l_torec = new ArrayList();
			List l_tcp = new ArrayList();
			List listaCarro = new ArrayList();
			Long cliente_id = null;
		    UtilsCarroCompra utils = new UtilsCarroCompra();
			
		    String ses_loc_id = null;
			String ses_colaborador = null;
			String ses_cli_rut = null;
			
			if(session.getAttribute("ses_loc_id") != null){ses_loc_id = session.getAttribute("ses_loc_id").toString();}
			if(session.getAttribute("ses_colaborador") != null){ses_colaborador = session.getAttribute("ses_colaborador").toString();}
			if(session.getAttribute("ses_cli_rut") != null){ses_cli_rut = session.getAttribute("ses_cli_rut").toString();}

			if(getServletConfig().getInitParameter("pag_form")!= null)
		       myContextParam =
			getServletConfig().getInitParameter("pag_form");
			
			if (session.getAttribute("ses_cli_id")!= null)
	            cliente_id = new Long(session.getAttribute("ses_cli_id").toString());
	        else{
	            cliente_id = new Long(1);
	            session.setAttribute("ses_cli_id", "1");
	        }
			if (session.getAttribute("ses_promo_tcp") != null) {
				l_tcp = (List) session.getAttribute("ses_promo_tcp");
				l_torec.addAll(l_tcp);
			}
			if (session.getAttribute("ses_cupones") != null) {
				List l_cupones = (List) session.getAttribute("ses_cupones");
				l_torec.addAll(l_cupones);
			}
			if (session.getAttribute("ses_invitado_id") != null)
				idSession = session.getAttribute("ses_invitado_id").toString();
			/* Se identifica en sesion la primera carga */
			String value = null;
			if (session.getAttribute("primera") != null) {
				value = (String) session.getAttribute("primera");
				primera = Boolean.getBoolean(value);
			} else {
				session.setAttribute("primera", "false");
			}
			// Se recupera la salida para el servlet
			ServletOutputStream out = arg1.getOutputStream();
			List idsProductos = new ArrayList();
			if (session.getAttribute("ids_prod_add") != null) {
				idsProductos = (ArrayList) session.getAttribute("ids_prod_add");
				session.removeAttribute("ids_prod_add");
			}
 			// Recupera pagina desde web.xml y se inicia parser
			String pag_form = rb.getString("conf.dir.html") + "" + getServletConfig().getInitParameter("pag_form");
			this.getLogger().debug("Template:" + pag_form);
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();
			List categorias =(List) session.getAttribute("CATEGORIAS");
			List listaTop = new ArrayList(); 
			try{
			 		 
					long total = 0L;
					    listaCarro = biz.carroComprasPorCategorias(Long.parseLong(session.getAttribute("ses_cli_id").toString()), session.getAttribute("ses_loc_id").toString(), idSession);
						if (listaCarro != null && listaCarro.size() > 0) {
							listaCarro = biz.cargarPromocionesMiCarro(listaCarro,l_torec, Integer.parseInt(session.getAttribute("ses_loc_id").toString()));
						}
					if(categorias != null){
					    top = utils.agrupaCategorias(categorias, top, listaCarro, rb, idsProductos, primerProdNuevo, total, ses_loc_id, ses_colaborador, ses_cli_rut,  listaCarro,  biz,  l_torec);
					}else{
						 List list_categoria = biz.productosGetCategorias( cliente_id.longValue() );
						 if(list_categoria != null){
					        session.setAttribute("CATEGORIAS", list_categoria);
					        top = utils.agrupaCategorias(list_categoria, top, listaCarro, rb, idsProductos, primerProdNuevo, total, ses_loc_id, ses_colaborador, ses_cli_rut,  listaCarro,  biz,  l_torec);
						 }else{
							 System.out.println("Error OrderItemDisplay Error No carga Categorias !!!!");
							 logger.error("Error SQL OrderItemDisplay Error No carga Categorias !!!!");
							 throw new CommandException();
						 }
					}
					top.setDynamicValueSets("dto", listaCarro);
			 
			}catch (Exception e) {
				// TODO: handle exception
				top = new ValueSet();
				top.setDynamicValueSets("lista_carro", listaTop);
				top = utils.getPromoMiCarro(rb, l_torec, listaCarro, biz, top, 0, ses_loc_id, ses_colaborador, ses_cli_rut);
				top.setDynamicValueSets("dto", listaCarro);
				logger.error("Error OrderItemDisplay." + e +" id prod" +idProductoAgregado);
				logger.debug("Error OrderItemDisplay." + e +" id prod" +idProductoAgregado);
			}
			//cuando el carro esta vacio.
			if ((listaCarro != null && listaCarro.size() == 0) && top != null && (utils.cuentaLista(top) == 0 || top.getDynamicValueSets("lista_carro") == null)) {  
				IValueSet fila_lista_sel = new ValueSet();
				fila_lista_sel.setVariable("p", "");
				List datos_p = new ArrayList();
				datos_p.add(fila_lista_sel);
				top.setDynamicValueSets("lista_carro_img", datos_p);
			} else if(top != null && (top.getDynamicValueSets("lista_carro") !=  null && top.getDynamicValueSets("lista_carro").isEmpty())){
				IValueSet fila_lista_sel = new ValueSet();
				fila_lista_sel.setVariable("p", "");
				List datos_p = new ArrayList();
				datos_p.add(fila_lista_sel);
				top.setDynamicValueSets("lista_carro_img", datos_p);
			}
			if(top != null && (top.getDynamicValueSets("lista_carro") != null && top.getDynamicValueSets("lista_carro_img") != null)){
				if(!top.getDynamicValueSets("lista_carro").isEmpty())
				  top.getDynamicValueSets("lista_carro_img").clear();
			}
			
			//Genero estructura para chaordic_meta
			List chaordic_prd = new ArrayList();
			IValueSet ch_prod = null;
			for(int i = 0 ; i < listaCarro.size() ; i++){
				ch_prod = new ValueSet();
				MiCarroDTO producto = (MiCarroDTO)listaCarro.get(i);
				ch_prod.setVariable("{id_prod}", producto.getPro_id() + "");
				ch_prod.setVariable("{precio}", producto.getPrecio() + "");
				ch_prod.setVariable("{cantidad}", producto.getCantidad() + "");
				chaordic_prd.add(ch_prod);
			}
			top.setDynamicValueSets("CHAORDIC_PROD", chaordic_prd);
			
			String id_cart = "";
			if (session.getAttribute("ses_cli_id")!= null && !session.getAttribute("ses_cli_id").toString().equals("1"))
				id_cart = session.getAttribute("ses_cli_id").toString();
	        else{
	        	id_cart = session.getAttribute("ses_id").toString();
	        }
			top.setVariable("{id_carro}", id_cart);
			 
			session.setAttribute("top", top);
			session.setAttribute("dto",listaCarro);
			String result = tem.toString(top);
			arg1.setContentLength(result.length());
			out.print(result);
			out.flush();
			
		} catch (Exception e) {
			logger.debug("Error OrderItemDisplay : " + e.getMessage() );
			logger.error("Error OrderItemDisplay : " + e.getMessage() );
			throw new CommandException(e);
		}

	}

	

}
