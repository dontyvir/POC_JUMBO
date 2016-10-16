// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FeedbackProductoDTO.java

package cl.bbr.promo.carga.dto;


public class FeedbackProductoDTO
{

    public FeedbackProductoDTO()
    {
        promocion = 0;
        producto = 0L;
        codret = 0;
        filler = null;
    }

    public FeedbackProductoDTO(int promocion, long producto, int codret)
    {
        this.promocion = promocion;
        this.producto = producto;
        this.codret = codret;
        filler = null;
    }

    public FeedbackProductoDTO(int promocion, int producto, int codret, String filler)
    {
        this.promocion = promocion;
        this.producto = producto;
        this.codret = codret;
        this.filler = filler;
    }

    public int getCodret()
    {
        return codret;
    }

    public void setCodret(int codret)
    {
        this.codret = codret;
    }

    public String getFiller()
    {
        return filler;
    }

    public void setFiller(String filler)
    {
        this.filler = filler;
    }

    public long getProducto()
    {
        return producto;
    }

    public void setProducto(long producto)
    {
        this.producto = producto;
    }

    public int getPromocion()
    {
        return promocion;
    }

    public void setPromocion(int promocion)
    {
        this.promocion = promocion;
    }

    private int promocion;
    private long producto;
    private int codret;
    private String filler;
}