// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:23:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Registro1Wrap.java

package cl.bbr.promo.carga.helper;

import cl.bbr.promo.carga.dto.Registro1DTO;
import java.text.SimpleDateFormat;

public class Registro1Wrap
{

    public Registro1Wrap()
    {
    }

    public static Registro1DTO wrap(String reg)
    {
        Registro1DTO dto = new Registro1DTO();
        try
        {
            if(reg.length() != 248)
            {
                return null;
            } else
            {
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                dto.setTipoReg(Integer.parseInt(reg.substring(0, 1)));
                dto.setCodigo(Integer.parseInt(reg.substring(1, 7)));
                dto.setTipoPromo(Integer.parseInt(reg.substring(7, 10)));
                dto.setVersion(Integer.parseInt(reg.substring(10, 11)));
                java.util.Date fecha = df.parse("20" + reg.substring(11, 21) + "00");
                dto.setInicio(fecha);
                fecha = df.parse("20" + reg.substring(21, 31) + "00");
                dto.setTermino(fecha);
                dto.setDescriptor(reg.substring(31, 45));
                dto.setMinCantidad(Integer.parseInt(reg.substring(45, 49)));
                dto.setMinMonto(Integer.parseInt(reg.substring(49, 57)));
                dto.setTramo1Monto(Long.parseLong(reg.substring(57, 65)));
                dto.setTramo1Dcto(Integer.parseInt(reg.substring(65, 69)));
                dto.setTramo2Monto(Long.parseLong(reg.substring(69, 77)));
                dto.setTramo2Dcto(Integer.parseInt(reg.substring(77, 81)));
                dto.setTramo3Monto(Long.parseLong(reg.substring(81, 89)));
                dto.setTramo3Dcto(Integer.parseInt(reg.substring(89, 93)));
                dto.setTramo4Monto(Long.parseLong(reg.substring(93, 101)));
                dto.setTramo4Dcto(Integer.parseInt(reg.substring(101, 105)));
                dto.setTramo5Monto(Long.parseLong(reg.substring(105, 113)));
                dto.setTramo5Dcto(Integer.parseInt(reg.substring(113, 117)));
                dto.setBenef1Fpago(Integer.parseInt(reg.substring(117, 119)));
                dto.setBenef1Cuotas(Integer.parseInt(reg.substring(119, 121)));
                dto.setBenef1TCP(Integer.parseInt(reg.substring(121, 125)));
                dto.setBenef1Monto(Integer.parseInt(reg.substring(125, 133)));
                dto.setBenef2Fpago(Integer.parseInt(reg.substring(133, 135)));
                dto.setBenef2Cuotas(Integer.parseInt(reg.substring(135, 137)));
                dto.setBenef2TCP(Integer.parseInt(reg.substring(137, 141)));
                dto.setBenef2Monto(Long.parseLong(reg.substring(141, 149)));
                dto.setBenef3Fpago(Integer.parseInt(reg.substring(149, 151)));
                dto.setBenef3Cuotas(Integer.parseInt(reg.substring(151, 153)));
                dto.setBenef3TCP(Integer.parseInt(reg.substring(153, 157)));
                dto.setBenef3Monto(Long.parseLong(reg.substring(157, 165)));
                dto.setCondicion1(Integer.parseInt(reg.substring(165, 171)));
                dto.setCondicion2(Integer.parseInt(reg.substring(171, 177)));
                dto.setCondicion3(Integer.parseInt(reg.substring(177, 183)));
                dto.setFlagProrrateo(Integer.parseInt(reg.substring(183, 186)));
                dto.setFlagRecuperable(Integer.parseInt(reg.substring(186, 187)));
                dto.setCanal(Integer.parseInt(reg.substring(187, 189)));
                return dto;
            }
        }
        catch(Exception e)
        {
            return null;
        }
    }
}