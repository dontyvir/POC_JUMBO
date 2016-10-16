// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TcpCuponDTO.java

package cl.bbr.promo.lib.dto;


public class TcpCuponDTO
{

    public TcpCuponDTO()
    {
        cupon = null;
        generado = false;
        canjeado = false;
        cant = 0;
    }

    public TcpCuponDTO(String cupon, boolean generado, boolean canjeado, int cant)
    {
        this.cupon = cupon;
        this.generado = generado;
        this.canjeado = canjeado;
        this.cant = cant;
    }

    public boolean isCanjeado()
    {
        return canjeado;
    }

    public void setCanjeado(boolean canjeado)
    {
        this.canjeado = canjeado;
    }

    public int getCant()
    {
        return cant;
    }

    public void setCant(int cant)
    {
        this.cant = cant;
    }

    public String getCupon()
    {
        return cupon;
    }

    public void setCupon(String cupon)
    {
        this.cupon = cupon;
    }

    public boolean isGenerado()
    {
        return generado;
    }

    public void setGenerado(boolean generado)
    {
        this.generado = generado;
    }

    private String cupon;
    private boolean generado;
    private boolean canjeado;
    private int cant;
}