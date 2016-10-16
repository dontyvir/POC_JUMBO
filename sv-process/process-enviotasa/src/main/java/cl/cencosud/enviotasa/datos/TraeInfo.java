/*
 * Created on 19-mar-2010
 */
package cl.cencosud.enviotasa.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cl.cencosud.enviotasa.util.Db;
import cl.cencosud.enviotasa.util.Util;

/**
 * @author imoyano
 */
public class TraeInfo {

    public static Pedido getInfoPedidoJumbo(Connection con, Pedido ped) throws SQLException {
        ped.setExisteEnBD(false);
        try {
            String sql = "select upper(p.NOM_CLIENTE) nombre_cliente, cli.CLI_EMAIL, p.RUT_CLIENTE, bp.CAT_RUT_CLIENTE, " +
                         "p.DV_CLIENTE, p.FCREACION, bp.CAT_NRO_TARJETA, trx.POS_FECHA, lo.NOM_LOCAL, " +
                         "sum(trx.POS_MONTO_FP) monto_capturado " +
                         "from bodba.bo_pedidos p " +
                         "inner join bodba.bo_trx_mp trx on trx.ID_PEDIDO = p.ID_PEDIDO " +
                         "inner join fodba.fo_clientes cli on cli.CLI_ID = p.ID_CLIENTE " +
                         "inner join bodba.botonpagocat bp on bp.ID_PEDIDO = p.ID_PEDIDO " +
                         "inner join bodba.bo_locales lo on lo.ID_LOCAL = p.ID_LOCAL " +
                         "where bp.CAT_TIPO_AUTORIZACION = 'A' and p.ID_PEDIDO = ? " +
                         "group by p.NOM_CLIENTE, cli.CLI_EMAIL, p.RUT_CLIENTE, bp.CAT_RUT_CLIENTE, p.DV_CLIENTE, p.FCREACION, bp.CAT_NRO_TARJETA, trx.POS_FECHA, lo.NOM_LOCAL";
            
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, ped.getIdPedido());
            ResultSet rs = stm.executeQuery();
            
            if ( rs.next() ) {
                ped.setExisteEnBD(true);
                ped.setNombreCliente(rs.getString("nombre_cliente"));
                ped.setMailCliente(rs.getString("CLI_EMAIL"));
                ped.setRutCliente(rs.getLong("RUT_CLIENTE"));
                ped.setDvCliente(rs.getString("DV_CLIENTE"));
                ped.setFechaCompra(rs.getString("FCREACION"));
                ped.setTarjeta(rs.getString("CAT_NRO_TARJETA"));
                ped.setFechaCaptura(rs.getString("POS_FECHA"));
                ped.setMontoCaptura(rs.getDouble("monto_capturado"));
                ped.setRutClienteMas(rs.getString("CAT_RUT_CLIENTE"));
                ped.setLocal(rs.getString("NOM_LOCAL"));
            }
            Db.close(rs, stm);
        } catch (Exception e) {
            System.out.println("* Error getting information of Pedidos from BD Jumbo *");
            System.out.println(Util.getStackTrace(e));
        }
        return ped;
    }
    
    public static Pedido getInfoPedidoEasy(Connection con, Pedido ped) throws SQLException {
        ped.setExisteEnBD(false);    
        double montoTrxHija = 0;
        try {
            String sql = "select sum(trx.POS_MONTO_FP) monto_capturado_hija " +
                         "from bodba.bo_pedidos p " +
                         "inner join bodba.bo_trx_mp trx on trx.ID_PEDIDO = p.ID_PEDIDO " +
                         "where p.ID_PEDIDO_PADRE = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, ped.getIdPedido());
            ResultSet rs = stm.executeQuery();
            
            if ( rs.next() ) {
                if ( rs.getString("monto_capturado_hija") != null )
                    montoTrxHija = rs.getDouble("monto_capturado_hija");                
            }
            Db.close(rs, stm);
        } catch (Exception e) {
            System.out.println("* Error getting information of Pedidos (hija) from BD Easy *");
            System.out.println(Util.getStackTrace(e));
        }
        try {
            String sql = "select upper(p.NOM_CLIENTE) nombre_cliente, cli.CLI_EMAIL, p.RUT_CLIENTE, bp.CAT_RUT_CLIENTE, " +
                         "p.DV_CLIENTE, p.FCREACION, bp.CAT_NRO_TARJETA, trx.POS_FECHA, lo.NOM_LOCAL, " +
                         "sum(trx.POS_MONTO_FP) monto_capturado " +
                         "from bodba.bo_pedidos p " +
                         "inner join bodba.bo_trx_mp trx on trx.ID_PEDIDO = p.ID_PEDIDO " +
                         "inner join fodba.fo_clientes cli on cli.CLI_ID = p.ID_CLIENTE " +
                         "inner join bodba.botonpagocat bp on bp.ID_PEDIDO = p.ID_PEDIDO " +
                         "inner join bodba.bo_locales lo on lo.ID_LOCAL = p.ID_LOCAL " +
                         "where bp.CAT_TIPO_AUTORIZACION = 'A' and p.ID_PEDIDO = ? " +
                         "group by p.NOM_CLIENTE, cli.CLI_EMAIL, p.RUT_CLIENTE, bp.CAT_RUT_CLIENTE, p.DV_CLIENTE, p.FCREACION, bp.CAT_NRO_TARJETA, trx.POS_FECHA, lo.NOM_LOCAL";
            
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, ped.getIdPedido());
            ResultSet rs = stm.executeQuery();
            
            if ( rs.next() ) {
                ped.setExisteEnBD(true);
                ped.setNombreCliente(rs.getString("nombre_cliente"));
                ped.setMailCliente(rs.getString("CLI_EMAIL"));
                ped.setRutCliente(rs.getLong("RUT_CLIENTE"));
                ped.setDvCliente(rs.getString("DV_CLIENTE"));
                ped.setFechaCompra(rs.getString("FCREACION"));
                ped.setTarjeta(rs.getString("CAT_NRO_TARJETA"));
                ped.setFechaCaptura(rs.getString("POS_FECHA"));
                ped.setMontoCaptura( ( rs.getDouble("monto_capturado") + montoTrxHija ));
                ped.setRutClienteMas(rs.getString("CAT_RUT_CLIENTE"));
                ped.setLocal(rs.getString("NOM_LOCAL"));
            }
            Db.close(rs, stm);
        } catch (Exception e) {
            System.out.println("* Error getting information of Pedidos from BD Easy *");
            System.out.println(Util.getStackTrace(e));
        }
        return ped;
    }

}
