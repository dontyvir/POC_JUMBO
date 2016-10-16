// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Reg6SeccionxPromo.java

package cl.bbr.promo.carga.dto;


public class Reg6SeccionxPromo
{

    public Reg6SeccionxPromo()
    {
        seccion = 0;
        lunes = false;
        martes = false;
        miercoles = false;
        jueves = false;
        viernes = false;
        sabado = false;
        domingo = false;
        esp1 = false;
        esp2 = false;
        esp3 = false;
        esp4 = false;
        esp5 = false;
        esp6 = false;
        esp7 = false;
        esp8 = false;
    }

    public Reg6SeccionxPromo(int seccion, boolean lunes, boolean martes, boolean miercoles, boolean jueves, boolean viernes, boolean sabado, 
            boolean domingo, boolean esp1, boolean esp2, boolean esp3, boolean esp4, boolean esp5, boolean esp6, 
            boolean esp7, boolean esp8)
    {
        this.seccion = seccion;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.esp1 = esp1;
        this.esp2 = esp2;
        this.esp3 = esp3;
        this.esp4 = esp4;
        this.esp5 = esp5;
        this.esp6 = esp6;
        this.esp7 = esp7;
        this.esp8 = esp8;
    }

    public boolean isDomingo()
    {
        return domingo;
    }

    public void setDomingo(boolean domingo)
    {
        this.domingo = domingo;
    }

    public boolean isEsp1()
    {
        return esp1;
    }

    public void setEsp1(boolean esp1)
    {
        this.esp1 = esp1;
    }

    public boolean isEsp2()
    {
        return esp2;
    }

    public void setEsp2(boolean esp2)
    {
        this.esp2 = esp2;
    }

    public boolean isEsp3()
    {
        return esp3;
    }

    public void setEsp3(boolean esp3)
    {
        this.esp3 = esp3;
    }

    public boolean isEsp4()
    {
        return esp4;
    }

    public void setEsp4(boolean esp4)
    {
        this.esp4 = esp4;
    }

    public boolean isEsp5()
    {
        return esp5;
    }

    public void setEsp5(boolean esp5)
    {
        this.esp5 = esp5;
    }

    public boolean isEsp6()
    {
        return esp6;
    }

    public void setEsp6(boolean esp6)
    {
        this.esp6 = esp6;
    }

    public boolean isEsp7()
    {
        return esp7;
    }

    public void setEsp7(boolean esp7)
    {
        this.esp7 = esp7;
    }

    public boolean isEsp8()
    {
        return esp8;
    }

    public void setEsp8(boolean esp8)
    {
        this.esp8 = esp8;
    }

    public boolean isJueves()
    {
        return jueves;
    }

    public void setJueves(boolean jueves)
    {
        this.jueves = jueves;
    }

    public boolean isLunes()
    {
        return lunes;
    }

    public void setLunes(boolean lunes)
    {
        this.lunes = lunes;
    }

    public boolean isMartes()
    {
        return martes;
    }

    public void setMartes(boolean martes)
    {
        this.martes = martes;
    }

    public boolean isMiercoles()
    {
        return miercoles;
    }

    public void setMiercoles(boolean miercoles)
    {
        this.miercoles = miercoles;
    }

    public boolean isSabado()
    {
        return sabado;
    }

    public void setSabado(boolean sabado)
    {
        this.sabado = sabado;
    }

    public int getSeccion()
    {
        return seccion;
    }

    public void setSeccion(int seccion)
    {
        this.seccion = seccion;
    }

    public boolean isViernes()
    {
        return viernes;
    }

    public void setViernes(boolean viernes)
    {
        this.viernes = viernes;
    }

    private int seccion;
    private boolean lunes;
    private boolean martes;
    private boolean miercoles;
    private boolean jueves;
    private boolean viernes;
    private boolean sabado;
    private boolean domingo;
    private boolean esp1;
    private boolean esp2;
    private boolean esp3;
    private boolean esp4;
    private boolean esp5;
    private boolean esp6;
    private boolean esp7;
    private boolean esp8;
}