// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro7Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Registro7DTO;

public class Registro7Wrap
{

    public Registro7Wrap()
    {
    }

    public static Registro7DTO wrap(String reg)
    {
        Registro7DTO dto = new Registro7DTO();
        try
        {
            if(reg.length() != 248)
            {
                return null;
            } else
            {
                dto.setTipoReg(Integer.parseInt(reg.substring(0, 1)));
                dto.setLineas(Integer.parseInt(reg.substring(1, 7)));
                return dto;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
}