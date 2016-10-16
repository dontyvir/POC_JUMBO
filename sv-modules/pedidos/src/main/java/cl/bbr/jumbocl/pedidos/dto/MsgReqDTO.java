package cl.bbr.jumbocl.pedidos.dto;

import cl.bbr.jumbocl.pedidos.util.FormatUtils;


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

public class MsgReqDTO {
	
	public String numUnico;
	public String monto;
	public String ult4Digit;
	public String fecha;
	public String hora;
	public String numCuotas;
	public String tipoCuota;
	public String tbk_secuencia_x;
	public String tbk_secuencia_y;
	
	public static final int NUMUNICO_LEN  = 26;
	public static final int MONTO_LEN     = 9;
	public static final int ULT4DIGIT_LEN = 4;
	public static final int FECHA_LEN     = 8;
	public static final int HORA_LEN      = 6;
	public static final int NUMCUOTAS_LEN = 3;
	public static final int TIPOCUOTA_LEN = 1;
	public static final int TBK_SECUENCIA_X_LEN = 3;
	public static final int TBK_SECUENCIA_Y_LEN = 3;

    
    /**
     * @return Devuelve fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @return Devuelve hora.
     */
    public String getHora() {
        return hora;
    }
    /**
     * @return Devuelve monto.
     */
    public String getMonto() {
        return monto;
    }
    /**
     * @return Devuelve numCuotas.
     */
    public String getNumCuotas() {
        return numCuotas;
    }
    /**
     * @return Devuelve numUnico.
     */
    public String getNumUnico() {
        return numUnico;
    }

    /**
     * @return Devuelve tipoCuota.
     */
    public String getTipoCuota() {
        return tipoCuota;
    }
    /**
     * @return Devuelve ult4Digit.
     */
    public String getUlt4Digit() {
        return ult4Digit;
    }
    /**
     * @param fecha El fecha a establecer.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @param hora El hora a establecer.
     */
    public void setHora(String hora) {
        this.hora = hora;
    }
    /**
     * @param monto El monto a establecer.
     */
    public void setMonto(String monto) {
        this.monto = monto;
    }
    /**
     * @param numCuotas El numCuotas a establecer.
     */
    public void setNumCuotas(String numCuotas) {
        this.numCuotas = numCuotas;
    }
    /**
     * @param numUnico El numUnico a establecer.
     */
    public void setNumUnico(String numUnico) {
        this.numUnico = numUnico;
    }
    /**
     * @param tipoCuota El tipoCuota a establecer.
     */
    public void setTipoCuota(String tipoCuota) {
        this.tipoCuota = tipoCuota;
    }
    /**
     * @param ult4Digit El ult4Digit a establecer.
     */
    public void setUlt4Digit(String ult4Digit) {
        this.ult4Digit = ult4Digit;
    }
    /**
     * @return Devuelve tbk_secuencia_x.
     */
    public String getTbk_secuencia_x() {
        return tbk_secuencia_x;
    }
    /**
     * @return Devuelve tbk_secuencia_y.
     */
    public String getTbk_secuencia_y() {
        return tbk_secuencia_y;
    }
    /**
     * @param tbk_secuencia_x El tbk_secuencia_x a establecer.
     */
    public void setTbk_secuencia_x(String tbk_secuencia_x) {
        this.tbk_secuencia_x = tbk_secuencia_x;
    }
    /**
     * @param tbk_secuencia_y El tbk_secuencia_y a establecer.
     */
    public void setTbk_secuencia_y(String tbk_secuencia_y) {
        this.tbk_secuencia_y = tbk_secuencia_y;
    }

    
    
	public String toMsg(){
		
		String out = "";

		out += FormatUtils.formatField(numUnico,  NUMUNICO_LEN,  FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(monto,     MONTO_LEN,     FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(ult4Digit, ULT4DIGIT_LEN, FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fecha,     FECHA_LEN,     FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(hora,      HORA_LEN,      FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(numCuotas, NUMCUOTAS_LEN, FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(tipoCuota, TIPOCUOTA_LEN, FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(tbk_secuencia_x, TBK_SECUENCIA_X_LEN, FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(tbk_secuencia_y, TBK_SECUENCIA_Y_LEN, FormatUtils.ALIGN_RIGHT, "0");
	
		return out;
	}

	/*public byte[] toBytes(){
        String s = null;

		s = this.getNumUnico().getBytes() + 
            new String( this.getMonto().getBytes() ) +  
            new String( this.getUlt4Digit().getBytes() ) + 
            new String( this.getFecha().getBytes() ) + 
            new String( this.getHora().getBytes() ) + 
            new String( this.getNumCuotas().getBytes() ) +
            new String( this.getTipoCuota().getBytes() ) +
            new String( this.getTbk_secuencia_x().getBytes() ) +
            new String( this.getTbk_secuencia_y().getBytes() );

    	try {
			return s.getBytes( "ISO-8859-1" );
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
			return  null;
		}
	}

	public int getLargoMensaje(){
	    return (NUMUNICO_LEN + MONTO_LEN + ULT4DIGIT_LEN + FECHA_LEN + 
	            HORA_LEN + NUMCUOTAS_LEN + TIPOCUOTA_LEN + 
	            TBK_SECUENCIA_X_LEN + TBK_SECUENCIA_Y_LEN);
	}***/
}
