// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro4DTO.java

package cl.bbr.promo.carga.dto;

import java.util.List;

public class Registro4DTO
{

    public Registro4DTO()
    {
        tipoReg = 0;
        productos = null;
    }

    public Registro4DTO(int tipoReg, List productos)
    {
        this.tipoReg = tipoReg;
        this.productos = productos;
    }

    public List getProductos()
    {
        return productos;
    }

    public void setProductos(List productos)
    {
        this.productos = productos;
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
    private List productos;
}