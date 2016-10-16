package cl.bbr.irsmonitor.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cl.bbr.irsmonitor.bizdelegate.BizDelegate;
import cl.bbr.irsmonitor.datos.ProductoC1;
import cl.bbr.irsmonitor.datos.ProductoC2;
import cl.bbr.irsmonitor.dto.C1ReqDTO;
import cl.bbr.irsmonitor.dto.C1RspDTO;
import cl.bbr.irsmonitor.dto.C2ReqDTO;
import cl.bbr.irsmonitor.dto.HeaderPosDTO;
import cl.bbr.irsmonitor.exceptions.IrsMonException;
import cl.bbr.irsmonitor.utils.Utiles;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.parametros.service.ParametrosService;
import cl.bbr.jumbocl.pedidos.dto.BotonPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.FacturaDTO;
import cl.bbr.jumbocl.pedidos.dto.POSFeedbackProcPagoDTO;
import cl.bbr.jumbocl.pedidos.dto.POSProductoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDTO;
import cl.bbr.jumbocl.pedidos.dto.TrxMpDetalleDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.shared.conexion.IRSDataSource;
import cl.bbr.jumbocl.shared.log.Logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;

public class Controlador {
	
	private Logging 		logger 	= new Logging(this);
	private ByteBuffer 		buffer;
	private HeaderPosDTO 	header	= new HeaderPosDTO();
	private C1ReqDTO 		c1req 	= new C1ReqDTO();
	private C1RspDTO 		c1rsp 	= new C1RspDTO();
	private C2ReqDTO 		c2req;
	private String 			respuesta = "";
	
	ResourceBundle rb = ResourceBundle.getBundle("bo");
    
	/**
	 * Constructor de la Clase
	 */
	public Controlador(ByteBuffer pckt){
		
		this.buffer = pckt;
		
	}
	
	//Solo se agrega para realizar las pruebas con junit*****
	//public Controlador(){}

	
	/*
	 * Métodos públicos de la clase
	 * 
	 */
	
	
	/**
	 * Ejecuta método principal
	 * Procesa mensaje, de acuerdo al tipo de mensaje (C1,C2) realiza la 
	 * acción correcta.
	 *
	 */
	public void execute(){

		// Parseamos el header
		parseHeader();
		
		logger.debug("---------HEADER----------");
		logger.debug("Header Recibido:"+header.toMsg());
		logger.debug("----------Requerimiento "+ header.getTipoTrx() +"----------");
		
		/*
		 *  Procesa la data dependiendo del tipo de mensaje
		 *  
		 */
	
		if ( header.getTipoTrx().equals("C1") ){
			
			/*
			 * Tipo de mensaje C1
			 * 
			 */
			
			// parsea la data
			this.parseC1Req();
		
			// llamamos metodo que procesa la respuesta al requerimento
			this.doProcesaC1();//Solo se comenta para revisar con Junit
		
			
			// Seteamos la respuesta
			if ( c1rsp.getCodRsp().equals("000") ){ 
				/*
				 * Respuesta OK
				 * 
				 */
						
				// seteamos el largo de la respuesta (data) en el header
				header.setLargo(c1rsp.getLargo()+"");
			
				// seteamos la salida. (Header + Data)
				this.respuesta = header.toMsg() + c1rsp.toMsg();
				
			}
			else
			{
				/*
				 * Respuesta con Codigo de Error
				 * 
				 */
				
				// seteamos el largo de la respuesta (data) en el header
				header.setLargo(c1rsp.getExcepcionLargo() + "");
			
				// seteamos la salida. (Header + Data)
				this.respuesta = header.toMsg() + c1rsp.toExceptionMsg();
				
			}
				
			
			
		}
		else if ( header.getTipoTrx().equals("C2") ){
			/*
			 * Tipo de mensaje C2
			 * 
			 */
						
			// parsea la data del mensaje
			try {
				this.c2req = this.parseC2Req( buffer );
			} catch (IrsMonException e) {
				logger.error("No se pudo parsear el mensaje C2: " + e.getMessage());
				e.printStackTrace();
			} 
			
			
			// ejecuta método que invoca proceso de negocio
			try {
				this.doProcesaFeedback( c2req );
			} catch (IrsMonException e) {
				logger.error("Error al procesar respuesta C2: " + e.getMessage());
				e.printStackTrace();
			} catch (SystemException e) {
				logger.error("Error de sistema al procesar respuesta C2: " + e.getMessage());
				e.printStackTrace();
			}
			
			
			// seteamos el largo de la respuesta (data) en el header
			header.setLargo("0"); 		// como sólo respondemos con el header, el largo es cero
			
			// seteamos la salida. Header
			this.respuesta = header.toMsg();
			
		}
		else if( header.getTipoTrx().equals("C3") ){
			// Método no implementado	
		}
		

		
	}
	
	
	/**
	 * Obtiene respuesta del mensaje
	 * @return String respuesta
	 */
	public String getRespuesta(){
			
		logger.debug(">>>---------RESPUESTA----------<<<");
		logger.debug("rsp:" + this.respuesta );
		logger.debug(">>>---------FIN RESPUESTA----------<<<");

   	    // String out = "0000000000000000000000000000000000000000000C100000000337000OK                                      00000***********7777081002TBK11536913810Johanna Díaz Vergara               15369138100000000000                                                                                                                                                                         ";
  	    // out += "x#0x#0x#0x#0x#0x#0x#0x#0x#0x#0";
		
		return this.respuesta;
	}
	
	
	/* *******************************************************************************************
	 * Métodos privados de la clase
	 * 
	 */
	
	
	/**
	 * Parsea header del buffer
	 *
	 */
	private void parseHeader(){
		
		// Parsea el Header
		header.setEmpresa	(Utiles.buffer2string(buffer,HeaderPosDTO.EMPRESA_LEN));
		header.setLocal		(Utiles.buffer2string(buffer,HeaderPosDTO.LOCAL_LEN));
		header.setPos		(Utiles.buffer2string(buffer,HeaderPosDTO.POS_LEN));
		header.setBoleta	(Utiles.buffer2string(buffer,HeaderPosDTO.BOLETA_LEN));
		header.setTrxSma	(Utiles.buffer2string(buffer,HeaderPosDTO.TRXSMA_LEN));
		header.setFecha		(Utiles.buffer2string(buffer,HeaderPosDTO.FECHA_LEN));
		header.setHora		(Utiles.buffer2string(buffer,HeaderPosDTO.HORA_LEN));
		header.setOperador	(Utiles.buffer2string(buffer,HeaderPosDTO.OPERADOR_LEN));
		header.setTipoTrx	(Utiles.buffer2string(buffer,HeaderPosDTO.TIPOTRX_LEN));
		header.setVersion	(Utiles.buffer2string(buffer,HeaderPosDTO.VERSION_LEN));
		header.setJournal	(Utiles.buffer2string(buffer,HeaderPosDTO.JOURNAL_LEN));
		header.setLargo		(Utiles.buffer2string(buffer,HeaderPosDTO.LARGO_LEN));
		
		
	}
	
	
	/**
	 * Parsea data del tipo C1Req
	 *
	 */
	private void parseC1Req(){
		
		// Obtenemos data, en este caso es el número de OP
		c1req.setOp(Utiles.buffer2string(buffer,C1ReqDTO.OP_LEN));

		logger.debug("---------DATA----------");
		logger.debug("N°OP:" + c1req.getOp());
		logger.debug("---------END DATA----------");
		
	}
	
	
	/**
	 * Procesa la respuesta de C1 y llena la variable c1rsp
	 *
	 */
	//public void doProcesaC1(long id_trx){//Solo realizar prueba con Junit *****
	private void doProcesaC1(){
		
		PedidoDTO 	pedido 	= null;
		WebpayDTO    wp     = null; 
		BotonPagoDTO bp     = null;
		TrxMpDTO 	trxmp	= null;
		boolean excepciones = false;		
		List facturas 		= new ArrayList();
		List prods 			= new ArrayList();
		long id_pedido 		= -1;
		long id_trx			= -1; //Solo se comenta para revisar con jUnit *****
		
		c1rsp.setCodRsp("000"); // 000 : OK
		c1rsp.setGlosa("OK");
		
		// Parseamos el numero de trxmp como tipo de dato long
		//Solo se comenta l bloque try para revisar con jUnit
		try
		{
			id_trx = Long.parseLong( c1req.getOp() );  //ojo que inicialmente correspondia al numero de pedido
		} catch (NumberFormatException e){
			logger.error("No se pudo parsear el número de TRXOP");
			c1rsp.setCodRsp("001");
			c1rsp.setGlosa("Numero TRXOP malformado");
			excepciones = true;
		}
	
		// Creamos al BizDelegate
		BizDelegate biz = new BizDelegate();

		
		// obtenemos encabezado del pedido
		try {
			
			// obtenemos header de la transacción que se está invocando
			trxmp	= biz.getTrxPagoById( id_trx );

			// obtenemos el pedido relacionado con la transacción		
			pedido 	= biz.getPedidosById( trxmp.getId_pedido() );
			
			id_pedido = pedido.getId_pedido();
			
			// Validamos que el pedido este en estado EN PAGO o PAGO RECHAZADO
			if (pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_EN_PAGO ||
			        pedido.getId_estado() == Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO){
				
			    // Validamos que la trx esté en estado Creada o Rechazada
				if ( trxmp.getId_estado() != Constantes.ID_ESTAD_TRXMP_CREADA
					 && trxmp.getId_estado() != Constantes.ID_ESTAD_TRXMP_RECHAZADA ){
					
					logger.info("No se puede cursar una trx en estado distinto a Creada o a Enbodega");
					c1rsp.setCodRsp("002");
					c1rsp.setGlosa("Trx OP en estado     incorrecto");
					excepciones = true;
				}
			}else{
				logger.info("No se puede cursar el Pedido ya que esta en estado distinto a EN PAGO o PAGO RECHAZADO");
				c1rsp.setCodRsp("002");
				c1rsp.setGlosa("OP en estado  incorrecto");
				excepciones = true;
	
			}
			
			
			
			// Validamos que el pedido esté en estado "En Bodega" //cambiar por trxmp
			/*
			if ( pedido.getId_estado() != Constantes.ID_ESTAD_PEDIDO_EN_BODEGA ){
				logger.info("No se puede cursar el pago de un pedido en estado distinto a En Bodega");
				c1rsp.setCodRsp("002");
				c1rsp.setGlosa("OP en estado incorrecto");
				excepciones = true;
			}
			*/
			
		} catch (IrsMonException e) {
			if ( e.getMessage().matches("(?i).*TRX005.*") ){
				logger.info("Trx N° " + id_trx + " no existe");
				c1rsp.setCodRsp("003");
				c1rsp.setGlosa("Trx OP no existe");
			}
			else if ( e.getMessage().matches("(?i).*OPE001.*") ){
				logger.error("Pedido N° " + id_pedido + " no existe");
				c1rsp.setCodRsp("004");
				c1rsp.setGlosa("Excepción no controlada");
			}
			else
			{
				c1rsp.setCodRsp("004");
				c1rsp.setGlosa("Excepción no Controlada");
				logger.error("Excepción no controlada");
				e.printStackTrace();
			}
			excepciones = true;
		} catch (SystemException e) {
			c1rsp.setCodRsp("005");
			c1rsp.setGlosa("Excepción de Sistema");
			logger.error("SystemException");
			e.printStackTrace();
			excepciones = true;
		}
	
		
		// Si hay excepciones se devuelve C1 tal cual
		if ( excepciones == true ) {
			logger.debug("OP con excepciones...");
			logger.debug("Msg:" + c1rsp.toMsg());
			return; // Fin
		}
		
		logger.debug("Inicio");
			
	
		// Obtenemos listado de facturas (que debiese ser una sola)
		if ( pedido.getTipo_doc().equals("F") ){ //Factura
			try {
				facturas = biz.getFacturasByIdPedido(id_pedido);
				
				if ( facturas.size() == 0 ){
					c1rsp.setCodRsp("005");
					c1rsp.setGlosa("Excepción de Sistema");
					logger.error("El pedido viene con tipo doc Factura, pero no se ha encontrado la información de esta.");
					excepciones = true;
				}
				
				
			} catch (IrsMonException e) {
				c1rsp.setCodRsp("004");
				c1rsp.setGlosa("Excepción no Controlada");
				logger.error("Excepción no controlada");
				e.printStackTrace();
				excepciones = true;
			}
		}
		
		
		
		// Obtenemos productos pickeados del pedido
		
		//prods = biz.getDetPickingByIdPedido(id_pedido);
		//prods = getDetalleProductosPedido(id_pedido); // eliminar el bypass al pasar a producción
		
		List lst_productos = new ArrayList();
				
		
		try {
			lst_productos = biz.getTrxPagoDetalleByIdTrxMp( id_trx );
		} catch (IrsMonException e) {
			c1rsp.setCodRsp("004");
			c1rsp.setGlosa("Excepción no Controlada");
			logger.error("Excepción no controlada");
			e.printStackTrace();
			excepciones = true;
		} catch (SystemException e) {
			c1rsp.setCodRsp("005");
			c1rsp.setGlosa("Excepción de Sistema");
			logger.error("SystemException");
			e.printStackTrace();
			excepciones = true;
		}
		
		
		// Si hay excepciones se devuelve C1 tal cual
		if ( excepciones == true ) {
			logger.debug("OP con excepciones...");
			logger.debug("Msg:" + c1rsp.toMsg());
			return; // Fin
		}
		
		
		// iteramos sobre los productos TrxMpDetalleDTO obtenidos en el biz
		// y lo transformamos a ProductoC1 
		for (int i=0; i<lst_productos.size();i++){
			TrxMpDetalleDTO p2 = (TrxMpDetalleDTO)lst_productos.get(i);
			

			//
			
			ProductoC1 p1 = new ProductoC1();
			
			
			//double precio 		= p2.getPrecio();			//(int)rs.getDouble("precio"); 
			double cant		= p2.getCantidad();				//rs.getDouble("cant_solic");
			boolean pesable = false;
			
			//int monto = (int)(precio * cant);
			int monto = (int) Math.round(p2.getPrecio());// precio;
			
			//String str_valor = monto + "";
			//str_valor = Utiles.addCharToString(str_valor,"0",5,Utiles.ALIGN_LEFT);
			
			
			// detección de pesables
			String cbarra = p2.getCod_barra();				//rs.getString("cod_barra");

			if ( cbarra.substring(0,2).equals("24") && cbarra.length() == 11 ){  // JSE: 20061219
				pesable = false;//RON Santa Teresa
			}else if ( cbarra.substring(0,2).equals("24") && cbarra.length() > 7 ){  // JSE: 20061219
				pesable = true;
			}
			
			
			if( pesable ){ // pesables
				
				p1.setIndicat("1"); // 1: pesable
				
				int entero = (int) Math.floor(cant);
				int gramos = (int) (( cant*1000 - entero*1000 ));
					
				logger.debug("cant: " + cant + "=" + entero + "+" + gramos);
				
				p1.setCantidad(entero+"");
				p1.setDecimal(gramos+"");
			
				// consideramos solo los primeros 7 caracteres del cod barra
				cbarra = cbarra.substring(0,7);	
				
			}
			else // productos normales
			{
				p1.setIndicat("0"); // 0: cantidad
				
				String str_cant = ((int)cant)+"";
				
				p1.setCantidad(str_cant);
				p1.setDecimal("0");
				
				// cortamos el dígito verificador del codigo de barras
				cbarra = cbarra.substring(0,cbarra.length()-1);  
				
			}

			// alineamos campos numéricos a la izquierda y rellenamos con cero 0
			// hasta completar el largo 12
			cbarra = Utiles.addCharToString(cbarra,"0",12,Utiles.ALIGN_LEFT);

						
			p1.setSku(cbarra); 			// Codigo de barra
			p1.setPrecio(monto+"");  	// precio, en realidad es el monto
			

			// Agregamos prod al listado
			prods.add(p1);
			
			
		}
						
		// seteamos el listado de productos a la respuesta		
		c1rsp.setProductos(prods);
		String NumeroUnico = "";
		try{
		    if (!pedido.getNum_mp().trim().equals("") && !pedido.getNum_mp().equals("X")){
		        NumeroUnico = pedido.getId_pedido() + "00";
		    }else{ // SI NUM_MP == X ==> PAGO CON WEBPAY Y BOTON DE PAGO
				if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
					wp = biz.webpayGetPedido(id_pedido);
					NumeroUnico = wp.getTBK_ORDEN_COMPRA()+"";
				}else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_CAT)){
					bp = biz.botonPagoGetByPedido(id_pedido);
					NumeroUnico = bp.getIdCatPedido()+"";
				}
		    }
		    
			
		}catch (SystemException e){
			e.printStackTrace();
		}catch (IrsMonException e){
			e.printStackTrace();
		}
		
		//logger.debug("getNum_mp    : " + pedido.getNum_mp()); //_unmask
		//logger.debug("4 Utl. Dig.  : " + pedido.getNum_mp().substring(pedido.getNum_mp().length() - 4));
		//logger.debug("4 Utl. Dig.  : " + wp.getTBK_FINAL_NUMERO_TARJETA());
		//logger.debug("getFecha_exp : " + pedido.getFecha_exp());
		//logger.debug("getN_cuotas  : " + wp.getTBK_NUMERO_CUOTAS());
		logger.debug("getMedio_pago: " + pedido.getMedio_pago());				
		logger.debug("getDv_tit    : " + pedido.getDv_tit());
		
		logger.debug("getRut_tit: " + pedido.getRut_tit());
		logger.debug("getNom_tit: " + pedido.getNom_tit());
		logger.debug("getApat_tit: " + pedido.getApat_tit());
		logger.debug("getAmat_tit: " + pedido.getAmat_tit());
		
		
		// llenamos c1 con los datos del pedido
			
		c1rsp.setFpNumeroUnico(NumeroUnico);
		//c1rsp.setFp_4_ultimos_digitos_tarj(pedido.getNum_mp().substring(pedido.getNum_mp().length() - 4));
		//c1rsp.setFpCuotas(pedido.getN_cuotas()+"");
		
		if (!pedido.getNum_mp().trim().equals("") && !pedido.getNum_mp().equals("X")){
		    c1rsp.setFp_4_ultimos_digitos_tarj(pedido.getNum_mp().substring(pedido.getNum_mp().length() - 4));
		    c1rsp.setFpCuotas(pedido.getN_cuotas()+"");
	    }else{// SI NUM_MP == X ==> PAGO CON WEBPAY Y BOTON DE PAGO
			if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
				c1rsp.setFp_4_ultimos_digitos_tarj(wp.getTBK_FINAL_NUMERO_TARJETA());
				c1rsp.setFpCuotas(wp.getTBK_NUMERO_CUOTAS()+"");
			}else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_CAT)){
			    if (bp.getNroTarjeta().equals("*****")){
			        c1rsp.setFp_4_ultimos_digitos_tarj("0000");
			    }else{
			        c1rsp.setFp_4_ultimos_digitos_tarj(bp.getNroTarjeta().substring(bp.getNroTarjeta().length() - 4));
			    }
				//c1rsp.setFp_4_ultimos_digitos_tarj(bp.getNroTarjeta().substring(bp.getNroTarjeta().length() - 4));
				c1rsp.setFpCuotas(bp.getNroCuotas()+"");
			}
	    }
		logger.debug("4 Utl. Dig.  : " + c1rsp.getFp_4_ultimos_digitos_tarj());
		logger.debug("getN_cuotas  : " + c1rsp.getFpCuotas());
		
		
		c1rsp.setFpServicio(pedido.getMedio_pago());  //TBK, CAT
		
		// Regla: 0 es sin cuota y mayor que cero es con cuota;
		if (!pedido.getNum_mp().trim().equals("") && !pedido.getNum_mp().equals("X")){
			if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_CAT) ||
					pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
			    if ( pedido.getN_cuotas() == 0 )
				    c1rsp.setFpTipoCuota("0"); // 0 : sin cuotas
			    else
				    c1rsp.setFpTipoCuota("1"); // 1: cuota fija
	        }else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_PARIS)){
			    if ( pedido.getN_cuotas() == 1 )
				    c1rsp.setFpTipoCuota("0"); // 0 : sin cuotas
			    else
				    c1rsp.setFpTipoCuota("1"); // 1: cuota fija
	        }
		}else{// SI NUM_MP == X ==> PAGO CON WEBPAY Y BOTON DE PAGO
		    if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_CAT)){
			    if (bp.getNroCuotas() == Integer.getInteger("0") )
				    c1rsp.setFpTipoCuota("0"); // 0 : sin cuotas
			    else
				    c1rsp.setFpTipoCuota("1"); // 1: cuota fija		        
		    }else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
			    if ( wp.getTBK_NUMERO_CUOTAS() == 0 )
				    c1rsp.setFpTipoCuota("0"); // 0 : sin cuotas
			    else
				    c1rsp.setFpTipoCuota("1"); // 1: cuota fija
	        }
		}
        
        
		String dv = pedido.getDv_cliente();
		
		if (dv.equals("k") || dv.equals("K"))
			dv = "10";
		else
			dv = "0" + dv;  // dv=1 -> 01
		
		// rut del comprador
		c1rsp.setFpRut( pedido.getRut_cliente() + dv );
		
		//c1rsp.setPedidoId((int)pedido.getId_pedido());
		
		// nombre del comprador
		c1rsp.setFpNombre(pedido.getNom_cliente());

		//PUNTOS RUT
		String rutAcumulaPuntos="0000000000";
		int maxPuntosRut=50000000;

		try {
			ParametrosService ps = new ParametrosService();
			ParametroDTO parRutAcumulaPuntos = ps.getParametroByName("LISTENER_POS_DEFAULT_RUT");
			ParametroDTO parMaxPuntosRut = ps.getParametroByName("LISTENER_POS_MAX_RUT_PUNTOS");
			rutAcumulaPuntos = parRutAcumulaPuntos.getValor();
			maxPuntosRut=Integer.parseInt(parMaxPuntosRut.getValor());
		} catch (Exception e) {
			// TODO Bloque catch generado automáticamente
			logger.info(e.getMessage());
		}		
		
		if ( pedido.getTipo_doc().equals("F") ){//SI ES FACTURA
			FacturaDTO oFactura = pedido.getFactura();
			
			if(pedido.getRut_cliente() != oFactura.getRut() && oFactura.getRut() <= maxPuntosRut){
				String dvFactura=oFactura.getDv();
				if (dvFactura.equals("k") || dvFactura.equals("K"))
					dvFactura = "10";
				else
					dvFactura = "0" + dvFactura;  // dv=1 -> 01
				
				rutAcumulaPuntos = String.valueOf(oFactura.getRut()) +dvFactura;
			}else if (pedido.getRut_cliente() == oFactura.getRut() && pedido.getRut_cliente() <= maxPuntosRut){
				rutAcumulaPuntos =  String.valueOf(pedido.getRut_cliente()) + dv;					
			}
		}else{//SI ES BOLETA
			if (pedido.getRut_cliente() <= maxPuntosRut){
				rutAcumulaPuntos =  String.valueOf(pedido.getRut_cliente()) + dv;				
			}
		}
		
		c1rsp.setPuntos_rut(rutAcumulaPuntos.trim());
		
		// En caso de tratarse de una Factura
		if ( pedido.getTipo_doc().equals("F") ){
						
			FacturaDTO fac = (FacturaDTO)facturas.get(0);
			
			// Información de factura (en caso que aplique)
			
			dv = fac.getDv();
			
			if (dv.equals("k") || dv.equals("K"))
				dv = "10";
			else
				dv = "0" + dv;  // dv=1 -> 01
			
			c1rsp.setFcRut(fac.getRut() + dv);
			c1rsp.setFcNombre(fac.getRazon());
			c1rsp.setFcDireccion(fac.getDireccion());
			c1rsp.setFcTelefono(fac.getFono());
			c1rsp.setFcGiro(fac.getGiro());
			c1rsp.setFcComuna(fac.getComuna());
			c1rsp.setFcCiudad(fac.getCiudad());
		}
		
		//ASIGNA NUMERO DE COMERCIO
		String numComercio = "";
		
		if ((!pedido.getNum_mp().trim().equals("") && !pedido.getNum_mp().equals("X")) ||
		        pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO)){
		    c1rsp.setFpCodComercio("0");
		    c1rsp.setFpSecuenciaOperTBK_X("0");
		    c1rsp.setFpSecuenciaOperTBK_Y("0");
		}else{// SI NUM_MP == X ==> PAGO CON WEBPAY Y BOTON DE PAGO
			if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_CAT)){
			    //Fono Compras Clientes
				if (pedido.getId_usuario_fono() != 0 && pedido.getOrigen().equals("W")){
					numComercio = rb.getString("CAT.numComercioFOCliFono");
				//Venta Empresas
				}else if (pedido.getOrigen().equals("V")){
					numComercio = rb.getString("CAT.numComercioFOVE");
				//Compra realizada por el Cliente
				}else if (pedido.getId_usuario_fono() == 0 && pedido.getOrigen().equals("W")){
					numComercio = rb.getString("CAT.numComercioFOCliente");
				}
				//numComercio = rb.getString("CAT.numComercio");
			    c1rsp.setFpCodComercio(numComercio);
			    c1rsp.setFpSecuenciaOperTBK_X("0");
			    c1rsp.setFpSecuenciaOperTBK_Y("0");
			}else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
				if (pedido.getId_usuario_fono() != 0 && pedido.getOrigen().equals("V")){
					numComercio = rb.getString("TBK.numComercioNoAutenticado");
				}else{
					numComercio = rb.getString("TBK.numComercioAutenticado");
				}
				
			    c1rsp.setFpCodComercio(numComercio);
			    //c1rsp.setFpSecuenciaOperTBK_X(trxmp.getTbk_secuencia_x()+"");
			    //c1rsp.setFpSecuenciaOperTBK_Y(trxmp.getTbk_secuencia_y()+"");
			    c1rsp.setFpSecuenciaOperTBK_X(pedido.getTbk_secuencia_x()+"");
			    c1rsp.setFpSecuenciaOperTBK_Y(pedido.getTbk_secuencia_y()+"");
			}/*else if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_LINEA_CREDITO)){
			    c1rsp.setFpCodComercio("0");
			    c1rsp.setFpSecuenciaOperTBK_X("0");
			    c1rsp.setFpSecuenciaOperTBK_Y("0");
			}*/
		}
		

		logger.debug("msg:" + c1rsp.toMsg());
		
		// escribe archivo
		//writetext(id_pedido, c1rsp.toMsgPosTest());
		
		
		logger.debug("Fin Rutina");
		
	}


	
	/**
	 * OJO No utilizar este metodo
	 * Obtiene productos pickeados de la ronda (asumiendo) que todo lo que se
	 * pidió fue pickeado
	 * Esta función es sólo para pruebas y debe ser eliminada en producción
	 * @return List ProductoC1
	 */
	public List getDetalleProductosPedido(long id_pedido){

		Connection con = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

 	   String SQL1 = 
         	" select dp.id_detalle as id_detalle, dp.id_producto as id_producto, " + 
		    " dp.id_pedido as id_pedido, max(cb.cod_barra) as cod_barra, " +
		    " cb.unid_med as unid_med, dp.descripcion as descripcion, dp.precio as precio, " +  
		    " dp.cant_solic as cant_solic " +
 		   	" from bo_detalle_pedido dp " +
			" left join bo_codbarra cb on cb.id_producto = dp.id_producto " +
			" where dp.id_pedido = " + id_pedido +
			" group by dp.id_detalle, dp.id_producto,  dp.id_pedido, cb.unid_med, dp.descripcion, " +
			" dp.precio, dp.cant_solic "
 		   	;
 	   
 	   
	   logger.debug("SQL:" + SQL1);
		
	   List list_pickeados = new ArrayList();
	   
	   
	   
	       try {
	            con = IRSDataSource.getSingleton().getConnection();
				stm = con.prepareStatement( SQL1 );
				rs = stm.executeQuery();

				while (rs.next()) {
					
					ProductoC1 p1 = new ProductoC1();
			
					
					int precio 	= (int)rs.getDouble("precio"); 
					double cant		= rs.getDouble("cant_solic");
					boolean pesable = false;
					
					//int monto = (int)(precio * cant);
					int monto = (int) precio;
					
					//String str_valor = monto + "";
					//str_valor = Utiles.addCharToString(str_valor,"0",5,Utiles.ALIGN_LEFT);
					
					
					// detección de pesables
					String cbarra = rs.getString("cod_barra");
					
					if ( cbarra.substring(0,2).equals("24") && cbarra.length() == 8 ){
						pesable = true;
					}
					
					
					if( pesable ){ // pesables
						
						//cbarra = cbarra.substring(0,cbarra.length()-1);  //se corta dv
						//cbarra += str_valor;		// concatenamos el supuesto "precio de venta"
						
						p1.setIndicat("1"); // 1: pesable

						
						int entero = (int) Math.floor(cant);
						int gramos = (int) (( cant*1000 - entero*1000 ));
							
							
						logger.debug("cant: " + cant + "=" + entero + "+" + gramos);
						//logger.debug( "parte entera: " + entero);
						//logger.debug( "gramos: " + gramos );
					
						
						
						p1.setCantidad(entero+"");
						p1.setDecimal(gramos+"");
					
						
						
					}
					else // productos normales
					{
						/*
						if ( cbarra.length() == 13 ){             //Si es EAN13
							cbarra = cbarra.substring(0,12);	  //cortamos el dv
						}
						*/
						p1.setIndicat("0"); // 0: cantidad
						
						String str_cant = ((int)cant)+"";
						
						p1.setCantidad(str_cant);
						p1.setDecimal("0");
						
						
					}

					
					// alineamos campos numéricos a la izquierda y rellenamos con cero 0
					//cbarra = Utiles.addCharToString(cbarra,"0",12,Utiles.ALIGN_LEFT);
					
					cbarra = cbarra.substring(0,cbarra.length()-1);  //se corta dv
					
					p1.setSku(cbarra); 				// Codigo de barra, va sin sin dv
					p1.setPrecio(monto+"");  	// precio, en realidad es el monto
					

					// Agregamos prod al listado
					list_pickeados.add(p1);
					
					
				}
				
				
				
	        } catch (SQLException e){
	        	System.out.println("SQLException --> " + e.getMessage());
	        } catch(Exception e) {
	            e.printStackTrace();
	            System.out.println("Exception --> " + e.getMessage());
	            logger.debug("Error");
	        } finally {
	            try { rs.close(); } catch(Exception e) { logger.error("error al cerrar rs"); }
	            try { stm.close(); } catch(Exception e) { logger.error("error al cerrar stm"); }
	            try { con.close(); } catch(Exception e) { logger.error("error al cerrar con"); }
	        }

	        
	        return list_pickeados;
	        
	        
	}
	
	
	/**
	 * Parsea buffer de la data y lo deja en un objeto DTO
	 * @param buff
	 * @return C2ReqDTO
	 * @throws IrsMonException 
	 */
	private C2ReqDTO parseC2Req( ByteBuffer buff ) throws IrsMonException {
		
		C2ReqDTO req = new C2ReqDTO();
		List productos = new ArrayList();
		
		// largo de la data
		int largo_data = Integer.parseInt(header.getLargo());
		
		// largo fijo data
		int largo_fijo_data = C2ReqDTO.getLargoFijoData(); 
		
		// largo del segmento de productos empaquetados
		int largo_variable = largo_data - largo_fijo_data;
		
		// largo del segmento que viene en la data variable
		final int largo_segmento = ProductoC2.getLargo(); 
		
		// número de segmentos que viene en la data variable
		int num_segmentos = largo_variable / largo_segmento;
		
		// duplicamos el buffer para logearlo
		ByteBuffer buff2 = null;
		buff2 = buff.duplicate();
		
		logger.debug("Msg C2 Recibido: " + Utiles.buffer2string(buff2,largo_data));
		logger.debug("------------------------------------------------------------------");

		// seteamos data fija
		req.setFeedBack	(Utiles.buffer2string(buff,C2ReqDTO.FEEDBACK_LEN));
		req.setOp		(Utiles.buffer2string(buff,C2ReqDTO.OP_LEN));
		req.setMonto_op	(Utiles.buffer2string(buff,C2ReqDTO.MONTO_OP_LEN));
		req.setFp_error	(Utiles.buffer2string(buff,C2ReqDTO.FPAGO_ERROR));
		req.setFp_glosa	(Utiles.buffer2string(buff,C2ReqDTO.FPAGO_GLOSA_LEN));
		
		// logger
		logger.debug("Parseando mensaje C2 Req...");
		logger.debug("Cod Feedback: " 	+ req.getFeedBack() );
		logger.debug("Cod OP: " 		+ req.getOp() );
		logger.debug("Monto: " 			+ req.getMonto_op() );
		logger.debug("Fp Error: " 		+ req.getFp_error() );
		logger.debug("Fp glosa: " 		+ req.getFp_glosa() );
		
		
		// seteamos data variable
		for (int i=0; i<num_segmentos; i++){
			
			/*
			String strpack 		= "";
			String strunpack 	= "";
			
			// obtiene 7 bytes empaquetados
			strpack = Utiles.buffer2string(buff,largo_segmento);
			
			// creamos arreglo de bytes para meter el string empaquetado
			byte[] paquete = new byte[largo_segmento];
			
			try {
				// agregamos string de buffer al arreglo de bytes
				Utiles.addStringToByteArray(paquete,0,strpack);
				
				// desempaquetamos el arreglo de bytes
				strunpack = Utiles.unpack(paquete,0,7);
				
				logger.debug("Unpack: " + strunpack);
				
			} catch (Exception e) {
				logger.error("No se pudo desempaquetar productos: " + e.getMessage());
				throw new IrsMonException(e);
			}
			
			ProductoC2 prod = new ProductoC2();
			
			prod.setSku(strunpack.substring(0,ProductoC2.SKU_LEN));
			prod.setRet(strunpack.substring(ProductoC2.getLargo()-ProductoC2.RET_LEN,ProductoC2.getLargo()));
			*/
			
			ProductoC2 prod = new ProductoC2();
			
			prod.setSku( Utiles.buffer2string(buff,ProductoC2.SKU_LEN) );
			prod.setRet( Utiles.buffer2string(buff,ProductoC2.RET_LEN) );
		
			
			logger.debug("sku: " + prod.getSku());
			logger.debug("cod: " + prod.getRet());			
			
			// agregamos al listado de productos
			productos.add(prod);
			
		} // for
		
		
		// seteamos listado de productos
		req.setProductos(productos);
		
		// retornamos objeto
		return req;
	}

	
	
	/**
	 * Procesa Feedback del POS
	 * @param req
	 * @throws IrsMonException
	 * @throws SystemException
	 */
	private void doProcesaFeedback(C2ReqDTO req) throws IrsMonException, SystemException{
		
		long 	id_trxmp;
		int		monto_op;
		
		id_trxmp 	= Long.parseLong( req.getOp() );
		monto_op 	= Integer.parseInt( req.getMonto_op() );
	
		int num_pos 	= 0;
		int num_boleta 	= 0;
		int num_sma		= 0;
		
		try {
			num_pos 	= Integer.parseInt( header.getPos() );
			num_boleta	= Integer.parseInt( header.getBoleta() );
			num_sma		= Integer.parseInt( header.getTrxSma() );
		} catch(Exception e) {
			logger.error("Error al convertir numero de pos a int, se asigna valor=0 en algunos datos...");
		}
		
		
		// Creamos al bizdelegate
		BizDelegate biz = new BizDelegate();
	
		// Creamos DTO para invocar metodo de negocio
		POSFeedbackProcPagoDTO fback = new POSFeedbackProcPagoDTO();
		
		// seteamos los campos fijos
		fback.setId_trxmp		( id_trxmp );
		fback.setCod_feedback	( req.getFeedBack() );
		fback.setMonto_pagado	( monto_op );
		fback.setFp_error		( req.getFp_error() );
		fback.setFp_glosa		( req.getFp_glosa() );
		fback.setNum_pos		( num_pos );
		fback.setNum_boleta		( num_boleta );
		fback.setNum_sma		( num_sma );
		
		// Agregado JSE: 20061219
		fback.setFecha			( header.getFecha() );
		fback.setHora			( header.getHora() );
		
		
		
		// seteamos los campos variables
		List lst_prods = new ArrayList();
		for( int i=0; i<req.getProductos().size(); i++){
			ProductoC2 p1 = (ProductoC2)req.getProductos().get(i);
			
			POSProductoDTO p2 = new POSProductoDTO();
			p2.setCbarra( p1.getSku() );
			p2.setCod_ret( p1.getRet() );
			
			lst_prods.add( p2 );
		}
		
		// seteamos listado de productos
		fback.setProds( lst_prods );
		
		// invocamos método de negocio
		biz.doProcesaFeedbackPOS( fback );
		
		
		
	}
	
	
	
	
	
	
	/**
	 * Escribe mensaje en archivo de texto
	 * Esta función debe ser borrada en producción, ya que se utilizó
	 * durante el periodo de desarrollo y pruebas iniciales
	 * @param id_pedido
	 * @param msg
	 */
	private static void writetext(long id_pedido, String msg) {
		
		String n_op = id_pedido + "";
		int len = n_op.length();
		
		n_op = Utiles.addCharToString(n_op,"0",10,Utiles.ALIGN_LEFT);
		
		try {
			FileWriter outFile = new FileWriter("C:/tmp/"+n_op+".txt");
			PrintWriter out = new PrintWriter(outFile);

			// Also could be written as follows on one line
			// Printwriter out = new PrintWriter(new FileWriter(args[0]));

			// Write text to file
			out.print(msg);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	/* Setters y Getters */
	
	public HeaderPosDTO getHeader() {
		return header;
	}

	public void setHeader(HeaderPosDTO header) {
		this.header = header;
	}	
	
	
	
}