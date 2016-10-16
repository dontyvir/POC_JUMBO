/*
 * Creado el 02-06-2006
 */
package cl.jumbo.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;

/**
 * @author Cristian Arriagada
 * <p>Clase para el envío de mails, en formato texto y html</p>
 * 
 */
public class EnvioMail {
	private String to;
	private String cc;
	private String from;
	private String subject;
	private String text;
	
	/**
	 * @return Devuelve cc.
	 */
	public String getCc() {
		return cc;
	}
	/**
	 * @return Devuelve from.
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @return Devuelve subject.
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @return Devuelve text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * @return Devuelve to.
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param cc El cc a establecer.
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}
	/**
	 * @param from El from a establecer.
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	/**
	 * @param subject El subject a establecer.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @param text El text a establecer.
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @param to El to a establecer.
	 */
	public void setTo(String to) {
		this.to = to;
	}
	
	public void sendText() throws AddressException, MessagingException{
		javax.naming.InitialContext ctx;
		try {
			ctx = new javax.naming.InitialContext();
			javax.mail.Session mail_session = (javax.mail.Session) ctx.lookup("smtp/jumbo");
			Properties props = mail_session.getProperties();
			props.put("mail.smtp.port", "25"); 
	    	MimeMessage msg = new MimeMessage(mail_session);
	    	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getTo()));
	    	if (this.getCc() != null)
	    		msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(this.getCc()));
	    	msg.setFrom(new InternetAddress(this.getFrom()));
	    	msg.setSubject(this.getSubject());
	    	msg.setText(this.getText());
	    	msg.setSentDate(new Date());
	    	Transport.send(msg);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
	
	public void sendHtml() throws AddressException, MessagingException{
		javax.naming.InitialContext ctx;
		try {
			ctx = new javax.naming.InitialContext();
			javax.mail.Session mail_session = (javax.mail.Session) ctx.lookup("smtp/jumbo");
			Properties props = mail_session.getProperties();
			props.put("mail.smtp.port", "25"); 
	    	MimeMessage msg = new MimeMessage(mail_session);
	    	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.getTo()));
	    	if (this.getCc() != null)
	    		msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(this.getCc()));
	    	msg.setFrom(new InternetAddress(this.getFrom()));
	    	msg.setSubject(this.getSubject());
	    	msg.setContent(this.getText(), "text/html");
	    	msg.setSentDate(new Date());
	    	Transport.send(msg);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

}
