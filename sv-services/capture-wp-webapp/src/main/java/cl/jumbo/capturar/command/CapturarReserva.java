/*
 * Creado el 05-11-2010
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.jumbo.capturar.command;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TimerTask;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ServiceException;
import cl.bbr.jumbocl.pedidos.dao.DAOFactory;
import cl.bbr.jumbocl.pedidos.dao.JdbcPedidosDAO;
import cl.bbr.jumbocl.pedidos.dto.LogPedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.exceptions.PedidosDAOException;
import cl.bbr.jumbocl.pedidos.util.FormatUtils;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.capturar.bizdelegate.BizDelegate;
import cl.jumbo.capturar.exceptions.FuncionalException;
import cl.jumbo.capturar.utils.ClienteSwitch;

import com.transbank.capturatrx.ws.BackendGatewayServerProxy;
import com.transbank.capturatrx.ws.TrxInput;
import com.transbank.capturatrx.ws.TrxReturn;


/**
 * @author rsuarezr
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class CapturarReserva extends TimerTask {

	//private String cod_comercio = "";
	protected Logging logger = new Logging(this);
	ResourceBundle rb = ResourceBundle.getBundle("config");
	
	/* (sin Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	public void run() {
		BizDelegate biz = null;
		long id_pedido = 0;
		try {	
			biz = new BizDelegate();
			//Obtener el codigo de comercio
			//getCodComercio();
			//logger.debug("cod_comercio = "+cod_comercio);
			//Obtener el conjunto de OP que seran procesadas para la captura del Webpay
			actualizarCompraWeb(biz, "FALSE");

			JdbcPedidosDAO dao1 = (JdbcPedidosDAO) DAOFactory.getDAOFactory(DAOFactory.JDBC).getPedidosDAO();

			HashMap lst_op = biz.obtenerLstOPByTBK();
			if (lst_op.size() > 0){
			    logger.info("*****  INICIA LA CAPTURA DE RESERVA. ******");
			    logger.debug("SE REVISARAN "+lst_op.size()+ " ORDENES.");
			}
			
			LogPedidoDTO log2 = null;
			//int nro_intentos = 0;

			Iterator it = lst_op.keySet().iterator();
			while (it.hasNext()) {
			    // Get key
			    String idPedido = it.next().toString();
			    id_pedido = Long.parseLong(idPedido);			    
			    //long MontoCapturaOP = Long.parseLong((String)(lst_op.get(idPedido)));
			    long MontoCapturaOP = ((Long)(lst_op.get(idPedido))).longValue();
			    //logger.debug("MontoCapturaOP: "+MontoCapturaOP);
			    logger.debug("idPedido: "+idPedido + ", MontoCapturaOP:"+MontoCapturaOP);
			    
				PedidoDTO pedido = dao1.getPedidoById( id_pedido );

				String numComercio="";
				if (pedido.getId_usuario_fono() != 0 || pedido.getOrigen().equals("V")){
					numComercio = rb.getString("TBK.numComercioNoAutenticado").substring(4);
				}else{
					numComercio = rb.getString("TBK.numComercioAutenticado").substring(4);
				}

				//Obtener informacion de webpay
				//logger.debug("("+id_pedido+") Obtener datos Webpay");
				WebpayDTO webpay = biz.getWebpayByOP(pedido.getId_pedido());
				//enviar mensaje
				//String xml_captura = "";
				
				if ( webpay != null ) {
					
					//logger.debug("("+id_pedido+") Enviar captura de OP");
					//String xml_rpta = enviar_captura(pedido, webpay);
					String xml_rpta = "";//enviar_captura_ws(MontoCapturaOP, webpay, numComercio);
                    
                    String msgTbk = "3||Captura ya fue Realizada para esta Autorizacion";
					
					boolean envio_captura = false;
					if ( xml_rpta.startsWith("0") ) {
                        envio_captura = true;
                    }
                    if ( msgTbk.equalsIgnoreCase( xml_rpta ) ) {
                        logger.info("("+id_pedido+") Ya estaba capturada:" + msgTbk);
                        envio_captura = true;
                    }
					//logger.debug("("+id_pedido+") Devuelve: "+xml_rpta+", envio_captura:"+envio_captura);
					if ( envio_captura ) {
	                    //actualizar flag de captura webpay a n
	                    biz.setModFlagWebpayByOP(id_pedido, "F");
                        
                        if ( !msgTbk.equalsIgnoreCase( xml_rpta ) ) {						
                            // en caso que la captura fue exitosa, actualizar op 
                            ClienteSwitch cliSwitch = new ClienteSwitch();
                            if (cliSwitch.insertaTrxSwitch(id_pedido)) {
                                logger.info("("+id_pedido+") Registro Insertado Correctamente");
                            } else {
                                logger.info("("+id_pedido+") Error en la Inserción del Registro");
                            }                        
                        }
                        
	                    //***************************************************
	                    // cambiamos el estado de la OP a Pagado
	                    biz.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGADO);

	                    logger.info("("+id_pedido+") Cambio estado pedido a Pagado");

	                    // agregamos al log
	                    log2 = new LogPedidoDTO();
	                    log2.setId_pedido(id_pedido);
	                    log2.setUsuario("SYSTEM");
	                    log2.setLog("OP cambia estado a Pagada");

	                    try { 
	                        biz.addLogPedido(log2);
	                    } catch (Exception e) {
	                        logger.info("("+id_pedido+") No se pudo agregar información al log del pedido");
	                    }
	                    
					} else if ( !envio_captura  ) {
						biz.setModFlagWebpayByOP(id_pedido, "R");
						//No se realizo la captura del pago, se cambia op a pago rechazado
			            // Cambia estado de la transaccion a Rechazada
			            /*try {
			                biz.setModEstadoTrxOP(id_pedido, Constantes.ID_ESTAD_TRXMP_RECHAZADA);
			            } catch (Exception e) {
			                logger.error("Error al modificar trx de pago pago");
			                //throw new SystemException(e);
			            }*/

			            // Cambia estado del pedido a Pago Rechazado
			            try {
			                biz.setModEstadoPedido(id_pedido, Constantes.ID_ESTAD_PEDIDO_PAGO_RECHAZADO);
			            } catch (Exception e) {
			                logger.error("("+id_pedido+") Error al modificar estado pedido");
			                //throw new SystemException(e);
			            }
						
	                    // registrar errores de capturar en el log
	                    log2 = new LogPedidoDTO();
	                    log2.setId_pedido(id_pedido);
	                    log2.setUsuario("SYSTEM");
	                    log2.setLog("Problemas en la captura del pago. OP pasa a Pago rechazado.");
	                    try {
	                        biz.addLogPedido(log2);
	                        log2.setLog("Error de TBK:"+xml_rpta);
	                        biz.addLogPedido(log2);
	                    } catch (Exception e) {
	                        logger.info("("+id_pedido+") No se pudo agregar información al log del pedido");
	                    }
					}
				}
			}//fin del for
			if (lst_op.size() > 0){
			    logger.info("***** Finaliza la captura de reserva. ******");
			}
			//Thread.sleep(3000);
		} catch (ServiceException e) {			
			logger.error("ServiceException!." + "("+id_pedido+") Enviar captura de OP" + e.getMessage());
		} catch (PedidosDAOException e){
			logger.error("PedidosDAOException!." + "("+id_pedido+") Enviar captura de OP" + e.getMessage());
		} catch (Exception e) {
			logger.error("ServiceException!." + "("+id_pedido+") Enviar captura de OP" + e);
		}finally{
			actualizarCompraWeb(biz, "TRUE");
		}
      		
		//logger.info("* A terminar pedidos");
        //TerminaCompra t = new TerminaCompra();
        //t.terminaComprasIncompletas(biz,rb);
		//logger.info("*** FIN ***");
		
	}
	
	private void actualizarCompraWeb(BizDelegate biz, String valor){
   	 try {
			biz.actualizaParametroByName("CAPTURA_WEB", valor);
		} catch (FuncionalException e) {		
			 logger.error("Error Actualizar (CAPTURA_WEB), " + e);
		}	
   }
	
	/**
	 * @return
	 */
	/*private void getCodComercio() {
		
		try {
			java.io.InputStream log4jProps = this
			.getClass()
			.getClassLoader()
			.getResourceAsStream("conexion.properties");
			Properties prop = new Properties();
			try {
				prop.load(log4jProps);
			} catch (IOException e) {
				e.printStackTrace();
			}
			

			//cod_comercio = prop.getProperty("cod.comercio");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Error en prop:"+e.getMessage());
		}
		// TODO Apéndice de método generado automáticamente

	}*/
	/**
	 * @param pedido
	 * @param webpay
	 * @return
	 */
	private String enviar_captura_ws(long MontoCapruta, WebpayDTO webpay, String numComercio) {

		String rpta = "";
		try {
			// CONFIGURA CERTIFICADOS DE SEGURIDAD CON TRANSBANK
			logger.debug("Se realizo el System.setProperty()");
//          CERTIFICADOS USADOS PARA LA CONEXIÓN CONTRA AMBIENTE DE QA DE TBK DESDE ALMENDRO DMZ
  			/*System.setProperty("com.ibm.ssl.performURLHostNameVerification", "false");
			System.setProperty("com.ibm.ssl.contextProvider","IBMJSSE2");
			System.setProperty("com.ibm.ssl.protocol","TLS");
			System.setProperty("com.ibm.ssl.keyStoreType","JKS");
			System.setProperty("com.ibm.ssl.keyStore","/u02/WebSphere/AppServer/profiles/default/etc/client.jks");
			System.setProperty("com.ibm.ssl.keyStorePassword","tbktbk09");
			System.setProperty("javax.net.ssl.keyStoreType","JKS");
			System.setProperty("javax.net.ssl.keyStore","/u02/WebSphere/AppServer/profiles/default/etc/client.jks");
			System.setProperty("javax.net.ssl.keyStorePassword","tbktbk09");
			
			System.setProperty("com.ibm.ssl.trustStoreType","JKS");
			System.setProperty("com.ibm.ssl.trustStore","/u02/WebSphere/AppServer/profiles/default/etc/ca.jks");
			System.setProperty("com.ibm.ssl.trustStorePassword","tbktbk09");
			System.setProperty("javax.net.ssl.trustStoreType","JKS");
			System.setProperty("javax.net.ssl.trustStore","/u02/WebSphere/AppServer/profiles/default/etc/ca.jks");
			System.setProperty("javax.net.ssl.trustStorePassword","tbktbk09");*/

			
			// CERTIFICADOS PARA PRODUCCIÓN
			System.setProperty("com.ibm.ssl.performURLHostNameVerification", "false");
			System.setProperty("com.ibm.ssl.contextProvider","IBMJSSE2");
			System.setProperty("com.ibm.ssl.protocol","SSLv2");
			System.setProperty("com.ibm.ssl.keyStoreType","JKS");
			System.setProperty("com.ibm.ssl.keyStore","/u02/WebSphere/AppServer/profiles/default/etc/jumbocl.jks");
			System.setProperty("com.ibm.ssl.keyStorePassword","jumbocl");
			System.setProperty("javax.net.ssl.keyStoreType","JKS");
			System.setProperty("javax.net.ssl.keyStore","/u02/WebSphere/AppServer/profiles/default/etc/jumbocl.jks");
			System.setProperty("javax.net.ssl.keyStorePassword","jumbocl");
			
			System.setProperty("com.ibm.ssl.trustStoreType","JKS");
			System.setProperty("com.ibm.ssl.trustStore","/u02/WebSphere/AppServer/profiles/default/etc/catbk.jks");
			System.setProperty("com.ibm.ssl.trustStorePassword","jumbocl");
			System.setProperty("javax.net.ssl.trustStoreType","JKS");
			System.setProperty("javax.net.ssl.trustStore","/u02/WebSphere/AppServer/profiles/default/etc/catbk.jks");
			System.setProperty("javax.net.ssl.trustStorePassword","jumbocl");
            
			
			logger.debug("Se realizo el System.setProperty()");
			//ver si se puede ejeuctar via wen service
			BackendGatewayServerProxy srv = new BackendGatewayServerProxy();
			srv.setEndpoint(rb.getString("TBK.URL_WS"));
			//colocar datos hacia webpay
			
			String OrdenPedido = FormatUtils.addCharToString(webpay.getTBK_ORDEN_COMPRA()+"","0",26,FormatUtils.ALIGN_RIGHT);
			
			logger.debug("\nDATOS ENVIADOS A TBK:\n"
					   + "=====================\n"
					   + "codComercio: " + numComercio + "\n"
					   + "codAut     : " + webpay.getTBK_CODIGO_AUTORIZACION().trim()+ "\n"
					   + "fec_aut    : " + webpay.getTBK_FECHA_TRANSACCION() + "\n"
				       + "finTarj    : " + webpay.getTBK_FINAL_NUMERO_TARJETA() + "\n"
					   + "monto      : " + MontoCapruta + "\n"
					   + "OrdenPedido: " + OrdenPedido);
					//", orden :"+FormatUtils.completaCadena(String.valueOf(webpay.getTBK_ORDEN_COMPRA()),'0',26-String.valueOf(webpay.getTBK_ORDEN_COMPRA()).length(),'I'));
			TrxInput trx = new TrxInput();
			trx.setCodAut(webpay.getTBK_CODIGO_AUTORIZACION().trim());
			trx.setCodCred(numComercio);
			trx.setFechaAut(webpay.getTBK_FECHA_TRANSACCION());
			trx.setFinTarjeta(webpay.getTBK_FINAL_NUMERO_TARJETA());
			trx.setMontoCaptura(Double.parseDouble(MontoCapruta + ""));
			/*
			//TODO:CONSULTA SOLO PARA PRUEBA DIRECTA CON TBK
			//trx.setOrdenPedido(FormatUtils.completaCadena(webpay.getTBK_VCI(),'0',26-webpay.getTBK_VCI().length(),'I'));
			*/
			//trx.setOrdenPedido(FormatUtils.completaCadena(String.valueOf(webpay.getTBK_ORDEN_COMPRA()),'0',26-String.valueOf(webpay.getTBK_ORDEN_COMPRA()).length(),'I'));
			trx.setOrdenPedido(OrdenPedido);

			//invocar al webservice
			TrxReturn res =  srv.capturaDiferida(trx);
			//obtener la descripcion de error
			rpta = res.getCodErr()+"||"+res.getDesErr();
			logger.debug("Rpta TBK:"+res.getCodErr()+", decripcion:"+res.getDesErr());
		} catch (RemoteException e) {
			//problemas al invocar al webservice
			logger.debug("Error RMI : "+e.getMessage());
		} catch (Exception e) {
			logger.debug("Error general : "+e.getMessage());
		}
		
		return rpta;
		
	}

}
