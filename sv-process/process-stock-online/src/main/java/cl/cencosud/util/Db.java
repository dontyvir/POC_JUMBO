
package cl.cencosud.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import cl.cencosud.beans.Local;


/**
 * @author jdroguett
 * 
 * Preferences - Java - Code Style - Code Templates
 */
public class Db {

    // protected static Logger logger = Logger.getLogger(Db.class);

    public static Connection conexion(String user, String password, String driver, String url) throws Exception {

        Connection con = null;
        try {

            try {
                Class.forName(driver).newInstance();
            } catch (ClassNotFoundException e) {
                throw new Exception("Error: El driver no se encuentra");
            }
            Properties p = new Properties();
            p.setProperty("user", user);
            p.setProperty("password", password);

            System.out.println("URL : " +url);
            
            if ((con = DriverManager.getConnection(url, p)) == null) {
                throw new Exception("Error: la conexion es nula");
            }
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } catch (Exception e) {
            System.out.println("Error al obtener nueva conexión: " + e.getMessage());
            throw new Exception("Error al obtener nueva conexión: " + e.getMessage());
        }
        return con;
    }

    /**
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    public static List locales(Connection con) throws SQLException {

        List locales = new ArrayList();
        String sql = "select ID_LOCAL, COD_LOCAL, NOM_LOCAL from bodba.bo_locales";
        PreparedStatement ps = con.prepareStatement(sql + " with ur ");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Local local = new Local();
            local.setId(rs.getInt("ID_LOCAL"));
            local.setCodigo(rs.getString("COD_LOCAL"));
            local.setNombre(rs.getString("NOM_LOCAL"));
            locales.add(local);
        }
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        System.out.println("Locales cargado : " + locales.size());
        return locales;
    }

   

    public static PreparedStatement preparedStatement(Connection con, String sql) throws SQLException {

        return con.prepareStatement(sql);
    }

   

    // ///////// fin lucene

    public static void close(Connection con) {

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Problema SQL close " + e.getMessage());
            }
        }
    }

    public static void close(Statement st) {

        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                System.out.println("Problema SQL close " + e.getMessage());
            }
        }
    }

    public static void close(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.println("Problema SQL close " + e.getMessage());
            }
        }
    }

    public static void close(ResultSet rs, Statement st) {

        close(rs);
        close(st);
    }


    public static String calculaTiempo(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return hours + ":" + minutes + ":" + seconds;
    }

}
