package cl.bbr.bol.utils;

import java.util.List;

import cl.bbr.jumbocl.contenidos.dto.EstadoDTO;

public class FormatosVarios {

	public static final String SINDATO = "---";
	
	public FormatosVarios() {
	}
	
	public static String frmEstado(List lst, String estado){
		String res = SINDATO;
		for(int i=0;i<lst.size();i++){
			EstadoDTO dto = (EstadoDTO) lst.get(i);
			if(dto.getId_estado()==estado.charAt(0))
				res = dto.getNombre();
		}
		return res;
	}
}
