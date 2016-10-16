package cl.cencosud.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;

import cl.cencosud.lucene.util.Producto;
import cl.cencosud.util.Parametros;

/**
 * @author jdroguett Centraliza la información que se guarda en el indice
 */
public class Ind {
    public static File SINONIMOS_FILE = new File(Parametros.getString("PATH_SINONIMOS"));

    public static File INDEX_DIR = new File(Parametros.getString("PATH_INDEX"));

    /**
     * Al buscar, por ejemplo, el número 10 lucene entrega todos los número que
     * comienzan con 10 (ie. 101, 102, etc). Esta marca evita que ocurra esto.
     */
    private static String MARCA = "j";

    public static List createDocuments(Producto p, Analyzer analyzer) throws Exception {
        List lista = new ArrayList();

        Document doc = createDocument(p, analyzer);

        Iterator ite = p.getCategorias().iterator();
        if (p.getCategorias().size() != 0) {
            while (ite.hasNext()) {
                String categoria = (String) ite.next();
                String cat = textoIndex(categoria);
                doc.add(new Field("categoria", categoria, Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field("stemCat", stem(cat, analyzer), Field.Store.YES, Field.Index.NO));
            }
            lista.add(doc);
        }
        return lista;
    }

    public static Document createDocument(Producto p, Analyzer analyzer) throws Exception {
        Document doc = new Document();

        // descripcion completa
        String producto = textoIndex(p.getTipo() + " " + p.getDescripcion() + " " + p.getMarca());

        // para buscar
        doc.add(new Field("id", p.getIdFo() + MARCA, Field.Store.YES, Field.Index.ANALYZED));
        //doc.add(new Field("producto", producto, Field.Store.YES, Field.Index.ANALYZED));

        doc.add(new Field("producto", p.getTipo(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("producto", p.getDescripcionBOCorta(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("producto", p.getDescripcionBOLarga(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("producto", p.getDescripcionLarga(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("producto", p.getMarca(), Field.Store.YES, Field.Index.ANALYZED));
        
        //doc.add(new Field("producto", producto, Field.Store.YES, Field.Index.ANALYZED));

        doc.add(new Field("descripcionCompleta", producto, Field.Store.YES, Field.Index.NO));

        doc.add(new Field("codigo", p.getCodigo(), Field.Store.YES, Field.Index.ANALYZED));
        // para ordenar
        doc.add(new Field("stemPro", stem(producto, analyzer), Field.Store.YES, Field.Index.NO));
        // datos del produco a mostrar
        doc.add(new Field("idBo", p.getIdBo() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("tipo", p.getTipo(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("descripcion", p.getDescripcion(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("unidadMedida", p.getUnidadMedida(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("imagenMiniFicha", (p.getImagenMiniFicha() == null ? "" : p.getImagenMiniFicha()), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("marcaId", p.getMarcaId() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("marca", p.getMarca(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("cantidadMinima", p.getCantidadMinima() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("cantidadMaxima", p.getCantidadMaxima() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("unidadNombre", p.getUnidadNombre(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("conNota", p.isConNota() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("esParticionable", p.getEsParticionable(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("particion", p.getParticion() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field("cantidadEnEmpaque", (p.getCantidadEnEmpaque() == null ? "0" : p.getCantidadEnEmpaque() + ""), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("precios", p.getPreciosCodeStr(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("publicaciones", p.getPublicacionesCodeStr(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("pubAutocompletar", (p.isPubAutocompletar() ? 1 : 0) + "", Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("tienestock", p.getTieneStockStr() , Field.Store.YES, Field.Index.NO));
        Iterator iterator = p.getLocales().iterator();
        while(iterator.hasNext()) {
            String loc_id = (String) iterator.next();
            doc.add(new Field("loc_id", loc_id, Field.Store.YES, Field.Index.ANALYZED));
            
        }
        return doc;
    } 

    /**
     * 
     * @param reader
     * @param query
     * @return
     * @throws CorruptIndexException
     * @throws IOException
     */
    public static List getProductos(IndexSearcher reader, Query query) throws CorruptIndexException, IOException {
        TopDocCollector collector = new TopDocCollector(50);
        reader.search(query, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        List lista = new ArrayList();
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Producto producto = getProducto(reader, docId);
            lista.add(producto);
        }
        return lista;
    }

    /**
     * @param reader
     * @param docId
     * @return
     * @throws CorruptIndexException
     * @throws IOException
     * @throws NumberFormatException
     */
    public static Producto getProducto(IndexSearcher reader, int docId) throws CorruptIndexException, IOException, NumberFormatException {
        Document doc = reader.doc(docId);
        Producto producto = new Producto();
        String sId = doc.get("id");
        sId = sId.substring(0, sId.length() - 1);// le saco la MARCA
        producto.setIdFo(Integer.parseInt(sId));
        producto.setDescripcionCompleta(doc.get("descripcionCompleta"));
        String[] stem = { doc.get("stemCat"), doc.get("stemPro") };
        producto.setStem(stem);
        producto.setCodigo(doc.get("codigo"));
        producto.setTipo(doc.get("tipo"));
        producto.setDescripcion(doc.get("descripcion"));
        producto.setUnidadMedida(doc.get("unidadMedida"));
        producto.setImagenMiniFicha(doc.get("imagenMiniFicha"));
        producto.setMarca(doc.get("marca"));
        producto.setMarcaId(Integer.parseInt(doc.get("marcaId")));
        producto.setCantidadMinima(new BigDecimal(doc.get("cantidadMinima")));
        producto.setCantidadMaxima(new BigDecimal(doc.get("cantidadMaxima")));
        producto.setUnidadNombre(doc.get("unidadNombre"));
        producto.setConNota("true".equals(doc.get("conNota")));
        producto.setIdBo(Integer.parseInt(doc.get("idBo")));
        producto.setEsParticionable(doc.get("esParticionable"));
        producto.setParticion(Long.parseLong(doc.get("particion")));
        producto.setCategoria(doc.get("categoria"));
        producto.setCantidadEnEmpaque(new BigDecimal(doc.get("cantidadEnEmpaque")));
        producto.setPreciosCodeStr(doc.get("precios"));
        producto.setPublicacionesCodeStr(doc.get("publicaciones"));
        return producto;
    }

    /**
     * Para que al buscar por id se busque el número exacto
     * 
     * @param id
     * @return
     * @throws ParseException
     */
    public static Query parseId(QueryParser qp, int id) throws ParseException {
        return qp.parse(id + "j");
    }

    /**
     * 
     * @param texto
     * @return
     */
    private static String textoIndex(String texto) {
        Pattern patron = Pattern.compile("\\-");
        Matcher match = patron.matcher(texto);
        texto = match.replaceAll(" ");
        return texto;
    }

    /**
     * Retorna texto aplicando stemer
     * 
     * @param s
     * @return
     * @throws IOException
     */
    private static String stem(String s, Analyzer analyzer) throws IOException {
        TokenStream ts = analyzer.tokenStream("", new StringReader(s));
        StringBuffer sb = new StringBuffer();
        Token token = new Token();
        while ((token = ts.next(token)) != null) {
            sb.append(token.term() + " ");
        }
        return sb.toString();
    }
}
