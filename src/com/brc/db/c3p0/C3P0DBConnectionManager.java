 package com.brc.db.c3p0;
 
 import com.brc.model.DBModel;
 import com.mchange.v2.c3p0.ComboPooledDataSource;
 import java.sql.Connection;
 import java.sql.DriverManager;
 import java.sql.SQLException;
 
 public class C3P0DBConnectionManager
 {
   private static ComboPooledDataSource cpds = null;
   private static String DBurl;
   private static String DBdriver;
   private static String DBuser;
   private static String DBpwd;
 
   public static void setDbModel(DBModel model)
     throws Exception
   {
     try
     {
       if ("oracle".equals(model.getDBtype())) {
         DBdriver = "oracle.jdbc.driver.OracleDriver";
       }
       DBurl = model.getDBurl();
       DBuser = model.getDBuser();
       DBpwd = model.getDBpwd();
       testConnection();
     } catch (Exception ex) {
       throw new Exception(ex);
     }
   }
 
   private static void testConnection()
     throws SQLException
   {
     Connection con = null;
     try {
       Class.forName(DBdriver);
       con = DriverManager.getConnection(DBurl, DBuser, DBpwd);
     } catch (Exception e) {
       throw new SQLException(e.getMessage());
     } finally {
       if (con != null)
         con.close();
     }
   }
 
   public static void init()
     throws Exception
   {
     int Min_PoolSize = 5;
     int Max_PoolSize = 50;
     int Acquire_Increment = 5;
     int Initial_PoolSize = 10;
 
     int Idle_Test_Period = 3000;
 
     String Validate = C3P0SystemConfig.getConfigInfomation("c3p0.validate");
     if (Validate.equals("")) {
       Validate = "false";
     }
     try
     {
       Min_PoolSize = Integer.parseInt(
         C3P0SystemConfig.getConfigInfomation("c3p0.minPoolSize"));
     } catch (Exception ex) {
       ex.printStackTrace();
     }
     try
     {
       Acquire_Increment = Integer.parseInt(
         C3P0SystemConfig.getConfigInfomation("c3p0.acquireIncrement"));
     } catch (Exception ex) {
       ex.printStackTrace();
     }
     try
     {
       Max_PoolSize = Integer.parseInt(
         C3P0SystemConfig.getConfigInfomation("c3p0.maxPoolSize"));
     } catch (Exception ex) {
       ex.printStackTrace();
     }
     try
     {
       Initial_PoolSize = Integer.parseInt(
         C3P0SystemConfig.getConfigInfomation("c3p0.initialPoolSize"));
     } catch (Exception ex) {
       ex.printStackTrace();
     }
     try
     {
       Idle_Test_Period = Integer.parseInt(
         C3P0SystemConfig.getConfigInfomation("c3p0.idleConnectionTestPeriod"));
     } catch (Exception ex) {
       ex.printStackTrace();
     }
     try {
       cpds = new ComboPooledDataSource();
       cpds.setDriverClass(DBdriver);
       cpds.setJdbcUrl(DBurl);
       cpds.setUser(DBuser);
       cpds.setPassword(DBpwd);
       cpds.setInitialPoolSize(Initial_PoolSize);
       cpds.setMinPoolSize(Min_PoolSize);
       cpds.setMaxPoolSize(Max_PoolSize);
       cpds.setAcquireIncrement(Acquire_Increment);
       cpds.setIdleConnectionTestPeriod(Idle_Test_Period);
       cpds.setTestConnectionOnCheckout(Boolean.getBoolean(Validate));
     } catch (Exception ex) {
       throw ex;
     }
   }
 
   public static Connection getConnection()
     throws Exception
   {
     Connection connection = null;
     try {
       if (cpds == null) {
         init();
       }
 
       connection = cpds.getConnection();
     } catch (Exception ex) {
       throw ex;
     }
     return connection;
   }
 
   public static void close()
   {
     try
     {
       if (cpds != null) {
         cpds.close();
         cpds = null;
       }
     } catch (Exception ex) {
       ex.printStackTrace();
     }
   }
 }

