// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro7DTO.java

package cl.bbr.promo.carga.dto;


public class Registro7DTO
{

    public Registro7DTO()
    {
        tipoReg = 0;
        lineas = 0;
    }

    public Registro7DTO(int tipoReg, int lineas)
    {
        this.tipoReg = tipoReg;
        this.lineas = lineas;
    }

    public int getLineas()
    {
        return lineas;
    }

    public void setLineas(int lineas)
    {
        this.lineas = lineas;
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
    private int lineas;
}