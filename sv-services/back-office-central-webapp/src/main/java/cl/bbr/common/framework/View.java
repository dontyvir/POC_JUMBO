package cl.bbr.common.framework;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import cl.bbr.jumbocl.shared.log.Logging;

public class View {

	protected 	Logging		logger;
	private		String		html;
	private		PrintWriter	out;
	
	
	public View(HttpServletResponse res) throws IOException{
		html = new String("");
		out = res.getWriter();
		res.setContentType("text/html");
		res.setHeader("Cache-Control", "no-cache");
	}	
	
	public void setHtmlOut(String out){
		this.html = out;
	}
	
	public void Output(){
		out.print(html);
		out.close();
	}
	
}
