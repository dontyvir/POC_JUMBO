package cl.bbr.boc.view;

	import java.util.ArrayList;
	import java.util.List;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;

	import net.sf.fastm.ITemplate;
	import net.sf.fastm.IValueSet;
	import net.sf.fastm.TemplateLoader;
	import net.sf.fastm.ValueSet;
	import cl.bbr.boc.service.CategoriasSevice;
	import cl.bbr.common.framework.Command;
	import cl.bbr.common.framework.View;
	import cl.bbr.jumbocl.usuarios.dto.UserDTO;
	import cl.jumbo.common.dto.CategoriaBannerDTO;
	/**
	 * formulario de ingreso de una categoría para administrar sus banners en apartado minihome
	 * @author carriagada
	 */
	public class ViewBannersCatWebNewForm extends Command{
		
		protected void Execute(HttpServletRequest req, HttpServletResponse res, UserDTO usr) throws Exception {
			View salida = new View(res);
			
			logger.debug("User: " + usr.getLogin());
					
			// Recupera pagina desde web.xml
			String html = getServletConfig().getInitParameter("TplFile");
			// le aasignamos el prefijo con la ruta
			html = path_html + html;
			logger.debug("Template: " + html);
			
			//3. Template	
			TemplateLoader load = new TemplateLoader(html);
			ITemplate tem = load.getTemplate();
			IValueSet top = new ValueSet();

			CategoriasSevice categoriasSevice = new CategoriasSevice();
            List categorias = categoriasSevice.getCategoriasParaMiniHome();

            List vistaCategorias = vistaCategorias(categorias);

            top.setDynamicValueSets("CATEGORIAS", vistaCategorias);
			
            String result = tem.toString(top);
            salida.setHtmlOut(result);
            salida.Output();
		    	
		}
        
        /**
            * @param categorias
            * @return
            */
           private List vistaCategorias(List categorias) {
              List lista = new ArrayList();
              for (int i = 0; i < categorias.size(); i++) {
                 CategoriaBannerDTO dto = (CategoriaBannerDTO) categorias.get(i);
                 IValueSet cat = new ValueSet();
                 cat.setVariable("{cat_id}", dto.getId() + "");
                 cat.setVariable("{cat_nombre}", dto.getNombre());
                 lista.add(cat);
              }
              return lista;
           }        
        
}
