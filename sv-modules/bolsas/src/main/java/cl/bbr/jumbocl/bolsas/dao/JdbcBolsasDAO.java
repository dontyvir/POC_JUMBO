package cl.bbr.jumbocl.bolsas.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cl.bbr.jumbocl.bolsas.dto.BitacoraDTO;
import cl.bbr.jumbocl.bolsas.dto.BolsaDTO;
import cl.bbr.jumbocl.bolsas.exceptions.BolsasDAOException;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;

/**
 * Clase que permite consultar los Casos que se encuentran en la base de datos.
 * @author imoyano
 *
 */
public class JdbcBolsasDAO implements BolsasDAO {
	
	/**
	 * Permite generar los eventos en un archivo log.
	 */
	Logging logger = new Logging(this);

	/**
	 * Permite la conexión con la base de datos.
	 */
	Connection 	conn 		= null;

	/**
	 * Permite el manejo de la transaccionalidad, para procesos de multiples operaciones en la base de datos
	 */
	JdbcTransaccion trx		= null;
	
	// ************ Métodos Privados *************** //
	
	/**
	 * Obtiene la conexión
	 * 
	 * @return Connection
	 * @throws Exception 
	 */
	private Connection getConnection() throws SQLException{
		
		if ( conn == null ){
			conn = JdbcDAOFactory.getConexion();
		}
		
		logger.debug("Conexion usada por el dao: " + conn);
		
		return this.conn;

	}

	
	// ************ Métodos Publicos *************** //
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
	// ******************************************** //	
	
	/**
	 * Setea una transacción al dao y le asigna su conexión
	 * 
	 * @param  trx JdbcTransaccion 
	 * 
	 * @throws PedidosDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
			throws BolsasDAOException {
		this.trx = trx;
		try {
			conn = trx.getConnection();
		} catch (DAOException e) {
			throw new BolsasDAOException(e);
		}
	}
	
	/**
	 * Constructor
	 *
	 */
	public JdbcBolsasDAO(){
		
	}

    /**
     * @return
     */
    public List getStockBolsasRegalo(String cod_sucursal) throws BolsasDAOException {
	    
		List bolsas = new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " select b.act_bolsa, b.cod_bolsa, b.desc_bolsa, b.cod_barra_bolsa, b.id_producto, " +
					 " coalesce((select s.stock from bo_stock_bolsas s " +
					 " where s.cod_bolsa = b.cod_bolsa and s.cod_sucursal = ? ), 0) as stock " +
					 " from BODBA.bo_bolsas b, BODBA.fo_productos c where b.id_producto = c.pro_id order by cod_bolsa";
		
		logger.debug("* SQL "+cod_sucursal+" getStockBolsasRegalo :"+sql + " WITH UR ");
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setString(1, cod_sucursal);
			
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    BolsaDTO bolsa = new BolsaDTO();
			    
			    bolsa.setCod_bolsa(rs.getString("cod_bolsa"));
			    bolsa.setDesc_bolsa(rs.getString("desc_bolsa"));
			    bolsa.setAct(rs.getString("act_bolsa"));
			    bolsa.setCod_barra_bolsa(rs.getString("cod_barra_bolsa"));
			    bolsa.setStock(rs.getInt("stock"));
			    bolsa.setId_producto(rs.getInt("id_producto"));
			    
			    bolsas.add(bolsa);
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getStockBolsasRegalo - Problema SQL (close)", e);
			}
		}
		return bolsas;
    }
    
    
    
    public List getBolsasRegalo() throws BolsasDAOException {
	    
		List bolsas = new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " select a.cod_bolsa, a.desc_bolsa, a.cod_barra_bolsa, a.id_producto, b.PRO_COD_SAP from BODBA.bo_bolsas a, BODBA.fo_productos b where a.id_producto = b.pro_id order by cod_bolsa ";
		
		logger.debug("* SQL getBolsasRegalo :"+sql + " WITH UR ");
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    BolsaDTO bolsa = new BolsaDTO();
			    
			    bolsa.setCod_bolsa(rs.getString("cod_bolsa"));
			    bolsa.setDesc_bolsa(rs.getString("desc_bolsa"));
			    bolsa.setCod_barra_bolsa(rs.getString("cod_barra_bolsa"));
			    bolsa.setId_producto(rs.getInt("id_producto"));
			    bolsa.setCod_sap(rs.getString("PRO_COD_SAP"));
			    
			    bolsas.add(bolsa);
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getBolsasRegalo - Problema SQL (close)", e);
			}
		}
		return bolsas;
    }
    
    
    public List getBitacoraBolsasRegalo(String cod_sucursal) throws BolsasDAOException {
	    
		List bitacora = new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * FROM BODBA.BO_LOG_BOLSAS " +
					 //" WHERE COD_SUCURSAL = ?" +
					 " ORDER BY FECHA_OPERACION DESC, HORA_OPERACION DESC ";
		
		logger.debug("* SQL getBitacoraBolsasRegalo :"+sql+" cod_sucursal : "+cod_sucursal);
		
		try{

			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			
			while (rs.next()) {
			    BitacoraDTO operacion = new BitacoraDTO();
			    operacion.setCod_operacion(rs.getString("cod_operacion"));
			    operacion.setDesc_operacion(rs.getString("desc_operacion"));
			    operacion.setFecha_operacion(rs.getString("fecha_operacion"));
			    operacion.setUsuario_operacion(rs.getString("usuario_operacion"));
			    operacion.setHora_operacion(rs.getString("hora_operacion"));
			    
			    bitacora.add(operacion);
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getBitacoraBolsasRegalo - Problema SQL (close)", e);
			}
		}
		return bitacora;
    }
    
    
    public boolean existeStockBolsaSucursal(String id_sucursal, String cod_bolsa) throws BolsasDAOException {
	    
		boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.BO_STOCK_BOLSAS WHERE COD_BOLSA = ? AND COD_SUCURSAL = ? ";
		
		logger.debug("* SQL existeStockBolsaSucursal :"+sql);
		
		try{
			//prepare
			//con = JdbcDAOFactory.getConexion();
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, cod_bolsa );
			stm.setString(2, id_sucursal );
			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				flag = true;
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : existeStockBolsaSucursal - Problema SQL (close)", e);
			}
		}
		return flag;
    }


    /**
     * @param m
     */
    public void actualizarStockBolsa(String cod_bolsa, String cod_sucursal, int stock) throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		String sql = new String();
		
		String sql1 = " UPDATE  BODBA.BO_STOCK_BOLSAS " +
					 " SET STOCK = ? " +
					 " WHERE COD_BOLSA = ? " +
					 " AND COD_SUCURSAL = ? ";
					 
		
		String sql2 = " INSERT INTO BODBA.BO_STOCK_BOLSAS VALUES (  ?, ?, ? ) ";
		
		if(existeStockBolsaSucursal(cod_sucursal, cod_bolsa)){
			sql = sql1;
		}else{
			sql = sql2;
			
		}
		
		logger.debug("* SQL actualizarStockBolsa :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			stm.setInt(1, stock );
			stm.setString(2, cod_bolsa );
			if( cod_sucursal != null && !cod_sucursal.equals("")){
				stm.setString(3, cod_sucursal );
			}
			
			stm.executeUpdate();
            
		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}
    }
    
    
    
    public void insertarRegistroBitacoraBolsas(String desc_operacion, String usuario, String cod_sucursal) throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		
		String sql = " insert into BODBA.bo_log_bolsas values " +
					 " ( char(NEXT VALUE FOR SEQ_BOC_LOG_BOLSAS), " +
					 " ?, " +
					 " ?, " +
					 " ?, " +
					 " current time," +
					 " ? )";
		
		logger.debug("* SQL insertarRegistroBitacoraBolsas :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1, desc_operacion );
			
			java.util.Date fecha = new java.util.Date();
			stm.setObject(2, new java.sql.Date(fecha.getTime()));
			
			stm.setString(3, usuario );
			stm.setString(4, cod_sucursal );
			stm.executeUpdate();

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : insertarRegistroBitacoraBolsas - Problema SQL (close)", e);
			}
		}

    }
    
    
    public void asignacionBolsaCliente(String rut_cliente, String cod_bolsa) throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		String sql = new String();
		boolean flagExisteLista = false;
		
		String sql1 = " insert into BODBA.bo_asignacion_bolsas values ( ?, ? ) ";
		String sql2 = " update BODBA.bo_asignacion_bolsas set cod_bolsa = ? where rut_cliente = ? ";
		
		flagExisteLista = existeAsignacionCliente(rut_cliente);
		if(flagExisteLista){
			sql = sql2;
		}else{
			sql = sql1;
		}
		
		logger.debug("* SQL asignacionBolsaCliente :"+sql);
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			
			if(flagExisteLista){
				stm.setString(1, cod_bolsa );
				stm.setString(2, rut_cliente );
			}else{
				stm.setString(1, rut_cliente );
				stm.setString(2, cod_bolsa );
			}
			
			stm.executeUpdate();

		}catch (SQLException sqle) {
			logger.error("SQL1 EXCEPTION ="+sqle);
			sqle.printStackTrace();
			throw new BolsasDAOException(String.valueOf(sqle.getErrorCode()),sqle);
		}catch(Exception e){
			logger.error("SQL2 EXCEPTION ="+e);
			e.printStackTrace();
			throw new BolsasDAOException(String.valueOf(e.getCause()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : asignacionBolsaCliente - Problema SQL (close)", e);
			}
		}

    }
    
    
    public boolean existeAsignacionCliente(String rut_cliente) throws BolsasDAOException {
	    
		boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.BO_ASIGNACION_BOLSAS WHERE RUT_CLIENTE = ? ";
		
		logger.debug("* SQL existeAsignacionCliente :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, rut_cliente );
			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				flag = true;
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : existeAsignacionCliente - Problema SQL (close)", e);
			}
		}

		return flag;
    }
    
    
    public void crearBolsaRegalo(BolsaDTO bolsa) throws BolsasDAOException {
        PreparedStatement stm 	= null;
		
		String sql = " insert into BODBA.bo_bolsas values ( ?, ?, ?, ? , ?) ";
		
		logger.debug("* SQL crearBolsaRegalo :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);

			stm.setString(1, bolsa.getCod_bolsa() );
			stm.setString(2, bolsa.getDesc_bolsa() );
			stm.setString(3, bolsa.getCod_barra_bolsa() );
			stm.setInt(4, bolsa.getId_producto() );
			stm.setString(5, "S");
			
			stm.executeUpdate();
			
		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {

				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : crearBolsaRegalo - Problema SQL (close)", e);
			}
		}

    }
    

    public void activaBolsa() throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		PreparedStatement stm2 	= null;
		
		String sql = "SELECT distinct act_bolsa FROM BODBA.BO_BOLSAS ";
		String updSq1 = "update BODBA.BO_BOLSAS set act_bolsa = 'N'";
		String updSq2 = "update BODBA.BO_BOLSAS set act_bolsa = 'S'";
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			rs = stm.executeQuery();
			
			rs.next();
			if(rs.getString("act_bolsa").equals("S")){
				stm2 = conn.prepareStatement(updSq1);
				stm2.executeUpdate();
			}else{
				stm2 = conn.prepareStatement(updSq2);
				stm2.executeUpdate();
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (stm2 != null)
					stm2.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : activaBolsa - Problema SQL (close)", e);
			}
		}

    }
    
    public void eliminarBolsaRegalo(BolsaDTO bolsa) throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		PreparedStatement stm2 	= null;
		
		String sql  = " delete from BODBA.bo_bolsas where cod_bolsa = ? ";
		String sql2 = " delete from BODBA.bo_stock_bolsas where cod_bolsa = ? ";
		
		logger.debug("* SQL eliminarBolsaRegalo :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm2 = conn.prepareStatement(sql2);

			stm.setString(1, bolsa.getCod_bolsa() );
			stm2.setString(1, bolsa.getCod_bolsa() );
			
			stm2.executeUpdate();
			stm.executeUpdate();

		}catch (SQLException e) {
			e.printStackTrace();
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm2 != null)
					stm2.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : eliminarBolsaRegalo - Problema SQL (close)", e);
			}
		}

    }
    
    public BolsaDTO getBolsaRegalo(String cod_bolsa) throws BolsasDAOException {
    	BolsaDTO bolsa = new BolsaDTO();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " select * from BODBA.bo_bolsas where cod_bolsa = ? ";
		
		logger.debug("* SQL getBolsaRegalo :"+sql + " WITH UR ");
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			stm.setString(1, cod_bolsa );
			
			rs = stm.executeQuery();
			
			while(rs.next()){
				bolsa.setCod_bolsa(rs.getString(1));
				bolsa.setDesc_bolsa(rs.getString(2));
				bolsa.setCod_barra_bolsa(rs.getString(3));
				bolsa.setId_producto(rs.getInt(4));
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}

		
		return bolsa;
    }
    
    
    public List getAsignacionesBolsas() throws BolsasDAOException {
	    
		List asignaciones = new ArrayList();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.BO_ASIGNACION_BOLSAS ";
		
		logger.debug("* SQL getAsignacionesBolsas :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			rs = stm.executeQuery();
			
			while (rs.next()) {
				Hashtable hAsign = new Hashtable();
				hAsign.put("rut_cliente", rs.getString("RUT_CLIENTE"));
				hAsign.put("cod_bolsa", rs.getString("COD_BOLSA"));
			    
			    asignaciones.add(hAsign);
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getAsignacionesBolsas - Problema SQL (close)", e);
			}
		}

		return asignaciones;
    }
    
    
    public Hashtable getAsignacionBolsaCliente(String rut_cliente) throws BolsasDAOException {
	    
    	Hashtable hAsign = new Hashtable();
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.BO_ASIGNACION_BOLSAS " +
					 " WHERE RUT_CLIENTE = ? ";
		
		logger.debug("* SQL getAsignacionBolsaCliente :"+sql);
		
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");	
			
			stm.setString(1, rut_cliente);
			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				hAsign.put("rut_cliente", rs.getString("RUT_CLIENTE"));
				hAsign.put("cod_bolsa", rs.getString("COD_BOLSA"));
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : xxx - Problema SQL (close)", e);
			}
		}

		return hAsign;
    }
    
    
    public void actualizarBolsa(BolsaDTO bolsa) throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		PreparedStatement stm2 	= null;
		
		String sq1 = " UPDATE  BODBA.BO_BOLSAS " +
					 " SET DESC_BOLSA = ? " +
					 " , COD_BARRA_BOLSA = ? " +
					 " WHERE COD_BOLSA = ? ";
		
		String sq2 = " UPDATE  FODBA.FO_PRODUCTOS " +
		 			 " SET PRO_DES_CORTA = ? " +
		             " , PRO_DES_LARGA = ? " +
					 " WHERE PRO_ID in ( select id_producto from BODBA.BO_BOLSAS where COD_BOLSA = ? ) ";
					 
		logger.debug("* SQL actualizarBolsa :"+sq1+" "+sq2);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sq1);	
			stm.setString(1, bolsa.getDesc_bolsa() );
			stm.setString(2, bolsa.getCod_barra_bolsa() );
			stm.setString(3, bolsa.getCod_bolsa() );		
			stm.executeUpdate();

			stm2 = conn.prepareStatement(sq2);			
			stm2.setString(1, bolsa.getDesc_bolsa() );
			stm2.setString(2, bolsa.getDesc_bolsa() );	
			stm2.setString(3, bolsa.getCod_bolsa() );
			stm2.executeUpdate();

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : actualizarBolsa - Problema SQL (close)", e);
			}
		}

    }
	
	
    
    public void asignarBolsaCliente(String rut_cliente, String id_bolsa) throws BolsasDAOException {	    
		PreparedStatement stm 	= null;
		String sql = " INSERT INTO BODBA.BO_ASIGNACION_BOLSAS VALUES ( ?, ? ) ";
		logger.debug("* SQL asignarBolsaCliente :"+sql);
		
		try {
			conn = this.getConnection();
			stm = conn.prepareStatement(sql);
			stm.setString(1, rut_cliente );
			stm.setString(2, id_bolsa );
			stm.executeUpdate();
		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : asignarBolsaCliente - Problema SQL (close)", e);
			}
		}

    }
    
    
    public boolean existeEnListaBase(String rut_cliente) throws BolsasDAOException {
    	boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.BO_ASIGNACION_BOLSAS WHERE RUT_CLIENTE = ? ";
		logger.debug("* SQL existeEnListaBase :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, rut_cliente );
			
			rs = stm.executeQuery();
			while (rs.next()) {
				flag = true;
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : existeEnListaBase - Problema SQL (close)", e);
			}
		}

		return flag;
    }
    
    
    public boolean estaActivoServicioBolsas() throws BolsasDAOException {
    	boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT distinct act_bolsa  " +
					 " FROM BODBA.BO_BOLSAS ";
		logger.debug("* SQL existeEnListaBase :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			
			rs = stm.executeQuery();
			while (rs.next()) {
				if(rs.getString("act_bolsa").equals("S"))
				  flag = true;
				else 
				  flag = false;
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : estaActivoServicioBolsas - Problema SQL (close)", e);
			}
		}

		return flag;
    }    
    
    public boolean validaCodSap(String cod_sap) throws BolsasDAOException {
    	boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * " +
					 " FROM BODBA.BO_PRODUCTOS WHERE COD_PROD1 = ? ";
		
		
		logger.debug("* SQL validaCodSap :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, cod_sap );
			
			rs = stm.executeQuery();
			while (rs.next()) {
				flag = true;
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : validaCodSap - Problema SQL (close)", e);
			}
		}

		return flag;
    }
    
    public boolean validaCodBolsa(String cod_bolsa) throws BolsasDAOException {
    	boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT * FROM BODBA.BO_BOLSAS WHERE COD_BOLSA = ? ";

		logger.debug("* SQL validaCodBolsa :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, cod_bolsa );
			
			rs = stm.executeQuery();
			while (rs.next()) {
				flag = true;
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : validaCodBolsa - Problema SQL (close)", e);
			}
		}
		return flag;
    }

    public boolean validaCodBolsaSap(String cod_bolsa, String cod_sap) throws BolsasDAOException {
    	boolean flag = false;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
	
		String sql = " SELECT * FROM FODBA.FO_PRODUCTOS  Where pro_cod_sap = ? ";

		logger.debug("* SQL validaCodBolsaSap ["+cod_sap+"] :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, cod_sap);
			
			rs = stm.executeQuery();
			while (rs.next()) {

				flag = true;
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : validaCodBolsaSap - Problema SQL (close)", e);
			}
		}
		return flag;
    }
    
    
    public long getIdProdBO(String cod_sap) throws BolsasDAOException {
    	long id_producto = 0;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT ID_PRODUCTO " +
					 " FROM BODBA.BO_PRODUCTOS WHERE COD_PROD1 = ? ";

		logger.debug("* SQL getIdProdBO :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, cod_sap );
			
			rs = stm.executeQuery();
			while (rs.next()) {
				id_producto = Long.parseLong(rs.getString("ID_PRODUCTO"));
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdProdBO - Problema SQL (close)", e);
			}
		}
		return id_producto;
    }
    
    public long getIdProdFO(String cod_sap) throws BolsasDAOException {
    	long id_producto = 0;
		PreparedStatement stm 	= null;
		ResultSet rs 			= null;
		
		String sql = " SELECT PRO_ID " +
					 " FROM BODBA.FO_PRODUCTOS WHERE PRO_COD_SAP = ? ";

		logger.debug("* SQL getIdProdFO :"+sql);
		try{
			conn = this.getConnection();
			stm = conn.prepareStatement(sql + " WITH UR");
			stm.setString(1, cod_sap );
			
			rs = stm.executeQuery();
			while (rs.next()) {
				id_producto = Long.parseLong(rs.getString("PRO_ID"));
			}

		}catch (SQLException e) {
			throw new BolsasDAOException(String.valueOf(e.getErrorCode()),e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				releaseConnection();
			} catch (SQLException e) {
				logger.error("[Metodo] : getIdProdFO - Problema SQL (close)", e);
			}
		}
		return id_producto;
    }
    
    
}
