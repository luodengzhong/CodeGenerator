 package com.brc.model;
 
 import java.io.Serializable;
 import java.util.List;
 
 public class TableModel
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private List<Object> selecedTables;
   private String objectName;
   private String basePath;
   private String storePath;
   private String jspPath;
   private boolean isBeanBol;
   private boolean isServiceBol;
   private boolean isJspBol;
   private boolean isXMLBol;
   private boolean isActionBol;
   private boolean isOneDomainBol;
   private String author;
   private DBModel model;
 
   public List<Object> getSelecedTables()
   {
     return this.selecedTables;
   }
 
   public void setSelecedTables(List<Object> selecedTables) {
     this.selecedTables = selecedTables;
   }
 
   public String getObjectName() {
     return this.objectName;
   }
 
   public void setObjectName(String objectName) {
     this.objectName = objectName;
   }
 
   public String getBasePath() {
     return this.basePath;
   }
 
   public void setBasePath(String basePath) {
     this.basePath = basePath;
   }
 
   public String getStorePath() {
     return this.storePath;
   }
 
   public void setStorePath(String storePath) {
     this.storePath = storePath;
   }
 
   public String getJspPath() {
     return this.jspPath;
   }
 
   public void setJspPath(String jspPath) {
     this.jspPath = jspPath;
   }
 
   public boolean isBeanBol() {
     return this.isBeanBol;
   }
 
   public void setBeanBol(boolean isBeanBol) {
     this.isBeanBol = isBeanBol;
   }
 
   public boolean isServiceBol() {
     return this.isServiceBol;
   }
 
   public void setServiceBol(boolean isServiceBol) {
     this.isServiceBol = isServiceBol;
   }
 
   public boolean isJspBol() {
     return this.isJspBol;
   }
 
   public void setJspBol(boolean isJspBol) {
     this.isJspBol = isJspBol;
   }
 
   public boolean isXMLBol() {
     return this.isXMLBol;
   }
 
   public void setXMLBol(boolean isXMLBol) {
     this.isXMLBol = isXMLBol;
   }
 
   public boolean isActionBol() {
     return this.isActionBol;
   }
 
   public void setActionBol(boolean isActionBol) {
     this.isActionBol = isActionBol;
   }
 
   public boolean isOneDomainBol() {
     return this.isOneDomainBol;
   }
 
   public void setOneDomainBol(boolean isOneDomainBol) {
     this.isOneDomainBol = isOneDomainBol;
   }
 
   public DBModel getModel() {
     return this.model;
   }
 
   public void setModel(DBModel model) {
     this.model = model;
   }
 
   public String getAuthor() {
     return this.author;
   }
 
   public void setAuthor(String author) {
     this.author = author;
   }
 }

