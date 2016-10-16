/*
 * Created on 26-ago-2008
 *
 */
package cl.cencosud.lucene;

/**
 * @author jdroguett
 *
 */
import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.tartarus.snowball.ext.SpanishStemmer;

/**
 * Spanish stemming algorithm.
 */
public final class SpanishStemFilter extends TokenFilter {

   private SpanishStemmer stemmer;

   public SpanishStemFilter(TokenStream in) {
      super(in);
      stemmer = new SpanishStemmer();
   }

   public final Token next(Token reusableToken) throws IOException {
      Token nextToken = input.next(reusableToken);
      if (nextToken == null)
         return null;
      String originalTerm = nextToken.term();
      /* excepcion a la regla: bebida y beb� tienen la misma raiz */
      if (originalTerm.startsWith("bebida")) {
         originalTerm += "ida";
      }

      stemmer.setCurrent(originalTerm);
      stemmer.stem();
      String finalTerm = stemmer.getCurrent();
      // Don't bother updating, if it is unchanged.
      if (!originalTerm.equals(finalTerm))
         nextToken.setTermBuffer(finalTerm);
      return nextToken;
   }

   /**
    * Set a alternative/custom Stemmer for this filter.
    */
   public void setStemmer(SpanishStemmer stemmer) {
      if (stemmer != null) {
         this.stemmer = stemmer;
      }
   }
}
