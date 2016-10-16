package cl.bbr.irsmonitor.utils;


import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Utiles {

	
	//
	// Constantes de alineacion
	//
	/** Tipo de alineación a la izquierda */
	public static final int ALIGN_LEFT			= 0;
	/** Tipo de alineación a la derecha */
	public static final int ALIGN_RIGHT			= 1;

	//
	// Manejo de log's y trace
	//
	private static String		stringTrace		= "trace-->";
	private static String		stringError		= "Error-->";
	private static String		stringWarning	= "alerta-->";
	private static boolean		flagTrace		= false;
	private static FileWriter	fileLog;
	private static String		fileLogName;
	private static String		fileLogOldName;
	private static int			fileLogMaxLen;

	//
	// Descripcion de meses
	//
	public static String[] meses_year = { "ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC" };


	/**
	 * @param name Nombre del archivo de log.
	 * @param max_len Maximo tamaño archivo.
	 * @param append_mode boolean indica escritura en el archivo.
	 * @return Flag boolean que indica si el proceso de escritura se realizó con exito.
	 */
	public static boolean openTrace( String name, int max_len, boolean append_mode ) {
		// Se crea y abre el archivo trace y de error para escritura
		try {
			setFileLogName( name + ".log" );
			setFileLogOldName( name + "Old.log" );
			setFileLogMaxLen( max_len );
			fileLog = new FileWriter( getFileLogName() , append_mode );
			return true;
		}
		catch (IOException eio) {
			System.out.println( "IOException: Utiles : openTrace --->" + eio );
			return false;
		}
		catch (RuntimeException ex) {
			System.out.println( "RuntimeException: Utiles : openTrace --->" + ex );
			return false;
		}
		catch( Exception e ) {
			System.out.println( "Exception: Utiles : openTrace --->" + e );
			return false;
		}
	}

	/**
	 * Despliega por pantalla o escribe en archivo los mensajes de trace segun 
	 * indique flag de trace. 
	 * @param buffer Contiene mensaje a ser desplegado o escrito en archivo.
	 */
	public static void writeTrace( String buffer ) {
		try {

			if ( isFlagTrace() ) {
				// Desplegar en pantalla del S.O.

				File fichero = new File( getFileLogName() );
				if ( fichero.length() > getFileLogMaxLen() ){
					File fichero_old=new File( getFileLogOldName() );
					if (getFileLog() != null)
						getFileLog().close();
					boolean resp = fichero.renameTo(fichero_old);
					if ( resp = false){
						System.out.println( stringWarning + "No se pudo copiar el archivo de error" );
					}
					resp = fichero.delete();
					if ( resp = false){
						System.out.println( stringWarning + "No se pudo borrar el archivo de error" );
					}

					setFileLog(new FileWriter( getFileLogName(), true));
				}

				if (getFileLog() != null) {
					getFileLog().write( stringTrace + Utiles.obtenerFechaActualMilisegundos() + " " + buffer + "\n");
					getFileLog().flush();
				}
			}
		} 
		catch (FileNotFoundException efn) {
			System.out.println(  "FileNotFoundException: Utiles : writeTrace --->" + efn );
		}
		catch (IOException eio) {
			System.out.println( "IOException: Utiles : writeTrace --->" + eio );
		}
		catch (RuntimeException ex) {
			System.out.println( "RuntimeException: Utiles : writeTrace --->" + ex );
		}
		catch( Exception e ) {
			System.out.println( "Exception: Utiles : writeTrace --->" + e );
		}
	}

	/**
	 * Despliega errores, independientemente del flag de trace
	 * @param buffer Contiene mensaje a ser desplegado.
	 */
	public static void writeError( String buffer ) {
		try {
			File fichero=new File( getFileLogName() );
			if ( fichero.length() > getFileLogMaxLen() ){
				File fichero_old=new File( getFileLogOldName() );
				if (getFileLog() != null)
					getFileLog().close();
				boolean resp = fichero.renameTo(fichero_old);
				if ( resp = false){
					System.out.println( stringWarning + "No se pudo copiar el archivo de error" );
				}
				resp = fichero.delete();
				if ( resp = false){
					System.out.println( stringWarning + "No se pudo borrar el archivo de error" );
				}
                    
				setFileLog(new FileWriter( getFileLogName(), true));
			}

			if (getFileLog() != null) {
				getFileLog().write( stringError + Utiles.obtenerFechaActualMilisegundos() + " " + buffer + "\n");
				getFileLog().flush();
			}
		} 
		catch (FileNotFoundException efn) {
			System.out.println(  "FileNotFoundException: Utiles : writeError --->" + efn );
		}
		catch (IOException eio) {
			System.out.println( "IOException: Utiles : writeError --->" + eio );
		}
		catch (RuntimeException ex) {
			System.out.println( "RuntimeException: Utiles : writeError --->" + ex );
		}
		catch( Exception e ) {
			System.out.println( "Exception: Utiles : writeError --->" + e );
		}
	}

	/**
     * Despliega el error grave y se sale de la aplicacion!!
	 * @param buffer Contiene mensaje a ser desplegado.
	 */
	public static void writeExitError( String buffer ) {
		try {

			writeError( stringError + buffer );
			if (getFileLog() != null)
				getFileLog().close();
		} 
		catch (IOException eio) {
			System.out.println( "IOException: Utiles : writeExitError --->" + eio );
		}
		catch (RuntimeException ex) {
			System.out.println( "RuntimeException: Utiles : writeExitError --->" + ex );
		}
		catch( Exception e ) {
			System.out.println( "Exception: Utiles : writeExitError --->" + e );
		}
		System.exit( 1 );
	}

	/**
	 * Obtiene la fecha actual del sistema, incluidos milisegundos.
	 * @return String con la fecha en formato DD/MES/AAAA HH:MM:SS:mmm.
	 */
	public static String obtenerFechaActualMilisegundos(){
		StringBuffer fecha = new StringBuffer();
		Calendar calendar = new GregorianCalendar();
		Date actualTime = new Date();

		String cbMes[] =   {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
		try{
			calendar.setTime(actualTime);

			// Obtener dia
			int valorFecha = calendar.get(Calendar.DAY_OF_MONTH);
			if (valorFecha < 10){
				fecha.append("0" + String.valueOf(valorFecha) + "/");
			}
			else{
				fecha.append(String.valueOf(valorFecha) + "/");
			}

			// Obtener mes
			valorFecha = calendar.get(Calendar.MONTH);
			fecha.append(cbMes[valorFecha] + "/");

			// Obtener year
			valorFecha = calendar.get(Calendar.YEAR);
			fecha.append(String.valueOf(valorFecha) + " ");

			// Obtener horas        
			valorFecha = calendar.get(Calendar.HOUR_OF_DAY);
			if (valorFecha < 10){
				fecha.append("0" + String.valueOf(valorFecha) + ":");
			}
			else{
				fecha.append(String.valueOf(valorFecha) + ":");
			}

			// Obtener minutos
			valorFecha = calendar.get(Calendar.MINUTE);
			if (valorFecha < 10){
				fecha.append("0" + String.valueOf(valorFecha) + ":");
			}
			else{
				fecha.append(String.valueOf(valorFecha) + ":");
			}

			// Obtener segundos
			valorFecha = calendar.get(Calendar.SECOND);
			if (valorFecha < 10){
				fecha.append("0" + String.valueOf(valorFecha));
			}
			else{
				fecha.append(String.valueOf(valorFecha));
			}

		   // Obtener milisegundos
		   valorFecha = calendar.get(Calendar.MILLISECOND);
		   if ( String.valueOf(valorFecha).length() < 2)
			   fecha.append(":00" + String.valueOf(valorFecha));
		   else{
			   if ( String.valueOf(valorFecha).length() == 2)
				   fecha.append(":0" + String.valueOf(valorFecha));
			   else
				   fecha.append(":" + String.valueOf(valorFecha));
		   }

			return fecha.toString();
	   }
	   catch (RuntimeException ex) {
		   Utiles.writeError( "Utiles: RuntimeException: obtenerFechaActualMilisegundos --->" + ex );
		   return null;
	   }
	   catch( Exception e ) {
		   Utiles.writeError( "Utiles: Exception: obtenerFechaActualMilisegundos --->" + e );
		   return null;
	   }
	}

	/**
	 * Retorna el archivo de log.
	 * @return - Retorna fileLog
	 */
	public static FileWriter getFileLog() {
		return fileLog;
	}

	/**
     * Setea archivo de log.
	 * @param writer - Setear writer
	 */
	public static void setFileLog(FileWriter writer) {
		fileLog = writer;
	}

	/**
	 * Indica si el flag de escritura de archivo de trace esta habilitado.
	 * @return El estado del trace (habilitado=true, deshabilitado=false)
	 */
	public static boolean isFlagTrace() {
		return flagTrace;
	}

	/**
	 * Setea el flag de estado del trace.
	 * @param b Configura el estado del trace
	 */
	public static void setFlagTrace(boolean b) {
		flagTrace = b;
	}


	/**
	 * Convierte un arreglo de bytes a un string
	 * @param bt Arreglo de bytes a convertir.
	 * @param offSet Indice de comienzo en arreglo de bytes.
	 * @param len Total de bytes a convertir.
	 * @return - string convertido desde el arreglo de bytes.
	 */
	public static String bytesToString(byte bt[], int offSet, int len) {
		return new String(bt, offSet, len);
	}

	/**
	 * Agrega una cadena string a un arreglo de bytes.
	 * @param array Arreglo de bytes de destino.
	 * @param pos Indice de posición de comienzo en el arreglo.
	 * @param str Cadena string a agregar al arreglo de bytes.
	 * @throws Exception
	 */
	public static void addStringToByteArray(byte[] array, int pos, String str) throws Exception {
		for (int i = 0; i < str.length(); i++) {
			array[pos + i] = (byte) str.charAt(i);
		}
	}

	/**
	 * Agrega un byte a un arreglo de bytes.
	 * @param array Arreglo de bytes de destino.
	 * @param pos Indice de posición donde se agrega el byte.
	 * @param b byte que se agregará.
	 * @throws Exception
	 */
	public static void addByteToByteArray(byte[] array, int pos, byte b) throws Exception {
		array[pos] = b;
	}

	/**
	 * Agrega caracteres a un string
	 * @param str String original
	 * @param chr String(caracter) que se agregara a string original
	 * @param len Largo final de string modificado
	 * @param pos Posicion donde se agregaran los caracteres (ALIGN_LEFT, ALIGN_RIGHT)
	 * @return - String modificado
	 */
	public static String addCharToString(String str, String chr, int len, int pos) {
		String tmp = new String();
		len = len - str.length();

		for ( int cont = 0; cont < len; cont++ )
			tmp = tmp + chr.charAt(0);

		if (pos == Utiles.ALIGN_LEFT)
			str = tmp + str;

		if (pos == Utiles.ALIGN_RIGHT)
			str = str + tmp;

		return str;
	}

	/**
	 * Empaqueta un string y lo traspasa a un arreglo de bytes.
	 * @param array Arreglo de bytes que se empaquetará.
	 * @param Offset Indica la posicion donde se comenzará a dejar los datos empaquetados. 
	 * @param value Cadena de string a empaquetar.
	 */
	 public static void pack(byte[] array, int Offset, String value) {
		 // Si es impar el total de digitos se agrega un 0 a la izquierda
		 if ( value.length() % 2 == 1 )
			 value = (char) 0xFF + value;

		 // Se hace un pack por cada par de bytes del array
		 for ( int i=0,j=Offset; i<value.length(); i+=2,j++ )
			 array[j] = (byte) ( ( ( value.charAt(i) & 0x0F ) << 4 ) + ( value.charAt(i+1) & 0x0F ) );
	 }

	/**
	 * Desempaqueta un arreglo de bytes y lo traspasa a una cadena string.
	 * @param array Arreglo de bytes a desempaquetar.
	 * @param pos Indica la posicion donde se comenzará a desempaquetar los datos.
	 * @param len Total de bytes a desempaquetar.
	 * @return String con datos desempaquetados.
	 */
	 public static String unpack(byte[] array, int pos, int len) {
		 byte ret[] = new byte[len*2];
		 byte num;
		 int w = 0;

		 for ( int i=pos,cont=0; cont<len; i++,cont++ ) {
			 ret[w] = (byte) ( ( (array[i] & 0xF0) >> 4) | 0x30 );
			 ret[w+1] = (byte) ( (array[i] & 0x0F) | 0x30 );
			 w=w+2;
			 }
		 return new String(ret);
	 }

	/**
	 * Convierte un arreglo de bytes hexadecimal a integer.
	 * @param array Arreglo de bytes a convertir.
	 * @return - valor entero
	 */
	public static int hexToInt(byte[] array) {
		return Integer.parseInt(String.valueOf(array), 16);
	}

	/**
	 * Convierte un byte hexadecimal a integer.
	 * @param bt Byte a convertir.
	 * @return - valor entero
	 */
	public static int hexToInt(byte bt) {
		return Integer.parseInt(String.valueOf(bt), 16);
	}

	/**
	 * Convierte un string hexadecimal a integer.
	 * @param hex String a convertir.
	 * @return - valor entero
	 */
	public static int hexToInt(String hex) {
		return Integer.parseInt(hex,16);
	}

	/**
	 * Convierte un arreglo de bytes hexadecimal a long
	 * @param array Arreglo de bytes a convertir.
	 * @return - valor long
	 */
	public static long hexToLong(byte[] array) {
		return Long.parseLong(String.valueOf(array), 16);
	}

	/**
	 * Convierte un byte hexadecimal a long
	 * @param bt Byte a convertir.
	 * @return - valor long
	 */
	public static long hexToLong(byte bt) {
		return Long.parseLong(String.valueOf(bt), 16);
	}

	/**
	 * Convierte un string hexadecimal a long
	 * @param hex String a convertir.
	 * @return - valor long
	 */
	public static long hexToLong(String hex) {
		return Long.parseLong(hex,16);
	}

	/**
	 * Agrega un arreglo de bytes a otro arreglo de bytes.
	 * @param array1 Arreglo de bytes de destino.
	 * @param pos Posicion de inicio(arreglo de destino).
	 * @param array2 Arreglo de bytes de origen. 
	 * @param len Total de bytes a copiar.
	 * @throws Exception
	 */
	public static void addArrayByteToByteArray(byte[] array1, int pos, byte[]array2, int len ) throws Exception {
		for (int i = 0; i < len; i++) {
			array1[pos + i] = (byte)array2[i];
		}
	}

	/**
	 * Agrega un arreglo de bytes a otro arreglo de bytes, indicando inicio en arreglo de origen y destino.
	 * @param array1 Arreglo de bytes de destino.
	 * @param pos1 Posicion de inicio en arreglo de destino.
	 * @param array2 Arreglo de bytes de origen.
	 * @param pos2 Posicion de inicio en arreglo de origen.
	 * @param len Total de bytes a copiar.
	 * @throws Exception
	 */
	public static void addArrayByteToByteArray(byte[] array1, int pos1, byte[]array2, int pos2, int len ) throws Exception {
		for (int i = 0; i < len; i++) {
			array1[pos1 + i] = (byte)array2[pos2 + i];
		}
	}

	/**
	 * Despliega mensaje de trace
	 * @param str
	 */
	public static void displayTrace(String str) {
		System.out.println(str);
	}

	/**
	 * Despliega mensaje de error
	 * @param str
	 */
	public static void displayError(String str) {
		System.out.println(str);
	}

	/**
	 * Trasforma un monto en numeros a palabras.
	 * @param mountInt Valor entero a transformar.
	 * @return string con monto en palabras.
	 */
	public static String numberToWords(int mountInt) {
		StringBuffer mountAux = new StringBuffer("");
		String mountStr = new String("");
		int len = 0;
		int valChar = 0;
		int valDec = 0;
		int valY = 0;
		boolean concatUnit = true;
		boolean concatY = false;
		
		mountStr = String.valueOf(mountInt);	
		len = mountStr.length();
	
		if (len==7) {
			valChar = Integer.parseInt(mountStr.substring(0,1));	
			switch (valChar) {
				case 1: mountAux.append("un millon ");		 break;
				case 2: mountAux.append("dos millones ");	 break;
				case 3: mountAux.append("tres millones ");	 break;
				case 4: mountAux.append("cuatro millones "); break;
				case 5: mountAux.append("cinco millones ");	 break;
				case 6: mountAux.append("seis millones ");	 break;
				case 7: mountAux.append("siete millones ");	 break;
				case 8: mountAux.append("ocho millones ");   break;
				case 9: mountAux.append("nueve millones ");  break;
			}
			mountStr = mountStr.substring(1,len);
			len = mountStr.length();
		}
	
		if (len==6) {
			valChar = Integer.parseInt(mountStr.substring(0,1));
			switch (valChar) {
				case 1: 
					if (Integer.parseInt(mountStr.substring(1,3)) > 0) {
						mountAux.append("ciento ");
					} else
						mountAux.append("cien ");
					break;
				case 2: mountAux.append("doscientos ");		break;
				case 3: mountAux.append("trescientos ");	break;
				case 4: mountAux.append("cuatrocientos "); 	break;
				case 5: mountAux.append("quinientos ");		break;
				case 6: mountAux.append("seiscientos "); 	break;
				case 7: mountAux.append("setecientos ");	break;
				case 8: mountAux.append("ochocientos ");	break;
				case 9: mountAux.append("novecientos ");	break;
			}
			mountStr = mountStr.substring(1,len);
			len = mountStr.length();
		}
	
		if (len==5) {
			valChar = Integer.parseInt(mountStr.substring(0,1));
			valY = Integer.parseInt(mountStr.substring(1,3));
			valDec = Integer.parseInt(mountStr.substring(0,2));
			if (valY > 0)
				concatY = true;
				
			switch (valChar) {
				case 1: 
				    if (valDec < 16) {
				    	switch (valDec){
							case 10: mountAux.append("diez "); break;
							case 11: {
								mountAux.append("once mil ");
								concatUnit = false; 
								break;
							}
							case 12: {
								mountAux.append("doce mil ");
								concatUnit = false; 
								break;
							}
							case 13: {
								mountAux.append("trece mil "); 
								concatUnit = false;
								break;
							}
							case 14: {
								mountAux.append("catorce mil "); 
								concatUnit = false;
								break;
							}
							case 15: {
								mountAux.append("quince mil "); 
								concatUnit = false;
								break;
							}
						}
				    } else
						mountAux.append("diez ");	
				    break;
				case 2: mountAux.append("veinte ");		break;
				case 3: mountAux.append("treinta ");	break;
				case 4: mountAux.append("cuarenta ");	break;
				case 5: mountAux.append("cincuenta ");	break;
				case 6: mountAux.append("sesenta ");	break;
				case 7: mountAux.append("setenta ");	break;
				case 8: mountAux.append("ochenta ");	break;
				case 9: mountAux.append("noventa ");	break;
			}
			if (concatY && concatUnit)
				mountAux.append("y ");
				
			mountStr = mountStr.substring(1,len);
			len = mountStr.length();
		}

		if (len==4) {
			valChar = Integer.parseInt(mountStr.substring(0,1));
			switch (valChar) {
				case 0:	if (concatUnit)
							mountAux.append("mil ");		
						break;
				case 1: if (concatUnit)
							mountAux.append("un mil ");	
						break; 
				case 2: if (concatUnit)
							mountAux.append("dos mil ");	
						break;
				case 3: if (concatUnit)
							mountAux.append("tres mil ");	
						break;
				case 4: if (concatUnit)
							mountAux.append("cuatro mil ");	
						break;
				case 5: if (concatUnit) 
							mountAux.append("cinco mil ");	
						break;
				case 6: mountAux.append("seis mil ");	break;
				case 7: mountAux.append("siete mil ");	break;
				case 8: mountAux.append("ocho mil ");	break;
				case 9: mountAux.append("nueve mil ");	break;
			}
			mountStr = mountStr.substring(1,len);
			len = mountStr.length();
			concatUnit = true;
		}

		if (len==3) {
			valChar = Integer.parseInt(mountStr.substring(0,1));
			switch (valChar) {
				case 1: 
					if (Integer.parseInt(mountStr.substring(1,3)) > 0) {
						mountAux.append("ciento ");
					} else
						mountAux.append("cien ");
					break;
				case 2: mountAux.append("doscientos ");	   break;
				case 3: mountAux.append("trescientos ");   break;
				case 4: mountAux.append("cuatrocientos "); break;
				case 5: mountAux.append("quinientos ");    break;
				case 6: mountAux.append("seiscienros ");   break;
				case 7: mountAux.append("setecientos ");   break;
				case 8: mountAux.append("ochocientos ");   break;
				case 9: mountAux.append("novecientos ");   break;
			}
			mountStr = mountStr.substring(1,len);
			len = mountStr.length();
		}
	
		if (len==2) {
			valDec = 0;
			valY = 0;
			valChar = Integer.parseInt(mountStr.substring(0,1));
			valDec = Integer.parseInt(mountStr.substring(0,2));
			valY = Integer.parseInt(mountStr.substring(1,2));
			
			if (valY > 0)
				concatY = true;
			
			switch (valChar) {
				case 1: 
				 	switch (valDec){
						case 10: mountAux.append("diez "); break;
						case 11: {
							mountAux.append("once ");
							concatUnit = false; 
							break;
						}
						case 12: {
							mountAux.append("doce ");
							concatUnit = false; 
							break;
						}
						case 13: {
							mountAux.append("trece "); 
							concatUnit = false;
							break;
						}
						case 14: {
							mountAux.append("catorce "); 
							concatUnit = false;
							break;
						}
						case 15: {
							mountAux.append("quince "); 
							concatUnit = false;
							break;
						}
				 	}
				 	break;
				case 2: mountAux.append("veinte ");    break;
				case 3: mountAux.append("treinta ");   break;
				case 4: mountAux.append("cuarenta ");  break;
				case 5: mountAux.append("cincuenta "); break;
				case 6: mountAux.append("sesenta ");   break;
				case 7: mountAux.append("setenta ");   break;
				case 8: mountAux.append("ochenta ");   break;
				case 9: mountAux.append("noventa ");   break;
			}
			
			if (concatY && concatUnit)
				mountAux.append("y ");
			
			mountStr = mountStr.substring(1,len);
			len = mountStr.length();
		}

		if (len==1) {
			valChar = Integer.parseInt(mountStr.substring(0,1));
			switch (valChar) {
				case 1:	if (concatUnit)
							mountAux.append("uno");		
						break; 
				case 2: if (concatUnit)
							mountAux.append("dos");		
						break;
				case 3: if (concatUnit) 
							mountAux.append("tres");	
						break;
				case 4: if (concatUnit) 
							mountAux.append("cuatro ");	
						break;
				case 5: if (concatUnit)
							mountAux.append("cinco");	
						break;
				case 6: mountAux.append("seis");	break;
				case 7: mountAux.append("siete");	break;
				case 8: mountAux.append("ocho");	break;
				case 9: mountAux.append("nueve");	break;
			}
		}
		return mountAux.toString(); 
	}

	/**
	 * Formatea un monto entero con puntos o coma.
	 * @param number Número entero a formatear.
	 * @param type Tipo de formateo si es 1 formatea con puntos(.), si es 2 con comas(,)
	 * @return String con numero formateado.
	 */
	public static String formatNumber(int number, int type){
		String numberString = new String("");
		
		if (type==1) {
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN);
			numberString = nf.format(number);
		}	
		if (type==2) { 
			NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
			numberString = nf.format(number);
		}
		return numberString;		
	}

    /**
     * Formatea un monto long con puntos o coma.
     * @param number Número Long a formatear.
     * @param type Tipo de formateo si es 1 formatea con puntos(.), si es 2 con comas(,)
     * @return String con numero formateado.
     */
    public static String formatLong(long number, int type){
        String numberString = new String("");
        
        if (type==1) {
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.ITALIAN);
            numberString = nf.format(number);
        }   
        if (type==2) { 
            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
            numberString = nf.format(number);
        }
        return numberString;        
    }
	
	/**
	 * Chequea si un bit esta encendido (Little endian) 
	 * @param num Representacion numerica del byte
	 * @param bit Bit a chequear (posicion)
	 * @return - Flag "true" si bit encendido, "false" si bit apagado.
	 */
	public static boolean checkBit(byte num, int bit) {
		int offset = 0;
		offset = (int)Math.pow(2, bit);
	 
		return ( (num & offset) == offset ) ? true : false;
	}

	/**
	 * Retorna un bit especifico (Little endian) .
	 * @param num Representacion numerica del byte.
	 * @param bit Bit a rescatar (posicion).
	 * @return - String "1" si bit encendido, "0" apagado.
	 */
	public static String getBit(byte num, int bit) {
		return Utiles.checkBit(num, bit) ? "1" : "0";
	}

	/**
	 * Obtiene la fecha actual del sistema.
	 * @return String con la fecha en formato DD/MES/AAAA HH:MM:SS
	 */
	public static String obtenerFechaActual(){
 
		String cbMes[] = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN","JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
		Calendar calendar = new GregorianCalendar();
		Date actualTime = new Date();
		calendar.setTime(actualTime);
	 
		String fecha;
	        
		// Obtener dia
	  	int valorFecha = calendar.get(Calendar.DAY_OF_MONTH);
	  	if (valorFecha < 10){
	   		fecha = "0";
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	} else{
	   		fecha = String.valueOf(valorFecha);
	  	}
	  	fecha = fecha.concat("/");
	    
	  	// Obtener mes
	  	valorFecha = calendar.get(Calendar.MONTH);
	  	fecha = fecha.concat(cbMes[valorFecha]);
	  	fecha = fecha.concat("/");
	 
		// Obtener year
	  	valorFecha = calendar.get(Calendar.YEAR);
	  	fecha = fecha.concat(String.valueOf(valorFecha));
	  	fecha = fecha.concat(" ");
	 
		// Obtener horas        
	  	valorFecha = calendar.get(Calendar.HOUR_OF_DAY);
	  	if (valorFecha < 10){
	   		fecha = fecha.concat("0");
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	} else {
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	}
	  	fecha = fecha.concat(":");
	 
		// Obtener minutos
	  	valorFecha = calendar.get(Calendar.MINUTE);
	 	if (valorFecha < 10){
	   		fecha = fecha.concat("0");
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	} else {
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	}
	  	fecha = fecha.concat(":");
	            
	  	// Obtener segundos
	  	valorFecha = calendar.get(Calendar.SECOND);
	  	if (valorFecha < 10){
	   		fecha = fecha.concat("0");
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	} else {
	   		fecha = fecha.concat(String.valueOf(valorFecha));
	  	}
	    
	  return fecha;
	 }

	/**
	 * Obtiene la fecha actual del sistema
	 * @return String con la fecha en formato DD/MES/AAAA HH:MM AM
	 */
	public static String fechaActual(){
 
		String cbMes[] = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN","JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
		Calendar calendar = new GregorianCalendar();
		Date actualTime = new Date();
		calendar.setTime(actualTime);
     
		String fecha;
            
		// Obtener dia
		int valorFecha = calendar.get(Calendar.DAY_OF_MONTH);
		if (valorFecha < 10){
			fecha = "0";
			fecha = fecha.concat(String.valueOf(valorFecha));
		} else{
			fecha = String.valueOf(valorFecha);
		}
		fecha = fecha.concat("/");
        
		// Obtener mes
		valorFecha = calendar.get(Calendar.MONTH);
		fecha = fecha.concat(cbMes[valorFecha]);
		fecha = fecha.concat("/");
     
		// Obtener year
		valorFecha = calendar.get(Calendar.YEAR);
		fecha = fecha.concat(String.valueOf(valorFecha));
		fecha = fecha.concat(" ");
     
		// Obtener horas        
		valorFecha = calendar.get(Calendar.HOUR);
		if (valorFecha < 10){
			fecha = fecha.concat("0");
			fecha = fecha.concat(String.valueOf(valorFecha));
		} else {
			fecha = fecha.concat(String.valueOf(valorFecha));
		}
		fecha = fecha.concat(":");
     
		// Obtener minutos
		valorFecha = calendar.get(Calendar.MINUTE);
		if (valorFecha < 10){
			fecha = fecha.concat("0");
			fecha = fecha.concat(String.valueOf(valorFecha));
		} else {
			fecha = fecha.concat(String.valueOf(valorFecha));
		}
        
		// Obtener AM 0 PM
		valorFecha = calendar.get(Calendar.AM_PM);
		if (valorFecha == 0)
			fecha = fecha.concat(" AM");
		else
			fecha = fecha.concat(" PM");
        
	  return fecha;
	 }

	/**
	 * Obtiene la fecha actual del sistema.
	 * @return String con la fecha en formato AAAAMMDD
	 */
	public static String obtenerSoloFechaActual(){
 
		Calendar calendar = new GregorianCalendar();
		Date actualTime = new Date();
		calendar.setTime(actualTime);
	 
		String fecha;
	        
		// Obtener year
		int valorFecha = calendar.get(Calendar.YEAR);
		fecha = String.valueOf(valorFecha);

		// Obtener mes
		valorFecha = calendar.get(Calendar.MONTH);
		valorFecha += 1;
		if (valorFecha < 10){
			fecha = fecha.concat("0");
			fecha = fecha.concat(String.valueOf(valorFecha));
		} else{
			fecha = fecha.concat(String.valueOf(valorFecha));
		}
		
		// Obtener dia
		valorFecha = calendar.get(Calendar.DAY_OF_MONTH);
		if (valorFecha < 10){
			fecha = fecha.concat("0");
			fecha = fecha.concat(String.valueOf(valorFecha));
		} else{
			fecha = fecha.concat(String.valueOf(valorFecha));
		}
		
		return fecha;
	 }

	/**
	 * Obtiene la Hora actual del sistema.
	 * @return String con la Hora en formato HHMMSS
	 */
	public static String obtenerSoloHoraActual(){
		Calendar calendar = new GregorianCalendar();
		Date actualTime = new Date();
		calendar.setTime(actualTime);
		
		String hora;
	 
		// Obtener horas        
		int valorHora = calendar.get(Calendar.HOUR_OF_DAY);
		if (valorHora < 10){
			hora = "0";
			hora = hora.concat(String.valueOf(valorHora));
		} else {
			hora = String.valueOf(valorHora);
		}
		
	 
		// Obtener minutos
		valorHora = calendar.get(Calendar.MINUTE);
		if (valorHora < 10){
			hora = hora.concat("0");
			hora = hora.concat(String.valueOf(valorHora));
		} else {
			hora = hora.concat(String.valueOf(valorHora));
		}
		
	            
		// Obtener segundos
		valorHora = calendar.get(Calendar.SECOND);
		if (valorHora < 10){
			hora = hora.concat("0");
			hora = hora.concat(String.valueOf(valorHora));
		} else {
			hora = hora.concat(String.valueOf(valorHora));
		}
		
		return hora;
	}

	 
	 /**
	  * Convierte una numero en formato BCD a decimal.
	  * @param bcd String con numero en formato BCD.  
	  * @return Cadena string con número decimal. 
	  */
	 public static String BCDToDec (String bcd) {
	 	boolean existeData = true;
		String decimal = new String("");
	 	int cont = 0;
	 	int largo = bcd.length();
	 		 	
		if (largo > 2) {
			while (existeData) {
				decimal = decimal.concat(bcd.substring(bcd.length()-2, bcd.length()));
				bcd = bcd.substring(0, bcd.length()-2);
				cont += 2;
				if (cont >= largo)
					existeData = false;
			}
		} else
			decimal = bcd;
				 
	 	return decimal;
	 }

    /**
     * Retorna el largo máximo seteado del archivo log.
     * @return Valor entero.
     */
    public static int getFileLogMaxLen() {
        return fileLogMaxLen;
    }

    /**
     * Retorna el nombre del archivo log definido.
     * @return String con nombre del archivo.
     */
    public static String getFileLogName() {
        return fileLogName;
    }

    /**
     * Retorna el nombre del archivo log de respaldo.
     * @return String con nombre del archivo.
     */
    public static String getFileLogOldName() {
        return fileLogOldName;
    }

    /**
     * Uso interno de la clase - setea el tamaño máximo del archivo log.
     * @param i Indica el tamaño del archivo log.
     */
    private static void setFileLogMaxLen(int i) {
        fileLogMaxLen = i;
    }

    /**
     * Uso interno de la clase - setea el nombre del archivo de log.
     * @param string Nombre del archivo log.
     */
    private static void setFileLogName(String string) {
        fileLogName = string;
    }

    /**
     * Uso interno de la clase - setea el nombre del archivo de log de respaldo.
     * @param string Nombre del archivo log de respaldo.
     */
    private static void setFileLogOldName(String string) {
        fileLogOldName = string;
    }

	/**
	 * Al string lo formatea al largo len con ceros a la izquierda.
	 * @param string String a formatear, no puede ser vacio.
	 * @param len Largo del string final.
	 * @return Retorna el string formateado o null cuando hay error.
	 */
	public static String setInteger(String string, int len){
		try{
			int lenDif = len - string.length();
			if( lenDif < 0){
				Utiles.writeError(" Utiles : setInteger ---> El len variable no corresponde " + string);
				return null;
			}
			StringBuffer auxString = new StringBuffer();
			for ( int i = 0; i < lenDif; i++ ) {
				if( i==0){
					auxString.append("0");
				}
				else{
					auxString.append("0");
				}
			}
			auxString.append(string);
			return auxString.toString();
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : setInteger : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : setInteger : Exception --->" + e );
			return null;
		}
	}

	/**
	 * Al string lo formatea al largo len con ceros a la izquierda.
	 * @param string String a formatear, puede ser vacio.
	 * @param len Largo del string final.
	 * @return Retorna el string formateado o null cuando hay error.
	 */
	public static String setIntegerVacio(String string, int len){
		try{
			int lenDif = len - string.length();
			if( lenDif < 0){
				Utiles.writeError("Utiles : setIntegerVacio ---> El len variable no corresponde " + string);
				return null;
			}
			StringBuffer auxString = new StringBuffer();
			for ( int i = 0; i < lenDif; i++ ) {
				if( i==0){
					auxString.append("0");
				}
				else{
					auxString.append("0");
				}
			}
			auxString.append(string);
			return auxString.toString();
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : setIntegerVacio : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : setIntegerVacio : Exception --->" + e );
			return null;
		}
	}

	/**
	 * Al string lo formatea al largo len con espacios en blanco a la derecha.
	 * @param string String a formatear, no vacio.
	 * @param len Largo del string final.
	 * @return Retorna el string formateado o null cuando hay error
	 */
	public static String setString(String string, int len){
		try{
			int lenDif = len - string.length();
			if (string.length() == 0){
				Utiles.writeError("Utiles : setString ---> La variable no corresponde esta en blanco "+ string);
				return null;
			}
			if( lenDif < 0){
				Utiles.writeError("Utiles : setString ---> El len variable no corresponde " + string);
				return null;
			}
			StringBuffer auxString = new StringBuffer(string);
			for ( int i = 0; i < lenDif; i++ ) {
				auxString.append(" ");
			}
			return auxString.toString();
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : setString : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : setString : Exception --->" + e );
			return null;
		}
	}

    /**
     * public String setString(String string, int len)
     * Al string lo formatea al largo len
     * con espacios en blanco a la derecha
     * @param string, string a formatear, no vacio
     * @param len, largo del string final
     * @return, retorna el string formateado o
     * null cuando hay error
     */
    public static String setStringDer(String string, int len){
        try{
            int lenDif = len - string.length();
            if (string.length() == 0){
                Utiles.writeError("Utiles : setString ---> La variable no corresponde esta en blanco "+ string);
                return null;
            }
            if( lenDif < 0){
                Utiles.writeError("Utiles : setString ---> El len variable no corresponde " + string);
                return null;
            }
            //StringBuffer auxString = new StringBuffer(string);
            StringBuffer auxString = new StringBuffer();
            for ( int i = 0; i < lenDif; i++ ) {
                auxString.append(" ");
            }
            auxString.append(string);
            return auxString.toString();
        }
        catch (RuntimeException ex) {
            Utiles.writeError( "RuntimeException: Utiles : setString --->" + ex );
            return null;
        }
        catch( Exception e ) {
            Utiles.writeError( "Exception: Utiles : setString --->" + e );
            return null;
        }
    }

	/**
	 * Al string lo formatea al largo len con espacios en blanco a la derecha.
	 * @param string String a formatear, puede ser vacio.
	 * @param len Largo del string final.
	 * @return Retorna el string formateado o null cuando hay error.
	 */
	public static String setStringVacio(String string, int len){
		try{
			int lenDif = len - string.length();
			if( lenDif < 0){
				Utiles.writeError("Utiles : setStringVacio ---> El len variable no corresponde " + string);
				return null;
			}
			StringBuffer auxString = new StringBuffer(string);
			for ( int i = 0; i < lenDif; i++ ) {
				auxString.append(" ");
			}
			return auxString.toString();
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : setStringVacio : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : setStringVacio : Exception --->" + e );
			return null;
		}
	}

	/**
	 * Valida si el string es numerico.
	 * @param string String a validar.
	 * @return Retorna int 1 => OK, 0 => Error.
	 */
	public static int validarInteger(String string) {
		try{
			for( int i = 0; i < string.length(); i ++){
				if (!Character.isDigit(string.charAt(i))) {
					return(0);
				}
			}
			return(1);
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : validarInteger : RuntimeException --->" + ex );
			return 0;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : validarInteger : Exception --->" + e );
			return 0;
		}
	}

	/**
	 * Valida si un string esta en blanco.
	 * @param string String a validar.
	 * @return Retorna int 1 => OK, 0 => Error.
	 */
	public static int validarStringVacio(String string) {
		try{
			boolean flag = true;
			for( int i = 0; i < string.length(); i ++){
			if (string.charAt(i) != ' ') {
				flag = false;
				}
			}
			if ( flag == true){
				Utiles.writeError("Utiles : validarStringVacio ---> El string en blanco " + string);
				return(0);
			}
			return(1);
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : validarStringVacio : RuntimeException --->" + ex );
			return 0;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : validarStringVacio : Exception --->" + e );
			return 0;
		}
	}

	/**
	 * Verifica si es una fecha válida. 
	 * @param Y Valor entero correspondiente al año.
	 * @param M Valor entero correspondiente al mes.
	 * @param D Valor entero correspondiente al dia.
	 * @return - Retorna true si es una fecha y false en caso contrario.
	 */
	public static boolean isValidDate(int Y, int M, int D){
		try{
			if ((Y == 0) &&  (M == 0) && (M == 0)){
				return true;
			} 
			if((Y >= 1) && (Y <= 9999) && (M >=1) && (M <= 12) &&
				(D >=1) && (D <= daysPerMonth(Y, M))){
				return true; 
			  }
			  else 
				  return false;
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : isValidDate : RuntimeException --->" + ex );
			return false;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : isValidDate : Exception --->" + e );
			return false;
		}
	}

	/**
	 * Retorna los dias que corresponden a cada mes segun calendario.
	 * @return - Valor entero con número de días. 
	 */
	private static int daysPerMonth(int aYear, int aMonth){

		try{
			boolean esBisiesto;
			esBisiesto = new GregorianCalendar().isLeapYear(aYear);
			final int[] DaysInMonth = new int[13];
			DaysInMonth[1] = 31; // Enero
			DaysInMonth[2] = 28; // Febrero, el método devuelve 29 si es bisiesto
			DaysInMonth[3] = 31; // Marzo
			DaysInMonth[4] = 30; // Abril
			DaysInMonth[5] = 31; // Mayo
			DaysInMonth[6] = 30; // Junio
			DaysInMonth[7] = 31; // Julio
			DaysInMonth[8] = 31; // Agosto
			DaysInMonth[9] = 30; // Septiembre
			DaysInMonth[10] = 31; // Octubre
			DaysInMonth[11] = 30; // Noviembre
			DaysInMonth[12] = 31; // Diciembre
    
			  int resultado = DaysInMonth[aMonth];
			  if ((aMonth == 2) && (esBisiesto == true)){ 
				  return resultado + 1; 
			  }
			  else 
				  return resultado;
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : daysPerMonth : RuntimeException --->" + ex );
			return 0;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : daysPerMonth : Exception --->" + e );
			return 0;
		}
	}
    
	/**
	 * Valida si el string es numerico.
	 * @param string String a validar.
	 * @return Retorna int 1 => OK, 0 => Error.
	 */
	public static String formatearInteger(String string) {
		try{
			String valor;
			for( int i = 0; i < string.length(); i ++){
				if ( string.charAt(i) != '0') {
					valor = string.substring(i, string.length());
					return(valor);
				}
			}
			return(string);
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : formatearInteger : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : formatearInteger : Exception --->" + e );
			return null;
		}
	}

	/**
	 * Valida si el string es numerico.
	 * @param string String a validar.
	 * @return Retorna int 1 => OK, 0 => Error.
	 */
	public static String formatearIntegerTotal(String string) {
		try{
			String valor;
			if ( Integer.parseInt(string) == 0){
				return ("0");
			}
			for( int i = 0; i < string.length(); i ++){
				if ( string.charAt(i) != '0') {
					valor = string.substring(i, string.length());
					return(valor);
				}
			}
			return(string);
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : formatearIntegerTotal : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : formatearIntegerTotal : Exception --->" + e );
			return null;
		}
	}

	/**
	 * Valida si el string es numerico.
	 * @param string String a validar.
	 * @return Retorna int 1 => OK, 0 => Error.
	 */
	public static String formatearLongTotal(String string) {
		try{
			String valor;
			if ( Long.parseLong(string) == 0){
				return ("0");
			}
			for( int i = 0; i < string.length(); i ++){
				if ( string.charAt(i) != '0') {
					valor = string.substring(i, string.length());
					return(valor);
				}
			}
			return(string);
		}
		catch (RuntimeException ex) {
			Utiles.writeError( "Utiles : formatearLongTotal : RuntimeException --->" + ex );
			return null;
		}
		catch( Exception e ) {
			Utiles.writeError( "Utiles : formatearLongTotal : Exception --->" + e );
			return null;
		}
	}

	/**
	 * Convierte un string de caracteres a un string binario.
	 * @param str String a convertir.
	 * @return - Retorna un string convertido a Binario
	 */
	 public static String stringToBinary( String str ) {
		 String strBin = new String();
		 for ( int pos = 0; pos < str.length(); pos++ )
			 strBin = strBin + Integer.toBinaryString( str.charAt( pos ) );
            
		 return strBin;
	 }
	 
	/**
	 * Formatea la fecha de tipo YYYYMMDD como DD/MM/YYYY
	 * @param str String con la fecha formatear.
	 * @return String con fecha formateada, si es string ingresado es null o el largo 
	 * no es válido retorna null.
	 */
	public static String formatFecha( String str ) {
		String fecha = new String("");
		if (str != null && str.length()==8) {
			fecha = fecha.concat(str.substring(6,8)); 
			fecha = fecha.concat("/");
			fecha = fecha.concat(str.substring(4,6));
			fecha = fecha.concat("/");
			fecha = fecha.concat(str.substring(0,4));
			return fecha;
		} else 
			return null; 
	}
	
	/**
	 * Formatea la hora de tipo HHMMSS como HH:MM:SS
	 * @param str String con la hora a formatear.
	 * @return String con hora formateada, si es string ingresado es null o el largo 
	 * no es válido retorna null.
	 */
	public static String formatHora( String str ) {
		String hora = new String("");
		if (str != null && str.length()==6) {
			hora = hora.concat(str.substring(0,2)); 
			hora = hora.concat(":");
			hora = hora.concat(str.substring(2,4));
			hora = hora.concat(":");
			hora = hora.concat(str.substring(4,6));
			return hora;
		} else 
			return null; 
	}	  

	/**
	 * Convierte la fecha de tipo YYYYMMDD como DDMMYYYY
	 * @param str String con la fecha a convertir.
	 * @return String con la fecha transformada, si el atring ingresado es null o el largo
	 * no es válido retorna null.
	 */
	public static String formatFechaCheque( String str ) {
		String fecha = new String("");
		if (str != null && str.length()==8) {
			fecha = fecha.concat(str.substring(6,8)); 
			fecha = fecha.concat(str.substring(4,6));
			fecha = fecha.concat(str.substring(0,4));
			return fecha;
		} else 
			return null; 
	}

	/**
	 * Obtiene digito verificar de un rut (sin digito)
	 * @param rut Rut
	 * @return Digito verificador del rut
	 */
	public static String calculaDigitoRut(String rut) {
		int largo = rut.length();
		int dig = 0;
		int cont = 2;
		int suma = 0;
		int result = 0;
		try {
			for (int pos = largo; pos > 0; pos--) {
				dig = Integer.parseInt(String.valueOf(rut.charAt(pos - 1)));
				suma = suma + (dig * cont);
				cont++;
				if (cont == 8)
					cont = 2;
			}
			result = suma % 11;
			result = 11 - result;
			if (result == 11)
				return "0";
			if (result == 10)
				return "K";
			
			return String.valueOf(result);
			
		} catch (Exception e) {
			Utiles.writeError("Exception: Utiles: toHexToString--> " + e);
			return null;
		}
	}

	/**
	 * Convierte un hexadecimal(numerico) a hexadecimal(caracter)
	 * @param b Arreglo de bytes con hexadecimal(numerico)
	 * @return String que representa un hexadecimal(caracterer)
	 */
	public static String toHexToString(byte[] b) {
		String hex = new String();

		try {
			for(int i=0;i<b.length;i++)
				hex = hex + (char)b[i]; 
			
			return hex;
		} catch(NullPointerException npe) {
			Utiles.writeError("NullPointerException: Utiles: toHexToString--> " + npe);
		} catch(Exception e) {
			Utiles.writeError("Exception: Utiles: toHexToString--> " + e);
		}
		
		return null;
	}

	/**
	 * Valida campos seteados de un substring de TSL
	 * @param string Data a validar
	 * @param max_len Largo maximo permitido
	 * @return String valido de largo permitido
	 * @throws NullPointerException
	 * @throws Exception
	 */
	public static String checkSetField(String string, int max_len) throws NullPointerException, Exception{
		if (string.length() > max_len)
			return string.substring(0, max_len);
		else
			return string;
	}

	/**
	 * Enciende un bit
	 * @param byte_num Numero decimal (representa un binario)
	 * @param bit Bit que se desea encender
	 * @return Numero decimal con bit encendido (representa un binario
	 */
	public static int setBitOn(int byte_num, int bit) {
		return (byte_num | (int)Math.pow(2, bit));
	}

	
	/**
	 * Extrae string de un buffer
	 * @param buff ByteBuffer
	 * @param len largo a extraer
	 * @return
	 */
	public static String buffer2string(ByteBuffer buff, int len){
		byte []tmp = new byte[len];
		buff.get(tmp);
	//	tmp = null;
		return new String(tmp);
	}
	
	
}
