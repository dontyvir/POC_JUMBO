package cl.cencosud.lucene;

/**
 * @author jdroguett
 * 
 */
import java.io.File;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import cl.cencosud.util.Parametros;

public class SnowballAnalyzerTest extends TestCase {

    //private static File sinonimosFile = new File(Parametros.getString("PATH_SINONIMOS"));

    public SnowballAnalyzerTest(String name) {

        super(name);
    }

    public void assertAnalyzesTo( Analyzer a, String input, String[] output ) throws Exception {

        /*Token t = new Token();
        TokenStream ts = a.tokenStream("content", new StringReader(input));

        for ( int i = 0; i < output.length; i++ ) {
            t = ts.next(new Token());
            assertNotNull(t);
            String real = new String(t.termBuffer(), 0, t.termLength());
            assertEquals(output[i], real);
        }
        assertNull(ts.next(t));
        ts.close();*/
    }

    public void testSpanish() throws Exception {

        /*SinonimoMap sinonimoMap = new SinonimoMap(sinonimosFile);
        Analyzer a = new SpanishAnalyzer(sinonimoMap);*/
        /*
         * assertAnalyzesTo(a, "foo bar . FOO <> BAR", new String[] { "foo",
         * "bar", "foo", "bar" }); assertAnalyzesTo(a, "C.A.M.", new String[] {
         * "cam" }); assertAnalyzesTo(a, "C++", new String[] { "c" });
         * assertAnalyzesTo(a, "\"QUOTED\" word", new String[] { "quoted",
         * "word" }); assertAnalyzesTo(a, "El camino del hombre recto", new
         * String[] { "camin", "hombr", "rect"});
         */
        //assertAnalyzesTo(a, "lapices Azucar aZ�CAR coraz�nes azucares est� por todos lados rodeado de la injusticia de los ego�stas y la tiran�a de los hombres malos.", new String[] { "lapiz",
        //        "azucar", "azucar", "corazon", "azucar", "lado", "rodeado", "injustici", "egoista", "y", "tirania", "hombr", "malo" });
    }
}
