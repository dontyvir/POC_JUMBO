// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro0DTO.java

package cl.bbr.promo.carga.dto;

import java.util.Date;
import java.util.List;

public class Registro0DTO
{

    public Registro0DTO()
    {
        tipoReg = 0;
        fecha = null;
        tipoPromo = 0;
        locales = null;
    }

    public Registro0DTO(int tipoReg, Date fecha, int tipoPromo, List locales)
    {
        this.tipoReg = tipoReg;
        this.fecha = fecha;
        this.tipoPromo = tipoPromo;
        this.locales = locales;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public List getLocales()
    {
        return locales;
    }

    public void setLocales(List locales)
    {
        this.locales = locales;
    }

    public int getTipoPromo()
    {
        return tipoPromo;
    }

    public void setTipoPromo(int tipoPromo)
    {
        this.tipoPromo = tipoPromo;
    }

    public int getTipoReg()
    {
        return tipoReg;
    }

    public void setTipoReg(int tipoReg)
    {
        this.tipoReg = tipoReg;
    }

    private int tipoReg;
    private Date fecha;
    private int tipoPromo;
    private List locales;
}