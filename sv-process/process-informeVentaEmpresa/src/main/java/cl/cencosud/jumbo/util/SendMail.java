package cl.cencosud.jumbo.util;

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

import org.apache.log4j.Logger;

public class SendMail {
	
	   private String host;
	   private String from;

	   protected static Logger logger = Logger.getLogger(SendMail.class.getName());

	   public SendMail(String host, String from) {
	      this.host = host;
	      this.from = from;
	   }

	   public void enviar(String destinatario, String copia, String subject, String bodyHtml, List archivos) throws Exception {
	      String mailer = "sendhtml";
	      Properties props = new Properties();
	     
	      props.put("mail.smtp.host", this.host);
	      Session session = Session.getInstance(props, null);
	      
	      
	      Message msg = new MimeMessage(session);
	      try {
	    	  
	         msg.setFrom(new InternetAddress(this.from));
	         msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, false));
	         
	         if(logger.isDebugEnabled()){
	        	 logger.debug(" Mdestinatario SMTP :"+this.from);
    			 logger.debug(" Message.RecipientType.TO agregando :"+destinatario);
	         }
	         
	         
	         if (copia != null && !copia.equals("")){
	        	  logger.debug(" Message.RecipientType.CC agregando :"+copia);
	        	  if (copia.indexOf(',') > 0) {  
	        		  msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(copia));   
	        	  }
	              else {
	            	  msg.setRecipient(Message.RecipientType.CC, new InternetAddress(copia));
	              }
	         }
	         
	         msg.setSubject(subject);

	         MimeMultipart mm = new MimeMultipart();
	         MimeBodyPart mbp = new MimeBodyPart();
	         
	         if(logger.isDebugEnabled()){
	        	 logger.debug(" subject  :"+subject);
    			 logger.debug(" DataHandler  :"+bodyHtml);
	         }

	         DataHandler dh = new DataHandler(bodyHtml, "text/html");
	         mbp.setDataHandler(dh);
	         mm.addBodyPart(mbp);

	         if (archivos != null && archivos.size() > 0) {
	            for (int i = 0; i < archivos.size(); i++) {
	            		            	 
	               File archivo = (File) archivos.get(i);
	               if(logger.isDebugEnabled())
	        			 logger.debug(" Message FileDataSource :"+archivo.getName());
	               
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
	         logger.info(" SendMail - > Se envio Correo ...");
	      } catch (Exception e) {
	    	  logger.error(e);	        
	         throw e;
	      }
	   }
}
