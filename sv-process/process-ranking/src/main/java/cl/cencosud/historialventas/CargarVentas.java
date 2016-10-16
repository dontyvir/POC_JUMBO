/*
 * Created on 06-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.historialventas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import cl.cencosud.util.Archivo;
import cl.cencosud.util.Db;
import cl.cencosud.util.Filtro;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 * 
 * Clase que carga las ventas de los locales en la base de datos vthdba
 * 
 * Existe la tabla compras_archivos que permite identificar en que archivos SAP
 * estaba cada compra grabada: ""create table vthdba.compras_archivos ( id
 * integer not null GENERATED AS IDENTITY primary key, nombre varchar(17) not
 * null unique, cargado smallint default 0 ) ""
 * 
 * Se hace referencia en la tabla compras: ""alter table vthdba.compras add
 * column compras_archivos_id integer not null references
 * vthdba.compras_archivos (id)""
 * 
 *  
 */
public class CargarVentas {

    private static final int RANGO = 500;

    public static final int CARGADO = 1;

    public static final int NO_EXISTE = -1;

    public static void main(String[] args) throws Exception {
        long ini = System.currentTimeMillis();
        String driver = Parametros.getString("DRIVER");

        String user = Parametros.getString("USER");
        String password = Parametros.getString("PASSWORD");
        String url = Parametros.getString("URL");
        Connection con = Db.conexion(user, password, driver, url);
        System.out.println("CON : " + con);

        String vUser = Parametros.getString("V_USER");
        String vPassword = Parametros.getString("V_PASSWORD");
        String vUrl = Parametros.getString("V_URL");
        Connection vCon = Db.conexion(vUser, vPassword, driver, vUrl);
        System.out.println("VCON : " + vCon);

        CargarVentas cargar = new CargarVentas();

        String[] archivos = Archivo.listaDeArchivosJCV(new Filtro(new java.util.Date(), new java.util.Date(),
                new String[0]));
        archivos = cargar.verificarCarga(vCon, archivos);

        for (int i = 0; i < archivos.length; i++) {
            List boletas = Archivo.cargarBoletasJCV(Archivo.path() + archivos[i]);
            System.out.println(boletas.size());
            int archivoId = cargar.grabarArchivo(vCon, archivos[i]);
            cargar.grabarVentas(vCon, boletas, archivoId);
            cargar.actualizarEstadoArchivo(vCon, archivoId);
        }
        System.out.println("Tiempo (seg): " + ((System.currentTimeMillis() - ini) / 1000));
    }

    public void grabarVentas(Connection con, List boletas, int archivoId) throws Exception {
        //con.setAutoCommit(false); //transacción por archivo
        try {
            String sqlInsert = "insert into vthdba.compras(lo_cod_local, mp_codigo_cdp,       "
                    + "es_id, co_rut, co_fecha, co_caja, co_importe, co_ticket, compras_archivos_id) "
                    + "values(?,?,?,?,?,?,?,?,?)";

            PreparedStatement psInsert = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < boletas.size(); i++) {
                Boleta boleta = (Boleta) boletas.get(i);
                psInsert.setString(1, boleta.getLocal());
                psInsert.setInt(2, boleta.getMedioPago());
                psInsert.setInt(3, 2);
                psInsert.setInt(4, boleta.getRutCliente());
                psInsert.setDate(5, new Date(boleta.getFecha().getTime()));
                psInsert.setInt(6, boleta.getCaja());
                psInsert.setFloat(7, boleta.getMontoTotal());
                psInsert.setInt(8, boleta.getTicket());
                psInsert.setInt(9, archivoId);
                psInsert.executeUpdate();
                ResultSet rs = psInsert.getGeneratedKeys();
                if (rs.next()) {
                    boleta.setId(rs.getInt(1));
                }
            }

            System.out.println("Listo boletas");
            String sqlInsertLinea = "insert into vthdba.compra_productos(co_id_compra, cp_codigo_ean, cp_cantidad) "
                    + "values(?,?,?)";
            PreparedStatement psInsertLinea = con.prepareStatement(sqlInsertLinea);
            int j = 1;
            int n = 0;
            for (int i = 0; i < boletas.size(); i++) {
                Boleta boleta = (Boleta) boletas.get(i);
                Enumeration lineas = boleta.getLineas();
                while (lineas.hasMoreElements()) {
                    Linea linea = (Linea) lineas.nextElement();
                    psInsertLinea.setInt(1, boleta.getId());
                    psInsertLinea.setLong(2, linea.getCodigoBarra());
                    psInsertLinea.setFloat(3, linea.getCantidad());
                    //psInsertLinea.executeUpdate();
                    n++;
                    psInsertLinea.addBatch();
                }
                if (j * RANGO == i) {
                    psInsertLinea.executeBatch();
                    System.out.println(i);
                    j++;
                    psInsertLinea.clearBatch();
                }
            }
            psInsertLinea.executeBatch();
            //con.commit();
            System.out.println("Listo lineas de boletas");
        } catch (SQLException e) {
            //con.rollback();
            //con.setAutoCommit(true);
            e.printStackTrace();
            if (e.getNextException() != null)
                e.getNextException().printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Indica si el estado del archivo
     * 
     * @param con
     * @param nombre
     * @return CARGADO, NO_EXISTE, NO_CARGADO == id del archivo
     * @throws SQLException
     */
    public int estadoArchivo(Connection con, String nombre) throws SQLException {
        String sql = "select id, cargado from vthdba.compras_archivos where nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nombre);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return (rs.getInt("cargado") == CARGADO ? CARGADO : rs.getInt("id"));
        return NO_EXISTE;
    }

    /**
     * Archivos no completaron su carga
     * 
     * @param con
     * @param nombre
     * @throws SQLException
     */
    public void limpiarArchivo(Connection con, int id) throws SQLException {
        //borra en cascada
        String delete = "delete from vthdba.compras where compras_archivos_id = ?";
        PreparedStatement ps = con.prepareStatement(delete);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    /**
     * Guarda el nombre del archivo a cargar,
     * 
     * @param con
     * @param nombre
     * @return
     * @throws SQLException
     */
    public int grabarArchivo(Connection con, String nombre) throws SQLException {
        //por defecto la columna cargado es cero (false)
        String sql = "insert into vthdba.compras_archivos(nombre) values (?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, nombre);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        int id = 0;
        if (rs.next())
            id = rs.getInt(1);
        rs.close();
        ps.close();
        return id;
    }

    /**
     * Marca al archivo como cargado completamente
     * 
     * @param con
     * @param string
     * @throws SQLException
     */
    public void actualizarEstadoArchivo(Connection con, int archivoId) throws SQLException {
        String sql = "update vthdba.compras_archivos set cargado = 1 where id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, archivoId);
        ps.executeUpdate();
        ps.close();
    }

    /**
     * Devuelve los archivos que no estan cargados en la base de datos
     * 
     * @param con
     * @param archivos
     * @return
     * @throws SQLException
     */
    private String[] verificarCarga(Connection con, String[] archivos) throws SQLException {
        String sql = "select id from vthdba.compras_archivos where nombre = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        List listaNueva = new ArrayList();

        for (int i = 0; i < archivos.length; i++) {
            ps.setString(1, archivos[i]);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                listaNueva.add(archivos[i]);
            }
        }
        return (String[]) listaNueva.toArray(new String[listaNueva.size()]);
    }
}
