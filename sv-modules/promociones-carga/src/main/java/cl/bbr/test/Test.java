// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:21:15
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Test.java

package cl.bbr.test;

import cl.bbr.promo.carga.helper.Util;
//import java.io.PrintStream;

public class Test
{

    public Test()
    {
    }

    public static void main(String args[])
    {
        String numero = null;
        numero = "000002480605";
        System.out.println("--------------");
        System.out.println("numero=" + numero + ", dv=" + Util.getEan13(numero));
        numero = "2480605";
        System.out.println("--------------");
        System.out.println("numero=" + numero + ", dv=" + Util.getEan13(numero));
    }
}