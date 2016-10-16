package cl.jumbo.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

public class EnvioMailsProcess {

   private String host;
   private String from;

   /**
    * @throws Exception
    * @throws Exception
    *
    */

   public static void main(String[] args) throws Exception {
      // hora inicial para el calculo del tiempo total
      java.util.Date horaInicial = new java.util.Date();
      System.out.println("__ INICIO envio mails _______________________________");

      String driver = Parametros.getString("DRIVER");
      String user = Parametros.getString("USER");
      String password = Parametros.getString("PASSWORD");
      String url = Parametros.getString("URL");
      Connection con = Db.conexion(user, password, driver, url);
      System.out.println("CON ("+url+"): " + con);
      EnvioMailsProcess envio = new EnvioMailsProcess();
      envio.processing(con);

      // calculo tiempo
      java.util.Date horaFinal = new java.util.Date();
      long dif = horaFinal.getTime() - horaInicial.getTime();
      long segundos = dif / 1000;
      long minutos = segundos / 60;
      segundos = segundos - minutos * 60;
      System.out.println("__ FIN - tiempo total: " + minutos + "m" + segundos + "s");
   }

   public EnvioMailsProcess() {
      this.host = Parametros.getString("mail.smtp.host");
      this.from = Parametros.getString("mail.from");
   }

   private List getMailsSinEnviar(Connection con) throws SQLException {
      List lista = new ArrayList();
      String sql = "select * from fodba.FO_SEND_MAIL where FSM_ESTADO = '0' fetch first 100 rows only";
      PreparedStatement ps = con.prepareStatement(sql);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
         Mail mail = new Mail();
         mail.setIdMail(rs.getInt("FSM_ID"));
         mail.setRemitente(rs.getString("FSM_REMITE"));
         mail.setDestinatario(rs.getString("FSM_DESTINA"));
         mail.setCopiaDestinatario(rs.getString("FSM_COPIA"));
         mail.setSubject(rs.getString("FSM_SUBJECT"));
         mail.setData(rs.getClob("FSM_DATA"));
         lista.add(mail);
      }
      return lista;
   }

   private void cambiarEstadoMail(Connection con, int id, String estado) throws SQLException {
      String sql = "update fodba.FO_SEND_MAIL set FSM_ESTADO = ?, FSM_STMPSEND = current_timestamp where FSM_ID= ?";
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setString(1, estado);
      ps.setInt(2, id);
      ps.executeUpdate();
   }

   public void processing(Connection con) throws MessagingException, IOException, SQLException, SQLException {
      long ini = System.currentTimeMillis();
      //System.out.println("Comienza la ejecución del proceso de envio de emails del dia/hora " + new Date());
      List mails = getMailsSinEnviar(con);
      System.out.println("Se recuperan los mails a enviar ("+( mails==null ? 0 : mails.size() )+")");
      Iterator ite = mails.iterator();
      String copia = null;
      Mail mail = null;
      String estado = null;
      while (ite.hasNext()) {
         long dif = System.currentTimeMillis() - ini;
         if (dif >= 110000){
            System.out.println("Finaliza antes de que se cumplan los 2 minutos");
            break;
         }
         mail = (Mail) ite.next();
         try {
            System.out.println("    - Mail a: " + mail.getDestinatario() + " (" + mail.getSubject() + ")");
            copia = "";
            if (mail.getCopiaDestinatario() != null && !mail.getCopiaDestinatario().trim().equals(""))
               copia = mail.getCopiaDestinatario();

            estado = this.enviarEMail(mail.getRemitente(), mail.getDestinatario(), copia, mail.getSubject(), mail.getData());
            cambiarEstadoMail(con, mail.getIdMail(), estado);
         } catch (Exception e) {
            e.printStackTrace();
            cambiarEstadoMail(con, mail.getIdMail(), "2");
         }
      }
      //System.out.println("FIN: " + new Date());
   }

   public String clobToString(Clob cl) throws SQLException, IOException {
      if (cl == null)
         return "";
      StringBuffer strOut = new StringBuffer();
      String aux;
      BufferedReader br = new BufferedReader(cl.getCharacterStream());
      while ((aux = br.readLine()) != null)
         strOut.append(aux);
      return strOut.toString();
   }

   private String enviarEMail(String remitente, String destinatario, String copia, String subject, Clob data) {
      String mailer = "sendhtml";
      Properties props = new Properties();
      props.put("mail.smtp.host", this.host);
      Session session = Session.getInstance(props, null);
      Message msg = new MimeMessage(session);
      try {
    	 if(isEmail(remitente))
    		 msg.setFrom(new InternetAddress(remitente));
    	 else
             msg.setFrom(new InternetAddress(this.from));

         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, false));
         if (copia != null && !copia.trim().equals(""))
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copia, false));
         msg.setSubject(subject);

         String datos = clobToString((Clob) data);
         msg.setDataHandler(new DataHandler(new ByteArrayDataSource(datos, "text/html")));
         msg.setHeader("X-Mailer", mailer);
         msg.setSentDate(new Date());
         // send the thing off
         Transport.send(msg);
      } catch (Exception e) {
         e.printStackTrace();
         return "2"; //Error
      }
      return "1"; //Sin errores
   }

   public boolean isEmail(String correo) {
       Pattern pat = null;
       Matcher mat = null;
       pat = Pattern.compile("^[\\w\\-\\_\\+]+(\\.[\\w\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
       mat = pat.matcher(correo);
       if (mat.find()) {
           return true;
       }else{
           return false;
       }
   }
}
