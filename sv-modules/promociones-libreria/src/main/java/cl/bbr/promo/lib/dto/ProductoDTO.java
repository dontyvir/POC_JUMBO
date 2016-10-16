// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProductoDTO.java

package cl.bbr.promo.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductoDTO
    implements Serializable
{

    public ProductoDTO()
    {
        codigo = 0L;
        depto = 0;
        flagVenta = ' ';
        flagCantidad = ' ';
        cantidad = 0;
        precio = 0L;
        promocion = new ArrayList();
    }

    public ProductoDTO(long codigo, int depto, char flagVenta, char flagCantidad, int cantidad, long precio, List promocion)
    {
        this.codigo = codigo;
        this.depto = depto;
        this.flagVenta = flagVenta;
        this.flagCantidad = flagCantidad;
        this.cantidad = cantidad;
        this.precio = precio;
        this.promocion = promocion;
    }

    public int getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(int cantidad)
    {
        this.cantidad = cantidad;
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

    public char getFlagCantidad()
    {
        return flagCantidad;
    }

    public void setFlagCantidad(char flagCantidad)
    {
        this.flagCantidad = flagCantidad;
    }

    public char getFlagVenta()
    {
        return flagVenta;
    }

    public void setFlagVenta(char flagVenta)
    {
        this.flagVenta = flagVenta;
    }

    public long getPrecio()
    {
        return precio;
    }

    public void setPrecio(long precio)
    {
        this.precio = precio;
    }

    public List getPromocion()
    {
        return promocion;
    }

    public void setPromocion(List promocion)
    {
        this.promocion = promocion;
    }

    private long codigo;
    private int depto;
    private char flagVenta;
    private char flagCantidad;
    private int cantidad;
    private long precio;
    private List promocion;
    private long idProducto;

    
	private int rubro;

	public int getRubro() {
		return rubro;
	}

	public void setRubro(int rubro) {
		this.rubro = rubro;
	}

	public long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	//[20121114avc
    private boolean afectoDescColaborador;

    /**
     * @return Returns the afectoDescColaborador.
     */
    public boolean isAfectoDescColaborador() {
        return afectoDescColaborador;
}
    /**
     * @param afectoDescColaborador
     *            The afectoDescColaborador to set.
     */
    public void setAfectoDescColaborador(boolean afectoDescColaborador) {
        this.afectoDescColaborador = afectoDescColaborador;
    }
    //]20121114avc
}