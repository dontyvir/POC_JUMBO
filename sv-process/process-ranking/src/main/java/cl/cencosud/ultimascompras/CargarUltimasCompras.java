/*
 * Created on 20-may-2008
 *
 */
package cl.cencosud.ultimascompras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cl.cencosud.historialventas.Boleta;
import cl.cencosud.historialventas.Linea;
import cl.cencosud.util.Archivo;
import cl.cencosud.util.Db;
import cl.cencosud.util.FiltroJCV;
import cl.cencosud.util.Lista;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 * 
 * Carga las últimas compras realizadas por los clientes jumbo.cl en los locales
 * físicos en los últimos 3 meses. Para que estas compras aparezcan en sus
 * cuentas de jumbo.cl
 * 
 * Se agrega la columna fecha a la tabla fo_productos_compra: ""alter table
 * fodba.fo_productos_compra add column fpc_fecha date""
 * 
 * Inserta en las tablas fo_compras_locales y fo_productos_compra.
 * 
 * 1.- Inserta todos los productos para cada cliente que vienen en los archivos
 * JCV.
 * 
 * 2.- Finalmente borra todos los productos que tienen fecha anterior a 3 meses
 * atrás.
 */
public class CargarUltimasCompras {
    public static int RANGO = 5000;

    /**
     * Esto porque se llena db2 no soporta SQL muy grandes
     */
    public static int RANGO2 = 1000;

    public static int diasAtras = 90;

    public static void main(String[] args) throws Exception {
        int dias = 0;
        if (args.length > 0)
            dias = Integer.parseInt(args[0]);

        long ini = System.currentTimeMillis();
        String driver = Parametros.getString("DRIVER");
        String locales = Parametros.getString("LOCALES_COMPRAS");
        System.out.println(locales);
        String[] aLocales = locales.split("\\s*,\\s*");

        String user = Parametros.getString("USER");
        String password = Parametros.getString("PASSWORD");
        String url = Parametros.getString("URL");
        Connection con = Db.conexion(user, password, driver, url);
        System.out.println("CON : " + con);

        String vUser = Parametros.getString("V_USER");
        String vPassword = Parametros.getString("V_PASSWORD");
        String vUrl = Parametros.getString("V_URL");
        Connection vCon = Db.conexion(vUser, vPassword, driver, vUrl);
        System.out.println("VCON : " + vCon);

        try {
            CargarUltimasCompras cargarUltimasCompras = new CargarUltimasCompras();
            if (dias != -1) {
                cargarUltimasCompras.cargar(con, vCon, aLocales, dias);
            } else {
                cargarUltimasCompras.moverClientesNuevos(con, vCon);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        System.out.println("Tiempo (seg): " + ((System.currentTimeMillis() - ini) / 1000));
    }

    /**
     * @param locales
     * @throws Exception
     *  
     */
    private void cargar(Connection con, Connection vCon, String[] locales, int dias) throws Exception {
        GregorianCalendar hoy = new GregorianCalendar();
        hoy.add(Calendar.DAY_OF_YEAR, -dias);
        System.out.println(hoy.getTime());

        GregorianCalendar fechaAtras = (GregorianCalendar) hoy.clone();
        fechaAtras.add(Calendar.DAY_OF_YEAR, -diasAtras);

        String[] archivos = Archivo.listaDeArchivosJCV(new FiltroJCV(hoy, locales));
        for (int i = 0; i < archivos.length; i++) {
            List boletas = Archivo.cargarBoletasJCV(Archivo.path() + archivos[i]);
            //lista de rut que vienen en las boletas
            List rutClientes = listaDeRut(boletas);
            //de los rut de las boletas veo cuales son clientes WEB y NO WEB
            List rutClientesWeb = listaDeRutClientesWeb(con, rutClientes);
            List rutClientesNoWeb = Lista.menos(rutClientes, rutClientesWeb);
            //separa las boletas con clientes WEB y NO WEB
            List boletasClientesWeb = new ArrayList();
            List boletasClientesNoWeb = new ArrayList();
            separarBoletas(boletas, rutClientesWeb, boletasClientesWeb, boletasClientesNoWeb);

            System.out.println("Cantidad de ruts: " + rutClientes.size());
            System.out.println("Cantidad de ruts antiguos: " + rutClientesWeb.size());
            System.out.println("Cantidad de ruts nuevos: " + rutClientesNoWeb.size());
            System.out.println("cantidad de boletas: " + boletas.size());
            System.out.println("cantidad de boletas de clientes Web: " + boletasClientesWeb.size());
            System.out.println("cantidad de boletas de clientes no Web: " + boletasClientesNoWeb.size());

            //si existen nuevos clientes Web los inserta
            System.out.println("Insertando cliente web nuevos");
            insertarClientesWebNuevos(con, rutClientesWeb);

            //Obtengo las ultimas compras en locales guardadas de los clientes
            // WEB
            Hashtable listaDeCompras = cargarComprasClientesWeb(con, rutClientesWeb, fechaAtras);
            System.out.println("Compras en locales de clientes WEB: " + listaDeCompras.size());
            //mezclo nuevas boleta con ultimas compras guardadas
            System.out.println("mezcla compras clientes WEB");
            ultimasCompras(boletasClientesWeb, listaDeCompras);
            insertarComprasClienteWeb(con, listaDeCompras);

            if (boletasClientesNoWeb.size() > 0) {
                System.out.println("cargar compras clientes NO WEB");
                Hashtable listaDeComprasNoWeb = cargarComprasClientesNoWeb(vCon, rutClientesNoWeb, fechaAtras);
                System.out.println("mezcla compras clientes NO WEB");
                ultimasCompras(boletasClientesNoWeb, listaDeComprasNoWeb);
                System.out.println("Insertar compras clientes NO WEB");
                insertarComprasClientesNoWeb(vCon, listaDeComprasNoWeb);
            }
        }
    }

    /**
     * Separa las boletas de cliente Web de los clientes no Web
     * 
     * @param boletas
     * @param rutClientesWeb
     * @param boletasWeb
     * @param boletasNoWeb
     */
    private void separarBoletas(List boletas, List rutClientesWeb, List boletasWeb, List boletasNoWeb) {
        for (int i = 0; i < boletas.size(); i++) {
            Boleta boleta = (Boleta) boletas.get(i);
            Integer rut = new Integer(boleta.getRutCliente());
            if (rutClientesWeb.indexOf(rut) != -1) {
                boletasWeb.add(boleta);
            } else if (boleta.getRutCliente() != 0) {
                boletasNoWeb.add(boleta);
            }
        }
    }

    /**
     * Entrega la lista de ruts obtenidos desde las boletas cargadas.
     * 
     * @param boletas
     * @return
     */
    private List listaDeRut(List boletas) {
        List lista = new ArrayList();
        for (Iterator iter = boletas.iterator(); iter.hasNext();) {
            Boleta boleta = (Boleta) iter.next();
            if (boleta.getRutCliente() != 0) {
                lista.add(new Integer(boleta.getRutCliente()));
            }
        }
        return lista;
    }

    /**
     * De la lista de rut que viene como paramentro se devuelve el subconjunto
     * de rut que son clientes web
     * 
     * @param con
     * @param rutClientes
     * @return
     * @throws SQLException
     */
    private List listaDeRutClientesWeb(Connection con, List rutClientes) throws SQLException {
        List lista = Lista.subListas(rutClientes, RANGO2);
        Set ruts = new HashSet();
        for (int i = 0; i < lista.size(); i++) {
            List subLista = (List) lista.get(i);
            String sql = "select cli_rut from fodba.fo_clientes where cli_rut in " + Lista.join(subLista);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int rut = rs.getInt("cli_rut");
                ruts.add(new Integer(rut));
            }
        }

        List rutAntiguos = new ArrayList();
        Iterator listaRut = ruts.iterator();
        while (listaRut.hasNext()) {
            Integer rut = (Integer) listaRut.next();
            rutAntiguos.add(rut);
        }
        return rutAntiguos;
    }

    private void insertarClientesWebNuevos(Connection con, List rutClientes) throws SQLException {
        //verifica si hay clientes Web nuevos
        List lista = Lista.subListas(rutClientes, RANGO2);
        Set ruts = new HashSet();
        for (int i = 0; i < lista.size(); i++) {
            List subLista = (List) lista.get(i);
            String sql = "select fcl_id from fodba.fo_compras_locales where fcl_id in " + Lista.join(subLista);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int rut = rs.getInt("fcl_id");
                ruts.add(new Integer(rut));
            }
        }

        //veo cuales son los nuevos
        List nuevos = new ArrayList();
        for (int i = 0; i < rutClientes.size(); i++) {
            //si no existe => es nuevo
            if (!ruts.contains(rutClientes.get(i)))
                nuevos.add(rutClientes.get(i));
        }

        String sql = "insert into fodba.fo_compras_locales"
                + "(fcl_id, fcl_estado, fcl_fecha, fcl_rut, fcl_codigo_local, fcl_nombre_local) "
                + "values(?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        System.out.println("clientes web nuevos: " + nuevos.size());
        for (int i = 0; i < nuevos.size(); i++) {
            Integer rut = (Integer) nuevos.get(i);
            ps.setInt(1, rut.intValue());
            ps.setInt(2, 3);
            ps.setDate(3, new java.sql.Date((new Date()).getTime()));
            ps.setInt(4, rut.intValue());
            ps.setString(5, "J500");
            ps.setString(6, "Local");
            ps.addBatch();
            if (i % RANGO == 0) {
                ps.executeBatch();
                System.out.println(i);
                ps.clearBatch();
            }
        }
        ps.executeBatch();
    }

    /**
     * Primero elimina las compras de cada cliente e inserta las nuevas compras
     * (elimina sólo las compras de los clientes que vienen en la hashtable
     * compras)
     * 
     * @param con
     * @param listaDeCompras
     * @param lista
     * @throws SQLException
     */
    private void insertarComprasClienteWeb(Connection con, Hashtable listaDeCompras) throws SQLException {
        //Borro todas las compras de los rut que se actualizarán
        String sql = "delete from fodba.fo_productos_compra where fcl_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        Enumeration ruts = listaDeCompras.keys();
        int i = 0;
        while (ruts.hasMoreElements()) {
            Integer rut = (Integer) ruts.nextElement();
            ps.setInt(1, rut.intValue());
            i++;
            ps.addBatch();
            if (i % RANGO == 0) {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        ps.executeBatch();
        ps.close();

        //se insertan todas las compras
        String sqlInsertCompras = "insert into fodba.fo_productos_compra                               "
                + "(fpc_id, fcl_id, fpc_ean, fpc_cantidad, fpc_fecha)                                          "
                + "values(?, ?, ?, ?, ?)";
        PreparedStatement psInsertCompra = con.prepareStatement(sqlInsertCompras);
        ruts = listaDeCompras.keys();
        i = 0;
        while (ruts.hasMoreElements()) {
            Integer rut = (Integer) ruts.nextElement();
            int iRut = rut.intValue();
            if (iRut != 0) {
                Compra compra = (Compra) listaDeCompras.get(rut);
                Enumeration detalle = compra.getDetalle();
                while (detalle.hasMoreElements()) {
                    DetalleCompra detalleCompra = (DetalleCompra) detalle.nextElement();
                    psInsertCompra.setInt(1, iRut);
                    psInsertCompra.setInt(2, iRut);
                    psInsertCompra.setString(3, detalleCompra.getCodigoBarra() + "");
                    psInsertCompra.setFloat(4, detalleCompra.getCantidad());
                    psInsertCompra.setDate(5, new java.sql.Date(detalleCompra.getFecha().getTime()));
                    psInsertCompra.addBatch();
                    i++;
                    if (i % RANGO == 0) {
                        psInsertCompra.executeBatch();
                        System.out.println(i);
                        psInsertCompra.clearBatch();
                    }
                }
            }
        }
        psInsertCompra.executeBatch();
        System.out.println("FIN insertar compras: " + i);
    }

    /**
     * @param con
     * @param listaDeComprasNoWeb
     * @param rutClientesNoWeb
     * @throws SQLException
     */
    private void insertarComprasClientesNoWeb(Connection con, Hashtable listaDeComprasNoWeb)
            throws SQLException {
        //Borro todas las compras de los rut que se actualizarán
        String sql = "delete from vthdba.local_compras where rut = ? ";
        PreparedStatement ps = con.prepareStatement(sql);
        Enumeration ruts = listaDeComprasNoWeb.keys();
        int i = 0;
        while (ruts.hasMoreElements()) {
            Integer rut = (Integer) ruts.nextElement();
            i++;
            ps.setInt(1, rut.intValue());
            ps.addBatch();
            if (i % RANGO == 0) {
                ps.executeBatch();
                ps.clearBatch();
            }
        }
        ps.executeBatch();

        try {
            //se insertan todas las compras
            String sqlInsertCompras = "insert into vthdba.local_compras                                  "
                    + "(rut, codigo_barra, cantidad, fecha)                                              "
                    + "values(?, ?, ?, ?)";
            PreparedStatement psInsertCompra = con.prepareStatement(sqlInsertCompras);
            ruts = listaDeComprasNoWeb.keys();
            i = 0;
            while (ruts.hasMoreElements()) {
                Integer rut = (Integer) ruts.nextElement();
                int iRut = rut.intValue();
                if (iRut != 0) {
                    Compra compra = (Compra) listaDeComprasNoWeb.get(rut);
                    Enumeration detalle = compra.getDetalle();
                    while (detalle.hasMoreElements()) {
                        DetalleCompra detalleCompra = (DetalleCompra) detalle.nextElement();
                        psInsertCompra.setInt(1, iRut);
                        psInsertCompra.setLong(2, detalleCompra.getCodigoBarra());
                        psInsertCompra.setFloat(3, detalleCompra.getCantidad());
                        psInsertCompra.setDate(4, new java.sql.Date(detalleCompra.getFecha().getTime()));
                        psInsertCompra.addBatch();
                        i++;
                        if (i % RANGO == 0) {
                            psInsertCompra.executeBatch();
                            System.out.println(i);
                            psInsertCompra.clearBatch();
                        }
                    }
                }
            }
            psInsertCompra.executeBatch();
            System.out.println("FIN insertar compras: " + i);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getNextException() != null)
                e.getNextException().printStackTrace();
            throw e;
        }
    }

    /**
     * Mezcla las nuevas compras con las antiguas, reemplazando una compra
     * antigua por la nueva compra o manteniendola cuando no hay una nueva
     * compra.
     * 
     * @param boletas
     * @param comprasPorRut
     */
    private void ultimasCompras(List boletas, Hashtable comprasPorRut) {
        for (Iterator iter = boletas.iterator(); iter.hasNext();) {
            Boleta boleta = (Boleta) iter.next();
            Integer rut = new Integer(boleta.getRutCliente());
            Compra compra = (Compra) comprasPorRut.get(rut);

            if (compra == null)
                compra = new Compra();

            Enumeration lineas = boleta.getLineas();
            while (lineas.hasMoreElements()) {
                Linea linea = (Linea) lineas.nextElement();
                compra.add(linea.getCodigoBarra(), linea.getCantidad(), boleta.getFecha());
            }
            comprasPorRut.put(rut, compra);
        }
    }

    /**
     * Carga las últimas compras de los clientes desde la base de datos, no
     * carga las compras de más de 90 días
     * 
     * @param con
     * @param rutClientes
     *            Clientes WEB
     * @param fechaAtras
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    private Hashtable cargarComprasClientesWeb(Connection con, List rutClientes, GregorianCalendar fechaAtras)
            throws SQLException, ParseException {
        List lista = Lista.subListas(rutClientes, RANGO2);
        Hashtable listaDeCompras = new Hashtable();
        for (int i = 0; i < lista.size(); i++) {
            List subLista = (List) lista.get(i);
            cargarComprasPorRut(con, Lista.join(subLista), fechaAtras, listaDeCompras);
        }
        return listaDeCompras;
    }

    private void cargarComprasPorRut(Connection con, String rutClientes, GregorianCalendar fechaAtras,
            Hashtable listaDeCompras) throws SQLException {
        System.out.println("tamaño string : " + rutClientes.length());

        String sql = "select fcl_id as rut, fpc_ean, fpc_cantidad, fpc_fecha             "
                + "from fodba.fo_productos_compra                                        "
                + "where fpc_fecha >= ? and fcl_id in " + rutClientes;
        PreparedStatement ps = con.prepareStatement(sql);
        //System.out.println(sql);
        ps.setDate(1, new java.sql.Date(fechaAtras.getTimeInMillis()));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Integer rut = new Integer(rs.getInt("rut"));
            long codigoBarra = Long.parseLong(rs.getString("fpc_ean"));
            float cantidad = rs.getFloat("fpc_cantidad");
            Date fec = rs.getDate("fpc_fecha");

            Compra compra = (Compra) listaDeCompras.get(rut);
            if (compra == null)
                compra = new Compra();

            compra.add(codigoBarra, cantidad, fec);
            listaDeCompras.put(rut, compra);
        }
    }

    /**
     * @param con
     * @param rutClientesNoWeb
     * @param fechaAtras
     * @return
     * @throws SQLException
     */
    private Hashtable cargarComprasClientesNoWeb(Connection con, List rutClientesNoWeb, GregorianCalendar fechaAtras)
            throws SQLException {
        List lista = Lista.subListas(rutClientesNoWeb, RANGO2);
        Hashtable listaDeCompras = new Hashtable();
        for (int i = 0; i < lista.size(); i++) {
            List subLista = (List) lista.get(i);
            cargarComprasPorRutNoWeb(con, Lista.join(subLista), fechaAtras, listaDeCompras);
        }
        return listaDeCompras;
    }

    /**
     * @param con
     * @param string
     * @param fechaAtras
     * @param listaDeCompras
     * @throws SQLException
     */
    private void cargarComprasPorRutNoWeb(Connection con, String rutClientesNoWeb, GregorianCalendar fechaAtras,
            Hashtable listaDeCompras) throws SQLException {
        System.out.println("tamaño string no WEB: " + rutClientesNoWeb.length());

        String sql = "select rut, codigo_barra, cantidad, fecha                          "
                + "from vthdba.local_compras                                             "
                + "where fecha >= ? and rut in " + rutClientesNoWeb;

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDate(1, new java.sql.Date(fechaAtras.getTimeInMillis()));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Integer rut = new Integer(rs.getInt("rut"));
            long codigoBarra = rs.getLong("codigo_barra");
            float cantidad = rs.getFloat("cantidad");
            Date fec = rs.getDate("fecha");

            Compra compra = (Compra) listaDeCompras.get(rut);
            if (compra == null)
                compra = new Compra();

            compra.add(codigoBarra, cantidad, fec);
            listaDeCompras.put(rut, compra);
        }
    }

    /**
     * Extrae las compras de los clientes Web nuevos de la base vthjm y las
     * compia en la base jmcl. Las elimina finalmente de la base vthjm
     * 
     * @param con
     * @param vCon
     * @throws SQLException
     */
    private void moverClientesNuevos(Connection con, Connection vCon) throws SQLException {
        List rutClientesNuevos = clientesNuevos(con);
        agregarClientesNuevo(vCon, rutClientesNuevos);
        Hashtable compras = cargarComprasClientesNuevos(vCon);
        System.out.println("compras mover: " + compras.size());
        insertarClientesWebNuevos(con, Collections.list(compras.keys()));
        insertarComprasClienteWeb(con, compras);
    }

    /**
     * 
     * @param con
     * @return lista de rut de clientes Web que no tengan compras historicas
     * @throws SQLException
     */
    private List clientesNuevos(Connection con) throws SQLException {
        String sql = "select cli_rut from fodba.fo_clientes "
                + "where cli_rut not in (select fcl_id from fodba.fo_compras_locales)";
        List lista = new ArrayList();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int rut = rs.getInt("cli_rut");
            lista.add(new Integer(rut));
        }
        rs.close();
        ps.close();
        return lista;
    }

    /**
     * Agrega los cliente sin compras historias a vthjm para poder hacer el join
     * de la consulta
     * 
     * @param vCon
     * @throws SQLException
     */
    private void agregarClientesNuevo(Connection vCon, List rutClientesNuevos) throws SQLException {
        //elimino la lista anterios
        String sql = "delete from vthdba.clientes_jumbo";
        PreparedStatement ps = vCon.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        sql = "insert into vthdba.clientes_jumbo(cli_rut) values(?)";
        ps = vCon.prepareStatement(sql);

        for (int i = 0; i < rutClientesNuevos.size(); i++) {
            Integer rut = (Integer) rutClientesNuevos.get(i);
            ps.setInt(1, rut.intValue());
            ps.addBatch();
            if (i % RANGO == 0) {
                ps.executeBatch();
                System.out.println(i);
                ps.clearBatch();
            }
        }
        ps.executeBatch();
        ps.close();
    }

    /**
     * Lista de compras historicas de los clientes nuevos
     * 
     * @param con
     * @return
     * @throws SQLException
     */
    private Hashtable cargarComprasClientesNuevos(Connection con) throws SQLException {
        Hashtable listaDeCompras = new Hashtable();

        String sql = "select rut, codigo_barra, cantidad, fecha                          "
                + "from vthdba.local_compras lc inner join vthdba.clientes_jumbo cj      "
                + "on lc.rut = cj.cli_rut                                                ";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Integer rut = new Integer(rs.getInt("rut"));
            long codigoBarra = rs.getLong("codigo_barra");
            float cantidad = rs.getFloat("cantidad");
            Date fec = rs.getDate("fecha");

            Compra compra = (Compra) listaDeCompras.get(rut);
            if (compra == null)
                compra = new Compra();

            compra.add(codigoBarra, cantidad, fec);
            listaDeCompras.put(rut, compra);
        }
        return listaDeCompras;
    }

}
