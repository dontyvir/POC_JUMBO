/*
 * Created on 30-ene-2009
 */
package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.dao.DestacadosDAO;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.dto.DestacadoDTO;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.boc.dto.MarcaDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author jdroguett
 */
public class JdbcDestacados extends JdbcDAO implements DestacadosDAO {

    private static ConexionUtil conexionUtil = new ConexionUtil();

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#getDestacados()
     */
    public List getDestacados() throws DAOException {
        String sql = "select d.ID, d.DESCRIPCION, d.FECHA_HORA_INI, d.FECHA_HORA_FIN, lo.cod_local, lo.nom_local, count(p.producto_id) as cantidad "
                + " from BODBA.DESTACADOS d "
                + " left outer join bodba.destacados_productos p on d.id = p.destacado_id "
                + " left outer join bodba.destacados_locales l on d.id = l.destacado_id "
                + " left outer join bodba.bo_locales lo on lo.id_local = l.local_id "
                + " group by d.id, d.DESCRIPCION, d.FECHA_HORA_INI, d.FECHA_HORA_FIN, lo.cod_local, lo.nom_local  "
                + " order by d.id desc";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List lista = new ArrayList();
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql + " WITH UR");
            rs = ps.executeQuery();
            int id = -1;
            DestacadoDTO destacadoDTO = null;
            while (rs.next()) {
                if (rs.getInt("id") != id) {
                    id = rs.getInt("id");
                    destacadoDTO = new DestacadoDTO();
                    LocalDTO local = new LocalDTO();
                    destacadoDTO.setId(rs.getInt("id"));
                    destacadoDTO.setDescripcion(rs.getString("descripcion"));
                    destacadoDTO.setFechaHoraIni(rs.getTimestamp("fecha_hora_ini"));
                    destacadoDTO.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin"));
                    destacadoDTO.setCantidadProductos(rs.getInt("cantidad"));
                    local.setCodigo(rs.getString("cod_local"));
                    local.setNombre(rs.getString("nom_local"));
                    destacadoDTO.addLocal(local);
                    lista.add(destacadoDTO);
                } else {
                    LocalDTO local = new LocalDTO();
                    local.setCodigo(rs.getString("cod_local"));
                    local.setNombre(rs.getString("nom_local"));
                    destacadoDTO.addLocal(local);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            close(rs, ps, con);
        }
        return lista;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#updDestacado(cl.bbr.boc.dto.DestacadoDTO)
     */
    public void updDestacado(DestacadoDTO destacadoDTO) throws DAOException {
        String updateDes = "update destacados set descripcion= ?, fecha_hora_ini= ?, fecha_hora_fin= ?, imagen= ?" +
        		" where id = ? ";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = conexionUtil.getConexion();
            con.setAutoCommit(false);
            ps = con.prepareStatement(updateDes);
            ps.setString(1, destacadoDTO.getDescripcion());
            ps.setTimestamp(2, new Timestamp(destacadoDTO.getFechaHoraIni().getTime()));
            ps.setTimestamp(3, new Timestamp(destacadoDTO.getFechaHoraFin().getTime()));
            ps.setString(4, destacadoDTO.getImagen());
            ps.setInt(5, destacadoDTO.getId());
            ps.executeUpdate();

            if (destacadoDTO.getLocales().size() > 0) {
                delLocales(con, destacadoDTO.getId());
                insLocales(con, destacadoDTO.getId(), destacadoDTO.getLocales());
            }
            if (destacadoDTO.getProductos().size() > 0) {
                delProductos(con, destacadoDTO.getId());
                insProductos(con, destacadoDTO.getId(), destacadoDTO.getProductos());
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            try {
				con.commit();
				con.setAutoCommit(true);
				close(ps, con);
				
			} catch (SQLException e) {
				logger.error("[Metodo] : updDestacado - Problema SQL (close)", e);
			}
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#addDestacado(cl.bbr.boc.dto.DestacadoDTO)
     */
    public void addDestacado(DestacadoDTO destacadoDTO) throws DAOException {
        String insertDes = "insert into destacados(descripcion, fecha_hora_ini, fecha_hora_fin, imagen)values(?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = conexionUtil.getConexion();
            con.setAutoCommit(false);

            ps = con.prepareStatement(insertDes, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, destacadoDTO.getDescripcion());
            ps.setTimestamp(2, new Timestamp(destacadoDTO.getFechaHoraIni().getTime()));
            ps.setTimestamp(3, new Timestamp(destacadoDTO.getFechaHoraFin().getTime()));
            ps.setString(4, destacadoDTO.getImagen());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                destacadoDTO.setId(rs.getInt(1));
            }

            insLocales(con, destacadoDTO.getId(), destacadoDTO.getLocales());
            insProductos(con, destacadoDTO.getId(), destacadoDTO.getProductos());

        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new DAOException(e);
        } finally {
            try {
				con.commit();
				con.setAutoCommit(true);
				close(rs, ps, con);
				
			} catch (SQLException e) {
				logger.error("[Metodo] : updDestacado - Problema SQL (close)", e);
			}
        }
    }

    /**
     * Se le pasa la conexión para cuenda se use una transación explícita
     * 
     * @param con
     * @param destacadoId
     * @param locales
     * @throws SQLException
     */
    private void insLocales(Connection con, int destacadoId, List locales) throws SQLException {
        String insertLoc = "insert into destacados_locales(local_id, destacado_id) values(?,?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insertLoc);
            for (int i = 0; i < locales.size(); i++) {
                LocalDTO local = (LocalDTO) locales.get(i);
                ps.setInt(1, local.getId());
                ps.setInt(2, destacadoId);
                ps.executeUpdate();
            }
        } finally {
            close(ps);
        }
    }

    /**
     * 
     * @param con
     * @param destacadoId
     * @throws SQLException
     */
    private void delLocales(Connection con, int destacadoId) throws SQLException {
        String insertLoc = "delete from destacados_locales where destacado_id = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insertLoc);
            ps.setInt(1, destacadoId);
            ps.executeUpdate();
        } finally {
            close(ps);
        }
    }

    /**
     * 
     * @param con
     * @param destacadoId
     * @throws SQLException
     */
    private void delProductos(Connection con, int destacadoId) throws SQLException {
        String insertPro = "delete from destacados_productos where destacado_id = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insertPro);
            ps.setInt(1, destacadoId);
            ps.executeUpdate();
        } finally {
            close(ps);
        }
    }

    /**
     * Se le pasa la conexión para cuenda se use una transación explícita
     * 
     * @param con
     * @param destacadoId
     * @param productos
     * @throws SQLException
     */
    private void insProductos(Connection con, int destacadoId, List productos) throws SQLException {
       //producto_id del FO_PRODUCTOS == pro_id
        String insertPro = "insert into destacados_productos(destacado_id, producto_id) values(?,?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(insertPro);
            for (int i = 0; i < productos.size(); i++) {
                FOProductoDTO producto = (FOProductoDTO) productos.get(i);
                ps.setInt(1, destacadoId);
                ps.setInt(2, producto.getId());
                ps.executeUpdate();
            }
        } finally {
            close(ps);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#delDestacado(int)
     */
    public void delDestacado(int id) throws DAOException {
        //las refeferencias estan ON DELETE CASCADE
        String sql = "delete from bodba.destacados where id = " + id;
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            close(ps, con);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#getDestacado(int)
     */
    public DestacadoDTO getDestacado(int id) throws DAOException {
        String sql = "select d.ID, d.DESCRIPCION, d.FECHA_HORA_INI, d.FECHA_HORA_FIN, d.imagen "
                + " from BODBA.DESTACADOS d where id = " + id;

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DestacadoDTO destacado = null;
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            destacado = new DestacadoDTO();

            while (rs.next()) {
                destacado.setId(rs.getInt("id"));
                destacado.setDescripcion(rs.getString("descripcion"));
                destacado.setFechaHoraIni(rs.getTimestamp("fecha_hora_ini"));
                destacado.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin"));
                destacado.setImagen(rs.getString("imagen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            close(rs, ps, con);
        }
        return destacado;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#getProductosDestacados(int)
     */
    public List getProductosDestacados(int id) throws DAOException {
        String sql = "select dp.producto_id, pro_cod_sap, pro_tipo_producto, pro_des_corta, mar_id, mar_nombre   "
                + " from bodba.destacados_productos dp "
                + " inner join fodba.fo_productos fp on fp.pro_id = dp.producto_id  "
                + " left outer join fodba.fo_marcas m on m.mar_id = fp.pro_mar_id "
                + " where destacado_id = "
                + id
                + " order by dp.producto_id desc ";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List lista = new ArrayList();
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            FOProductoDTO producto = null;
            while (rs.next()) {
                producto = new FOProductoDTO();
                producto.setId(rs.getInt("producto_id"));
                producto.setCodSap(rs.getString("pro_cod_sap"));
                producto.setDescripcion(rs.getString("pro_tipo_producto") + " " + rs.getString("pro_des_corta"));
                MarcaDTO marca = new MarcaDTO(rs.getInt("mar_id"), rs.getString("mar_nombre"));
                producto.setMarca(marca);
                lista.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            close(rs, ps, con);
        }
        return lista;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.bbr.boc.dao.DestacadosDAO#getDestacadoLocales(int)
     */
    public List getDestacadoLocales(int id) throws DAOException {
        String sql = "select dl.local_id, cod_local, nom_local " + " from bodba.destacados_locales dl "
                + " inner join bodba.bo_locales l on l.id_local = dl.local_id " + " where destacado_id = " + id
                + " order by l.cod_local";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List lista = new ArrayList();
        try {
            con = conexionUtil.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            LocalDTO local = null;
            while (rs.next()) {
                local = new LocalDTO();
                local.setId(rs.getInt("local_id"));
                local.setCodigo(rs.getString("cod_local"));
                local.setNombre(rs.getString("nom_local"));
                lista.add(local);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        } finally {
            close(rs, ps, con);
        }
        return lista;
    }
}
