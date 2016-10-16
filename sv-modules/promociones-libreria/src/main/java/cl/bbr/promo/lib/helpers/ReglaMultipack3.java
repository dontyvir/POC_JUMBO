// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReglaMultipack3.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import cl.bbr.promo.lib.dto.*;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp, TipoPromocionTriple, FormaPago, Util

public class ReglaMultipack3 extends Tcp
    implements TipoPromocionTriple
{

    public ReglaMultipack3()
    {
        logger = new Logging(this);
    }

    public void initReglaPromocion(PromocionEntity promoBenef, PromocionEntity promoCond1, PromocionEntity promoCond2, List prodBenef, List prodCond1, List prodCond2)
    {
        regPromoBenef = promoBenef;
        regPromoCond1 = promoCond1;
        regPromoCond2 = promoCond2;
        this.prodBenef = prodBenef;
        this.prodCond1 = prodCond1;
        this.prodCond2 = prodCond2;
        listTcp = new ArrayList();
    }

    public ProrrateoPromocionProductoDTO calculaDcto(int fpago, int cuotas, List TCP, int canal, int local)
    {
        ProductoBenConDTO prod = new ProductoBenConDTO();
        ProrrateoPromocionProductoDTO prorrateoPorPromocion = new ProrrateoPromocionProductoDTO();
        List prorrateoProducto = new ArrayList();
        ProrrateoProductoDTO pp = new ProrrateoProductoDTO();
        prorrateoPorPromocion.setTipo("P");
        prorrateoPorPromocion.setCodigo(regPromoBenef.getCodigo());
        prorrateoPorPromocion.setDescripcion(regPromoBenef.getDescripcion());
        prorrateoPorPromocion.setDescuento(0L);
        listTcp = TCP;
        if(canal != regPromoBenef.getCanal())
            return null;
        FormaPago fp = new FormaPago();
        long beneficio;
        int tcp;
        if(fp.obtieneBeneficio(regPromoBenef, fpago, cuotas, listTcp))
        {
            beneficio = (long)fp.getBeneficio();
            tcp = fp.getTcp();
        } else
        {
            return null;
        }
        int acum_cant_cond1;
        int acum_cant_cond2;
        int acum_cant_benef = acum_cant_cond1 = acum_cant_cond2 = 0;
        int min_cant_benef = regPromoBenef.getMinCantidad();
        int min_cant_cond1 = regPromoCond1.getMinCantidad();
        int min_cant_cond2 = regPromoCond2.getMinCantidad();
        boolean gc1;
        boolean gc2;
        boolean gb = gc1 = gc2 = false;
        boolean gc2gc1gb = true;
        if(regPromoBenef.getFlagProrrateo() == 1)
            gb = true;
        if(regPromoCond1.getFlagProrrateo() == 1)
            gc1 = true;
        if(regPromoCond2.getFlagProrrateo() == 1)
            gc2 = true;
        if(regPromoBenef.getFlagProrrateo() == 0 || regPromoCond1.getFlagProrrateo() == 0 || regPromoCond2.getFlagProrrateo() == 0)
            gc2gc1gb = false;
        else
            gb = gc1 = gc2 = false;
        int TOPE_BENEFICIO = 0;
        int TOPE_CONDICION1 = 0;
        int TOPE_CONDICION2 = 0;
        long tot_acum = 0L;
        long tot_acum_p = 0L;
        long monto_tot_desc = 0L;
        long monto_desc = 0L;
        if(prodBenef == null || prodCond1 == null || prodCond2 == null)
            return null;
        for(int i = TOPE_BENEFICIO; i < prodBenef.size(); i++)
        {
            int cantTcp = getCantidadTCP(listTcp, tcp);
            if(cantTcp <= 0 && tcp != 0)
                break;
            acum_cant_benef++;
            prod = (ProductoBenConDTO)prodBenef.get(i);
            if(gb || gc2gc1gb)
                tot_acum_p += prod.getPrecio();
            tot_acum += prod.getPrecio();
            if(acum_cant_benef == min_cant_benef)
            {
                for(int j = TOPE_CONDICION1; j < prodCond1.size(); j++)
                {
                    acum_cant_cond1++;
                    prod = (ProductoBenConDTO)prodCond1.get(j);
                    if(gc1 || gc2gc1gb)
                        tot_acum_p += prod.getPrecio();
                    tot_acum += prod.getPrecio();
                    if(acum_cant_cond1 == min_cant_cond1 && acum_cant_benef != 0)
                    {
                        for(int k = TOPE_CONDICION2; k < prodCond2.size(); k++)
                        {
                            acum_cant_cond2++;
                            prod = (ProductoBenConDTO)prodCond2.get(k);
                            if(gc2 || gc2gc1gb)
                                tot_acum_p += prod.getPrecio();
                            tot_acum += prod.getPrecio();
                            if(acum_cant_cond2 != min_cant_cond2 || acum_cant_cond1 == 0)
                                continue;
                            monto_desc = tot_acum - beneficio;
                            monto_tot_desc += monto_desc;
                            logger.debug("monto_desc=" + monto_desc);
                            if(monto_desc > 0L)
                            {
                                if(monto_desc > 0L)
                                    listTcp = quemaTCP(listTcp, tcp);
                                long acum_dcto_p = 0L;
                                long diff = 0L;
                                if(gb || gc2gc1gb)
                                {
                                    for(int w = TOPE_BENEFICIO; w <= i; w++)
                                    {
                                        prod = (ProductoBenConDTO)prodBenef.get(w);
                                        pp = new ProrrateoProductoDTO();
                                        pp.setCantPeso(prod.getCant());
                                        pp.setCodigo(prod.getCodigo());
                                        pp.setDepto(prod.getDepto());
                                        pp.setFlagVenta(prod.getFlag());
                                        pp.setPrecio(prod.getPrecio());
                                        long dcto_p = Util.calculaProrrateo(prod.getPrecio(), tot_acum_p, monto_desc);
                                        acum_dcto_p += dcto_p;
                                        pp.setProrrateo(dcto_p);
                                        prorrateoProducto.add(pp);
                                    }

                                }
                                if(gc1 || gc2gc1gb)
                                {
                                    for(int w = TOPE_CONDICION1; w <= j; w++)
                                    {
                                        prod = (ProductoBenConDTO)prodCond1.get(w);
                                        pp = new ProrrateoProductoDTO();
                                        pp.setCantPeso(prod.getCant());
                                        pp.setCodigo(prod.getCodigo());
                                        pp.setDepto(prod.getDepto());
                                        pp.setFlagVenta(prod.getFlag());
                                        pp.setPrecio(prod.getPrecio());
                                        long dcto_p = Util.calculaProrrateo(prod.getPrecio(), tot_acum_p, monto_desc);
                                        acum_dcto_p += dcto_p;
                                        pp.setProrrateo(dcto_p);
                                        prorrateoProducto.add(pp);
                                    }

                                }
                                if(gc2 || gc2gc1gb)
                                {
                                    for(int w = TOPE_CONDICION2; w <= k; w++)
                                    {
                                        prod = (ProductoBenConDTO)prodCond2.get(w);
                                        pp = new ProrrateoProductoDTO();
                                        pp.setCantPeso(prod.getCant());
                                        pp.setCodigo(prod.getCodigo());
                                        pp.setDepto(prod.getDepto());
                                        pp.setFlagVenta(prod.getFlag());
                                        pp.setPrecio(prod.getPrecio());
                                        long dcto_p = Util.calculaProrrateo(prod.getPrecio(), tot_acum_p, monto_desc);
                                        acum_dcto_p += dcto_p;
                                        diff = monto_desc - acum_dcto_p;
                                        if(w == k && diff > 0L && (gc2 || gc2gc1gb))
                                            dcto_p += diff;
                                        pp.setProrrateo(dcto_p);
                                        prorrateoProducto.add(pp);
                                    }

                                }
                                tot_acum = 0L;
                                tot_acum_p = 0L;
                                TOPE_BENEFICIO = i + 1;
                                TOPE_CONDICION1 = j + 1;
                                TOPE_CONDICION2 = k + 1;
                                acum_cant_benef = 0;
                                acum_cant_cond1 = 0;
                                acum_cant_cond2 = 0;
                            }
                            break;
                        }

                    }
                    if(TOPE_CONDICION1 == j + 1)
                        break;
                }

            }
        }

        logger.debug("MONTO_TOT_DESC=" + monto_tot_desc);
        if(monto_tot_desc == 0L)
        {
            return null;
        } else
        {
            prorrateoPorPromocion.setDescuento(monto_tot_desc);
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
    private PromocionEntity regPromoCond1;
    private PromocionEntity regPromoCond2;
    private List prodBenef;
    private List prodCond1;
    private List prodCond2;
    private List listTcp;
}