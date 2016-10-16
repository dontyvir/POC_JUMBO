/*
 * Creado el 08-jul-04
 *
 * Para cambiar la plantilla para este archivo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
package cl.cencosud.jumbo.home;

import cl.cencosud.jumbo.beans.BeanAtrasosXJornada;
import cl.cencosud.jumbo.beans.BeanCumplimientoMensual;
import cl.cencosud.jumbo.beans.BeanEstadisticas;
import cl.cencosud.jumbo.beans.BeanJornada;
import cl.cencosud.jumbo.beans.BeanLocal;
import cl.cencosud.jumbo.beans.BeanParamConfig;
import cl.cencosud.jumbo.beans.BeanPedidosComunaDiario;
import cl.cencosud.jumbo.beans.BeanPedidosComunaMensual;
import cl.cencosud.jumbo.beans.BeanPedidosXComuna;
import cl.cencosud.jumbo.beans.BeanTipoDespachoMensual;
import cl.cencosud.jumbo.beans.PedidoDesp;
import cl.cencosud.jumbo.conexion.ConexionUtil;
import cl.cencosud.jumbo.log.Logging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
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
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


/**
 * @author rbelmar
 *
 * Para cambiar la plantilla para este comentario de tipo generado vaya a
 * Ventana&gt;Preferencias&gt;Java&gt;Generación de código&gt;Código y comentarios
 */
public class SchedulingHome {
	private static SchedulingHome ch = null;
	private Logging logger = new Logging(this);
	private int numFila=0, numFila2=0, numFilaDet=0;
	BeanEstadisticas estadistica = null;
	//private Map Locales = new LinkedHashMap();
	
	public static SchedulingHome getHome() {
		if (ch == null)
			ch = new SchedulingHome();
		return ch;
	}

	
	public HashMap getListadoOP(Calendar cal, int id_local, String hora_desp){
		String METHOD_NAME = "getListadoOP";
		Connection conn = null;
	    HashMap lstPedidos = null;
		Statement stm=null;
		ResultSet rs=null;
		String filtroLocal = "";
		String filtroHoraDesp = "";

		try {
            DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
            String FechaQuery = DateFormatQuery.format(cal.getTime()).toUpperCase();
			
            ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			String SQL = "SELECT DATE(R.FECHA_HORA_SALIDA) AS FECHA "
                       + "      ,R.ID_RUTA "
                       + "      ,P.ID_PEDIDO "
                       + "      ,CASE WHEN P.ORIGEN = 'A' THEN CONCAT('JV', CHAR(PE.NRO_GUIA_CASO)) " // JUMBO VA
                       + "            WHEN P.ORIGEN = 'C' THEN CONCAT('C', CHAR(PE.NRO_GUIA_CASO)) "  // CASO
                       + "            ELSE ' ' "
                       + "       END AS JUMBO_VA "
                       + "      ,TIMESTAMP(CONCAT(CONCAT(CHAR(JD.FECHA), ' '), CHAR(HD.HINI, LOCAL))) AS FINI_DESP "
                       + "      ,TIMESTAMP(CONCAT(CONCAT(CHAR(JD.FECHA), ' '), CHAR(HD.HFIN, LOCAL))) AS FFIN_DESP "
                       + "      ,CONCAT(CONCAT(CONCAT(' ', SUBSTR(CHAR(HD.HINI),1, 2)), ' a '), CONCAT(SUBSTR(CHAR(HD.HFIN), 1, 2), ' ')) AS HORARIO "
                       + "      ,CASE WHEN (P.TIPO_DESPACHO = 'N') THEN ' '  " //NORMAL
                       + "            WHEN (P.TIPO_DESPACHO = 'C') THEN 'E'  " //ECONÓMICO
                       + "            WHEN (P.TIPO_DESPACHO = 'E') THEN 'EX' " //EXPRESS
                       + "            WHEN (P.TIPO_DESPACHO = 'R') THEN 'RL' " //RETIRO EN LOCAL
                       + "       END AS TIPO_DESPACHO "
                       + "      ,CASE WHEN P.ORIGEN IN ('A', 'C') THEN COALESCE(P.MONTO_PEDIDO, 0) "
                       + "            WHEN P.ORIGEN IN ('W', 'V') THEN COALESCE(TP.POS_MONTO, 0) "
                       + "            ELSE 0 "
                       + "       END AS MONTO "
                       + "      ,CASE WHEN (TP.POS_FECHA IS NULL) THEN '-' "
                       + "            ELSE CONCAT(CONCAT(CONCAT(CONCAT(SUBSTR(TP.POS_FECHA, 5,2), '/'), SUBSTR(TP.POS_FECHA, 3,2)), '/20'), SUBSTR(TP.POS_FECHA, 1,2)) "
                       + "       END AS POS_FECHA "
                       + "      ,CASE WHEN (TP.POS_HORA IS NULL) THEN '-' "
                       + "            ELSE CONCAT(CONCAT(CONCAT(CONCAT(SUBSTR(TP.POS_HORA, 1,2), ':'), SUBSTR(TP.POS_HORA, 3,2)), ':'), SUBSTR(TP.POS_HORA, 5,2)) "
                       + "       END AS POS_HORA "
                       + "      ,CASE WHEN (P.CANT_BINS IS NULL) THEN 0 "
                       + "            ELSE P.CANT_BINS "
                       + "       END AS CANT_BINS "
                       + "      ,' ' AS HORA_RECEP_BOL "
                       //+ "      ,SUBSTR(CHAR(TIME(R.FECHA_HORA_SALIDA), LOCAL), 1, 5) AS SALIDA_CAMION "
                       //+ "      ,SUBSTR(CHAR(TIME(PE.FECHA_HORA_LLEGADA_DOM), LOCAL), 1, 5) AS LLEGADA_DOM "
                       //+ "      ,SUBSTR(CHAR(TIME(PE.FECHA_HORA_SALIDA_DOM), LOCAL), 1, 5) AS SALIDA_DOM "
                       + "      ,R.FECHA_HORA_SALIDA AS SALIDA_CAMION "
                       + "      ,PE.FECHA_HORA_LLEGADA_DOM AS LLEGADA_DOM "
                       + "      ,PE.FECHA_HORA_SALIDA_DOM AS SALIDA_DOM "
                       + "      ,TIMESTAMPDIFF (4, CHAR( PE.FECHA_HORA_SALIDA_DOM -  PE.FECHA_HORA_LLEGADA_DOM)) AS TIEMPO_ENTREGA "
                       + "      ,CT.NOMBRE_CHOFER "
                       + "      ,UPPER(PT.PATENTE) AS PATENTE "
                       + "      ,CASE WHEN (PE.CUMPLIMIENTO = 'T') THEN 'A TIEMPO' "
                       + "            WHEN (PE.CUMPLIMIENTO = 'R') THEN 'RETRASADO' "
                       + "            WHEN (PE.CUMPLIMIENTO = 'A') THEN 'ADELANTADO' "
                       + "       END AS ESTATUS_ENTREGA "
                       + "      ,COALESCE(RD.ID_RESPONSABLE_DESP, 0) AS ID_RESP_INCUMPLIMIENTO "
                       + "      ,COALESCE(RD.NOMBRE_RESPONSABLE,'') AS RESP_INCUMPLIMIENTO "
                       + "      ,COALESCE(MD.MOTIVO, '') AS MOTIVO_INCUMPLIMIENTO "
                       + "      ,UPPER(P.NOM_CLIENTE) AS NOM_CLIENTE "
                       + "      ,CASE WHEN (P.DIR_ID IS NULL) THEN UPPER(P.DIR_CALLE) "
                       + "            ELSE CASE WHEN (P.DIR_DEPTO = '') THEN CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(', P.DIR_TIPO_CALLE), ') '), UPPER(P.DIR_CALLE)), '  #'), P.DIR_NUMERO) "
                       + "                      ELSE CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(', P.DIR_TIPO_CALLE), ') '), UPPER(P.DIR_CALLE)), '  #'), P.DIR_NUMERO), '   DEPTO. Nº '), P.DIR_DEPTO) "
                       + "                 END "
                       + "       END AS DIRECCION "
                       + "      ,C.NOMBRE AS COMUNA "
                       + "      ,CASE WHEN (P.ID_CLIENTE IS NULL) THEN CONCAT(CONCAT(P.TELEFONO, '  /  '), P.TELEFONO2) "
                       + "            ELSE CASE WHEN (COALESCE(CLI.CLI_FON_NUM_2,'') = '' AND COALESCE(CLI.CLI_FON_NUM_3,'') = '') THEN "
                       + "                           CONCAT(CONCAT(CONCAT('(',CLI.CLI_FON_COD_1), ') '), CLI.CLI_FON_NUM_1) "
                       + "                      WHEN (COALESCE(CLI.CLI_FON_NUM_3,'') = '') THEN "
                       + "                           CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(',CLI.CLI_FON_COD_1), ') '), CLI.CLI_FON_NUM_1), '  /  '), '('), CLI.CLI_FON_COD_2), ') '), CLI.CLI_FON_NUM_2) "
                       + "                      ELSE CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT('(',CLI.CLI_FON_COD_1), ') '), CLI.CLI_FON_NUM_1), '  /  '), '('), CLI.CLI_FON_COD_2), ') '), CLI.CLI_FON_NUM_2), '  /  '), '('), CLI.CLI_FON_COD_3), ') '), CLI.CLI_FON_NUM_3) "
                       + "                 END "     
                       + "       END AS TELEFONOS "
                       + "      ,CASE WHEN (UPPER(P.NOM_TBANCARIA) <> '[NULL]') THEN UPPER(P.NOM_TBANCARIA) "
                       + "            ELSE 'CHEQUE' "
                       + "       END AS CONFIRMACION "
                       + "      ,CASE WHEN (PE.REPROGRAMADA > 0) THEN 'SÍ' "
                       + "            ELSE 'NO' "
                       + "       END AS REPROGRAMACION "
                       + "      ,COALESCE(PE.REPROGRAMADA, 0) AS NUM_REPROGRAMACIONES "
                       + "      ,COALESCE(RDESP.NOMBRE_RESPONSABLE,'') AS NOMBRE_RESPONSABLE "
                       + "FROM BODBA.BO_RUTA R "
                       + "     LEFT JOIN BODBA.BO_PEDIDOS_EXT           PE ON PE.ID_RUTA             = R.ID_RUTA "
                       + "     JOIN BODBA.BO_PEDIDOS                     P ON P.ID_PEDIDO            = PE.ID_PEDIDO "
                       + "     JOIN BODBA.BO_JORNADA_DESP               JD ON JD.ID_JDESPACHO        = P.ID_JDESPACHO "
                       + "     JOIN BODBA.BO_HORARIO_DESP               HD ON HD.ID_HOR_DESP         = JD.ID_HOR_DESP "
                       + "     LEFT JOIN (SELECT TP1.ID_PEDIDO, SUM(TP1.POS_MONTO_FP) AS POS_MONTO, " 
                       + "                       MAX(TP1.POS_FECHA) AS POS_FECHA, MAX(TP1.POS_HORA) AS POS_HORA "
                       + "                FROM BODBA.BO_TRX_MP TP1 "
                       + "                GROUP BY TP1.ID_PEDIDO "
                       + "                ) AS TP ON TP.ID_PEDIDO = P.ID_PEDIDO "
                       + "     JOIN BODBA.BO_CHOFER_TRANS               CT ON CT.ID_CHOFER_TRANS     = R.ID_CHOFER_TRANS "
                       + "     JOIN BODBA.BO_PATENTE_TRANS              PT ON PT.ID_PATENTE_TRANS    = R.ID_PATENTE_TRANS " 
                       + "     LEFT JOIN BODBA.BO_MOTIVO_DESPACHO       MD ON MD.ID_MOTIVO_DESP      = PE.ID_MOTIVO_CUMPLIMIENTO "
                       + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO  RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                       + "     LEFT JOIN BODBA.BO_COMUNAS                C ON C.ID_COMUNA            = P.ID_COMUNA "
                       + "     LEFT JOIN FODBA.FO_CLIENTES             CLI ON CLI.CLI_ID             = P.ID_CLIENTE "
                       + "     LEFT JOIN (SELECT RD1.ID_PEDIDO, MAX(RD1.ID_REPROGRAMACION) AS ID_REP, " 
                       + "                       RD1.ID_MOTIVO_DESP, MD1.MOTIVO, " 
                       + "                       RD1.ID_RESPONSABLE_DESP, RPD1.NOMBRE_RESPONSABLE "
                       + "                FROM BODBA.BO_REPROGRAMACION_DESP RD1 "
                       + "                     JOIN BODBA.BO_MOTIVO_DESPACHO      MD1 ON MD1.ID_MOTIVO_DESP = RD1.ID_MOTIVO_DESP "
                       + "                     JOIN BODBA.BO_RESPONSABLE_DESPACHO RPD1 ON RPD1.ID_RESPONSABLE_DESP = RD1.ID_RESPONSABLE_DESP "
                       + "                GROUP BY RD1.ID_PEDIDO, RD1.ID_MOTIVO_DESP, MD1.MOTIVO, RD1.ID_RESPONSABLE_DESP, RPD1.NOMBRE_RESPONSABLE "
                       + "                ) RDESP ON RDESP.ID_PEDIDO = P.ID_PEDIDO "
                       + "WHERE R.ID_ESTADO = 67 " //RUTA FINALIZADA
                       + "  AND DATE(R.FECHA_HORA_SALIDA) = DATE('" + FechaQuery + "') "
                       //+ "  AND DATE(R.FECHA_HORA_SALIDA) = DATE('2010-04-06') "
                       + "  AND P.ID_LOCAL = " + id_local + " "
                       //+ "  AND P.ID_LOCAL = 2 "
                       + "ORDER BY DATE(R.FECHA_HORA_SALIDA), R.ID_RUTA, CHAR(TIME(R.FECHA_HORA_SALIDA), LOCAL), "
                       + "         CHAR(TIME(PE.FECHA_HORA_LLEGADA_DOM), LOCAL), CHAR(TIME(PE.FECHA_HORA_SALIDA_DOM), LOCAL)";
			logger.debug("Query (getListadoOP - Planilla Tracking): " + SQL);

			stm = conn.createStatement();
			rs = stm.executeQuery(SQL);
			lstPedidos = new HashMap();
			int i=0;
			int ruta_ant = 0;
			int ruta_act = 0;
			
			int aTiempo = 0;
			int AtrasosLocal = 0;
			int AtrasosTransporte = 0;
			int AtrasosSistemas = 0;
			int AtrasosCliente = 0;
			int TipoDespN  = 0;
			int TipoDespE  = 0;
			int TipoDespEX = 0;
			int TipoDespRL = 0;
			
			HashMap ListaPedidosComuna = new HashMap();
			HashMap ListaAtrasosJornada = new HashMap();
			//HashMap ListaTipoDespacho = new HashMap();
			
			Timestamp salida_dom_ant = null;
			while (rs.next()){
			    i++;
			    PedidoDesp ped = new PedidoDesp();
			    ped.setFecha(rs.getString("FECHA")); //FECHA DE DESPACHO
				ped.setId_ruta(rs.getString("ID_RUTA"));
				ped.setId_pedido(rs.getString("ID_PEDIDO"));
				ped.setJumbo_va(rs.getString("JUMBO_VA"));
				ped.setHorario(rs.getString("HORARIO").trim());
				ped.setTipo_despacho(rs.getString("TIPO_DESPACHO")); //HORARIO DE DESPACHO
				ped.setMonto(rs.getString("MONTO"));
				ped.setPos_fecha(rs.getString("POS_FECHA"));
				ped.setPos_hora(rs.getString("POS_HORA"));
				ped.setCant_bins(rs.getString("CANT_BINS"));
				ped.setSalida_camion(FormatoHora(rs.getTimestamp("SALIDA_CAMION")));
				ped.setLlegada_dom(FormatoHora(rs.getTimestamp("LLEGADA_DOM")));
				ped.setSalida_dom(FormatoHora(rs.getTimestamp("SALIDA_DOM")));
				
				//GregorianCalendar date1 = new GregorianCalendar();
				//date1.setTime(rs.getTimestamp("SALIDA_DOM"));
				String ptoApto="";
				ruta_act = rs.getInt("ID_RUTA");
				if ((ruta_ant == 0) || (ruta_ant != ruta_act)){ //primer registro
				    ptoApto = DiferenciaHoras(rs.getTimestamp("SALIDA_CAMION"), rs.getTimestamp("LLEGADA_DOM"));
				    salida_dom_ant = rs.getTimestamp("SALIDA_DOM");
				} else if (ruta_ant == ruta_act){
				    ptoApto = DiferenciaHoras(salida_dom_ant, rs.getTimestamp("LLEGADA_DOM"));
				    salida_dom_ant = rs.getTimestamp("SALIDA_DOM");
				}
				ped.setPto_a_pto(ptoApto);
				
				ped.setTiempo_entrega(rs.getString("TIEMPO_ENTREGA"));
				ped.setNombre_chofer(rs.getString("NOMBRE_CHOFER"));
				ped.setPatente(rs.getString("PATENTE"));
				ped.setEstatus_entrega(rs.getString("ESTATUS_ENTREGA"));
				ped.setResp_incumplimiento(rs.getString("RESP_INCUMPLIMIENTO"));
				ped.setMotivo_incumplimiento(rs.getString("MOTIVO_INCUMPLIMIENTO"));
				ped.setNom_cliente(rs.getString("NOM_CLIENTE"));
				ped.setDireccion(rs.getString("DIRECCION"));
				ped.setComuna(rs.getString("COMUNA"));
				ped.setTelefonos(rs.getString("TELEFONOS"));
				ped.setConfirmacion(rs.getString("CONFIRMACION"));
				ped.setReprogramacion(rs.getString("REPROGRAMACION"));
				ped.setNum_reprogramaciones(rs.getString("NUM_REPROGRAMACIONES"));
				ped.setNombre_responsable(rs.getString("NOMBRE_RESPONSABLE"));
				lstPedidos.put(new Integer(i), ped);
				ruta_ant = ruta_act;
				
				/***************************************************************/
				/*****************   E S T A D I S T I C A S   *****************/
				/***************************************************************/
				/***************************************************************/
				/*****************   C U M P L I M I E N T O   *****************/
				/***************************************************************/
				if (rs.getInt("ID_RESP_INCUMPLIMIENTO") == 1 || rs.getInt("ID_RESP_INCUMPLIMIENTO") == 4 ||
				        rs.getInt("ID_RESP_INCUMPLIMIENTO") == 7 || rs.getInt("ID_RESP_INCUMPLIMIENTO") == 8){
				    AtrasosLocal++;
				}else if (rs.getInt("ID_RESP_INCUMPLIMIENTO") == 3){
				    AtrasosTransporte++;
				}else if (rs.getInt("ID_RESP_INCUMPLIMIENTO") == 5){
				    AtrasosSistemas++;
				}else if (rs.getInt("ID_RESP_INCUMPLIMIENTO") == 6){
				    AtrasosCliente++;
				}else if (rs.getInt("ID_RESP_INCUMPLIMIENTO") == 0){
				    aTiempo++;
				}
				
				/***************************************************************/
				/***********   P E D I D O S   P O R   C O M U N A   ***********/
				/***************************************************************/
				if (ListaPedidosComuna.get(ped.getComuna()) == null){
				    BeanPedidosXComuna pcom = new BeanPedidosXComuna();
				    pcom.setCantOP(1);
				    pcom.setComuna(ped.getComuna());
				    ListaPedidosComuna.put(ped.getComuna(), pcom);
				}else{
				    BeanPedidosXComuna pcom = (BeanPedidosXComuna)ListaPedidosComuna.get(rs.getString("COMUNA"));
				    double suma = pcom.getCantOP() + 1;
				    pcom.setCantOP(suma);
				}
				
				/***************************************************************/
				/***********   A T R A S O   P O R   J O R N A D A   ***********/
				/***************************************************************/
				int minutosAtraso = 0;
				if (ped.getEstatus_entrega().equals("RETRASADO")){
			        //calcular los minutos entre la hora de fin de la jornada
			        //y el tiempo de llegada a la casa
			        minutosAtraso = DiferenciaHorasEnMinutos(rs.getTimestamp("FFIN_DESP"), rs.getTimestamp("LLEGADA_DOM"));
				}else if (ped.getEstatus_entrega().equals("ADELANTADO")){
			        //calcular los minutos entre la hora de llegada a la casa 
			        //y la hora de inicio de la jornada
				    minutosAtraso = DiferenciaHorasEnMinutos(rs.getTimestamp("LLEGADA_DOM"), rs.getTimestamp("FINI_DESP"));
				}
				
				
				if (!ped.getEstatus_entrega().equalsIgnoreCase("A TIEMPO")){
				    if (ListaAtrasosJornada.get(ped.getHorario()) == null){
				        BeanAtrasosXJornada ajor = new BeanAtrasosXJornada();
				        ajor.setJornada(ped.getHorario());
				        ajor.setAtrasoTotal(minutosAtraso);
				        ajor.setCantOP(1);
				        ajor.setAtrasoMaximo(minutosAtraso);
				        ListaAtrasosJornada.put(ped.getHorario(), ajor);
					}else{
					    BeanAtrasosXJornada ajor = (BeanAtrasosXJornada) ListaAtrasosJornada.get(ped.getHorario());
					    ajor.setAtrasoTotal(ajor.getAtrasoTotal() + minutosAtraso);
					    ajor.setCantOP(ajor.getCantOP() + 1);
					    if (ajor.getAtrasoMaximo() < minutosAtraso){
					        ajor.setAtrasoMaximo(minutosAtraso);
					    }
					}

                    //  REPROGRAMACIONES
			        if (ped.getReprogramacion().equalsIgnoreCase("SÍ")){
			            if (ListaAtrasosJornada.get("REPROGRAMADAS") == null){
			                BeanAtrasosXJornada rep = new BeanAtrasosXJornada();
			                rep.setJornada(ped.getHorario());
			                rep.setAtrasoTotal(minutosAtraso);
			                rep.setCantOP(1);
			                rep.setAtrasoMaximo(minutosAtraso);
			                ListaAtrasosJornada.put("REPROGRAMADAS", rep);
			            }else{
			                BeanAtrasosXJornada rep = (BeanAtrasosXJornada) ListaAtrasosJornada.get("REPROGRAMADAS");
			                rep.setAtrasoTotal(rep.getAtrasoTotal() + minutosAtraso);
			                rep.setCantOP(rep.getCantOP() + 1);
						    if (rep.getAtrasoMaximo() < minutosAtraso){
						        rep.setAtrasoMaximo(minutosAtraso);
						    }
			            }
			        }				    
				}
				
				/***************************************************************/
				/*************   T I P O   D E   D E S P A C H O   *************/
				/***************************************************************/
				// TIPO DE DESPACHO "NORMAL"
				if (ped.getTipo_despacho().equalsIgnoreCase(" ")){
                    TipoDespN++;
    			
                // TIPO DE DESPACHO "ECONOMICO"
				}else if (ped.getTipo_despacho().equalsIgnoreCase("E")){
				    TipoDespE++;

				// TIPO DE DESPACHO "EXPRESS"
				}else if (ped.getTipo_despacho().equalsIgnoreCase("EX")){
				    TipoDespEX++;

				// TIPO DE DESPACHO "RETIRO EN LOCAL"
				}else if (ped.getTipo_despacho().equalsIgnoreCase("RL")){
				    TipoDespRL++;
				}

				/***************************************************************/
				
			}
			int TotalPedidos = i;
			
			estadistica = new BeanEstadisticas();
			estadistica.setCantidadTotalOP(TotalPedidos);
			estadistica.setCumplimientoATiempo(aTiempo);
			estadistica.setCumplimientoAtrasosLocal(AtrasosLocal);
			estadistica.setCumplimientoAtrasosTransporte(AtrasosTransporte);
			estadistica.setCumplimientoAtrasosSistemas(AtrasosSistemas);
			estadistica.setCumplimientoAtrasosCliente(AtrasosCliente);
			estadistica.setCumplimientoTotal(aTiempo + AtrasosLocal + AtrasosTransporte + AtrasosSistemas + AtrasosCliente);
			estadistica.setAtrasosXJornada(ListaAtrasosJornada);
			estadistica.setPedidosXComuna(ListaPedidosComuna);
			estadistica.setTipoDespN(TipoDespN);
			estadistica.setTipoDespE(TipoDespE);
			estadistica.setTipoDespEX(TipoDespEX);
			estadistica.setTipoDespRL(TipoDespRL);
			
			
			/*01.-FECHA
              02.-ID_RUTA
              03.-ID_PEDIDO
              04.-JUMBO_VA
              05.-HORARIO
              06.-TIPO_DESPACHO
              07.-MONTO
              08.-POS_FECHA
              09.-POS_HORA
              10.-CANT_BINS
              11.-SALIDA_CAMION
              12.-LLEGADA_DOM
              13.-SALIDA_DOM
              14.-TIEMPO_ENTREGA
              15.-NOMBRE_CHOFER
              16.-PATENTE
              17.-ESTATUS_ENTREGA
              18.-RESP_INCUMPLIMIENTO
              19.-MOTIVO_INCUMPLIMIENTO
              20.-NOM_CLIENTE
              21.-DIRECCION
              22.-COMUNA
              23.-TELEFONOS
              24.-CONFIRMACION
              25.-REPROGRAMACION
              26.-NUM_REPROGRAMACIONES
              27.-NOMBRE_RESPONSABLE*/

			rs.close();
			stm.close();
		} catch (SQLException e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": SQLException: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		}catch(Exception e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
                if (rs != null)  rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		logger.debug("Se listaron:"+lstPedidos.size());
		return lstPedidos;

	}
	
	
	public String DiferenciaHoras(Timestamp Hora1, Timestamp Hora2){
	    
        GregorianCalendar date1 = new GregorianCalendar();
        date1.setTime(Hora1);
        
        GregorianCalendar date2 = new GregorianCalendar();
        date2.setTime(Hora2);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();


        //obtenemos los segundos
        long segundos = diff / 1000;
      
        //obtenemos las horas
        long horas = segundos / 3600;
      
        //restamos las horas para continuar con minutos
        segundos -= horas*3600;
      
        //igual que el paso anterior
        long minutos = segundos /60;
        segundos -= minutos*60;
      
        if (segundos > 30){
            minutos++;
        }
        if (minutos > 59){
            horas++;
            minutos=0;
        }

        //ponemos los resultados en un mapa :-)
        //System.out.println("horas   : " + Long.toString(horas));
        //System.out.println("minutos : " + Long.toString(minutos));
        //System.out.println("segundos: " + Long.toString(segundos));

	    return (Long.toString(horas) + ":" + Long.toString(minutos));
	}

	
	public int DiferenciaHorasEnMinutos(Timestamp Hora1, Timestamp Hora2){
	    
        GregorianCalendar date1 = new GregorianCalendar();
        date1.setTime(Hora1);
        
        GregorianCalendar date2 = new GregorianCalendar();
        date2.setTime(Hora2);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();


        //obtenemos los segundos
        long segundos = diff / 1000;
      
        //obtenemos las horas
        //long horas = segundos / 3600;
      
        //restamos las horas para continuar con minutos
        //segundos -= horas*3600;
      
        //igual que el paso anterior
        long minutos = segundos /60;
        segundos -= minutos*60;
        
        if (segundos > 30){
            minutos++;
        }

        //ponemos los resultados en un mapa :-)
        //System.out.println("horas   : " + Long.toString(horas));
        //System.out.println("minutos : " + );
        //System.out.println("segundos: " + Long.toString(segundos));

	    return (Integer.parseInt(Long.toString(minutos)));
	}

	
	public String FormatoHora(Timestamp fecha){
	    GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(fecha);
        
	    DateFormat DateFormatQuery  = new SimpleDateFormat("HH:mm", new Locale("es", "ES", ""));
        String Hora = DateFormatQuery.format(cal.getTime()).toUpperCase();
        
	    return Hora;
	}
	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public List listadoLocales(){
		String METHOD_NAME = "listadoLocales";
		Connection conn = null;
		List lista_locales = new ArrayList();
		BeanLocal loc = null;
		
		Statement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en listadoLocales");
			String sql = "SELECT L.ID_LOCAL, L.COD_LOCAL, L.NOM_LOCAL "
                       + "FROM BODBA.BO_LOCALES L "
                       + "ORDER BY L.ID_LOCAL ASC";
			logger.debug(sql);

			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			while (rs.next()) {
				loc = new BeanLocal();
				loc.setId_local(rs.getInt("id_local"));
				loc.setCod_local(rs.getString("cod_local"));
				loc.setNom_local(rs.getString("nom_local"));
				lista_locales.add(loc);
			}
			rs.close();
			stm.close();
		} catch (Exception e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_locales;
	}

	
	
	/**
	 * Da formato a una fecha de tipo Date
	 * 
	 * @param  fecha
	 * @return String
	 */
	public String frmFechaByDate(Date fecha){
	    String METHOD_NAME = "frmFechaByDate";
		try{
			SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyyMMdd");
			return bartDateFormat.format(fecha);
		}catch(Exception e){
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
			return "";
		}
	}


	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public HashMap ResumenMensualCumplimiento(int local, Calendar calendario){
		String METHOD_NAME = "ResumenMensualCumplimiento";
        DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
        String FechaQuery = DateFormatQuery.format(calendario.getTime()).toUpperCase();
		Connection conn = null;
		HashMap lista_Cumplimientos = new HashMap();
		Statement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en ResumenMensualCumplimiento");
			String sql = "SELECT DATE(R.FECHA_HORA_SALIDA) AS FECHA "
                       + "      ,COUNT(P.ID_PEDIDO) AS CANT_OP "
                       + "      ,CASE WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 3) THEN 'TRANSPORTE' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 5) THEN 'SISTEMAS' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 6) THEN 'CLIENTE' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 0) THEN 'A TIEMPO' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) IN (1,4,7,8)) THEN 'LOCAL' "
                       + "      END AS RESP_INCUMPLIMIENTO "
                       + "FROM BODBA.BO_RUTA R "
                       + "     LEFT JOIN BODBA.BO_PEDIDOS_EXT           PE ON PE.ID_RUTA             = R.ID_RUTA "
                       + "     JOIN BODBA.BO_PEDIDOS                     P ON P.ID_PEDIDO            = PE.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO  RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                       + "     JOIN FODBA.FO_DIRECCIONES                 D ON D.DIR_ID               = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO                  POL ON POL.ID_POLIGONO        = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS                       Z ON Z.ID_ZONA              = POL.ID_ZONA "
                       + "WHERE R.ID_ESTADO = 67 "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND MONTH(R.FECHA_HORA_SALIDA) = MONTH('" + FechaQuery + "') "
                       + "GROUP BY DATE(R.FECHA_HORA_SALIDA), RD.ID_RESPONSABLE_DESP "
                       + "ORDER BY DATE(R.FECHA_HORA_SALIDA), RESP_INCUMPLIMIENTO";
			logger.debug(sql);

			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			while (rs.next()) {
			    String keyFecha = frmFechaByDate(rs.getDate("FECHA"));
			    if (lista_Cumplimientos.get(keyFecha) == null){
			        BeanCumplimientoMensual cump = new BeanCumplimientoMensual();
			        
			        cump.setFecha(rs.getDate("FECHA"));
					if (rs.getString("RESP_INCUMPLIMIENTO").equals("LOCAL")){
					    cump.setAtrasosLocal(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("TRANSPORTE")){
					    cump.setAtrasosTransporte(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("SISTEMAS")){
					    cump.setAtrasosSistemas(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("CLIENTE")){
					    cump.setAtrasosCliente(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("A TIEMPO")){
					    cump.setATiempo(rs.getInt("CANT_OP"));
					}
					
					lista_Cumplimientos.put(keyFecha, cump);
			    }else{
			        BeanCumplimientoMensual cump = (BeanCumplimientoMensual)lista_Cumplimientos.get(keyFecha);
			        
					if (rs.getString("RESP_INCUMPLIMIENTO").equals("LOCAL")){
					    cump.setAtrasosLocal(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("TRANSPORTE")){
					    cump.setAtrasosTransporte(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("SISTEMAS")){
					    cump.setAtrasosSistemas(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("CLIENTE")){
					    cump.setAtrasosCliente(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("A TIEMPO")){
					    cump.setATiempo(rs.getInt("CANT_OP"));
					}

			        lista_Cumplimientos.put(keyFecha, cump);
			    }
			}
			rs.close();
			stm.close();
		} catch (Exception e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_Cumplimientos;
	}

	
	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public HashMap ResumenMensualPedidosReprogramados(int local, Calendar calendario){
		String METHOD_NAME = "ResumenMensualPedidosReprogramados";
        DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
        String FechaQuery = DateFormatQuery.format(calendario.getTime()).toUpperCase();

		Connection conn = null;
		HashMap lista_PedRep = new HashMap();
		
		Statement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en ResumenMensualPedidosReprogramados");
			/*String sql = "SELECT JD.FECHA, COUNT(P.ID_PEDIDO) AS CANT_OP "
                       + "      ,CASE WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 3) THEN 'TRANSPORTE' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 5) THEN 'SISTEMAS' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 6) THEN 'CLIENTE' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 0) THEN 'A TIEMPO' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP, 0) IN (1,4,7,8)) THEN 'LOCAL' "
                       + "      END AS RESP_INCUMPLIMIENTO "
                       + "FROM BODBA.BO_REPROGRAMACION_DESP RDESP " 
                       + "     JOIN BODBA.BO_JORNADA_DESP JD ON JD.ID_JDESPACHO = RDESP.ID_JDESPACHO_ANT "
                       + "     JOIN BODBA.BO_PEDIDOS       P ON P.ID_PEDIDO     = RDESP.ID_PEDIDO "
                       + "     JOIN BODBA.BO_PEDIDOS_EXT  PE ON PE.ID_PEDIDO    = P.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO  RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                       + "     JOIN FODBA.FO_DIRECCIONES   D ON D.DIR_ID        = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO    POL ON POL.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS         Z ON Z.ID_ZONA       = POL.ID_ZONA "
                       + "WHERE MONTH(JD.FECHA) =  MONTH(CURRENT DATE) "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "GROUP BY JD.FECHA, RD.ID_RESPONSABLE_DESP "
                       + "ORDER BY JD.FECHA, RD.ID_RESPONSABLE_DESP";*/
			
			String sql = "SELECT JDR.FECHA AS FECHA, COUNT(P.ID_PEDIDO) AS CANT_OP "
                       + "      ,CASE WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) = 3) THEN 'TRANSPORTE' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) = 5) THEN 'SISTEMAS' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) IN (1,4,7,8)) THEN 'LOCAL' "
                       + "      END AS RESP_INCUMPLIMIENTO "
                       + "FROM BODBA.BO_REPROGRAMACION_DESP RDESP " 
                       + "     JOIN BODBA.BO_JORNADA_DESP              JDR ON JDR.ID_JDESPACHO       = RDESP.ID_JDESPACHO_ANT "
                       + "     JOIN BODBA.BO_PEDIDOS                     P ON P.ID_PEDIDO            = RDESP.ID_PEDIDO "
                       + "     JOIN BODBA.BO_PEDIDOS_EXT                PE ON PE.ID_PEDIDO           = P.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO  RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                       + "     JOIN FODBA.FO_DIRECCIONES                 D ON D.DIR_ID               = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO                  POL ON POL.ID_POLIGONO        = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS                       Z ON Z.ID_ZONA              = POL.ID_ZONA "
                       + "WHERE MONTH(JDR.FECHA) =  MONTH('" + FechaQuery + "') "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND RD.ID_RESPONSABLE_DESP IS NOT NULL "
                       + "  AND COALESCE(RD.ID_RESPONSABLE_DESP, 0) <> 6 "
                       + "GROUP BY JDR.FECHA, RD.ID_RESPONSABLE_DESP "
                       + " "
                       + "UNION "
                       + " "
                       + "SELECT DISTINCT MAX(JDR.FECHA) AS FECHA, COUNT(P.ID_PEDIDO) AS CANT_OP, 'CLIENTE' AS RESP_INCUMPLIMIENTO "
                       + "FROM BODBA.BO_REPROGRAMACION_DESP RDESP " 
                       + "     JOIN BODBA.BO_JORNADA_DESP              JDR ON JDR.ID_JDESPACHO       = RDESP.ID_JDESPACHO_ANT "
                       + "     JOIN BODBA.BO_PEDIDOS                     P ON P.ID_PEDIDO            = RDESP.ID_PEDIDO "
                       + "     JOIN BODBA.BO_JORNADA_DESP              JDP ON JDP.ID_JDESPACHO       = P.ID_JDESPACHO "
                       + "     JOIN BODBA.BO_PEDIDOS_EXT                PE ON PE.ID_PEDIDO           = P.ID_PEDIDO "
                       + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO  RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                       + "     JOIN FODBA.FO_DIRECCIONES                 D ON D.DIR_ID               = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO                  POL ON POL.ID_POLIGONO        = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS                       Z ON Z.ID_ZONA              = POL.ID_ZONA "
                       + "WHERE MONTH(JDR.FECHA) =  MONTH('" + FechaQuery + "') "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND COALESCE(RD.ID_RESPONSABLE_DESP, 0) = 6 "
                       + "  AND (DAYS(JDP.FECHA)-DAYS(JDR.FECHA)) > 1 "
                       + "GROUP BY JDR.FECHA, JDP.FECHA, RD.ID_RESPONSABLE_DESP "
                       + " "
                       + "ORDER BY FECHA, RESP_INCUMPLIMIENTO";
			logger.debug(sql);

			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			while (rs.next()) {
			    String keyFecha = frmFechaByDate(rs.getDate("FECHA"));
			    if (lista_PedRep.get(keyFecha) == null){
			        BeanCumplimientoMensual cump = new BeanCumplimientoMensual();
			        
			        cump.setFecha(rs.getDate("FECHA"));
					if (rs.getString("RESP_INCUMPLIMIENTO").equals("LOCAL")){
					    cump.setAtrasosLocal(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("TRANSPORTE")){
					    cump.setAtrasosTransporte(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("SISTEMAS")){
					    cump.setAtrasosSistemas(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("CLIENTE")){
					    cump.setAtrasosCliente(rs.getInt("CANT_OP"));
					}
					
					lista_PedRep.put(keyFecha, cump);
			    }else{
			        BeanCumplimientoMensual cump = (BeanCumplimientoMensual)lista_PedRep.get(keyFecha);
			        
					if (rs.getString("RESP_INCUMPLIMIENTO").equals("LOCAL")){
					    cump.setAtrasosLocal(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("TRANSPORTE")){
					    cump.setAtrasosTransporte(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("SISTEMAS")){
					    cump.setAtrasosSistemas(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("CLIENTE")){
					    cump.setAtrasosCliente(rs.getInt("CANT_OP"));
					}

					lista_PedRep.put(keyFecha, cump);
			    }
			}
			rs.close();
			stm.close();
		} catch (Exception e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_PedRep;
	}


	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public HashMap ResumenMensualCumplimientoPedidosReprogramados(int local, Calendar calendario){
		String METHOD_NAME = "ResumenMensualCumplimientoPedidosReprogramados";
        DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
        String FechaQuery = DateFormatQuery.format(calendario.getTime()).toUpperCase();

		Connection conn = null;
		HashMap lista_CumpRep = new HashMap();
		
		Statement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en ResumenMensualCumplimientoPedidosReprogramados");
			String sql = "SELECT DATE(R.FECHA_HORA_SALIDA) AS FECHA "
                       + "      ,COUNT(P.ID_PEDIDO) AS CANT_OP "
                       + "      ,CASE WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) = 3) THEN 'TRANSPORTE' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) = 5) THEN 'SISTEMAS' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) = 0) THEN 'A TIEMPO' "
                       + "            WHEN (COALESCE(RD.ID_RESPONSABLE_DESP,  0) IN (1,4,7,8)) THEN 'LOCAL' "
                       + "      END AS RESP_INCUMPLIMIENTO "
                       + "FROM BODBA.BO_RUTA R "
                       + "     LEFT JOIN BODBA.BO_PEDIDOS_EXT          PE ON PE.ID_RUTA             = R.ID_RUTA "
                       + "     LEFT JOIN BODBA.BO_RESPONSABLE_DESPACHO RD ON RD.ID_RESPONSABLE_DESP = PE.ID_RESPONSABLE_CUMPLIMIENTO "
                       + "     JOIN BODBA.BO_PEDIDOS                    P ON P.ID_PEDIDO            = PE.ID_PEDIDO "
                       + "     JOIN FODBA.FO_DIRECCIONES                D ON D.DIR_ID               = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO                 POL ON POL.ID_POLIGONO        = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS                      Z ON Z.ID_ZONA              = POL.ID_ZONA "
                       + "WHERE R.ID_ESTADO = 67 "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND MONTH(R.FECHA_HORA_SALIDA) = MONTH('" + FechaQuery + "') "
                       + "  AND PE.REPROGRAMADA > 0 "
                       + "  AND COALESCE(RD.ID_RESPONSABLE_DESP, 0) <> 6 "
                       + "GROUP BY DATE(R.FECHA_HORA_SALIDA), RD.ID_RESPONSABLE_DESP "
                       + "ORDER BY DATE(R.FECHA_HORA_SALIDA), RD.ID_RESPONSABLE_DESP";
			logger.debug(sql);

			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			while (rs.next()) {
			    String keyFecha = frmFechaByDate(rs.getDate("FECHA"));
			    if (lista_CumpRep.get(keyFecha) == null){
			        BeanCumplimientoMensual cump = new BeanCumplimientoMensual();
			        
			        cump.setFecha(rs.getDate("FECHA"));
					if (rs.getString("RESP_INCUMPLIMIENTO").equals("LOCAL")){
					    cump.setAtrasosLocal(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("TRANSPORTE")){
					    cump.setAtrasosTransporte(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("SISTEMAS")){
					    cump.setAtrasosSistemas(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("A TIEMPO")){
					    cump.setATiempo(rs.getInt("CANT_OP"));
					}
					
					lista_CumpRep.put(keyFecha, cump);
			    }else{
			        BeanCumplimientoMensual cump = (BeanCumplimientoMensual)lista_CumpRep.get(keyFecha);
			        
					if (rs.getString("RESP_INCUMPLIMIENTO").equals("LOCAL")){
					    cump.setAtrasosLocal(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("TRANSPORTE")){
					    cump.setAtrasosTransporte(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("SISTEMAS")){
					    cump.setAtrasosSistemas(rs.getInt("CANT_OP"));
					}else if (rs.getString("RESP_INCUMPLIMIENTO").equals("A TIEMPO")){
					    cump.setATiempo(rs.getInt("CANT_OP"));
					}

					lista_CumpRep.put(keyFecha, cump);
			    }
			}
			rs.close();
			stm.close();
		} catch (Exception e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_CumpRep;
	}


	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public HashMap ResumenMensualPedidosXComuna(int local, Calendar calendario){
		String METHOD_NAME = "ResumenMensualPedidosXComuna";
        DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
        String FechaQuery = DateFormatQuery.format(calendario.getTime()).toUpperCase();

		Connection conn = null;
		HashMap lista_PedidosComuna  = new HashMap();
		
		Statement stm = null;
		ResultSet rs  = null;

		try {
			logger.debug("en ResumenMensualPedidosXComuna");
			String sql = "SELECT DATE(R.FECHA_HORA_SALIDA) AS FECHA "
                       + "      ,COUNT(P.ID_PEDIDO) AS CANT_OP "
                       + "      ,C.NOMBRE AS COMUNA "
                       + "FROM BODBA.BO_RUTA R "
                       + "     LEFT JOIN BODBA.BO_PEDIDOS_EXT PE ON PE.ID_RUTA      = R.ID_RUTA "
                       + "     JOIN BODBA.BO_PEDIDOS           P ON P.ID_PEDIDO     = PE.ID_PEDIDO "
                       + "     JOIN FODBA.FO_DIRECCIONES       D ON D.DIR_ID        = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO        POL ON POL.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS             Z ON Z.ID_ZONA       = POL.ID_ZONA "
                       + "     JOIN BODBA.BO_COMUNAS           C ON C.ID_COMUNA     = POL.ID_COMUNA "
                       + "WHERE R.ID_ESTADO = 67 "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND MONTH(R.FECHA_HORA_SALIDA) = MONTH('" + FechaQuery + "') "
                       + "GROUP BY DATE(R.FECHA_HORA_SALIDA), C.NOMBRE "
                       + "ORDER BY DATE(R.FECHA_HORA_SALIDA), C.NOMBRE";
			logger.debug(sql);

			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			while (rs.next()) {
			    String keyFecha = frmFechaByDate(rs.getDate("FECHA"));
			    if (lista_PedidosComuna.get(rs.getString("COMUNA")) == null){
			        BeanPedidosComunaDiario pcd = new BeanPedidosComunaDiario();
			        HashMap lista_PedidosDiarios = new HashMap();
			        
			        pcd.setFecha(rs.getTimestamp("FECHA"));
			        pcd.setCant_pedidos(rs.getDouble("CANT_OP"));
			        
			        BeanPedidosComunaMensual pcm = new BeanPedidosComunaMensual();
			        pcm.setComuna(rs.getString("COMUNA"));
			        pcm.setTotalPedidos(rs.getDouble("CANT_OP"));
			        
			        lista_PedidosDiarios.put(keyFecha, pcd);
			        pcm.setPedidosDiarios(lista_PedidosDiarios);
			        
			        lista_PedidosComuna.put(rs.getString("COMUNA"), pcm);
			    }else{
			        BeanPedidosComunaMensual pcm = (BeanPedidosComunaMensual)lista_PedidosComuna.get(rs.getString("COMUNA"));
			        BeanPedidosComunaDiario pcd = new BeanPedidosComunaDiario();
			        
			        pcd.setFecha(rs.getTimestamp("FECHA"));
			        pcd.setCant_pedidos(rs.getDouble("CANT_OP"));
			        
			        pcm.setTotalPedidos( pcm.getTotalPedidos() + rs.getDouble("CANT_OP"));
			        
			        HashMap lista_PedidosDiarios = pcm.getPedidosDiarios();
			        lista_PedidosDiarios.put(keyFecha, pcd);
			    }
			}
			rs.close();
			stm.close();
		} catch (Exception e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_PedidosComuna;
	}

	
	/**
	 * Obtiene un listado de locales
	 * 
	 * @return List de LocalEntity's
	 * @throws ClientesDAOException
	 */	
	public HashMap ResumenMensualTipoDespacho(int local, Calendar calendario){
		String METHOD_NAME = "ResumenMensualTipoDespacho";
        DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
        String FechaQuery = DateFormatQuery.format(calendario.getTime()).toUpperCase();

		Connection conn = null;
		HashMap lista_TipoDespacho = new HashMap();
		
		Statement stm = null;
		ResultSet rs = null;

		try {
			logger.debug("en ResumenMensualTipoDespacho");
			String sql = "SELECT DATE(R.FECHA_HORA_SALIDA) AS FECHA "
                       + "      ,COUNT(P.ID_PEDIDO) AS CANT_OP "
                       + "      ,CASE WHEN (P.TIPO_DESPACHO = 'N') THEN 'NORMAL' "
                       + "            WHEN (P.TIPO_DESPACHO = 'C') THEN 'ECONOMICO' "
                       + "            WHEN (P.TIPO_DESPACHO = 'E') THEN 'EXPRESS' "
                       + "       END AS TIPO_DESPACHO "
                       + "FROM BODBA.BO_RUTA R "
                       + "     LEFT JOIN BODBA.BO_PEDIDOS_EXT PE ON PE.ID_RUTA      = R.ID_RUTA "
                       + "     JOIN BODBA.BO_PEDIDOS           P ON P.ID_PEDIDO     = PE.ID_PEDIDO "
                       + "     JOIN FODBA.FO_DIRECCIONES       D ON D.DIR_ID        = P.DIR_ID "
                       + "     JOIN BODBA.BO_POLIGONO        POL ON POL.ID_POLIGONO = D.ID_POLIGONO "
                       + "     JOIN BODBA.BO_ZONAS             Z ON Z.ID_ZONA       = POL.ID_ZONA "
                       + "WHERE R.ID_ESTADO = 67 "
                       + "  AND Z.ID_LOCAL = " + local + " "
                       + "  AND MONTH(R.FECHA_HORA_SALIDA) = MONTH('" + FechaQuery + "') "
                       + "GROUP BY DATE(R.FECHA_HORA_SALIDA), P.TIPO_DESPACHO "
                       + "ORDER BY DATE(R.FECHA_HORA_SALIDA), P.TIPO_DESPACHO";
			logger.debug(sql);

			ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			stm = conn.createStatement();

			rs = stm.executeQuery(sql);
			while (rs.next()) {
			    String keyFecha = frmFechaByDate(rs.getDate("FECHA"));
			    if (lista_TipoDespacho.get(keyFecha) == null){
			        BeanTipoDespachoMensual tdesp = new BeanTipoDespachoMensual();
			        
			        tdesp.setFecha(rs.getDate("FECHA"));
					if (rs.getString("TIPO_DESPACHO").equals("NORMAL")){
					    tdesp.setTipoDespN(rs.getInt("CANT_OP"));
					}else if (rs.getString("TIPO_DESPACHO").equals("ECONOMICO")){
					    tdesp.setTipoDespE(rs.getInt("CANT_OP"));
					}else if (rs.getString("TIPO_DESPACHO").equals("EXPRESS")){
					    tdesp.setTipoDespEX(rs.getInt("CANT_OP"));
					}
					
					lista_TipoDespacho.put(keyFecha, tdesp);
			    }else{
			        BeanTipoDespachoMensual tdesp = (BeanTipoDespachoMensual)lista_TipoDespacho.get(keyFecha);
			        
					if (rs.getString("TIPO_DESPACHO").equals("NORMAL")){
					    tdesp.setTipoDespN(rs.getInt("CANT_OP"));
					}else if (rs.getString("TIPO_DESPACHO").equals("ECONOMICO")){
					    tdesp.setTipoDespE(rs.getInt("CANT_OP"));
					}else if (rs.getString("TIPO_DESPACHO").equals("EXPRESS")){
					    tdesp.setTipoDespEX(rs.getInt("CANT_OP"));
					}

					lista_TipoDespacho.put(keyFecha, tdesp);
			    }
				
			    //BeanTipoDespachoMensual
			}
			rs.close();
			stm.close();
		} catch (Exception e) {
		    logger.debug(this.getClass() + ":" + METHOD_NAME + ": Exception: " + e.getMessage() + ":" + e.fillInStackTrace());
			e.printStackTrace();
		} finally {
            try {
            	if (rs != null) rs.close();
                if (stm != null) stm.close();
            } catch (SQLException e) {
            	logger.error(e.getMessage());
            	e.printStackTrace();
            }
        }
		return lista_TipoDespacho;
	}


	public HSSFCellStyle getEstiloConBordes(HSSFWorkbook wb, HSSFFont font){
		HSSFCellStyle estilo = wb.createCellStyle();
		estilo.setFont(font);
		estilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		//estilo.setFillBackgroundColor(HSSFColor.BLUE.index);
		//estilo.setFillForegroundColor(HSSFColor.GREEN.index);
		estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//estilo.setWrapText(true);

		return estilo;
	}
	
	public HSSFCellStyle getEstiloConBordes(HSSFWorkbook wb, HSSFFont font, short alineacion){
		HSSFCellStyle estilo = wb.createCellStyle();
		estilo.setFont(font);
		estilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		//estilo.setFillBackgroundColor(HSSFColor.WHITE.index);
		estilo.setFillForegroundColor(HSSFColor.WHITE.index);
		estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estilo.setAlignment(alineacion);
		//estilo.setWrapText(true);

		return estilo;
	}

	
	public HSSFCellStyle getEstiloConBordes(HSSFWorkbook wb, HSSFFont font, short alineacion, short ColorRelleno){
		HSSFCellStyle estilo = wb.createCellStyle();
		estilo.setFont(font);
		estilo.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		estilo.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		//estilo.setFillBackgroundColor(HSSFColor.WHITE.index);
		estilo.setFillForegroundColor(ColorRelleno);
		estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		estilo.setAlignment(alineacion);
		//estilo.setWrapText(true);

		return estilo;
	}

	public HSSFFont getFuente(HSSFWorkbook wb, short color){
	    HSSFFont fuente = wb.createFont();
		fuente.setFontHeightInPoints((short)10);
		fuente.setFontName("Arial");
		fuente.setItalic(false);
		fuente.setColor(color);
	    return fuente;
	}
	

	public HSSFFont getFuenteNegrita(HSSFWorkbook wb, short color){
	    HSSFFont fuente = wb.createFont();
		fuente.setFontHeightInPoints((short)10);
		fuente.setFontName("Arial");
		fuente.setItalic(false);
		fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fuente.setColor(color);
	    return fuente;
	}
	

	public HSSFSheet estadisticaCumplimiento(HSSFWorkbook wb, HSSFSheet hoja){
	    
	    numFila=numFila + 2;
		for (int i=0; i<6; i++){
		    HSSFRow row = hoja.createRow((short) numFila);
		    
		    HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short)10);
			font.setFontName("Arial");
			font.setItalic(false);

			for (int j=0; j<7; j++){
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0){
			        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			        font.setColor(HSSFColor.WHITE.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.GREEN.index);
			    }else{
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(new HSSFRichTextString(""));
				
				cell.setCellStyle(style);
			}
			hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,4));
			numFila++;
		}
		
		
		for (int i=0; i<2; i++){
		    //int z=7+i;
		    HSSFRow row = hoja.createRow((short) numFila);

			for (int j=0; j<6; j++){
			    
			    HSSFFont font = wb.createFont();
				font.setFontHeightInPoints((short)10);
				font.setFontName("Arial");
				font.setItalic(false);
				
				HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (j<5){
			        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			        font.setColor(HSSFColor.WHITE.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.GREEN.index);
			    }else{
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
				HSSFCell cell = row.createCell(j);
				cell.setCellValue(new HSSFRichTextString(""));
				cell.setCellStyle(style);
			}
			hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,4));
			numFila++;
		}

		HSSFRow row2 = hoja.getRow((short) 2);
		HSSFCell cell_2_0 = row2.getCell(0);
		cell_2_0.setCellValue(new HSSFRichTextString("Cumplimiento Compromiso de Entregas"));
		
		HSSFCell cell_2_5 = row2.getCell(5);
		cell_2_5.setCellValue(new HSSFRichTextString("Cant. OP"));//Agregar Fecha Actual en Formato: 17-feb
		
		HSSFCell cell_2_6 = row2.getCell(6);
		cell_2_6.setCellValue(new HSSFRichTextString("Total general"));

		
		//HSSFDataFormat formatoNumero = wb.createDataFormat();
		//HSSFDataFormat formatoPorcentaje = wb.createDataFormat();
		
		HSSFRow row3 = hoja.getRow((short) 3);
		HSSFCell cell_3_0 = row3.getCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("Pedidos A Tiempo"));
		
		HSSFCell cell_3_5 = row3.getCell(5);
		cell_3_5.setCellValue(estadistica.getCumplimientoATiempo()); //estadistica.getCumplimientoATiempo()
		//HSSFCellStyle estilo = cell_3_5.getCellStyle();
		//estilo.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		//cell_3_5.setCellStyle(estilo);

		HSSFCell cell_3_6 = row3.getCell(6);
		cell_3_6.setCellValue(estadistica.getCumplimientoATiempo()/estadistica.getCantidadTotalOP());
		HSSFCellStyle estiloPorcentaje = cell_3_6.getCellStyle();
		estiloPorcentaje.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
		cell_3_6.setCellStyle(estiloPorcentaje);

		
		
		HSSFRow row4 = hoja.getRow((short) 4);
		HSSFCell cell_4_0 = row4.getCell(0);
		cell_4_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Transporte"));
		
		HSSFCell cell_4_5 = row4.getCell(5);
		cell_4_5.setCellValue(estadistica.getCumplimientoAtrasosTransporte());
		
		HSSFCell cell_4_6 = row4.getCell(6);
		cell_4_6.setCellValue(estadistica.getCumplimientoAtrasosTransporte()/estadistica.getCantidadTotalOP());
		cell_4_6.setCellStyle(estiloPorcentaje);

		
		
		HSSFRow row5 = hoja.getRow((short) 5);
		HSSFCell cell_5_0 = row5.getCell(0);
		cell_5_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Sistema"));
		
		HSSFCell cell_5_5 = row5.getCell(5);
		cell_5_5.setCellValue(estadistica.getCumplimientoAtrasosSistemas());
		
		HSSFCell cell_5_6 = row5.getCell(6);
		cell_5_6.setCellValue(estadistica.getCumplimientoAtrasosSistemas()/estadistica.getCantidadTotalOP());
		cell_5_6.setCellStyle(estiloPorcentaje);

		
		
		HSSFRow row6 = hoja.getRow((short) 6);
		HSSFCell cell_6_0 = row6.getCell(0);
		cell_6_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Local"));
		
		HSSFCell cell_6_5 = row6.getCell(5);
		cell_6_5.setCellValue(estadistica.getCumplimientoAtrasosLocal());
		
		HSSFCell cell_6_6 = row6.getCell(6);
		cell_6_6.setCellValue(estadistica.getCumplimientoAtrasosLocal()/estadistica.getCantidadTotalOP());
		cell_6_6.setCellStyle(estiloPorcentaje);

		
		
		HSSFRow row7 = hoja.getRow((short) 7);
		HSSFCell cell_7_0 = row7.getCell(0);
		cell_7_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Cliente"));
		
		HSSFCell cell_7_5 = row7.getCell(5);
		cell_7_5.setCellValue(estadistica.getCumplimientoAtrasosCliente());
		
		HSSFCell cell_7_6 = row7.getCell(6);
		cell_7_6.setCellValue(estadistica.getCumplimientoAtrasosCliente()/estadistica.getCantidadTotalOP());
		cell_7_6.setCellStyle(estiloPorcentaje);
		

		
		
		HSSFRow row8 = hoja.getRow((short) 8);
		HSSFCell cell_8_0 = row8.getCell(0);
		cell_8_0.setCellValue(new HSSFRichTextString("Total Pedidos Despachados"));
		
		HSSFCell cell_8_5 = row8.getCell(5);
		cell_8_5.setCellValue(estadistica.getCumplimientoTotal());

		
		
		HSSFRow row9 = hoja.getRow((short) 9);
		HSSFCell cell_9_0 = row9.getCell(0);
		cell_9_0.setCellValue(new HSSFRichTextString("% Cumplimiento entrega a tiempo"));
		
		HSSFCell cell_9_5 = row9.getCell(5);
		cell_9_5.setCellValue(estadistica.getCumplimientoATiempo()/estadistica.getCantidadTotalOP());
		cell_9_5.setCellStyle(estiloPorcentaje);

	    return hoja;
	}

	
	public HSSFSheet estadisticaAtrasosPorJornada(HSSFWorkbook wb, HSSFSheet hoja, HashMap jornadas){
	    
	    SortedSet set = new TreeSet();
	    
	    //INGRESA LOS OBJETOS AL OBJETO "TREESET" PARA LUEGO SER RECUPERADOS EN ORDEN
	    for (Iterator it=jornadas.keySet().iterator(); it.hasNext(); ) {
	        String key = it.next().toString();
	        set.add(key);
        }

	    numFila++;
	    HSSFRow rowHead = hoja.createRow((short) numFila);
	    
	    HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setItalic(false);

	    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREEN.index);
	    
		for (int j=0; j<8; j++){
			HSSFCell cell = rowHead.createCell(j);
			cell.setCellValue(new HSSFRichTextString(""));
			cell.setCellStyle(style);
		}
		hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,4));

		//HSSFRow row1 = hoja.getRow((short) numFila);
		HSSFCell cell_10_0 = rowHead.getCell(0);
		cell_10_0.setCellValue(new HSSFRichTextString("Atrasos por jornada"));
		
		HSSFCell cell_10_5 = rowHead.getCell(5);
		cell_10_5.setCellValue(new HSSFRichTextString("Cantidad"));
		
		HSSFCell cell_10_6 = rowHead.getCell(6);
		cell_10_6.setCellValue(new HSSFRichTextString("Atraso promedio"));

		HSSFCell cell_10_7 = rowHead.getCell(7);
		cell_10_7.setCellValue(new HSSFRichTextString("Atraso Máximo"));


		HashMap ListaAtrasosJornada = estadistica.getAtrasosXJornada();
		
		
		int i=0;
		//int z=11;
		numFila++;
	    Iterator it = set.iterator();
	    while (it.hasNext()) {
	        // Get element
	        String jornada = it.next().toString();
	        
	        BeanJornada jor = (BeanJornada)jornadas.get(jornada);
	        BeanAtrasosXJornada ajor = (BeanAtrasosXJornada) ListaAtrasosJornada.get(jornada);
	        
		    HSSFFont font1 = wb.createFont();
			font1.setFontHeightInPoints((short)10);
			font1.setFontName("Arial");
			font1.setItalic(false);

		    HSSFRow rowJornada = hoja.createRow((short) numFila);
		    
			for (int j=0; j<8; j++){
			    HSSFCellStyle style1 = getEstiloConBordes(wb, font1, HSSFCellStyle.ALIGN_LEFT);
		        style1.setFont(font1);
		        style1.setFillForegroundColor(jor.getColor());

				HSSFCell cell = rowJornada.createCell(j);
				if (j==0){
				    cell.setCellValue(new HSSFRichTextString(jor.getHorario()));
				}else{
				    cell.setCellValue(new HSSFRichTextString(""));
				}
				cell.setCellStyle(style1);
			}
			
			if (ajor != null){
				// CANTIDAD
				HSSFCell cell_cant = rowJornada.getCell(5);
				cell_cant.setCellValue(ajor.getCantOP());
				
				
				// ATRASO PROMEDIO
				HSSFCell cell_AtProm = rowJornada.getCell(6);
				cell_AtProm.setCellValue(new HSSFRichTextString(ajor.getAtrasoPromedioHHMM()));
				/*cell_AtProm.setCellValue(ajor.getAtrasoPromedio());
				HSSFCellStyle estilo = cell_AtProm.getCellStyle();
				estilo.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
				cell_AtProm.setCellStyle(estilo);*/
				
				// ATRASO MAXIMO
				HSSFCell cell_AtMax = rowJornada.getCell(7);
				cell_AtMax.setCellValue(new HSSFRichTextString(ajor.getAtrasoMaximoHHMM()));
				//cell_AtMax.setCellValue(ajor.getAtrasoMaximo());
			}
			
			hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,4));
			numFila++;
	    }

	    
		//REPROGRAMADAS
	    /*HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short)10);
		font2.setFontName("Arial");
		font2.setItalic(false);*/

        HSSFRow rowReprogramadas = hoja.createRow((short) numFila);
		for (int j=0; j<8; j++){
		    HSSFCellStyle style2 = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        style2.setFont(font);
	        style2.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);

			HSSFCell cell = rowReprogramadas.createCell(j);
			cell.setCellValue(new HSSFRichTextString(""));
			cell.setCellStyle(style2);
		}
		hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,4));

		HSSFCell cell_rep_0 = rowReprogramadas.getCell(0);
		cell_rep_0.setCellValue(new HSSFRichTextString("REPROGRAMADAS"));
		
		
		BeanAtrasosXJornada ajor = (BeanAtrasosXJornada) ListaAtrasosJornada.get("REPROGRAMADAS");
		if (ajor != null){
			// CANTIDAD
			HSSFCell cell_cant = rowReprogramadas.getCell(5);
			cell_cant.setCellValue(ajor.getCantOP());
			
			
			// ATRASO PROMEDIO
			HSSFCell cell_AtProm = rowReprogramadas.getCell(6);
			cell_AtProm.setCellValue(new HSSFRichTextString(ajor.getAtrasoPromedioHHMM()));
			/*cell_AtProm.setCellValue(ajor.getAtrasoPromedio());
			HSSFCellStyle estilo = cell_AtProm.getCellStyle();
			estilo.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
			cell_AtProm.setCellStyle(estilo);*/
			
			// ATRASO MAXIMO
			HSSFCell cell_AtMax = rowReprogramadas.getCell(7);
			cell_AtMax.setCellValue(new HSSFRichTextString(ajor.getAtrasoMaximoHHMM()));
			//cell_AtMax.setCellValue(ajor.getAtrasoMaximo());
		}

		
	    return hoja;
	}

	
	
	public HSSFSheet estadisticaPedidosComuna(HSSFWorkbook wb, HSSFSheet hoja){
	    
	    numFila2=numFila2 + 2;
	    
	    HSSFRow rowHead = null;
	    if (hoja.getRow(numFila2) != null){
	        rowHead = hoja.getRow(numFila2);
	    }else{
	        rowHead = hoja.createRow((short) numFila2);
	    }
	    
	    HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setItalic(false);

	    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREEN.index);
	    
		for (int j=0; j<3; j++){
			HSSFCell cell = rowHead.createCell(j+9);
			cell.setCellValue(new HSSFRichTextString(""));
			cell.setCellStyle(style);
		}
		//hoja.addMergedRegion(new CellRangeAddress(numFila2,numFila2,0,4));

		HSSFCell cell_2_9 = rowHead.getCell(9);
		cell_2_9.setCellValue(new HSSFRichTextString("N°"));
		
		HSSFCell cell_2_10 = rowHead.getCell(10);
		cell_2_10.setCellValue(new HSSFRichTextString("Comuna"));
		
		HSSFCell cell_2_11 = rowHead.getCell(11);
		cell_2_11.setCellValue(new HSSFRichTextString("Total"));

				
	    /********************************************************************************/
		HashMap ListaPedidosComuna = estadistica.getPedidosXComuna();
	    
		numFila2++;
		
		
	    //INGRESA LOS OBJETOS AL OBJETO "TREESET" PARA LUEGO SER RECUPERADOS EN ORDEN
		SortedSet setComunas = new TreeSet();
	    for (Iterator it=ListaPedidosComuna.keySet().iterator(); it.hasNext(); ) {
	        String key = it.next().toString();
	        setComunas.add(key);
        }

	    Iterator it = setComunas.iterator();
	    while (it.hasNext()) {
	        // Get element
	        String comuna = it.next().toString();
	        
	        BeanPedidosXComuna pcom = (BeanPedidosXComuna) ListaPedidosComuna.get(comuna);
	        
		    HSSFFont font1 = wb.createFont();
			font1.setFontHeightInPoints((short)10);
			font1.setFontName("Arial");
			font1.setItalic(false);

		    //HSSFRow rowComuna = hoja.createRow((short) numFila2);
		    
		    HSSFRow rowComuna = null;
		    if (hoja.getRow(numFila2) != null){
		        rowComuna = hoja.getRow(numFila2);
		    }else{
		        rowComuna = hoja.createRow((short) numFila2);
		    }

			for (int j=0; j<3; j++){
			    HSSFCellStyle style1 = getEstiloConBordes(wb, font1, HSSFCellStyle.ALIGN_LEFT);
		        style1.setFont(font1);
		        style1.setFillForegroundColor(HSSFColor.WHITE.index);

				HSSFCell cell = rowComuna.createCell(j+9);
				if (j==0){
				    cell.setCellValue(pcom.getCantOP());
				}else if (j==1){
				    cell.setCellValue(new HSSFRichTextString(pcom.getComuna()));
				}else if (j==2){
				    style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
				    cell.setCellValue(pcom.getCantOP()/estadistica.getCantidadTotalOP());
				}
				cell.setCellStyle(style1);
				
			}
			numFila2++;
	    }
		
	    return hoja;
	}

	
	public HSSFSheet estadisticaTipoDespacho(HSSFWorkbook wb, HSSFSheet hoja){
	    
	    numFila2=numFila2 + 2;
	    
	    HSSFRow rowHead = null;
	    if (hoja.getRow(numFila2) != null){
	        rowHead = hoja.getRow(numFila2);
	    }else{
	        rowHead = hoja.createRow((short) numFila2);
	    }
	    
	    HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setItalic(false);

	    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);
        style.setFillForegroundColor(HSSFColor.GREEN.index);
	    
		for (int j=0; j<3; j++){
			HSSFCell cell = rowHead.createCell(j+9);
			cell.setCellValue(new HSSFRichTextString(""));
			cell.setCellStyle(style);
		}

		HSSFCell cell_2_9 = rowHead.getCell(9);
		cell_2_9.setCellValue(new HSSFRichTextString("TIPO DESP."));
		
		HSSFCell cell_2_10 = rowHead.getCell(10);
		cell_2_10.setCellValue(new HSSFRichTextString("Cantidad"));
		
		HSSFCell cell_2_11 = rowHead.getCell(11);
		cell_2_11.setCellValue(new HSSFRichTextString("Total"));

		
		
		

	    /********************************************************************************/
		numFila2++;
		
		
		for (int i=0; i<4; i++){

		    HSSFFont font1 = wb.createFont();
			font1.setFontHeightInPoints((short)10);
			font1.setFontName("Arial");
			font1.setItalic(false);

		    //HSSFRow rowComuna = hoja.createRow((short) numFila2);
		    
		    HSSFRow rowTipoDesp = null;
		    if (hoja.getRow(numFila2) != null){
		        rowTipoDesp = hoja.getRow(numFila2);
		    }else{
		        rowTipoDesp = hoja.createRow((short) numFila2);
		    }

			for (int j=0; j<3; j++){
			    HSSFCellStyle style1 = getEstiloConBordes(wb, font1, HSSFCellStyle.ALIGN_LEFT);
		        style1.setFont(font1);
		        style1.setFillForegroundColor(HSSFColor.WHITE.index);

				HSSFCell cell = rowTipoDesp.createCell(j+9);
				if (j==0){
				    switch(i){
				        case 0: cell.setCellValue(new HSSFRichTextString("N"));break;
				        case 1: cell.setCellValue(new HSSFRichTextString("E"));break;
				        case 2: cell.setCellValue(new HSSFRichTextString("EX"));break;
				        case 3: cell.setCellValue(new HSSFRichTextString("RL"));break;
				    }
				    
				}else if (j==1){
				    switch(i){
				        case 0: cell.setCellValue(estadistica.getTipoDespN());break;
				        case 1: cell.setCellValue(estadistica.getTipoDespE());break;
				        case 2: cell.setCellValue(estadistica.getTipoDespEX());break;
				        case 3: cell.setCellValue(estadistica.getTipoDespRL());break;
				    }
				}else if (j==2){
				    style1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
				    switch(i){
				        case 0: cell.setCellValue(estadistica.getTipoDespN()/estadistica.getCantidadTotalOP());
				                break;
				        case 1: cell.setCellValue(estadistica.getTipoDespE()/estadistica.getCantidadTotalOP());
		                        break;
				        case 2: cell.setCellValue(estadistica.getTipoDespEX()/estadistica.getCantidadTotalOP());
		                        break;
				        case 3: cell.setCellValue(estadistica.getTipoDespRL()/estadistica.getCantidadTotalOP());
        		                break;
				    }
				}
				cell.setCellStyle(style1);
			}
			numFila2++;
	    }
		/************************************************************/
	    HSSFFont font2 = wb.createFont();
		font2.setFontHeightInPoints((short)10);
		font2.setFontName("Arial");
		font2.setItalic(false);
		font2.setColor(HSSFColor.RED.index);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	    //HSSFRow rowComuna = hoja.createRow((short) numFila2);
	    
	    HSSFRow rowTipoDespTotal = null;
	    if (hoja.getRow(numFila2) != null){
	        rowTipoDespTotal = hoja.getRow(numFila2);
	    }else{
	        rowTipoDespTotal = hoja.createRow((short) numFila2);
	    }

	    HSSFCellStyle style1 = getEstiloConBordes(wb, font2, HSSFCellStyle.ALIGN_LEFT);
        style1.setFillForegroundColor(HSSFColor.WHITE.index);
	    HSSFCell cell_td_9 = rowTipoDespTotal.createCell(9);
	    cell_td_9.setCellValue(new HSSFRichTextString("TOTAL"));
	    cell_td_9.setCellStyle(style1);
	    
	    HSSFCell cell_td_10 = rowTipoDespTotal.createCell(10);
	    cell_td_10.setCellValue(estadistica.getCantidadTotalOP());
	    cell_td_10.setCellStyle(style1);

	    HSSFCellStyle style2 = getEstiloConBordes(wb, font2, HSSFCellStyle.ALIGN_LEFT);
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
	    HSSFCell cell_td_11 = rowTipoDespTotal.createCell(11);
	    cell_td_11.setCellValue(estadistica.getCantidadTotalOP()/estadistica.getCantidadTotalOP());
	    cell_td_11.setCellStyle(style2);

		/************************************************************/
	    
	    return hoja;
	}


	
	public void GeneraExcel(Map ListadoOP, BeanLocal loc, String filename, BeanParamConfig param, Calendar calendario){
        String METHOD_NAME = "GeneraExcel";
        String RutaImagen1 = param.getPathImagen1();
        String RutaImagen2 = param.getPathImagen2();
        
        //HSSFWorkbook wb = new HSSFWorkbook();
        


        
		SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dateFormatHoja= new SimpleDateFormat("ddMMyyyy");
		String Fecha     = dateFormat.format(calendario.getTime());
		String FechaHoja = dateFormatHoja.format(calendario.getTime());
		
		try{
		    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
		    
	        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("plantillas/Plantilla" + diasMes + ".xls"));
	        HSSFWorkbook wb = new HSSFWorkbook(fs);

	        
		    numFila=0;
		    numFila2=0;
		    
		    HashMap jornadas = ListadoJornadas(ListadoOP);
		    //AsignaColorPorJornada(jornadas);
		    
            HSSFDataFormat format = wb.createDataFormat();

            
			// Create a new font and alter it.
			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short)10);
			font2.setFontName("Arial");
			font2.setItalic(false);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font2.setColor(HSSFColor.WHITE.index);

			// Create a new font and alter it.
			HSSFFont font4 = wb.createFont();
			font4.setFontHeightInPoints((short)14);
			font4.setFontName("Arial");
			font4.setItalic(false);
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font4.setColor(HSSFColor.GREY_50_PERCENT.index);

			// Fonts are set into a style so create a new one to use.


			HSSFCellStyle style4 = wb.createCellStyle();
			style4.setFont(font4);
			style4.setBorderTop(HSSFCellStyle.BORDER_NONE);
			style4.setBorderBottom(HSSFCellStyle.BORDER_NONE);
			style4.setBorderRight(HSSFCellStyle.BORDER_NONE);
			style4.setBorderLeft(HSSFCellStyle.BORDER_NONE);
			style4.setFillForegroundColor(HSSFColor.WHITE.index);
			style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			//style4.setFillPattern(HSSFCellStyle.DIAMONDS);
			//style4.setWrapText(true);


			/***********************************************************************/
/*		    HashMap jornadas = ListadoJornadas(ListadoOP);
		    
		    
		    HSSFSheet hoja = wb.createSheet("TestColores");
		    HSSFRow fila = hoja.createRow((short) 0);

		    Hashtable colores = HSSFColor.getIndexHash();
		    Iterator  it = colores.keySet().iterator();
		    int z=0;
		    while (it.hasNext()){
		        Number  index = (Number) it.next();
		        HSSFColor color = (HSSFColor) colores.get(index);
		        
				HSSFCell celda = fila.createCell(z);
				int pos1 = color.toString().indexOf("$");
				int pos2 = color.toString().indexOf("@");
				String nom_color = color.toString().substring(pos1, pos2);
				celda.setCellValue(new HSSFRichTextString("(" + color.getIndex() + ") " + nom_color));
				
				HSSFCellStyle estilo = wb.createCellStyle();
				estilo.setFont(font);
				estilo.setFillForegroundColor(color.getIndex());
				estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				celda.setCellStyle(estilo);
				z++;
		    }*/
		
			
			/***********************************************************************/
			/***********************************************************************/
		    /*HashMap jornadas = ListadoJornadas(ListadoOP);
		    
		    
		    HSSFSheet hoja = wb.createSheet("TestColores");

		    Hashtable colores = HSSFColor.getIndexHash();
		    Iterator  it = colores.keySet().iterator();
		    int z=0;
		    while (it.hasNext()){
		        Number  index = (Number) it.next();
		        HSSFColor color = (HSSFColor) colores.get(index);
		        
			    HSSFRow fila = hoja.createRow((short) z);

				HSSFCell celda0 = fila.createCell(0);
				int pos1 = color.toString().indexOf("$");
				int pos2 = color.toString().indexOf("@");
				String nom_color = color.toString().substring(pos1+1, pos2);
				celda0.setCellValue(new HSSFRichTextString("(" + color.getIndex() + ") " + nom_color));
				
				HSSFCell celda1 = fila.createCell(1);
				HSSFCellStyle estilo = wb.createCellStyle();
				estilo.setFont(font);
				estilo.setFillForegroundColor(color.getIndex());
				estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				celda1.setCellStyle(estilo);
				z++;
		    }
			hoja.autoSizeColumn((short)0);
			hoja.autoSizeColumn((short)1);*/
		
			
			/***********************************************************************/
			
			HSSFSheet sheet = wb.createSheet(FechaHoja);
			
			HSSFRow row0 = sheet.createRow((short) numFila);
			HSSFCell cell = row0.createCell(4);
			cell.setCellValue(new HSSFRichTextString("Local (" + loc.getNom_local() + ") | Fecha (" + Fecha + ")"));
			cell.setCellStyle(style4);

			sheet = estadisticaCumplimiento(wb, sheet);
			sheet = estadisticaAtrasosPorJornada(wb, sheet, jornadas);
			sheet = estadisticaPedidosComuna(wb, sheet);
			sheet = estadisticaTipoDespacho(wb, sheet);
			
			//agrega imagen1 - Tabla de Motivos de Faltantes
    	    /*HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    	    HSSFClientAnchor anchor;
    	    anchor = new HSSFClientAnchor(0,0,0,0,(short)0,0,(short)3,7);
    	    anchor.setAnchorType(2);
    	    patriarch.createPicture(anchor, loadPicture( RutaImagen1, wb ));*/

			//agrega imagen2 - Logo Jumbo.cl
    	    /*HSSFClientAnchor anchor2;
    	    anchor2 = new HSSFClientAnchor(0,0,0,0,(short)4,0,(short)10,7);
    	    anchor2.setAnchorType(2);
    	    patriarch.createPicture(anchor2, loadPicture( RutaImagen2, wb ));*/

    	    /*Y para mezclar celdas existe este metodo:
    	        sheet.addMergedRegion(new CellRangeAddress(fi,ff,ci,cf));
    	        Donde los parametros estan dados por
    	        int fi: Fila inicial
    	        int ff: Fila final
    	        int ci: columna inicial
    	        int cf: columna final*/
    	    
    	    if(numFila<numFila2){
    	        numFila = numFila2;
    	    }

			numFila = numFila + 2;
			HSSFRow rowHead1 = sheet.createRow((short) numFila);
			for (int i=0; i<29; i++){
				HSSFCell cell_19_0 = rowHead1.createCell(i);
				cell_19_0.setCellValue(new HSSFRichTextString(""));
				HSSFCellStyle style = getEstiloConBordes(wb, font2);
				style.setFillForegroundColor(HSSFColor.LIME.index);
				cell_19_0.setCellStyle(style);
			}
			
			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,0,7));
			//HSSFRow row9 = sheet.getRow((short) 9);
			HSSFCell cell_19_0 = rowHead1.getCell(0);
			cell_19_0.setCellValue(new HSSFRichTextString("Estado"));
			//style.setFillForegroundColor(HSSFColor.LIME.index);
			//cell_9_0.setCellStyle(style);
			
			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,8,11));
			HSSFCell cell_19_8 = rowHead1.getCell(8);
			cell_19_8.setCellValue(new HSSFRichTextString("Local"));
			//style.setFillForegroundColor(HSSFColor.LIME.index);
			//cell_8_0.setCellStyle(style);

			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,12,13));
			HSSFCell cell_19_12 = rowHead1.getCell(12);
			cell_19_12.setCellValue(new HSSFRichTextString("Domicilio"));

			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,14,15));
			HSSFCell cell_19_14 = rowHead1.getCell(14);
			cell_19_14.setCellValue(new HSSFRichTextString("Estadísticas"));

			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,16,17));
			HSSFCell cell_19_16 = rowHead1.getCell(16);
			cell_19_16.setCellValue(new HSSFRichTextString("Despachador"));

			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,18,20));
			HSSFCell cell_19_18 = rowHead1.getCell(18);
			cell_19_18.setCellValue(new HSSFRichTextString("Cumplimiento"));

			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,21,24));
			HSSFCell cell_19_21 = rowHead1.getCell(21);
			cell_19_21.setCellValue(new HSSFRichTextString("Destino/Cliente."));

			HSSFCell cell_19_25 = rowHead1.getCell(25);
			cell_19_25.setCellValue(new HSSFRichTextString("Medio de Pago"));

			sheet.addMergedRegion(new CellRangeAddress(numFila,numFila,26,28));
			HSSFCell cell_19_26 = rowHead1.getCell(26);
			cell_19_26.setCellValue(new HSSFRichTextString("Entrega"));

			
			HSSFCellStyle style = getEstiloConBordes(wb, font2);
			style.setFillForegroundColor(HSSFColor.GREEN.index);
			style.setWrapText(true);
			
			numFila++;
			HSSFRow rowHead2 = sheet.createRow((short) numFila);
			rowHead2.setHeightInPoints(25);

			HSSFCell cell_0_0 = rowHead2.createCell(0);
			cell_0_0.setCellValue(new HSSFRichTextString("Nº"));
			cell_0_0.setCellStyle(style);

			HSSFCell cell_0_1 = rowHead2.createCell(1);
			cell_0_1.setCellValue(new HSSFRichTextString("Fecha"));
			cell_0_1.setCellStyle(style);

			HSSFCell cell_0_2 = rowHead2.createCell(2);
			cell_0_2.setCellValue(new HSSFRichTextString("Id Ruta"));
			cell_0_2.setCellStyle(style);
			
			HSSFCell cell_0_3 = rowHead2.createCell(3);
			cell_0_3.setCellValue(new HSSFRichTextString("Id Pedido"));
			cell_0_3.setCellStyle(style);
			
			HSSFCell cell_0_4 = rowHead2.createCell(4);
			cell_0_4.setCellValue(new HSSFRichTextString("Jumbo Va"));
			cell_0_4.setCellStyle(style);

			HSSFCell cell_0_5 = rowHead2.createCell(5);
			cell_0_5.setCellValue(new HSSFRichTextString("Horario"));
			cell_0_5.setCellStyle(style);

			HSSFCell cell_0_6 = rowHead2.createCell(6);
			cell_0_6.setCellValue(new HSSFRichTextString("Tipo Despacho"));
			cell_0_6.setCellStyle(style);

			HSSFCell cell_0_7 = rowHead2.createCell(7);
			cell_0_7.setCellValue(new HSSFRichTextString("Monto"));
			cell_0_7.setCellStyle(style);

			HSSFCell cell_0_8 = rowHead2.createCell(8);
			cell_0_8.setCellValue(new HSSFRichTextString("Pos Fecha"));
			cell_0_8.setCellStyle(style);

			HSSFCell cell_0_9 = rowHead2.createCell(9);
			cell_0_9.setCellValue(new HSSFRichTextString("Pos Hora"));
			cell_0_9.setCellStyle(style);

			HSSFCell cell_0_10 = rowHead2.createCell(10);
			cell_0_10.setCellValue(new HSSFRichTextString("Cant. Bins"));
			cell_0_10.setCellStyle(style);

			HSSFCell cell_0_11 = rowHead2.createCell(11);
			cell_0_11.setCellValue(new HSSFRichTextString("Salida Camion"));
			cell_0_11.setCellStyle(style);

			HSSFCell cell_0_12 = rowHead2.createCell(12);
			cell_0_12.setCellValue(new HSSFRichTextString("Llegada Dom"));
			cell_0_12.setCellStyle(style);

			HSSFCell cell_0_13 = rowHead2.createCell(13);
			cell_0_13.setCellValue(new HSSFRichTextString("Salida Dom"));
			cell_0_13.setCellStyle(style);

			HSSFCell cell_0_14 = rowHead2.createCell(14);
			cell_0_14.setCellValue(new HSSFRichTextString("Pto. a Pto."));
			cell_0_14.setCellStyle(style);
			
			HSSFCell cell_0_15 = rowHead2.createCell(15);
			cell_0_15.setCellValue(new HSSFRichTextString("Tiempo Entrega"));
			cell_0_15.setCellStyle(style);

			HSSFCell cell_0_16 = rowHead2.createCell(16);
			cell_0_16.setCellValue(new HSSFRichTextString("Nombre Chofer"));
			cell_0_16.setCellStyle(style);

			HSSFCell cell_0_17 = rowHead2.createCell(17);
			cell_0_17.setCellValue(new HSSFRichTextString("Patente"));
			cell_0_17.setCellStyle(style);

			HSSFCell cell_0_18 = rowHead2.createCell(18);
			cell_0_18.setCellValue(new HSSFRichTextString("Estatus Entrega"));
			cell_0_18.setCellStyle(style);

			HSSFCell cell_0_19 = rowHead2.createCell(19);
			cell_0_19.setCellValue(new HSSFRichTextString("Resp Incumplimiento"));
			cell_0_19.setCellStyle(style);

			HSSFCell cell_0_20 = rowHead2.createCell(20);
			cell_0_20.setCellValue(new HSSFRichTextString("Motivo Incumplimiento"));
			cell_0_20.setCellStyle(style);

			HSSFCell cell_0_21 = rowHead2.createCell(21);
			cell_0_21.setCellValue(new HSSFRichTextString("Nom Cliente"));
			cell_0_21.setCellStyle(style);

			HSSFCell cell_0_22 = rowHead2.createCell(22);
			cell_0_22.setCellValue(new HSSFRichTextString("Direccion"));
			cell_0_22.setCellStyle(style);

			HSSFCell cell_0_23 = rowHead2.createCell(23);
			cell_0_23.setCellValue(new HSSFRichTextString("Comuna"));
			cell_0_23.setCellStyle(style);

			HSSFCell cell_0_24 = rowHead2.createCell(24);
			cell_0_24.setCellValue(new HSSFRichTextString("Telefonos"));
			cell_0_24.setCellStyle(style);

			HSSFCell cell_0_25 = rowHead2.createCell(25);
			cell_0_25.setCellValue(new HSSFRichTextString("Confirmacion"));
			cell_0_25.setCellStyle(style);

			HSSFCell cell_0_26 = rowHead2.createCell(26);
			cell_0_26.setCellValue(new HSSFRichTextString("Reprogramacion"));
			cell_0_26.setCellStyle(style);

			HSSFCell cell_0_27 = rowHead2.createCell(27);
			cell_0_27.setCellValue(new HSSFRichTextString("Num. Reprogramaciones"));
			cell_0_27.setCellStyle(style);

			HSSFCell cell_0_28 = rowHead2.createCell(28);
			cell_0_28.setCellValue(new HSSFRichTextString("Nombre Responsable"));
			cell_0_28.setCellStyle(style);

			
			HSSFFont fontNormal = wb.createFont();
			fontNormal.setFontHeightInPoints((short)10);
			fontNormal.setFontName("Arial");
			fontNormal.setItalic(false);

			HSSFFont fontNegrita = wb.createFont();
			fontNegrita.setFontHeightInPoints((short)10);
			fontNegrita.setFontName("Arial");
			fontNegrita.setItalic(false);
			fontNegrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			HSSFFont fontEstatus1 = wb.createFont();
			fontEstatus1.setFontHeightInPoints((short)10);
			fontEstatus1.setFontName("Arial");
			fontEstatus1.setItalic(false);
			fontEstatus1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			fontEstatus1.setColor(HSSFColor.LIGHT_BLUE.index);

			HSSFFont fontEstatus2 = wb.createFont();
			fontEstatus2.setFontHeightInPoints((short)10);
			fontEstatus2.setFontName("Arial");
			fontEstatus2.setItalic(false);
			fontEstatus2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			fontEstatus2.setColor(HSSFColor.RED.index);

			
			//int j = 20;
			/*Iterator it = ListadoOP.values().iterator();
			while (it.hasNext()) {
			    PedidoDesp ped = (PedidoDesp)it.next();*/
			for (int i=1; i <= ListadoOP.size(); i++){
			    PedidoDesp ped = (PedidoDesp)ListadoOP.get(new Integer(i));
			    numFila++;
			    

				HSSFCellStyle style2 = wb.createCellStyle();
				style2.setFont(fontNormal);
				style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style2.setFillForegroundColor(HSSFColor.WHITE.index);
				style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				//style2.setFillPattern(HSSFCellStyle.DIAMONDS);
				//style2.setWrapText(true);
				
				
				HSSFCellStyle style3 = wb.createCellStyle();
				style3.setFont(fontNormal);
				style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style3.setFillForegroundColor(HSSFColor.WHITE.index);
				//style3.setFillPattern(HSSFCellStyle.DIAMONDS);
				style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				//style3.setWrapText(true);
				

				HSSFCellStyle styleMoneda = wb.createCellStyle();
				styleMoneda.setFont(fontNegrita);
				styleMoneda.setBorderTop(HSSFCellStyle.BORDER_THIN);
				styleMoneda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				styleMoneda.setBorderRight(HSSFCellStyle.BORDER_THIN);
				styleMoneda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				styleMoneda.setFillForegroundColor(HSSFColor.WHITE.index);
				//styleMoneda.setFillPattern(HSSFCellStyle.DIAMONDS);
				styleMoneda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				//styleMoneda.setDataFormat((short) 5); //5, "($#,##0_);($#,##0)"
				styleMoneda.setDataFormat(format.getFormat("$ #,##0_)"));
				styleMoneda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

				
				if (ped.getJumbo_va().trim().equals("")){
					style2      = getColorPorJornada(jornadas, style2, ped.getHorario());
					style3      = getColorPorJornada(jornadas, style3, ped.getHorario());
					styleMoneda = getColorPorJornada(jornadas, styleMoneda, ped.getHorario());
				}else{
				    style2.setFillForegroundColor(HSSFColor.TURQUOISE.index);
				    style3.setFillForegroundColor(HSSFColor.TURQUOISE.index);
				    styleMoneda.setFillForegroundColor(HSSFColor.TURQUOISE.index);
				}
			    
				/*************************************************/

				HSSFCellStyle StyleStatus = wb.createCellStyle();
				StyleStatus.cloneStyleFrom(style3);
				
				if (ped.getEstatus_entrega().trim().equals("A TIEMPO")){
				    StyleStatus.setFont(fontEstatus1);
				}else{
				    StyleStatus.setFont(fontEstatus2);
				}
				/*************************************************/

				
			    HSSFRow rowDetalle = sheet.createRow((short) numFila);
			    
			    String fecha = ped.getFecha();
			    Calendar cal = Calendar.getInstance();
			    int year = Integer.parseInt(fecha.substring(0, 4));
			    int mes  = Integer.parseInt(fecha.substring(5, 7)) - 1;
			    int dia  = Integer.parseInt(fecha.substring(8, 10));
			    cal.set(year, mes, dia);
			    
	            DateFormat DateFormat  = new SimpleDateFormat("dd MMM", new Locale("es", "ES", ""));
				Fecha = DateFormat.format(cal.getTime()).toUpperCase();

				// Create a cell and put a value in it.
				HSSFCell cell_0 = rowDetalle.createCell(0);
				cell_0.setCellValue(Double.parseDouble(i+""));
				cell_0.setCellStyle(style2);

				HSSFCell cell_1 = rowDetalle.createCell(1);
				cell_1.setCellValue(new HSSFRichTextString(Fecha));
				cell_1.setCellStyle(style2);

				HSSFCell cell_2 = rowDetalle.createCell(2);
				//System.out.print("Id Ruta : " + ped.getId_ruta());
				cell_2.setCellValue(new HSSFRichTextString(ped.getId_ruta()));
				cell_2.setCellStyle(style2);

				HSSFCell cell_3 = rowDetalle.createCell(3);
				//System.out.print(" | Id Pedido : " + ped.getId_pedido());
				cell_3.setCellValue(new HSSFRichTextString(ped.getId_pedido()));
				cell_3.setCellStyle(style3);

				HSSFCell cell_4 = rowDetalle.createCell(4);
				cell_4.setCellValue(new HSSFRichTextString(ped.getJumbo_va()));
				cell_4.setCellStyle(style3);
				
				HSSFCell cell_5 = rowDetalle.createCell(5);
				cell_5.setCellValue(new HSSFRichTextString(ped.getHorario()));
				//style3 = AsignaColorPorJornada(jornadas, style3, ped.getHorario());
				cell_5.setCellStyle(style3);
				
				HSSFCell cell_6 = rowDetalle.createCell(6);
				cell_6.setCellValue(new HSSFRichTextString(ped.getTipo_despacho()));
				cell_6.setCellStyle(style3);

				HSSFCell cell_7 = rowDetalle.createCell(7);
				//System.out.print(" | Monto: " + ped.getMonto() + "\n");
				cell_7.setCellValue(Double.parseDouble(ped.getMonto()));
				cell_7.setCellStyle(styleMoneda);

				HSSFCell cell_8 = rowDetalle.createCell(8);
				cell_8.setCellValue(new HSSFRichTextString(ped.getPos_fecha()));
				cell_8.setCellStyle(style2);

				HSSFCell cell_9 = rowDetalle.createCell(9);
				cell_9.setCellValue(new HSSFRichTextString(ped.getPos_hora()));
				cell_9.setCellStyle(style2);

				HSSFCell cell_10 = rowDetalle.createCell(10);
				cell_10.setCellValue(new HSSFRichTextString(ped.getCant_bins()));
				cell_10.setCellStyle(style2);

				HSSFCell cell_11 = rowDetalle.createCell(11);
				cell_11.setCellValue(new HSSFRichTextString(ped.getSalida_camion()));
				cell_11.setCellStyle(style2);

				HSSFCell cell_12 = rowDetalle.createCell(12);
				cell_12.setCellValue(new HSSFRichTextString(ped.getLlegada_dom()));
				cell_12.setCellStyle(style2);

				HSSFCell cell_13 = rowDetalle.createCell(13);
				cell_13.setCellValue(new HSSFRichTextString(ped.getSalida_dom()));
				cell_13.setCellStyle(style3);

				HSSFCell cell_14 = rowDetalle.createCell(14);
				cell_14.setCellValue(new HSSFRichTextString(ped.getPto_a_pto()));
				cell_14.setCellStyle(style3);
				
				HSSFCell cell_15 = rowDetalle.createCell(15);
				cell_15.setCellValue(new HSSFRichTextString(ped.getTiempo_entrega()));
				cell_15.setCellStyle(style3);

				HSSFCell cell_16 = rowDetalle.createCell(16);
				cell_16.setCellValue(new HSSFRichTextString(ped.getNombre_chofer()));
				cell_16.setCellStyle(style3);

				HSSFCell cell_17 = rowDetalle.createCell(17);
				cell_17.setCellValue(new HSSFRichTextString(ped.getPatente()));
				cell_17.setCellStyle(style3);

				HSSFCell cell_18 = rowDetalle.createCell(18);
				cell_18.setCellValue(new HSSFRichTextString(ped.getEstatus_entrega()));
				cell_18.setCellStyle(StyleStatus);

				HSSFCell cell_19 = rowDetalle.createCell(19);
				cell_19.setCellValue(new HSSFRichTextString(ped.getResp_incumplimiento()));
				cell_19.setCellStyle(style3);

				HSSFCell cell_20 = rowDetalle.createCell(20);
				cell_20.setCellValue(new HSSFRichTextString(ped.getMotivo_incumplimiento()));
				cell_20.setCellStyle(style3);

				HSSFCell cell_21 = rowDetalle.createCell(21);
				cell_21.setCellValue(new HSSFRichTextString(ped.getNom_cliente()));
				cell_21.setCellStyle(style3);

				HSSFCell cell_22 = rowDetalle.createCell(22);
				cell_22.setCellValue(new HSSFRichTextString(ped.getDireccion()));
				cell_22.setCellStyle(style3);

				HSSFCell cell_23 = rowDetalle.createCell(23);
				cell_23.setCellValue(new HSSFRichTextString(ped.getComuna()));
				cell_23.setCellStyle(style3);

				HSSFCell cell_24 = rowDetalle.createCell(24);
				cell_24.setCellValue(new HSSFRichTextString(ped.getTelefonos()));
				cell_24.setCellStyle(style3);

				HSSFCell cell_25 = rowDetalle.createCell(25);
				cell_25.setCellValue(new HSSFRichTextString(ped.getConfirmacion()));
				cell_25.setCellStyle(style3);

				HSSFCell cell_26 = rowDetalle.createCell(26);
				cell_26.setCellValue(new HSSFRichTextString(ped.getReprogramacion()));
				cell_26.setCellStyle(style3);

				HSSFCell cell_27 = rowDetalle.createCell(27);
				cell_27.setCellValue(new HSSFRichTextString(ped.getNum_reprogramaciones()));
				cell_27.setCellStyle(style3);

				HSSFCell cell_28 = rowDetalle.createCell(28);
				cell_28.setCellValue(new HSSFRichTextString(ped.getNombre_responsable()));
				cell_28.setCellStyle(style3);

			}
			
			sheet.autoSizeColumn((short)0);  // Nº
			sheet.autoSizeColumn((short)1);  // FECHA
			sheet.autoSizeColumn((short)2);  // ID_RUTA
			sheet.autoSizeColumn((short)3);  // ID_PEDIDO
			sheet.setColumnWidth((short)4, 3500);  // JUMBO_VA
			sheet.autoSizeColumn((short)5);  // HORARIO
			sheet.autoSizeColumn((short)6);  // TIPO_DESPACHO
			sheet.setColumnWidth((short)7, 3500);  // MONTO
			sheet.autoSizeColumn((short)8);  // POS_FECHA
			sheet.autoSizeColumn((short)9);  // POS_HORA
			sheet.autoSizeColumn((short)10); // CANT_BINS
			sheet.autoSizeColumn((short)11); // SALIDA_CAMION
			sheet.autoSizeColumn((short)12); // LLEGADA_DOM
			sheet.autoSizeColumn((short)13); // SALIDA_DOM
			sheet.autoSizeColumn((short)14); // PTO. A PTO.
			sheet.autoSizeColumn((short)15); // TIEMPO_ENTREGA
			sheet.autoSizeColumn((short)16); // NOMBRE_CHOFER
			sheet.autoSizeColumn((short)17); // PATENTE
			sheet.autoSizeColumn((short)18); // ESTATUS_ENTREGA
			sheet.autoSizeColumn((short)19); // RESP_INCUMPLIMIENTO
			sheet.autoSizeColumn((short)20); // MOTIVO_INCUMPLIMIENTO
			sheet.autoSizeColumn((short)21); // NOM_CLIENTE
			sheet.autoSizeColumn((short)22); // DIRECCION
			sheet.autoSizeColumn((short)23); // COMUNA
			sheet.autoSizeColumn((short)24); // TELEFONOS
			sheet.autoSizeColumn((short)25); // CONFIRMACION
			sheet.autoSizeColumn((short)26); // REPROGRAMACION
			sheet.autoSizeColumn((short)27); // NUM_REPROGRAMACIONES
			sheet.autoSizeColumn((short)28); // NOMBRE_RESPONSABLE
			/*01.-FECHA
            02.-ID_RUTA
            03.-ID_PEDIDO
            04.-JUMBO_VA
            05.-HORARIO
            06.-TIPO_DESPACHO
            07.-MONTO
            08.-POS_FECHA
            09.-POS_HORA
            10.-CANT_BINS
            11.-SALIDA_CAMION
            12.-LLEGADA_DOM
            13.-SALIDA_DOM
            14.-TIEMPO_ENTREGA
            15.-NOMBRE_CHOFER
            16.-PATENTE
            17.-ESTATUS_ENTREGA
            18.-RESP_INCUMPLIMIENTO
            19.-MOTIVO_INCUMPLIMIENTO
            20.-NOM_CLIENTE
            21.-DIRECCION
            22.-COMUNA
            23.-TELEFONOS
            24.-CONFIRMACION
            25.-REPROGRAMACION
            26.-NUM_REPROGRAMACIONES
            27.-NOMBRE_RESPONSABLE*/
		    
			/*
			 * Agrega Resumen Mensual
			 */
			wb = ResumenGeneral(wb, param, loc, calendario);
			
			FileOutputStream fos = new FileOutputStream(filename);
			wb.write(fos);
			fos.close();
		}catch(FileNotFoundException e){
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
		    e.printStackTrace();
		}catch(IOException e){
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
		    e.printStackTrace();
		}catch(Exception e){
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
		    e.printStackTrace();
		}
    }
    
	

	public HashMap ListadoJornadas(Map ListadoOP){
	    HashMap jornadas = new HashMap();
        HashMap ListColores = new HashMap();
        ListColores.put("1",  HSSFColor.LIGHT_YELLOW.index + "");
        ListColores.put("2",  HSSFColor.LIGHT_GREEN.index + "");
        ListColores.put("3",  HSSFColor.TAN.index + "");
        ListColores.put("4",  HSSFColor.LIGHT_ORANGE.index + "");
        ListColores.put("5",  HSSFColor.PALE_BLUE.index + "");
        ListColores.put("6",  HSSFColor.LIGHT_TURQUOISE.index + "");
        ListColores.put("7",  HSSFColor.LIGHT_CORNFLOWER_BLUE.index + "");
        ListColores.put("8",  HSSFColor.CORAL.index + "");
        ListColores.put("9",  HSSFColor.SKY_BLUE.index + "");
        ListColores.put("10", HSSFColor.LIME.index + "");

        int j=0;
	    for (int i=1; i <= ListadoOP.size(); i++){
		    PedidoDesp ped = (PedidoDesp)ListadoOP.get(new Integer(i));
		    
		    if (jornadas.get(ped.getHorario()) == null){
		        j++;
		        BeanJornada jor = new BeanJornada();
		        jor.setHorario(ped.getHorario());
		        jor.setColor(Short.parseShort(ListColores.get(j +"").toString()));
		        jornadas.put(ped.getHorario(), jor);
		    }
	    }
	    return jornadas;
	}
	
    
    public HSSFCellStyle getColorPorJornada(HashMap jornadas, HSSFCellStyle estilo, String Jornada){
        BeanJornada jor = (BeanJornada)jornadas.get(Jornada);
        estilo.setFillForegroundColor(jor.getColor());
        estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        return estilo;
    }
    
    
    
    public HSSFWorkbook ResumenGeneral(HSSFWorkbook wb, BeanParamConfig param, BeanLocal local, Calendar calendario){
        String METHOD_NAME = "ResumenGeneral"; 
        try{
            numFila=0;
            numFila2=0;
            numFilaDet=0;
            
            DateFormat DateFormatQuery  = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "ES", ""));
            String FechaQuery = DateFormatQuery.format(calendario.getTime()).toUpperCase();

            HSSFSheet hoja = wb.getSheet("Resumen General");
            String nameHoja = "";
            int cantHojas = wb.getNumberOfSheets();
            for (int i=0; i < cantHojas; i++){
                nameHoja = wb.getSheetName(i);    
            }
            
            
            
            hoja = ResumenCumplimiento(wb, hoja, local.getId_local(), calendario);
            hoja = ResumenReprogramaciones(wb, hoja, local.getId_local(), calendario);
            hoja = ResumenCumplimientoReprogramaciones(wb, hoja, local.getId_local(), calendario);
            
            HashMap list_TipoDesp = ResumenMensualTipoDespacho(local.getId_local(), calendario);
            hoja = ResumenTipoDespacho(wb, hoja, list_TipoDesp, calendario);
            hoja = ResumenPorcentualTipoDespacho(wb, hoja, list_TipoDesp, calendario);

            HashMap list_Comunas = ResumenMensualPedidosXComuna(local.getId_local(), calendario);
            hoja = ResumenDemadaPorComuna(wb, hoja, list_Comunas, calendario);
            hoja = ResumenDemadaPorcentualPorComuna(wb, hoja, list_Comunas, calendario);
            
            
		}catch(Exception e){
		    logger.debug("ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
		    e.printStackTrace();
		}
        return wb;
    }
    

	public HSSFSheet ResumenCumplimiento(HSSFWorkbook wb, HSSFSheet hoja, int local, Calendar calendario){
	    int colDet = 0;
		double aTiempo = 0;
		double AtrasosLocal = 0;
		double AtrasosTransporte = 0;
		double AtrasosSistemas = 0;
		double AtrasosCliente = 0;
		double TotalPedidos = 0;
		

	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    
	    HashMap list_cump = ResumenMensualCumplimiento(local, calendario);
	    
	    numFilaDet=numFilaDet + 2;
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.RED.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle style3 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
        
		/*HSSFRow row3 = hoja.createRow((short) 3);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("Cumplimiento Compromiso de Entregas"));
		cell_3_0.setCellStyle(style1);

		HSSFRow row4 = hoja.createRow((short) 4);
		HSSFCell cell_4_0 = row4.createCell(0);
		cell_4_0.setCellValue(new HSSFRichTextString("Pedidos A Tiempo"));
		cell_4_0.setCellStyle(style2);

		HSSFRow row5 = hoja.createRow((short) 5);
		HSSFCell cell_5_0 = row5.createCell(0);
		cell_5_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Transporte"));
		cell_5_0.setCellStyle(style2);

		HSSFRow row6 = hoja.createRow((short) 6);
		HSSFCell cell_6_0 = row6.createCell(0);
		cell_6_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Sistema"));
		cell_6_0.setCellStyle(style2);

		HSSFRow row7 = hoja.createRow((short) 7);
		HSSFCell cell_7_0 = row7.createCell(0);
		cell_7_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Local"));
		cell_7_0.setCellStyle(style2);

		HSSFRow row8 = hoja.createRow((short) 8);
		HSSFCell cell_8_0 = row8.createCell(0);
		cell_8_0.setCellValue(new HSSFRichTextString("Pedidos Atrasados Responsabilidad  Cliente"));
		cell_8_0.setCellStyle(style2);

		HSSFRow row9 = hoja.createRow((short) 9);
		HSSFCell cell_9_0 = row9.createCell(0);
		cell_9_0.setCellValue(new HSSFRichTextString("Total Pedidos Despachados"));
		cell_9_0.setCellStyle(style1);

		HSSFRow row10 = hoja.createRow((short) 10);
		HSSFCell cell_10_0 = row10.createCell(0);
		cell_10_0.setCellValue(new HSSFRichTextString("% Cumplimiento entrega a tiempo"));
		cell_10_0.setCellStyle(style1);*/
		
		

		//hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		for (int i=0; i<9; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    

		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1 && i<7){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }else if (i >= 7){
			        font.setColor(HSSFColor.RED.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

				BeanCumplimientoMensual cump = (BeanCumplimientoMensual) list_cump.get(key);
				
				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            break;
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            break;
                    case 2: if (cump != null){
                                cell.setCellValue(cump.getATiempo());
                                aTiempo += cump.getATiempo();
                                TotalPedidos += cump.getATiempo();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                    case 3: if (cump != null){
                                cell.setCellValue(cump.getAtrasosTransporte());
                                AtrasosTransporte += cump.getAtrasosTransporte();
                                TotalPedidos += cump.getAtrasosTransporte();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                    case 4: if (cump != null){
                                cell.setCellValue(cump.getAtrasosSistemas());
                                AtrasosSistemas += cump.getAtrasosSistemas();
                                TotalPedidos += cump.getAtrasosSistemas();
                            }else{
                                cell.setCellValue(0);
                            } 
                            break;
                    case 5: if (cump != null){
                                cell.setCellValue(cump.getAtrasosLocal());
                                AtrasosLocal += cump.getAtrasosLocal();
                                TotalPedidos += cump.getAtrasosLocal();
                            }else{
                                cell.setCellValue(0);
                            }
                            
                            break;
                    case 6: if (cump != null){
                                cell.setCellValue(cump.getAtrasosCliente());
                                AtrasosCliente += cump.getAtrasosCliente();
                                TotalPedidos += cump.getAtrasosCliente();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                    case 7: if (cump != null){
                                cell.setCellValue(cump.getTotal());
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                    case 8: HSSFCellStyle estiloPorcentaje = cell.getCellStyle();
                            estiloPorcentaje.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                            cell.setCellStyle(estiloPorcentaje);
                            if (cump != null){
                                cell.setCellValue((cump.getATiempo() + cump.getAtrasosCliente())/cump.getTotal());
                            }else{
                                cell.setCellValue(0);
                            }
                            
                            break;
				}
				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			numFilaDet++;
		}
		
		numFilaDet = 3;
		int ColMontoTotGral = colDet;
		int ColPorcTotGral  = ++colDet;

		for (int i=1; i<9; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
			
			HSSFCell cell  = row.createCell(ColMontoTotGral);
			HSSFCell cell2 = row.createCell(ColPorcTotGral);
			cell.setCellStyle(style2);
			cell2.setCellStyle(style2);

			HSSFCellStyle estiloPorcentaje2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	        estiloPorcentaje2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
	        
			switch (i){
				case 1: cell.setCellValue(new HSSFRichTextString("Total General"));
				        cell.setCellStyle(style1);

			        	cell2.setCellValue(new HSSFRichTextString("Total General"));
				        cell2.setCellStyle(style1);
				        break;
				case 2: cell.setCellValue(aTiempo);
				        cell.setCellStyle(style2);
				        
				        cell2.setCellValue(aTiempo/TotalPedidos);
				        cell2.setCellStyle(estiloPorcentaje2);
				        break;
				case 3: cell.setCellValue(AtrasosTransporte);
	                    cell.setCellStyle(style2);
		        
				        //HSSFCellStyle estiloPorcentaje2 = cell2.getCellStyle();
			            //estiloPorcentaje2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
	                    cell2.setCellValue(AtrasosTransporte/TotalPedidos);
				        cell2.setCellStyle(estiloPorcentaje2);
	                    break;
				case 4: cell.setCellValue(AtrasosSistemas);
		                cell.setCellStyle(style2);
		        
		                cell2.setCellValue(AtrasosSistemas/TotalPedidos);
				        cell2.setCellStyle(estiloPorcentaje2);
		                break;
				case 5: cell.setCellValue(AtrasosLocal);
		                cell.setCellStyle(style2);
		        
	                    cell2.setCellValue(AtrasosLocal/TotalPedidos);
				        cell2.setCellStyle(estiloPorcentaje2);
		                break;
				case 6: cell.setCellValue(AtrasosCliente);
		                cell.setCellStyle(style2);
		        
	                    cell2.setCellValue(AtrasosCliente/TotalPedidos);
				        cell2.setCellStyle(estiloPorcentaje2);
		                break;
				case 7: cell.setCellValue(TotalPedidos);
	                    cell.setCellStyle(style3);
	                    break;
				case 8: HSSFCellStyle estiloPorcentaje1 = wb.createCellStyle();
	                    estiloPorcentaje1.cloneStyleFrom(style3);
	                    //estiloPorcentaje1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
	                    estiloPorcentaje1.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));

				        cell.setCellValue(aTiempo/TotalPedidos);
	                    cell.setCellStyle(estiloPorcentaje1);
	                    break;
			}
			numFilaDet++;
		}
		hoja.autoSizeColumn((short)ColMontoTotGral);
		hoja.autoSizeColumn((short)ColPorcTotGral);
		//hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,4));
		
	    return hoja;
	}
	

	public HSSFSheet ResumenReprogramaciones(HSSFWorkbook wb, HSSFSheet hoja, int local, Calendar calendario){
	    int colDet = 0;
		double AtrasosLocal = 0;
		double AtrasosTransporte = 0;
		double AtrasosSistemas = 0;
		double AtrasosCliente = 0;
		double TotalPedidos = 0;

		
	    numFilaDet=numFilaDet + 20;
	    int tempNumFilaDet = numFilaDet;
	    
	    HashMap list_PedRep = ResumenMensualPedidosReprogramados(local, calendario);
	    
	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.RED.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle style3 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
        
		/*HSSFRow row3 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("Reprogramaciones de despacho"));
		cell_3_0.setCellStyle(style1);

		numFilaDet++;
		HSSFRow row4 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_4_0 = row4.createCell(0);
		cell_4_0.setCellValue(new HSSFRichTextString("Reprogramado Solicitado por Cliente  + 24 hrs"));
		cell_4_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row5 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_5_0 = row5.createCell(0);
		cell_5_0.setCellValue(new HSSFRichTextString("Reprogramación por atraso Sistema"));
		cell_5_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row6 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_6_0 = row6.createCell(0);
		cell_6_0.setCellValue(new HSSFRichTextString("Reprogramación por atraso Local"));
		cell_6_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row7 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_7_0 = row7.createCell(0);
		cell_7_0.setCellValue(new HSSFRichTextString("Reprogramación por atraso Transporte"));
		cell_7_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row8 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_8_0 = row8.createCell(0);
		cell_8_0.setCellValue(new HSSFRichTextString("Total Pedidos Reprogramados"));
		cell_8_0.setCellStyle(style1);*/



		hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		numFilaDet = tempNumFilaDet;
		numFilaDet--;
		for (int i=0; i<7; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    

		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1 && i<6){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }else if (i >= 6){
			        font.setColor(HSSFColor.RED.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

				BeanCumplimientoMensual cump = (BeanCumplimientoMensual) list_PedRep.get(key);
				
				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            break;
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            break;
                    case 2: if (cump != null){
                                cell.setCellValue(cump.getAtrasosCliente());
                                AtrasosCliente += cump.getAtrasosCliente();
                                TotalPedidos += cump.getAtrasosCliente();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;

                    case 3: if (cump != null){
                                cell.setCellValue(cump.getAtrasosSistemas());
                                AtrasosSistemas += cump.getAtrasosSistemas();
                                TotalPedidos += cump.getAtrasosSistemas();
                            }else{
                                cell.setCellValue(0);
                            } 
                            break;
                    case 4: if (cump != null){
                                cell.setCellValue(cump.getAtrasosLocal());
                                AtrasosLocal += cump.getAtrasosLocal();
                                TotalPedidos += cump.getAtrasosLocal();
                            }else{
                                cell.setCellValue(0);
                            }
                            
                            break;
                    
                    case 5: if (cump != null){
                                cell.setCellValue(cump.getAtrasosTransporte());
                                AtrasosTransporte += cump.getAtrasosTransporte();
                                TotalPedidos += cump.getAtrasosTransporte();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                    case 6: if (cump != null){
                                cell.setCellValue(cump.getTotal());
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
				}
				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			numFilaDet++;
		}
		
		numFilaDet = tempNumFilaDet;
		//numFilaDet++;
		int ColMontoTotGral = colDet;

		for (int i=1; i<7; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
			
			HSSFCell cell  = row.createCell(ColMontoTotGral);
			cell.setCellStyle(style2);

			switch (i){
				case 1: cell.setCellValue(new HSSFRichTextString("Total General"));
				        cell.setCellStyle(style1);
				        break;
				case 2: cell.setCellValue(AtrasosCliente);
                        cell.setCellStyle(style2);
                        break;
				case 3: cell.setCellValue(AtrasosSistemas);
		                cell.setCellStyle(style2);
		                break;
				case 4: cell.setCellValue(AtrasosLocal);
		                cell.setCellStyle(style2);
		                break;
		        case 5: cell.setCellValue(AtrasosTransporte);
                        cell.setCellStyle(style2);
                        break;
		        case 6: cell.setCellValue(TotalPedidos);
	                    cell.setCellStyle(style3);
	                    break;
			}
			numFilaDet++;
		}
		hoja.autoSizeColumn((short)ColMontoTotGral);


	    return hoja;
	}

    

	public HSSFSheet ResumenCumplimientoReprogramaciones(HSSFWorkbook wb, HSSFSheet hoja, int local, Calendar calendario){
	    int colDet = 0;
	    double aTiempo = 0;
		double AtrasosLocal = 0;
		double AtrasosTransporte = 0;
		double AtrasosSistemas = 0;
		double TotalPedidos = 0;

		
	    numFilaDet=numFilaDet + 3;
	    int tempNumFilaDet = numFilaDet;
	    
	    HashMap list_CumpPedRep = ResumenMensualCumplimientoPedidosReprogramados(local, calendario);
	    
	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.RED.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle style3 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
        
		/*HSSFRow row3 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("Cumplimiento Compromiso  Reprogramaciones"));
		cell_3_0.setCellStyle(style1);

		numFilaDet++;
		HSSFRow row4 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_4_0 = row4.createCell(0);
		cell_4_0.setCellValue(new HSSFRichTextString("Reprogramados Entregados a Tiempo"));
		cell_4_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row5 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_5_0 = row5.createCell(0);
		cell_5_0.setCellValue(new HSSFRichTextString("Reprogramado Atrasados Responsabilidad Transporte"));
		cell_5_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row6 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_6_0 = row6.createCell(0);
		cell_6_0.setCellValue(new HSSFRichTextString("Reprogramado Atrasados Responsabilidad  Local"));
		cell_6_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row7 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_7_0 = row7.createCell(0);
		cell_7_0.setCellValue(new HSSFRichTextString("Reprogramado Atrasados Responsabilidad  Sistema"));
		cell_7_0.setCellStyle(style2);*/

		/*numFilaDet++;
		HSSFRow row8 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_8_0 = row8.createCell(0);
		cell_8_0.setCellValue(new HSSFRichTextString("Total Pedidos Reprogramados"));
		cell_8_0.setCellStyle(style1);*/



		hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		numFilaDet = tempNumFilaDet;
		numFilaDet--;
		for (int i=0; i<6; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    

		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1 && i<6){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }/*else if (i >= 6){
			        font.setColor(HSSFColor.RED.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }*/
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

				BeanCumplimientoMensual cump = (BeanCumplimientoMensual) list_CumpPedRep.get(key);
				
				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            break;
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            break;
                    case 2: if (cump != null){
                                cell.setCellValue(cump.getATiempo());
                                aTiempo += cump.getATiempo();
                                TotalPedidos += cump.getATiempo();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;

                    case 3: if (cump != null){
                                cell.setCellValue(cump.getAtrasosTransporte());
                                AtrasosTransporte += cump.getAtrasosTransporte();
                                TotalPedidos += cump.getAtrasosTransporte();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                            
                    case 4: if (cump != null){
                                cell.setCellValue(cump.getAtrasosLocal());
                                AtrasosLocal += cump.getAtrasosLocal();
                                TotalPedidos += cump.getAtrasosLocal();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                            

                    case 5: if (cump != null){
                                cell.setCellValue(cump.getAtrasosSistemas());
                                AtrasosSistemas += cump.getAtrasosSistemas();
                                TotalPedidos += cump.getAtrasosSistemas();
                            }else{
                                cell.setCellValue(0);
                            } 
                            break;
                            
                    /*case 6: if (cump != null){
                                cell.setCellValue(cump.getTotal());
                            }else{
                                cell.setCellValue(0);
                            }
                            break;*/
				}
				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			numFilaDet++;
		}
		
		numFilaDet = tempNumFilaDet;
		//numFilaDet++;
		int ColMontoTotGral = colDet;

		for (int i=1; i<6; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
			
			HSSFCell cell  = row.createCell(ColMontoTotGral);
			cell.setCellStyle(style2);

			switch (i){
				case 1: cell.setCellValue(new HSSFRichTextString("Total General"));
				        cell.setCellStyle(style1);
				        break;
				        
				case 2: cell.setCellValue(aTiempo);
                        cell.setCellStyle(style2);
                        break;
		                
		        case 3: cell.setCellValue(AtrasosTransporte);
                        cell.setCellStyle(style2);
                        break;
                        
				case 4: cell.setCellValue(AtrasosLocal);
                        cell.setCellStyle(style2);
                        break;
                        
				case 5: cell.setCellValue(AtrasosSistemas);
		                cell.setCellStyle(style2);
		                break;
		                
		        /*case 6: cell.setCellValue(TotalPedidos);
	                    cell.setCellStyle(style3);
	                    break;*/
			}
			numFilaDet++;
		}
		hoja.autoSizeColumn((short)ColMontoTotGral);


	    return hoja;
	}


	public HSSFSheet ResumenDemadaPorComuna(HSSFWorkbook wb, HSSFSheet hoja, HashMap list_Comunas, Calendar calendario){
	    int colDet = 0;

	    //HashMap list_Comunas = ResumenMensualPedidosXComuna(local);
	    
	    SortedSet set = new TreeSet();
	    
	    //INGRESA LOS OBJETOS AL "TREESET" PARA LUEGO SER RECUPERADOS EN ORDEN
	    for (Iterator it=list_Comunas.keySet().iterator(); it.hasNext(); ) {
	        String key = it.next().toString();
	        set.add(key); // Listado de Comunas en Orden Ascendente
        }

	    
	    numFilaDet=numFilaDet + 28;
	    int tempNumFilaDet = numFilaDet;
	    
	    
	    
	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    //HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.RED.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    //HSSFCellStyle style3 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
        
	    
		HSSFRow row3 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("Demanda por comuna"));
		cell_3_0.setCellStyle(style1);


		hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		numFilaDet = tempNumFilaDet;
		numFilaDet--;


		//******************************************
		
		for (int i=0; i<=1; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    
		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
				
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            break;
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            break;
				}
				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			if (i==1){
				HSSFCell cell  = row.createCell(colDet);
				cell.setCellStyle(style2);

		        cell.setCellValue(new HSSFRichTextString("Total General"));
				cell.setCellStyle(style1);

			}
			numFilaDet++;
		}


        //*******************************************
	    
		
        Iterator it = set.iterator();
	    while (it.hasNext()) {
	        String comuna = it.next().toString();
	        
	        BeanPedidosComunaMensual pcm = (BeanPedidosComunaMensual) list_Comunas.get(comuna);
	        
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    
		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.GREEN.index);
		    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
		    
		    colDet = 0;
		    
	        for (int j=0; j<=(diasMes+1); j++){
	            
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

	            HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
	            if (j==0){
	                cell.setCellValue(new HSSFRichTextString(pcm.getComuna()));
	                cell.setCellStyle(style2);
	            }else if (j>0 && j<=diasMes){
	                BeanPedidosComunaDiario pcd = (BeanPedidosComunaDiario) pcm.getPedidosDiarios().get(key);
	                if (pcd == null){
	                    cell.setCellValue(0);
	                }else{
	                    cell.setCellValue(pcd.getCant_pedidos());
	                }
	            }else if (j>diasMes){
	                cell.setCellValue(pcm.getTotalPedidos());
	                cell.setCellStyle(style2);
	            }
	            hoja.autoSizeColumn((short)j);
	        }
	        numFilaDet++;
	    }

	    return hoja;
	}



	public HSSFSheet ResumenDemadaPorcentualPorComuna(HSSFWorkbook wb, HSSFSheet hoja, HashMap list_Comunas, Calendar calendario){
	    int colDet = 0;

	    //HashMap list_Comunas = ResumenMensualPedidosXComuna(local);
	    
	    SortedSet set = new TreeSet();
	    
	    //INGRESA LOS OBJETOS AL "TREESET" PARA LUEGO SER RECUPERADOS EN ORDEN
	    for (Iterator it=list_Comunas.keySet().iterator(); it.hasNext(); ) {
	        String key = it.next().toString();
	        set.add(key); // Listado de Comunas en Orden Ascendente
        }

	    
	    numFilaDet=numFilaDet + 3;
	    int tempNumFilaDet = numFilaDet;
	    
	    
	    
	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.GREEN.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle stylePorcentaje = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    //stylePorcentaje.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
	    stylePorcentaje.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));

	    HSSFCellStyle stylePorcentaje2 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    //stylePorcentaje2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
	    stylePorcentaje2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));

	    
		HSSFRow row3 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("Demanda Porcentual por comuna"));
		cell_3_0.setCellStyle(style1);


		hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		numFilaDet = tempNumFilaDet;
		numFilaDet--;


		//******************************************
		
		for (int i=0; i<=1; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    
		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
				
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            break;
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            break;
				}
				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			if (i==1){
				HSSFCell cell  = row.createCell(colDet);
				cell.setCellStyle(style2);

		        cell.setCellValue(new HSSFRichTextString("Total General"));
				cell.setCellStyle(style1);

			}
			numFilaDet++;
		}


        //*******************************************

		// Obtiene el total de pedidos por Día.
		HashMap PedidosXDia = new HashMap();
		double TotalGeneral = 0;
		
        Iterator it = set.iterator();
	    while (it.hasNext()) {
	        String comuna = it.next().toString();
	        
	        BeanPedidosComunaMensual pcm = (BeanPedidosComunaMensual) list_Comunas.get(comuna);
	        TotalGeneral += pcm.getTotalPedidos();
	        
	        for (int j=1; j<=(diasMes+1); j++){
	            
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

                BeanPedidosComunaDiario pcd = (BeanPedidosComunaDiario) pcm.getPedidosDiarios().get(key);
                if (pcd != null){
                    if (PedidosXDia.get(key) == null){
                        PedidosXDia.put(key, pcd.getCant_pedidos() + "");
                    }else{
                        double sumaDia = Double.parseDouble(PedidosXDia.get(key).toString()) + pcd.getCant_pedidos();
                        PedidosXDia.put(key, sumaDia + "");
                    }
                }
	        }
	    }
	    
        //*******************************************
		
        Iterator it2 = set.iterator();
	    while (it2.hasNext()) {
	        String comuna = it2.next().toString();
	        
	        BeanPedidosComunaMensual pcm = (BeanPedidosComunaMensual) list_Comunas.get(comuna);
	        
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    
		    colDet = 0;
		    
	        for (int j=0; j<=(diasMes+1); j++){
	            
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

	            HSSFCell cell = row.createCell(j);
				cell.setCellStyle(stylePorcentaje2);
	            if (j==0){
	                cell.setCellValue(new HSSFRichTextString(pcm.getComuna()));
	                cell.setCellStyle(style2);
	            }else if (j>0 && j<=diasMes){
	                BeanPedidosComunaDiario pcd = (BeanPedidosComunaDiario) pcm.getPedidosDiarios().get(key);
	                if (pcd == null){
	                    cell.setCellValue(0);
	                }else{
	                    cell.setCellValue(pcd.getCant_pedidos()/Double.parseDouble(PedidosXDia.get(key).toString()));
	                }
	            }else if (j>diasMes){
	                cell.setCellStyle(stylePorcentaje);
	                cell.setCellValue(pcm.getTotalPedidos()/TotalGeneral);
	            }
	            hoja.autoSizeColumn((short)j);
	        }
	        numFilaDet++;
	    }

	    return hoja;
	}



	public HSSFSheet ResumenTipoDespacho(HSSFWorkbook wb, HSSFSheet hoja, HashMap list_TipoDesp, Calendar calendario){
	    int colDet = 0;
	    double TipoDespN = 0;
		double TipoDespE = 0;
		double TipoDespEX = 0;
		double TotalPedidos = 0;

		
	    numFilaDet=numFilaDet + 3;
	    int tempNumFilaDet = numFilaDet;
	    
	    //HashMap list_TipoDesp = ResumenMensualTipoDespacho(local);
	    
	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.RED.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle style3 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
        
		/*HSSFRow row3 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("TIPO DESPACHO"));
		cell_3_0.setCellStyle(style1);

		numFilaDet++;
		HSSFRow row4 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_4_0 = row4.createCell(0);
		cell_4_0.setCellValue(new HSSFRichTextString("NORMAL"));
		cell_4_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row5 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_5_0 = row5.createCell(0);
		cell_5_0.setCellValue(new HSSFRichTextString("ECONOMICO"));
		cell_5_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row6 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_6_0 = row6.createCell(0);
		cell_6_0.setCellValue(new HSSFRichTextString("EXPRESS"));
		cell_6_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row7 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_7_0 = row7.createCell(0);
		cell_7_0.setCellValue(new HSSFRichTextString("TOTAL PEDIDOS DESPACHADOS"));
		cell_7_0.setCellStyle(style1);*/

		/*numFilaDet++;
		HSSFRow row8 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_8_0 = row8.createCell(0);
		cell_8_0.setCellValue(new HSSFRichTextString("Total Pedidos Reprogramados"));
		cell_8_0.setCellStyle(style1);*/



		hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		numFilaDet = tempNumFilaDet;
		numFilaDet--;
		for (int i=0; i<6; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    

		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1 && i<5){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }else if (i >= 5){
			        font.setColor(HSSFColor.RED.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(style);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

			    BeanTipoDespachoMensual TDesp = (BeanTipoDespachoMensual) list_TipoDesp.get(key);
				
				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            break;
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            break;
                    case 2: if (TDesp != null){
                                cell.setCellValue(TDesp.getTipoDespN());
                                TipoDespN += TDesp.getTipoDespN();
                                TotalPedidos += TDesp.getTipoDespN();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;

                    case 3: if (TDesp != null){
                                cell.setCellValue(TDesp.getTipoDespE());
                                TipoDespE += TDesp.getTipoDespE();
                                TotalPedidos += TDesp.getTipoDespE();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                            
                    case 4: if (TDesp != null){
                                cell.setCellValue(TDesp.getTipoDespEX());
                                TipoDespEX += TDesp.getTipoDespEX();
                                TotalPedidos += TDesp.getTipoDespEX();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                            
                    case 5: if (TDesp != null){
                                cell.setCellValue(TDesp.getTotalDespachos());
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
				}

				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			numFilaDet++;
		}
		
		numFilaDet = tempNumFilaDet;
		//numFilaDet++;
		int ColMontoTotGral = colDet;

		for (int i=1; i<6; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
			
			HSSFCell cell  = row.createCell(ColMontoTotGral);
			cell.setCellStyle(style2);

			switch (i){
				case 1: cell.setCellValue(new HSSFRichTextString("Total General"));
				        cell.setCellStyle(style1);
				        break;
				        
				case 2: cell.setCellValue(TipoDespN);
                        cell.setCellStyle(style2);
                        break;
		                
		        case 3: cell.setCellValue(TipoDespE);
                        cell.setCellStyle(style2);
                        break;
                        
				case 4: cell.setCellValue(TipoDespEX);
                        cell.setCellStyle(style2);
                        break;
                        
		        case 5: cell.setCellValue(TotalPedidos);
	                    cell.setCellStyle(style3);
	                    break;
			}
			numFilaDet++;
		}
		hoja.autoSizeColumn((short)ColMontoTotGral);


	    return hoja;
	}

	

	public HSSFSheet ResumenPorcentualTipoDespacho(HSSFWorkbook wb, HSSFSheet hoja, HashMap list_TipoDesp, Calendar calendario){
	    int colDet = 0;
	    double TipoDespN = 0;
		double TipoDespE = 0;
		double TipoDespEX = 0;
		double TotalPedidos = 0;

		
	    numFilaDet=numFilaDet + 3;
	    int tempNumFilaDet = numFilaDet;
	    
	    //HashMap list_TipoDesp = ResumenMensualTipoDespacho(local);
	    
	    /*Calendar cal = Calendar.getInstance();
		
	    if (Calendar.DAY_OF_MONTH == 1){
	        cal.add(Calendar.MONTH, -1);
	    }*/

	    int diasMes = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
	    
	    HSSFFont fuente1 = getFuenteNegrita(wb, HSSFColor.WHITE.index);
	    HSSFFont fuente2 = getFuenteNegrita(wb, HSSFColor.BLACK.index);
	    HSSFFont fuente3 = getFuenteNegrita(wb, HSSFColor.RED.index);
	    HSSFFont fuente4 = getFuenteNegrita(wb, HSSFColor.GREEN.index);

	    HSSFCellStyle style1 = getEstiloConBordes(wb, fuente1, HSSFCellStyle.ALIGN_LEFT, HSSFColor.LIME.index);
	    HSSFCellStyle style2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle style3 = getEstiloConBordes(wb, fuente3, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    HSSFCellStyle stylePorcentaje = getEstiloConBordes(wb, fuente4, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    stylePorcentaje.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
	    
	    HSSFCellStyle stylePorcentaje2 = getEstiloConBordes(wb, fuente2, HSSFCellStyle.ALIGN_LEFT, HSSFColor.WHITE.index);
	    stylePorcentaje2.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));

	    
		/*HSSFRow row3 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_3_0 = row3.createCell(0);
		cell_3_0.setCellValue(new HSSFRichTextString("TIPO DESPACHO PORCENTUAL"));
		cell_3_0.setCellStyle(style1);

		numFilaDet++;
		HSSFRow row4 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_4_0 = row4.createCell(0);
		cell_4_0.setCellValue(new HSSFRichTextString("NORMAL"));
		cell_4_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row5 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_5_0 = row5.createCell(0);
		cell_5_0.setCellValue(new HSSFRichTextString("ECONOMICO"));
		cell_5_0.setCellStyle(style2);

		numFilaDet++;
		HSSFRow row6 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_6_0 = row6.createCell(0);
		cell_6_0.setCellValue(new HSSFRichTextString("EXPRESS"));
		cell_6_0.setCellStyle(style2);*/

		/*numFilaDet++;
		HSSFRow row7 = hoja.createRow((short) numFilaDet);
		HSSFCell cell_7_0 = row7.createCell(0);
		cell_7_0.setCellValue(new HSSFRichTextString("TOTAL PEDIDOS DESPACHADOS"));
		cell_7_0.setCellStyle(style1);*/


		hoja.autoSizeColumn((short)0);
	    
	    SimpleDateFormat FormatYear = new SimpleDateFormat("yyyy");
	    SimpleDateFormat FormatMes = new SimpleDateFormat("MM");
		String year = FormatYear.format(calendario.getTime());
		String mes  = FormatMes.format(calendario.getTime());
		
		Locale locale = Locale.getDefault();

		numFilaDet = tempNumFilaDet;
		numFilaDet--;
		for (int i=0; i<5; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
		    

		    HSSFFont font = getFuenteNegrita(wb, HSSFColor.WHITE.index);
		    colDet = 1;
			for (int j=1; j<=diasMes; j++){
			    
			    // DEFINE ESTILO POR CELDA
			    HSSFCellStyle style = getEstiloConBordes(wb, font, HSSFCellStyle.ALIGN_LEFT);
			    if (i==0 || i==1){
			        style.setFillForegroundColor(HSSFColor.LIME.index);
			    }else{
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }
			    if (i>1 && i<5){
			        font.setColor(HSSFColor.GREEN.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }/*else if (i >= 5){
			        font.setColor(HSSFColor.RED.index);
			        style.setFont(font);
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			    }*/
				HSSFCell cell = row.createCell(j);
				cell.setCellStyle(stylePorcentaje);
				
				//******************************************
			    //*********  ASIGNA VALOR A CELDA  *********
			    Calendar hoy = Calendar.getInstance();
			    hoy.set(Integer.parseInt(year), Integer.parseInt(mes)-1, j);
			    
				String key = "";
			    if (j<10){
			        key = year + mes + "0" + j;
			    }else{
			        key = year + mes + j;
			    }

			    BeanTipoDespachoMensual TDesp = (BeanTipoDespachoMensual) list_TipoDesp.get(key);
				
				//DIA DE LA SEMANA
				switch (i){
				    case 0: SimpleDateFormat FormatDiaSemana = new SimpleDateFormat("EEE", locale);
				            String diaSemana = FormatDiaSemana.format(hoy.getTime());
				            cell.setCellValue(new HSSFRichTextString(diaSemana));
				            cell.setCellStyle(style);
				            break;
				            
				    case 1: SimpleDateFormat FormatDia = new SimpleDateFormat("dd MMM", locale);
                            String dia = FormatDia.format(hoy.getTime());
                            cell.setCellValue(new HSSFRichTextString(dia));
                            cell.setCellStyle(style);
                            break;
                            
                    case 2: if (TDesp != null){
                                cell.setCellValue(TDesp.getTipoDespN()/TDesp.getTotalDespachos());
                                TipoDespN += TDesp.getTipoDespN();
                                TotalPedidos += TDesp.getTipoDespN();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;

                    case 3: if (TDesp != null){
                                cell.setCellValue(TDesp.getTipoDespE()/TDesp.getTotalDespachos());
                                TipoDespE += TDesp.getTipoDespE();
                                TotalPedidos += TDesp.getTipoDespE();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                            
                    case 4: if (TDesp != null){
                                cell.setCellValue(TDesp.getTipoDespEX()/TDesp.getTotalDespachos());
                                TipoDespEX += TDesp.getTipoDespEX();
                                TotalPedidos += TDesp.getTipoDespEX();
                            }else{
                                cell.setCellValue(0);
                            }
                            break;
                            
                    /*case 5: if (TDesp != null){
                                cell.setCellValue(TDesp.getTotalDespachos());
                            }else{
                                cell.setCellValue(0);
                            }
                            break;*/
				}

				hoja.autoSizeColumn((short)colDet);
				colDet++;
			}
			numFilaDet++;
		}
		
		numFilaDet = tempNumFilaDet;
		//numFilaDet++;
		int ColMontoTotGral = colDet;

		for (int i=1; i<5; i++){
		    HSSFRow row = null;
		    if (hoja.getRow((short) numFilaDet) == null){
		        row = hoja.createRow((short) numFilaDet);
		    }else{
		        row = hoja.getRow((short) numFilaDet);
		    }
			
			HSSFCell cell  = row.createCell(ColMontoTotGral);
			cell.setCellStyle(stylePorcentaje2);

			switch (i){
				case 1: cell.setCellValue(new HSSFRichTextString("Total General"));
				        cell.setCellStyle(style1);
				        break;
				        
				case 2: cell.setCellValue(TipoDespN/TotalPedidos);
                        break;
		                
		        case 3: cell.setCellValue(TipoDespE/TotalPedidos);
                        break;
                        
				case 4: cell.setCellValue(TipoDespEX/TotalPedidos);
                        break;
                        
		        /*case 5: cell.setCellValue(TotalPedidos);
	                    cell.setCellStyle(style3);
	                    break;*/
			}
			numFilaDet++;
		}
		hoja.autoSizeColumn((short)ColMontoTotGral);


	    return hoja;
	}


/*
	public HSSFSheet Grafico1(HSSFWorkbook wb, HSSFSheet hoja){
	    
	    
	    HSSFClientAnchor a = new HSSFClientAnchor( 0, 0, 1023, 255, (short) 1, 0, (short) 1, 0 );
	    HSSFPatriarch patriarch = hoja.createDrawingPatriarch();
	    HSSFShapeGroup group = patriarch.createGroup( a );
	    group.setCoordinates( 0, 0, 80 * 4 , 12 * 23  );
	    float verticalPointsPerPixel = a.getAnchorHeightInPoints(hoja) / (float)Math.abs(group.getY2() - group.getY1());
	    EscherGraphics g = new EscherGraphics( group, wb, Color.black, verticalPointsPerPixel );
	    EscherGraphics2d g2d = new EscherGraphics2d( g );
	    drawChemicalStructure( g2d );

	    return hoja;
	}
*/
	
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
		} catch (Exception e) {
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
                
	            //Recupera la extensión del archivo
	            //String Ext = filename.substring(filename.indexOf('.')); 
                
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
    
	
	
	public HashMap RecuperaListaArchivos(String Path){
	    //Recupera el listado de Archivos que seran enviados por E-Mail
	    
	    HashMap archivos = new HashMap();
	    
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
                
	            //Recupera la extensión del archivo
	            String Ext = filename.substring(filename.indexOf('.')); 
                
	            if (Ext.equalsIgnoreCase(".zip")){
		            //Verifica si existe un archivo o directorio
		    	    boolean exists = (new File(Path + filename)).exists();
		    	    if (exists) {
		    	        // File or directory exists
	                    archivos.put(filename, Path + filename);
		    	        
		    	        //elimina un archivo
		    		    /*boolean success = (new File(Path + filename)).delete();
		    		    if (success) {
		    		        logger.debug("El Archivo '" + filename + "' fue Eliminada con Exito");
		    		    }*/
		    	        
		    	    } else {
		    	        // File or directory does not exist
		    	        logger.debug("El Archivo '" + filename + "' NO Existe.");
		    	    }
	            }
	        }
	    }
	    return archivos;
	}
    

	public void EnviaEMail(BeanParamConfig param, HashMap archivos){
	    String METHOD_NAME = "EnviaEMail";
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String fecha = dateFormat.format(cal.getTime());
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
            
            if (param.getCc() != null){
                for (Iterator it=param.getCc().keySet().iterator(); it.hasNext(); ){
    		        String key = it.next().toString();
                    msg.addRecipient(Message.RecipientType.CC,new InternetAddress(key));
                }
            }
            
            if (param.getCco() != null){
                for (Iterator it=param.getCco().keySet().iterator(); it.hasNext(); ){
    		        String key = it.next().toString();
                    msg.addRecipient(Message.RecipientType.BCC,new InternetAddress(key));
                }
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
            Iterator it = archivos.keySet().iterator();
    	    while (it.hasNext()) {
    	        String archivo = it.next().toString();

                messageBodyPart = new MimeBodyPart();
                String PathYarchivo = (String)archivos.get(archivo);
                
                //Verifica la Existencia del Archivo a Enviar
                boolean exists = (new File(PathYarchivo)).exists();
                
                if (exists){
                    DataSource source = new FileDataSource(PathYarchivo);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(archivo);
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            
            msg.setContent(multipart);
            msg.setSentDate(new Date());
            Transport.send(msg);
            
        }catch(MessagingException e){
            logger.debug(fecha + " - ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
            e.printStackTrace();
        }catch(Exception e){
            logger.debug(fecha + " - ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
            e.printStackTrace();
        }
	}
	

	public void EnviaEMail(BeanParamConfig param, String Path, String NombreArchivo){
	    String METHOD_NAME = "EnviaEMail";
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String fecha = dateFormat.format(cal.getTime());
        try{
            Properties prop =System.getProperties();
            prop.put("mail.smtp.host", param.getServer());
            prop.put("mail.smtp.port", param.getPuerto());
            Session ses1  = Session.getDefaultInstance(prop,null);
            MimeMessage msg = new MimeMessage(ses1);
            msg.setFrom(new InternetAddress(param.getFrom()));
            
            //Map destinatarios = getDestinatariosEMail();
            /*if (destinatarios != null && destinatarios.size() > 0){
                for (Iterator it=destinatarios.values().iterator(); it.hasNext(); ){
    	            String email = it.next().toString();
                    msg.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
                }
            }*/
            
            for (Iterator it=param.getTo().keySet().iterator(); it.hasNext(); ){
		        String key = it.next().toString();
                msg.addRecipient(Message.RecipientType.TO,new InternetAddress(key));
            }
            
            if (param.getCc() != null){
                for (Iterator it=param.getCc().keySet().iterator(); it.hasNext(); ){
    		        String key = it.next().toString();
                    msg.addRecipient(Message.RecipientType.CC,new InternetAddress(key));
                }
            }
            
            if (param.getCco() != null){
                for (Iterator it=param.getCco().keySet().iterator(); it.hasNext(); ){
    		        String key = it.next().toString();
                    msg.addRecipient(Message.RecipientType.BCC,new InternetAddress(key));
                }
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
            logger.debug(fecha + " - ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
            e.printStackTrace();
        }catch(Exception e){
            logger.debug(fecha + " - ERROR: " + this.getClass() + "." + METHOD_NAME + " : " + e);
            e.printStackTrace();
        }
	}
	

    /*public Map getDestinatariosEMail(){
		String METHOD_NAME = "getDestinatariosEMail";
		Connection conn = null;
		String Query = "";
		Map destinatarios = new LinkedHashMap();

		try{
		    ConexionUtil cu = new ConexionUtil();
			conn = cu.getConexion();
			Statement stmt = conn.createStatement();
			Query = "SELECT SYF.NOMBRES, SYF.APELLIDOS, SYF.MAIL "
                  + "FROM BODBA.BO_MAIL_SYF SYF "
                  + "WHERE SYF.ACTIVADO = '1'";
			
			logger.debug("Query (getDestinatariosEMail): " + Query);
			ResultSet rs = stmt.executeQuery(Query);
			while (rs.next()){
			    String indice = rs.getString("MAIL").trim();
			    String email  = rs.getString("MAIL").trim();
			    //String email  = rs.getString("NOMBRES").trim() + " " + 
			    //                rs.getString("APELLIDOS").trim() + "<" + 
			    //                rs.getString("MAIL").trim() + ">";
			    
			    destinatarios.put(indice, email);
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
    	return destinatarios;
    }*/

}
