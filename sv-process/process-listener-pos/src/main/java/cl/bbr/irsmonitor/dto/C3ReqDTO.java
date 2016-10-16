package cl.bbr.irsmonitor.dto;

import cl.bbr.irsmonitor.datos.ReqRsp;

public class C3ReqDTO extends ReqRsp{
	public static final int OP_LEN    = 10;

	private String op;          //10 bytes

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}
}
