package cl.cencosud.informes.descuento.colaborador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.informes.descuento.colaborador.dto.ColaboradorDTO;
import cl.cencosud.informes.descuento.colaborador.dto.InformeColaboradorDTO;
import cl.cencosud.util.Db;
import cl.cencosud.util.Parametros;

public class InformeColaboradorDAO extends Db {
	private Connection con;
	protected static Logger logger = Logger.getLogger(InformeColaboradorDAO.class.getName());
	private static boolean isFirst = true;

	public InformeColaboradorDAO() throws Exception {
		String user = Parametros.getString("USER");
		String password = Parametros.getString("PASSWORD");
		String driver = Parametros.getString("DRIVER");
		String url = Parametros.getString("URL");
		con = conexion(user, password, driver, url);
		if(logger.isDebugEnabled())	logger.debug("		URL DB: "+ url);
	}

	public void closeConecciones() {
		Db.close(con);
	}

	public List getColaboradores() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List colaboradores = new ArrayList();
		try {
			String sql = "SELECT COL_RUT, COD_EMPRESA, EMPRESA FROM FODBA.FO_COLABORADOR ORDER BY COL_RUT";
			ps = con.prepareStatement(sql + " WITH UR");
			rs = ps.executeQuery();
			while (rs.next()) {
				ColaboradorDTO colaborador = new ColaboradorDTO();
				colaborador.setColRut(rs.getInt("COL_RUT"));
				colaborador.setCodEmpresa(rs.getInt("COD_EMPRESA"));
				colaborador.setEmpresa(rs.getString("EMPRESA"));
				colaboradores.add(colaborador);
			}
			if(logger.isDebugEnabled())	logger.debug("		Query: " + sql);
		} catch (SQLException e) {
			logger.error("Error: ", e);
		} finally {
			try {
				if (rs != null)	rs.close();
				if (ps != null) ps.close();
			} catch (Exception e) {
				logger.error("Error: ", e);
			}
		}
		return colaboradores;
	}

	public InformeColaboradorDTO getInformeColaborador(ColaboradorDTO colaborador, boolean isOne, boolean isNewYear) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		InformeColaboradorDTO informeColaborador = new InformeColaboradorDTO();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT COALESCE(SUM(B.PRECIO_LISTA*(B.CANT_SOLIC - B.CANT_FALTAN)), 0) AS COMPRA_ACUMULADA,");
			sql.append(" COALESCE(SUM((B.PRECIO_LISTA - B.PRECIO) * (B.CANT_SOLIC - B.CANT_FALTAN)), 0) AS DSCTO_ACUMULADO");
			sql.append(" FROM BODBA.BO_PEDIDOS A");
			sql.append(" INNER JOIN BODBA.BO_DETALLE_PEDIDO B ON A.ID_PEDIDO = B.ID_PEDIDO");
			sql.append(" INNER JOIN BODBA.BO_PROMOS_DETPED C ON B.ID_DETALLE = C.ID_DETALLE");
			sql.append(" WHERE A.RUT_CLIENTE = ? AND ");
			if(isOne && isNewYear){
				sql.append("MONTH(A.FCREACION) = (MONTH(CURRENT DATE) - 1) AND YEAR(A.FCREACION) = (YEAR(CURRENT DATE) - 1)");
			} else if(isOne) {
				sql.append("MONTH(A.FCREACION) = (MONTH(CURRENT DATE) - 1) AND YEAR(A.FCREACION) = YEAR(CURRENT DATE)");
			} else {
				sql.append("MONTH(A.FCREACION) = MONTH(CURRENT DATE) AND YEAR(A.FCREACION) = YEAR(CURRENT DATE)");
			}
			sql.append(" AND A.ID_ESTADO IN (3,4,5,6,7,8,9,10) AND C.PROMO_CODIGO = -1 ");
			ps = con.prepareStatement(sql + " WITH UR");
			ps.setInt(1, colaborador.getColRut());
			rs = ps.executeQuery();
			if (rs.next()) {
				if(rs.getInt("COMPRA_ACUMULADA") > 0){
					informeColaborador.setCompraAcumulada(rs.getInt("COMPRA_ACUMULADA"));
					informeColaborador.setDescuentoAcumlado(rs.getInt("DSCTO_ACUMULADO"));
					informeColaborador.setColaborador(colaborador);
				} else {
					informeColaborador = null;
				}
			}
			if(logger.isDebugEnabled() && isFirst) {
				logger.debug("		Query: " + sql);
				isFirst = false;
			}
		} catch (SQLException e) {
			logger.error("Error: ", e);
		} finally {
			try {
				if (rs != null)	rs.close();
				if (ps != null) ps.close();
			} catch (Exception e) {
				logger.error("Error: ", e);
			}
		}
		return informeColaborador;
	}
	
}
