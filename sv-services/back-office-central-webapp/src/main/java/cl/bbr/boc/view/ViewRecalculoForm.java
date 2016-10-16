package cl.bbr.boc.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ProductosRelacionadosPromoDTO;
import cl.bbr.jumbocl.pedidos.promos.dto.ResumenPedidoPromocionDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * Muestra el monitor de Empresas
 * despliega los datos de la empresa, se puede utilizar filtros de búsqueda.
 * 
 * @author BBR
 *
 */
public class ViewRecalculoForm extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		long id_pedido		=-1;
		long id_local 		=-1;
		long monto_dscto_ped=0;
		String msje			= "";
		String modo ="0";
		logger.debug("User: " + usr.getLogin());

		// 1. Parámetros de inicialización servlet
	
		String html = getServletConfig().getInitParameter("TplFile");
		// le aasignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);	
		
		// 2. Procesa parámetros del request
		logger.debug("id_pedido:"+req.getParameter("id_pedido"));
		if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido");
		}
		id_pedido = Integer.parseInt( req.getParameter("id_pedido") );
		
		logger.debug("id_local:"+req.getParameter("id_local"));
		if ( req.getParameter("id_local") == null ){
			throw new ParametroObligatorioException("id_local");
		}
		id_local = Integer.parseInt( req.getParameter("id_local") );

		logger.debug("monto_dscto_ped:"+req.getParameter("monto_dscto_ped"));
		if ( req.getParameter("monto_dscto_ped") == null ){
			throw new ParametroObligatorioException("monto_dscto_ped");
		}
		monto_dscto_ped = Integer.parseInt( req.getParameter("monto_dscto_ped") );
		
		//mensaje
		if( req.getParameter("msje") != null )
			msje = req.getParameter("msje");
		
		if (req.getParameter("mod") != null ){
			modo = req.getParameter("mod"); 
		}
		
		//		3. Template
		View salida = new View(res);
	
		TemplateLoader load = new TemplateLoader(html);
		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4.  Rutinas Dinámicas 
		// 4.0 Bizdelegator 
		BizDelegate biz= new BizDelegate();
		
		//genera el recalculo de promociones
		ArrayList datos = new ArrayList();
		List lst_productos = new ArrayList();
		double sumatoria = 0.0;
		boolean edicion=false;
		try{
			PedidoDTO pedido = biz.getPedidosById(id_pedido);
			
			//[20121108avc
			ClientesDTO cliente = biz.getClienteById(pedido.getId_cliente());
			//]20121108avc
			
			//verifica modo edicion (asignado al usuario)
			if ( usr.getId_pedido() == id_pedido ){
				edicion = true;
			}
			logger.debug("edicion:"+edicion);
						
			//verifica estado del pedido y si esta en modo de edicion
			if((pedido.getId_estado()<Constantes.ID_ESTAD_PEDIDO_VALIDADO)&&(edicion)){
				//habilita aplicacion del recalculo
				top.setVariable("{hab_boton}"   ,"enabled");
			}
			else{
				//deshabilita aplicacion del recalculo
				top.setVariable("{hab_boton}"   ,"disabled");
			}			
			
			logger.debug("antes del recalculo");
			
//			[20121108avc
			lst_productos = biz.doRecalculoPromocion(id_pedido, id_local, cliente.isColaborador() ? new Long(cliente.getRut()) : null);
//			]20121108avc
			
			logger.debug("despues del recalculo");
			//se imprimen las promociones asignadas a los productos
			for (int i = 0; i< lst_productos.size(); i++){
				IValueSet fila_est = new ValueSet();
				ResumenPedidoPromocionDTO promo = (ResumenPedidoPromocionDTO)lst_productos.get(i);
				fila_est.setVariable("{id_promocion}", String.valueOf(promo.getId_promocion()));
				fila_est.setVariable("{cod_promo}", String.valueOf(promo.getPromo_codigo()));
				fila_est.setVariable("{tipo}", String.valueOf(promo.getTipo_promo()));
				fila_est.setVariable("{descripcion}", String.valueOf(promo.getDesc_promo()));							
				fila_est.setVariable("{fini}", String.valueOf(promo.getFec_ini()));
				fila_est.setVariable("{ffin}", String.valueOf(promo.getFec_fin()));			
				//fila_est.setVariable("{dscto_tot}", String.valueOf(promo.getDescto_total())+"%");
				fila_est.setVariable("{monto_descto_tot}", Formatos.formatoPrecio(promo.getMonto_descuento()));
				fila_est.setVariable("{i}", String.valueOf(i));
				
				sumatoria +=promo.getMonto_descuento();
				logger.debug("promo.getMonto_descuento() REC: "+ promo.getMonto_descuento());
				logger.debug("DESC REC: " + sumatoria);
				//productos relacionados
				List lst_productos_rel = new ArrayList();
				lst_productos_rel = promo.getProd_relacionados();
				String str_relacionados="";
				String linea_detalle_promo = "";
				
				for (int j = 0; j< lst_productos_rel.size(); j++){
					ProductosRelacionadosPromoDTO prel = (ProductosRelacionadosPromoDTO)lst_productos_rel.get(j);
					if (j>0){
						str_relacionados += ", ";
						linea_detalle_promo += "#";
					}
					str_relacionados += prel.getId_detalle()+" $"+prel.getProrrateo()+" ";
					double descuento_unitario = 0;
					descuento_unitario = Math.round(prel.getProrrateo() / prel.getCantidad());
					linea_detalle_promo += prel.getId_detalle()+","+prel.getId_producto()+","+prel.getPrecio_lista()+","+descuento_unitario;
				}
				logger.debug("productos relacionados id_detalles:"+str_relacionados);
				logger.debug("linea_detalle_promo: "+linea_detalle_promo);
				fila_est.setVariable("{lst_prod_rel}", promo.getDesc_prod());
				fila_est.setVariable("{prod_relacionados}", linea_detalle_promo);
				datos.add(fila_est);
			}
		}catch(BocException ex){
			ex.printStackTrace();
			logger.error("No puede realizar el calculo de promocion");	
			
		}
			
		top.setVariable("{total_reg}"  , String.valueOf(lst_productos.size()));
		top.setVariable("{msje}"  , msje);
		top.setVariable("{monto_dscto_ped}"  , String.valueOf(monto_dscto_ped));
		top.setVariable("{monto_dscto_ped_f}"  , Formatos.formatoPrecio(monto_dscto_ped));
		top.setVariable("{id_pedido}"  , String.valueOf(id_pedido));
		top.setVariable("{id_local}"  , String.valueOf(id_local));
		top.setVariable("{monto_dscto_nvo}"  , String.valueOf(sumatoria));
		top.setVariable("{monto_dscto_nvo_f}"  , Formatos.formatoPrecio(sumatoria));
		top.setVariable("{mod}"  , modo);

		// 6. Setea variables bloques		
		top.setDynamicValueSets("NEW_PROD_PROMO", datos);
		
		top.setVariable("{hdr_nombre}"	,usr.getNombre() + " " + usr.getApe_paterno());
		Date now = new Date();
		top.setVariable("{hdr_fecha}"	,now.toString());			
		
		//		 7. Salida Final
		String result = tem.toString(top);		
		salida.setHtmlOut(result);
		salida.Output();		
	
	}
	
}
