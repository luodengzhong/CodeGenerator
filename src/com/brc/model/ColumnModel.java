 package com.brc.model;
 
 import java.util.ArrayList;
 import java.util.List;
 
 public class ColumnModel
 {
   private String name = "noname_column";
 
   private String nullable = "Y";
 
   private String type = "java.lang.String";
 
   private String originType = "VARCHAR2";
 
   private String length = "20";
 
   private String comment = "";
   protected static final String DB_DATE2_CLASS = "java.util.Date";
   protected static final String DB_DATETIME_CLASS = "java.sql.Timestamp";
 
   ColumnModel()
   {
     this.type = "java.lang.String";
     this.length = "20";
   }
 
   ColumnModel(String name, String comment) {
     this();
     this.name = name;
     this.comment = comment;
   }
 
   public String getType() {
     return this.type;
   }
 
   public String getShortType() {
     if (this.type.startsWith("java.lang.")) {
       String noPacake = this.type.substring("java.lang.".length());
       if (noPacake.indexOf(".") == -1) {
         return noPacake;
       }
     }
     return this.type;
   }
 
   public String getSureType() {
     int dot = this.type.lastIndexOf(".");
     if ((dot != -1) && (dot != this.type.length() - 1)) {
       return this.type.substring(dot + 1);
     }
     return this.type;
   }
 
   public String getLength() {
     return this.length;
   }
 
   public void setType(String type) {
     this.type = type;
   }
 
   public void setName(String name) {
     this.name = name;
   }
 
   public String getName() {
     return this.name;
   }
 
   public void setComment(String comment) {
     this.comment = comment;
   }
 
   public String getComment() {
     return this.comment;
   }
 
   public String getNullable() {
     return this.nullable;
   }
 
   public String getOriginType() {
     return this.originType;
   }
 
   public boolean isNullable() {
     if ("N".equals(this.nullable.toUpperCase())) {
       return false;
     }
     return true;
   }
 
   public int getLengthInt()
   {
     int leng = 0;
     try {
       leng = Integer.parseInt(this.length);
     }
     catch (Exception localException) {
     }
     return leng;
   }
 
   public String getPureComment()
   {
     String pure = "";
     if (this.comment != null) {
       pure = this.comment.trim();
     }
     if (pure.lastIndexOf(" ") != -1) {
       pure = pure.substring(pure.lastIndexOf(" ") + 1);
     }
     return pure;
   }
 
   public void setLength(String length) {
     this.length = length;
   }
 
   public void setNullable(String nullable) {
     this.nullable = nullable;
   }
 
   public void setOriginType(String originType) {
     this.originType = originType;
   }
 
   public static List<ColumnModel> parseColumnDetail(List<String[]> columns) {
     if ((columns == null) || (columns.size() == 0)) {
       return null;
     }
     List cols = new ArrayList(columns.size());
     for (String[] column : columns) {
       cols.add(parseColumnDetail(column));
     }
     return cols;
   }
 
   private static ColumnModel parseColumnDetail(String[] column)
   {
     if ((column == null) || (5 != column.length)) {
       return null;
     }
     String name = column[0].trim();
     String nullable = column[1].trim();
     String orginType = column[2].trim();
     String leng = column[3].trim();
     if (column[4] == null) {
       column[4] = "";
     }
     String comment = column[4].trim();
 
     ColumnModel typeLength = new ColumnModel();
     typeLength.setName(name);
     typeLength.setNullable(nullable);
     typeLength.setOriginType(orginType);
     typeLength.setLength(leng);
     typeLength.setComment(comment);
 
     int length = typeLength.getLengthInt();
 
     if ("DATE".equals(orginType.toUpperCase())) {
       if (comment.toUpperCase().endsWith("TIMESTAMP"))
       {
         typeLength.setType("java.sql.Timestamp");
       }
       else typeLength.setType("java.util.Date");
 
     }
     else if ("NUMBER".equals(orginType.toUpperCase())) {
       typeLength.setType("java.math.BigDecimal");
 
       if (length <= 4)
         typeLength.setType("java.lang.Integer");
       else if ((name.toUpperCase().equals("ID")) || 
         (name.toUpperCase().endsWith("ID")) || 
         (comment.endsWith("ID")) || 
         (name.toUpperCase().equals("CREATEBY")) || 
         (name.toUpperCase().equals("LASTUPDATEBY")) || 
         (name.toUpperCase().equals("VERSION")) || 
         (name.toUpperCase().equals("SEQUENCE")))
         typeLength.setType("java.lang.Long");
       else if ((name.toUpperCase().endsWith("STATUS")) || 
         (name.toUpperCase().startsWith("IS")) || 
         (name.toUpperCase().endsWith("TYPE")) || 
         (name.toUpperCase().endsWith("KIND")) || 
         (comment.endsWith("标志")) || (comment.endsWith("类型")) || 
         (comment.endsWith("状态"))) {
         typeLength.setType("java.lang.Integer");
       }
     }
     return typeLength;
   }
 }

