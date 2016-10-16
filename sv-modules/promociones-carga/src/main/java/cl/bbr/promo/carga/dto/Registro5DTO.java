// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro5DTO.java

package cl.bbr.promo.carga.dto;

import java.util.List;

public class Registro5DTO
{

    public Registro5DTO()
    {
        tipoReg = 0;
        promociones = null;
    }

    public Registro5DTO(int tipoReg, List promociones)
    {
        this.tipoReg = tipoReg;
        this.promociones = promociones;
    }

    public List getPromociones()
    {
        return promociones;
    }

    public void setPromociones(List promociones)
    {
        this.promociones = promociones;
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
    private List promociones;
}