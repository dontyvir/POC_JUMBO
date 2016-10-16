package cl.jumbo.capturar.dto;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;



/**
 * <b>Descripción:</b> MsgSwitch - Clase que permite almacenar y  
 * manipular un mensaje transaccional recibido desde un cliente (requerimiento) o
 * emisor (respuesta) dentro del ambiente de Switch Server. 
 * <p> Por cada mensaje se mantienen los siguientes datos:
 * <li> largo del mensaje en su totalidad bajo formato short (2 bytes) </li> 
 * <li> versión de la mensajería (1 byte) </li>
 * <li> nombre del servicio requerido (8 bytes) </li> 
 * <li> timeout de espera para el mensaje transaccional expresado en segundos (3 bytes) </li>
 * <li> largo del mensaje transaccional a nivel del cliente/emisor (4 bytes) </li>
 * <li> código de respuesta a nivel del Switch Server (2 bytes) </li> 
 * <li> identificador de cliente (TPDU) asignado a nivel del Switch Server (2 bytes, rango 00-FF) </li>
 * <li> mensaje transaccional recibido (n bytes) </li> </p> 
 * @author Silvio Belledonne B.
 * @author BBR e-commerce & retail
 * @version 1.0
 * @since 14/06/2006
 **/

public class MsgSwitchDTO {
	

	public static final int LENMSGSWITCH_LEN      = 2;
	public static final int VERSIONMSGSWITCH_LEN  = 1;
	public static final int SERVICEMSGSWITCH_LEN  = 8;
	public static final int TIMEOUTMSGSWITCH_LEN  = 3;
	public static final int LENTRXMSGSWITCH_LEN   = 4;
	public static final int CRMSGSWITCH_LEN       = 2;
	public static final int IDCLIENTMSGSWITCH_LEN = 2;
	
	
	/*** Largo del mensaje en su totalidad bajo formato short. */
	public short lenMsgSwitch;
	/*** Versión de la mensajería transaccional. */
	public char versionMsgSwitch;
	/*** Nombre del servicio requerido. */
	public byte [] serviceMsgSwitch;
	/*** Timeout asignado al servicio requerido. */
	public int timeoutMsgSwitch;
	/*** Largo del mensaje transaccional generado por el cliente/emisor. */
	public int lentrxMsgSwitch;
	/*** Código de respuesta asignado por el ambiente de Switch Server. */
	public byte [] crMsgSwitch;
	/*** Identificación de cliente asignado por el ambiente de Switch Server. */
	public byte [] idclientMsgSwitch;
	/*** Mensaje transaccional generado por el cliente/emisor. */
	public byte [] trxMsgSwitch;
	
	/**
     * Constructor para creación de un mensaje transaccional del Switch Server para envío.
     * @see MsgSwitch#setTrxMsgSwitch(byte[])
     */	
	public MsgSwitchDTO() {

		// Se setea el largo del mensaje transaccional del Switch Server, para
		// lo cual se suman los largos de todos sus campos, excluyendo el largo
		// del mensaje transaccional del cliente/emisor
		lenMsgSwitch = (short) (VERSIONMSGSWITCH_LEN + SERVICEMSGSWITCH_LEN + TIMEOUTMSGSWITCH_LEN 
		             + LENTRXMSGSWITCH_LEN + CRMSGSWITCH_LEN + IDCLIENTMSGSWITCH_LEN);
		
		// Se setea la versión en '0'
		this.setVersionMsgSwitch( '0' );

		// Se define el largo del código de servicio requerido y se
		// rellena con ceros
		serviceMsgSwitch = new byte[8];
		byte fillbyte = (byte) '0';
		Arrays.fill( serviceMsgSwitch, fillbyte );
		
		// Se define el timeout en 0
		timeoutMsgSwitch = 0;
		
		// Se define el largo de la transacción del cliente/emisor en cero
		lentrxMsgSwitch = 0;

		// Se define el largo del código de respuesta y se rellena con
		// ceros
		crMsgSwitch = new byte[2];
		Arrays.fill( crMsgSwitch, fillbyte );
		
		// Se define el largo del identificador de cliente y se rellena con
		// ceros
		idclientMsgSwitch = new byte[2];
		Arrays.fill( idclientMsgSwitch, fillbyte );
		
	}
		
	/**
     * Constructor para creación de un mensaje transaccional del Switch Server 
     * para recepción.
     * @param lenmsg Largo del mensaje transaccional generado por el Switch Server.
     * @param datosrec Arreglo de bytes con el mensaje transaccional a generar.
     */	
	public MsgSwitchDTO( short lenmsg, byte[] datosrec ) {

		// Se setea la versión recibida
		this.setVersionMsgSwitch( (char) datosrec[0] );

		// Se setea el código de servicio recibido
		byte[] serv = new byte[8];
		int i;
		for ( i = 0; i < 8; i++ )
			serv[i] = datosrec[i+1];
		this.setServiceMsgSwitch( serv );
		
		// Se setea el timeout recibido
		byte[] tmout = new byte[3];
		for ( i = 0; i < 3; i++ )
			tmout[i] = datosrec[i+1+8];
		String s = new String( tmout );
		this.setTimeoutMsgSwitch( Integer.parseInt( s ) );
		
		// Se setea el largo de la transacción del cliente/emisor recibido
		byte[] ltrx = new byte[4];
		for ( i = 0; i < 4; i++ )
			ltrx[i] = datosrec[i+1+8+3];
		s = new String( ltrx );		
		this.setLentrxMsgSwitch( Integer.parseInt( s ) );

		// Se setea el código de respuesta recibido
		byte[] crmsg = new byte[2];
		for ( i = 0; i < 2; i++ )
			crmsg[i] = datosrec[i+1+8+3+4];
		this.setCrMsgSwitch( crmsg );
		
		// Se setea el identificador de cliente recibido
		byte[] idclientmsg = new byte[2];
		for ( i = 0; i < 2; i++ )
			idclientmsg[i] = datosrec[i+1+8+3+4+2];
		this.setIdclientMsgSwitch( idclientmsg );
		
		// Se setea el mensaje transaccional del cliente/emisor recibido 
		// y se rellena con ceros
		//byte[] trxms = new byte[this.getLentrxMsgSwitch()];
		//for ( i = 0; i < this.getLentrxMsgSwitch(); i++ )
		byte[] trxms = new byte[lenmsg-20];
		for ( i = 0; i < (lenmsg-20); i++ )
			trxms[i] = datosrec[i+1+8+3+4+2+2];
		this.setTrxMsgSwitch( trxms );
		// Se setea el largo del mensaje transaccional del Switch Server, se hace
		// al final porque el método MsgSwitch#setTrxMsgSwitch altero el valor
		lenMsgSwitch = lenmsg;
		
	}
	
	/**
	 * @return Retorna el largo del mensaje transaccional del Switch Server.
	 */
	public short getLenMsgSwitch() {
		return lenMsgSwitch;
	}
	
	/**
	 * Setea el largo del mensaje transaccional del Switch Server.
	 * @param lenMsgSwitch Largo del mensaje transaccional.
	 */
	public void setLenMsgSwitch( short lenMsgSwitch ) {
		this.lenMsgSwitch = lenMsgSwitch;
	}
	
	/**
	 * @return Retorna la versión de la mensajería transaccional
	 * del Switch Server.
	 */
	public char getVersionMsgSwitch() {
		return versionMsgSwitch;
	}

	/**
	 * Setea el versión de la mensajería transaccional
	 * del Switch Server. 
	 * @param versionMsgSwitch Versión de la mensajería transaccional.
	 */
	public void setVersionMsgSwitch( char versionMsgSwitch ) {
		this.versionMsgSwitch = versionMsgSwitch;
	}

	/**
	 * @return Retorna el nombre del servicio requerido del mensaje transaccional
	 * del Switch Server.
	 */
	public byte[] getServiceMsgSwitch() {
		return serviceMsgSwitch;
	}

	/**
	 * Setea el nombre del servicio requerido del mensaje transaccional
	 * del Switch Server.
	 * @param serviceMsgSwitch Nombre del servicio requerido.
	 */
	public void setServiceMsgSwitch( byte[] serviceMsgSwitch ) {
		this.serviceMsgSwitch = serviceMsgSwitch;
	}

	/**
	 * @return Retorna el timeout en segundos definido para el servicio requerido.
	 */
	public int getTimeoutMsgSwitch() {
		return timeoutMsgSwitch;
	}

	/**
	 * Setea el timeout en segundos para el servicio requerido.
	 * @param timeoutMsgSwitch Timeout del servicio requerido.
	 */
	public void setTimeoutMsgSwitch( int timeoutMsgSwitch ) {
		this.timeoutMsgSwitch = timeoutMsgSwitch;
	}

	/**
	 * @return Retorna el largo del mensaje transaccional del cliente/emisor.
	 */
	public int getLentrxMsgSwitch() {
		return lentrxMsgSwitch;
	}
	
	/**
	 * Setea el largo del mensaje transaccional del cliente/emisor.
	 * @param lentrxMsgSwitch Largo del mensaje transaccional.
	 */
	public void setLentrxMsgSwitch( int lentrxMsgSwitch ) {
		this.lentrxMsgSwitch = lentrxMsgSwitch;
	}
	
	/**
	 * @return Retorna el código de respuesta asignado al mensaje 
	 * transaccional del Switch Server.
	 */
	public byte[] getCrMsgSwitch() {
		return crMsgSwitch;
	}

	/**
	 * Setea el código de respuesta del mensaje transaccional del Switch Server.
	 * @param crMsgSwitch Código de respuesta asignado.
	 */
	public void setCrMsgSwitch( byte[] crMsgSwitch ) {
		this.crMsgSwitch = crMsgSwitch;
	}

	/**
	 * @return Retorna el identificador del cliente asignado por el Switch Server. 
	 */
	public byte[] getIdclientMsgSwitch() {
		return idclientMsgSwitch;
	}

	/**
	 * Setea el identificador del cliente asignado por el Switch Server.
	 * @param idclientMsgSwitch Identificador de cliente asignado.
	 */
	public void setIdclientMsgSwitch( byte[] idclientMsgSwitch ) {
		this.idclientMsgSwitch = idclientMsgSwitch;
	}

	/**
	 * @return Retorna el mensaje transaccional generado por el cliente/emisor.
	 */
	public byte[] getTrxMsgSwitch() {
		return trxMsgSwitch;
	}
	
	/**
	 * Setea el mensaje transaccional generador por el cliente/emisor y actualiza
	 * los largos de la mensajería (Switch Server y cliente/emisor).
	 * @param trxMsgSwitch Mensaje transaccional.
	 */
	public void setTrxMsgSwitch( byte[] trxMsgSwitch ) {
		// Se actualizan los largos de la mensajería (Switch Server y cliente/emisor)
	    lenMsgSwitch += (short) trxMsgSwitch.length;
		lentrxMsgSwitch = trxMsgSwitch.length;
		this.trxMsgSwitch = trxMsgSwitch;
	}
	
	/**
	 * Convierte mensaje transaccional a string para logging.
	 * @return Retorna mensaje transaccional en formato de string.
	 */
    public String toString() {
    	NumberFormat formatter = new DecimalFormat( "00000" );
        String lenMsgSwitch = formatter.format( this.getLenMsgSwitch() );
        formatter = new DecimalFormat( "000" );
        String timeoutMsgSwitch = formatter.format( this.getTimeoutMsgSwitch() );
        formatter = new DecimalFormat( "0000" );
        String lenTrxMsgSwitch = formatter.format( this.getLentrxMsgSwitch() );
    	String s = null;
		try {
			s = lenMsgSwitch + " " +
			    this.getVersionMsgSwitch() + " " +
			    new String( this.getServiceMsgSwitch() ) + " " + 
			    timeoutMsgSwitch + " " +
			    lenTrxMsgSwitch + " " +
			    new String( this.getCrMsgSwitch() ) + " " +
			    new String( this.getIdclientMsgSwitch() ) + " " +
			    new String( this.getTrxMsgSwitch(), "ISO-8859-1" );
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
    	return s;
    }
	
    /**
	 * Convierte mensaje transaccional a un arreglo de bytes sin incorporar
	 * largo del mensaje total.
	 * @return Retorna mensaje transaccional en formato de arreglo de bytes.
	 */
    public byte[] toBytes() {
    	NumberFormat ftimeout = new DecimalFormat( "000" );
    	NumberFormat flentrx = new DecimalFormat( "0000" );
        String s = null;
		try {
			s = this.getVersionMsgSwitch() + 
			    new String( this.getServiceMsgSwitch() ) +  
			    new String( ftimeout.format( this.getTimeoutMsgSwitch() ) ) + 
			    new String( flentrx.format( this.getLentrxMsgSwitch()) ) + 
			    new String( this.getCrMsgSwitch() ) + 
			    new String( this.getIdclientMsgSwitch() ) +
			    new String( this.getTrxMsgSwitch(), "ISO-8859-1" );
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
    	try {
			return s.getBytes( "ISO-8859-1" );
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
			return  null;
		}
    }
}
