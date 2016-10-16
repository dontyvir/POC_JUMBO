/*
 * Created on 05-11-2012
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cl.bbr.boc.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cl.bbr.boc.bizdelegate.BizDelegate;
import cl.bbr.boc.exceptions.BocException;
import cl.bbr.common.framework.Command;
import cl.bbr.common.framework.ForwardParameters;
import cl.bbr.jumbocl.common.codes.Constantes;
import cl.bbr.jumbocl.common.exceptions.ParametroObligatorioException;
import cl.bbr.jumbocl.contenidos.collaboration.ProcDelAllProductCategory;
import cl.bbr.jumbocl.usuarios.dto.UserDTO;

/**
 * @author Administrador
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ModSupAllProductCat extends Command {
    protected void Execute(HttpServletRequest req, HttpServletResponse res,
            UserDTO usr) throws Exception {
        if (req.getParameter("id_cat") == null) {
            throw new ParametroObligatorioException("id_cat es nulo");
        }
        if (req.getParameter("url") == null) {
            throw new ParametroObligatorioException("url es nulo");
        }
        if (req.getParameter("origen") == null) {
            throw new ParametroObligatorioException("origen es nulo");
        }

        long paramId_categoria = Long.parseLong(req.getParameter("id_cat"));
        String paramUrl = req.getParameter("url");
        String paramOrigen = req.getParameter("origen");
        String mensDesa = getServletConfig().getInitParameter("MensDesa");

        BizDelegate biz = new BizDelegate();
        ForwardParameters fp = new ForwardParameters();
        fp.add(req.getParameterMap());

        String UrlError = "";
        if (paramOrigen.equals("CAT"))
            UrlError = getServletConfig().getInitParameter("UrlErrorCat");
        if (paramOrigen.equals("PROD"))
            UrlError = getServletConfig().getInitParameter("UrlErrorProd");

        try {
            ProcDelAllProductCategory param = new ProcDelAllProductCategory(
                    paramId_categoria, usr.getLogin(), mensDesa);
            boolean result = biz.setDelAllProductCategory(param);
            logger.debug("desasociar:" + result);

            List lst_pro = biz.getProductosByCategId(paramId_categoria);
            logger.debug("categorias relacionadas:" + lst_pro.size());

//            if (lst_pro.size() == 0) {
//                logger.debug("Se eliminaron todos los productos.");
//                fp.add("rc", Constantes._EX_PROD_SIN_CAT);
//                paramUrl = UrlError + fp.forward();
//                logger.debug("paramUrl:" + paramUrl);
//            }
        } catch (BocException e) {
            logger.debug("Controlando excepción del AddProdcatweb: "
                    + e.getMessage());
            if (e.getMessage().equals(Constantes._EX_PROD_ID_NO_EXISTE)) {
                logger.debug("El código de producto ingresado no existe");
                fp.add("rc", Constantes._EX_PROD_ID_NO_EXISTE);
                paramUrl = UrlError + fp.forward();
            }
            if (e.getMessage().equals(Constantes._EX_CAT_ID_NO_EXISTE)) {
                logger.debug("El código de la categoria no existe");
                fp.add("rc", Constantes._EX_CAT_ID_NO_EXISTE);
                paramUrl = UrlError + fp.forward();
            }
            if (e.getMessage().equals(Constantes._EX_CAT_PROD_REL_NO_EXISTE)) {
                logger
                        .debug("La relación entre categoría y producto no existen.No se puede eliminar.");
                fp.add("rc", Constantes._EX_CAT_PROD_REL_NO_EXISTE);
                paramUrl = UrlError + fp.forward();
            } else {
                logger.debug("Controlando excepción: " + e.getMessage());
            }
        }
        res.sendRedirect(paramUrl);
    }
}
