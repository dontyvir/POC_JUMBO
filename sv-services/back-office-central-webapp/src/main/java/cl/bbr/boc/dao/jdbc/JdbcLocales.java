/*
 * Created on 15-abr-2010
 */
package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.dao.LocalesDAO;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.jumbo.common.dao.jdbc.JdbcDAO;

/**
 * @author jdroguett
 */
public class JdbcLocales extends JdbcDAO implements LocalesDAO {

   private static ConexionUtil conexionUtil = new ConexionUtil();

   /*
    * (non-Javadoc)
    * 
    * @see cl.bbr.boc.dao.LocalesDAO#getLocales()
    */
   public List getLocales() throws DAOException {
      String sql = "select id_local, cod_local, nom_local from bodba.bo_locales";
      Connection con = null;
      PreparedStatement ps = null;
      ResultSet rs = null;
      List lista = new ArrayList();
      try {
         con = conexionUtil.getConexion();
         ps = con.prepareStatement(sql + " WITH UR");
         rs = ps.executeQuery();
         while(rs.next()){
            LocalDTO local = new LocalDTO();
            local.setId(rs.getInt("id_local"));
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
