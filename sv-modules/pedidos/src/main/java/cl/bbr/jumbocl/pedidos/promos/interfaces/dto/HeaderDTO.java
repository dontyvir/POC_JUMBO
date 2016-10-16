package cl.bbr.jumbocl.pedidos.promos.interfaces.dto;

import java.io.Serializable;

import cl.bbr.jumbocl.common.utils.Formatos;

public class HeaderDTO implements Serializable{
	/* constantes de largo header */
	public static final int CANAL_LEN          = 1;
	public static final int CADENA_LEN         = 1;
	public static final int LOCAL_LEN          = 3;
	public static final int POS_LEN            = 3;
	public static final int FECHA_LEN          = 8;
	public static final int HORA_LEN           = 6;
	public static final int DOCUMENTO_LEN      = 10;
	public static final int OPERADOR_LEN       = 8;
	public static final int JOURNAL_LEN        = 6;
	public static final int TIPO_TRX_LEN       = 2;
	public static final int FILLER_LEN         = 2;
	
	public static final int CUPONES_HEADER_LEN   = 50;
	
	/* variables header */
	public String canal; 
	public String cadena;
	public String local;
	public String pos;
	public String fecha;
	public String hora;
	public String documento;
	public String operador;
	public String journal;
	public String tipo_trx;
	public String filler;
	
	public HeaderDTO() {		
	}
	
	public String toMsg(){
		String out = "";
		out += Formatos.formatField(canal, 		CANAL_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(cadena, 	CADENA_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(local, 		LOCAL_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(pos, 		POS_LEN, 		Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(fecha, 		FECHA_LEN, 		Formatos.ALIGN_LEFT,"0");
		out += Formatos.formatField(hora, 		HORA_LEN, 		Formatos.ALIGN_LEFT,"0");
		out += Formatos.formatField(documento, 	DOCUMENTO_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(operador, 	OPERADOR_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(journal, 	JOURNAL_LEN, 	Formatos.ALIGN_RIGHT,"0");
		out += Formatos.formatField(tipo_trx, 	TIPO_TRX_LEN, 	Formatos.ALIGN_RIGHT," ");
		out += Formatos.formatField(filler, 	FILLER_LEN, 	Formatos.ALIGN_RIGHT,"0");	
		return out;
	}

	/**
	 * @return Returns the cadena.
	 */
	public String getCadena() {
		return cadena;
	}

	/**
	 * @param cadena The cadena to set.
	 */
	public void setCadena(String cadena) {
		this.cadena = cadena;
	}

	/**
	 * @return Returns the canal.
	 */
	public String getCanal() {
		return canal;
	}

	/**
	 * @param canal The canal to set.
	 */
	public void setCanal(String canal) {
		this.canal = canal;
	}

	/**
	 * @return Returns the documento.
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento The documento to set.
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return Returns the filler.
	 */
	public String getFiller() {
		return filler;
	}

	/**
	 * @param filler The filler to set.
	 */
	public void setFiller(String filler) {
		this.filler = filler;
	}

	/**
	 * @return Returns the hora.
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora The hora to set.
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return Returns the journal.
	 */
	public String getJournal() {
		return journal;
	}

	/**
	 * @param journal The journal to set.
	 */
	public void setJournal(String journal) {
		this.journal = journal;
	}

	/**
	 * @return Returns the local.
	 */
	public String getLocal() {
		return local;
	}

	/**
	 * @param local The local to set.
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * @return Returns the operador.
	 */
	public String getOperador() {
		return operador;
	}

	/**
	 * @param operador The operador to set.
	 */
	public void setOperador(String operador) {
		this.operador = operador;
	}

	/**
	 * @return Returns the pos.
	 */
	public String getPos() {
		return pos;
	}

	/**
	 * @param pos The pos to set.
	 */
	public void setPos(String pos) {
		this.pos = pos;
	}

	/**
	 * @return Returns the tipo_trx.
	 */
	public String getTipo_trx() {
		return tipo_trx;
	}

	/**
	 * @param tipo_trx The tipo_trx to set.
	 */
	public void setTipo_trx(String tipo_trx) {
		this.tipo_trx = tipo_trx;
	}
	
	

}
