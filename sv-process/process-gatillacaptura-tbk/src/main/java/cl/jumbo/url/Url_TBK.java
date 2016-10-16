package cl.jumbo.url;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import cl.jumbo.url.purga.Purga;
import cl.jumbo.url.util.Db;

public class Url_TBK {
	protected static Logger logger = Logger.getLogger(Url_TBK.class.getName());

	// ruta del properties Config
	static PropertyResourceBundle configBundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("Config");
	// ruta del properties purga
	static ResourceBundle purgaBundle = ResourceBundle.getBundle("purga");

	public static void main(String[] args) {
		try {
			logger.info("***	INIT Url_TBK	***");
			String URLCaptura = configBundle.getString("URLCaptura");
			String ServerName = configBundle.getString("ConfigName");			
			if (logger.isDebugEnabled()) {
				logger.debug("Server URlCaptura: " + ServerName);
			}
			URL url = new URL(URLCaptura);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null);
			wr.close();
			rd.close();
			String user = purgaBundle.getString("USER");
			String password = purgaBundle.getString("PASSWORD");
			String driver = purgaBundle.getString("DRIVER");
			String urlConexion = purgaBundle.getString("URL");
			String DbName = purgaBundle.getString("purgaName");
			if (logger.isDebugEnabled()) {
				logger.debug("DATA BASE: " + DbName);
			}
			Connection con = Db.conexion(user, password, driver, urlConexion);	
			logger.info("***	INIT PURGA");
			Purga p = new Purga();
			p.dejaPedidosTemporales(con);
			p.liberaCapacidadesDespacho(con);
			p.liberaCapacidadesPicking(con);
			p.dejaPedidosParaBorrar(con);
			logger.info("END PURGA");
			Db.close(con);	
		} catch (Exception e) {
			logger.error("ERROR Url_TBK: ", e);
		}
		logger.info("***	END Url_TBK		***");
	}
}