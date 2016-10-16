// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:17:05
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MatrizSeccionDAO.java

package cl.bbr.promo.lib.dao;

import cl.bbr.irs.promolib.entity.MatrizSeccionEntity;
import cl.bbr.promo.lib.exception.IrsPromocionException;
import java.util.List;

public interface MatrizSeccionDAO
{

    public abstract List getAllMatrizSeccion(int i)
        throws IrsPromocionException;

    public abstract MatrizSeccionEntity getMatrizBySeccion(int i, int j)
        throws IrsPromocionException;
}