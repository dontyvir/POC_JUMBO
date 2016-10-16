package cl.cencosud.procesos.chaordic.ctrl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import cl.cencosud.procesos.chaordic.dao.ChaordicDAO;
import cl.cencosud.procesos.chaordic.dto.ConfigDTO;
import cl.cencosud.procesos.chaordic.log.Logging;

/**
 * @author 
 * @
 */
public class ChaordicCTRL {

    private  Logging logger = new Logging(this);


    public ConfigDTO recuperarParametros() throws Exception {

        ResourceBundle rb = ResourceBundle.getBundle("chaordicConf");
        ConfigDTO conf = new ConfigDTO();
        conf.setIp(rb.getString("chaordic.ip_base_datos"));
        conf.setPuerto(rb.getString("chaordic.puerto"));
        
        conf.setUsuario(rb.getString("chaordic.usuario"));
        conf.setPass(rb.getString("chaordic.pass"));
        conf.setBaseDatos(rb.getString("chaordic.base_datos"));
        /*conf.setDirectorioRespaldo(rb.getString("chaordic.directorio_respaldo"));
        conf.setAsunto(rb.getString("chaordic.asunto"));
        conf.setDestinatario(rb.getString("chaordic.destinatario"));*/
        conf.setDriver(rb.getString("chaordic.driver"));
        conf.setRutArchivo(rb.getString("chaordic.ruta_archivo"));
   
        return conf;

    }
    
    /**
     * Entrega conexion
     * 
     * @param conf
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public Connection crearConexion(ConfigDTO conf) throws SQLException, Exception {
        ChaordicDAO dao = new ChaordicDAO();
        Connection con = dao.generaConeccion(conf);
        return con;
    }
    
    public List getListaProductosChaordic(ConfigDTO conf, String idLocal) {

        Connection con=null;
        List listaProductos=null;
        
        try {
        	ChaordicDAO dao = new ChaordicDAO();
            listaProductos  = new ArrayList();
        
            con = crearConexion(conf);
        	
            listaProductos = dao.getExtraeProductosChaordic(con,idLocal);
            
        } catch (SQLException ex) {
            logger.error("SQL Error :" + ex);
        } catch (Exception e) {
            logger.error("Error :" + e);
        }finally{ 
        	if(con != null){
        		try{
        			con.close();
        		}catch(Exception e){
        			logger.error("Error al cerrar conexión :" + e.getMessage());
        		}
        	}
        }
        return listaProductos;
    }

    public void getArchivoXML(List listaProductos,ConfigDTO conf) {
        try {
        	ChaordicDAO dao = new ChaordicDAO();

            dao.getExtraeArchivoXML(listaProductos,conf.getRutArchivo());
            
        } catch (Exception e) {
        	logger.error("Error :" + e);
        }
        return ;
    }    
}