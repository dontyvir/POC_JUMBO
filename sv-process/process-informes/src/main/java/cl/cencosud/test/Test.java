/*
 * Created on 29-sep-2009
 */
package cl.cencosud.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Stack;
import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 */
public class Test {
   public static void main(String[] args) throws Exception {

      String[][] fact = cargarCSV("facturas.csv");

      List facturas = new ArrayList();
      for (int i = 0; i < fact.length; i++) {
         Factura f = new Factura();
         f.setNumero(Integer.parseInt(fact[i][0]));
         f.setMonto(Integer.parseInt(fact[i][1]));
         String fecha = fact[i][2];
         f.setFecha(new GregorianCalendar(Integer.parseInt(fecha.substring(6, 10)), Integer.parseInt(fecha.substring(3,
               5)) - 1, Integer.parseInt(fecha.substring(0, 2))));

         f.setLocal(fact[i][3]);
         facturas.add(f);
         //System.out.println(f.getFecha().getTime());
      }

      List transacciones = getTrx();

      int total = 0;
      for (int i = 0; i < facturas.size(); i++) {
         Factura f = (Factura) facturas.get(i);
         total += f.getMonto();
      }
      System.out.println(total);

      total = 0;
      for (int i = 0; i < transacciones.size(); i++) {
         Transaccion t = (Transaccion) transacciones.get(i);
         total += t.getMonto();
      }
      System.out.println(total);

      List conSucursal = new ArrayList();

      System.out.println(facturas.size());
      System.out.println(transacciones.size());

      //primero mismo monto-fecha-local
      for (int i = 0; i < facturas.size(); i++) {
         Factura f = (Factura) facturas.get(i);
         for (int j = 0; j < transacciones.size(); j++) {
            Transaccion t = (Transaccion) transacciones.get(j);
            if (t.compareTo(f) == 0) {
               f.setSucursal(t.getSucursal());
               conSucursal.add(f);
               facturas.remove(i);
               transacciones.remove(j);
               i--;
               break;
            }
         }
      }
      System.out.println("con sucursal: " + conSucursal.size());
      System.out.println(facturas.size());
      System.out.println(transacciones.size());
      //segundo sumas (varias facturas para una transaccion) todas las de la misma fecha y local
      for (int i = 0; i < transacciones.size(); i++) {
         Transaccion t = (Transaccion) transacciones.get(i);
         List lFacturas = new ArrayList();
         //filtro facturas del local y fecha de la transaccion
         for (int j = 0; j < facturas.size(); j++) {
            Factura f = (Factura) facturas.get(j);
            if (f.getFecha().getTimeInMillis() == t.getFecha().getTimeInMillis() && f.getLocal().equals(t.getLocal())
                  && t.getMonto() >= f.getMonto()) {
               lFacturas.add(facturas.get(j));
            }
         }
         System.out.println("monto t: " + t.getMonto());
         if(t.getMonto() == 214584){
            System.out.println("monto t: " + t.getMonto() + "*********************************************************");
            System.out.println("monto t: " + t.getMonto() + "*********************************************************");
            System.out.println("monto t: " + t.getMonto() + "*********************************************************");
            System.out.println("monto t: " + t.getMonto() + "*********************************************************");
            System.out.println("monto t: " + t.getMonto() + "*********************************************************");
            
            for (int j = 0; j < lFacturas.size(); j++) {
               Factura fija = (Factura) lFacturas.get(j);
               System.out.println("*******************: " + fija.getMonto());
               
            }
            
         }

         Stack pila = new Stack();

         int size = lFacturas.size();
         int suma = 0;
         int hasta = 2;
         int j = 0;
         while (suma != t.getMonto()) {
            Factura fija = (Factura) lFacturas.get(j);
            pila.push(fija);
            suma = fija.getMonto();
            System.out.println("fija: " + fija.getMonto() + " hasta: " + hasta + "   suma: " + suma);
            int k = j + 1;
            int c = 0;
            while (c < size - 1) {
               c++;
               Factura f = (Factura) lFacturas.get(k % size);
               pila.push(f);
               System.out.println("suma: " + suma);
               suma += f.getMonto();
               System.out.println(" m: " + f.getMonto() + "  suma: " + suma);
               if (suma == t.getMonto())
                  break;
               
               /*if (suma > t.getMonto()) {
                  f = (Factura) pila.pop();
                  suma -= f.getMonto();
                  System.out.println("-m: " + f.getMonto() + "  suma: " + suma);
               }*/
               
               
               
               System.out.println("pila.size:" + pila.size());
               if (pila.size() >= hasta || suma > t.getMonto()) {
                  System.out.println("entra if");
                  pila.clear();
                  pila.push(fija);
                  suma = fija.getMonto();
               }
               k++;
            }//while
            j++;
            if(j >= size){
               j = 0;
               hasta++;
               if(hasta > 5)
                  return;
            }
         }

         //elimina las factura que ya se les encontró sucursal
         if (t.getMonto() == suma) {
            System.out.print(" suma: " + suma + " " + pila.isEmpty());
            while (!pila.isEmpty()) {
               Factura f = (Factura) pila.pop();
               f.setSucursal(t.getSucursal());
               conSucursal.add(f);
               facturas.remove(f);
            }
            //transacciones.remove(i);
         } else
            i--; //repite si ni encontró factura de la transaccion o se mantiene en la posicion.
         System.out.println(" con sucursal: " + conSucursal.size() + " i:" + i + " t: " + t.getMonto()
               + " t:" + t.getLocal());
         //System.out.println(facturas.size());
         //System.out.println(transacciones.size());
      }

      System.out.println("con sucursal: " + conSucursal.size());
      System.out.println(facturas.size());
      System.out.println(transacciones.size());

   }

   public static String[][] cargarCSV(String archivo) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader("D:/tmp/" + archivo));
      String linea = null;

      List lista = new ArrayList();

      while ((linea = br.readLine()) != null) {
         String col[] = linea.split(",");
         List columna = new ArrayList();
         for (int j = 0; j < col.length; j++) {
            col[j] = col[j].trim();
            if (col[j].charAt(0) == '"' && col[j].charAt(col[j].length() - 1) == '"') {
               columna.add(col[j].substring(1, col[j].length() - 1));
            } else if (col[j].charAt(0) == '"' && col[j].charAt(col[j].length() - 1) != '"') {
               String tmp = col[j].substring(1, col[j].length());
               while (col[++j].charAt(col[j].length() - 1) != '"') {
                  tmp += "," + col[j].substring(0, col[j].length());
               }
               tmp += "," + col[j].substring(0, col[j].length() - 1);
               columna.add(tmp);
            } else {
               columna.add(col[j]);
            }
         }
         lista.add(columna);
      }
      String[][] datos = new String[lista.size()][];
      for (int i = 0; i < lista.size(); i++) {
         datos[i] = (String[]) ((List) lista.get(i)).toArray(new String[((ArrayList) lista.get(i)).size()]);
      }
      br.close();
      return datos;
   }

   public static List getTrx() throws Exception {
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String driver = Parametros.getString("DRIVER");
      String url = Parametros.getString("URL");
      Connection con = Db.conexion(user, password, driver, url);
      System.out.println("CON : " + con);
      List lista = new ArrayList();

      String sql = "select l.cod_local, c.cli_email, c.cli_nombre, pos_monto_fp, t.updated_at, t.id_pedido "
            + "from fodba.fo_clientes c inner join bodba.bo_trx_mp t on t.id_cliente = c.cli_id "
            + "inner join bo_locales l on l.id_local = t.id_local "
            + "where cli_rut = 76407810 and t.updated_at >= '2009-05-11'  and t.updated_at <= '2009-09-16' "
            + "order by updated_at";
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
         Transaccion t = new Transaccion();
         t.setLocal(rs.getString("cod_local"));
         t.setMonto(rs.getInt("pos_monto_fp"));
         GregorianCalendar fecha = new GregorianCalendar();
         fecha.setTimeInMillis(rs.getDate("updated_at").getTime());
         t.setFecha(fecha);
         t.setSucursal(rs.getString("cli_nombre"));
         lista.add(t);
         //System.out.println(t.getFecha().getTime());
      }
      return lista;
   }
}
