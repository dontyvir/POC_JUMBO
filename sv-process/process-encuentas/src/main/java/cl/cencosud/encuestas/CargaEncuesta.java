/*
 * Created on 19-mar-2010
 */
package cl.cencosud.encuestas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cl.cencosud.encuestas.datos.Mail;
import cl.cencosud.encuestas.datos.Pedido;
import cl.cencosud.util.Db;

/**
 * @author imoyano
 */
public class CargaEncuesta {

    public List getPedidosParaEncuestas(Connection con) throws SQLException {
        List pedidos = new ArrayList();
        PreparedStatement stm = null;
        ResultSet rs = null;
        String sql = "select p.ID_PEDIDO, c.CLI_RUT, c.CLI_NOMBRE nombre, c.CLI_EMAIL, p.DIR_TIPO_CALLE, " +
                     "p.DIR_CALLE, p.DIR_NUMERO, p.DIR_DEPTO, co.NOMBRE comuna, pe.FECHA_HORA_LLEGADA_DOM entrega, " +
                     "MONTH( p.FCREACION ) mes " +
                     "from bodba.bo_pedidos p " +
                     "inner join fodba.fo_clientes c on c.CLI_ID = p.ID_CLIENTE " +
                     "inner join bodba.bo_comunas co on co.ID_COMUNA = p.ID_COMUNA " +
                     "inner join bodba.bo_pedidos_ext pe on pe.ID_PEDIDO = p.ID_PEDIDO " +
                     "where p.ID_ESTADO = 10 and " +
                     "pe.FECHA_HORA_LLEGADA_DOM is not null and " +
                     "c.CLI_RECIBE_ENCUESTAS = 1 and " +
                     "pe.ENCUESTA_ENVIAR = 1 and " +
                     "pe.FECHA_HORA_LLEGADA_DOM < ? ";
        
        GregorianCalendar hoy = new GregorianCalendar();
        hoy.add(Calendar.DAY_OF_MONTH, -3);
        
        stm = con.prepareStatement(sql);
        stm.setDate(1, new java.sql.Date(hoy.getTimeInMillis()));
        rs = stm.executeQuery();

        while (rs.next()) {
            Pedido ped = new Pedido();
            ped.setIdPedido(rs.getLong("id_pedido"));            
            if ( rs.getString("nombre")!=null && rs.getString("mes")!=null && rs.getString("CLI_EMAIL")!=null && rs.getString("CLI_RUT")!=null ) {
                ped.setNombreCliente(rs.getString("nombre"));
    //            String direccion = "";
    //            if ( rs.getString("DIR_TIPO_CALLE") != null )
    //                direccion += " " + rs.getString("DIR_TIPO_CALLE");
    //            if ( rs.getString("DIR_CALLE") != null )
    //                direccion += " " + rs.getString("DIR_CALLE");
    //            if ( rs.getString("DIR_NUMERO") != null )
    //                direccion += " " + rs.getString("DIR_NUMERO");
    //            if ( rs.getString("DIR_DEPTO") != null )
    //                direccion += " " + rs.getString("DIR_DEPTO");
    //            ped.setDireccionCliente(direccion);
    //            ped.setComunaCliente(rs.getString("comuna"));
                ped.setFechaHoraEntrega(rs.getString("entrega"));
                ped.setMesPedido(rs.getString("mes"));
                ped.setEmailCliente(rs.getString("CLI_EMAIL"));
                ped.setRutCliente(rs.getString("CLI_RUT"));
                pedidos.add(ped);
            }
        }
        Db.close(rs, stm);
        return pedidos;
    }
    
    
    public String addMail(Connection con, List mails) throws SQLException {
        String pedidosConEncuestaEnviada = "";
        String coma = "";
        
        String sql = "INSERT INTO FODBA.FO_SEND_MAIL " +
                     "( FSM_IDFRM ,FSM_REMITE, FSM_DESTINA, FSM_SUBJECT, FSM_DATA, FSM_ESTADO, FSM_STMPSEND ) " +
                     "VALUES " +
                     "( ?, ?, ?, ?, ?, ?, ? ) ";
        PreparedStatement stm = con.prepareStatement(sql);
        
        for ( int i=0; i < mails.size(); i++ ) {
            try {
                Mail mail = (Mail) mails.get(i);
                stm.setString(1, "FO" ); 
                stm.setString(2, mail.getRemitente() );            
                stm.setString(3, mail.getDestinatario() );
                stm.setString(4, mail.getTitulo() );           
                stm.setString(5, mail.getContenido() );          
                stm.setString(6, "0" );                   
                stm.setNull(7, java.sql.Types.INTEGER );            
                stm.executeUpdate();
                
                pedidosConEncuestaEnviada += coma + "" + mail.getIdPedido();
                coma = ",";
            } catch (Exception e) {
                System.out.println("Error al enviar encuesta:" + e.getMessage());
            }
        }
        Db.close(stm);
        return pedidosConEncuestaEnviada;
    }


    public void modificaPedidoConEncuestaEnviada(Connection con, String pedidosConEncuestaEnviada) throws SQLException {
        String sql = "UPDATE BODBA.BO_PEDIDOS_EXT " +
                     "SET ENCUESTA_ENVIAR = 2 " +
                     "WHERE ID_PEDIDO IN ( " + pedidosConEncuestaEnviada + ") ";
        PreparedStatement stm = con.prepareStatement(sql);
        stm.executeUpdate();
        Db.close(stm);        
    }   
}
