package cl.cencosud.procesos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.beans.Costo;
import cl.cencosud.beans.Empresa;
import cl.cencosud.beans.Inventario;
import cl.cencosud.beans.Producto;
import cl.cencosud.filtro.Filtro;
import cl.cencosud.util.Parametros;

public class Archivo {
    
    private static Logger logger = Logger.getLogger(Archivo.class);

    private static String path = Parametros.getString("PATH_ARCHIVOS");
    //private static String pathSST = Parametros.getString("PATH_ARCHIVOS_SST");    
    
	/**
     * Lee el csv
     * 
     * @param archivo
     * @return
     * @throws IOException
     */
    public static String[][] cargarCSV( String archivo ) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(path + archivo));
        String linea = null;

        List lista = new ArrayList();
        while ( ( linea = br.readLine() ) != null ) {
        	try{
            String col[] = linea.split(",");
            List columna = new ArrayList();
            for ( int j = 0; j < col.length; j++ ) {
                col[j] = col[j].trim();
                if ( col[j].charAt(0) == '"' && col[j].charAt(col[j].length() - 1) == '"' ) {
                    columna.add(col[j].substring(1, col[j].length() - 1));
                } else if ( col[j].charAt(0) == '"' && col[j].charAt(col[j].length() - 1) != '"' ) {
                    String tmp = col[j].substring(1, col[j].length());
                    while ( col[++j].charAt(col[j].length() - 1) != '"' ) {
                        tmp += "," + col[j].substring(0, col[j].length());
                    }
                    tmp += "," + col[j].substring(0, col[j].length() - 1);
                    columna.add(tmp);
                } else {
                    columna.add(col[j]);
                }
            }
            lista.add(columna);
	        }catch(Exception e){
	        	logger.error("Error al obtener el precio del archivo en la linea " + linea + ". Error: " + e.getMessage(), e);
				e.printStackTrace();
			}
        }
        String[][] datos = new String[lista.size()][];
        for ( int i = 0; i < lista.size(); i++ ) {
            datos[i] = (String[]) ( (ArrayList) lista.get(i) ).toArray(new String[ ( (ArrayList) lista.get(i) ).size()]);
        }
        br.close();
        return datos;
    }     
    
    /**
     * @param archivo_precios
     * @return
     * @throws IOException
     */
//    public static List cargarPreciosCSV( String archivo_precios ) throws IOException {
//
//        GregorianCalendar hoy = new GregorianCalendar();
//        int mes = Integer.parseInt(archivo_precios.substring(7, 9));
//        int dia = Integer.parseInt(archivo_precios.substring(9, 11));
//        hoy.set(Calendar.MONTH, mes - 1);
//        hoy.set(Calendar.DAY_OF_MONTH, dia);
//        hoy.set(Calendar.HOUR_OF_DAY, 0);
//        hoy.set(Calendar.MINUTE, 0);
//        hoy.set(Calendar.SECOND, 0);
//        hoy.set(Calendar.MILLISECOND, 0);
//
//        Date fechaPrecioNuevo = hoy.getTime();
//        // System.out.println("Fecha precio nuevo: " + fechaPrecioNuevo);
//
//        Hashtable unidades = new Hashtable();
//        // unidades que vienen en el archivo con unidades que se usan en la base
//        // de datos y en SAP
//        unidades.put("BG", "BAG");
//        unidades.put("CS", "CS");
//        unidades.put("KGM", "KG");
//        unidades.put("LTR", "L");
//        unidades.put("MTK", "M2");
//        unidades.put("MTR", "M");
//        unidades.put("PCE", "ST");
//        unidades.put("PK", "PAK");        
//           
//        BufferedReader br = new BufferedReader(new FileReader(path + archivo_precios));
//        String linea = null;
//        List lista = new ArrayList();
//        
//        long x = 0;        
//        while ( ( linea = br.readLine() ) != null ) {
//            x++;
//            try {
//                String unidad = linea.substring(44, 47).trim();
//                if ( unidades.get(unidad) != null ) {                	                	                       	                	
//                    Precio precio = new Precio();
//                    precio.setCodigoLocal(linea.substring(0, 4));
//                    /*
//                     * estoy suponiendo que los codigos de producto siempre son
//                     * enteros aunque se manejen como string, si fueran string
//                     * vendrián con espacios a la izquiersa y no con ceros como
//                     * vienen en el archivo de precios.
//                     */
//                    precio.setCodigoProducto(String.valueOf(Integer.valueOf(linea.substring(4, 22))));
//                    precio.setPrecio(Integer.valueOf(linea.substring(22, 32)).intValue());
//                    precio.setCodigoBarra(linea.substring(32, 44));
//                    precio.setUnidadMedida((String) unidades.get(unidad));
//                    precio.setFechaPrecioNuevo(fechaPrecioNuevo);
//                    precio.setNombreArchivo(archivo_precios);
//                    lista.add(precio);
//                }
//            } catch (Exception e) {
//                logger.error("Error al obtener precio del archivo:"+archivo_precios+" en la linea:"+x+". Error: " + e.getMessage(), e);
//            }
//        }           
//        logger.info("Precios cargados:" + lista.size());        
//        br.close();
//        return lista;
//    }
       
    	    
    /**
     * Carga una lista de objetos barra del CSV
     * 
     * @param archivo
     * @return lista de objetos barra
     * @throws IOException
     */
    public static List cargarBarrasCSV( String archivo ) throws IOException {

        // ('BC','IE','HE','HK','IC','CH','UC','VC','ZP')
        HashSet tiposPermitidos = new HashSet();
        tiposPermitidos.add("BC");
        tiposPermitidos.add("IE");
        tiposPermitidos.add("HE");
        tiposPermitidos.add("HK");
        tiposPermitidos.add("IC");
        tiposPermitidos.add("CH");
        tiposPermitidos.add("UC");
        tiposPermitidos.add("VC");
        tiposPermitidos.add("ZP");

        // /nuevos tipos de codigos de barra
        tiposPermitidos.add("ZH");
        tiposPermitidos.add("JC");
        tiposPermitidos.add("ZE");
        tiposPermitidos.add("ZS");
        // /

        HashSet unidadesPermitidas = new HashSet();
        unidadesPermitidas.add("BAG");
        unidadesPermitidas.add("CS");
        unidadesPermitidas.add("KG");
        unidadesPermitidas.add("L");
        unidadesPermitidas.add("M");
        unidadesPermitidas.add("M2");
        unidadesPermitidas.add("PAK");
        unidadesPermitidas.add("ST");

        /*
         * Otras unidades KAN, PAK, CS, L, BUL, ST, M, KG, M2, BAG, GLL, ROM,
         * OF3
         */
        String[][] values = cargarCSV(archivo);;
       
        List barras = new ArrayList();
        for ( int i = 0; i < values.length; i++ ) {
        	try {
            if ( tiposPermitidos.contains(values[i][2]) && values[i][1].length() < 14 && unidadesPermitidas.contains(values[i][4]) ) {
                Barra barra = new Barra();
                barra.setCodigoProducto(values[i][0]);
                barra.setCodigo(values[i][1]);
                barra.setTipo(values[i][2]);
                // permite nulos
                barra.setPpal(values[i][3].equals("") ? null : values[i][3]);
                barra.setUnidad(values[i][4]);
                barras.add(barra);
            }
        	} catch (Exception e) {
        		logger.error("Problema al cargar cvs de codigos de barra, se salta la linea "+values[i]+e);
			}
        }

        logger.info("Carga Barras CSV.- Barras rechazadas por tipo:" + ( values.length - barras.size() ) + ", aceptadas:" + barras.size());
        return barras;
    }

    /**
     * El código del producto (cod_prod1) aparece una sola vez en el archivo CSV
     * de productos. (Aparece varias veces en el archivo de códigos de barras)
     * 
     * @param archivo
     * @return clave hash: codigoProducto, dato:un objeto producto
     * @throws IOException
     */
    public static Hashtable cargarProductosCSV( String archivo ) throws IOException {

        Hashtable productos = new Hashtable();
        String[][] values = cargarCSV(archivo);
        for ( int i = 0; i < values.length; i++ ) {
        	try{
            Producto producto = new Producto();
            producto.setCodigo(values[i][0]);
            producto.setCodigo2( ( "".equals(values[i][1]) ? null : values[i][1] ));
            producto.setNombre( ( "".equals(values[i][2]) ? null : values[i][2] ));
            producto.setDescripcion( ( "".equals(values[i][3]) ? null : values[i][3] ));
            producto.setEstado(Integer.parseInt(values[i][4]));
            producto.setCodigoCategoria(values[i][5]);
            producto.setMarca(values[i][6]);
            producto.setCodigoProPpal(values[i][7]);
            producto.setOrigen(values[i][8]);
            producto.setUnidadBase(values[i][9]);
            producto.setEan13(values[i][10]);
            producto.setUnidadEmpaque(values[i][11]);

            // la columna 11 y 12 corresponden a num_conv y den_conv pero no se
            // usan
            producto.setAtributo9(values[i][14]);
            producto.setAtributo10(values[i][15]);
            productos.put(producto.getCodigo(), producto);
        	}catch(Exception e){
        		logger.error(values[i],e);
        }
        }
        values = null;
        logger.info("Carga productos CSV: " + productos.size());
        return productos;
    }

    /**
     * clave: codigoLocal + codigoProducto, dato: objeto inventario
     * 
     * @param archivo
     * @return
     * @throws IOException
     */
    public static Hashtable cargarInventarioCSV( String archivo ) throws IOException {

        Hashtable inventarios = new Hashtable();
        String[][] values = cargarCSV(archivo);
        for ( int i = 0; i < values.length; i++ ) {
        	try {
            String codigoLocal = values[i][1];
            String codigoProducto = values[i][2];
            String costoPromedio = values[i][10];

            Inventario inventario = new Inventario();
            inventario.setMixLocal(Integer.parseInt(values[i][values[i].length - 3]));
            inventario.setBloqueoCompra(values[i][values[i].length - 2]);
            inventario.setCostoPromedio(Integer.parseInt(costoPromedio));
            inventarios.put(codigoLocal + codigoProducto, inventario);
			} catch (Exception e) {
				logger.error(values[i],e);
			}
        }

        logger.info("CSV Inventario cargado: " + inventarios.size());
        return inventarios;
    }

    public static String nombreArchivoCSV( String prefijo, String extension ) {

        return nombreArchivoCSV(null, prefijo, extension);
    }

    public static String nombreArchivoCSV( String ruta, String prefijo, String extension ) {

        if ( ruta != null ) {
            path = ruta;
        }

        File directorio = new File(path);

        logger.info("Directorio : " +directorio);
        
        String[] lista = directorio.list(new Filtro(prefijo, extension));

        if ( lista.length == 1 ) {
            return lista[0];
        }

        int fechaMax = 0;
        String archivo = "";
        for ( int i = 0; i < lista.length; i++ ) {
            System.out.println(lista[i]);
            String sFecha = lista[i].substring(prefijo.length(), lista[i].length() - 4);
            System.out.println(sFecha);
            int fecha = Integer.parseInt(sFecha.substring(4) + sFecha.substring(0, 2) + sFecha.substring(2, 4));

            if ( fecha > fechaMax ) {
                archivo = lista[i];
                fechaMax = fecha;
            }
        }

        return archivo;
    }
    
    /* 
     * Se comentan el meetodo, NO pertenecen al modulo /process-cargaproductos,
     * estas estan vinculadas al modulo /process-stock-online
     * */
    /*
    public static String nombreArchivoCSV( String ruta, String prefijo, String extension, String rutaSST ) {       
        File directorio = new File(pathSST);

        logger.info("Directorio : " +directorio);
        
        String[] lista = directorio.list(new Filtro(prefijo, extension));

        if ( lista.length == 1 ) {
            return lista[0];
        }
        int fechaMax = 0;
        String archivo = "";
        for ( int i = 0; i < lista.length; i++ ) {
            System.out.println(lista[i]);
            String sFecha = lista[i].substring(prefijo.length(), lista[i].length() - 4);
            System.out.println(sFecha);
            int fecha = Integer.parseInt(sFecha.substring(4) + sFecha.substring(0, 2) + sFecha.substring(2, 4));

            if ( fecha > fechaMax ) {
                archivo = lista[i];
                fechaMax = fecha;
            }
        }
        return archivo;
    }
    */

    public static String nombreArchivo( String prefijo ) {

        return nombreArchivo(null, prefijo);
    }

    public static String nombreArchivo( String ruta, String prefijo ) {

        if ( ruta != null ) {
            path = ruta;
        }
        File directorio = new File(path);

        String[] lista = directorio.list(new Filtro(prefijo, ""));

        if ( lista.length == 1 ) {
            return lista[0];
        }

        int fechaMax = 0;
        String archivo = "";
        for ( int i = 0; i < lista.length; i++ ) {
            if ( lista[i].indexOf('.') != -1 ) {
                continue;
            }
            // sin año, sólo mes y día
            String sFecha = lista[i].substring(prefijo.length(), prefijo.length() + 4);
            int fecha = Integer.parseInt(sFecha.substring(0, 2) + sFecha.substring(2));

            if ( fecha > fechaMax ) {
                archivo = lista[i];
                fechaMax = fecha;
            }
        }

        return archivo;
    }

    /**
     * Formato archivo: dos columnas: primera columna codigo producto, segunda
     * columna unidad de medida
     * 
     * @param archivo
     * @return clave: unidad, dato: arraylist(String) de codigos
     * @throws IOException
     */
    public static Hashtable cargarProductosBloqueados( String archivo ) throws IOException {

        String[][] values = cargarCSV(archivo);
        Hashtable unidadMedida = new Hashtable();

        for ( int i = 0; i < values.length; i++ ) {
            try {
            String codigo = values[i][0];
            String unidad = values[i][1];

            List codigos = (List) unidadMedida.get(unidad);
            if ( codigos == null ) {
                codigos = new ArrayList();
            }
            codigos.add(codigo);
            unidadMedida.put(unidad, codigos);
            } catch (Exception e) {
            	logger.error(values[i],e);
			}
            
            
        }
        return unidadMedida;
    }

    /**
     * @param string
     */
    public static void borrar( String archivo ) {

        File file = new File(path + archivo);
        file.delete();
    }

    /**
     * @param archivo
     * @return
     * @throws Exception
     */
    public static List cargarEmpresas( String archivo ) throws Exception {

        List empresas = new ArrayList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path + archivo));
            String linea;
            while ( ( linea = br.readLine() ) != null ) {
                if ( linea.equals("") ) {
                    continue;
                }
                String rut = linea.substring(221, 237).trim();
                String saldo = linea.substring(293, 308).trim();
                Empresa empresa = new Empresa();
                empresa.setDv(rut.charAt(rut.length() - 1));
                empresa.setRut(Integer.parseInt(rut.substring(0, rut.length() - 2)));
                if ( saldo.charAt(saldo.length() - 1) == '-' ) {
                    empresa.setSaldo(new BigDecimal("-" + saldo.substring(0, saldo.length() - 1)));
                } else {
                    empresa.setSaldo(new BigDecimal(saldo));
                }
                empresas.add(empresa);
            }
        } catch (Exception e) {
            logger.error("Exception:"+e.getMessage(), e);
            throw e;
        } finally {
            if ( br != null ) {
                br.close();
            }
        }
        return empresas;
    }

    public static Hashtable cargarCostos( String archivo ) throws Exception {

        BigDecimal CERO = new BigDecimal("0");
        Hashtable costos = new Hashtable();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path + archivo));
            String linea;
            while ( ( linea = br.readLine() ) != null ) {
                if ( linea.equals("") ) {
                    continue;
                }
                String codigo = linea.substring(0, 18).trim();
                String sCosto = linea.substring(22, 34).trim();
                Costo costo = new Costo();
                try {
                    costo.setCodigoProd( ( new Long(codigo) ).toString());
                } catch (Exception e) {
                    continue;
                }
                costo.setCosto(new BigDecimal(sCosto));
                if ( !costo.getCosto().equals(CERO) ) {
                    costos.put(costo.getCodigoProd(), costo);
                }
            }
        } catch (Exception e) {
            logger.error("Exception:"+e.getMessage(), e);
            throw e;
        } finally {
            if ( br != null ) {
                br.close();
            }
        }
        return costos;
    }

}