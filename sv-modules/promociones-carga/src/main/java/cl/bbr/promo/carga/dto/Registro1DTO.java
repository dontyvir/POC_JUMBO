// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:22:42
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro1DTO.java

package cl.bbr.promo.carga.dto;

import java.util.Date;

public class Registro1DTO
{

    public Registro1DTO()
    {
        tipoReg = 0;
        codigo = 0;
        tipoPromo = 0;
        version = 0;
        inicio = null;
        termino = null;
        descriptor = null;
        minCantidad = 0;
        minMonto = 0L;
        tramo1Monto = 0L;
        tramo1Dcto = 0.0D;
        tramo2Monto = 0L;
        tramo2Dcto = 0.0D;
        tramo3Monto = 0L;
        tramo3Dcto = 0.0D;
        tramo4Monto = 0L;
        tramo4Dcto = 0.0D;
        tramo5Monto = 0L;
        tramo5Dcto = 0.0D;
        benef1Fpago = 0;
        benef1Cuotas = 0;
        benef1TCP = 0;
        benef1Monto = 0.0D;
        benef2Fpago = 0;
        benef2Cuotas = 0;
        benef2TCP = 0;
        benef2Monto = 0.0D;
        benef3Fpago = 0;
        benef3Cuotas = 0;
        benef3TCP = 0;
        benef3Monto = 0.0D;
        condicion1 = 0;
        condicion2 = 0;
        condicion3 = 0;
        flagProrrateo = 0;
        flagRecuperable = 0;
        canal = 0;
    }

    public Registro1DTO(int tipoReg, int codigo, int tipoPromo, int version, Date inicio, Date termino, String descriptor, 
            int minCantidad, long minMonto, long tramo1Monto, double tramo1Dcto, 
            long tramo2Monto, double tramo2Dcto, long tramo3Monto, double tramo3Dcto, long tramo4Monto, double tramo4Dcto, long tramo5Monto, 
            double tramo5Dcto, int benef1Fpago, int benef1Cuotas, int benef1TCP, double benef1Monto, 
            int benef2Fpago, int benef2Cuotas, int benef2TCP, double benef2Monto, int benef3Fpago, int benef3Cuotas, 
            int benef3TCP, double benef3Monto, int condicion1, int condicion2, int condicion3, int flagProrrateo, 
            int flagRecuperable, int canal)
    {
        this.tipoReg = tipoReg;
        this.codigo = codigo;
        this.tipoPromo = tipoPromo;
        this.version = version;
        this.inicio = inicio;
        this.termino = termino;
        this.descriptor = descriptor;
        this.minCantidad = minCantidad;
        this.minMonto = minMonto;
        this.tramo1Monto = tramo1Monto;
        this.tramo1Dcto = tramo1Dcto;
        this.tramo2Monto = tramo2Monto;
        this.tramo2Dcto = tramo2Dcto;
        this.tramo3Monto = tramo3Monto;
        this.tramo3Dcto = tramo3Dcto;
        this.tramo4Monto = tramo4Monto;
        this.tramo4Dcto = tramo4Dcto;
        this.tramo5Monto = tramo5Monto;
        this.tramo5Dcto = tramo5Dcto;
        this.benef1Fpago = benef1Fpago;
        this.benef1Cuotas = benef1Cuotas;
        this.benef1TCP = benef1TCP;
        this.benef1Monto = benef1Monto;
        this.benef2Fpago = benef2Fpago;
        this.benef2Cuotas = benef2Cuotas;
        this.benef2TCP = benef2TCP;
        this.benef2Monto = benef2Monto;
        this.benef3Fpago = benef3Fpago;
        this.benef3Cuotas = benef3Cuotas;
        this.benef3TCP = benef3TCP;
        this.benef3Monto = benef3Monto;
        this.condicion1 = condicion1;
        this.condicion2 = condicion2;
        this.condicion3 = condicion3;
        this.flagProrrateo = flagProrrateo;
        this.flagRecuperable = flagRecuperable;
        this.canal = canal;
    }

    public int getBenef1Cuotas()
    {
        return benef1Cuotas;
    }

    public void setBenef1Cuotas(int benef1Cuotas)
    {
        this.benef1Cuotas = benef1Cuotas;
    }

    public int getBenef1Fpago()
    {
        return benef1Fpago;
    }

    public void setBenef1Fpago(int benef1Fpago)
    {
        this.benef1Fpago = benef1Fpago;
    }

    public double getBenef1Monto()
    {
        return benef1Monto;
    }

    public void setBenef1Monto(double benef1Monto)
    {
        this.benef1Monto = benef1Monto;
    }

    public int getBenef1TCP()
    {
        return benef1TCP;
    }

    public void setBenef1TCP(int benef1TCP)
    {
        this.benef1TCP = benef1TCP;
    }

    public int getBenef2Cuotas()
    {
        return benef2Cuotas;
    }

    public void setBenef2Cuotas(int benef2Cuotas)
    {
        this.benef2Cuotas = benef2Cuotas;
    }

    public int getBenef2Fpago()
    {
        return benef2Fpago;
    }

    public void setBenef2Fpago(int benef2Fpago)
    {
        this.benef2Fpago = benef2Fpago;
    }

    public double getBenef2Monto()
    {
        return benef2Monto;
    }

    public void setBenef2Monto(double benef2Monto)
    {
        this.benef2Monto = benef2Monto;
    }

    public int getBenef2TCP()
    {
        return benef2TCP;
    }

    public void setBenef2TCP(int benef2TCP)
    {
        this.benef2TCP = benef2TCP;
    }

    public int getBenef3Cuotas()
    {
        return benef3Cuotas;
    }

    public void setBenef3Cuotas(int benef3Cuotas)
    {
        this.benef3Cuotas = benef3Cuotas;
    }

    public int getBenef3Fpago()
    {
        return benef3Fpago;
    }

    public void setBenef3Fpago(int benef3Fpago)
    {
        this.benef3Fpago = benef3Fpago;
    }

    public double getBenef3Monto()
    {
        return benef3Monto;
    }

    public void setBenef3Monto(double benef3Monto)
    {
        this.benef3Monto = benef3Monto;
    }

    public int getBenef3TCP()
    {
        return benef3TCP;
    }

    public void setBenef3TCP(int benef3TCP)
    {
        this.benef3TCP = benef3TCP;
    }

    public int getCanal()
    {
        return canal;
    }

    public void setCanal(int canal)
    {
        this.canal = canal;
    }

    public int getCodigo()
    {
        return codigo;
    }

    public void setCodigo(int codigo)
    {
        this.codigo = codigo;
    }

    public int getCondicion1()
    {
        return condicion1;
    }

    public void setCondicion1(int condicion1)
    {
        this.condicion1 = condicion1;
    }

    public int getCondicion2()
    {
        return condicion2;
    }

    public void setCondicion2(int condicion2)
    {
        this.condicion2 = condicion2;
    }

    public int getCondicion3()
    {
        return condicion3;
    }

    public void setCondicion3(int condicion3)
    {
        this.condicion3 = condicion3;
    }

    public String getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(String descriptor)
    {
        this.descriptor = descriptor;
    }

    public int getFlagProrrateo()
    {
        return flagProrrateo;
    }

    public void setFlagProrrateo(int flagProrrateo)
    {
        this.flagProrrateo = flagProrrateo;
    }

    public int getFlagRecuperable()
    {
        return flagRecuperable;
    }

    public void setFlagRecuperable(int flagRecuperable)
    {
        this.flagRecuperable = flagRecuperable;
    }

    public Date getInicio()
    {
        return inicio;
    }

    public void setInicio(Date inicio)
    {
        this.inicio = inicio;
    }

    public int getMinCantidad()
    {
        return minCantidad;
    }

    public void setMinCantidad(int minCantidad)
    {
        this.minCantidad = minCantidad;
    }

    public long getMinMonto()
    {
        return minMonto;
    }

    public void setMinMonto(long minMonto)
    {
        this.minMonto = minMonto;
    }

    public Date getTermino()
    {
        return termino;
    }

    public void setTermino(Date termino)
    {
        this.termino = termino;
    }

    public int getTipoPromo()
    {
        return tipoPromo;
    }

    public void setTipoPromo(int tipoPromo)
    {
        this.tipoPromo = tipoPromo;
    }

    public int getTipoReg()
    {
        return tipoReg;
    }

    public void setTipoReg(int tipoReg)
    {
        this.tipoReg = tipoReg;
    }

    public double getTramo1Dcto()
    {
        return tramo1Dcto;
    }

    public void setTramo1Dcto(double tramo1Dcto)
    {
        this.tramo1Dcto = tramo1Dcto;
    }

    public long getTramo1Monto()
    {
        return tramo1Monto;
    }

    public void setTramo1Monto(long tramo1Monto)
    {
        this.tramo1Monto = tramo1Monto;
    }

    public double getTramo2Dcto()
    {
        return tramo2Dcto;
    }

    public void setTramo2Dcto(double tramo2Dcto)
    {
        this.tramo2Dcto = tramo2Dcto;
    }

    public long getTramo2Monto()
    {
        return tramo2Monto;
    }

    public void setTramo2Monto(long tramo2Monto)
    {
        this.tramo2Monto = tramo2Monto;
    }

    public double getTramo3Dcto()
    {
        return tramo3Dcto;
    }

    public void setTramo3Dcto(double tramo3Dcto)
    {
        this.tramo3Dcto = tramo3Dcto;
    }

    public long getTramo3Monto()
    {
        return tramo3Monto;
    }

    public void setTramo3Monto(long tramo3Monto)
    {
        this.tramo3Monto = tramo3Monto;
    }

    public double getTramo4Dcto()
    {
        return tramo4Dcto;
    }

    public void setTramo4Dcto(double tramo4Dcto)
    {
        this.tramo4Dcto = tramo4Dcto;
    }

    public long getTramo4Monto()
    {
        return tramo4Monto;
    }

    public void setTramo4Monto(long tramo4Monto)
    {
        this.tramo4Monto = tramo4Monto;
    }

    public double getTramo5Dcto()
    {
        return tramo5Dcto;
    }

    public void setTramo5Dcto(double tramo5Dcto)
    {
        this.tramo5Dcto = tramo5Dcto;
    }

    public long getTramo5Monto()
    {
        return tramo5Monto;
    }

    public void setTramo5Monto(long tramo5Monto)
    {
        this.tramo5Monto = tramo5Monto;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    private int tipoReg;
    private int codigo;
    private int tipoPromo;
    private int version;
    private Date inicio;
    private Date termino;
    private String descriptor;
    private int minCantidad;
    private long minMonto;
    private long tramo1Monto;
    private double tramo1Dcto;
    private long tramo2Monto;
    private double tramo2Dcto;
    private long tramo3Monto;
    private double tramo3Dcto;
    private long tramo4Monto;
    private double tramo4Dcto;
    private long tramo5Monto;
    private double tramo5Dcto;
    private int benef1Fpago;
    private int benef1Cuotas;
    private int benef1TCP;
    private double benef1Monto;
    private int benef2Fpago;
    private int benef2Cuotas;
    private int benef2TCP;
    private double benef2Monto;
    private int benef3Fpago;
    private int benef3Cuotas;
    private int benef3TCP;
    private double benef3Monto;
    private int condicion1;
    private int condicion2;
    private int condicion3;
    private int flagProrrateo;
    private int flagRecuperable;
    private int canal;
}