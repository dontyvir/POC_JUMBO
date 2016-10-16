// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:51
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Test.java

package cl.bbr.test;

import cl.bbr.promo.lib.helpers.Util;
//import java.io.PrintStream;
import java.util.*;

public class Test
{

    public Test()
    {
    }

    public static void main(String args[])
    {
        Date fecha = new Date();
        long actual = Long.parseLong(Util.getDate(fecha).substring(0, 14));
        System.out.println("actual=" + actual);
        List lista = new ArrayList();
        lista.add("a");
        lista.add("b");
        lista.add("c");
        lista.add("d");
        double beneficio = 0.0D;
        if(beneficio == 0.0D)
            System.out.println("SI");
        else
            System.out.println("NO");
        for(int w = 0; w < lista.size(); w++)
        {
            if(lista.get(w).equals("c"))
                lista.remove(w);
            System.out.print(lista.get(w) + " ");
        }

        System.exit(0);
    }
}