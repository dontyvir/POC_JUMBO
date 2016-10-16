/*
 * Created on 07-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.cencosud.historialventas.Boleta;
import cl.cencosud.historialventas.Linea;

/**
 * @author jdroguett
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Archivo {
    public static String path() {
        return Parametros.getString("PATH_ARCHIVOS");
    }

    public static String[] listaDeArchivosJCV(FilenameFilter filtro) {
        String path = path();
        File directorio = new File(path);
        String[] lista = directorio.list(filtro);
        return lista;
    }

    /**
     * Entrega el listado de boletas del archivo
     * 
     * @param archivo
     * @return
     * @throws Exception
     */
    public static List cargarBoletasJCV(String archivo) throws Exception {
        System.out.println(archivo);
        List lista = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea = null;

        //        TK|J501|30|2|20080224| local, caja, ticket, fecha
        //        CL|03284089|2| rut, medioPago
        //        AR|780509503000|2.000|
        //        AR|780700092201|2.000|
        //        MP|41|9960| medioPagoSAP, monto (puede haber más de una linea MP por
        // boleta)
        //        TK|J501|77|1|20080224|
        //        CL|21839559|2|
        //        AR|13277|4.000|
        //        AR|208200124058|1.000|
        //        AR|800660|1.000|
        //        AR|800661|1.000|
        //        AR|800661|-1.000|
        //        MP|44|2980|

        int i = 0;

        try {
            Boleta boleta = null;
            while ((linea = br.readLine()) != null) {
                i++;
                String[] dato = linea.split("\\|");
                if ("TK".equals(dato[0])) {
                    boleta = new Boleta();
                    lista.add(boleta);
                    boleta.setLocal(dato[1]);
                    boleta.setCaja(Integer.parseInt(dato[2]));
                    boleta.setTicket(Integer.parseInt(dato[3]));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    boleta.setFecha(sdf.parse(dato[4]));
                } else if ("CL".equals(dato[0])) {
                    boleta.setRutCliente(Integer.parseInt(dato[1]));
                    boleta.setMedioPago(Integer.parseInt(dato[2]));
                } else if ("AR".equals(dato[0])) {
                    Linea lineaBoleta = new Linea();
                    lineaBoleta.setCodigoBarra(Long.parseLong(dato[1]));
                    lineaBoleta.setCantidad(Float.parseFloat(dato[2]));
                    boleta.addLinea(lineaBoleta);
                } else if ("MP".equals(dato[0])) {
                    boleta.addMontoTotal(Integer.parseInt(dato[2]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en linea : " + i);
            throw e;
        }
        return lista;
    }
    
    public static List cargarClientesBoletasJCV(String archivo) throws Exception{
        List lista = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea = null;
        int i = 0;

        try {
            Boleta boleta = null;
            while ((linea = br.readLine()) != null) {
                i++;
                String[] dato = linea.split("\\|");
                if ("TK".equals(dato[0])) {
                    boleta = new Boleta();
                    lista.add(boleta);
                    boleta.setLocal(dato[1]);
                    boleta.setCaja(Integer.parseInt(dato[2]));
                    boleta.setTicket(Integer.parseInt(dato[3]));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    boleta.setFecha(sdf.parse(dato[4]));
                } else if ("CL".equals(dato[0])) {
                    boleta.setRutCliente(Integer.parseInt(dato[1]));
                    boleta.setMedioPago(Integer.parseInt(dato[2]));
                } else if ("AR".equals(dato[0])) {
                    Linea lineaBoleta = new Linea();
                    lineaBoleta.setCodigoBarra(Long.parseLong(dato[1]));
                    lineaBoleta.setCantidad(Float.parseFloat(dato[2]));
                    boleta.addLinea(lineaBoleta);
                } else if ("MP".equals(dato[0])) {
                    boleta.addMontoTotal(Integer.parseInt(dato[2]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en linea : " + i);
            throw e;
        }

        return lista;
    }
}
