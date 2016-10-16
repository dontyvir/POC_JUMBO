package cl.bbr.jumbocl.contenidos.exceptions;

public class ElementoDAOException extends java.lang.Exception {
		
		public ElementoDAOException() {
			super("Exception, ElementoDAOException Detectada!!!...");
		}

		public ElementoDAOException(Throwable throwable) {
			super(throwable);
		}

		public ElementoDAOException(String message) {
			super(message);
		}

		public ElementoDAOException(String message, Throwable throwable) {
			super(message, throwable);
		}

		public ElementoDAOException(Exception e) {
			super(e.getMessage());
		}


	}
