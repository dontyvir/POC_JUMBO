package cl.bbr.bol.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import cl.bbr.jumbocl.clientes.dto.ClientesDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.common.utils.Utils;
import cl.bbr.jumbocl.pedidos.dto.BinDTO;
import cl.bbr.jumbocl.pedidos.dto.MonitorPedidosDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidosCriteriaDTO;
import cl.bbr.jumbocl.pedidos.dto.ProductosBinDTO;
import cl.bbr.jumbocl.pedidos.dto.RondaDTO;
import cl.bbr.jumbocl.pedidos.dto.ZonaDTO;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
/**
 * Formulario que despliega un listado para impresión de etiquetas 
 * @author mleiva
 */
public class ViewPrintEtiquetas extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2714925113990129028L;
	protected static final int N_ETIQUETA_CON_BINS = 2; 

	protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {

		String param_id_ronda = "";
		long id_ronda = -1;

		// 1. Parámetros de inicialización servlet
		String html = getServletConfig().getInitParameter("TplFile");
		// le asignamos el prefijo con la ruta
		html = path_html + html;
		logger.debug("Template: " + html);

		// 2. Procesa parámetros del request

		logger.debug("Procesando parámetros...");
		if (req.getParameter("id_ronda") == null) {
			throw new ParametroObligatorioException("id_ronda");
		}
		param_id_ronda = req.getParameter("id_ronda");
		id_ronda = Long.parseLong(param_id_ronda);
		logger.debug("id_ronda = " + req.getParameter("id_ronda"));

		if (id_ronda == -1)
			throw new ParametroObligatorioException("id_ronda viene vacío");
		
		// 3. Template (dejar tal cual)
		View salida = new View(res);

		TemplateLoader load = new TemplateLoader(html);

		ITemplate tem = load.getTemplate();
		IValueSet top = new ValueSet();
		
		String local_tipo_picking = usr.getLocal_tipo_picking();

		// 4. Rutinas Dinámicas
		
		// 4.0 Creamos al BizDelegator
		BizDelegate bizDelegate = new BizDelegate();
	
		// 4.1 Obtenemos la ronda
		RondaDTO ronda = bizDelegate.getRondaById(id_ronda);
	
		int cant_bin = 0; 
		
		//List pedidos = bizDelegate.getPedidosByRonda( id_ronda );
		
		//boolean getCliente = false;
		
		List l_man = new ArrayList();
		List l_bins = new ArrayList();
		HashMap l_prod_sueltos = new HashMap();
		
		List listaBins = new ArrayList();
		if (local_tipo_picking.equals("L")){
		    listaBins = bizDelegate.getBinsRondaPKL(id_ronda);;
		}else{
		    listaBins = bizDelegate.getBinsRonda(id_ronda);;
		}

		//List bins = bizDelegate.getBinsRonda(id_ronda);
		int cant_reg = 1;
		for( int i = 0; i < listaBins.size(); i++ ) {

			BinDTO bod1 = (BinDTO)listaBins.get(i);

			// Recuperar zona de la OP
			// creamos los criterios
			PedidosCriteriaDTO criterio = new PedidosCriteriaDTO();
			criterio.setId_pedido(bod1.getId_pedido());
			criterio.setTipo_picking(ronda.getTipo_picking());
			List l_pedidos = bizDelegate.getPedidosByCriteria( criterio );
			String zona ="";
			String fecha_despacho = "";
			String horai_despacho = "";
			String horaf_despacho = "";
			String ap_paterno_cli = "";
			String nombre_cli = "";

			if( l_pedidos.size() > 0 ) {
				MonitorPedidosDTO pedido = (MonitorPedidosDTO) l_pedidos.get(0);
				zona = pedido.getZona_nombre();
				fecha_despacho = Formatos.frmFecha(pedido.getFdespacho());
				horai_despacho = pedido.getHdespacho();
				horai_despacho = horai_despacho.substring( 0, horai_despacho.length()-3 );
				horaf_despacho = pedido.getHdespacho_fin();
				horaf_despacho = horaf_despacho.substring( 0, horaf_despacho.length()-3 );
				ClientesDTO cliente = new ClientesDTO ();
				try{
					cliente  = bizDelegate.getClienteByRut(pedido.getRut_cliente());
				}catch (Exception e){
					cliente.setPaterno(pedido.getNom_cliente().split(" ")[1]);
					cliente.setNombre(pedido.getNom_cliente().split(" ")[0]);
				}	
				if (cliente.getPaterno() != null){
					if (cliente.getPaterno().length() > 10){
					    ap_paterno_cli = cliente.getPaterno().substring(0, 10).toUpperCase();
					}else{
					    ap_paterno_cli = cliente.getPaterno().toUpperCase();
					}
				}
				if (cliente.getNombre() != null){
					if (cliente.getNombre().length() > 10){
					    nombre_cli = cliente.getNombre().substring(0, 10).toUpperCase();
					}else{
					    nombre_cli = cliente.getNombre().toUpperCase();
					}
				}
			}

			// BIN es virtual 
			// Recuperar los productos por Bin
			if( !bod1.getTipo().equals(Constantes.TIPO_BIN_FIJO) ) {
				
				List l_prod = bizDelegate.getProductosBin(bod1.getId_bp());

				if( l_prod.size() == 0 )
					continue;

				// Una etiqueta por cada producto del bin
				for( int j = 0; j < l_prod.size(); j++ ) {
					ProductosBinDTO prod1 = (ProductosBinDTO)l_prod.get(j);
					
				    if (l_prod_sueltos.get(prod1.getCod_prod_sap()) == null){
				        List detprod = new ArrayList();
				        if (prod1.getDescripcion().length() > 32){
				            detprod.add(0, prod1.getDescripcion().substring(0, 32));//Descripcion
				        }else{
				            detprod.add(0, prod1.getDescripcion());//Descripcion
				        }
				        detprod.add(1, ""+prod1.getCantidad());//Cantidad
				        l_prod_sueltos.put(prod1.getCod_prod_sap(), detprod);
				    }else{
				        List detprod = (List)l_prod_sueltos.get(prod1.getCod_prod_sap());
				        double cantProd = Double.parseDouble(detprod.get(1).toString()) + prod1.getCantidad();
				        detprod.set(1, ""+cantProd);
				        l_prod_sueltos.put(prod1.getCod_prod_sap(), detprod);
				    }

					// Una etiqueta por cada unidad del producto
					for(int c = 0; c < prod1.getCantidad(); c++ ) {
					    		    
						IValueSet fila = new ValueSet();
						fila.setVariable("{num_op}"		,String.valueOf(bod1.getId_pedido()));
						fila.setVariable("{cod_bin}"	,String.valueOf(bod1.getCod_bin()));
						fila.setVariable("{id_ronda}"	,param_id_ronda);
						fila.setVariable("{zona_des}"   ,zona);					
						fila.setVariable("{hora_ini}"   ,horai_despacho);
						fila.setVariable("{hora_fin}"   ,horaf_despacho);
						fila.setVariable("{fecha_desp}" ,fecha_despacho);
						fila.setVariable("{ap_paterno_cli}", ap_paterno_cli);
						fila.setVariable("{nombre_cli}", nombre_cli);

						/*if (prod1.getDescripcion().length() > 50){
							fila.setVariable("{nom_producto}",prod1.getDescripcion().substring(0,50) + "<br>" + "");
						}else{
							fila.setVariable("{nom_producto}",prod1.getDescripcion() + "<br>" + "");
						}*/
	
						List l_bin_pro = new ArrayList();
						IValueSet fila_producto = new ValueSet();
						String descrProducto = Utils.separarDescripcionesLargas( prod1.getDescripcion() );
						if (descrProducto.length() > 60){
						    fila_producto.setVariable("{nom_producto}",descrProducto.substring(0,60));
						}else{
						    fila_producto.setVariable("{nom_producto}",descrProducto);
						}
						//fila_producto.setVariable("{nom_producto}",prod1.getDescripcion());
						l_bin_pro.add(fila_producto);
						fila.setDynamicValueSets("nom_pro", l_bin_pro);
						
						// Etiqueta par o impar
						if( (cant_reg % 2) == 0 ) {
							// siguiente fila
							fila.setVariable("{siguiente_fila}","</tr><tr>");
						}else{
							fila.setVariable("{siguiente_fila}","");
						}
						cant_reg++;
						
						l_bins.add(fila);
					}
				}
			// BIN no virtual
			} else {
				for(int x = 0; x < N_ETIQUETA_CON_BINS ; x++){
					IValueSet fila = new ValueSet();
					fila.setVariable("{num_op}"		,String.valueOf(bod1.getId_pedido()));
					fila.setVariable("{zona_des}"   ,zona);
					fila.setVariable("{hora_ini}"   ,horai_despacho);
					fila.setVariable("{hora_fin}"   ,horaf_despacho);
					fila.setVariable("{fecha_desp}" ,fecha_despacho);
					fila.setVariable("{ap_paterno_cli}", ap_paterno_cli);
					fila.setVariable("{nombre_cli}", nombre_cli);
	
					List l_bin_pro = new ArrayList();
					IValueSet fila_producto = new ValueSet();
					fila_producto.setVariable("{cod_bin}"	, String.valueOf(bod1.getCod_bin()));
					fila_producto.setVariable("{id_ronda}"	,param_id_ronda);
					
					if (bod1.getNombre_sector_picking().equalsIgnoreCase("Frescos")){
					    fila_producto.setVariable("{sector_camara}"	, "Frescos");
					}else if (bod1.getNombre_sector_picking().equalsIgnoreCase("Congelados")){
					    fila_producto.setVariable("{sector_camara}"	, "Congelados");
					}else{
					    fila_producto.setVariable("{sector_camara}"	, "");
					}
					l_bin_pro.add(fila_producto);
					fila.setDynamicValueSets("nom_pro_sin", l_bin_pro);
					
					// Etiqueta par o impar
					if( (cant_reg % 2) == 0 ) {
						// siguiente fila
						fila.setVariable("{siguiente_fila}","</tr><tr>");
					}else{
						fila.setVariable("{siguiente_fila}","");
					}
					cant_reg++;
					String etiqueta = fila.toString();
					l_bins.add(fila);
				}
			}
			
			// Datos para el manifiesto
			IValueSet fila_man = new ValueSet();
			//fila_man.setVariable("{pos_bin}",bod1.getCod_ubicacion()+"");
			fila_man.setVariable("{nro_bin}",bod1.getCod_bin()+"");
			fila_man.setVariable("{nro_op}",bod1.getId_pedido()+"");
			fila_man.setVariable("{zona}", "("+zona+")");
			//ZonaDTO zona = new ZonaDTO();
			//zona = bizDelegate.
			l_man.add(fila_man);
			cant_bin++;
			
		}
		
		UserDTO Pickeador = bizDelegate.getUserById(ronda.getPickeador());
		top.setVariable( "{pick_nombre}", Pickeador.getNombre()+" " + Pickeador.getApe_paterno()+" " + Pickeador.getApe_materno() );
		top.setVariable( "{id_ronda}", param_id_ronda);
		top.setVariable( "{cant_bin}", cant_bin+"");

		long hini = 0L;
		long hfin = 0L;
		double diff = 0D;
		double horas = 0D;
		if (ronda.getTipo_picking().equals("N")){
			// CALCULA PRODUCTIVIDAD DE UNA RONDA ==> PRODUCTOS DE LA RONDA / ( HORA FIN PICKING - HORA INICIO PICKING )
			hini = ronda.getFini_picking().getTime();
			hfin = ronda.getFfin_picking().getTime();
		}else if (ronda.getTipo_picking().equals("L")){
			// CALCULA PRODUCTIVIDAD DE UNA RONDA LIGHT ==> PRODUCTOS DE LA RONDA / ( HORA INICIO PICKING LIGHT - HORA INICIO PICKING )
			if (ronda.getFecha_inico_ronda_pkl() != null){
			    hini = ronda.getFecha_inico_ronda_pkl().getTime();
				hfin = ronda.getFini_picking().getTime();
			}
		}
		
		if (ronda.getTipo_picking().equals("L") && ronda.getFecha_inico_ronda_pkl() == null){
		    top.setVariable("{productividad_ronda}", "0 prod./hrs.");
		}else{
			diff = hfin - hini;
			horas = (diff/(1000*60*60));
			top.setVariable("{productividad_ronda}", Formatos.formatoNumeroSinDecimales(ronda.getCant_prods()/horas)+" prod./hrs.");		    
		}
		/*double diff = hfin - hini;
		//String horas = Formatos.formatoNumeroUnDecimal(diff/(1000*60*60));
		double horas = (diff/(1000*60*60));
		
		top.setVariable("{productividad_ronda}", Formatos.formatoNumeroSinDecimales(ronda.getCant_prods()/horas)+" prod./hrs.");*/
		
		// Mensaje de las zonas
		List l_zonas = new ArrayList();
		List zonas = bizDelegate.getZonasFinalizadas(id_ronda);
		for( int i = 0; i < zonas.size(); i++ ) {
			ZonaDTO zona = (ZonaDTO)zonas.get(i);
			IValueSet fila = new ValueSet();
			fila.setVariable("{mensaje_zona}", zona.getNombre() );
			l_zonas.add(fila);
		}

		// Listado de Productos Sueltos
		List l_prod_s = new ArrayList();
	    Iterator it = l_prod_sueltos.keySet().iterator();
	    while (it.hasNext()) {
	        String codProd = it.next().toString();
	        List detprod = (List)l_prod_sueltos.get(codProd);
			IValueSet fila = new ValueSet();
			fila.setVariable("{desc_prod}", detprod.get(0));
			fila.setVariable("{cant_prod}", detprod.get(1));
			l_prod_s.add(fila);
	    }

	    if (l_prod_s.size() > 0){
	        top.setVariable("{productos_sueltos}", "  <tr>" +
                                                   "    <td colspan=\"3\"><br><b>Listado de Productos Sueltos.</b><br></td>" +
                                                   "  </tr>");
	    }else{
	        top.setVariable("{productos_sueltos}", "");
	    }
		// 5. Setea variables del template
		
		// 6. Setea variables bloques
		top.setDynamicValueSets("lista_bins", l_bins);
		top.setDynamicValueSets("lista_man", l_man);
		top.setDynamicValueSets("lista_zonas", l_zonas);
		top.setDynamicValueSets("lista_prod_s", l_prod_s);

		
		// 7. Salida Final
		String result = tem.toString(top);
		salida.setHtmlOut(result);
		salida.Output();

	}
}
