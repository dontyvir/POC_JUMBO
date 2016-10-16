package cl.bbr.jumbocl.contenidos.exceptions;

public class CampanaDAOException extends java.lang.Exception {
		
		public CampanaDAOException() {
			super("Exception, CampanaDAOException Detectada!!!...");
		}

		public CampanaDAOException(Throwable throwable) {
			super(throwable);
		}

		public CampanaDAOException(String message) {
			super(message);
		}

		public CampanaDAOException(String message, Throwable throwable) {
			super(message, throwable);
		}

		public CampanaDAOException(Exception e) {
			super(e.getMessage());
		}


	}
