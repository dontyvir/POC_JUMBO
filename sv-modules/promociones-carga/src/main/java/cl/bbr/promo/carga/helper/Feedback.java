// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:31
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Feedback.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.FeedbackProductoDTO;
import cl.bbr.promo.carga.dto.FeedbackSeccionDTO;

public abstract class Feedback
{

    public Feedback()
    {
    }

    public static FeedbackProductoDTO excepProducto(int promocion, long producto, int codret)
    {
        return new FeedbackProductoDTO(promocion, producto, codret);
    }

    public static FeedbackSeccionDTO excepSeccion(int seccion, int promocion, int codret)
    {
        return new FeedbackSeccionDTO(seccion, promocion, codret);
    }
}