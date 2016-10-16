/*
 * Created on 30-ene-2009
 */
package cl.bbr.fo.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.fo.dao.DatosClienteDAO;
import cl.bbr.fo.dto.AlternativaDTO;
import cl.bbr.fo.dto.PreguntaCheckboxDTO;
import cl.bbr.fo.dto.PreguntaComboboxDTO;
import cl.bbr.fo.dto.PreguntaDTO;
import cl.bbr.fo.dto.PreguntaFechaDTO;
import cl.bbr.fo.dto.PreguntaNumeroDTO;
import cl.bbr.fo.dto.RespuestaDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author jdroguett
 */
public class JdbcDatosCliente extends JdbcDAO implements DatosClienteDAO {

   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dao.DatosClienteDAO#getPreguntas()
    */
   public List getPreguntas(int clienteId) throws DAOException {
      List lista = new ArrayList();
      String sql = "select pre.id as pre_id, pre.enunciado as pre_enunciado, pre.control, pre.depende_alt_id, "
            + "alt.id as alt_id, alt.enunciado as alt_enunciado "
            + ",(   select case when dat.alternativa_id is null then dat.respuesta else dat.alternativa_id end "
            + "     from fodba.fo_clientes_datos dat where dat.pregunta_id = pre.id and "
            + "       (dat.alternativa_id = alt.id or dat.alternativa_id is null) and dat.cliente_id = "
            + clienteId
            + " ) as respuesta, "
            + " (select count(*) from fodba.fo_clientes_datos dat_dep where dat_dep.alternativa_id = pre.depende_alt_id and dat_dep.cliente_id = "
            + clienteId
            + " ) as cant "
            + " from fodba.fo_clientes_preguntas pre left outer join fodba.fo_clientes_alternativas alt on alt.pregunta_id = pre.id "
            + " order by pre.orden, pre.id, alt.orden ";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql  + " WITH UR");
         rs = ps.executeQuery();
         PreguntaDTO pre = null;
         int pre_id_old = 0;
         while (rs.next()) {
            int pre_id = rs.getInt("pre_id");
            if (pre_id != pre_id_old) {
               pre_id_old = pre_id;
               if ("numero".equals(rs.getString("control")))
                  pre = new PreguntaNumeroDTO();
               else if ("fecha".equals(rs.getString("control")))
                  pre = new PreguntaFechaDTO();
               else if ("combobox".equals(rs.getString("control")))
                  pre = new PreguntaComboboxDTO();
               else if ("checkbox".equals(rs.getString("control")))
                  pre = new PreguntaCheckboxDTO();

               pre.setId(pre_id);
               pre.setEnunciado(rs.getString("pre_enunciado"));
               pre.setDependeAltId(rs.getInt("depende_alt_id"));
               int resp = rs.getInt("respuesta");
               if (rs.wasNull())
                  resp = -1;
               pre.setRespuesta(resp);
               int cant = rs.getInt("cant");
               logger.debug("cant cant: " + cant);
               pre.setOcultar(cant == 0 && pre.getDependeAltId() != 0);
               lista.add(pre);
            }
            int alt_id = rs.getInt("alt_id");
            if (!rs.wasNull()) {
               AlternativaDTO alt = new AlternativaDTO();
               alt.setId(alt_id);
               alt.setEnunciado(rs.getString("alt_enunciado"));
               alt.setElegida(rs.getInt("respuesta") == alt_id);
               pre.addAlternativas(alt);
            }
         }
      } catch (SQLException e) {
         throw new DAOException(e);
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dao.DatosClienteDAO#updateRespuestas(java.util.List)
    */
   public void updateRespuestas(List respuestas, int clienteId) throws DAOException {
      String sqlDel = "delete from fodba.fo_clientes_datos where cliente_id = " + clienteId;
      String sqlIns = "insert into fodba.fo_clientes_datos (cliente_id, pregunta_id, alternativa_id, respuesta) "
            + " values (?, ?, ?, ?)";

      Connection con = null;
      PreparedStatement ps = null;
      try {
         con = conexionUtil.getConexion();
         con.setAutoCommit(false);
         ps = con.prepareStatement(sqlDel);
         ps.executeUpdate();

         ps = con.prepareStatement(sqlIns);
         for (int i = 0; i < respuestas.size(); i++) {
            RespuestaDTO resp = (RespuestaDTO) respuestas.get(i);
            ps.setInt(1, clienteId);
            ps.setInt(2, resp.getPreguntaId());

            if ("FECHA".equals(resp.getControl()) || "NUMERO".equals(resp.getControl())) {
               ps.setNull(3, Types.INTEGER);
               if (resp.getRespuesta() >= 0){
                  ps.setInt(4, resp.getRespuesta());
                  ps.executeUpdate();
               }
            } else if ("COMBOBOX".equals(resp.getControl()) || "CHECKBOX".equals(resp.getControl())) {
               int[] alts = resp.getAlternativasId();
               ps.setNull(4, Types.INTEGER);
               for (int j = 0; j < alts.length; j++) {
                  if (alts[j] > 0) {
                     ps.setInt(3, alts[j]);
                     ps.executeUpdate();
                  }
               }
            }
         }
        
      } catch (SQLException e) {
         rollback(con);
         throw new DAOException(e);
      } finally {
      	 try {
			con.commit();
		} catch (SQLException e1) {}
        autoCommitTrue(con);
        close(ps, con);
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.fo.dao.DatosClienteDAO#getDependencia()
    */
   public List getDependencia() throws DAOException {
      String sql = "select pre.id, pre.depende_alt_id, alt.pregunta_id as depende_pre_id "
            + " from fodba.fo_clientes_preguntas pre inner join fodba.fo_clientes_alternativas alt "
            + " on pre.depende_alt_id = alt.id";
      List lista = new ArrayList();
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql  + " WITH UR");
         rs = ps.executeQuery();
         while (rs.next()) {
            int[] dep = { rs.getInt("id"), rs.getInt("depende_alt_id"), rs.getInt("depende_pre_id") };
            lista.add(dep);
         }
      } catch (SQLException e) {
         throw new DAOException();
      } finally {
         close(rs, ps, con);
      }
      return lista;
   }
}
