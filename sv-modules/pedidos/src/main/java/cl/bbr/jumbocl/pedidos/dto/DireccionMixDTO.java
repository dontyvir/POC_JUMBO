package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class DireccionMixDTO implements Serializable {
	

	private static final long serialVersionUID = 1883116914573074962L;
	
	private long ses_zona_id;//", String.valueOf( dir3.getZona_id()  ));
	private long ses_loc_id;//", String.valueOf( dir3.getLoc_cod() ));
	private long ses_dir_id;//", String.valueOf( dir3.getId() ));
	private String ses_dir_alias;//", dir3.getAlias());                        
	private String ses_forma_despacho;//", "N"); 
	private String ses_destination;//", "UltComprasForm?opcion=1");
	private boolean isDireccionMix =false;
	
	

	public DireccionMixDTO(long ses_zona_id, long ses_loc_id, long ses_dir_id,
			String ses_dir_alias, String ses_forma_despacho,
			String ses_destination, boolean isDireccionMix) {
		this.ses_zona_id = ses_zona_id;
		this.ses_loc_id = ses_loc_id;
		this.ses_dir_id = ses_dir_id;
		this.ses_dir_alias = ses_dir_alias;
		this.ses_forma_despacho = ses_forma_despacho;
		this.ses_destination = ses_destination;
		this.isDireccionMix = isDireccionMix;
	}

	public DireccionMixDTO() {
		// TODO Apéndice de constructor generado automáticamente
	}

	public long getSes_zona_id() {
		return ses_zona_id;
	}

	public void setSes_zona_id(long ses_zona_id) {
		this.ses_zona_id = ses_zona_id;
	}

	public long getSes_loc_id() {
		return ses_loc_id;
	}

	public void setSes_loc_id(long ses_loc_id) {
		this.ses_loc_id = ses_loc_id;
	}

	public long getSes_dir_id() {
		return ses_dir_id;
	}

	public void setSes_dir_id(long ses_dir_id) {
		this.ses_dir_id = ses_dir_id;
	}

	public String getSes_dir_alias() {
		return ses_dir_alias;
	}

	public void setSes_dir_alias(String ses_dir_alias) {
		this.ses_dir_alias = ses_dir_alias;
	}

	public String getSes_forma_despacho() {
		return ses_forma_despacho;
	}

	public void setSes_forma_despacho(String ses_forma_despacho) {
		this.ses_forma_despacho = ses_forma_despacho;
	}

	public String getSes_destination() {
		return ses_destination;
	}

	public void setSes_destination(String ses_destination) {
		this.ses_destination = ses_destination;
	}

	public boolean isDireccionMix() {
		return isDireccionMix;
	}

	public void setDireccionMix(boolean isDireccionMix) {
		this.isDireccionMix = isDireccionMix;
	}
	
	

}
