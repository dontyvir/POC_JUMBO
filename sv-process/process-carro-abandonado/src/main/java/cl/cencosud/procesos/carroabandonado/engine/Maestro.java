package cl.cencosud.procesos.carroabandonado.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import cl.cencosud.procesos.carroabandonado.util.JFactory;
import cl.cencosud.procesos.carroabandonado.dao.CarroAbandonadoDAO;
import cl.cencosud.procesos.carroabandonado.dto.CarroAbandonadoDTO;
import cl.cencosud.procesos.carroabandonado.util.ConnectionDB2;
import cl.cencosud.procesos.carroabandonado.util.Util;

public class Maestro {
	protected static Logger logger = Logger.getLogger(Maestro.class.getName());
	
	private static final String GOOGLE_RECUERDA = "recuerda";
	private static final String GOOGLE_DESPACHO = "despacho";
	private static final String GOOGLE_SECCION = "seccion_";
	private static final String GOOGLE_RUBRO = "rubro_";
	

	/**
	 * Método que se encarga de realizar el proceso de recuperación y envio del mail
	 * con cupon de descuento a clientes con carros abandonados.
	 */
	public void process() throws Exception{
		
		logger.debug("[Maestro][process] Ingreso al metodo");
		
		String startDate = Util.getFechaHoraActual();
		logger.info("[Maestro][process] Comienza la ejecución del proceso " + startDate);
		
		CarroAbandonadoDAO dao = new CarroAbandonadoDAO();
		List lstCarrAband = new ArrayList();
		try {
			logger.info("[Maestro][process] Realizo llamado a obtener carros abandonados");
			List carros = dao.getCarrosAbandonados();
			
			Iterator it =  carros.iterator();
			while (it.hasNext()) {
				Map carro = (Map) it.next();
				int cli_id = ((Integer) carro.get("CLI_ID")).intValue();
				CarroAbandonadoDTO carrAband = new CarroAbandonadoDTO();
				carrAband.setCli_id(cli_id);
				carrAband.setCli_rut(((Integer) carro.get("CLI_RUT")).intValue());
				carrAband.setCli_dv((String)carro.get("CLI_DV"));
				carrAband.setCli_nombre((String)carro.get("CLI_NOMBRE"));
				carrAband.setCli_apellido_pat((String)carro.get("CLI_APELLIDO_PAT"));
				carrAband.setCli_apellido_mat((String)carro.get("CLI_APELLIDO_MAT"));
				carrAband.setCli_email((String)carro.get("CLI_EMAIL"));

				// dao para recuperar total en dinero del carro
				carrAband = dao.getSumaCarroCompra(carrAband);

				// si el total es menor a indicado como minimo, no se agrega este carro al trabajo
				if (carrAband.getCar_suma() <= Integer.parseInt(Util.getPropertiesString("carro.monto.minimo")))
					continue;
				
				
				if (carrAband.getCar_suma() <= Integer.parseInt(Util.getPropertiesString("carro.monto.maximo")))
				{
					// dao para recuperar id categoria y nombre categoria con mas dineros
					//carrAband = dao.getRubroMasCaro(carrAband);
					List listaProductosRubro = dao.getRubroMasCaro(carrAband);
					
					Iterator itListaProductosRubro =  listaProductosRubro.iterator();
					while (itListaProductosRubro.hasNext()) 
					{
						CarroAbandonadoDTO carroAbandonadoDto = (CarroAbandonadoDTO) itListaProductosRubro.next();
						// dao para recuperar cupon por Rubro
						carroAbandonadoDto = dao.getCuponRubro(carroAbandonadoDto);
						if(carroAbandonadoDto.getCar_cupon_cantidad()> 0)
						{
							//carrAband = carroAbandonadoDto;
							carrAband.setCat_nombre(carroAbandonadoDto.getCat_nombre());
							carrAband.setCat_id(carroAbandonadoDto.getCat_id());
							carrAband.setRubro_id(carroAbandonadoDto.getRubro_id());
							carrAband.setCat_suma(carroAbandonadoDto.getCat_suma());
							
							carrAband.setCar_cupon(carroAbandonadoDto.getCar_cupon());
							carrAband.setCar_tipo_cupon(carroAbandonadoDto.getCar_tipo_cupon());
							carrAband.setCar_cupon_cantidad(carroAbandonadoDto.getCar_cupon_cantidad());
							carrAband.setCar_cupon_monto_mail(carroAbandonadoDto.getCar_cupon_monto_mail());
							carrAband.setCar_fecha_fin(carroAbandonadoDto.getCar_fecha_fin());
							break;
						}
					}
					// dao para recuperar cupon por seccion
					//carrAband = dao.getCuponRubro(carrAband);
					
					if (carrAband.getCar_cupon_cantidad()== 0)
					{
						List listaProductosSeccion = dao.getSeccionMasCara(carrAband);
						Iterator itListaProductosSeccion =  listaProductosSeccion.iterator();
						
						while (itListaProductosSeccion.hasNext()) 
						{
							CarroAbandonadoDTO carroAbandonadoDto = (CarroAbandonadoDTO) itListaProductosSeccion.next();
							// dao para recuperar cupon por seccion
							carroAbandonadoDto = dao.getCuponSeccion(carroAbandonadoDto);
							if(carroAbandonadoDto.getCar_cupon_cantidad()> 0)
							{
								//carrAband = carroAbandonadoDto;
								
								carrAband.setCat_nombre(carroAbandonadoDto.getCat_nombre());
								carrAband.setCat_id(carroAbandonadoDto.getCat_id());
								carrAband.setRubro_id(carroAbandonadoDto.getRubro_id());
								carrAband.setCat_suma(carroAbandonadoDto.getCat_suma());
								
								carrAband.setCar_cupon(carroAbandonadoDto.getCar_cupon());
								carrAband.setCar_tipo_cupon(carroAbandonadoDto.getCar_tipo_cupon());
								carrAband.setCar_cupon_cantidad(carroAbandonadoDto.getCar_cupon_cantidad());
								carrAband.setCar_cupon_monto_mail(carroAbandonadoDto.getCar_cupon_monto_mail());
								carrAband.setCar_fecha_fin(carroAbandonadoDto.getCar_fecha_fin());
								
								break;
							}
						}
						//carrAband = dao.getSeccionMasCara(carrAband);
						//carrAband = dao.getCuponSeccion(carrAband);
							
					}
										
					// si no hay cupones para esa seccion se busca por uno de despacho
					if (carrAband.getCar_cupon_cantidad() == 0){
						carrAband = dao.getCuponDespacho(carrAband);
						
						//si ya no quedan cupones de despacho, se salta este cliente
						if (carrAband.getCar_cupon_cantidad() == 0){
							continue;
						}
					}
				}
				else{
					carrAband.setCar_cupon_cantidad(0);
					carrAband.setCar_tipo_cupon("X");
				}
				logger.info("[Maestro][process] Realizo el llamado a generar el mail");
				carrAband = setTextMail(carrAband);
				lstCarrAband.add(carrAband);
			}

//			testImprimeCarros(lstCarrAband); //TODO borrar test

			logger.info("[Maestro][process] Realizo el envio de los correos electronicos");
			enviarEmails(lstCarrAband);
			
		} catch (SQLException e) {
			
			logger.error("[Maestro][process] Error al recuperar el reporte",e);
			throw new Exception("Error al recuperar el reporte, detalle " + e.getMessage());
			
		} catch (Exception e) {
			
			logger.error("[Maestro][process] Error al generar o enviar el excel",e);
			throw new Exception("Error al generar o enviar el excel, detalle " + e.getMessage());
			
		}finally{
			
			logger.debug("[Maestro][process] Cierro conexiones");
			ConnectionDB2.closeConnectionDB2();
			
		}
		logger.info("[Maestro][process] Finaliza la ejecución del proceso " + Util.getDateFromMsec(startDate, Util.getFechaHoraActual()));
		logger.debug("[Maestro][process] Finaliza metodo");
	}
	
//	public void testImprimeCarros(List lstCarrAband){
//		
//		List lstCarrAbandTemp = lstCarrAband;
//		
//		for (int i = 0; i < lstCarrAbandTemp.size(); i++){
//			CarroAbandonadoDTO carrAband = (CarroAbandonadoDTO)lstCarrAbandTemp.get(i);
//			
//			System.out.println();
//			System.out.println("getCli_nombre: "+carrAband.getCli_nombre());
//			System.out.println("getCli_apellido_pat: "+carrAband.getCli_apellido_pat());
//			System.out.println("getCli_rut: "+carrAband.getCli_rut());
//			System.out.println("getCli_email: "+carrAband.getCli_email());
//			System.out.println();
//			System.out.println("getCat_id: "+carrAband.getCat_id());
//			System.out.println("getCat_nombre: "+carrAband.getCat_nombre());
//			System.out.println("getCat_suma: "+carrAband.getCat_suma());
//			System.out.println();
//			System.out.println("getCar_suma: "+carrAband.getCar_suma());
//			System.out.println("getCar_cupon_cantidad: "+carrAband.getCar_cupon_cantidad());
//			System.out.println("getCar_cupon: "+carrAband.getCar_cupon());
//			System.out.println("getCar_tipo_cupon: "+carrAband.getCar_tipo_cupon());
//			System.out.println();
//			System.out.println("getCar_text_mail: "+carrAband.getCar_text_mail().toString());
//			System.out.println();
//		}
//
//	}
	
	public CarroAbandonadoDTO setTextMail(CarroAbandonadoDTO carrAbandMet) {

			CarroAbandonadoDTO carrAband = carrAbandMet;
			StringBuffer textoPagina = new StringBuffer();
			String linea = null;
			
//			SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat salida = new SimpleDateFormat("dd-MM-yyyy");
			
			String car_tipo_cupon = carrAband.getCar_tipo_cupon();
			String cli_nombres = carrAband.getCli_nombre() + " " + carrAband.getCli_apellido_pat();
			carrAband.setCli_nombres_mail(cli_nombres);
			String car_cupon = carrAband.getCar_cupon();
			String car_cupon_monto = carrAband.getCar_cupon_monto_mail();
			String cat_nombre = carrAband.getCat_nombre();	
			
			
//			String car_fecha_fin_sinformato = carrAband.getCar_fecha_fin();
			
			String car_fecha_fin="";
			if ((carrAband.getCar_fecha_fin()==null) || (carrAband.getCar_fecha_fin().equalsIgnoreCase("null")))
					{
			 		car_fecha_fin="";
			 		}
			else{
//				System.out.println("fecha a parsear:"+carrAband.getCar_fecha_fin());
				// se cambia el formato de la fecha a dd-MM-yyyy
				try {
						car_fecha_fin = salida.format(format.parse(carrAband.getCar_fecha_fin()));
					} catch (ParseException e1) {
				// TODO Bloque catch generado automáticamente
						e1.printStackTrace();
			}
			}
			logger.debug("Fecha formato:"+car_fecha_fin);
			String google = "";
			
			String direccion = Util.getPropertiesString("html.cupon.directorio");
			String pag_dscto = Util.getPropertiesString("html.cupon.descuento");
			String pag_dpcho = Util.getPropertiesString("html.cupon.despacho");
			String pag_recda = Util.getPropertiesString("html.cupon.recuerda");
			
			
			
			if (car_tipo_cupon.equalsIgnoreCase("R")){
				try {
					BufferedReader br = new BufferedReader(new FileReader(direccion+pag_dscto));

					google = GOOGLE_RUBRO+Util.fixCaracteres(cat_nombre.toLowerCase());
					
					while ((linea = br.readLine()) != null) {

						linea = linea.replaceAll("\\{nombre\\}", cli_nombres);
						linea = linea.replaceAll("\\{dcto\\}", car_cupon_monto);
						linea = linea.replaceAll("\\{cupon\\}", car_cupon);
						linea = linea.replaceAll("\\{categoria\\}", cat_nombre);
						linea = linea.replaceAll("\\{fecha\\}", car_fecha_fin);
						linea = linea.replaceAll("\\{google\\}", google);
						textoPagina.append(linea);
					}
					br.close(); br = null;
				} catch (FileNotFoundException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				}
			}else if (car_tipo_cupon.equalsIgnoreCase("S")){
				try {
					BufferedReader br = new BufferedReader(new FileReader(direccion+pag_dscto));

					google = GOOGLE_SECCION+Util.fixCaracteres(cat_nombre.toLowerCase());
					
					while ((linea = br.readLine()) != null) {

						linea = linea.replaceAll("\\{nombre\\}", cli_nombres);
						linea = linea.replaceAll("\\{dcto\\}", car_cupon_monto);
						linea = linea.replaceAll("\\{cupon\\}", car_cupon);
						linea = linea.replaceAll("\\{categoria\\}", cat_nombre);
						linea = linea.replaceAll("\\{fecha\\}", car_fecha_fin);
						linea = linea.replaceAll("\\{google\\}", google);
						textoPagina.append(linea);
					}
					br.close(); br = null;
				} catch (FileNotFoundException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				}
			}
			else if (car_tipo_cupon.equalsIgnoreCase("D")){
				try {
					BufferedReader br = new BufferedReader(new FileReader(direccion+pag_dpcho));
					google = GOOGLE_DESPACHO;
					while ((linea = br.readLine()) != null) {

						linea = linea.replaceAll("\\{nombre\\}", cli_nombres);
						linea = linea.replaceAll("\\{cupon\\}", car_cupon);
						linea = linea.replaceAll("\\{fecha\\}", car_fecha_fin);
						linea = linea.replaceAll("\\{google\\}", google);
						textoPagina.append(linea);
					}
					br.close(); br = null;
				} catch (FileNotFoundException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				}
			}
			else{ // solo recordatorio
				try {
					BufferedReader br = new BufferedReader(new FileReader(direccion+pag_recda));
					google = GOOGLE_RECUERDA;
					while ((linea = br.readLine()) != null) {

						linea = linea.replaceAll("\\{nombre\\}", cli_nombres);
						//linea = linea.replaceAll("\\{fecha\\}", car_fecha_fin);
						linea = linea.replaceAll("\\{google\\}", google);
						textoPagina.append(linea);
					}
					br.close(); br = null;
				} catch (FileNotFoundException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("[Maestro][setTextMail] Error al abrir archivo",e);
					e.printStackTrace();
				}
			}

			carrAband.setCar_text_mail(textoPagina);
			return carrAband;
	}

	/**
	 * Método que se encarga de realizar el envio del correo electronico
	 * @param file
	 * @throws Exception
	 */
	public boolean enviaEmail(CarroAbandonadoDTO carrAband){
		
		logger.debug("[Maestro][enviaEmail] Ingreso al método");
		logger.debug("[Maestro][enviaEmail] Se realiza el envio de correo electronico");
		
		// Se recuperan los valores para el envio de correo electronico.
		String host 	= Util.getPropertiesString("mail.smtp.host");
		String from 	= Util.getPropertiesString("mail.from");		
		String to 		= "";//Util.getPropertiesString("mail.to");
		String cc 		= "";//Util.getPropertiesString("mail.cc");		
		String subject	= Util.getPropertiesString("mail.subject");		
		String bodyHtml = "";//Util.getPropertiesString("mail.bodyHtml");
		String sendmail = Util.getPropertiesString("mail.send");
				
		// Se setean los archivos adjuntos.
		List archivos = null;
		
		CarroAbandonadoDTO carrAbandTemp = carrAband;
		//FIXME
//		CORREO SE UTILIZA SOLO PARA DESARROLLO
//		to = "cristian.valdebenito@cencosud.cl";//carrAbandTemp.getCli_email();
		to = carrAbandTemp.getCli_email();
		bodyHtml = carrAbandTemp.getCar_text_mail().toString();
		subject = carrAbandTemp.getCli_nombres_mail()+subject;
		
		// 1 indica que el correo electronico se debe realizar
		if("1".equals(sendmail)){
			logger.info("[Maestro][enviaEmail] Conectando SMTP host["+host+"]");
			
			if (logger.isDebugEnabled()){
				logger.debug("[Maestro][enviaEmail] host["+host+"]");
				logger.debug("[Maestro][enviaEmail] from["+from+"]");
				logger.debug("[Maestro][enviaEmail] to["+to+"]");
				logger.debug("[Maestro][enviaEmail] cc["+cc+"]");
				logger.debug("[Maestro][enviaEmail] subject["+subject+"]");
				logger.debug("[Maestro][enviaEmail] bodyHtml["+bodyHtml+"]");
			}
			
			// Se realiza el envio de mail.
			try {
				JFactory.getSendMail(host, from, bodyHtml, archivos).enviar(to, cc, subject);
			} catch (Exception e) {
				logger.error("[Maestro][enviaEmail] Error al enviar email",e);
				return false;
			}
			
		}else{
			logger.info("[Maestro][enviaEmail][IMPORTANTE] La funcion que envia email se encuentra DESACTIVADA (Para activar el envio de email cambie el parametro 'mail.send=1' en la configuracion.)");
		}
		
		logger.info("[Maestro][enviaEmail]Correo enviado correctamente");
		return true;
	}

	public void enviarEmails(List lstCarrAband){
		List lstCarrAbandTemp = lstCarrAband;
		CarroAbandonadoDAO dao = new CarroAbandonadoDAO();
		StringBuffer textoEmail = new StringBuffer();
		String textoEmailModif = "";
//		String urlJumbo = Util.getPropertiesString("jumbo.url.fo")+"VerCuponMail?key=";
//		String key = "";
		for (int i = 0; i < lstCarrAbandTemp.size(); i++){
			String key = "";
			String urlJumbo = Util.getPropertiesString("jumbo.url.fo")+"VerCuponMail?key=";
			CarroAbandonadoDTO carrAband = (CarroAbandonadoDTO)lstCarrAbandTemp.get(i);
			try {
				int idCarroAbandonado = dao.getInsertCarroAbandonado(carrAband);
				int idClienteCarro = carrAband.getCli_id();
				
				try {
					key = Util.b64encode((idCarroAbandonado+"").getBytes());
					key = key.replaceAll("=", "_");
					urlJumbo = urlJumbo + key;
//					System.out.println("*** "+urlJumbo);
				} catch (MessagingException e) {
					logger.error("[Maestro][enviarEmails] Error al codificar url",e);
					e.printStackTrace();
				} catch (IOException e) {
					logger.error("[Maestro][enviarEmails] Error al codificar url",e);
					e.printStackTrace();
				}

				if (idCarroAbandonado > 0){
					textoEmail = carrAband.getCar_text_mail();
					textoEmailModif = textoEmail.toString().replaceAll("\\{visitamail\\}", urlJumbo);
					textoEmail = new StringBuffer(textoEmailModif);
					carrAband.setCar_text_mail(textoEmail);
					if (enviaEmail(carrAband)){
						// marcar en BD carro de compra como email enviado
						dao.getUpdateCarroCompra(idClienteCarro);
						
						// marcar en BD carro de compra como email enviado
						dao.getUpdateCarroAbandonado(idCarroAbandonado);
					}
				}
			} catch (SQLException e) {
				logger.error("[Maestro][enviarEmails] Error al insertar carro abandonado a BD",e);
				e.printStackTrace();
			}
		}
	}
	
}
