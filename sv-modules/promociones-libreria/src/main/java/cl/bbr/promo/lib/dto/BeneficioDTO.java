// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BeneficioDTO.java

package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.List;

public class BeneficioDTO
    implements Serializable
{

    public BeneficioDTO()
    {
        codigo = 0;
        tipo = 0;
        productos = null;
    }

    public BeneficioDTO(int codigo, int tipo, List productos)
    {
        this.codigo = codigo;
        this.tipo = tipo;
        this.productos = productos;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public List getProductos()
    {
        return productos;
    }

    public void setProductos(List promociones)
    {
        productos = promociones;
    }

    public int getTipo()
    {
        return tipo;
    }

    public void setTipo(int tipo)
    {
        this.tipo = tipo;
    }

    private int codigo;
    private int tipo;
    private List productos;
}