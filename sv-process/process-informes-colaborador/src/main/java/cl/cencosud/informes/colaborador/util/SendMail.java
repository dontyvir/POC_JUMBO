package cl.cencosud.informes.colaborador.util;

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

/**
 * Clase que se encarga de realizar el envio de correo electronico.
 * 
 */
public class SendMail {

	/**
	 * Host del servidor smtp
	 */
	private String host;
	/**
	 * de quien
	 */
	private String from;
	/**
	 * cuerpo del correo.
	 */
	private String bodyHtml;
	/**
	 * lista de archivos a enviar.
	 */
	private List archivos;
	
	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(SendMail.class.getName());

	/**
	 * Metodo constructor de la clase.
	 * 
	 * @param host servidor de smtp
	 * @param from	de quien
	 * @param bodyHtml cuerpo a enviar
	 * @param archivos archivos adjuntos.
	 */
	public SendMail(String host, String from, String bodyHtml, List archivos) {
		this.host = host;
		this.from = from;
		this.archivos = archivos;
		this.bodyHtml = bodyHtml;
	}

	/**
	 * Método que se encarga de realizar el envio de correo.
	 * 
	 * @param destinatario	destinatarios de correo.
	 * @param copia			cc para correos.
	 * @param subject		titulo del correo.
	 * @throws Exception	Exception en caso de error.
	 */
	public void enviar(String destinatario, String copia, String subject)
			throws Exception {
		
		logger.debug("[SendMail][enviar] Ingreso al metodo");
		
		String mailer = "sendhtml";
		Properties props = new Properties();
		props.put("mail.smtp.host", this.host);
		
		Session session = Session.getInstance(props, null);

		Message msg = new MimeMessage(session);
		
		try {
			
			msg.setFrom(new InternetAddress(this.from));
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(destinatario, false));

			if (logger.isDebugEnabled()) {
				
				logger.debug("[SendMail][enviar] Mdestinatario SMTP :" + this.from);
				logger.debug("[SendMail][enviar] Message.RecipientType.TO agregando :" + destinatario);
			
			}

			if (copia != null && !copia.equals("")) {
			
				logger.debug("[SendMail][enviar] Message.RecipientType.CC agregando :" + copia);
				
				if (copia.indexOf(',') > 0) {
				
					msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(copia));
				
				} else {
				
					msg.setRecipient(Message.RecipientType.CC, new InternetAddress(copia));
				
				}
			
			}

			msg.setSubject(subject);

			MimeMultipart mm  = new MimeMultipart();
			MimeBodyPart  mbp = new MimeBodyPart();

			if (logger.isDebugEnabled()) {
				
				logger.debug("[SendMail][enviar] subject  :" + subject);
				logger.debug("[SendMail][enviar] DataHandler  :" + this.bodyHtml);
			
			}

			DataHandler dh = new DataHandler(this.bodyHtml, "text/html");
			mbp.setDataHandler(dh);
			mm.addBodyPart(mbp);

			if (this.archivos != null && this.archivos.size() > 0) {
			
				for (int i = 0; i < this.archivos.size(); i++) {

					File archivo = (File) this.archivos.get(i);
				
					if (logger.isDebugEnabled()){
					
						logger.debug("[SendMail][enviar] Message FileDataSource :" + archivo.getName());
					
					}
					
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
			
			logger.info("[SendMail][enviar] Enviando correo");
			Transport.send(msg);
			logger.info("[SendMail][enviar] SendMail - > Se envio Correo ...");
			
		} catch (Exception e) {
			
			logger.error("[SendMail][enviar] Error al realizar el envio de correo",e);
			throw e;
		
		}
	}
}
