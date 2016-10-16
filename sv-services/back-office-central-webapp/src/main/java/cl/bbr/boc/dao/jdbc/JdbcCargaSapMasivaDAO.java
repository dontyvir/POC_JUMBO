package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cl.bbr.boc.dao.CargaSapMasivaDAO;
import cl.bbr.boc.dto.BOCargaSapMasivaDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.jumbocl.common.model.ProductoLogEntity;
import cl.bbr.jumbocl.contenidos.dto.CargaSapMasivaLogDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CargaSapMasivaDAOException;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.jumbo.common.dao.jdbc.JdbcDAO;


/**
 * 
 * @author jolazogu
 *
 */
public class JdbcCargaSapMasivaDAO extends JdbcDAO implements CargaSapMasivaDAO {
	
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);

	JdbcTransaccion trx = null;
	Connection conn = null;
	

	/**
	 * 
	 * @param idGrupo
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteIdGrupo( String idGrupo ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {

			String SQL = "SELECT * FROM BO_CATPROD WHERE ID_CATPROD = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString( 1, idGrupo );
			
			
			rs = stm.executeQuery();	
			
			if ( rs.next() )
				return result = true;
			
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteIdGrupo - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteIdGrupo - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getExisteIdGrupo - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}

	/**
	 * 
	 * @param cargaSapMasiva
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteSkuBOProductoPrecioLocal( BOCargaSapMasivaDTO cargaSapMasiva ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
			
		try {

			String SQL = "SELECT * FROM BO_PRODUCTOS p "+
						 "INNER JOIN BO_PRECIOS pl on pl.COD_PROD1 = p.COD_PROD1 "+
						 "WHERE p.COD_PROD1 = ? and UCASE(p.UNI_MED)=UCASE(?)";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			stm.setString( 1, cargaSapMasiva.getCodigoSap() );
		    stm.setString( 2, cargaSapMasiva.getUnidadMedida() );
			
		    	rs = stm.executeQuery();	
			
		    	if ( rs.next() ) 		
		    		result = true;
		    	
		    
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteSkuBOProductoPrecioLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteSkuBOProductoPrecioLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getExisteSkuBOProductoPrecioLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	public boolean getExisteSkuBOProducto( BOCargaSapMasivaDTO cargaSapMasiva ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
			
		try {

			String SQL = "SELECT * FROM BO_PRODUCTOS WHERE COD_PROD1 = ? and UCASE(UNI_MED)=UCASE(?)";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			stm.setString( 1, cargaSapMasiva.getCodigoSap() );
		    stm.setString( 2, cargaSapMasiva.getUnidadMedida() );
			
		    	rs = stm.executeQuery();	
			
		    	if ( rs.next() ) 		
		    		result = true;
		    	
		    
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteSkuBOProductoPrecioLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteSkuBOProductoPrecioLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getExisteSkuBOProductoPrecioLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param codigoSAP
	 * @param unidadMedida
	 * @return
	 * @throws DAOException
	 */
	public BOProductoDTO getBoProducto( String codigoSAP, String unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		BOProductoDTO producto = null;
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT ID_PRODUCTO, DES_CORTA, UNI_MED, COD_PROD1, DES_LARGA FROM BO_PRODUCTOS WHERE COD_PROD1 = ? AND UCASE(UNI_MED)=UCASE(?) ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setString(1, codigoSAP);
			 stm.setString(2, unidadMedida);
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
					
				 producto = new BOProductoDTO();
				 
				 producto.setId(rs.getInt(1));
				 producto.setDescripcion_corta(rs.getString(2));
				 producto.setUnidad(rs.getString(3));
				 producto.setCodSap(rs.getString(4));
				 producto.setDescripcion_larga(rs.getString(5));
				 
			 }	
			
		} catch ( SQLException ex ) {
			
			logger.error( "getBoProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getBoProducto - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getBoProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return producto;
	
	}
	
	/**
	 * 
	 * @param cargaSapMasiva
	 * @return
	 * @throws DAOException
	 */
	public boolean setInsertarBOProducto( BOCargaSapMasivaDTO cargaSapMasiva ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO BO_PRODUCTOS (ID_CATPROD, UNI_MED, COD_PROD1, DES_CORTA, DES_LARGA, EAN13, ESTADO, FCARGA) " +
					"VALUES (?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
			
			stm = conexion.prepareStatement( SQL ); 
			
	    	stm.setString( 1, cargaSapMasiva.getIdGrupo() );
	    	stm.setString( 2, cargaSapMasiva.getUnidadMedida() );
	    	stm.setString( 3, cargaSapMasiva.getCodigoSap() );
	    	
	    	if (cargaSapMasiva.getDescripcion().length() > 17 )
	    		stm.setString( 4, cargaSapMasiva.getDescripcion().substring(0, 17) );
	    	else
	    		stm.setString( 4, cargaSapMasiva.getDescripcion() );
	    	
	    	stm.setString( 5, cargaSapMasiva.getDescripcion() );
	    	stm.setString( 6, cargaSapMasiva.getCodBarra() );
	    	stm.setInt( 7, 1 );
	    	
	    	int i = stm.executeUpdate();
	    	
	    	if(i>0)
	    		result = true;
		
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarBOProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarBOProducto - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "setInsertarBOProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param cargaSapMasiva
	 * @param idProducto
	 * @return
	 * @throws DAOException
	 */
	public boolean setInsertarBOCodBarra( BOCargaSapMasivaDTO cargaSapMasiva, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO BO_CODBARRA (COD_PROD1, COD_BARRA, TIP_CODBAR, UNID_MED, ID_PRODUCTO, ESTADOACTIVO, COD_BARRA_BIGINT) " +
					"VALUES (?,?,?,?,?,?,?)";
			
			stm = conexion.prepareStatement( SQL ); 
			
	    	stm.setString( 1, cargaSapMasiva.getCodigoSap() );
	    	stm.setString( 2, cargaSapMasiva.getCodBarra() );
	    	stm.setString( 3, "HE" );
	    	stm.setString( 4, cargaSapMasiva.getUnidadMedida() );
	    	stm.setInt( 5, idProducto );
	    	stm.setInt( 6, 1 );
	    	stm.setString( 7, cargaSapMasiva.getCodBarra().substring(0, cargaSapMasiva.getCodBarra().length()-1) );
	    	
	    	int i = stm.executeUpdate();
	    	
	    	if(i>0)
	    		result = true;
		
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarBOCodBarra - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarBOCodBarra - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "setInsertarBOCodBarra - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	
	public boolean getExisteBOCodBarra( BOCargaSapMasivaDTO cargaSapMasiva, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			
			String SQL = "SELECT * FROM BO_CODBARRA WHERE COD_PROD1 = ? AND COD_BARRA = ? AND UNID_MED = ? AND ID_PRODUCTO = ? ";
			
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString( 1, cargaSapMasiva.getCodigoSap() );
	    	stm.setString( 2, cargaSapMasiva.getCodBarra() );
	    	stm.setString( 3, cargaSapMasiva.getUnidadMedida() );
	    	stm.setInt( 4, idProducto );
	    	 		
		    rs = stm.executeQuery();	
						
			if ( rs.next() )				
				result = true;
		
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteBOCodBarra - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteBOCodBarra - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getExisteBOCodBarra - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param cargaSapMasiva
	 * @param idProducto
	 * @param idLocal
	 * @param codLocal
	 * @return
	 * @throws DAOException
	 */
	public boolean setInsertarBOPrecios( BOCargaSapMasivaDTO cargaSapMasiva, int idProducto, int idLocal, String codLocal) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO BO_PRECIOS (ID_LOCAL, ID_PRODUCTO, COD_PROD1, COD_LOCAL, PREC_VALOR, UMEDIDA, COD_BARRA, ESTADOACTIVO, PRECIO_ANTIGUO, PRECIO_NUEVO, ALERTA_CAMBIO, COSTO_PROMEDIO, BLOQ_COMPRA, MIX_LOCAL, FECHA_PRECIO_ANTIGUO, FECHA_PRECIO_NUEVO) " +
					"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_timestamp,current_timestamp)";
			
			stm = conexion.prepareStatement( SQL ); 
			
	    	stm.setInt( 1, idLocal );
	    	stm.setInt( 2, idProducto );
	    	stm.setString( 3, cargaSapMasiva.getCodigoSap() );
	    	stm.setString( 4, codLocal );
	    	stm.setBigDecimal( 5, cargaSapMasiva.getPrecio() );
	    	stm.setString( 6, cargaSapMasiva.getUnidadMedida() );
	    	stm.setString( 7, cargaSapMasiva.getCodBarra().substring(0, cargaSapMasiva.getCodBarra().length() -1 ) );
	    	stm.setInt( 8, 1 );
	    	stm.setBigDecimal(9, cargaSapMasiva.getPrecio() );
	    	stm.setBigDecimal(10, cargaSapMasiva.getPrecio() );
	    	stm.setInt( 11, 1 );
	    	stm.setInt( 12, 0 );
	    	stm.setString( 13, "NO" );
	    	stm.setInt( 14, 1 );
	    	
	    	int i = stm.executeUpdate();
	    	
	    	if(i>0)
	    		result = true;
		
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarBOPrecios - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarBOPrecios - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "setInsertarBOPrecios - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	
	/**
	 * 
	 * @param cargaSapMasiva
	 * @param idProducto
	 * @return
	 * @throws DAOException
	 */
	public boolean setConPrecio( BOCargaSapMasivaDTO cargaSapMasiva, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
				
				String SQL = "UPDATE BO_PRODUCTOS SET CON_PRECIO = ? WHERE ID_PRODUCTO = ? and COD_PROD1 = ?";
				
				stm = conexion.prepareStatement( SQL ); 
				
		    	stm.setString( 1, "S" );
		    	stm.setInt( 2, idProducto );
		    	stm.setString( 3, cargaSapMasiva.getCodigoSap() );
		    	
		    	int i = stm.executeUpdate();
		    	
		    	if(i>0)
		    		result = true;
							
		} catch ( SQLException ex ) {
			
			logger.error( "setConPrecio - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setConPrecio - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "setInserUpdateBOPrecios - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 	
	 * @param codLocal
	 * @return
	 * @throws DAOException
	 */
	public int getIdLocal( String codLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		int idLocal = 0;
			
		try {

			String SQL = "SELECT ID_LOCAL FROM BO_LOCALES WHERE COD_LOCAL = ?";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString( 1, codLocal );
			 		
		    rs = stm.executeQuery();	
						
			if ( rs.next() ) { 					
				
			idLocal = rs.getInt(1);
				
		    }
			
		} catch ( SQLException ex ) {
			
			logger.error( "getIdLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getIdLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getIdLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return idLocal;
	
	}
	
	/**
	 * 
	 * @param log
	 * @return
	 * @throws CargaSapMasivaDAOException
	 */
	public int agregaLogProducto(CargaSapMasivaLogDTO log) throws CargaSapMasivaDAOException {
		
	
		Connection conexion = null;	
		PreparedStatement stm = null;
		ResultSet rs = null;
		int id = -1;
		
		try {

			conexion = conexionUtil.getConexion();
			logger.debug("en agregaLogProducto");
			if(log.getUsuario()==null)
				log.setUsuario("Usuario 1");
			String sql = "INSERT INTO fo_pro_tracking (tra_pro_id, tra_bo_pro_id, tra_fec_crea, tra_usuario, tra_texto ) " +
				" VALUES (?,?, current_timestamp,?,?)";
			logger.debug(sql);
			logger.debug("val:"+log.getCod_prod()+","+log.getCod_prod_bo()+","+log.getFec_crea()+","+log.getUsuario()+","+log.getTexto());
			stm = conexion.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			stm.setLong(1, log.getCod_prod());
			stm.setLong(2, log.getCod_prod_bo());
			//stm.setString(3, log.getFec_crea());
			stm.setString(3, log.getUsuario());
			stm.setString(4, log.getTexto());
			stm.executeUpdate();
			
			rs = stm.getGeneratedKeys();
	        if (rs.next()) {
	            id = rs.getInt(1);
	        }

		} catch (Exception e) {
			logger.debug("Problema :"+ e);
			throw new CargaSapMasivaDAOException(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				
				if (stm != null)
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
				
			} catch (SQLException e) {
				logger.error("[Metodo] : agregaLogProducto - Problema SQL (close)", e);
			}
		}
		
		return id;
		
	}

	/**
	 * 
	 * @param productosCargaSapMasiva
	 * @return
	 * @throws CargaSapMasivaDAOException
	 */
	public List getLogByProductId(List productosCargaSapMasiva) throws CargaSapMasivaDAOException {
	
	
		List lst_log = new ArrayList();
		ProductoLogEntity log = null;
		Connection conexion = null;	
		PreparedStatement stm = null;
		ResultSet rs = null;
	
		try {

			conexion = conexionUtil.getConexion();
			String sql = " SELECT tra_id, tra_pro_id, tra_fec_crea, tra_usuario, tra_texto " +
			" FROM fo_pro_tracking WHERE tra_pro_id = ? and tra_bo_pro_id = ?"  ;
		
			stm = conexion.prepareStatement(sql  + " WITH UR");
		
			Iterator it = productosCargaSapMasiva.iterator();
		
			while ( it.hasNext() ) {

	    		BOCargaSapMasivaDTO cargaSapMasiva = ( BOCargaSapMasivaDTO )it.next();
	    
	    		BOProductoDTO productoBO = getBoProducto(cargaSapMasiva.getCodigoSap(), cargaSapMasiva.getUnidadMedida());
	    	
	    		stm.setLong(1,productoBO.getId());
	    	
	    		stm.setLong(2,Long.parseLong( cargaSapMasiva.getCodigoSap() ) );
			
	    		rs = stm.executeQuery();
			
				while (rs.next()) {
			
					log = new ProductoLogEntity();
					log.setId(new Long(rs.getString("tra_id")));
					log.setCod_prod(new Long(rs.getString("tra_pro_id")));
					log.setFec_crea(rs.getTimestamp("tra_fec_crea"));
					log.setUsuario(rs.getString("tra_usuario"));
					log.setTexto(rs.getString("tra_texto"));
					
					lst_log.add(log);
			
				}
			
	    	}	
		
		} catch (Exception e) {
			logger.debug("Problema:"+ e);
			throw new CargaSapMasivaDAOException(e);
		} finally {
			
			try {
	
				if (rs != null)
					rs.close();
			
				if (stm != null)
					stm.close();
			
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch (SQLException e) {
				logger.error("[Metodo] : getLogByProductId - Problema SQL (close)", e);
			}
	
		}
	
		logger.debug("cant en lista:"+lst_log.size());
		return lst_log;

	}

	/**
	 * 
	 * @param trx
	 * @throws CargaSapMasivaDAOException
	 */
	public void setTrx(JdbcTransaccion trx)
		throws CargaSapMasivaDAOException {
			this.trx = trx;
			try {
				conn = trx.getConnection();
			} catch (DAOException e) {
				throw new CargaSapMasivaDAOException(e);
			}
		}
	
}
