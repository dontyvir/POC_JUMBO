package cl.cencosud.archivossap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Local;
import cl.cencosud.util.LogUtil;
import cl.cencosud.util.Parametros;

/**
 * Programa que genera un archivo por local diariamente con los productos
 * publicados en jumbo.cl
 */
public class GenerarArchivoProductos {

	static {
		LogUtil.initLog4J();
	}

	private static Logger logger = Logger.getLogger(GenerarArchivoProductos.class);

	public static void main(String[] args) throws Exception {
		GenerarArchivoProductos archivoProductos = new GenerarArchivoProductos();
		archivoProductos.generar();
	}

	public void generar() throws Exception {
		String user = Parametros.getString("USER");
		String password = Parametros.getString("PASSWORD");
		String driver = Parametros.getString("DRIVER");
		String url = Parametros.getString("URL");
		Connection con = DbCarga.conexion(user, password, driver, url);
		logger.debug("CON : " + con);
		String path = Parametros.getString("PATH_ARCHIVOS_PARA_SAP");

		List locales = DbCarga.locales(con);

		for (int i = 0; i < locales.size(); i++) {
			Local local = (Local) locales.get(i);
			List lista = DbCarga.productos(con, local.getId());
			String archivo = generarNombreArchivo(local.getCodigo());
			grabarArchivo(lista, path + archivo);
		}

		logger.debug("Fin");
	}

	public void grabarArchivo(List lista, String archivo) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
		bw.write("ARTICULO\tCOD_BARRA\tTIENDA\tARTICULO_GRUPO");
		bw.newLine();
		for (int i = 0; i < lista.size(); i++) {
			String[] producto = (String[]) lista.get(i);
			for (int j = 0; j < producto.length; j++) {
				bw.write(producto[j] + (j != producto.length - 1 ? "\t" : ""));
			}
			bw.newLine();
		}
		bw.close();

		bw = new BufferedWriter(new FileWriter(archivo + ".trg"));
		bw.newLine();
		bw.close();
	}

	public String generarNombreArchivo(String codigoLocal) {
		String nombre = "JCL" + codigoLocal;
		GregorianCalendar hoy = new GregorianCalendar();
		nombre += hoy.get(Calendar.YEAR);
		int mes = hoy.get(Calendar.MONTH) + 1;
		nombre += (mes < 10 ? "0" : "") + mes;
		int dia = hoy.get(Calendar.DAY_OF_MONTH);
		nombre += (dia < 10 ? "0" : "") + dia;
		int hora = hoy.get(Calendar.HOUR_OF_DAY);
		nombre += (hora < 10 ? "0" : "") + hora;
		int minuto = hoy.get(Calendar.MINUTE);
		nombre += (minuto < 10 ? "0" : "") + minuto;
		int segundo = hoy.get(Calendar.SECOND);
		nombre += (segundo < 10 ? "0" : "") + segundo;
		return nombre;
	}
}
