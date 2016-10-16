package cl.cencosud.jumbo.util;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.ConstructorUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

import cl.cencosud.jumbo.Constant.Constant;
import cl.cencosud.jumbo.exceptions.ExceptionInParam;
import cl.cencosud.jumbo.exceptions.GrabilityException;
/**
 * <p>General purpose utility methods related to processing a servlet request
 * in the Struts controller framework.</p>
 *
 */
public class RequestUtils {
    // ------------------------------------------------------- Static Variables

    /**
     * <p>Commons Logging instance.</p>
     */
    //protected static Log log = LogFactory.getLog(RequestUtils.class);

    // --------------------------------------------------------- Public Methods

    /**
     * <p>Create and return an absolute URL for the specified context-relative
     * path, based on the server and context information in the specified
     * request.</p>
     *
     * @param request The servlet request we are processing
     * @param path    The context-relative path (must start with '/')
     * @return absolute URL based on context-relative path
     * @throws MalformedURLException if we cannot create an absolute URL
     */
    public static URL absoluteURL(HttpServletRequest request, String path)
        throws MalformedURLException {
        return (new URL(serverURL(request), request.getContextPath() + path));
    }

    /**
     * <p>Return the <code>Class</code> object for the specified fully
     * qualified class name, from this web application's class loader.</p>
     *
     * @param className Fully qualified class name to be loaded
     * @return Class object
     * @throws ClassNotFoundException if the class cannot be found
     */
    public static Class applicationClass(String className)
        throws ClassNotFoundException {
        return applicationClass(className, null);
    }

    /**
     * <p>Return the <code>Class</code> object for the specified fully
     * qualified class name, from this web application's class loader.</p>
     *
     * @param className   Fully qualified class name to be loaded
     * @param classLoader The desired classloader to use
     * @return Class object
     * @throws ClassNotFoundException if the class cannot be found
     */
    public static Class applicationClass(String className,
        ClassLoader classLoader)
        throws ClassNotFoundException {
        if (classLoader == null) {
            // Look up the class loader to be used
            classLoader = Thread.currentThread().getContextClassLoader();

            if (classLoader == null) {
                classLoader = RequestUtils.class.getClassLoader();
            }
        }

        // Attempt to load the specified class
        return (classLoader.loadClass(className));
    }

    /**
     * <p>Return a new instance of the specified fully qualified class name,
     * after loading the class from this web application's class loader. The
     * specified class <strong>MUST</strong> have a public zero-arguments
     * constructor.</p>
     *
     * @param className Fully qualified class name to use
     * @return new instance of class
     * @throws ClassNotFoundException if the class cannot be found
     * @throws IllegalAccessException if the class or its constructor is not
     *                                accessible
     * @throws InstantiationException if this class represents an abstract
     *                                class, an interface, an array class, a
     *                                primitive type, or void
     * @throws InstantiationException if this class has no zero-arguments
     *                                constructor
     */
    public static Object applicationInstance(String className)
        throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        return applicationInstance(className, null);
    }

    /**
     * <p>Return a new instance of the specified fully qualified class name,
     * after loading the class from this web application's class loader. The
     * specified class <strong>MUST</strong> have a public zero-arguments
     * constructor.</p>
     *
     * @param className   Fully qualified class name to use
     * @param classLoader The desired classloader to use
     * @return new instance of class
     * @throws ClassNotFoundException if the class cannot be found
     * @throws IllegalAccessException if the class or its constructor is not
     *                                accessible
     * @throws InstantiationException if this class represents an abstract
     *                                class, an interface, an array class, a
     *                                primitive type, or void
     * @throws InstantiationException if this class has no zero-arguments
     *                                constructor
     */
    public static Object applicationInstance(String className,
        ClassLoader classLoader)
        throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        return (applicationClass(className, classLoader).newInstance());
    }


    /**
     * <p>Look up and return current user locale, based on the specified
     * parameters.</p>
     *
     * @param request The request used to lookup the Locale
     * @param locale  Name of the session attribute for our user's Locale.  If
     *                this is <code>null</code>, the default locale key is
     *                used for the lookup.
     * @return current user locale
     * @since Struts 1.2
     */
    public static Locale getUserLocale(HttpServletRequest request, String locale) {
        Locale userLocale = null;
        HttpSession session = request.getSession(false);

        /*if (locale == null) {
            locale = Globals.LOCALE_KEY;
        }*/

        // Only check session if sessions are enabled
        if (session != null) {
            userLocale = (Locale) session.getAttribute(locale);
        }

        if (userLocale == null) {
            // Returns Locale based on Accept-Language header or the server default
            userLocale = request.getLocale();
        }

        return userLocale;
    }


    /**
     * <p>Compute the printable representation of a URL, leaving off the
     * scheme/host/port part if no host is specified. This will typically be
     * the case for URLs that were originally created from relative or
     * context-relative URIs.</p>
     *
     * @param url URL to render in a printable representation
     * @return printable representation of a URL
     */
    public static String printableURL(URL url) {
        if (url.getHost() != null) {
            return (url.toString());
        }

        String file = url.getFile();
        String ref = url.getRef();

        if (ref == null) {
            return (file);
        } else {
            StringBuffer sb = new StringBuffer(file);

            sb.append('#');
            sb.append(ref);

            return (sb.toString());
        }
    }

    /**
     * <p>Return the URL representing the current request. This is equivalent
     * to <code>HttpServletRequest.getRequestURL</code> in Servlet 2.3.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the current request
     * @throws MalformedURLException if a URL cannot be created
     */
    public static URL requestURL(HttpServletRequest request)
        throws MalformedURLException {
        StringBuffer url = requestToServerUriStringBuffer(request);

        return (new URL(url.toString()));
    }

    /**
     * <p>Return the URL representing the scheme, server, and port number of
     * the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the scheme, server, and port number of the
     *         current request
     * @throws MalformedURLException if a URL cannot be created
     */
    public static URL serverURL(HttpServletRequest request)
        throws MalformedURLException {
        StringBuffer url = requestToServerStringBuffer(request);

        return (new URL(url.toString()));
    }

    /**
     * <p>Return the string representing the scheme, server, and port number
     * of the current request. Server-relative URLs can be created by simply
     * appending the server-relative path (starting with '/') to this.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the scheme, server, and port number of the
     *         current request
     * @since Struts 1.2.0
     */
    public static StringBuffer requestToServerUriStringBuffer(
        HttpServletRequest request) {
        StringBuffer serverUri =
            createServerUriStringBuffer(request.getScheme(),
                request.getServerName(), request.getServerPort(),
                request.getRequestURI());

        return serverUri;
    }

    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server,
     * and port number of the current request. Server-relative URLs can be
     * created by simply appending the server-relative path (starting with
     * '/') to this.</p>
     *
     * @param request The servlet request we are processing
     * @return URL representing the scheme, server, and port number of the
     *         current request
     * @since Struts 1.2.0
     */
    public static StringBuffer requestToServerStringBuffer(
        HttpServletRequest request) {
        return createServerStringBuffer(request.getScheme(),
            request.getServerName(), request.getServerPort());
    }

    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server,
     * and port number of the current request.</p>
     *
     * @param scheme The scheme name to use
     * @param server The server name to use
     * @param port   The port value to use
     * @return StringBuffer in the form scheme: server: port
     * @since Struts 1.2.0
     */
    public static StringBuffer createServerStringBuffer(String scheme,
        String server, int port) {
        StringBuffer url = new StringBuffer();

        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }

        url.append(scheme);
        url.append("://");
        url.append(server);

        if ((scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }

        return url;
    }

    /**
     * <p>Return <code>StringBuffer</code> representing the scheme, server,
     * and port number of the current request.</p>
     *
     * @param scheme The scheme name to use
     * @param server The server name to use
     * @param port   The port value to use
     * @param uri    The uri value to use
     * @return StringBuffer in the form scheme: server: port
     * @since Struts 1.2.0
     */
    public static StringBuffer createServerUriStringBuffer(String scheme,
        String server, int port, String uri) {
        StringBuffer serverUri = createServerStringBuffer(scheme, server, port);

        serverUri.append(uri);

        return serverUri;
    }
    
    
    /**
     * Procesa el request y ejecuta el servico correspondiente.
     *
     * @param HttpServletRequest request, HttpServletResponse response, String m
     * @return void.
     */ 
	public static void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding(Constant.ENCODING);
		
		String service = (!StringUtils.isEmpty(request.getServletPath()))? request.getServletPath().replaceAll("/", ""):"";
		String restMethod = StringUtils.capitalise(request.getMethod().toLowerCase());
		String contextPath = request.getContextPath();
		
		String classInput = "cl.cencosud.jumbo.input.dto."+service+"."+restMethod+"Input"+service+"DTO";
		String classCtrl  = "cl.cencosud.jumbo.ctrl.Ctrl"+service;
		
		request.setAttribute("service", service);
		request.setAttribute("restMethod", restMethod.toUpperCase());
		request.setAttribute("contextPath",contextPath);
		request.setAttribute("classInput", classInput);
		request.setAttribute("classCtrl", classCtrl);
		request.setAttribute("requestId", String.valueOf(System.currentTimeMillis()));
				
		try {
			//importa las clases inputDTO y ctrl asociadas al servicio invocado.
			Class clsInput	= ClassUtils.getClass(classInput);
			Class clsCtrl	= ClassUtils.getClass(classCtrl);
					
			Object oInput = ConstructorUtils.invokeConstructor(clsInput, request);
			MethodUtils.invokeExactMethod(oInput, "isValid", null);
			
			Object oCtrl  = ConstructorUtils.invokeConstructor(clsCtrl, oInput);
			Object output = MethodUtils.invokeExactMethod(oCtrl, restMethod.toLowerCase()+service, null);
			
			ResponseUtils.printStringJson(response, request, MethodUtils.invokeExactMethod(output, "toJson", null).toString(), null);
			clsInput = null;
			clsCtrl	= null;
			oInput = null;
			oCtrl = null;
			output = null;
			
		} catch (ClassNotFoundException e) {
			
			request.setAttribute("status", Constant.SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			request.setAttribute("error_message", Constant.MSG_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			ResponseUtils.printError(request, response, e);
			
		} catch (NoSuchMethodException e) {
			
			request.setAttribute("status", Constant.SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			request.setAttribute("error_message", Constant.MSG_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			ResponseUtils.printError(request, response, e);
			
		} catch (IllegalAccessException e) {
			
			request.setAttribute("status", Constant.SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			request.setAttribute("error_message", Constant.MSG_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			ResponseUtils.printError(request, response, e);
						
		} catch (InstantiationException e) {
			
			request.setAttribute("status", Constant.SC_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			request.setAttribute("error_message", Constant.MSG_NO_SE_ENCONTRO_CONF_NOMBRE_SERVICIO);
			ResponseUtils.printError(request, response, e);	
			
		} catch (InvocationTargetException e) {
						
			try{
				GrabilityException oGrabilityException = (GrabilityException) e.getTargetException(); //fillInStackTrace();
				request.setAttribute("status", oGrabilityException.getCodError());
				request.setAttribute("error_message", oGrabilityException.getMsjError());
				ResponseUtils.printError(request, response, oGrabilityException);//cuando se creo la excepcion se loggeo.
				return;
				
			} catch(Exception eg){}
	
			try{
				ExceptionInParam oExceptionInParam = (ExceptionInParam) e.getTargetException(); //fillInStackTrace();
				request.setAttribute("status", oExceptionInParam.getCodError());
				request.setAttribute("error_message", oExceptionInParam.getMsjError());
				ResponseUtils.printError(request, response, oExceptionInParam);//cuando se creo la excepcion se loggeo.
				return;
				
			} catch(Exception eg){}
			
			//Se logea al BAM
			request.setAttribute("status", Constant.SC_BAD_REQUEST);
			if(e.getMessage() != null){
				request.setAttribute("error_message", Constant.MSG_ERROR_EJECUCION+": "+e.getMessage());
			}else{
				request.setAttribute("error_message", Constant.MSG_ERROR_EJECUCION);
			}
			ResponseUtils.printError(request, response, e);		
			
		} catch (Exception e) {
			
			request.setAttribute("status", Constant.SC_ERROR_EJECUCION);
			if(e.getMessage() != null){
				request.setAttribute("error_message", Constant.MSG_ERROR_EJECUCION+": "+e.getMessage());
			}else{
				request.setAttribute("error_message", Constant.MSG_ERROR_EJECUCION);
			}
			ResponseUtils.printError(request, response, e);	
			
		}		
	}
	
	
    public static String encoderPass(String strToEncrypt){
        try
        {
        	  /*Cipher cipher2 = Cipher.getInstance("AES/CBC/NoPadding");
       	      SecretKeySpec key = new SecretKeySpec(Constant.KEY_PASS_AES.getBytes("UTF-8"), "AES");
       	      cipher2.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec("AODVNUASDNVVAOVF".getBytes("UTF-8")));
       	   final String encryptedString2 = (new BASE64Encoder()).encode(cipher2.doFinal(strToEncrypt.getBytes("UTF-8")));*/

        	
            //Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            Cipher cipher = Cipher.getInstance("AES");
            final SecretKeySpec secretKey = new SecretKeySpec(Constant.KEY_PASS_AES.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = (new BASE64Encoder()).encode(cipher.doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        }
        catch (Exception e)
        {
           e.printStackTrace();
           return null;
        }

    }
	
	public static String decoderPass(String pass) {
		try
		{
			//Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			Cipher cipher = Cipher.getInstance("AES");
			final SecretKeySpec secretKey = new SecretKeySpec(Constant.KEY_PASS_AES.getBytes(), "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			final String decryptedString = new String(cipher.doFinal((new BASE64Decoder()).decodeBuffer(pass)));
			return decryptedString;
		}
		catch (Exception e)
		{
			//throw new SystemException(e);
	         e.printStackTrace();
	           return null;
		}
	}	

}
