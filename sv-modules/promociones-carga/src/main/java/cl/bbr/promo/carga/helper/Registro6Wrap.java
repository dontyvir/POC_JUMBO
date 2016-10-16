// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro6Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Reg6SeccionxPromo;
import cl.bbr.promo.carga.dto.Registro6DTO;
import java.util.ArrayList;
import java.util.List;

public class Registro6Wrap
{

    public Registro6Wrap()
    {
    }

    public static Registro6DTO wrap(String reg)
    {
        Registro6DTO dto = new Registro6DTO();
        List secciones = new ArrayList();
        try
        {
            if(reg.length() != 248)
                return null;
            for(int i = 1; i < 180; i += 18)
            {
                int codigo = Integer.parseInt(reg.substring(i, i + 3));
                if(codigo > 0)
                {
                    Reg6SeccionxPromo sec = new Reg6SeccionxPromo();
                    sec.setSeccion(codigo);
                    if(reg.substring(i + 3, i + 4).equals("1"))
                        sec.setLunes(true);
                    if(reg.substring(i + 4, i + 5).equals("1"))
                        sec.setMartes(true);
                    if(reg.substring(i + 5, i + 6).equals("1"))
                        sec.setMiercoles(true);
                    if(reg.substring(i + 6, i + 7).equals("1"))
                        sec.setJueves(true);
                    if(reg.substring(i + 7, i + 8).equals("1"))
                        sec.setViernes(true);
                    if(reg.substring(i + 8, i + 9).equals("1"))
                        sec.setSabado(true);
                    if(reg.substring(i + 9, i + 10).equals("1"))
                        sec.setDomingo(true);
                    if(reg.substring(i + 10, i + 11).equals("1"))
                        sec.setEsp1(true);
                    if(reg.substring(i + 11, i + 12).equals("1"))
                        sec.setEsp2(true);
                    if(reg.substring(i + 12, i + 13).equals("1"))
                        sec.setEsp3(true);
                    if(reg.substring(i + 13, i + 14).equals("1"))
                        sec.setEsp4(true);
                    if(reg.substring(i + 14, i + 15).equals("1"))
                        sec.setEsp5(true);
                    if(reg.substring(i + 15, i + 16).equals("1"))
                        sec.setEsp6(true);
                    if(reg.substring(i + 16, i + 17).equals("1"))
                        sec.setEsp7(true);
                    if(reg.substring(i + 17, i + 18).equals("1"))
                        sec.setEsp8(true);
                    secciones.add(sec);
                }
            }

            dto.setTipoReg(Integer.parseInt(reg.substring(0, 1)));
            dto.setSeccion(secciones);
            return dto;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}