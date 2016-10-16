package cl.bbr.jumbocl.common.model;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que captura desde la base de datos los datos de perfiles
 * @author bbr
 *
 */
public class PerfilesEntity implements Serializable{
	private Long idPerfil;
	private String nombre;
	private String descripcion;
   
	private List lst_cmd;
   
   //private DbComandos[] RefCxpCmds;
	private UsuariosEntity[] RefUxpUsers;
/**
 * @return Returns the descripcion.
 */
public String getDescripcion() {
	return descripcion;
}
/**
 * @param descripcion The descripcion to set.
 */
public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}
/**
 * @return Returns the idPerfil.
 */
public Long getIdPerfil() {
	return idPerfil;
}
/**
 * @param idPerfil The idPerfil to set.
 */
public void setIdPerfil(Long idPerfil) {
	this.idPerfil = idPerfil;
}
/**
 * @return Returns the nombre.
 */
public String getNombre() {
	return nombre;
}
/**
 * @param nombre The nombre to set.
 */
public void setNombre(String nombre) {
	this.nombre = nombre;
}
/**
 * @return Returns the lst_cmd.
 */
public List getLst_cmd() {
	return lst_cmd;
}
/**
 * @param lst_cmd The lst_cmd to set.
 */
public void setLst_cmd(List lst_cmd) {
	this.lst_cmd = lst_cmd;
}

}