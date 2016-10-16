/*
 * Created on 09-may-2008
 *
 */
package cl.cencosud.ranking;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import cl.cencosud.historialventas.Boleta;
import cl.cencosud.historialventas.Linea;
import cl.cencosud.util.Archivo;
import cl.cencosud.util.Db;
import cl.cencosud.util.Filtro;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 * 
 * Nueva forma de calcular ranking directamente de los archivos de texto
 *  
 */
public class CalcularRanking {
    /*
     * Clave = codigo de barra, dato = cantidad de boletas donde aparece el
     * producto
     */
    private Hashtable ranking;

    public static void main(String[] args) throws Exception {
        int dias = 0;
        if (args.length > 0)
            dias = Integer.parseInt(args[0]);

        long ini = System.currentTimeMillis();
        String locales = Parametros.getString("LOCALES_RANKING");
        System.out.println(locales);
        String[] aLocales = locales.split("\\s*,\\s*");

        String driver = Parametros.getString("DRIVER");

        String user = Parametros.getString("USER");
        String password = Parametros.getString("PASSWORD");
        String url = Parametros.getString("URL");
        Connection con = Db.conexion(user, password, driver, url);
        System.out.println("CON : " + con);
        
        String cUser = Parametros.getString("C_USER");
        String cPassword = Parametros.getString("C_PASSWORD");
        String cUrl = Parametros.getString("C_URL");
        Connection cCon = Db.conexion(cUser, cPassword, driver, cUrl);
        System.out.println("CON : " + cCon);


        CalcularRanking calcularRanking = new CalcularRanking();
        calcularRanking.obtenerRanking(aLocales, dias);
        System.out.println("tiempo carga archivos: " + ((System.currentTimeMillis() - ini) / 1000));
        calcularRanking.insertarDatos(con);
        calcularRanking.insertarRanking(con);
        System.out.println("tiempo final: " + ((System.currentTimeMillis() - ini) / 1000));
        
        calcularRanking.insertarDatos(cCon);
        calcularRanking.insertarRanking(cCon);
        System.out.println("tiempo final consul: " + ((System.currentTimeMillis() - ini) / 1000));
    }

    public CalcularRanking() {
        ranking = new Hashtable();
    }

    /**
     * @param locales
     * @throws Exception
     *  
     */
    private void obtenerRanking(String[] locales, int dias) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        GregorianCalendar hoy = new GregorianCalendar();
        hoy.add(Calendar.DAY_OF_YEAR, -dias);
        //borrar
        //hoy.set(Calendar.YEAR, 2008);
        //hoy.set(Calendar.MONTH, 04);
        //hoy.set(Calendar.DAY_OF_MONTH, 5);
        //fin borrar
        //hoy.add(Calendar.DAY_OF_YEAR, -1);
        System.out.println("hoy: " + sdf.format(new Date(hoy.getTimeInMillis())));
        hoy.set(Calendar.HOUR_OF_DAY, 0);
        hoy.set(Calendar.MINUTE, 0);
        hoy.set(Calendar.SECOND, 0);
        hoy.set(Calendar.MILLISECOND, 0);
        Date hasta = new Date(hoy.getTimeInMillis());
        hoy.add(Calendar.DAY_OF_YEAR, -7);
        Date desde = new Date(hoy.getTimeInMillis());

        System.out.println("desde: " + sdf.format(desde));
        System.out.println("hasta: " + sdf.format(hasta));

        System.out.println(desde.getTime());
        System.out.println(hasta.getTime());

        //listado de archivos
        String[] archivos = Archivo.listaDeArchivosJCV(new Filtro(desde, hasta, locales));

        int totalBoletas = 0;
        int totalLineas = 0;
        for (int i = 0; i < archivos.length; i++) {
            //obtengo las boletas del archivo
            List boletas = Archivo.cargarBoletasJCV(Archivo.path() + archivos[i]);
            System.out.println(boletas.size());
            totalBoletas += boletas.size();
            for (Iterator iter = boletas.iterator(); iter.hasNext();) {
                Boleta boleta = (Boleta) iter.next();
                totalLineas += boleta.getLineasSize();
            }
            calcular(boletas, desde, hasta);
        }
        System.out.println("Total boletas: " + totalBoletas);
        System.out.println("Total Lineas: " + totalLineas);

    }

    private void calcular(List boletas, Date desde, Date hasta) {

        long lDesde = desde.getTime();
        long lHasta = hasta.getTime();

        for (Iterator iter = boletas.iterator(); iter.hasNext();) {
            Boleta boleta = (Boleta) iter.next();
            if (boleta.getFecha().getTime() >= lDesde && boleta.getFecha().getTime() < lHasta) {
                Enumeration lineas = boleta.getLineas();
                while (lineas.hasMoreElements()) {
                    Linea linea = (Linea) lineas.nextElement();
                    Long codigoBarra = new Long(linea.getCodigoBarra());
                    Integer cantidad = (Integer) ranking.get(codigoBarra);
                    if (cantidad != null) {
                        cantidad = new Integer(cantidad.intValue() + 1);
                    } else {
                        cantidad = new Integer(1);
                    }
                    ranking.put(codigoBarra, cantidad);
                }
            }
        }
    }

    /**
     * Inserta los datos en la tabla vhtdba.ranking
     * 
     * @param con
     * @param lista
     * @throws SQLException
     */
    private void insertarDatos(Connection con) throws SQLException {
        String sqlDelete = "delete from vthdba.ranking";
        Statement st = con.createStatement();
        st.executeUpdate(sqlDelete);
        st.close();

        String sqlInsert = "insert into vthdba.ranking(codigo_barra, boletas) values (?,?)";
        PreparedStatement ps = con.prepareStatement(sqlInsert);
        Enumeration barras = ranking.keys();
        int i = 0;
        while (barras.hasMoreElements()) {
            i++;
            Long codigoBarra = (Long) barras.nextElement();
            Integer cantidad = (Integer) ranking.get(codigoBarra);
            ps.setLong(1, codigoBarra.longValue());
            ps.setInt(2, cantidad.intValue());
            ps.addBatch();
            if (i % 5000 == 0) {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        ps.executeBatch();
        ps.close();
    }

    /**
     * @param con
     * @throws SQLException
     */
    private void insertarRanking(Connection con) throws SQLException {
        String sqlDelete = "delete from bodba.ranking_productos";
        Statement st = con.createStatement();
        st.executeUpdate(sqlDelete);
        st.close();

        String sqlInsert = "insert into bodba.ranking_productos                                                "
                + "select pro.id_catprod, ran.codigo_barra, cod.cod_barra, tip_codbar, pro.cod_prod1, cod.unid_med, "
                + "des_corta, des_larga, con_precio,                                               "
                + "case when pro.estado = 1 then 'S' else 'N' end as HABILITADO_SAP, boletas       "
                + "from vthdba.ranking ran                                                         "
                + "inner join bodba.bo_codbarra cod on   ran.codigo_barra  = cod.cod_barra_bigint  "
                + "inner join bodba.bo_productos pro on pro.id_producto = cod.id_producto          ";
        st = con.createStatement();
        st.executeUpdate(sqlInsert);
        st.close();
    }

}
