package cl.bbr.jumbocl.pedidos.dto;


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

public class MsgRspDTO {
	
	public String CodRespuesta;
	public String CodBaseDatos;
	public String msgRespuesta;
	
	public static final int CODRESPUESTA_LEN = 2;
	public static final int CODBASEDATOS_LEN = 5;
	public static final int MSGRESPUESTA_LEN = 100;


    
    /**
     * @return Devuelve codBaseDatos.
     */
    public String getCodBaseDatos() {
        return CodBaseDatos;
    }
    /**
     * @return Devuelve codRespuesta.
     */
    public String getCodRespuesta() {
        return CodRespuesta;
    }
    /**
     * @return Devuelve msgRespuesta.
     */
    public String getMsgRespuesta() {
        return msgRespuesta;
    }
    /**
     * @param codBaseDatos El codBaseDatos a establecer.
     */
    public void setCodBaseDatos(String codBaseDatos) {
        CodBaseDatos = codBaseDatos;
    }
    /**
     * @param codRespuesta El codRespuesta a establecer.
     */
    public void setCodRespuesta(String codRespuesta) {
        CodRespuesta = codRespuesta;
    }
    /**
     * @param msgRespuesta El msgRespuesta a establecer.
     */
    public void setMsgRespuesta(String msgRespuesta) {
        this.msgRespuesta = msgRespuesta;
    }
}
