/*
 * Created on 30-ene-2009
 *
 */
package cl.jumbo.common.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import cl.bbr.jumbocl.shared.log.Logging;

/**
 * @author jdroguett
 *
 */
public class JdbcDAO {
    public Logging logger = new Logging(this);
    
    /**
     * Cierra todo
     * 
     * @param rs
     * @param ps
     * @param con
     */
    public void close(ResultSet rs, PreparedStatement st, Connection con) {
        close(rs);
        close(st);
        close(con);
    }
    
    public void close(PreparedStatement st, Connection con) {
       close(st);
       close(con);
   }

    /**
     * Cierra el resulset
     * 
     * @param rs
     */
    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar el resulset: ", e);
            }
        }
    }

    /**
     * Cierra el statement
     * 
     * @param st
     */
    public void close(PreparedStatement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar el Statement: ", e);
            }
        }
    }

    /**
     * Cierra la conexión
     * 
     * @param con
     */
    public void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar la Connection: ", e);
            }
        }
    }
    
    public void rollback(Connection con){
       if (con != null) {
          try {
              con.rollback();
          } catch (SQLException e) {
              logger.error("Error en rollback: ", e);
          }
      }
    }
    
    public void autoCommitTrue(Connection con){
       if (con != null) {
          try {
              con.setAutoCommit(false);
          } catch (SQLException e) {
              logger.error("Error en con.setAutoCommit(false): ", e);
          }
      }
    }
}
