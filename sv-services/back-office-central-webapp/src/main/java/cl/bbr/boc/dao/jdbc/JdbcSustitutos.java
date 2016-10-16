/*
 * Created on 13-may-2009
 */
package cl.bbr.boc.dao.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.dao.SustitutosDAO;
import cl.bbr.boc.dto.DetallePedidoDTO;
import cl.bbr.boc.dto.SustitutoDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author jdroguett
 */
public class JdbcSustitutos extends JdbcDAO implements SustitutosDAO {
   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.SustitutosDAO#detallePedido(int)
    */
   public List detallePedido(int pedidoId) throws DAOException {
      String sql = "select ped.id_local, det.id_dpicking, det.id_producto, det.cbarra, det.precio, det.cant_pick, det.descripcion, detp.descripcion as descripcion_producto "
            + " from bodba.bo_detalle_picking det inner join bodba.bo_pedidos ped on ped.id_pedido = det.id_pedido "
            + " inner join bodba.bo_detalle_pedido detp on detp.id_detalle = det.id_detalle "
            + " where ped.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and ped.id_pedido = " + pedidoId;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      List lista = new ArrayList();
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         while (rs.next()) {
            DetallePedidoDTO det = new DetallePedidoDTO();
            det.setLocalId(rs.getInt("id_local"));
            det.setId(rs.getInt("id_dpicking"));
            det.setProductoId(rs.getInt("id_producto"));
            det.setBarra(rs.getString("cbarra"));
            det.setPrecio(rs.getInt("precio"));
            det.setCantidadPickeada(rs.getBigDecimal("cant_pick"));
            det.setDescripcion(rs.getString("descripcion"));
            det.setDescripcionProducto(rs.getString("descripcion_producto"));
            lista.add(det);
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.SustitutosDAO#sustituto(java.lang.String, java.lang.String)
    */
   public SustitutoDTO sustituto(String localId, String barra) throws DAOException {
      String sql = "SELECT PRE.ID_LOCAL, L.NOM_LOCAL, PRE.PREC_VALOR, CB.COD_BARRA, P.ID_PRODUCTO, P.DES_LARGA "
            + " FROM BODBA.BO_PRODUCTOS P "
            + "     inner JOIN BODBA.BO_CODBARRA CB ON CB.ID_PRODUCTO  = P.ID_PRODUCTO "
            + "     inner JOIN BODBA.BO_PRECIOS PRE ON PRE.ID_PRODUCTO = P.ID_PRODUCTO "
            + "     inner JOIN BODBA.BO_LOCALES L   ON L.ID_LOCAL      = PRE.ID_LOCAL  " + " WHERE CB.COD_BARRA = '"
            + barra + "' and L.id_local = " + localId;
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         if (rs.next()) {
            SustitutoDTO sustituto = new SustitutoDTO();
            sustituto.setLocal(rs.getString("nom_local"));
            sustituto.setPrecio(rs.getInt("prec_valor"));
            sustituto.setBarra(rs.getString("cod_barra"));
            sustituto.setDescripcion(rs.getString("des_larga"));
            sustituto.setProductoId(rs.getInt("id_producto"));
            return sustituto;
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.SustitutosDAO#updateSustituto(cl.bbr.boc.dto.SustitutoDTO)
    */
   public int updateSustituto(SustitutoDTO sustituto) throws DAOException {
      String sql = "update bodba.bo_detalle_picking set id_producto = ? , cbarra = ?, precio = ?, descripcion = ? "
            + " where id_dpicking = ? ";
      Connection con = null;
      PreparedStatement ps = null;
      int n = 0;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         ps.setInt(1, sustituto.getProductoId());
         ps.setString(2, sustituto.getBarra());
         ps.setBigDecimal(3, new BigDecimal(sustituto.getPrecio()));
         ps.setString(4, sustituto.getDescripcion());
         ps.setInt(5, sustituto.getDetPickingId());
         n = ps.executeUpdate();
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }
      return n;
   }
}
