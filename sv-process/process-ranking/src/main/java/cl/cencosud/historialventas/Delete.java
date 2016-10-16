/*
 * Created on 08-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.historialventas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Delete {
    public static void main(String[] args) throws Exception {
        String driver = Parametros.getString("DRIVER");

        String vUser = Parametros.getString("V_USER");
        String vPassword = Parametros.getString("V_PASSWORD");
        String vUrl = Parametros.getString("V_URL");
        Connection vCon = Db.conexion(vUser, vPassword, driver, vUrl);
        System.out.println("VCON : " + vCon);

        borrarCompras(vCon);

        String user = Parametros.getString("USER");
        String password = Parametros.getString("PASSWORD");
        String url = Parametros.getString("URL");
        Connection con = Db.conexion(user, password, driver, url);
        System.out.println("CON : " + con);

        //borrarProductosCompra(con);

    }

    /**
     * @param con
     * @throws SQLException
     */
    private static void borrarProductosCompra(Connection con) throws SQLException {
        String sql = "select min(fcl_id) from  fodba.fo_productos_compra";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.println("111111111111111111");
        if (rs.next()) {
            int n = rs.getInt(1);
            sql = "select max(fcl_id) from  fodba.fo_productos_compra";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                int m = rs.getInt(1);
                sql = "delete from fodba.fo_productos_compra where fcl_id <= ?";
                ps = con.prepareStatement(sql);
                for (int i = n; i < m + 100000; i += 100000) {
                    ps.setInt(1, i);
                    ps.executeUpdate();
                    //System.out.println("222222222");
                    con.commit();
                }
            }
        }

    }

    /**
     * @param vCon
     * @throws SQLException
     */
    private static void borrarCompras(Connection vCon) throws SQLException {
        String sql = "select min(co_id_compra), max(co_id_compra) from vthdba.compras";
        PreparedStatement ps = vCon.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int idMin = rs.getInt(1);
            int idMax = rs.getInt(2);

            String sqlDel1 = "delete from vthdba.compra_productos where co_id_compra < ?";
            String sqlDel2 = "delete from vthdba.compras  where co_id_compra < ?";

            PreparedStatement psDel1 = vCon.prepareStatement(sqlDel1);
            PreparedStatement psDel2 = vCon.prepareStatement(sqlDel2);
            for (int i = idMin; i <= idMax + 10; i += 10) {
                psDel1.setInt(1, i);
                psDel2.setInt(1, i);
                psDel1.executeUpdate();
                psDel2.executeUpdate();
            }
        }
    }

}
