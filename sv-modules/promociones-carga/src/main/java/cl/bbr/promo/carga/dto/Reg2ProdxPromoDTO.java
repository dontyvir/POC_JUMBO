// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Reg2ProdxPromoDTO.java

package cl.bbr.promo.carga.dto;


public class Reg2ProdxPromoDTO
{

    public Reg2ProdxPromoDTO()
    {
        producto = 0L;
        prioridad = 0;
        codigo = 0;
    }

    public Reg2ProdxPromoDTO(long producto, int prioridad, int codigo)
    {
        this.producto = producto;
        this.prioridad = prioridad;
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

    public int getPrioridad()
    {
        return prioridad;
    }

    public void setPrioridad(int prioridad)
    {
        this.prioridad = prioridad;
    }

    public long getProducto()
    {
        return producto;
    }

    public void setProducto(long producto)
    {
        this.producto = producto;
    }

    private long producto;
    private int prioridad;
    private int codigo;
}