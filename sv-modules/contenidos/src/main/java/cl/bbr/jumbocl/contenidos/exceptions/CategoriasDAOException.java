package cl.bbr.jumbocl.contenidos.exceptions;

public class CategoriasDAOException extends java.lang.Exception {
		
		public CategoriasDAOException() {
			super("Exception, CategoriasDAOException Detectada!!!...");
		}

		public CategoriasDAOException(Throwable throwable) {
			super(throwable);
		}

		public CategoriasDAOException(String message) {
			super(message);
		}

		public CategoriasDAOException(String message, Throwable throwable) {
			super(message, throwable);
		}

		public CategoriasDAOException(Exception e) {
			super(e.getMessage());
		}


	}
