package cl.cencosud.procesos.chaordic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cl.cencosud.procesos.chaordic.dto.ConfigDTO;
import cl.cencosud.procesos.chaordic.dto.ProductosChaordicDTO;
import cl.cencosud.procesos.chaordic.log.Logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
/**
 * @author 
 * 
 */
public class ChaordicDAO {

	private Logging logger = new Logging(this);

    /**
     * Entrega una nueva conexion
     * 
     * @param conf
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public Connection generaConeccion(ConfigDTO conf) throws SQLException, Exception {

        Connection con = null;
        try {
            Class.forName(conf.getDriver());
            //logger.debug("Driver: " + conf.getDriver());
            StringBuffer sb = new StringBuffer();
            sb.append("jdbc:db2://");
            sb.append(conf.getIp());
            sb.append(":");
            sb.append(conf.getPuerto());
            sb.append("/");
            sb.append(conf.getBaseDatos());
            //logger.debug("url de coneccion: " + sb.toString());
            //System.out.println(sb.toString());
            con = DriverManager.getConnection(sb.toString(), conf.getUsuario(), conf.getPass());

        } catch (Exception e) {
            logger.error("error al generar la conexion de base de datos ", e);
            throw e;
        }

        return con;
    }
    
	public List getExtraeProductosChaordic(Connection con, String idLocal) {
		PreparedStatement stm = null;
		ResultSet rs = null;
		List listaProductosChaordic = new ArrayList();
		StringBuffer sql = new StringBuffer();

		try {
			sql.append("   SELECT DISTINCT ");
			sql.append("          FP.PRO_ID AS id, ");
			sql.append("          FP.PRO_TIPO_PRODUCTO AS title, ");
			sql.append("          TRIM(NVL(FP.PRO_DES_CORTA,'')) AS description, ");
			sql.append("          TRIM(CAT2.CAT_NOMBRE)||' > '||TRIM(CAT1.CAT_NOMBRE) ");
			sql.append("          ||' > '||TRIM(CAT.CAT_NOMBRE) AS product_type, ");
			sql.append("          'http://www.jumbo.cl' AS link, ");
			sql.append("          FP.PRO_IMAGEN_FICHA AS image_link, ");
			sql.append("          decode(locate('ANTES $',UPPER(PROMO.DESCR)),0,'', ");
			sql.append("				trim(replace(substr(nvl(PROMO.DESCR,''), ");
			sql.append("          		locate('$',PROMO.DESCR)+1),'.',''))) ");
			sql.append("          		AS sale_price, ");
			sql.append("          PRLOC.PRE_VALOR AS price, ");
			sql.append("          decode(PRLOC.PRE_TIENESTOCK, ");
			sql.append("  			   1,'in stock', ");
			sql.append("  			   'out of stock') AS availability, ");
			sql.append("          nvl(MAR.MAR_NOMBRE,'') AS brand, ");
			sql.append("          FP.PRO_INTER_VALOR AS custom_label_0, ");
			sql.append("          FP.PRO_INTER_MAX AS custom_label_1, ");
			sql.append("          FP.PRO_INTER_VALOR AS custom_label_2, ");
			sql.append("  		  DECODE(FP.PRO_TIPRE, ");
			sql.append("					'ST', 'Und', ");
			sql.append("					'KG', 'Kg', ");
			sql.append("					'CS', 'Und', ");
			sql.append("				 	'L',  'Und', ");
			sql.append("					'M',  'Und', ");
			sql.append("					'M2', 'Und', ");
			sql.append("					'PAK','Und', ");
			sql.append("					'BAG','Und', ");
			sql.append("					' ') AS custom_label_3, ");
			sql.append("          '$@'||(CAST(DECODE(FP.PRO_UNIDAD_MEDIDA, 0,0, ");
			sql.append("          ROUND(MED.UNI_CANTIDAD * PRLOC.PRE_VALOR / ");
			sql.append("				FP.PRO_UNIDAD_MEDIDA)) AS INT)||'@ x ' ");
			sql.append("                ||MED.UNI_DESC) AS custom_label_4, ");
			sql.append("          NVL(PROMO.DESCR,'') AS custom_label_5, ");
			sql.append("          NVL(PROMO.BANNER,'') AS custom_label_6, ");
			sql.append("          'cab='||CAT2.CAT_ID|| ");
			sql.append("          '&int='||CAT1.CAT_ID|| ");
			sql.append("          '&ter='||CAT.CAT_ID AS custom_label_7 ");
			sql.append("     FROM BODBA.BO_PRODUCTOS AS BP ");
			sql.append("     JOIN FODBA.FO_PRODUCTOS AS FP ");
			sql.append("	   ON BP.COD_PROD1 = FP.PRO_COD_SAP ");
			sql.append("  	  AND FP.PRO_ESTADO = 'A' ");
			sql.append("     JOIN FODBA.FO_PRODUCTOS_CATEGORIAS AS PRCAT ");
			sql.append("  	   ON PRCAT.PRCA_PRO_ID = FP.PRO_ID ");
			sql.append("     JOIN FODBA.FO_CATEGORIAS AS CAT ");
			sql.append("  	   ON PRCAT.PRCA_CAT_ID = CAT.CAT_ID ");
			sql.append("  	  AND CAT.CAT_ESTADO = 'A' ");
			sql.append("     JOIN FODBA.FO_CATSUBCAT AS SUBCAT ");
			sql.append("  	   ON PRCAT.PRCA_CAT_ID = SUBCAT.SUBCAT_ID ");
			sql.append("     JOIN FODBA.FO_CATEGORIAS AS CAT1 ");
			sql.append("  	   ON CAT1.CAT_ID = SUBCAT.CAT_ID ");
			sql.append("  	  AND CAT1.CAT_ESTADO = 'A' ");
			sql.append("     JOIN FODBA.FO_CATSUBCAT AS SCAT1 ");
			sql.append("  	   ON CAT1.CAT_ID = SCAT1.SUBCAT_ID ");
			sql.append("     JOIN FODBA.FO_CATEGORIAS AS CAT2 ");
			sql.append("  	   ON CAT2.CAT_ID = SCAT1.CAT_ID ");
			sql.append("  	  AND CAT2.CAT_ESTADO = 'A' ");
			sql.append("     JOIN FODBA.FO_PRECIOS_LOCALES AS PRLOC ");
			sql.append("  	   ON FP.PRO_ID = PRLOC.PRE_PRO_ID ");
			sql.append("      AND PRLOC.PRE_ESTADO = 'A' ");
			sql.append("      AND PRLOC.PRE_LOC_ID = " + idLocal + " ");
			sql.append("     JOIN FODBA.FO_MARCAS AS MAR ");
			sql.append("  	   ON FP.PRO_MAR_ID = MAR.MAR_ID ");
			sql.append("     JOIN FODBA.FO_UNIDADES_MEDIDA AS MED ");
			sql.append("  	   ON MED.UNI_ID = FP.PRO_UNI_ID ");
			sql.append("     LEFT OUTER JOIN ");
			sql.append("          (SELECT DISTINCT ");
			sql.append("				  DESCR, ");
			sql.append("		          BANNER, ");
			sql.append("                  ID_PRODUCTO");
			sql.append("             FROM ( ");
			sql.append("                   SELECT DISTINCT ");
			sql.append("						  PRM.DESCR, ");
			sql.append("  			              PRM.BANNER, ");
			sql.append("                          PPR.ID_PRODUCTO ");
			sql.append("                     FROM BODBA.PR_PROMOCION AS PRM ");
			sql.append("                     JOIN BODBA.PR_PRODUCTO_PROMOS AS PPR ");
			sql.append("  			           ON (PRM.COD_PROMO = PPR.COD_PROMO1 ");
			sql.append("  					  AND PPR.COD_PROMO1 != 0 ");
			sql.append("     		          AND CURRENT TIMESTAMP BETWEEN ");
			sql.append("  			              PRM.FINI AND PRM.FFIN) ");
			sql.append("                    WHERE PPR.ID_LOCAL = " + idLocal + " ");
			sql.append("                    UNION ALL ");
			sql.append("                   SELECT DISTINCT ");
			sql.append("						  PRM.DESCR, ");
			sql.append("  			              PRM.BANNER, ");
			sql.append("  			              PPR.ID_PRODUCTO ");
			sql.append("                     FROM BODBA.PR_PROMOCION AS PRM ");
			sql.append("                     JOIN BODBA.PR_PRODUCTO_PROMOS AS PPR ");
			sql.append("  			           ON (PRM.COD_PROMO = PPR.COD_PROMO2 ");
			sql.append("  					  AND PPR.COD_PROMO2 != 0 ");
			sql.append("  					  AND CURRENT TIMESTAMP BETWEEN ");
			sql.append("  					      PRM.FINI AND PRM.FFIN) ");
			sql.append("                    WHERE PPR.ID_LOCAL = " + idLocal + " ");
			sql.append("                    UNION ALL ");
			sql.append("                   SELECT DISTINCT ");
			sql.append("						  PRM.DESCR, ");
			sql.append("  			              PRM.BANNER, ");
			sql.append("  			              PPR.ID_PRODUCTO ");
			sql.append("                     FROM BODBA.PR_PROMOCION AS PRM ");
			sql.append("                     JOIN BODBA.PR_PRODUCTO_PROMOS AS PPR ");
			sql.append("  			           ON (PRM.COD_PROMO = PPR.COD_PROMO3 ");
			sql.append("  					  AND PPR.COD_PROMO3 != 0 ");
			sql.append("  					  AND CURRENT TIMESTAMP BETWEEN ");
			sql.append("  					      PRM.FINI AND PRM.FFIN) ");
			sql.append("                    WHERE PPR.ID_LOCAL = " + idLocal + " ");
			sql.append("                  ) ");
			sql.append("          UNION ");
			sql.append("              SELECT NVL(PPR.PRM_DESC_BANNER,'') AS DESCR, ");
			sql.append("                     NVL(PPR.PRM_NOM_BANNER,'') AS BANNER, ");
			sql.append("                     BPR.ID_PRODUCTO ");
			sql.append("                FROM FODBA.FO_PRODUCTOS_PROMO AS PPR ");
			sql.append("                JOIN FODBA.FO_PRODUCTOS AS FPR ");
			sql.append("				  ON PPR.PRM_PROD_ID = FPR.PRO_ID ");
			sql.append("                JOIN BODBA.BO_PRODUCTOS AS BPR ");
			sql.append("				  ON FPR.PRO_COD_SAP = BPR.COD_PROD1 ");
			sql.append("          )  AS PROMO ");
			sql.append("          ON BP.ID_PRODUCTO = PROMO.ID_PRODUCTO ");
			sql.append("  ORDER BY 1,2 ");
			sql.append("  WITH UR ");
			
			stm = con.prepareStatement(sql.toString());

			rs = stm.executeQuery();
			
			while (rs.next()) {	
				
				ProductosChaordicDTO proChaordic = new ProductosChaordicDTO();
				
				proChaordic.setId(rs.getLong("id"));
				proChaordic.setTitle(rs.getString("title"));
				proChaordic.setDescription(rs.getString("description"));
				proChaordic.setProduct_type(rs.getString("product_type"));
				proChaordic.setLink(rs.getString("link"));
				proChaordic.setImage_link(rs.getString("image_link"));
				double price = rs.getDouble("price");
				proChaordic.setPrice(price);
				String texto = rs.getString("sale_price");
				texto = recuperaMonto(texto);

				if(texto.length() > 0){
					double sale_price = Double.parseDouble(texto);
					if(price < sale_price){
						proChaordic.setSale_price(price);
						proChaordic.setPrice(sale_price);
					}else{
						proChaordic.setSale_price(sale_price);
					}
				}
				proChaordic.setAvailability(rs.getString("availability"));
				proChaordic.setBrand(rs.getString("brand"));
				proChaordic.setCustom_label_0(rs.getString("custom_label_0"));
				proChaordic.setCustom_label_1(rs.getString("custom_label_1"));
				proChaordic.setCustom_label_2(rs.getString("custom_label_2"));
				proChaordic.setCustom_label_3(rs.getString("custom_label_3"));
				proChaordic.setCustom_label_4(formateaMontoEnCadena(rs.getString("custom_label_4")));
				proChaordic.setCustom_label_5(rs.getString("custom_label_5"));
				proChaordic.setCustom_label_6(rs.getString("custom_label_6"));
				proChaordic.setCustom_label_7(rs.getString("custom_label_7"));
				
				listaProductosChaordic.add(proChaordic);
			}
			
			rs.close();
			rs = null;
			stm.close();
			stm = null;
			
		}catch (SQLException e) {
           	logger.error(" SQL Error " + e.getMessage());
		}catch(Exception e){
			logger.error(" Error " + e.getMessage());
		}finally {
            try {
            	if (rs != null){
            		rs.close();
            	}
                if (stm != null){
                	stm.close();
                }
            } catch (SQLException e) {
            	logger.error("Error al cerrar Statement " + e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Proceso ok");
		
		return listaProductosChaordic;		
	}

	public void CreaZip(String RutaArchivo, String filename){
		String METHOD_NAME = "CreaZip";
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
		} catch (Exception e) {
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
			e.printStackTrace();
		}
	}
	
	public void getExtraeArchivoXML(List listaProdChaordic,String rutArchivo) throws IOException, Exception {

		BufferedWriter buffWriter = null;
		
		OutputStreamWriter osw = null;
		
		int i =0;

		try{

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date());
						
			File fileDir=new File(rutArchivo+"/file.xml");
			
			osw = new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8");
			buffWriter = new BufferedWriter(osw);
			
			StringBuffer out = new StringBuffer();
			
			out.append("<?xml version='1.0' encoding='UTF-8' standalone='yes'?> \n");
	
			out.append("<rss xmlns:date='http://exslt.org/dates-and-times' xmlns:g='http://base.google.com/ns/1.0' version='2.0'> \n");
			
			out.append("<channel> \n");
			
			out.append("<title>Jumbo Chile</title> \n");
			
			out.append("<link>http://www.jumbo.cl</link> \n");		
	
			out.append("<last_build_date>" + timeStamp + "</last_build_date> \n");
			
			long cod_prod = 0; // variable para almacenar código de producto
			long cod_prod_ant = 0; // variable para diferenciar productos
			
			StringBuffer linea_top = new StringBuffer();
			StringBuffer linea_mid = new StringBuffer();
			StringBuffer linea_bot = new StringBuffer();
			
			int cantRegistros = listaProdChaordic.size();
	
			for ( i = 0; i < listaProdChaordic.size() ; i++ ){
				
				ProductosChaordicDTO pc = new ProductosChaordicDTO();
				pc = (ProductosChaordicDTO)listaProdChaordic.get(i);
				
				cod_prod = pc.getId();
				
				if(cod_prod != cod_prod_ant){
					if(cod_prod_ant != 0){
						// escribo el producto anterior
						out.append(linea_top.toString());
						out.append(linea_mid.toString());
						out.append(linea_bot.toString());
						
						// limpio las variables
						linea_top = new StringBuffer();
						linea_mid = new StringBuffer();
						linea_bot = new StringBuffer();
					}
					
					linea_top.append("<item> \n");
	
					linea_top.append("<g:id>" + String.valueOf(pc.getId()) + "</g:id> \n");
				
					linea_top.append("<title><![CDATA[" + pc.getTitle() + "]]></title> \n");
				
					linea_top.append("<description><![CDATA[" + pc.getDescription() + "]]></description> \n");
				}
				
				linea_mid.append("<g:product_type><![CDATA[" + pc.getProduct_type() + "]]></g:product_type> \n");
				
				if(cod_prod != cod_prod_ant){
					linea_bot.append("<link>http://www.jumbo.cl</link> \n");
					
					linea_bot.append("<g:image_link>http://www.jumbo.cl/FO_IMGS/gr/" + pc.getImage_link()+ "</g:image_link> \n");
					
					//si precio anterior está definido
					Double dObj = new Double(pc.getSale_price());
					if ( dObj != null && dObj.doubleValue() != 0.0){
						linea_bot.append("<g:sale_price>" + String.valueOf(pc.getSale_price()) + "</g:sale_price> \n");
					}
		
					linea_bot.append("<g:price>" + String.valueOf(pc.getPrice()) + "</g:price> \n");
		
					linea_bot.append("<g:availability>" + pc.getAvailability() + "</g:availability> \n");
		
					linea_bot.append("<g:brand><![CDATA[" + pc.getBrand() + "]]></g:brand> \n");
					
					linea_bot.append("<g:custom_label_0>" + pc.getCustom_label_0() + "</g:custom_label_0> \n");
		
					linea_bot.append("<g:custom_label_1>" + pc.getCustom_label_1() + "</g:custom_label_1> \n");
		
					linea_bot.append("<g:custom_label_2>" + pc.getCustom_label_2() + "</g:custom_label_2> \n");
					
					linea_bot.append("<g:custom_label_3>" + pc.getCustom_label_3() + "</g:custom_label_3> \n");
					
					linea_bot.append("<g:custom_label_4>" + pc.getCustom_label_4() + "</g:custom_label_4> \n");
					
					if ( !("".equals(pc.getCustom_label_5()))){
						linea_bot.append("<g:custom_label_5><![CDATA[" + pc.getCustom_label_5() + "]]></g:custom_label_5> \n");
					}
					
					if ( !("".equals(pc.getCustom_label_6()))){
						linea_bot.append("<g:custom_label_6>http://www.jumbo.cl/FO_IMGS/img/promociones/" + pc.getCustom_label_6() + "</g:custom_label_6> \n");
					}
					if ( !("".equals(pc.getCustom_label_7()))){
						linea_bot.append("<g:custom_label_7><![CDATA[http://www.jumbo.cl/FO/CategoryDisplay?" + pc.getCustom_label_7() + "]]></g:custom_label_7> \n");
					}
					linea_bot.append("</item> \n");
				}
				
				cod_prod_ant = cod_prod;
			}
			// cargo último producto
			out.append(linea_top.toString());
			out.append(linea_mid.toString());
			out.append(linea_bot.toString());
	
			out.append("</channel>");
			out.append("\n");
	
		    out.append("</rss>");
		    
			buffWriter.write("" + out.toString());

		    //this.CreaZip(rutArchivo, "file.xml");
		    
		    logger.info("Archivo feed.xml generado");
		    
			generaArchivoResumen(cantRegistros, rutArchivo);
		   
		} catch (IOException e) {
			logger.error("ERROR IO " + e);
			throw new Exception(e);
		} catch (Exception e) {
			logger.error("ERROR " + e);
			throw new Exception(e);
		} finally {
			if(buffWriter != null)
				buffWriter.close();
			if(osw != null)
				osw.close();
		}
	  return ;
	}
	
	public void generaArchivoResumen(int cantRegistros, String rutArchivo) throws IOException, Exception {

		BufferedWriter buffWriter = null;
		OutputStreamWriter osw = null;

		try{

			String timeStamp = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date());
						
			File fileDir=new File(rutArchivo+"/resumen.txt");
			
			osw = new OutputStreamWriter(new FileOutputStream(fileDir), "UTF8");
			buffWriter = new BufferedWriter(osw);
			
			StringBuffer out = new StringBuffer();
			
			out.append("Última generación de archivo feed.xml con fecha " + timeStamp + " \n");
			out.append(" \n");
			out.append("Total de productos informados: " + cantRegistros + " \n");
			out.append(" \n");
			buffWriter.write("" + out.toString());
		    
		    logger.info("Archivo resumen generado");
		   
		} catch (IOException e) {
			logger.error("ERROR IO " + e);
			throw new Exception(e);
		} catch (Exception e) {
			logger.error("ERROR " + e);
			throw new Exception(e);
		} finally {
			if(buffWriter != null)
				buffWriter.close();
			if(osw != null)
				osw.close();
		}
	  return ;
	}
	
	public String recuperaMonto(String cadena){
		String retorno = "";
		if(null != cadena && cadena.length() > 0){
			for(int i = 0 ; i < cadena.length() ; i++){
				char letra = cadena.charAt(i);
				int codigo = (int)cadena.charAt(i);
				int siguiente = 0;
				if(i < cadena.length()-1){
					siguiente = (int)cadena.charAt(i+1);
				}
				if(codigo >= 48 && codigo <= 57){
					retorno += letra;
					if(siguiente < 48 || siguiente > 57){
						break;
					}
				}
			}
		}
		return retorno;
	}

	
	/**
	 * Toma el monto enviado por parámetro y lo formatea como número, lo
	 * devuelve como un string que incluye el monto con los puntos de mil
	 * correspondientes
	 * 
	 * @param value
	 *            String con el monto a formatear
	 * @return String con monto en formato 9.999.999
	 * @throws Exception
	 */
	static public String aplicaFormato(String value) throws Exception {
		String retorno = "";
		try {
			NumberFormat.getInstance();
			NumberFormat formateo = NumberFormat.getNumberInstance();

			retorno = formateo.format(Long.parseLong(value));
			return retorno;
		} catch (Exception e) {
			throw new Exception("GeneraArchivo.aplicaFormato Error al aplicar formato ", e);
		}
	}
	
	/**
	 * Busca los montos delimitados con '@' (ej. '@9999999@') dentro del String
	 * enviado como parámetro y reemplaza estos montos con los montos
	 * formateados ($9.999.999), la demás información no se modifica, devuelve
	 * el nuevo String resultante
	 * 
	 * @param line
	 *            String con los montos a formatear
	 * @return String con montos en formato $9.999.999
	 * @throws Exception
	 */
	static public String formateaMontoEnCadena(String line) throws Exception {

		String strLeft = ""; // the substring before '@'
		String amount = ""; // the number between '@'s, string to be formatted
		String strRight = ""; // the substring after '@'
		int leftIndex = 0; // position of '@' character in line parameter
		int rightIndex = 0; // position of a second '@' character in line
							// parameter

		try {
			for (int index = 0; index < line.length(); index++) {

				if (line.indexOf("@") != -1) {
					strLeft = line.substring(leftIndex, line.indexOf("@"));

					leftIndex = line.indexOf('@');

					strRight = line.substring(leftIndex + 1);

					String amountStr = strRight.substring(0,
							strRight.indexOf("@"));
					amount = aplicaFormato(amountStr);

					rightIndex = line.substring(leftIndex + 1).indexOf('@');

					strRight = strRight.substring(rightIndex + 1);

					line = strLeft + amount + strRight;

					leftIndex = 0;
					rightIndex = 0;

				}

			}
			return line;
		} catch (Exception e) {
			throw new Exception("GeneraArchivo.formateaMontoEnCadena Error al formatear monto ",e);
		}
	}
}

