/*
 * Created on 09-nov-2009
 *
 */
package cl.bbr.bol.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.bbr.bol.dao.PickeadorDAO;
import cl.bbr.bol.dto.AsistenciaDTO;
import cl.bbr.bol.dto.AsistenciaPickeadorDTO;
import cl.bbr.bol.dto.EncargadoDTO;
import cl.bbr.bol.dto.PickeadorDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author jdroguett
 *  
 */
public class PickeadorJdbc extends JdbcDAO implements PickeadorDAO {
   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.bol.dao.PickeadorDAO#getEncargados(int)
    */
   public List getEncargados(int localId) throws DAOException {
      List lista = new ArrayList();
      String sql = "select u.id_usuario, login, nombre, apellido, apellido_mat, count(ep.pickeador_id) as nPickeadores "
            + "from bodba.bo_usuarios u inner join bodba.bo_usuarios_locales l on l.id_usuario = u.id_usuario "
            + "inner join bodba.bo_usuxperf p on u.id_usuario = p.id_usuario "
            + "inner join bodba.encargados_pickeadores ep on ep.encargado_id = u.id_usuario "
            + "inner join bodba.bo_usuarios_locales lp on lp.id_usuario = ep.pickeador_id  "
            + "where l.id_local = "
            + localId
            + " and lp.id_local = "
            + localId
            + " and id_perfil = 63 and estado = 'A' "
            + "group by u.id_usuario, login, nombre, apellido, apellido_mat ";

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         
         while (rs.next()) {
            EncargadoDTO dto = new EncargadoDTO();
            dto.setId(rs.getInt("id_usuario"));
            dto.setLogin(rs.getString("login"));
            String nombre = rs.getString("nombre") + " " + rs.getString("apellido") + " "
                  + rs.getString("apellido_mat");
            dto.setNombre(nombre);
            dto.setPickeadoresAsociados(rs.getInt("nPickeadores"));
            lista.add(dto);
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
    * @see cl.bbr.bol.dao.PickeadorDAO#getEncargados(int, java.lang.String)
    */
   public List getEncargados(int localId, String patron) throws DAOException {
      List lista = new ArrayList();
      String sql = "select u.id_usuario, login, nombre, apellido, apellido_mat "
            + "from bodba.bo_usuarios u inner join bodba.bo_usuarios_locales l on l.id_usuario = u.id_usuario "
            + "inner join bodba.bo_usuxperf p on u.id_usuario = p.id_usuario " + " where l.id_local = " + localId
            + " and id_perfil = 63 and estado = 'A' " + " and (lower(login) like '%" + patron
            + "%' or lower(nombre || '  ' || apellido || '  ' || apellido_mat) like '%" + patron + "%')";

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         while (rs.next()) {
            EncargadoDTO dto = new EncargadoDTO();
            dto.setId(rs.getInt("id_usuario"));
            dto.setLogin(rs.getString("login"));
            String nombre = rs.getString("nombre") + " " + rs.getString("apellido") + " "
                  + rs.getString("apellido_mat");
            dto.setNombre(nombre);
            lista.add(dto);
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
    * @see cl.bbr.bol.dao.PickeadorDAO#getPickeadoresPorEncargado(int, java.lang.String)
    */
   public List getPickeadoresPorEncargado(int localId, int encargadoId) throws DAOException {
      List lista = new ArrayList();
      String sql = "select id_usuario, login, nombre, apellido || ' ' || apellido_mat as apellido, case when pickeador_id is null then 0 else 1 end as orden "
            + " from (select u.id_usuario, login, nombre, apellido, apellido_mat "
            + " from bodba.bo_usuarios u "
            + " inner join bodba.bo_usuarios_locales l on l.id_usuario = u.id_usuario "
            + " inner join bodba.bo_usuxperf p on u.id_usuario = p.id_usuario "
            + " where l.id_local = ? "
            + " and id_perfil = 12 and estado = 'A' ) as pick "
            + " left outer  join "
            + " (select pickeador_id "
            + " from bodba.bo_usuarios e left outer join bodba.encargados_pickeadores ep on e.id_usuario = ep.encargado_id "
            + " where e.id_usuario = ? ) as enc "
            + " on pick.id_usuario = pickeador_id "
            + " order by orden desc, apellido";

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         ps.setInt(1, localId);
         ps.setInt(2, encargadoId);
         rs = ps.executeQuery();
         while (rs.next()) {
            PickeadorDTO dto = new PickeadorDTO();
            dto.setId(rs.getInt("id_usuario"));
            dto.setLogin(rs.getString("login"));
            dto.setNombre(rs.getString("nombre"));
            dto.setApellido(rs.getString("apellido"));
            dto.setAsignado(rs.getInt("orden") > 0);
            lista.add(dto);
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
    * @see cl.bbr.bol.dao.PickeadorDAO#updAsociacion(int[])
    */
   public void updAsociacion(int[] pickeadoresId, int encargadoId, int localId) throws DAOException {
      String sqlDel = "delete from bodba.encargados_pickeadores where encargado_id = " + encargadoId
            + " and local_id = " + localId;
      String sqlIns = "insert into bodba.encargados_pickeadores (encargado_id, pickeador_id, local_id) values (?,?,?)";

      Connection con = null;
      PreparedStatement ps = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sqlDel);
         ps.executeUpdate();

         ps = con.prepareStatement(sqlIns);
         ps.setInt(1, encargadoId);
         ps.setInt(3, localId);
         for (int i = 0; i < pickeadoresId.length; i++) {
            ps.setInt(2, pickeadoresId[i]);
            ps.executeUpdate();
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.bol.dao.PickeadorDAO#getAsistencias()
    */
   public List getAsistencias() throws DAOException {
      List lista = new ArrayList();
      String sql = "select id, nombre, descripcion from bodba.asistencias order by orden";

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql);
         rs = ps.executeQuery();
         while (rs.next()) {
            AsistenciaDTO dto = new AsistenciaDTO();
            dto.setId(rs.getInt("id"));
            dto.setNombre(rs.getString("nombre"));
            dto.setDescripcion(rs.getString("descripcion"));
            lista.add(dto);
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
    * @see cl.bbr.bol.dao.PickeadorDAO#getPickeadores(int)
    */
   public List getAsistenciasPickeadores(int localId, int encargadoId, Date fecha) throws DAOException {
      List lista = new ArrayList();
      String sql = "select id_usuario, login, u.nombre, apellido, apellido_mat, ap.id, ap.fecha, a.nombre as asistencia "
            + " from bodba.encargados_pickeadores e inner join bodba.bo_usuarios u on u.id_usuario = e.pickeador_id "
            + " left outer join bodba.asistencias_pickeadores ap on ap.pickeador_id = e.pickeador_id and ap.local_id = e.local_id and ap.fecha = ? "
            + " left outer join bodba.asistencias a on a.id = ap.asistencia_id             "
            + " where e.encargado_id = ? and e.local_id = ?";

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         ps.setDate(1, new java.sql.Date(fecha.getTime()));
         ps.setInt(2, encargadoId);
         ps.setInt(3, localId);
         rs = ps.executeQuery();
         while (rs.next()) {
            PickeadorDTO dto = new PickeadorDTO();
            dto.setId(rs.getInt("id_usuario"));
            dto.setLogin(rs.getString("login"));
            dto.setNombre(rs.getString("nombre"));
            dto.setApellido(rs.getString("apellido") + " " + rs.getString("apellido_mat"));
            AsistenciaPickeadorDTO asis = new AsistenciaPickeadorDTO();
            asis.setId(rs.getInt("id"));
            asis.setAsistencia(rs.getString("asistencia"));
            dto.setAsistencia(asis);
            lista.add(dto);
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
    * @see cl.bbr.bol.dao.PickeadorDAO#updAsistencia(cl.bbr.bol.dto.AsistenciaPickeadorDTO)
    */
   public void updAsistencia(AsistenciaPickeadorDTO asistenciaPick) throws DAOException {
      String sqlUpd = "update bodba.asistencias_pickeadores "
            + "set asistencia_id = (select id from bodba.asistencias where nombre = ?), local_id = ? where id = ?";
      String sqlIns = "insert into bodba.asistencias_pickeadores(pickeador_id, fecha, asistencia_id, local_id) "
            + "values (?,?,(select id from bodba.asistencias where nombre = ?), ?)";
      String sqlDel = "delete from bodba.asistencias_pickeadores where id = ?";

      Connection con = null;
      PreparedStatement ps = null;
      try {
         con = conexionUtil.getConexion();
         if (asistenciaPick.getId() != 0){
            if(asistenciaPick.getAsistencia() == null || asistenciaPick.getAsistencia().equals("")){
               ps = con.prepareStatement(sqlDel);
               ps.setInt(1, asistenciaPick.getId());
               ps.executeUpdate();
            }else{
               ps = con.prepareStatement(sqlUpd);
               ps.setString(1, asistenciaPick.getAsistencia());
               ps.setInt(2, asistenciaPick.getLocalId());
               ps.setInt(3, asistenciaPick.getId());
               ps.executeUpdate();
            }
         } else if (asistenciaPick.getId() == 0 && (asistenciaPick.getAsistencia() != null && !asistenciaPick.getAsistencia().equals(""))) {
            ps = con.prepareStatement(sqlIns);
            ps.setInt(1, asistenciaPick.getPickeadorId());
            ps.setDate(2, new java.sql.Date(asistenciaPick.getFecha().getTime()));
            ps.setString(3, asistenciaPick.getAsistencia());
            ps.setInt(4, asistenciaPick.getLocalId());
            ps.executeUpdate();
         }
      } catch (Exception e) {
         throw new DAOException(e);
      } finally {
         close(ps, con);
      }
   }
}
