// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Reg5PromoSeccionActivasDTO.java

package cl.bbr.promo.carga.dto;


public class Reg5PromoSeccionActivasDTO
{

    public Reg5PromoSeccionActivasDTO()
    {
        tipo = 0;
        codigo = 0;
    }

    public Reg5PromoSeccionActivasDTO(int tipo, int codigo)
    {
        this.tipo = tipo;
        this.codigo = codigo;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public int getTipo()
    {
        return tipo;
    }

    public void setTipo(int tipo)
    {
        this.tipo = tipo;
    }

    private int tipo;
    private int codigo;
}