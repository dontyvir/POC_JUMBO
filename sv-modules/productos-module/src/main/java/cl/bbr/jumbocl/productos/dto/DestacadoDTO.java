/*
 * Created on 05-feb-2009
 *
 */
package cl.bbr.jumbocl.productos.dto;

/**
 * @author jdroguett
 *  
 */
public class DestacadoDTO {
    private int id;

    private String descripcion;

    private String imagen;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
