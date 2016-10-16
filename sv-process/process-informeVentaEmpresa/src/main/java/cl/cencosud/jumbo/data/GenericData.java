package cl.cencosud.jumbo.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.log4j.Logger;

import cl.cencosud.jumbo.util.JFactory;

public class GenericData {
	
	Connection conn = null;
	protected static Logger logger = Logger.getLogger(GenericData.class.getName());

	public GenericData(){		
		conn = JFactory.getConnectionDB2();		
	}
	
	public void purge() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Bloque catch generado automáticamente
			logger.error(e);
		}		
	}
	
	public String getfechaBD() {		
	  	
		String fecha="";
		
        try {
			String sql = "SELECT (current date) as fecha FROM SYSIBM.SYSDUMMY1 WITH UR";
			
			if(logger.isDebugEnabled())	logger.debug(" getfechaReporte::SQL: " + sql);
			Object[] array = (Object[]) JFactory.getQueryRunner().query(conn, sql, new ArrayHandler());
			fecha = String.valueOf(array[0]);
			
        } catch (Exception e) {
        	logger.error(GenericData.class.getName()+".getfechaBD :: Error: ", e);
        }
        return fecha;
	}
	
	public ArrayList getEmailUsuarios() {
		
		PreparedStatement stm =null;
		ResultSet rs = null;
		ArrayList emails = null;
		
		try {
			//ID_PARAMETRO-NOMBRE-VALOR
			String Query = "SELECT VALOR FROM BO_PARAMETROS WHERE NOMBRE = 'EMAIL_INFORME_VENTA_EMPRESA' ";
			stm = conn.prepareStatement(Query + " WITH UR");

			rs = stm.executeQuery();
			if(rs.next()){			
				String[] param = (rs.getString("VALOR") != null )? rs.getString("VALOR").split(";"):null;
				if(param != null && param.length > 0){
					emails = new ArrayList();
					for(int i=0; i <param.length; i++){
						try{
							emails.add(param[i]);
						}catch(ArrayIndexOutOfBoundsException e){
							logger.error(GenericData.class.getName()+".getEmailUsuarios :: Error Sin destinatarios email: ", e);
							emails = null;
						}						
					}					
				}
			}

		} catch (SQLException e) {
			logger.error(GenericData.class.getName()+".getEmailUsuarios :: Error: ", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
			} catch (SQLException e) {
				logger.error("[Metodo] : getEmailUsuarios - Problema SQL (close)", e);
			}
		}
		return emails;
	}
}
