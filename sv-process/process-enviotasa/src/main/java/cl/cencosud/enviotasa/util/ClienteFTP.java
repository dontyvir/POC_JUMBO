package cl.cencosud.enviotasa.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

public class ClienteFTP {
	private URLConnection m_cliemte;
	private String host;
	private String user;
	private String password;
	private String remoteFile;
	private String errorMsg;
	private String successMsg;
	
	public ClienteFTP() {
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRemoteFile(String rf) {
		this.remoteFile = rf;
	}
	public synchronized String getLastSuccess() {
		if(successMsg == null) {
			return "";
		}
        return successMsg;
	}
	public synchronized String getLastError() {
		if(errorMsg == null) {
			return "";
		}
        return errorMsg;
	}
	public synchronized boolean uploadFile(String localFileName) {
		try {
			InputStream inputStream = new FileInputStream(localFileName);
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			OutputStream outputStream = m_cliemte.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(outputStream);
			byte[] buffer = new byte[1024];
			int readCount;
			while((readCount = bis.read(buffer)) > 0) {
				bos.write(buffer,0,readCount);
			}
			bos.close();
			this.successMsg = "Uploaded";
			return true;
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw,true);
			e.printStackTrace(pw);
			errorMsg = sw.getBuffer().toString();
			return false;
		}
	}
	public synchronized boolean downloadFile(String localFileName) {
		try {
			InputStream is = m_cliemte.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			OutputStream os = new FileOutputStream(localFileName);
			BufferedOutputStream bos = new BufferedOutputStream(os);
			byte[] buffer = new byte[1024];
			int readCount;
			while((readCount = bis.read()) > 0) {
				bos.write(buffer,0,readCount);
			}
			bos.close();
			is.close();
			this.successMsg = "Downloaded";
			return true;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw,true);
			e.printStackTrace(pw);
			errorMsg = sw.getBuffer().toString();
			return false;
		}
	}
	public synchronized boolean connect()  {
		try {
			URL url = new URL("ftp://"+user+":"+password+"@"+host+"/"+remoteFile+";type=i");
			m_cliemte = url.openConnection();
			return true;
		} catch(Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw,true);
			e.printStackTrace(pw);
			errorMsg = sw.getBuffer().toString();
			return false;
		}
	}
	
}
