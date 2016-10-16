/*
 * Created on 29-mar-2010
 */
package cl.bbr.bol.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.bbr.bol.dao.TransporteDAO;
import cl.bbr.bol.dto.CuadraturaDTO;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author jdroguett
 */
public class TransporteJdbc extends JdbcDAO implements TransporteDAO {
	
   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.bol.dao.TransporteDAO#getCuadratura(java.util.Date, java.util.Date, int, int, java.sql.Time,
    *      java.sql.Time)
    */
   public List getCuadratura(int localId, Date fechaIni, Date fechaFin, int patente_id, int chofer_id,
         Time horaIniDesp, Time horaFinDesp) throws DAOException {
      List lista = new ArrayList();
      String sql = "select jd.fecha as fechaDespacho, r.id_ruta, pe.id_pedido, pt.patente, ct.nombre_chofer,"
            + " p.dir_tipo_calle, p.dir_calle,  p.dir_numero, dir_depto, c.nombre as comuna,"
            + " hd.hini, hd.hfin, r.fecha_hora_salida, pe.fecha_hora_llegada_dom, pe.fecha_hora_salida_dom,"
            + " pe.reprogramada, rd.nombre_responsable, md.motivo, "
            + "(select count(*) from bodba.bo_bins_pedido bp where bp.tipo = 'F' and bp.id_pedido = p.id_pedido) as cant_bins "
            + " from bodba.bo_ruta r inner join bodba.bo_pedidos_ext pe on pe.id_ruta = r.id_ruta"
            + " inner join bodba.bo_patente_trans pt on pt.id_patente_trans = r.id_patente_trans"
            + " inner join bodba.bo_chofer_trans ct on ct.id_chofer_trans = r.id_chofer_trans"
            + " inner join bodba.bo_pedidos p on p.id_pedido = pe.id_pedido"
            + " inner join bodba.bo_comunas c on c.id_comuna = p.id_comuna"
            + " inner join bodba.bo_jornada_desp jd on jd.id_jdespacho = p.id_jdespacho"
            + " inner join bodba.bo_horario_desp hd on hd.id_hor_desp = jd.id_hor_desp"
            + " left outer join bodba.bo_reprogramacion_desp rp on rp.id_pedido = pe.id_pedido"
            + " left outer  join bodba.bo_responsable_despacho rd on rd.id_responsable_desp = rp.id_responsable_desp"
            + " left outer join bodba.bo_motivo_despacho md on md.id_motivo_desp = rp.id_motivo_desp"
            + " where p.id_estado not in (" + Constantes.ID_PEDIDOS_PREINGRESADOS + ") and r.id_local = ? and r.id_estado = 67 and jd.fecha >= ? and jd.fecha <= ?";
      if (chofer_id > 0) {
         sql += " and r.id_chofer_trans = " + chofer_id;
      }
      if (patente_id > 0) {
         sql += " and r.id_patente_trans = " + patente_id;
      }
      if (horaIniDesp != null && horaFinDesp != null) {
         sql += " and hd.hini = ? and hd.hfin = ? ";
      }
      sql += " order by jd.fecha, r.id_ruta, pe.id_pedido";

      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         ps.setInt(1, localId);
         ps.setDate(2, new java.sql.Date(fechaIni.getTime()));
         ps.setDate(3, new java.sql.Date(fechaFin.getTime()));
         if (horaIniDesp != null && horaFinDesp != null) {
            ps.setTime(4, horaIniDesp);
            ps.setTime(5, horaFinDesp);
         }
         rs = ps.executeQuery();
         while (rs.next()) {
            CuadraturaDTO c = new CuadraturaDTO();
            c.setFechaDespacho(new Date(rs.getDate("fechaDespacho").getTime()));
            c.setRutaId(rs.getInt("id_ruta"));
            c.setPedidoId(rs.getInt("id_pedido"));
            c.setCantidadBines(rs.getInt("cant_bins"));
            c.setPatente(rs.getString("patente"));
            c.setChofer(rs.getString("nombre_chofer"));
            c.setDireccionDespacho(rs.getString("dir_tipo_calle") + " " + rs.getString("dir_calle") + " "
                  + rs.getString("dir_numero") + " " + (rs.getString("dir_depto")));
            c.setDireccionDespacho(c.getDireccionDespacho().replaceAll("null", "").trim());
            c.setComuna(rs.getString("comuna"));
            Time hini = rs.getTime("hini");
            Time hfin = rs.getTime("hfin");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            c.setJornadaDespacho(sdf.format(hini) + "-" + sdf.format(hfin));
            c.setHoraActivacionRuta(new Date(rs.getTimestamp("fecha_hora_salida").getTime()));
            c.setHoraLlegadaDomicilio(rs.getTimestamp("fecha_hora_llegada_dom") == null ? null : new Date(rs
                  .getTimestamp("fecha_hora_llegada_dom").getTime()));
            c.setHoraSalidaDomicilio(rs.getTimestamp("fecha_hora_salida_dom") == null ? null : new Date(rs
                  .getTimestamp("fecha_hora_salida_dom").getTime()));
            c.setReprogramacion(rs.getInt("reprogramada"));
            c.setResponsable(rs.getString("nombre_responsable"));
            c.setMotivo(rs.getString("motivo"));
            lista.add(c);
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }
}
