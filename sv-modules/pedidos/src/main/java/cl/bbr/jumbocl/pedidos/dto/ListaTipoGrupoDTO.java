package cl.bbr.jumbocl.pedidos.dto;

public class ListaTipoGrupoDTO {
    
	private int idListaTipoGrupo;
	private String descripcion;
    private String activado;
    private String nombreArchivo;
    private String texto;
	
	
    /**
     * @return Devuelve descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @return Devuelve idListaTipoGrupo.
     */
    public int getIdListaTipoGrupo() {
        return idListaTipoGrupo;
    }
    /**
     * @param descripcion El descripcion a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @param idListaTipoGrupo El idListaTipoGrupo a establecer.
     */
    public void setIdListaTipoGrupo(int idListaTipoGrupo) {
        this.idListaTipoGrupo = idListaTipoGrupo;
    }
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @return Devuelve nombreArchivo.
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    /**
     * @return Devuelve texto.
     */
    public String getTexto() {
        return texto;
    }
    /**
     * @param activado El activado a establecer.
     */
    public void setActivado(String activado) {
        this.activado = activado;
    }
    /**
     * @param nombreArchivo El nombreArchivo a establecer.
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    /**
     * @param texto El texto a establecer.
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }
}
