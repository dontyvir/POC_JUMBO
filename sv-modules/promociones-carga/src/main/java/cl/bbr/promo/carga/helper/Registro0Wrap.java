// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro0Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Registro0DTO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Registro0Wrap
{

    public Registro0Wrap()
    {
    }

    public static Registro0DTO wrap(String reg)
    {
        List locales = new ArrayList();
        Registro0DTO dto = new Registro0DTO();
        try
        {
            if(reg.length() != 248)
                return null;
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            java.util.Date fecha = df.parse(reg.substring(1, 15));
            for(int i = 16; i < 236; i += 4)
            {
                int local = Integer.parseInt(reg.substring(i, i + 4));
                if(local > 0)
                    locales.add(String.valueOf(local));
            }

            dto.setTipoReg(Integer.parseInt(reg.substring(0, 1)));
            dto.setFecha(fecha);
            dto.setTipoPromo(Integer.parseInt(reg.substring(15, 16)));
            dto.setLocales(locales);
            return dto;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}