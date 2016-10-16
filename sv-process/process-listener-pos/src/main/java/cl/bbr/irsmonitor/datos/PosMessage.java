package cl.bbr.irsmonitor.datos;

import java.nio.ByteBuffer;

import cl.bbr.irsmonitor.dto.HeaderPosDTO;

public class PosMessage {

	private   ByteBuffer buff;
	
	/*
	private void setField(String field, int len){
		byte []tmp = new byte[len];
		this.buff.get(tmp);
		field = new String(tmp);
		System.out.println("->" + field);
		tmp = null;
	}
	*/
	
	private String setField(int len){
		byte []tmp = new byte[len];
		this.buff.get(tmp);
	//	tmp = null;
		return new String(tmp);
	}
	
	public HeaderPosDTO parseHeader(ByteBuffer buff){
		//this.buff = buff.duplicate();
		this.buff = buff;
		HeaderPosDTO header = new HeaderPosDTO();
		
		header.setEmpresa(setField(HeaderPosDTO.EMPRESA_LEN));
		header.setLocal(setField(HeaderPosDTO.LOCAL_LEN));
		header.setPos(setField(HeaderPosDTO.POS_LEN));
		header.setBoleta(setField(HeaderPosDTO.BOLETA_LEN));
		header.setTrxSma(setField(HeaderPosDTO.TRXSMA_LEN));
		header.setFecha(setField(HeaderPosDTO.FECHA_LEN));
		header.setHora(setField(HeaderPosDTO.HORA_LEN));
		header.setOperador(setField(HeaderPosDTO.OPERADOR_LEN));
		header.setTipoTrx(setField(HeaderPosDTO.TIPOTRX_LEN));
		header.setVersion(setField(HeaderPosDTO.VERSION_LEN));
		header.setJournal(setField(HeaderPosDTO.JOURNAL_LEN));
		header.setLargo(setField(HeaderPosDTO.LARGO_LEN));
		
	
		
		/*
		String tmp = null;	
		setField(tmp, HeaderPos.EMPRESA_LEN);
		header.setEmpresa(tmp);
		setField(tmp, HeaderPos.LOCAL_LEN);
		header.setLocal(tmp);
		setField(tmp, HeaderPos.POS_LEN);
		header.setPos(tmp);
		setField(tmp, HeaderPos.BOLETA_LEN);
		header.setBoleta(tmp);
		setField(tmp, HeaderPos.TRXSMA_LEN);
		header.setTrxSma(tmp);
		setField(tmp, HeaderPos.FECHA_LEN);
		header.setFecha(tmp);
		setField(tmp, HeaderPos.HORA_LEN);
		header.setHora(tmp);
		setField(tmp, HeaderPos.OPERADOR_LEN);
		header.setOperador(tmp);
		setField(tmp, HeaderPos.TIPOTRX_LEN);
		header.setTipoTrx(tmp);
		setField(tmp, HeaderPos.VERSION_LEN);
		header.setVersion(tmp);
		setField(tmp, HeaderPos.JOURNAL_LEN);
		header.setJournal(tmp);
		setField(tmp, HeaderPos.LARGO_LEN);
		header.setLargo(tmp);
		*/
		return header;
	}

	
}
