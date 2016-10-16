// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro6DTO.java

package cl.bbr.promo.carga.dto;

import java.util.List;

public class Registro6DTO
{

    public Registro6DTO()
    {
        tipoReg = 0;
        seccion = null;
    }

    public Registro6DTO(int tipoReg, List seccion)
    {
        this.tipoReg = tipoReg;
        this.seccion = seccion;
    }

    public List getSeccion()
    {
        return seccion;
    }

    public void setSeccion(List seccion)
    {
        this.seccion = seccion;
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
    private List seccion;
}