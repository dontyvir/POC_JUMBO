// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaPromocionSimple.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionDoble, FormaPago, Util

public class ReglaPromocionSimple extends Tcp
    implements TipoPromocionDoble
{

    public ReglaPromocionSimple()
    {
        logger = new Logging(this);
    }

    public void initReglaPromocion(PromocionEntity promoBenef, PromocionEntity promoCond, List prodBenef, List prodCond)
    {
        regPromoBenef = promoBenef;
        regPromoCond = promoCond;
        this.prodBenef = prodBenef;
        this.prodCond = prodCond;
        listTcp = new ArrayList();
    }

    public ProrrateoPromocionProductoDTO calculaDcto(int fpago, int cuotas, List TCP, int canal, int local)
    {
        ProductoBenConDTO p_cond = new ProductoBenConDTO();
        ProductoBenConDTO p_benef = new ProductoBenConDTO();
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        int acum_cant = 0;
        long total_dcto;
        long acum_monto = total_dcto = 0L;
        double dcto;
        double beneficio = dcto = 0.0D;
        long acum_suma_p;
        long tot_acum_p = acum_suma_p = 0L;
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromoBenef.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromoBenef.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
        listTcp = TCP;
        if(canal != regPromoBenef.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        int tcp;
        if(fp.obtieneBeneficio(regPromoBenef, fpago, cuotas, listTcp))
        {
            beneficio = (long)fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        int min_cant = regPromoCond.getMinCantidad();
        long min_monto = regPromoCond.getMinMonto();
        boolean gc1;
        boolean gb = gc1 = false;
        boolean gc1gb = true;
        if(regPromoBenef.getFlagProrrateo() == 1)
            gb = true;
        if(regPromoCond.getFlagProrrateo() == 1)
            gc1 = true;
        if(regPromoBenef.getFlagProrrateo() == 0 || regPromoCond.getFlagProrrateo() == 0)
            gc1gb = false;
        else
            gb = gc1 = false;
        int TOPE_MIN = 0;
        for(int i = 0; i < prodBenef.size(); i++)
        {
            int cantTcp = getCantidadTCP(listTcp, tcp);
            //logger.debug("tcp=" + tcp + " | cantTcp=" + cantTcp);
            if(cantTcp <= 0 && tcp != 0)
                break;
            p_benef = (ProductoBenConDTO)prodBenef.get(i);
            acum_suma_p += p_benef.getPrecio();
            if(gb || gc1gb)
                tot_acum_p += p_benef.getPrecio();
            for(int j = TOPE_MIN; j < prodCond.size(); j++)
            {
                p_cond = (ProductoBenConDTO)prodCond.get(j);
                acum_suma_p += p_cond.getPrecio();
                if(gc1 || gc1gb)
                    tot_acum_p += p_cond.getPrecio();
                if(regPromoCond.getTipo() == 100)
                {
                    acum_cant++;
                    dcto = 0.0D;
                    if(acum_cant == min_cant)
                    {
                        if(regPromoBenef.getTipo() == 102)
                        {
                            if(beneficio == 0.0D)
                                return null;
                            dcto = ((double)p_benef.getPrecio() * beneficio) / 100D;
                            dcto = (dcto + 0.5D) / 1.0D;
                            total_dcto += (long)dcto;
                            long acum_dcto_p = 0L;
                            long diff = 0L;
                            if(gc1 || gc1gb)
                            {
                                for(int k = TOPE_MIN; k <= j; k++)
                                {
                                    ProductoBenConDTO p = (ProductoBenConDTO)prodCond.get(k);
                                    ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                    pp.setCodigo(p.getCodigo());
                                    pp.setCantPeso(p.getCant());
                                    pp.setDepto(p.getDepto());
                                    pp.setFlagVenta(p.getFlag());
                                    pp.setPrecio(p.getPrecio());
                                    long dcto_p = Util.calculaProrrateo(p.getPrecio(), tot_acum_p, (long)dcto);
                                    acum_dcto_p += dcto_p;
                                    diff = (long)dcto - acum_dcto_p;
                                    if(k == j && diff > 0L && gc1)
                                        dcto_p += diff;
                                    pp.setProrrateo(dcto_p);
                                    prorrateoProducto.add(pp);
                                }

                            }
                            if(gb || gc1gb)
                            {
                                ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                pp.setCodigo(p_benef.getCodigo());
                                pp.setCantPeso(p_benef.getCant());
                                pp.setDepto(p_benef.getDepto());
                                pp.setFlagVenta(p_benef.getFlag());
                                pp.setPrecio(p_benef.getPrecio());
                                long dcto_p = Util.calculaProrrateo(p_benef.getPrecio(), tot_acum_p, (long)dcto);
                                acum_dcto_p += dcto_p;
                                diff = (long)dcto - acum_dcto_p;
                                if(diff > 0L && (gb || gc1gb))
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }
                            acum_cant = 0;
                            acum_suma_p = 0L;
                            tot_acum_p = 0L;
                        }
                        if(regPromoBenef.getTipo() == 103)
                        {
                            dcto = (double)p_benef.getPrecio() - beneficio;
                            if(dcto < 0.0D)
                                dcto = p_benef.getPrecio();
                            total_dcto += (long)dcto;
                            long acum_dcto_p = 0L;
                            long diff = 0L;
                            if(gc1 || gc1gb)
                            {
                                for(int k = TOPE_MIN; k <= j; k++)
                                {
                                    ProductoBenConDTO p = (ProductoBenConDTO)prodCond.get(k);
                                    ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                    pp.setCodigo(p.getCodigo());
                                    pp.setCantPeso(p.getCant());
                                    pp.setDepto(p.getDepto());
                                    pp.setFlagVenta(p.getFlag());
                                    pp.setPrecio(p.getPrecio());
                                    long dcto_p = Util.calculaProrrateo(p.getPrecio(), tot_acum_p, (long)dcto);
                                    acum_dcto_p += dcto_p;
                                    diff = (long)dcto - acum_dcto_p;
                                    if(k == j && diff > 0L && gc1)
                                        dcto_p += diff;
                                    pp.setProrrateo(dcto_p);
                                    prorrateoProducto.add(pp);
                                }

                            }
                            if(gb || gc1gb)
                            {
                                ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                pp.setCodigo(p_benef.getCodigo());
                                pp.setCantPeso(p_benef.getCant());
                                pp.setDepto(p_benef.getDepto());
                                pp.setFlagVenta(p_benef.getFlag());
                                pp.setPrecio(p_benef.getPrecio());
                                long dcto_p = Util.calculaProrrateo(p_benef.getPrecio(), tot_acum_p, (long)dcto);
                                acum_dcto_p += dcto_p;
                                diff = (long)dcto - acum_dcto_p;
                                if(diff > 0L && (gb || gc1gb))
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }
                            acum_cant = 0;
                            acum_suma_p = 0L;
                            tot_acum_p = 0L;
                        }
                        if(regPromoBenef.getTipo() == 104)
                        {
                            dcto = p_benef.getPrecio();
                            total_dcto += (long)dcto;
                            long acum_dcto_p = 0L;
                            long diff = 0L;
                            if(gc1 || gc1gb)
                            {
                                for(int k = TOPE_MIN; k <= j; k++)
                                {
                                    ProductoBenConDTO p = (ProductoBenConDTO)prodCond.get(k);
                                    ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                    pp.setCodigo(p.getCodigo());
                                    pp.setCantPeso(p.getCant());
                                    pp.setDepto(p.getDepto());
                                    pp.setFlagVenta(p.getFlag());
                                    pp.setPrecio(p.getPrecio());
                                    long dcto_p = Util.calculaProrrateo(p.getPrecio(), tot_acum_p, (long)dcto);
                                    acum_dcto_p += dcto_p;
                                    diff = (long)dcto - acum_dcto_p;
                                    if(k == j && diff > 0L && gc1)
                                        dcto_p += diff;
                                    pp.setProrrateo(dcto_p);
                                    prorrateoProducto.add(pp);
                                }

                            }
                            if(gb || gc1gb)
                            {
                                ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                pp.setCodigo(p_benef.getCodigo());
                                pp.setCantPeso(p_benef.getCant());
                                pp.setDepto(p_benef.getDepto());
                                pp.setFlagVenta(p_benef.getFlag());
                                pp.setPrecio(p_benef.getPrecio());
                                long dcto_p = Util.calculaProrrateo(p_benef.getPrecio(), tot_acum_p, (long)dcto);
                                acum_dcto_p += dcto_p;
                                diff = (long)dcto - acum_dcto_p;
                                if(diff > 0L && (gb || gc1gb))
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }
                            acum_cant = 0;
                            acum_suma_p = 0L;
                            tot_acum_p = 0L;
                        }
                        TOPE_MIN = j + 1;
                        break;
                    }
                    if(dcto > 0.0D)
                        listTcp = quemaTCP(listTcp, tcp);
                }
                if(regPromoCond.getTipo() != 101)
                    continue;
                acum_monto += p_cond.getPrecio();
                dcto = 0.0D;
                if(acum_monto >= min_monto)
                {
                    if(regPromoBenef.getTipo() == 102)
                    {
                        if(beneficio == 0.0D)
                            return null;
                        dcto = ((double)p_benef.getPrecio() * beneficio) / 100D;
                        dcto = (dcto + 0.5D) / 1.0D;
                        total_dcto += (long)dcto;
                        long acum_dcto_p = 0L;
                        long diff = 0L;
                        if(gc1 || gc1gb)
                        {
                            for(int k = TOPE_MIN; k <= j; k++)
                            {
                                ProductoBenConDTO p = (ProductoBenConDTO)prodCond.get(k);
                                ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                pp.setCodigo(p.getCodigo());
                                pp.setCantPeso(p.getCant());
                                pp.setDepto(p.getDepto());
                                pp.setFlagVenta(p.getFlag());
                                pp.setPrecio(p.getPrecio());
                                long dcto_p = Util.calculaProrrateo(p.getPrecio(), tot_acum_p, (long)dcto);
                                acum_dcto_p += dcto_p;
                                diff = (long)dcto - acum_dcto_p;
                                if(k == j && diff > 0L && gc1)
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }

                        }
                        if(gb || gc1gb)
                        {
                            ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                            pp.setCodigo(p_benef.getCodigo());
                            pp.setCantPeso(p_benef.getCant());
                            pp.setDepto(p_benef.getDepto());
                            pp.setFlagVenta(p_benef.getFlag());
                            pp.setPrecio(p_benef.getPrecio());
                            long dcto_p = Util.calculaProrrateo(p_benef.getPrecio(), tot_acum_p, (long)dcto);
                            acum_dcto_p += dcto_p;
                            diff = (long)dcto - acum_dcto_p;
                            if(diff > 0L && (gb || gc1gb))
                                dcto_p += diff;
                            pp.setProrrateo(dcto_p);
                            prorrateoProducto.add(pp);
                        }
                        acum_cant = 0;
                        acum_suma_p = 0L;
                        tot_acum_p = 0L;
                    }
                    if(regPromoBenef.getTipo() == 103)
                    {
                        dcto = (double)p_benef.getPrecio() - beneficio;
                        if(dcto <= 0.0D)
                            dcto = p_benef.getPrecio();
                        total_dcto += (long)dcto;
                        long acum_dcto_p = 0L;
                        long diff = 0L;
                        if(gc1 || gc1gb)
                        {
                            for(int k = TOPE_MIN; k <= j; k++)
                            {
                                ProductoBenConDTO p = (ProductoBenConDTO)prodCond.get(k);
                                ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                pp.setCodigo(p.getCodigo());
                                pp.setCantPeso(p.getCant());
                                pp.setDepto(p.getDepto());
                                pp.setFlagVenta(p.getFlag());
                                pp.setPrecio(p.getPrecio());
                                long dcto_p = Util.calculaProrrateo(p.getPrecio(), tot_acum_p, (long)dcto);
                                acum_dcto_p += dcto_p;
                                diff = (long)dcto - acum_dcto_p;
                                if(k == j && diff > 0L && gc1)
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }

                        }
                        if(gb || gc1gb)
                        {
                            ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                            pp.setCodigo(p_benef.getCodigo());
                            pp.setCantPeso(p_benef.getCant());
                            pp.setDepto(p_benef.getDepto());
                            pp.setFlagVenta(p_benef.getFlag());
                            pp.setPrecio(p_benef.getPrecio());
                            long dcto_p = Util.calculaProrrateo(p_benef.getPrecio(), tot_acum_p, (long)dcto);
                            acum_dcto_p += dcto_p;
                            diff = (long)dcto - acum_dcto_p;
                            if(diff > 0L && (gb || gc1gb))
                                dcto_p += diff;
                            pp.setProrrateo(dcto_p);
                            prorrateoProducto.add(pp);
                        }
                        acum_cant = 0;
                        acum_suma_p = 0L;
                        tot_acum_p = 0L;
                    }
                    if(regPromoBenef.getTipo() == 104)
                    {
                        dcto = p_benef.getPrecio();
                        total_dcto += (long)dcto;
                        long acum_dcto_p = 0L;
                        long diff = 0L;
                        if(gc1 || gc1gb)
                        {
                            for(int k = TOPE_MIN; k <= j; k++)
                            {
                                ProductoBenConDTO p = (ProductoBenConDTO)prodCond.get(k);
                                ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                                pp.setCodigo(p.getCodigo());
                                pp.setCantPeso(p.getCant());
                                pp.setDepto(p.getDepto());
                                pp.setFlagVenta(p.getFlag());
                                pp.setPrecio(p.getPrecio());
                                long dcto_p = Util.calculaProrrateo(p.getPrecio(), tot_acum_p, (long)dcto);
                                acum_dcto_p += dcto_p;
                                diff = (long)dcto - acum_dcto_p;
                                if(k == j && diff > 0L && gc1)
                                    dcto_p += diff;
                                pp.setProrrateo(dcto_p);
                                prorrateoProducto.add(pp);
                            }

                        }
                        if(gb || gc1gb)
                        {
                            ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
                            pp.setCodigo(p_benef.getCodigo());
                            pp.setCantPeso(p_benef.getCant());
                            pp.setDepto(p_benef.getDepto());
                            pp.setFlagVenta(p_benef.getFlag());
                            pp.setPrecio(p_benef.getPrecio());
                            long dcto_p = Util.calculaProrrateo(p_benef.getPrecio(), tot_acum_p, (long)dcto);
                            acum_dcto_p += dcto_p;
                            diff = (long)dcto - acum_dcto_p;
                            if(diff > 0L && (gb || gc1gb))
                                dcto_p += diff;
                            pp.setProrrateo(dcto_p);
                            prorrateoProducto.add(pp);
                        }
                        acum_cant = 0;
                        acum_suma_p = 0L;
                        tot_acum_p = 0L;
                    }
                    TOPE_MIN = j + 1;
                    break;
                }
                if(dcto > 0.0D)
                    listTcp = quemaTCP(listTcp, tcp);
            }

        }

        if(total_dcto == 0L)
        {
            return null;
        } else
        {
            prorrateoPorPromocion.setDescuento(total_dcto);
            prorrateoPorPromocion.setListadoProductos(prorrateoProducto);
            return prorrateoPorPromocion;
        }
    }

    public List getListTcp()
    {
        return listTcp;
    }

    Logging logger;
    private PromocionEntity regPromoBenef;
    private PromocionEntity regPromoCond;
    private List prodBenef;
    private List prodCond;
    private List listTcp;
}