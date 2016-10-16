package cl.cencosud.rmi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Precio;
import cl.cencosud.constantes.Constantes;
import cl.cencosud.constantes.ConstantesSQL;
import cl.cencosud.procesos.DbCarga;
import cl.cencosud.util.CargaPreciosUtil;
import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

/**
 * Actualiza cada precios en base de datos e indice lucene, está en una
 * transacción para que enten sincronizados
 * 
 * @author jdroguett
 */
public class ModificarPrecios {
	private Logger logger = Logger.getLogger(ModificarPrecios.class);

	private PreparedStatement psBo = null;
	private PreparedStatement psFo = null;
	private PreparedStatement psFoTra = null;
	
	/**
	 * Se itera el objeto precios con los datos a actualizar.
	 * 
	 * @param precios
	 * @throws Exception
	 */
	public void preparaPreciosActualizar(List precios) throws Exception {
		Connection con = DbCarga.conexion(Parametros.getString("USER"), Parametros.getString("PASSWORD"), Parametros.getString("DRIVER"), Parametros.getString("URL"));
		for (int i = 0; i < precios.size(); i++) {
			try {
				Precio precio = (Precio) precios.get(i);			
				logger.debug("Archivo Precio, Entrada,[precio actual="+precio.getPrecioActual() + ", precio nuevo=" + precio.getPrecio() + 
						", idProducto="+precio.getIdProducto() + ", idLocal=" + precio.getIdLocal() +
						", codigoProducto="+precio.getCodigoProducto() + 
						" idFoProducto=" + precio.getIdFoProducto() + " de la interfaz "+precio.getNombreArchivo()+" ]");
				modificarPrecio(con, precio);
			} catch (Exception e) {
				logger.error("Error al preparar los precios actualizar: " + e);
			}
		}
	}

	/**
	 * Actualiza en base de datos y en indice lucene en una transacción
	 * 
	 * @param con, Connection
	 * @param precio, Precio
	 * @throws Exception
	 */
	private void modificarPrecio(Connection con, Precio precio) throws Exception {		
		con.setAutoCommit(false);
		StringBuffer sbuilder = new StringBuffer();
		try {
			psBo = con.prepareStatement(ConstantesSQL.CP_UPD_BO_PRECIOS);
			psFo = con.prepareStatement(ConstantesSQL.CP_UPD_FO_PRECIOS_LOCLAES);
			psFoTra = con.prepareStatement(ConstantesSQL.CP_FO_PRO_TRACKING);
			
			psBo.setInt(1, precio.getPrecio());
			psBo.setInt(2, precio.getPrecio());
			psBo.setInt(3, precio.getIdProducto());
			psBo.setInt(4, precio.getIdLocal());			
			sbuilder.append("Precio Automatico:");
			if (psBo.executeUpdate() == 1) {
				sbuilder.append("update [bo_precios],");
			} else {
				sbuilder.append("NO update [bo_precios],");
			}

			psFo.setInt(1, precio.getPrecio());
			psFo.setInt(2, precio.getIdFoProducto());
			psFo.setInt(3, precio.getIdLocal());
			if (psFo.executeUpdate() == 1) {
				sbuilder.append("update [fo_precios_locales],");				 
			} else {
				sbuilder.append("NO update [fo_precios_locales],");
			}
						
			psFoTra.setInt(1, precio.getIdFoProducto());
			psFoTra.setInt(2, precio.getIdProducto());			
			psFoTra.setString(3, "Interfaces Carga Sap");
			psFoTra.setString(4, "Se actualiza precio automatico de "+CargaPreciosUtil.convertMonedaPeso(String.valueOf(precio.getPrecioActual()), Constantes.CLP_FORMAT)
								 + " a " + CargaPreciosUtil.convertMonedaPeso(String.valueOf(precio.getPrecio()), Constantes.CLP_FORMAT) +", por interfaz " + precio.getNombreArchivo());									
			if (psFoTra.executeUpdate() == 1) {
				sbuilder.append("update [fo_pro_tracking]");
			}else{
				sbuilder.append("NO update [fo_pro_tracking]");
			}
			
			logger.debug(sbuilder.toString());
			
			con.commit();				

		} catch (Exception e) {
			logger.error("ERROR, en modificar los precios, " + e);
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
			Db.close(psBo);
			Db.close(psFo);
			Db.close(psFoTra);
		}
	}

	
	/**
	 * Se itera el objeto precios del local maestro, con los datos a actualizar.
	 * 
	 * @param precios
	 * @throws Exception
	 */
	public void preparaPrecioLocalMaestro(HashMap hash, int idLocalMaestro) throws Exception {
		Connection con = DbCarga.conexion(Parametros.getString("USER"), Parametros.getString("PASSWORD"), Parametros.getString("DRIVER"), Parametros.getString("URL"));
		PreparedStatement ps = DbCarga.preparedStatement(con, ConstantesSQL.CP_SEL_DB);
		try {						
			Iterator it = hash.entrySet().iterator();
			while (it.hasNext()) {
				Entry ent = (Entry) it.next();
				Precio p = (Precio) ent.getValue();			
				Precio pDb = DbCarga.getPrecio(ps, idLocalMaestro, 	p.getCodigoProducto(), p.getUnidadMedida(), p.getNombreArchivo());
				if (pDb != null) {
					//int localId = DbCarga.getLocalId(con, p.getCodigoLocal());
					
					logger.debug("Precio automatico en local maestro, entrada [ " +
							"precio actual="+ pDb.getPrecioActual() + ", precio nuevo=" + pDb.getPrecio() + 
							", idProducto=" + pDb.getIdProducto() + ", idLocal=" + pDb.getIdLocal() +
							", codigoProducto file="+p.getCodigoProducto() +
							", idFoProducto=" + pDb.getIdFoProducto() + " de la interfaz " + pDb.getNombreArchivo());
							
					
					modificarPrecioLocalMaestro(con, pDb);
				}
			}
		} catch (Exception e) {
			logger.error("Error preparar precio local maestro: " + e);
		}
	}
	
	/**
	 * Actualiza en base de datos y en indice lucene en una transacción
	 *
	 * Cambia los precios cargados diariamnete a traves de archivos sap, le pone
	 * el precio de local los dominicos a todos los locales si es que existe
	 * producto en ese local.
	 *
	 * @param con, Connection
	 * @param precio, Precio
	 * @throws Exception
	 */
	private void modificarPrecioLocalMaestro(Connection con, Precio precio) throws Exception {		
		con.setAutoCommit(false);
		try {
			psBo = con.prepareStatement(ConstantesSQL.CP_UPD_MAESTRO_BO_PRECIOS);
			psFo = con.prepareStatement(ConstantesSQL.CP_UPD_MAESTRO_FO_PRECIOS_LOCLAES);
			psFoTra = con.prepareStatement(ConstantesSQL.CP_FO_PRO_TRACKING);
			
			psBo.setDouble(1, precio.getPrecio());
			psBo.setDouble(2, precio.getPrecio());
			psBo.setInt(3, precio.getIdProducto());
			psBo.setDouble(4, precio.getPrecio());
			
			StringBuffer sbuilder = new StringBuffer();
			try {				
				sbuilder.append("Precio Automatico Maestro:");
				if (psBo.executeUpdate() == 1) {
					sbuilder.append("update [bo_precios],");						
				} else {
					sbuilder.append("NO update [bo_precios],");						
				}				
			} catch (SQLException ex) {
				logger.error("ERROR, en modificar los precios de local maestro tabla [bo_precios], " + ex);
			}
		
			psFo.setDouble(1, precio.getPrecio());
			psFo.setInt(2, precio.getIdFoProducto());
			psFo.setDouble(3, precio.getPrecio());
			if (psFo.executeUpdate() == 1) {
				sbuilder.append("update [fo_precios_locales],");				
			} else {
				sbuilder.append("NO update [fo_precios_locales],");				
			}			
		
			psFoTra.setInt(1, precio.getIdFoProducto());
			psFoTra.setInt(2, precio.getIdProducto());			
			psFoTra.setString(3, "Interfaces Carga Sap");
			psFoTra.setString(4, "Se actualiza precio automatico en local maestro a " + CargaPreciosUtil.convertMonedaPeso(String.valueOf(precio.getPrecio()), Constantes.CLP_FORMAT) +", por interfaz " + precio.getNombreArchivo());
			if (psFoTra.executeUpdate() == 1) {
				sbuilder.append("update [fo_pro_tracking]");					
			}else{
				sbuilder.append("NO update [fo_pro_tracking]");				
			}	
			
			logger.debug(sbuilder.toString());
			
			con.commit();				
						
		} catch (Exception e) {
			logger.error("ERROR, en modificar los precios en local maestro, " + e);
			con.rollback();
			throw e;
		} finally {
			con.setAutoCommit(true);
			Db.close(psBo);
			Db.close(psFo);
			Db.close(psFoTra);			
		}
	}

}
