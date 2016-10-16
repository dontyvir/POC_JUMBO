package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;
import cl.bbr.bol.bizdelegate.BizDelegate;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.View;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.utils.NumericUtils;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.DespachoDTO;
import cl.bbr.jumbocl.pedidos.dto.DetallePickingDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorTrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * despliega una hoja de despacho
 * @author BBR
 */
public class ViewHojaDespacho extends Command {

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
		
		String	param_id_pedido = "";
		long 	id_pedido		=-1;
		
		/*ResourceBundle rb = ResourceBundle.getBundle("bo");
		String key = rb.getString("GoogleMaps.key");*/

		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request
		logger.debug("Procesando parámetros...");
		/*if ( req.getParameter("id_pedido") == null ){
			throw new ParametroObligatorioException("id_pedido es null");
		}*/
		
		param_id_pedido	= req.getParameter("id_pedido");
		
		id_pedido = Long.parseLong(param_id_pedido);	
		logger.debug("id_pedido: " + param_id_pedido);
		
		String local_tipo_picking = usr.getLocal_tipo_picking();
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();

		// 4. Rutinas Dinámicas
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
		
		/*LocalDTO local = bizDelegate.getLocalByID(usr.getId_local());
		top.setVariable("{latitud}", local.getLatitud()+"");
		top.setVariable("{longitud}", local.getLongitud()+"");
		top.setVariable("{nom_local}", usr.getLocal());
		top.setVariable("{key}", key);*/

		DespachoDTO despacho = bizDelegate.getDespachoById(id_pedido);
		PedidoDTO pedido = bizDelegate.getPedido(id_pedido);
		List listaBins = new ArrayList();
        if ( !pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_CASOS_CTE) && !pedido.getOrigen().equalsIgnoreCase(Constantes.ORIGEN_JV_CTE) ) {
    		if (local_tipo_picking.equals("L")){
    		    listaBins = bizDelegate.getBinsPedidoPKL(id_pedido);
    		} else {
    		    listaBins = bizDelegate.getBinsPedido(id_pedido);
    		}
        }

		//listaBins = bizDelegate.getBinsPedido(id_pedido);
		//List listTipos = bizDelegate.getEstadosByVis("TB","S");
		ArrayList bins = new ArrayList();
		logger.debug("numero de bins:" + listaBins.size());
		int cantidadBandejas = 0;
		for (int i=0; i<listaBins.size(); i++){
			BinDTO bin1 = (BinDTO)listaBins.get(i);
			if(pedido.getDispositivo().equalsIgnoreCase("M")){
				cantidadBandejas++;
				IValueSet fila = new ValueSet();
				fila.setVariable("{cod_bin}"		,String.valueOf(bin1.getCod_bin()));			
				fila.setVariable("{sector_picking}"	,String.valueOf(bin1.getNombre_sector_picking()));	
				bins.add(fila);
			}else if (!bin1.getTipo().equalsIgnoreCase("V")) {
				cantidadBandejas++;
				IValueSet fila = new ValueSet();
				fila.setVariable("{cod_bin}"		,String.valueOf(bin1.getCod_bin()));
				/*fila.setVariable("{cod_ubicacion}"	,String.valueOf(bin1.getCod_ubicacion()));
				fila.setVariable("{cod_sello_1}"	,String.valueOf(bin1.getCod_sello1()));
				fila.setVariable("{cod_sello_2}"	,String.valueOf(bin1.getCod_sello2()));	*/
				
				fila.setVariable("{sector_picking}"	,String.valueOf(bin1.getNombre_sector_picking()));	
				//fila.setVariable("{tipo}"	,FormatosVarios.frmEstado(listTipos, bin1.getTipo()));
				bins.add(fila);
			}
		}
		//4.2 Detalle de Productos
		List listaproductos = new ArrayList();
		listaproductos = bizDelegate.getProductosPedido(id_pedido);
		ArrayList ped = new ArrayList();
		for (int i=0; i<listaproductos.size(); i++){			
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO ped1 = (ProductosPedidoDTO)listaproductos.get(i);
			fila.setVariable("{cod_bin}"		,String.valueOf(ped1.getCod_bin()));
			fila.setVariable("{codigo}"			,String.valueOf(ped1.getCod_producto()));
			fila.setVariable("{descripcion}"	,ped1.getDescripcion());
			fila.setVariable("{cant}"	        ,String.valueOf(ped1.getCant_solic()));			
			ped.add(fila);
		}
		
		//4.3 productos sueltos
		List listaprodsueltos = new ArrayList();
		listaprodsueltos = bizDelegate.getProductosSueltosByPedidoId(id_pedido);
		
		ArrayList sueltos = new ArrayList();
		for (int i=0; i<listaprodsueltos.size(); i++){			
			IValueSet fila = new ValueSet();
			ProductosPedidoDTO suel1 = new ProductosPedidoDTO();
			suel1 = (ProductosPedidoDTO)listaprodsueltos.get(i);	
			fila.setVariable("{descripcion}",	suel1.getDescripcion());
			fila.setVariable("{codigo}"		,	suel1.getCod_producto()+"-"+suel1.getUnid_medida());
			fila.setVariable("{cant}"		,	String.valueOf(suel1.getCant_pick()));
			sueltos.add(fila);
		}
		
		ZonaDTO zona = bizDelegate.getZonaDespacho(despacho.getId_zona());		
		
		List tieneRegalo = new ArrayList();
		IValueSet fila_blanca = new ValueSet();
		if (zona.getRegalo_clientes() > 0) {
		    //mostrar cajas de check para el cliente SI/NO recibí conforme
		    fila_blanca.setVariable("{blanco}", "");
		    tieneRegalo.add(fila_blanca);		    
		}
		top.setDynamicValueSets("recibe_regalo", tieneRegalo);
		
		
		// 5. Setea variables del template
		top.setVariable("{id_pedido}"		, String.valueOf(despacho.getId_pedido()));
		top.setVariable("{zona}"		, 	  zona.getNombre() + "  ( " + zona.getDescripcion() + " )" );
		top.setVariable("{fec_entrega}"		, Utils.cambiaFormatoFecha( despacho.getF_despacho() ));
		top.setVariable("{h_entrega}"		, despacho.getH_ini()+" - "+despacho.getH_fin());
		top.setVariable("{nom_cliente}"		, despacho.getNom_cliente());
        top.setVariable("{rut_cliente}"     , despacho.getCliente().getRut() + "-" + despacho.getCliente().getDv());
		top.setVariable("{direccion}"		, despacho.getDir_tipo_calle()+" "+despacho.getDir_calle()+" "+despacho.getDir_numero()+" "+despacho.getDir_depto());
		if(!"".equalsIgnoreCase(despacho.getDir_depto())){
			top.setVariable("{txt_dir}"		,"(Dpto)");
		}else{
			top.setVariable("{txt_dir}"		,"(Casa)");
		}
		top.setVariable("{total_bins}"		, String.valueOf(despacho.getCant_bins()));		
		top.setVariable("{comuna}"			, despacho.getComuna());
		top.setVariable("{telefono}"		, despacho.getCod_telefono()+"-"+despacho.getTelefono());
		if ( ( despacho.getTelefono() != null || !"".equals(despacho.getTelefono()) ) && ( despacho.getTelefono2() != null || !"".equals(despacho.getTelefono2()) ) ) {
			top.setVariable("{palito}"		, " / ");
		} else {
			top.setVariable("{palito}"		, " ");
		}
		top.setVariable("{celular}"			, despacho.getCod_telefono2()+"-"+despacho.getTelefono2());		
		
        if ( despacho.getObservaciones().length() > 0 ) {
            top.setVariable("{observaciones}"	, despacho.getObservaciones()+", "+despacho.getIndicaciones());
        } else {
            top.setVariable("{observaciones}"   , despacho.getIndicaciones());
        }
        
		top.setVariable("{total_ban_y_cool}", String.valueOf(cantidadBandejas));
		
		//obtiene el total de productos sumando las cantidades pickeadas
		List listaproductos_pick = new ArrayList();
		listaproductos_pick = bizDelegate.getDetPickingByIdPedido(id_pedido);
		double total_cantidades_prods = 0;
		for ( int i = 0; i < listaproductos_pick.size(); i++ ) {	
			DetallePickingDTO ped1 = ( DetallePickingDTO ) listaproductos_pick.get(i);
			total_cantidades_prods += ped1.getCant_pick();			
			//logger.debug("id_prod:"+ped1.getId_producto() + " cantidad"+ped1.getCant_pick());
		}
		
		if ( NumericUtils.tieneDecimalesSignificativos( total_cantidades_prods, 1 ) ) {
		    total_cantidades_prods = Utils.redondear( total_cantidades_prods, 0 );
        }
		
		top.setVariable("{total_cant_prod}", String.valueOf( new Double( total_cantidades_prods).intValue()) );
		
		//obtiene el monto toal, similar al proceso que realiza la generación de trx_pago
		double monto_total = bizDelegate.getMontoTotalHojaDespachoByIdPedido(id_pedido);
		top.setVariable("{monto_total}"		, "$"+ (new Double (monto_total)).intValue() );
		
		List listaTrxMp = new ArrayList();
		listaTrxMp = bizDelegate.getTrxMpByIdPedido(id_pedido);
		logger.debug("size = "+listaTrxMp.size());
		if ( listaTrxMp.size() > 0 ) {
			for ( int i = 0; i < listaTrxMp.size(); i++ ) {
				logger.debug("i ="+i);
				MonitorTrxMpDTO row = (MonitorTrxMpDTO) listaTrxMp.get(i);
				if ( i == 0 ) {
					logger.debug("tiene data");
					logger.debug("row.getNum_doc() " + row.getNum_doc());
					top.setVariable("{num_doc}" ,String.valueOf(row.getNum_doc()));
					if ( row.getFecha() == null ) {
						top.setVariable("{fec_venta}"		,"");
					} else {	
						logger.debug("row.getFecha()"+row.getFecha()); 
						top.setVariable("{fec_venta}"		,row.getFecha().substring(4)+"-"+row.getFecha().substring(2,4)+"-"+row.getFecha().substring(0,2));
					}			
					top.setVariable("{num_caja}"		,String.valueOf(row.getNum_caja()));
				}	
			}
		} else {
			top.setVariable("{num_doc}"			," ");
			top.setVariable("{fec_venta}"		," ");
			top.setVariable("{num_caja}"		," ");
		}
		
		if (despacho.getTipoDespacho() == null){
		    despacho.setTipoDespacho("N");
		}
		
		if (despacho.getTipoDespacho().equals("R")) {
            top.setVariable("{accion_desp}"  , "Retirar");
            top.setVariable("{per_autorizada}"  , despacho.getPers_autorizada() + " " + despacho.getRutPersonaRetira());
            
            top.setVariable("{tipo_despacho}", "Retiro en Local");            
        } else {
            top.setVariable("{accion_desp}"  , "Recibir");
            top.setVariable("{per_autorizada}"  , despacho.getPers_autorizada());
        
            if (despacho.getTipoDespacho().equals("N")) {
                top.setVariable("{tipo_despacho}", "Normal");
            } else if (despacho.getTipoDespacho().equals("E")) {
                top.setVariable("{tipo_despacho}", "Express");
            } else if (despacho.getTipoDespacho().equals("C")) {
                top.setVariable("{tipo_despacho}", "Econ&oacute;mico");
            }
        }
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("select_bins_pedido", bins);
		top.setDynamicValueSets("select_detalle_productos", ped);		
		top.setDynamicValueSets("select_detalle_productos_sueltos", sueltos);		
		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();
	}
}
