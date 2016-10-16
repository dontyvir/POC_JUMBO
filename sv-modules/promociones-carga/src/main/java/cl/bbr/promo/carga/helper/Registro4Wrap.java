// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro4Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Reg4ProdxEliminarDTO;
import cl.bbr.promo.carga.dto.Registro4DTO;
import java.util.ArrayList;
import java.util.List;

public class Registro4Wrap
{

    public Registro4Wrap()
    {
    }

    public static Registro4DTO wrap(String reg)
    {
        Registro4DTO dto = new Registro4DTO();
        List productos = new ArrayList();
        try
        {
            if(reg.length() != 248)
                return null;
            for(int i = 1; i < 195; i += 13)
            {
                Reg4ProdxEliminarDTO prod = new Reg4ProdxEliminarDTO();
                long ean = Long.parseLong(reg.substring(i, i + 12));
                int prio = Integer.parseInt(reg.substring(i + 12, i + 13));
                if(ean > 0L)
                {
                    prod.setProducto(ean);
                    prod.setPrioridad(prio);
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