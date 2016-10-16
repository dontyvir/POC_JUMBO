package cl.jumbo.ws.sustitucion.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.SystemException;
import cl.bbr.jumbocl.pedidos.dto.TPAuditSustitucionDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.jumbo.ws.sustitucion.bizdelegate.BizDelegate;
import cl.jumbo.ws.sustitucion.exceptions.WsException;


/**
 * Realiza el procesamiento de la carga y descarga de rondas con un usuario autentificado.
 */
public class Controlador {

    private Logging logger = new Logging(this);
	
	/**
	 * Constructor
	 */
	public Controlador(){
	}



    /**
     * Método de carga de ronda. Utiliza los métodos de negocio.
     * @param id_ronda
     * @param login
     * @param password
     * @throws WsException
     * @throws SystemException
     * @throws WsException
     */
    public List getDetalleProdSustitutos(Document DocListBarSust, String id_ronda)
        throws WsException, SystemException{
        
        
        //Document DocListBarSust = dto.getBarraSustitutosXML();
        
        // Creamos al BizDelegate
        BizDelegate biz = new BizDelegate();
        
        /*String xml = "<CodBarras>"
                    + "   <barra>7803473000767</barra>"
                    + "   <barra>24021043</barra>"
                    + "</CodBarras>";*/
        /*String xml5 = "<AuditoriaSustitutos>"
            + "  <CodBarras>"
            + "    <barra>7613033130526</barra>"
            + "  </CodBarras>"
            + "  <CodBarras>"
            + "    <barra>7802810005519</barra>"
            + "  </CodBarras>"
            + "</AuditoriaSustitutos>";*/


//      Crear Bins******************
        List lst_barras = new ArrayList();
        
        //Se extraen los valores de las tabla BinOp de descarga
        Element raizBarSust = DocListBarSust.getRootElement();
        List nBarSust =  raizBarSust.getChildren("CodBarras");
        Iterator it = nBarSust.iterator();
        
        //USAR XSTREAN PARA PARSEAR EL XML
        
        logger.debug("\n\n  Barras Auditoria de Sustitucion: ");
        int j=1;
        while (it.hasNext()){
            
            Element i = (Element)it.next();
            lst_barras.add(i.getChild("barra").getText());
            logger.debug("\n" + i.getChild("barra").getText());
            System.out.println("Barra N°" + j + ": " + i.getChild("barra").getText());
            
            //lst_barras.add(i.getText());
            //System.out.println("Barra N°" + j + ": " + i.getText());
            //logger.debug("\n" + i.getText());
            j++;
        }
        
        // obtenemos los codigos de barra utilizados en la sustitucion
        List lstBarrasSust = biz.getBarrasAuditoriaSustitucion(lst_barras, Long.parseLong(id_ronda));
        
        logger.debug("ok Ctrl getDetalleProdSustitutos");
        return lstBarrasSust;
    }

    

    /**
     * Metodo para la descarga de rondas
     * @param dtoDescarga
     * @return mensaje
     * @throws SystemException
     * @throws WsException 
     * @throws WsException
     */
    public String doDescargaAuditoriaSustitucion(Document docAudSust)       
        throws SystemException, WsException{
    
        int id_ronda = 0;

        System.out.println("docAudSust: " + docAudSust.toString());

        List lst_AudSust = new ArrayList();
        TPAuditSustitucionDTO AudSust;
        
        Element raizAudSust = docAudSust.getRootElement();
        List nAudSust =  raizAudSust.getChildren("auditoria_sustitucion");
        Iterator it = nAudSust.iterator();
            
        while (it.hasNext()){
            AudSust = new TPAuditSustitucionDTO();
            Element i = (Element)it.next();
            AudSust.setId(Integer.parseInt(i.getChild("id").getText()));
            AudSust.setId_auditoria(Integer.parseInt(i.getChild("id_auditoria").getText()));
            AudSust.setId_ronda(Integer.parseInt(i.getChild("id_ronda").getText()));
            AudSust.setId_dronda(Integer.parseInt(i.getChild("id_dronda").getText()));
            AudSust.setId_pedido(Integer.parseInt(i.getChild("id_pedido").getText()));
            AudSust.setId_detalle(Integer.parseInt(i.getChild("id_detalle").getText()));
            AudSust.setId_dpicking(Integer.parseInt(i.getChild("id_dpicking").getText()));
            AudSust.setId_usuario(Integer.parseInt(i.getChild("id_usuario").getText()));
            AudSust.setCod_barra_orig(i.getChild("cod_barra_orig").getText());
            AudSust.setCod_barra_sust(i.getChild("cod_barra_sust").getText());
            AudSust.setCantidad_sust(Integer.parseInt(i.getChild("cantidad_sust").getText()));
            AudSust.setPrecio_sust(Double.parseDouble(i.getChild("precio_sust").getText()));
            AudSust.setUnid_med_sust(i.getChild("unid_med_sust").getText());
            AudSust.setAccion(i.getChild("accion").getText());
            lst_AudSust.add(AudSust);

            if (id_ronda == 0){
                id_ronda = Integer.parseInt(i.getChild("id_ronda").getText());
            }
            String datos = "\n\n Descarga auditoria_sustitucion "
                         + "\n =============================="
                         + "\n id            : " + i.getChild("id").getText()
                         + "\n id_auditoria  : " + i.getChild("id_auditoria").getText()
                         + "\n id_ronda      : " + i.getChild("id_ronda").getText()
                         + "\n id_dronda     : " + i.getChild("id_dronda").getText()
                         + "\n id_pedido     : " + i.getChild("id_pedido").getText()
                         + "\n id_detalle    : " + i.getChild("id_detalle").getText()
                         + "\n id_dpicking   : " + i.getChild("id_dpicking").getText()
                         + "\n id_usuario    : " + i.getChild("id_usuario").getText()
                         + "\n cod_barra_orig: " + i.getChild("cod_barra_orig").getText()
                         + "\n cod_barra_sust: " + i.getChild("cod_barra_sust").getText()
                         + "\n cantidad_sust : " + i.getChild("cantidad_sust").getText()
                         + "\n precio_sust   : " + i.getChild("precio_sust").getText()
                         + "\n unid_med_sust : " + i.getChild("unid_med_sust").getText()
                         + "\n accion        : " + i.getChild("accion").getText();
            System.out.println(datos);
            logger.debug(datos);
        }
            
        System.out.println("Construyo la Auditoria de Sustitucion");
            
        
        // Creamos al BizDelegate
        BizDelegate biz = new BizDelegate();
        
        try {
            if (lst_AudSust.size() > 0){
                biz.recepcionAuditoriaSustitucion(lst_AudSust);
                biz.marcaRondaAuditoriaSustitucion(id_ronda);
            }
        } catch (WsException e) {
            if ( e.getMessage().equals(Constantes._EX_RON_ID_INVALIDO) ){
                throw new WsException("WSE001"); //ronda no existe
            }
            else if ( e.getMessage().equals(Constantes._EX_RON_ESTADO_INADECUADO) ){
                throw new WsException("WSE002"); //ronda en estado incorrecto
            }
            else
                throw new WsException("WSE005"); //error no controlado
        } catch (SystemException e) {
            throw new WsException("WSE005"); //error no controlado
        }
        return "Descarga Completada!!";
    }


}
