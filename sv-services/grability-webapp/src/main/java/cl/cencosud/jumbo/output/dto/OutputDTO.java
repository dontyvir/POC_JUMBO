package cl.cencosud.jumbo.output.dto;

public abstract class OutputDTO {	

	private String status;//"status of the request",
	private String error_message;//"error message to describe the status code",
			
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public abstract String toJson();

}
