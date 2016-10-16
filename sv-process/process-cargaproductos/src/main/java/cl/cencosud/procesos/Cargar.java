package cl.cencosud.procesos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Inventario;
import cl.cencosud.beans.Local;
import cl.cencosud.beans.Mail;
import cl.cencosud.beans.Precio;
import cl.cencosud.beans.Producto;
import cl.cencosud.facade.CargarPreciosFacade;
import cl.cencosud.util.Db;
import cl.cencosud.util.LogUtil;
//import cl.cencosud.util.LogUtil;
import cl.cencosud.util.Parametros;

public class Cargar {
    
    static {
        
        LogUtil.initLog4J();
        
    }
    
	static Logger logger = Logger.getLogger(Cargar.class);
    
    //private static Logger logger = Logger.getLogger(Cargar.class);

    private static int RANGO = 30000;

    public static void main( String[] args ) {

        logger.info("****************************** Iniciamos proceso del día Martes: " + fecha() + "******************************************************");
       
        
        try {
            long iniTotal = System.currentTimeMillis();
            String user = Parametros.getString("USER");
            String password = Parametros.getString("PASSWORD");
            String driver = Parametros.getString("DRIVER");
            String url = Parametros.getString("URL");
            Connection con = DbCarga.conexion(user, password, driver, url);
            System.out.println("CON : " + url + " - " + con);

            Cargar cargar = new Cargar();

            if ( args.length > 0 && args[0].equals("fin") ) {
                logger.info("Se ejecuta sólo la parte final");
                cargar.finalizacion(con);
                cargar.cambiaPreciosLocales(con);
            } else if ( args.length > 0 && args[0].equals("desbloquear") ) {
                logger.info("Se ejecuta desbloquearPublicar");
                cargar.desbloquearPublicar(con);
            } else {
                logger.info("Se ejecuta la parte inicial y final");
                cargar.inicio(con);
                cargar.finalizacion(con);
                cargar.cambiaPreciosLocales(con);
            }

            logger.info("Tiempo total de proceso: " + calculaTiempo(iniTotal));
        } catch (Exception e) {
            logger.error("Exception:"+e.getMessage(), e);
            //throw e;
        }
    }

    /**
     * @param con
     * @throws SQLException
     * 
     */
    private void finalizacion( Connection con ) throws SQLException {

        logger.info("**********inicio proceso de finalizacion***************");
        long ini = System.currentTimeMillis();
        actualizarBloqueoPrecios(con);
        logger.info("/********************Fin actualizarBloqueoPrecios tiempo: " + calculaTiempo(ini));

        ini = System.currentTimeMillis();
        actualizarPreciosLocales(con);
        logger.info("/******************Fin actualizarPreciosLocales tiempo: " + calculaTiempo(ini));

        ini = System.currentTimeMillis();
        despublicarProductos(con);
        logger.info("/***************Fin despublicarProductos tiempo: " + calculaTiempo(ini));

        ini = System.currentTimeMillis();
        despublicarProductosEnLocal(con);
        logger.info("Fin despublicarProductosEnLocal tiempo: " + calculaTiempo(ini));

        logger.info("********************************FIN PROCESO*****************************************");
    }

    public void inicio( Connection con ) throws Exception {

        long ini = System.currentTimeMillis();
        logger.info("**********inicio de Proceso***************");
        // respaldarTablas(con);
        // System.out.println("tiempo: " + ((System.currentTimeMillis() - ini) /
        // 1000.0));

        String archivo_categorias = Archivo.nombreArchivoCSV("UCTS500", "CSV");
        logger.info("archivo_categorias " + archivo_categorias);
        try{
        actualizarCategorias(con, archivo_categorias);
        }catch (Exception e) {
        	logger.warn("no se pudieron cargar las Categorias, " + e);
		}
        System.gc();
        logger.info("**********Fin actualizarCategorias tiempo: " + calculaTiempo(ini) + "****************");

        ini = System.currentTimeMillis();
        String archivo_productos = Archivo.nombreArchivoCSV("SPDC500", "CSV");
        String archivo_barras = Archivo.nombreArchivoCSV("UBRS500", "CSV");
        logger.debug("archivo_productos:" + archivo_productos);
        logger.debug("archivo_barras:" + archivo_barras);
        try{
        actualizarProductos(con, archivo_productos, archivo_barras);
        }catch (Exception e) {
        	logger.warn("no se pudieron cargar los productos, " + e);
		}
        System.gc();
        logger.info("**********Fin actualizarProductos tiempo: " + calculaTiempo(ini) + "**********************");

        ini = System.currentTimeMillis();
        try{
        actualizarBarras(con, archivo_barras);
        }catch (Exception e) {
        	logger.warn("no se pudieron cargar los productos, " + e);
		}
        System.gc();
        logger.info("******************Fin actualizarBarras tiempo: " + calculaTiempo(ini) + "**********************");

        ini = System.currentTimeMillis();
        List locales = DbCarga.locales(con);
        for ( int i = 0; i < locales.size(); i++ ) {
          Local local = (Local) locales.get(i);
            logger.debug("Vamos a actualizar local " + local.getNombre());
          String archivo_inventario = Archivo.nombreArchivoCSV("SST" + local.getCodigo(), "CSV");
            logger.debug("archivo_inventario:" + archivo_inventario);
            String archivo_precios = Archivo.nombreArchivo("JCC" + local.getCodigo());
            logger.debug("archivo_precios:" + archivo_precios);
            actualizarPrecios(con, local.getId(), archivo_precios, archivo_inventario);
            System.gc();
        }

        mantieneBloqueos(con);

        logger.info("*****************Fin actualizarPrecios en los locales tiempo: " + calculaTiempo(ini) + "****************");
    }

    /**
     * @param ini
     * @return
     */

    public static String calculaTiempo( long ini ) {

        long milisegundos = ( System.currentTimeMillis() - ini );
        long hora = milisegundos / 3600000;
        long restohora = milisegundos % 3600000;
        long minuto = restohora / 60000;
        long restominuto = restohora % 60000;
        long segundo = restominuto / 60000;
        String out = ( hora > 9 ) ? "" + hora : "0" + hora;
        out += " : ";
        out += ( minuto > 9 ) ? "" + minuto : "0" + minuto;
        out += " : ";
        out += ( segundo > 9 ) ? "" + segundo : "0" + segundo;
        return out;
    }

    /**
     * Método que matiene los bloqueos que se indican en la columna "bloqueado"
     * de la tabla bo_precios. Esta columna tiene mayor prioridad que la columna
     * bloq_compra. La columna "bloqueado" indica 1 bloqueado y 0 manda lo que
     * indique la columna bloq_compra
     * 
     * @param con
     * @throws SQLException
     */
    private void mantieneBloqueos( Connection con ) throws SQLException {

        PreparedStatement ps = null;
        try {
            
            String sql = "update bodba.bo_precios set bloq_compra = 'SI' where bloqueado = 1";
            
            ps = con.prepareStatement(sql);
            ps.executeUpdate();            

        } catch (Exception e) {
            logger.error("* Error en mantieneBloqueos:" + e.getMessage(), e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            ps = null;
        }
   }
   /**
    * @param con
    * @throws SQLException
    */
    /*
   private void respaldarTablas(Connection con) throws SQLException {
      System.out.println("Respaldo de tablas");
      Date hoy = new Date();

      SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_hhmm");
      String fecha = df.format(hoy);

      String[] tablas = new String[8];

      tablas[0] = "bodba.bo_catprod";
      tablas[1] = "bodba.bo_productos";
      tablas[2] = "bodba.bo_codbarra";
      tablas[3] = "bodba.bo_precios";
      tablas[4] = "fodba.fo_productos";
      tablas[5] = "fodba.fo_cod_barra";
      tablas[6] = "fodba.fo_precios_locales";
      tablas[7] = "fodba.fo_pro_tracking";

      for (int i = 0; i < tablas.length; i++) {
         System.out.println("Respaldando " + tablas[i] + "_" + fecha);
         String sql = "create table " + tablas[i] + "_" + fecha + " like " + tablas[i];
         System.out.println(sql);
         Statement st = con.createStatement();
         st.execute(sql);

         sql = "insert into " + tablas[i] + "_" + fecha + " select * from " + tablas[i];
         System.out.println(sql);
         st = con.createStatement();
         st.execute(sql);
        }
    }    
//*/
    /**
     * @param con
     * @throws SQLException
     */
    private void despublicarProductos( Connection con ) throws SQLException {

        logger.info("***************Despublicar productos***************");// (ver
                                                                                  // fo_pro_tracking)");
        /*
         * guarda en la tabla fo_pro_tracking los productos despublicados
         */
      String sqlLog1 = "insert into fodba.fo_pro_tracking "
            + "(TRA_BO_PRO_ID, TRA_PRO_ID,TRA_FEC_CREA, TRA_USUARIO,TRA_TEXTO) "
            + "select  bp.id_producto,  fp.pro_id,  CURRENT TIMESTAMP, "
                + "        'Interfaces Carga Sap', 'Despublica producto descontinuado' " + "from bodba.BO_PRODUCTOS bp "
            + "inner join fodba.FO_PRODUCTOS fp on fp.pro_id_bo = bp.id_producto AND fp.pro_estado = 'A' "
            + "WHERE bp.mix_web = 'S' AND (bp.estado = 0 OR bp.estadoactivo = '0') ";

        /* Despublico si viene de SAP despublicado */
      String sql1 = "merge into fodba.fo_productos as fproductos                                    "
            + "using (select  id_producto                                                             "
            + "       from bodba.BO_PRODUCTOS                                                     "
            + "       WHERE mix_web='S' AND (estado = 0 OR estadoactivo = '0') )as bproductos         "
            + "on (fproductos.pro_id_bo = bproductos.id_producto and fproductos.pro_estado = 'A')     "
            + "WHEN MATCHED THEN                                                                      "
                + "      UPDATE SET fproductos.pro_estado = 'D', fproductos.pro_id_desp = 0               ";

        con.setAutoCommit(false);
        PreparedStatement ps = null;
        int n = 0;
        try {
            ps = con.prepareStatement(sqlLog1);
            n = ps.executeUpdate();
            logger.debug("insertados en logs = " + n);
            ps = con.prepareStatement(sql1);
            n = ps.executeUpdate();
            logger.debug("despublicados = " + n);
            con.commit();
        } catch (SQLException e) {
            logger.debug("* Error despublicarProductos1:" + e.getMessage(), e);
            con.rollback();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            ps = null;
            con.setAutoCommit(true);
        }

        String sqlLog2 = "insert into fodba.fo_pro_tracking                                                        "
                + "(TRA_BO_PRO_ID, TRA_PRO_ID,TRA_FEC_CREA, TRA_USUARIO,TRA_TEXTO)                                 "
                + "SELECT p.id_producto, fp.pro_id, CURRENT TIMESTAMP,                                             "
                + "       'Interfaces Carga Sap', 'Despublica producto bloqueado para compra en todos sus locales' "
                + "FROM bodba.BO_PRODUCTOS p                                                                       "
                + "JOIN fodba.FO_PRODUCTOS fp ON fp.pro_id_bo = p.id_producto AND fp.pro_estado = 'A'              "
                + "WHERE p.mix_web = 'S' and p.con_precio = 'N' and fp.evitar_pub_des != 'S'                       ";

        /*
         * Despublico el producto en todos sus locales si no viene con precio en
         * todos los locales (es decir si viene bloqueado), se valida
         * evitar_pub_des
         */
        String sql2 = "merge into fodba.fo_productos as fp                                                  "
                + "using ( SELECT id_producto                                                               "
                + "              FROM bodba.BO_PRODUCTOS                                                    "
                + "              WHERE mix_web = 'S' and con_precio='N') as bp                              "
                + "on ( fp.pro_id_bo = bp.id_producto AND fp.pro_estado = 'A' and fp.evitar_pub_des != 'S') "
                + "WHEN MATCHED THEN                                                                        "
                + "      UPDATE SET fp.pro_estado = 'D', fp.pro_id_desp = 0                                 ";

        try {
            ps = con.prepareStatement(sqlLog2);
            n = ps.executeUpdate();
            logger.debug("insertados en logs = " + n);
            ps = con.prepareStatement(sql2);
            n = ps.executeUpdate();
            logger.debug("despublicados = " + n);
            con.commit();
        } catch (SQLException e) {
            logger.error("* Error despublicarProductos2:" + e.getMessage(), e);
            con.rollback();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            ps = null;
            con.setAutoCommit(true);
        }
    }

    /**
     * @param con
     * @throws SQLException
     */
    private void despublicarProductosEnLocal( Connection con ) throws SQLException {

        logger.info("Despublicar productos en cada local");// (ver
                                                                  // fo_pro_tracking)");
        con.setAutoCommit(false);
        PreparedStatement ps = null;

      String sqlLog1 = "insert into fodba.fo_pro_tracking "
            + "(TRA_BO_PRO_ID, TRA_PRO_ID,TRA_FEC_CREA, TRA_USUARIO,TRA_TEXTO) "
            + "SELECT  p.id_producto, fp.pro_id, CURRENT TIMESTAMP, "
                + "'Interfaces Carga Sap', 'Despublica producto bloqueado para compra en local: '  || bloc.cod_local "

            + "FROM bodba.BO_PRODUCTOS p "
            + "inner join fodba.FO_PRODUCTOS fp ON fp.pro_id_bo = p.id_producto "
            + "inner join bodba.BO_PRECIOS bpre on bpre.id_producto = p.id_producto "
            + "inner join bodba.BO_LOCALES bloc on bloc.id_local = bpre.id_local "
            + "inner join fodba.fo_precios_locales fpre on fpre.pre_pro_id = fp.pro_id and fpre.pre_loc_id = bloc.id_local "
            + "WHERE p.mix_web = 'S' and p.con_precio = 'S' and fp.pro_estado = 'A' "
            + "  and bpre.bloq_compra = 'SI' and fpre.pre_estado = 'A' and fp.evitar_pub_des != 'S'";

        /*
         * Despublico el producto en el local donde no tiene precio, valido
         * evitar_pub_des
         */
      String sql1 = "merge into fodba.fo_precios_locales as fpre      "
            + "using (select fpro.pro_id, bpre.id_local             "
            + "        FROM bodba.BO_PRODUCTOS bpro                 "
            + "        inner join fodba.FO_PRODUCTOS fpro on fpro.pro_id_bo = bpro.id_producto      "
            + "        inner join bodba.BO_PRECIOS bpre on bpre.id_producto = bpro.id_producto      "
            + "        where bpro.mix_web = 'S' and bpro.con_precio = 'S' and fpro.pro_estado = 'A' "
            + "          and bpre.bloq_compra = 'SI' and fpro.evitar_pub_des != 'S') as precios     "
            + "on (fpre.pre_pro_id = precios.pro_id and fpre.pre_loc_id = precios.id_local)         "
            + "WHEN MATCHED THEN                                      "
                + "             UPDATE SET fpre.pre_estado = 'D' ";

        int n = 0;
        try {
            ps = con.prepareStatement(sqlLog1);
            n = ps.executeUpdate();
            logger.debug("insertados en logs = " + n);
            ps = con.prepareStatement(sql1);
            n = ps.executeUpdate();
            logger.debug("despublicados = " + n);
            con.commit();
        } catch (Exception e) {
            logger.error("* Error despublicarProductosEnLocal:" + e.getMessage(), e);            
            con.rollback();
        } finally {
            if (ps != null) {
                ps.close();                
            }
            ps = null;
            con.setAutoCommit(true);
        }
    }

    /**
     * @param con
     * @throws SQLException
     */
    private void actualizarBloqueoPrecios( Connection con ) throws SQLException {

        logger.info("***********Inicio de Actualizar bloqueo de precios**************");
        String pathIntentos = Parametros.getString("PATH_INTENTOS");
        String pathMinutos = Parametros.getString("PATH_MINUTOS");
        int intentos = Integer.parseInt(pathIntentos);
        int minutos = Integer.parseInt(pathMinutos);
        String destinatarios = Parametros.getString("devolucion.destinatario");
        int nextProcess = 1;
        /*
         * actualiza la columna tot_precio con la cantidad de precios de cada
         * id_producto */
      String sql1 = " merge into bodba.bo_productos as p " + 
                    " using (select id_producto, count(*) as tot " +
                    " from bodba.bo_precios " +
                    " group by id_producto)  as precio " + 
                    " on (precio.id_producto = p.id_producto) " +
                    " WHEN MATCHED THEN " + 
                    " UPDATE SET p.tot_precios = precio.tot ";  
       

      /*
       * actualiza la columa tot_precio_bloq con la cantidad de precios bloqueados para cada id_producto
       */
     
      String sql2 = " merge into bodba.bo_productos as p " +
                    " using (select id_producto, count(*) as tot " +
                    " from bodba.bo_precios " +
                    " where bloq_compra ='SI' " +
                    " group by id_producto)  as precio " +
                    " on (precio.id_producto = p.id_producto) " +
                    " WHEN MATCHED THEN " +
                    " UPDATE SET p.tot_precios_bloq = precio.tot ";

      /*
       * Vuelve a cero cuando se desbloquean todos
       */
      
      String sql3 = " merge into bodba.bo_productos as p " +
                    " using ( select p.id_producto, count(p.id_producto) as tot " +
                    " from bodba.bo_precios p where p.bloq_compra ='NO' " +
                    " group by p.id_producto " +
                    " having count(p.id_producto) = ( select count(pp.id_producto) " +
                    " from bodba.bo_precios pp " +
                    " where p.id_producto = pp.id_producto)) as precio " +
                    " on (precio.id_producto = p.id_producto) " +
                    " WHEN MATCHED THEN " +
                    " UPDATE SET p.tot_precios_bloq = 0 ";

      /*
       * actualiza la columna con_precio, tiene precio si los precios bloquedos son menos que el total de precios
       */
      
       String sql4 = "update bodba.bo_productos " +
                    "set con_precio = (case when tot_precios_bloq < tot_precios then 'S' else 'N' end)";

      
      
      boolean ok1 = false;
      int x=1;
      do {
          try {
               if(nextProcess == 1){
                    logger.debug(" SQL1 intento:" + x);
                    PreparedStatement ps1 = con.prepareStatement(sql1);
                    ps1.executeUpdate();
                    ps1.close();
                    ok1 = true;
                    nextProcess = 2;
                    logger.info("******Actualiza la columa tot_precio_bloq con la cantidad de precios bloqueados para cada id_producto*****");
                }

            } catch (Exception e) {
                logger.error("* Error actualizarBloqueoPrecios1:" + e.getMessage());
                Mail mail = new Mail();
                mail.setFsm_destina(destinatarios);
                mail.setFsm_estado("0");
                mail.setFsm_subject("Error actualizarBloqueoPrecios SQL1");
                mail.setFsm_idfrm("Carga");
                mail.setFsm_remite("cargas productos");
                mail.setFsm_data("Fallo SQL1 ejecutar en servidor Copihue la sgte Sh: /home/was/programas_consolas/carga/bin/cargar_finalizacion.sh");
                if ( x == intentos ) {
                    addMail(mail, con);
                    logger.info("*******-Se envia mail de ERROR SQL1-**********");
                }
                try {
                    Thread.sleep(minutos);
                } catch (InterruptedException ex) {
                    logger.error("fallo sleep", e);
                }

            }
            x++;
        } while ( x <= intentos && !ok1 );

        boolean ok2 = false;
        int x2 = 1;
        do {
            try {

                if ( nextProcess == 2 ) {
                    logger.debug(" SQL2 intento:" + x2);
                    PreparedStatement ps2 = con.prepareStatement(sql2);
                    ps2.executeUpdate();
                    ps2.close();
                    ok2 = true;
                    nextProcess = 3;
                    logger.info("*******Vuelve a cero cuando se desbloquean todos***********");
                }
            } catch (Exception e) {
                logger.error("* Error actualizarBloqueoPrecios2:" + e.getMessage());
                Mail mail = new Mail();
                mail.setFsm_destina(destinatarios);
                mail.setFsm_estado("0");
                mail.setFsm_subject("Error actualizarBloqueoPrecios SQL2");
                mail.setFsm_idfrm("Carga");
                mail.setFsm_remite("cargas productos");
                mail.setFsm_data("Fallo SQL2 ejecutar en servidor Copihue la sgte Sh: /home/was/programas_consolas/carga/bin/cargar_finalizacion.sh");
                     if(x2 == intentos){ 
                    addMail(mail, con);
                    logger.info("*******-Se envia mail de ERROR SQL2-**********");
                }
                try {
                    Thread.sleep(minutos);
                } catch (InterruptedException ex) {
                    logger.error("fallo sleep 2", e);
                }

            }
            x2++;
        } while ( x2 <= intentos && !ok2 );

        boolean ok3 = false;
        int x3 = 1;
        do {
            try {
                if ( nextProcess == 3 ) {
                    logger.info(" SQL3 intento:" + x3);
                    PreparedStatement ps3 = con.prepareStatement(sql3);
                    ps3.executeUpdate();
                    ps3.close();
                    ok3 = true;
                    nextProcess = 4;
                    logger.info("*******Actualiza la columna con_precio, tiene precio si los precios bloquedos son menos que el total de precios*******");
                }
            } catch (Exception e) {
                logger.error("* Error actualizarBloqueoPrecios3:" + e.getMessage());
                Mail mail = new Mail();
                mail.setFsm_destina(destinatarios);
                mail.setFsm_estado("0");
                mail.setFsm_subject("Error actualizarBloqueoPrecios SQL3");
                mail.setFsm_idfrm("Carga");
                mail.setFsm_remite("cargas productos");
                mail.setFsm_data("Fallo SQL3 ejecutar en servidor Copihue la sgte Sh: /home/was/programas_consolas/carga/bin/cargar_finalizacion.sh");
                      if(x3 == intentos){ 
                    addMail(mail, con);
                    logger.info("*******-Se envia mail de ERROR SQL3-**********");
                }
                nextProcess = 4;
                try {
                    Thread.sleep(minutos);
                } catch (InterruptedException ex) {
                    logger.debug("fallo sleep 3" + e);
                }

            }
            x3++;
        } while ( x3 <= intentos && !ok3 );

        boolean ok4 = false;
        int x4 = 1;
        do {
            try {
                if ( nextProcess == 4 ) {
                    logger.debug(" SQL4 intento:" + x4);
                    PreparedStatement ps4 = con.prepareStatement(sql4);
                    ps4.executeUpdate();
                    ps4.close();
                    ok4 = true;
                    nextProcess = 4;
                    logger.info("**********Actualiza la columna tot_precio con la cantidad de precios de cada id_producto*********");
                }
            } catch (Exception e) {
                logger.error("* Error actualizarBloqueoPrecios4:" + e.getMessage());
                Mail mail = new Mail();
                mail.setFsm_destina(destinatarios);
                mail.setFsm_estado("0");
                mail.setFsm_subject("Error actualizarBloqueoPrecios SQL4");
                mail.setFsm_idfrm("Carga");
                mail.setFsm_remite("cargas productos");
                mail.setFsm_data("Fallo SQL4 ejecutar en servidor Copihue la sgte Sh: /home/was/programas_consolas/carga/bin/cargar_finalizacion.sh");
                     if(x4 == intentos){ 
                    addMail(mail, con);
                    logger.info("*******-Se envia mail de ERROR SQL4-**********");
                }
                try {
                    Thread.sleep(minutos);
                } catch (InterruptedException ex) {
                    logger.error("fallo sleep 4", e);
                }

            }
            x4++;
        } while ( x4 <= intentos && !ok4 );

    }

    /**
     * Actualiza los precios de fo_precios_locales, todos los precios aunque el
     * producto este bloqueado.
     * 
     * @param con
     * @throws SQLException
     */
    private void actualizarPreciosLocales( Connection con ) throws SQLException {

        logger.info("***********Inicio actualizar precios locales*************");
        // actualiza todos los precios
        String sql = "merge into fodba.fo_precios_locales fprecios                                             "
                + "using (select fproductos.pro_id as pro_id, bprecios.id_local as id_local, bprecios.prec_valor as valor "
                + "       from  fodba.fo_productos fproductos                                                  "
                + "       inner join bodba.bo_precios bprecios on bprecios.id_producto  = fproductos.pro_id_bo "
                + "       ) precios                                                                            "
                + "on (fprecios.pre_pro_id = precios.pro_id and fprecios.pre_loc_id = precios.id_local)        "
                + "WHEN MATCHED THEN                                                                          "

                + "   UPDATE SET fprecios.pre_valor = precios.valor                                            "
                + "WHEN NOT MATCHED THEN                                                                       "
            + "   INSERT (PRE_PRO_ID, PRE_LOC_ID, PRE_VALOR, PRE_COSTO, PRE_STOCK, PRE_ESTADO)             "
            + "   VALUES  (precios.pro_id, precios.id_local, precios.valor, 0, 0, 'A') ";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();

        } catch (Exception e) {
            logger.error("*** Error en actualizarPreciosLocales:" + e.getMessage(), e);
        }
        
    }

    /**
     * Actualiza las categorias, actualiza sus atributos o inserta nuevas
     * categorias según archivo SAP
     * 
     * @throws IOException
     * @throws SQLException
     */
    private void actualizarCategorias( Connection con, String archivo ) throws IOException, SQLException {

        logger.error("Iniciando actualizacion de categorias");
        String values[][] = Archivo.cargarCSV(archivo);

        String sqlUpdate = "update bodba.BO_CATPROD set ID_CATPROD_PADRE=?, DESCAT=?, CAT_NIVEL=?, CAT_TIPO=? where ID_CATPROD = ?";
        String sqlInsert = "insert into bodba.BO_CATPROD (ID_CATPROD, ID_CATPROD_PADRE, DESCAT, CAT_NIVEL, CAT_TIPO, ESTADOACTIVO) values(?,?,?,?,?,?)";
        PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
        PreparedStatement psInsert = con.prepareStatement(sqlInsert);
        for ( int i = 0; i < values.length; i++ ) {
            psUpdate.setString(1, values[i][1]);
            psUpdate.setString(2, values[i][2]);
            psUpdate.setInt(3, Integer.parseInt(values[i][3]));
            psUpdate.setString(4, values[i][4]);
            psUpdate.setString(5, values[i][0]);

            int resultado = psUpdate.executeUpdate();
            // si resulta es cero la categoria no existe y se debe insertar
            if ( resultado == 0 ) {
                psInsert.setString(1, values[i][0]);
                psInsert.setString(2, values[i][1]);
                psInsert.setString(3, values[i][2]);
                psInsert.setInt(4, Integer.parseInt(values[i][3]));
                psInsert.setString(5, values[i][4]);
                psInsert.setString(6, "1");
                psInsert.executeUpdate();
            }
        }
        if ( psUpdate != null ) {
            psUpdate.close();
        }
        if ( psInsert != null ) {
            psInsert.close();
        }
        
        psUpdate = null;
        psInsert = null;
    }

    /**
     * Actualiza los atributos de cada producto, en la base de datos, según los
     * datos que vienen en el archivo SAP, inserta un nuevo producto cuando el
     * código del producto no existe. (Nuevos productos con código existente
     * pero distinta unidad de medida no se insertan en este método (ver
     * actualizaBarras))
     * 
     * @param con
     * @param archivo_productos
     * @throws IOException
     * @throws SQLException
     */
    private void actualizarProductos( Connection con, String archivo_productos, String archivo_barras ) throws IOException, SQLException {

        logger.info("iniciando actualizacion de productos");
        List barrasCSV = Archivo.cargarBarrasCSV(archivo_barras);
        Hashtable productosCSV = Archivo.cargarProductosCSV(archivo_productos);

      if (productosCSV.size() == 0)
         return;
      
      Hashtable productosDB = DbCarga.productos(con);

      String sqlUpdate = "update BODBA.BO_PRODUCTOS set ID_CATPROD = ?, COD_PROD2 = ?,"
            + "DES_CORTA = ?, DES_LARGA = ?, ESTADO = ?, MARCA = ?, COD_PROPPAL = ?, ORIGEN = ?, UN_BASE = ?, "
            + "EAN13 = ?, UN_EMPAQUE = ?, ATRIB9 = ?, ATRIB10 = ?, FCARGA = current timestamp, ESTADOACTIVO = '1' "
            + "where COD_PROD1 = ?";

      String sqlInsert = "insert into BODBA.BO_PRODUCTOS(ID_CATPROD, COD_PROD2,            "
            + "DES_CORTA, DES_LARGA, ESTADO, MARCA, COD_PROPPAL, ORIGEN, UN_BASE,            "
            + "EAN13, UN_EMPAQUE, ATRIB9, ATRIB10, cod_prod1, uni_med, FCARGA, ESTADOACTIVO) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, current timestamp, '1')                ";

        PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
        PreparedStatement psInsert = con.prepareStatement(sqlInsert);

        int c = 0; // copiar
        int n = 0; // nuevo (insertar)
        int a = 0; // actualizar
        int aa = 0; // actualizar
        int b = 0; // no es necesario actualizar
        int j = 0;
        for ( int i = 0; i < barrasCSV.size(); i++ ) {
            if ( j * RANGO == i ) {
                logger.debug("actualizarProductos - rango cargarBarrasCSV: " + i);
                j++;
            }
            // cada linea del archivo de barras CSV
         Barra barraCSV = (Barra) barrasCSV.get(i);
            // se busca el producto correspondiente en el archivo CSV
         Producto productoCSV = (Producto) productosCSV.get(barraCSV.getCodigoProducto());
            // se busca el producto correspondiente en la base de datos
         Hashtable unidades = (Hashtable) productosDB.get(barraCSV.getCodigoProducto());

            if ( unidades != null && unidades.get(barraCSV.getUnidad()) == null ) {
                /*
                 * el producto existe en la base de datos pero con otras
                 * unidades de medidas => copiar
                 */
                c++;
            Producto copia = (Producto) unidades.elements().nextElement();

                psInsert.setString(1, copia.getCodigoCategoria());
                psInsert.setString(2, copia.getCodigo2());
                psInsert.setString(3, copia.getNombre());
                psInsert.setString(4, copia.getDescripcion());
                psInsert.setInt(5, copia.getEstado());
                psInsert.setString(6, copia.getMarca());
                psInsert.setString(7, copia.getCodigoProPpal());
                psInsert.setString(8, copia.getOrigen());
                psInsert.setString(9, copia.getUnidadBase());
                psInsert.setString(10, barraCSV.getCodigo());
                psInsert.setString(11, copia.getUnidadEmpaque());
                psInsert.setString(12, copia.getAtributo9());
                psInsert.setString(13, copia.getAtributo10());
                psInsert.setString(14, copia.getCodigo());
                psInsert.setString(15, barraCSV.getUnidad());

                try {
                    psInsert.executeUpdate();

                } catch (SQLException e) {
                    logger.error("* Error en copia: " + copia.getCodigo() + " unidad: " + barraCSV.getUnidad() + ". Error:" + e.getMessage());
                    // throw e;
                }

            } else if ( unidades == null && productoCSV != null ) {
                /*
                 * el producto no existe en la base de datos pero si viene en el
                 * archivo csv => insertar
                 */
                n++;
                psInsert.setString(1, productoCSV.getCodigoCategoria());
                psInsert.setString(2, productoCSV.getCodigo2());
                psInsert.setString(3, productoCSV.getNombre());
                psInsert.setString(4, productoCSV.getDescripcion());
                psInsert.setInt(5, productoCSV.getEstado());
                psInsert.setString(6, productoCSV.getMarca());
                psInsert.setString(7, productoCSV.getCodigoProPpal());
                psInsert.setString(8, productoCSV.getOrigen());
                psInsert.setString(9, productoCSV.getUnidadBase());
                psInsert.setString(10, productoCSV.getEan13());
                psInsert.setString(11, productoCSV.getUnidadEmpaque());
                psInsert.setString(12, productoCSV.getAtributo9());
                psInsert.setString(13, productoCSV.getAtributo10());
                psInsert.setString(14, productoCSV.getCodigo());
                psInsert.setString(15, barraCSV.getUnidad());

                try {
                    psInsert.executeUpdate();

                } catch (SQLException e) {
                    logger.error("* Error en insert: " + productoCSV.getCodigo() + "  uni: " + barraCSV.getUnidad() + ". Error:" + e.getMessage());
                    // throw e;
                }

            } else if ( unidades != null && productoCSV != null ) {
                /*
                 * el producto existe en la base de datos y también viene en el
                 * archivo csv => actualizar
                 */

                /*
                 * Hasta aquí el producto del CSV y el de la base de datos
                 * tienen el mismo codigo SAP, falta ver si tienen distinto
                 * nombre y descripcion para actualizarlos
                 */
                if ( !productoCSV.equals(unidades.elements().nextElement()) ) {
                    /*
                     * Tienen distinto nombre o descripcion o categoría => se
                     * actualizan todos sus datos excepto la unidad de medida,
                     * puede que el update actualice más de una fila cuando el
                     * codigo SAP está varias veces en la tabla productos.
                     */
                    a++;
                    psUpdate.setString(1, productoCSV.getCodigoCategoria());
                    psUpdate.setString(2, productoCSV.getCodigo2());
                    psUpdate.setString(3, productoCSV.getNombre());
                    psUpdate.setString(4, productoCSV.getDescripcion());
                    psUpdate.setInt(5, productoCSV.getEstado());
                    psUpdate.setString(6, productoCSV.getMarca());
                    psUpdate.setString(7, productoCSV.getCodigoProPpal());
                    psUpdate.setString(8, productoCSV.getOrigen());
                    psUpdate.setString(9, productoCSV.getUnidadBase());
                    psUpdate.setString(10, productoCSV.getEan13());
                    psUpdate.setString(11, productoCSV.getUnidadEmpaque());
                    psUpdate.setString(12, productoCSV.getAtributo9());
                    psUpdate.setString(13, productoCSV.getAtributo10());
                    psUpdate.setString(14, productoCSV.getCodigo());
                    try {
                        aa += psUpdate.executeUpdate();

                    } catch (SQLException e) {
                        logger.error("* Error en update: " + productoCSV.getCodigo() + "  uni: " + barraCSV.getUnidad() + ". Error:" + e.getMessage());
                        // throw e;
                    }
                } else {
                    b++;
                }
            }
        }
        if ( psInsert != null ) {
            psInsert.close();
        }
        if ( psUpdate != null ) {
            psUpdate.close();
        }

        logger.info("Productos.- con nueva unidad de medida copiados:" + c + ", nuevos insertados:" + n + ", actualizados:" + a + " aa:" + aa + ", no es necesario actualizar:" + b);
        
        psInsert = null;
        psUpdate = null;

    }
 
    
    /**
     * @param con
     * @param archivo_codigobarras
     * @throws IOException
     * @throws SQLException
     */
    private void actualizarBarras( Connection con, String archivo ) throws IOException, SQLException {

        logger.info("iniciando actualización de barras");
        List values = Archivo.cargarBarrasCSV(archivo);
        if ( values.size() == 0 ) {
            return;
        }

        logger.info("Total a cargar:" + values.size());

        Hashtable productosDB = DbCarga.productos(con);
        Hashtable barrasDB = DbCarga.barras(con);
        // actualiza sólo si es el mismo código de barra
      String sqlUpdate = "update BODBA.bo_codbarra set TIP_CODBAR = ?, COD_PPAL = ? "
            + "where cod_prod1 = ? and unid_med = ? and cod_barra = ? ";

      String sqlInsert = "insert into BODBA.bo_codbarra(cod_barra, tip_codbar, cod_ppal, cod_prod1, "
            + "unid_med, id_producto, estadoactivo, cod_barra_bigint) values(?,?,?,?,?,?,'1',?)";

        PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
        PreparedStatement psInsert = con.prepareStatement(sqlInsert);

        int j = 0;
        int b = 0; // nuevo codigo
        int c = 0; // contniue
        int d = 0; // nueva barra de producto existente
        int e = 0; // barra actualizada
        for ( int i = 0; i < values.size(); i++ ) {
            if ( j * RANGO == i ) {
                logger.debug("actualizarBarras - rango cargarBarrasCSV:" + i);
                j++;
            }
          Barra barraCSV = (Barra) values.get(i);
            String key = barraCSV.getCodigoProducto() + barraCSV.getUnidad() + barraCSV.getCodigo();
          Barra barra = (Barra) barrasDB.get(key);

            if ( barra == null ) {
                // la barra no esta en la base de datos => nueva barra
                psInsert.setString(1, barraCSV.getCodigo());
                psInsert.setString(2, barraCSV.getTipo());
                psInsert.setString(3, barraCSV.getPpal());
                psInsert.setString(4, barraCSV.getCodigoProducto());
                psInsert.setString(5, barraCSV.getUnidad());
                psInsert.setLong(7, barraCSV.getCodigoBigInt());
                // busco el id del producto
              Hashtable unidades = (Hashtable) productosDB.get(barraCSV.getCodigoProducto());
                if ( unidades != null ) {
                  Producto producto = (Producto) unidades.get(barraCSV.getUnidad());
                    if ( producto != null ) {
                        d++;
                        psInsert.setInt(6, producto.getId());
                        try {
                            psInsert.executeUpdate();
                        } catch (Exception ex) {
                            logger.error("* Error al insertar el codigo de barra [" + ( i + 1 ) + "] -> " + ex.getMessage());
                            // ex.printStackTrace();
                        }
                    }
                } else {
                    /*
                     * HACER NADA: El producto debió estar insertado ya que
                     * primero de actualizan los productos, por lo tanto es un
                     * código de barra sin producto.
                     */
                    b++;
                }
            } else {
                // la barra esta en la base de datos => actualizar
                if ( barraCSV.getTipo() != null && barraCSV.getTipo().equals(barra.getTipo())
                // ppal puede ser nulo
                        && ( barraCSV.getPpal() == barra.getPpal() || ( barraCSV.getPpal() != null && barraCSV.getPpal().equals(barra.getPpal()) ) ) ) {
                    c++;
                    continue;
                }
                psUpdate.setString(1, barraCSV.getTipo());
                psUpdate.setString(2, barraCSV.getPpal());
                psUpdate.setString(3, barraCSV.getCodigoProducto());
                psUpdate.setString(4, barraCSV.getUnidad());
                psUpdate.setString(5, barraCSV.getCodigo());

                try {
                    psUpdate.executeUpdate();
                    e++;
                } catch (Exception ex) {
                    logger.error("* Error al actualizar el codigo de barra [" + ( i + 1 ) + "] -> " + ex.getMessage());
                    // ex.printStackTrace();
                }
            }
        }
      if ( psUpdate != null )
            psUpdate.close();
      if ( psInsert != null )
            psInsert.close();

        logger.info("Barras.- sin productos=" + b + ", no se necesitan actualizar=" + c + ", nuevas=" + d + ", actualizas=" + e);
        
        psUpdate = null;
        psInsert = null;

    }

    /**
     * @param con
     * @param archivo_precios
     * @throws Exception
     */
    private void actualizarPrecios( Connection con, int idLocal, String archivo_precios, String archivo_inventario ) throws Exception {

        long inix = System.currentTimeMillis();
        try {
        	
        	List preciosFile = null;
        	try{
        		//preciosFile = Archivo.cargarPreciosCSV(archivo_precios);
        		preciosFile = CargarPreciosFacade.getListaPrecios(archivo_precios);        		
        	}catch(Exception e){
        		e.printStackTrace();
                logger.error(e.getMessage());
        	}
            
            Hashtable inventarioCSV = Archivo.cargarInventarioCSV(archivo_inventario);
            Hashtable preciosDb = DbCarga.precios(con, idLocal);
            // para insertar una sola vez el precio por producto_id y local_id
            Hashtable preciosNuevos = new Hashtable();
            // para modificar una sola vez el precio cuando viene repetido en el
            // archivo de precios
            Hashtable preciosModificados = new Hashtable();

            // precio fuera del límite de variación
            Hashtable preciosFuera = new Hashtable();

            int n1 = 0; // nuevo
            int a1 = 0; // actualizar
            int a2 = 0; // actualizar precios realmente diferentes a los
            // actuales
            for ( int i = 0; i < preciosFile.size(); i++ ) {
            Precio precioFile = (Precio) preciosFile.get(i);
            Precio precioDb = (Precio) preciosDb.get(precioFile.getCodigoProducto() + precioFile.getUnidadMedida());
            Inventario inventario = (Inventario) inventarioCSV.get(precioFile.getCodigoLocal() + precioFile.getCodigoProducto());

                if ( precioDb == null ) {
                    n1++;
                    if ( precioFile.getPrecio() > 0 ) {
                        /*
                         * se setea el id del local ya que en el archivo viene
                         * el código del local. Producto nuevo para insertar.
                         */
                        precioFile.setIdLocal(idLocal);
                        preciosNuevos.put(precioFile.getCodigoProducto() + precioFile.getUnidadMedida(), precioFile);
                    }
                } else {
                    a1++;
                    /*
                     * Sólo actualizo el precio si es mayor que cero y ( si es
                     * distinto al precio actual or el bloqueo de compra es
                     * distinto )
                     * 
                     * verifico fecha precio nuevo, ya que algunos pudieron ser
                     * actualizados por el proceso de precios parciales
                     */
               if (precioFile.getPrecio() > 0
                     && (precioFile.getFechaPrecioNuevo().getTime() > precioDb.getFechaPrecioNuevo().getTime() || precioDb
                           .getFechaPrecioNuevo() == null)
                     && (precioDb.getPrecio() != precioFile.getPrecio() || (inventario != null && !precioDb
                           .getBloqueoCompra().equals(inventario.getBloqueoCompra())))) {
                  a2++;

                        precioFile.setIdLocal(idLocal);
                        precioFile.setIdProducto(precioDb.getIdProducto());
                        // si precioFile no esta fuera de rango
                  if (!precioDb.isFueraDeRango(precioFile))
                            preciosModificados.put(precioFile.getCodigoProducto() + precioFile.getUnidadMedida(), precioFile);
                  else
                            preciosFuera.put(precioFile.getCodigoProducto() + precioFile.getUnidadMedida(), precioFile);
                        }
                    }
                }
            

            // para desocupar memoria
            preciosFile = null;
            preciosDb = null;
            logger.debug("Precios.- nuevos:" + n1 + ", nuevos mayores que cero:" + preciosNuevos.size() + ", posible de actualizar:" + a1 + ", mayores que cero y distintos al actual:" + a2
                    + ", cumplen limites de variación:" + preciosModificados.size() + ", NO cumplen limites de variación: " + preciosFuera.size());

            if ( preciosNuevos.size() > 0 || preciosModificados.size() > 0 ) {
                insertarPrecios(con, preciosNuevos, inventarioCSV);
                modificarPrecios(con, preciosModificados, inventarioCSV);
            }
            if ( preciosFuera.size() > 0 ) {
                modificarPreciosFueraRango(con, preciosFuera, inventarioCSV);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("* Ocurrio un error en actualizarPrecios para el local " + idLocal + ". Error: " + e.getMessage());
            // throw e;
        }

        logger.info("Fin actualizarPrecios de local " + idLocal + ", tiempo: " + calculaTiempo(inix));

    }

    public void cambiaPreciosLocales( Connection con ) throws Exception {

        logger.info("*****Inicia proceso cambiar precios a prod. igual que local al local maestro******");
        List locales = DbCarga.locales(con);
        for ( int i = 0; i < locales.size(); i++ ) {
               Local local = (Local)locales.get(i);
            logger.debug("local :" + local.getId());
            asignaPrecioLocalMaestro(con, local.getId());

        }
    }

    /**
     * @param con
     * @param preciosModificados
     * @param localId
     * @param inventarioCSV
     * @throws SQLException
     */
    private void modificarPrecios( Connection con, Hashtable preciosModificados, Hashtable inventarioCSV ) throws SQLException {

      String sql = "update BODBA.BO_PRECIOS set UMEDIDA = ?, COD_BARRA = ?,                     "
            + "MIX_LOCAL = ?, BLOQ_COMPRA = ?, "
            + "PRECIO_ANTIGUO = PREC_VALOR, FECHA_PRECIO_ANTIGUO = FECHA_PRECIO_NUEVO,      "
            + "PRECIO_NUEVO = ?, FECHA_PRECIO_NUEVO = CURRENT_DATE, PREC_VALOR = ?, ALERTA_CAMBIO = 1 "
            + "where id_local = ? and id_producto = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        int n = 0;
        Enumeration enu = preciosModificados.elements();
        while ( enu.hasMoreElements() ) {
         Precio precio = (Precio) enu.nextElement();
            try {
             Inventario inventario = (Inventario) inventarioCSV.get(precio.getCodigoLocal() + precio.getCodigoProducto());
                if ( inventario != null ) {
                    ps.setString(1, precio.getUnidadMedida());
                    ps.setString(2, precio.getCodigoBarra());
                    ps.setInt(3, inventario.getMixLocal());
                    ps.setString(4, inventario.getBloqueoCompra());
                    ps.setInt(5, precio.getPrecio());
                    ps.setInt(6, precio.getPrecio());
                    ps.setInt(7, precio.getIdLocal());
                    ps.setInt(8, precio.getIdProducto());
                    ps.executeUpdate();
                    n++;
                }
            } catch (Exception e) {
                System.err.println("* Error al updetear precio con Cod: " + precio.getCodigoProducto() + "  unidad: " + precio.getUnidadMedida() + "  local: " + precio.getCodigoLocal() + ". Error:"
                        + e.getMessage());
        }         
        }
        if ( ps != null ) {
            ps.close();
        }
        ps = null;

        logger.info("Precios actualizados: " + n);
   }
   
   
    private void asignaPrecioLocalMaestro(Connection con, int local) throws Exception {
    	int localMaestro = Integer.parseInt(Parametros.getString("LOCAL"));
    	if(localMaestro != 0){
            String sqlBo = "merge into bodba.bo_precios as precios "+
                       "using(select p.PREC_VALOR,p.ID_PRODUCTO from bodba.bo_precios p where p.ID_LOCAL="+localMaestro+") as pre "+    
                           "on (precios.id_producto = pre.id_producto and precios.id_local ="+local+") "+
                           "when matched then "+
                           "update set precios.PREC_VALOR = pre.PREC_VALOR";
            
            String sqlFo = "merge into fodba.fo_precios_locales fprecios "+                                           
                           "using (select fproductos.pro_id as pro_id, bprecios.id_local as id_local, bprecios.prec_valor as valor "+ 
                           "from  fodba.fo_productos fproductos "+                                                 
                           "inner join bodba.bo_precios bprecios on bprecios.id_producto  = fproductos.pro_id_bo "+
                       "where  bprecios.ID_LOCAL="+localMaestro+") precios "+                                                                            
                           "on (fprecios.pre_pro_id = precios.pro_id and fprecios.PRE_LOC_ID="+local+") "+        
                           "WHEN MATCHED THEN "+                                                                                
                           "UPDATE SET fprecios.pre_valor = precios.valor";  
           // System.out.println("sqlBo "+sqlBo);
           // System.out.println("sqlFo"+sqlFo);
            PreparedStatement psBo = null;
            PreparedStatement psFo = null;
            con.setAutoCommit(false);
            try {
            psBo = con.prepareStatement(sqlBo);
            psFo = con.prepareStatement(sqlFo);           
            psBo.executeUpdate(); 
            psFo.executeUpdate();    
    
                con.commit();
                
            } catch (Exception e) {
            logger.error("Exception:"+e.getMessage(), e);
                con.rollback();
                throw e;
           } finally {
                con.setAutoCommit(true);
                Db.close(psBo);
                Db.close(psFo);
           }
    	}else{
    		logger.info("No se estandarizan los precios, no hay local maestro.");
            
     }
     }

   /**
    * 
    * @param con
    * @param preciosFuera
    * @param inventarioCSV
    * @throws SQLException
    */
    private void modificarPreciosFueraRango( Connection con, Hashtable preciosFuera, Hashtable inventarioCSV ) throws SQLException {

      String sql = "update BODBA.BO_PRECIOS set UMEDIDA = ?, COD_BARRA = ?,                     "
            + "MIX_LOCAL = ?, BLOQ_COMPRA = ?,                                                "
            + "PRECIO_NUEVO = ?, FECHA_PRECIO_NUEVO = CURRENT_DATE                            "
            + "where id_local = ? and id_producto = ?                                         ";

      PreparedStatement ps = con.prepareStatement(sql);
      int n = 0;
        Enumeration enu = preciosFuera.elements();
        while ( enu.hasMoreElements() ) {
         Precio precio = (Precio) enu.nextElement();
            try {
             Inventario inventario = (Inventario) inventarioCSV.get(precio.getCodigoLocal() + precio.getCodigoProducto());
                if ( inventario != null ) {
                    ps.setString(1, precio.getUnidadMedida());
                    ps.setString(2, precio.getCodigoBarra());
                    ps.setInt(3, inventario.getMixLocal());
                    ps.setString(4, inventario.getBloqueoCompra());
                    ps.setInt(5, precio.getPrecio());
                    ps.setInt(6, precio.getIdLocal());
                    ps.setInt(7, precio.getIdProducto());
                    ps.executeUpdate();
                    n++;
                }
            } catch (Exception e) {
                logger.error("* Error al updetear precio fuera de rango con Cod: " + precio.getCodigoProducto() + "  unidad: " + precio.getUnidadMedida() + "  local: " + precio.getCodigoLocal()
                        + ". Error:" + e.getMessage());
            }
        }
      if ( ps != null )
          ps.close();
      
        logger.info("Precios fuera de rango actualizados: " + n);
    }

    /**
     * 
     * @param con
     * @param precios
     * @param localID
     * @param archivo_inventario
     * @throws SQLException
     * @throws IOException
     */
    private void insertarPrecios( Connection con, Hashtable precios, Hashtable inventarioCSV ) throws SQLException, IOException {

      if (precios.size() == 0)
         return;
      Hashtable productosDb = DbCarga.productos(con);

      String sql = "insert into BODBA.BO_PRECIOS (ID_LOCAL, ID_PRODUCTO, COD_PROD1, COD_LOCAL, "
            + "PREC_VALOR, UMEDIDA, COD_BARRA, MIX_LOCAL, BLOQ_COMPRA, ESTADOACTIVO, "
            + "PRECIO_ANTIGUO, PRECIO_NUEVO, ALERTA_CAMBIO, FECHA_PRECIO_ANTIGUO, FECHA_PRECIO_NUEVO) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,CURRENT_DATE)";

        PreparedStatement ps = con.prepareStatement(sql);
        int n = 0; // realmente insertados
        Enumeration enu = precios.elements();
        while ( enu.hasMoreElements() ) {
         Precio precio = (Precio) enu.nextElement();
            try {
                ps.setInt(1, precio.getIdLocal());

            Hashtable unidades = (Hashtable) productosDb.get(precio.getCodigoProducto());
                if ( unidades != null ) {
               Producto productoDb = (Producto) unidades.get(precio.getUnidadMedida());
               Inventario inventario = (Inventario) inventarioCSV.get(precio.getCodigoLocal() + precio.getCodigoProducto());
               if (productoDb != null && inventario != null) {
                        n++;
                        ps.setInt(2, productoDb.getId());
                        ps.setString(3, precio.getCodigoProducto());
                        ps.setString(4, precio.getCodigoLocal());
                        ps.setInt(5, precio.getPrecio());
                        ps.setString(6, precio.getUnidadMedida());
                        ps.setString(7, precio.getCodigoBarra());
                        ps.setInt(8, inventario.getMixLocal());
                        ps.setString(9, inventario.getBloqueoCompra());
                        ps.setString(10, "1"); // al parecer no sirve para nada
                        ps.setInt(11, 0); // precio antiguo
                        ps.setInt(12, precio.getPrecio()); // precio nuevo
                        ps.setInt(13, 1);// alerta cambio
                        ps.executeUpdate();
                    }
                }

            } catch (SQLException e) {
                logger.error("* Error al insertar precio con Cod: " + precio.getCodigoProducto() + "  unidad: " + precio.getUnidadMedida() + "  local: " + precio.getCodigoLocal() + ". Error:"
                        + e.getMessage());
            }
        }

        logger.info("Precios insertados: " + n);
    }

    /**
     * Actualiza la columna bloq_compra = 'SI' y publica producto
     * 
     * @param con
     * @throws IOException
     * @throws SQLException
     */
    public void desbloquearPublicar( Connection con ) throws IOException, SQLException {

        logger.debug("cargado archivo");
        Hashtable unidades = Archivo.cargarProductosBloqueados("productosBloqueados.csv");
        logger.debug("unidades cargadas: " + unidades.size());
        Enumeration keys = unidades.keys();
        while ( keys.hasMoreElements() ) {
         String key = (String) keys.nextElement();
         List codigos = (List) unidades.get(key);
            String sCodigos = join(codigos);
         String sqlUpdate = "update bodba.bo_precios set BLOQ_COMPRA = 'NO' where cod_prod1 in " + sCodigos
               + " and umedida = ?";
            PreparedStatement ps = con.prepareStatement(sqlUpdate);
            ps.setString(1, key);
            int i = ps.executeUpdate();
            logger.debug("unidad: " + key + " actualiza: " + i);
        }
        // llama a finalizacion primero
        finalizacion(con);

        keys = unidades.keys();
        while ( keys.hasMoreElements() ) {
         String key = (String) keys.nextElement();
         List codigos = (List) unidades.get(key);
         String sCodigos = join(codigos);
         String sqlUpdate = "update fodba.fo_productos set pro_estado = 'A' where pro_cod_sap in " + sCodigos
               + "and pro_tipre = ? ";
         PreparedStatement ps = con.prepareStatement(sqlUpdate);
         ps.setString(1, key);
         int i = ps.executeUpdate();
            logger.debug("unidad: " + key + " actualiza: " + i);
        }

        keys = unidades.keys();
        while ( keys.hasMoreElements() ) {
         String key = (String) keys.nextElement();
         List codigos = (List) unidades.get(key);
            String sCodigos = join(codigos);
         String sqlUpdate = "update fodba.fo_precios_locales set pre_estado = 'A' where pre_pro_id in "
               + "(select pro_id from fodba.fo_productos where pro_cod_sap in " + sCodigos + " and pro_tipre = ?)";
            PreparedStatement ps = con.prepareStatement(sqlUpdate);
            ps.setString(1, key);
            int i = ps.executeUpdate();
            logger.debug("unidad: " + key + " actualiza: " + i);
        }

        keys = unidades.keys();
        while ( keys.hasMoreElements() ) {
         String key = (String) keys.nextElement();
         List codigos = (List) unidades.get(key);
            String sCodigos = join(codigos);
         String sqlInsert = "insert into fodba.fo_pro_tracking "
               + "(TRA_BO_PRO_ID, TRA_PRO_ID,TRA_FEC_CREA, TRA_USUARIO,TRA_TEXTO) "
               + "select pro_id_bo, pro_id, CURRENT TIMESTAMP, 'solicitud automatica (sime)', 'El producto ha sido publicado' "
               + "from fodba.fo_productos where pro_cod_sap in " + sCodigos + " and pro_tipre = ?";

            PreparedStatement ps = con.prepareStatement(sqlInsert);
            ps.setString(1, key);
            int i = ps.executeUpdate();
            logger.debug("unidad: " + key + " actualiza: " + i);
        }
    }

    /**
     * Entrega un string con los rut distinto de cero.
     * 
     * Formato: "(123,43234,454,56456)"
     * 
     * @param boletas
     * @return
     */
    public String join( List list ) {

        StringBuffer lista = new StringBuffer("('");
        for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
         String elemento = (String) iter.next();
            lista.append(elemento);
            if ( iter.hasNext() ) {
                lista.append("','");
            }
        }
        lista.append("')");
        return lista.toString();
    }

    private static String fecha() {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.util.Date date = new java.util.Date();
        String s = dateFormat.format(date);
        return s;
    }

    public void addMail( Mail mail, Connection con ) {

        try {
              PreparedStatement stm = con.prepareStatement("INSERT INTO FO_SEND_MAIL ( " +
                            "FSM_IDFRM, " +
                            "FSM_REMITE, " +
                            "FSM_DESTINA, " +
                            "FSM_COPIA, " +
                            "FSM_SUBJECT, " +
                            "FSM_DATA, " +
                            "FSM_ESTADO, " +
                            "FSM_STMPSAVE, " +
                            "FSM_STMPSEND ) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) ");
            stm.setString(1, mail.getFsm_idfrm());
            stm.setString(2, mail.getFsm_remite());
            stm.setString(3, mail.getFsm_destina());
            if( mail.getFsm_copia() != null )
                stm.setString(4, mail.getFsm_copia() );
            else
                stm.setNull(4, java.sql.Types.INTEGER );
            stm.setString(5, mail.getFsm_subject());
            stm.setString(6, mail.getFsm_data());
            stm.setString(7, mail.getFsm_estado());
            stm.setTimestamp(8, new Timestamp(mail.getFsm_stmpsave()));
            stm.setNull(9, java.sql.Types.INTEGER);
            // logger.debug("SQL (addMail): " + stm.toString());
            stm.executeUpdate();

            if ( con != null && !con.isClosed() ) {
                con.close();
            }
        } catch (SQLException ex) {
            logger.error("addMail - Problema SQL", ex);
            try {
                if ( con != null && !con.isClosed() ) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("addMail - Problema SQL (close)", ex);
            }
            
            // throw new EmailDAOException(ex);
        } catch (Exception ex) {
            logger.error("addMail - Problema General", ex);
            try {
                if ( con != null && !con.isClosed() ) {
                    con.close();
                }
            } catch (SQLException e) {
                logger.error("addMail - Problema SQL (close)", ex);
            }

        }

    }

}