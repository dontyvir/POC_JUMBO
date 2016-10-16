package cl.jumbo.url.purga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import cl.jumbo.url.util.Db;

public class Purga {
	protected static Logger logger = Logger.getLogger(Purga.class.getName());

	public void dejaPedidosTemporales(Connection con) throws SQLException {
		int affectedRows = 0;
		logger.info("***	INIT dejaPedidosTemporales");
		String sql = "UPDATE BODBA.BO_PEDIDOS P SET P.ID_ESTADO = 68 WHERE P.ID_ESTADO = 1 AND P.ORIGEN = 'W' AND TIMESTAMP ( CONCAT(CONCAT(VARCHAR(P.FCREACION),'-'), VARCHAR(P.HCREACION)) ) < (CURRENT TIMESTAMP - 60 MINUTES)";
		if (logger.isDebugEnabled()) {
			logger.debug("SQL dejaPedidosTemporales: " + sql);
		}
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			affectedRows = stm.executeUpdate();
			Db.close(stm);
		} catch (Exception e) {
			logger.error("ERROR dejarPedidosTemporales: ", e);
		}
		logger.info("END dejaPedidosTemporales ROWS AFFECTED: "	+ String.valueOf(affectedRows));
	}

	public void liberaCapacidadesDespacho(Connection con) {
		int affectedRows = 0;
		logger.info("***	INIT liberaCapacidadesDespacho");
		String sql = "MERGE INTO BODBA.BO_JORNADA_DESP JD2 USING (SELECT JD.FECHA AS FECHA_DESP, JD.ID_JDESPACHO, JD.CAPAC_DESPACHO, JD.CAPAC_OCUPADA,  COUNT(P.ID_PEDIDO) AS PEDIDOS_A_LIBERAR FROM BODBA.BO_PEDIDOS P JOIN BODBA.BO_JORNADA_DESP  JD ON JD.ID_JDESPACHO = P.ID_JDESPACHO WHERE P.ID_ESTADO = 68 GROUP BY JD.FECHA, JD.ID_JDESPACHO, JD.CAPAC_DESPACHO, JD.CAPAC_OCUPADA ORDER BY JD.FECHA ) D2 ON (JD2.ID_JDESPACHO = D2.ID_JDESPACHO) WHEN MATCHED AND (JD2.CAPAC_OCUPADA < D2.PEDIDOS_A_LIBERAR) THEN UPDATE SET JD2.CAPAC_OCUPADA = 0 WHEN MATCHED AND (JD2.CAPAC_OCUPADA >= D2.PEDIDOS_A_LIBERAR) THEN UPDATE SET JD2.CAPAC_OCUPADA = (JD2.CAPAC_OCUPADA - D2.PEDIDOS_A_LIBERAR) ELSE IGNORE";
		if (logger.isDebugEnabled()) {
			logger.debug("SQL liberaCapacidadesDespacho: " + sql);
		}
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			affectedRows = stm.executeUpdate();
			Db.close(stm);
		} catch (Exception e) {
			logger.error("ERROR liberaCapacidadesDespacho: ", e);
		}
		logger.info("END liberaCapacidadesDespacho ROWS AFFECTED: "	+ String.valueOf(affectedRows));
	}

	public void liberaCapacidadesPicking(Connection con) {
		int affectedRows = 0;
		logger.info("***	INIT liberaCapacidadesPicking");
		String sql = "MERGE INTO BODBA.BO_JORNADAS_PICK JP2 USING (SELECT JP.FECHA AS FECHA_PICK, JP.ID_JPICKING, JP.CAPAC_PICKING, JP.CAPAC_OCUPADA, SUM(P.CANT_PRODUCTOS) AS CAPACIDAD_A_LIBERAR FROM BODBA.BO_PEDIDOS P JOIN BODBA.BO_JORNADAS_PICK JP ON JP.ID_JPICKING  = P.ID_JPICKING WHERE P.ID_ESTADO = 68 GROUP BY JP.FECHA, JP.ID_JPICKING, JP.CAPAC_PICKING, JP.CAPAC_OCUPADA ORDER BY JP.FECHA ) D2 ON (JP2.ID_JPICKING = D2.ID_JPICKING) WHEN MATCHED AND (JP2.CAPAC_OCUPADA < D2.CAPACIDAD_A_LIBERAR) THEN UPDATE SET JP2.CAPAC_OCUPADA = 0 WHEN MATCHED AND (JP2.CAPAC_OCUPADA >= D2.CAPACIDAD_A_LIBERAR) THEN UPDATE SET JP2.CAPAC_OCUPADA = (JP2.CAPAC_OCUPADA - D2.CAPACIDAD_A_LIBERAR) ELSE IGNORE";
		if (logger.isDebugEnabled()) {
			logger.debug("SQL liberaCapacidadesPicking: " + sql);
		}
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			affectedRows = stm.executeUpdate();
			Db.close(stm);
		} catch (Exception e) {
			logger.error("ERROR liberaCapacidadesPicking: ", e);
		}
		logger.info("END liberaCapacidadesPicking ROWS AFFECTED: " + String.valueOf(affectedRows));
	}

	public void dejaPedidosParaBorrar(Connection con) {
		int affectedRows = 0;
		logger.info("***	INIT dejaPedidosParaBorrar");
		String sql = "UPDATE BODBA.BO_PEDIDOS P SET P.ID_ESTADO = 69 WHERE P.ID_ESTADO = 68";
		if (logger.isDebugEnabled()) {
			logger.debug("SQL dejaPedidosParaBorrar: " + sql);
		}
		try {
			PreparedStatement stm = con.prepareStatement(sql);
			affectedRows = stm.executeUpdate();
			Db.close(stm);
		} catch (Exception e) {
			logger.error("ERROR dejaPedidosParaBorrar: ", e);
		}
		logger.info("END dejaPedidosParaBorrar ROWS AFFECTED: "	+ String.valueOf(affectedRows));
	}
}
