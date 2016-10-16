/*
 * Created on 16-feb-2009
 *
 */
package cl.bbr.fo.util.busqueda;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * @author jdroguett
 * Viene de ISOLatin1AccentFilter, Lo saque para que no me elimine la �
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A filter that replaces accented characters in the ISO Latin 1 character set
 * (ISO-8859-1) by their unaccented equivalent. The case will not be altered.
 * <p>
 * For instance, '&agrave;' will be replaced by 'a'.
 * <p>
 */
public class AcentosFilter extends TokenFilter {
    public AcentosFilter(TokenStream input) {
        super(input);
    }

    private char[] output = new char[256];

    private int outputPos;

    public final Token next(final Token reusableToken) throws java.io.IOException {
        //assert reusableToken != null;
        Token nextToken = input.next(reusableToken);
        if (nextToken != null) {
            final char[] buffer = nextToken.termBuffer();
            final int length = nextToken.termLength();
            // If no characters actually require rewriting then we
            // just return token as-is:
            for (int i = 0; i < length; i++) {
                final char c = buffer[i];
                if (c >= '\u00c0' && c <= '\uFB06') {
                    removeAccents(buffer, length);
                    nextToken.setTermBuffer(output, 0, outputPos);
                    break;
                }
            }
            return nextToken;
        }
        return null;
    }

    /**
     * To replace accented characters in a String by unaccented equivalents.
     */
    public final void removeAccents(char[] input, int length) {

        // Worst-case length required:
        final int maxSizeNeeded = 2 * length;

        int size = output.length;
        while (size < maxSizeNeeded)
            size *= 2;

        if (size != output.length)
            output = new char[size];

        outputPos = 0;

        int pos = 0;

        for (int i = 0; i < length; i++, pos++) {
            final char c = input[pos];

            // Quick test: if it's not in range then just keep
            // current character
            if (c < '\u00c0' || c > '\uFB06')
                output[outputPos++] = c;
            else {
                switch (c) {
                case '\u00C0': // �
                case '\u00C1': // �
                case '\u00C2': // �
                case '\u00C3': // �
                case '\u00C4': // �
                case '\u00C5': // �
                    output[outputPos++] = 'A';
                    break;
                case '\u00C6': // �
                    output[outputPos++] = 'A';
                    output[outputPos++] = 'E';
                    break;
                case '\u00C7': // �
                    output[outputPos++] = 'C';
                    break;
                case '\u00C8': // �
                case '\u00C9': // �
                case '\u00CA': // �
                case '\u00CB': // �
                    output[outputPos++] = 'E';
                    break;
                case '\u00CC': // �
                case '\u00CD': // �
                case '\u00CE': // �
                case '\u00CF': // �
                    output[outputPos++] = 'I';
                    break;
                case '\u0132': // ?
                    output[outputPos++] = 'I';
                    output[outputPos++] = 'J';
                    break;
                case '\u00D0': // �
                    output[outputPos++] = 'D';
                    break;
                //case '\u00D1': // �
                //    output[outputPos++] = 'N';
                //    break;
                case '\u00D2': // �
                case '\u00D3': // �
                case '\u00D4': // �
                case '\u00D5': // �
                case '\u00D6': // �
                case '\u00D8': // �
                    output[outputPos++] = 'O';
                    break;
                case '\u0152': // �
                    output[outputPos++] = 'O';
                    output[outputPos++] = 'E';
                    break;
                case '\u00DE': // �
                    output[outputPos++] = 'T';
                    output[outputPos++] = 'H';
                    break;
                case '\u00D9': // �
                case '\u00DA': // �
                case '\u00DB': // �
                case '\u00DC': // �
                    output[outputPos++] = 'U';
                    break;
                case '\u00DD': // �
                case '\u0178': // �
                    output[outputPos++] = 'Y';
                    break;
                case '\u00E0': // �
                case '\u00E1': // �
                case '\u00E2': // �
                case '\u00E3': // �
                case '\u00E4': // �
                case '\u00E5': // �
                    output[outputPos++] = 'a';
                    break;
                case '\u00E6': // �
                    output[outputPos++] = 'a';
                    output[outputPos++] = 'e';
                    break;
                case '\u00E7': // �
                    output[outputPos++] = 'c';
                    break;
                case '\u00E8': // �
                case '\u00E9': // �
                case '\u00EA': // �
                case '\u00EB': // �
                    output[outputPos++] = 'e';
                    break;
                case '\u00EC': // �
                case '\u00ED': // �
                case '\u00EE': // �
                case '\u00EF': // �
                    output[outputPos++] = 'i';
                    break;
                case '\u0133': // ?
                    output[outputPos++] = 'i';
                    output[outputPos++] = 'j';
                    break;
                case '\u00F0': // �
                    output[outputPos++] = 'd';
                    break;
                //case '\u00F1': // �
                //    output[outputPos++] = 'n';
                //    break;
                case '\u00F2': // �
                case '\u00F3': // �
                case '\u00F4': // �
                case '\u00F5': // �
                case '\u00F6': // �
                case '\u00F8': // �
                    output[outputPos++] = 'o';
                    break;
                case '\u0153': // �
                    output[outputPos++] = 'o';
                    output[outputPos++] = 'e';
                    break;
                case '\u00DF': // �
                    output[outputPos++] = 's';
                    output[outputPos++] = 's';
                    break;
                case '\u00FE': // �
                    output[outputPos++] = 't';
                    output[outputPos++] = 'h';
                    break;
                case '\u00F9': // �
                case '\u00FA': // �
                case '\u00FB': // �
                case '\u00FC': // �
                    output[outputPos++] = 'u';
                    break;
                case '\u00FD': // �
                case '\u00FF': // �
                    output[outputPos++] = 'y';
                    break;
                case '\uFB00': // ?
                    output[outputPos++] = 'f';
                    output[outputPos++] = 'f';
                    break;
                case '\uFB01': // ?
                    output[outputPos++] = 'f';
                    output[outputPos++] = 'i';
                    break;
                case '\uFB02': // ?
                    output[outputPos++] = 'f';
                    output[outputPos++] = 'l';
                    break;
                // following 2 are commented as they can break the maxSizeNeeded
                // (and doing *3 could be expensive)
                //            case '\uFB03': // ?
                //                output[outputPos++] = 'f';
                //                output[outputPos++] = 'f';
                //                output[outputPos++] = 'i';
                //                break;
                //            case '\uFB04': // ?
                //                output[outputPos++] = 'f';
                //                output[outputPos++] = 'f';
                //                output[outputPos++] = 'l';
                //                break;
                case '\uFB05': // ?
                    output[outputPos++] = 'f';
                    output[outputPos++] = 't';
                    break;
                case '\uFB06': // ?
                    output[outputPos++] = 's';
                    output[outputPos++] = 't';
                    break;
                default:
                    output[outputPos++] = c;
                    break;
                }
            }
        }
    }
}
