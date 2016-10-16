//package cl.cencosud.procesos;
//
////import java.io.File;
//import java.sql.Connection;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//
//import net.sf.fastm.ITemplate;
//import net.sf.fastm.IValueSet;
//import net.sf.fastm.TemplateLoader;
//import net.sf.fastm.ValueSet;
//
//import org.apache.log4j.Logger;
//
//import cl.cencosud.beans.Mail;
//import cl.cencosud.beans.Precio;
//import cl.cencosud.constantes.Constantes;
//import cl.cencosud.util.LogUtil;
//import cl.cencosud.util.Parametros;
////import java.util.Date;
////import java.util.Properties;
////import javax.activation.DataHandler;
////import javax.activation.FileDataSource;
////import javax.mail.Message;
////import javax.mail.Session;
////import javax.mail.Transport;
////import javax.mail.internet.InternetAddress;
////import javax.mail.internet.MimeBodyPart;
////import javax.mail.internet.MimeMessage;
////import javax.mail.internet.MimeMultipart;
//
//public class SendMail {
//	
//	static {
//		LogUtil.initLog4J();
//	}
//	
//	private static Logger logger = Logger.getLogger(SendMail.class);
//	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
//	/*
//	private String host;
//	private String from;
//	private String bodyHtml;
//	private List archivos;	
//	* */
//	
//	public SendMail() {
//	}
//
//	 /* 
//	 * Se comentan metodos,SendMail y enviar, NO pertenecen al modulo /process-cargaproductos,
//	 * estas estan vinculadas al modulo /process-stock-online
//	 * */
//	/*
//	public SendMail(String host, String from, String bodyHtml, List archivos) {
//		this.host = host;
//		this.from = from;
//		this.archivos = archivos;
//		this.bodyHtml = bodyHtml;
//	}
//
//	public void enviar(String destinatario, String copia, String subject)
//			throws Exception {
//		String mailer = "sendhtml";
//		Properties props = new Properties();
//		props.put("mail.smtp.host", this.host);
//		Session session = Session.getInstance(props, null);
//		Message msg = new MimeMessage(session);
//		try {
//			msg.setFrom(new InternetAddress(this.from));
//			msg.setRecipients(Message.RecipientType.TO,
//					InternetAddress.parse(destinatario, false));
//			if (copia != null && !copia.trim().equals(""))
//				msg.setRecipients(Message.RecipientType.CC,
//						InternetAddress.parse(copia, false));
//			msg.setSubject(subject);
//
//			MimeMultipart mm = new MimeMultipart();
//			MimeBodyPart mbp = new MimeBodyPart();
//
//			DataHandler dh = new DataHandler(this.bodyHtml, "text/html");
//			mbp.setDataHandler(dh);
//			mm.addBodyPart(mbp);
//
//			if (this.archivos.size() > 0) {
//				for (int i = 0; i < this.archivos.size(); i++) {
//					File archivo = (File) this.archivos.get(i);
//					FileDataSource fds = new FileDataSource(archivo);
//					mbp = new MimeBodyPart();
//					mbp.setDataHandler(new DataHandler(fds));
//					mbp.setFileName(archivo.getName());
//					mm.addBodyPart(mbp);
//				}
//			}
//
//			msg.setContent(mm);
//			msg.setHeader("X-Mailer", mailer);
//			msg.setSentDate(new Date());
//			Transport.send(msg);
//			System.out.println("Se envio Correo ... ");
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
//	*/
//	
//	private Connection getConnection() throws Exception{
//		return DbCarga.conexion(Parametros.getString("USER"), Parametros.getString("PASSWORD"), Parametros.getString("DRIVER"), Parametros.getString("URL"));		
//	}
//	
//	private Mail getMail(String estado, String subject, String idFrm, String remite, String data){
//		Mail mail = new Mail();
//		mail.setFsm_destina(Parametros.getString("precios.alert.destinatario"));
//		mail.setFsm_estado(estado);
//		mail.setFsm_subject(subject);
//		mail.setFsm_idfrm(idFrm);
//		mail.setFsm_remite(remite);
//		mail.setFsm_data(data);
//		return mail;
//	}
//	
//	private Mail getMailPreciosNoActualizados(String estado, String subject, String idFrm, String remite, String data){
//		Mail mail = new Mail();
//		mail.setFsm_destina(Parametros.getString("precios.alert.destinatario.no.actualizados"));
//		mail.setFsm_estado(estado);
//		mail.setFsm_subject(subject);
//		mail.setFsm_idfrm(idFrm);
//		mail.setFsm_remite(remite);
//		mail.setFsm_data(data);
//		return mail;
//	}
//	
//	/**
//	 *	Se envia mail de alerta con los archivos que estan corruptos 
//	 * **/
//	public void sendMailAlertFileGZ(List listFilesError) {
//		try {
//			String mail_tpl = Constantes.alert_mail_file_gz_html;
//			TemplateLoader mail_load = new TemplateLoader(mail_tpl);
//			ITemplate mail_tem = mail_load.getTemplate();
//
//			String mail_result = mail_tem.toString(contenidoMailErrorFileGZ(listFilesError));
//			Mail mail = getMail("0", "Alerta de Archivos - Interface JCP.", "Carga", "cargas productos", mail_result);			
//			Connection con = getConnection();
//
//			Cargar carga = new Cargar();
//			carga.addMail(mail, con);
//			logger.info("*** Se envia mail de Alerta, con archivos corruptos ***");
//		} catch (Exception e) {
//			logger.error("Exception:" + e.getMessage(), e);
//		}
//	}
//	private IValueSet contenidoMailErrorFileGZ(List listFilesError)throws Exception {
//		IValueSet mail_top = new ValueSet();
//		List listaArchivos = new ArrayList();
//
//		for (Iterator iter = listFilesError.iterator(); iter.hasNext();) {
//			IValueSet fila = new ValueSet();
//			String nombreArchivo = (String) iter.next();
//			fila.setVariable("{nombreArchivo}", nombreArchivo);
//			listaArchivos.add(fila);
//		}
//		if (listaArchivos.size() > 0) {
//			mail_top.setDynamicValueSets("LISTA_ARCHIVOS", listaArchivos);
//		}
//		return mail_top;
//	}
//	
//	
//	/**
//	 *	Se envia mail de alerta con las unidades no validas 
//	 *
//	 * **/
//	public void sendMailAlertUnitsNovalidas(List listaPreciosNoValidas) {
//		try {
//			String mail_tpl = Constantes.alert_mail_jcp_html;
//			TemplateLoader mail_load = new TemplateLoader(mail_tpl);
//			ITemplate mail_tem = mail_load.getTemplate();
//
//			HashMap mp = contenidoMailInterfaceJCP(listaPreciosNoValidas);
//			
//			IValueSet ivalue  =(IValueSet)mp.get("mail");
//
//			String mail_result = "";
//			if(ivalue != null){
//				mail_result = mail_tem.toString(ivalue);
//				//String mail_result = mail_tem.toString(contenidoMailInterfaceJCP(listaPreciosNoValidas));
//				Mail mail = getMail("0", "Alerta de Archivos - Interface JCP.", "Carga", "cargas productos", mail_result);
//				Connection con = getConnection();
//
//				Cargar carga = new Cargar();
//				carga.addMail(mail, con);
//				logger.info("*** Se envia mail de Alerta, con las unidades no validas **");
//			}
//		} catch (Exception e) {
//			logger.error("Exception:" + e.getMessage(), e);
//		}
//	}
//	private static HashMap contenidoMailInterfaceJCP(List listaPreciosNoValidas) throws Exception {	
//		IValueSet mail_top = new ValueSet();
//		List listaProductoDetalle = new ArrayList();
//		HashMap hm = new HashMap();
//		Iterator iter = listaPreciosNoValidas.iterator();
//		while(iter.hasNext()){
//			List list1 = (List)iter.next();
//						
//			Iterator iter2 = list1.iterator();
//			while(iter2.hasNext()){							
//				IValueSet fila = new ValueSet();
//				Precio producto = (Precio)iter2.next();
//				
//				if(producto.getCodigoLocal()!=null && String.valueOf(producto.getCodigoProducto())!=null &&
//						String.valueOf(producto.getPrecio())!=null && String.valueOf(producto.getCodigoBarra())!=null &&
//								producto.getUnidadMedida()!=null ){
//									
//					fila.setVariable("{codigoLocal}", producto.getCodigoLocal());
//					fila.setVariable("{codigoProducto}",String.valueOf(producto.getCodigoProducto()));
//					fila.setVariable("{precio}", String.valueOf(producto.getPrecio()));
//					fila.setVariable("{codigoBarra}", String.valueOf(producto.getCodigoBarra()));
//					fila.setVariable("{unidadMedida}",producto.getUnidadMedida());
//					fila.setVariable("{fechaPrecioNuevo}", sdf.format(producto.getFechaPrecioNuevo()));
//					fila.setVariable("{nombreArchivo}", producto.getNombreArchivo());
//					fila.setVariable("{precioActual}",  String.valueOf(producto.getPrecioActual()));
//					
//					listaProductoDetalle.add(fila);
//				}
//			}
//		}
//		if (listaProductoDetalle.size() > 0) {
//			mail_top.setDynamicValueSets("LISTA_PRODUCTO_DETALLE", listaProductoDetalle);
//			hm.put("mail", mail_top);
//		}else{
//			hm.put("mail", null);
//		}		
//		return hm;
//	}	
//	
//	/**
//	 * 
//	 * Se envia mail de alerta con precios no actualizados
//	 */
//	public void sendMailAlertPreciosNoActualizados(List listaPreciosNoActualizados) {
//		try {
//			String mail_tpl = Constantes.alert_mail_file_precios_no_actualizados;
//			TemplateLoader mail_load = new TemplateLoader(mail_tpl);
//			ITemplate mail_tem = mail_load.getTemplate();
//
//			HashMap mp = contenidoMailInterfaceJCP(listaPreciosNoActualizados);
//			
//			IValueSet ivalue  =(IValueSet)mp.get("mail");
//
//			String mail_result = "";
//			if(ivalue != null){
//				mail_result = mail_tem.toString(ivalue);
//				//String mail_result = mail_tem.toString(contenidoMailInterfaceJCP(listaPreciosNoValidas));
//				Mail mail = getMailPreciosNoActualizados("0", "Alerta de Archivos - Interface JCP.", "Carga", "cargas productos", mail_result);
//				Connection con = getConnection();
//
//				Cargar carga = new Cargar();
//				carga.addMail(mail, con);
//				logger.info("*** Se envia mail de Alerta, con precios no actualizados **");
//			}
//		} catch (Exception e) {
//			logger.error("Exception:" + e.getMessage(), e);
//		}
//	}
//}
