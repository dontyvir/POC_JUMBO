// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProrrateoPromocionSeccionDTO.java

package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProrrateoPromocionSeccionDTO
    implements Serializable
{

    public ProrrateoPromocionSeccionDTO()
    {
        tipo = null;
        codigo = 0;
        descuento = 0L;
        descripcion = null;
        listadoSeccion = new ArrayList();
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

    public List getListadoSeccion()
    {
        return listadoSeccion;
    }

    public void setListadoSeccion(List listadoSeccion)
    {
        this.listadoSeccion = listadoSeccion;
    }

    public String getTipo()
    {
        return tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String toString()
    {
        return "codigo=" + codigo + ", descuento=" + descuento + ", descripcion=" + descripcion + ", secciones=" + listadoSeccion.size();
    }

    private String tipo;
    private int codigo;
    private long descuento;
    private String descripcion;
    private List listadoSeccion;
}