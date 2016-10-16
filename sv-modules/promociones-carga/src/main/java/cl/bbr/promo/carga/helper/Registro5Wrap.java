// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro5Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Reg5PromoSeccionActivasDTO;
import cl.bbr.promo.carga.dto.Registro5DTO;
import java.util.ArrayList;
import java.util.List;

public class Registro5Wrap
{

    public Registro5Wrap()
    {
    }

    public static Registro5DTO wrap(String reg)
    {
        Registro5DTO dto = new Registro5DTO();
        List promociones = new ArrayList();
        try
        {
            if(reg.length() != 248)
                return null;
            for(int i = 1; i < 135; i += 9)
            {
                Reg5PromoSeccionActivasDTO promo = new Reg5PromoSeccionActivasDTO();
                int tipo = Integer.parseInt(reg.substring(i, i + 3));
                int codigo = Integer.parseInt(reg.substring(i + 3, i + 9));
                promo.setTipo(tipo);
                promo.setCodigo(codigo);
                promociones.add(promo);
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