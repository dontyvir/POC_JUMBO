package cl.cencosud.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cl.cencosud.lucene.util.Producto;
import cl.cencosud.util.Db;
import cl.cencosud.util.LogUtil;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett
 *  
 */
public class IndizadorProductos {


    //Se remueve la llamada a LogUtil y la variable logger
    //
	   static {
        
        LogUtil.initLog4J();
        
    }
	
	static Logger logger = Logger.getLogger(IndizadorProductos.class);
	

    public IndizadorProductos() throws IOException {
        //se eimina la inicializacion de las variables en el constructor
    }

    public static void main(String[] args) throws Exception {
        java.util.Date horaInicial = new java.util.Date();
        logger.info("Inicia proceso :" + horaInicial + " param:" + (args.length > 0 ? args[0] : "s/p"));

        String user = Parametros.getString("USER");
        String password = Parametros.getString("PASSWORD");
        String driver = Parametros.getString("DRIVER");
        String url = Parametros.getString("URL");
        Connection con = Db.conexion(user, password, driver, url);
        con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        logger.info("CON : " + con);

        File indexDir = new File(Parametros.getString("PATH_INDEX"));

        long start = new Date().getTime();
        IndizadorProductos ip = new IndizadorProductos();
        if (args.length > 0 && args[0].equals("actualizar")) {
        	logger.info("Actualizando indices");
            ip.actualizarIndice(con);
        } else if (args.length > 0 && args[0].equals("autocompletar")) {
        	logger.info("Creando autocompletar");
            IndizadorProductos.indexAutocompletar(con);
        } else {
        	logger.info("creando indices");
            ip.index(Db.productosConEstado(con));
        }

        con.close();

        logger.info("__________tiempo : " + Db.calculaTiempo(new Date().getTime() - start));
    }

    public static void indexOrtografia() throws Exception {
        String path = Parametros.getString("PATH_ORTOGRAFIA");
        Directory spellIndexDirectory = FSDirectory.getDirectory(new File(path));
        SpellChecker spellchecker = new SpellChecker(spellIndexDirectory);
        spellchecker.clearIndex();

        spellchecker.indexDictionary(new PlainTextDictionary(new File(path + "/es_CL.txt")));
    }

    /**
     * Crea el indice lucene con todos los productos del FO
     *  
     */
    public void index(List productos) throws Exception {
        Directory directory = FSDirectory.getDirectory(new File(Parametros.getString("PATH_INDEX")));
        Analyzer analyzer = new SpanishAnalyzer(new SinonimoMap(Ind.SINONIMOS_FILE));
        IndexWriter writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

        writer.setUseCompoundFile(false);
        logger.info("-- Inicio index de productos");
        for (Iterator iter = productos.iterator(); iter.hasNext();) {
            Producto p = (Producto) iter.next();
            List docs = Ind.createDocuments(p, analyzer);
            for (int i = 0; i < docs.size(); i++) {
                writer.addDocument((Document) docs.get(i));
            }
        }
        writer.optimize();
        writer.commit();
        writer.close();
        logger.info("Fin maxDoc: " + writer.maxDoc() + " numDocs: " + writer.numDocs());
    }

    /**
     * Actualiza todos los datos de los productos de FO en el indice lucene
     */
    public void actualizarIndice(Connection con) throws Exception {
        Directory directory = FSDirectory.getDirectory(new File(Parametros.getString("PATH_INDEX")));
        List productos = Db.productosConEstado(con);

        logger.info("Actualizando... productos de la bd: " + productos.size());
        Analyzer analyzer = new SpanishAnalyzer(new SinonimoMap(Ind.SINONIMOS_FILE));
        IndexWriter writer = new IndexWriter(directory, analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
        writer.setUseCompoundFile(false);
        logger.info("-- Inicio actualizacion");
        try {
            QueryParser qpId = new QueryParser("id", new StandardAnalyzer());
            for (Iterator iter = productos.iterator(); iter.hasNext();) {
                Producto p = (Producto) iter.next();
                Query query = Ind.parseId(qpId, p.getIdFo());
                writer.deleteDocuments(query);
                List docs = Ind.createDocuments(p, analyzer);
                for (int i = 0; i < docs.size(); i++) {
                    writer.addDocument((Document) docs.get(i));
                }
            }
        } finally {
            writer.optimize();
            writer.commit();
            writer.close();
            logger.info("Fin maxDoc: " + writer.maxDoc() + " numDocs: " + writer.numDocs());
        }
    }

    public static void indexAutocompletar(Connection con) throws Exception {
        
        Directory dirIndex = FSDirectory.getDirectory(new File(Parametros.getString("PATH_INDEX")));
        Directory dirAuto = FSDirectory.getDirectory(new File(Parametros.getString("PATH_AUTOCOMPLETAR")));

        Analyzer analyzer = new SpanishAnalyzer(new SinonimoMap(Ind.SINONIMOS_FILE));

        List productos = Db.productosConEstado(con);
        List categorias = Db.categorias(con);
        TreeSet treeSet = new TreeSet();
        
        // por categorias
        logger.info("agrega categorias");
        for (int i = 0; i < categorias.size(); i++) {
            treeSet.add(((String) categorias.get(i)).toLowerCase().trim());
            if(((String) categorias.get(i)).toLowerCase().equals("máscara para pestañas"))
            	logger.info("OJO");
        }

        //      por nombre tipo producto
        logger.info("agrega productos");
        for (Iterator iter = productos.iterator(); iter.hasNext();) {
            Producto p = (Producto) iter.next();
        
            treeSet.add(p.getTipo().toLowerCase().trim());
            treeSet.add(p.getMarca().toLowerCase().trim());
        }

        // por nombre producto
//        System.out.println("agrega descripcion");
//        for (Iterator iter = productos.iterator(); iter.hasNext();) {
//            Producto p = (Producto) iter.next();
//            treeSet.add(p.getDescripcionCompleta().toLowerCase());
//        }

        IndexWriter writer = new IndexWriter(dirAuto, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        writer.setUseCompoundFile(false);

        // //////////
        IndexSearcher is = new IndexSearcher(dirIndex);
        // /////////

        for (Iterator iter = treeSet.iterator(); iter.hasNext();) {
            String producto = (String) iter.next();
            BooleanQuery bq = query(QueryParser.escape(producto), analyzer);
            TopDocCollector collector = new TopDocCollector(3000);
            is.search(bq, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            boolean found = false;
            Set local = new HashSet(); 
            for (int i = 0; i < hits.length; i++) {
                int docId = hits[i].doc;
                Document doc = is.doc(docId);
                
                String[] loc_id = doc.getValues("loc_id");
                for(int j=0;j<loc_id.length; j++) {
                    local.add(loc_id[j]);
                }
                found = true;
            }
            if(found) {
                Document doc = new Document();
                doc.add(new Field("producto", producto , Field.Store.YES, Field.Index.ANALYZED));
                Iterator iterator = local.iterator();
                while(iterator.hasNext()) {
                    doc.add(new Field("loc_id", (String) iterator.next(), Field.Store.YES, Field.Index.ANALYZED));
                }
                writer.addDocument(doc);
                logger.info(producto + "=" + hits.length + " hits "+ ArrayUtils.toString(local.toArray()));
                
            } else {
            	logger.info(producto + "=" + hits.length + " hits");
            }
        }
        

        writer.optimize();
        writer.close();

        is.close();

    }

    private static BooleanQuery query(String q, Analyzer analyzer) throws ParseException {
        QueryParser qpProd = new QueryParser("producto", analyzer);
        qpProd.setDefaultOperator(QueryParser.AND_OPERATOR);//por defecto and
        QueryParser qpCat = new QueryParser("categoria", analyzer);
        qpCat.setDefaultOperator(QueryParser.AND_OPERATOR);//por defecto and

        BooleanQuery bq = new BooleanQuery();
        bq.add(qpProd.parse(q), BooleanClause.Occur.SHOULD);
        bq.add(qpCat.parse(q), BooleanClause.Occur.SHOULD);
        return bq;
    }
    /**
    private static BooleanQuery query(String q, Analyzer analyzer) throws ParseException {

        QueryParser qpProd = new QueryParser("producto", analyzer);
        qpProd.setDefaultOperator(QueryParser.AND_OPERATOR);// por defecto and
        QueryParser qpCat = new QueryParser("categoria", analyzer);
        qpCat.setDefaultOperator(QueryParser.AND_OPERATOR);// por defecto and

        QueryParser qpPubAuto = new QueryParser("pubAutocompletar", new StandardAnalyzer());

        BooleanQuery bq0 = new BooleanQuery();
        bq0.add(qpProd.parse(q), BooleanClause.Occur.SHOULD);
        bq0.add(qpCat.parse(q), BooleanClause.Occur.SHOULD);

        BooleanQuery bq1 = new BooleanQuery();
        bq1.add(bq0, BooleanClause.Occur.MUST);
        bq1.add(qpPubAuto.parse("1"), BooleanClause.Occur.MUST);

        return bq0;
    }
**/
    public static String textoAutocompleta(String texto) {

        Pattern patron = Pattern.compile("[^\\w\\sÑñÁáÉéÍíÓóÚúÄäËëÏïÖöÜü']");
        Matcher match = patron.matcher(texto);
        texto = match.replaceAll(" ");

        String[] palabras = { "pote", "envase", "lata", "doy pack", "doypack", "gr", "grs", "gramo", "gramos", "litro", "litros", "kg", "kgs", "kilo", "kilos",
                "aprox", "unid", "unidad", "unidades", "caja", "cajas", "bolsa", "bolsas", "botella", "botellas", "pack", "pak", "bandeja", "bandejas", "mt",
                "mts", "cm", "cms", "mm", "ml", "cc", "c", "u", "n", "r", "f", "x" };
        StringBuffer palabraFuera = new StringBuffer("\\b\\d*");
        for (int i = 0; i < palabras.length; i++) {
            if (i < palabras.length - 1) {
                palabraFuera.append(palabras[i] + "\\b|\\b\\d*");
            } else {
                palabraFuera.append(palabras[i] + "\\b");
            }
        }

        patron = Pattern.compile(palabraFuera.toString(), Pattern.CASE_INSENSITIVE);
        match = patron.matcher(texto + " ");
        texto = match.replaceAll(" ");

        /*
         * elimina cualquier número, excepto mp3, mp4, 3M
         */
        patron = Pattern.compile("(\\b(\\d+[a-z]+\\d+)+\\b)|(\\b\\d+\\b)");
        match = patron.matcher(texto);
        texto = match.replaceAll("");

        patron = Pattern.compile("\\s{2,}");
        match = patron.matcher(texto);
        texto = match.replaceAll(" ");

        return texto;
    }
}
