 package com.brc.model;
 
 public class DBModel
 {
   public static final String ORACLE_FLAG = "oracle";
   public static final String DB2_FLAG = "db2";
   public static final String MYSQL_FLAG = "mysql";
   private String DBurl;
   private String DBdriver;
   private String DBtype;
   private String DBname;
   private String DBuser;
   private String DBpwd;
 
   public String getDBurl()
   {
     return this.DBurl;
   }
 
   public void setDBurl(String burl) {
     this.DBurl = burl;
   }
 
   public String getDBdriver() {
     return this.DBdriver;
   }
 
   public void setDBdriver(String bdriver) {
     this.DBdriver = bdriver;
   }
 
   public String getDBtype() {
     return this.DBtype;
   }
 
   public void setDBtype(String btype) {
     this.DBtype = btype;
   }
 
   public String getDBname() {
     return this.DBname;
   }
 
   public void setDBname(String bname) {
     this.DBname = bname;
   }
 
   public String getDBuser() {
     return this.DBuser;
   }
 
   public void setDBuser(String buser) {
     this.DBuser = buser;
   }
 
   public String getDBpwd() {
     return this.DBpwd;
   }
 
   public void setDBpwd(String bpwd) {
     this.DBpwd = bpwd;
   }
 }

