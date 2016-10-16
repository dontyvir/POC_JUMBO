// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro2Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Reg2ProdxPromoDTO;
import cl.bbr.promo.carga.dto.Registro2DTO;
import java.util.ArrayList;
import java.util.List;

public class Registro2Wrap
{

    public Registro2Wrap()
    {
    }

    public static Registro2DTO wrap(String reg)
    {
        Registro2DTO dto = new Registro2DTO();
        List productos = new ArrayList();
        try
        {
            if(reg.length() != 248)
                return null;
            for(int i = 1; i < 190; i += 19)
            {
                Reg2ProdxPromoDTO prod = new Reg2ProdxPromoDTO();
                long ean = Long.parseLong(reg.substring(i, i + 12));
                int prio = Integer.parseInt(reg.substring(i + 12, i + 13));
                int promo = Integer.parseInt(reg.substring(i + 13, i + 19));
                if(ean > 0L)
                {
                    prod = new Reg2ProdxPromoDTO(ean, prio, promo);
                    productos.add(prod);
                }
            }

            dto.setTipoReg(Integer.parseInt(reg.substring(0, 1)));
            dto.setProductos(productos);
            return dto;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}