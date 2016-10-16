// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProrrateoProductoSeccionDTO.java

package cl.bbr.promo.lib.dto;


public class ProrrateoProductoSeccionDTO
{

    public ProrrateoProductoSeccionDTO()
    {
        codigo = 0L;
        depto = 0;
        precio = 0L;
        prorrateoProducto = 0L;
        prorrateoSeccion = 0L;
    }

    public long getCodigo()
    {
        return codigo;
    }

    public void setCodigo(long codigo)
    {
        this.codigo = codigo;
    }

    public int getDepto()
    {
        return depto;
    }

    public void setDepto(int depto)
    {
        this.depto = depto;
    }

    public long getPrecio()
    {
        return precio;
    }

    public void setPrecio(long precio)
    {
        this.precio = precio;
    }

    public long getProrrateoProducto()
    {
        return prorrateoProducto;
    }

    public void setProrrateoProducto(long prorrateoProducto)
    {
        this.prorrateoProducto = prorrateoProducto;
    }

    public long getProrrateoSeccion()
    {
        return prorrateoSeccion;
    }

    public void setProrrateoSeccion(long prorrateoSeccion)
    {
        this.prorrateoSeccion = prorrateoSeccion;
    }

    public String toString()
    {
        return "codigo=" + codigo + ", depto=" + depto + ", precio=" + precio + ", p_prod=" + prorrateoProducto + ", p_sec=" + prorrateoSeccion;
    }

    private long codigo;
    private int depto;
    private long precio;
    private long prorrateoProducto;
    private long prorrateoSeccion;
}