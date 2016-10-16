package cl.cencosud.procesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import cl.cencosud.util.Db;
import cl.cencosud.util.LogUtil;

public class DbCarga extends Db {

	   static {
        
        LogUtil.initLog4J();
        
    }
	
	static Logger logger = Logger.getLogger(DbCarga.class);
	
	
   public DbCarga() {
   }

  
}