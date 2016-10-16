package cl.jumbo.url.util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Db
{
  public static Connection conexion(String user, String password, String driver, String url)
    throws Exception
  {
    Connection con = null;
    try {
      try {
        Class.forName(driver).newInstance();
      } catch (ClassNotFoundException e) {
        throw new Exception("Error: El driver no se encuentra");
      }
      Properties p = new Properties();
      p.setProperty("user", user);
      p.setProperty("password", password);

      if ((con = DriverManager.getConnection(url, p)) == null) {
        throw new Exception("Error: la conexion es nula");
      }
      con.setTransactionIsolation(1);
    } catch (Exception e) {
      System.out.println("Error al obtener nueva conexión: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("Error al obtener nueva conexión: " + e.getMessage());
    }
    return con;
  }

  public static void close(Connection con) {
    if (con != null)
      try {
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
  }

  public static void close(Statement st)
  {
    if (st != null)
      try {
        st.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
  }

  public static void close(ResultSet rs)
  {
    if (rs != null)
      try {
        rs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
  }

  public static void close(ResultSet rs, Statement st)
  {
    close(rs);
    close(st);
  }
}