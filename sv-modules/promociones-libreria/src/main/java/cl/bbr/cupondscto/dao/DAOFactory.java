package cl.bbr.cupondscto.dao;

public abstract class DAOFactory {

  public static final int HIBERNATE = 1;
  public static final int JDBC = 2;
  
  public static DAOFactory getDAOFactory(
	  int whichFactory) {
  
	switch (whichFactory) {
	  case JDBC   : 
	   	  return new JdbcDAOFactory();
	  default           : 
		  return null;
	}
  }

  public abstract CuponDsctoDAO getCuponDsctoDAO();
  
  public abstract CuponDsctoDAO setIdCuponIdPedido();
  
}
