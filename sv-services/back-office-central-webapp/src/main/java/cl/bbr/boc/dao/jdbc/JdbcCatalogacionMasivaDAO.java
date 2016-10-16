package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.omg.CORBA.UNKNOWN;

import cl.bbr.boc.command.PreciosEdit;
import cl.bbr.boc.dao.CatalogacionMasivaDAO;
import cl.bbr.boc.dao.StockOnLineDAO;
import cl.bbr.boc.dto.BOCatalogacionMasivaDTO;
import cl.bbr.boc.dto.BODetalleSemiautomaticoDTO;
import cl.bbr.boc.dto.BOPrecioDTO;
import cl.bbr.boc.dto.BOPreciosLocalesDTO;
import cl.bbr.boc.dto.BOProductoDTO;
import cl.bbr.boc.dto.BOStockONLineDTO;
import cl.bbr.boc.dto.DetallePedidoDTO;
import cl.bbr.boc.dto.FOProductoDTO;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.boc.utils.Constantes;
import cl.bbr.jumbocl.common.model.PrecioLocalEntity;
import cl.bbr.jumbocl.common.model.PrecioSapEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.common.model.ProductoLogEntity;
import cl.bbr.jumbocl.contenidos.dto.CatalogacionMasivaLogDTO;
import cl.bbr.jumbocl.contenidos.dto.FichaProductoDTO;
import cl.bbr.jumbocl.contenidos.dto.MarcasDTO;
import cl.bbr.jumbocl.contenidos.dto.PreciosSapDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductoLogDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.contenidos.dto.UnidadMedidaDTO;
import cl.bbr.jumbocl.contenidos.exceptions.CatalogacionMasivaDAOException;
import cl.bbr.jumbocl.pedidos.dto.BarraAuditoriaSustitucionDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;
import cl.jumbo.common.dao.jdbc.JdbcDAO;


/**
 * 
 * @author jolazogu
 *
 */
public class JdbcCatalogacionMasivaDAO extends JdbcDAO implements CatalogacionMasivaDAO {
	
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);

	JdbcTransaccion trx = null;
	Connection conn = null;
	

	/**
	 * 
	 * @param marca
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteMarcaProducto( String marca ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean isMarca = false;
		
		try {

			String SQL = "SELECT * FROM FODBA.FO_MARCAS WHERE UCASE(MAR_NOMBRE)=UCASE(?) AND MAR_ESTADO = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString( 1, marca );
			stm.setString( 2, "A" );
			
			rs = stm.executeQuery();	
			
			if ( rs.next() ) {				
				
				return isMarca = true;
			
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteMarcaProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteMarcaProducto - Problema General", ex );
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
				
				logger.error( "getExisteMarcaProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return isMarca;
	
	}

	/**
	 * 
	 * @param unidadMedidaPPM
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteUnidadMedidaPPM( String unidadMedidaPPM ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean isUnidadMedidaPPM = false;
			
		try {

			String SQL = "SELECT * FROM FODBA.FO_UNIDADES_MEDIDA WHERE UCASE(UNI_DESC)=UCASE(?) AND UNI_ESTADO = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString( 1, unidadMedidaPPM );
			stm.setString( 2, "A" );
			
			rs = stm.executeQuery();	
			
			if ( rs.next() ) {				
				
				return isUnidadMedidaPPM = true;
			
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteUnidadMedidaPPM - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteUnidadMedidaPPM - Problema General", ex );
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
				
				logger.error( "getExisteUnidadMedidaPPM - Problema SQL (close)", e );
			
			}
		
		}
		
		return isUnidadMedidaPPM;
	
	}
	
	/**
	 * 
	 * @param sectorPicking
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteProductoSector( String sectorPicking, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
			
		try {

			String SQL = "SELECT * FROM BO_PROD_SECTOR WHERE ID_SECTOR = ? AND ID_PRODUCTO = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setInt( 1, Integer.parseInt( sectorPicking ) );
			stm.setInt( 2, idProducto );
			
			rs = stm.executeQuery();	
			
			if ( rs.next() )
				return result = true;
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteProductoSector - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteProductoSector - Problema General", ex );
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
				
				logger.error( "getExisteProductoSector - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	
	public boolean getExisteSectorPicking( String sectorPicking ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
			
		try {

			String SQL = "SELECT * FROM BO_SECTOR WHERE ID_SECTOR = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setInt( 1, Integer.parseInt( sectorPicking ) );
			
			rs = stm.executeQuery();	
			
			if ( rs.next() )
				return result = true;
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteSectorPicking - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteSectorPicking - Problema General", ex );
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
				
				logger.error( "getExisteSectorPicking - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}

	/**
	 * 
	 * @param listProductosXls
	 * @return
	 * @throws DAOException
	 */
	public boolean getProEstadoProducto( BOCatalogacionMasivaDTO catalogacionMasiva ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean proEstadoIsA = false;
			
		try {

			String SQL = "SELECT PRO_ESTADO FROM FO_PRODUCTOS WHERE PRO_COD_SAP = ? and UCASE(PRO_TIPRE)=UCASE(?) ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			stm.setString( 1, catalogacionMasiva.getCodigoSap() );
		    stm.setString( 2, catalogacionMasiva.getUnidadMedida() );
			
		    	rs = stm.executeQuery();	
			
		    	if ( rs.next() ) {				
				
		    		if(rs.getString(1).equals("A") || rs.getString(1).equals("D") || rs.getString(1).equals("N"))
		    			return proEstadoIsA = true;
		    			
		    	}
		    
		} catch ( SQLException ex ) {
			
			logger.error( "getProEstadoIsA - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getProEstadoIsA - Problema General", ex );
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
				
				logger.error( "getProEstadoIsA - Problema SQL (close)", e );
			
			}
		
		}
		
		return proEstadoIsA;
	
	}
	
	
	public boolean getExisteSkuFOProducto( BOCatalogacionMasivaDTO catalogacionMasiva ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean proEstadoIsA = false;
			
		try {

			String SQL = "SELECT * FROM FO_PRODUCTOS WHERE PRO_COD_SAP = ? and UCASE(PRO_TIPRE)=UCASE(?)";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			stm.setString( 1, catalogacionMasiva.getCodigoSap() );
		    stm.setString( 2, catalogacionMasiva.getUnidadMedida() );
			
		    	rs = stm.executeQuery();	
			
		    	if ( rs.next() ) 		
		    		proEstadoIsA = true;
		    	
		    
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteSkuFOProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteSkuFOProducto - Problema General", ex );
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
				
				logger.error( "getExisteSkuFOProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return proEstadoIsA;
	
	}
	
	/**
	 * 
	 * @param codLocal
	 * @return
	 * @throws DAOException
	 */
	public String getLocalNoExiste( String codLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String mensaje = "";
		
		String locales[] = codLocal.split(",");
	
		try {

			String SQL = "SELECT * FROM BO_LOCALES WHERE COD_LOCAL = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
		
			
			 for (int i = 0; i < locales.length; i++) {
					
				 String local = locales[i];
				 stm.setString(1, local);
					
				 rs = stm.executeQuery();	
					
				 if ( rs.next() )
					 mensaje = "";
				 else
					 mensaje = local;
					
			 }
			
		} catch ( SQLException ex ) {
			
			logger.error( "getLocalNoExiste - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getLocalNoExiste - Problema General", ex );
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
				
				logger.error( "getLocalNoExiste - Problema SQL (close)", e );
			
			}
		
		}
		
		return mensaje;
	
	}
	
	
	/**
	 * 
	 * @param cod_prod1
	 * @param unidadMedida
	 * @return
	 * @throws DAOException
	 */
	public String getValidandoDatosBO( String cod_prod1, String unidadMedida ) throws DAOException {

		
		String mensaje = "";
		
		try {
			
			 if ( !getValidarExisteBOProductos( cod_prod1, unidadMedida ) ) {
				 mensaje = mensaje + "SKU y/o Unidad de Medida no existe.<br>";
			 }

			 mensaje = mensaje + getValidarEstadosBOProductos( cod_prod1, unidadMedida );
			 
			 if ( !getPrecioBOPrecios(cod_prod1, unidadMedida ) ) {
				 mensaje = mensaje + "Faltan locales a crear con precios.<br>";
			 }
			 
			 if ( !getCodigoBarraBOCodBarra(cod_prod1, unidadMedida) ) {
				 mensaje = mensaje + "No tiene un codigo de barra asignado.<br>";
			 }
			
		} catch ( Exception ex ) {
			
			logger.error( "getValidandoDatosBO - Problema General", ex );
			throw new DAOException( ex );
		
		} 
		
		return mensaje;
	
	}
	
	public boolean getValidarExisteBOProductos( String cod_prod1, String unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean existe = false;
		
		try {

			 conexion = conexionUtil.getConexion();
			 String SQL = "SELECT * FROM BO_PRODUCTOS WHERE COD_PROD1 = ? AND UNI_MED = ?";
			
		     stm = conexion.prepareStatement( SQL + " WITH UR" );
		     stm.setString(1, cod_prod1);
		     stm.setString(2, unidadMedida);
					
			rs = stm.executeQuery();	
					
			if ( rs.next() ) 	
				existe = true;
			else
				existe = false;
			
			
		} catch ( SQLException ex ) {
			
			logger.error( "getValidarExisteBOProductos - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getValidarExisteBOProductos - Problema General", ex );
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
				
				logger.error( "getValidarExisteBOProductos - Problema SQL (close)", e );
			
			}
		
		}
	
		return existe;
		
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @return
	 * @throws DAOException
	 */
	public String getValidarEstadosBOProductos( String cod_prod1, String unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String mensaje = "";
		
		try {

			 conexion = conexionUtil.getConexion();
			 String SQL = "SELECT ESTADO, ESTADOACTIVO FROM BO_PRODUCTOS WHERE COD_PROD1 = ? AND UNI_MED = ?";
			
		     stm = conexion.prepareStatement( SQL + " WITH UR" );
		     stm.setString(1, cod_prod1);
		     stm.setString(2, unidadMedida);
					
			rs = stm.executeQuery();	
					
			if ( rs.next() ) {				
						
				if ( rs.getInt(1) == 0) 
					mensaje = mensaje + "El proceso de SAP, a dejando inactivo este producto.<br>";
				
				if ( rs.getString(2).equals("0") )
					mensaje = mensaje + "El producto esta desactivado.<br>";
				
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getValidarEstadosBOProductos - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getValidarEstadosBOProductos - Problema General", ex );
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
				
				logger.error( "getValidarEstadosBOProductos - Problema SQL (close)", e );
			
			}
		
		}
	
		return mensaje;
		
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @return
	 * @throws DAOException
	 */
	public boolean getPrecioBOPrecios( String cod_prod1, String unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		int cont = 0;
		boolean result = false; 
		
		try {

			 conexion = conexionUtil.getConexion();
			 
			 String SQLBOPRE = "SELECT * FROM BO_PRECIOS WHERE COD_PROD1 = ? AND UMEDIDA = ?";
				
			 stm = conexion.prepareStatement( SQLBOPRE + " WITH UR" );
			 stm.setString(1, cod_prod1);
			 stm.setString(2, unidadMedida);
						
			 rs = stm.executeQuery();	
						
			 while ( rs.next() ) {				
				cont++;		
			 }
			
			 if (cont == 8)
				 result = true;
				
		} catch ( SQLException ex ) {
			
			logger.error( "getPrecioBOPrecios - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getPrecioBOPrecios - Problema General", ex );
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
				
				logger.error( "getPrecioBOPrecios - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @param idProducto
	 * @return
	 * @throws DAOException
	 */
	public BarraAuditoriaSustitucionDTO getCodigoBarraBOCodBarra( BOCatalogacionMasivaDTO catalogacionMasiva, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		BarraAuditoriaSustitucionDTO codigoBarra = null;
		
		try {

			 conexion = conexionUtil.getConexion();
			 
			 String SQLBOBAR = "SELECT COD_BARRA, TIP_CODBAR FROM BO_CODBARRA WHERE COD_PROD1 = ? AND UCASE(UNID_MED)=UCASE(?) AND ID_PRODUCTO = ?";
				
			 stm = conexion.prepareStatement( SQLBOBAR + " WITH UR" );
			 stm.setString(1, catalogacionMasiva.getCodigoSap());
			 stm.setString(2, catalogacionMasiva.getUnidadMedida());
			 stm.setInt(3, idProducto);
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
							
				 codigoBarra = new BarraAuditoriaSustitucionDTO();
						
				 codigoBarra.setCod_barra(rs.getString(1));
				 codigoBarra.setTip_codbar(rs.getString(2));
				 
			 }
			
		} catch ( SQLException ex ) {
			
			logger.error( "getCodigoBarraBOCodBarra - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getCodigoBarraBOCodBarra - Problema General", ex );
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
				
				logger.error( "getCodigoBarraBOCodBarra - Problema SQL (close)", e );
			
			}
		
		}
		
		return codigoBarra;
	
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @param producto
	 * @param usr
	 * @throws DAOException
	 */
	public boolean setInsertarFOProducto( BOCatalogacionMasivaDTO catalogacionMasiva, BOProductoDTO producto, UserDTO usr, long idMarca, long unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;	
		
		try {

			String SQL = "INSERT INTO FODBA.FO_PRODUCTOS (PRO_COD_SAP, PRO_TIPRE, PRO_TIPO_PRODUCTO, PRO_MAR_ID, PRO_DES_CORTA, PRO_DES_LARGA, "+
						 "PRO_UNI_ID, PRO_UNIDAD_MEDIDA, PRO_NOTA, PRO_PREPARABLE, PRO_INTER_VALOR, PRO_INTER_MAX, PRO_PARTICIONABLE, EVITAR_PUB_DES, " +
						 "PRO_IMAGEN_MINIFICHA, PRO_ID_BO, PRO_ESTADO, PRO_GENERICO, PRO_FCREA, PRO_USER_MOD, PRO_IMAGEN_FICHA, " +
						 "PRO_RANKING_VENTAS, PRO_TIPO_SEL, PRO_PARTICION, PILA_PORCION ) " +
						 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,?,?,?,?,?)";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL );
			
		    	stm.setString( 1, catalogacionMasiva.getCodigoSap() );
		    	stm.setString( 2, catalogacionMasiva.getUnidadMedida() );
		    	stm.setString( 3, catalogacionMasiva.getTipoProducto() );
		    	stm.setLong( 4, idMarca );
		    	stm.setString( 5, catalogacionMasiva.getDescCortaProducto() );
		    	stm.setString( 6, catalogacionMasiva.getDescLargaProducto() );
		    	stm.setLong( 7, unidadMedida );
		    	stm.setDouble( 8, Double.parseDouble(catalogacionMasiva.getContenidoPPM()) );
		    	stm.setString( 9, catalogacionMasiva.getComentarioPPM() );
		    	stm.setString( 10, catalogacionMasiva.getPreparable() );
		    	stm.setDouble( 11, Double.parseDouble(catalogacionMasiva.getIntervaloMedida()) );
		    	stm.setDouble( 12, Double.parseDouble(catalogacionMasiva.getMaximo()) );
		    	stm.setString( 13, catalogacionMasiva.getParticionable() );
		    	stm.setString( 14, catalogacionMasiva.getEvitarPublicacion() );
		    	stm.setString( 15, producto.getCodSap()+"-"+producto.getUnidad()+".jpg" );
		    	stm.setInt( 16, producto.getId() );
		    	stm.setString( 17, "D" );
		    	stm.setString( 18, "P" );
		    	stm.setLong( 19, usr.getId_usuario() );
		    	stm.setString( 20,  producto.getCodSap()+"-"+producto.getUnidad()+".jpg" );
		    	stm.setInt( 21, 0 );
		    	stm.setString( 22,  "I" );
		    	stm.setInt( 23,  0 );
		    	stm.setDouble( 24,  0 );
		    	
		    	int i = stm.executeUpdate();	
			
		    	if(i>0)
		    		result = true;
		    	
		    	
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarFOProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarFOProducto - Problema General", ex );
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
				
				logger.error( "setInsertarFOProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param codigoSAP
	 * @return
	 * @throws DAOException
	 */
	public String getIdProductoFOProductos( String codigoSAP ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String mensaje = "";
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT PRO_ID FROM FO_PRODUCTOS WHERE PRO_COD_SAP = ? ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setInt(1, Integer.parseInt(codigoSAP));
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
							
				 return mensaje = rs.getString(1);
						
			 }
				
		} catch ( SQLException ex ) {
			
			logger.error( "getIdProductoFOProductos - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getIdProductoFOProductos - Problema General", ex );
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
				
				logger.error( "getIdProductoFOProductos - Problema SQL (close)", e );
			
			}
		
		}
		
		return mensaje;
	
	}
	
	/**
	 * 
	 * @param codigoSAP
	 * @return
	 * @throws DAOException
	 */
	public List getPrecioLocalBOPrecios( String codigoSAP ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		BOPreciosLocalesDTO precio = null;
		
		List precioLocal = new ArrayList();
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT ID_LOCAL, PREC_VALOR, COD_LOCAL FROM BO_PRECIOS WHERE COD_PROD1 = ? ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setString(1, codigoSAP);
						
			 rs = stm.executeQuery();	
						
			 while ( rs.next() ) {				
							
				 precio = new BOPreciosLocalesDTO();
				 
				precio.setPreLocID(rs.getInt(1));
				precio.setPreValor(rs.getFloat(2));
				precio.setPreLocal(rs.getString(3));
				
				precioLocal.add(precio);
				
			 }
				
		} catch ( SQLException ex ) {
			
			logger.error( "getPrecioLocalBOPrecios - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getPrecioLocalBOPrecios - Problema General", ex );
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
				
				logger.error( "getPrecioLocalBOPrecios - Problema SQL (close)", e );
			
			}
		
		}
		
		return precioLocal;
	
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @param idProducto
	 * @param precioLocal
	 * @param stockTodosLocales
	 * @param depublicarTodosLocales
	 * @throws DAOException
	 */
	
	public boolean setInsertarFOPreciosLocales( BOCatalogacionMasivaDTO catalogacionMasiva, String idProducto, List precioLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		BOPreciosLocalesDTO prelocal = new BOPreciosLocalesDTO();
		
		logger.info( "validar si esta vacío que implica sin stock en todos los locales..." );
		
		logger.info( "validar si esta vacío que implica despublicado en todos los locales..." );
	    
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO FODBA.FO_PRECIOS_LOCALES (PRE_PRO_ID, PRE_LOC_ID, PRE_COSTO, PRE_VALOR, PRE_STOCK, PRE_ESTADO, PRE_TIENESTOCK) " +
					"VALUES (?,?,?,?,?,?,?)";
			
				stm = conexion.prepareStatement( SQL ); 
				
				Iterator it = precioLocal.iterator();
				
			    while ( it.hasNext() ) {
			    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setInt( 1, Integer.parseInt(idProducto) );
			    	stm.setInt( 2, prelocal.getPreLocID() );
			    	stm.setDouble( 3, 0 );
			    	stm.setDouble( 4, prelocal.getPreValor() );
			    	stm.setInt( 5, 0 );
			    	stm.setString( 6, "D" );
			    	stm.setInt( 7, 0 );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			
			    }	
			
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarFOPreciosLocales - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarFOPreciosLocales - Problema General", ex );
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
				
				logger.error( "setInsertarFOPreciosLocales - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/*public boolean setInsertarFOPreciosLocales( BOCatalogacionMasivaDTO catalogacionMasiva, String idProducto, List precioLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		boolean conStockTodos = false;
		boolean sinStockTodos = false;
		boolean publicarTodos = false;
		boolean despublicarTodos = false;
		
		BOPreciosLocalesDTO prelocal = new BOPreciosLocalesDTO();
		
		logger.info( "validar si esta vacío que implica sin stock en todos los locales..." );
		
		if (catalogacionMasiva.getStockLocales().equals("TODOS"))
			conStockTodos = true;
		else if (catalogacionMasiva.getStockLocales().equals(""))
			sinStockTodos = true;
		
		logger.info( "validar si esta vacío que implica despublicado en todos los locales..." );
	    
		if (catalogacionMasiva.getPublicadoLocales().equals("TODOS"))
			publicarTodos = true;
		else if (catalogacionMasiva.getPublicadoLocales().equals(""))
			despublicarTodos = true;
		
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO FODBA.FO_PRECIOS_LOCALES (PRE_PRO_ID, PRE_LOC_ID, PRE_COSTO, PRE_VALOR, PRE_STOCK, PRE_ESTADO, PRE_TIENESTOCK) " +
					"VALUES (?,?,?,?,?,?,?)";
			
			String SQLS = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_TIENESTOCK = ? " +
					"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
			String SQLP = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ? " +
					"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
			if (conStockTodos || sinStockTodos || publicarTodos || despublicarTodos) {
				
				stm = conexion.prepareStatement( SQL ); 
				
				Iterator it = precioLocal.iterator();
				
			    while ( it.hasNext() ) {
			    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setInt( 1, Integer.parseInt(idProducto) );
			    	stm.setInt( 2, prelocal.getPreLocID() );
			    	stm.setDouble( 3, 0 );
			    	stm.setDouble( 4, prelocal.getPreValor() );
			    	stm.setInt( 5, 0 );
			    	
			    	if(publicarTodos)
			    		stm.setString( 6, "A" );
			    	else
			    		stm.setString( 6, "D" );
			    	
			    	if (conStockTodos)
			    		stm.setInt( 7, 1 );
			    	else
			    		stm.setInt( 7, 0 );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			
			    }
				
			}
			
			
			
			
			if ( !conStockTodos && !sinStockTodos && !publicarTodos && !despublicarTodos) {
				
				stm = conexion.prepareStatement( SQL ); 
				
				
					String publicarLocales[] = catalogacionMasiva.getPublicadoLocales().split(",");
					String stockLocales[] = catalogacionMasiva.getStockLocales().split(",");
				
					
					if (stockLocales.length < publicarLocales.length) {
					
						for (int x=0;x<stockLocales.length;x++) {
							  boolean encontrado = false;
							  int iter = 0; // Iterador
							 
							  while ((!encontrado) && (iter < publicarLocales.length)) {
							    if (publicarLocales[iter] == stockLocales[x])
							      encontrado = true;
							    iter++;
							  }
							 
							  if (encontrado) {
							    System.out.println (stockLocales[x] + " es un número repetido. Encontrado en " + iter + " búsquedas");
							  
							    Iterator it = precioLocal.iterator();
								
								 while ( it.hasNext() ) {
									    
								    	prelocal = (BOPreciosLocalesDTO)it.next();
								
								    		if (stockLocales[x].toString().equals(prelocal.getPreLocal())) {
								    	
								    			stm.setInt( 1, Integer.parseInt(idProducto) );
										    	stm.setInt( 2, prelocal.getPreLocID() );
										    	stm.setDouble( 3, 0 );
										    	stm.setDouble( 4, prelocal.getPreValor() );
										    	stm.setInt( 5, 0 );
										    	stm.setString( 6, "A" );
										    	stm.setInt( 7, 1 );
										    	
										    	int z = stm.executeUpdate();	
											    
										    	if(z>0)
										    		result = true;
										
								    		}
								    	
								 }
								    	
							  }else {
							    System.out.println (stockLocales[x] + " no está en la lista. No encontrado en " + iter + " búsquedas");
							    
							    Iterator it = precioLocal.iterator();
								
								 while ( it.hasNext() ) {
									    
								    	prelocal = (BOPreciosLocalesDTO)it.next();
								
								    		if (stockLocales[x].toString().equals( prelocal.getPreLocal( ) )) {
								    	
								    			stm.setInt( 1, Integer.parseInt(idProducto) );
										    	stm.setInt( 2, prelocal.getPreLocID() );
										    	stm.setDouble( 3, 0 );
										    	stm.setDouble( 4, prelocal.getPreValor() );
										    	stm.setInt( 5, 0 );
										    	stm.setString( 6, "D" );
										    	stm.setInt( 7, 1 );
										    	
										    	int z = stm.executeUpdate();	
											    
										    	if(z>0)
										    		result = true;
										
								    		}
								    	
								 }
							    
							  }
						
						}
					
					}
					
					if (publicarLocales.length < stockLocales.length) {
						
						for (int x=0;x<publicarLocales.length;x++) {
							  boolean encontrado = false;
							  int iter = 0; // Iterador
							 
							  while ((!encontrado) && (iter < stockLocales.length)) {
							    if (stockLocales[iter] == publicarLocales[x])
							      encontrado = true;
							    iter++;
							  }
							 
							  if (encontrado) {
							    System.out.println (publicarLocales[x] + " es un número repetido. Encontrado en " + iter + " búsquedas");
							  
							    Iterator it = precioLocal.iterator();
								
								 while ( it.hasNext() ) {
									    
								    	prelocal = (BOPreciosLocalesDTO)it.next();
								
								    		if (publicarLocales[x].toString().equals(prelocal.getPreLocal())) {
								    	
								    			stm.setInt( 1, Integer.parseInt(idProducto) );
										    	stm.setInt( 2, prelocal.getPreLocID() );
										    	stm.setDouble( 3, 0 );
										    	stm.setDouble( 4, prelocal.getPreValor() );
										    	stm.setInt( 5, 0 );
										    	stm.setString( 6, "A" );
										    	stm.setInt( 7, 1 );
										    	
										    	int z = stm.executeUpdate();	
											    
										    	if(z>0)
										    		result = true;
										
								    		}
								    	
								 }
								    	
							  }else {
							    System.out.println (publicarLocales[x] + " no está en la lista. No encontrado en " + iter + " búsquedas");
							    
							    Iterator it = precioLocal.iterator();
								
								 while ( it.hasNext() ) {
									    
								    	prelocal = (BOPreciosLocalesDTO)it.next();
								
								    		if (publicarLocales[x].toString().equals( prelocal.getPreLocal( ) )) {
								    	
								    			stm.setInt( 1, Integer.parseInt(idProducto) );
										    	stm.setInt( 2, prelocal.getPreLocID() );
										    	stm.setDouble( 3, 0 );
										    	stm.setDouble( 4, prelocal.getPreValor() );
										    	stm.setInt( 5, 0 );
										    	stm.setString( 6, "A" );
										    	stm.setInt( 7, 0 );
										    	
										    	int z = stm.executeUpdate();	
											    
										    	if(z>0)
										    		result = true;
										
								    		}
								    	
								 }
							    
							  }
						
						}
					
					}
			
			}
			
			if ( (conStockTodos || sinStockTodos) && (!publicarTodos || !despublicarTodos)) {
				
				
				stm = conexion.prepareStatement( SQL ); 
				
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
					prelocal = (BOPreciosLocalesDTO)it.next();
								    	
						stm.setInt( 1, Integer.parseInt(idProducto) );
				    	stm.setInt( 2, prelocal.getPreLocID() );
				    	stm.setDouble( 3, 0 );
				    	stm.setDouble( 4, prelocal.getPreValor() );
				    	stm.setInt( 5, 0 );
				    	stm.setString( 6, "D" );
				    	
				    	if (conStockTodos)
				    		stm.setInt( 7, 1 );
				    	else
				    		stm.setInt( 7, 0 );
				    	
				    	int z = stm.executeUpdate();	
					    
				    	if(z>0)
				    		result = true;
					
				}
								 	
			stm = conexion.prepareStatement( SQLP ); 
			
				String publicarLocales[] = catalogacionMasiva.getPublicadoLocales().split(",");
				
				Iterator itp = precioLocal.iterator();
								
				while ( itp.hasNext() ) {
									    
					prelocal = (BOPreciosLocalesDTO)itp.next();
					
					for (int x=0;x<publicarLocales.length;x++) {
					
					if (publicarLocales[x].toString().equals(prelocal.getPreLocal())) {
								    	
						stm.setDouble( 1, 0 );
				    	stm.setDouble( 2, prelocal.getPreValor() );
				    	stm.setInt( 3, 0 );
				    	stm.setString( 4, "A" );
				    	stm.setInt( 5, Integer.parseInt(idProducto) );
				    	stm.setInt( 6, prelocal.getPreLocID() );
				    	
				    	
				    	int z = stm.executeUpdate();	
					    
				    	if(z>0)
				    		result = true;
										
					}
					
					}

				}
				
			}
			
			
			if ( (!conStockTodos || !sinStockTodos) && (publicarTodos || despublicarTodos)) {
				
				stm = conexion.prepareStatement( SQL ); 
				
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
					prelocal = (BOPreciosLocalesDTO)it.next();
								    	
						stm.setInt( 1, Integer.parseInt(idProducto) );
				    	stm.setInt( 2, prelocal.getPreLocID() );
				    	stm.setDouble( 3, 0 );
				    	stm.setDouble( 4, prelocal.getPreValor() );
				    	stm.setInt( 5, 0 );
				    	
				    	if (publicarTodos)
				    		stm.setString( 6, "A" );
				    	else
				    		stm.setString( 6, "D" );
				    	
				    	stm.setInt( 7, 0 );
				    	
				    	int z = stm.executeUpdate();	
					    
				    	if(z>0)
				    		result = true;
					
				}
				
				stm = conexion.prepareStatement( SQLS ); 
				
				String stockLocales[] = catalogacionMasiva.getStockLocales().split(",");
				
				Iterator its = precioLocal.iterator();
								
				while ( its.hasNext() ) {
									    
					prelocal = (BOPreciosLocalesDTO)its.next();
					
					for (int x=0;x<stockLocales.length;x++) {
					
					if (stockLocales[x].toString().equals(prelocal.getPreLocal())) {
								    	
						stm.setDouble( 1, 0 );
				    	stm.setDouble( 2, prelocal.getPreValor() );
				    	stm.setInt( 3, 0 );
				    	stm.setInt( 4, 1 );
				    	stm.setInt( 5, Integer.parseInt(idProducto) );
				    	stm.setInt( 6, prelocal.getPreLocID() );
				    	
				    	int z = stm.executeUpdate();	
					    
				    	if(z>0)
				    		result = true;
										
					
					}
								 
					}
				
				}
			
			}
			
				
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarFOPreciosLocales - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarFOPreciosLocales - Problema General", ex );
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
				
				logger.error( "setInsertarFOPreciosLocales - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}*/
	
	public boolean setModificarFOPreciosLocales( BOCatalogacionMasivaDTO catalogacionMasiva, String idProducto, List precioLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		BOPreciosLocalesDTO prelocal = new BOPreciosLocalesDTO();
		
		logger.info( "validar si esta vacío que implica sin stock en todos los locales..." );
	    
		logger.info( "validar si esta vacío que implica despublicado en todos los locales..." );
	    
		try {
			
			conexion = conexionUtil.getConexion();
			
			
				String SQL = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ?, PRE_TIENESTOCK = ? " +
						"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
				stm = conexion.prepareStatement( SQL ); 
			
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setDouble( 1, 0 );
			    	stm.setDouble( 2, prelocal.getPreValor() );
			    	stm.setInt( 3, 0 );
			    	stm.setString( 4, "D" );
			    	stm.setInt( 5, 0 );
			    	stm.setInt( 6, Integer.parseInt(idProducto) );
			    	stm.setInt( 7, prelocal.getPreLocID() );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			    	
				}
			
			
		} catch ( SQLException ex ) {
			
			logger.error( "setModificarFOPreciosLocales - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setModificarFOPreciosLocales - Problema General", ex );
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
				
				logger.error( "setModificarFOPreciosLocales - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/*public boolean setModificarFOPreciosLocales( BOCatalogacionMasivaDTO catalogacionMasiva, String idProducto, List precioLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean conStockTodos = false;
		boolean sinStockTodos = false;
		boolean publicarTodos = false;
		boolean despublicarTodos = false;
		boolean result = false;
		
		BOPreciosLocalesDTO prelocal = new BOPreciosLocalesDTO();
		
		logger.info( "validar si esta vacío que implica sin stock en todos los locales..." );
	    
		if (catalogacionMasiva.getStockLocales().equals("TODOS"))
			conStockTodos = true;
		else if (catalogacionMasiva.getStockLocales().equals(""))
			sinStockTodos = true;
		
		logger.info( "validar si esta vacío que implica despublicado en todos los locales..." );
	    
		if (catalogacionMasiva.getPublicadoLocales().equals("TODOS"))
			publicarTodos = true;
		else if (catalogacionMasiva.getPublicadoLocales().equals(""))
			despublicarTodos = true;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			if (sinStockTodos) {
				
				String SQLU = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_TIENESTOCK = ? " +
						"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
				stm = conexion.prepareStatement( SQLU ); 
				
				Iterator itp = precioLocal.iterator();
				
				while ( itp.hasNext() ) {
				    
			    	prelocal = (BOPreciosLocalesDTO)itp.next();
			    	
			    	stm.setDouble( 1, 0 );
			    	stm.setDouble( 2, prelocal.getPreValor() );
			    	stm.setInt( 3, 0 );
			    	stm.setInt( 4, 0 );
			    	stm.setInt( 5, Integer.parseInt(idProducto) );
			    	stm.setInt( 6, prelocal.getPreLocID() );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			    	
				}
				
			}
			
			if (despublicarTodos) {
				
				String SQL = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ?, PRE_TIENESTOCK = ? " +
						"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
				stm = conexion.prepareStatement( SQL ); 
			
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setDouble( 1, 0 );
			    	stm.setDouble( 2, prelocal.getPreValor() );
			    	stm.setInt( 3, 0 );
			    	stm.setString( 4, "D" );
			    	stm.setInt( 5, 1 );
			    	stm.setInt( 6, Integer.parseInt(idProducto) );
			    	stm.setInt( 7, prelocal.getPreLocID() );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			    	
				}
				
			}
			
			if (conStockTodos && publicarTodos) {
			
				String SQL = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ?, PRE_TIENESTOCK = ? " +
						"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
				stm = conexion.prepareStatement( SQL ); 
			
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setDouble( 1, 0 );
			    	stm.setDouble( 2, prelocal.getPreValor() );
			    	stm.setInt( 3, 0 );
			    	stm.setString( 4, "A" );
			    	stm.setInt( 5, 1 );
			    	stm.setInt( 6, Integer.parseInt(idProducto) );
			    	stm.setInt( 7, prelocal.getPreLocID() );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			    	
				}
				
			}
			
			if (!conStockTodos && publicarTodos) {
				
				String SQL = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ? " +
						"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
				stm = conexion.prepareStatement( SQL ); 
			
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setDouble( 1, 0 );
			    	stm.setDouble( 2, prelocal.getPreValor() );
			    	stm.setInt( 3, 0 );
			    	stm.setString( 4, "A" );
			    	stm.setInt( 5, Integer.parseInt(idProducto) );
			    	stm.setInt( 6, prelocal.getPreLocID() );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			    	
				}
				
				if (!catalogacionMasiva.getStockLocales().equals("")) {
					
					String SQLU = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_TIENESTOCK = ? " +
							"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
				
					stm = conexion.prepareStatement( SQLU ); 
					
					String stockLocales[] = catalogacionMasiva.getStockLocales().split(",");
					
					Iterator itp = precioLocal.iterator();
					
					while ( itp.hasNext() ) {
					    
				    	prelocal = (BOPreciosLocalesDTO)itp.next();
				    	
				    	stm.setDouble( 1, 0 );
				    	stm.setDouble( 2, prelocal.getPreValor() );
				    	stm.setInt( 3, 0 );
				    	stm.setInt( 4, 0 );
				    	stm.setInt( 5, Integer.parseInt(idProducto) );
				    	stm.setInt( 6, prelocal.getPreLocID() );
				    	
				    	int i = stm.executeUpdate();	
					    
				    	if(i>0)
				    		result = true;
				    	
					}
					
					
					for(int j=0;j<stockLocales.length;j++){
					
						itp = precioLocal.iterator();
						
						 while ( itp.hasNext() ) {
							    
							 prelocal = (BOPreciosLocalesDTO)itp.next();
						
					         if(prelocal.getPreLocal().equals( stockLocales[j].toString())) {
					         
					        	 stm.setDouble( 1, 0 );
					        	 stm.setDouble( 2, prelocal.getPreValor() );
					        	 stm.setInt( 3, 0 );
					        	 stm.setInt( 4, 1 );
					        	 stm.setInt( 5, Integer.parseInt(idProducto) );
					        	 stm.setInt( 6, prelocal.getPreLocID() );
						    	
					        	 int y = stm.executeUpdate();	
								    
					        	 if(y>0)
							    	result = true;
					         
					         }
					
						 }
					
					}
					
				}
				
			}
			
			if (conStockTodos && !publicarTodos) {
				
				String SQL = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_TIENESTOCK = ? " +
						"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
			
				stm = conexion.prepareStatement( SQL ); 
				
				Iterator it = precioLocal.iterator();
				
				while ( it.hasNext() ) {
				    
			    	prelocal = (BOPreciosLocalesDTO)it.next();
			    	
			    	stm.setDouble( 1, 0 );
			    	stm.setDouble( 2, prelocal.getPreValor() );
			    	stm.setInt( 3, 0 );
			    	stm.setInt( 4, 1 );
			    	stm.setInt( 5, Integer.parseInt(idProducto) );
			    	stm.setInt( 6, prelocal.getPreLocID() );
			    	
			    	int i = stm.executeUpdate();	
				    
			    	if(i>0)
			    		result = true;
			    	
				}
				
				if (!catalogacionMasiva.getPublicadoLocales().equals("")) {
					
					String SQLU = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ? " +
							"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
				
					stm = conexion.prepareStatement( SQLU ); 
					
					String publicarLocales[] = catalogacionMasiva.getPublicadoLocales().split(",");
					
					Iterator itp = precioLocal.iterator();
					
					while ( itp.hasNext() ) {
					    
				    	prelocal = (BOPreciosLocalesDTO)itp.next();
				    	
				    	stm.setDouble( 1, 0 );
				    	stm.setDouble( 2, prelocal.getPreValor() );
				    	stm.setInt( 3, 0 );
				    	stm.setString( 4, "D" );
				    	stm.setInt( 5, Integer.parseInt(idProducto) );
				    	stm.setInt( 6, prelocal.getPreLocID() );
				    	
				    	int i = stm.executeUpdate();	
					    
				    	if(i>0)
				    		result = true;
				    	
					}
					
					for(int j=0;j<publicarLocales.length;j++){
					
						itp = precioLocal.iterator();
						
						 while ( itp.hasNext() ) {
							    
							 prelocal = (BOPreciosLocalesDTO)itp.next();
						
					         if(prelocal.getPreLocal().equals( publicarLocales[j].toString())) {
					         
					        	 stm.setDouble( 1, 0 );
					        	 stm.setDouble( 2, prelocal.getPreValor() );
					        	 stm.setInt( 3, 0 );
					        	 stm.setString( 4, "A" );
					        	 stm.setInt( 5, Integer.parseInt(idProducto) );
					        	 stm.setInt( 6, prelocal.getPreLocID() );
						    	
					        	 int y = stm.executeUpdate();	
								    
					        	 if(y>0)
							    	 result = true;
					        	 
					         }
				             
						 }
					
					}
				
				}
				
			}
			
			if (!conStockTodos && !publicarTodos) {
				
				if (!catalogacionMasiva.getPublicadoLocales().equals("")) {
					
					String SQLU = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_ESTADO = ? " +
							"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
				
					stm = conexion.prepareStatement( SQLU ); 
					
					String publicarLocales[] = catalogacionMasiva.getPublicadoLocales().split(",");
					
					Iterator itp = precioLocal.iterator();
					
					while ( itp.hasNext() ) {
					    
				    	prelocal = (BOPreciosLocalesDTO)itp.next();
				    	
				    	stm.setDouble( 1, 0 );
				    	stm.setDouble( 2, prelocal.getPreValor() );
				    	stm.setInt( 3, 0 );
				    	stm.setString( 4, "D" );
				    	stm.setInt( 5, Integer.parseInt(idProducto) );
				    	stm.setInt( 6, prelocal.getPreLocID() );
				    	
				    	int i = stm.executeUpdate();	
					    
				    	if(i>0)
				    		result = true;
				    	
					}
					
					for(int j=0;j<publicarLocales.length;j++){
					
						itp = precioLocal.iterator();
						
						 while ( itp.hasNext() ) {
							    
							 prelocal = (BOPreciosLocalesDTO)itp.next();
						
					         if(prelocal.getPreLocal().equals( publicarLocales[j].toString())) {
					         
					        	 stm.setDouble( 1, 0 );
					        	 stm.setDouble( 2, prelocal.getPreValor() );
					        	 stm.setInt( 3, 0 );
					        	 stm.setString( 4, "A" );
					        	 stm.setInt( 5, Integer.parseInt(idProducto) );
					        	 stm.setInt( 6, prelocal.getPreLocID() );
						    	
					        	 int y = stm.executeUpdate();	
								    
					        	 if(y>0)
							    	result = true;
					         
					         }
					
						 }
					
					}
					
				}
				
				if (!catalogacionMasiva.getStockLocales().equals("")) {
					
					String SQLU = "UPDATE FODBA.FO_PRECIOS_LOCALES SET PRE_COSTO = ?, PRE_VALOR = ?, PRE_STOCK =?, PRE_TIENESTOCK = ? " +
							"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? ";
				
					stm = conexion.prepareStatement( SQLU ); 
					
					String stockLocales[] = catalogacionMasiva.getStockLocales().split(",");
					
					Iterator itp = precioLocal.iterator();
					
					while ( itp.hasNext() ) {
					    
				    	prelocal = (BOPreciosLocalesDTO)itp.next();
				    	
				    	stm.setDouble( 1, 0 );
				    	stm.setDouble( 2, prelocal.getPreValor() );
				    	stm.setInt( 3, 0 );
				    	stm.setInt( 4, 0 );
				    	stm.setInt( 5, Integer.parseInt(idProducto) );
				    	stm.setInt( 6, prelocal.getPreLocID() );
				    	
				    	int i = stm.executeUpdate();	
					    
				    	if(i>0)
				    		result = true;
				    	
					}
					
					for(int j=0;j<stockLocales.length;j++){
					
						itp = precioLocal.iterator();
						
						 while ( itp.hasNext() ) {
							    
							 prelocal = (BOPreciosLocalesDTO)itp.next();
						
					         if(prelocal.getPreLocal().equals( stockLocales[j].toString())) {
					         
					        	 stm.setDouble( 1, 0 );
					        	 stm.setDouble( 2, prelocal.getPreValor() );
					        	 stm.setInt( 3, 0 );
					        	 stm.setInt( 4, 1 );
					        	 stm.setInt( 5, Integer.parseInt(idProducto) );
					        	 stm.setInt( 6, prelocal.getPreLocID() );
						    	
					        	 int y = stm.executeUpdate();	
								    
					        	 if(y>0)
							    	result = true;
					         	 
					         }
					
						 }
					
					}
					
						 
						
					
				}
				
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "setModificarFOPreciosLocales - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setModificarFOPreciosLocales - Problema General", ex );
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
				
				logger.error( "setModificarFOPreciosLocales - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}*/
	
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
	
	
	public MarcasDTO getFOMarca( String marcaProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		MarcasDTO marca = null;
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT MAR_ID FROM FO_MARCAS WHERE UCASE(MAR_NOMBRE)=UCASE(?) ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setString(1, marcaProducto);
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
					
				 marca = new MarcasDTO();
				 
				 marca.setId(rs.getInt(1));
				 
			 }	
			
		} catch ( SQLException ex ) {
			
			logger.error( "getFOMarca - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getFOMarca - Problema General", ex );
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
				
				logger.error( "getFOMarca - Problema SQL (close)", e );
			
			}
		
		}
		
		return marca;
	
	}
	
	
	public UnidadMedidaDTO getFOUnidadMedidaPPM( String unidadMedidaDesc ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		UnidadMedidaDTO unidadMedida = null;
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT UNI_ID FROM FO_UNIDADES_MEDIDA WHERE UCASE(UNI_DESC)=UCASE(?)";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setString(1, unidadMedidaDesc);
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
					
				 unidadMedida = new UnidadMedidaDTO(rs.getLong(1), "", 0, "");
				 
				 unidadMedida.setId( rs.getLong(1));
				 
			 }	
			
		} catch ( SQLException ex ) {
			
			logger.error( "getFOUnidadMedidaPPM - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getFOUnidadMedidaPPM - Problema General", ex );
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
				
				logger.error( "getFOUnidadMedidaPPM - Problema SQL (close)", e );
			
			}
		
		}
		
		return unidadMedida;
	
	}
	
	/**
	 * 
	 * @param codigoSAP
	 * @param unidadMedida
	 * @return
	 * @throws DAOException
	 */
	public FOProductoDTO getFOProducto( String codigoSAP, String unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		FOProductoDTO producto = null;
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT PRO_ID FROM FO_PRODUCTOS WHERE PRO_COD_SAP = ? AND UCASE(PRO_TIPRE)=UCASE(?) ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setString(1, codigoSAP);
			 stm.setString(2, unidadMedida);
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
					
				 producto = new FOProductoDTO();
				 
				 producto.setId(rs.getInt(1));
				 
			 }
				
			
		} catch ( SQLException ex ) {
			
			logger.error( "getFOProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getFOProducto - Problema General", ex );
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
				
				logger.error( "getFOProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return producto;
	
	}
	
	public BOProductoDTO getBoPrecio( String codigoSAP ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		BOProductoDTO producto = null;
		
		try {

			conexion = conexionUtil.getConexion();
			 
			 String SQL = "SELECT ID_PRODUCTO, DES_CORTA, UNI_MED, COD_PROD1, DES_LARGA FROM FO_PRODUCTOS WHERE COD_PROD1 = ? ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setInt(1, Integer.parseInt(codigoSAP));
						
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
			
			logger.error( "getProductoBySkuUniMed - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getProductoBySkuUniMed - Problema General", ex );
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
				
				logger.error( "getProductoBySkuUniMed - Problema SQL (close)", e );
			
			}
		
		}
		
		return producto;
	
	}
	
	/**
	 * 
	 * @param codigoSAP
	 * @param unidadMedida
	 * @return
	 * @throws DAOException
	 */
	public boolean getCodigoBarraBOCodBarra( String codigoSAP, String unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		boolean existe = false;
		
		try {

			conexion = conexionUtil.getConexion();
			 
			String SQL = "SELECT COD_BARRA, TIP_CODBAR FROM BO_CODBARRA WHERE COD_PROD1 = ?  AND UNID_MED = ? ";
				
			 stm = conexion.prepareStatement( SQL + " WITH UR" );
			 stm.setString(1, codigoSAP);
			 stm.setString(2, unidadMedida);
						
			 rs = stm.executeQuery();	
						
			 if ( rs.next() ) {				
							
				 existe = true;
				
			 }
				
		} catch ( SQLException ex ) {
			
			logger.error( "getCodigoBarraBOCodBarra - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getCodigoBarraBOCodBarra - Problema General", ex );
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
				
				logger.error( "getCodigoBarraBOCodBarra - Problema SQL (close)", e );
			
			}
		
		}
		
		return existe;
	
	}
	
	/**
	 * 
	 * @param codigoBarra
	 * @param idProducto
	 * @throws DAOException
	 */
	public boolean setInsertarFoCodBarra( BarraAuditoriaSustitucionDTO codigoBarra, String idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO FODBA.FO_COD_BARRA (BAR_CODIGO, BAR_TIPO_CODIGO, BAR_PRO_ID, BAR_ESTADO) " +
					"VALUES (?,?,?,?)";
			
			stm = conexion.prepareStatement( SQL ); 
			
		    	stm.setString( 1, codigoBarra.getCod_barra() );
		    	stm.setString( 2, codigoBarra.getTip_codbar() );
		    	stm.setInt( 3, Integer.parseInt(idProducto) );
		    	stm.setString( 4, "A" );
		    	
		    	int i = stm.executeUpdate();
		    	
		    	if(i>0)
		    		result = true;
			
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarFoCodBarra - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarFoCodBarra - Problema General", ex );
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
				
				logger.error( "setInsertarFoCodBarra - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @throws DAOException
	 */
	public boolean setModificarBOProducto( BOCatalogacionMasivaDTO catalogacionMasiva ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "UPDATE BO_PRODUCTOS SET MIX_WEB =? WHERE COD_PROD1 =? AND UNI_MED =? ";
			
			stm = conexion.prepareStatement( SQL ); 
			
			
		    	stm.setString( 1, "S" );
		    	stm.setString( 2, catalogacionMasiva.getCodigoSap() );
		    	stm.setString( 3, catalogacionMasiva.getUnidadMedida() );
		    	
		    	int i = stm.executeUpdate();
		    	
		    	if(i>0)
		    		result = true;
			
		} catch ( SQLException ex ) {
			
			logger.error( "setModificarBOProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setModificarBOProducto - Problema General", ex );
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
				
				logger.error( "setModificarBOProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
		
	}
	
	public boolean setModificarFOProducto( BOCatalogacionMasivaDTO catalogacionMasiva, BOProductoDTO producto, UserDTO usr, long idMarca, long unidadMedida ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			String SQL = "UPDATE FODBA.FO_PRODUCTOS SET PRO_COD_SAP = ?, PRO_TIPRE = ?, PRO_TIPO_PRODUCTO = ?, PRO_MAR_ID = ?, PRO_DES_CORTA = ?, " +
						"PRO_DES_LARGA = ?, PRO_UNI_ID = ?, PRO_UNIDAD_MEDIDA = ?, PRO_NOTA = ?, PRO_PREPARABLE = ?, PRO_INTER_VALOR = ?, " +
						"PRO_INTER_MAX = ?, PRO_PARTICIONABLE = ?, EVITAR_PUB_DES = ?, PRO_IMAGEN_MINIFICHA = ?, PRO_ID_BO = ?, PRO_ESTADO = ?, " +
						"PRO_GENERICO = ?, PRO_USER_MOD = ?, PRO_IMAGEN_FICHA = ?, PRO_RANKING_VENTAS = ?, PRO_TIPO_SEL = ?, " +
						"PRO_PARTICION = ?, PILA_PORCION = ? " +
						"WHERE PRO_COD_SAP = ? AND PRO_TIPRE = ?";
			
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL );
			
			stm.setString( 1, catalogacionMasiva.getCodigoSap() );
	    	stm.setString( 2, catalogacionMasiva.getUnidadMedida() );
	    	stm.setString( 3, catalogacionMasiva.getTipoProducto() );
	    	stm.setLong( 4, idMarca );
	    	stm.setString( 5, catalogacionMasiva.getDescCortaProducto() );
	    	stm.setString( 6, catalogacionMasiva.getDescLargaProducto() );
	    	stm.setLong( 7, unidadMedida );
	    	stm.setDouble( 8, Double.parseDouble(catalogacionMasiva.getContenidoPPM()) );
	    	stm.setString( 9, catalogacionMasiva.getComentarioPPM() );
	    	stm.setString( 10, catalogacionMasiva.getPreparable() );
	    	stm.setDouble( 11, Double.parseDouble(catalogacionMasiva.getIntervaloMedida()) );
	    	stm.setDouble( 12, Double.parseDouble(catalogacionMasiva.getMaximo()) );
	    	stm.setString( 13, catalogacionMasiva.getParticionable() );
	    	stm.setString( 14, catalogacionMasiva.getEvitarPublicacion() );
	    	stm.setString( 15, producto.getCodSap()+"-"+producto.getUnidad()+".jpg" );
	    	stm.setInt( 16, producto.getId() );
	    	stm.setString( 17, "D" );
	    	stm.setString( 18, "P" );
	    	stm.setLong( 19, usr.getId_usuario() );
	    	stm.setString( 20,  producto.getCodSap()+"-"+producto.getUnidad()+".jpg" );
	    	stm.setInt( 21, 0 );
	    	stm.setString( 22,  "I" );
	    	stm.setInt( 23,  0 );
	    	stm.setDouble( 24,  0 );
	    	
	    	stm.setString( 25, catalogacionMasiva.getCodigoSap() );
	    	stm.setString( 26, catalogacionMasiva.getUnidadMedida() );
			
			int i = stm.executeUpdate();	
			
		    if(i>0)
				result = true;
		  
			
		} catch ( SQLException ex ) {
			
			logger.error( "setModificarFOProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setModificarFOProducto - Problema General", ex );
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
				
				logger.error( "setModificarFOProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @param idProducto
	 * @throws DAOException
	 */
	public boolean setInsertarProductoSector( BOCatalogacionMasivaDTO catalogacionMasiva, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO BO_PROD_SECTOR (ID_SECTOR, ID_PRODUCTO) " +
					"VALUES (?,?)";
			
			stm = conexion.prepareStatement( SQL ); 
			
	    	stm.setInt( 1, Integer.parseInt(catalogacionMasiva.getSectorPicking()) );
	    	stm.setInt( 2, idProducto );
	    	
	    	int i = stm.executeUpdate();
	    	
	    	if(i>0)
	    		result = true;
		
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarProductoSector - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarProductoSector - Problema General", ex );
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
				
				logger.error( "setInsertarProductoSector - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @param idProducto
	 * @throws DAOException
	 */
	public boolean setInsertarProductoCategoria( String idCategoria, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "INSERT INTO FO_PRODUCTOS_CATEGORIAS (PRCA_CAT_ID, PRCA_PRO_ID, PRCA_ESTADO, PRCA_ORDEN, PRCA_CON_PAGO) " +
					"VALUES (?,?,?,?,?)";
			
			stm = conexion.prepareStatement( SQL ); 
				
			stm.setInt( 1, Integer.parseInt( idCategoria ) );
			stm.setInt( 2, idProducto );
			stm.setString( 3, "A" );
			stm.setInt( 4, 1000 );
			stm.setString( 5, "S" );
			    	
			int i = stm.executeUpdate();
			
			if(i>0)
				result = true;
			    		
		
		} catch ( SQLException ex ) {
			
			logger.error( "setInsertarProductoCategoria - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setInsertarProductoCategoria - Problema General", ex );
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
				
				logger.error( "setInsertarProductoCategoria - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param listaCategorias
	 * @return
	 * @throws DAOException
	 */
	public String getExisteCategoria( List listaCategorias ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		String mensaje = "";
			
		try {

			String SQL = "SELECT ter.CAT_TIPO, inter.CAT_TIPO, cab.CAT_TIPO FROM fo_categorias ter "+
						"inner join fo_catsubcat terint on (ter.cat_id = terint.subcat_id) "+
						"inner join fo_categorias inter on (terint.cat_id = inter.cat_id and inter.cat_estado = 'A') "+
						"inner join fo_catsubcat intcab on (inter.cat_id = intcab.subcat_id) "+
						"inner join fo_categorias cab on (intcab.cat_id = cab.cat_id and cab.cat_estado = 'A') "+   
						"where ter.CAT_ID = ?";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			Iterator it = listaCategorias.iterator();
    		
		    while ( it.hasNext() ) {
 		    	
		    	String categoria = ( String )it.next();
						
		    	stm.setString( 1, categoria );
		    		
		    	rs = stm.executeQuery();	
						
				if ( rs.next() ) { 					
				
					if (rs.getString(1).equals("T") && rs.getString(3).equals("C"))
						mensaje = "";
					else
						mensaje = "La categoria " +categoria+ " no es terminal.<br>";
				}	
				else
					return mensaje = "La categoria "+categoria+ " esta desactivada o eliminada.<br>";
				
		    }
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteCategoria - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteCategoria - Problema General", ex );
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
				
				logger.error( "getExisteCategoria - Problema SQL (close)", e );
			
			}
		
		}
		
		return mensaje;
	
	}
	
	/**
	 * 
	 * @param idCategoria
	 * @param idProducto
	 * @return
	 * @throws DAOException
	 */
	public boolean getExisteProductoCategoria( String idCategoria, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean existe = false;
			
		try {

			String SQL = "SELECT * FROM FO_PRODUCTOS_CATEGORIAS WHERE PRCA_CAT_ID = ? AND PRCA_PRO_ID = ?";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			stm.setInt( 1, Integer.parseInt( idCategoria ));
			stm.setInt( 2, idProducto );
		    		
		    rs = stm.executeQuery();	
						
			if ( rs.next() ) 					
				existe = true;
			
			
			
		} catch ( SQLException ex ) {
			
			logger.error( "getExisteProductoCategoria - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getExisteProductoCategoria - Problema General", ex );
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
				
				logger.error( "getExisteProductoCategoria - Problema SQL (close)", e );
			
			}
		
		}
		
		return existe;
	
	}
	
	/**
	 * 
	 * @param catalogacionMasiva
	 * @param idProducto
	 * @throws DAOException
	 */
	public boolean setModificarProductoSector( BOCatalogacionMasivaDTO catalogacionMasiva, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "UPDATE BO_PROD_SECTOR SET ID_SECTOR = ? WHERE ID_PRODUCTO = ? ";
			
			stm = conexion.prepareStatement( SQL ); 
			
	    	stm.setInt( 1, Integer.parseInt(catalogacionMasiva.getSectorPicking()) );
	    	stm.setInt( 2, idProducto );
	    	
	    	int i = stm.executeUpdate();
	    	
	    	if(i>0)
	    		result = true;
		
		} catch ( SQLException ex ) {
			
			logger.error( "setModificarProductoSector - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setModificarProductoSector - Problema General", ex );
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
				
				logger.error( "setModificarProductoSector - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	/**
	 * 
	 * @param idCategoria
	 * @param idProducto
	 * @throws DAOException
	 */
	public boolean setModificarProductoCategoria( String idCategoria, int idProducto ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean result = false;
		
		try {
			
			conexion = conexionUtil.getConexion();
			
			String SQL = "UPDATE FO_PRODUCTOS_CATEGORIAS SET PRCA_CAT_ID = ?, PRCA_PRO_ID = ?, PRCA_ESTADO = ?, PRCA_ORDEN = ?, PRCA_CON_PAGO = ?" +
						" WHERE PRCA_CAT_ID = ? AND PRCA_PRO_ID = ? ";
			
			stm = conexion.prepareStatement( SQL ); 
			
			stm.setInt( 1, Integer.parseInt( idCategoria ) );
    		stm.setInt( 2, idProducto );
    		stm.setString( 3, "A" );
    		stm.setInt( 4, 1000 );
    		stm.setString( 5, "S" );
    	
    		stm.setInt( 6, Integer.parseInt( idCategoria ) );
    		stm.setInt( 7, idProducto );
    		
    		int i = stm.executeUpdate();
    		
    		if(i>0)
    			result = true;
    		
		} catch ( SQLException ex ) {
			
			logger.error( "setModificarProductoCategoria - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "setModificarProductoCategoria - Problema General", ex );
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
				
				logger.error( "setModificarProductoCategoria - Problema SQL (close)", e );
			
			}
		
		}
		
		return result;
	
	}
	
	
public int agregaLogProducto(CatalogacionMasivaLogDTO log) throws CatalogacionMasivaDAOException {
		
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
			throw new CatalogacionMasivaDAOException(e);
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


public List getLogByProductId(List productosCatalogacionMasiva) throws CatalogacionMasivaDAOException {
	
	List lst_log = new ArrayList();
	ProductoLogEntity log = null;
	Connection conexion = null;	
	PreparedStatement stm = null;
	ResultSet rs = null;
	try {

		conexion = conexionUtil.getConexion();
		String sql = " SELECT tra_id, tra_pro_id, tra_fec_crea, tra_usuario, tra_texto " +
			" FROM fo_pro_tracking WHERE tra_pro_id = ? and tra_bo_pro_id = ? ORDER BY 3 DESC"  ;
		stm = conexion.prepareStatement(sql  + " WITH UR");
		
		Iterator it = productosCatalogacionMasiva.iterator();
		
	    while ( it.hasNext() ) {

	    	BOCatalogacionMasivaDTO catalogacionMasiva = ( BOCatalogacionMasivaDTO )it.next();
	    
	    	FOProductoDTO productoFO = getFOProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
	    	
	    	BOProductoDTO productoBO = getBoProducto(catalogacionMasiva.getCodigoSap(), catalogacionMasiva.getUnidadMedida());
	    	
	    	
	    	if (productoFO != null)
	    		stm.setLong(1,productoFO.getId());
	    	else
	    		stm.setLong(1,productoBO.getId());
	    	
	    	stm.setLong(2,Long.parseLong( catalogacionMasiva.getCodigoSap() ) );
			
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
		throw new CatalogacionMasivaDAOException(e);
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


public void setTrx(JdbcTransaccion trx)
		throws CatalogacionMasivaDAOException {
			this.trx = trx;
			try {
				conn = trx.getConnection();
			} catch (DAOException e) {
				throw new CatalogacionMasivaDAOException(e);
			}
		}
	
}
