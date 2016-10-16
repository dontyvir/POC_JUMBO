// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TipoPromocionDoble.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.promo.lib.dto.ProrrateoPromocionProductoDTO;
import java.util.List;

public interface TipoPromocionDoble
{

    public abstract void initReglaPromocion(PromocionEntity promocionentity, PromocionEntity promocionentity1, List list, List list1);

    public abstract ProrrateoPromocionProductoDTO calculaDcto(int i, int j, List list, int k, int l);
}