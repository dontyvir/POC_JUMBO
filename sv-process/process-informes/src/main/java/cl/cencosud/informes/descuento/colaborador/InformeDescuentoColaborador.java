package cl.cencosud.informes.descuento.colaborador;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.cencosud.informes.descuento.colaborador.dao.InformeColaboradorDAO;
import cl.cencosud.informes.descuento.colaborador.dto.ColaboradorDTO;
import cl.cencosud.informes.descuento.colaborador.dto.InformeColaboradorDTO;
import cl.cencosud.util.Parametros;
import cl.cencosud.util.SendMail;

public class InformeDescuentoColaborador {
	protected static Logger logger = Logger.getLogger(InformeDescuentoColaborador.class.getName());
	
	public static void main(String[] args) {
		try {
			logger.info("INIT	INFORME DESCUENTO COLABORADOR");
			enviarCorreo(generaExcel(getDataInforme()));
			logger.info("END	INFORME DESCUENTO COLABORADOR");
		} catch (Exception e) {
			logger.error("Error: ", e);
		}
	}
	
	public static String getFechaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
        return formateador.format(ahora);
    }
	
	public static String getDiaActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd");
        return formateador.format(ahora);
    }
	
	public static String getMesActual() {
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("MM");
        return formateador.format(ahora);
    }
	
	public static List getDataInforme() throws Exception {
		logger.info("Init	Obtener data colaboradores");
		List listInforme = new ArrayList();
		boolean isOne = false;
		boolean isNewYear = false;
		System.out.println("	Dia: " + getDiaActual());
		System.out.println("	Mes: " + getMesActual());
		if(getDiaActual().equals("01"))	isOne = true;
		if(getMesActual().equals("01"))	isNewYear = true;
		InformeColaboradorDAO icDAO = new InformeColaboradorDAO();
		List colaboradores = icDAO.getColaboradores();
		logger.info("	Cantidadad de colaboradores: " + colaboradores.size());
		Iterator it = colaboradores.iterator();
		while (it.hasNext()) {
			ColaboradorDTO colaborador = (ColaboradorDTO) it.next();
			InformeColaboradorDTO icDTO = icDAO.getInformeColaborador(colaborador, isOne, isNewYear);
			if(icDTO != null){
				listInforme.add(icDTO);
			}
		}
		icDAO.closeConecciones();
		if(listInforme.size() > 0){
			logger.info("	Cantidad en informe: " + listInforme.size());
		} else {
			logger.info("NO SE REGISTRAN MOVIMIENTOS");
		}
		logger.info("End	Obtener data colaboradores");
		return listInforme;
	}

	public static File generaExcel(List listInforme) throws Exception {
		logger.info("Init	Construccion de xls");
		String rutaInforme = Parametros.getString("ic.ruta.file") + getFechaActual() +
				"inf_desc_colaborador" + ".xls";
		HSSFWorkbook libro = new HSSFWorkbook();
		HSSFSheet hoja = null;
		HSSFRow fila = null;
		HSSFCell celda = null;
		HSSFRichTextString texto = null;
		int contFilas = 0;
		String colName[] = {"RUT", "Compra", "Descuento"};
		hoja = libro.createSheet("Colaboradores");
		fila = hoja.createRow(contFilas);
		for(int i=0; i < 3; i++){
			celda = fila.createCell(i);
			texto = new HSSFRichTextString(colName[i]);
			celda.setCellValue(texto);
		}
		contFilas++;
		Iterator it = listInforme.iterator();
		while (it.hasNext()) {
			InformeColaboradorDTO icDTO = (InformeColaboradorDTO) it.next();
			fila = hoja.createRow(contFilas);
			celda = fila.createCell(0);
			celda.setCellValue(icDTO.getColaborador().getColRut());
			celda = fila.createCell(1);
			celda.setCellValue(icDTO.getCompraAcumulada());
			celda = fila.createCell(2);
			celda.setCellValue(icDTO.getDescuentoAcumlado());
			contFilas++;
		}
		logger.info("	Cantidad de filas: " + listInforme.size());	
		FileOutputStream setFile = new FileOutputStream(rutaInforme);
		libro.write(setFile);
		setFile.close();
		File file = new File(rutaInforme);
		logger.info("End	Construccion de xls");
		return file;
	}

	public static void enviarCorreo(File file) throws Exception {
		logger.info("Init	Enviar correo");
		String to = Parametros.getString("ic.mail.to");
		String cc = Parametros.getString("ic.mail.cc");
		String host = Parametros.getString("mail.smtp.host");
		String subject = "Informe Quincenal Descuento Colaborador";
		String from = "Informe Descuento Colaborador <no-reply@cencosud.cl>";
		String bodyHtml = "Estimados,<br /><br />" +
				"Se adjunta informe quincenal Descuento Colaborador.<br /><br />" +
				"Saludos";
		List archivos = new ArrayList();
		archivos.add(file);
		new SendMail(host, from, bodyHtml, archivos).enviar(to, cc, subject);
		if (logger.isDebugEnabled())	logger.debug("	Host: " + host);
		logger.debug("	To: " + to);
		logger.debug("	Cc: " + cc);
		logger.info("End	Enviar correo");
	}

}
