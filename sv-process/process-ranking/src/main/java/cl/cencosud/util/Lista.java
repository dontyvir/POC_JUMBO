/*
 * Created on 27-may-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.cencosud.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jdroguett
 * 
 * metodos utiles para listas
 */
public class Lista {
    /**
     * Divide una lista en sublistas de tamaño "rango"
     * 
     * @param datos
     * @param rango
     * @return
     */
    public static List subListas(List datos, int rango) {
        List lista = new ArrayList();
        int i = 0;
        while (i < datos.size()) {
            List subLista = datos.subList(i, (i + rango > datos.size() ? datos.size() : i + rango));
            lista.add(subLista);
            i += rango;
        }
        return lista;
    }

    /**
     * Entrega una lista con los elemenos de primero menos los elementos de
     * segundo
     * 
     * @param primero
     * @param segundo
     * @return
     */
    public static List menos(List primero, List segundo) {
        List lista = new ArrayList();
        for (Iterator iter = primero.iterator(); iter.hasNext();) {
            Object elemento = (Object) iter.next();
            if (segundo.indexOf(elemento) == -1)
                lista.add(elemento);
        }
        return lista;
    }

    /**
     * Entrega un string con los rut distinto de cero.
     * 
     * Formato: "(123,43234,454,56456)"
     * 
     * @param boletas
     * @return
     */
    public static String join(List list) {
        StringBuffer lista = new StringBuffer("(");
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Integer entero = (Integer) iter.next();
            lista.append(entero);
            if (iter.hasNext())
                lista.append(",");
        }
        lista.append(")");
        return lista.toString();
    }

}
