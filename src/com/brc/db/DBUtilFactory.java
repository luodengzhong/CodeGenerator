 package com.brc.db;
 
 import com.brc.db.impl.OrcaleUtilImpl;
 
 public class DBUtilFactory
 {
   public static DBUtil getDBUtil(String type)
   {
     DBUtil util = null;
     if (type.equals("oracle")) {
       util = new OrcaleUtilImpl();
     }
     return util;
   }
 }

