package cl.cencosud.jumbo.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import cl.bbr.jumbocl.parametros.dto.ParametroDTO;
import cl.bbr.jumbocl.shared.log.Logging;
import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.bizdelegate.BizDelegatePayment;
import cl.cencosud.jumbo.exceptions.GrabilityException;

/**
 * Servlet Filter implementation class RefererFilter
 */
public class RefererFilter implements Filter {

	 private ServletContext context;
	 private boolean enable = false;
	 private String headerName ="X-authorized";
	 Logging logger = new Logging(this);
	 
	/**
	 * Default constructor.
	 */
	public RefererFilter() {
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {

		logger.info("RefererFilter init() \n");
		this.context = fConfig.getServletContext();
		this.enable  = StringUtils.equals("1", fConfig.getInitParameter("enable"));
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		logger.info("RefererFilter destroy() \n");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest sreq, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		if (this.enable) {
			
			CustomHttpServletRequest request = new CustomHttpServletRequest((HttpServletRequest)sreq);
			
			//HttpServletRequest httpRequest = (HttpServletRequest) request;
				 
			logger.info("AUTHORIZED-RemoteAddr : "+request.getRemoteAddr());
			logger.info("RemoteHost : "+request.getRemoteHost());		 
			logger.info("ContentType : "+request.getContentType());
			logger.info("CharacterEncoding : "+request.getCharacterEncoding());
			logger.info("ContentLength : "+request.getContentLength());
			logger.info("ContextPath : "+request.getContextPath());
			logger.info("LocalAddr : "+request.getLocalAddr());
			logger.info("LocalName : "+request.getLocalName());
			logger.info("LocalPort : "+request.getLocalPort());
			logger.info("Method : "+request.getMethod());
			logger.info("PathInfo : "+request.getPathInfo());
			logger.info("PathTranslated : "+request.getPathTranslated());
			logger.info("Protocol : "+request.getProtocol());
			logger.info("QueryString : "+request.getQueryString());
			logger.info("RemotePort : "+request.getRemotePort());
			logger.info("RemoteUser : "+request.getRemoteUser());
			logger.info("RequestURI : "+request.getRequestURI());
			logger.info("Scheme : "+request.getScheme());
			logger.info("ServerName : "+request.getServerName());
			logger.info("ServerPort : "+request.getServerPort());
			logger.info("ServletPath : "+request.getServletPath());
			logger.info("Class : "+request.getClass());
			logger.info("Locale : "+request.getLocale());
			logger.info("RequestURL : "+request.getRequestURL());
			 
			 Enumeration headers = request.getHeaderNames();			 
	         while (headers.hasMoreElements()) {
	             String header = (String) headers.nextElement();
	             logger.info(header + " : " + request.getHeader(header));
	         }
			
						
			BizDelegatePayment biz = new BizDelegatePayment();
			ParametroDTO oRarametro = null;
			try {
				oRarametro = biz.getParametroByName(Constant.IP_REFERER_REQUEST);
			} catch (GrabilityException e) {
				// TODO Bloque catch generado automáticamente
				e.printStackTrace();
			}
			if(oRarametro == null){
				request.addHeader(headerName, "Not-Compared");
				logger.info("x-AUTHORIZED-RefererFilter : IP_REFERER_PARAM_NULL \n");
				chain.doFilter(request, response);
			}else{
			
				if(StringUtils.equals(oRarametro.getValor(), "*")){
					request.addHeader(headerName, "*");
					logger.info("x-AUTHORIZED-RefererFilter : * \n");
					chain.doFilter(request, response);
				}else{

					boolean isValidIP = false;
					String ip = "";
					
					if(StringUtils.contains(oRarametro.getValor(), ",")){
						String[] ips = StringUtils.split(oRarametro.getValor(), ","); 
						for(int i=0; i < ips.length; i++){
							if(StringUtils.equals(ips[i],request.getRemoteAddr())){
								ip = request.getRemoteAddr();
								isValidIP = true;
								break;
							}
						}
					}else{
						ip = oRarametro.getValor();
						isValidIP = (StringUtils.equals(request.getRemoteAddr(), ip));
					}
					
					
					if (!isValidIP) {
						HttpServletResponse httpResponse = (HttpServletResponse) response;
						httpResponse.addHeader(headerName, "none");
						httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						logger.info("NOT x-AUTHORIZED-RefererFilter : ip: "+ request.getRemoteAddr()+ " "+request.getRemoteHost()+"\n");
						return;
					} else {
						request.addHeader(headerName, ip);
						logger.info("x-AUTHORIZED-RefererFilter : ip"+ip+"\n");						
						chain.doFilter(request, response);
					}				
				}
			}
			
		} else {
			// pass the request along the filter chain
			chain.doFilter(sreq, response);
		}

	}

}
