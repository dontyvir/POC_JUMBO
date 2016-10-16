// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProrrateoSeccionDTO.java

package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProrrateoSeccionDTO
    implements Serializable
{

    public ProrrateoSeccionDTO()
    {
        depto = 0;
        precio = 0L;
        prorrateo = 0L;
        listadoProductos = new ArrayList();
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

    public long getProrrateo()
    {
        return prorrateo;
    }

    public void setProrrateo(long prorrateo)
    {
        this.prorrateo = prorrateo;
    }

    public List getListadoProductos()
    {
        return listadoProductos;
    }

    public void setListadoProductos(List listadoProductos)
    {
        this.listadoProductos = listadoProductos;
    }

    public String toString()
    {
        return "depto=" + depto + ", monto=" + precio + ", prorrateo=" + prorrateo + ", productos=" + listadoProductos.size();
    }

    private int depto;
    private long precio;
    private long prorrateo;
    private List listadoProductos;
}