// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:17:09
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PromoSeccionDAO.java

package cl.bbr.promo.lib.dao;

import cl.bbr.irs.promolib.entity.PromoSeccionEntity;
import cl.bbr.promo.lib.exception.IrsPromocionException;
import java.util.List;

public interface PromoSeccionDAO
{

    public abstract List getAllPromoSeccion(int i)
        throws IrsPromocionException;

    public abstract PromoSeccionEntity getPromoByTipo(int i, int j)
        throws IrsPromocionException;
}