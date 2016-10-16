// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FeedbackSeccionDTO.java

package cl.bbr.promo.carga.dto;


public class FeedbackSeccionDTO
{

    public FeedbackSeccionDTO()
    {
        codigo = 0;
        promocion = 0;
        codret = 0;
        filler = null;
    }

    public FeedbackSeccionDTO(int codigo, int promocion, int codret)
    {
        this.codigo = codigo;
        this.promocion = promocion;
        this.codret = codret;
        filler = null;
    }

    public FeedbackSeccionDTO(int codigo, int promocion, int codret, String filler)
    {
        this.codigo = codigo;
        this.promocion = promocion;
        this.codret = codret;
        this.filler = filler;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
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

    public int getPromocion()
    {
        return promocion;
    }

    public void setPromocion(int promocion)
    {
        this.promocion = promocion;
    }

    private int codigo;
    private int promocion;
    private int codret;
    private String filler;
}