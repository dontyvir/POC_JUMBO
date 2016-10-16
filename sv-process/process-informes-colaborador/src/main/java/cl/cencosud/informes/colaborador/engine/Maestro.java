package cl.cencosud.informes.colaborador.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.cencosud.informes.colaborador.dao.InformeColaboradorDAO;
import cl.cencosud.informes.colaborador.util.ConnectionDB2;
import cl.cencosud.informes.colaborador.util.JFactory;
import cl.cencosud.informes.colaborador.util.Util;

/**
 * Clase que se encarga de gestionar las tareas 
 * a realizar por el proceso del informe
 */
public class Maestro {
	
	private static final int MAX_ROW_EXCEL = 65000;
	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(Maestro.class.getName());
	
	
	/**
	 * Método que se encarga de realizar el proceso de recuperación y envio del reporte
	 * de venta descuento colaborador.
	 */
	public void process() throws Exception{
		
		logger.debug("[Maestro][process] Ingreso al metodo");
		String startDate = Util.getFechaHoraActual();
		logger.info("[Maestro][process] Comienza la ejecución del proceso " + startDate);
		
		InformeColaboradorDAO dao = new InformeColaboradorDAO();
		
		try {
			
			logger.info("[Maestro][process] Realizo llamado a obtener el reporte");
			List reporte = dao.getReporteVentaDescuentoColaborador();
			Iterator iter = reporte.iterator();
			while(iter.hasNext()){
				Map opsCreadas = (Map) iter.next(); 
				long id_pedido = Long.valueOf(String.valueOf(opsCreadas.get("OP"))).longValue();
				List descuentos = dao.descuentoPorId(id_pedido);
				int descuentosTotales = 0;
				Iterator iter2 = descuentos.iterator();
				while(iter2.hasNext()){
					Map descSum = (Map) iter2.next(); 
					descuentosTotales+= Integer.parseInt(String.valueOf(descSum.get("DIF_PRECIO")));
				}
				opsCreadas.put("DIFERENCIA_POR_REEMPLAZO",Integer.valueOf(String.valueOf(descuentosTotales)));
			}
			
			
			logger.info("[Maestro][process] Realizo el llamado a generar el Excel");
			File excel = creaInforme(reporte);
			
			logger.info("[Maestro][process] Realizo el envio del correo electronico");
			enviaInforme(excel);
			
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
	
	
	/**
	 * Método que se encarga de crear el archivo Excel para enviar el reporte
	 * 
	 * @param reporte listado con el reporte
	 * @return <code>File</code> Con excel a enviar
	 * @throws Exception En caso de error en la generación del excel
	 */
	public static File creaInforme(List reporte) throws Exception {	

		logger.debug("[Maestro][creaInforme] Ingreso al metodo");
		
		String rutaInforme = Util.getPropertiesString("path.archivo.excel") + Util.getFechaActual() +Util.getPropertiesString("reporte.nombre.excel");
		HSSFWorkbook libro = JFactory.getHSSFWorkbook();
		
		HSSFCell celda = null;
		int contFilas = 0;
		
		HSSFCellStyle styleHead = libro.createCellStyle();
		styleHead.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		styleHead.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHead.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		styleHead.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		styleHead.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		styleHead.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		
		HSSFCellStyle stylebody = libro.createCellStyle();
		stylebody.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        stylebody.setBorderTop(HSSFCellStyle.BORDER_THIN);
        stylebody.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        stylebody.setBorderRight(HSSFCellStyle.BORDER_THIN);
		
		String colName[] ={"RUT","NOMBRE","TOTAL_BRUTO","TOTAL_RESERVADO","TOTAL_DESCUENTO_RESERVADO","LOCAL",
									  "FECHA_COMPRA","OP","TOTAL_REAL","DESCUENTO_FINAL","DIFERENCIA_POR_REEMPLAZO"};
		
		logger.debug("[Maestro][creaInforme] Recupero la cantidad de hojas que debo crear en el excel");
		int cantidadHojas = (int)Math.round(reporte.size()/MAX_ROW_EXCEL + .5);
		logger.debug("[Maestro][creaInforme] cantidad de hojas " + cantidadHojas);
		
		logger.debug("[Maestro][creaInforme] Por cada una de las hojas realizo la instancia del HSSFSheet ");
		HSSFSheet[] hoja = new HSSFSheet[cantidadHojas];
		// por cada una de las hojas que debo crear instancio un objeto dentro del arreglo.
		for (int i = 0; i < cantidadHojas; i++) {
			hoja[i]	= libro.createSheet();
			hoja[i].setGridsPrinted(false);
			hoja[i].setPrintGridlines(false);
			hoja[i].setDisplayGridlines(false); 
			
			// creo la cabecera para cada una de las hojas del excel
			HSSFRow fila	= hoja[i].createRow(contFilas);		
			for(int j=0; j < colName.length; j++){
				celda = fila.createCell(j);
				celda.setCellStyle(styleHead);
				celda.setCellValue(new HSSFRichTextString(colName[j].replaceAll("_"," ")));
			}
			
		}
		
		logger.debug("[Maestro][creaInforme] Por cada una de las hojas realizo la instancia del HSSFSheet ");
		List reporteColaborador = new ArrayList();
		Iterator iter = reporte.iterator();
		while(iter.hasNext()){
			Map opsCreadas = (Map) iter.next(); 
			String op = String.valueOf(opsCreadas.get("OP"));
			int valorReal = ((BigDecimal)opsCreadas.get("TOTAL_REAL")).toBigInteger().intValue();
			Iterator iterSum =  reporte.iterator();
			while(iterSum.hasNext()){
				Map opsReporte = (Map) iterSum.next(); 
				String opReporte = String.valueOf(opsReporte.get("OP"));
				if(op.equals(opReporte) &&  opsCreadas != opsReporte){
					int valorRealReporte = ((BigDecimal)opsReporte.get("TOTAL_REAL")).toBigInteger().intValue();
					valorReal += valorRealReporte;
					opsCreadas.put("TOTAL_REAL",String.valueOf(valorReal));
					if(iterSum.hasNext()){
						iter.next();
					}
				}
			}
				reporteColaborador.add(opsCreadas);
		}
		
		
		
		
		//la fila se aumenta para no sobreescribir el header
		contFilas++;
		int contadorHoja = 0;
		Iterator it =  reporteColaborador.iterator();
		
		while (it.hasNext()) {
		
			Map opsCreadas = (Map) it.next(); 
			
			//cuando las filas sean multiplo del maximo, aumento el valor, cambio de hora 
			// y comienzo nuevamente la fila en 1 para la nueva hoja
			if(contFilas != 0 && contFilas % MAX_ROW_EXCEL == 0){
				contadorHoja++;
				contFilas = 1;
			}
			
			HSSFRow fila	= hoja[contadorHoja].createRow(contFilas);
			for(int i=0; i < colName.length; i++){
				celda = fila.createCell(i);
				celda.setCellStyle(stylebody);			
				
				if(opsCreadas.get(colName[i]) instanceof BigDecimal){
					
					// en caso de que la columna sea del tipo BigDecimal, saco el valor integer y lo transformo a String, para eliminar el .0000
					celda.setCellValue(new HSSFRichTextString(((BigDecimal)opsCreadas.get(colName[i])).toBigInteger().toString()));
				
				}else{
					
					// En cualquier otro caso, recupero el valor y lo transformo a String
					celda.setCellValue(new HSSFRichTextString(String.valueOf(opsCreadas.get(colName[i]))));
				
				}
				
			}
			
			contFilas++;
		
		}
			
		logger.info("[Maestro][creaInforme] Se creara el archivo excel " + rutaInforme);
		logger.info("[Maestro][creaInforme] Cantidad registros: " + reporte.size());	
		
		// Se crea el archivo, y se escribe el excel.
		FileOutputStream setFile = new FileOutputStream(rutaInforme);
		libro.write(setFile);
		setFile.close();			
		logger.info("[Maestro][creaInforme] End Construccion de xls");
		
		return new File(rutaInforme);
	}
	
	/**
	 * Método que se encarga de realizar el envio del correo electronico con los adjuntos
	 * @param file
	 * @throws Exception
	 */
	public static void enviaInforme(File file) throws Exception {
		
		logger.debug("[Maestro][enviaInforme] Ingreso al método");
		logger.debug("[Maestro][enviaInforme] Se realiza el envio de correo electronico");
		
		// Se recuperan los valores para el envio de correo electronico.
		String host 	= Util.getPropertiesString("mail.smtp.host");
		String from 	= Util.getPropertiesString("mail.from");		
		String to 		= Util.getPropertiesString("mail.to");
		String cc 		= Util.getPropertiesString("mail.cc");		
		String subject	= Util.getPropertiesString("mail.subject");		
		String bodyHtml = Util.getPropertiesString("mail.bodyHtml");
		String sendmail = Util.getPropertiesString("mail.send");
				
		// Se setean los archivos adjuntos.
		List archivos = new ArrayList(1);
		archivos.add(file);
		
		// 1 indica que el correo electronico se debe realizar
		if("1".equals(sendmail)){
			logger.info("[Maestro][enviaInforme] Conectando SMTP host["+host+"]");
			
			if (logger.isDebugEnabled()){
				logger.debug("[Maestro][enviaInforme] host["+host+"]");
				logger.debug("[Maestro][enviaInforme] from["+from+":");
				logger.debug("[Maestro][enviaInforme] to["+to+"]");
				logger.debug("[Maestro][enviaInforme] cc["+cc+"]");
				logger.debug("[Maestro][enviaInforme] subject["+subject+"]");
				logger.debug("[Maestro][enviaInforme] bodyHtml["+bodyHtml+"]");
			}
			
			// Se realiza el envio de mail.
			JFactory.getSendMail(host, from, bodyHtml, archivos).enviar(to, cc, subject);
			
		}else{
			
			logger.info("[Maestro][enviaInforme][IMPORTANTE] La funcion que envia el reporte via email se encuentra DESACTIVADA (Para activar el envio de email cambie el parametro 'mail.send=1' en la configuracion.)");
		
		}
		
		logger.info("[Maestro][enviaInforme]Correo enviado correctamente");
	}
	
	
}
