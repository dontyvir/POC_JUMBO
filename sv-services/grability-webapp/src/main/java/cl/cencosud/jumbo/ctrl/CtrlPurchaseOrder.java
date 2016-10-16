package cl.cencosud.jumbo.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.clientes.dto.CarroCompraDTO;
import cl.bbr.jumbocl.clientes.dto.DireccionesDTO;
import cl.bbr.jumbocl.clientes.dto.LocalDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDTO;
import cl.bbr.jumbocl.pedidos.collaboration.ProcInsPedidoDetalleFODTO;
import cl.bbr.jumbocl.pedidos.dto.JorDespachoCalDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.promo.lib.dto.CuponDsctoDTO;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.Constant.ConstantPurchaseOrder;
import cl.cencosud.jumbo.Constant.ConstantShoppingCart;
import cl.cencosud.jumbo.bizdelegate.BizDelegatePurchaseOrder;
import cl.cencosud.jumbo.exceptions.GrabilityException;
import cl.cencosud.jumbo.input.dto.PurchaseOrder.PostInputPurchaseOrderDTO;
import cl.cencosud.jumbo.output.dto.PurchaseOrder.PostOutputPurchaseOrderDTO;

public class CtrlPurchaseOrder extends Ctrl {

	PostInputPurchaseOrderDTO purchaseOrderDTO;

	public CtrlPurchaseOrder(PostInputPurchaseOrderDTO purchaseOrderDTO) {
		super();
		this.purchaseOrderDTO = purchaseOrderDTO;
	}

	/**
	 * @return
	 * @throws GrabilityException
	 */
	public PostOutputPurchaseOrderDTO postPurchaseOrder() throws GrabilityException{

		PostOutputPurchaseOrderDTO outputDTO = new PostOutputPurchaseOrderDTO();
		BizDelegatePurchaseOrder biz = new BizDelegatePurchaseOrder();

		//try {
			ProcInsPedidoDTO pedido = new ProcInsPedidoDTO();
			DireccionesDTO direccion = purchaseOrderDTO.getDireccion();// biz1.getDireccionByIdDir(purchaseOrderDTO.getAddress_id());
			LocalDTO olocalDTO = purchaseOrderDTO.getLocal();
			
			pedido.setDir_id(purchaseOrderDTO.getAddress_id());
			pedido.setCosto_desp(purchaseOrderDTO.getJourney_price());
			pedido.setId_cliente(purchaseOrderDTO.getClient_id());
			pedido.setObservacion(purchaseOrderDTO.getOrder_observation());
			pedido.setMedio_pago(purchaseOrderDTO.getPayment_method());
			pedido.setSin_gente_txt(purchaseOrderDTO.getReceiver_observation());
			pedido.setId_local_fact(purchaseOrderDTO.getLocal_id());
			pedido.setId_jdespacho(purchaseOrderDTO.getDelivery_journey_id());
			pedido.setId_estado(Constantes.ID_ESTAD_PEDIDO_PRE_INGRESADO);
			pedido.setLst_tcp(new ArrayList());
			pedido.setSin_gente_op(Integer.parseInt(ConstantPurchaseOrder.SIN_GENTE_OP)); // se deja siempre en 0,ref:OrderCreate
			pedido.setDispositivo(Constant.SOURCE);
			// Condiciones segun el medio de pago
			if ("TBK".equalsIgnoreCase(purchaseOrderDTO.getPayment_method())) {
				pedido.setId_usuario_fono(ConstantPurchaseOrder.ID_USUARIO_FONO_COMPRAS_GENERICO);
				pedido.setNom_tbancaria(ConstantPurchaseOrder.NOMBRE_TARJETA_BANCARIA);
				
			}			
			if ("R".equalsIgnoreCase(purchaseOrderDTO.getShipping_type())) {
				pedido.setSin_gente_txt(purchaseOrderDTO.getOther_receiver());
				pedido.setSin_gente_rut(purchaseOrderDTO.getReceiver_rut());
				pedido.setSin_gente_dv(purchaseOrderDTO.getReceiver_dv());
				//Retiro Local
				pedido.setId_zona(olocalDTO.getId_zona_retiro());
				
			} else if (!("R".equalsIgnoreCase(purchaseOrderDTO.getShipping_type()))) {
				pedido.setSin_gente_txt(purchaseOrderDTO.getOther_receiver());
				pedido.setSin_gente_rut(0);
				pedido.setSin_gente_dv("");
				//Despacho
				pedido.setId_zona(direccion.getId_zon());
			}
			pedido.setTipo_doc(ConstantPurchaseOrder.TIPO_DOCUMENTO);
			pedido.setId_local_desp(purchaseOrderDTO.getLocal_id());
			
			//TODO:Modificado por el tipo de despacho C
			if(StringUtils.equals("C", purchaseOrderDTO.getShipping_type())){//getShipping_type			
				JorDespachoCalDTO oJorDespachoCalDTO = biz.getJornadaDespachoById(purchaseOrderDTO.getDelivery_journey_id());
				pedido.setFecha_despacho(oJorDespachoCalDTO.getFecha());
			}			
			
			pedido.setTipo_despacho(purchaseOrderDTO.getShipping_type());
			pedido.setPol_id(ConstantPurchaseOrder.ID_POLIGONO);
			pedido.setPol_sustitucion(ConstantPurchaseOrder.POLIGONO_SUSTITUCION);
			boolean aplicaCuponDscto = false;
			boolean isCuponDespacho = false;
			boolean isCuponProducto = false;
			boolean isCuponRubro = false;
			boolean isCuponSeccion = false;
			boolean isCuponCAT = false;
			boolean isCuponTBK = false;
			List cuponProds = null;
			CuponDsctoDTO cddto = biz.getCuponDscto(purchaseOrderDTO.getCoupon_code());
			if (cddto != null) {
				cuponProds = new ArrayList();
				if (cddto.getTipo().equals("D")) {
					isCuponDespacho = true;
				} else if (cddto.getTipo().equals("P")) {
					isCuponProducto = true;
					cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "P");
				} else if (cddto.getTipo().equals("R")) {
					isCuponRubro = true;
					cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "R");
				} else if (cddto.getTipo().equals("S")
						|| cddto.getTipo().equals("TS")) {
					isCuponSeccion = true;
					cuponProds = biz.getProdsCupon(cddto.getId_cup_dto(), "S");
				}
				if (cddto.getMedio_pago() == 0) {
					isCuponCAT = true;
					isCuponTBK = true;
				} else if (cddto.getMedio_pago() == 1) {
					isCuponCAT = true;
				} else {
					isCuponTBK = true;
				}
			}
			if (isCuponDespacho) {
            	if (isCuponCAT && "CAT".equals(pedido.getMedio_pago())) {
                	pedido.setCosto_desp(1);
                } else if (isCuponTBK && "TBK".equals(pedido.getMedio_pago())) {
                	pedido.setCosto_desp(1);
                }
            }
			pedido.setLst_cupones(new ArrayList());
			double cantidadProductos = 0.0;
			double montoTotal = 0.0;
			// Detalle del pedido
			List list_det = new ArrayList();
			List lcarro = biz.carroComprasGetProductos(pedido.getId_cliente(),"" + pedido.getId_local_fact(), null);
			
			if(lcarro == null || lcarro.size() == 0){
				throw new GrabilityException(ConstantShoppingCart.SC_CARRO_VACIO, ConstantShoppingCart.MSG_CARRO_VACIO);
			}
			
			// Recuperar los datos de los productos
			for (int i = 0; i < lcarro.size(); i++) {
				CarroCompraDTO prods = (CarroCompraDTO) lcarro.get(i);
				ProcInsPedidoDetalleFODTO detalle = new ProcInsPedidoDetalleFODTO();
				detalle.setId_producto_fo(Long.parseLong(prods.getPro_id()));
				detalle.setCant_solic(prods.getCantidad());
				detalle.setCon_nota("");
				if (prods.getNota() == null) {
					detalle.setObservacion("");
				} else {
					if (prods.getNota().equalsIgnoreCase("null")) {
						detalle.setObservacion("");
					} else {
						detalle.setObservacion(prods.getNota());
					}
				}
				detalle.setPrecio_unitario(prods.getPrecio());
				detalle.setTipoSel(prods.getTipoSel());
				detalle.setPrecio_lista(prods.getPrecio());
				list_det.add(detalle);
				cantidadProductos += prods.getCantidad();
				montoTotal += (prods.getCantidad() * prods.getPrecio());
				// inicio cdd
				if (!aplicaCuponDscto) {
					if (isCuponProducto) {
						Iterator itProds = cuponProds.iterator();
						while (itProds.hasNext()) {
							long productoCupon = Long.parseLong(itProds.next().toString());
							if (productoCupon == prods.getId_bo()) {
								aplicaCuponDscto = true;
								if (cddto.getDespacho() == 1)
									isCuponDespacho = true;
								break;
							}
						}
					} else if (isCuponRubro) {
						Iterator itProds = cuponProds.iterator();
						while (itProds.hasNext()) {
							int productoCupon = Integer.parseInt(itProds.next().toString());
							int seccionRubro = Integer.parseInt(prods.getCatsap() + prods.getId_rubro());
							if (productoCupon == seccionRubro) {
								aplicaCuponDscto = true;
								if (cddto.getDespacho() == 1)
									isCuponDespacho = true;
								break;
							}
						}
					} else if (isCuponSeccion) {
						Iterator itProds = cuponProds.iterator();
						while (itProds.hasNext()) {
							int productoCupon = Integer.parseInt(itProds.next().toString());
							if (productoCupon == Integer.parseInt(prods.getCatsap())) {
								aplicaCuponDscto = true;
								if (cddto.getDespacho() == 1)
									isCuponDespacho = true;
								break;
							}
						}
					}
				}
				// fin cdd
			}
			pedido.setCantidadProductos(cantidadProductos);
			//double porcientoValorCarro = montoTotal*(ConstantPurchaseOrder.PORC_DIFERENCIA_VALORCARRO_MONTO/100);
			//double rangoMenorValorCarro = montoTotal - porcientoValorCarro;
			//double rangoMayorValorCarro = montoTotal + porcientoValorCarro;
			//if ((purchaseOrderDTO.getAmount() >= rangoMenorValorCarro)&&(purchaseOrderDTO.getAmount() <= rangoMayorValorCarro)){//comparar rangos contra valor
				pedido.setMontoTotal(purchaseOrderDTO.getAmount());
			//}else {				
				//throw new ExceptionInParam(ConstantPurchaseOrder.SC_MONTO_FUERA_DE_RANGO, ConstantPurchaseOrder.MSG_MONTO_FUERA_DE_RANGO);
			//}
			pedido.setMontoTotal(montoTotal);
			pedido.setMontoTotal(purchaseOrderDTO.getAmount());
			pedido.setProductos(list_det);
			//try {
			long idPedido = biz.doInsPedidoNew(pedido, cddto,	cuponProds);
			outputDTO.setOrder_id(idPedido);
			outputDTO.setStatus(Constant.SC_OK);
			outputDTO.setError_message(Constant.MSG_OK);
			String user = "SYSTEM";
	        ingresaLogOP(idPedido, user, biz);
	       
			PedidoDTO pedidoGenerado = biz.getPedidoById(idPedido);
			outputDTO.setCart_amount((long)pedidoGenerado.getMonto());
			outputDTO.setJourney_price((long)pedidoGenerado.getCosto_despacho());
			outputDTO.setReserved_amount((long)pedidoGenerado.getMonto_reservado());
			
	            
	        	

		return outputDTO;
	}
	public void ingresaLogOP(long idPedido, String user, BizDelegatePurchaseOrder biz) throws GrabilityException{
		String mensajeLog = "<b>[APP MOBILE]</b> Pedido generado con aplicacion Movil";
        ParametrosService ps = new ParametrosService();
        ParametroDTO par;
		try {
			par = ps.getParametroByName("MENSAJE_LOG_GRABILITY");
	           if (par!=null){
	            	mensajeLog = par.getValor();
	           }
	        biz.insertaTrackingOP(idPedido, user, mensajeLog);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
