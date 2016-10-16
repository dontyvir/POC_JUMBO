// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 28/09/2009 17:20:12
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Tcp.java

package cl.bbr.promo.lib.helpers;

import cl.bbr.promo.lib.dto.TcpClienteDTO;
import java.util.List;

public class Tcp
{

    public Tcp()
    {
    }

    public boolean validaTCP(List tcpCliente, int tcpPromo)
    {
        if(tcpCliente == null)
            return false;
        try
        {
            for(int i = 0; i < tcpCliente.size(); i++)
            {
                TcpClienteDTO tcp = (TcpClienteDTO)tcpCliente.get(i);
                if(tcp.getTcp() == tcpPromo)
                    return true;
            }

        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }
        return false;
    }

    public int getCantidadTCP(List tcpCliente, int tcpPromo)
    {
        if(tcpCliente == null)
            return 0;
        try
        {
            for(int i = 0; i < tcpCliente.size(); i++)
            {
                TcpClienteDTO tcp = (TcpClienteDTO)tcpCliente.get(i);
                if(tcp.getTcp() == tcpPromo)
                    return tcp.getMax() - tcp.getCant();
            }

        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }
        return 0;
    }

    public List quemaTCP(List tcpCliente, int tcpPromo)
    {
        if(tcpCliente == null)
            return null;
        List listado = tcpCliente;
        try
        {
            for(int i = 0; i < listado.size(); i++)
            {
                TcpClienteDTO tcp = (TcpClienteDTO)listado.get(i);
                if(tcp.getTcp() != tcpPromo)
                    continue;
                int cant = tcp.getCant() + 1;
                tcp.setCant(cant);
                listado.set(i, tcp);
                break;
            }

        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }
        return listado;
    }
}