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

public class CustomCompator implements SortComparatorSource {
    
    static {
        
        LogUtil.initLog4J();
        
    }

    private Logger logger = Logger.getLogger(CustomCompator.class);

    private static final long serialVersionUID = 1194784480083412311L;
    private String q;

    public CustomCompator(String q) {
        this.q = q;
    }

    public ScoreDocComparator newComparator(final IndexReader indexReader, final String str) throws IOException {
        return new ScoreDocComparator() {

            public int compare(ScoreDoc scoreDoc1, ScoreDoc scoreDoc2) {
                try {
                    Document doc1 = indexReader.document(scoreDoc1.doc);
                    Document doc2 = indexReader.document(scoreDoc2.doc);
                    String strVal1 = doc1.get(str);
                    String strVal2 = doc2.get(str);

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
                        return strVal1.length() - strVal2.length();
                    }
                    return 1;
                } catch (IOException e) {
                    logger.error("IOException:"+e.getMessage(), e);
                }
                return 0;
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
