package cl.bbr.jumbocl.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import sun.misc.BASE64Encoder;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.ObjetoDTO;
import cl.bbr.jumbocl.common.exceptions.SystemException;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
/**
 * 
 * Utilitarios
 * 
 * @author BBR e-commerce & retail
 *
 */
public class Utils {
	
	//se utiliza para validar un correo
	 private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";	 
	 

	/**
	 * Constructor
	 *
	 */
	public Utils() {
	}	
	
	/**
	 * Genera una nueva clave aleatorea
	 * 
	 * @param largo
	 *            Largo de la clave que se requiere
	 * @return Clave generada
	 */
	public static String genPassword(int largo) {
		Random random = new Random();
		String result = "";

		for (int i = 0; i < largo; i++) {
			int rs = random.nextInt(26);
			int intVal = rs + 65;
			char charVal = (char) intVal;
			result = result + charVal;
		}

		return result;

	}
	
	/**
	 * Encripta en MD5
	 * 
	 * @param texto 	Texto a encriptar
	 * @return 			texto encriptado
	 * @throws SystemException
	 */
	public static String encriptar( String texto ) throws SystemException {
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SystemException("Problemas con encriptacion clave", e);
		}
		
		md.update(texto.getBytes());
		
		byte raw[] = md.digest(); // Obtención del resumen de mensaje
		String hash = (new BASE64Encoder()).encode(raw); // Traducción a BASE64
		return hash;		
				
	}	
	
	/**
	 * Elimina caracteres no permitidos por la base de datos
	 * 
	 * @param valor Texto a revisar
	 * @param largo Largo permitido máximo
	 * @return Texto revisado
	 */
	public static String stringToDb( String valor, int largo ) {

		String res = null;
		
		if( valor != null ) {
			
			res = valor.replaceAll("[\\s]+", " ");
			res = res.replaceAll("[^A-Za-z0-9-@\\-\\. áÁéÉíÍóÓúÚñÑüÜ_]+", "");
			
			if( res.length() > largo )
				res = res.substring(0,largo-1);
			
		}
		
		return res;		
		
	}
	
	/**
	 * 
	 * Permite redondear al intervalo más cercano a la cantidad de productos ingresada
	 * 
	 * @param cantidad
	 * @param intervalo
	 * @return cantidad redondeada
	 */
	public static double redondear( double cantidad, double intervalo ) {
		double ret = cantidad;
		if( Math.floor(cantidad) == cantidad ) {
			ret = cantidad;
		}
		else if( Math.floor(cantidad) < cantidad && cantidad < Math.ceil(cantidad) ) {
			double valor_deca = cantidad*1000-Math.floor(cantidad)*1000; 
			double valor_decb = intervalo*1000-Math.floor(intervalo)*1000;
			//Hay que aproximar al siguiente
			if( valor_decb == 0 )
				valor_decb = intervalo*1000;
			if( valor_deca%valor_decb == 0 ) {
				ret = cantidad;
			}
			else {
				ret = cantidad - (cantidad-Math.floor(cantidad)) + intervalo;
			}
		}
		
		return Double.parseDouble(Formatos.formatoIntervalo( ret ));
		
	}
	
	/**
	 * Permite redondear el valor decimal al entero.
	 * 
	 * @param valor valor a redondear
	 * @return Valor redondeado
	 */
	public static long redondear( double valor ) {
		
		return Math.round(valor);
		
	}
	
	/**
	 * Permite redondear el valor double con 'x' decimales
	 * @param numero
	 * @param decimales
	 * @return
	 */
	public static double redondear(double valor, int decimales) {
	    valor = Math.round(valor*Math.pow(10,decimales))/Math.pow(10,decimales);
	    return valor;
    }
	
	
	/**
	 * Transforma el número de OP de acuerdo a función OP2=A*OP+B 
	 * 
	 * @param a Factor
	 * @param b Factor
	 * @param op Número de OP
	 * 
	 * @return Número de OP transformado
	 */
	public static String numOP( long a, long b, long op ) {
		
		String ret = (a*op+b) + ""; 
		
		return ret;
		
	}
	
	/**
	 * método que obtiene la fecha y hora actual
	 * 
	 * @return String
	 */
	public static String getFecHoraActual(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return bartDateFormat.format(date);
	}
	
	/**
	 * método que obtiene la fecha actual
	 * 
	 * @return String
	 */
	public static String getFechaActual(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return bartDateFormat.format(date);
	}
	
	/**
	 * método que obtiene la fecha actual
	 * 
	 * @return String
	 */
	public static String getFechaActualByPatron(String patron){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(patron);
        Date date = new Date();
        return bartDateFormat.format(date);
	}
	
    /**
     * Las palabras largas separadas con '/' son separadas para la impresion de etiquetas
     * @param descripcion
     * @return la descripcion con palabras separadas
     */
	public static String separarDescripcionesLargas(String descripcion) {
        return descripcion.replaceAll("/"," / ");
    }
	
	/**
	 * Una fecha de tipo yyyy-mm-dd lo transforma a dd-mm-yyyy, separados por / o -
	 * @param oldFecha
	 * @return
	 */
	public static String cambiaFormatoFecha(String oldFecha) {
	    String newFecha = "";
	    try {
	        newFecha = oldFecha.substring(8,10)+"-"+oldFecha.substring(5,7)+"-"+oldFecha.substring(0,4);    
        } catch (Exception e) {
            newFecha = "";
        }
	    return newFecha;
	}
    
    /**
     * Combo box con los valores de los tipos de pedidos
     * @return
     */
    public static List comboTipoPedido() {
        List lista = new ArrayList();
        
        ObjetoDTO obj1 = new ObjetoDTO();
        obj1.setCodigo(Constantes.ORIGEN_WEB_CTE);
        obj1.setNombre(Constantes.ORIGEN_WEB_TXT);
        lista.add(obj1);
        
        ObjetoDTO obj2 = new ObjetoDTO();
        obj2.setCodigo(Constantes.ORIGEN_VE_CTE);
        obj2.setNombre(Constantes.ORIGEN_VE_TXT);
        lista.add(obj2);
        
        ObjetoDTO obj3 = new ObjetoDTO();
        obj3.setCodigo(Constantes.ORIGEN_CASOS_CTE);
        obj3.setNombre(Constantes.ORIGEN_CASOS_TXT);
        lista.add(obj3);
        
        ObjetoDTO obj4 = new ObjetoDTO();
        obj4.setCodigo(Constantes.ORIGEN_JV_CTE);
        obj4.setNombre(Constantes.ORIGEN_JV_TXT);
        lista.add(obj4);
        
        return lista;
    }
    
    /**
     * Combo box para listar pedidos reprogramados
     * @return
     */
    public static List comboReprogramada() {
        List lista = new ArrayList();
        
        ObjetoDTO obj1 = new ObjetoDTO();
        obj1.setCodigo("S");
        obj1.setNombre("Si");
        lista.add(obj1);
        
        ObjetoDTO obj2 = new ObjetoDTO();
        obj2.setCodigo("N");
        obj2.setNombre("No");
        lista.add(obj2);
        
        return lista;
    }
    
    /**
     * Metodo para saber si al momento de reprogramar el pedido, mostramos el precio del despacho
     * para que se pueda modificar.
     * @param idEstado
     * @return
     */
    public static boolean considerarPreciosParaReprogramar(long idEstado) {
        for ( int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_REPROGRAMAR_CON_PRECIOS.length; i++) {
            if ( Long.parseLong( Constantes.ESTADOS_PEDIDO_PARA_REPROGRAMAR_CON_PRECIOS[i] ) == idEstado ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que nos indica si debemos considerar la jornada de picking para realizar una reprogramacion
     * @param idEstado
     * @return
     */
    public static boolean considerarPickingParaReprogramar(long idEstado) {
        for ( int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_REPROGRAMAR_CON_CAMBIO_DE_JPICKING.length; i++) {
            if ( Long.parseLong( Constantes.ESTADOS_PEDIDO_PARA_REPROGRAMAR_CON_CAMBIO_DE_JPICKING[i] ) == idEstado ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metodo para saber si mostramos el checkbox si el pedido estuvo en transito
     * para que se pueda modificar.
     * @param idEstado
     * @return
     */
    public static boolean mostrarCheckSiPedidoEstuvoEnTransito(long idEstado) {
        for ( int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_MOSTRAR_EN_TRANSITO.length; i++) {
            if ( Long.parseLong( Constantes.ESTADOS_PEDIDO_PARA_MOSTRAR_EN_TRANSITO[i] ) == idEstado ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Nos indica si el pedido puede ser reprogramado
     * @param idEstado
     * @return
     */
    public static boolean puedeReprogramar(long idEstado) {
        for ( int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_REPROGRAMAR_DESPACHO.length; i++) {
            if ( Long.parseLong( Constantes.ESTADOS_PEDIDO_PARA_REPROGRAMAR_DESPACHO[i] ) == idEstado ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Indica en caso que el pedido este en un estado critico para ser reprogramado 
     * @param idEstado
     * @return
     */
    public static boolean alertarAntesDeReprogramar(long idEstado) {
        for ( int i=0; i < Constantes.ESTADOS_PEDIDO_PARA_ALERTAR_PREREPROGRAMAR_DESPACHO.length; i++) {
            if ( Long.parseLong( Constantes.ESTADOS_PEDIDO_PARA_ALERTAR_PREREPROGRAMAR_DESPACHO[i] ) == idEstado ) {
                return true;
            }
        }
        return false;
    }
    
    
    public static String fechaJornadaDespacho(String despacho) {
        return despacho.substring(0,4) + "-" +despacho.substring(5,7) + "-" +despacho.substring(8,10);        
    }
    
    
	public static void main(String[] args) throws SystemException {
        //System.out.println("clave:"+encriptar("1234")); //gdyb21LQTcIANtvYMT7QVQ==
        /*
        String texto = "A recent study conducted by the Energy Ministry surveyed exactly how Chileans perceive the option of nuclear power. In sample of 1,500 people, 67 percent rejected the idea of having a nuclear reactor on Chilean soil. The survey also found that 80 percent said they worried it was an overly expensive source of energy, 69.7 percent said they were concerned over environmental damage, and 59 percent ranked it as the most dangerous source of renewable energy. The survey also determined the first thought that comes to mind when discussing nuclear energy with a Chilean: either destruction for 22 percent or danger 22 percent. Images of exploding nuclear warheads and the catastrophe of Chernobyl plant in Russia appear to have given the general public a dubious view about nuclear energy. Still, in the same survey, 74 percent said they were open to analyzing the nuclear option if it is shown to be cleaner and more economical than other commercial alternatives."; 
        String[] textArray = texto.split(" ");
        for (int i=0; i < textArray.length; i++) {
          System.out.println( textArray[ i ].toString().charAt( 0 ) );
        }
        */        
        //System.out.println("TEST 12345600:" + idOrdenToIdPedido(12345600));
    }

    /**
     * Entrega el numero con un cero a la izquierda, usado para las cuotas webpay y tambien para
     * la secuencia de pago, ambas van del '00' al '99' 
     * 
     * @param tbk_numero_cuotas
     * @return
     */
    public static String secuenciaStr(int numero) {
        if ( numero >= 0 && numero <= 9 ) {
            return "0" + numero;            
        }
        return ""+numero;
    }

    /**
     * @param tbk_tipo_pago
     * @return
     */
    public static String webpayTipoCuotas(String tbk_tipo_pago) {
        if ("VN".equalsIgnoreCase(tbk_tipo_pago)) {
            return "Sin cuotas";
        } else if ("VC".equalsIgnoreCase(tbk_tipo_pago)) {
            return "Normal";
        } else if ("SI".equalsIgnoreCase(tbk_tipo_pago)) {
            return "Sin intereses";
        } else if ("CI".equalsIgnoreCase(tbk_tipo_pago)) {
            return "Cuotas comercio";
        } else if ("VD".equalsIgnoreCase(tbk_tipo_pago)) {
            return "RedCompra";
        }
        return "";
    }

    /**
     * Toma el valor de la orden de compra de los medios de pago TBK o CAT que es el idPedido + secuencia_pago
     * y devuelve solo el valor del idPedido que manejamos internamente en el negocio
     * @param nroOrdenCompra
     * @return
     */
    public static int idOrdenToIdPedido(int nroOrdenCompra) {
        String orden = String.valueOf(nroOrdenCompra);
        return Integer.parseInt(orden.substring(0,orden.length()-2));
    }
    
    /*
     * 
     * DESDE EL FO
     * */
    
    

	/**
	 * Genera una nueva clave aleatorea
	 * 
	 * @param largo
	 *            Largo de la clave que se requiere
	 * @return Clave generada
	 */
	public static String genPasswordFO(int largo) {
		Random random = new Random();
		String result = "";

		for (int i = 0; i < largo; i++) {
			int rs = random.nextInt(26);
			int intVal = rs + 65;
			char charVal = (char) intVal;
			result = result + charVal;
		}

		return result;

	}
	
	/**
	 * Encripta en MD5
	 * 
	 * @param texto 	Texto a encriptar
	 * @return 			texto encriptado
	 * @throws SystemException
	 */
	public static String encriptarFO( String texto ) throws SystemException {
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new SystemException("Problemas con encriptacion clave", e);
		}
		
		md.update(texto.getBytes());
		
		byte raw[] = md.digest(); // Obtención del resumen de mensaje
		String hash = (new BASE64Encoder()).encode(raw); // Traducción a BASE64
		return hash;		
				
	}	
	
	/**
	 * Elimina caracteres no permitidos por la base de datos
	 * 
	 * @param valor Texto a revisar
	 * @param largo Largo permitido máximo
	 * @return Texto revisado
	 */
	public static String stringToDbFO( String valor, int largo ) {

		String res = null;
		
		if( valor != null ) {
			
			res = valor.replaceAll("[\\s]+", " ");
			res = res.replaceAll("[^A-Za-z0-9-@\\-\\. áÁéÉíÍóÓúÚñÑüÜ_]+", "");
			
			if( res.length() > largo )
				res = res.substring(0,largo-1);
			
		}
		
		return res;		
		
	}
	
	/**
	 * 
	 * Permite redondear al intervalo más cercano a la cantidad de productos ingresada
	 * 
	 * @param cantidad
	 * @param intervalo
	 * @return cantidad redondeada
	 */
	public static double redondearFO( double cantidad, double intervalo ) {
		double ret = cantidad;
		if( Math.floor(cantidad) == cantidad ) {
			ret = cantidad;
		}
		else if( Math.floor(cantidad) < cantidad && cantidad < Math.ceil(cantidad) ) {
			double valor_deca = cantidad*1000-Math.floor(cantidad)*1000; 
			double valor_decb = intervalo*1000-Math.floor(intervalo)*1000;
			//Hay que aproximar al siguiente
			if( valor_decb == 0 )
				valor_decb = intervalo*1000;
			if( valor_deca%valor_decb == 0 ) {
				ret = cantidad;
			}
			else {
				ret = cantidad - (cantidad-Math.floor(cantidad)) + intervalo;
			}
		}
		
		return Double.parseDouble(Formatos.formatoIntervaloFO( ret ));
		
	}
	
	/**
	 * Permite redondear el valor decimal al entero.
	 * 
	 * @param valor valor a redondear
	 * @return Valor redondeado
	 */
	public static long redondearFO( double valor ) {
		
		return Math.round(valor);
		
	}
	
	/**
	 * Transforma el número de OP de acuerdo a función OP2=A*OP+B 
	 * 
	 * @param a Factor
	 * @param b Factor
	 * @param op Número de OP
	 * 
	 * @return Número de OP transformado
	 */
	public static String numOPFO( long a, long b, long op ) {
		
		String ret = (a*op+b) + ""; 
		
		return ret;
		
	}
	
	/**
	 * Devuelve la fecha actual, con el formato q queramos
	 * @param patronStr
	 * @return
	 */
	public static String fechaActualFO(String patronStr) {
	    SimpleDateFormat formatter = new SimpleDateFormat(patronStr);
	    Calendar fecha = Calendar.getInstance();		
		Date fechaDate = fecha.getTime();
	    return formatter.format(fechaDate);
	}
    
    public static String printNombreFO(String nombre) {
        nombre = nombre.replaceAll("Ñ","&Ntilde;");
        nombre = nombre.replaceAll("ñ","&ntilde;");
        nombre = nombre.replaceAll("Á","&Aacute;");
        nombre = nombre.replaceAll("á","&aacute;");
        nombre = nombre.replaceAll("É","&Eacute;");
        nombre = nombre.replaceAll("é","&eacute;");
        nombre = nombre.replaceAll("Í","&Iacute;");
        nombre = nombre.replaceAll("í","&iacute;");
        nombre = nombre.replaceAll("Ó","&Oacute;");
        nombre = nombre.replaceAll("ó","&oacute;");
        nombre = nombre.replaceAll("Ú","&Uacute;");
        nombre = nombre.replaceAll("ú","&uacute;");
        return nombre;
    }
    
    public static boolean isNumericFO( String s ){
        try{
            double d = Double.parseDouble( s );
            return true;
        }
        catch( NumberFormatException err ){
            return false;
        }
    }
    
    /** LOS SIGUIENTES METODOS SE AGREGARON PARA FORMULARIO KCC
     * Metodo encargado de validar los campos de un formulario 
     * @param string
     * @return
     */
    public static String sanitizeFO(String string) {
    	  return string
    	     .replaceAll("(?i)<script.*?>.*?</script.*?>", "")   // case 1
    	     .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "") // case 2
    	     .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");     // case 3
    	}
    
    
    /**
     *validacion de correo 
     * 
     * @param email
     *           email para validacion
     * 
     */
    public static boolean validateEmailFO(String email) {
 
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
 
    }
    
    public static boolean validateTextoFO(String texto, String patron) {
    	 
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(texto);
        return matcher.matches();
 
    }

	// VALIDACION DE RUT  - UTILIZADO EN FORMULARIO KCC
    //*********************************************************
    
    public static boolean verificarRutFO(int rut, char dv) {
    	int m = 0, s = 1;
    	for (; rut != 0; rut /= 10) {
    	s = (s + rut % 10 * (9 - m++ % 6)) % 11;
    	}
    	return dv == (char) (s != 0 ? s + 47 : 75);
    }
    
    public static String utf8DecodeFO(String txt){
	   String var=new String(txt);
	   byte[] arrByte;
		try {
			arrByte = var.getBytes("ISO-8859-1");
			return new String(arrByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Bloque catch generado automáticamente
			e.printStackTrace();
			return new String("");
		}	   
    }
    
    //FIN METODOS AGREGADOS FORMULARIO KCC
    
    
    public static boolean isEmpty(String str) {
    	return StringUtils.isEmpty(str);
    	//return str == null || str.length() == 0;
    }
	
    public static boolean isBlank(String str) {
    	return StringUtils.isBlank(str);
    	//return str == null || str.length() == 0;
    }
}
