package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;
import java.util.List;

public class POSFeedbackProcPagoDTO implements Serializable{

	private int 	num_pos;
	private int 	num_boleta;
	private int 	num_sma;
	private String	fecha;
	private String	hora;
	
	private String	cod_feedback;
	private long	id_trxmp;
	private int		monto_pagado;
	private String 	fp_error;
	private String	fp_glosa;
	
	private List	prods;
	
	
	public POSFeedbackProcPagoDTO(){
		
	}

	public String getCod_feedback() {
		return cod_feedback;
	}


	public void setCod_feedback(String cod_feedback) {
		this.cod_feedback = cod_feedback;
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
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


	public String getHora() {
		return hora;
	}


	public void setHora(String hora) {
		this.hora = hora;
	}


	public long getId_trxmp() {
		return id_trxmp;
	}


	public void setId_trxmp(long id_trxmp) {
		this.id_trxmp = id_trxmp;
	}


	public int getMonto_pagado() {
		return monto_pagado;
	}


	public void setMonto_pagado(int monto_pagado) {
		this.monto_pagado = monto_pagado;
	}


	public int getNum_boleta() {
		return num_boleta;
	}


	public void setNum_boleta(int num_boleta) {
		this.num_boleta = num_boleta;
	}


	public int getNum_pos() {
		return num_pos;
	}


	public void setNum_pos(int num_pos) {
		this.num_pos = num_pos;
	}


	public int getNum_sma() {
		return num_sma;
	}


	public void setNum_sma(int num_sma) {
		this.num_sma = num_sma;
	}


	public List getProds() {
		return prods;
	}


	public void setProds(List prods) {
		this.prods = prods;
	}
	
	
	
	
	
}
