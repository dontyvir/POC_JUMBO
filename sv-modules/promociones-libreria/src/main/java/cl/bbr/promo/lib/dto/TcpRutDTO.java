// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TcpRutDTO.java

package cl.bbr.promo.lib.dto;


public class TcpRutDTO
{

    public TcpRutDTO()
    {
        tcp = 0;
        cant = 0;
    }

    public TcpRutDTO(int tcp, int cant)
    {
        this.tcp = tcp;
        this.cant = cant;
    }

    public int getCant()
    {
        return cant;
    }

    public void setCant(int cant)
    {
        this.cant = cant;
    }

    public int getTcp()
    {
        return tcp;
    }

    public void setTcp(int tcp)
    {
        this.tcp = tcp;
    }

    private int tcp;
    private int cant;
}