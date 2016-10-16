package cl.cencosud.jumbo.home;

import java.io.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;

import cl.cencosud.jumbo.beans.BeanParamConfig;

public class SyFCrontabRun {

	private static Logger logger;
	private static final String CONFIG_BUNDLE_NAME = "Config";

	static PropertyResourceBundle configBundle = (PropertyResourceBundle) PropertyResourceBundle
			.getBundle(CONFIG_BUNDLE_NAME);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd MMM yyyy HH:mm:ss.SSS");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat(
			"yyyyMMdd_HHmmss");

	public SyFCrontabRun() {
	}

	static BeanParamConfig getParamProperties() {
		BeanParamConfig param = new BeanParamConfig();
		param.setNombreArchivo(configBundle.getString("NombreArchivo"));
		param.setTipo(configBundle.getString("Tipo"));
		param.setHorarios(parseaParam(configBundle.getString("Horarios")));
		param.setUltimaJornada(configBundle.getString("UltimaJornada"));
		param.setNumReintentos(configBundle.getString("NumReintentos"));
		param.setIntervalo(configBundle.getString("Intervalo"));
		param.setServer(configBundle.getString("mail.server"));
		param.setFrom(configBundle.getString("mail.from"));
		param.setTo(parseaParam(configBundle.getString("mail.to")));
		param.setCc(parseaParam(configBundle.getString("mail.cc")));
		param.setCco(parseaParam(configBundle.getString("mail.cco")));
		param.setSubject(configBundle.getString("mail.subject"));
		param.setMensaje(configBundle.getString("mail.mensaje"));
		param.setPathImagen1(configBundle.getString("PathImagen1"));
		param.setPathImagen2(configBundle.getString("PathImagen2"));
		param.setPorcentaje(Integer.parseInt(configBundle
				.getString("PorcentajeFaltante")));
		param.setObtenerMails(configBundle.getString("obtenerMail").trim());
		return param;
	}

	private static Map parseaParam(String listaHorarios) {
		Map parametros = new LinkedHashMap();
		String valor;
		for (StringTokenizer stringtokenizer = new StringTokenizer(
				listaHorarios, ",", false); stringtokenizer.hasMoreTokens(); parametros
				.put(valor, valor)) {
			valor = stringtokenizer.nextToken();
		}

		return parametros;
	}

	public static String convertTimeToString(Time time, String formatoOut) {
		String retorna = new String();
		if (time != null) {
			SimpleDateFormat sdfOutput = new SimpleDateFormat(formatoOut);
			retorna = sdfOutput.format(time).toString();
		} else {
			retorna = "";
		}
		return retorna;
	}

	public static Time convertStringToTime(String strFecha,
			String formatoIngreso) {
		Time fecha = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formatoIngreso);
		if (strFecha != null && !strFecha.equals("")) {
			Date date = null;
			try {
				date = sdf.parse(strFecha);
			} catch (ParseException e) {
				date = new Date();
			}
			fecha = new Time(date.getTime());
		}
		return fecha;
	}

	private static void run(BeanParamConfig param, SyFCrontabRunHome home,
			String HoraIni, String HoraFin, String HoraIniUltJor,
			String HoraFinUltJor) {
		
		logger.debug("\n\nTASKS - Fecha y Hora de planificaci\363n del Informe de S&F: "
				+ dateFormat.format(new Date()));
		
		System.out.println("Ultima jornada  :" + HoraIniUltJor + " - "
				+ HoraFinUltJor);
		
		String Fecha = dateFormat2.format(new Date());
		String filename = param.getNombreArchivo() + Fecha;
		String Path = "";
		
		try {
			File fd = new File("temp");
			Path = fd.getCanonicalPath();
			logger.debug("Path (temp):" + Path);
			System.out.println("Path (temp):" + Path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		home.EliminaArchivosXLS(Path + "/");
		String RutaArchivoXLS = Path + "/" + filename + ".xls";
		logger.debug("RutaArchivoXLS: " + RutaArchivoXLS);
		System.out.println("RutaArchivoXLS: " + RutaArchivoXLS);
		
		logger.debug("Jornada: " + HoraIni + " - " + HoraFin);
		if ("".equals(HoraIni.trim()) || "".equals(HoraFin.trim())) {
			HoraIni = HoraIniUltJor;
			HoraFin = HoraFinUltJor;
		}
		System.out.println("Jornada ****** ****** : " + HoraIni + " - "
				+ HoraFin);
		
		System.out.println("Obtener Sustitutos Faltantes ......");
		Map SustFaltLocal = home.getSustitutosFaltantes(HoraIni, HoraFin);
		System.out.println("Obtener Sustitutos Faltantes : OK");
		System.out.println("---------------------------------");
		
		System.out.println("Obtener resumen Sustitutos Faltantes ......");
		Map Resumen = home.getResumenSF(HoraIni, HoraFin);
		System.out.println("Obtener resumen Sustitutos Faltantes : OK");
		System.out.println("---------------------------------");
		
		Map ResumenFinalDia = null;
		System.out
				.println("=====================================================");
		System.out.println(" Validar : " + HoraIni + " = " + HoraIniUltJor
				+ " && " + HoraFinUltJor + " = " + HoraFin);
		System.out
				.println("=====================================================");
		
		if (HoraIni.equals(HoraIniUltJor) && HoraFin.equals(HoraFinUltJor)) {
			System.out
					.println("__________________________________________________________________");
			System.out.println("Obtener resumen Final dia ......");
			ResumenFinalDia = home.getResumenSF("00:00:00", HoraFin);
			System.out.println("Obtener resumen Final dia : OK");
			System.out
					.println("__________________________________________________________________");
		}
		
		System.out.println("Gererar Excel ......");
		home.GeneraExcel(SustFaltLocal, Resumen, ResumenFinalDia,
				RutaArchivoXLS, param, HoraIni.subSequence(0, 2).toString(),
				HoraFin.subSequence(0, 2).toString());
		System.out.println("EXCEL generado con Exito.");
		System.out.println("---------------------------------");
		logger.debug("EXCEL generado con Exito.");
		
		System.out.println("Path XLS : " + Path + "/" + filename + ".xls");
		home.CreaZipXLS(Path + "/", filename + ".xls");
		logger.debug("COMPREASION A ZIP generado con Exito.");
		
		try {
			System.out.println("Path Mail : " + Path + "/" + filename + ".zip");
			home.EnviaEMail(param, Path + "/", filename + ".zip");
			logger.debug("CORREO Enviado con Exito.");
			System.out.println("CORREO Enviado con Exito.");
		} catch (RuntimeException e1) {
			System.out.println("Problemas al Enviar correo : [ "
					+ e1.toString() + " ]");
		}
	}

	public static void main(String args[]) {
		System.out.println("***___INICIO SYF___***");
		SyFCrontabRunHome home = SyFCrontabRunHome.getHome();
		Date now = new Date();
		Time horaNow = new Time(now.getTime());
		String actualHora = convertTimeToString(horaNow, "HH:mm:ss");
		long longHora = convertStringToTime(actualHora, "HH:mm:ss").getTime();
		BeanParamConfig param = getParamProperties();
		Map lstHorarios = param.getHorarios();
		int pos = 0;
		String _HoraIni = "";
		String _HoraFin = "";
		String HoraIniUltJor = "";
		String HoraFinUltJor = "";
		
		for (Iterator it = lstHorarios.keySet().iterator(); it.hasNext();) {
			String key = it.next().toString();
			pos = key.indexOf("-");
			String HoraIni = "";
			String HoraFin = "";
			HoraIni = key.substring(0, pos);
			HoraFin = key.substring(pos + 1);
			long longHoraIni = convertStringToTime(HoraIni, "HH:mm:ss").getTime();
			long longHoraFin = convertStringToTime(HoraFin, "HH:mm:ss").getTime();
			
			HoraIniUltJor = param.getUltimaJornada().substring(0, pos);
			HoraFinUltJor = param.getUltimaJornada().substring(pos + 1);
			System.out.println(" Validar : " + HoraIni + " <= " + actualHora
					+ " && " + actualHora + " < " + HoraFin);
			
			if (longHoraIni <= longHora && longHora < longHoraFin) {
				System.out.println(" Validar : VERDADERO");
				System.out.println("Jornada Actual : " + HoraIni + " - "
						+ HoraFin);
				System.out
						.println("_____________________________________________________________");
				
				_HoraIni = HoraIni;
				_HoraFin = HoraFin;
				System.out.println("Reportar Jornada : " + _HoraIni + " - "
						+ _HoraFin);
				System.out
						.println("_____________________________________________________________");
				run(param, home, HoraIni, HoraFin, HoraIniUltJor, HoraFinUltJor);
				break;
			}
			
			if (longHora >= longHoraFin && HoraFin.equals("23:59:00")) {
				System.out.println(" Validar : " + actualHora + " >= "
						+ HoraFin);
				System.out.println("Ultima Jornada : " + HoraIni + " - "
						+ HoraFin);
				System.out.println("Reportar Ultima Jornada : " + HoraIniUltJor
						+ " - " + HoraFinUltJor);
				System.out
						.println("_____________________________________________________________");
				run(param, home, HoraIni, HoraFin, HoraIniUltJor, HoraFinUltJor);
				break;
			}
			
			System.out.println("No Reportar Jornada : " + HoraIni + " - "
					+ HoraFin);
			System.out
					.println("*************************************************************");
			
			_HoraIni = HoraIni;
			_HoraFin = HoraFin;
		}

		System.out.println("***___FIN SYF___***");
	}

	static {
		logger = Logger.getLogger(cl.cencosud.jumbo.home.SyFCrontabRun.class);
	}
}