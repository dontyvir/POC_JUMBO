package cl.bbr.fo.command;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.fastm.ITemplate;
import net.sf.fastm.IValueSet;
import net.sf.fastm.TemplateLoader;
import net.sf.fastm.ValueSet;

import cl.bbr.cupondscto.dto.CarroAbandonadoDTO;
import cl.bbr.fo.bizdelegate.BizDelegate;
import cl.bbr.fo.exception.CommandException;

public class VerCuponCarroAbandonado extends Command  {
	
	private static final String PAG_DCTO = "pag_dcto";
	private static final String PAG_DCHO = "pag_despacho";
	private static final String PAG_RCDA = "pag_recuerda";

	private static final String GOOGLE_RECUERDA = "recuerda";
	private static final String GOOGLE_DESPACHO = "despacho";
	private static final String GOOGLE_SECCION = "seccion_";
	private static final String GOOGLE_RUBRO = "rubro_";
	
	protected void execute(HttpServletRequest req, HttpServletResponse res) throws CommandException {
		try {
	        this.getLogger().debug("Comienzo VerCuponCarroAbandonado");

	     
			//HttpSession session = req.getSession();
			PrintWriter out = res.getWriter();
	        
	        res.setContentType("text/html");
	        res.setHeader("Cache-Control", "no-cache");
	        res.setCharacterEncoding("ISO-8859-1");

			ResourceBundle rb = ResourceBundle.getBundle("fo");
			//TODO
			BizDelegate biz = new BizDelegate();
			CarroAbandonadoDTO carroAbandonado = new CarroAbandonadoDTO();
			String google = "";
			String key = req.getParameter("key");
			key = key.replaceAll("_", "=");
			key = new String(b64decode(key));
			int id_Mail = Integer.parseInt(key); 

			carroAbandonado = biz.getCuponCarroAbandonado(id_Mail);
			
			String car_tipo_dcto = carroAbandonado.getCar_tipo_dcto();
			String pag_form = "";
			
			// Recupera pagina desde web.xml
			if (car_tipo_dcto.equalsIgnoreCase("S")){
				pag_form = rb.getString("conf.apache.dir.html") + "" + getServletConfig().getInitParameter(PAG_DCTO);
				google = GOOGLE_SECCION+fixCaracteres(carroAbandonado.getCar_nom_seccion().toLowerCase());
			}else if (car_tipo_dcto.equalsIgnoreCase("R")){
				pag_form = rb.getString("conf.apache.dir.html") + "" + getServletConfig().getInitParameter(PAG_DCTO);
				google = GOOGLE_RUBRO+fixCaracteres(carroAbandonado.getCar_nom_seccion().toLowerCase());
			}else if (car_tipo_dcto.equalsIgnoreCase("D")){
				pag_form = rb.getString("conf.apache.dir.html") + "" + getServletConfig().getInitParameter(PAG_DCHO);
				google = GOOGLE_DESPACHO;
			}
			else {
				pag_form = rb.getString("conf.apache.dir.html") + "" + getServletConfig().getInitParameter(PAG_RCDA);
				google = GOOGLE_RECUERDA;
			}
			
			this.getLogger().debug( "Template:"+pag_form );
			TemplateLoader load = new TemplateLoader(pag_form);
			ITemplate tem = load.getTemplate();

	        IValueSet top = new ValueSet();
	        
	        //formato fecha cupon carro abandonado
	        
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat salida = new SimpleDateFormat("dd-MM-yyyy");
	         
			String car_fecha_fin="";
//			if ((carroAbandonado.getCar_fecha_fin()==null) || (carroAbandonado.getCar_fecha_fin().equalsIgnoreCase("null")))
			if ((carroAbandonado.getCar_fecha_fin().equalsIgnoreCase("null") || (carroAbandonado.getCar_fecha_fin()==null)))
			 		car_fecha_fin=""; 
			 		
			else{
				
				// se cambia el formato de la fecha a dd-MM-yyyy
				try {
						car_fecha_fin = salida.format(format.parse(carroAbandonado.getCar_fecha_fin()));
					} catch (ParseException e1) {
				// TODO Bloque catch generado automáticamente
						e1.printStackTrace();
					}
				
			}
			//---------------------------Fin Cambio formato fecha
	        
	        top.setVariable("{nombre}", carroAbandonado.getCar_cli_nombres());
	        top.setVariable("{fecha}", car_fecha_fin);
	        top.setVariable("{visitamail}", "#");
	        top.setVariable("{dcto}", carroAbandonado.getCar_monto_dcto());
	        top.setVariable("{categoria}", carroAbandonado.getCar_nom_seccion());
	        top.setVariable("{cupon}", carroAbandonado.getCar_cupon());
	        top.setVariable("{google}", google);
	        
			//TODO
	        String result = tem.toString(top);
			out.print(result);
			
		} catch (Exception e) {
			this.getLogger().error(e);
			throw new CommandException( e );
		}

	}
	 
	private byte[] b64decode(String s) throws MessagingException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
		InputStream b64is = MimeUtility.decode(bais, "Base64");
		byte[] tmp = new byte[s.length()];
		int n = b64is.read(tmp);
		byte[] res = new byte[n];
		System.arraycopy(tmp, 0, res, 0, n);
		return res;
	}

	private String fixCaracteres(String s){
		String retorno="";
		
		retorno = s.replaceAll(" ", "_");
		retorno = retorno.replaceAll("á", "a");
		retorno = retorno.replaceAll("é", "e");
		retorno = retorno.replaceAll("í", "i");
		retorno = retorno.replaceAll("ó", "o");
		retorno = retorno.replaceAll("ú", "u");

		return retorno;
	}

}
