package cl.cencosud.archivossap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.util.Db;
import cl.cencosud.util.LogUtil;

public class DbCarga extends Db {

	static {
		LogUtil.initLog4J();
	}

	static Logger logger = Logger.getLogger(DbCarga.class);

	public static List productos(Connection con, int idLocal)
			throws SQLException {

		String sql = "select distinct bpro.cod_prod1, bar.cod_barra, loc.cod_local, bpro.id_catprod "
				+ "from bo_productos bpro inner join fodba.fo_productos fpro on fpro.pro_id_bo = bpro.id_producto "
				+ "                      inner join fodba.fo_precios_locales fpre on fpre.pre_pro_id = fpro.pro_id "
				+ "                      inner join bo_locales loc on loc.id_local = fpre.pre_loc_id "
				+ "                      inner join bo_codbarra bar on bar.id_producto = bpro.id_producto "
				+ "                      inner join fodba.fo_productos_categorias fpc on fpc.prca_pro_id = fpro.pro_id "
				+ "                      inner join fodba.fo_categorias fsubcat on fsubcat.cat_id = fpc.prca_cat_id "
				+ "                      inner join fodba.fo_catsubcat fsub on fsub.subcat_id = fsubcat.cat_id "
				+ "                      inner join fodba.fo_categorias fcat on fcat.cat_id = fsub.cat_id "
				+ "where bpro.mix_web = 'S' and loc.id_local = ? and fpre.pre_estado = 'A' "
				+ " and fpro.pro_estado = 'A' "
				+ " and fsubcat.cat_estado = 'A' and fcat.cat_estado = 'A'";

		PreparedStatement ps = con.prepareStatement(sql + " with ur ");
		ps.setInt(1, idLocal);
		ResultSet rs = ps.executeQuery();
		List lista = new ArrayList();
		while (rs.next()) {
			String[] producto = { rs.getString("cod_prod1"),
					rs.getString("cod_barra"), rs.getString("cod_local"),
					rs.getString("id_catprod") };
			lista.add(producto);
		}
		try {
			rs.close();
		} catch (SQLException e) {
			logger.error("Problema SQL close " + e.getMessage());
		}
		return lista;
	}
}
