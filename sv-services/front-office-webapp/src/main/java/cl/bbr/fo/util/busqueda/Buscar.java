/*
 * Created on 09-mar-2009
 */
package cl.bbr.fo.util.busqueda;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cl.bbr.jumbocl.productos.dto.ProductoDTO;
import cl.bbr.log.Logging;

/**
 * @author jdroguett
 *  
 */
public class Buscar {
    private static ResourceBundle rb = ResourceBundle.getBundle("fo");

    private static File indexDir = new File(rb.getString("path.buscador"));

    private static Logging logger = new Logging(Buscar.class);

    private static int MAX_SUG = 50;

    /**
     * Entrega la lista de ids de los productos encontrados seg˙n las palabras a
     * buscar.
     * 
     * @param q
     * @return
     * @throws Exception
     */
    public static List search(String q) throws Exception {
        if ("".equals(q))
            return new ArrayList();

        //evito que busque secuencias de menos de 3 caracteres con *, por ej:
        // a*, pa*, etc.
        //Ahora no permito * para no saturar el servidor
        if (q.indexOf("*") != -1) {
            /*
             * Pattern patron =
             * Pattern.compile("[^\\w\\s—Ò¡·…ÈÕÌ”Û⁄˙ƒ‰ÀÎœÔ÷ˆ‹¸']"); Matcher
             * match = patron.matcher(q); String otro_q = match.replaceAll("");
             * if (otro_q.length() < 3) { return new ArrayList(); }
             */
            return new ArrayList();
        }
        q = QueryParser.escape(q);

        List idsProductos = new ArrayList();
        IndexSearcher is = null;
        Directory fsDir = null;
        SpanishAnalyzer analyzer = new SpanishAnalyzer(Sinonimos.getInstance().getSinominoMap());
        try {
            fsDir = FSDirectory.getDirectory(indexDir);
            is = new IndexSearcher(fsDir);
            BooleanQuery bq = query(q, analyzer);
            /////////////////////////
            //Al ordenar con Lucene se consume la memoria luego de un tiempo
            //SortField sortField = new SortField("producto", new
            // Comparator(q.toLowerCase()));
            //Sort orden = new Sort(sortField);
            /////////////////////////
            //Hits hits = is.search(query, orden);
            TopDocCollector collector = new TopDocCollector(3000);
            is.search(bq, collector);
            //collector = new TopDocCollector(is.maxDoc());
            //is.search(query, collector);

            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            logger.info("(search)Busqueda por: '" + q + "' hits: " + hits.length);

            List lista = new ArrayList();
            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document doc = is.doc(docId);
                ProductoDTO producto = new ProductoDTO();
                String sId = doc.get("id");
                sId = sId.substring(0, sId.length() - 1);
                producto.setPro_id(Long.parseLong(sId));
                producto.setDescripcion(doc.get("producto"));
                String[] stem = { doc.get("stemCat"), doc.get("stemPro") };
                producto.setStem(stem);
                lista.add(producto);
            }
            //ordenar sin usar lucene
            TokenStream tokenStream = analyzer.tokenStream("", new StringReader(q));
            Collections.sort(lista, new OrdenBusqueda(tokenStream));
            //fin ordenar

            for (int i = 0; i < lista.size(); i++) {
                idsProductos.add(((ProductoDTO) lista.get(i)).getPro_id() + "");
            }
        } catch (Exception e) {
            logger.error("Error en la busqueda (Buscar): " + e);
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
            if (fsDir != null)
                fsDir.close();
        }
        return idsProductos;
    }

    public static List productos(String q, int idLocal) throws Exception {
        if ("".equals(q))
            return new ArrayList();

        if (q.indexOf("*") != -1) {
            return new ArrayList();
        }
        q = QueryParser.escape(q);

        IndexSearcher is = null;
        Directory fsDir = null;

        SpanishAnalyzer analyzer = new SpanishAnalyzer(Sinonimos.getInstance().getSinominoMap());
        Hashtable lista = new Hashtable();

        List l = new ArrayList();
        try {
            fsDir = FSDirectory.getDirectory(indexDir);
            is = new IndexSearcher(fsDir);
            BooleanQuery bq = query(q, analyzer);

            TopDocCollector collector = new TopDocCollector(3000);
            is.search(bq, collector);

            ScoreDoc[] hits = collector.topDocs().scoreDocs;

            if (hits.length == 0) {
                Query qCod = queryCodigo(q);
                is.search(qCod, collector);
                hits = collector.topDocs().scoreDocs;
            }

            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document doc = is.doc(docId);
                //13-12-2012 hans santibanez - validaciÛn que no sea nulo y que no sea string blanco
                if (doc.get("publicaciones")!=null && !"".equals(doc.get("publicaciones").trim()) && isPublicado(idLocal, doc.get("publicaciones"))) {
                //13-12-2012 hans santibanez
                    ProductoDTO producto = new ProductoDTO();
                    String sId = doc.get("id");
                    sId = sId.substring(0, sId.length() - 1);
                    producto.setPro_id(Long.parseLong(sId));
                    producto.setDescripcionCompleta(doc.get("producto"));
                    String[] stem = { doc.get("stemCat"), doc.get("stemPro") };
                    producto.setStem(stem);

                    producto.setTipo_producto(doc.get("tipo"));
                    producto.setDescripcion(doc.get("descripcion"));
                    producto.setTipre(doc.get("unidadMedida"));
                    producto.setImg_chica(doc.get("imagenMiniFicha"));
                    //
                    producto.setPrecio(getPrecio(idLocal, doc.get("precios")));
                    producto.setCantidadEnEmpaque(Double.parseDouble(doc.get("cantidadEnEmpaque")));
                    //
                    producto.setMarca(doc.get("marca"));
                    producto.setMarca_id(Long.parseLong(doc.get("marcaId")));
                    producto.setCantidadMinima(new BigDecimal(doc.get("cantidadMinima")));
                    producto.setCantidadMaxima(new BigDecimal(doc.get("cantidadMaxima")));
                    producto.setUnidad_nombre(doc.get("unidadNombre"));
                    producto.setCon_nota("true".equals(doc.get("conNota")));
                    producto.setPro_id_bo(Integer.parseInt(doc.get("idBo")));
                    producto.setEsParticionable(doc.get("esParticionable"));
                    producto.setParticion(Long.parseLong(doc.get("particion")));
                    producto.setCategoria(doc.get("categoria"));
                    if ((doc.get("tienestock") != null) && (getStock(idLocal,doc.get("tienestock")).equals("true")))
                        producto.setTieneStock(true);
                    else if (doc.get("tienestock") == null)
                        producto.setTieneStock(true);
                    else
                        producto.setTieneStock(false);
                    lista.put(new Long(producto.getPro_id()), producto);
                }
            }
            //20120822 Andres Valle
            logger.info("(productos)Busqueda por: '" + q + "' hits: " + hits.length + "/" + lista.size());
            //-20120822 Andres Valle
            //ordenar sin usan lucene
            l = new ArrayList(lista.values());

            TokenStream tokenStream = analyzer.tokenStream("", new StringReader(q));
            Collections.sort(l, new OrdenBusqueda(tokenStream));
            //fin ordenar
        } catch (Exception e) {
            logger.error("Error en la busqueda (Buscar): " + e);
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
            if (fsDir != null)
                fsDir.close();
        }
        return l;
    }

    private static double getPrecio(int local, String precios) {
        String[] aPrecios = precios.split("\\|");
        for (int i = 0; i < aPrecios.length; i += 2) {
            if (Integer.parseInt(aPrecios[i]) == local)
                return Double.parseDouble(aPrecios[i + 1]);
        }
        return 0;
    }

    private static String getStock(int local, String stock) {
    	logger.info("stock: "+stock);
    	if (!"".equals(stock) && stock.indexOf("|")!=-1){
	    	String[] arregloStrock = new String[1];
	    	if(stock.indexOf(",")!=-1){
	    		arregloStrock = stock.split(",");
	    	}else{
	    		arregloStrock[0] = stock;
	    	}
	    	String localStr = "";
	    	String resultado = "";
	        if (arregloStrock != null){
		        for (int i = 0; i < arregloStrock.length; i++) {
		        	if(arregloStrock[i].indexOf("|")!=-1){
			        	localStr = arregloStrock[i].substring(0,arregloStrock[i].indexOf("|"));
			        	resultado = arregloStrock[i].substring(arregloStrock[i].indexOf("|")+1 ,arregloStrock[i].length());
		        	}
		            if (Integer.parseInt(localStr) == local)
		                return resultado;
		        }
	        }
    	}
        return "true";
    }
    
    private static boolean isPublicado(int local, String publicaciones) {
        String[] aPublicaciones = publicaciones.split("\\|");
        for (int i = 0; i < aPublicaciones.length; i += 2) {
            if (Integer.parseInt(aPublicaciones[i]) == local)
                return "true".equals(aPublicaciones[i + 1]);
        }
        return false;
    }

    /**
     * @param q
     * @param idLocal
     * @param analyzer
     * @return
     * @throws ParseException
     */
    private static BooleanQuery query(String q, SpanishAnalyzer analyzer) throws ParseException {
        QueryParser qpProd = new QueryParser("producto", analyzer);
        qpProd.setDefaultOperator(QueryParser.AND_OPERATOR);//por defecto and
        QueryParser qpCat = new QueryParser("categoria", analyzer);
        qpCat.setDefaultOperator(QueryParser.AND_OPERATOR);//por defecto and

        BooleanQuery bq = new BooleanQuery();
        bq.add(qpProd.parse(q), BooleanClause.Occur.SHOULD);
        bq.add(qpCat.parse(q), BooleanClause.Occur.SHOULD);
        logger.debug("query: " + bq);
        return bq;
    }

    private static Query queryCodigo(String q) throws ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        QueryParser qpCod = new QueryParser("codigo", analyzer);
        qpCod.setDefaultOperator(QueryParser.AND_OPERATOR);//por defecto and
        logger.debug("query: " + qpCod.parse(q));
        return qpCod.parse(q);
    }

    /**
     * @param palabra
     * @return
     * @throws IOException
     */
    public static String[] ortografia(String palabra) throws IOException {
        palabra = palabra.toLowerCase();

        Directory fsDir = null;
        IndexSearcher is = null;
        try {
            //la primer vez se demora ya que carga el diccionario
            SpellChecker spellChecker = Ortografia.getInstance().getSpellChecker();
            String[] resultado = spellChecker.suggestSimilar(palabra, 5);

            fsDir = FSDirectory.getDirectory(indexDir);
            is = new IndexSearcher(fsDir);
            SpanishAnalyzer analyzer = new SpanishAnalyzer(Sinonimos.getInstance().getSinominoMap());

            List lista = new ArrayList();
            try {
                for (int i = 0; i < resultado.length; i++) {
                    Query query = query(resultado[i], analyzer);
                    TopDocCollector collector = new TopDocCollector(1);
                    is.search(query, collector);
                    ScoreDoc[] hits = collector.topDocs().scoreDocs;
                    logger.debug("cantidad: " + resultado[i] + "   " + hits.length);
                    if (hits.length > 0)
                        lista.add(resultado[i]);
                }
            } catch (Exception e) {
                logger.error("Error al buscar en ortografia: " + e.getMessage());
            }
            return (String[]) lista.toArray(new String[lista.size()]);
        } finally {
            if (is != null)
                is.close();
            if (fsDir != null)
                fsDir.close();
        }
    }

    /**
     * @param listaProductos
     * @return
     */
    public static String[] sugerencias(List listaProductos) {
        class Sugerencia implements Comparable {
            StringBuffer pal = new StringBuffer();

            int pos;

            public int compareTo(Object o) {
                Sugerencia s1 = (Sugerencia) o;
                return this.pos - s1.pos;
            }
        }
        HashSet stop = SpanishAnalyzer.SP_STOP_WORDS;
        Hashtable hash = new Hashtable();
        for (int k = 0; k < listaProductos.size(); k++) {
            ProductoDTO productoDTO = (ProductoDTO) listaProductos.get(k);
            String producto = productoDTO.getDescripcionCompleta().toLowerCase();
            String[] palabras = producto.split("[\\s\\,\\.\\&]+");
            int n = 0;
            Sugerencia sug = new Sugerencia();
            for (int i = 0; i < palabras.length; i++) {
                String p = palabras[i];
                sug.pal.append(p + " ");
                sug.pos = k;
                if (!stop.contains(p))
                    n++;
                if (n > 1) {
                    hash.put(sug.pal.toString(), sug);
                    break;
                }
            }
            if (hash.size() > MAX_SUG) //supuesto: listaProductos viene
                // ordenado
                break;
        }

        List l = new ArrayList(hash.values());
        Collections.sort(l);
        String[] resultado = new String[(l.size() < MAX_SUG ? l.size() : 50)];
        for (int i = 0; i < l.size() && i < MAX_SUG; i++) {
            resultado[i] = ((Sugerencia) l.get(i)).pal.toString();
        }
        return resultado;
    }

    public static void main(String[] args) throws Exception {
        List lista = Buscar.productos("bebidas", 2);
}
}
