package cl.bbr.boc.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cl.bbr.boc.command.PreciosEdit;
import cl.bbr.boc.dao.StockOnLineDAO;
import cl.bbr.boc.dto.BODetalleSemiautomaticoDTO;
import cl.bbr.boc.dto.BOPreciosLocalesDTO;
import cl.bbr.boc.dto.BOStockONLineDTO;
import cl.bbr.boc.dto.LocalDTO;
import cl.bbr.boc.utils.Constantes;
import cl.bbr.jumbocl.common.model.PrecioLocalEntity;
import cl.bbr.jumbocl.common.model.PrecioSapEntity;
import cl.bbr.jumbocl.common.model.ProductoEntity;
import cl.bbr.jumbocl.contenidos.dto.PreciosSapDTO;
import cl.bbr.jumbocl.contenidos.dto.ProductosSapDTO;
import cl.bbr.jumbocl.shared.conexion.ConexionUtil;
import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.common.dao.jdbc.JdbcDAO;


/**
 * 
 * @author jolazogu
 *
 */
public class JdbcStockOnLineDAO extends JdbcDAO implements StockOnLineDAO {
	
	private static ConexionUtil conexionUtil = new ConexionUtil();
	Logging logger = new Logging(this);
	private static final String SIN_STOCK = "SIN STOCK";
	private static final String CON_STOCK = "CON STOCK";
	private static final String SI = "SI";
	private static final String NO = "NO";
	private static final String EXITO = "EXITO";

	
	public List getListProductosStockOnlinePorLocal( long idLocal, int idEstado ) throws DAOException {
	    
	    List listaConfiguraciones = new ArrayList();
	    Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
	    
		try {
			
			conexion   = conexionUtil.getConexion();
			
			String SQL = "SELECT ID_LOCAL, ID_PRODUCTO, PUBLICAR "+  
						 "FROM BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND ESTADO = ?";
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			stm.setInt( 2, idEstado);
			
			rs = stm.executeQuery();			
			
			while ( rs.next() ) {
				 
				BOStockONLineDTO stockOnline = new BOStockONLineDTO();
				
				stockOnline.setIdLocal( rs.getInt( 1 ) );
				stockOnline.setIdProducto( rs.getInt( 2 ) );
				stockOnline.setPublica( rs.getInt( 3 ) );
				
				listaConfiguraciones.add( stockOnline );
			
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getListProductosStockOnlinePorLocal - Problema SQL: " + ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getListProductosStockOnlinePorLocal - Problema General", ex );
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
			
				logger.error( "getListProductosStockOnlinePorLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return listaConfiguraciones;
	
	}
	   
	 
	public BOPreciosLocalesDTO getPreciosLocalesPorIDLocalYIDProducto( int idLocal, int idProducto ) throws DAOException {
	    
	    Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		BOPreciosLocalesDTO stockOnline = new BOPreciosLocalesDTO();
	    
		try {
			conexion   = conexionUtil.getConexion();
			String SQL = "SELECT PRE_PRO_ID, PRE_TIENESTOCK FROM  FODBA.FO_PRECIOS_LOCALES WHERE PRE_LOC_ID = ? AND PRE_PRO_ID = ?";
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setInt( 1, idLocal );
			stm.setInt( 2, idProducto );
			
			rs = stm.executeQuery();			
			
			if ( rs.next() ) {
				
				stockOnline.setPreProID( rs.getInt( 1 ) );
				stockOnline.setPreTieneStock( rs.getInt( 2 ) );
				
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getPreciosLocalesPorIDLocalYIDProducto - Problema SQL: " + ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getPreciosLocalesPorIDLocalYIDProducto - Problema General", ex );
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
				
				logger.error( "getPreciosLocalesPorIDLocalYIDProducto - Problema SQL (close)", e );
			
			}
		
		}
		
		return stockOnline;
	
	}
	   
 	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public List getListaProductosFoPreciosLocalesPorLocal( long idLocal ) throws DAOException {

		List listaConfiguracionOnLine = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "SELECT BOPRO.COD_PROD1,BOPRO.UNI_MED,CATPRO.DESCAT,CATPRO.ID_CATPROD_PADRE, CATPRO.ID_CATPROD, FOPRELOC.PRE_PRO_ID "+
						"FROM FODBA.FO_PRECIOS_LOCALES FOPRELOC "+
						"INNER JOIN FODBA.FO_PRODUCTOS FPO ON FPO.PRO_ID= FOPRELOC.PRE_PRO_ID "+
                        "INNER JOIN BODBA.BO_PRODUCTOS BOPRO ON BOPRO.ID_PRODUCTO = FPO.PRO_ID_BO "+ 
						"INNER JOIN BO_CATPROD CATPRO ON CATPRO.ID_CATPROD = BOPRO.ID_CATPROD "+
						"WHERE FOPRELOC.PRE_LOC_ID = ? AND FOPRELOC.PRE_ESTADO = ? ";
			
			conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			stm.setString( 2, Constantes.PUBLICADO );
			
			rs = stm.executeQuery();

			List listaCatProd = this.getCatProd();
			
			LocalDTO local = this.getLocal(idLocal);
			
			while ( rs.next() ) {
				
				BOStockONLineDTO skuOnLineDTO = new BOStockONLineDTO();
				BOStockONLineDTO rubroOnLineDTO = new BOStockONLineDTO();
				BOStockONLineDTO subRubroOnLineDTO = new BOStockONLineDTO();
				BOStockONLineDTO seccionOnLineDTO = new BOStockONLineDTO();
								
				skuOnLineDTO.setSkuProducto( rs.getString( 1 ) );
				skuOnLineDTO.setUnidadDeMedida( rs.getString( 2 ) );
				skuOnLineDTO.setNombreProducto( rs.getString( 3 ) );
				skuOnLineDTO.setIdCatProdPadre( rs.getString( 4 ) );
				skuOnLineDTO.setIdCatProd( rs.getString( 5 ) );
				skuOnLineDTO.setIdProducto( rs.getInt( 6 ) );
				
				skuOnLineDTO.setCodLocal( local.getCodigo() );
				skuOnLineDTO.setNombreLocal( local.getNombre() );
				
				subRubroOnLineDTO 		= this.getCatProdSubRubro( listaCatProd, rs.getString( 4 ) );
				rubroOnLineDTO			= this.getCatProdRubro( listaCatProd, subRubroOnLineDTO.getIdCatProdPadre() );
				seccionOnLineDTO		= this.getCatProdSeccion( listaCatProd, rubroOnLineDTO.getIdCatProdPadre() );
					
				skuOnLineDTO.setNombreSeccion( seccionOnLineDTO.getNombreSeccion() );
				skuOnLineDTO.setNombreRubro( rubroOnLineDTO.getNombreRubro() );
				skuOnLineDTO.setNombreSubRubro( subRubroOnLineDTO.getNombreSubRubro() );
					
				listaConfiguracionOnLine.add( skuOnLineDTO );
					
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getListaProductosFoPreciosLocalesPorLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getListaProductosFoPreciosLocalesPorLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} 
		finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getListaProductosFoPreciosLocalesPorLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return listaConfiguracionOnLine;
	
	}
	
	
	public List getListaProductosBoStockOnLinePorLocal( long idLocal ) throws DAOException {

		List listaConfiguracionOnLine = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "SELECT ID_CATPROD, ID_PRODUCTO, ESTADO, STOCK_MINIMO, MODO, COD_PROD1 " +
					"FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? ";
			
			conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			rs = stm.executeQuery();

			while ( rs.next() ) {
				
				BOStockONLineDTO skuOnLineDTO = new BOStockONLineDTO();
					
				skuOnLineDTO.setIdCatProd( rs.getString( 1 ) );
				skuOnLineDTO.setIdProducto( rs.getInt( 2 ) );
				skuOnLineDTO.setEstado( rs.getInt( 3 ) );
				skuOnLineDTO.setStockMinimo( rs.getInt( 4 ) );
				skuOnLineDTO.setModo( rs.getInt( 5 ) );
				skuOnLineDTO.setSkuProducto( rs.getString( 6 ) );
					
				listaConfiguracionOnLine.add( skuOnLineDTO );
					
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getListaProductosBoStockOnLinePorLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getListaProductosBoStockOnLinePorLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} 
		finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getListaProductosBoStockOnLinePorLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return listaConfiguracionOnLine;
	
	}
	
	/**
	 * 
	 * @param listaCatProd
	 * @param idCatProd
	 * @return
	 */
	private BOStockONLineDTO getCatProdSeccion( List listaCatProd, String idCatProd ) {
		
		Iterator it = listaCatProd.iterator();
			
		while ( it.hasNext() ) {
            
			BOStockONLineDTO i = ( BOStockONLineDTO )it.next();
			
			if( i.getIdSeccion() != null) {
				if( i.getIdSeccion() != null && i.getIdSeccion().equals( idCatProd ) ) {
						
					return i;
					
				}
			}
				
		}
		
		return new BOStockONLineDTO();
		
	}
	
	/**
	 * 
	 * @param listaCatProd
	 * @param idCatProd
	 * @return
	 */
	private BOStockONLineDTO getCatProdRubro( List listaCatProd, String idCatProd ) {
		
		Iterator it = listaCatProd.iterator();
			
		while ( it.hasNext() ) {
            
			BOStockONLineDTO i = ( BOStockONLineDTO )it.next();
				
			if( i.getIdRubro() != null ) {
				if( i.getIdRubro().equals( idCatProd ) ) {
						
					return i;
					
				}
			}
				
		}
		
		return new BOStockONLineDTO();
		
	}
	
	/**
	 * 
	 * @param listaCatProd
	 * @param idCatProd
	 * @return
	 */
	private BOStockONLineDTO getCatProdSubRubro( List listaCatProd, String idCatProd ) {
		
		Iterator it = listaCatProd.iterator();
			
		while ( it.hasNext() ) {
	        
			BOStockONLineDTO i = ( BOStockONLineDTO )it.next();
			
			if(i.getIdSubRubro() != null ) {
			
				if( i.getIdSubRubro().equals( idCatProd ) ) {
				
					return i;
					
				}
			}
				
		}
		
		return new BOStockONLineDTO();
		
	}


	/**
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List getCatProd() throws DAOException {

		Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		List				listaCatProd = new ArrayList(); 
		
		try {

			String SQL = "SELECT ID_CATPROD,CAT_NIVEL,ID_CATPROD_PADRE,DESCAT FROM BODBA.BO_CATPROD ";
			
			conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			
			rs = stm.executeQuery();

			while( rs.next() ) {
				
				BOStockONLineDTO stockOnLineDTO = new BOStockONLineDTO();
				
				stockOnLineDTO.setCatNivel( rs.getInt( 2 ) );
				stockOnLineDTO.setIdCatProdPadre( rs.getString( 3 ) );				
				stockOnLineDTO.setSkuProducto( rs.getString( 1 ) );
				stockOnLineDTO.setNombreProducto( rs.getString( 4 ) );
				stockOnLineDTO.setIdSubRubro( rs.getString( 1 ) );
				stockOnLineDTO.setNombreSubRubro( rs.getString( 4 ) );
				stockOnLineDTO.setIdRubro( rs.getString( 1 ) );
				stockOnLineDTO.setNombreRubro( rs.getString( 4 ) );
				stockOnLineDTO.setIdSeccion( rs.getString( 1 ) );
				stockOnLineDTO.setNombreSeccion( rs.getString( 4 ) );
				stockOnLineDTO.setIdGerencia( rs.getString( 1 ) );
				stockOnLineDTO.setNombreGerencia( rs.getString( 4 ) );
				
				listaCatProd.add( stockOnLineDTO );
			
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getCatProd - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getCatProd - Problema General", ex );
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
			
				logger.error( "getCatProd - Problema SQL (close)", e );
			
			}
		
		}
		
		return listaCatProd;
	
	}
	
	public LocalDTO getLocal(long idLocal) throws DAOException {

		Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		LocalDTO local = null;
		
		try {

			// seccion catnivel 1 , rubro 2, subrubro 3 , sku
			String SQL = "SELECT COD_LOCAL,NOM_LOCAL FROM BO_LOCALES where ID_LOCAL = ? ";
			//BOStockONLineDTO
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement(SQL + " WITH UR");
			stm.setLong(1, idLocal);
			rs = stm.executeQuery();

				if(rs.next()) {
				
					local = new LocalDTO();
					
					local.setCodigo(rs.getString(1));
					local.setNombre(rs.getString(2));
				
			}
			
		} catch (SQLException ex) {
			logger.error("getConfiguracionStockOnLineSeccion - Problema SQL", ex);
			throw new DAOException(ex);
		} catch (Exception ex) {
			logger.error("getConfiguracionStockOnLineSeccion - Problema General", ex);
			throw new DAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getConfiguracionStockOnLineSeccion - Problema SQL (close)", e);
			}
		}
		return local;
	}
	
	
	public long[] cantidadDeProductosTendranCambios( long idLocal, List productos ) throws DAOException {             
		
		Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		long cantidadQuedaranSinStock = 0;
		long cantidadQuedaranConStock = 0;
		long cantidadQuedaranSinStockN = 0;
		long cantidadQuedaranConStockN = 0;
		long cantidades[] = new long[ 5 ];
		cantidades[ 4 ] = 0;
		
		try {
			
			String SQL = "SELECT ID_PRODUCTO, PUBLICAR FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND CONFIRMAR = 1 AND ESTADO = 0";
		    
			conexion = conexionUtil.getConexion();
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			Iterator it = productos.iterator();
			
			while ( it.hasNext() ) {
			
				BOPreciosLocalesDTO pl = ( BOPreciosLocalesDTO )it.next();
				
				rs = stm.executeQuery();
				
				while ( rs.next() ) {
				
					if( pl.getPreProID() == rs.getInt( 1 ) ) {
						
						if ( rs.getInt(2) == 1 && pl.getPreTieneStock() == 0 ) {
							
							cantidadQuedaranConStock++;
							cantidadQuedaranSinStockN++;
						
						} else if ( rs.getInt(2) == 0 && pl.getPreTieneStock() == 1 ){
						
							cantidadQuedaranSinStock++;
							cantidadQuedaranConStockN++;
						
						} 
						
					}
					
					cantidades[ 4 ] = 1;
					
				}
			}
			
			cantidades[ 0 ] = cantidadQuedaranConStock;
			cantidades[ 1 ] = cantidadQuedaranSinStock;
			cantidades[ 2 ] = cantidadQuedaranConStockN;
			cantidades[ 3 ] = cantidadQuedaranSinStockN;
			 
			 
		} catch ( Exception ex ) { 
			
			logger.error( "Error cantidadDeProductosTendranCambios: ", ex );
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
				
				logger.error( "cantidadDeProductosTendranCambios - Problema SQL (close)", e );
			
			}
		
		}
		
		return cantidades;
	
	}
	

	
	public List getProductosPorLocal( long idLocal ) throws DAOException {

		List productos = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "SELECT PRE_PRO_ID, PRE_TIENESTOCK FROM FO_PRECIOS_LOCALES "+
						 "WHERE PRE_LOC_ID = ? AND " +
						 "PRE_PRO_ID IN ( SELECT ID_PRODUCTO FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND CONFIRMAR = 1 and ESTADO = 0 )";
			
			conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			stm.setLong( 2, idLocal );
			
			rs = stm.executeQuery();

			while ( rs.next() ) {
				
				BOPreciosLocalesDTO pl = new BOPreciosLocalesDTO();
				
				pl.setPreProID( rs.getInt( 1 ) );
				pl.setPreTieneStock( rs.getInt( 2 ) );
				
				productos.add(pl);
					
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getProductosPorLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getProductosPorLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} 
		finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getProductosPorLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return productos;
	
	}
	
	
	
	public long[] cantidadDeProductosActualmente( long idLocal ) throws DAOException {             
		
		Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		long cantidadSinStock = 0;
		long cantidadConStock = 0;
		long cantidadDespublicados = 0;
		long cantidades[] = new long[ 3 ];
		
		try {
			
			String SQL = "SELECT PRE_TIENESTOCK, PRE_ESTADO FROM FO_PRECIOS_LOCALES WHERE PRE_LOC_ID = ? ";
		    
			conexion = conexionUtil.getConexion();
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			rs = stm.executeQuery();			
			
			while ( rs.next() ) {
				
				if( rs.getString( 2 ).equals( "A" ) ) {
					
					if ( rs.getInt( 1 ) == 1 ) {
					
						cantidadConStock++;
					
					}else if ( rs.getInt( 1 ) == 0 ) {
						
						cantidadSinStock++;
						
					}
						
				}else if ( rs.getString( 2 ).equals( "D" ) ) {
					
					cantidadDespublicados++;
					
				}
					
			} 
			
			cantidades[ 0 ] = cantidadSinStock;
			cantidades[ 1 ] = cantidadConStock;
			cantidades[ 2 ] = cantidadDespublicados;
			 
			 
			 
		} catch ( Exception ex ) { 
			
			logger.error( "Error cantidadDeProductosActualmente: ", ex );
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
				
				logger.error( "cantidadDeProductosTendranCambios - Problema SQL (close)", e );
			
			}
		
		}
		
		return cantidades;
	
	}
	
	

	public int getTotalMaestra( long idLocal ) throws DAOException {             
		
		Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		int total = 0;
		
		try {
			
			String SQL = "SELECT TOTAL_MAESTRA FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND CONFIRMAR = 1 GROUP BY TOTAL_MAESTRA";
		    
			conexion = conexionUtil.getConexion();
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			rs = stm.executeQuery();			
			
			if ( rs.next() ) {
				
				total = rs.getInt( 1 );
					
			} 	
		
		} catch ( Exception ex ) { 
			
			logger.error( "Error cantidadDeProductosActualmente: ", ex );
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
				
				logger.error( "cantidadDeProductosTendranCambios - Problema SQL (close)", e );
			
			}
		
		}
		
		return total;
	
	}
	
	public List getDetalleSemiautomatico( long idLocal ) throws DAOException {
		
		List listaDetalleSemiautomatico = new ArrayList();
		
		try {
			
			List lista = this.getListProductosStockOnlinePorLocal( idLocal, Constantes.CONSTANTE_ESTADO_INT_SEMIAUTOMATICO );
			 
			logger.info( "Total de productos SemiAutomatico " +lista.size() );
			 
			for( int i=0; lista.size() > i ; i++ ) {
				
				BOStockONLineDTO configuracionStock = ( BOStockONLineDTO ) lista.get( i );
				
	        	BOPreciosLocalesDTO producto = getPreciosLocalesPorIDLocalYIDProducto( configuracionStock.getIdLocal(), configuracionStock.getIdProducto() );
	        			
	        			BODetalleSemiautomaticoDTO detalleSemiautomatico = new BODetalleSemiautomaticoDTO();
		        		
	        			cargarDetalleSemiautomatico( detalleSemiautomatico,producto );
	        			
	        			if( configuracionStock.getPublica() == 0 && producto.getPreTieneStock() == 1 ) {
		        			
		        			detalleSemiautomatico.setEstadoInicial( CON_STOCK );
		        			detalleSemiautomatico.setEstadoFinal( "<b>" +SIN_STOCK+ "</b>" );
		        			detalleSemiautomatico.setCambioEstado( SI );
		        			
		        			listaDetalleSemiautomatico.add( detalleSemiautomatico );
		        				
		        		} else if( configuracionStock.getPublica() == 1 && producto.getPreTieneStock() == 0 ) {
		        			
		        			detalleSemiautomatico.setEstadoInicial( SIN_STOCK );
		        			detalleSemiautomatico.setEstadoFinal( "<b>" +CON_STOCK+ "</b>" );
		        			detalleSemiautomatico.setCambioEstado( SI );
		        			
		        			listaDetalleSemiautomatico.add( detalleSemiautomatico );
		        		
		        		} else if( configuracionStock.getPublica() == producto.getPreTieneStock() ) {
		        			
		        			if( producto.getPreTieneStock() == 0 ) {
		        			
		        				detalleSemiautomatico.setEstadoInicial( SIN_STOCK );
			        			detalleSemiautomatico.setEstadoFinal( SIN_STOCK );
			        			
		        			} else {
		        				
		        				detalleSemiautomatico.setEstadoInicial( CON_STOCK );
			        			detalleSemiautomatico.setEstadoFinal( CON_STOCK );
			        		
		        			}
		        			
		        			detalleSemiautomatico.setCambioEstado( NO );
		        			
		        			listaDetalleSemiautomatico.add( detalleSemiautomatico );
		        			
		        		}
	        			
	        }
		
		} catch ( Exception ex ) { 
			
			throw new DAOException( ex );
		
		}
		
		return listaDetalleSemiautomatico;
	
	}

	
	

	public List getProductosDetalleSemiautomatico( long idLocal, List productos ) throws DAOException {
		
	
		Connection 			conexion = null;
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		BOStockONLineDTO stockOnline = null;
		List productosDetalleSemiautomatico = new ArrayList();
		
		try {
		
			String SQL = "SELECT ID_PRODUCTO, PUBLICAR, COD_PROD1, STOCK_MINIMO FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND CONFIRMAR = 1 AND ESTADO = 0";
	    
			conexion = conexionUtil.getConexion();
		
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
		
		Iterator it = productos.iterator();
		
		while ( it.hasNext() ) {
		
			BOPreciosLocalesDTO pl = ( BOPreciosLocalesDTO )it.next();
			
			rs = stm.executeQuery();
			
			while ( rs.next() ) {
				
				
				if( pl.getPreProID() == rs.getInt( 1 ) ) {
				
					stockOnline = new BOStockONLineDTO();
					
					stockOnline.setSkuProducto( rs.getString( 3 ) );
					stockOnline.setStockReal(pl.getPreStock());
					stockOnline.setStockMinimo( rs.getInt( 4 ) );
					stockOnline.setIdProducto( pl.getPreProID() );
					stockOnline.setEstado( 0 );
					
					if ( rs.getInt( 2 ) == 1 && pl.getPreTieneStock() == 0 ) {
						
						stockOnline.setEstadoInicial( SIN_STOCK );
						stockOnline.setEstadoFinal( CON_STOCK );
						stockOnline.setResultadoTransaccion( EXITO );
						
						productosDetalleSemiautomatico.add(stockOnline);
						
					} else if ( rs.getInt( 2 ) == 0 && pl.getPreTieneStock() == 1 ) {
						
						stockOnline.setEstadoInicial( CON_STOCK );
						stockOnline.setEstadoFinal( SIN_STOCK );
						stockOnline.setResultadoTransaccion( EXITO );
					
						productosDetalleSemiautomatico.add(stockOnline);
						
					}
					
				}
				
			}
		
		}
		 
	} catch ( Exception ex ) { 
		
		logger.error( "Error cantidadDeProductosTendranCambios: ", ex );
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
			
			logger.error( "cantidadDeProductosTendranCambios - Problema SQL (close)", e );
		
		}
	
	}
	
		return productosDetalleSemiautomatico;
	
	}
	
	/**
	 * 
	 * @param detalleSemiautomatico
	 * @param producto
	 * @throws DAOException
	 */
	private void cargarDetalleSemiautomatico( BODetalleSemiautomaticoDTO detalleSemiautomatico, BOPreciosLocalesDTO producto ) throws DAOException {
	    
		BOStockONLineDTO boStockONLineDTO = getProductoPorIdProducto( producto.getPreProID() );
	    
		detalleSemiautomatico.setSku( boStockONLineDTO.getSkuProducto() );
	    
	    detalleSemiautomatico.setRubro( boStockONLineDTO.getNombreRubro() );
		
		detalleSemiautomatico.setSeccion( boStockONLineDTO.getNombreSeccion() );
		
		detalleSemiautomatico.setSubrubro( boStockONLineDTO.getNombreSubRubro() );
		 
	}
	
	/**
	 * 
	 * @param idProduct
	 * @return
	 * @throws DAOException
	 */
	private ProductosSapDTO getProductoByIdProducto( int idProducto ) throws DAOException{
		
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		ProductosSapDTO producto = null;
		 
		try {

			String SQL = "SELECT bp.COD_PROD1, bp.ID_CATPROD "+
						"FROM FO_PRODUCTOS fp "+
						"inner join BO_PRODUCTOS bp on bp.ID_PRODUCTO = fp.PRO_ID_BO "+
						"WHERE fp.PRO_ID = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setInt( 1, idProducto );
			
			rs = stm.executeQuery();			
			
			if ( rs.next() ){
				
				producto = new ProductosSapDTO();
				
				producto.setCod_prod_1( rs.getString( 1 ) );
				producto.setId_cat( rs.getString( 2 ) );
			
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getProductoByIdProducto - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getProductoByIdProducto - Problema General", ex );
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
				
				logger.error("getProductoByIdProducto - Problema SQL (close)", e);
			
			}
		
		}
		
		return producto;
	
	}
	
	
	
	/**
	 * 
	 * @param preProID
	 * @return
	 * @throws DAOException
	 */
	private BOStockONLineDTO getProductoPorIdProducto( int preProID ) throws DAOException {
		
		List listaCatProd = this.getCatProd();
		
		ProductosSapDTO producto = this.getProductoByIdProducto( preProID );
		
			BOStockONLineDTO skuOnLineDTO		= new BOStockONLineDTO();
			BOStockONLineDTO rubroOnLineDTO 	= new BOStockONLineDTO();
			BOStockONLineDTO subRubroOnLineDTO	= new BOStockONLineDTO();
			BOStockONLineDTO seccionOnLineDTO	= new BOStockONLineDTO();
			
			skuOnLineDTO.setIdProducto( preProID );
			skuOnLineDTO.setSkuProducto( producto.getCod_prod_1() );
			skuOnLineDTO.setIdCatProdPadre( producto.getId_cat() );
			
			subRubroOnLineDTO 	= this.getCatProdSubRubro( listaCatProd, skuOnLineDTO.getIdCatProdPadre() );
			rubroOnLineDTO 		= this.getCatProdRubro( listaCatProd, subRubroOnLineDTO.getIdCatProdPadre() );
			seccionOnLineDTO 	= this.getCatProdSeccion( listaCatProd, rubroOnLineDTO.getIdCatProdPadre() );
			
			skuOnLineDTO.setNombreSeccion( seccionOnLineDTO.getNombreSeccion() );
			skuOnLineDTO.setNombreRubro( rubroOnLineDTO.getNombreRubro() );
			skuOnLineDTO.setNombreSubRubro( subRubroOnLineDTO.getNombreSubRubro() );

			return skuOnLineDTO;
		
	}
	
	
	/**
	 * 
	 * @param idLocal
	 * @throws DAOException
	 */
	public void getLimpiarTablaStockOnlinePorLocal( long idLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "DELETE FROM BO_STOCK_ONLINE WHERE ID_LOCAL = ?";
			
		    conexion = conexionUtil.getConexion();
			conexion.setAutoCommit(false);
		    
		    stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			stm.executeUpdate();
			
		} catch ( SQLException ex ) {
			
			try {
				
				conexion.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			ex.printStackTrace();
			
			logger.error( "getLimpiarTablaStockOnlinePorLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			try {
			
				conexion.rollback();
            
			} catch (SQLException e1) {
                
            	e1.printStackTrace();
            }
            
			ex.printStackTrace();
			
			logger.error( "getLimpiarTablaStockOnlinePorLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				conexion.commit();
				conexion.setAutoCommit(true);
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getLimpiarTablaStockOnlinePorLocal - Problema SQL (close)", e );
				
			}
		
		}
	
	}
	
	
	/**
	 * 
	 * @param idLocal
	 * @throws DAOException
	 */
	public void getConfirmarSemiautomaticoStockOnlinePorLocal( long idLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "UPDATE BO_STOCK_ONLINE SET CONFIRMAR = 2 WHERE ID_LOCAL = ? AND ESTADO = 0";
			
		    conexion = conexionUtil.getConexion();
		    conexion.setAutoCommit(false);
			
		    stm = conexion.prepareStatement( SQL );
			stm.setLong( 1, idLocal );
			
			stm.executeUpdate();
			
			
		} catch ( SQLException ex ) {
			
			try {
				
				conexion.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			ex.printStackTrace();
			
			logger.error( "getLimpiarTablaStockOnlinePorLocal - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			try {
				
				conexion.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			ex.printStackTrace();
			logger.error( "getLimpiarTablaStockOnlinePorLocal - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				conexion.commit();
				conexion.setAutoCommit(true);
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "getLimpiarTablaStockOnlinePorLocal - Problema SQL (close)", e );
				
			}
		
		}
	
	}
	
	/**
	 * 
	 * @param archivoFiltrado
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public boolean setInsertRegistroExcelToBD( List archivoFiltrado, long idLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean cargaCompleta = false;
		ProductosSapDTO prodSap = new ProductosSapDTO();
		
		try {

			String SQL = "INSERT INTO BODBA.BO_STOCK_ONLINE(ID_CATPROD, ID_LOCAL, ID_PRODUCTO, ESTADO, STOCK_MINIMO, MODO, COD_PROD1, TOTAL_MAESTRA) " +
					     "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ";
			
			
		    conexion = conexionUtil.getConexion();
		    conexion.setAutoCommit(false);
		    
		    stm = conexion.prepareStatement( SQL );
			
			Iterator itAF = archivoFiltrado.iterator();
			
			logger.info( "Inicio de cargarArchivoStockOnLine" );
			
			while ( itAF.hasNext() ) {				
				
				BOStockONLineDTO stockOnLine = ( BOStockONLineDTO ) itAF.next();
				
				prodSap = this.getProductoPorSku( stockOnLine );
				
				if (prodSap != null) {
				
					stm.setString( 1, prodSap.getId_cat() );
					stm.setLong( 2, idLocal );
					stm.setLong( 3, prodSap.getId() );
					stm.setInt( 4, stockOnLine.getEstado() );
					stm.setInt( 5, stockOnLine.getStockMinimo() );
					stm.setInt( 6, stockOnLine.getModo() );	
					stm.setString( 7, stockOnLine.getSkuProducto() );
					stm.setInt( 8, stockOnLine.getTotalMaestra() );
					
					stm.executeUpdate();
					
				}else {
					
					logger.info("Producto SKU : " +stockOnLine.getSkuProducto() + "...No ha sido agregado al mix.");
					
				}
				
				
			}
			
			logger.info( "Fin de cargarArchivoStockOnLine" );
			cargaCompleta = true;
			
		} catch ( SQLException ex ) {
			
			try {
	            
				conexion.rollback();
	        
			} catch (SQLException e1) {
	        
				e1.printStackTrace();
	        
			}
	        
			ex.printStackTrace();
	        logger.error( "setInsertRegistroExcelToBD - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			try {
	        
				conexion.rollback();
	        
			} catch (SQLException e1) {
	        	
	        	e1.printStackTrace();
	        }
	        
			ex.printStackTrace();
			logger.error( "setInsertRegistroExcelToBD - Problema General", ex );
			throw new DAOException( ex );
		
		} finally {
			
			try {
				
				conexion.commit();
				conexion.setAutoCommit(true);
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
				
				logger.error( "setInsertRegistroExcelToBD - Problema SQL (close)", e );
			
			}
		
		}
		
		return cargaCompleta;
	
	}

	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public boolean setEjecutaProcesoSemiAutomatico( long idLocal ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean cargaCompleta = false;
		
		try {

			String SQL = "UPDATE FO_PRECIOS_LOCALES "+
						"SET PRE_TIENESTOCK = (SELECT PUBLICAR FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND ESTADO = 0 AND ID_PRODUCTO = PRE_PRO_ID) "+
						"WHERE PRE_PRO_ID IN (SELECT ID_PRODUCTO FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND ESTADO = 0) AND PRE_LOC_ID = ? ";
			
		    conexion = conexionUtil.getConexion();
		    conexion.setAutoCommit(false);
		    
		    stm = conexion.prepareStatement( SQL );
			stm.setLong( 1, idLocal );
			stm.setLong( 2, idLocal );
			stm.setLong( 3, idLocal );
			
			stm.executeUpdate();
			
			cargaCompleta = true;
			
		} catch ( SQLException ex ) {
			

			try {
				
				conexion.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
			
			ex.printStackTrace();
			
			logger.error( "setEjecutaProcesoSemiAutomatico - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			try {
				
				conexion.rollback();
            
			} catch (SQLException e1) {
                
            	e1.printStackTrace();
            }
            
			ex.printStackTrace();
			
			logger.error( "setEjecutaProcesoSemiAutomatico - Problema General", ex );
			throw new DAOException( ex );
			
		} finally {
			
			try {
			
				conexion.commit();
				conexion.setAutoCommit(true);
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
				if ( conexion != null && !conexion.isClosed() )
					conexion.close();
			
			} catch ( SQLException e ) {
			
				logger.error( "setEjecutaProcesoSemiAutomatico - Problema SQL (close)", e );
			
			}
		
		}
		
		return cargaCompleta;
	
	}

	/**
	 * 
	 * @param codProd1
	 * @return
	 * @throws DAOException
	 */
	public ProductosSapDTO getProductoPorSku( BOStockONLineDTO stockOnLine ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		ProductosSapDTO producto = null;
		
		try {

			String SQL = "SELECT fp.PRO_ID, bp.ID_CATPROD "+
						"FROM FO_PRODUCTOS fp "+
						"inner join BO_PRODUCTOS bp on bp.ID_PRODUCTO = fp.PRO_ID_BO "+
						"WHERE fp.PRO_COD_SAP = ? AND UNI_MED = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString(1, stockOnLine.getSkuProducto());
			stm.setString(2, stockOnLine.getUnidadDeMedida());
			
			rs = stm.executeQuery();	
			
			if ( rs.next() ) {				
				
				producto = new ProductosSapDTO();
				
				producto.setId( rs.getLong( 1 ) );
				producto.setId_cat( rs.getString( 2 ) );
			
			}
			
		} catch ( SQLException ex ) {
			
			logger.error( "getProductoPorSku - Problema SQL", ex );
			throw new DAOException( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getProductoPorSku - Problema General", ex );
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
				
				logger.error( "getProductoPorSku - Problema SQL (close)", e );
			
			}
		
		}
		
		return producto;
	
	}
	
	/**
	 * 
	 * @param cod_prod1
	 * @param uniMed
	 * @return
	 * @throws DAOException
	 */
	public boolean getProductoBySkuUniMed( String cod_prod1, String uniMed ) throws DAOException {

		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		boolean skuUniMed = false;
		
		try {

			String SQL = "SELECT * FROM BO_PRODUCTOS WHERE COD_PROD1 = ? and UNI_MED = ? ";
			
		    conexion = conexionUtil.getConexion();
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setString( 1, cod_prod1 );
			stm.setString( 2, uniMed );
			
			rs = stm.executeQuery();	
			
			if ( rs.next() ) {				
				
				return skuUniMed = true;
			
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
		
		return skuUniMed;
	
	}


	public long[] cantidadDeProductosTendranCambios(long idLocal)
			throws DAOException {
		// TODO Apéndice de método generado automáticamente
		return null;
	}


	


}
