package cl.cencosud.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;


import cl.cencosud.beans.MailDTO;
import cl.cencosud.constantes.Constantes;
import cl.cencosud.util.Parametros;

public class Mail {

	private static Logger logger = Logger.getLogger(Mail.class);		
	private static SimpleDateFormat sdfMail = new SimpleDateFormat("HH:mm:ss a 'del' dd/MM/yyyy");
	
	public static void sendMail(File fileAttach) throws Exception, MessagingException {
		logger.debug("Inicio envio mails");		
		Mail.processing(fileAttach);
	}
   
	private static void processing(File fileAttach)throws Exception, MessagingException {
		List mails = getListaDestinatarios(fileAttach);     
		Iterator ite = mails.iterator();		
		MailDTO mail = null;
		while (ite.hasNext()) {
			mail = (MailDTO) ite.next();
			sendMail(mail);
		}
	}

	private static Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", Constantes.HOST);
		props.put("mail.smtp.timeout", Constantes.CONNECTION_TIMEOUT);		
		props.put("mail.smtp.connectiontimeout", Constantes.TIMEOUT);
		
		return props;
	}

	private static void sendMail(MailDTO mail) throws Exception, MessagingException {
		Session session = Session.getInstance(getProperties(), null);		
		Message msg = new MimeMessage(session);
		try {
			logger.debug("Mail To:" + mail.getDestinatario());
			msg.setFrom(new InternetAddress( Constantes.FROM ));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getDestinatario(), false));
			if (mail.getCopiaDestinatario() != null && !mail.getCopiaDestinatario().trim().equals("")){
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCopiaDestinatario(), false));
			}
			msg.setFrom(new InternetAddress( Constantes.FROM ));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getDestinatario(), false));
			msg.setSubject(mail.getSubject());
			msg.setSentDate(new Date());

			// creates message part
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setContent(mail.getBodyHtml(), "text/html");

	        // creates multi-part
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(messageBodyPart);
	        
	        List attachedFiles = mail.getFileAttach();
	        // adds attachments
	        if (attachedFiles != null && attachedFiles.size() > 0) {
	        	for (int i = 0; i < attachedFiles.size(); i++) {
	        		File archivo = (File) attachedFiles.get(i);

	        		MimeBodyPart attachPart = new MimeBodyPart();
	                try {
	                	attachPart.attachFile(archivo);
	                } catch (IOException ex) {
	                	ex.printStackTrace();
	                }
	                multipart.addBodyPart(attachPart);	                
	            }
	        }

	        // sets the multi-part as e-mail's content
	        msg.setContent(multipart);

			// sends the e-mail
			Transport.send(msg);

			logger.debug("Mail enviado.");
		}catch (MessagingException ms) {
			//logger.error("Mail no ha podido ser enviado");
			throw new MessagingException("Mail no ha podido ser enviado",ms);
			
		} catch (Exception ex) {
			logger.error("Error envio mail, " + ex);
		}		
	}
	
	private static List getListaDestinatarios(File fileAttach )throws Exception {
		List lista = new ArrayList();
		String[] destinatariosLista = Parametros.getString("DESTINATARIOS_PRECIOS_PARCIALES").split(",");

		for (int i = 0; i < destinatariosLista.length; i++) {
			String html = getStringHtml();			
			String emailDestinatario = destinatariosLista[i];

			MailDTO mail = new MailDTO();
			//mail.setIdMail(1);
			mail.setRemitente( Constantes.FROM );
			mail.setDestinatario( emailDestinatario );
			mail.setCopiaDestinatario("");
			mail.setSubject( Constantes.SUBJECT );
			//mail.setData(null);

			html = html.replaceAll("\\{fecha\\}", getFechaHora());
			//html = html.replaceAll("\\{nombre\\}", key.replaceAll("_", " "));
			mail.setBodyHtml(html);

			List listAttachFile = new ArrayList();
			//Iterator ite2 = listaFileNamePNI.iterator();
			//while(ite2.hasNext()){
			//String name = (String)ite2.next();
				listAttachFile.add(fileAttach );
			//}					
			mail.setFileAttach(listAttachFile);
			lista.add(mail);
		}
		return lista;
	}
			
	 public static String getStringHtml() {
		 StringBuffer contentBuffer = new StringBuffer();
		 BufferedReader in = null;	
		 String file = Constantes.INFO_HTML;		 
		 try {
			 in = new BufferedReader(new FileReader( file ));
			 String str;
			 while ((str = in.readLine()) != null) {
				 contentBuffer.append(str);           
			 }
			 in.close();
		 }catch (IOException ioe) {
			 logger.error("ERROR, IOException, " + ioe);
		 }catch (Exception ex) {
			 logger.error("ERROR, archivo de mail," + ex);
		 }finally{
			 if (in != null) {
				 try {
					 in.close();
				 }catch (IOException iox) {
					 logger.error("ERROR, " + iox);
				 }
			 }
		 }
		 return contentBuffer.toString();
	 }

	private static String getFechaHora(){
		return sdfMail.format(new Date());	
	}
}
