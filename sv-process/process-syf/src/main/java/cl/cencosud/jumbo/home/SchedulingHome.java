/*
 * Creado el 08-jul-04
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.home;

import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.beans.BeanDetalle;
import cl.cencosud.jumbo.conexion.ConexionUtil;
import cl.cencosud.jumbo.log.Logging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;


/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class SchedulingHome {
	private static SchedulingHome ch = null;
	private Logging logger = new Logging(this);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_ HHmmss");
	//private Map Locales = new LinkedHashMap();
	
	public static SchedulingHome getHome() {
		if (ch == null)
			ch = new SchedulingHome();
		return ch;
	}


	/*
	 * 
	 * SELECT L.NOM_LOCAL AS LOCAL, DP.ID_DETALLE AS ID_DETALLE, INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AS CAT_PROD, 
       DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UNI_MED1, DP.DESCRIPCION DESCR1, DP.CANT_SOLIC AS CANT_SOLIC, 
       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.COD_PROD1 END COD_PROD2, 
       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.UNI_MED END UNI_MED2, 
       PIK.DESCRIPCION DESCR2, SUM(PIK.CANT_PICK) AS CANT_PICK_FALT, R.ID_RONDA 
FROM BODBA.BO_DETALLE_PICKING PIK 
     LEFT JOIN BODBA.BO_DETALLE_PEDIDO DP    ON PIK.ID_DETALLE   = DP.ID_DETALLE AND PIK.SUSTITUTO = 'S' 
     LEFT JOIN BODBA.BO_DETALLE_RONDAS AS DR ON DR.ID_DETALLE    = DP.ID_DETALLE 
     LEFT JOIN BODBA.BO_PRODUCTOS PROD       ON PROD.ID_PRODUCTO = PIK.ID_PRODUCTO 
     JOIN BODBA.BO_RONDAS R                  ON R.ID_RONDA       = DR.ID_RONDA 
     JOIN BODBA.BO_SECTORES_LOCAL S          ON S.ID_SECTOR      = DP.ID_SECTOR 
     JOIN BODBA.BO_LOCALES L                 ON L.ID_LOCAL       = S.ID_LOCAL 
     JOIN BODBA.BO_PEDIDOS P                 ON P.ID_PEDIDO      = DP.ID_PEDIDO AND P.ORIGEN = 'W' 
WHERE DATE(R.FFIN_PICKING) = CURRENT DATE 
  AND TIME(R.FFIN_PICKING) BETWEEN TIME('00:00:00') AND TIME('12:00:00')
  AND INTEGER(DP.COD_PROD1) <> INTEGER(PROD.COD_PROD1) 
  AND POSSTR(PIK.DESCRIPCION, '+') = 0
  AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) 
                                    FROM BODBA.BO_INF_SYF SF 
                                    WHERE SF.COD_PROD_ORIGINAL = DP.COD_PROD1 
                                      AND SF.UME_ORIGINAL      = DP.UNI_MED 
                                      AND SF.COD_PROD_SUS      = PROD.COD_PROD1 
                                      AND SF.UME_SUS           = PROD.UNI_MED) 
GROUP BY L.NOM_LOCAL, PROD.ID_CATPROD, DP.ID_DETALLE, DP.COD_PROD1, 
         DP.UNI_MED, DP.DESCRIPCION, DP.CANT_SOLIC, 
         PIK.ID_PRODUCTO, PROD.COD_PROD1, PROD.UNI_MED, PIK.DESCRIPCION, R.ID_RONDA 

UNION

SELECT L.NOM_LOCAL AS LOCAL, DP.ID_DETALLE AS ID_DETALLE, INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AS CAT_PROD, 
       DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UNI_MED1, DP.DESCRIPCION AS DESCR1, DP.CANT_SOLIC AS CANT_SOLIC, 
       '' AS COD_PROD2, '' AS UNI_MED2, '' AS DESCR2,
       DP.CANT_FALTAN AS CANT_PICK_FALT, R.ID_RONDA 
FROM BODBA.BO_DETALLE_PEDIDO DP 
     JOIN BODBA.BO_PRODUCTOS PROD    ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO 
     JOIN BODBA.BO_PEDIDOS P         ON P.ID_PEDIDO   = DP.ID_PEDIDO 
     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_DETALLE = DP.ID_DETALLE 
     JOIN BODBA.BO_RONDAS R          ON R.ID_RONDA    = DR.ID_RONDA 
     JOIN BODBA.BO_LOCALES L         ON L.ID_LOCAL    = P.ID_LOCAL 
WHERE DP.CANT_FALTAN > 0 
  AND DATE(R.FFIN_PICKING) = CURRENT DATE 
  AND TIME(R.FFIN_PICKING) BETWEEN TIME('00:00:00') AND TIME('12:00:00') 
ORDER BY LOCAL, CAT_PROD, DESCR1;
	 */

	
    public Map getSustitutosFaltantes(String HoraIni, String HoraFin){
		String METHOD_NAME = "getSustitutosFaltantes";
		Connection conn = null;
		String Query = "";
		Map Locales = new LinkedHashMap();

		try{
			Calendar Fecha = Calendar.getInstance();
			Fecha.add(Calendar.DAY_OF_MONTH, 1 );
		    
		    ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			Statement stmt = conn.createStatement();
			Query = "SELECT L.NOM_LOCAL AS LOCAL, DP.ID_DETALLE AS ID_DETALLE, INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AS CAT_PROD, " 
                  + "       DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UNI_MED1, DP.DESCRIPCION DESCR1, DP.CANT_SOLIC AS CANT_SOLIC, " 
                  + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.COD_PROD1 END COD_PROD2, " 
                  + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE PROD.UNI_MED END UNI_MED2, " 
                  + "       PIK.DESCRIPCION DESCR2, SUM(PIK.CANT_PICK) AS CANT_PICK_FALT, R.ID_RONDA " 
                  + "FROM BODBA.BO_DETALLE_PICKING PIK " 
                  + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO DP    ON PIK.ID_DETALLE   = DP.ID_DETALLE AND PIK.SUSTITUTO = 'S' " 
                  + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS AS DR ON DR.ID_DETALLE    = DP.ID_DETALLE " 
                  + "     LEFT JOIN BODBA.BO_PRODUCTOS PROD       ON PROD.ID_PRODUCTO = PIK.ID_PRODUCTO " 
                  + "     JOIN BODBA.BO_RONDAS R                  ON R.ID_RONDA       = DR.ID_RONDA " 
                  + "     JOIN BODBA.BO_SECTORES_LOCAL S          ON S.ID_SECTOR      = DP.ID_SECTOR " 
                  + "     JOIN BODBA.BO_LOCALES L                 ON L.ID_LOCAL       = S.ID_LOCAL  "
                  + "     JOIN BODBA.BO_PEDIDOS P                 ON P.ID_PEDIDO      = DP.ID_PEDIDO AND P.ORIGEN = 'W' " 
                  + "WHERE DATE(R.FFIN_PICKING) = CURRENT DATE " 
                  + "  AND TIME(R.FFIN_PICKING) BETWEEN TIME('" + HoraIni + "') AND TIME('" + HoraFin + "') "
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
                  + "         PIK.ID_PRODUCTO, PROD.COD_PROD1, PROD.UNI_MED, PIK.DESCRIPCION, R.ID_RONDA " 
                  + " "
                  + "UNION "
                  + " "
                  + "SELECT L.NOM_LOCAL AS LOCAL, DP.ID_DETALLE AS ID_DETALLE, INTEGER(SUBSTR(PROD.ID_CATPROD, 1, 2)) AS CAT_PROD, " 
                  + "       DP.COD_PROD1 AS COD_PROD1, DP.UNI_MED AS UNI_MED1, DP.DESCRIPCION AS DESCR1, DP.CANT_SOLIC AS CANT_SOLIC, " 
                  + "       '' AS COD_PROD2, '' AS UNI_MED2, '' AS DESCR2, "
                  + "       DP.CANT_FALTAN AS CANT_PICK_FALT, R.ID_RONDA " 
                  + "FROM BODBA.BO_DETALLE_PEDIDO DP " 
                  + "     JOIN BODBA.BO_PRODUCTOS PROD    ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO " 
                  + "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_PEDIDO   = DP.ID_PEDIDO " 
                  + "     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_DETALLE = DP.ID_DETALLE  "
                  + "     JOIN BODBA.BO_RONDAS R          ON R.ID_RONDA    = DR.ID_RONDA " 
                  + "     JOIN BODBA.BO_LOCALES L         ON L.ID_LOCAL    = P.ID_LOCAL " 
                  + "WHERE DP.CANT_FALTAN > 0  "
                  + "  AND DATE(R.FFIN_PICKING) = CURRENT DATE  "
                  + "  AND TIME(R.FFIN_PICKING) BETWEEN TIME('" + HoraIni + "') AND TIME('" + HoraFin + "') " 
                  + "ORDER BY LOCAL, CAT_PROD, DESCR1";
			
			logger.debug("Query (getSustitutosFaltantes): " + Query);
			ResultSet rs = stmt.executeQuery(Query);
			while (rs.next()){
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
			    
			    if (Locales.get(bd.getLocal()) != null){
			        ((LinkedHashMap)Locales.get(bd.getLocal())).put(bd.getIdDetalle(), bd);
			    }else{
			        Map local = new LinkedHashMap();
			        local.put(bd.getIdDetalle(), bd);
			        Locales.put(bd.getLocal(), local);
			    }
			}
            rs.close();
            stmt.close();
		}catch(SQLException e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": SQLException: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}catch(Exception e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}finally{
			if(conn != null){
			   try{
				  conn.close();
			   }catch (Exception e){
			      logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
				  e.printStackTrace();
			   }
			}
		}
    	return Locales;
    }

	
    /*public Map getSustitutos(String HoraIni, String HoraFin, Map Locales){
		String METHOD_NAME = "getSustitutos";
		Connection conn = null;
		String Query = "";
		//Map Locales = new LinkedHashMap();

		try{
			Calendar Fecha = Calendar.getInstance();
			Fecha.add(Calendar.DAY_OF_MONTH, 1 );
		    
		    ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			Statement stmt = conn.createStatement();
			Query = "SELECT L.NOM_LOCAL LOCAL, INTEGER(SUBSTR(P.ID_CATPROD, 1, 2)) AS CAT_PROD, DP.ID_DETALLE ID_DETALLE, DP.COD_PROD1 COD_PROD1, " 
                  + "       DP.UNI_MED UNI_MED1, DP.DESCRIPCION DESCR1,  DP.CANT_SOLIC, "
                  + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE P.COD_PROD1 END COD_PROD2, "
                  + "       CASE WHEN PIK.ID_PRODUCTO IS NULL THEN '' ELSE P.UNI_MED END UNI_MED2, "
                  + "       PIK.DESCRIPCION DESCR2, SUM(PIK.CANT_PICK) AS CANT_PICK, RD.ID_RONDA "
                  + "FROM BODBA.BO_DETALLE_PICKING PIK " 
                  + "     LEFT JOIN BODBA.BO_DETALLE_PEDIDO DP    ON PIK.ID_DETALLE = DP.ID_DETALLE AND PIK.SUSTITUTO = 'S' " 
                  + "     LEFT JOIN BODBA.BO_DETALLE_RONDAS AS DR ON DR.ID_DETALLE  = DP.ID_DETALLE " 
                  + "     LEFT JOIN BODBA.BO_PRODUCTOS P          ON P.ID_PRODUCTO  = PIK.ID_PRODUCTO " 
                  + "     JOIN BODBA.BO_RONDAS RD                 ON RD.ID_RONDA    = DR.ID_RONDA "
                  + "     JOIN BODBA.BO_SECTORES_LOCAL S          ON S.ID_SECTOR    = DP.ID_SECTOR "
                  + "     JOIN BODBA.BO_LOCALES L                 ON L.ID_LOCAL     = S.ID_LOCAL " 
                  + "     JOIN BODBA.BO_PEDIDOS PED               ON PED.ID_PEDIDO  = DP.ID_PEDIDO AND PED.ORIGEN = 'W' "
                  + "WHERE DATE(RD.FFIN_PICKING) = CURRENT DATE "
                  + "  AND TIME(RD.FFIN_PICKING) BETWEEN TIME('" + HoraIni + "') AND TIME('" + HoraFin + "') "
                  + "  AND INTEGER(DP.COD_PROD1) <> INTEGER(P.COD_PROD1) "
                  + "  AND POSSTR(PIK.DESCRIPCION, '+') = 0 "
                  + "  AND INTEGER(DP.COD_PROD1) NOT IN (SELECT INTEGER(SF.COD_PROD_ORIGINAL) "  
                  + "                                    FROM BODBA.BO_INF_SYF SF "
                  + "                                    WHERE SF.COD_PROD_ORIGINAL = DP.COD_PROD1 "
                  + "                                      AND SF.UME_ORIGINAL      = DP.UNI_MED "
                  + "                                      AND SF.COD_PROD_SUS      = P.COD_PROD1 "
                  + "                                      AND SF.UME_SUS           = P.UNI_MED) "
                  + "GROUP BY L.NOM_LOCAL, P.ID_CATPROD, DP.ID_DETALLE, DP.COD_PROD1, " 
                  + "         DP.UNI_MED, DP.DESCRIPCION, DP.CANT_SOLIC, "
                  + "         PIK.ID_PRODUCTO, P.COD_PROD1, P.UNI_MED, PIK.DESCRIPCION, RD.ID_RONDA "                                       
                  + "ORDER BY L.NOM_LOCAL, CAT_PROD, DP.DESCRIPCION";
			
			
			logger.debug("Query (getSustitutos): " + Query);
			ResultSet rs = stmt.executeQuery(Query);
			while (rs.next()){
			    BeanDetalle bd = new BeanDetalle();
			    bd.setLocal(rs.getString("LOCAL"));
			    bd.setCatProd(rs.getString("CAT_PROD"));
			    bd.setIdDetalle(rs.getString("ID_DETALLE"));
			    bd.setCodProd1(rs.getString("COD_PROD1"));
			    bd.setUniMed1(rs.getString("UNI_MED1"));
			    bd.setDescripcion1(rs.getString("DESCR1"));
			    bd.setCantSolicitada(rs.getString("CANT_SOLIC"));
			    bd.setCodProd2(rs.getString("COD_PROD2"));
			    bd.setUniMed2(rs.getString("UNI_MED2"));
			    bd.setDescripcion2(rs.getString("DESCR2"));
			    bd.setCantPickeadaFaltante(rs.getString("CANT_PICK"));
			    bd.setIdRonda(rs.getString("ID_RONDA"));
			    
			    if (Locales.get(bd.getLocal()) != null){
			        ((LinkedHashMap)Locales.get(bd.getLocal())).put(bd.getIdDetalle(), bd);
			    }else{
			        Map local = new LinkedHashMap();
			        local.put(bd.getIdDetalle(), bd);
			        Locales.put(bd.getLocal(), local);
			    }
			}
            rs.close();
            stmt.close();
		}catch(SQLException e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": SQLException: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}catch(Exception e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}finally{
			if(conn != null){
			   try{
				  conn.close();
			   }catch (Exception e){
			      logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
				  e.printStackTrace();
			   }
			}
		}
    	return Locales;
    }


    public Map getFaltantes(int porcFalt, String HoraIni, String HoraFin, Map Locales){
		String METHOD_NAME = "getFaltantes";
		Connection conn = null;
		String Query = "";
		//Map Locales = new LinkedHashMap();

		try{
		    ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			Statement stmt = conn.createStatement();
			
			Query = "SELECT L.NOM_LOCAL, DP.ID_DETALLE ID_DETALLE, SUBSTR(PROD.ID_CATPROD, 1, 2) AS CAT_PROD, "
                  + "       R.ID_RONDA, DP.COD_PROD1 COD_PROD1, DP.UNI_MED, "
                  + "       DP.DESCRIPCION DESCR, DP.CANT_SOLIC CANT_SOLIC, DP.CANT_FALTAN CANT_FALTAN "
                  + "FROM BODBA.BO_DETALLE_PEDIDO DP "
                  + "     JOIN BODBA.BO_PRODUCTOS PROD    ON PROD.ID_PRODUCTO = DP.ID_PRODUCTO "
                  + "     JOIN BODBA.BO_PEDIDOS P         ON P.ID_PEDIDO   = DP.ID_PEDIDO "
                  + "     JOIN BODBA.BO_DETALLE_RONDAS DR ON DR.ID_DETALLE = DP.ID_DETALLE "
                  + "     JOIN BODBA.BO_RONDAS R          ON R.ID_RONDA    = DR.ID_RONDA "
                  + "     JOIN BODBA.BO_LOCALES L         ON L.ID_LOCAL    = P.ID_LOCAL "
                  + "WHERE DP.CANT_FALTAN > 0 "
                  + "  AND DATE(R.FFIN_PICKING) = CURRENT DATE "
                  + "  AND TIME(R.FFIN_PICKING) BETWEEN TIME('" + HoraIni + "') AND TIME('" + HoraFin + "') "
                  + "ORDER BY L.NOM_LOCAL, CAT_PROD, DP.DESCRIPCION";
			
			logger.debug("Query (getFaltantes): " + Query);
			ResultSet rs = stmt.executeQuery(Query);
			while (rs.next()){
			    boolean agregaReg = true;
			    BeanDetalle bd = new BeanDetalle();
			    bd.setLocal(rs.getString("NOM_LOCAL"));
			    bd.setIdDetalle(rs.getString("ID_DETALLE"));
			    bd.setCatProd(rs.getString("CAT_PROD"));
			    bd.setCodProd1(rs.getString("COD_PROD1"));
			    bd.setUniMed1(rs.getString("UNI_MED"));
			    bd.setDescripcion1(rs.getString("DESCR"));
			    bd.setCantSolicitada(rs.getString("CANT_SOLIC"));
			    bd.setCantPickeadaFaltante(rs.getString("CANT_FALTAN"));
			    bd.setIdRonda(rs.getString("ID_RONDA"));
			    
			    if (bd.getUniMed1().equals("KG")){
			        double MaxCantFaltante = (porcFalt * Double.parseDouble(bd.getCantSolicitada())) / 100;    
				    if (Double.parseDouble(bd.getCantPickeadaFaltante()) < MaxCantFaltante){
				        agregaReg = false;
				    }
			    }
			    
			    if (agregaReg){
				    if (Locales.get(bd.getLocal()) != null){
				        ((LinkedHashMap)Locales.get(bd.getLocal())).put(bd.getIdDetalle(), bd);
				    }else{
				        Map local = new LinkedHashMap();
				        local.put(bd.getIdDetalle(), bd);
				        Locales.put(bd.getLocal(), local);
				    }			        
			    }
			}
            rs.close();
            stmt.close();
		}catch(SQLException e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": SQLException: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}catch(Exception e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}finally{
			if(conn != null){
			   try{
				  conn.close();
			   }catch (Exception e){
			      logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
				  e.printStackTrace();
			   }
			}
		}
    	return Locales;
    }*/

    
    public void GeneraExcel(Map SFLocal, String filename, String RutaImagen, String HoraIni, String HoraFin){
        String METHOD_NAME = "GeneraExcel";
        HSSFWorkbook wb = new HSSFWorkbook();
		SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
		String Fecha = dateFormat.format(new Date());
		try{
            HSSFDataFormat format = wb.createDataFormat();

			
			// Create a new font and alter it.
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short)10);
			font.setFontName("Arial");
			font.setItalic(false);
			//font.setStrikeout(true);

			
			// Create a new font and alter it.
			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short)10);
			font2.setFontName("Arial");
			font2.setItalic(false);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// Create a new font and alter it.
			HSSFFont font4 = wb.createFont();
			font4.setFontHeightInPoints((short)14);
			font4.setFontName("Arial");
			font4.setItalic(false);
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font4.setColor(HSSFColor.GREY_50_PERCENT.index);

			
			// Fonts are set into a style so create a new one to use.
			HSSFCellStyle style = wb.createCellStyle();
			style.setFont(font2);
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style.setFillBackgroundColor(HSSFColor.BLUE.index);
			style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style.setWrapText(true);

			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setFont(font);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			//style2.setFillPattern(HSSFCellStyle.DIAMONDS);
			//style2.setWrapText(true);

			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setFont(font);
			style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style3.setFillForegroundColor(HSSFColor.WHITE.index);
			//style3.setFillPattern(HSSFCellStyle.DIAMONDS);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//style3.setWrapText(true);

			HSSFCellStyle style4 = wb.createCellStyle();
			style4.setFont(font4);
			style4.setBorderTop(HSSFCellStyle.BORDER_NONE);
			style4.setBorderBottom(HSSFCellStyle.BORDER_NONE);
			style4.setBorderRight(HSSFCellStyle.BORDER_NONE);
			style4.setBorderLeft(HSSFCellStyle.BORDER_NONE);
			style4.setFillForegroundColor(HSSFColor.WHITE.index);
			//style4.setFillPattern(HSSFCellStyle.DIAMONDS);
			//style4.setWrapText(true);

			
			//OBTIENE LISTADO DE LOCALES DE SUSTITUTOS Y FALTANTES
			Map locales = new LinkedHashMap();
			for (Iterator it=SFLocal.keySet().iterator(); it.hasNext(); ) {
		        String key = it.next().toString();
		        if (locales.get(key) == null){
		            locales.put(key, key);    
		        }
		        //Object value = Sustitutos.get(key);
		    }

			//OBTIENE LISTADO DE LOCALES DE FALTANTES
			/*for (Iterator it=Faltantes.keySet().iterator(); it.hasNext(); ) {
		        String key = it.next().toString();
		        if (locales.get(key) == null){
		            locales.put(key, key);    
		        }
		        //Object value = Sustitutos.get(key);
		    }*/
			
			for (Iterator it=locales.keySet().iterator(); it.hasNext(); ) {
		        String key = it.next().toString();
		        Map lstSF = null;
		        //Map lstFaltantes = null;
		        //Object value = Sustitutos.get(key);
		        if (SFLocal.get(key) != null){
		            lstSF = (LinkedHashMap)SFLocal.get(key);
		        }
		        /*if (Faltantes.get(key) != null){
		            lstFaltantes = (LinkedHashMap)Faltantes.get(key);
		        }*/

				HSSFSheet sheet = wb.createSheet(key);
				//agrega imagen
	    	    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	    	    HSSFClientAnchor anchor;
	    	    anchor = new HSSFClientAnchor(0,0,0,0,(short)4,0,(short)5,7);
	    	    anchor.setAnchorType(2);
	    	    patriarch.createPicture(anchor, loadPicture( RutaImagen, wb ));

			
				HSSFRow row8 = sheet.createRow((short) 8);
				HSSFCell cell_8_4 = row8.createCell((short) 4);
				cell_8_4.setCellValue(new HSSFRichTextString("Local (" + key + ") | Fecha (" + Fecha + ") | Jornada (" + HoraIni + " a " + HoraFin + ")"));
				cell_8_4.setCellStyle(style4);

	    	    
				
				HSSFRow row0 = sheet.createRow((short) 10);

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
				cell_0_3.setCellValue(new HSSFRichTextString("N° RONDA"));
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
				cell_0_9.setCellValue(new HSSFRichTextString("DESCRIPCION SUSTITUTO (SI ESTA EN BLANCO NO SE SUSTITUYO)"));
				cell_0_9.setCellStyle(style);

				HSSFCell cell_0_10 = row0.createCell((short) 10);
				cell_0_10.setCellValue(new HSSFRichTextString("MOTIVO FALTANTE"));
				cell_0_10.setCellStyle(style);

				HSSFCell cell_0_11 = row0.createCell((short) 11);
				cell_0_11.setCellValue(new HSSFRichTextString("Nº DE ORDEN"));
				cell_0_11.setCellStyle(style);

				HSSFCell cell_0_12 = row0.createCell((short) 12);
				cell_0_12.setCellValue(new HSSFRichTextString("OBSERVACION"));
				cell_0_12.setCellStyle(style);

				int j = 10;
				if(lstSF != null){
				    for (Iterator its=lstSF.keySet().iterator(); its.hasNext(); ){
				        String key1 = its.next().toString();
					    j++;
					    HSSFRow row = sheet.createRow((short) j);

                        BeanDetalle bd = (BeanDetalle)lstSF.get(key1);

                        //BeanSustitutos bs = (BeanSustitutos)lstSF.get(keys);
						// Create a cell and put a value in it.
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
						
						HSSFCell cell_12 = row.createCell((short) 12);
						cell_12.setCellValue(new HSSFRichTextString(" "));
						cell_12.setCellStyle(style2);
				    }
				}
				
				/*if(lstFaltantes != null){
				    for (Iterator itf=lstFaltantes.keySet().iterator(); itf.hasNext(); ){
				        String keyf = itf.next().toString();
				        BeanFaltantes bf = (BeanFaltantes)lstFaltantes.get(keyf);

					    j++;
					    HSSFRow row = sheet.createRow((short) j);
					    
						// Create a cell and put a value in it.
						HSSFCell cell_0 = row.createCell((short) 0);
						cell_0.setCellValue(Double.parseDouble(bf.getCatProd()));
						cell_0.setCellStyle(style2);

						HSSFCell cell_1 = row.createCell((short) 1);
						cell_1.setCellValue(new HSSFRichTextString(bf.getCodProd1()));
						cell_1.setCellStyle(style2);

						HSSFCell cell_2 = row.createCell((short) 2);
						cell_2.setCellValue(new HSSFRichTextString(bf.getUniMed()));
						cell_2.setCellStyle(style2);

						HSSFCell cell_3 = row.createCell((short) 3);
						cell_3.setCellValue(Double.parseDouble(bf.getIdRonda()));
						cell_3.setCellStyle(style3);
						
						HSSFCell cell_4 = row.createCell((short) 4);
						cell_4.setCellValue(new HSSFRichTextString(bf.getDescripcion()));
						cell_4.setCellStyle(style2);
						
						HSSFCell cell_5 = row.createCell((short) 5);
						cell_5.setCellValue(Double.parseDouble(bf.getCantSolicitada()));
						cell_5.setCellStyle(style3);

						HSSFCell cell_6 = row.createCell((short) 6);
						cell_6.setCellValue(Double.parseDouble(bf.getCantFaltante()));
						cell_6.setCellStyle(style3);

						HSSFCell cell_7 = row.createCell((short) 7);
						cell_7.setCellValue(new HSSFRichTextString(" "));
						cell_7.setCellStyle(style2);

						HSSFCell cell_8 = row.createCell((short) 8);
						cell_8.setCellValue(new HSSFRichTextString(" "));
						cell_8.setCellStyle(style2);
						
						HSSFCell cell_9 = row.createCell((short) 9);
						cell_9.setCellValue(new HSSFRichTextString(" "));
						cell_9.setCellStyle(style2);
						
						HSSFCell cell_10 = row.createCell((short) 10);
						cell_10.setCellValue(new HSSFRichTextString(" "));
						cell_10.setCellStyle(style2);
						
						HSSFCell cell_11 = row.createCell((short) 11);
						cell_11.setCellValue(new HSSFRichTextString(" "));
						cell_11.setCellStyle(style2);
						
						HSSFCell cell_12 = row.createCell((short) 12);
						cell_12.setCellValue(new HSSFRichTextString(" "));
						cell_12.setCellStyle(style2);
                    }
				}*/
				sheet.autoSizeColumn((short)0);   //SECCION
				sheet.autoSizeColumn((short)1);   //SAP
				sheet.autoSizeColumn((short)2);   //UNIDAD DE MEDIDA
				sheet.autoSizeColumn((short)3);   //N° RONDA
				sheet.autoSizeColumn((short)4);   //DESCRIPCION PRODUCTO
				sheet.autoSizeColumn((short)5);   //CANT. SOLICITADA
				sheet.autoSizeColumn((short)6);   //CANT. FALTANTE / CANT. PICKEADA
				sheet.autoSizeColumn((short)7);   //SAP SUS
				sheet.autoSizeColumn((short)8);   //UME SUS
				sheet.autoSizeColumn((short)9);   //DESCRIPCION SUSTITUTO (SI ESTA EN BLANCO NO SE SUSTITUYO)
				sheet.autoSizeColumn((short)10);  //MOTIVO FALTANTE
				sheet.autoSizeColumn((short)11);  //Nº DE ORDEN
				sheet.setColumnWidth((short)12, (short)20000);  //OBSERVACION
		    }
			FileOutputStream fos = new FileOutputStream(filename);
			wb.write(fos);
			fos.close();
		}catch(Exception e){
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
		    e.printStackTrace();
		}
    }
    
    
	public void CreaZipXLS(String RutaArchivo, String filename){
		String METHOD_NAME = "CreaZipXML";
		try {
			File fd = new File(RutaArchivo + filename);
			java.io.FileInputStream fis = new java.io.FileInputStream(fd);
    	
			// These are the files to include in the ZIP file
			String[] filenames = new String[]{filename};
    
			// Create a buffer for reading the files
			byte[] buf = new byte[1024];
			
			// Create the ZIP file
			String outFilename = RutaArchivo + filename.substring(0, filename.indexOf('.')) + ".zip";//"outfile.zip";
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
    
			// Compress the files
			for (int i=0; i<filenames.length; i++) {
				//java.io.FileInputStream in = new java.io.FileInputStream(filenames[i]);
    
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(filenames[i]));
    
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = fis.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				fis.close();
			}
			// Complete the ZIP file
			out.close();
		} catch (IOException e) {
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
			e.printStackTrace();
		}
	}

	
    private static int loadPicture( String path, HSSFWorkbook wb ) throws IOException{
        int pictureIndex;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try{
            fis = new FileInputStream( path);
            bos = new ByteArrayOutputStream( );
            int c;
            while ( (c = fis.read()) != -1)
                bos.write( c );
            pictureIndex = wb.addPicture( bos.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG );
        }finally{
            if (fis != null)
                fis.close();
            if (bos != null)
                bos.close();
        }
        return pictureIndex;
    }

	
	public void EliminaArchivosXLS(String Path){
	    //ecupera listado de archivo y subdirectorios de un Directorio
	    File dir = new File("temp");
	    
	    String[] children = dir.list();
	    if (children == null) {
	        // Either dir does not exist or is not a directory
	        logger.debug("El directorio 'temp' NO Existe o NO ES un Directorio");
	    } else {
	        for (int i=0; i<children.length; i++) {
	            // Get filename of file or directory
	            String filename = children[i];
	            
	            String Ext = filename.substring(filename.indexOf('.'));
                
	            //Verifica si existe un archivo o directorio
	    	    boolean exists = (new File(Path + filename)).exists();
	    	    if (exists) {
	    	        // File or directory exists
                    
	    	        //elimina un archivo
	    		    boolean success = (new File(Path + filename)).delete();
	    		    if (success) {
	    		        logger.debug("El Archivo '" + filename + "' fue Eliminada con Exito");
	    		    }
	    	    } else {
	    	        // File or directory does not exist
	    	        logger.debug("El Archivo '" + filename + "' NO Existe.");
	    	    }
	        }
	    }
	}
    
	
	public void EnviaEMail(BeanParamConfig param, String Path, String NombreArchivo){
	    String METHOD_NAME = "EnviaEMail";
        try{
            Properties prop =System.getProperties();
            prop.put("mail.smtp.host", param.getServer());
            prop.put("mail.smtp.port", param.getPuerto());
            Session ses1  = Session.getDefaultInstance(prop,null);
            MimeMessage msg = new MimeMessage(ses1);
            msg.setFrom(new InternetAddress(param.getFrom()));
            
            for (Iterator it=param.getTo().keySet().iterator(); it.hasNext(); ){
		        String key = it.next().toString();
                msg.addRecipient(Message.RecipientType.TO,new InternetAddress(key));
            }
        
            for (Iterator it=param.getCc().keySet().iterator(); it.hasNext(); ){
		        String key = it.next().toString();
                msg.addRecipient(Message.RecipientType.CC,new InternetAddress(key));
            }

            for (Iterator it=param.getCco().keySet().iterator(); it.hasNext(); ){
		        String key = it.next().toString();
                msg.addRecipient(Message.RecipientType.BCC,new InternetAddress(key));
            }

            msg.setSubject(param.getSubject());

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(param.getMensaje());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            // File1
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(Path + NombreArchivo);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(NombreArchivo);
            multipart.addBodyPart(messageBodyPart);
            
            msg.setContent(multipart);
            msg.setSentDate(new Date());
            Transport.send(msg);
            
        }catch(MessagingException e){
            logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
            e.printStackTrace();
        }
	}
}
