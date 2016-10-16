/*
 * Creado el 16-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import net.sf.fastm.IValueSet;
import net.sf.fastm.ValueSet;

import cl.bbr.jumbocl.common.model.PerfilesEntity;
import cl.bbr.jumbocl.common.utils.Formatos;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author imoyano
 * 
 * Para cambiar la plantilla de este comentario generado, vaya a Ventana -
 * Preferencias - Java - Estilo de código - Plantillas de código
 */
public class CasosUtil {

    /**
     * Método utilizado en la creacion de un XML para funciones de Ajax
     * 
     * @param fecha
     *            Fecha a formatear
     * @return Fecha formateada, en caso de no existir la fecha devuelve un
     *         espacio en blanco.
     */
    public String frmFechaXml(String fecha) {
        if (!fecha.equals(null) && !fecha.equals("")) {
            return Formatos.frmFecha(fecha);
        }
        return "-";
    }

    /**
     * Método utilizado en la creacion de un XML para funciones de Ajax
     * 
     * @param texto
     *            Texto para incorporar en el xml
     * @return Si no existe texto, deja un espacio en blanco
     */
    public String frmTextoXml(String texto) {
        if (texto == null || texto.equals("")) {
            return "-";
        }
        return texto;
    }

    /**
     * Método utilizado en la creacion de un XML para funciones de Ajax
     * 
     * @param numero
     * @return Si el número es '0', devuelve un espacio en blanco
     */
    public String frmNumeroXml(long numero) {
        if (numero == 0) {
            return "-";
        }
        return String.valueOf(numero);
    }

    static public Date frmFecha(String fecha, String patron) {
        SimpleDateFormat formatter = new SimpleDateFormat(patron);
        try {
            if (fecha != null && !fecha.equalsIgnoreCase("")) {
                return new Date(formatter.parse(fecha).getTime());
            }
        } catch (ParseException e) {            
        }
        return null;
    }
    
    /**
     * Convierte un String a Timestamp
     * @param fecha
     * @param patron
     * @return
     */
    static public Timestamp frmTimestamp(String fechaHora, String patron) {
        SimpleDateFormat formatter = new SimpleDateFormat(patron);
        try {
            if (fechaHora != null && !fechaHora.equalsIgnoreCase("")) {
                return new Timestamp(formatter.parse(fechaHora).getTime());
            }
        } catch (ParseException e) {            
        }
        return null;
    }

    /**
     * Cambia el formato de una fecha que viende desde la pagina dd/MM/yyyy para ser ingresada en la BD yyyy-MM-dd
     * @param fechaResolucion
     * @return
     */
    public static String cambioFrmFc(String fechaResolucion) {
        if (fechaResolucion.length() == 10) {
            String dia = "";
            String mes = "";
            String anio = "";
            
            dia = fechaResolucion.substring(0,2);
            mes = fechaResolucion.substring(3,5);
            anio = fechaResolucion.substring(6,10);
            
            return anio+"-"+mes+"-"+dia;
            
        }
        return null;
    }
    
    /**
     * Nos entrega un listado para generar el combo de las alarmas
     * @param filtroAlarma Codigo de la alarma por la cual filtró para dejar seleccionada
     * @return Listado de alarmas
     */
    public static ArrayList generaComboAlarma(String filtroAlarma) {
        ArrayList lista = new ArrayList();
        IValueSet fila = new ValueSet();
        fila.setVariable("{id_alarma}", "V");
		fila.setVariable("{alarma}", "Vencido");
		if (filtroAlarma.equalsIgnoreCase("V")) {
			fila.setVariable("{sel_alarma}", "selected");
		} else {
			fila.setVariable("{sel_alarma}", "");
		}				
		lista.add(fila);
		
		fila = new ValueSet();
        fila.setVariable("{id_alarma}", "P");
		fila.setVariable("{alarma}", "Por vencer");
		if (filtroAlarma.equalsIgnoreCase("P")) {
			fila.setVariable("{sel_alarma}", "selected");
		} else {
			fila.setVariable("{sel_alarma}", "");
		}				
		lista.add(fila);
		
		fila = new ValueSet();
        fila.setVariable("{id_alarma}", "N");
		fila.setVariable("{alarma}", "Normal");
		if (filtroAlarma.equalsIgnoreCase("N")) {
			fila.setVariable("{sel_alarma}", "selected");
		} else {
			fila.setVariable("{sel_alarma}", "");
		}				
		lista.add(fila);
		
		fila = new ValueSet();
        fila.setVariable("{id_alarma}", "F");
		fila.setVariable("{alarma}", "Finalizado");
		if (filtroAlarma.equalsIgnoreCase("F")) {
			fila.setVariable("{sel_alarma}", "selected");
		} else {
			fila.setVariable("{sel_alarma}", "");
		}				
		lista.add(fila);
		
		fila = new ValueSet();
        fila.setVariable("{id_alarma}", "E");
		fila.setVariable("{alarma}", "Escalados");
		if (filtroAlarma.equalsIgnoreCase("E")) {
			fila.setVariable("{sel_alarma}", "selected");
		} else {
			fila.setVariable("{sel_alarma}", "");
		}				
		lista.add(fila);
		
        return lista;
    }
    
    /**
     * Nos entrega la fecha minima q podemos seleccionar como fecha de resolución - Usado en la creacion del caso
     * @param dia Días de resolución
     * @return Fecha actual, más los día de resolución
     */
    public static String fechaActualMasDiaResolucion(int dia) {    
        Calendar cHoy = Calendar.getInstance();
        cHoy.add(Calendar.DAY_OF_MONTH, dia);
        java.util.Date dHoy = cHoy.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    
        return sdf.format(dHoy);
    }
    
    /**
     * Nos entrega la fecha minima q podemos seleccionar como fecha de resolución - Usado en la modificacion de un caso
     * @param dia Días de resolución
     * @return Fecha actual, más los día de resolución
     */
    public static String fechaCreacionMasDiaResolucion(String fechaCreacion, int dia) {
        Calendar cFechaCreacion = Calendar.getInstance();
        java.util.Date dFechaCreacion = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dFechaCreacion = sdf.parse(fechaCreacion);
            cFechaCreacion.setTime(dFechaCreacion);
            cFechaCreacion.add(Calendar.DAY_OF_MONTH, dia);            
        } catch (ParseException e1) {}
	    return mesAnio(cFechaCreacion.get(Calendar.DAY_OF_MONTH))+"/"+mesAnio(cFechaCreacion.get(Calendar.MONTH)+1)+"/"+cFechaCreacion.get(Calendar.YEAR);
    }
    
    /**
     * @param i
     * @return
     */
    private static String mesAnio(int i) {
        if (i < 10) {
            return "0"+i;
        }
        return ""+i;
    }

    /**
     * Se utiliza para los datos que esten nulos en la BD
     * @param texto
     * @return
     */
    public static String frmTexto(String texto) {
        if (texto == null) {
            return " ";
        }
        return texto;
    }
    
    public static String frmFechaHoraSinSegundos(String fecha) {
        String fcHr = Formatos.frmFechaHora(fecha);        
        if (fcHr.length() > 10) {
            return fcHr.substring(0,fcHr.length()-3);
        }        
        return fcHr;        
    }
    
    /**
     * Nos entrega la fecha o la hora actual, de acuerdo al PATRON
     * @param patron
     * @return Fecha u Hora
     */
    public static String fechaActualByPatron(String patron) {    
        Calendar cHoy = Calendar.getInstance();
        java.util.Date dHoy = cHoy.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(patron);
	    return sdf.format(dHoy);
    }
    
    /**
     * Nos indica si el usuario tiene el perfil de Supervisor. Si no es supervisor se desabilita el combo de escalamiento
     * @param usr
     * @return
     */
    public static boolean esSupervisor(UserDTO usr, long idSupervisor) {
        for (int i = 0; i < usr.getPerfiles().size(); i++) {
            PerfilesEntity perf = (PerfilesEntity) usr.getPerfiles().get(i);
            if ( perf.getIdPerfil().longValue() == idSupervisor ) {
                return true;
            }
        }        
        return false;
    }
    
    /**
     * Nos indica si el usuario tiene el perfil de Call Center.
     * @param usr
     * @return
     */
    public static boolean esCallCenter(UserDTO usr, long idCallCenter) {
        for ( int i=0; i < usr.getPerfiles().size(); i++ ) {
            PerfilesEntity per = (PerfilesEntity) usr.getPerfiles().get(i);
            if ( per.getIdPerfil().longValue() == idCallCenter ) {
                return true;    
            }
        }
        return false;
    }
}
