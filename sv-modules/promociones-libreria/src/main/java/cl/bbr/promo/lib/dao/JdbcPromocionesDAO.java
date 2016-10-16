package cl.bbr.promo.lib.dao;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import cl.bbr.jumbocl.shared.log.Logging;
import cl.bbr.promo.lib.dto.ClienteKccDTO;
import cl.bbr.promo.lib.dto.ClientePRDTO;
import cl.bbr.promo.lib.dto.ClienteSG6DTO;
import cl.bbr.promo.lib.dto.FOTcpDTO;
import cl.bbr.promo.lib.dto.MedioPagoNormalizadoDTO;
import cl.bbr.promo.lib.dto.PrioridadPromosDTO;
import cl.bbr.promo.lib.dto.PromocionCriterio;
import cl.bbr.promo.lib.dto.PromocionDTO;
import cl.bbr.promo.lib.dto.SamsungGalaxy6DTO;
import cl.bbr.promo.lib.exception.PromocionesDAOException;
import cl.bbr.promo.lib.helpers.Util;

/**
 * <p>Clase para la interacción con el repositorio de datos. Esta clase debe ser instanciada sólo desde
 * capa de servicios (service).</p>
 * 
 * <p>Esta clase contiene los métodos para consultar, modificar, ingresar datos en el repositorio.</p>
 *  
 * @author BBR ecommerce & retail
 *
 */
public class JdbcPromocionesDAO implements PromocionesDAO {


	/**
	 * Comentario para <code>logger</code> instancia del log de la aplicación para la clase.
	 */
	Logging logger = new Logging(this);

	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getPromocionesByProductoId(long)
	 */
	public List getPromocionesByProductoId( PromocionCriterio criterio ) throws PromocionesDAOException {
		List lista = new ArrayList();
		Connection conexion = null;
	    PreparedStatement stm = null;
	    PreparedStatement stmBanner = null;
	    ResultSet rs = null;   
	    ResultSet rsBanner = null;
	    
		// Recuperar los TCP de los clientes
		List lista_tcp = criterio.getLista_tcp();
		String l_tcp_in = "0";
		if( lista_tcp != null ) {
			for ( int i = 0; lista_tcp != null && i < lista_tcp.size(); i++ ) {
				FOTcpDTO tcp = (FOTcpDTO)lista_tcp.get(i);
				logger.debug("TCP "+i+" nro:" + tcp.getTcp_nro() );
				l_tcp_in += ","+tcp.getTcp_nro();
			}
		}
		logger.debug( "l_tcp_in:" + l_tcp_in );
		
		try {
			//query para las promociones de banner, en el caso que no exista promocion del producto
			//obtiene el id de la promocion
			String queryBann= " select prm_desc_banner as descr,prm_nom_banner as banner,prm_color_banner as colorbanner"+ 
							  " from  FODBA.FO_PRODUCTOS_PROMO where PRM_PROD_ID = ?";
			
			String query =  " select * " +
							" from pr_producto_promos as prpr " +
							"      join pr_promocion as prom1 on prom1.cod_promo = prpr.cod_promo1 and prom1.id_local = prpr.id_local" +
							" where prpr.id_producto = " + criterio.getId_producto_bo() + 
							" and prpr.id_local = " + criterio.getId_local() + 
							" and prpr.cod_promo1 <> 0 " +
							" and prom1.fini <= current_timestamp " +
							" and prom1.ffin >= current_timestamp " +
							" and (" +
							"	( prom1.beneficio1 > 0 and prom1.tcp1 in ("+l_tcp_in+") ) " +
							"   or " +
							"   ( prom1.beneficio2 > 0 and prom1.tcp2 in ("+l_tcp_in+") ) " +
							"   or " +
							"   ( prom1.beneficio3 > 0 and prom1.tcp3 in ("+l_tcp_in+") ) ) " +
							" UNION " +
							" select * " +
							" from pr_producto_promos as prpr " +
							"      join pr_promocion as prom1 on prom1.cod_promo = prpr.cod_promo2 and prom1.id_local = prpr.id_local" +
							" where prpr.id_producto = " + criterio.getId_producto_bo() + 
							" and prpr.id_local = " + criterio.getId_local() +
							" and prpr.cod_promo2 <> 0 " +
							" and prom1.fini <= current_timestamp " +
							" and prom1.ffin >= current_timestamp " +
							" and (" +
							"	( prom1.beneficio1 > 0 and prom1.tcp1 in ("+l_tcp_in+") ) " +
							"   or " +
							"   ( prom1.beneficio2 > 0 and prom1.tcp2 in ("+l_tcp_in+") ) " +
							"   or " +
							"   ( prom1.beneficio3 > 0 and prom1.tcp3 in ("+l_tcp_in+") ) ) " +
							" UNION " +
							" select * " +
							" from pr_producto_promos as prpr " +
							"      join pr_promocion as prom1 on prom1.cod_promo = prpr.cod_promo3 and prom1.id_local = prpr.id_local" +
							" where prpr.id_producto = " + criterio.getId_producto_bo() + 
							" and prpr.id_local = " + criterio.getId_local() +
							" and prpr.cod_promo3 <> 0 " +
							" and prom1.fini <= current_timestamp " +
							" and prom1.ffin >= current_timestamp " +
							" and (" +
							"	( prom1.beneficio1 > 0 and prom1.tcp1 in ("+l_tcp_in+") ) " +
							"   or " +
							"   ( prom1.beneficio2 > 0 and prom1.tcp2 in ("+l_tcp_in+") ) " +
							"   or " +
							"   ( prom1.beneficio3 > 0 and prom1.tcp3 in ("+l_tcp_in+") ) ) ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query  + " WITH UR");
			logger.debug( criterio.toString() );
			rs = stm.executeQuery();
			
				//existen promociones
				while (rs.next()) {
					PromocionDTO promocion = null;
					promocion = new PromocionDTO();
					promocion.setCod_promo( rs.getLong("cod_promo") );
					promocion.setBanner( rs.getString("banner") );				
					promocion.setId_promocion( rs.getLong("id_promocion") );
					promocion.setDescr( rs.getString("descr") );
					promocion.setCant_min( rs.getLong("cant_min") );
					
					promocion.setMonto1( rs.getLong("monto1") );
					promocion.setDescuento1(rs.getDouble("descuento1"));
					
					promocion.setMonto2(rs.getLong("monto2"));
					promocion.setDescuento2(rs.getDouble("descuento2"));
					
					promocion.setMonto3(rs.getLong("monto3"));
					promocion.setDescuento3(rs.getDouble("descuento3"));
					
					promocion.setMonto4(rs.getLong("monto4"));
					promocion.setDescuento4(rs.getDouble("descuento4"));
					
					promocion.setMonto5(rs.getLong("monto5"));
					promocion.setDescuento5(rs.getDouble("descuento5"));
					
					promocion.setFp1(rs.getLong("fp1"));
					promocion.setNum_cuota1(rs.getLong("num_cuota1"));
					promocion.setTcp1(rs.getLong("tcp1"));
					promocion.setBeneficio1(rs.getDouble("beneficio1"));
					
					promocion.setFp2(rs.getLong("fp2"));
					promocion.setNum_cuota2(rs.getLong("num_cuota2"));
					promocion.setTcp2(rs.getLong("tcp2"));
					promocion.setBeneficio2(rs.getDouble("beneficio2"));
					
					promocion.setFp3(rs.getLong("fp3"));
					promocion.setNum_cuota3(rs.getLong("num_cuota3"));
					promocion.setTcp3(rs.getLong("tcp3"));					
					promocion.setBeneficio3(rs.getDouble("beneficio3"));
					
					promocion.setCondicion1(rs.getLong("condicion1"));
					promocion.setCondicion2(rs.getLong("condicion2"));
					promocion.setCondicion3(rs.getLong("condicion3"));
					
					lista.add(promocion);
				}

				if(lista.size() == 0){
				//agregar a promociones los banner de productos para desplegar
				//en las pantallas de jumbo.cl
				conexion = JdbcDAOFactory.getConexion();
				stmBanner = conexion.prepareStatement(queryBann);
				stmBanner.setLong(1, criterio.getId_producto_fo());
				rsBanner = stmBanner.executeQuery();
				while (rsBanner.next()) {
					PromocionDTO promocionBann = null;
					promocionBann = new PromocionDTO();
					//promocion.setCod_promo( rsBanner.getLong("cod_promo") );
					promocionBann.setBanner( rsBanner.getString("banner") );				
					promocionBann.setColorBann( rsBanner.getString("colorbanner") );
					promocionBann.setDescr( rsBanner.getString("descr") );
						
					lista.add(promocionBann);
				}
				}
			
		} catch (SQLException ex) {
			logger.error("getPromocionesByProductoId - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getPromocionesByProductoId - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
	            try {
	                if (rs != null)
	                    rs.close();
	                if (rsBanner != null)
	                	rsBanner.close();
	                if (stm != null)
	                    stm.close();
	                if (stmBanner != null)
	                	stmBanner.close();
	                //Cierra coneccion
	                if (conexion != null && !conexion.isClosed())
	                    conexion.close();
	            } catch (SQLException e) {
	                logger.error("getPromocionesByProductoId - Problema SQL (close)", e);
	            }
	    }
		
		return lista;
	}
	
	/**
	 * Entrega las promociones de todos los productos de la lista
	 */
	public Hashtable getPromociones( String listaIdBo, int idLocal, List lista_tcp) throws PromocionesDAOException {
		Hashtable promociones = new Hashtable();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		// crea lista de TCP Recuperar los TCP de los clientes
		StringBuffer l_tcp_in = new StringBuffer("(0");
        for (Iterator iter = lista_tcp.iterator(); iter.hasNext();) {
            FOTcpDTO tcp = (FOTcpDTO) iter.next();
            logger.debug("TCP nro:" + tcp.getTcp_nro() );
            l_tcp_in.append("," + tcp.getTcp_nro());
        }
        l_tcp_in.append(")");
        logger.debug( "l_tcp_in:" + l_tcp_in.toString() );
		/////////////////////////////////////////////
		
		try {
			String[] query =  {" select * " +
							" from pr_producto_promos as prpr " +
							"      join pr_promocion as prom1 on prom1.cod_promo = prpr.cod_promo1 and prom1.id_local = prpr.id_local" +
							" where prpr.id_producto in " + listaIdBo + 
							" and prpr.id_local = " + idLocal + 
							" and prpr.cod_promo1 <> 0 " +
							" and prom1.fini <= current_timestamp " +
							" and prom1.ffin >= current_timestamp " +
							" and (" +
							"	( prom1.beneficio1 > 0 and prom1.tcp1 in "+l_tcp_in.toString()+" ) " +
							"   or " +
							"   ( prom1.beneficio2 > 0 and prom1.tcp2 in "+l_tcp_in.toString()+" ) " +
							"   or " +
							"   ( prom1.beneficio3 > 0 and prom1.tcp3 in "+l_tcp_in.toString()+" ) ) " ,
							//" UNION " +
							" select * " +
							" from pr_producto_promos as prpr " +
							"      join pr_promocion as prom1 on prom1.cod_promo = prpr.cod_promo2 and prom1.id_local = prpr.id_local" +
							" where prpr.id_producto in " + listaIdBo + 
							" and prpr.id_local = " + idLocal +
							" and prpr.cod_promo2 <> 0 " +
							" and prom1.fini <= current_timestamp " +
							" and prom1.ffin >= current_timestamp " +
							" and (" +
							"	( prom1.beneficio1 > 0 and prom1.tcp1 in "+l_tcp_in.toString()+" ) " +
							"   or " +
							"   ( prom1.beneficio2 > 0 and prom1.tcp2 in "+l_tcp_in.toString()+" ) " +
							"   or " +
							"   ( prom1.beneficio3 > 0 and prom1.tcp3 in "+l_tcp_in.toString()+" ) ) " ,
							//" UNION " +
							" select * " +
							" from pr_producto_promos as prpr " +
							"      join pr_promocion as prom1 on prom1.cod_promo = prpr.cod_promo3 and prom1.id_local = prpr.id_local" +
							" where prpr.id_producto in " + listaIdBo + 
							" and prpr.id_local = " + idLocal +
							" and prpr.cod_promo3 <> 0 " +
							" and prom1.fini <= current_timestamp " +
							" and prom1.ffin >= current_timestamp " +
							" and (" +
							"	( prom1.beneficio1 > 0 and prom1.tcp1 in "+l_tcp_in.toString()+" ) " +
							"   or " +
							"   ( prom1.beneficio2 > 0 and prom1.tcp2 in "+l_tcp_in.toString()+" ) " +
							"   or " +
							"   ( prom1.beneficio3 > 0 and prom1.tcp3 in "+l_tcp_in.toString()+" ) ) "};
			
			conexion = JdbcDAOFactory.getConexion();
			
			for (int i = 0; i < query.length; i++) {
			    stm = conexion.prepareStatement(query[i]  + " WITH UR");
				rs = stm.executeQuery();
	
				while (rs.next()) {
					PromocionDTO promocion = new PromocionDTO();
					Integer idProducto = new Integer(rs.getInt("id_producto"));
					promocion.setCod_promo( rs.getLong("cod_promo") );
					promocion.setTipo_promo(rs.getLong("TIPO_PROMO"));
                    promocion.setBanner( rs.getString("banner") );	
                    
					promocion.setId_promocion( rs.getLong("id_promocion") );
					promocion.setDescr( rs.getString("descr") );
					promocion.setCant_min( rs.getLong("cant_min") );
					
					promocion.setMonto1( rs.getLong("monto1") );
					promocion.setDescuento1(rs.getDouble("descuento1"));
					
					promocion.setMonto2(rs.getLong("monto2"));
					promocion.setDescuento2(rs.getDouble("descuento2"));
					
					promocion.setMonto3(rs.getLong("monto3"));
					promocion.setDescuento3(rs.getDouble("descuento3"));
					
					promocion.setMonto4(rs.getLong("monto4"));
					promocion.setDescuento4(rs.getDouble("descuento4"));
					
					promocion.setMonto5(rs.getLong("monto5"));
					promocion.setDescuento5(rs.getDouble("descuento5"));
					
					promocion.setFp1(rs.getLong("fp1"));
					promocion.setNum_cuota1(rs.getLong("num_cuota1"));
					promocion.setTcp1(rs.getLong("tcp1"));
					promocion.setBeneficio1(rs.getDouble("beneficio1"));					
					
					promocion.setFp2(rs.getLong("fp2"));
					promocion.setNum_cuota2(rs.getLong("num_cuota2"));
					promocion.setTcp2(rs.getLong("tcp2"));
					promocion.setBeneficio2(rs.getDouble("beneficio2"));
					
					promocion.setFp3(rs.getLong("fp3"));
					promocion.setNum_cuota3(rs.getLong("num_cuota3"));
					promocion.setTcp3(rs.getLong("tcp3"));					
					promocion.setBeneficio3(rs.getDouble("beneficio3"));
					
					promocion.setCondicion1(rs.getLong("condicion1"));
					promocion.setCondicion2(rs.getLong("condicion2"));
					promocion.setCondicion3(rs.getLong("condicion3"));
					
					List promPorProducto = (List) promociones.get(idProducto);
					
					if(promPorProducto == null)
					    promPorProducto = new ArrayList();
					promPorProducto.add(promocion);
					promociones.put(idProducto, promPorProducto);
				}
			}
			
			//Obtiene las promociones por seccion asociadas al local.
			ArrayList promocionesTipo900 =  (ArrayList) getPromocionesSeccionByLocal(idLocal);
            //Si tiene promociones vigentes por tipo, se buscan las secciones a la que pertenece esta promo.
            if(!promocionesTipo900.isEmpty()){
            	Hashtable promoSecciones = getSeccionPromoByLocal(promocionesTipo900, idLocal);	  
                if(!promoSecciones.isEmpty()){
                	listaIdBo = listaIdBo.replaceAll("\\(", "");
                	listaIdBo = listaIdBo.replaceAll("\\)", "");
    	            String[] idsBO = (listaIdBo != null)? listaIdBo.split(","):null;
    	            if(idsBO != null){    	            	
    	            	for(int p = 0 ; p < idsBO.length; p++){
    	            		String idProd = idsBO[p];
    	            		int seccionProducto = getSeccionProductoByIdProducto(idProd);
    	            		    	            		
    	        			if (seccionProducto != 0 && promoSecciones.containsKey(String.valueOf(seccionProducto))){
    	        				        				
    	            			List promPorProducto = (List) promociones.get(new Integer(idProd));
    	            			if(promPorProducto == null)
    	    					    promPorProducto = new ArrayList();	            			
    	            			promPorProducto.add((PromocionDTO)promoSecciones.get(String.valueOf(seccionProducto)));
    	            			promociones.put(new Integer(idProd), promPorProducto);		            		
    	        			}          			            		
    	            	}
    	            }
                }
            }       
            
			
		} catch (SQLException ex) {
			logger.error("getPromociones- Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {

			logger.error("getPromociones- Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getPromociones - Problema SQL (close)", e);
            }
		} 
		
		return promociones;
	}
	
	//Obtiene promociones por seccion
	public List getPromocionesSeccionByLocal(int idLocal) throws PromocionesDAOException {
		
		Connection conexion = null;
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList promocionesTipo900 = new ArrayList();
		
		int dia = Util.getDayOfWeek();
        if (dia == 1)
            dia = 906;
        else
            dia = 900 + (dia - 2);
		
		try {
	    	
	        //Lo primero es buscar las promociones activas por tipo (tipo corresponde al dia de la semana.)
            String sqlPromoSecc = " SELECT PR.* FROM PR_PROMO_SECCION PS " +
            		" INNER JOIN PR_PROMOCION PR ON PR.COD_PROMO = PS.COD_PROMO AND (PR.FINI <= current_timestamp AND PR.FFIN >= current_timestamp and PR.ID_LOCAL="+idLocal+")" +
            		" WHERE (PS.ID_TIPO = "+dia+" OR PS.ID_TIPO in (907,908,909,910,911,912,913,914)) AND PS.COD_PROMO <> 0 AND PS.ID_LOCAL = "+idLocal;
                        
            conexion = JdbcDAOFactory.getConexion();
            stmt = conexion.createStatement();
            rs= stmt.executeQuery(sqlPromoSecc + " WITH UR");
            
            while (rs.next()) {  
            	PromocionDTO promocion = new PromocionDTO();
				promocion.setCod_promo( rs.getLong("cod_promo") );				
				promocion.setTipo_promo(rs.getLong("TIPO_PROMO"));				
                promocion.setBanner( rs.getString("banner") );                
				promocion.setId_promocion( rs.getLong("id_promocion") );				
				promocion.setDescr( rs.getString("descr"));				
				promocion.setCant_min( rs.getLong("cant_min") );
				
				promocion.setMonto1( rs.getLong("monto1") );
				promocion.setDescuento1(rs.getDouble("descuento1"));
				
				promocion.setMonto2(rs.getLong("monto2"));
				promocion.setDescuento2(rs.getDouble("descuento2"));
				
				promocion.setMonto3(rs.getLong("monto3"));
				promocion.setDescuento3(rs.getDouble("descuento3"));
				
				promocion.setMonto4(rs.getLong("monto4"));
				promocion.setDescuento4(rs.getDouble("descuento4"));
				
				promocion.setMonto5(rs.getLong("monto5"));
				promocion.setDescuento5(rs.getDouble("descuento5"));
				
				promocion.setFp1(rs.getLong("fp1"));
				promocion.setNum_cuota1(rs.getLong("num_cuota1"));
				promocion.setTcp1(rs.getLong("tcp1"));
				promocion.setBeneficio1(rs.getDouble("beneficio1"));
				
				promocion.setFp2(rs.getLong("fp2"));
				promocion.setNum_cuota2(rs.getLong("num_cuota2"));
				promocion.setTcp2(rs.getLong("tcp2"));
				promocion.setBeneficio2(rs.getDouble("beneficio2"));
				
				promocion.setFp3(rs.getLong("fp3"));
				promocion.setNum_cuota3(rs.getLong("num_cuota3"));
				promocion.setTcp3(rs.getLong("tcp3"));
				promocion.setBeneficio3(rs.getDouble("beneficio3"));
				
				promocion.setCondicion1(rs.getLong("condicion1"));
				promocion.setCondicion2(rs.getLong("condicion2"));
				promocion.setCondicion3(rs.getLong("condicion3"));  
				promocionesTipo900.add(promocion);
			}
			
		} catch (SQLException ex) {
			logger.error("getPromocionesPorSeccionByLocal - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getPromocionesPorSeccionByLocal - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getPromocionesPorSeccionByLocal - Problema SQL (close)", e);
            }
		} 
		
		return promocionesTipo900;
	}
	
	//Obtiene las secciones de la promocion
	public Hashtable getSeccionPromoByLocal(ArrayList promocionesTipo900,int idLocal) throws PromocionesDAOException {
		
		Connection conexion = null;
		Hashtable promoSecciones = new Hashtable();	
		
		try {	    	
            conexion = JdbcDAOFactory.getConexion();
           
        	Iterator it = promocionesTipo900.iterator();
        	while(it.hasNext()){
        		PromocionDTO promocion = (PromocionDTO) it.next();
        		String sqlMatriz = " SELECT int(ID_SECCION) FROM BODBA.PR_MATRIZ_SECCION WHERE ID_LOCAL = "+idLocal;    
        		 
     			switch((int)promocion.getTipo_promo()){    
     				case 914:
     					sqlMatriz+= " AND ESP8 = 1 ";
     				break;
     				case 913:
     					sqlMatriz+= " AND ESP7 = 1 ";
     				break;
     				case 912:
     					sqlMatriz+= " AND ESP6 = 1 ";
     				break;
     				case 911:
     					sqlMatriz+= " AND ESP5 = 1 ";
     				break;
     				case 910:
     					sqlMatriz+= " AND ESP4 = 1 ";
     				break;
     				case 909:
     					sqlMatriz+= " AND ESP3 = 1 ";
     				break;
     				case 908:
     					sqlMatriz+= " AND ESP2 = 1 ";
     				break;
     				case 907:
     					sqlMatriz+= " AND ESP1 = 1 ";
     				break;
     				case 906:
     					sqlMatriz+= " AND DOMINGO = 1";
     				break;      
     				case 905:
     					sqlMatriz+= " AND SABADO = 1";
     				break;  
     				case 904:
     					sqlMatriz+= " AND VIERNES = 1";
     				break;  
     				case 903:
     					sqlMatriz+= " AND JUEVES = 1";
     				break;  
     				case 902:
     					sqlMatriz+= " AND MIERCOLES = 1";
     				break;  
     				case 901:
     					sqlMatriz+= " AND MARTES = 1";
     				break;  
     				case 900:
     					sqlMatriz+= " AND LUNES = 1";
     				break;  
     				default: 
     					sqlMatriz+= "AND ID_SECCION > 10000";
     				break;
     			} 
     			
     			Statement stmt = conexion.createStatement();
    			ResultSet rs = stmt.executeQuery(sqlMatriz + " WITH UR");
    			while (rs.next()) {
    				promoSecciones.put(rs.getString("ID_SECCION"), promocion);
    			}
    			try {
	                if (rs != null)
	                    rs.close();
	                if (stmt != null)
	                    stmt.close();
	            } catch (SQLException e) {
	                logger.error("getPromocionesPorSeccionByLocal - Problema SQL (close)", e);
	            }
        	}
			
		} catch (SQLException ex) {
			logger.error("getSeccionPromoByLocal - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getSeccionPromoByLocal - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getSeccionPromoByLocal - Problema SQL (close)", e);
            }
		}
		
		return promoSecciones;	 
	}
	
	//Obtiene las seccion de un producto
	public int getSeccionProductoByIdProducto(String IdProducto) throws PromocionesDAOException {
		
		Connection conexion = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		int resultado = 0;
		
		try {
			String sqlSeccionProducto =  "SELECT int(substr(id_catprod,1,2)) AS SECCION FROM BODBA.BO_PRODUCTOS WHERE ID_PRODUCTO="+IdProducto; 
			
			conexion = JdbcDAOFactory.getConexion();
			stmt = conexion.createStatement();
			
			rs = stmt.executeQuery(sqlSeccionProducto + " WITH UR");
			
			if (rs.next()){
				resultado = rs.getInt("SECCION");
			}
			
			
		} catch (SQLException ex) {
			logger.error("getseccionProductoByIdProducto - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getseccionProductoByIdProducto - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getseccionProductoByIdProducto - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
	}
	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getIdBoProductoId(cl.bbr.fo.promociones.dto.PromocionCriterio)
	 */
	public long getIdBoProductoId(PromocionCriterio criterio) throws PromocionesDAOException {
		
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		long resultado = 0;
		
		try {
			String query =  " select pro_id_bo " +
							" from fo_productos " +
							" where pro_id = " + criterio.getId_producto_fo();
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query  + " WITH UR");
			logger.debug( criterio.toString() );
			rs = stm.executeQuery();

			while (rs.next()) {
				resultado = rs.getLong("pro_id_bo");
			}
			
		} catch (SQLException ex) {
			logger.error("getIdBoProductoId - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getIdBoProductoId - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getIdBoProductoId - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getPromocionesByTCP(java.util.List)
	 */
	public List getPromocionesByTCP(List tcp) throws PromocionesDAOException {
		List lista = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {

			if( tcp != null ) {
			
				String query_in = "";
				String tag = "";
				
				for( int i = 0; tcp != null && i < tcp.size(); i++ ) {
					FOTcpDTO criterio = (FOTcpDTO) tcp.get(i);
					query_in += tag + criterio.getTcp_nro();
					tag = ",";
				}
				
				String query =  "SELECT * " +
						" FROM BODBA.PR_PROMOCION " +
						" WHERE current_timestamp BETWEEN fini AND ffin " +
						" AND ( tcp1 in ("+query_in+") OR tcp2 in ("+query_in+") OR tcp3 in ("+query_in+") )";
				
				conexion = JdbcDAOFactory.getConexion();
				stm = conexion.prepareStatement(query  + " WITH UR");
				rs = stm.executeQuery();
	
				while (rs.next()) {
					PromocionDTO promocion = null;
					promocion = new PromocionDTO();
					promocion.setCod_promo( rs.getLong("cod_promo") );
					promocion.setBanner( rs.getString("banner") );				
					promocion.setId_promocion( rs.getLong("id_promocion") );
					promocion.setDescr( rs.getString("descr") );
					promocion.setCant_min( rs.getLong("cant_min") );
					
					promocion.setMonto1( rs.getLong("monto1") );
					promocion.setDescuento1(rs.getDouble("descuento1"));
					
					promocion.setMonto2(rs.getLong("monto2"));
					promocion.setDescuento2(rs.getDouble("descuento2"));
					
					promocion.setMonto3(rs.getLong("monto3"));
					promocion.setDescuento3(rs.getDouble("descuento3"));
					
					promocion.setMonto4(rs.getLong("monto4"));
					promocion.setDescuento4(rs.getDouble("descuento4"));
					
					promocion.setMonto5(rs.getLong("monto5"));
					promocion.setDescuento5(rs.getDouble("descuento5"));
					
					promocion.setFp1(rs.getLong("fp1"));
					promocion.setNum_cuota1(rs.getLong("num_cuota1"));
					promocion.setTcp1(rs.getLong("tcp1"));
					promocion.setBeneficio1(rs.getDouble("beneficio1"));
					
					promocion.setFp2(rs.getLong("fp2"));
					promocion.setNum_cuota2(rs.getLong("num_cuota2"));
					promocion.setTcp2(rs.getLong("tcp2"));
					promocion.setBeneficio2(rs.getDouble("beneficio2"));
					
					promocion.setFp3(rs.getLong("fp3"));
					promocion.setNum_cuota3(rs.getLong("num_cuota3"));
					promocion.setTcp3(rs.getLong("tcp3"));
					promocion.setBeneficio3(rs.getDouble("beneficio3"));
					
					promocion.setCondicion1(rs.getLong("condicion1"));
					promocion.setCondicion2(rs.getLong("condicion2"));
					promocion.setCondicion3(rs.getLong("condicion3"));
					
					lista.add(promocion);
				}
			}
			
		} catch (SQLException ex) {
			logger.error("getPromocionesByTCP - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getPromocionesByTCP - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getPromocionesByTCP - Problema SQL (close)", e);
            }
		} 
		
		return lista;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getPromocionesByTCP(java.util.List)
	 */
	public List getPromocionesByTCP(List tcp, String id_local) throws PromocionesDAOException {
		List lista = new ArrayList();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			if( tcp != null ) {
				String query_in = "";
				String tag = "";
				
				for( int i = 0; tcp != null && i < tcp.size(); i++ ) {
					FOTcpDTO criterio = (FOTcpDTO) tcp.get(i);
					query_in += tag + criterio.getTcp_nro();
					tag = ",";
				}
				
				String query = "SELECT * "
                             + "FROM BODBA.PR_PROMOCION "
                             + "WHERE current_timestamp BETWEEN fini AND ffin "
                             + "  AND ( tcp1 in ("+query_in+") OR tcp2 in ("+query_in+") OR tcp3 in ("+query_in+") ) "
                             + "  AND ID_LOCAL = " + id_local;
				
				conexion = JdbcDAOFactory.getConexion();
				stm = conexion.prepareStatement(query  + " WITH UR");
				rs = stm.executeQuery();
	
				while (rs.next()) {
					PromocionDTO promocion = null;
					promocion = new PromocionDTO();
					promocion.setCod_promo( rs.getLong("cod_promo") );
					promocion.setBanner( rs.getString("banner") );				
					promocion.setId_promocion( rs.getLong("id_promocion") );
					promocion.setDescr( rs.getString("descr") );
					promocion.setCant_min( rs.getLong("cant_min") );
					
					promocion.setMonto1( rs.getLong("monto1") );
					promocion.setDescuento1(rs.getDouble("descuento1"));
					
					promocion.setMonto2(rs.getLong("monto2"));
					promocion.setDescuento2(rs.getDouble("descuento2"));
					
					promocion.setMonto3(rs.getLong("monto3"));
					promocion.setDescuento3(rs.getDouble("descuento3"));
					
					promocion.setMonto4(rs.getLong("monto4"));
					promocion.setDescuento4(rs.getDouble("descuento4"));
					
					promocion.setMonto5(rs.getLong("monto5"));
					promocion.setDescuento5(rs.getDouble("descuento5"));
					
					promocion.setFp1(rs.getLong("fp1"));
					promocion.setNum_cuota1(rs.getLong("num_cuota1"));
					promocion.setTcp1(rs.getLong("tcp1"));
					promocion.setBeneficio1(rs.getDouble("beneficio1"));
					
					promocion.setFp2(rs.getLong("fp2"));
					promocion.setNum_cuota2(rs.getLong("num_cuota2"));
					promocion.setTcp2(rs.getLong("tcp2"));
					promocion.setBeneficio2(rs.getDouble("beneficio2"));
					
					promocion.setFp3(rs.getLong("fp3"));
					promocion.setNum_cuota3(rs.getLong("num_cuota3"));
					promocion.setTcp3(rs.getLong("tcp3"));
					promocion.setBeneficio3(rs.getDouble("beneficio3"));
					
					promocion.setCondicion1(rs.getLong("condicion1"));
					promocion.setCondicion2(rs.getLong("condicion2"));
					promocion.setCondicion3(rs.getLong("condicion3"));
					
					lista.add(promocion);
				}
			}
		} catch (SQLException ex) {
			logger.error("getPromocionesByTCP - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getPromocionesByTCP - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getPromocionesByTCP - Problema SQL (close)", e);
            }
		} 
		return lista;
	}

	
	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getPromosPrioridadProducto(long, long)
	 */
	public PrioridadPromosDTO getPromosPrioridadProducto(long id_producto, long id_local) throws PromocionesDAOException {
		
		PrioridadPromosDTO resultado = new PrioridadPromosDTO();
		Connection conexion = null;
        PreparedStatement stm1 = null;
        //PreparedStatement stm2 = null;
        //PreparedStatement stm3 = null;
        ResultSet rs1 = null;
        //ResultSet rs2 = null;
        //ResultSet rs3 = null;
		
		try {           
            conexion = JdbcDAOFactory.getConexion();
            
            
            String q1 = " SELECT produc_promo.ID_PRODUCTO,  " +
            		"	NVL((SELECT COD_PROMO FROM BODBA.PR_PROMOCION pr_promo_h WHERE pr_promo_h.COD_PROMO=produc_promo.COD_PROMO1 and pr_promo_h.ID_LOCAL=? and pr_promo_h.fini <= current_timestamp and pr_promo_h.ffin >= current_timestamp),0)  as COD_PROMO1, " + 
            		"	NVL((SELECT COD_PROMO FROM BODBA.PR_PROMOCION pr_promo_h WHERE pr_promo_h.COD_PROMO=produc_promo.COD_PROMO2 and pr_promo_h.ID_LOCAL=? and pr_promo_h.fini <= current_timestamp and pr_promo_h.ffin >= current_timestamp),0) as COD_PROMO2, " +
            		"	NVL((SELECT COD_PROMO FROM BODBA.PR_PROMOCION pr_promo_h WHERE pr_promo_h.COD_PROMO=produc_promo.COD_PROMO3 and pr_promo_h.ID_LOCAL=? and pr_promo_h.fini <= current_timestamp and pr_promo_h.ffin >= current_timestamp),0) as COD_PROMO3 " +

            		"	FROM BODBA.PR_PRODUCTO_PROMOS produc_promo  " +
            		"	inner join BODBA.PR_PROMOCION pr_promo on produc_promo.cod_promo1 = pr_promo.cod_promo and ( pr_promo.fini <= current_timestamp and pr_promo.ffin >= current_timestamp ) " +
            		"	or  produc_promo.cod_promo2 = pr_promo.cod_promo and ( pr_promo.fini <= current_timestamp and pr_promo.ffin >= current_timestamp ) " +
            		"	 or  produc_promo.cod_promo3 = pr_promo.cod_promo and ( pr_promo.fini <= current_timestamp and pr_promo.ffin >= current_timestamp ) " +
            		"	WHERE produc_promo.id_producto = ?  " +
            		"	AND pr_promo.id_local = ?  " +
            		"	AND produc_promo.id_local = ?  " ;
            
            
            stm1 = conexion.prepareStatement(q1  + " WITH UR");
            stm1.setLong(1, id_local);
            stm1.setLong(2, id_local);
            stm1.setLong(3, id_local);
            stm1.setLong(4, id_producto);
            stm1.setLong(5, id_local);
            stm1.setLong(6, id_local);
            
            rs1 = stm1.executeQuery();
            
            if (rs1.next()) {
				if(rs1.getLong("COD_PROMO1")>0)
					resultado.setCodPromoEvento(rs1.getLong("COD_PROMO1"));
				
				if(rs1.getLong("COD_PROMO2")>0)
					resultado.setCodPromoPeriodica(rs1.getLong("COD_PROMO2"));
				 
				if(rs1.getLong("COD_PROMO3")>0)
					resultado.setCodPromoNormal(rs1.getLong("COD_PROMO3"));
            }
            
            
            /*
            String q1 = "	SELECT prod.cod_promo1 " +
                        "	FROM fodba.PR_PRODUCTO_PROMOS prod " +
                        "	inner join fodba.pr_promocion pro1 " +
                        "	on prod.cod_promo1 = pro1.cod_promo " +
                        "	WHERE prod.id_producto = ? " +
                        "	AND pro1.id_local = ? " +
                        "	and prod.id_local = ? " +
                        "	and ( pro1.fini <= current_timestamp and pro1.ffin >= current_timestamp )";            
            stm1 = conexion.prepareStatement(q1  + " WITH UR");
            stm1.setLong(1, id_producto);
            stm1.setLong(2, id_local);
            stm1.setLong(3, id_local);
            rs1 = stm1.executeQuery();
            if (rs1.next()) {
                resultado.setCodPromoEvento(rs1.getLong("cod_promo1"));
            }
            
            String q2 = "	SELECT prod.cod_promo2 " +
                        "	FROM fodba.PR_PRODUCTO_PROMOS prod " +
                        "	inner join fodba.pr_promocion pro2 " +
                        "	on prod.cod_promo2 = pro2.cod_promo " +
                        "	WHERE prod.id_producto = ? " +
                        "	AND pro2.id_local = ? " +
                        "	and prod.id_local = ? " +
                        "	and ( pro2.fini <= current_timestamp and pro2.ffin >= current_timestamp )"; 
 
            stm2 = conexion.prepareStatement(q2  + " WITH UR");
            stm2.setLong(1, id_producto);
            stm2.setLong(2, id_local);
            stm2.setLong(3, id_local);
            
            rs2 = stm2.executeQuery();
            
            if (rs2.next()) {
                resultado.setCodPromoPeriodica(rs2.getLong("cod_promo2"));
            }
 
            String q3 = "	SELECT prod.cod_promo3 " +
                        "	FROM fodba.PR_PRODUCTO_PROMOS prod " +
                        "	inner join fodba.pr_promocion pro3 " +
                        "	on prod.cod_promo3 = pro3.cod_promo " +
                        "	WHERE prod.id_producto = ? " +
                        "	AND pro3.id_local = ? " +
                        "	and prod.id_local = ? " +
                        "	and ( pro3.fini <= current_timestamp and pro3.ffin >= current_timestamp )";
 
            stm3 = conexion.prepareStatement(q3  + " WITH UR");
            stm3.setLong(1, id_producto);
            stm3.setLong(2, id_local);
            stm3.setLong(3, id_local);
            rs3 = stm3.executeQuery();
            
            if (rs3.next()) {
                resultado.setCodPromoNormal(rs3.getLong("cod_promo3"));
            }
            */

		} catch (SQLException ex) {
			logger.error("getPromosPrioridadProducto - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getPromosPrioridadProducto - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs1 != null)
                    rs1.close();
                if (stm1 != null)
                    stm1.close();
                
               /* if (rs2 != null)
                    rs2.close();
                if (stm2 != null)
                    stm2.close();
                
                 if (rs3 != null)
                    rs3.close();
                if (stm3 != null)
                    stm3.close();*/
                
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("conviertecarroDonald - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getMedioPAgoNormalizado(cl.bbr.fo.promociones.dto.MedioPagoNormalizadoDTO)
	 */
	public MedioPagoNormalizadoDTO getMedioPAgoNormalizado(MedioPagoNormalizadoDTO mp) throws PromocionesDAOException {
		
		MedioPagoNormalizadoDTO resultado = new MedioPagoNormalizadoDTO();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			
			String query_jmcl = "";
			if( mp.getMp_jmcl() != null )
				query_jmcl = " and mp_jmcl = '"+mp.getMp_jmcl()+"' ";
			String query_promo = "";
			if( mp.getMp_promo() != null )
				query_promo = " and mp_promo = '"+mp.getMp_promo()+"' ";
			if( mp.getMp_jmcl_ncuotas() != null )
				query_promo = " and mp_jmcl_ncuotas = "+mp.getMp_jmcl_ncuotas()+" ";
			
			String query = 	" SELECT mp_promo, mp_jmcl " +
			  				" FROM PR_MEDIO_PAGO " +
							" WHERE 1=1 " + query_jmcl + query_promo;
			
			conexion = JdbcDAOFactory.getConexion();
			 stm = conexion.prepareStatement(query  + " WITH UR");
			 rs = stm.executeQuery();

			if (rs.next()) {
				resultado.setMp_jmcl(rs.getString("mp_jmcl"));				
				resultado.setMp_promo(rs.getString("mp_promo"));
			}
			
		
		} catch (SQLException ex) {
			logger.error("getMedioPAgoNormalizado - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getMedioPAgoNormalizado - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getMedioPAgoNormalizado - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
	}

	/* (sin Javadoc)
	 * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getCodigoLocalPos(long)
	 */
	public int getCodigoLocalPos(long id_local) throws PromocionesDAOException {
		
		int resultado = 0;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			
			String query = " SELECT cod_local_pos " +
			  " FROM bo_locales " +
			  " WHERE id_local = ? ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query  + " WITH UR");
			stm.setLong(1, id_local);
			rs = stm.executeQuery();

			if (rs.next()) {
				resultado = rs.getInt("cod_local_pos");				
			}

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCodigoLocalPos - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
		
	}
	
	/**
	 * Entrega las promociones de todos los productos de la lista
	 */
	public Hashtable getPromocionesBanner( String listaIdProd, int idLocal, List lista_tcp) throws PromocionesDAOException {
		Hashtable promociones = new Hashtable();
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			//query para las promociones de banner, en el caso que no exista promocion del producto
			//obtiene el id de la promocion
			String[] queryBann=  {" select prm_prod_id as id_producto, prm_desc_banner as descr,prm_nom_banner as banner,prm_color_banner as colorbanner"+ 
							  " from  FODBA.FO_PRODUCTOS_PROMO where PRM_PROD_ID in " + listaIdProd +""};			
			conexion = JdbcDAOFactory.getConexion();
			for (int i = 0; i < queryBann.length; i++) {
			    stm = conexion.prepareStatement(queryBann[i]);
				rs = stm.executeQuery();
	
			while (rs.next()) {
				PromocionDTO promocionBann = null;
				promocionBann = new PromocionDTO();
				Integer idProducto = new Integer(rs.getInt("id_producto"));
				promocionBann.setBanner( rs.getString("banner") );				
				promocionBann.setColorBann( rs.getString("colorbanner") );
				promocionBann.setDescr( rs.getString("descr") );
					
				List promPorProducto = (List) promociones.get(idProducto);
				if(promPorProducto == null)
				    promPorProducto = new ArrayList();
					promPorProducto.add(promocionBann);
					promociones.put(idProducto, promPorProducto);
				}
			}
		} catch (SQLException ex) {
			logger.error("getPromocionesBanner- Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {

			logger.error("getPromocionesBanner- Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getPromocionesBanner - Problema SQL (close)", e);
            }
		} 
		
		return promociones;
	}
	
	 //--FORMULARIO KCC
    /* (non-Javadoc)
     * @see cl.bbr.fo.promociones.dao.PromocionesDAO#addDataClienteKcc(cl.bbr.fo.promociones.dto.ClienteKccDTO)
     */
	public boolean addDataClienteKcc(ClienteKccDTO dataClienteKcc) throws PromocionesDAOException {
	      String sqlIns = "insert into fodba.FO_KCC_CLIENTE ( kcc_rut, kcc_dv, kcc_nombre_completo, kcc_email, kcc_direccion, kcc_comunaDespacho," +
	      		" kcc_sexo, kcc_talla, kcc_anosBebe, kcc_mesesBebe, kcc_boleta, kcc_recibirInformacion) " +
	            " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	      	
	        Connection con = null;
	        PreparedStatement ps = null;
	        boolean success = false;
	        
	        try {
	           con = JdbcDAOFactory.getConexion();
	           
	           ps = con.prepareStatement(sqlIns);
	           
	           ps.setString(1,dataClienteKcc.getRut());
	           ps.setString(2,dataClienteKcc.getDv());
	           ps.setString(3,dataClienteKcc.getNombreCompleto());
	           ps.setString(4,dataClienteKcc.getEmail());
	           ps.setString(5,dataClienteKcc.getDireccion());
	           ps.setString(6,dataClienteKcc.getComunaDespacho());
	           ps.setString(7,dataClienteKcc.getSexo());
	           ps.setString(8,dataClienteKcc.getTalla());
	           ps.setInt(9,dataClienteKcc.getAnnosBebe());
	           ps.setInt(10,dataClienteKcc.getMesesBebe());
	           ps.setString(11,dataClienteKcc.getBoleta());
	           ps.setString(12,dataClienteKcc.getAceptaInformacion());
	           
	          int r = ps.executeUpdate();
	           
	           success = true;
	           
	        } catch (SQLException e) {
	        	logger.error("addDataClienteKcc - Problema SQL", e);
	            throw new PromocionesDAOException(e);
	         } finally {
	        	 try {
	 	   			//con.commit();
	 	         		 ps.close();
	 	         	 } catch (SQLException e1) {
	 	         		logger.error("addDataClienteKcc - Error al cerrar ps", e1);
	 	         	 }
	 	         	 try {
	 		   			//con.commit();
	 		         	con.close();
	 		        } catch (SQLException e1) {
	 		        	logger.error("addDataClienteKcc - Error al cerrar con", e1);
	 		        }
	 	           //autoCommitTrue(con);
	 	           //close(ps, con);
	 	         }

		return success;
	}
	
	/**
	 * @param Cod_barra
	 * @param id_local
	 * @return
	 * @throws PromocionesDAOException
	 */
	public String getProductoDescripcion(long Cod_barra) throws PromocionesDAOException {
		
		String resultado = "";
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		try {
			
			String query = "SELECT fp.PRO_TIPO_PRODUCTO, fp.PRO_DES_CORTA, fm.MAR_NOMBRE "+ 
							"FROM FO_PRODUCTOS fp "+
							"inner join FO_MARCAS fm on fm.MAR_ID = fp.PRO_MAR_ID "+
							"inner join BO_CODBARRA bc on bc.ID_PRODUCTO = fp.PRO_ID_BO "+
							"WHERE bc.COD_BARRA= ? ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query  + " WITH UR");
			stm.setLong(1, Cod_barra);
			rs = stm.executeQuery();

			if (rs.next()) {
			
				resultado = rs.getString(1) + " " + rs.getString(3) + " " + rs.getString(2);
	            
				if (resultado.length() > 255) {
	             
					resultado = resultado.substring(0, 255);
	                
				 }
	            
			}

		} catch (SQLException ex) {
			logger.error("getProductoDescripcion - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getProductoDescripcion - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getProductoDescripcion - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
		
	}
	
    /* (non-Javadoc)
     * @see cl.bbr.fo.promociones.dao.PromocionesDAO#getPromociones(java.lang.String, java.lang.String)
     */
    public boolean getClienteKccByRut(String rut, String dv) throws PromocionesDAOException {
		boolean resultado = false;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
	
		try {
			
			String query = " SELECT count(1) as cont FROM " +
					"FODBA.FO_KCC_CLIENTE WHERE kcc_rut=? AND kcc_dv=? ";
			
			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query  + " WITH UR");
			stm.setString(1, rut);
			stm.setString(2, dv);
			
			rs = stm.executeQuery();

			if (rs.next()) {
				if ((rs.getInt("cont")) > 0) {
					resultado = true;
				} else {
					resultado = false;
				}
			} else {
				resultado = false;
			}
			

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
            try {
                if (rs != null)
                    rs.close();
                if (stm != null)
                    stm.close();
                //Cierra coneccion
                if (conexion != null && !conexion.isClosed())
                    conexion.close();
            } catch (SQLException e) {
                logger.error("getCodigoLocalPos - Problema SQL (close)", e);
            }
		} 
		
		return resultado;
		
    }
    public boolean addDataClientePR(ClientePRDTO dataClientePR)
			throws PromocionesDAOException {
		String sqlIns = "insert into fodba.FO_PR_CLIENTE ( pr_rut, pr_dv, pr_nombre,pr_apellidopaterno, pr_email, pr_direccion, pr_comunaDespacho,"
				+ "  pr_recibirInformacion, pr_fechanacimiento) "
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection con = null;
		PreparedStatement ps = null;
		boolean success = false;

		try {
			con = JdbcDAOFactory.getConexion();

			ps = con.prepareStatement(sqlIns);
			ps.setString(1, dataClientePR.getRut());
			ps.setString(2, dataClientePR.getDv());
			ps.setString(3, dataClientePR.getNombre());
			ps.setString(4, dataClientePR.getApellido());
			ps.setString(5, dataClientePR.getEmail());
     		ps.setString(6, dataClientePR.getDireccion());
			ps.setString(7, dataClientePR.getComunaDespacho());
			ps.setString(8, dataClientePR.getAceptaInformacion());
			ps.setDate(9, new Date (dataClientePR.getFechaNacimiento().getTime()));
			int r = ps.executeUpdate();

			success = true;

		} catch (SQLException e) {
			logger.error("addDataClienteKcc - Problema SQL", e);
			throw new PromocionesDAOException(e);
		} finally {
			try {
				// con.commit();
				ps.close();
			} catch (SQLException e1) {
				logger.error("addDataClienteKcc - Error al cerrar ps", e1);
			}
			try {
				// con.commit();
				con.close();
			} catch (SQLException e1) {
				logger.error("addDataClienteKcc - Error al cerrar con", e1);
			}
			// autoCommitTrue(con);
			// close(ps, con);
		}

		return success;
	}
    
    public boolean getClientePRByRut(String rut, String dv)
			throws PromocionesDAOException {
		boolean resultado = false;
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			String query = " SELECT count(1) as cont FROM "
					+ "FODBA.FO_PR_CLIENTE WHERE pr_rut=? AND pr_dv=? ";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setString(1, rut);
			stm.setString(2, dv);

			rs = stm.executeQuery();

			if (rs.next()) {
				if ((rs.getInt("cont")) > 0) {
					resultado = true;
				} else {
					resultado = false;
				}
			} else {
				resultado = false;
			}

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				// Cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCodigoLocalPos - Problema SQL (close)", e);
			}
		}

		return resultado;
	}
	
    /**
     * 
     */
	public ClienteSG6DTO getClienteByRut(ClienteSG6DTO cliente)
			throws PromocionesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			String query = " SELECT CLI_ID,CLI_FON_COD_1,CLI_FON_NUM_1, CLI_NOMBRE, CLI_APELLIDO_PAT, CLI_APELLIDO_MAT, CLI_EMAIL "+
						"FROM FODBA.FO_CLIENTES "+
						"WHERE CLI_RUT = ?";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setString(1, cliente.getRut());

			rs = stm.executeQuery();

			if (rs.next()) {
				cliente.setRegistrado(true);
				cliente.setNombre( rs.getString("CLI_NOMBRE"));
				cliente.setApellido(rs.getString("CLI_APELLIDO_PAT"));
				String apellidoM = rs.getString("CLI_APELLIDO_MAT");
				if(null != apellidoM && !"".equals(apellidoM.trim())){
					cliente.setApellido(cliente.getApellido()+ " "+ apellidoM);
				}
				cliente.setId(rs.getInt("CLI_ID"));
				cliente.setEmail(rs.getString("CLI_EMAIL"));
				cliente.setTelefono(rs.getString("CLI_FON_NUM_1"));
				cliente.setCodFono(rs.getString("CLI_FON_COD_1"));
					
			} else {
				cliente.setRegistrado(false);
			}

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				// Cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCodigoLocalPos - Problema SQL (close)", e);
			}
		}

		return cliente;
	}
	/**
	 * 
	 */
	public ArrayList getModelosSamsung(String llave) throws PromocionesDAOException {
		ArrayList modelosSamsung = new ArrayList();
		
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			String query = " SELECT PAR_ID,PAR_NOMBRE,PAR_DESCRIPCION,PAR_VALOR FROM FODBA.FO_PARAMETROS WHERE PAR_LLAVE = ? ";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setString(1, llave);

			rs = stm.executeQuery();

			while (rs.next()) {
				SamsungGalaxy6DTO samsung = new SamsungGalaxy6DTO();
				samsung.setId(rs.getInt("PAR_ID"));
				samsung.setPar_nombre(rs.getString("PAR_NOMBRE"));
				samsung.setPar_descripcion(rs.getString("PAR_DESCRIPCION"));
				samsung.setStock(rs.getString("PAR_VALOR"));
				modelosSamsung.add(samsung);
			} 

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				// Cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCodigoLocalPos - Problema SQL (close)", e);
			}
		}
		return modelosSamsung;
	}
	
	
	/**
	 * 
	 */
	public int getReservasSamsungCliente(ClienteSG6DTO cliente)
			throws PromocionesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		int cantidad = 0;
		try {

			String query = " SELECT COUNT(RSA_ID) AS RESERVAS FROM FODBA.FO_RESERVAS_SAMSUNG RS INNER JOIN FO_CLIENTES CLI " +
					"ON RS.RSA_CLI_ID = CLI.CLI_ID WHERE CLI_RUT =  ? ";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setString(1, cliente.getRut());

			rs = stm.executeQuery();

			if (rs.next()) {
				cantidad = rs.getInt("RESERVAS");
				
			}

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				// Cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCodigoLocalPos - Problema SQL (close)", e);
			}
		}

		return cantidad;
	}

	public boolean registrarReservaSamsung(ClienteSG6DTO cliente)
			throws PromocionesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		boolean resultado = false;
		try {

			String query = "INSERT INTO FODBA.FO_RESERVAS_SAMSUNG" +
					"(RSA_ID_MODELO,RSA_MODELO,RSA_CLI_ID,RSA_RUT,RSA_DV,RSA_NOMBRE,RSA_APELLIDO,RSA_DIRECCION,RSA_COMUNA,RSA_FONO,RSA_EMAIL,RSA_FCREACION) " +
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,CURRENT TIMESTAMP)  ";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query);
			stm.setString(1, String.valueOf(cliente.getId_modelo()));
			stm.setString(2, cliente.getModeloSamsung());
			stm.setString(3, String.valueOf(cliente.getId()));
			stm.setString(4, cliente.getRut());
			stm.setString(5, cliente.getDv());
			stm.setString(6, cliente.getNombre());
			stm.setString(7, cliente.getApellido());
			stm.setString(8, cliente.getDireccion());
			stm.setString(9, cliente.getComuna());
			stm.setString(10, cliente.getTelefono());
			stm.setString(11, cliente.getEmail());

			int rs = stm.executeUpdate();

			if (rs > 0){
				resultado = true;
				String query2 = "UPDATE FODBA.FO_PARAMETROS SET PAR_VALOR=(CAST(PAR_VALOR as INT) -1) WHERE PAR_ID = ?";
				PreparedStatement stm2 = null;
				stm2 = conexion.prepareStatement(query2);
				stm2.setString(1, String.valueOf(cliente.getId_modelo()));
				
				stm2.executeUpdate();
			}

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
			try {
				if (stm != null)
					stm.close();
				// Cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCodigoLocalPos - Problema SQL (close)", e);
			}
		}

		return resultado;
	}
	
	public ClienteSG6DTO getDireccionCliente(ClienteSG6DTO cliente)
			throws PromocionesDAOException {
		Connection conexion = null;
		PreparedStatement stm = null;
		ResultSet rs = null;

		try {

			String query = " SELECT DIR.DIR_CALLE, DIR.DIR_NUMERO, DIR.DIR_DEPTO, COM.NOMBRE " +
					"FROM FODBA.FO_DIRECCIONES DIR INNER JOIN BODBA.BO_COMUNAS COM ON DIR.DIR_COM_ID = COM.ID_COMUNA WHERE DIR.DIR_CLI_ID = ? ";

			conexion = JdbcDAOFactory.getConexion();
			stm = conexion.prepareStatement(query + " WITH UR");
			stm.setString(1, String.valueOf(cliente.getId()));

			rs = stm.executeQuery();

			if (rs.next()) {
				cliente.setComuna( rs.getString("NOMBRE"));
				String direccion = rs.getString("DIR_CALLE") + " " + rs.getString("DIR_NUMERO");
				String depto = rs.getString("DIR_DEPTO");
				if(null != depto && !"".equals(depto.trim())){
					direccion = direccion + " depto. " + depto;
				}
				cliente.setDireccion(direccion);
			} else {
				cliente.setRegistrado(false);
			}

		} catch (SQLException ex) {
			logger.error("getCodigoLocalPos - Problema SQL", ex);
			throw new PromocionesDAOException(ex);
		} catch (Exception ex) {
			logger.error("getCodigoLocalPos - Problema General", ex);
			throw new PromocionesDAOException(ex);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stm != null)
					stm.close();
				// Cierra coneccion
				if (conexion != null && !conexion.isClosed())
					conexion.close();
			} catch (SQLException e) {
				logger.error("getCodigoLocalPos - Problema SQL (close)", e);
			}
		}

		return cliente;
	}
	
	
}