package cl.cencosud.procesos;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {
	   private String host;
	   private String from;
	   private String bodyHtml;
	   private List archivos;

	   public SendMail(String host, String from, String bodyHtml, List archivos) {
	      this.host = host;
	      this.from = from;
	      this.archivos = archivos;
	      this.bodyHtml = bodyHtml;

	   }

	   public void enviar(String destinatario, String copia, String subject) throws Exception {
	      String mailer = "sendhtml";
	      Properties props = new Properties();
	      props.put("mail.smtp.host", this.host);
	      Session session = Session.getInstance(props, null);
	      Message msg = new MimeMessage(session);
	      try {
	         msg.setFrom(new InternetAddress(this.from));
	         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, false));
	         if (copia != null && !copia.trim().equals(""))
	            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copia, false));
	         msg.setSubject(subject);

	         MimeMultipart mm = new MimeMultipart();
	         MimeBodyPart mbp = new MimeBodyPart();

	         DataHandler dh = new DataHandler(this.bodyHtml, "text/html");
	         mbp.setDataHandler(dh);
	         mm.addBodyPart(mbp);

	         if (this.archivos.size() > 0) {
	            for (int i = 0; i < this.archivos.size(); i++) {
	               File archivo = (File) this.archivos.get(i);
	               FileDataSource fds = new FileDataSource(archivo);
	               mbp = new MimeBodyPart();
	               mbp.setDataHandler(new DataHandler(fds));
	               mbp.setFileName(archivo.getName());
	               mm.addBodyPart(mbp);
	            }
	         }

	         msg.setContent(mm);
	         msg.setHeader("X-Mailer", mailer);
	         msg.setSentDate(new Date());
	         Transport.send(msg);
	         System.out.println("Se envio Correo ... ");
	      } catch (Exception e) {
	         e.printStackTrace();
	         throw e;
	      }
	   }
}
