// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProrrateoPromocionProductoDTO.java

package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.List;

public class ProrrateoPromocionProductoDTO
    implements Serializable
{

    public ProrrateoPromocionProductoDTO()
    {
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public long getDescuento()
    {
        return descuento;
    }

    public void setDescuento(long descuento)
    {
        this.descuento = descuento;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public List getListadoProductos()
    {
        return listadoProductos;
    }

    public void setListadoProductos(List listadoProrrateo)
    {
        listadoProductos = listadoProrrateo;
    }

    private String tipo;
    private int codigo;
    private long descuento;
    private String descripcion;
    private List listadoProductos;
}