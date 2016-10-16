package cl.jumbo.ventamasiva.ctrl;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import cl.bbr.jumbocl.common.dto.CarroCompraCategoriasDTO;
import cl.bbr.jumbocl.common.dto.CarroCompraProductosDTO;
import cl.bbr.jumbocl.pedidos.dto.LocalDTO;
import cl.bbr.jumbocl.pedidos.dto.PagoVentaMasivaDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.jumbo.ventamasiva.BizDelegate.BizDelegate;
import cl.jumbo.ventamasiva.Constant.Constant;
import cl.jumbo.ventamasiva.command.Command;
import cl.jumbo.ventamasiva.log.Logging;
import cl.jumbo.ventamasiva.utils.Util;

public class CtrlPurchaseValidate extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logging logger = new Logging(this);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private JSONObject objOut = null; 
	private JSONObject objHeader = null;
	private JSONObject objResponse = null;
	 
	protected void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		PagoVentaMasivaDTO dto = null;
		request.setCharacterEncoding(Constant.ENCODING);
		try{
			response = getHeader(response, request);
			
			if (!"GET".equals(request.getMethod())) {
				printStringJson(response, "Error, Metodo 'GET' no es permitido.");
				return;
			}
			
			if (request.getParameter("token") != null) {
				BizDelegate biz = new BizDelegate();
				
				String token = request.getParameter("token").toString();								
				logger.info("token entrada["+token+"]");
				
				//String token = URLEncoder.encode(request.getParameter("token").toString());
				//String token = request.getParameter("token").toString();
				//logger.info("token entrada, URLEncoder["+token+"]");
				
				dto = biz.getPagoVentaMasivaByToken(token);								
				if(dto != null){
					logger.info("dto["+dto.toString()+"]");
				
					if(dto.getIdPedido()>0 && dto.getTokenPago().length()>0){		
						
						PedidoDTO pedido = biz.getPedidoById(dto.getIdPedido()); //getProductosXAlerta(dto.getIdPedido(), Constantes.KEY_ALERTA_COMPRA_OP);
						//List productos = biz.getProductosPedido(dto.getIdPedido());
						List productos = biz.getProductosSolicitadosVMById(dto.getIdPedido());
						if(pedido != null && productos!=null){
							LocalDTO loc = biz.getLocalRetiro(pedido.getId_local());
							printStringJson(response, pedido, productos, loc, "OK");
						}
					}
				}else{
					printStringJson(response, "Error, token no encontrado.");
				}
			}else{
				printStringJson(response, "Error, falta de parametro.");
			}
		}catch(Exception e){
			printStringJson(response, "Error, fallo en ejecucion.");
		}
	}
	
	private HttpServletResponse getHeader(HttpServletResponse response, HttpServletRequest request){
		response.setContentType("application/json; charset="+Constant.ENCODING);
		response.addHeader("X-requestId", String.valueOf(request.getAttribute("requestId")));
		response.addHeader("X-authorized", String.valueOf(request.getHeader("X-authorized")));
		response.addHeader("X-author", Constant.AUTHOR);
		return response;
	}

	public void printStringJson(HttpServletResponse response, String mensaje)throws Exception{
		objOut = new JSONObject(); 
		objHeader = new JSONObject();
		objResponse = new JSONObject();

		objResponse.put("Code", "1");
		objResponse.put("Message", mensaje);
		objResponse.put("AppId", Constant.APPLICATION);
		objResponse.put("DateTime", sdf.format(new Date()));

		objHeader.put("Response",objResponse.toString());
		objOut.put("Header" , objHeader.toString());

		OutPutJson(objOut, response);
	}

	public void printStringJson(HttpServletResponse response, PedidoDTO pedido, List productos, LocalDTO loc, String mensaje)throws Exception{
		 try {			 			 
			 objOut	= new JSONObject();
			 objHeader = new JSONObject();
			 objResponse = new JSONObject();
			 JSONObject objResumen = new JSONObject();
			 JSONObject objDetalle = new JSONObject();

			 objResponse.put("Code", "0");
			 objResponse.put("Message", "OK");
			 objResponse.put("AppId", Constant.APPLICATION);
			 objResponse.put("DateTime", sdf.format(new Date()));
			 
			 objResumen.put("id_pedido", pedido.getId_pedido()+"");
			 objResumen.put("nombre_cliente", pedido.getNom_cliente());
			 objResumen.put("rut", pedido.getRut_cliente()+""+pedido.getDv_cliente());			 			 			 
			 int posIni = pedido.getHdespacho().indexOf(":", 3);
			 int posFin = pedido.getHfindespacho().indexOf(":", 3);			
			 objResumen.put("fecha_tramo", pedido.getFdespacho() + " "+pedido.getHdespacho().substring(0, posIni) + " - " + pedido.getHfindespacho().substring(0, posFin));
			 
			 if (pedido.getTipo_despacho().equalsIgnoreCase("R")) {					
				String direc = loc.getDireccion() != null ? loc.getDireccion() : "";
				String indic = pedido.getIndicacion() != null ? pedido.getIndicacion() : "";
				objResumen.put("lugar_despacho", indic + " " + direc); 
			 }else{
				 if (pedido.getDir_depto().length() > 0) {
					 objResumen.put("lugar_despacho", pedido.getDir_tipo_calle() + " " + pedido.getDir_calle() + " " + pedido.getDir_numero()+ ", "+ pedido.getDir_depto()+", "+ pedido.getNom_comuna());
					 //objResumen.put("lugar_despacho", pedido.getDir_tipo_calle() + " " + pedido.getDir_calle() + " " + pedido.getDir_numero() + ", " + pedido.getDir_depto() + ", " + pedido.getNom_comuna());
				} else {
					 objResumen.put("lugar_despacho", pedido.getDir_tipo_calle() + " " + pedido.getDir_calle() + " " + pedido.getDir_numero()+ ", "+ pedido.getNom_comuna());
					//objResumen.put("lugar_despacho", pedido.getDir_tipo_calle() + " " + pedido.getDir_calle() + " " + pedido.getDir_numero() + ", " + pedido.getNom_comuna());
				}	 
			 }

			 objResumen.put("cantidad", pedido.getCant_prods()+"");
			 objResumen.put("monto", pedido.getMonto()+"");
			 objResumen.put("monto_despacho", pedido.getCosto_despacho() + "");
			 objResumen.put("monto_reservado",pedido.getMonto_reservado() + "");
			 objResponse.put("resumenPedido", objResumen.toString());			 
			 				 
			 objDetalle.put("medio_pago", pedido.getMedio_pago());			 
			 objDetalle.put("fecha_ingreso", cl.bbr.jumbocl.common.utils.Formatos.frmFecha(pedido.getFingreso()));
			 objDetalle.put("tipo_trans", "Venta");
			 objResponse.put("detallePedido", objDetalle.toString());
			 			 
			 double precio_total = 0;
			 ArrayList prds = new ArrayList();
			 for (int i=0; i < productos.size(); i++) {
				 CarroCompraCategoriasDTO cat = (CarroCompraCategoriasDTO) productos.get(i);
				 List prod = cat.getCarroCompraProductosDTO();

				 for (int j = 0; j < prod.size(); j++) {
					 CarroCompraProductosDTO producto = (CarroCompraProductosDTO) prod.get(j);
					 LinkedHashMap detalleProductos = new LinkedHashMap();
					 detalleProductos.put("cantidad", producto.getCantidad()+"");
					 detalleProductos.put("descripcion", producto.getNombre()+"");
					 detalleProductos.put("cod_sap", producto.getCodigo()+"");
					 detalleProductos.put("precio_unitario", producto.getPpum()+"");					 
					 precio_total = Util.redondearFO(producto.getPpum() * producto.getCantidad());
					 detalleProductos.put("precio_total", precio_total+"");
					 
					 if (producto.getStock() != 0) {
						 if (producto.getUnidad_tipo().charAt(0) == 'S') {												
							 for (double v = 0; v <= producto.getInter_maximo(); v += producto.getInter_valor()) {
								 detalleProductos.put("cantidad", v + "");
							 }
						 } else {
							 detalleProductos.put("cantidad", producto.getCantidad() + "");
						 }
					 }
					 prds.add(detalleProductos);
				 }
			 }
			 objResponse.put("detalleProductos", prds);			 
			 
			 objHeader.put("Response",objResponse.toString());
			 objOut.put("Header" , objHeader.toString());

			 OutPutJson(objOut, response);
		 } catch (IOException ex) {
			 logger.error(ex);
		 } catch (Exception ex) {
			 logger.error(ex);
		 }
	 }
	 
	 private void OutPutJson(JSONObject objOut, HttpServletResponse response) throws IOException, Exception{
		 PrintWriter output;
		 String js = objOut.toString();
		 logger.info(js+"\n");
		 output = response.getWriter();
		 output.println(js);							
		 output.close(); 
		 response.flushBuffer();
	 }
}
