// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProductoBenConDTO.java

package cl.bbr.promo.lib.dto;


public class ProductoBenConDTO
{

    public ProductoBenConDTO()
    {
        codigo = 0L;
        depto = 0;
        flag = ' ';
        cant = 0;
        precio = 0L;
    }

    public ProductoBenConDTO(long codigo, int depto, char flag, int cant, long precio)
    {
        this.codigo = codigo;
        this.depto = depto;
        this.flag = flag;
        this.cant = cant;
        this.precio = precio;
    }

    public int getCant()
    {
        return cant;
    }

    public void setCant(int cant)
    {
        this.cant = cant;
    }

    public long getCodigo()
    {
        return codigo;
    }

    public void setCodigo(long codigo)
    {
        this.codigo = codigo;
    }

    public int getDepto()
    {
        return depto;
    }

    public void setDepto(int depto)
    {
        this.depto = depto;
    }

    public char getFlag()
    {
        return flag;
    }

    public void setFlag(char flag)
    {
        this.flag = flag;
    }

    public long getPrecio()
    {
        return precio;
    }

    public void setPrecio(long precio)
    {
        this.precio = precio;
    }

    private long codigo;
    private int depto;
    private char flag;
    private int cant;
    private long precio;
}