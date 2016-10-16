package cl.cencosud.jumbo.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cl.cencosud.jumbo.beans.BeanLocal;
import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.beans.PedidoDesp;
import cl.cencosud.jumbo.conexion.ConexionUtil;
import cl.cencosud.jumbo.log.Logging;

public class Home {

    private static Home ch = null;
    private Logging logger = new Logging(this);
    private int numFila = 0;
    private int numFila2 = 0;
    private int TotalVecesEnRuta = 0;

    public static Home getHome() {

        if ( ch == null ) {
            ch = new Home();
        }
        return ch;
    }

    public List getListadoOP( Calendar cal, int id_local ) {

        SimpleDateFormat dateFormatIni = new SimpleDateFormat("yyyy-MM-");
        SimpleDateFormat dateFormatFin = new SimpleDateFormat("yyyy-MM-dd");
        String METHOD_NAME = "getCostoDespacho";
        String SQL = "";
        String FechaIni = "";
        String FechaFin = "";
        Connection conn = null;
        List lstPedidos = null;
        Statement stm = null;
        ResultSet rs = null;

        this.TotalVecesEnRuta = 0;
        String CostoDespachoOP = "";

        int diasMes = cal.getActualMaximum(5);
        int dia = cal.get(5);

        if ( dia == diasMes ) {
            if ( ( id_local == 2 ) || ( id_local == 3 ) ) {
                FechaIni = dateFormatIni.format(cal.getTime()) + "16";
            } else {
                FechaIni = dateFormatIni.format(cal.getTime()) + "01";
            }
            FechaFin = dateFormatFin.format(cal.getTime());
        } else if ( ( dia == 15 ) && ( ( id_local == 2 ) || ( id_local == 3 ) ) ) {
            FechaIni = dateFormatIni.format(cal.getTime()) + "01";
            FechaFin = dateFormatFin.format(cal.getTime());
        }

        if ( ( !FechaIni.equals("") ) && ( !FechaFin.equals("") ) ) {
            switch ( id_local ) {
                case 1:
                    CostoDespachoOP = "      ,CASE WHEN (COALESCE(BINS.CANTIDAD, 0) < 13) THEN (7021*PE.VECES_EN_RUTA)             ELSE ((7021 + (COALESCE(BINS.CANTIDAD, 0) - 12)* 420)*PE.VECES_EN_RUTA)        END AS COSTO_DESPACHO ";

                    break;
                case 2:
                    CostoDespachoOP = "      ,CASE WHEN (Z.ID_ZONA IN (101, 182)) THEN (8400*PE.VECES_EN_RUTA)             ELSE (6400*PE.VECES_EN_RUTA)        END AS COSTO_DESPACHO ";

                    break;
                case 3:
                    CostoDespachoOP = "      ,CASE WHEN (Z.ID_ZONA = 81) THEN (2000*PE.VECES_EN_RUTA)             WHEN (Z.ID_ZONA = 82) THEN (4000*PE.VECES_EN_RUTA)             WHEN (Z.ID_ZONA IN (83, 202, 364)) THEN (7000*PE.VECES_EN_RUTA)        END AS COSTO_DESPACHO ";
            }

            try {
                ConexionUtil cu = new ConexionUtil();
                conn = cu.getConexion();

                SQL = "SELECT Z.ID_LOCAL       ,JD.FECHA       ,Z.ID_ZONA       ,Z.NOMBRE AS NOM_ZONA       ,C.ID_COMUNA       ,C.NOMBRE AS NOM_COMUNA       ,COALESCE(PE.ID_RUTA, 0) AS ID_RUTA       ,ER.ID_ESTADO AS ID_ESTADO_RUTA       ,ER.NOMBRE AS NOM_ESTADO_RUTA       ,P.ID_PEDIDO       ,P.ID_ESTADO AS ID_ESTADO_PEDIDO       ,E.NOMBRE AS NOM_ESTADO_PEDIDO       ,CASE WHEN P.ORIGEN = 'A' THEN CONCAT('JV', CHAR(PE.NRO_GUIA_CASO))             WHEN P.ORIGEN = 'C' THEN CONCAT('C', CHAR(PE.NRO_GUIA_CASO))             ELSE ' '        END AS JUMBO_VA       ,COALESCE(BINS.CANTIDAD, 0) AS CANT_BINS       ,PE.VECES_EN_RUTA       ,CASE WHEN (PE.CUMPLIMIENTO = 'T') THEN 'A TIEMPO'             WHEN (PE.CUMPLIMIENTO = 'R') THEN 'RETRASADO'             WHEN (PE.CUMPLIMIENTO = 'A') THEN 'ADELANTADO'             ELSE '---'        END AS ESTATUS_ENTREGA       ,COALESCE(RD.ID_RESPONSABLE_DESP, 0) AS ID_RESP_INCUMPLIMIENTO       ,COALESCE(RD.NOMBRE_RESPONSABLE,'') AS RESP_INCUMPLIMIENTO       ,COALESCE(MD.MOTIVO, '') AS MOTIVO_INCUMPLIMIENTO "
                        + CostoDespachoOP
                        + "FROM BODBA.BO_PEDIDOS P "
                        + "     LEFT JOIN BODBA.BO_PEDIDOS_EXT           PE ON PE.ID_PEDIDO           = P.ID_PEDIDO "
                        + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO  RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                        + "     LEFT JOIN BODBA.BO_MOTIVO_DESPACHO       MD ON MD.ID_MOTIVO_DESP      = PE.ID_MOTIVO_CUMPLIMIENTO "
                        + "     JOIN BODBA.BO_JORNADA_DESP               JD ON JD.ID_JDESPACHO        = P.ID_JDESPACHO "
                        + "     JOIN BODBA.BO_ZONAS                       Z ON Z.ID_ZONA              = JD.ID_ZONA "
                        + "     JOIN BODBA.BO_ESTADOS                     E ON E.ID_ESTADO            = P.ID_ESTADO "
                        + "     LEFT JOIN BODBA.BO_COMUNAS                C ON C.ID_COMUNA            = P.ID_COMUNA "
                        + "     LEFT JOIN BODBA.BO_RUTA                   R ON R.ID_RUTA              = PE.ID_RUTA "
                        + "     LEFT JOIN BODBA.BO_ESTADOS               ER ON ER.ID_ESTADO           = R.ID_ESTADO "
                        + "     LEFT JOIN (SELECT BP.ID_PEDIDO, COUNT(BP.ID_BP) AS CANTIDAD "
                        + "                FROM BODBA.BO_BINS_PEDIDO BP "
                        + "                WHERE BP.TIPO = 'F' "
                        + "                GROUP BY BP.ID_PEDIDO) AS BINS ON BINS.ID_PEDIDO = P.ID_PEDIDO "
                        + "WHERE Z.ID_LOCAL = "
                        + id_local
                        + " "
                        + "  AND P.ID_ESTADO IN (8, 9, 10) "
                        + "  AND JD.FECHA BETWEEN DATE('"
                        + FechaIni
                        + "') AND DATE('"
                        + FechaFin
                        + "') "
                        + "GROUP BY Z.ID_LOCAL, JD.FECHA, Z.ID_ZONA, Z.NOMBRE, C.ID_COMUNA, C.NOMBRE, PE.ID_RUTA, ER.ID_ESTADO, ER.NOMBRE, P.ID_PEDIDO, "
                        + "         P.ORIGEN, PE.NRO_GUIA_CASO, P.ID_ESTADO, E.NOMBRE, PE.CUMPLIMIENTO, BINS.CANTIDAD, "
                        + "         RD.ID_RESPONSABLE_DESP, RD.NOMBRE_RESPONSABLE, MD.MOTIVO, PE.VECES_EN_RUTA " + "ORDER BY JD.FECHA, Z.ID_ZONA, C.ID_COMUNA, PE.ID_RUTA, P.ID_PEDIDO";
                logger.debug("Query (getListadoOP - Planilla Tracking): " + SQL);

                stm = conn.createStatement();
                rs = stm.executeQuery(SQL);
                lstPedidos = new ArrayList();
                

                while ( rs.next() ) {

                    PedidoDesp ped = new PedidoDesp();
                    ped.setId_local(rs.getString("ID_LOCAL"));
                    ped.setFecha(rs.getString("FECHA"));
                    ped.setId_zona(rs.getString("ID_ZONA"));
                    ped.setNom_zona(rs.getString("NOM_ZONA"));
                    ped.setId_comuna(rs.getString("ID_COMUNA"));
                    ped.setNom_comuna(rs.getString("NOM_COMUNA"));
                    ped.setId_ruta(rs.getString("ID_RUTA"));
                    ped.setId_estado_ruta(rs.getString("ID_ESTADO_RUTA"));
                    ped.setNom_estado_ruta(rs.getString("NOM_ESTADO_RUTA"));
                    ped.setId_pedido(rs.getString("ID_PEDIDO"));
                    ped.setId_estado_pedido(rs.getString("ID_ESTADO_PEDIDO"));
                    ped.setNom_estado_pedido(rs.getString("NOM_ESTADO_PEDIDO"));
                    ped.setJumboVa(rs.getString("JUMBO_VA"));
                    ped.setCant_bins(rs.getString("CANT_BINS"));
                    ped.setVeces_en_ruta(rs.getString("VECES_EN_RUTA"));
                    ped.setEstatus_entrega(rs.getString("ESTATUS_ENTREGA"));
                    ped.setId_resp_incumplimiento(rs.getString("ID_RESP_INCUMPLIMIENTO"));
                    ped.setResp_incumplimiento(rs.getString("RESP_INCUMPLIMIENTO"));
                    ped.setMotivo_incumplimiento(rs.getString("MOTIVO_INCUMPLIMIENTO"));
                    if ( !CostoDespachoOP.equals("") ) {
                        ped.setCosto_despacho(rs.getString("COSTO_DESPACHO"));
                    }
                    this.TotalVecesEnRuta += Integer.parseInt(ped.getVeces_en_ruta());

                    lstPedidos.add(ped);
                }
                
            } catch (SQLException e) {
                logger.error(getClass() + ":" + METHOD_NAME + ": SQLException: " + e.getMessage(), e);                
            } catch (Exception e) {
                logger.error(getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage(), e);
                
            } finally {
                try {
                    if ( rs != null ) {
                        rs.close();
                    }
                    if ( stm != null ) {
                        stm.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    logger.error("Error cerrando conexiones --> "+e.getMessage(), e);                    
                } finally {
                    rs = null;
                    stm = null;
                    conn = null;
                }
            }
        }
        return lstPedidos;
    }

    public String FormatoHora( Timestamp fecha ) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(fecha);

        DateFormat DateFormatQuery = new SimpleDateFormat("HH:mm", new Locale("es", "ES", ""));
        String Hora = DateFormatQuery.format(cal.getTime()).toUpperCase();

        return Hora;
    }

    public List listadoLocales() {

        List lista_locales;
        String METHOD_NAME = "listadoLocales";
        Connection conn = null;
        lista_locales = new ArrayList();
        BeanLocal loc = null;
        Statement stm = null;
        ResultSet rs = null;
        try {
            logger.debug("en listadoLocales");
            String sql = "SELECT L.ID_LOCAL, L.COD_LOCAL, L.NOM_LOCAL FROM BODBA.BO_LOCALES L ORDER BY L.ID_LOCAL ASC";
            logger.debug(sql);
            ConexionUtil cu = new ConexionUtil();
            conn = cu.getConexion();
            stm = conn.createStatement();
            for ( rs = stm.executeQuery(sql); rs.next(); lista_locales.add(loc) ) {
                loc = new BeanLocal();
                loc.setId_local(rs.getInt("id_local"));
                loc.setCod_local(rs.getString("cod_local"));
                loc.setNom_local(rs.getString("nom_local"));
            }
            
        } catch (Exception e) {
            logger.error(getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage(), e);            
        } finally {
            try {
                if ( rs != null ) {
                    rs.close();
                }
                if ( stm != null ) {
                    stm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("Error cerrando conexiones --> "+ e.getMessage(), e);                
            } finally {
                rs = null;
                stm = null;
                conn = null;
            }
        }
        return lista_locales;
    }

    public String frmFechaByDate( Date fecha ) {

        String METHOD_NAME = "frmFechaByDate";
        try {
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
            return bartDateFormat.format(fecha);
        } catch (Exception e) {
            logger.error(getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage(), e);            
        }
        return "";
    }

    public HSSFCellStyle getEstiloConBordes( HSSFWorkbook wb, HSSFFont font ) {

        HSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFont(font);
        estilo.setBorderTop((short) 2);
        estilo.setBorderBottom((short) 2);
        estilo.setBorderRight((short) 2);
        estilo.setBorderLeft((short) 2);
        estilo.setFillPattern((short) 1);
        estilo.setAlignment((short) 2);

        return estilo;
    }

    public HSSFCellStyle getEstiloConBordes( HSSFWorkbook wb, HSSFFont font, short alineacion ) {

        HSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFont(font);
        estilo.setBorderTop((short) 2);
        estilo.setBorderBottom((short) 2);
        estilo.setBorderRight((short) 2);
        estilo.setBorderLeft((short) 2);
        estilo.setFillForegroundColor((short) 9);
        estilo.setFillPattern((short) 1);
        estilo.setAlignment(alineacion);

        return estilo;
    }

    public HSSFCellStyle getEstiloConBordes( HSSFWorkbook wb, HSSFFont font, short alineacion, short ColorRelleno ) {

        HSSFCellStyle estilo = wb.createCellStyle();
        estilo.setFont(font);
        estilo.setBorderTop((short) 2);
        estilo.setBorderBottom((short) 2);
        estilo.setBorderRight((short) 2);
        estilo.setBorderLeft((short) 2);
        estilo.setFillForegroundColor(ColorRelleno);
        estilo.setFillPattern((short) 1);
        estilo.setAlignment(alineacion);

        return estilo;
    }

    public HSSFFont getFuente( HSSFWorkbook wb, short color ) {

        HSSFFont fuente = wb.createFont();
        fuente.setFontHeightInPoints((short) 10);
        fuente.setFontName("Arial");
        fuente.setItalic(false);
        fuente.setColor(color);
        return fuente;
    }

    public HSSFFont getFuenteNegrita( HSSFWorkbook wb, short color ) {

        HSSFFont fuente = wb.createFont();
        fuente.setFontHeightInPoints((short) 10);
        fuente.setFontName("Arial");
        fuente.setItalic(false);
        fuente.setBoldweight((short) 700);
        fuente.setColor(color);
        return fuente;
    }

    public HSSFWorkbook JumboCL( HSSFWorkbook wb, List ListadoOP, BeanLocal loc, BeanParamConfig param, Calendar calendario ) {

        String METHOD_NAME = "JumboCL_Formato1";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //SimpleDateFormat dateFormatHoja = new SimpleDateFormat("ddMMyyyy");
        String Fecha = dateFormat.format(calendario.getTime());
        //String FechaHoja = dateFormatHoja.format(calendario.getTime());

        this.numFila2 = 0;
        try {
            int diasMes = calendario.getActualMaximum(5);
            this.numFila = 0;

            HSSFDataFormat format = wb.createDataFormat();

            HSSFFont font2 = wb.createFont();
            font2.setFontHeightInPoints((short) 10);
            font2.setFontName("Arial");
            font2.setItalic(false);
            font2.setBoldweight((short) 700);
            font2.setColor((short) 9);

            HSSFFont font4 = wb.createFont();
            font4.setFontHeightInPoints((short) 14);
            font4.setFontName("Arial");
            font4.setItalic(false);
            font4.setBoldweight((short) 700);
            font4.setColor((short) 23);

            HSSFCellStyle style4 = wb.createCellStyle();
            style4.setFont(font4);
            style4.setBorderTop((short) 0);
            style4.setBorderBottom((short) 0);
            style4.setBorderRight((short) 0);
            style4.setBorderLeft((short) 0);
            style4.setFillForegroundColor((short) 9);
            style4.setFillPattern((short) 1);

            HSSFSheet hoja = wb.createSheet("Jumbo.cl");

            HSSFRow row0 = hoja.createRow((short) this.numFila);
            HSSFCell cell = row0.createCell(4);
            cell.setCellValue(new HSSFRichTextString("Local (" + loc.getNom_local() + ") | Fecha (" + Fecha + ")"));
            cell.setCellStyle(style4);

            this.numFila += 2;
            int numCol = 0;
            int numColDet = 0;

            HSSFCellStyle style = getEstiloConBordes(wb, font2);
            style.setFillForegroundColor((short) 17);
            style.setWrapText(true);

            this.numFila += 1;
            HSSFRow rowHead2 = hoja.createRow((short) this.numFila);
            rowHead2.setHeightInPoints(25.0F);

            HSSFCell cell_0_0 = rowHead2.createCell(numCol);
            cell_0_0.setCellValue(new HSSFRichTextString("Nº"));
            cell_0_0.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_1 = rowHead2.createCell(numCol);
            cell_0_1.setCellValue(new HSSFRichTextString("Fecha"));
            cell_0_1.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_2 = rowHead2.createCell(numCol);
            cell_0_2.setCellValue(new HSSFRichTextString("Zona"));
            cell_0_2.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_3 = rowHead2.createCell(numCol);
            cell_0_3.setCellValue(new HSSFRichTextString("Comuna"));
            cell_0_3.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_4 = rowHead2.createCell(numCol);
            cell_0_4.setCellValue(new HSSFRichTextString("Ruta"));
            cell_0_4.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_5 = rowHead2.createCell(numCol);
            cell_0_5.setCellValue(new HSSFRichTextString("Estado Ruta"));
            cell_0_5.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_6 = rowHead2.createCell(numCol);
            cell_0_6.setCellValue(new HSSFRichTextString("Pedido"));
            cell_0_6.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_7 = rowHead2.createCell(numCol);
            cell_0_7.setCellValue(new HSSFRichTextString("Estado OP"));
            cell_0_7.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_9 = rowHead2.createCell(numCol);
            cell_0_9.setCellValue(new HSSFRichTextString("Cant. Bins"));
            cell_0_9.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_10 = rowHead2.createCell(numCol);
            cell_0_10.setCellValue(new HSSFRichTextString("Veces en Ruta"));
            cell_0_10.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_11 = rowHead2.createCell(numCol);
            cell_0_11.setCellValue(new HSSFRichTextString("Estatus Entrega"));
            cell_0_11.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_12 = rowHead2.createCell(numCol);
            cell_0_12.setCellValue(new HSSFRichTextString("Resp. Incumplimiento"));
            cell_0_12.setCellStyle(style);
            numCol++;

            if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta > 300 ) ) {
                HSSFCell cell_0_13 = rowHead2.createCell(numCol);
                cell_0_13.setCellValue(new HSSFRichTextString("Costo Despacho"));
                cell_0_13.setCellStyle(style);
                numCol++;
            } else if ( ( loc.getId_local() != 4 ) && ( loc.getId_local() != 6 ) && ( loc.getId_local() != 7 ) && ( loc.getId_local() != 8 ) ) {
                HSSFCell cell_0_13 = rowHead2.createCell(numCol);
                cell_0_13.setCellValue(new HSSFRichTextString("Costo Despacho"));
                cell_0_13.setCellStyle(style);
                numCol++;
            }

            HSSFFont fontNormal = wb.createFont();
            fontNormal.setFontHeightInPoints((short) 10);
            fontNormal.setFontName("Arial");
            fontNormal.setItalic(false);

            HSSFFont fontNegrita = wb.createFont();
            fontNegrita.setFontHeightInPoints((short) 10);
            fontNegrita.setFontName("Arial");
            fontNegrita.setItalic(false);
            fontNegrita.setBoldweight((short) 700);

            HSSFFont fontEstatus1 = wb.createFont();
            fontEstatus1.setFontHeightInPoints((short) 10);
            fontEstatus1.setFontName("Arial");
            fontEstatus1.setItalic(false);
            fontEstatus1.setBoldweight((short) 700);
            fontEstatus1.setColor((short) 48);

            HSSFFont fontEstatus2 = wb.createFont();
            fontEstatus2.setFontHeightInPoints((short) 10);
            fontEstatus2.setFontName("Arial");
            fontEstatus2.setItalic(false);
            fontEstatus2.setBoldweight((short) 700);
            fontEstatus2.setColor((short) 10);

            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setFont(fontNormal);
            style2.setBorderTop((short) 1);
            style2.setBorderBottom((short) 1);
            style2.setBorderRight((short) 1);
            style2.setBorderLeft((short) 1);
            style2.setFillForegroundColor((short) 9);
            style2.setFillPattern((short) 1);
            style2.setAlignment((short) 0);
            style2.setVerticalAlignment((short) 1);

            HSSFCellStyle style3 = wb.createCellStyle();
            style3.setFont(fontNormal);
            style3.setBorderTop((short) 1);
            style3.setBorderBottom((short) 1);
            style3.setBorderRight((short) 1);
            style3.setBorderLeft((short) 1);
            style3.setFillForegroundColor((short) 9);

            style3.setAlignment((short) 2);
            style3.setFillPattern((short) 1);

            HSSFCellStyle styleMoneda = wb.createCellStyle();
            styleMoneda.setFont(fontNegrita);
            styleMoneda.setBorderTop((short) 1);
            styleMoneda.setBorderBottom((short) 1);
            styleMoneda.setBorderRight((short) 1);
            styleMoneda.setBorderLeft((short) 1);
            styleMoneda.setFillForegroundColor((short) 9);

            styleMoneda.setAlignment((short) 3);

            styleMoneda.setDataFormat(format.getFormat("$ #,##0_)"));
            styleMoneda.setFillPattern((short) 1);

            HSSFPatriarch patr = hoja.createDrawingPatriarch();

            int posFecha = 0;
            int posZona = 0;
            int posComuna = 0;
            int posRuta = 0;
            int CantOP_JumboCL = 0;
            int TotalVecesEnRuta_JumboCL = 0;
            int DespachosTemucoZ1 = 0;
            int DespachosPMonttZ1_ContadorDia = 0;
            int DespachosPMonttZ1_Monto = 0;
            HashMap RutasTemucoZ2 = new HashMap();
            HashMap RutasTemucoZ3Z4 = new HashMap();

            HashMap RutasPMonttZ3 = new HashMap();
            HashMap RutasPMonttZ2_PVaras = new HashMap();
            HashMap RutasPMonttZ2_Llanquihue = new HashMap();
            HashMap RutasPMonttZ2_Frutillar = new HashMap();

            String FechaAnt = "";
            String ZonaAnt = "";
            String ComunaAnt = "";
            String RutaAnt = "";

            if ( loc.getId_local() == 5 ) {
                for ( int i = 0; i < ListadoOP.size(); i++ ) {
                    PedidoDesp ped = (PedidoDesp) ListadoOP.get(i);

                    if ( this.TotalVecesEnRuta < 1201 ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 7400));
                    } else if ( ( this.TotalVecesEnRuta > 1200 ) && ( this.TotalVecesEnRuta < 1501 ) ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 7200));
                    } else if ( ( this.TotalVecesEnRuta > 1500 ) && ( this.TotalVecesEnRuta < 2001 ) ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 6600));
                    } else if ( this.TotalVecesEnRuta > 2000 ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 6200));
                    }
                }
            } else if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta > 300 ) ) {
                for ( int i = 0; i < ListadoOP.size(); i++ ) {
                    PedidoDesp ped = (PedidoDesp) ListadoOP.get(i);

                    ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 7300));
                }

            }

            int CantTotalOP = ListadoOP.size();
            for ( int i = 0; i < ListadoOP.size(); i++ ) {
                PedidoDesp ped = (PedidoDesp) ListadoOP.get(i);

                if ( ped.getJumboVa().trim().equals("") ) {
                    this.numFila += 1;
                    CantOP_JumboCL++;
                    numColDet = 0;

                    HSSFCellStyle StyleStatus = wb.createCellStyle();
                    StyleStatus.cloneStyleFrom(style3);

                    if ( ped.getEstatus_entrega().trim().equals("A TIEMPO") ) {
                        StyleStatus.setFont(fontEstatus1);
                    } else {
                        StyleStatus.setFont(fontEstatus2);
                    }

                    if ( Integer.parseInt(ped.getId_local()) == 7 ) {
                        if ( ( Integer.parseInt(ped.getId_zona()) == 386 ) && ( RutasTemucoZ3Z4.get(ped.getId_ruta()) == null ) ) {
                            RutasTemucoZ2.put(ped.getId_ruta(), ped.getId_ruta());
                        }

                        if ( ( Integer.parseInt(ped.getId_zona()) == 387 ) || ( Integer.parseInt(ped.getId_zona()) == 388 ) ) {
                            if ( RutasTemucoZ2.get(ped.getId_ruta()) != null ) {
                                RutasTemucoZ2.remove(ped.getId_ruta());
                            }
                            RutasTemucoZ3Z4.put(ped.getId_ruta(), ped.getId_ruta());
                        }

                    }

                    if ( Integer.parseInt(ped.getId_local()) == 8 ) {
                        if ( Integer.parseInt(ped.getId_zona()) == 405 ) {
                            if ( Integer.parseInt(ped.getId_comuna()) == 247 ) {
                                RutasPMonttZ2_PVaras.put(ped.getId_ruta(), ped.getId_ruta());
                            }

                            if ( Integer.parseInt(ped.getId_comuna()) == 243 ) {
                                if ( RutasPMonttZ2_PVaras.get(ped.getId_ruta()) != null ) {
                                    RutasPMonttZ2_PVaras.remove(ped.getId_ruta());
                                }
                                RutasPMonttZ2_Llanquihue.put(ped.getId_ruta(), ped.getId_ruta());
                            }

                            if ( ( Integer.parseInt(ped.getId_comuna()) == 240 ) || ( Integer.parseInt(ped.getId_comuna()) == 242 ) || ( Integer.parseInt(ped.getId_comuna()) == 246 ) ) {
                                if ( RutasPMonttZ2_PVaras.get(ped.getId_ruta()) != null ) {
                                    RutasPMonttZ2_PVaras.remove(ped.getId_ruta());
                                } else if ( RutasPMonttZ2_Llanquihue.get(ped.getId_ruta()) != null ) {
                                    RutasPMonttZ2_Llanquihue.remove(ped.getId_ruta());
                                }
                                RutasPMonttZ2_Frutillar.put(ped.getId_ruta(), ped.getId_ruta());
                            }

                        }

                        if ( Integer.parseInt(ped.getId_zona()) == 406 ) {
                            if ( RutasPMonttZ2_PVaras.get(ped.getId_ruta()) != null ) {
                                RutasPMonttZ2_PVaras.remove(ped.getId_ruta());
                            } else if ( RutasPMonttZ2_Llanquihue.get(ped.getId_ruta()) != null ) {
                                RutasPMonttZ2_Llanquihue.remove(ped.getId_ruta());
                            } else if ( RutasPMonttZ2_Frutillar.get(ped.getId_ruta()) != null ) {
                                RutasPMonttZ2_Frutillar.remove(ped.getId_ruta());
                            }
                            RutasPMonttZ3.put(ped.getId_ruta(), ped.getId_ruta());
                        }

                    }

                    HSSFRow rowDetalle = hoja.createRow((short) this.numFila);

                    String fecha = ped.getFecha();
                    Calendar cal = Calendar.getInstance();
                    int year = Integer.parseInt(fecha.substring(0, 4));
                    int mes = Integer.parseInt(fecha.substring(5, 7)) - 1;
                    int dia = Integer.parseInt(fecha.substring(8, 10));
                    cal.set(year, mes, dia);

                    DateFormat DateFormat = new SimpleDateFormat("dd MMM", new Locale("es", "ES", ""));
                    Fecha = DateFormat.format(cal.getTime()).toUpperCase();

                    HSSFCell cell_0 = rowDetalle.createCell(numColDet);
                    cell_0.setCellValue(new Double(CantOP_JumboCL).doubleValue());
                    cell_0.setCellStyle(style2);
                    numColDet++;

                    HSSFCell cell_1 = rowDetalle.createCell(numColDet);
                    cell_1.setCellStyle(style2);

                    if ( ( FechaAnt.equals("") ) || ( !FechaAnt.equals(Fecha) ) ) {
                        cell_1.setCellValue(new HSSFRichTextString(Fecha));

                        if ( ( !FechaAnt.equals("") ) && ( !FechaAnt.equals(Fecha) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posFecha, this.numFila - 1, 1, 1));
                        }

                        FechaAnt = Fecha;
                        posFecha = this.numFila;
                    }
                    numColDet++;

                    HSSFCell cell_2 = rowDetalle.createCell(numColDet);
                    cell_2.setCellStyle(style2);

                    if ( ( ZonaAnt.equals("") ) || ( !ZonaAnt.equals(ped.getNom_zona()) ) ) {
                        cell_2.setCellValue(new HSSFRichTextString(ped.getNom_zona()));

                        if ( ( !ZonaAnt.equals("") ) && ( !ZonaAnt.equals(ped.getNom_zona()) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posZona, this.numFila - 1, 2, 2));
                        }

                        ZonaAnt = ped.getNom_zona();
                        posZona = this.numFila;
                    }
                    numColDet++;

                    if ( ( Integer.parseInt(ped.getId_local()) == 7 ) && ( Integer.parseInt(ped.getId_zona()) == 385 ) ) {
                        DespachosTemucoZ1 += Integer.parseInt(ped.getVeces_en_ruta());
                    }
                    if ( ( Integer.parseInt(ped.getId_local()) == 8 ) && ( Integer.parseInt(ped.getId_zona()) == 404 ) ) {
                        DespachosPMonttZ1_ContadorDia++;
                        if ( ( FechaAnt.equals("") ) || ( !FechaAnt.equals(Fecha) ) ) {
                            if ( ( !FechaAnt.equals("") ) && ( !FechaAnt.equals(Fecha) ) && ( DespachosPMonttZ1_ContadorDia > 10 ) ) {
                                DespachosPMonttZ1_Monto += ( DespachosPMonttZ1_ContadorDia - 10 ) * 2500;
                            }
                            FechaAnt = Fecha;
                        }

                    }

                    HSSFCell cell_3 = rowDetalle.createCell(numColDet);
                    cell_3.setCellStyle(style2);

                    if ( ( ComunaAnt.equals("") ) || ( !ComunaAnt.equals(ped.getNom_comuna()) ) ) {
                        cell_3.setCellValue(new HSSFRichTextString(ped.getNom_comuna()));

                        if ( ( !ComunaAnt.equals("") ) && ( !ComunaAnt.equals(ped.getNom_comuna()) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posComuna, this.numFila - 1, 3, 3));
                        }

                        ComunaAnt = ped.getNom_comuna();
                        posComuna = this.numFila;
                    }
                    numColDet++;

                    HSSFCell cell_4 = rowDetalle.createCell(numColDet);
                    cell_4.setCellStyle(style2);

                    if ( ( RutaAnt.equals("") ) || ( !RutaAnt.equals(ped.getId_ruta()) ) ) {
                        cell_4.setCellValue(Double.parseDouble(ped.getId_ruta()));

                        if ( ( !RutaAnt.equals("") ) && ( !RutaAnt.equals(ped.getId_ruta()) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posRuta, this.numFila - 1, 4, 4));
                        }

                        RutaAnt = ped.getId_ruta();
                        posRuta = this.numFila;
                    }
                    numColDet++;

                    HSSFCell cell_5 = rowDetalle.createCell(numColDet);
                    cell_5.setCellValue(new HSSFRichTextString(ped.getNom_estado_ruta()));
                    cell_5.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_6 = rowDetalle.createCell(numColDet);
                    cell_6.setCellValue(Double.parseDouble(ped.getId_pedido()));
                    cell_6.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_7 = rowDetalle.createCell(numColDet);
                    cell_7.setCellValue(new HSSFRichTextString(ped.getNom_estado_pedido()));
                    cell_7.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_9 = rowDetalle.createCell(numColDet);
                    cell_9.setCellValue(Double.parseDouble(ped.getCant_bins()));
                    cell_9.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_10 = rowDetalle.createCell(numColDet);
                    cell_10.setCellValue(Double.parseDouble(ped.getVeces_en_ruta()));
                    cell_10.setCellStyle(style3);
                    numColDet++;
                    TotalVecesEnRuta_JumboCL += Integer.parseInt(ped.getVeces_en_ruta());

                    HSSFCell cell_11 = rowDetalle.createCell(numColDet);
                    cell_11.setCellValue(new HSSFRichTextString(ped.getEstatus_entrega()));
                    cell_11.setCellStyle(StyleStatus);
                    numColDet++;

                    HSSFCell cell_12 = rowDetalle.createCell(numColDet);
                    cell_12.setCellValue(new HSSFRichTextString(ped.getResp_incumplimiento()));

                    if ( !ped.getResp_incumplimiento().trim().equals("") ) {
                        HSSFComment comment1 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 9, this.numFila - 1, (short) 12, this.numFila + 2));

                        HSSFRichTextString string = new HSSFRichTextString("Motivo: " + ped.getMotivo_incumplimiento());
                        string.applyFont(fontNegrita);
                        comment1.setString(string);
                        cell_12.setCellComment(comment1);
                    }
                    cell_12.setCellStyle(style3);
                    numColDet++;

                    if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta > 300 ) ) {
                        HSSFCell cell_13 = rowDetalle.createCell(numColDet);
                        cell_13.setCellValue(Double.parseDouble(ped.getCosto_despacho()));
                        cell_13.setCellStyle(styleMoneda);
                        numColDet++;
                    } else if ( ( loc.getId_local() != 4 ) && ( loc.getId_local() != 6 ) && ( loc.getId_local() != 7 ) && ( loc.getId_local() != 8 ) ) {
                        HSSFCell cell_13 = rowDetalle.createCell(numColDet);
                        cell_13.setCellValue(Double.parseDouble(ped.getCosto_despacho()));
                        cell_13.setCellStyle(styleMoneda);
                        numColDet++;
                    }
                }
                if ( ( CantTotalOP == i + 1 ) && ( CantOP_JumboCL > 0 ) ) {
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posFecha, this.numFila, 1, 1));
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posZona, this.numFila, 2, 2));
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posComuna, this.numFila, 3, 3));
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posRuta, this.numFila, 4, 4));
                }

            }

            this.numFila2 = 3;

            if ( ( loc.getId_local() == 3 ) || ( loc.getId_local() == 4 ) || ( loc.getId_local() == 7 ) || ( loc.getId_local() == 8 ) ) {
                HSSFRow rowDetalle3 = hoja.getRow((short) this.numFila2);
                HSSFCell cell_3_15 = rowDetalle3.createCell(15);
                if ( loc.getId_local() != 8 ) {
                    cell_3_15.setCellValue(new HSSFRichTextString("Fijo Mensual: "));
                } else {
                    cell_3_15.setCellValue(new HSSFRichTextString("Fijo Diario: "));
                }
                cell_3_15.setCellStyle(style);

                HSSFCell cell_3_16 = rowDetalle3.createCell(16);
                if ( loc.getId_local() == 3 ) {
                    int fijo = 7000000;
                    int dia = calendario.get(5);
                    if ( dia == 15 ) {
                        fijo = fijo * dia / diasMes;
                    } else if ( dia == diasMes ) {
                        fijo = fijo * ( diasMes - 15 ) / diasMes;
                    }
                    cell_3_16.setCellValue(fijo);
                } else if ( loc.getId_local() == 4 ) {
                    cell_3_16.setCellValue(3617000.0D);
                } else if ( loc.getId_local() == 7 ) {
                    cell_3_16.setCellValue(2400000.0D);
                } else if ( loc.getId_local() == 8 ) {
                    cell_3_16.setCellValue(48000.0D);
                }
                cell_3_16.setCellStyle(styleMoneda);
                this.numFila2 += 1;
            } else if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta <= 300 ) ) {
                HSSFRow rowDetalle3 = hoja.getRow((short) this.numFila2);
                HSSFCell cell_3_15 = rowDetalle3.createCell(15);
                cell_3_15.setCellValue(new HSSFRichTextString("Fijo Mensual: "));
                cell_3_15.setCellStyle(style);

                HSSFCell cell_3_16 = rowDetalle3.createCell(16);
                if ( this.TotalVecesEnRuta < 151 ) {
                    cell_3_16.setCellValue(2000000.0D);
                } else if ( ( this.TotalVecesEnRuta > 150 ) && ( this.TotalVecesEnRuta < 301 ) ) {
                    cell_3_16.setCellValue(2200000.0D);
                }
                cell_3_16.setCellStyle(styleMoneda);
                this.numFila2 += 1;
            }

            HSSFRow rowDetalle4 = hoja.getRow((short) this.numFila2);
            HSSFCell cell_4_15 = rowDetalle4.createCell(15);
            cell_4_15.setCellValue(new HSSFRichTextString("Cantidad de Pedidos: "));
            cell_4_15.setCellStyle(style);

            HSSFCell cell_4_16 = rowDetalle4.createCell(16);
            cell_4_16.setCellValue(CantOP_JumboCL);
            cell_4_16.setCellStyle(style2);
            this.numFila2 += 1;

            HSSFRow rowDetalle5 = hoja.getRow((short) this.numFila2);
            HSSFCell cell_5_15 = rowDetalle5.createCell(15);
            cell_5_15.setCellValue(new HSSFRichTextString("Total Jumbo.cl: "));
            cell_5_15.setCellStyle(style);

            if ( ( loc.getId_local() == 1 ) || ( loc.getId_local() == 2 ) ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                cell_5_16.setCellFormula("SUM($M$5:$M$" + ( this.numFila + 1 ) + ")");
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 3 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "(($Q$4*" + TotalVecesEnRuta_JumboCL + ")/" + this.TotalVecesEnRuta + ")";
                String ValorVariable = "SUM($M$5:$M$" + ( this.numFila + 1 ) + ")";
                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 4 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "(($Q$4*" + TotalVecesEnRuta_JumboCL + ")/" + this.TotalVecesEnRuta + ")";
                String ValorVariable = "0";
                if ( this.TotalVecesEnRuta > 500 ) {
                    double porcJumboCL = TotalVecesEnRuta_JumboCL / this.TotalVecesEnRuta;
                    double deltaJumboCL = ( this.TotalVecesEnRuta - 500 ) * porcJumboCL;
                    ValorVariable = String.valueOf( ( deltaJumboCL * 5198.0D ));
                }
                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 5 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);

                String ValorVariable = "SUM($M$5:$M$" + ( this.numFila + 1 ) + ")";
                cell_5_16.setCellFormula(ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 6 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "(($Q$4*" + TotalVecesEnRuta_JumboCL + ")/" + this.TotalVecesEnRuta + ")";
                String ValorVariable = "SUM($M$5:$M$" + ( this.numFila + 1 ) + ")";
                if ( this.TotalVecesEnRuta > 300 ) {
                    cell_5_16.setCellFormula(ValorVariable);
                } else {
                    cell_5_16.setCellFormula(ValorFijo);
                }
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 7 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "($Q$4 * 2)/3";
                String ValorVariable = "";
                if ( DespachosTemucoZ1 > 120 ) {
                    ValorVariable = "(" + ( DespachosTemucoZ1 - 120 ) + " * 2000) + ";
                }
                ValorVariable = ValorVariable + " (" + RutasTemucoZ2.size() + " * 55000)";
                ValorVariable = ValorVariable + " + (" + RutasTemucoZ3Z4.size() + " * 80000)";

                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 8 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                int diasTrabajadosPMontt = DiasDespachoPuertoMontt(calendario);
                String ValorFijo = "(($Q$4 * " + diasTrabajadosPMontt + ")*" + TotalVecesEnRuta_JumboCL + ")/" + this.TotalVecesEnRuta;

                String ValorVariable = String.valueOf(DespachosPMonttZ1_Monto);
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ2_PVaras.size() + " * 25000)";
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ2_Llanquihue.size() + " * 38000)";
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ2_Frutillar.size() + " * 42000)";
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ3.size() + " * 120000)";

                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            }

            hoja.setColumnWidth(0, 1000);
            hoja.setColumnWidth(1, 2000);

            hoja.setColumnWidth(2, 5000);
            hoja.setColumnWidth(3, 5700);
            hoja.setColumnWidth(4, 2000);
            hoja.autoSizeColumn((short)5);

            hoja.autoSizeColumn((short)6);
            hoja.autoSizeColumn((short)7);

            hoja.setColumnWidth(8, 2000);
            hoja.setColumnWidth(9, 2000);
            hoja.setColumnWidth(10, 3500);
            hoja.setColumnWidth(11, 4000);
            if ( loc.getId_local() != 4 ) {
                hoja.setColumnWidth(12, 2800);
            }

            hoja.setColumnWidth(15, 5200);
            hoja.setColumnWidth(16, 3500);
        } catch (Exception e) {
            logger.error("ERROR: " + getClass() + "." + METHOD_NAME + " : " + e, e);            
        }
        return wb;
    }

    public HSSFWorkbook JumboVa( HSSFWorkbook wb, List ListadoOP, BeanLocal loc, BeanParamConfig param, Calendar calendario ) {

        String METHOD_NAME = "JumboVa";

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //SimpleDateFormat dateFormatHoja = new SimpleDateFormat("ddMMyyyy");
        String Fecha = dateFormat.format(calendario.getTime());
        //String FechaHoja = dateFormatHoja.format(calendario.getTime());

        this.numFila2 = 0;
        try {
            //int diasMes = calendario.getActualMaximum(5);

            this.numFila = 0;

            HSSFDataFormat format = wb.createDataFormat();

            HSSFFont font2 = wb.createFont();
            font2.setFontHeightInPoints((short) 10);
            font2.setFontName("Arial");
            font2.setItalic(false);
            font2.setBoldweight((short) 700);
            font2.setColor((short) 9);

            HSSFFont font4 = wb.createFont();
            font4.setFontHeightInPoints((short) 14);
            font4.setFontName("Arial");
            font4.setItalic(false);
            font4.setBoldweight((short) 700);
            font4.setColor((short) 23);

            HSSFCellStyle style4 = wb.createCellStyle();
            style4.setFont(font4);
            style4.setBorderTop((short) 0);
            style4.setBorderBottom((short) 0);
            style4.setBorderRight((short) 0);
            style4.setBorderLeft((short) 0);
            style4.setFillForegroundColor((short) 9);
            style4.setFillPattern((short) 1);

            HSSFSheet hoja = wb.createSheet("JumboVa");

            HSSFRow row0 = hoja.createRow((short) this.numFila);
            HSSFCell cell = row0.createCell(4);
            cell.setCellValue(new HSSFRichTextString("Local (" + loc.getNom_local() + ") | Fecha (" + Fecha + ")"));
            cell.setCellStyle(style4);

            this.numFila += 2;
            int numCol = 0;
            int numColDet = 0;

            HSSFCellStyle style = getEstiloConBordes(wb, font2);
            style.setFillForegroundColor((short) 17);
            style.setWrapText(true);

            this.numFila += 1;
            HSSFRow rowHead2 = hoja.createRow((short) this.numFila);
            rowHead2.setHeightInPoints(25.0F);

            HSSFCell cell_0_0 = rowHead2.createCell(numCol);
            cell_0_0.setCellValue(new HSSFRichTextString("Nº"));
            cell_0_0.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_1 = rowHead2.createCell(numCol);
            cell_0_1.setCellValue(new HSSFRichTextString("Fecha"));
            cell_0_1.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_2 = rowHead2.createCell(numCol);
            cell_0_2.setCellValue(new HSSFRichTextString("Zona"));
            cell_0_2.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_3 = rowHead2.createCell(numCol);
            cell_0_3.setCellValue(new HSSFRichTextString("Comuna"));
            cell_0_3.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_4 = rowHead2.createCell(numCol);
            cell_0_4.setCellValue(new HSSFRichTextString("Ruta"));
            cell_0_4.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_5 = rowHead2.createCell(numCol);
            cell_0_5.setCellValue(new HSSFRichTextString("Estado Ruta"));
            cell_0_5.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_6 = rowHead2.createCell(numCol);
            cell_0_6.setCellValue(new HSSFRichTextString("Pedido"));
            cell_0_6.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_7 = rowHead2.createCell(numCol);
            cell_0_7.setCellValue(new HSSFRichTextString("Estado OP"));
            cell_0_7.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_8 = rowHead2.createCell(numCol);
            cell_0_8.setCellValue(new HSSFRichTextString("JumboVa"));
            cell_0_8.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_9 = rowHead2.createCell(numCol);
            cell_0_9.setCellValue(new HSSFRichTextString("Cant. Bins"));
            cell_0_9.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_10 = rowHead2.createCell(numCol);
            cell_0_10.setCellValue(new HSSFRichTextString("Veces en Ruta"));
            cell_0_10.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_11 = rowHead2.createCell(numCol);
            cell_0_11.setCellValue(new HSSFRichTextString("Estatus Entrega"));
            cell_0_11.setCellStyle(style);
            numCol++;

            HSSFCell cell_0_12 = rowHead2.createCell(numCol);
            cell_0_12.setCellValue(new HSSFRichTextString("Resp. Incumplimiento"));
            cell_0_12.setCellStyle(style);
            numCol++;

            if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta > 300 ) ) {
                HSSFCell cell_0_13 = rowHead2.createCell(numCol);
                cell_0_13.setCellValue(new HSSFRichTextString("Costo Despacho"));
                cell_0_13.setCellStyle(style);
                numCol++;
            } else if ( ( loc.getId_local() != 4 ) && ( loc.getId_local() != 6 ) && ( loc.getId_local() != 7 ) && ( loc.getId_local() != 8 ) ) {
                HSSFCell cell_0_13 = rowHead2.createCell(numCol);
                cell_0_13.setCellValue(new HSSFRichTextString("Costo Despacho"));
                cell_0_13.setCellStyle(style);
                numCol++;
            }

            HSSFFont fontNormal = wb.createFont();
            fontNormal.setFontHeightInPoints((short) 10);
            fontNormal.setFontName("Arial");
            fontNormal.setItalic(false);

            HSSFFont fontNegrita = wb.createFont();
            fontNegrita.setFontHeightInPoints((short) 10);
            fontNegrita.setFontName("Arial");
            fontNegrita.setItalic(false);
            fontNegrita.setBoldweight((short) 700);

            HSSFFont fontEstatus1 = wb.createFont();
            fontEstatus1.setFontHeightInPoints((short) 10);
            fontEstatus1.setFontName("Arial");
            fontEstatus1.setItalic(false);
            fontEstatus1.setBoldweight((short) 700);
            fontEstatus1.setColor((short) 48);

            HSSFFont fontEstatus2 = wb.createFont();
            fontEstatus2.setFontHeightInPoints((short) 10);
            fontEstatus2.setFontName("Arial");
            fontEstatus2.setItalic(false);
            fontEstatus2.setBoldweight((short) 700);
            fontEstatus2.setColor((short) 10);

            HSSFCellStyle style2 = wb.createCellStyle();
            style2.setFont(fontNormal);
            style2.setBorderTop((short) 1);
            style2.setBorderBottom((short) 1);
            style2.setBorderRight((short) 1);
            style2.setBorderLeft((short) 1);
            style2.setFillForegroundColor((short) 9);
            style2.setFillPattern((short) 1);
            style2.setAlignment((short) 0);
            style2.setVerticalAlignment((short) 1);

            HSSFCellStyle style3 = wb.createCellStyle();
            style3.setFont(fontNormal);
            style3.setBorderTop((short) 1);
            style3.setBorderBottom((short) 1);
            style3.setBorderRight((short) 1);
            style3.setBorderLeft((short) 1);
            style3.setFillForegroundColor((short) 9);

            style3.setAlignment((short) 2);
            style3.setFillPattern((short) 1);

            HSSFCellStyle styleMoneda = wb.createCellStyle();
            styleMoneda.setFont(fontNegrita);
            styleMoneda.setBorderTop((short) 1);
            styleMoneda.setBorderBottom((short) 1);
            styleMoneda.setBorderRight((short) 1);
            styleMoneda.setBorderLeft((short) 1);
            styleMoneda.setFillForegroundColor((short) 9);

            styleMoneda.setAlignment((short) 3);

            styleMoneda.setDataFormat(format.getFormat("$ #,##0_)"));
            styleMoneda.setFillPattern((short) 1);

            HSSFPatriarch patr = hoja.createDrawingPatriarch();

            int posFecha = 0;
            int posZona = 0;
            int posComuna = 0;
            int posRuta = 0;
            int CantOP_JumboVa = 0;
            int TotalVecesEnRuta_JumboVa = 0;
            int DespachosTemucoZ1 = 0;
            int DespachosPMonttZ1_ContadorDia = 0;
            int DespachosPMonttZ1_Monto = 0;
            HashMap RutasTemucoZ2 = new HashMap();
            HashMap RutasTemucoZ3Z4 = new HashMap();

            HashMap RutasPMonttZ3 = new HashMap();
            HashMap RutasPMonttZ2_PVaras = new HashMap();
            HashMap RutasPMonttZ2_Llanquihue = new HashMap();
            HashMap RutasPMonttZ2_Frutillar = new HashMap();

            String FechaAnt = "";
            String ZonaAnt = "";
            String ComunaAnt = "";
            String RutaAnt = "";

            if ( loc.getId_local() == 5 ) {
                for ( int i = 0; i < ListadoOP.size(); i++ ) {
                    PedidoDesp ped = (PedidoDesp) ListadoOP.get(i);

                    if ( this.TotalVecesEnRuta < 1201 ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 7400));
                    } else if ( ( this.TotalVecesEnRuta > 1200 ) && ( this.TotalVecesEnRuta < 1501 ) ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 7200));
                    } else if ( ( this.TotalVecesEnRuta > 1500 ) && ( this.TotalVecesEnRuta < 2001 ) ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 6600));
                    } else if ( this.TotalVecesEnRuta > 2000 ) {
                        ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 6200));
                    }
                }
            } else if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta > 300 ) ) {

                for ( int i = 0; i < ListadoOP.size(); i++ ) {

                    PedidoDesp ped = (PedidoDesp) ListadoOP.get(i);
                    ped.setCosto_despacho(String.valueOf(Integer.parseInt(ped.getVeces_en_ruta()) * 7300));
                }

            }

            int CantTotalOP = ListadoOP.size();
            for ( int i = 0; i < ListadoOP.size(); i++ ) {
                PedidoDesp ped = (PedidoDesp) ListadoOP.get(i);

                if ( !ped.getJumboVa().trim().equals("") ) {
                    this.numFila += 1;
                    CantOP_JumboVa++;
                    numColDet = 0;

                    HSSFCellStyle StyleStatus = wb.createCellStyle();
                    StyleStatus.cloneStyleFrom(style3);

                    if ( ped.getEstatus_entrega().trim().equals("A TIEMPO") ) {
                        StyleStatus.setFont(fontEstatus1);
                    } else {
                        StyleStatus.setFont(fontEstatus2);
                    }

                    if ( Integer.parseInt(ped.getId_local()) == 7 ) {
                        if ( ( Integer.parseInt(ped.getId_zona()) == 386 ) && ( RutasTemucoZ3Z4.get(ped.getId_ruta()) == null ) ) {
                            RutasTemucoZ2.put(ped.getId_ruta(), ped.getId_ruta());
                        }

                        if ( ( Integer.parseInt(ped.getId_zona()) == 387 ) || ( Integer.parseInt(ped.getId_zona()) == 388 ) ) {
                            if ( RutasTemucoZ2.get(ped.getId_ruta()) != null ) {
                                RutasTemucoZ2.remove(ped.getId_ruta());
                            }
                            RutasTemucoZ3Z4.put(ped.getId_ruta(), ped.getId_ruta());
                        }

                    }

                    if ( Integer.parseInt(ped.getId_local()) == 8 ) {
                        if ( Integer.parseInt(ped.getId_zona()) == 405 ) {
                            if ( Integer.parseInt(ped.getId_comuna()) == 247 ) {
                                RutasPMonttZ2_PVaras.put(ped.getId_ruta(), ped.getId_ruta());
                            }

                            if ( Integer.parseInt(ped.getId_comuna()) == 243 ) {
                                if ( RutasPMonttZ2_PVaras.get(ped.getId_ruta()) != null ) {
                                    RutasPMonttZ2_PVaras.remove(ped.getId_ruta());
                                }
                                RutasPMonttZ2_Llanquihue.put(ped.getId_ruta(), ped.getId_ruta());
                            }

                            if ( ( Integer.parseInt(ped.getId_comuna()) == 240 ) || ( Integer.parseInt(ped.getId_comuna()) == 242 ) || ( Integer.parseInt(ped.getId_comuna()) == 246 ) ) {
                                if ( RutasPMonttZ2_PVaras.get(ped.getId_ruta()) != null ) {
                                    RutasPMonttZ2_PVaras.remove(ped.getId_ruta());
                                } else if ( RutasPMonttZ2_Llanquihue.get(ped.getId_ruta()) != null ) {
                                    RutasPMonttZ2_Llanquihue.remove(ped.getId_ruta());
                                }
                                RutasPMonttZ2_Frutillar.put(ped.getId_ruta(), ped.getId_ruta());
                            }

                        }

                        if ( Integer.parseInt(ped.getId_zona()) == 406 ) {
                            if ( RutasPMonttZ2_PVaras.get(ped.getId_ruta()) != null ) {
                                RutasPMonttZ2_PVaras.remove(ped.getId_ruta());
                            } else if ( RutasPMonttZ2_Llanquihue.get(ped.getId_ruta()) != null ) {
                                RutasPMonttZ2_Llanquihue.remove(ped.getId_ruta());
                            } else if ( RutasPMonttZ2_Frutillar.get(ped.getId_ruta()) != null ) {
                                RutasPMonttZ2_Frutillar.remove(ped.getId_ruta());
                            }
                            RutasPMonttZ3.put(ped.getId_ruta(), ped.getId_ruta());
                        }

                    }

                    HSSFRow rowDetalle = hoja.createRow((short) this.numFila);

                    String fecha = ped.getFecha();
                    Calendar cal = Calendar.getInstance();
                    int year = Integer.parseInt(fecha.substring(0, 4));
                    int mes = Integer.parseInt(fecha.substring(5, 7)) - 1;
                    int dia = Integer.parseInt(fecha.substring(8, 10));
                    cal.set(year, mes, dia);

                    DateFormat DateFormat = new SimpleDateFormat("dd MMM", new Locale("es", "ES", ""));
                    Fecha = DateFormat.format(cal.getTime()).toUpperCase();

                    HSSFCell cell_0 = rowDetalle.createCell(numColDet);
                    cell_0.setCellValue(new Double(CantOP_JumboVa).doubleValue());
                    cell_0.setCellStyle(style2);
                    numColDet++;

                    HSSFCell cell_1 = rowDetalle.createCell(numColDet);
                    cell_1.setCellStyle(style2);

                    if ( ( FechaAnt.equals("") ) || ( !FechaAnt.equals(Fecha) ) ) {
                        cell_1.setCellValue(new HSSFRichTextString(Fecha));

                        if ( ( !FechaAnt.equals("") ) && ( !FechaAnt.equals(Fecha) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posFecha, this.numFila - 1, 1, 1));
                        }

                        FechaAnt = Fecha;
                        posFecha = this.numFila;
                    }
                    numColDet++;

                    HSSFCell cell_2 = rowDetalle.createCell(numColDet);
                    cell_2.setCellStyle(style2);

                    if ( ( ZonaAnt.equals("") ) || ( !ZonaAnt.equals(ped.getNom_zona()) ) ) {
                        cell_2.setCellValue(new HSSFRichTextString(ped.getNom_zona()));

                        if ( ( !ZonaAnt.equals("") ) && ( !ZonaAnt.equals(ped.getNom_zona()) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posZona, this.numFila - 1, 2, 2));
                        }

                        ZonaAnt = ped.getNom_zona();
                        posZona = this.numFila;
                    }
                    numColDet++;

                    if ( ( Integer.parseInt(ped.getId_local()) == 7 ) && ( Integer.parseInt(ped.getId_zona()) == 385 ) ) {
                        DespachosTemucoZ1 += Integer.parseInt(ped.getVeces_en_ruta());
                    }
                    if ( ( Integer.parseInt(ped.getId_local()) == 8 ) && ( Integer.parseInt(ped.getId_zona()) == 404 ) ) {
                        DespachosPMonttZ1_ContadorDia++;
                        if ( ( FechaAnt.equals("") ) || ( !FechaAnt.equals(Fecha) ) ) {
                            if ( ( !FechaAnt.equals("") ) && ( !FechaAnt.equals(Fecha) ) && ( DespachosPMonttZ1_ContadorDia > 10 ) ) {
                                DespachosPMonttZ1_Monto += ( DespachosPMonttZ1_ContadorDia - 10 ) * 2500;
                            }
                            FechaAnt = Fecha;
                        }

                    }

                    HSSFCell cell_3 = rowDetalle.createCell(numColDet);
                    cell_3.setCellStyle(style2);

                    if ( ( ComunaAnt.equals("") ) || ( !ComunaAnt.equals(ped.getNom_comuna()) ) ) {
                        cell_3.setCellValue(new HSSFRichTextString(ped.getNom_comuna()));

                        if ( ( !ComunaAnt.equals("") ) && ( !ComunaAnt.equals(ped.getNom_comuna()) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posComuna, this.numFila - 1, 3, 3));
                        }

                        ComunaAnt = ped.getNom_comuna();
                        posComuna = this.numFila;
                    }
                    numColDet++;

                    HSSFCell cell_4 = rowDetalle.createCell(numColDet);
                    cell_4.setCellStyle(style2);

                    if ( ( RutaAnt.equals("") ) || ( !RutaAnt.equals(ped.getId_ruta()) ) ) {
                        cell_4.setCellValue(Double.parseDouble(ped.getId_ruta()));

                        if ( ( !RutaAnt.equals("") ) && ( !RutaAnt.equals(ped.getId_ruta()) ) ) {
                            hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posRuta, this.numFila - 1, 4, 4));
                        }

                        RutaAnt = ped.getId_ruta();
                        posRuta = this.numFila;
                    }
                    numColDet++;

                    HSSFCell cell_5 = rowDetalle.createCell(numColDet);
                    cell_5.setCellValue(new HSSFRichTextString(ped.getNom_estado_ruta()));
                    cell_5.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_6 = rowDetalle.createCell(numColDet);
                    cell_6.setCellValue(Double.parseDouble(ped.getId_pedido()));
                    cell_6.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_7 = rowDetalle.createCell(numColDet);
                    cell_7.setCellValue(new HSSFRichTextString(ped.getNom_estado_pedido()));
                    cell_7.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_8 = rowDetalle.createCell(numColDet);
                    cell_8.setCellValue(new HSSFRichTextString(ped.getJumboVa().trim()));
                    cell_8.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_9 = rowDetalle.createCell(numColDet);
                    cell_9.setCellValue(Double.parseDouble(ped.getCant_bins()));
                    cell_9.setCellStyle(style3);
                    numColDet++;

                    HSSFCell cell_10 = rowDetalle.createCell(numColDet);
                    cell_10.setCellValue(Double.parseDouble(ped.getVeces_en_ruta()));
                    cell_10.setCellStyle(style3);
                    numColDet++;
                    TotalVecesEnRuta_JumboVa += Integer.parseInt(ped.getVeces_en_ruta());

                    HSSFCell cell_11 = rowDetalle.createCell(numColDet);
                    cell_11.setCellValue(new HSSFRichTextString(ped.getEstatus_entrega()));
                    cell_11.setCellStyle(StyleStatus);
                    numColDet++;

                    HSSFCell cell_12 = rowDetalle.createCell(numColDet);
                    cell_12.setCellValue(new HSSFRichTextString(ped.getResp_incumplimiento()));

                    if ( !ped.getResp_incumplimiento().trim().equals("") ) {
                        HSSFComment comment1 = patr.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 9, this.numFila - 1, (short) 12, this.numFila + 2));

                        HSSFRichTextString string = new HSSFRichTextString("Motivo: " + ped.getMotivo_incumplimiento());
                        string.applyFont(fontNegrita);
                        comment1.setString(string);
                        cell_12.setCellComment(comment1);
                    }
                    cell_12.setCellStyle(style3);
                    numColDet++;

                    if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta > 300 ) ) {
                        HSSFCell cell_13 = rowDetalle.createCell(numColDet);
                        cell_13.setCellValue(Double.parseDouble(ped.getCosto_despacho()));
                        cell_13.setCellStyle(styleMoneda);
                        numColDet++;
                        
                    } else if ( ( loc.getId_local() != 4 ) && ( loc.getId_local() != 6 ) && ( loc.getId_local() != 7 ) && ( loc.getId_local() != 8 ) ) {
                        HSSFCell cell_13 = rowDetalle.createCell(numColDet);
                        cell_13.setCellValue(Double.parseDouble(ped.getCosto_despacho()));
                        cell_13.setCellStyle(styleMoneda);
                        numColDet++;

                    }
                }
                if ( ( CantTotalOP == i + 1 ) && ( CantOP_JumboVa > 0 ) ) {
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posFecha, this.numFila, 1, 1));
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posZona, this.numFila, 2, 2));
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posComuna, this.numFila, 3, 3));
                    hoja.addMergedRegion(new org.apache.poi.hssf.util.CellRangeAddress(posRuta, this.numFila, 4, 4));
                }

            }

            this.numFila2 = 3;

            if ( ( loc.getId_local() == 3 ) || ( loc.getId_local() == 4 ) || ( loc.getId_local() == 7 ) || ( loc.getId_local() == 8 ) ) {
                HSSFRow rowDetalle3 = hoja.getRow((short) this.numFila2);
                HSSFCell cell_3_15 = rowDetalle3.createCell(15);
                if ( loc.getId_local() != 8 ) {
                    cell_3_15.setCellValue(new HSSFRichTextString("Fijo Mensual: "));
                } else {
                    cell_3_15.setCellValue(new HSSFRichTextString("Fijo Diario: "));
                }
                cell_3_15.setCellStyle(style);

                HSSFCell cell_3_16 = rowDetalle3.createCell(16);
                if ( loc.getId_local() == 3 ) {
                    cell_3_16.setCellValue(7000000.0D);
                } else if ( loc.getId_local() == 4 ) {
                    cell_3_16.setCellValue(3617000.0D);
                } else if ( loc.getId_local() == 7 ) {
                    cell_3_16.setCellValue(2400000.0D);
                } else if ( loc.getId_local() == 8 ) {
                    cell_3_16.setCellValue(48000.0D);
                }
                cell_3_16.setCellStyle(styleMoneda);
                this.numFila2 += 1;
            } else if ( ( loc.getId_local() == 6 ) && ( this.TotalVecesEnRuta <= 300 ) ) {
                HSSFRow rowDetalle3 = hoja.getRow((short) this.numFila2);
                HSSFCell cell_3_15 = rowDetalle3.createCell(15);
                cell_3_15.setCellValue(new HSSFRichTextString("Fijo Mensual: "));
                cell_3_15.setCellStyle(style);

                HSSFCell cell_3_16 = rowDetalle3.createCell(16);
                if ( this.TotalVecesEnRuta < 151 ) {
                    cell_3_16.setCellValue(2000000.0D);
                } else if ( ( this.TotalVecesEnRuta > 150 ) && ( this.TotalVecesEnRuta < 301 ) ) {
                    cell_3_16.setCellValue(2200000.0D);
                }
                cell_3_16.setCellStyle(styleMoneda);
                this.numFila2 += 1;
            }

            HSSFRow rowDetalle4 = null;
            if ( hoja.getRow((short) this.numFila2) != null ) {
                rowDetalle4 = hoja.getRow((short) this.numFila2);
            } else {
                rowDetalle4 = hoja.createRow((short) this.numFila2);
            }
            HSSFCell cell_4_15 = rowDetalle4.createCell(15);
            cell_4_15.setCellValue(new HSSFRichTextString("Cantidad de Pedidos: "));
            cell_4_15.setCellStyle(style);

            HSSFCell cell_4_16 = rowDetalle4.createCell(16);
            cell_4_16.setCellValue(CantOP_JumboVa);
            cell_4_16.setCellStyle(style2);
            this.numFila2 += 1;

            HSSFRow rowDetalle5 = null;
            if ( hoja.getRow((short) this.numFila2) != null ) {
                rowDetalle5 = hoja.getRow((short) this.numFila2);
            } else {
                rowDetalle5 = hoja.createRow((short) this.numFila2);
            }
            HSSFCell cell_5_15 = rowDetalle5.createCell(15);
            cell_5_15.setCellValue(new HSSFRichTextString("Total JumboVa: "));
            cell_5_15.setCellStyle(style);

            if ( ( loc.getId_local() == 1 ) || ( loc.getId_local() == 2 ) ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                cell_5_16.setCellFormula("SUM($M$5:$M$" + ( this.numFila + 1 ) + ")");
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 3 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "(($Q$4*" + TotalVecesEnRuta_JumboVa + ")/" + this.TotalVecesEnRuta + ")";
                String ValorVariable = "SUM($M$5:$M$" + ( this.numFila + 1 ) + ")";
                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 4 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "(($Q$4*" + TotalVecesEnRuta_JumboVa + ")/" + this.TotalVecesEnRuta + ")";
                String ValorVariable = "0";
                if ( this.TotalVecesEnRuta > 500 ) {
                    double porcJumboVa = TotalVecesEnRuta_JumboVa / this.TotalVecesEnRuta;
                    double deltaJumboVa = ( this.TotalVecesEnRuta - 500 ) * porcJumboVa;
                    ValorVariable = String.valueOf(deltaJumboVa * 5198.0D);
                }
                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 5 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);

                String ValorVariable = "SUM($M$5:$M$" + ( this.numFila + 1 ) + ")";
                cell_5_16.setCellFormula(ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 6 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "(($Q$4*" + TotalVecesEnRuta_JumboVa + ")/" + this.TotalVecesEnRuta + ")";
                String ValorVariable = "SUM($M$5:$M$" + ( this.numFila + 1 ) + ")";
                if ( this.TotalVecesEnRuta > 300 ) {
                    cell_5_16.setCellFormula(ValorVariable);
                } else {
                    cell_5_16.setCellFormula(ValorFijo);
                }
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 7 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                String ValorFijo = "$Q$4/3";
                String ValorVariable = "";
                if ( DespachosTemucoZ1 > 120 ) {
                    ValorVariable = "(" + ( DespachosTemucoZ1 - 120 ) + " * 2000) + ";
                }
                ValorVariable = ValorVariable + " (" + RutasTemucoZ2.size() + " * 55000)";
                ValorVariable = ValorVariable + " + (" + RutasTemucoZ3Z4.size() + " * 80000)";

                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            } else if ( loc.getId_local() == 8 ) {
                HSSFCell cell_5_16 = rowDetalle5.createCell(16);
                int diasTrabajadosPMontt = DiasDespachoPuertoMontt(calendario);
                String ValorFijo = "(($Q$4 * " + diasTrabajadosPMontt + ")*" + TotalVecesEnRuta_JumboVa + ")/" + this.TotalVecesEnRuta;

                String ValorVariable = String.valueOf(DespachosPMonttZ1_Monto);
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ2_PVaras.size() + " * 25000)";
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ2_Llanquihue.size() + " * 38000)";
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ2_Frutillar.size() + " * 42000)";
                ValorVariable = ValorVariable + " + (" + RutasPMonttZ3.size() + " * 120000)";

                cell_5_16.setCellFormula(ValorFijo + " + " + ValorVariable);
                cell_5_16.setCellStyle(styleMoneda);
            }

            hoja.setColumnWidth(0, 1000);
            hoja.setColumnWidth(1, 2000);

            hoja.setColumnWidth(2, 5000);
            hoja.setColumnWidth(3, 5700);
            hoja.setColumnWidth(4, 2000);
            hoja.autoSizeColumn((short)5);

            hoja.autoSizeColumn((short)6);
            hoja.autoSizeColumn((short)7);

            hoja.setColumnWidth(8, 2000);
            hoja.setColumnWidth(9, 2000);
            hoja.setColumnWidth(10, 3500);
            hoja.setColumnWidth(11, 4000);
            if ( loc.getId_local() != 4 ) {
                hoja.setColumnWidth(12, 2800);
            }

            hoja.setColumnWidth(15, 5200);
            hoja.setColumnWidth(16, 3500);
        } catch (Exception e) {
            logger.error("ERROR: " + getClass() + "." + METHOD_NAME + " : ", e);            
        }
        return wb;
    }

    public int DiasDespachoPuertoMontt( Calendar calendar ) {

        int diasTrabajadosPMontt = 0;

        int diasMes = calendar.getActualMaximum(5);
        int year = calendar.get(1);
        int mes = calendar.get(2);

        for ( int i = 1; i <= diasMes; i++ ) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, mes, i);

            if ( cal.get(7) != 1 ) {
                if ( cal.get(2) == 0 ) {
                    if ( cal.get(5) != 1 ) {
                        diasTrabajadosPMontt++;
                    }
                } else if ( cal.get(2) == 4 ) {
                    if ( cal.get(5) != 1 ) {
                        diasTrabajadosPMontt++;
                    }
                } else if ( cal.get(2) == 8 ) {
                    if ( cal.get(5) != 18 ) {
                        diasTrabajadosPMontt++;
                    }
                } else if ( cal.get(2) == 11 ) {
                    if ( cal.get(5) != 25 ) {
                        diasTrabajadosPMontt++;
                    }
                } else {
                    diasTrabajadosPMontt++;
                }

            }

        }

        return diasTrabajadosPMontt;
    }

    public void CreaZipXLS( String RutaArchivo, String filename ) {

        String METHOD_NAME = "CreaZipXML";
        try {
            File fd = new File(RutaArchivo + filename);
            FileInputStream fis = new FileInputStream(fd);
            String filenames[] = { filename };
            byte buf[] = new byte[1024];
            String outFilename = RutaArchivo + filename.substring(0, filename.indexOf('.')) + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            for ( int i = 0; i < filenames.length; i++ ) {
                out.putNextEntry(new ZipEntry(filenames[i]));
                int len;
                while ( ( len = fis.read(buf) ) > 0 ) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                fis.close();
            }

            out.close();
        } catch (IOException e) {
            logger.error("ERROR: " + getClass() + "." + METHOD_NAME + " : " + e.getMessage(), e);            
        }
    }

    public void EliminaArchivosXLS( String Path ) {

        File dir = new File("temp");

        String[] children = dir.list();
        if ( children == null ) {
            logger.debug("El directorio 'temp' NO Existe o NO ES un Directorio");
        } else {
            for ( int i = 0; i < children.length; i++ ) {
                String filename = children[i];

                boolean exists = new File(Path + filename).exists();
                if ( exists ) {
                    boolean success = new File(Path + filename).delete();
                    if ( success ) {
                        logger.debug("El Archivo '" + filename + "' fue Eliminada con Exito");
                    }
                } else {
                    logger.debug("El Archivo '" + filename + "' NO Existe.");
                }
            }
        }
    }

    public HashMap RecuperaListaArchivos( String Path ) {

        HashMap archivos = new HashMap();

        File dir = new File("temp");

        String[] children = dir.list();
        if ( children == null ) {
            logger.debug("El directorio 'temp' NO Existe o NO ES un Directorio");
        } else {
            for ( int i = 0; i < children.length; i++ ) {
                String filename = children[i];

                String Ext = filename.substring(filename.indexOf('.'));

                if ( !Ext.equalsIgnoreCase(".zip") ) {
                    continue;
                }
                boolean exists = new File(Path + filename).exists();
                if ( exists ) {
                    archivos.put(filename, Path + filename);
                } else {
                    logger.debug("El Archivo '" + filename + "' NO Existe.");
                }
            }
        }

        return archivos;
    }

    public void EnviaEMail( BeanParamConfig param, HashMap archivos ) {

        String METHOD_NAME = "EnviaEMail";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String fecha = dateFormat.format(cal.getTime());
        try {
            Properties prop = System.getProperties();
            prop.put("mail.smtp.host", param.getServer());
            prop.put("mail.smtp.port", param.getPuerto());
            Session ses1 = Session.getDefaultInstance(prop, null);
            MimeMessage msg = new MimeMessage(ses1);
            msg.setFrom(new InternetAddress(param.getFrom()));

            for ( Iterator it = param.getTo().keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(key));
            }

            if ( param.getCc() != null ) {
                for ( Iterator it = param.getCc().keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(key));
                }
            }

            if ( param.getCco() != null ) {
                for ( Iterator it = param.getCco().keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(key));
                }
            }

            msg.setSubject(param.getSubject());

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(param.getMensaje());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            Iterator it = archivos.keySet().iterator();
            while ( it.hasNext() ) {
                String archivo = (String) it.next();

                messageBodyPart = new MimeBodyPart();
                String PathYarchivo = (String) archivos.get(archivo);
                DataSource source = new FileDataSource(PathYarchivo);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(archivo);
                multipart.addBodyPart(messageBodyPart);
            }

            msg.setContent(multipart);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException e) {
            logger.error(fecha + " - ERROR: " + getClass() + "." + METHOD_NAME + " : " + e.getMessage(), e);            
        } catch (Exception e) {
            logger.error(fecha + " - ERROR: " + getClass() + "." + METHOD_NAME + " : " + e.getMessage(), e);
        }
    }

    public void EnviaEMail( BeanParamConfig param, String Path, String NombreArchivo ) {

        String METHOD_NAME = "EnviaEMail";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String fecha = dateFormat.format(cal.getTime());
        try {
            Properties prop = System.getProperties();
            prop.put("mail.smtp.host", param.getServer());
            prop.put("mail.smtp.port", param.getPuerto());
            Session ses1 = Session.getDefaultInstance(prop, null);
            MimeMessage msg = new MimeMessage(ses1);
            msg.setFrom(new InternetAddress(param.getFrom()));

            for ( Iterator it = param.getTo().keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(key));
            }

            if ( param.getCc() != null ) {
                for ( Iterator it = param.getCc().keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    msg.addRecipient(Message.RecipientType.CC, new InternetAddress(key));
                }
            }

            if ( param.getCco() != null ) {
                for ( Iterator it = param.getCco().keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(key));
                }
            }

            msg.setSubject(param.getSubject());

            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(param.getMensaje());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(Path + NombreArchivo);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(NombreArchivo);
            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException e) {
            logger.error(fecha + " - ERROR: " + getClass() + "." + METHOD_NAME + " : " + e.getMessage(), e);            
        } catch (Exception e) {
            logger.error(fecha + " - ERROR: " + getClass() + "." + METHOD_NAME + " : " + e.getMessage(), e);
        }
    }
}

/*
 * Location:D:\EAvendanoA\JUMBO.CL\EXPORT_INTEGRATION_2012\JUMBO_PROCESS\
 * InformePreFacturacion\lib\InformePreFacturacion.jar Qualified Name:
 * cl.cencosud.jumbo.home.Home JD-Core Version: 0.6.0
 */