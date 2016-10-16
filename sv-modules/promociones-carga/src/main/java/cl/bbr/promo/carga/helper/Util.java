// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Util.java

package cl.bbr.promo.carga.helper;


public class Util
{

    public Util()
    {
    }

    public static int getEan13(String barcode)
    {
        int dv = -1;
        if(barcode.length() > 12)
            barcode = barcode.substring(barcode.length() - 12);
        else
        if(barcode.length() < 12)
        {
            barcode = "000000000000" + barcode;
            barcode = barcode.substring(barcode.length() - 12);
        }
        if(barcode.length() != 12)
            return -1;
        int arr[] = new int[12];
        int suma = 0;
        int j = 1;
        for(int i = 0; i < 12; i++)
        {
            arr[i] = Integer.parseInt(barcode.substring(i, i + 1)) * j;
            if(j == 1)
                j = 3;
            else
                j = 1;
            suma += arr[i];
        }

        int mult;
        for(mult = 10; suma > mult; mult += 10);
        dv = mult - suma;
        return dv;
    }
}