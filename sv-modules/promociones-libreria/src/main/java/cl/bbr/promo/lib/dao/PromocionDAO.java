// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:17:06
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PromocionDAO.java

package cl.bbr.promo.lib.dao;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.promo.lib.exception.IrsPromocionException;

public interface PromocionDAO
{

    public abstract PromocionEntity getPromoById(int i, int j)
        throws IrsPromocionException;
}