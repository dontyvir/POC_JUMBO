/*
 * Created on 26-ago-2008
 *
 */
package cl.cencosud.lucene;

/**
 * @author jdroguett
 *
 */
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/** Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link 
 * LowerCaseFilter}, {@link StopFilter} and {@link SpanishStemFilter}. */

/**
 * Analyzer for Spanish using the <b
 * style="color:black;background-color:#ffff66">SNOWBALL </b> stemmer. Supports
 * an external list of stopwords (words that will not be indexed at all). A
 * default set of stopwords is used unless an alternative list is specified, the
 * exclusion list is empty by default.
 */

public class SpanishAnalyzer extends Analyzer {

   /**
    * An array containing some common Spanish words that are usually not useful
    * for searching. Imported from http://www.unine.ch/info/clef/.
    */

   public static final String[] SPANISH_STOP_WORDS = { "a", "al", "algo", "algunas", "algunos", "ante", "antes",
         "como", "con", "contra", "cual", "cuando", "de", "del", "desde", "donde", "durante", "e", "el", "ella",
         "ellas", "ellos", "en", "entre", "era", "erais", "eramos", "eran", "eras", "eres", "es", "esa", "esas", "ese",
         "eso", "esos", "esta", "esta", "estaba", "estabais", "estabamos", "estaban", "estabas", "estad", "estada",
         "estadas", "estado", "estados", "estais", "estamos", "estan", "estando", "estar", "estara", "estaran",
         "estaras", "estare", "estareis", "estaremos", "estaria", "estariais", "estariamos", "estarian", "estarias",
         "estas", "estas", "este", "este", "esteis", "estemos", "esten", "estes", "esto", "estos", "estoy", "estuve",
         "estuviera", "estuvierais", "estuvieramos", "estuvieran", "estuvieras", "estuvieron", "estuviese",
         "estuvieseis", "estuviesemos", "estuviesen", "estuvieses", "estuvimos", "estuviste", "estuvisteis", "estuvo",
         "fue", "fuera", "fuerais", "fueramos", "fueran", "fueras", "fueron", "fuese", "fueseis", "fuesemos", "fuesen",
         "fueses", "fui", "fuimos", "fuiste", "fuisteis", "ha", "habeis", "haber", "habia", "habiais", "habiamos",
         "habian", "habias", "habida", "habidas", "habido", "habidos", "habiendo", "habra", "habran", "habras",
         "habre", "habreis", "habremos", "habria", "habriais", "habriamos", "habrian", "habrias", "han", "has",
         "hasta", "hay", "haya", "hayais", "hayamos", "hayan", "hayas", "he", "hemos", "hube", "hubiera", "hubierais",
         "hubieramos", "hubieran", "hubieras", "hubieron", "hubiese", "hubieseis", "hubiesemos", "hubiesen",
         "hubieses", "hubimos", "hubiste", "hubisteis", "hubo", "la", "las", "le", "les", "lo", "los", "mas", "me",
         "mi", "mia", "mias", "mio", "mios", "mis", "mucho", "muchos", "muy", "nada", "ni", "no", "nos", "nosotras",
         "nosotros", "nuestra", "nuestras", "nuestro", "nuestros", "o", "os", "otra", "otras", "otro", "otros", "para",
         "pero", "poco", "por", "porque", "que", "quien", "quienes", "se", "sea", "seais", "seamos", "sean", "seas",
         "ser", "sera", "seran", "seras", "sere", "sereis", "seremos", "seria", "seriais", "seriamos", "serian",
         "serias", "si", "sido", "siendo", "sin", "sobre", "sois", "somos", "son", "soy", "su", "sus", "suya", "suyas",
         "suyo", "suyos", "tambien", "tanto", "tendra", "tendran", "tendras", "tendre", "tendreis", "tendremos",
         "tendria", "tendriais", "tendriamos", "tendrian", "tendrias", "tened", "teneis", "tenemos", "tenga",
         "tengais", "tengamos", "tengan", "tengas", "tengo", "tenia", "teniais", "teniamos", "tenian", "tenias",
         "tenida", "tenidas", "tenido", "tenidos", "teniendo", "ti", "tiene", "tienen", "tienes", "todo", "todos",
         "tu", "tus", "tuve", "tuviera", "tuvierais", "tuvieramos", "tuvieran", "tuvieras", "tuvieron", "tuviese",
         "tuvieseis", "tuviesemos", "tuviesen", "tuvieses", "tuvimos", "tuviste", "tuvisteis", "tuvo", "tuya", "tuyas",
         "tuyo", "tuyos", "un", "una", "uno", "unos", "vosotras", "vosotros", "vuestra", "vuestras", "vuestro",
         "vuestros", "y", "ya", "yo" };
   public static final HashSet SP_STOP_WORDS = new HashSet(Arrays.asList(SPANISH_STOP_WORDS));

   /**
    * Contains the stopwords used with the StopFilter.
    */
   private Set stopTable = new HashSet();

   /**
    * Contains words that should be indexed but not stemmed.
    */
   private Set exclTable = new HashSet();

   private SinonimoMap sinonimos;

   /**
    * Builds an analyzer with the default stop words.
    */
   public SpanishAnalyzer(SinonimoMap sinonimoMap) {
      stopTable = StopFilter.makeStopSet(SPANISH_STOP_WORDS);
      sinonimos = sinonimoMap;
   }

   /** Builds an analyzer with the given stop words. */
   public SpanishAnalyzer(String[] stopWords) {
      stopTable = StopFilter.makeStopSet(stopWords);
   }

   /**
    * Builds an analyzer with the given stop words from file.
    * 
    * @throws IOException
    */
   public SpanishAnalyzer(File stopWords) throws IOException {
      stopTable = new HashSet(WordlistLoader.getWordSet(stopWords));
   }

   /**
    * Constructs a {@link StandardTokenizer}filtered by a {@link
    * StandardFilter}, a {@link LowerCaseFilter}, a {@link StopFilter}and a
    * {@link SpanishStemFilter}.
    */
   public final TokenStream tokenStream(String fieldName, Reader reader) {
      TokenStream result = new StandardTokenizer(reader);
      result = new StandardFilter(result);
      result = new AcentosFilter(result);
      result = new LowerCaseFilter(result);
      result = new StopFilter(result, stopTable);
      result = new SpanishPluralFilter(result);
      result = new SinonimoTokenFilter(result, sinonimos);
      result = new SpanishStemFilter(result); //raiz de la palabra
      return result;
   }
}
