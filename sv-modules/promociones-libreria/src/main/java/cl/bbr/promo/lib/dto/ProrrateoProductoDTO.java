// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:19:08
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ProrrateoProductoDTO.java

package cl.bbr.promo.lib.dto;

import java.io.Serializable;

public class ProrrateoProductoDTO
    implements Serializable
{

    public ProrrateoProductoDTO()
    {
    }

    public int getCantPeso()
    {
        return cantPeso;
    }

    public void setCantPeso(int cantPeso)
    {
        this.cantPeso = cantPeso;
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

    public long getProrrateo()
    {
        return prorrateo;
    }

    public void setProrrateo(long prorrateo)
    {
        this.prorrateo = prorrateo;
    }

    private long codigo;
    private int depto;
    private char flagVenta;
    private int cantPeso;
    private long precio;
    private long prorrateo;
    private boolean desc;
    private int indiceAgrup;
    
    
	/**
	 * @return el indiceAgrup
	 */
	public int getIndiceAgrup() {
		return indiceAgrup;
	}

	/**
	 * @param indiceAgrup el indiceAgrup a establecer
	 */
	public void setIndiceAgrup(int indiceAgrup) {
		this.indiceAgrup = indiceAgrup;
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}
}