/*
 * Created on 12-nov-2008
 */
package cl.bbr.fo.command.mobi;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import cl.bbr.fo.command.Command;
import cl.bbr.fo.util.busqueda.OrdenAutoCompletar;
import cl.bbr.fo.util.busqueda.Sinonimos;
import cl.bbr.fo.util.busqueda.SpanishAnalyzer;

/**
 * @author jdroguett
 *  
 */
public class PasoDosAutocompletar extends Command {
	private File indexDir;

	public PasoDosAutocompletar() {
		ResourceBundle rb = ResourceBundle.getBundle("fo");
		indexDir = new File(rb.getString("path.autocompletar"));
	}

	protected void execute(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		arg1.setCharacterEncoding("UTF-8");
		arg0.setCharacterEncoding("UTF-8");

		String q = arg0.getParameter("q");
		//      getLogger().debug("qqq: " + q);

		PrintWriter out = arg1.getWriter();
		StringBuffer result = new StringBuffer();

//+20121024avc
		String localId = null;
		if (arg0.getSession().getAttribute("ses_loc_id") != null) {
		    localId = arg0.getSession().getAttribute("ses_loc_id").toString();
		    
		}
		List productos = search(q, localId);
//-20121024avc		
		for (int i = 0; i < productos.size(); i++) {
			result.append(productos.get(i) + "\n");
		}
		out.print(result.toString());
	}

//+20121024avc
	private List search(String q, String localId) throws Exception {
//-20121024avc
		Directory fsDir = null;
		IndexSearcher is = null;
		try {
			fsDir = FSDirectory.getDirectory(indexDir);
			is = new IndexSearcher(fsDir);

//+20121024avc			
			Query query = createQuery(q, localId);
//-20121024avc
			//Pierde memoria al ordenar con lucene, se ordena al final el
			//resultado
			//SortField sortField = new SortField("producto", new
			// ComparatorAutoCompletar(q.toLowerCase()));
			//Sort orden = new Sort(sortField);
			//Hits hits = is.search(query, orden);

			TopDocCollector collector = new TopDocCollector(600);
			is.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			getLogger().debug("autocompletar hits.length(): " + hits.length);
			logger.info("(autocompletar)Busqueda por: '" + q + "' hits: " + hits.length);
			List lista = new ArrayList();
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				Document doc = is.doc(docId);
				lista.add(doc.get("producto") + "|" + doc.get("cantidad"));
			}
			if (lista.size() > 1) {
				Collections.sort(lista, new OrdenAutoCompletar(q.toLowerCase()));
			}
			getLogger().debug("lista.size(): " + lista.size());
			if (lista.size() > 200)
				return lista.subList(0, 200);
			return lista;
		} catch (Exception e) {
			this.getLogger().error("Error en la busqueda (Autocompletar): " + e);
			e.printStackTrace();
		} finally {
			if (is != null)
				is.close();
			if (fsDir != null)
				fsDir.close();
		}
		return new ArrayList();
	}

//+20121024avc
    /**
     * @param q
     * @return
     * @throws ParseException
     */
    private Query createQuery(String q, String localId) throws ParseException {
		q = q.trim();
		q = QueryParser.escape(q);

		Analyzer analyzer = new SpanishAnalyzer(Sinonimos.getInstance().getSinominoMap());
		QueryParser qp = new QueryParser("producto", analyzer);
		qp.setDefaultOperator(QueryParser.AND_OPERATOR);
		
		BooleanQuery bq = new BooleanQuery();
		bq.add(qp.parse(q), BooleanClause.Occur.MUST);
		
		if(localId != null) {
		    bq.add(new QueryParser("loc_id", analyzer).parse(localId), BooleanClause.Occur.MUST);
		}
        // TODO Auto-generated method stub
        return bq;
    }
//-20121024avc    
}
