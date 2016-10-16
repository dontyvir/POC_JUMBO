// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FormaPago.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.log.Logging;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            Tcp

public class FormaPago extends Tcp
{

    public FormaPago()
    {
        logger = new Logging(this);
        beneficio = 0.0D;
        tcp = 0;
    }

    public boolean obtieneBeneficio(PromocionEntity promo, int fpago, int cuotas, List TCP)
    {
        int fp = fpago;
        int p_fpago = promo.getBenef1FormaPago();
        int p_cuotas = promo.getBenef1NroCuotas();
        int p_tcp = promo.getBenef1TCP();
        if(p_fpago == FPAGO_TODAS || p_fpago < FPAGO_TODAS_MAS && p_fpago == fpago || p_fpago == FPAGO_TODAS_EASY && fpago > 1 && fpago <= 9 || p_fpago == FPAGO_TODAS_JUMBO && fpago > 70 && fpago <= 79 || p_fpago == FPAGO_TODAS_PARIS && fpago > 80 && fpago <= 89)
        {
            if((p_cuotas == CUOTAS_TODAS || p_cuotas == cuotas) && (p_tcp == TCP_TODOS || validaTCP(TCP, p_tcp)))
            {
                tcp = promo.getBenef1TCP();
                beneficio = promo.getBenef1Monto();
                logger.debug("p_fpago=" + p_fpago + ", beneficio1=" + beneficio);
                return p_fpago != FPAGO_TODAS || p_cuotas != CUOTAS_TODAS || p_tcp != TCP_TODOS || beneficio != 0.0D;
            }
        } else
        if(p_fpago >= FPAGO_TODAS_MAS && p_fpago <= 99)
        {
            if(fpago >= FPAGO_TODAS_EASY && fpago <= 9)
                fpago += 89;
            else
            if(fpago >= FPAGO_TODAS_JUMBO && fpago <= 79)
                fpago += 20;
            else
            if(fpago >= FPAGO_TODAS_PARIS && fpago <= 89)
                fpago += 10;
            if((p_fpago == fpago || p_fpago == FPAGO_TODAS_MAS && fpago > 90 && fpago <= 99) && (p_cuotas == CUOTAS_TODAS || p_cuotas == cuotas) && (p_tcp == TCP_TODOS || validaTCP(TCP, p_tcp)))
            {
                beneficio = promo.getBenef1Monto();
                logger.debug("p_fpago=" + p_fpago + ", beneficio1=" + beneficio);
                tcp = promo.getBenef1TCP();
                return true;
            }
        }
        fpago = fp;
        p_fpago = promo.getBenef2FormaPago();
        p_cuotas = promo.getBenef2NroCuotas();
        p_tcp = promo.getBenef2TCP();
        if(p_fpago == FPAGO_TODAS || p_fpago < FPAGO_TODAS_MAS && p_fpago == fpago || p_fpago == FPAGO_TODAS_EASY && fpago > 1 && fpago <= 9 || p_fpago == FPAGO_TODAS_JUMBO && fpago > 70 && fpago <= 79 || p_fpago == FPAGO_TODAS_PARIS && fpago > 80 && fpago <= 89)
        {
            if((p_cuotas == CUOTAS_TODAS || p_cuotas == cuotas) && (p_tcp == TCP_TODOS || validaTCP(TCP, p_tcp)))
            {
                tcp = promo.getBenef2TCP();
                beneficio = promo.getBenef2Monto();
                logger.debug("p_fpago=" + p_fpago + ", beneficio2=" + beneficio);
                return p_fpago != FPAGO_TODAS || p_cuotas != CUOTAS_TODAS || p_tcp != TCP_TODOS || beneficio != 0.0D;
            }
        } else
        if(p_fpago >= FPAGO_TODAS_MAS && p_fpago <= 99)
        {
            if(fpago >= FPAGO_TODAS_EASY && fpago <= 9)
                fpago += 89;
            else
            if(fpago >= FPAGO_TODAS_JUMBO && fpago <= 79)
                fpago += 20;
            else
            if(fpago >= FPAGO_TODAS_PARIS && fpago <= 89)
                fpago += 10;
            if((p_fpago == fpago || p_fpago == FPAGO_TODAS_MAS && fpago > 90 && fpago <= 99) && (p_cuotas == CUOTAS_TODAS || p_cuotas == cuotas) && (p_tcp == TCP_TODOS || validaTCP(TCP, p_tcp)))
            {
                beneficio = promo.getBenef2Monto();
                logger.debug("p_fpago=" + p_fpago + ", beneficio2=" + beneficio);
                tcp = promo.getBenef2TCP();
                return true;
            }
        }
        fpago = fp;
        p_fpago = promo.getBenef3FormaPago();
        p_cuotas = promo.getBenef3NroCuotas();
        p_tcp = promo.getBenef3TCP();
        if(p_fpago == FPAGO_TODAS || p_fpago < FPAGO_TODAS_MAS && p_fpago == fpago || p_fpago == FPAGO_TODAS_EASY && fpago > 1 && fpago <= 9 || p_fpago == FPAGO_TODAS_JUMBO && fpago > 70 && fpago <= 79 || p_fpago == FPAGO_TODAS_PARIS && fpago > 80 && fpago <= 89)
        {
            if((p_cuotas == CUOTAS_TODAS || p_cuotas == cuotas) && (p_tcp == TCP_TODOS || validaTCP(TCP, p_tcp)))
            {
                tcp = promo.getBenef3TCP();
                beneficio = promo.getBenef3Monto();
                logger.debug("p_fpago=" + p_fpago + ", beneficio3=" + beneficio);
                return p_fpago != FPAGO_TODAS || p_cuotas != CUOTAS_TODAS || p_tcp != TCP_TODOS || beneficio != 0.0D;
            }
        } else
        if(p_fpago >= FPAGO_TODAS_MAS && p_fpago <= 99)
        {
            if(fpago >= FPAGO_TODAS_EASY && fpago <= 9)
                fpago += 89;
            else
            if(fpago >= FPAGO_TODAS_JUMBO && fpago <= 79)
                fpago += 20;
            else
            if(fpago >= FPAGO_TODAS_PARIS && fpago <= 89)
                fpago += 10;
            if((p_fpago == fpago || p_fpago == FPAGO_TODAS_MAS && fpago > 90 && fpago <= 99) && (p_cuotas == CUOTAS_TODAS || p_cuotas == cuotas) && (p_tcp == TCP_TODOS || validaTCP(TCP, p_tcp)))
            {
                beneficio = promo.getBenef3Monto();
                tcp = promo.getBenef3TCP();
                logger.debug("p_fpago=" + p_fpago + ", beneficio3=" + beneficio);
                return true;
            }
        }
        return false;
    }

    public double getBeneficio()
    {
        return beneficio;
    }

    public int getTcp()
    {
        return tcp;
    }

    Logging logger;
    private static int FPAGO_TODAS = 0;
    private static int FPAGO_TODAS_EASY = 1;
    private static int FPAGO_TODAS_JUMBO = 70;
    private static int FPAGO_TODAS_PARIS = 80;
    private static int FPAGO_TODAS_MAS = 90;
    private static int CUOTAS_TODAS = 0;
    private static int TCP_TODOS = 0;
    double beneficio;
    int tcp;

}