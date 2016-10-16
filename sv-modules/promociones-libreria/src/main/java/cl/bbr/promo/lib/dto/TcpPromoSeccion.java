// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TcpPromoSeccion.java

package cl.bbr.promo.lib.dto;


public class TcpPromoSeccion
{

    public TcpPromoSeccion()
    {
        promo = 0;
        tcp = 0;
        cant = 0;
    }

    public TcpPromoSeccion(int promo, int tcp, int cant)
    {
        this.promo = promo;
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

    public int getPromo()
    {
        return promo;
    }

    public void setPromo(int promo)
    {
        this.promo = promo;
    }

    public int getTcp()
    {
        return tcp;
    }

    public void setTcp(int tcp)
    {
        this.tcp = tcp;
    }

    private int promo;
    private int tcp;
    private int cant;
}