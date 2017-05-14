 package com.brc.model;
 
 import java.io.Serializable;
 
 public class GenerateModel
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String tableComment;
   private String objectName;
   private String writeFilePath;
   private String packageName;
   private String tableName;
   private String tempName;
   private String resourcePath;
   private String servicePackageName;
   private String serviceName;
   private String content;
   private String author;
   private DBModel model;
   private String tableId;
   private GenerateModel detailMode;
   private String mainTableName;
 
   public String getTableComment()
   {
     return this.tableComment;
   }
 
   public void setTableComment(String tableComment) {
     this.tableComment = tableComment;
   }
 
   public String getObjectName() {
     return this.objectName;
   }
 
   public void setObjectName(String objectName) {
     this.objectName = objectName;
   }
 
   public String getWriteFilePath() {
     return this.writeFilePath;
   }
 
   public void setWriteFilePath(String writeFilePath) {
     this.writeFilePath = writeFilePath;
   }
 
   public String getPackageName() {
     return this.packageName;
   }
 
   public void setPackageName(String packageName) {
     this.packageName = packageName;
   }
 
   public String getTableName() {
     return this.tableName;
   }
 
   public void setTableName(String tableName) {
     this.tableName = tableName;
   }
 
   public DBModel getModel() {
     return this.model;
   }
 
   public void setModel(DBModel model) {
     this.model = model;
   }
 
   public String getTempName() {
     return this.tempName;
   }
 
   public void setTempName(String tempName) {
     this.tempName = tempName;
   }
 
   public String getResourcePath() {
     return this.resourcePath;
   }
 
   public void setResourcePath(String resourcePath) {
     this.resourcePath = resourcePath;
   }
 
   public String getContent() {
     return this.content;
   }
 
   public void setContent(String content) {
     this.content = content;
   }
 
   public String getServicePackageName() {
     return this.servicePackageName;
   }
 
   public void setServicePackageName(String servicePackageName) {
     this.servicePackageName = servicePackageName;
   }
 
   public String getServiceName() {
     return this.serviceName;
   }
 
   public void setServiceName(String serviceName) {
     this.serviceName = serviceName;
   }
 
   public String getAuthor() {
     return this.author;
   }
 
   public void setAuthor(String author) {
     this.author = author;
   }

   public static long getSerialVersionUID() {
     return serialVersionUID;
   }

   public String getTableId() {
     return tableId;
   }

   public void setTableId(String tableId) {
     this.tableId = tableId;
   }

   public GenerateModel getDetailMode() {
     return detailMode;
   }

   public void setDetailMode(GenerateModel detailMode) {
     this.detailMode = detailMode;
   }


   public String getMainTableName() {
     return mainTableName;
   }

   public void setMainTableName(String mainTableName) {
     this.mainTableName = mainTableName;
   }
 }

