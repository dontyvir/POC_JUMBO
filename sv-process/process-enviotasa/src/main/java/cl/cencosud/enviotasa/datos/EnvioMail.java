/*
 * Created on 19-mar-2010
 */
package cl.cencosud.enviotasa.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cl.cencosud.enviotasa.util.Db;
import cl.cencosud.enviotasa.util.Util;

/**
 * @author imoyano
 */
public class EnvioMail {

    public static void addMail(Connection con, Mail mail) throws SQLException {
        String sql = "INSERT INTO FODBA.FO_SEND_MAIL " +
                     "( FSM_IDFRM ,FSM_REMITE, FSM_DESTINA, FSM_COPIA, FSM_SUBJECT, FSM_DATA, FSM_ESTADO, FSM_STMPSEND ) " + 
                     "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
        PreparedStatement stm = con.prepareStatement(sql);
        try {
            stm.setString(1, "FO");
            stm.setString(2, mail.getRemitente());
            stm.setString(3, mail.getDestinatario());
            if ( mail.getCopia() != null && !"".equalsIgnoreCase( mail.getCopia() ) ) {
                stm.setString(4, mail.getCopia());    
            } else {
                stm.setNull(4, java.sql.Types.VARCHAR);
            }
            stm.setString(5, mail.getTitulo());
            stm.setString(6, mail.getContenido());
            stm.setString(7, "0");
            stm.setNull(8, java.sql.Types.INTEGER);
            stm.executeUpdate();

        } catch (Exception e) {
            System.out.println("* Error saving mail information in the BD *");
            System.out.println(Util.getStackTrace(e));
        }
        Db.close(stm);
    }

}
