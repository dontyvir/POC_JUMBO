package cl.bbr.jumbocl.common.utils;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.dto.FechaDTO;

/**
 * Clase que permite colocar formatos a algunos tipos de variables 
 * @author bbr
 *
 */
public class Formatos {
	
	 /**
     * Formato para Fecha y Hora
     */
    public static final String DATE_CAL  = "dd-MM";
    public static final String DATE_BD  = "yyyy-MM-dd";
    public static final String HOUR_CAL  = "HH:mm";
    public static final String YYYYMMDD  = "yyyy-MM-dd";
    public static final String EEEEEEEEE  = "EEEEEEEEE";
    public static final DecimalFormat cantidad  = new DecimalFormat("#####.###");
    

	public static final String DATE_TIME 	= "dd-MM-yyyy HH:mm:ss";
	public static final String DATE 		= "dd-MM-yyyy";
	public static final String TIME 		= "HH:mm:ss";
	public static final String FRM_DATE_TIME= "yyyy-MM-dd HH:mm:ss";
	public static final String FRM_DATE		= "yyyy-MM-dd";
	public static final String FRM_DATE_TIME_STR= "yyyyMMdd HHmmss";
	public static final String FRM_DATE_TIME_STR2= "yyyyMMddHHmmss";

	public static final String SINDATO 	= "---";
	//Genero
	public static final String FEM 		= "Femenino";
	public static final String MSC 		= "Masculino";
	public static final String FEM_ID 		= "F";
	public static final String MSC_ID 		= "M";
	public static final String FEM_TXT 		= "Femenino";
	public static final String MSC_TXT 		= "Masculino";
	
	//Mix de productos Sap
	public static final String MIXS 	= "Si";
	public static final String MIXN 	= "No";
	//Direcciones de Prod. sugeridos
	public static final String SUGUNI 	= "A  => B";
	public static final String SUGBID 	= "A <=> B";
	//Constantes
	public static final int	DIA_EN_SEG 		= 24*60*60;
	public static final int	DIA_EN_MILISEG 	= 24*60*60*1000; 	
	public static final int	MAX_DIAXSEMANA	= 7;
	public static final int	MAX_SEMANAXANIO	= 53;
		
	/** Tipo de alineación a la izquierda */
	public static final int ALIGN_LEFT			= 0;
	/** Tipo de alineación a la derecha */
	public static final int ALIGN_RIGHT			= 1;
	
	/**
	 * Formatos de Boton de pago
	 * */
	public static final String BTN_PAGO_FRM_FECHA	= "AAAAMMDD";
	public static final String BTN_PAGO_FRM_HORA	= "HHmmss";
	
	/**
	 * Constructor
	 */
	public Formatos() {
	}

	/**
	 * método que permite dar un formato de precio 
	 * @param  num double
	 * @return num en String con signo peso
	 */
	public static String formatoPrecio( double num ) {

		Locale lidioma = new Locale("cl", "CL");
		/*
		NumberFormat df = NumberFormat.getCurrencyInstance(lidioma);
		df.setMaximumFractionDigits(2);						
		return df.format( num );
		*/
		NumberFormat df = NumberFormat.getInstance(lidioma);
		df.setMaximumFractionDigits(0);		
		return "$"+df.format( num );		

	}
	
	/**
	 * método que permite dar un formato a un numero (2 digitos decimales)
	 * @param  num
	 * @return String
	 */
	public static String formatoNumero( double num ) {
		
		Locale lidioma = new Locale("cl", "CL");
		NumberFormat df = NumberFormat.getInstance(lidioma);
		df.setMaximumFractionDigits(2);		
		return df.format( num );

	}	
	
	
	/**
	 * método que permite dar un formato a un numero (2 digitos decimales)
	 * @param  num
	 * @return String
	 */
	public static String formatoNumeroSinDecimales( double num ) {
		
		Locale lidioma = new Locale("cl", "CL");
		NumberFormat df = NumberFormat.getInstance(lidioma);
		df.setMaximumFractionDigits(0);		
		return df.format( num );

	}	
	
	/**
	 * método que permite dar un formato a un numero (2 digitos decimales)
	 * @param  num
	 * @return String
	 */
	public static String formatoNumeroUnDecimal( double num ) {
		
		Locale lidioma = new Locale("cl", "CL");
		NumberFormat df = NumberFormat.getInstance(lidioma);
		df.setMaximumFractionDigits(1);		
		return df.format( num );

	}	
	

	/**
	 * método que permite dar un formato de cantidad
	 * @param num
	 * @return num+"" String
	 * 
	 */
	public static String formatoCantidad( double num ) {
		
		/*
		Locale lidioma = new Locale("cl", "CL");
		NumberFormat df = NumberFormat.getInstance(lidioma);
		df.setMaximumFractionDigits(2);
		return df.format( num );
		*/
		return num+"";

	}	
	
	/**
	 * método que permite dar un formato a un numero con 3 decimales
	 * @param num
	 * @return numero
	 * 
	 */
	public static double formatoNum3Dec( double num ) {
		double num_aux = Math.rint(num*1000)/1000;
		if( Math.floor(num_aux) == num_aux ) {
		    return Math.round(Math.ceil( num_aux ));
        }
		return Math.rint(num_aux*1000)/1000;

	}

	
	/** 
	 * método que permite dar un formato de kilogramo
	 * @param num
	 * @return double
	 */
	public static double kg2gr( double num ) {
		
		return num*1000;

	}	
	
	/**
	 * método que permite dar un formato a una unidad de peso 
	 * @param  unidad String 
	 * @return texto String
	 */
	public static String formatoUnidad( String unidad ) {

		String texto = "";
		
		if( unidad.compareTo("ST") == 0 ) {
			texto = "Uni.";
		}
		else
			texto = unidad;
		
		return texto;
	}
	
	/**
	 * método que permite dar formato de unidad de precio
	 * @param  unidad
	 * @return String
	 */
	public static String formatoUnidadPrecio( String unidad ) {

		String texto = "";
		
		if( unidad.compareTo("KG") == 0 ) {
			texto = "";
		}
		else
			texto = unidad;
		
		return texto;
	}	
	
	/**
	 * método que formatea caracteres extraños 
	 * @param  valor
	 * @param  largo
	 * @return String
	 */
	public static String stringToDb( String valor, int largo ) {
		
		String res = "";
		
		res = valor.replaceAll("[\\s]+", " ");
		res = res.replaceAll("[^A-Za-z0-9 -()\\.]+", "");
		
		if( res.length() > largo )
			res = res.substring(1,largo);
		
		return res;
		
	}
	
	/**
	 * método que formatea la fecha según un Timestamp, si la fecha no es nula se pasa a String, por el contrario se envia "----"
	 * @param  fecha Timestamp
	 * @return SINDATO String
	 */
	public static String frmFecha(Timestamp fecha){
		if (!(fecha==null)) {
			return frmFecha(fecha.toString());
        }
		return SINDATO;
	}
	/**
	 * método que formatea la fecha según un tipo Date, si la fecha no es nula se pasa a String, por el contrario se envia "----"
	 * @param  fecha Date
	 * @return String
	 */
	public static String frmFecha(Date fecha){
		if (!(fecha==null)) {
			return frmFecha(fecha.toString());
        }
		return SINDATO;
	}
	/**
	 * método que formatea una fecha según un String, si la fecha no es nula se pasa a String, por el contrario se envia "----"
	 * @param  fecha String 
	 * @return SINDATO String 
	 */
	public static String frmFecha(String fecha){
		if (!(fecha==null) && !fecha.equals("") && (fecha.length()>=10)) {
			return fecha.substring(8,10)+"/"+fecha.substring(5,7)+"/"+fecha.substring(0,4);
        }
        return SINDATO;
	}
	/**
	 * método que permite dar un formato de fecha y hora según un Timestamp, si la fecha y hora no es nula se pasa a String, por el contrario se envia "----"
	 * @param  fecha Timestamp 
	 * @return SINDATO String
	 */
	public static String frmFechaHora(Timestamp fecha){
		if (!(fecha==null)) {
			return frmFechaHora(fecha.toString());
        }
        return SINDATO;
	}
	/**
	 * método que permite dar un formato de fecha y hora según un Date, si la fecha y hora no es nula se pasa a String, por el contrario se envia "----"
	 * @param  fecha
	 * @return String
	 */
	public static String frmFechaHora(Date fecha){
		if (!(fecha==null)) {
			return frmFechaHora(fecha.toString());
        } 
        return SINDATO;
	}
	/**
	 * método que permite dar un formato de fecha y hora según un String, si la fecha y hora no es nula se pasa a String, por el contrario se envia "----"
	 * @param  fecha
	 * @return String
	 */
	public static String frmFechaHora(String fecha){
		if(!(fecha==null) && !fecha.equals("") && (fecha.length()>=10)) {
			return fecha.substring(8,10)+"/"+fecha.substring(5,7)+"/"+fecha.substring(0,4)+" "+fecha.substring(11,19);
        } 
        return SINDATO;
	}
    /**
     * método que permite dar un formato de fecha y hora según un String, si la fecha y hora no es nula se pasa a String, por el contrario se envia "----"
     * @param  fecha
     * @return String
     */
    public static String frmFechaHoraSinSegundos(String fecha){
        if(!(fecha==null) && !fecha.equals("") && (fecha.length()>=10)) {
            return fecha.substring(8,10)+"/"+fecha.substring(5,7)+"/"+fecha.substring(0,4)+" "+fecha.substring(11,16);
        } 
        return SINDATO;
    }	

	/**
	 * método que permite dar formato al genero de un usuario (femenino - masculino), si el dato viene nulo se envia "----"
	 * @param  genero String 
	 * @return MSC, FEM o SINDATO String
	 */
	public static String frmGenero(String genero) {		
		if(genero==null)
			return "";
		else if(genero.equals("M"))
			return MSC;
		else if(genero.equals("F"))
			return FEM;
		else
			return SINDATO;
	}
	/**
	 * método que permite dar un formato al email, si el email viene nulo entonces se envia "----"
	 * @param  email String
	 * @return email o SINDATO String
	 */
	public static String frmEmail(String email){
		if (!(email==null)) {
			return email;
        } 
        return SINDATO;
	}
	/**
	 * método que formatea sí, si se encuentra un mix por el contrario formatea no.
	 * @param  mix
	 * @return String
	 */
	public static String frmMix(String mix){
		if(mix.equals("S"))
			return MIXS;
		else if(mix.equals("N"))
			return MIXN;
		else
			return SINDATO;
	}
	/**
	 * método que permite dar formato a la dirección que tomen los sugeridos (unidireccionales o bidireccionales) 
	 * @param  dir
	 * @return String
	 */
	public static String frmSugDir(String dir){
		if(dir.equals("U"))
			return SUGUNI;
		else if(dir.equals("B"))
			return SUGBID;
		else
			return SINDATO;
	}
	
	/**
	 * método que obtiene la fecha y hora actual
	 * @return String
	 */
	public static String getFecHoraActual(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(FRM_DATE_TIME);
        Date date = new Date();
        return bartDateFormat.format(date);
	}
	
	/**
	 * método que obtiene la fecha actual
	 * @return String
	 */
	public static String getFechaActual(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(FRM_DATE);
        Date date = new Date();
        return bartDateFormat.format(date);
	}
	
	/**
	 * método que le da formato a una fecha
	 * @param  fecha String 
	 * @return fecha String
	 */
	public static String formatFecha(String fecha){
		if (fecha.equals("")) {
			return fecha;
        }
        return fecha.substring(6,10)+"-"+fecha.substring(3,5)+"-"+fecha.substring(0,2);
	}
	
	/**
	 * Da formato a una fecha de tipo Date
	 * 
	 * @param  fecha
	 * @return String
	 */
	public static String frmFechaByDate(Date fecha){
		try{
			SimpleDateFormat bartDateFormat = new SimpleDateFormat(Formatos.FRM_DATE);
			return bartDateFormat.format(fecha);
		}catch(Exception e){
			return SINDATO;
		}
	}

	/**
	 * Da formato a una fecha y hora del tipo Date
	 * 
	 * @param  fecha
	 * @return String
	 */
	public static String frmFechaHoraByDate(Date fecha){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(Formatos.FRM_DATE_TIME);
		 return bartDateFormat.format(fecha);
	}

	/**
	 * Da formato a una fecha y hora del tipo Date full
	 * 
	 * @param  fecha
	 * @return String
	 */
	public static String frmFechaHoraByDateFull(Date fecha){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(Formatos.FRM_DATE_TIME_STR2);
		 return bartDateFormat.format(fecha);
	}

	/**
	 * método que da formato a la hora de inicio 
	 * @param  fecha String 
	 * @return fecha String
	 */
	public static String formatFechaHoraIni(String fecha){
		if(fecha.equals("")) {
			return fecha;
        }
        return fecha.substring(6,10)+"-"+fecha.substring(3,5)+"-"+fecha.substring(0,2)+" 00:00:00";
	}

	/**
	 * método que da formato a la hora de fin
	 * @param  fecha
	 * @return String
	 */
	public static String formatFechaHoraFin(String fecha){
		if(fecha.equals("")) {
			return fecha;
        } 
        return fecha.substring(6,10)+"-"+fecha.substring(3,5)+"-"+fecha.substring(0,2)+" 23:59:59";
	}
	
	/**
	 * método que formatea una descripción
	 * @param  descripcion String 
	 * @param  longMax int 
	 * @return nuevo.toString() String
	 */
	public static String frmDescripcion(String descripcion, int longMax){
		StringBuffer nuevo = new StringBuffer();
		int i = 0;
		String br = "";
		int longitud=descripcion.length(); 
		while(i<longitud){
			nuevo.append(br);
			if(i+longMax<longitud){
				nuevo.append(descripcion.substring(i,i+longMax));
			}else{
				nuevo.append(descripcion.substring(i));
			}
			i+=longMax;
			br = "<br>";
		}
		return nuevo.toString();
		
	}
	
	/**
	 * Formatea el patrón de las búsquedas para las consultas
	 * 
	 * @param patron	Patrón a buscar
	 * @return			Texto formateado
	 */
	public static  String formatPatron( List patron ) {
		
		String patronaux = "";
		String strOr = " ";
		
		for (int i = 0; i < patron.size(); ++i){
			String pa_txt = (String)patron.get(i);
			// contiene tildes
			String pa_txt_sin_acentos = pa_txt.replaceAll("Á", "A");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("É", "E");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Í", "I");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ó", "O");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ú", "U");
			/*
			patronaux += strOr + " upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_des_corta),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' " +
			"or upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_des_larga),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' " +
			"or upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_tipo_producto),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' " +
			"or upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(mar_nombre),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+ pa_txt_sin_acentos + "%' " +
			"or upper(rtrim(ltrim(pro_cod_sap))) like '%"+ pa_txt_sin_acentos + "%' " ;
			*/
			patronaux += strOr + 
			"upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_tipo_producto),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' " +
			"or upper(rtrim(ltrim(replace(replace(replace(replace(replace(upper(mar_nombre),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+ pa_txt_sin_acentos + "%' " +
			"or upper(rtrim(ltrim(pro_cod_sap))) like '%"+ pa_txt_sin_acentos + "%' " ;
			strOr = " or ";
		}
		
		return patronaux;
		
	}
	
	/**
	 * método que quita los acentos de algún caracter
	 * @param  patron List 
	 * @return patronaux String
	 */
	public static String frmPatron( List patron ) {
		
		String patronaux = "";
		String strOr = " ";
		
		for (int i = 0; i < patron.size(); ++i){
			String pa_txt = (String)patron.get(i);
			// contiene tildes
			String pa_txt_sin_acentos = pa_txt.replaceAll("Á", "A");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("É", "E");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Í", "I");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ó", "O");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ú", "U");
			patronaux += strOr + " rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_des_corta),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))) like '%"+pa_txt_sin_acentos+"%' " +
			"or rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_des_larga),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))) like '%"+pa_txt_sin_acentos+"%' " +
			"or rtrim(ltrim(replace(replace(replace(replace(replace(upper(pro_tipo_producto),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))) like '%"+pa_txt_sin_acentos+"%' " +
			"or rtrim(ltrim(upper(pro_cod_sap))) like '%"+ pa_txt_sin_acentos + "%' " ;
			strOr = " or ";
		}
		
		return patronaux;
		
	}	
	
	/**
	 * método que quita los acentos de algún caracter para bo_productos
	 * @param  patron List 
	 * @return patronaux String
	 */
	public static String frmPatronBO( List patron ) {
		
		String patronaux = "";
		String strOr = " ";
		
		for (int i = 0; i < patron.size(); ++i){
			String pa_txt = (String)patron.get(i);
			// contiene tildes
			String pa_txt_sin_acentos = pa_txt.replaceAll("Á", "A");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("É", "E");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Í", "I");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ó", "O");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ú", "U");
			patronaux += strOr + " rtrim(ltrim(replace(replace(replace(replace(replace(upper(des_corta),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))) like '%"+pa_txt_sin_acentos+"%' " +
			"or rtrim(ltrim(replace(replace(replace(replace(replace(upper(des_larga),'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U'))) like '%"+pa_txt_sin_acentos+"%' " ;
			//"or rtrim(ltrim(upper(cod_prod1))) like '%"+ pa_txt_sin_acentos + "%' " ;
			strOr = " or ";
		}
		
		return patronaux;
		
	}	
	
	
	/**
	 * método que quita los acentos de algún caracter
	 * @param  patron List 
	 * @return patronaux String
	 */
	public static String frmPatron( List patron, long criterio ) {
		
		String patronaux = "";
		String strOr = " ";
		
		for (int i = 0; i < patron.size(); ++i){
			String pa_txt = (String)patron.get(i);
			// contiene tildes
			String pa_txt_sin_acentos = pa_txt.replaceAll("Á", "A");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("É", "E");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Í", "I");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ó", "O");
			pa_txt_sin_acentos = pa_txt_sin_acentos.replaceAll("Ú", "U");
			
			if (criterio == 1){
				patronaux += strOr + " upper(rtrim(ltrim(replace(replace(replace(replace(replace(pro_tipo_producto,'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' ";
			}else if(criterio == 2){
				patronaux += strOr + " upper(rtrim(ltrim(replace(replace(replace(replace(replace(pro_des_larga,'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' ";
			}else if(criterio == 3){
				patronaux += strOr + " upper(rtrim(ltrim(replace(replace(replace(replace(replace(mar_nombre,'Á','A'),'É','E'),'Í','I'),'Ó','O'),'Ú','U')))) like '%"+pa_txt_sin_acentos+"%' ";
			}
			strOr = " or ";
			
		}
		
		return patronaux;
		
	}
	
	/**
	 * método que elimina los espacios de una cadena
	 * @param  cadena
	 * @return String
	 */
	public static String frmSinEspacios(String cadena){
		StringBuffer elemento = new StringBuffer();
		while(cadena.length()>0){
			cadena = cadena.trim();
			if(cadena.indexOf(" ")!=-1){
				elemento.append(cadena.substring(0,cadena.indexOf(" ")));
				elemento.append(" ");
				cadena = cadena.substring(cadena.indexOf(" ")+1);
			}else{
				elemento.append(cadena);
				cadena = "";
			}
		}
		return elemento.toString();
	}
	
	/**
	 * método que obtiene el tiempo transcurrido de generado un evento
	 * @param  fecha String 
	 * @return result String 
	 */
	public static String tiempoTranscurrido( String fecha) {
		String result = "";
		if(!fecha.equals("")){
			try{
			   DateFormat formatter = new SimpleDateFormat(FRM_DATE_TIME);
			   Date dateFecha = (Date)formatter.parse(fecha);
			   Date dateActual = formatter.parse(formatter.format(new Date()));
		        
			   long tiempoFecha = dateFecha.getTime();
			   long tiempoActual = dateActual.getTime();
			   long diferEnSeg = (tiempoActual - tiempoFecha) ;
			   
			   if(diferEnSeg < DIA_EN_MILISEG){
				   result = getDiferFecActual(fecha);
			   }else{
				   SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy MM dd k:mm:ss");
			        Date date = new Date(diferEnSeg);
			        String temp = bartDateFormat.format(date);
			        int anios = Integer.parseInt(temp.substring(0,4)) - 1970 - 1;
			        //revisar años
			        if(anios>0){
			        	result += anios;
			        	if(anios==1){	result += "año ";
			        	}else{	result += "años ";		}
			        }
			        //revisar meses
			        temp = temp.substring(5);
			        int meses = Integer.parseInt(temp.substring(0,temp.indexOf(" "))) - 1;
			        if(meses>0) {
			        	result += meses;
			        	if(meses==1){	result += "mes ";
			        	}else{	result += "meses ";		}
			        }
			        //revisar dias
			        temp = temp.substring(3);
			        int dias = Integer.parseInt(temp.substring(0,temp.indexOf(" ")));
			        if(dias>0) {
			        	result += dias;
			        	if(dias==1){	result += "dia ";
			        	}else{	result += "dias ";		}
			        }
			        //colocar las horas, minutos y segundos al final
			        result += temp.substring(temp.indexOf(" ")+1);
			    }
			}catch(ParseException p){
				result = "";
			}
				   
			   }
		return result;
	}	

	/**
	 * método que obtiene la diferencia entre una fecha dada y la fecha actual
	 * @param  fecha String 
	 * @return result String
	 */
	public static String getDiferFecActual(String fecha){
		String result = "";
		try{
			DateFormat formatter = new SimpleDateFormat(FRM_DATE_TIME);
			Date dateFecha 	= (Date)formatter.parse(fecha);
			Date dateActual 	= formatter.parse( getFecHoraActual());
			
			long tiempoFecha 	= dateFecha.getTime();
			long tiempoActual 	= dateActual.getTime();
			long diferEnSeg 	= (tiempoActual - tiempoFecha)/1000 ;
			if(diferEnSeg < DIA_EN_SEG){
				long horas = diferEnSeg / (3600) ;
				if(horas > 0){
					result += horas+"h ";
				}
				long minutos = (diferEnSeg % 3600) / 60;
				if(minutos >0){
					result += minutos+"m ";
				}
				long segundos =(diferEnSeg % 3600) % 60;
				if(segundos >0){
					result += segundos+"s ";
				}
			}
		}catch(ParseException p){
			result = "";
		}
		
		return result;
	}

	/**
	 * método que obtiene el tiempo de acuerdo a tiempos en segundos
	 * @param  tiempo_en_seg long 
	 * @return result String
	 */
	public static String getTiempo(long tiempo_en_seg){
		String result = "";
			if(tiempo_en_seg < DIA_EN_SEG){
				result = getHorMinSeg(tiempo_en_seg);
			}else{
				//dias
				long dias = tiempo_en_seg / (3600*24);
				if(dias > 0){
					result += dias+"d ";
				}
				long horas = tiempo_en_seg % (3600*24) ;
				if(horas > 0){
					result += getHorMinSeg(horas);
				}
			}
		
		return result;
	}
	
	/**
	 * método que obtiene las horas, minutos y segundos según un tiempo determinado
	 * @param  tiempo long 
	 * @return result String
	 */
	public static String getHorMinSeg(long tiempo){
		String result = "";
		
		long horas = tiempo / (3600) ;
		if(horas > 0){
			result += horas+"h ";
		}
		long minutos = (tiempo % 3600) / 60;
		if(minutos >0){
			result += minutos+"m ";
		}
		long segundos =(tiempo % 3600) % 60;
		if(segundos >0){
			result += segundos+"s ";
		}
		return result;
	}
	
	/**
	 * Permite completar un cadena de caracteres 
	 * argumentos:
	 * @param cadena cadena inicial
	 * @param caracter caracter que se agrega a la cadena inicial
	 * @param cant_caracteres nro. de veces que el caracter se coloca en el resultado
	 * @param direccion cuyos valores permitidos son 'I' Izquierda, 'D' Derecha
	 * @return result String
	 *  
	 */
	/*public static String completaCadena (String cadena, char caracter, int cant_caracteres, char direccion){
		String result = "";
		if(!cadena.equals("") && !String.valueOf(caracter).equals("") && cant_caracteres >=0 && 
				( (direccion=='I') || (direccion=='D') ) ){
			if(direccion =='I'){
				for(int i=0; i<cant_caracteres; i++){
					result += caracter;
				}
				result += cadena;
			}else if(direccion =='D'){
				for(int i=0; i<cant_caracteres; i++){
					result += caracter;
				}
				result = cadena+result;
			}
		}
		
		return result;
	}*/

    /**
     * Permite redondear el valor decimal al entero.
     * 
     * @param valor
     * @return valor long
     */
    public static long redondear( double valor ) {
        
        return Math.round (valor);
        
    }
    
    /**
     * Permite cortar la cantidad de decimales.
     * 
     * @param numeroDecimales,decimal
     * @return decimal double
     */    
	public static double cortarDecimales(int numeroDecimales,double decimal){
		decimal = decimal*(Math.pow(10, numeroDecimales));
		decimal = Math.round(decimal);
		decimal = decimal/Math.pow(10, numeroDecimales);

		return decimal; 
	}
	
	/**
	 * método que formatea si hay asignado un precio  o no
	 * @param  con_precio String 
	 * @return Constantes.CON_PRE_S,Constantes.CON_PRE_N o SINDATO String
	 */
	public static String frmConPrecio(String con_precio){
		if(con_precio.equals("S"))
			return Constantes.CON_PRE_S;
		else if(con_precio.equals("N"))
			return Constantes.CON_PRE_N;
		else
			return SINDATO;
	}
    
	/**
	 * Obtiene nro de semana y anio a partir de la fecha
	 * 
	 * @param  fecha
	 * @return String
	 *
	public static String getNsemanaAnio(Date fecha){
		String n_sem_anio = "";
		Calendar cal_ini = new GregorianCalendar();
		cal_ini.setFirstDayOfWeek(Calendar.MONDAY);

		cal_ini.setTime(fecha);
		cal_ini.getTime();
		int anio = cal_ini.get(Calendar.YEAR);
		int n_semana	 = cal_ini.get(Calendar.WEEK_OF_YEAR);
		n_sem_anio = n_semana + "|" + anio; 
		
		return n_sem_anio;
	}*/
	
	/**
	 * Obtiene los datos de la fecha, segun la fecha ingresada
	 * @param  fecha
	 * @return FechaDTO
	 */
	public static FechaDTO getFechaDTOByString(String fecha){
		FechaDTO dto = new FechaDTO();
		Date fecha_d = new Date();
		try {			
			fecha_d = new SimpleDateFormat(FRM_DATE).parse(fecha);
		} catch(Exception E) {
			//System.out.println("Argumento fecha inválido" );
			return null;
		}		
		Calendar cal = new GregorianCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		cal.setTime(fecha_d);
		cal.getTime();
		int anio 		= cal.get(Calendar.YEAR);
		int mes         = cal.get(Calendar.MONTH);
		int n_semana	= cal.get(Calendar.WEEK_OF_YEAR);
		int dia 		= cal.get(Calendar.DAY_OF_WEEK);
		
		if(mes == 11 && n_semana == 1){
		    anio++;
		}
		
		if(dia>1)
			dia--;
		else
			dia = 7;
		//llenar el dto
		dto.setFecha(fecha_d);
		dto.setN_semana(n_semana);
		dto.setAnio(anio);
		dto.setFecha_Str(fecha);
		dto.setDia_semana(dia);
		
		return dto;
	}
	
	/**
	 * Suma o resta dias a una fecha inicial.
	 * 
	 * @param  fecha
	 * @param  nro_dias
	 * @param  operador
	 * @return String
	 */
	public static String getFechaFinal (String fecha, int nro_dias, char operador){
		Date fecha_d = new Date();
		try {			
			fecha_d = new SimpleDateFormat(FRM_DATE).parse(fecha);
		} catch(Exception E) {
			//System.out.println("Argumento fecha inválido" );
			return SINDATO;
		}
		long time_fec_fin = fecha_d.getTime();
		if(operador == '+'){
			time_fec_fin = fecha_d.getTime() + DIA_EN_MILISEG*nro_dias;
		}else if(operador == '-'){
			time_fec_fin = fecha_d.getTime() - DIA_EN_MILISEG*nro_dias;
		}
		Date fecha_f = new Date(time_fec_fin);
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(FRM_DATE);
		
		return bartDateFormat.format(fecha_f);
		
	}
	
	/**
	 * Obtiene el dia de la semana, segun la fecha
	 * - La semana comienza el dia Lunes , valor 1
	 * 
	 * @param  fecha
	 * @return int
	 */
	public static int getDiaSemanaByFecha(String fecha){
		Date fecha_d = new Date();
		try {			
			fecha_d = new SimpleDateFormat(FRM_DATE).parse(fecha);
		} catch(Exception E) {
			//System.out.println("Argumento fecha inválido" );
			return -1;
		}
		Calendar cal = new GregorianCalendar();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		cal.setTime(fecha_d);
		int dia = cal.get(Calendar.DAY_OF_WEEK);
		if(dia>1)
			dia--;
		else
			dia = 7;
		return dia;
	}
	
	/**
	 * Obtiene la fecha del dia, segun el dia de la semana, la semana y el año
	 * 
	 * @param dia_semana
	 * @param semana
	 * @param anio
	 * @return String 
	 */
	public static String getFechaByDiaSemAnio(int dia_semana, int semana, int anio){
		//OJO: al ejecutar, me muestra el dia anterior: si pido el dia 3 (miercoles), muestra del dia martes
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.YEAR, anio);
		if(semana > MAX_SEMANAXANIO)
			return SINDATO;
		cal.set(Calendar.WEEK_OF_YEAR, semana);
		if(dia_semana > MAX_DIAXSEMANA)
			return SINDATO;
		int dia = 0;
		if(dia_semana==7){
			dia = 1;
		}else{
			dia = dia_semana+1;
		}
		cal.set(Calendar.DAY_OF_WEEK, dia);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date fecha = cal.getTime();
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(FRM_DATE_TIME);
		return bartDateFormat.format(fecha);
	}
	
	/**
	 * Diferencia en dias, entre 2 fechas
	 * 
	 * @param  fecha_ini
	 * @param  fecha_fin
	 * @return int
	 */
	public static int diferDiasEntreFechas(String fecha_ini, String fecha_fin){
		Date fec_ini = new Date();
		Date fec_fin = new Date();
		try{
			fec_ini = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_ini);
			fec_fin = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_fin);
		}catch(Exception e){
			return -1;
		}
		long time_ini = fec_ini.getTime();
		long time_fin = fec_fin.getTime();
		long dia = (time_fin - time_ini)/DIA_EN_MILISEG; 
		
		return Integer.parseInt(dia+"");
	}
	
	/**
	 * Obtiene fecha del tipo Date, segun la cadena
	 * 
	 * @param  fecha
	 * @return Date
	 */
	public static Date getFechaDateByString(String fecha){
		try {			
			return new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
		} catch(Exception E) {
			return new Date();
		}
	}
	
	/**
	 * Formato definido para un valor de intervalo
	 * 
	 * @param num Número a formatear
	 * @return Texto formateado
	 */
	public static String formatoIntervalo( double num ) {		
		double num_aux = Math.rint(num*1000)/1000;
		if( Math.floor(num_aux) == num_aux ) {
			return Math.round(Math.ceil( num_aux ))+"";
        }
        return Math.rint(num_aux*1000)/1000+"";
	}	
	
	/**
	 * Elimina caracteres no válidos
	 * 
	 * @param valor Texto a revisar
	 * @return Texto revisado
	 */	
	public static String stringToSearch( String valor ) {
		
		String res = "";
		
		res = valor.replaceAll("[\\s]+", " ");
		res = res.replaceAll("[^A-Za-z0-9 áÁéÉíÍóÓúÚñÑüÜ]+", "");
				
		return res;
		
	}	
	
    /**
     * Formatea un String alineandolo a la derecha relleno con vacios a la izquierda
     * @param string, string a formatear, no vacio
     * @param len, largo del string final
     * @return, retorna el string formateado o null cuando hay error
     */
    public static String setStringAlignDerRellenoIzq(String string, int len){
    	StringBuffer auxString = new StringBuffer();
        try{
            int lenDif = len - string.length();
            if (string.length() == 0){            
                return null;
            }            
            if( lenDif < 0){            	
                return string.substring(0,len);
            }
            for ( int i = 0; i < lenDif; i++ ) {
                auxString.append(" ");
            }
            auxString.append(string);
            return auxString.toString();
        }
        catch (RuntimeException ex) {            
            return null;
        }
        catch( Exception e ) {
            
            return null;
        }
    }
    /**
     * Formatea un String alineandolo a la izquierda relleno con vacios a la derecha
     * @param string, string a formatear, no vacio
     * @param len, largo del string final
     * @return retorna el string formateado o null cuando hay error
     */
    public static String setStringAlignIzqRellenoDer(String string, int len){
    	StringBuffer auxString = new StringBuffer();
        try{
            int lenDif = len - string.length();
            if (string.length() == 0){            
                return null;
            }            
            if( lenDif < 0){            	
                return string.substring(0,len);
            }
            auxString.append(string);
            for ( int i = 0; i < lenDif; i++ ) {
                auxString.append(" ");
            }
           return auxString.toString();
        }
        catch (RuntimeException ex) {            
            return null;
        }
        catch( Exception e ) {
            
            return null;
        }
    }
    /**
	 * formatea texto con largo máximo, rellena con espacios en blanco
	 * @param String cadena
	 * @param int largo
	 * @param int alineamiento
	 * @param char caracter de relleno
	 * @return
	 */
	public static String formatField(String cadena, int largocampo, int alineamiento, String caracter){
		
		int largocadena;
		String out = "";
		
		if ( cadena == null){
			return addCharToString(out,caracter,largocampo,alineamiento);
		}
		
		largocadena = cadena.length();
		
		if ( largocadena > largocampo )
			out = cadena.substring(0,largocampo);
		else
			out = addCharToString(cadena,caracter,largocampo,alineamiento);
	
		return out;
		
	}

	/**
	 * Agrega caracteres a un string
	 * @param str String original
	 * @param chr String(caracter) que se agregara a string original
	 * @param len Largo final de string modificado
	 * @param pos Posicion donde se alineará el texto original (ALIGN_LEFT, ALIGN_RIGHT)
	 * @return - String modificado
	 */
	public static String addCharToString(String str, String chr, int len, int pos) {
		String tmp = new String();
		len = len - str.length();

		for ( int cont = 0; cont < len; cont++ )
			tmp = tmp + chr.charAt(0);

		if (pos == ALIGN_RIGHT)
			str = tmp + str;

		if (pos == ALIGN_LEFT)
			str = str + tmp;

		return str;
	}
	
	/**
	 * método que obtiene la fecha y hora actual
	 * @return String
	 */
	public static String getFecHoraActualStr(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(FRM_DATE_TIME_STR);
        Date date = new Date();
        return bartDateFormat.format(date);
	}

    /**
     * Devuelve solo la hora (hh:mm) del string fecha hora (dd-mm-yyyy hh:mm:ss)
     * @param fcHoraEntrega
     * @return
     */
    public static String frmHora(String fecha) {
        if ( fecha != null) {
            if (fecha.length() >= 16) {
                return fecha.substring(11,16);
            }
        }
        return "";
    }	
    
    /**
     * Devuelve solo la hora, sin segundos del string hh:mm:ss
     * @param fcHoraEntrega
     * @return
     */
    public static String frmHoraSola(String fecha) {
        if ( fecha != null) {
            if (fecha.length() >= 5) {
                return fecha.substring(0,5);
            }
        }
        return "";
    }
    
    public static String fechaHora(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME);
        return simpleDateFormat.format(date);
	}
    
    public static String fecha(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE);
        return simpleDateFormat.format(date);
	}
    
    public static Date toDate(String date) throws ParseException{
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE);
       return simpleDateFormat.parse(date);
    }
    
    public static String hora(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
	}

    /**
     * Obtiene fecha del tipo Date, segun la cadena y patron
     * 
     * @param  fecha
     * @param patron
     * @return Date
     */
    public static Date getFechaDateByStringAndPatron(String fecha, String patron){
        try {           
            return new SimpleDateFormat(patron).parse(fecha);
        } catch(Exception E) {
            return new Date();
        }
    }
    
    /**
     * @param tipoPedido
     * @return
     */
    public static String getDescripcionOrigenPedido(String origenPedido) {
        if (Constantes.ORIGEN_WEB_CTE.equalsIgnoreCase(origenPedido)) {
            return Constantes.ORIGEN_WEB_TXT;     
        } else if (Constantes.ORIGEN_VE_CTE.equalsIgnoreCase(origenPedido)) {
            return Constantes.ORIGEN_VE_TXT;     
        } else if (Constantes.ORIGEN_CASOS_CTE.equalsIgnoreCase(origenPedido)) {
            return Constantes.ORIGEN_CASOS_TXT;     
        } else if (Constantes.ORIGEN_JV_CTE.equalsIgnoreCase(origenPedido)) {
            return Constantes.ORIGEN_JV_TXT;     
        }
        return "--";
    }

    /**
     * @param tipo_despacho
     * @return
     */
    public static String getDescripcionTipoDespacho(String tipoDespacho) {
        if (Constantes.TIPO_DESPACHO_ECONOMICO_CTE.equalsIgnoreCase(tipoDespacho)) {
            return Constantes.TIPO_DESPACHO_ECONOMICO_TXT;
        } else if (Constantes.TIPO_DESPACHO_EXPRESS_CTE.equalsIgnoreCase(tipoDespacho)) {
            return Constantes.TIPO_DESPACHO_EXPRESS_TXT;
        } else if (Constantes.TIPO_DESPACHO_RETIRO_CTE.equalsIgnoreCase(tipoDespacho)) {
            return Constantes.TIPO_DESPACHO_RETIRO_TXT;
        }else if (Constantes.TIPO_DESPACHO_UMBRAL_CTE.equalsIgnoreCase(tipoDespacho)) {
            return Constantes.TIPO_DESPACHO_UMBRAL_TXT;
        } 
        return "Normal";
    }

    /**
     * @param cumplimiento
     * @return
     */
    public static String getDescripcionCumplimiento(String cumplimiento) {
        if (Constantes.CUMPLIMIENTO_CTE_ADELANTADO.equalsIgnoreCase(cumplimiento)) {
            return Constantes.CUMPLIMIENTO_DESC_ADELANTADO;
        } else if (Constantes.CUMPLIMIENTO_CTE_RETRASADO.equalsIgnoreCase(cumplimiento)) {
            return Constantes.CUMPLIMIENTO_DESC_RETRASADO;
        } else if (Constantes.CUMPLIMIENTO_CTE_EN_TIEMPO.equalsIgnoreCase(cumplimiento)) {
            return Constantes.CUMPLIMIENTO_DESC_EN_TIEMPO;
        }
        return "--";
    }
    
    public static String getDescripcionReprogramada(int repro) {
        if (repro == 0) {
            return "No";
        }
        return "Si";
    }

    public static String getHoraActualBtnPago(){
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(BTN_PAGO_FRM_HORA);
        Date date = new Date();
        return bartDateFormat.format(date);
	}
    
    public static String getDescripcionMedioPago(String mp) {
        if (Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(mp)) {
            return Constantes.MEDIO_PAGO_TBK_DESC;
        } else if (Constantes.MEDIO_PAGO_CAT.equalsIgnoreCase(mp)) {
            return Constantes.MEDIO_PAGO_CAT_DESC;
        }
        return "--";
    }
    
    public static String getDescripcionTipoDocumento(String doc) {
        if (Constantes.TIPO_DOC_FACTURA.equalsIgnoreCase(doc)) {
            return Constantes.TIPO_DOC_FACTURA_DESC;
        } else if (Constantes.TIPO_DOC_BOLETA.equalsIgnoreCase(doc)) {
            return Constantes.TIPO_DOC_BOLETA_DESC;
        }
        return "--";
    }
    
    /**
     * Agrega los puntos
     * 
     * @return Texto formateado
     */
    public static String formatoRut(long num) {
        String numero = String.valueOf(num);
        String numero_out = "";
        int j = 1;
        for (int i = numero.length(); i > 0; i--) {
            numero_out += numero.charAt(i - 1) + "";
            if (j != numero.length() && j % 3 == 0)
                numero_out += ".";
            j++;
        }
        numero = "";
        for (int i = numero_out.length() - 1; i >= 0; i--)
            numero += numero_out.charAt(i);
        return numero;
    }
    
    public static String calendarToString(Calendar date, String patron) {
        String strdate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(patron);
        if (date != null)
            strdate = sdf.format(date.getTime());
        return strdate;
    }
    
    public static String fechaLiberacionReserva(String medioPago, String fechaIngresoPedido, String patron, int diasResguardo) {
        Date date = Formatos.getFechaDateByStringAndPatron(fechaIngresoPedido, "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if ( Constantes.MEDIO_PAGO_TBK.equalsIgnoreCase(medioPago)) {
            cal.add(Calendar.DAY_OF_YEAR, ( Constantes.DIAS_RESERVADOS_TBK - diasResguardo ) );
        } else {
            cal.add(Calendar.DAY_OF_YEAR, ( Constantes.DIAS_RESERVADOS_CAT - diasResguardo ) );            
        }
        return Formatos.calendarToString(cal,patron);        
    }
    
    public static String cambioFormatoFecha(String fecha, String patronIn, String patronOut) {
        Date date = Formatos.getFechaDateByStringAndPatron(fecha, patronIn);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Formatos.calendarToString(cal,patronOut);  
        
    }
    
    /*
     * 
     * DESDE EL FO
     * */
    
    /**
     * J
     * @param numero
     * @return
     */
    public static String cantidadFO(Number numero){
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("######.###", dfs);
        return df.format(numero);
    }

    /**
     * Formato definido para el precio
     * 
     * @param num
     *            Número a formatear
     * @return Texto formateado
     */
    public static String formatoPrecioFO(double num) {

        String numero = Math.round(num) + "";
        String numero_out = "";
        int j = 1;
        for (int i = numero.length(); i > 0; i--) {
            numero_out += numero.charAt(i - 1) + "";
            if (j != numero.length() && j % 3 == 0)
                numero_out += ".";
            j++;
        }
        numero = "$";
        for (int i = numero_out.length() - 1; i >= 0; i--)
            numero += numero_out.charAt(i);
        return numero;

    }

    /**
     * Formato definido para un valor de intervalo
     * 
     * @param num
     *            Número a formatear
     * @return Texto formateado
     */
    public static String formatoIntervaloFO(double num) {

        double num_aux = Math.rint(num * 1000) / 1000;
        if (Math.floor(num_aux) == num_aux)
            return Math.round(Math.ceil(num_aux)) + "";
        return Math.rint(num_aux * 1000) / 1000 + "";

    }

    /**
     * Formato definido para la cantidad
     * 
     * @param num
     *            Número a formatear
     * @return Texto formateado
     */
    public static long formatoCantidadFO(double num) {
        return Math.round(Math.ceil(num));
    }

    /**
     * Formato definido para unidad de medida
     * 
     * @param unidad
     *            Unidad a formatear
     * @return Texto formateado
     */
    public static String formatoUnidadFO(String unidad) {

        String texto = "";

        if ("KG".equals(unidad)) {
            texto = "Kg";
        } else
            texto = "Und";

        return texto;
    }

    /**
     * Elimina caracteres no válidos
     * 
     * @param valor
     *            Texto a revisar
     * @return Texto revisado
     */
    public static String stringToSearchFO(String valor) {

        String res = "";

        res = valor.replaceAll("[\\s]+", " ");
        res = res.replaceAll("[^A-Za-z0-9 áÁéÉíÍóÓúÚñÑüÜ]+", "");

        return res;

    }
    
    public static String sanitizeAccentsFO(String word)
    {
    	String wSanitize = "";
    	if(word != null){
    		wSanitize = word.replace('á', 'a').replace('é', 'e').replace('í', 'i').replace('ó', 'o').replace('ú', 'u');
    		wSanitize = wSanitize.replace('Á', 'A').replace('É', 'E').replace('Í', 'I').replace('Ó', 'O').replace('Ú', 'U');
    	}
    	return wSanitize;
    }
    
    public static String sanitizeEneFO(String word)
    {
    	String wSanitize = "";
    	if(word != null){
    		wSanitize = word.replace('ñ', 'n');
    		wSanitize = wSanitize.replace('Ñ', 'N');
    	}
    	return wSanitize;
    }
    
    /**
     * mejorado
     * @param num
     * @param sw
     * @return
     */
    public static String formatoPrecioFO(double num, boolean sw) {

    	NumberFormat formatter = NumberFormat.getCurrencyInstance();
    	String moneyString = "";
    	try{ 
            moneyString = formatter.format(num);
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	
      return moneyString;
    }
    
    /**
     * 
     * @param cadena
     * @return
     */
    public static long unFormatoPrecioFO(String cadena) {

        //String numero = Math.round(num) + "";
        String numero_out = "";
        long numero = 0L;
        int j = 1;
        try{
	        char strArray[] = cadena.toCharArray();
	        for(int i=0; i < strArray.length; i++){
	        	 if( isNumeroFO(strArray[i]))
	        		 numero_out += strArray[i];
	         }
	        numero = Long.parseLong(numero_out);
        }catch (Exception e) {
			// TODO: handle exception
		}
        return numero;
        }
       
        
    /**
     * 
     * @param c
     * @return
     */
    private static boolean isNumeroFO(char c) {
		// TODO Auto-generated method stub
    	String[] digito = {"0","1","2","3","4","5","6","7","8","9"};
    	boolean value = false;
    	try{
    		String valor = c +" ";
    		for (int i = 0; i < digito.length; i++) {
				if( digito[i].equals(valor.trim())){
					value = true;
					break;
				}
			}
    	}catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

    
}
