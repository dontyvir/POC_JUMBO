
package cl.jumbo.capturar.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.pedidos.dto.PedidoDTO;
import cl.bbr.jumbocl.pedidos.dto.WebpayDTO;
import cl.bbr.jumbocl.pedidos.service.PedidosService;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.capturar.dto.MsgReqDTO;
import cl.jumbo.capturar.dto.MsgRspDTO;
import cl.jumbo.capturar.dto.MsgSwitchDTO;




public class ClienteSwitch {
	
	PedidoDTO pedido = null;
	long montoTotal = 0L;
	private MsgSwitchDTO ms = new MsgSwitchDTO();
	private MsgReqDTO msgReq = new MsgReqDTO();
	private MsgRspDTO msgRsp = new MsgRspDTO();
	
	public SocketChannel client = null;
	public InetSocketAddress isa = null;
	public ByteBuffer bufpru;
	Logging logger = new Logging(this);	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");

	/**
	 * Objeto con archivo de propiedades para Switch Server Transbank.
	 */
	//private static Properties properties;
	
	/**
	 * direccion archivo de propiedads
	 */
	//static final String PROPERTIES_FILE = "clientejumbocl.properties";
	//static final String CONFIG_BUNDLE_NAME = "clientejumbocl";
	//static PropertyResourceBundle properties = (PropertyResourceBundle) PropertyResourceBundle.getBundle(CONFIG_BUNDLE_NAME);

	
	/**
	 * Constructor de la Clase
	 */
	public ClienteSwitch(){
	}

	
	public boolean insertaTrxSwitch(long id_pedido) //{    public static void main( String args[] )
	{	

	    boolean codResp = false;
		// trx con tamaño incorrecto
		//StringBuffer msg = new StringBuffer(requerimiento);
		
		
		
		// Creamos al BizDelegate
		//BizDelegate biz = new BizDelegate();

		PedidosService ps = new PedidosService();
	    
		
		// obtenemos encabezado del pedido
		try { 
		    
		    pedido 	= ps.getPedido( id_pedido );
		    WebpayDTO wp = ps.webpayGetPedido(id_pedido);

		    if (pedido.getMedio_pago().equals(Constantes.MEDIO_PAGO_TBK)){
				montoTotal = ps.getMontoTotalTrxByIdPedido(id_pedido);

				Calendar calendario = Calendar.getInstance();
				
				msgReq.setNumUnico(wp.getTBK_ORDEN_COMPRA() + "");
				msgReq.setMonto(montoTotal + "");
				//msgReq.setUlt4Digit(pedido.getNum_mp().substring(pedido.getNum_mp().length() - 4));
				msgReq.setUlt4Digit(wp.getTBK_FINAL_NUMERO_TARJETA());
				msgReq.setFecha(dateFormat.format(calendario.getTime()));
				msgReq.setHora(timeFormat.format(calendario.getTime()));
				//msgReq.setNumCuotas(pedido.getN_cuotas()+"");
				msgReq.setNumCuotas(wp.getTBK_NUMERO_CUOTAS() + "");
				
				// Regla: 0 es sin cuota y mayor que cero es con cuota;
				if ( wp.getTBK_NUMERO_CUOTAS() == 0 )
			        msgReq.setTipoCuota("0"); // 0 : sin cuotas
			    else
			        msgReq.setTipoCuota("1"); // 1: cuota fija
				
			    msgReq.setTbk_secuencia_x(pedido.getTbk_secuencia_x()+"");
			    msgReq.setTbk_secuencia_y(pedido.getTbk_secuencia_y()+"");

				
				ms = parseaHeader();
				
				StringBuffer re = new StringBuffer(msgReq.toMsg());
				
			   	String aux = re.toString();
				byte[] trx = aux.getBytes();
				ms.setTrxMsgSwitch( trx );
				
				int nBytes = 0;
				bufpru = ByteBuffer.allocate( 2048 );
				short j = ms.getLenMsgSwitch();
				
				bufpru.putShort( j ); // Largo del mensaje
				bufpru.put( ms.toBytes()); //Cabecera + Requerimiento
					
				bufpru.flip();

				client = makeConnection( client);
				logger.info( "Envio mensaje = " + aux );
				nBytes = client.write( bufpru );
				bufpru.clear();
				nBytes = client.read( bufpru );
				logger.info( "Cantidad de bytes leidos = " + nBytes );
				bufpru.flip();
				short len_s = bufpru.getShort();
				byte[] datosrec = new byte[(int)len_s];
				bufpru.get( datosrec );
				MsgSwitchDTO msrsp = new MsgSwitchDTO( len_s, datosrec );
				logger.info( "Mensaje respuesta = " + msrsp );
				MsgRspDTO msgResp = parseaRespuesta(len_s, msrsp);
				if (msgResp.getCodRespuesta().equals("00")){
				    codResp = true;
				}
				bufpru.clear();
				client.close();
				Thread.sleep( 50L );


				if ( client.isConnected() )
					client.close();
		    }
		}catch(IOException e) {
			e.printStackTrace();
		}catch ( InterruptedException e ) {
			e.printStackTrace();
		}catch (Exception e){
		    e.printStackTrace();
		}
		return codResp;
	}	
	
	public SocketChannel makeConnection( SocketChannel sc ) {
		try {
			sc = SocketChannel.open();
			//isa = new InetSocketAddress( "172.18.149.156", 4850 ); // DESARROLLO
			isa = new InetSocketAddress( "172.18.149.187", 4850 ); // PRODUCCIÓN
			boolean testcon = sc.connect(isa );
			if (testcon == false)
				logger.info("Test connect = " + testcon);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sc;
	}

	public MsgSwitchDTO parseaHeader(){
	    //Header header = new Header();
		ms.setVersionMsgSwitch( '0' );
		ms.setServiceMsgSwitch( new String( "JUMBO.CL" ).getBytes() );
		ms.setTimeoutMsgSwitch( 20 );
	    
		return ms;
	}
	
	public MsgRspDTO parseaRespuesta(short len_s, MsgSwitchDTO msrsp){
	    System.out.println("Largo: " + len_s);
	    System.out.println("Respuesta: " + msrsp);
	    
	    String resp = new String(msrsp.getTrxMsgSwitch());
	    
	    MsgRspDTO msgResp = new MsgRspDTO();
	    msgResp.setCodRespuesta(resp.substring(0, 2));
	    msgResp.setCodBaseDatos(resp.substring(2, 7));
	    msgResp.setMsgRespuesta(resp.substring(7));
	    
	    logger.debug("Código de Respuesta: " + msgResp.getCodRespuesta());
	    logger.debug("Código de BD       : " + msgResp.getCodBaseDatos());
	    logger.debug("Mensaje Respuesta  : " + msgResp.getMsgRespuesta());
	    
	    return msgResp;
	    
	}
}
