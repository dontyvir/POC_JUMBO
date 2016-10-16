// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Util.java

package cl.bbr.promo.lib.helpers;

import java.util.*;

public class Util
{

    public Util()
    {
    }

    public static int getDayOfWeek()
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return calendar.get(7);
    }

    public static String getDate(Date date)
    {
        StringBuffer fecha = new StringBuffer();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try
        {
            fecha.append(calendar.get(1));
            int aux = calendar.get(2) + 1;
            if(aux < 10)
                fecha.append("0");
            fecha.append(aux);
            aux = calendar.get(5);
            if(aux < 10)
                fecha.append("0");
            fecha.append(aux);
            aux = calendar.get(11);
            if(aux < 10)
                fecha.append("0");
            fecha.append(aux);
            aux = calendar.get(12);
            if(aux < 10)
                fecha.append("0");
            fecha.append(aux);
            aux = calendar.get(13);
            if(aux < 10)
                fecha.append("0");
            fecha.append(aux);
            return fecha.toString();
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public static String getDate()
    {
        return getDate(new Date());
    }

    public static long calculaProrrateo(long producto, long suma, long dcto)
    {
        float res = 0.0F;        
        res = Math.round(((float)producto / (float)suma) * (float)dcto);
        return (long)res;
    }
}