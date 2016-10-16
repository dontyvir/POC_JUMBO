package cl.bbr.irsmonitor.dto;


import java.util.List;


public class C2ReqDTO{
	
	public static final int FEEDBACK_LEN    = 3;
	public static final int OP_LEN  	    = 10;
	public static final int MONTO_OP_LEN  	= 8;
	public static final int FPAGO_ERROR     = 3;
	public static final int FPAGO_GLOSA_LEN = 20;

	
	private String feedBack;			//3 bytes
	private String op;					//10 bytes
	private String monto_op;			//8 bytes;
	private String fp_error;			//3 byte
	private String fp_glosa;			//20 bytes
	private List productos;
	
	
	
	public static int getLargoFijoData(){
		int largo = 0;
		
		largo += FEEDBACK_LEN;
		largo += OP_LEN;
		largo += MONTO_OP_LEN;
		largo += FPAGO_ERROR;
		largo += FPAGO_GLOSA_LEN;
		
		return largo;
	}
	
	

	/*
	 * Getters y Setters
	 */
	
	public String getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	public String getFp_error() {
		return fp_error;
	}
	public void setFp_error(String fp_error) {
		this.fp_error = fp_error;
	}
	public String getFp_glosa() {
		return fp_glosa;
	}
	public void setFp_glosa(String fp_glosa) {
		this.fp_glosa = fp_glosa;
	}
	public String getMonto_op() {
		return monto_op;
	}
	public void setMonto_op(String monto_op) {
		this.monto_op = monto_op;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public List getProductos() {
		return productos;
	}
	public void setProductos(List productos) {
		this.productos = productos;
	}

	
	
}
