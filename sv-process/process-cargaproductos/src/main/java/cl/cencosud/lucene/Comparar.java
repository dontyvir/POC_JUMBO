package cl.cencosud.lucene;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.search.SortField;

import cl.cencosud.util.LogUtil;

public class Comparar implements SortComparatorSource {
    
    static {
        
        LogUtil.initLog4J();
        
    }

    private Logger logger = Logger.getLogger(Comparar.class);
    
    /**
	 * 
	 */    
    private static final long serialVersionUID = -851006496236144148L;
    private String q;

    public Comparar(String q) {

        this.q = q;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.lucene.search.SortComparatorSource#newComparator(org.apache.lucene.index.IndexReader,
     *      java.lang.String)
     */
    public ScoreDocComparator newComparator(final IndexReader reader, final String fieldname) throws IOException {
        return new ScoreDocComparator() {
            public int compare(ScoreDoc i, ScoreDoc j) {
                try {
                    Document doc1 = reader.document(i.doc);
                    Document doc2 = reader.document(j.doc);
                    String strVal1 = doc1.get(fieldname);
                    String strVal2 = doc2.get(fieldname);

                    int i1 = strVal1.toLowerCase().indexOf(q);
                    int i2 = strVal2.toLowerCase().indexOf(q);

                    if ( i1 < 0 && i2 < 0 ) {
                        return strVal1.compareTo(strVal2);
                    } else if ( i1 < 0 ) {
                        return 1;
                    } else if ( i2 < 0 ) {
                        return -1;
                    } else if ( i1 < i2 ) {
                        return -1;
                    } else if ( i1 == i2 ) {
                        return strVal1.compareTo(strVal2);
                    }
                    return 1;
                } catch (Exception e) {
                    logger.error("Exception:"+e.getMessage(), e);
                    return 0;
                }
            }

            public Comparable sortValue( ScoreDoc scoreDoc ) {
                return new Float(scoreDoc.doc);
            }

            public int sortType() {
                return SortField.CUSTOM;
            }
        };
    }
}
