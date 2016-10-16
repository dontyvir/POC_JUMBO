/*
 * Creado el 21-08-2007
 *
 * Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.jumbocl.casos.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.bbr.jumbocl.casos.dto.ReclamosClienteDTO;

/**
 * .
 * 
 * @author imoyano
 *
 * Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class ReclamosClientesCasos {
	private Logger logger;
    public String fechaInicial	= "";
    public String fechaFinal	= "";
    public String tipoSalida	= "";
    public Hashtable llaves = new Hashtable();
    public List cabecera = new ArrayList();
    
    Calendar cFechaInicio = Calendar.getInstance();
    Calendar cFechaFin = Calendar.getInstance();
    Date dFechaInicio = new Date();
    Date dFechaFin = new Date();
    SimpleDateFormat patron = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat patronSemana = new SimpleDateFormat("wwyyyy");
    int semanaIni	= 0;
    int anioIni		= 0;
    int semanaFin	= 0;
    int anioFin		= 0;
    
    public String ddIni 	= "";
    public String mmIni 	= "";
    public String aaaaIni 	= "";
    
    public String ddFin 	= "";
    public String mmFin 	= "";
    public String aaaaFin 	= "";
    
    
    public ReclamosClientesCasos(String fechaInicial,String fechaFinal,String tipoSalida) {
        this.fechaInicial = fechaInicial;
        this.fechaFinal	  = fechaFinal;
        this.tipoSalida	  = tipoSalida;
        this.cFechaInicio.setFirstDayOfWeek(Calendar.MONDAY);
        this.cFechaFin.setFirstDayOfWeek(Calendar.MONDAY);
        generarCabecera();
    }
    
    public void generarCabecera() {
        
        Calendar cFechaTmp = Calendar.getInstance();
        cFechaTmp.setFirstDayOfWeek(Calendar.MONDAY);
        
        int semanaTmp	= 0;
        int anioTmp		= 0;
        
        try {
            dFechaInicio = patron.parse(fechaInicial);
            cFechaInicio.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            cFechaInicio.setTime(dFechaInicio);            
            logger.debug("fechaInicial:"+fechaInicial+" cFechaInicio:"+cFechaInicio.getTime());            
            
            dFechaFin = patron.parse(fechaFinal);
            cFechaFin.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            cFechaFin.setTime(dFechaFin);
            logger.debug("fechaFinal:"+fechaFinal+" cFechaFin:"+cFechaFin.getTime()); 
            
            semanaIni 	= cFechaInicio.get(Calendar.WEEK_OF_YEAR);
            anioIni 	= cFechaInicio.get(Calendar.YEAR);            
            
            int mesIni = cFechaInicio.get(Calendar.MONTH);
            // Caso de borde... cuando el mes es 11 (DIC) y la semana es 1, al anio tenemos q sumarle 1
            if (mesIni == 11 && semanaIni == 1) {
                anioIni += 1;
            }            
            logger.debug("semanaIni:"+semanaIni+" anioIni:"+anioIni+" mesIni:"+mesIni);
                        
            semanaFin 	= cFechaFin.get(Calendar.WEEK_OF_YEAR);
            anioFin 	= cFechaFin.get(Calendar.YEAR);
            
            int mesFin = cFechaFin.get(Calendar.MONTH);
            // Caso de borde... cuando el mes es 11 (DIC) y la semana es 1, al anio tenemos q sumarle 1
            if (mesFin == 11 && semanaFin == 1) {
                anioFin += 1;
            }
            logger.debug("semanaFin:"+semanaFin+" anioFin:"+anioFin+" mesFin:"+mesFin);
              
            cFechaTmp = cFechaInicio;
            cFechaTmp.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            
            boolean iterar = false;
            int i = 0;
            
            ddIni = diaMes(cFechaTmp.get(Calendar.DAY_OF_MONTH));
            mmIni = diaMes((cFechaTmp.get(Calendar.MONTH)+1));
            aaaaIni = String.valueOf(cFechaTmp.get(Calendar.YEAR));
            
            do {
                semanaTmp 	= cFechaTmp.get(Calendar.WEEK_OF_YEAR);
	            anioTmp 	= cFechaTmp.get(Calendar.YEAR);
                // Caso de borde... cuando el mes es 11 (DIC) y la semana es 1, al anio tenemos q sumarle 1
	            if (cFechaTmp.get(Calendar.MONTH) == 11 && semanaTmp == 1) {
	                anioTmp += 1;
	            }	            
                //llaves.put(patronSemana.format(  cFechaTmp.getTime()  ), new Integer(i));
	            llaves.put(diaMes(semanaTmp)+anioTmp, new Integer(i));
                
                String fechasCabecera = diaMes(cFechaTmp.get(Calendar.DAY_OF_MONTH))+"/"+diaMes((cFechaTmp.get(Calendar.MONTH)+1))+"/"+cFechaTmp.get(Calendar.YEAR);
	            cFechaTmp.add(Calendar.DAY_OF_YEAR, 6);
	            fechasCabecera += " - " + diaMes(cFechaTmp.get(Calendar.DAY_OF_MONTH))+"/"+diaMes((cFechaTmp.get(Calendar.MONTH)+1))+"/"+cFechaTmp.get(Calendar.YEAR);
	            
	            ddFin = diaMes(cFechaTmp.get(Calendar.DAY_OF_MONTH));
	            mmFin = diaMes((cFechaTmp.get(Calendar.MONTH)+1));
	            aaaaFin = String.valueOf(cFechaTmp.get(Calendar.YEAR));
	            
	            cabecera.add(i,fechasCabecera);
	            
	            cFechaTmp.add(Calendar.DAY_OF_YEAR, 1);
	                
	            semanaTmp 	= cFechaTmp.get(Calendar.WEEK_OF_YEAR);
	            anioTmp 	= cFechaTmp.get(Calendar.YEAR);
	            
	            if ((semanaIni==semanaFin)&&(anioIni==anioFin)) {
	                iterar = false;
	            } else {
	                iterar = true;
	                if ((anioTmp==anioFin)&&(semanaTmp>semanaFin)) {
	                    iterar = false;
	                }	                
	            }
	            i++;	        
            } while (iterar);
            
        } catch (ParseException e) {            
        }
    }
    
    /**
     * @param i
     * @return
     */
    private String diaMes(int i) {
        if (i < 10) {
            return "0"+i;
        }
        return ""+i;
    }

    private static int diasRestar(int dia) {
        if (dia == 1) {
            return 6;
        } else if (dia == 2) {
            return 0;
        } else if (dia == 3) {
            return 1;
        } else if (dia == 4) {
            return 2;
        } else if (dia == 5) {
            return 3;
        } else if (dia == 6) {
            return 4;
        } else if (dia == 7) {
            return 5;
        } else if (dia == 0) {
            return 7;
        }
        return 0;
    }
    
    public String tablaHtmlReclamos(List reclamosClientes) {
        String borde = "0";
        if (tipoSalida.equalsIgnoreCase("PLANILLA")) {
            borde = "1";
        }
        int columnas = this.cabecera.size();
        int anchoColumnas = 100;
        
        int ancho = 100 + (anchoColumnas * columnas);        
        
        String respuesta =  "<table class=\"tabla2\" align=\"center\" border=\""+borde+"\" cellpadding=\"2\" cellspacing=\"0\" width=\""+ancho+"\" >" +
							"	<tr id=\"titulo\">" +
							"		<td colspan=\""+(columnas+2)+"\">" +
							"			<table align=\"center\" border=\""+borde+"\" cellpadding=\"2\" cellspacing=\"0\" width=\""+ancho+"\" bgcolor=\"#CCCCCC\">" +
							"				<tr class=\"titulo_tabla\">" +
							"				  <th height=\"20\" colspan=\"2\">CLIENTES</th>" +
							"				  <th colspan=\""+columnas+"\">PUNTAJES POR SEMANA </th>" +
							"				</tr>" +
							"				<tr class=\"titulo_tabla\">" +
							"					<th height=\"20\" width=\"90px\">Rut</th>" +
							"					<th width=\"10px\">Dv</th>";
        
		for (int i=0; i < this.cabecera.size(); i++) {
		respuesta += 		"					<th width=\""+anchoColumnas+"px\">"+ this.cabecera.get(i) +" </th>";
		}        					
		respuesta += 		"				</tr>" +
							"			</table>" +
							"		</td>" +
							"	</tr>";
		
		if (reclamosClientes.size() > 0) {
		    for (int i=0; i < reclamosClientes.size(); i++) {
		    	ReclamosClienteDTO rc = (ReclamosClienteDTO) reclamosClientes.get(i);
		respuesta +=		"	<tr>" +
							"		<td height=\"25\" align=\"center\" width=\"90px\">"+rc.getCliRut()+"</td>" +
							"		<td align=\"center\" width=\"10px\">"+rc.getDv()+"</td>";
				for (int j=0; j < rc.getReclamos().length; j++) {
			    	if (rc.getReclamos()[j]==null) {
		respuesta +=		"		<td align=\"center\" width=\""+anchoColumnas+"px\">0</td>";
			    	} else {
		respuesta +=		"		<td align=\"center\" width=\""+anchoColumnas+"px\">"+rc.getReclamos()[j].toString()+"</td>";    
			    	}
				}		
		respuesta +=		"	</tr>";
			}
		} else {
		respuesta +=		"	<tr>" +
							"		<td height=\"25\" align=\"center\" width=\""+ancho+"px\">No existen puntajes para mostrar </td>" +
							"	</tr>";
		}
		respuesta +=		"</table>";
		
		return respuesta;    
    }

}
