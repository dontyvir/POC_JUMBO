package cl.cencosud.procesos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import cl.bbr.jumbocl.shared.exceptions.DAOException;
import cl.bbr.jumbocl.shared.transaccion.JdbcTransaccion;
import cl.cencosud.beans.Local;
import cl.cencosud.beans.StockOnLine;
import cl.cencosud.util.Constantes;
import cl.cencosud.util.Logging;

/**
 * 
 * @author jolazogu
 *
 */
public class ModificarIndicadorDeStock {
	
	private static Logging logger = new Logging(ModificarIndicadorDeStock.class);
	
	/**
	 * 
	 * @throws Exception
	 */
	public static void ejecutar() throws Exception {

		logger.debug( "Inicio del proceso ModificarIndicadorDeStock " );
		
		Connection con = DbCarga.conexion( Constantes.user, Constantes.password, Constantes.driver, Constantes.url );
		
		long ini = System.currentTimeMillis();
		Local local = null;
		
		List resumenStockOnLine = new ArrayList();
		List productosModificados = new ArrayList();
		
		JdbcTransaccion trx = new JdbcTransaccion();
		
		try {
			
			trx.begin();
			
			con.setAutoCommit(false);
			
			List locales = getLocales( con );
			
			if(locales != null)
				logger.info("Locales : " +locales.size());
			
			for ( int i = 0; i < locales.size(); i++ ) {
			
				long[] cantidadesActualmente = { 0, 0, 0, 0 };
				long[] cantidadesDespues = { 0, 0, 0, 0, 0, 0 };
				
				local = ( Local ) locales.get(i);
				
				String archivoSST = getNombreArchivoSSTPorLocal( local.getCodigo() );
				
				if ( !"".equals( archivoSST.trim() ) ) {
				
					cantidadesActualmente = cantidadDeProductosActualmente( con, local.getId() );
				   
					String values[][] = cargarCSV( archivoSST );

					List productosStockOnline = getProductosStockOnlinePorLocal( con, local.getId() );
					
					boolean ejecutoCrontab = false;
					List productosCambiados = new ArrayList(); 
					
					for ( int j = 0; j < productosStockOnline.size(); j++ ) {
						
						StockOnLine stockOnline = ( StockOnLine ) productosStockOnline.get( j );

						int stockReal = getStockReal( values, stockOnline.getSkuProducto() );
						
						if ( stockReal != Constantes.CONSTANTE_INT_ERROR && stockReal != Constantes.CONSTANTE_NO_ENCONTRADO ) {
							
							ejecutoCrontab = true;
							boolean marcado = false;
							
							if ( stockReal < stockOnline.getStockMinimo() ) {

								if ( stockOnline.getEstado() == Constantes.CONSTANTE_MODO_INT_ON ) {
								
									marcado = marcarSinStockAutomatico( con, local.getId(), stockOnline.getIdProducto() );
									
									stockOnline.setPublicarSinStock(marcado);
									productosCambiados.add(stockOnline);
									
								} else if ( stockOnline.getEstado() == Constantes.CONSTANTE_MODO_INT_OFF ) {
								
									marcarSinStockSemiAutomatico( con, local.getId(),stockOnline.getIdProducto(),stockOnline.getSkuProducto() );
								
								}
								
								if ( marcado ) {
									
									productosModificados.add(guardarModificacion( local.getNombre(), stockOnline.getSkuProducto(), stockReal, stockOnline.getStockMinimo(), stockOnline.getIdProducto(),
														Constantes.CON_STOCK, Constantes.SIN_STOCK, Constantes.EXITO, stockOnline.getEstado() ));
									
								} 
								
							} else {
								
								if ( stockOnline.getEstado() == Constantes.CONSTANTE_MODO_INT_ON ) {
								
									marcado = marcarConStockAutomatico( con, local.getId(), stockOnline.getIdProducto() );
									
									stockOnline.setPublicarConStock(marcado);
									productosCambiados.add(stockOnline);
									
								} else if ( stockOnline.getEstado() == Constantes.CONSTANTE_MODO_INT_OFF ) {
								
									marcarConStockSemiAutomatico( con, local.getId(), stockOnline.getIdProducto(), stockOnline.getSkuProducto() );
								
								}
								
								if ( marcado ) {
									
									productosModificados.add(guardarModificacion( local.getNombre(), stockOnline.getSkuProducto(), stockReal, stockOnline.getStockMinimo(),	stockOnline.getIdProducto(),
											Constantes.SIN_STOCK, Constantes.CON_STOCK, Constantes.EXITO, stockOnline.getEstado() ));
								
								} 
								
							}
							
						}

					}

					if( ejecutoCrontab ) {
						
						marcarConfirmarCrontab( con, local.getId() );
						
						cantidadesDespues = cantidadDeProductosTendranCambios( con, local.getId(), productosCambiados  );
						
						resumenStockOnLine.add(listaResumenPorLocal( con, local, cantidadesActualmente, cantidadesDespues));
						
						//limpiarTablaStockOnlinePorLocalEstadoAutomatico(con, local.getId());
						 
					}
					
				}
			
				
			}
			
			eliminarArchivo();
			
			generaExcel( productosModificados );
			
			enviarMailStockOnline( resumenStockOnLine );
			
			long fin = System.currentTimeMillis();
			
			logger.debug( "tiempo: " + ( fin - ini ) );
			
			eliminarArchivo();
			
			logger.debug( "Fin del proceso de ModificarIndicadorDeStock" );
		
		} catch ( Exception e ) {
			
			con.rollback();
			
			e.printStackTrace();
			logger.error( "ocurrieron problemas al procesar: " + e );
		
		}
		finally {
			
			try {
				
				con.commit();
				con.setAutoCommit(true);
				trx.end();
				
				if ( con != null )
					con.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "ModificarIndicadorDeStock - Problema SQL (close)", e );
			
			}
		
		}
		
	}
	
	
	private static void limpiarTablaStockOnlinePorLocalEstadoAutomatico( Connection conexion, int idLocal ) {
		
		PreparedStatement stm = null;
	
		try {

			String SQL = "DELETE FROM BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND ESTADO = 1 AND CONFIRMAR = 1 ";
			
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			stm.executeUpdate();

		} catch ( SQLException ex ) {
			
			logger.error( "limpiarTablaStockOnlinePorLocalEstadoAutomatico - Problema SQL", ex );
			
		} finally {
			
			try {
				
				if ( stm != null )
					stm.close();
				
			
			} catch ( SQLException e ) {
				
				logger.error( "limpiarTablaStockOnlinePorLocalEstadoAutomatico - Problema SQL (close)", e );
			
			}
		
		}
		
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private static List getLocales( Connection conexion ) throws Exception {

		PreparedStatement stm = null;
		ResultSet rs = null;
		List locales = new ArrayList();
		
		try {

			String SQL = "SELECT bl.COD_LOCAL, bl.ID_LOCAL, bl.NOM_LOCAL "+ 
						 "FROM BODBA.BO_STOCK_ONLINE bso "+
						 "INNER JOIN BODBA.BO_LOCALES bl ON bl.ID_LOCAL = bso.ID_LOCAL "+
						 "GROUP BY bl.COD_LOCAL, bl.ID_LOCAL, bl.NOM_LOCAL ";

			stm = conexion.prepareStatement( SQL + " WITH UR" );
			rs = stm.executeQuery();
			
			Local local = null;

			while( rs.next() ) {
				
				local = new Local();
				
				local.setCodigo( rs.getString( 1 ) );
				local.setId( rs.getInt( 2 ) );
				local.setNombre( rs.getString( 3 ) );
				
				locales.add( local );
				
			}

		} catch ( SQLException ex ) {
			
			logger.error( "getLocales - Problema SQL", ex );
			throw new Exception( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getLocales - Problema General", ex );
			throw new Exception( ex );
		
		} finally {
			
			try {
			
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
			
			} catch ( SQLException e ) {
				
				logger.error( "getLocales - Problema SQL (close)", e );
			
			}
		
		}
		
		return locales;
	
	}
	
	
	/**
	 * 
	 * @param codigoLocal
	 * @return
	 */
	private static String getNombreArchivoSSTPorLocal( String codigoLocal ) {

		String prefijo = "SST";
		String prefijoNombreArchivo = prefijo + codigoLocal;

		String archivo = Archivo.nombreArchivoCSV( null, prefijoNombreArchivo, "CSV", null);

		return archivo;

	}

	

	/**
	 * 
	 * @param con
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public static long[] cantidadDeProductosActualmente( Connection con, int idLocal ) throws Exception {             
		
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		long cantidadSinStock = 0;
		long cantidadConStock = 0;
		long cantidadDespublicados = 0;
		long cantidades[] = new long[ 3 ];
		
		try {
			
			String SQL = "SELECT PRE_TIENESTOCK, PRE_ESTADO FROM FO_PRECIOS_LOCALES WHERE PRE_LOC_ID = ? ";
		    
			stm = con.prepareStatement( SQL + " WITH UR" );
			stm.setInt( 1, idLocal );
			
			rs = stm.executeQuery();			
			
			while ( rs.next() ) {
				
				if( rs.getString( 2 ).equals( Constantes.PUBLICADO ) ) {
					
					if ( rs.getInt( 1 ) == 1 ) {
					
						cantidadConStock++;
					
					}else if ( rs.getInt( 1 ) == 0 ) {
						
						cantidadSinStock++;
						
					}
						
				}else if ( rs.getString( 2 ).equals( Constantes.DESPUBLICADO ) ) {
					
					cantidadDespublicados++;
					
				}
					
			} 
			
			cantidades[ 0 ] = cantidadSinStock;
			cantidades[ 1 ] = cantidadConStock;
			cantidades[ 2 ] = cantidadDespublicados;
			 
			 
		} catch ( Exception ex ) { 
			
			logger.error( "Error cantidadDeProductosActualmente: ", ex );
			throw new Exception( ex );
			
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
			
			} catch ( SQLException e ) {
				
				logger.error( "cantidadDeProductosActualmente - Problema SQL (close)", e );
			
			}
		
		
		}
		
		return cantidades;
	
	}

	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	private static List getProductosStockOnlinePorLocal( Connection con, int idLocal ) throws Exception {

		List listaConfiguraciones = new ArrayList();

		PreparedStatement stm = null;
		ResultSet rs = null;

		try {
			
			String SQL = "SELECT ID_STOCK_ONLINE,ID_CATPROD,ID_LOCAL,ID_PRODUCTO,ESTADO,STOCK_MINIMO,MODO,COD_PROD1 "+
						 "FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? ";

			stm = con.prepareStatement( SQL + " WITH UR" );
			stm.setInt( 1, idLocal );
			
			rs = stm.executeQuery();

			while ( rs.next() ) {
				
				StockOnLine stockOnline = new StockOnLine();
				
				stockOnline.setIdStockOnline( rs.getString( 1 ) );
				stockOnline.setIdCatProd( rs.getString( 2 ) );
				stockOnline.setIdLocal( rs.getInt( 3 ) );
				stockOnline.setIdProducto( rs.getInt( 4 ) );
				stockOnline.setEstado( rs.getInt( 5 ) );
				stockOnline.setStockMinimo( rs.getInt( 6 ) );
				stockOnline.setModo( rs.getInt( 7 ) );
				stockOnline.setSkuProducto( rs.getString( 8 ) );
				
				listaConfiguraciones.add( stockOnline );
				
			}

		} catch ( SQLException ex ) {
			
			logger.error( "getProductosStockOnlinePorLocal - Problema SQL", ex );
			throw new Exception( ex );
			
		} catch ( Exception ex ) {
			
			logger.error( "getProductosStockOnlinePorLocal - Problema General", ex );
			throw new Exception( ex );
			
		} finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
			} catch ( SQLException e ) {
			
				logger.error( "getProductosStockOnlinePorLocal - Problema SQL (close)", e );
				
			}
		}
		
		
		return listaConfiguraciones;

	}
	
	
	/**
	 * 
	 * @param con
	 * @param idLocal
	 * @param idProducto
	 * @return
	 * @throws Exception
	 */
	private static boolean marcarSinStockAutomatico( Connection con, int idLocal, int idProducto ) throws Exception {
		
		String sqlFo = "";
		String sqlFo1 = null;
		ResultSet rs = null;
		boolean terminoCorrectamente = false;
		
		sqlFo1 = "SELECT PRE_TIENESTOCK FROM FO_PRECIOS_LOCALES "+
				"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? AND PRE_ESTADO = ? ";
		
		sqlFo = "UPDATE FO_PRECIOS_LOCALES SET PRE_TIENESTOCK = 0 "+
				"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? AND PRE_ESTADO = ? ";
		
		PreparedStatement psFo = null;
		PreparedStatement psFo1 = null;
		
		con.setAutoCommit(false);
		
		psFo1 = con.prepareStatement( sqlFo1 + " WITH UR" );
		psFo1.setInt( 1, idProducto );
		psFo1.setInt( 2, idLocal );
		psFo1.setString( 3, Constantes.PUBLICADO );
		
		rs = psFo1.executeQuery();

		try {
			
			if ( rs.next() ) {
				
				if( rs.getInt( 1 ) == 1 ) {
					
					psFo = con.prepareStatement( sqlFo );
					psFo.setInt( 1, idProducto );
					psFo.setInt( 2, idLocal );
					psFo.setString( 3, Constantes.PUBLICADO );
					
					psFo.executeUpdate();
					
					terminoCorrectamente = true;
				}
			
			}	
			
			
		
		} catch ( Exception e ) {
			
			try {
				
				con.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			e.printStackTrace();
			
			logger.error( "Error método marcarSinStockAutomatico: ->idProducto " + idProducto + " -> idLocal " + idLocal, e );
			terminoCorrectamente = false;
			
		} 
		finally {
			
			try {
				
				con.commit();
				con.setAutoCommit(true);
				
				if ( psFo != null )
					psFo.close();
				
				if ( psFo1 != null )
					psFo1.close();
				
				if ( rs != null )
					rs.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "marcarSinStockAutomatico - Problema SQL (close)", e );
			
			}
		
		}

		return terminoCorrectamente;
	
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idLocal
	 * @param idProducto
	 * @param cod_prod1
	 * @return
	 * @throws Exception
	 */
	private static boolean marcarSinStockSemiAutomatico( Connection con, int idLocal, int idProducto, String cod_prod1 ) throws Exception {
		
		String sqlFo = "";
		String sqlFo1 = null;
		ResultSet rs = null;
		boolean terminoCorrectamente = false;
		
		sqlFo1 = "SELECT PRE_TIENESTOCK FROM FO_PRECIOS_LOCALES "+
				"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? AND PRE_ESTADO = ? ";

		sqlFo = "UPDATE BODBA.BO_STOCK_ONLINE SET PUBLICAR = 0 "+
				"WHERE ID_PRODUCTO = ? AND ID_LOCAL = ? AND COD_PROD1 = ? ";

		PreparedStatement psFo = null;
		PreparedStatement psFo1 = null;
		
		con.setAutoCommit(false);
	    
		psFo1 = con.prepareStatement( sqlFo1 + " WITH UR" );
		psFo1.setInt( 1, idProducto );
		psFo1.setInt( 2, idLocal );
		psFo1.setString( 3, Constantes.PUBLICADO );
		
		rs = psFo1.executeQuery();
		
		try {
			
			if( rs.next() ) {
				
				psFo = con.prepareStatement( sqlFo );
				psFo.setInt( 1, idProducto );
				psFo.setInt( 2, idLocal );
				psFo.setString( 3, cod_prod1 );
				
				psFo.executeUpdate();
				
				terminoCorrectamente = true;
				
			}	
		
		} catch ( Exception e ) {
			
			try {
				
				con.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
			
			logger.error( "Error método marcarSinStockSemiAutomatico: ->idProducto " + idProducto + " -> idLocal " + idLocal, e );
			terminoCorrectamente = false;
		
		}
		finally {
			
			try {
				
				con.commit();
				con.setAutoCommit(true);
				
				if ( psFo != null )
					psFo.close();
				
				if ( psFo1 != null )
					psFo1.close();
				
				if ( rs != null )
					rs.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "marcarSinStockSemiAutomatico - Problema SQL (close)", e );
			
			}
		
		}

		return terminoCorrectamente;
	
	}
	
	
	/**
	 * 
	 * @param wr
	 * @param sku
	 * @param stockReal
	 * @param stockMinimo
	 * @param idProducto
	 * @param estadoInicial
	 * @param estadoFinal
	 * @param resultadoTransaccion
	 * @param estado
	 */
	private static StockOnLine guardarModificacion( String local, String sku, int stockReal, int stockMinimo, int idProducto,
											String estadoInicial, String estadoFinal, String resultadoTransaccion, int estado ) {

		StockOnLine stockOnline = new StockOnLine(); 
		
		stockOnline.setNombreLocal(local);
		stockOnline.setSkuProducto(sku);
		stockOnline.setStockReal(stockReal);
		stockOnline.setStockMinimo(stockMinimo);
		stockOnline.setIdProducto(idProducto);
		stockOnline.setEstadoInicial(estadoInicial);
		stockOnline.setEstadoFinal(estadoFinal);
		stockOnline.setResultadoTransaccion(resultadoTransaccion);
		stockOnline.setEstado(estado);
		
		return stockOnline;
		
	}

	
	/**
	 * 
	 * @param con
	 * @param idLocal
	 * @param idProducto
	 * @return
	 */
	private static boolean marcarConStockAutomatico( Connection con, int idLocal, int idProducto) {
		
		String sqlFo = "";
		String sqlFo1 = "";
		ResultSet rs = null;
		boolean terminoCorrectamente = false;

		sqlFo1 = "SELECT PRE_TIENESTOCK FROM FO_PRECIOS_LOCALES "+
				"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? AND PRE_ESTADO = ? ";
		
		sqlFo = "UPDATE FO_PRECIOS_LOCALES SET PRE_TIENESTOCK = 1 "+
				"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? AND PRE_ESTADO = ? ";

		PreparedStatement psFo = null;
		PreparedStatement psFo1 = null;
	
		try {
		
			con.setAutoCommit(false);
			
			psFo1 = con.prepareStatement( sqlFo1 );
			psFo1.setInt( 1, idProducto );
			psFo1.setInt( 2, idLocal );
			psFo1.setString( 3, Constantes.PUBLICADO );
			
			rs = psFo1.executeQuery();
			
			if(rs.next()) {
			
				if ( rs.getInt( 1 ) == 0 ) {
					
					psFo = con.prepareStatement( sqlFo );
					psFo.setInt( 1, idProducto );
					psFo.setInt( 2, idLocal );
					psFo.setString( 3, Constantes.PUBLICADO );
					
					psFo.executeUpdate();
					
					terminoCorrectamente = true;
				
				}
				
			}
			
			
		} catch ( Exception e ) {
			
			try {
				
				con.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			e.printStackTrace();
			logger.error( "Error método marcarConStockAutomatico: ->idProducto " + idProducto + " -> idLocal " + idLocal, e );
			
			terminoCorrectamente = false;
			
		} finally {
			
			try {
				
				con.commit();
				con.setAutoCommit(true);
				
				if ( psFo != null )
					psFo.close();
				
				if ( psFo1 != null )
					psFo1.close();
				
				if ( rs != null )
					rs.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "marcarConStockAutomatico - Problema SQL (close)", e );
			
			}
		
		}

		return terminoCorrectamente;
	
	}
	
	/**
	 * 
	 * @param con
	 * @param idLocal
	 * @param idProducto
	 * @param cod_prod1
	 * @return
	 */
	private static boolean marcarConStockSemiAutomatico( Connection con, int idLocal, int idProducto, String cod_prod1 ) {
		
		String sqlFo = "";
		String sqlFo1 = "";
		ResultSet rs = null;
		boolean terminoCorrectamente = false;

		sqlFo1 = "SELECT PRE_TIENESTOCK FROM FO_PRECIOS_LOCALES "+
				"WHERE PRE_PRO_ID = ? AND PRE_LOC_ID = ? AND PRE_ESTADO = ? ";

		sqlFo = "UPDATE BODBA.BO_STOCK_ONLINE SET PUBLICAR = 1 "+
				"WHERE ID_PRODUCTO = ? AND ID_LOCAL = ? AND COD_PROD1 = ? ";

		PreparedStatement psFo = null;
		PreparedStatement psFo1 = null;
		
		try {
			
			con.setAutoCommit(false);
		
			psFo1 = con.prepareStatement( sqlFo1 );
			psFo1.setInt( 1, idProducto );
			psFo1.setInt( 2, idLocal );
			psFo1.setString( 3, Constantes.PUBLICADO );
			
			rs = psFo1.executeQuery();
			
			if(rs.next()) {
			
				psFo = con.prepareStatement( sqlFo );
			
				psFo.setInt( 1, idProducto );
				psFo.setInt( 2, idLocal );
				psFo.setString( 3, cod_prod1 );
				
				psFo.executeUpdate();
				
				terminoCorrectamente = true;
				
			}
		
		} catch ( Exception e ) {
			
			try {
				
				con.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			e.printStackTrace();
			logger.error( "Error método marcarConStockSemiAutomatico: ->idProducto " + idProducto + " -> idLocal " + idLocal, e );
			
			terminoCorrectamente = false;
			
		} finally {
			
			try {
				
				con.commit();
				con.setAutoCommit(true);
				
				if ( psFo != null )
					psFo.close();
				
				if ( psFo1 != null )
					psFo1.close();
				
				if ( rs != null )
					rs.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "marcarConStockSemiAutomatico - Problema SQL (close)", e );
			
			}
		
		}

		return terminoCorrectamente;
	
	}

	
	/**
	 * 
	 * @param con
	 * @param idLocal
	 * @throws SQLException 
	 */
	private static void marcarConfirmarCrontab( Connection con, int idLocal ) throws SQLException {
		
		ResultSet rs = null;
		
		String sqlFo = "UPDATE BODBA.BO_STOCK_ONLINE SET CONFIRMAR = 1 "+
					   "WHERE ID_LOCAL = ? ";

		String sqlBo = "SELECT count(*) FROM BO_STOCK_ONLINE WHERE CONFIRMAR = 2 AND ID_LOCAL = ? ";
		
		PreparedStatement psFo = null;
		PreparedStatement psBo = null;
		
		con.setAutoCommit(false);
		
		try {
			
			psBo = con.prepareStatement( sqlBo );
			psBo.setInt( 1, idLocal );
			
			rs = psBo.executeQuery();
			
			if(rs.next()) {
			
				if( rs.getInt(1) == 0 ) {
				
					psFo = con.prepareStatement( sqlFo );
					psFo.setInt( 1, idLocal );
					
					psFo.executeUpdate();
					
				}
				
			}
			
		
		} catch ( Exception e ) {
			
			try {
				
				con.rollback();
            
			} catch (SQLException e1) {
            
				e1.printStackTrace();
            
			}
            
			e.printStackTrace();
			
			logger.error( "Error método marcarConfirmarCrontab: -> idLocal " + idLocal, e );
		
		}
		finally {
			
			try {
				
				con.commit();
				con.setAutoCommit(true);
				
				if ( psFo != null )
					psFo.close();
				
				if ( psBo != null )
					psBo.close();
				
				if ( rs != null )
					rs.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "marcarConfirmarCrontab - Problema SQL (close)", e );
			
			}
		
		}
	
	}
	
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws DAOException
	 */
	public static long[] cantidadDeProductosTendranCambios( Connection con, long idLocal, List productosCambiados ) throws Exception {
		
		try {
			 
			List productos = getProductosPorLocal( con, idLocal );
			
			return cantidadDeProductosTendranCambios( idLocal, productos, productosCambiados );
		
		} catch ( Exception e ) {
			
			logger.error( "Error cantidadDeProductosTendranCambios: ", e );
			throw new Exception( e );
		
		 }
		
	 }
	
	
	/**
	 * 
	 * @param local
	 * @param cantidadesActualmente
	 * @param cantidadesDespues
	 * @return
	 * @throws Exception
	 */
	public static StockOnLine listaResumenPorLocal( Connection con, Local local, long[] cantidadesActualmente, long[] cantidadesDespues ) throws Exception{
		
		StockOnLine stockOnline = new StockOnLine();
		
		stockOnline.setNombreLocal(local.getNombre());
		stockOnline.setPublicadosConStock(formatoNumero(cantidadesActualmente[ 1 ]));
		stockOnline.setPublicadosSinStock(formatoNumero(cantidadesActualmente[ 0 ]));
		stockOnline.setDespublicados(formatoNumero(cantidadesActualmente[ 2 ]));
		
		stockOnline.setCambioConStock( "+" + formatoNumero(cantidadesDespues[ 0 ]) + ", -" + formatoNumero(cantidadesDespues[ 2 ] ) + "" );
		stockOnline.setCambioSinStock( "+" + formatoNumero( cantidadesDespues[ 1 ]) + ", -" + formatoNumero(cantidadesDespues[ 3 ] ) + "");
		stockOnline.setMaestra(formatoNumero(getTotalMaestra( con, local.getId() ) ) );
		
		return stockOnline;
		 
	}
	
	
	/**
	 * 
	 * @param productosModificados
	 * @return
	 * @throws IOException
	 */
	private static HSSFWorkbook generaExcel(List productosModificados) throws IOException{
		
		File file = new File(Constantes.pathRespaldos + "/" + getArchivoAutomaticoXLS());
		
		HSSFWorkbook wb = new HSSFWorkbook();
		
		try {

            HSSFSheet sheet = wb.createSheet(Constantes.TITULO_HOJA_EXCEL);
			
			HSSFFont font = wb.createFont();
			font.setFontHeightInPoints((short)10);
			font.setFontName("Arial");
			font.setItalic(false);

			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short)10);
			font2.setFontName("Arial");
			font2.setItalic(false);
			font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font2.setColor(HSSFColor.WHITE.index);
			
			HSSFFont font3 = wb.createFont();
			font3.setFontHeightInPoints((short)10);
			font3.setFontName("Arial");
			font3.setItalic(false);
			font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font3.setColor(HSSFColor.BLACK.index);
			
			HSSFCellStyle style = wb.createCellStyle();
			style.setFont(font2);
			style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style.setFillBackgroundColor(HSSFColor.BLUE.index);
			style.setFillForegroundColor(HSSFColor.GREEN.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			HSSFCellStyle style1 = wb.createCellStyle();
			style1.setFont(font3);
			style1.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
			style1.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
			style1.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
			style1.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
			style1.setFillBackgroundColor(HSSFColor.BLUE.index);
			style1.setFillForegroundColor(HSSFColor.YELLOW.index);
			style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCellStyle style2 = wb.createCellStyle();
			style2.setFont(font);
			style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style2.setFillForegroundColor(HSSFColor.WHITE.index);
			
			HSSFCellStyle style3 = wb.createCellStyle();
			style3.setFont(font);
			style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style3.setFillForegroundColor(HSSFColor.WHITE.index);
			style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			HSSFCellStyle style4 = wb.createCellStyle();
			style4.setFont(font);
			style4.setBorderTop(HSSFCellStyle.BORDER_THIN);
			style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style4.setFillForegroundColor(HSSFColor.WHITE.index);
			style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			
			
			HSSFRow row0 = sheet.createRow((short) 0);
			
			HSSFCell cell_0_1 = row0.createCell(0);
			cell_0_1.setCellValue(new HSSFRichTextString(Constantes.PRIMERA_CELDA));
			cell_0_1.setCellStyle(style);
			
			HSSFCell cell_0_2 = row0.createCell(1);
			cell_0_2.setCellValue(new HSSFRichTextString(Constantes.SEGUNDA_CELDA));
			cell_0_2.setCellStyle(style);
			
			/*HSSFCell cell_0_3 = row0.createCell(2);
			cell_0_3.setCellValue(new HSSFRichTextString(Constantes.TERCERA_CELDA));
			cell_0_3.setCellStyle(style);
			*/
			HSSFCell cell_0_3 = row0.createCell(2);
			cell_0_3.setCellValue(new HSSFRichTextString(Constantes.CUARTA_CELDA));
			cell_0_3.setCellStyle(style);
			
			HSSFCell cell_0_4 = row0.createCell(3);
			cell_0_4.setCellValue(new HSSFRichTextString(Constantes.QUINTA_CELDA));
			cell_0_4.setCellStyle(style);
			
			HSSFCell cell_0_5 = row0.createCell(4);
			cell_0_5.setCellValue(new HSSFRichTextString(Constantes.SEXTA_CELDA));
			cell_0_5.setCellStyle(style);
			
			HSSFCell cell_0_6 = row0.createCell(5);
			cell_0_6.setCellValue(new HSSFRichTextString(Constantes.SEPTIMA_CELDA));
			cell_0_6.setCellStyle(style);
			
			HSSFCell cell_0_7 = row0.createCell(6);
			cell_0_7.setCellValue(new HSSFRichTextString(Constantes.OCTAVA_CELDA));
			cell_0_7.setCellStyle(style);

			HSSFCell cell_0_8 = row0.createCell(7);
			cell_0_8.setCellValue(new HSSFRichTextString(Constantes.NOVENA_CELDA));
			cell_0_8.setCellStyle(style);

				
			/*****************************************************/
			int j = 0;
			
			Iterator it = productosModificados.iterator();
			
			while (it.hasNext()) {
				
				StockOnLine stockOnLine = (StockOnLine)it.next();
				
				j++;
			    
			    HSSFRow row = sheet.createRow((int) j);

			    HSSFCell cell_1 = row.createCell(0);
				cell_1.setCellValue(new HSSFRichTextString(stockOnLine.getNombreLocal()));
				cell_1.setCellStyle(style2);
			    
				HSSFCell cell_2 = row.createCell(1);
				cell_2.setCellValue(new HSSFRichTextString(stockOnLine.getSkuProducto()));
				cell_2.setCellStyle(style2);

				/*HSSFCell cell_3 = row.createCell(2);
				cell_3.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getStockReal())));
				cell_3.setCellStyle(style3);*/

				HSSFCell cell_3 = row.createCell(2);
				cell_3.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getStockMinimo())));
				cell_3.setCellStyle(style3);
				
				HSSFCell cell_4 = row.createCell(3);
				cell_4.setCellValue(new HSSFRichTextString(String.valueOf(stockOnLine.getIdProducto())));
				cell_4.setCellStyle(style3);
				
				HSSFCell cell_5 = row.createCell(4);
				cell_5.setCellValue(new HSSFRichTextString(stockOnLine.getEstadoInicial()));
				cell_5.setCellStyle(style3);
				
				HSSFCell cell_6 = row.createCell(5);
				cell_6.setCellValue(new HSSFRichTextString(stockOnLine.getEstadoFinal()));
				cell_6.setCellStyle(style3);
								
				HSSFCell cell_7 = row.createCell(6);
				cell_7.setCellValue(new HSSFRichTextString(stockOnLine.getResultadoTransaccion()));
				cell_7.setCellStyle(style3);
				
				HSSFCell cell_8 = row.createCell(7);
				cell_8.setCellValue(new HSSFRichTextString(getNombreModo(stockOnLine.getEstado())));
				cell_8.setCellStyle(style3);
				
			}
			
			/*sheet.autoSizeColumn((short)0);
			sheet.autoSizeColumn((short)1);
			sheet.autoSizeColumn((short)2);
			sheet.autoSizeColumn((short)3);
			sheet.autoSizeColumn((short)4);
			sheet.autoSizeColumn((short)5);
			sheet.autoSizeColumn((short)6);
			sheet.autoSizeColumn((short)7);
			sheet.autoSizeColumn((short)8);*/
			
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
	        fileOut.close();
			
		}catch(Exception e) {
			
			e.printStackTrace();
		
		}
		
		return wb;
    
    }
	
	
	/**
  	 * 
  	 * @param biz
  	 * @param local
  	 * @param cantidadesActualmente
  	 * @param cantidadesDespues
	 * @throws Exception 
  	 * @throws BocException
  	 */
  	private static void enviarMailStockOnline( List resumenStockOnine ) throws Exception {
  		
  		
   		String mail_tpl = Constantes.conf_path_html;

   		TemplateLoader mail_load = new TemplateLoader( mail_tpl );
   		ITemplate mail_tem = mail_load.getTemplate();	

   		String mail_result = mail_tem.toString( contenidoMailStockOnline( resumenStockOnine ) );
   		
   		File file = new File( Constantes.pathRespaldos + "/" + nombreArchivoStockOnline( Constantes.pathRespaldos, Constantes.AUTOMATICO, Constantes.XLS ) );
   
   		logger.info("Init	Enviar correo  archivo " +file.getName());
		String to = Constantes.DESTINATARIO;
		String cc = Constantes.CC;
		String host = Constantes.HOST;
		String subject = Constantes.SUBJECT;
		String from = Constantes.FROM;
		
		List archivos = new ArrayList();
		archivos.add(file);
		
		new SendMail(host, from, mail_result, archivos).enviar(to, cc, subject);
		
		logger.debug("	Host: " + host);
		logger.debug("	To: " + to);
		logger.debug("	Cc: " + cc);
		logger.info("End	Enviar correo");
		
	}
	
  	
  	/**
	 * 
	 * @param local
	 */
    public static void eliminarArchivo() {

    	File file = new File( Constantes.pathRespaldos + "/" + nombreArchivoStockOnline( Constantes.pathRespaldos, Constantes.AUTOMATICO, Constantes.XLS ) );
    	   
        file.delete();
    }
    
	
	/**
	 * 
	 * @param num
	 * @return
	 */
	public static String formatoNumero( long num ) {
		
		NumberFormat df = NumberFormat.getInstance();
		df.setMaximumFractionDigits(2);		
		return df.format( num );

	}	
	
	
	/**
	 * 
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public static List getProductosPorLocal( Connection conexion, long idLocal ) throws Exception {

		List productos = new ArrayList();
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			String SQL = "SELECT PRE_PRO_ID, PRE_TIENESTOCK FROM FO_PRECIOS_LOCALES "+
						 "WHERE PRE_LOC_ID = ? AND " +
						 "PRE_PRO_ID IN ( SELECT ID_PRODUCTO FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND CONFIRMAR = 1 and ESTADO = 1 )";
			
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
			throw new Exception( ex );
		
		} catch ( Exception ex ) {
			
			logger.error( "getProductosPorLocal - Problema General", ex );
			throw new Exception( ex );
		
		} 
		finally {
			
			try {
				
				if ( rs != null )
					rs.close();
				
				if ( stm != null )
					stm.close();
				
			} catch ( SQLException e ) {
				
				logger.error( "getProductosPorLocal - Problema SQL (close)", e );
			
			}
		
		}
		
		return productos;
	
	}
	

	/**
	 * 
	 * @param idLocal
	 * @param productos
	 * @return
	 * @throws Exception
	 */
	public static long[] cantidadDeProductosTendranCambios( long idLocal, List productos, List ProductosCambiados ) throws Exception {             
		
		
		long cantidadQuedaranSinStock = 0;
		long cantidadQuedaranConStock = 0;
		long cantidadQuedaranSinStockN = 0;
		long cantidadQuedaranConStockN = 0;
		long cantidades[] = new long[ 5 ];
		
		try {
			
			Iterator p = productos.iterator();
			
			while ( p.hasNext() ) {
			
				BOPreciosLocalesDTO pl = ( BOPreciosLocalesDTO )p.next();
				
				Iterator pc = ProductosCambiados.iterator();
				
				while ( pc.hasNext() ) {
				
					StockOnLine st = ( StockOnLine )pc.next();
					
					if( pl.getPreProID() == st.getIdProducto() ) {
						
						if ( st.isPublicarConStock() && pl.getPreTieneStock() == 1 ) {
							
							cantidadQuedaranConStock++;
							cantidadQuedaranSinStockN++;
						
						} else if ( st.isPublicarSinStock() && pl.getPreTieneStock() == 0 ){
						
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
			throw new Exception( ex );
			
		}
		
		return cantidades;
	
	}
	  	
  	
  	/**
  	 * 
  	 * @param ruta
  	 * @param prefijo
  	 * @param local
  	 * @param extension
  	 * @return
  	 */
  	public static String nombreArchivoStockOnline( String ruta, String prefijo, String extension ) {

        if ( ruta != null )
        	Constantes.path = ruta;
        
        File directorio = new File(Constantes.path);

        String[] lista = directorio.list(new Filtro(prefijo, extension));

        if ( lista.length == 1 )
             return lista[0];
        
        String archivo = "";
        for ( int i = 0; i < lista.length; i++ ) {
            
        	archivo = lista[i].substring(0,10);
             
        	if( archivo.equals( prefijo ) )
            	 return lista[i];
             
        }

        return archivo;
     
  	}
  	
  	
  	/**
  	 * 
  	 * @param biz
  	 * @return
  	 * @throws BocException 
  	 */
  	private static IValueSet contenidoMailStockOnline( List resumenStockOnine ) throws Exception {
  		
  		IValueSet mail_top = new ValueSet();
  		
  		List resumen = new ArrayList();
  		
  		for (int i = 0; i < resumenStockOnine.size(); i++) {
		
			IValueSet fila = new ValueSet();
			
			StockOnLine stockOnline = (StockOnLine) resumenStockOnine.get(i);
			
			fila.setVariable( "{local}", stockOnline.getNombreLocal());
			fila.setVariable( "{publicadosConStock}", String.valueOf( stockOnline.getPublicadosConStock() ) );
			fila.setVariable( "{publicadosSinStock}", String.valueOf( stockOnline.getPublicadosSinStock() ) );
			fila.setVariable( "{despublicados}", String.valueOf( stockOnline.getDespublicados() ) );
	      
			fila.setVariable( "{cambioConStock}", stockOnline.getCambioConStock() );
			fila.setVariable( "{cambioSinStock}", stockOnline.getCambioSinStock() );
			
			fila.setVariable( "{maestra}", String.valueOf(stockOnline.getMaestra()) );
		    
			resumen.add(fila);
	   
		}
		
		mail_top.setVariable( "{tipoProceso}", "Autom&aacute;tico"  );
		mail_top.setVariable( "{proceso}", "Semi Autom&aacute;tico"  );   
		
		if ( resumen.size() > 0 ) {
		
			mail_top.setDynamicValueSets("RESUMEN", resumen);
			mail_top.setVariable("{mensaje}", "");
			mail_top.setVariable("{estado}", "block");
		
		}else {
			
			mail_top.setVariable("{mensaje}", "No hubo cambios de publicaciones y/o despublicaciones en los locales.");
			mail_top.setVariable("{estado}", "none");
			
		}
			
		
  		return mail_top;
  	
  	}
	
	/**
	 * 
	 * @param modo
	 * @return
	 */
	private static String getNombreModo( int modo ) {
		
		String nombreModo = "";
		
		if ( modo == Constantes.CONSTANTE_ESTADO_INT_AUTOMATICO ) {
		
			nombreModo = Constantes.CONSTANTE_ESTADO_AUTOMATICO;
		
		} else if ( modo == Constantes.CONSTANTE_ESTADO_INT_SEMIAUTOMATICO ) {
		
			nombreModo = Constantes.CONSTANTE_ESTADO_SEMIAUTOMATICO;
		
		}
		
		return nombreModo;
	
	}

	/**
	 * 
	 * @return
	 */
	private static String getArchivoAutomaticoXLS() {

		Date date = new Date();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd_HH-mm-ss" );
		
		String nombre = "Automatico_" + dateFormat.format( date ) + ".xls";
		
		return nombre;
		
	}


	/**
	 * 
	 * @param valores
	 * @param idProducto
	 * @return
	 */
	private static int getStockReal( String valores[][], String idProducto ) {

		int retorno = Constantes.CONSTANTE_NO_ENCONTRADO;

		for ( int i = 0; i < valores.length; i++ ) {
			
			if ( idProducto.equals( valores[ i ][ 0 ] ) ) {
				
				try {
				
					if( valores[ i ][ 1 ].indexOf( '.' ) != -1 ) {
						
						retorno = Integer.parseInt( valores[ i ][ 1 ].substring( 0, valores[ i ][ 1 ].indexOf( "." ) ) );
						break;
					
					}
					
					retorno = Integer.parseInt( valores[ i ][ 1 ] );
					break;
				
				} catch ( NumberFormatException e ) {
					
					retorno = Constantes.CONSTANTE_INT_ERROR;
					break;
				
				}
			
			}
		
		}

		return retorno;
	
	}


	/**
	 * 
	 * @param archivo
	 * @return
	 * @throws IOException
	 */
	public static String[][] cargarCSV( String archivo ) throws IOException {

		BufferedReader br = new BufferedReader( new FileReader( Constantes.path + archivo ) );
		
		logger.info( "Ruta Archivo: " + Constantes.path + archivo );
		
		String fila = null;
		List lista = new ArrayList();

		boolean primeraLinea = false;
		
		while( ( fila = br.readLine() ) != null ) {
			
			if ( !primeraLinea ) {
				
				String columna[] = fila.split( "," );
				String resumenColumna[][] = new String[ 1 ][ 2 ];
				
				if ( !"".equals( columna[ 0 ].trim() ) && !"".equals( columna[ 2 ].trim() ) ) {
					
					resumenColumna[ 0 ][ 0 ] = columna[ 2 ].replaceAll( "\"", "" );
					resumenColumna[ 0 ][ 1 ] = columna[ 3 ].replaceAll( "\"", "" );
					
					lista.add( resumenColumna );
					
				}
				
			}
			
			primeraLinea = false;
		
		}

		String[][] datos = new String[ lista.size() ][ 2 ];
		
		for ( int i = 0; i < lista.size(); i++ ) {

			String resumenComlumna[][] = new String[ 1 ][ 2 ];
			
			resumenComlumna = ( String[][] ) lista.get( i );
			
			datos[ i ][ 0 ] = resumenComlumna[ 0 ][ 0 ];
			datos[ i ][ 1 ] = resumenComlumna[ 0 ][ 1 ];
			
		}

		br.close();
		return datos;
		
	}


	/**
	 * 
	 * @param conexion
	 * @param idLocal
	 * @return
	 * @throws Exception
	 */
	public static int getTotalMaestra( Connection conexion, long idLocal ) throws Exception {             
		
		PreparedStatement 	stm = null;
		ResultSet 			rs = null;
		
		int total = 0;
		
		try {
			
			String SQL = "SELECT TOTAL_MAESTRA FROM BODBA.BO_STOCK_ONLINE WHERE ID_LOCAL = ? AND CONFIRMAR = 1 GROUP BY TOTAL_MAESTRA";
		    
			stm = conexion.prepareStatement( SQL + " WITH UR" );
			stm.setLong( 1, idLocal );
			
			rs = stm.executeQuery();			
			
			if ( rs.next() ) {
				
				total = rs.getInt( 1 );
					
			} 	
		
		} catch ( Exception ex ) { 
			
			logger.error( "Error getTotalMaestra: ", ex );
			throw new Exception( ex );
			
		}
		 finally {
				
				try {
					
					if ( rs != null )
						rs.close();
					
					if ( stm != null )
						stm.close();
					
				
				} catch ( SQLException e ) {
					
					logger.error( "getTotalMaestra - Problema SQL (close)", e );
				
				}
		 
		 }
		
		return total;
	
	}

}
