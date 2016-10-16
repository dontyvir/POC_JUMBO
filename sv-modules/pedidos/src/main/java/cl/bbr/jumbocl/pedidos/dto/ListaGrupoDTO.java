package cl.bbr.jumbocl.pedidos.dto;

public class ListaGrupoDTO {
    
	private long idListaGrupo;
	private String nombre;
    private String activado;
    private ListaTipoGrupoDTO tipo = new ListaTipoGrupoDTO();
	
	
    /**
     * @return Devuelve idListaGrupo.
     */
    public long getIdListaGrupo() {
        return idListaGrupo;
    }
    /**
     * @return Devuelve nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @return Devuelve tipo.
     */
    public ListaTipoGrupoDTO getTipo() {
        return tipo;
    }
    /**
     * @param idListaGrupo El idListaGrupo a establecer.
     */
    public void setIdListaGrupo(long idListaGrupo) {
        this.idListaGrupo = idListaGrupo;
    }
    /**
     * @param nombre El nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    /**
     * @param tipo El tipo a establecer.
     */
    public void setTipo(ListaTipoGrupoDTO tipo) {
        this.tipo = tipo;
    }
    /**
     * @return Devuelve activado.
     */
    public String getActivado() {
        return activado;
    }
    /**
     * @param activado El activado a establecer.
     */
    public void setActivado(String activado) {
        this.activado = activado;
    }
}
