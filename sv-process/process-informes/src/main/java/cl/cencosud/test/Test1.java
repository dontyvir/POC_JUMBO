/*
 * Created on 24-mar-2010
 */
package cl.cencosud.test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cl.bbr.jumbocl.common.utils.Cifrador;
import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 *
 */
public class Test1 {

   public static void main(String[] args) throws Exception {
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      Connection con = Db.conexion(user, password, driver, url);
      System.out.println("CON : " + con);
      
      String sql = "select distinct num_mp from bo_pedidos where id_estado in (8,10) and medio_pago in ('CAT', 'PAR')";
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
     
      PrintWriter escribir = new PrintWriter(new BufferedWriter(new FileWriter("d:/tmp/cat.txt")));
      while(rs.next()){
         escribir.println(Cifrador.desencriptar("C6J11M1P4R5BCPVA",rs.getString(1)));
      }
      escribir.close();
   }
}
