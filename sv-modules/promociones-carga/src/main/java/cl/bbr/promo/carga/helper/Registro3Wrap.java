// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro3Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Registro3DTO;
import java.util.ArrayList;
import java.util.List;

public class Registro3Wrap
{

    public Registro3Wrap()
    {
    }

    public static Registro3DTO wrap(String reg)
    {
        Registro3DTO dto = new Registro3DTO();
        List promociones = new ArrayList();
        try
        {
            if(reg.length() != 248)
                return null;
            for(int i = 1; i < 240; i += 6)
            {
                int promo = Integer.parseInt(reg.substring(i, i + 6));
                if(promo > 0)
                    promociones.add(String.valueOf(promo));
            }

            dto.setTipoReg(Integer.parseInt(reg.substring(0, 1)));
            dto.setPromociones(promociones);
            return dto;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}