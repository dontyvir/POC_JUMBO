/**
 * NotificaPagoSoapBindingImpl.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis WSDL2Java emitter.
 */

package org.tempuri;

import java.util.Calendar;

import org.apache.log4j.Logger;

import cl.bbr.botonpago.bizdelegate.BizDelegate;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.cencosud.botonpago.NotificaPagoRep;

public class NotificaPagoSoapBindingImpl implements org.tempuri.NotificaPagoSoapPort {

    private static Logger __log = Logger.getLogger(NotificaPagoSoapBindingImpl.class);

    public org.tempuri.NotificaPagoExecuteResponse execute(org.tempuri.NotificaPagoExecute parameters) throws java.rmi.RemoteException {

        // se obtiene id del pedido
		String sIdCatPedido = parameters.getIdcarrocompra();
		String sIdPedido = sIdCatPedido.substring(0, sIdCatPedido.length()-2);
		
		__log.info("NOTIFICACION(op:"+sIdPedido+"): INICIO proceso de Notificacion desde BotonPagoTMAS--------------------");

        // muestra parametros recibidos (comentar luego)
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getIdcarrocompra="+parameters.getIdcarrocompra());
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getTipoautorizacion="+parameters.getTipoautorizacion());
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getCodigoautorizacion="+parameters.getCodigoautorizacion());
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getFechahoraautorizacion="+parameters.getFechahoraautorizacion());
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getNumerocuotas="+parameters.getNumerocuotas());
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getMontocuota="+parameters.getMontocuota());
        __log.debug("NOTIFICACION(op:"+sIdPedido+"): getMontooperacion="+parameters.getMontooperacion());
		
        
        // obtiene informacion para guaradar en la BD
        BotonPagoDTO bp = new BotonPagoDTO();
        bp.setIdPedido(Long.parseLong(sIdPedido));
        bp.setIdCatPedido(Long.parseLong(sIdCatPedido));
        bp.setTipoAutorizacion(parameters.getTipoautorizacion());
        bp.setCodigoAutorizacion(parameters.getCodigoautorizacion());
        bp.setFechaAutorizacion(parameters.getFechahoraautorizacion());
        bp.setNroCuotas(new Integer(parameters.getNumerocuotas()));
        bp.setMontoCuota(new Integer(parameters.getMontocuota()));
        bp.setMontoOperacion(new Integer(parameters.getMontooperacion()));

        // PREPARA RESPUESTA
        NotificaPagoExecuteResponse resp = new NotificaPagoExecuteResponse();
        resp.setIdcarrocompra(parameters.getIdcarrocompra());
        resp.setCodigoautorizacion(parameters.getCodigoautorizacion());
        resp.setTipoautorizacion(parameters.getTipoautorizacion());
        resp.setFechahoraautorizacion(Calendar.getInstance());
        resp.setNumerocuotas(parameters.getNumerocuotas());
        resp.setMontocuota(parameters.getMontocuota());
        resp.setMontooperacion(parameters.getMontooperacion());
        
        // notificacion de respuesta
        NotificaPagoRep not = new NotificaPagoRep();
        not.setCodigoResp(0);
        not.setGlosaResp("OK");
        if (!"A".equals(bp.getTipoAutorizacion()) ){
        	// si existe algo distinto a A:Aprobado le envío de vuelta a boton de pago
        	// el codigo de respuesta 2, que indica 'error' para que BntPago me envíe de vuelta al CheckOut
            not.setCodigoResp(2);
            not.setGlosaResp("Pago Rechazado por BotonPago ");
            __log.info("NOTIFICACION(op:"+sIdPedido+"): rechazado por TMAS, envío codigo de no OK");
        }
        
        resp.setNotificapago(not);
        
        __log.debug("\nRESPUESTA\n");
        __log.debug("Idcarrocompra        : " + resp.getIdcarrocompra());
        __log.debug("Codigoautorizacion   : " + resp.getCodigoautorizacion());
        __log.debug("Tipoautorizacion     : " + resp.getTipoautorizacion());
        __log.debug("Fechahoraautorizacion: " + resp.getFechahoraautorizacion());
        __log.debug("Numerocuotas         : " + resp.getNumerocuotas());
        __log.debug("Montocuota           : " + resp.getMontocuota());
        __log.debug("Montooperacion       : " + resp.getMontooperacion());
        
        NotificaPagoRep not2 = resp.getNotificapago();
        __log.debug("CodigoResp : " + not2.getCodigoResp());
        __log.debug("GlosaResp  : " + not2.getGlosaResp());
        
        try {
            
            // guarda información en la BD
        	// esto corresponde a la cabecera de la respuesta, la otra partae de la info viene como 
        	// parametros en la respuesta de coton de pago que llega al OrderComplete
            BizDelegate biz = new BizDelegate();    		
            biz.botonPagoSave(bp);

        } catch (Exception e) {
            
            // si hay algun error al guardar envía respuesta de NO OK
            
            __log.error("NOTIFICACION(op:"+sIdPedido+"): Excepcion: ", e);
            not.setCodigoResp(2);
            not.setGlosaResp("Error Registrando Notificacion en BD: " + e.getMessage());
        }

        // envía respuesta 
		__log.info("NOTIFICACION(op:"+sIdPedido+"): FIN    proceso de Notificacion desde BotonPagoTMAS--------------------");
        return resp;
    }

}
