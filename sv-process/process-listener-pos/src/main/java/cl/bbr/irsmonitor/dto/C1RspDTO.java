package cl.bbr.irsmonitor.dto;

import java.util.ArrayList;
import java.util.List;

import cl.bbr.irsmonitor.datos.ProductoC1;
import cl.bbr.irsmonitor.datos.ReqRsp;
//import cl.bbr.irsmonitor.utils.DEScrypt;
import cl.bbr.irsmonitor.utils.FormatUtils;
import cl.bbr.jumbocl.shared.log.Logging;

public class C1RspDTO extends ReqRsp{
	
	private Logging logger = new Logging(this);
	
	private String codRsp;
	private String Glosa;
	private String fpNumeroUnico; //Id_Pedido
	private String fp_4_ultimos_digitos_tarj;
	private String fpCuotas;
	private String fpServicio;
	private String fpTipoCuota;
	private String fpRut;
	private String fpNombre;
	private String puntos_rut;
	private String fcRut;
	private String fcNombre;
	private String fcDireccion;
	private String fcTelefono;
	private String fcGiro;
	private String fcComuna;
	private String fcCiudad;
	private String fpCodComercio;
	private String fpSecuenciaOperTBK_X;
	private String fpSecuenciaOperTBK_Y;
	private String filler = "";
	private List productos = new ArrayList();


	public static final int CODRSP_LEN           = 3;
	public static final int GLOSA_LEN            = 40;
	public static final int FPNUMERO_UNICO_LEN   = 26;
	public static final int FP4ULT_DIG_TARJ_LEN  = 4;
	public static final int FILLER1_LEN          = 2;
	public static final int FPCUOTAS_LEN         = 2;
	public static final int FPSERVICIO_LEN       = 3;
	public static final int FPTIPOCUOTA_LEN      = 1;
	public static final int FPRUT_LEN            = 10;
	public static final int FPNOMBRE_LEN         = 35;
	public static final int PUNTOS_RUT_LEN       = 10;
	public static final int FCRUT_LEN            = 10;
	public static final int FCNOMBRE_LEN         = 35;
	public static final int FCDIRECCION_LEN      = 35;
	public static final int FCTELEFONO_LEN       = 10;
	public static final int FCGIRO_LEN           = 15;
	public static final int FCCOMUNA_LEN         = 12;
	public static final int FCCIUDAD_LEN         = 12;
	public static final int FPCODCOMERCIO_LEN    = 12;
	public static final int FPSECUENCIA_TBK_X_LEN= 3;
	public static final int FPSECUENCIA_TBK_Y_LEN= 3;
	public static final int FILLER2_LEN          = 32;
	
	
		
	public C1RspDTO(){
		
	}
	
	
	
	/**
	 * Obtiene el largo de la data del mensaje
	 * @return int largo
	 */
	public int getLargo(){
		
		int largo = 0;
		
    	largo += CODRSP_LEN;
		largo += GLOSA_LEN;
		largo += FPNUMERO_UNICO_LEN;
		largo += FP4ULT_DIG_TARJ_LEN;
		largo += FILLER1_LEN;
		largo += FPCUOTAS_LEN;
		largo += FPSERVICIO_LEN;
		largo += FPTIPOCUOTA_LEN;
		largo += FPRUT_LEN;
		largo += FPNOMBRE_LEN;
		largo += PUNTOS_RUT_LEN;
		largo += FCRUT_LEN;
		largo += FCNOMBRE_LEN;
		largo += FCDIRECCION_LEN;
		largo += FCTELEFONO_LEN;
		largo += FCGIRO_LEN;
		largo += FCCOMUNA_LEN;
		largo += FCCIUDAD_LEN;
		largo += FPCODCOMERCIO_LEN;
		largo += FPSECUENCIA_TBK_X_LEN;
		largo += FPSECUENCIA_TBK_Y_LEN;
		largo += FILLER2_LEN;
		
		logger.debug("Largo Data Fija: " + largo);
		
		largo += ( productos.size() * ProductoC1.getLargo() );
				
		logger.debug("Largo Data Fija + Variable:" + largo);
		
		return largo;
		
	}
	
	
	/**
	 * Obtiene el largo de la data del mensaje cuando hay codigos de error
	 * @return int largo
	 */
	public int getExcepcionLargo(){
		
		int largo = 0;
		
		largo += 	CODRSP_LEN;
		largo += 	GLOSA_LEN;
		
		logger.debug("Largo Data Fija: " + largo);
		
		return largo;
		
	}
	
	
	
	/**
	 * Formatea mensje para salida
	 *
	 */
	public String toMsg(){
		
		// Encriptamos num_tarjeta + fecha_vencimiento
		//String n_tarjeta = FormatUtils.formatField(this.fpTarjeta, FP_N_TARJETA, FormatUtils.ALIGN_RIGHT, "0");
		//String f_venc = FormatUtils.formatField(this.fpVencimiento, FP_FVENC, FormatUtils.ALIGN_RIGHT,"0");
		
		//String nt_enc = DEScrypt.encryptBase64( n_tarjeta + f_venc );
	    //String codComercio = "";
	    //String batch = "";
	    //String Secuencia = "";
		
		//nuevo FIXME esto es sólo para pruebas
		//String NumeroUnico = FormatUtils.formatField(this.fpNumeroUnico, 26, FormatUtils.ALIGN_RIGHT, "0");
		//String finalNumTarjeta = "1234"; //this.fpTarjeta.substring(this.fpTarjeta.length()-4);
		
		//String nt_enc = DEScrypt.encryptBase64( n_tarjeta + f_venc );
		//String nt_enc = pedidoId + codComercio + codAutorizacion + finalNumTarjeta;
		
		//logger.debug("N tarj: " + n_tarjeta);
		//logger.debug("Fec Venc: " + f_venc);
		//logger.debug("tarj encrypt: " + nt_enc);
		
		String out = "";

		out += FormatUtils.formatField(codRsp, 			CODRSP_LEN, 		FormatUtils.ALIGN_RIGHT,"0");
		out += FormatUtils.formatField(Glosa, 			GLOSA_LEN, 			FormatUtils.ALIGN_LEFT," ");
		
		out += FormatUtils.formatField(fpNumeroUnico, FPNUMERO_UNICO_LEN, FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fp_4_ultimos_digitos_tarj, 	FP4ULT_DIG_TARJ_LEN, 	FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(filler, 						FILLER1_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fpCuotas, 					FPCUOTAS_LEN, 			FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpServicio, 					FPSERVICIO_LEN, 		FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fpTipoCuota, 				FPTIPOCUOTA_LEN, 		FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpRut, 						FPRUT_LEN, 				FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpNombre, 					FPNOMBRE_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(puntos_rut, 					PUNTOS_RUT_LEN, 		FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fcRut, 						FCRUT_LEN, 				FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fcNombre, 					FCNOMBRE_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcDireccion, 				FCDIRECCION_LEN, 		FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcTelefono, 					FCTELEFONO_LEN, 		FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcGiro, 						FCGIRO_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcComuna, 					FCCOMUNA_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcCiudad, 					FCCIUDAD_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fpCodComercio, 				FPCODCOMERCIO_LEN, 		FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpSecuenciaOperTBK_X, 		FPSECUENCIA_TBK_X_LEN,	FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpSecuenciaOperTBK_Y, 		FPSECUENCIA_TBK_Y_LEN,	FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(filler, 						FILLER2_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		
		String aux = "";
		// Formateamos el listado de productos
		for (int i=0; i<productos.size(); i++){
			ProductoC1 p1 = (ProductoC1)productos.get(i);
			aux = "";
			aux  = FormatUtils.formatField(p1.getSku()		, ProductoC1.SKU_LEN, 		FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getIndicat()	, ProductoC1.INDICAT_LEN, 	FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getCantidad()	, ProductoC1.CANTIDAD_LEN, 	FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getDecimal()	, ProductoC1.DECIMAL_LEN, 	FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getPrecio()	, ProductoC1.PRECIO_LEN, 	FormatUtils.ALIGN_RIGHT,"0");

			logger.debug("prod:" + aux);
			
			out += aux;
		}
		
	
		return out;

	}
	
	
	
	
	/**
	 * Formatea mensje para salida cuando hay una excepcion
	 *
	 */
	public String toExceptionMsg(){
		String out = "";

		out += FormatUtils.formatField(codRsp, 			CODRSP_LEN, 		FormatUtils.ALIGN_RIGHT,"0");
		out += FormatUtils.formatField(Glosa, 			GLOSA_LEN, 			FormatUtils.ALIGN_LEFT," ");
	
		return out;

	}
	
	
	
	
	/**
	 * Formatea mensje para salida de pruebas
	 *
	 */
	public String toMsgPosTest(){
		String out = "";

		//out += FormatUtils.formatField(codRsp, 			CODRSP_LEN, 		FormatUtils.ALIGN_RIGHT,"0");
		//out += FormatUtils.formatField(Glosa, 			GLOSA_LEN, 			FormatUtils.ALIGN_LEFT," ");
		
		out += FormatUtils.formatField(fpNumeroUnico, FPNUMERO_UNICO_LEN, FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fp_4_ultimos_digitos_tarj, 	FP4ULT_DIG_TARJ_LEN, 	FormatUtils.ALIGN_RIGHT, "0");
		
		out += "\n";
		
		out += FormatUtils.formatField(filler, 						FILLER1_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fpCuotas, 					FPCUOTAS_LEN, 			FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpServicio, 					FPSERVICIO_LEN, 		FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fpTipoCuota, 				FPTIPOCUOTA_LEN, 		FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpRut, 						FPRUT_LEN, 				FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpNombre, 					FPNOMBRE_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		
		out += "\n";
		
		out += FormatUtils.formatField(puntos_rut, 					PUNTOS_RUT_LEN, 		FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fcRut, 						FCRUT_LEN, 				FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fcNombre, 					FCNOMBRE_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcDireccion, 				FCDIRECCION_LEN, 		FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcTelefono, 					FCTELEFONO_LEN, 		FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcGiro, 						FCGIRO_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcComuna, 					FCCOMUNA_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		out += FormatUtils.formatField(fcCiudad, 					FCCIUDAD_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		
		out += "\n";
		
		out += FormatUtils.formatField(fpCodComercio, 				FPCODCOMERCIO_LEN, 		FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpSecuenciaOperTBK_X, 		FPSECUENCIA_TBK_X_LEN,	FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(fpSecuenciaOperTBK_Y, 		FPSECUENCIA_TBK_Y_LEN,	FormatUtils.ALIGN_RIGHT, "0");
		out += FormatUtils.formatField(filler, 						FILLER2_LEN, 			FormatUtils.ALIGN_LEFT,  " ");
		
		//out += "\n";
		
		String aux = "";
		// Formateamos el listado de productos
		for (int i=0; i<productos.size(); i++){
			ProductoC1 p1 = (ProductoC1)productos.get(i);
			aux = "\n";
			aux += FormatUtils.formatField(p1.getSku()		, ProductoC1.SKU_LEN, 		FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getIndicat()	, ProductoC1.INDICAT_LEN, 	FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getCantidad()	, ProductoC1.CANTIDAD_LEN, 	FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getDecimal()	, ProductoC1.DECIMAL_LEN, 	FormatUtils.ALIGN_RIGHT,"0");	
			aux += FormatUtils.formatField(p1.getPrecio()	, ProductoC1.PRECIO_LEN, 	FormatUtils.ALIGN_RIGHT,"0");
			
			//aux += "\n";
			
			logger.debug("prod:" + aux);
			
			
			out += aux;
		}
	
		return out;

	}
	

    /**
     * @return Devuelve codRsp.
     */
    public String getCodRsp() {
        return codRsp;
    }
    /**
     * @return Devuelve fcCiudad.
     */
    public String getFcCiudad() {
        return fcCiudad;
    }
    /**
     * @return Devuelve fcComuna.
     */
    public String getFcComuna() {
        return fcComuna;
    }
    /**
     * @return Devuelve fcDireccion.
     */
    public String getFcDireccion() {
        return fcDireccion;
    }
    /**
     * @return Devuelve fcGiro.
     */
    public String getFcGiro() {
        return fcGiro;
    }
    /**
     * @return Devuelve fcNombre.
     */
    public String getFcNombre() {
        return fcNombre;
    }
    /**
     * @return Devuelve fcRut.
     */
    public String getFcRut() {
        return fcRut;
    }
    /**
     * @return Devuelve fcTelefono.
     */
    public String getFcTelefono() {
        return fcTelefono;
    }
    /**
     * @return Devuelve filler.
     */
    public String getFiller() {
        return filler;
    }
    /**
     * @return Devuelve fp_4_ultimos_digitos_tarj.
     */
    public String getFp_4_ultimos_digitos_tarj() {
        return fp_4_ultimos_digitos_tarj;
    }
    /**
     * @return Devuelve fpCodComercio.
     */
    public String getFpCodComercio() {
        return fpCodComercio;
    }
    /**
     * @return Devuelve fpCuotas.
     */
    public String getFpCuotas() {
        return fpCuotas;
    }
    /**
     * @return Devuelve fpNombre.
     */
    public String getFpNombre() {
        return fpNombre;
    }
    /**
     * @return Devuelve fpNumeroUnico.
     */
    public String getFpNumeroUnico() {
        return fpNumeroUnico;
    }
    /**
     * @return Devuelve fpRut.
     */
    public String getFpRut() {
        return fpRut;
    }
    /**
     * @return Devuelve fpSecuenciaOperTBK_X.
     */
    public String getFpSecuenciaOperTBK_X() {
        return fpSecuenciaOperTBK_X;
    }
    /**
     * @return Devuelve fpSecuenciaOperTBK_Y.
     */
    public String getFpSecuenciaOperTBK_Y() {
        return fpSecuenciaOperTBK_Y;
    }
    /**
     * @return Devuelve fpServicio.
     */
    public String getFpServicio() {
        return fpServicio;
    }
    /**
     * @return Devuelve fpTipoCuota.
     */
    public String getFpTipoCuota() {
        return fpTipoCuota;
    }
    /**
     * @return Devuelve glosa.
     */
    public String getGlosa() {
        return Glosa;
    }
    /**
     * @return Devuelve logger.
     */
    public Logging getLogger() {
        return logger;
    }
    /**
     * @return Devuelve productos.
     */
    public List getProductos() {
        return productos;
    }
    /**
     * @return Devuelve puntos_rut.
     */
    public String getPuntos_rut() {
        return puntos_rut;
    }
    /**
     * @param codRsp El codRsp a establecer.
     */
    public void setCodRsp(String codRsp) {
        this.codRsp = codRsp;
    }
    /**
     * @param fcCiudad El fcCiudad a establecer.
     */
    public void setFcCiudad(String fcCiudad) {
        this.fcCiudad = fcCiudad;
    }
    /**
     * @param fcComuna El fcComuna a establecer.
     */
    public void setFcComuna(String fcComuna) {
        this.fcComuna = fcComuna;
    }
    /**
     * @param fcDireccion El fcDireccion a establecer.
     */
    public void setFcDireccion(String fcDireccion) {
        this.fcDireccion = fcDireccion;
    }
    /**
     * @param fcGiro El fcGiro a establecer.
     */
    public void setFcGiro(String fcGiro) {
        this.fcGiro = fcGiro;
    }
    /**
     * @param fcNombre El fcNombre a establecer.
     */
    public void setFcNombre(String fcNombre) {
        this.fcNombre = fcNombre;
    }
    /**
     * @param fcRut El fcRut a establecer.
     */
    public void setFcRut(String fcRut) {
        this.fcRut = fcRut;
    }
    /**
     * @param fcTelefono El fcTelefono a establecer.
     */
    public void setFcTelefono(String fcTelefono) {
        this.fcTelefono = fcTelefono;
    }
    /**
     * @param filler El filler a establecer.
     */
    public void setFiller(String filler) {
        this.filler = filler;
    }
    /**
     * @param fp_4_ultimos_digitos_tarj El fp_4_ultimos_digitos_tarj a establecer.
     */
    public void setFp_4_ultimos_digitos_tarj(String fp_4_ultimos_digitos_tarj) {
        this.fp_4_ultimos_digitos_tarj = fp_4_ultimos_digitos_tarj;
    }
    /**
     * @param fpCodComercio El fpCodComercio a establecer.
     */
    public void setFpCodComercio(String fpCodComercio) {
        this.fpCodComercio = fpCodComercio;
    }
    /**
     * @param fpCuotas El fpCuotas a establecer.
     */
    public void setFpCuotas(String fpCuotas) {
        this.fpCuotas = fpCuotas;
    }
    /**
     * @param fpNombre El fpNombre a establecer.
     */
    public void setFpNombre(String fpNombre) {
        this.fpNombre = fpNombre;
    }
    /**
     * @param fpNumeroUnico El fpNumeroUnico a establecer.
     */
    public void setFpNumeroUnico(String fpNumeroUnico) {
        this.fpNumeroUnico = fpNumeroUnico;
    }
    /**
     * @param fpRut El fpRut a establecer.
     */
    public void setFpRut(String fpRut) {
        this.fpRut = fpRut;
    }
    /**
     * @param fpSecuenciaOperTBK_X El fpSecuenciaOperTBK_X a establecer.
     */
    public void setFpSecuenciaOperTBK_X(String fpSecuenciaOperTBK_X) {
        this.fpSecuenciaOperTBK_X = fpSecuenciaOperTBK_X;
    }
    /**
     * @param fpSecuenciaOperTBK_Y El fpSecuenciaOperTBK_Y a establecer.
     */
    public void setFpSecuenciaOperTBK_Y(String fpSecuenciaOperTBK_Y) {
        this.fpSecuenciaOperTBK_Y = fpSecuenciaOperTBK_Y;
    }
/**
     * @param fpServicio El fpServicio a establecer.
     */
    public void setFpServicio(String fpServicio) {
        this.fpServicio = fpServicio;
    }
    /**
     * @param fpTipoCuota El fpTipoCuota a establecer.
     */
    public void setFpTipoCuota(String fpTipoCuota) {
        this.fpTipoCuota = fpTipoCuota;
    }
    /**
     * @param glosa El glosa a establecer.
     */
    public void setGlosa(String glosa) {
        Glosa = glosa;
    }
    /**
     * @param logger El logger a establecer.
     */
    public void setLogger(Logging logger) {
        this.logger = logger;
    }
    /**
     * @param productos El productos a establecer.
     */
    public void setProductos(List productos) {
        this.productos = productos;
    }
    /**
     * @param puntos_rut El puntos_rut a establecer.
     */
    public void setPuntos_rut(String puntos_rut) {
        this.puntos_rut = puntos_rut;
    }
}
