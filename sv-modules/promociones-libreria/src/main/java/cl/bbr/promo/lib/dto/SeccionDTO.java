// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SeccionDTO.java

package cl.bbr.promo.lib.dto;


public class SeccionDTO
{

    public SeccionDTO(int seccion, long monto)
    {
        this.seccion = seccion;
        this.monto = monto;
    }

    public SeccionDTO()
    {
        seccion = 0;
        monto = 0L;
    }

    public long getMonto()
    {
        return monto;
    }

    public void setMonto(long monto)
    {
        this.monto = monto;
    }

    public int getSeccion()
    {
        return seccion;
    }

    public void setSeccion(int seccion)
    {
        this.seccion = seccion;
    }

    private int seccion;
    private long monto;
    //[20121114avc
    private boolean afectoDescColaborador;
    
    /**
     * @return Returns the afectoDescColaborador.
     */
    public boolean isAfectoDescColaborador() {
        return afectoDescColaborador;
    }
    /**
     * @param afectoDescColaborador The afectoDescColaborador to set.
     */
    public void setAfectoDescColaborador(boolean afectoDescColaborador) {
        this.afectoDescColaborador = afectoDescColaborador;
    }
    //]20121114avc
}