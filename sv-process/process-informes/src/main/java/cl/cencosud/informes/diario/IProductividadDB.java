package cl.cencosud.informes.diario;

public interface IProductividadDB {
	 final static String NAME_TB_PRODUCTIVIDAD = "BODBA.IPRODUCTIVIDAD_DIARIO";
	 
	 /**
	  * desde un lunes hasta un domingo
      * (cant_pick + cant_pick_sust) / (segundos / 3600.0)) < ? se escribe de otra forma para evitar division por cero
      * tener presente las reglas de desigualdad
	  */
	 final static String SQL_PRODUCCIONPORLOCALCAJA = 
		 	"SELECT LOCAL_ID, " +
		 		  " DAYOFYEAR(FECHA) AS W," +
		 		  " MIN(FECHA) AS FECHA," +
             	  "(SUM(CANT_PICK) + SUM(CANT_PICK_SUST)) / DECIMAL( SUM(SEGUNDOS_CAJA)/ 3600.0, 10, 3) AS PH " +
             "FROM "+NAME_TB_PRODUCTIVIDAD +    
                 " WHERE FECHA >= ? AND FECHA < ? AND SEGUNDOS_CAJA > ?   " +
                 "  AND (CANT_PICK + CANT_PICK_SUST) * 3600 < ( ? * SEGUNDOS_CAJA)  AND TIPO_PICK = ?" +
                 "  GROUP BY LOCAL_ID, DAYOFYEAR(FECHA)";
	
	 /**
	  * (cant_pick + cant_pick_sust) / (segundos / 3600.0)) < ? se escribe de otra forma para evitar division por cero
	  * tener presente las reglas de desigualdad
	  */
     final static String SQL_PRODUCCIONPORPICKEADOR = 
			 "SELECT LOCAL_ID," +
				   " LOGIN," +
				   " DAYOFYEAR(FECHA) AS W," +
				   " FECHA AS FECHA, " +
				   " MIN(FECHA) AS FECHA, "+
              	   "(SUM(CANT_PICK) + SUM(CANT_PICK_SUST)) / DECIMAL(SUM(SEGUNDOS) / 3600.0, 10, 3) AS PH "+
              " FROM "+NAME_TB_PRODUCTIVIDAD+" INNER JOIN BODBA.BO_USUARIOS ON ID_USUARIO = USUARIO_ID "+
              "  WHERE FECHA >= ? AND FECHA < ? AND SEGUNDOS > ?  "+
              "   AND (CANT_PICK + CANT_PICK_SUST) * 3600 < ( ? * SEGUNDOS) "+
              "   AND TIPO_PICK = ? "+
              " GROUP BY LOCAL_ID, LOGIN, DAYOFYEAR(FECHA),FECHA  "+
              " ORDER BY LOCAL_ID, LOGIN, W ";
	 
	 final static String SQL_PRODUCCIONPORLOCAL = 
			 "SELECT LOCAL_ID, " +
				   " DAYOFYEAR(FECHA) AS W, " +
				   " MIN(FECHA) AS FECHA, "+
	               "(SUM(CANT_PICK) + SUM(CANT_PICK_SUST)) / DECIMAL(SUM(SEGUNDOS) / 3600.0, 10, 3) AS PH "+
	            " FROM "+NAME_TB_PRODUCTIVIDAD+" "+
	            " 	WHERE FECHA >= ? AND FECHA < ? AND SEGUNDOS > ?   "+
	            "  	 AND (CANT_PICK + CANT_PICK_SUST) * 3600 < ( ? * SEGUNDOS)  AND TIPO_PICK = ? "+
	            " GROUP BY LOCAL_ID, DAYOFYEAR(FECHA) ";
	 
	 final static String SQL_GETPORLOCAL = 
			 "SELECT LOCAL_ID, " +
				"	NOMBRE, " +
				"   APELLIDO, " +
				"   APELLIDO_MAT, "+
	            " 	SUM(CANT_PICK) AS CANT_PICK, " +
	            "	SUM(CANT_PICK_SUST) AS CANT_PICK_SUST, "+
	            " 	SUM(CANT_FALT) AS CANT_FALT, " +
	            "	SUM(CANT_FALT_NO_SUST) AS CANT_FALT_NO_SUST "+
	            " FROM BODBA.BO_USUARIOS  INNER JOIN " +NAME_TB_PRODUCTIVIDAD+" ON ID_USUARIO = USUARIO_ID "+
	            " 	WHERE FECHA = ?"+
	            "  		AND SEGUNDOS > ? AND (CANT_PICK + CANT_PICK_SUST) * 3600 < ( ? * SEGUNDOS) "+
	            " GROUP BY LOCAL_ID, NOMBRE, APELLIDO, APELLIDO_MAT "+
	            " ORDER BY LOCAL_ID, NOMBRE, APELLIDO, APELLIDO_MAT ";
	 
}
