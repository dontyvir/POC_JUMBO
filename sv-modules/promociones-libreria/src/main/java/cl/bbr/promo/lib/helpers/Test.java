// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Test.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.irs.promolib.entity.PromocionEntity;
import cl.bbr.promo.lib.dto.*;
//import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package cl.bbr.promo.lib.helpers:
//            ReglaMultipack3

public class Test
{

    public Test()
    {
    }

    public static void main(String args[])
    {
        int local = 1;
        int canal = 1;
        List tcp = new ArrayList();
        TcpClienteDTO tcpDto = new TcpClienteDTO();
        tcpDto.setTcp(1234);
        tcpDto.setMax(3);
        tcp.add(tcpDto);
        PromocionEntity promo1 = new PromocionEntity();
        promo1.setCodigo(1);
        promo1.setTipo(1);
        promo1.setDescripcion("ds_sc_por2");
        promo1.setBenef1Monto(500D);
        promo1.setCanal(canal);
        PromocionEntity promo2 = new PromocionEntity();
        promo2.setCodigo(2);
        promo2.setTipo(2);
        promo2.setDescripcion("ds_sc_pf_pes");
        promo2.setBenef1Monto(1500D);
        promo2.setCanal(canal);
        PromocionEntity promo444 = new PromocionEntity();
        promo444.setCodigo(444);
        promo444.setTipo(3);
        promo444.setMinCantidad(1);
        promo444.setDescripcion("DCTO CON COND");
        promo444.setBenef1TCP(1234);
        promo444.setBenef1Monto(13D);
        promo444.setCanal(canal);
        PromocionEntity promo11 = new PromocionEntity();
        promo11.setCodigo(11);
        promo11.setTipo(11);
        promo11.setDescripcion("PACK MxN");
        promo11.setBenef1Monto(301D);
        promo11.setCanal(canal);
        PromocionEntity promo12 = new PromocionEntity();
        promo12.setCodigo(12);
        promo12.setTipo(12);
        promo12.setDescripcion("MULTIPACK1");
        promo12.setMinCantidad(3);
        promo12.setBenef1Monto(1090D);
        promo12.setCanal(canal);
        PromocionEntity promo1051 = new PromocionEntity();
        promo1051.setCodigo(1051);
        promo1051.setTipo(100);
        promo1051.setMinCantidad(1);
        promo1051.setFlagProrrateo(1);
        PromocionEntity promo1050 = new PromocionEntity();
        promo1050.setCodigo(1050);
        promo1050.setTipo(105);
        promo1050.setDescripcion("MULTIPACK2");
        promo1050.setMinCantidad(1);
        promo1050.setBenef1Monto(500D);
        promo1050.setCondicion1(1051);
        promo1050.setCanal(canal);
        promo1050.setFlagProrrateo(1);
        PromocionEntity promo2052 = new PromocionEntity();
        promo2052.setCodigo(2052);
        promo2052.setTipo(200);
        promo2052.setMinCantidad(1);
        promo2052.setFlagProrrateo(1);
        PromocionEntity promo2051 = new PromocionEntity();
        promo2051.setCodigo(2051);
        promo2051.setTipo(200);
        promo2051.setMinCantidad(3);
        promo2051.setCondicion1(2052);
        promo2051.setFlagProrrateo(1);
        PromocionEntity promo2050 = new PromocionEntity();
        promo2050.setCodigo(2050);
        promo2050.setTipo(205);
        promo2050.setDescripcion("MULTIPACK3");
        promo2050.setMinCantidad(2);
        promo2050.setBenef1Monto(1000D);
        promo2050.setCondicion1(2051);
        promo2050.setCanal(canal);
        promo2050.setFlagProrrateo(1);
        PromocionEntity promo1021 = new PromocionEntity();
        promo1021.setCodigo(1021);
        promo1021.setTipo(101);
        promo1021.setMinMonto(1000L);
        promo1021.setFlagProrrateo(1);
        PromocionEntity promo1020 = new PromocionEntity();
        promo1020.setCodigo(1020);
        promo1020.setTipo(102);
        promo1020.setDescripcion("PROMO SIMPLE");
        promo1020.setBenef1Monto(100D);
        promo1020.setCondicion1(1021);
        promo1020.setCanal(canal);
        promo1020.setFlagProrrateo(1);
        PromocionEntity promo907 = new PromocionEntity();
        promo907.setCodigo(907);
        promo907.setTipo(907);
        promo907.setDescripcion("DCTO SECCION");
        promo907.setBenef1Monto(100D);
        promo907.setCanal(canal);
        List prodBenef = new ArrayList();
        List prodCond1 = new ArrayList();
        List prodCond2 = new ArrayList();
        ProductoBenConDTO pro = new ProductoBenConDTO(0x19debd01c7L, 99, 'P', 1, 349L);
        prodBenef.add(pro);
        pro = new ProductoBenConDTO(0x19debd01c7L, 99, 'P', 1, 349L);
        prodBenef.add(pro);
        pro = new ProductoBenConDTO(0x19debd01c7L, 99, 'P', 1, 349L);
        prodBenef.add(pro);
        pro = new ProductoBenConDTO(0x19debd01c7L, 99, 'P', 1, 349L);
        prodBenef.add(pro);
        pro = new ProductoBenConDTO(0x19debd01c7L, 99, 'P', 1, 349L);
        prodBenef.add(pro);
        pro = new ProductoBenConDTO(0x19debd01c7L, 99, 'P', 1, 349L);
        prodBenef.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x33bd7a038eL, 99, 'P', 1, 349L);
        prodCond1.add(pro);
        pro = new ProductoBenConDTO(0x4d9c370555L, 99, 'C', 1, 349L);
        prodCond2.add(pro);
        pro = new ProductoBenConDTO(0x4d9c370555L, 99, 'C', 1, 349L);
        prodCond2.add(pro);
        pro = new ProductoBenConDTO(0x4d9c370555L, 99, 'C', 1, 349L);
        prodCond2.add(pro);
        List secciones = new ArrayList();
        SeccionDTO sec = new SeccionDTO();
        sec.setSeccion(20);
        sec.setMonto(750L);
        secciones.add(sec);
        sec = new SeccionDTO();
        sec.setSeccion(30);
        sec.setMonto(450L);
        secciones.add(sec);
        ReglaMultipack3 pack3 = new ReglaMultipack3();
        pack3.initReglaPromocion(promo2050, promo2051, promo2052, prodBenef, prodCond1, prodCond2);
        ProrrateoPromocionProductoDTO pp_promo = pack3.calculaDcto(0, 0, tcp, canal, local);
        if(pp_promo == null)
        {
            System.out.println("NO HAY DCTO PRODUCTO!!");
        } else
        {
            System.out.println("---------------------------------------------------------");
            System.out.println("promo=" + pp_promo.getCodigo() + " | desc=" + pp_promo.getDescripcion() + " | dcto=" + pp_promo.getDescuento() + " | productos=" + pp_promo.getListadoProductos().size());
            for(int i = 0; i < pp_promo.getListadoProductos().size(); i++)
            {
                ProrrateoProductoDTO p_pro = (ProrrateoProductoDTO)pp_promo.getListadoProductos().get(i);
                System.out.println("[" + i + "]cod=" + p_pro.getCodigo() + "|precio=" + p_pro.getPrecio() + "|dcto=" + p_pro.getProrrateo() + "  ");
            }

            System.out.print("");
            System.out.println("---------------------------------------------------------");
        }
        System.exit(0);
    }
}