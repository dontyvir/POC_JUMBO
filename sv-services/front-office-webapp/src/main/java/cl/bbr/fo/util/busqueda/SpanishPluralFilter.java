/*
 * Created on 11-feb-2009
 */
package cl.bbr.fo.util.busqueda;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * @author jdroguett
 */
public class SpanishPluralFilter extends TokenFilter {

    public SpanishPluralFilter(TokenStream in) {
        super(in);
    }

    public final Token next(Token reusableToken) throws IOException {
        Token nextToken = input.next(reusableToken);
        if (nextToken == null) {
            return null;
        }
        final char[] palabra = nextToken.termBuffer();
        final int n = nextToken.termLength();
        int len = n;

        if (n > 3) {
            if ((palabra[n - 1] == 's') && (palabra[n - 2] == 'e') && (palabra[n - 3] == 's')
                    && (palabra[n - 4] == 'e')) {
                /* corteses -> cortés */
                len = n - 2;
            } else if ((palabra[n - 1] == 's') && (palabra[n - 2] == 'e') && (palabra[n - 3] == 'c')) {
                palabra[n - 3] = 'z'; /* dos veces -> una vez */
                len = n - 2;
            } else if ((palabra[n - 1] == 's') && (palabra[n - 2] == 'e') && (palabra[n - 3] == 'n')) {
                len = n - 2; /* corazones -> corazon */
            } else if ((palabra[n - 1] == 's') && (palabra[n - 2] == 'e')) {
                len = n - 2; //terminados en es
            } else if (palabra[n - 1] == 's') { //terminados en s
                len = n - 1;
            }
        }

        nextToken.setTermBuffer(palabra, 0, len);
        return nextToken;
    }

}
