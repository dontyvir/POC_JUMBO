/*
 * Created on 04-dic-2008
 */
package cl.bbr.fo.util.busqueda;

import java.util.Comparator;

/**
 * @author jdroguett
 */
public class OrdenAutoCompletar implements Comparator {
    private String q;
    
    public OrdenAutoCompletar(String q){
        this.q = q;
    }

    public int compare(Object o1, Object o2) {
        String s1 = (String) o1;
        String s2 = (String) o2;
        int i1 = s1.toLowerCase().indexOf(q);
        int i2 = s2.toLowerCase().indexOf(q);

        if (i1 < 0 && i2 < 0)
            return s1.compareTo(s2);
        else if (i1 < 0)
            return 1;
        else if (i2 < 0)
            return -1;
        else if (i1 < i2)
            return -1;
        else if (i1 == i2) {
            return s1.length() - s2.length();
        }
        return 1;
    }
}
