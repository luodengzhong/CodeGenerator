 package com.brc.db.impl;
 
 import com.brc.db.DBUtil;
 import com.brc.db.JDBCQuery;
 import com.brc.db.c3p0.C3P0DBConnectionManager;
 import java.sql.Connection;
 import java.util.List;
 
 public class OrcaleUtilImpl
   implements DBUtil
 {
   private Connection getConnection()
   {
     try
     {
       return C3P0DBConnectionManager.getConnection();
     } catch (Exception e) {
       e.printStackTrace();
     }
     return null;
   }
 
   public List<String[]> selectAllTables()
   {
     StringBuffer sql_sb = new StringBuffer(" SELECT ");
     sql_sb.append("TC.TABLE_NAME,TC.comments")
       .append(" FROM user_tab_comments TC").append(" WHERE EXISTS (")
       .append(" SELECT 1").append(" FROM USER_CONSTRAINTS UTC")
       .append(" WHERE UTC.CONSTRAINT_TYPE='P'")
       .append(" AND UTC.TABLE_NAME=TC.TABLE_NAME").append(") ");
     sql_sb.append(" ORDER BY TC.TABLE_NAME ");
     return JDBCQuery.excuteSQL(sql_sb.toString(), getConnection());
   }
 
   public List<String[]> selectTable(String tableName)
   {
     StringBuffer sql_sb = new StringBuffer(" SELECT ");
     sql_sb.append("TC.TABLE_NAME,TC.comments")
       .append(" FROM user_tab_comments TC").append(" WHERE EXISTS (")
       .append(" SELECT 1").append(" FROM USER_CONSTRAINTS UTC")
       .append(" WHERE UTC.CONSTRAINT_TYPE='P'")
       .append(" AND UTC.TABLE_NAME=TC.TABLE_NAME").append(") ");
     sql_sb.append(" AND TC.TABLE_NAME like ?");
     sql_sb.append(" ORDER BY TC.TABLE_NAME ");
     return JDBCQuery.excuteSQL(sql_sb.toString(), new String[] { "%" + 
       tableName.trim().toUpperCase() + "%" }, getConnection());
   }
 
   public List<String[]> selectTableDetail(String tableName)
   {
     StringBuffer sql_sb = new StringBuffer(" SELECT ");
     sql_sb.append("T.COLUMN_NAME,").append("T.NULLABLE,")
       .append("T.DATA_TYPE,")
       .append("nvl(T.DATA_PRECISION,T.DATA_LENGTH),")
       .append("CC.comments");
     sql_sb.append(" FROM user_tab_columns T,")
       .append("user_col_comments CC ")
       .append(" WHERE T.TABLE_NAME = ?")
       .append(" AND T.COLUMN_NAME = CC.COLUMN_NAME")
       .append(" AND T.TABLE_NAME = CC.table_name")
       .append(" ORDER BY T.COLUMN_ID ");
 
     return JDBCQuery.excuteSQL(sql_sb.toString(), new String[] { tableName
       .trim().toUpperCase() }, getConnection());
   }
 
   public List<String[]> selectTableNormalColsDet(String tableName)
   {
     StringBuffer pk_sb = new StringBuffer(" SELECT A.COLUMN_NAME ");
     pk_sb.append("FROM USER_CONS_COLUMNS A")
       .append(" WHERE  A.CONSTRAINT_NAME IN")
       .append("(SELECT T.CONSTRAINT_NAME")
       .append(" FROM USER_CONSTRAINTS T")
       .append(" WHERE T.CONSTRAINT_TYPE = 'P'")
       .append(" AND T.TABLE_NAME = ?)");
     StringBuffer sql_sb = new StringBuffer(" SELECT ");
     sql_sb.append("T.COLUMN_NAME,").append("T.NULLABLE,")
       .append("T.DATA_TYPE,")
       .append("nvl(T.DATA_PRECISION,T.DATA_LENGTH),")
       .append("CC.comments").append(" FROM user_tab_columns T,")
       .append("user_col_comments CC ")
       .append(" WHERE T.TABLE_NAME = ?")
       .append(" AND T.COLUMN_NAME = CC.COLUMN_NAME")
       .append(" AND T.TABLE_NAME = CC.table_name")
       .append(" AND T.COLUMN_NAME NOT IN(" + pk_sb + ")")
       .append(" ORDER BY T.COLUMN_ID ");
 
     return JDBCQuery.excuteSQL(sql_sb.toString(), 
       new String[] { tableName.trim().toUpperCase(), 
       tableName.trim().toUpperCase() }, getConnection());
   }
 
   public List<String[]> selectPrimryColumns(String tableName)
   {
     StringBuffer pk_sb = new StringBuffer(" SELECT A.COLUMN_NAME ");
     pk_sb.append("FROM USER_CONS_COLUMNS A")
       .append(" WHERE  A.CONSTRAINT_NAME IN")
       .append("(SELECT T.CONSTRAINT_NAME")
       .append(" FROM USER_CONSTRAINTS T")
       .append(" WHERE T.CONSTRAINT_TYPE = 'P'")
       .append(" AND T.TABLE_NAME = ?)");
     StringBuffer sql_sb = new StringBuffer(" SELECT ");
     sql_sb.append("T.COLUMN_NAME,").append("T.NULLABLE,")
       .append("T.DATA_TYPE,")
       .append("nvl(T.DATA_PRECISION,T.DATA_LENGTH),")
       .append("CC.comments").append(" FROM user_tab_columns T,")
       .append("user_col_comments CC ")
       .append(" WHERE T.TABLE_NAME = ?")
       .append(" AND T.COLUMN_NAME = CC.COLUMN_NAME")
       .append(" AND T.TABLE_NAME = CC.table_name")
       .append(" AND T.COLUMN_NAME IN(" + pk_sb + ")")
       .append(" ORDER BY T.COLUMN_ID ");
     return JDBCQuery.excuteSQL(sql_sb.toString(), 
       new String[] { tableName.trim().toUpperCase(), 
       tableName.trim().toUpperCase() }, getConnection());
   }
 }

