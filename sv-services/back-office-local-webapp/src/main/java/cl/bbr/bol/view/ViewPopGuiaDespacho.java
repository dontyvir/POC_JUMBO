package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.bbr.vte.empresas.dto.EmpresasDTO;
import cl.bbr.vte.empresas.dto.SucursalesDTO;


/**
 * Despliega el home
 * @author bbr
 */
public class ViewPopGuiaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String mensaje_adv="Advertencia:<br>";
		boolean advertencias =false;
		String	param_id_pedido = "";
		long 	id_pedido		=-1;		
		
		// 1. Parámetros de inicialización servlet
		ResourceBundle rb = ResourceBundle.getBundle("bo");
		String url_impresion_guia = rb.getString("conf.path.gd");
		
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		if (req.getParameter("id_pedido")==null){
			throw new ParametroObligatorioException("id_pedido es null");
		}
		param_id_pedido	= req.getParameter("id_pedido");
		id_pedido = Long.parseLong(param_id_pedido);	
		logger.debug("id_pedido: " + param_id_pedido);
		
		
		// 3. Template
		View salida = new View(res);
		
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		// 4.  Rutinas Dinámicas
		//		 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();

		PedidoDTO pedido = new PedidoDTO();		
		pedido = bizDelegate.getPedido(id_pedido);
		
		if (!pedido.getOrigen().equals("V")){
			mensaje_adv +="<li>El pedido no es venta empresa.<br> ";
			advertencias=true;
		}
		
		//Encabezado guia
		//url de impresion  = http://localhost:8080/JumboBOLocal/
		top.setVariable("{url_impresion_guia}",url_impresion_guia);		
		
		//1. Pedido
		//top.setVariable("{n_folio}","12345678901234");
		top.setVariable("{id_pedido}",param_id_pedido);		
		top.setVariable("{fecha_gd11i}","2007-03-27 ");
		
		//2. Nombre del cliente
		top.setVariable("{nom_cliente48i}",Formatos.setStringAlignIzqRellenoDer(pedido.getNom_cliente(), 48));
		//logger.debug("nombre cliente 48i formateado:["+Formatos.setStringDerRelleno(pedido.getNom_cliente(), 48)+"]");
				
		//3. Codigo del local despachador
		top.setVariable("{de_local6i}",pedido.getNom_local());
		
		//4. Direccion de envio
		//top.setVariable("{direccion58i}","Calle, Republica de Cuba 1489 - Depto. 303");
		String direccion = pedido.getDir_tipo_calle();		
		if (pedido.getDir_calle()!=null){direccion  += ", "+pedido.getDir_calle();}		
		if (pedido.getDir_numero()!=null){direccion += ", "+pedido.getDir_numero();}
		if (pedido.getDir_depto()!=null){direccion  += " - dpto."+pedido.getDir_depto();}	
		top.setVariable("{direccion58i}",Formatos.setStringAlignIzqRellenoDer(direccion,58));
		
		
		//5. Comuna		
		top.setVariable("{comuna16i}",Formatos.setStringAlignIzqRellenoDer(pedido.getNom_comuna(),16));
		
		//6. Rut
		top.setVariable("{rut20i}",Formatos.setStringAlignIzqRellenoDer(pedido.getRut_cliente()+"-"+pedido.getDv_cliente(),20));
		
		try{
		
			//7. Giro / Rubro se obtiene a traves del cli_id, cli_emp_id, emp_rubro
			logger.debug("id_sucursal:"+pedido.getId_cliente());
			SucursalesDTO sucursal= bizDelegate.getSucursalById(pedido.getId_cliente());
			logger.debug("id_sucursal:"+sucursal.getSuc_id());
			EmpresasDTO empresa = bizDelegate.getEmpresaById(sucursal.getSuc_emp_id());
			
			top.setVariable("{giro18i}",Formatos.setStringAlignIzqRellenoDer(empresa.getEmp_rzsocial(),18));
			
		}catch (Exception ex){
			logger.debug("No encuentra la sucursal/empresa del pedido :"+ex.getMessage());			
			mensaje_adv +="<li>El pedido no tiene sucursal y no se puede obtener el giro.<br> ";
			top.setVariable("{giro18i}",Formatos.setStringAlignIzqRellenoDer(" ",18));
			advertencias=true;
		}
		
		//8. DETALLE Pedido
		String str_c1 = Formatos.setStringAlignDerRellenoIzq("Pedido: "+param_id_pedido,70);
		top.setVariable("{detalle80c1}",str_c1);
		//9. DETALLE Total Productos OJO ESTE FALTA
		
		
		List listaproductos = new ArrayList();
		listaproductos = bizDelegate.getDetPickingByIdPedido(id_pedido);
		double total_cantidades_prods = 0;
		for (int i=0; i<listaproductos.size(); i++){						
			DetallePickingDTO ped1 = new DetallePickingDTO();
			ped1 = (DetallePickingDTO)listaproductos.get(i);			
			total_cantidades_prods += ped1.getCant_pick();
			logger.debug("id_prod:"+ped1.getId_producto() + " cantidad"+ped1.getCant_pick());
			}
		
		String str_c2 = Formatos.setStringAlignDerRellenoIzq("Total Productos: "+total_cantidades_prods,70);
		top.setVariable("{detalle80c2}",str_c2);
		

		//10. DETALLE monto total neto
		//obtiene el monto total, similar al de la hoja de despacho
		double monto_total = bizDelegate.getMontoTotalHojaDespachoByIdPedido(id_pedido);
		String str_c3 = Formatos.setStringAlignDerRellenoIzq("Monto Total Neto: $"+monto_total,70);
		top.setVariable("{detalle80c3}",str_c3);
	
		
		// 11. globales
			
		if (advertencias){
			top.setVariable("{mensaje}"	,mensaje_adv);
		}
		else{
			top.setVariable("{mensaje}"	,"");
		}
		
		top.setVariable("{ini_imprime}"	,"");
		top.setVariable("{fin_imprime}"	,"");			
		
		
		
		// 12. Setea variables del template
		// variables del header
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		top.setVariable("{hdr_local}"	,usr.getLocal());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());		

		
		// 6. Setea variables bloques		
		// NO hay
		
		
		logger.debug("User: " + usr.getLogin());
		logger.debug("IdLocal: " + usr.getId_local());
		logger.debug("IdPerfil: " + usr.getId_perfil());
		
		
		// 7. Salida Final		
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();	
		
	}

}
