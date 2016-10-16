// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TcpClienteDTO.java

package cl.bbr.promo.lib.dto;


public class TcpClienteDTO
{

    public TcpClienteDTO()
    {
        tcp = 0;
        cupon = null;
        max = 0;
        cant = 0;
    }

    public TcpClienteDTO(int tcp, String cupon, int max, int cant)
    {
        this.tcp = tcp;
        this.cupon = cupon;
        this.max = max;
        this.cant = cant;
    }

    public int getMax()
    {
        return max;
    }

    public void setMax(int max)
    {
        this.max = max;
    }

    public int getTcp()
    {
        return tcp;
    }

    public void setTcp(int tcp)
    {
        this.tcp = tcp;
    }

    public String getCupon()
    {
        return cupon;
    }

    public void setCupon(String cupon)
    {
        this.cupon = cupon;
    }

    public int getCant()
    {
        return cant;
    }

    public void setCant(int cant)
    {
        this.cant = cant;
    }

    private int tcp;
    private String cupon;
    private int max;
    private int cant;
}