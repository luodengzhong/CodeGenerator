 package com.brc.db.c3p0;
 
 import java.io.IOException;
 import java.util.Properties;
 
 public class C3P0SystemConfig
 {
   private static final String CONFIG_FILE = "/c3p0.properties";
   private static C3P0SystemConfig config;
   private Properties prop = null;
 
   private C3P0SystemConfig() {
     this.prop = new Properties();
     try {
       this.prop.load(getClass().getResourceAsStream("/c3p0.properties"));
     } catch (IOException e) {
       e.printStackTrace();
     }
   }
 
   private String getConfig(String name) {
     return this.prop.getProperty(name);
   }
 
   public static String getConfigInfomation(String name) {
     if (config == null) {
       config = new C3P0SystemConfig();
     }
     String value = config.getConfig(name);
     if (value != null)
       value = value.trim();
     return value;
   }
 }

