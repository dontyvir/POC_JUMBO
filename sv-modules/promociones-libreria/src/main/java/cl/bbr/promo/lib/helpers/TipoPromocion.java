// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TipoPromocion.java

package cl.bbr.promo.lib.helpers;


public abstract class TipoPromocion
{

    public TipoPromocion()
    {
    }

    public static final int TIPO_DCTO_SIMPLE_SC_PORCENTAJE = 1;
    public static final int TIPO_DCTO_SIMPLE_SC_PFIJO = 2;
    public static final int TIPO_DCTO_SIMPLE_CANT_PORCENTAJE = 3;
    public static final int TIPO_DCTO_SIMPLE_MONTO_PORCENTAJE = 4;
    public static final int TIPO_PACK_MxN = 11;
    public static final int TIPO_MULTIPACK1 = 12;
    public static final int TIPO_MULTIPACK2_CONDICION = 100;
    public static final int TIPO_MULTIPACK2_BENEFICIO = 105;
    public static final int TIPO_MULTIPACK3_CONDICION = 200;
    public static final int TIPO_MULTIPACK3_BENEFICIO = 205;
    public static final int TIPO_PROMO_SIMPLE_COND_CANTIDAD = 100;
    public static final int TIPO_PROMO_SIMPLE_COND_MONTO = 101;
    public static final int TIPO_PROMO_SIMPLE_BENEF_PORCENTAJE = 102;
    public static final int TIPO_PROMO_SIMPLE_BENEF_PRECIOFIJO = 103;
    public static final int TIPO_PROMO_SIMPLE_BENEF_REGALO = 104;
    public static final int TIPO_DCTO_LUNES_PORCENTAJE = 900;
    public static final int TIPO_DCTO_MARTES_PORCENTAJE = 901;
    public static final int TIPO_DCTO_MIERCOLES_PORCENTAJE = 902;
    public static final int TIPO_DCTO_JUEVES_PORCENTAJE = 903;
    public static final int TIPO_DCTO_VIERNES_PORCENTAJE = 904;
    public static final int TIPO_DCTO_SABADO_PORCENTAJE = 905;
    public static final int TIPO_DCTO_DOMINGO_PORCENTAJE = 906;
    public static final int TIPO_DCTO_ESP1_PORCENTAJE = 907;
    public static final int TIPO_DCTO_ESP2_PORCENTAJE = 908;
    public static final int TIPO_DCTO_ESP3_PORCENTAJE = 909;
    public static final int TIPO_DCTO_ESP4_PORCENTAJE = 910;
    public static final int TIPO_DCTO_ESP5_PORCENTAJE = 911;
    public static final int TIPO_DCTO_ESP6_PORCENTAJE = 912;
    public static final int TIPO_DCTO_ESP7_PORCENTAJE = 913;
    public static final int TIPO_DCTO_ESP8_PORCENTAJE = 914;
    public static final String TIPO_PRORRATEO_PRODUCTO = "P";
    public static final String TIPO_PRORRATEO_JERARQUIA = "J";
    public static final String TIPO_PRORRATEO_SECCION = "S";
    public static final String TIPO_PRORRATEO_BOLETA = "B";
    public static final char FLAG_VENTA = 86;
    public static final char FLAG_ANULACION = 65;
    public static final char FLAG_CANTIDAD = 67;
    public static final char FLAG_PESO = 80;
    public static final int PRORRATEO_ON = 1;
    public static final int PRORRATEO_OFF = 0;
    public static final int FLAG_ACUMULABLE_OFF = 3;
    public static final int FLAG_ACUMULABLE_ON = 4;
}