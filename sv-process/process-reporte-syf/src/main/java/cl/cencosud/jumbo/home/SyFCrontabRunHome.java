package cl.cencosud.jumbo.home;

import cl.cencosud.jumbo.beans.BeanDetalle;
import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.conexion.ConexionUtil;
import cl.cencosud.jumbo.conexion.IRSDataSource;
//import cl.cencosud.jumbo.log.Logging;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class SyFCrontabRunHome {

	private static SyFCrontabRunHome ch = null;
	private static Logger logger;

	static {
		logger = Logger.getLogger(cl.cencosud.jumbo.home.SyFCrontabRunHome.class);
	}
	
	public static SyFCrontabRunHome getHome() {
		if (ch == null) {
			ch = new SyFCrontabRunHome();
		}
		return ch;
	}

	public Map getSustitutosFaltantes(String HoraIni, String HoraFin) {
		Map Locales;
		String METHOD_NAME = "getSustitutosFaltantes";
		String Query = "";
		Locales = new LinkedHashMap();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = dateFormat.format(new Date());
		
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		System.out.println("________________________________");
		
		try {
			
			Query = "SELECT L.NOM_LOCAL AS LOCAL, DP.ID_DETALLE AS ID_DETALLE, INTEGER(SUBSTR(PROD.ID"
					+ "_CATPROD, 1, 2)) AS CAT_PROD,        DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UN"
					+ "I_MED1, DP.DESCRIPCION DESCR1, DP.CANT_SOLIC AS CANT_SOLIC,        CASE WHEN PIK"
					+ ".ID_PRODUCTO IS NULL THEN '' ELSE PROD.COD_PROD1 END COD_PROD2,        CASE WHEN"
					+ " PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.UNI_MED END UNI_MED2,        PIK.DESC"
					+ "RIPCION DESCR2, SUM(PIK.CANT_PICK) AS CANT_PICK_FALT, R.ID_RONDA FROM BODBA.BO_D"
					+ "ETALLE_PICKING PIK      LEFT JOIN BODBA.BO_DETALLE_PEDIDO DP    ON PIK.ID_DETALL"
					+ "E   = DP.ID_DETALLE AND PIK.SUSTITUTO = 'S'      LEFT JOIN BODBA.BO_DETALLE_ROND"
					+ "AS AS DR ON DR.ID_DETALLE    = DP.ID_DETALLE      LEFT JOIN BODBA.BO_PRODUCTOS P"
					+ "ROD       ON PROD.ID_PRODUCTO = PIK.ID_PRODUCTO      JOIN BODBA.BO_RONDAS R     "
					+ "             ON R.ID_RONDA       = DR.ID_RONDA      JOIN BODBA.BO_PEDIDOS P     "
					+ "            ON P.ID_PEDIDO      = DP.ID_PEDIDO AND P.ORIGEN = 'W'      JOIN BODB"
					+ "A.BO_LOCALES L                 ON L.ID_LOCAL       = P.ID_LOCAL  WHERE DATE(R.FF"
					+ "IN_PICKING) = DATE('"
					+ fecha
					+ "') "
					+ "  AND TIME(R.FFIN_PICKING) BETWEEN TIME('"
					+ HoraIni
					+ "') AND TIME('"
					+ HoraFin
					+ "') "
					+ "  AND INTEGER(DP.COD_PROD1) <> INTEGER(PROD.COD_PROD1) "
					+ "  AND POSSTR(PIK.DESCRIPCION, '+') = 0 "
					+ "  AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) "
					+ "                                    FROM BODBA.BO_INF_SYF SF "
					+ "                                    WHERE SF.COD_PROD_ORIGINAL = DP.COD_PROD1  "
					+ "                                      AND SF.UME_ORIGINAL      = DP.UNI_MED "
					+ "                                      AND SF.COD_PROD_SUS      = PROD.COD_PROD1 "
					+ "                                      AND SF.UME_SUS           = PROD.UNI_MED) "
					+ "GROUP BY L.NOM_LOCAL, PROD.ID_CATPROD, DP.ID_DETALLE, DP.COD_PROD1, "
					+ "         DP.UNI_MED, DP.DESCRIPCION, DP.CANT_SOLIC, "
					+ "         PIK.ID_PRODUCTO, PROD.COD_PROD1, PROD.UNI_MED, PIK.DESCRIPCION, R.ID_RO"
					+ "NDA "
					+ " "
					+ "UNION "
					+ " "
					+ "SELECT L.NOM_LOCAL AS LOCAL, DP.ID_DETALLE AS ID_DETALLE, INTEGER(SUBSTR(PROD.ID"
					+ "_CATPROD, 1, 2)) AS CAT_PROD, "
					+ "       DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UNI_MED1, DP.DESCRIPCION AS DESC"
					+ "R1, DP.CANT_SOLIC AS CANT_SOLIC, "
					+ "       '' AS COD_PROD2, '' AS UNI_MED2, '' AS DESCR2, "
					+ "       DP.CANT_FALTAN AS CANT_PICK_FALT, R.ID_RONDA "
					+ "FROM BODBA.BO_DETALLE_PEDIDO DP "
					+ "     JOIN BODBA.BO_PRODUCTOS PROD    ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO "
					+ "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_PEDIDO   = DP.ID_PEDIDO "
					+ "     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_DETALLE = DP.ID_DETALLE  "
					+ "     JOIN BODBA.BO_RONDAS R          ON R.ID_RONDA    = DR.ID_RONDA "
					+ "     JOIN BODBA.BO_LOCALES L         ON L.ID_LOCAL    = P.ID_LOCAL "
					+ "WHERE DP.CANT_FALTAN > 0  "
					+ "  AND DATE(R.FFIN_PICKING) = DATE('"
					+ fecha
					+ "') "
					+ "  AND TIME(R.FFIN_PICKING) BETWEEN TIME('"
					+ HoraIni
					+ "') AND TIME('"
					+ HoraFin
					+ "') "
					+ "ORDER BY LOCAL, CAT_PROD, DESCR1 WITH UR";
			
			logger.debug("Query (getSustitutosFaltantes): " + Query);
			
			conn = IRSDataSource.getSingleton().getConnection();
			stm = conn.prepareStatement( Query );
			rs = stm.executeQuery();
			
			while(rs.next()){
				BeanDetalle bd = new BeanDetalle();
				bd.setLocal(rs.getString("LOCAL"));
				bd.setIdDetalle(rs.getString("ID_DETALLE"));
				bd.setCatProd(rs.getString("CAT_PROD"));
				bd.setCodProd1(rs.getString("COD_PROD1"));
				bd.setUniMed1(rs.getString("UNI_MED1"));
				bd.setDescripcion1(rs.getString("DESCR1"));
				bd.setCantSolicitada(rs.getString("CANT_SOLIC"));
				bd.setCodProd2(rs.getString("COD_PROD2"));
				bd.setUniMed2(rs.getString("UNI_MED2"));
				bd.setDescripcion2(rs.getString("DESCR2"));
				bd.setCantPickeadaFaltante(rs.getString("CANT_PICK_FALT"));
				bd.setIdRonda(rs.getString("ID_RONDA"));
				
				if (Locales.get(bd.getLocal()) != null) {
					((LinkedHashMap) Locales.get(bd.getLocal())).put(
							bd.getIdDetalle(), bd);
				} else {
					Map local = new LinkedHashMap();
					local.put(bd.getIdDetalle(), bd);
					Locales.put(bd.getLocal(), local);
				}
			}
			
		} catch (SQLException e) {
			logger.debug(getClass() + ":" + METHOD_NAME + ": SQLException: "
					+ e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			logger.debug(getClass() + ":" + METHOD_NAME + ": Exception: "
					+ e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
							+ " : " + e);
					e.printStackTrace();
				}
			}
			
			try { rs.close(); } catch(Exception e) { logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
														+ " : Error al cerrar ResultSet, Descripcion: " + e); }
            
			try { stm.close(); } catch(Exception e) { logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
														+ " : Error al cerrar PreparedStatement, Descripcion: " + e); }
		}
		
		return Locales;
	}

	public Map getResumenSF(String HoraIni, String HoraFin) {
		String METHOD_NAME = "getResumenSF";
		String Query = "";
		Map Resumen = new LinkedHashMap();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = dateFormat.format(new Date());
		Connection conn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			
			Query = "SELECT CAT_PROD, COD_PROD, UNI_MED, DESCR, SUM(CANT_SOLIC) AS CANT_SOLIC FROM ( "
					+ "SELECT INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AS CAT_PROD,               DP.COD_"
					+ "PROD1 AS COD_PROD, DP.UNI_MED AS UNI_MED, DP.DESCRIPCION AS DESCR, DP.CANT_SOLIC"
					+ " AS CANT_SOLIC        FROM BODBA.BO_DETALLE_PICKING PIK             LEFT JOIN BO"
					+ "DBA.BO_DETALLE_PEDIDO DP    ON PIK.ID_DETALLE   = DP.ID_DETALLE AND PIK.SUSTITUT"
					+ "O = 'S'             LEFT JOIN BODBA.BO_DETALLE_RONDAS AS DR ON DR.ID_DETALLE    "
					+ "= DP.ID_DETALLE             LEFT JOIN BODBA.BO_PRODUCTOS PROD       ON PROD.ID_P"
					+ "RODUCTO = PIK.ID_PRODUCTO             JOIN BODBA.BO_RONDAS R                  ON"
					+ " R.ID_RONDA       = DR.ID_RONDA             JOIN BODBA.BO_PEDIDOS P             "
					+ "    ON P.ID_PEDIDO      = DP.ID_PEDIDO AND P.ORIGEN = 'W'        WHERE DATE(R.FF"
					+ "IN_PICKING) = DATE('"
					+ fecha
					+ "') "
					+ "         AND TIME(R.FFIN_PICKING) BETWEEN TIME('"
					+ HoraIni
					+ "') AND TIME('"
					+ HoraFin
					+ "') "
					+ "         AND INTEGER(DP.COD_PROD1) <> INTEGER(PROD.COD_PROD1) "
					+ "         AND POSSTR(PIK.DESCRIPCION, '+') = 0 "
					+ "         AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) "
					+ "                                           FROM BODBA.BO_INF_SYF SF "
					+ "                                           WHERE SF.COD_PROD_ORIGINAL = DP.COD_P"
					+ "ROD1 "
					+ "                                             AND SF.UME_ORIGINAL      = DP.UNI_M"
					+ "ED "
					+ "                                             AND SF.COD_PROD_SUS      = PROD.COD"
					+ "_PROD1 "
					+ "                                             AND SF.UME_SUS           = PROD.UNI"
					+ "_MED) "
					+ "       UNION "
					+ " "
					+ "       SELECT INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AS CAT_PROD, "
					+ "              DP.COD_PROD1 AS COD_PROD, DP.UNI_MED AS UNI_MED, DP.DESCRIPCION AS"
					+ " DESCR, DP.CANT_SOLIC AS CANT_SOLIC "
					+ "       FROM BODBA.BO_DETALLE_PEDIDO DP "
					+ "            JOIN BODBA.BO_PRODUCTOS PROD    ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO"
					+ " "
					+ "            JOIN BODBA.BO_PEDIDOS P         ON P.ID_PEDIDO   = DP.ID_PEDIDO "
					+ "            JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_DETALLE = DP.ID_DETALLE "
					+ "            JOIN BODBA.BO_RONDAS R          ON R.ID_RONDA    = DR.ID_RONDA "
					+ "       WHERE DP.CANT_FALTAN > 0 "
					+ "         AND DATE(R.FFIN_PICKING) = DATE('"
					+ fecha
					+ "') "
					+ "         AND TIME(R.FFIN_PICKING) BETWEEN TIME('"
					+ HoraIni
					+ "') AND TIME('"
					+ HoraFin
					+ "') "
					+ "       ) AS LISTA "
					+ "GROUP BY CAT_PROD, COD_PROD, UNI_MED, DESCR "
					+ "ORDER BY CANT_SOLIC DESC, DESCR ASC WITH UR";
			
			logger.debug("Query (getResumenSF): " + Query);
			BeanDetalle bd;
			
			conn = IRSDataSource.getSingleton().getConnection();
			stm = conn.prepareStatement( Query );
			rs = stm.executeQuery();
			 
			while(rs.next()){
				bd = new BeanDetalle();
				bd.setCatProd(rs.getString("CAT_PROD"));
				bd.setCodProd1(rs.getString("COD_PROD"));
				bd.setUniMed1(rs.getString("UNI_MED"));
				bd.setDescripcion1(rs.getString("DESCR"));
				bd.setCantSolicitada(rs.getString("CANT_SOLIC"));
				
				Resumen.put(bd.getCodProd1() + "-" + bd.getUniMed1(), bd);
			}
		
		} catch (SQLException e) {
			logger.debug(getClass() + ":" + METHOD_NAME + ": SQLException: "
					+ e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			logger.debug(getClass() + ":" + METHOD_NAME + ": Exception: "
					+ e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
							+ " : " + e);
					e.printStackTrace();
				}
			}
			
			try { rs.close(); } catch(Exception e) { logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
														+ " : Error al cerrar ResultSet, Descripcion: " + e); }
			
			try { stm.close(); } catch(Exception e) { logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
														+ " : Error al cerrar PreparedStatement, Descripcion: " + e); }
		}
		
		return Resumen;
	}

	public void GeneraExcel(Map SFLocal, Map Resumen, Map ResumenFinalDia,
			String filename, BeanParamConfig param, String HoraIni,
			String HoraFin) {
		
		String METHOD_NAME = "GeneraExcel";
		String RutaImagen1 = param.getPathImagen1();
		String RutaImagen2 = param.getPathImagen2();
		HSSFWorkbook wb = new HSSFWorkbook();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String Fecha = dateFormat.format(new Date());
		
		try {
			org.apache.poi.hssf.usermodel.HSSFDataFormat format = wb
					.createDataFormat();
			
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Arial");
			font.setItalic(false);
			
			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short) 10);
			font2.setFontName("Arial");
			font2.setItalic(false);
			font2.setBoldweight((short) 700);
			
			HSSFFont font4 = wb.createFont();
			font4.setFontHeightInPoints((short) 14);
			font4.setFontName("Arial");
			font4.setItalic(false);
			font4.setBoldweight((short) 700);
			font4.setColor((short) 23);
			
			HSSFCellStyle style = wb.createCellStyle();
			style.setFont(font2);
			style.setBorderTop((short) 2);
			style.setBorderBottom((short) 2);
			style.setBorderRight((short) 2);
			style.setBorderLeft((short) 2);
			style.setFillBackgroundColor((short) 12);
			style.setFillForegroundColor((short) 22);
			style.setFillPattern((short) 1);
			style.setAlignment((short) 2);
			
			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setFont(font);
			style2.setBorderTop((short) 1);
			style2.setBorderBottom((short) 1);
			style2.setBorderRight((short) 1);
			style2.setBorderLeft((short) 1);
			style2.setFillForegroundColor((short) 9);
			
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setFont(font);
			style3.setBorderTop((short) 1);
			style3.setBorderBottom((short) 1);
			style3.setBorderRight((short) 1);
			style3.setBorderLeft((short) 1);
			style3.setFillForegroundColor((short) 9);
			style3.setAlignment((short) 2);
			
			HSSFCellStyle style4 = wb.createCellStyle();
			style4.setFont(font4);
			style4.setBorderTop((short) 0);
			style4.setBorderBottom((short) 0);
			style4.setBorderRight((short) 0);
			style4.setBorderLeft((short) 0);
			style4.setFillForegroundColor((short) 9);
			
			Map locales = new LinkedHashMap();
			for (Iterator it = SFLocal.keySet().iterator(); it.hasNext();) {
				String key = it.next().toString();
				if (locales.get(key) == null) {
					locales.put(key, key);
				}
			}

			HSSFSheet sheet;
			for (Iterator it = locales.keySet().iterator(); it.hasNext(); sheet
					.setColumnWidth((short) 12, (short) 20000)) {
				
				String key = it.next().toString();
				Map lstSF = null;
				if (SFLocal.get(key) != null) {
					lstSF = (LinkedHashMap) SFLocal.get(key);
				}
				
				sheet = wb.createSheet(key);
				HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0,
						(short) 0, 0, (short) 3, 7);
				anchor.setAnchorType(2);
				patriarch.createPicture(anchor, loadPicture(RutaImagen1, wb));
				HSSFClientAnchor anchor2 = new HSSFClientAnchor(0, 0, 0, 0,
						(short) 4, 0, (short) 5, 7);
				
				anchor2.setAnchorType(2);
				patriarch.createPicture(anchor2, loadPicture(RutaImagen2, wb));
				HSSFRow row8 = sheet.createRow(8);
				HSSFCell cell_8_4 = row8.createCell((short) 4);
				cell_8_4.setCellValue(new HSSFRichTextString("Local (" + key
						+ ") | Fecha (" + Fecha + ") | Jornada (" + HoraIni
						+ " a " + HoraFin + ")"));
				cell_8_4.setCellStyle(style4);
				HSSFRow row0 = sheet.createRow(10);
				
				HSSFCell cell_0_0 = row0.createCell((short) 0);
				cell_0_0.setCellValue(new HSSFRichTextString("SECCION"));
				cell_0_0.setCellStyle(style);
				
				HSSFCell cell_0_1 = row0.createCell((short) 1);
				cell_0_1.setCellValue(new HSSFRichTextString("SAP"));
				cell_0_1.setCellStyle(style);
				
				HSSFCell cell_0_2 = row0.createCell((short) 2);
				cell_0_2.setCellValue(new HSSFRichTextString("UME"));
				cell_0_2.setCellStyle(style);
				
				HSSFCell cell_0_3 = row0.createCell((short) 3);
				cell_0_3.setCellValue(new HSSFRichTextString("N\260 RONDA"));
				cell_0_3.setCellStyle(style);
				
				HSSFCell cell_0_4 = row0.createCell((short) 4);
				cell_0_4.setCellValue(new HSSFRichTextString("DESCRIPCION PRODUCTO FALTANTE"));
				cell_0_4.setCellStyle(style);
				
				HSSFCell cell_0_5 = row0.createCell((short) 5);
				cell_0_5.setCellValue(new HSSFRichTextString("CANT. SOLICITADA"));
				cell_0_5.setCellStyle(style);
				
				HSSFCell cell_0_6 = row0.createCell((short) 6);
				cell_0_6.setCellValue(new HSSFRichTextString("CANT. FALTANTE"));
				cell_0_6.setCellStyle(style);
				
				HSSFCell cell_0_7 = row0.createCell((short) 7);
				cell_0_7.setCellValue(new HSSFRichTextString("SAP SUS"));
				cell_0_7.setCellStyle(style);
				
				HSSFCell cell_0_8 = row0.createCell((short) 8);
				cell_0_8.setCellValue(new HSSFRichTextString("UME SUS"));
				cell_0_8.setCellStyle(style);
				
				HSSFCell cell_0_9 = row0.createCell((short) 9);
				cell_0_9.setCellValue(new HSSFRichTextString(
						"DESCRIPCION SUSTITUTO (SI ESTA EN BLANCO NO SE SUSTITUYO)"));
				cell_0_9.setCellStyle(style);
				
				HSSFCell cell_0_10 = row0.createCell((short) 10);
				cell_0_10.setCellValue(new HSSFRichTextString("MOTIVO FALTANTE"));
				cell_0_10.setCellStyle(style);
				
				HSSFCell cell_0_11 = row0.createCell((short) 11);
				cell_0_11.setCellValue(new HSSFRichTextString("N\272 DE ORDEN"));
				cell_0_11.setCellStyle(style);
				
				HSSFCell cell_0_12 = row0.createCell((short) 12);
				cell_0_12.setCellValue(new HSSFRichTextString("OBSERVACION"));
				cell_0_12.setCellStyle(style);
				
				int j = 10;
				if (lstSF != null) {
					HSSFCell cell_12;
					
					for (Iterator its = lstSF.keySet().iterator(); its
							.hasNext(); cell_12.setCellStyle(style2)) {
						
						String key1 = its.next().toString();
						j++;
						HSSFRow row = sheet.createRow((short) j);
						BeanDetalle bd = (BeanDetalle) lstSF.get(key1);
						
						HSSFCell cell_0 = row.createCell((short) 0);
						cell_0.setCellValue(Double.parseDouble(bd.getCatProd()));
						cell_0.setCellStyle(style2);
						
						HSSFCell cell_1 = row.createCell((short) 1);
						cell_1.setCellValue(new HSSFRichTextString(bd.getCodProd1()));
						cell_1.setCellStyle(style2);
						
						HSSFCell cell_2 = row.createCell((short) 2);
						cell_2.setCellValue(new HSSFRichTextString(bd.getUniMed1()));
						cell_2.setCellStyle(style2);
						
						HSSFCell cell_3 = row.createCell((short) 3);
						cell_3.setCellValue(Double.parseDouble(bd.getIdRonda()));
						cell_3.setCellStyle(style3);
						
						HSSFCell cell_4 = row.createCell((short) 4);
						cell_4.setCellValue(new HSSFRichTextString(bd.getDescripcion1()));
						cell_4.setCellStyle(style2);
						
						HSSFCell cell_5 = row.createCell((short) 5);
						cell_5.setCellValue(Double.parseDouble(bd.getCantSolicitada()));
						cell_5.setCellStyle(style3);
						
						HSSFCell cell_6 = row.createCell((short) 6);
						cell_6.setCellValue(Double.parseDouble(bd.getCantPickeadaFaltante()));
						cell_6.setCellStyle(style3);
						
						HSSFCell cell_7 = row.createCell((short) 7);
						cell_7.setCellValue(new HSSFRichTextString(bd.getCodProd2()));
						cell_7.setCellStyle(style2);
						
						HSSFCell cell_8 = row.createCell((short) 8);
						cell_8.setCellValue(new HSSFRichTextString(bd.getUniMed2()));
						cell_8.setCellStyle(style2);
						
						HSSFCell cell_9 = row.createCell((short) 9);
						cell_9.setCellValue(new HSSFRichTextString(bd.getDescripcion2()));
						cell_9.setCellStyle(style2);
						
						HSSFCell cell_10 = row.createCell((short) 10);
						cell_10.setCellValue(new HSSFRichTextString(" "));
						cell_10.setCellStyle(style2);
						
						HSSFCell cell_11 = row.createCell((short) 11);
						cell_11.setCellValue(new HSSFRichTextString(" "));
						cell_11.setCellStyle(style2);
						
						cell_12 = row.createCell((short) 12);
						cell_12.setCellValue(new HSSFRichTextString(" "));
					}
				}
				
				sheet.autoSizeColumn((short) 0);
				sheet.autoSizeColumn((short) 1);
				sheet.autoSizeColumn((short) 2);
				sheet.autoSizeColumn((short) 3);
				sheet.setColumnWidth((short) 4, (short) 20000);
				sheet.autoSizeColumn((short) 5);
				sheet.autoSizeColumn((short) 6);
				sheet.autoSizeColumn((short) 7);
				sheet.autoSizeColumn((short) 8);
				sheet.setColumnWidth((short) 9, (short) 20000);
				sheet.autoSizeColumn((short) 10);
				sheet.autoSizeColumn((short) 11);
			}

			wb = AgregaResumen(wb, "Resumen Jornada", Fecha, param, Resumen,
					HoraIni, HoraFin, style, style2, style3, style4);
			
			if (ResumenFinalDia != null) {
				wb = AgregaResumen(wb, "Resumen Diario", Fecha, param,
						ResumenFinalDia, "00", HoraFin, style, style2, style3,
						style4);
			}
			
			FileOutputStream fos = new FileOutputStream(filename);
			wb.write(fos);
			fos.close();
			
		} catch (Exception e) {
			logger.debug("ERROR: " + getClass() + "." + METHOD_NAME + " : " + e);
			e.printStackTrace();
		}
	}

	public HSSFWorkbook AgregaResumen(HSSFWorkbook wb, String NombreHoja,
			String Fecha, BeanParamConfig param, Map lstResumen,
			String HoraIni, String HoraFin, HSSFCellStyle style,
			HSSFCellStyle style2, HSSFCellStyle style3, HSSFCellStyle style4) {
		
		String METHOD_NAME = "AgregaResumen";
		
		try {
			HSSFSheet sheet = wb.createSheet(NombreHoja);
			HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 3, 7);
			anchor.setAnchorType(2);
			patriarch.createPicture(anchor, loadPicture(param.getPathImagen1(), wb));
			
			HSSFClientAnchor anchor2 = new HSSFClientAnchor(0, 0, 0, 0, (short) 3, 0, (short) 4, 7);
			anchor2.setAnchorType(2);
			patriarch.createPicture(anchor2, loadPicture(param.getPathImagen2(), wb));
			
			HSSFRow row8 = sheet.createRow(8);
			HSSFCell cell_8_3 = row8.createCell((short) 3);
			cell_8_3.setCellValue(new HSSFRichTextString("Fecha (" + Fecha
					+ ") | Jornada (" + HoraIni + " a " + HoraFin + ")"));
			cell_8_3.setCellStyle(style4);
			
			HSSFRow row0 = sheet.createRow(10);
			HSSFCell cell_0_0 = row0.createCell((short) 0);
			cell_0_0.setCellValue(new HSSFRichTextString("SECCION"));
			cell_0_0.setCellStyle(style);
			
			HSSFCell cell_0_1 = row0.createCell((short) 1);
			cell_0_1.setCellValue(new HSSFRichTextString("SAP"));
			cell_0_1.setCellStyle(style);
			
			HSSFCell cell_0_2 = row0.createCell((short) 2);
			cell_0_2.setCellValue(new HSSFRichTextString("UME"));
			cell_0_2.setCellStyle(style);
			
			HSSFCell cell_0_3 = row0.createCell((short) 3);
			cell_0_3.setCellValue(new HSSFRichTextString("DESCRIPCION PRODUCTO FALTANTE"));
			cell_0_3.setCellStyle(style);
			
			HSSFCell cell_0_4 = row0.createCell((short) 4);
			cell_0_4.setCellValue(new HSSFRichTextString("CANT. FALTANTE"));
			cell_0_4.setCellStyle(style);
			
			int j = 10;
			if (lstResumen != null) {
				HSSFCell cell_4;
				
				for (Iterator its = lstResumen.keySet().iterator(); its
						.hasNext(); cell_4.setCellStyle(style3)) {
					
					String key = its.next().toString();
					j++;
					
					HSSFRow row = sheet.createRow((short) j);
					BeanDetalle bd = (BeanDetalle) lstResumen.get(key);
					
					HSSFCell cell_0 = row.createCell((short) 0);
					cell_0.setCellValue(Double.parseDouble(bd.getCatProd()));
					cell_0.setCellStyle(style2);
					
					HSSFCell cell_1 = row.createCell((short) 1);
					cell_1.setCellValue(new HSSFRichTextString(bd.getCodProd1()));
					cell_1.setCellStyle(style2);
					
					HSSFCell cell_2 = row.createCell((short) 2);
					cell_2.setCellValue(new HSSFRichTextString(bd.getUniMed1()));
					cell_2.setCellStyle(style2);
					
					HSSFCell cell_3 = row.createCell((short) 3);
					cell_3.setCellValue(new HSSFRichTextString(bd.getDescripcion1()));
					cell_3.setCellStyle(style2);
					
					cell_4 = row.createCell((short) 4);
					cell_4.setCellValue(Double.parseDouble(bd.getCantSolicitada()));
				}
			}
			
			sheet.autoSizeColumn((short) 0);
			sheet.autoSizeColumn((short) 1);
			sheet.autoSizeColumn((short) 2);
			sheet.setColumnWidth((short) 3, (short) 20000);
			sheet.autoSizeColumn((short) 4);
			
		} catch (Exception e) {
			logger.debug("ERROR: " + getClass() + "." + METHOD_NAME + " : " + e);
			e.printStackTrace();
		}
		
		return wb;
	}

	public void CreaZipXLS(String RutaArchivo, String filename) {
		String METHOD_NAME = "CreaZipXML";
		
		try {
			File fd = new File(RutaArchivo + filename);
			FileInputStream fis = new FileInputStream(fd);
			String filenames[] = { filename };
			byte buf[] = new byte[1024];
			
			String outFilename = RutaArchivo + filename.substring(0, filename.indexOf('.')) + ".zip";
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
			
			for (int i = 0; i < filenames.length; i++) {
				out.putNextEntry(new ZipEntry(filenames[i]));
				
				int len;
				while ((len = fis.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				
				out.closeEntry();
				fis.close();
			}

			out.close();
			
		} catch (IOException e) {
			logger.debug("ERROR: " + getClass() + "." + METHOD_NAME + " : " + e);
			e.printStackTrace();
		}
	}

	private static int loadPicture(String path, HSSFWorkbook wb)
			throws IOException {
		
		int pictureIndex;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		
		try {
			fis = new FileInputStream(path);
			bos = new ByteArrayOutputStream();
			int c;
			
			while ((c = fis.read()) != -1) {
				bos.write(c);
			}
			
			pictureIndex = wb.addPicture(bos.toByteArray(), 5);
			
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
		
		return pictureIndex;
	}

	public void EliminaArchivosXLS(String Path) {
		File dir = new File("temp");
		String children[] = dir.list();
		
		if (children == null) {
			logger.debug("El directorio 'temp' NO Existe o NO ES un Directorio");
		} else {
			
			for (int i = 0; i < children.length; i++) {
				String filename = children[i];
				boolean exists = (new File(Path + filename)).exists();
				
				if (exists) {
					boolean success = (new File(Path + filename)).delete();
					if (success) {
						logger.debug("El Archivo '" + filename + "' fue Eliminada con Exito");
					}
				} else {
					logger.debug("El Archivo '" + filename + "' NO Existe.");
				}
			}
		}
	}

	public void EnviaEMail(BeanParamConfig param, String Path,
			String NombreArchivo) {
		
		String METHOD_NAME = "EnviaEMail";
		
		try {
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host", param.getServer());
			Session ses1 = Session.getInstance(prop, null);
			MimeMessage msg = new MimeMessage(ses1);
			msg.setFrom(new InternetAddress(param.getFrom()));
			Map destinatarios = getDestinatariosEMail();
			System.out.println("Enviando CORREO....... : [ " + destinatarios.toString() + " ]");
			
			if (destinatarios != null && destinatarios.size() > 0) {
				String email;
				for (Iterator it = destinatarios.values().iterator(); it
						.hasNext(); msg.addRecipient(
						javax.mail.Message.RecipientType.TO,
						new InternetAddress(email))) {
					email = it.next().toString();
				}
			}
			
			if (param.getCc() != null) {
				String key;
				for (Iterator it = param.getCc().keySet().iterator(); it
						.hasNext(); msg.addRecipient(
						javax.mail.Message.RecipientType.CC,
						new InternetAddress(key))) {
					key = it.next().toString();
				}
			}
			
			if (param.getCco() != null) {
				String key;
				for (Iterator it = param.getCco().keySet().iterator(); it
						.hasNext(); msg.addRecipient(
						javax.mail.Message.RecipientType.BCC,
						new InternetAddress(key))) {
					key = it.next().toString();
				}
			}
			
			msg.setSubject(param.getSubject());
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(param.getMensaje());
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			javax.activation.DataSource source = new FileDataSource(Path + NombreArchivo);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(NombreArchivo);
			multipart.addBodyPart(messageBodyPart);
			
			msg.setContent(multipart);
			msg.setSentDate(new Date());
			
			System.out.println("Path + NombreArchivo : " + Path + NombreArchivo);
			System.out.println("...");
			
			Transport.send(msg);
			
		} catch (MessagingException e) {
			logger.debug("ERROR: " + getClass() + "." + METHOD_NAME + " : " + e);
			e.printStackTrace();
		}
	}

	public Map getDestinatariosEMail() {
		Map destinatarios;
		String METHOD_NAME = "getDestinatariosEMail";
		Connection conn = null;
		String Query = "";
		destinatarios = new LinkedHashMap();
		
		try {
			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			Statement stmt = conn.createStatement();
			
			Query = "SELECT SYF.NOMBRES, SYF.APELLIDOS, SYF.MAIL FROM BODBA.BO_MAIL_SYF SYF WHERE SYF"
					+ ".ACTIVADO = '1' WITH UR";
			logger.debug("Query (getDestinatariosEMail): " + Query);
			
			ResultSet rs;
			String indice;
			String email;
			
			for (rs = stmt.executeQuery(Query); rs.next(); destinatarios.put(
					indice, email)) {
				indice = rs.getString("MAIL").trim();
				email = rs.getString("MAIL").trim();
			}

			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			logger.debug(getClass() + ":" + METHOD_NAME + ": SQLException: "
					+ e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} catch (Exception e) {
			logger.debug(getClass() + ":" + METHOD_NAME + ": Exception: "
					+ e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					logger.debug("ERROR: " + getClass() + "." + METHOD_NAME
							+ " : " + e);
					e.printStackTrace();
				}
			}
		}
		
		return destinatarios;
	}
}