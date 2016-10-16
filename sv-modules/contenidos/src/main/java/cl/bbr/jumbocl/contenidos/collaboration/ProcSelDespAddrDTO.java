package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

public class ProcSelDespAddrDTO implements Serializable{
      private long id_op; // obligatorio
      private long dir_id; // obligatorio
	/**
	 * 
	 */
	public ProcSelDespAddrDTO() {
	}
	/**
	 * @param id_op
	 * @param dir_id
	 */
	public ProcSelDespAddrDTO(long id_op, long dir_id) {
		this.id_op = id_op;
		this.dir_id = dir_id;
	}
	/**
	 * @return Returns the dir_id.
	 */
	public long getDir_id() {
		return dir_id;
	}
	/**
	 * @return Returns the id_op.
	 */
	public long getId_op() {
		return id_op;
	}
	/**
	 * @param dir_id The dir_id to set.
	 */
	public void setDir_id(long dir_id) {
		this.dir_id = dir_id;
	}
	/**
	 * @param id_op The id_op to set.
	 */
	public void setId_op(long id_op) {
		this.id_op = id_op;
	}
	
      
}
