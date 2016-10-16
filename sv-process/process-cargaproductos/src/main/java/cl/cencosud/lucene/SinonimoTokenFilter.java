/*
 * Created on 23-feb-2009
 *
 */
package cl.cencosud.lucene;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * @author jdroguett
 *  
 */
public class SinonimoTokenFilter extends TokenFilter {
   public static final String SYNONYM_TOKEN_TYPE = "SYNONYM";
   private SinonimoMap synonyms;

   /**
    * @param input
    */
   protected SinonimoTokenFilter(TokenStream input, SinonimoMap synonyms) {
      super(input);
      this.synonyms = synonyms;
   }

   public Token next(final Token reusableToken) throws IOException {
      Token nextToken = input.next(reusableToken);
      if (nextToken == null)
         return null;

      String[] s = synonyms.getSinonimos(nextToken.term());
      if (s != null && s.length > 0)
         //la primera palabra ser� siempre la que participa en el indice
         nextToken = createToken(s[0], nextToken);

      return nextToken;
   }

   protected Token createToken(String synonym, final Token reusableToken) {
      reusableToken.reinit(synonym, 0, synonym.length());
      reusableToken.setTermBuffer(synonym);
      reusableToken.setType(SYNONYM_TOKEN_TYPE);
      reusableToken.setPositionIncrement(0);
      return reusableToken;
   }

}
