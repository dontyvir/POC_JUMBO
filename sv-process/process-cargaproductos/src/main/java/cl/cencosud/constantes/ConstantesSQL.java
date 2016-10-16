package cl.cencosud.constantes;

public class ConstantesSQL {

	public static final String CP_UPD_BO_PRECIOS = "update bo_precios set PRECIO_ANTIGUO = PREC_VALOR, FECHA_PRECIO_ANTIGUO = FECHA_PRECIO_NUEVO, FECHA_PRECIO_NUEVO = CURRENT_DATE, ALERTA_CAMBIO = 1, PRECIO_NUEVO = ?, PREC_VALOR = ? where id_producto = ? and id_local = ? ";

	public static final String CP_UPD_FO_PRECIOS_LOCLAES = "update fodba.fo_precios_locales set pre_valor = ? where pre_pro_id = ? and pre_loc_id = ? ";

	public static final String CP_SEL_DB = "SELECT ID_PRODUCTO, PREC_VALOR, PRO_ID, FECHA_PRECIO_NUEVO FROM bodba.bo_precios pre LEFT OUTER JOIN fodba.fo_productos pro ON pre.id_producto = pro.pro_id_bo WHERE id_local = ? and cod_prod1 = ? and umedida = ? ";

	public static final String CP_UPD_MAESTRO_BO_PRECIOS = "update bo_precios set PRECIO_ANTIGUO = PREC_VALOR, FECHA_PRECIO_ANTIGUO = FECHA_PRECIO_NUEVO, FECHA_PRECIO_NUEVO = CURRENT_DATE, PRECIO_NUEVO = ?, PREC_VALOR = ? where id_producto = ? and prec_valor <> ? ";

	public static final String CP_UPD_MAESTRO_FO_PRECIOS_LOCLAES = "update fodba.fo_precios_locales set pre_valor = ? where pre_pro_id = ? and pre_valor <> ? ";
	
	/* 
	 * Tracking
	 * */
	public static final String CP_FO_PRO_TRACKING = "insert into fodba.fo_pro_tracking (TRA_PRO_ID, TRA_BO_PRO_ID, TRA_FEC_CREA, TRA_USUARIO, TRA_TEXTO) values(?,?,CURRENT TIMESTAMP,?,?)";
		
}


