package cl.cencosud.filtro;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * 
 * @author jdroguett
 *
 * Filtro con expresion regular
 */
public class FiltroER implements FilenameFilter {
    private String expresion;

    public FiltroER(String expresion) {
       this.expresion =  expresion;
    }

    public boolean accept(File dir, String name) {
        return Pattern.matches(expresion, name);
    }
}