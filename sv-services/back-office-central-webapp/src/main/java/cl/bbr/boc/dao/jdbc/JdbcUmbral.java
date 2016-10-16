/*
 * Creado el 11-sep-2012
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */

package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.bbr.boc.dao.DAOFactoryParametros;
import cl.bbr.boc.dao.UmbralDAO;
import cl.bbr.boc.dto.UmbralDTO;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.jumbocl.clientes.exceptions.ClientesDAOException;
import cl.bbr.jumbocl.common.model.ParametroEntity;
import cl.bbr.jumbocl.contenidos.dto.ElementosCriteriaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.ElementoDAOException;
import cl.bbr.jumbocl.pedidos.exceptions.RondasDAOException;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * @author RMI -DNT
 *
 * TODO Para cambiar la plantilla de este comentario generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
public class JdbcUmbral  implements UmbralDAO {
	
	private static ConexionUtil conexionUtil = new ConexionUtil();
	
	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;
	/**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this); 
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return Connection
	 * @throws SQLException 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = DAOFactoryParametros.getConexion();
		}
		return this.conn;

	}
	
	/**
	 * Libera la conexión. Sólo si no es una conexión única, en cuyo caso
	 * no la cierra.
	 * 
	 * 
	 */
	private void releaseConnection(){
		if ( trx == null ){
            try {
            	if (conn != null){
            		conn.close();
            		conn = null;
            	}
            } catch (SQLException e) {
            	logger.error(e.getMessage(), e);

            }
		}
			
	}

	/* (sin Javadoc)
	 * @see cl.bbr.boc.dao.UmbralDAO#getUmbralesAll()
	 */
	public List getUmbralesAll() throws DAOException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.boc.dao.UmbralDAO#getParametrosByCriteria()
	 */
	public List getParametrosByCriteria(ElementosCriteriaDTO criterio) throws DAOException, ElementoDAOException {
		List lst_elem = new ArrayList();
		ParametroEntity elem = null;
		PreparedStatement stm = null; 
		ResultSet rs = null;
		logger.debug("en getParametrosByCriteria:");
		//variable del criterio de búsqueda
		//nombre de elemento
		String nom_elem = criterio.getNombre();
		String sqlNomElem = "";
		if (!nom_elem.equals(""))
		sqlNomElem = " AND upper(LOC.NOM_LOCAL) like \'%"+nom_elem.toUpperCase()+"%\'";
		//numero de elemento
		String num_elem = criterio.getNumero();
		String sqlNumElem = "";
		if (!num_elem.equals(""))
		sqlNumElem = " AND LOC.ID_LOCAL = "+num_elem;
		//variables de paginacion
		int pag = criterio.getPag();
		int regXpag = criterio.getRegsperpage();
		if(pag<0) pag = 1;
		if(regXpag<10) regXpag = 10;
		int iniReg = (pag-1)*regXpag + 1;
		int finReg = pag*regXpag;
		
		double cero = 0.00;
		try {

		/*	String sql = " SELECT BO_UMBRALES.*, BO_LOCALES.NOM_LOCAL FROM BO_UMBRALES, " +
				        " BO_LOCALES WHERE BO_UMBRALES.ID_LOCAL > 0 AND BO_UMBRALES.ID_LOCAL = BO_LOCALES.ID_LOCAL" +	sqlNomElem + sqlNumElem ;*/
			
			
			 String sql = " SELECT LOC.ID_LOCAL, LOC.NOM_LOCAL, UMB.FECHA_MODI,UMB.U_UNIDAD, U_MONTO,UMB.LOGIN, UMB.U_ACTIVACION  "+
				" FROM BO_LOCALES LOC LEFT OUTER JOIN BO_UMBRALES UMB "+
				" ON LOC.ID_LOCAL =UMB.ID_LOCAL "+
				" WHERE LOC.ID_LOCAL >0 "+	sqlNomElem + sqlNumElem ;
							
			logger.debug("Sql para ejecutar e : "+sql);
			conn = this.getConnection();
			stm =conn.prepareStatement(sql  + " WITH UR");
			rs = stm.executeQuery();
			while (rs.next()) {
				elem = new ParametroEntity();
				elem.setId_local(rs.getInt("id_local"));
				elem.setNom_local(rs.getString("nom_local"));
				//elem.setU_monto(rs.getDouble("u_monto"));
				if(rs.getString("u_monto")!=null){
					elem.setU_monto(rs.getDouble("u_monto"));
				}else{
					elem.setU_monto(cero);
				}
				
			//	elem.setU_unidad(rs.getDouble("u_unidad"));
				if(rs.getString("u_unidad")!=null){
					elem.setU_unidad(rs.getDouble("u_unidad"));
				}else{
					elem.setU_unidad(cero);
				}
				
				//elem.setU_activacion(rs.getString("U_activacion"));
				if(rs.getString("U_activacion")!=null){
					elem.setU_activacion(rs.getString("U_activacion"));
				}else{
					elem.setU_activacion("N");
				}
				if(rs.getDate("fecha_modi")!=null){
					elem.setFecha_modi(rs.getDate("fecha_modi"));
				}
								
				logger.debug("----- Resultado de la Query ----");
				logger.debug("Id del local : " + elem.getId_local());
				logger.debug("Nombre  del local : " + elem.getNom_local());
				logger.debug("Monto del Umbral : " + elem.getU_monto());
				logger.debug("Unidad del umbral : " + elem.getU_unidad());
				logger.debug("Umbral Activacion : " + elem.getU_activacion());
				logger.debug("----- Fin Resultado de la Query ----");
							
				lst_elem.add(elem);
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getElementosByCriteria - Problema SQL (close)", e);
			}
		}
		logger.debug("cantidad  en lista:"+lst_elem.size());
		return lst_elem;
	}


	/* (sin Javadoc)
	 * @see cl.bbr.boc.dao.UmbralDAO#getParametrosByCriteria()
	 */
	public List getParametrosByCriteria() throws DAOException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}

	

	/**
	 * Obtiene el elemento, segun el id
	 * 
	 * @param  id_elemento
	 * @return ElementoEntity
	 * @throws ElementoDAOException
	 */
	public ParametroEntity getElementoById(long id_elemento) throws ElementoDAOException{
		
		ParametroEntity elem = null;
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		logger.debug("en getElementoById:");

		try {
			String sql = " SELECT id_local, fecha_modi, u_unidad, u_monto, id_usuario, id_activacion " +
				" FROM bo_umbrales " +
				" WHERE id_local = ? ";
			logger.debug("sql:"+sql);
			conn = this.getConnection();
			stm = conn.prepareStatement(sql  + " WITH UR");
			stm.setLong(1,id_elemento);
			rs = stm.executeQuery();
			if (rs.next()) {
				elem = new ParametroEntity(); 
				elem.setId_local(rs.getInt("id_local"));
				elem.setU_monto(rs.getInt("u_monto"));
				elem.setU_unidad(rs.getInt("u_unidad"));
			//	elem.setU_activacion(rs.getString("u_activacion"));
			//	JOptionPane.showMessageDialog(null,elem.getU_activacion());
				
				if(rs.getString("u_activacion")!=null){
					elem.setU_activacion(rs.getString("u_activacion"));
				}else{
					elem.setU_activacion("N");
				}
				
				if(rs.getDate("fecha_modi")!=null){
					elem.setFecha_modi(rs.getDate("fecha_modi"));
				}/*else{
					elem.setFecha_modi("");
				}*/
				
				if(rs.getString("id_usuario")!=null){
					elem.setId_usuario(rs.getString("id_usuario"));
				}else{
					elem.setId_usuario("");
				}
				
			}

		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new ElementoDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getElementoById - Problema SQL (close)", e);
			}
		}
		return elem;
	}

	public UmbralDTO getPorcenUmbralById(long id_pedido) throws BocException {
        UmbralDTO umr = new UmbralDTO();
		
		PreparedStatement stm = null;
		ResultSet rs = null;
		String sql = "SELECT  H.PORC_MONTO , H.PORC_UNIDAD from BO_PEDIDOS_PORCENT H, BO_PEDIDOS P where H.ID_PEDIDO = P.ID_PEDIDO AND H.ID_PEDIDO = ?";

		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_pedido); 
			rs = stm.executeQuery();
			if (rs.next()) {
			
				umr.setU_monto(rs.getDouble("PORC_MONTO"));
				umr.setU_unidad(rs.getDouble("PORC_UNIDAD"));
			}

			
 		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new BocException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getPorcenUmbralById - Problema SQL (close)", e);
			}
		
		}
		
		return umr;
		
	}



		
	
	
	

	/* (sin Javadoc)
	 * @see cl.bbr.boc.dao.UmbralDAO#getUmbralByIdLocal(int)
	 */
	public UmbralDTO getUmbralByIdLocal(int id_local) throws ClientesDAOException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}
	

	
	 public void  updateUmbral(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws  Exception {	
	 	
	 	logger.debug("Comienzo de insertarUmbrales en  : "+ getClass()+"");
	 	logger.debug("Paramtros de entrada: ----");
	 	logger.debug("Id local : " + id_local);
	 	logger.debug("Fecha Modficacion : " + fecha_modi);
	 	logger.debug("Nombre Usuario : " + nom_usuario);
	 	logger.debug("Umbral Monto : " +u_monto);
	 	logger.debug("Umbral Unidad : " +u_unidad);
		logger.debug("Umbral Activacion : " +u_activacion);
		
		PreparedStatement stm 	= null;
		boolean result = false;
		
		String sql = " UPDATE BODBA.BO_UMBRALES " +
					 " SET U_UNIDAD = "+ u_unidad  +
					 " , U_MONTO =  " + u_monto +
					 " , U_ACTIVACION =  '"  + u_activacion + "' " +
					 " , FECHA_MODI = current date" +
					 " , LOGIN =  '" + nom_usuario +
					 "' WHERE ID_LOCAL =  " +id_local ;
		
		
		logger.debug("* SQL updateUmbrales : "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

			
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insertarUmbrales - Problema SQL (close)", e);
			}
		}
	}

	 
	 public void insertarUmbrales(int id_local,String fecha_modi, String nom_usuario, double u_unidad,double u_monto,String u_activacion) throws  Exception {	
	 	
	 	logger.debug("Comienzo de insertarUmbrales en  : "+ getClass()+"");
	 	logger.debug("Paramtros de entrada: ----");
	 	logger.debug("Id local : " + id_local);
	 	logger.debug("Fecha Modficacion : " + fecha_modi);
	 	logger.debug("Nombre Usuario : " + nom_usuario);
	 	logger.debug("Umbral Monto : " +u_monto);
	 	logger.debug("Umbral Unidad : " +u_unidad);
		logger.debug("Umbral Activacion : " +u_activacion);
		
		PreparedStatement stm 	= null;
		boolean result = false;
		
		String sql = "INSERT INTO BO_UMBRALES (ID_LOCAL,FECHA_MODI,U_UNIDAD,U_MONTO,LOGIN,U_ACTIVACION) VALUES ("+
					 id_local + " , CURRENT DATE  , " + u_unidad + " , " + u_monto + " , '"+ nom_usuario + "' , '" + u_activacion + "' )"; 
			
								
		
		logger.debug("* SQL insertarUmbrales : "+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			int i = stm.executeUpdate();
			if(i>0)
				result = true;

			
		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new ClientesDAOException(e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insertarUmbrales - Problema SQL (close)", e);
}
		}
	}

	

	 public long consultaIdLocal(long  id_local) throws RondasDAOException {
		logger.debug("en consultaIdLocal");
		long id_local_rec = 0L;
		PreparedStatement stm = null;
		ResultSet rs = null;
	
		try {
	
			String sql = "SELECT ID_LOCAL FROM BODBA.BO_UMBRALES WHERE ID_LOCAL = ? ";
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setLong(1, id_local);

			logger.debug("SQL: " + sql);
			rs = stm.executeQuery();
			if (rs.next()) {
				id_local_rec = rs.getLong("ID_LOCAL");
			}

		} catch (SQLException e) {
			throw new RondasDAOException(String.valueOf(e.getErrorCode()), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();

			} catch (SQLException e) {
				logger.error("[Metodo] : consultaIdLocal - Problema SQL (close)", e);
			} finally {
				rs = null;
				stm = null;

			}
		}
		logger.debug("id_local: " + id_local_rec);
		return id_local_rec;
	}

	 
	 
	 
}

	

	
	

