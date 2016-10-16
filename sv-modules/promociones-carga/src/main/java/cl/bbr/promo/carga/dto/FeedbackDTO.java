// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FeedbackDTO.java

package cl.bbr.promo.carga.dto;

import java.util.Date;
import java.util.List;

public class FeedbackDTO
{

    public FeedbackDTO()
    {
        tipo = 0;
        local = null;
        idtarea = 0;
        estado = 0;
        codret = 0;
        prodProc = 0;
        prodNoProc = 0;
        promoProc = 0;
        promoNoProc = 0;
        finicio = null;
        ftermino = null;
        bloque = 0;
        total = 0;
        regExcep = 0;
        excep = null;
    }

    public int getBloque()
    {
        return bloque;
    }

    public void setBloque(int bloque)
    {
        this.bloque = bloque;
    }

    public int getCodret()
    {
        return codret;
    }

    public void setCodret(int codret)
    {
        this.codret = codret;
    }

    public int getEstado()
    {
        return estado;
    }

    public void setEstado(int estado)
    {
        this.estado = estado;
    }

    public List getExcep()
    {
        return excep;
    }

    public void setExcep(List excep)
    {
        this.excep = excep;
    }

    public Date getFtermino()
    {
        return ftermino;
    }

    public void setFtermino(Date ftermino)
    {
        this.ftermino = ftermino;
    }

    public Date getFinicio()
    {
        return finicio;
    }

    public void setFinicio(Date finicio)
    {
        this.finicio = finicio;
    }

    public int getIdtarea()
    {
        return idtarea;
    }

    public void setIdtarea(int idtarea)
    {
        this.idtarea = idtarea;
    }

    public String getLocal()
    {
        return local;
    }

    public void setLocal(String local)
    {
        this.local = local;
    }

    public int getProdNoProc()
    {
        return prodNoProc;
    }

    public void setProdNoProc(int prodNoProc)
    {
        this.prodNoProc = prodNoProc;
    }

    public int getProdProc()
    {
        return prodProc;
    }

    public void setProdProc(int prodProc)
    {
        this.prodProc = prodProc;
    }

    public int getPromoNoProc()
    {
        return promoNoProc;
    }

    public void setPromoNoProc(int promoNoProc)
    {
        this.promoNoProc = promoNoProc;
    }

    public int getPromoProc()
    {
        return promoProc;
    }

    public void setPromoProc(int promoProc)
    {
        this.promoProc = promoProc;
    }

    public int getRegExcep()
    {
        return regExcep;
    }

    public void setRegExcep(int regExcep)
    {
        this.regExcep = regExcep;
    }

    public int getTipo()
    {
        return tipo;
    }

    public void setTipo(int tipo)
    {
        this.tipo = tipo;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    private int tipo;
    private String local;
    private int idtarea;
    private int estado;
    private int codret;
    private int prodProc;
    private int prodNoProc;
    private int promoProc;
    private int promoNoProc;
    private Date finicio;
    private Date ftermino;
    private int bloque;
    private int total;
    private int regExcep;
    private List excep;
}