/**
 * ConsultaCarroCompraSoapBindingImpl.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

import org.apache.log4j.Logger;

import cl.bbr.botonpago.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.cencosud.botonpago.ConsultaCarroCompraDet;
import cl.cencosud.botonpago.ConsultaCarroCompraRep;

public class ConsultaCarroCompraSoapBindingImpl implements
		org.tempuri.ConsultaCarroCompraSoapPort {

	private static Logger __log = Logger
			.getLogger(ConsultaCarroCompraSoapBindingImpl.class);

	public org.tempuri.ConsultaCarroCompraExecuteResponse execute(org.tempuri.ConsultaCarroCompraExecute parameters) throws java.rmi.RemoteException {

        // se obtiene id del pedido
		String sIdCatPedido = parameters.getIdcarrocompra();
		String sIdPedido = sIdCatPedido.substring(0, sIdCatPedido.length()-2);

		__log.info("CONSULTA(op:"+sIdPedido+"): INICIO proceso de Consulta desde BotonPagoTMAS--------------------");

		__log.debug("CONSULTA(op:"+sIdPedido+"): sobre OP= " + sIdPedido+"; idCatOP="+sIdCatPedido);
		

		//preparo primera parte de respuesta
		ConsultaCarroCompraExecuteResponse resp = new ConsultaCarroCompraExecuteResponse();
		resp.setIdcarrocompra(sIdCatPedido);
		ConsultaCarroCompraRep rep = new ConsultaCarroCompraRep();
		ConsultaCarroCompraDet[] detalles = null;

		try {

			// obtener datos del pedido
			BizDelegate biz = new BizDelegate();
			BotonPagoDTO bp = biz.botonPagoGetPedido(Long.parseLong(sIdPedido));
			// OJO no es necesario obtener el detalle del pedido,
			//        solo es necesario obtener el monto total de la OP y
			//        enviar 1 sólo registro como detalle con el total de
			//        la OP incluyendo despacho
			double monto = bp.getMontoReservado();

			int secuencia = 1;

			// preparo detalle con un solo registro con el monto total
			rep.setCodigoResp(0); // 0 = OK ; otro = no OK
			rep.setGlosaResp("OK");
			rep.setIdCarroCompra(sIdCatPedido);
			detalles = new ConsultaCarroCompraDet[1];
			ConsultaCarroCompraDet carroDet1 = new ConsultaCarroCompraDet();
			carroDet1.setSecuencia(secuencia);
			carroDet1.setMonto(monto);
			carroDet1.setGlosaServicio("Detalle OP");
			carroDet1.setNemoProveedor("Proveedor Jumbo.cl");
			detalles[0] = carroDet1;
			rep.setDetalleCarroCompra(detalles);

			resp.setConsultacarrocompra(rep);

			__log.info("CONSULTA(op:"+sIdPedido+"): va a enviar respuesta OK...(op="+sIdPedido+";opCat="+sIdCatPedido+";monto="+monto+")");

		} catch (Exception e) {
		    
		    // si hay algun problema, envio respuesta de NO OK
		    
			__log.error("CONSULTA(op:"+sIdPedido+"): Excepcion: ", e);
			rep.setCodigoResp(-1); // 0 = OK ; otro = no OK
			rep.setGlosaResp("ERROR al consultar el Carro Compra Exception: " + e.getMessage());
			rep.setIdCarroCompra(parameters.getIdcarrocompra());
			rep.setDetalleCarroCompra(detalles);
			resp.setConsultacarrocompra(rep);
			__log.info("CONSULTA(op:"+sIdPedido+"): va a enviar respuesta Error...(op="+sIdPedido+";opCat="+sIdCatPedido+";monto=0)");
		} finally {
			__log.info("CONSULTA(op:"+sIdPedido+"): FIN    proceso de Consulta desde BotonPagoTMAS--------------------");
		}
		return resp;
	}

}
