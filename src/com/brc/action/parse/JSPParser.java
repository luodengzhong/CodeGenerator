 package com.brc.action.parse;
 
 import com.brc.model.ColumnModel;
 import com.brc.model.DBModel;
 import com.brc.model.GenerateModel;
 import com.brc.model.TableModel;
 import com.brc.tool.StringUtil;
 import java.util.Iterator;
 import java.util.List;
 
 public class JSPParser extends AbstractParser
 {
   public void generate(TableModel tableModel)
     throws Exception
   {
     String storePath = tableModel.getStorePath();
     String jspPath = tableModel.getJspPath();
     boolean isOneDomainBol = tableModel.isOneDomainBol();
     String objectName = getObjectName(tableModel.getObjectName());
 
     for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
       GenerateModel model = new GenerateModel();
       String tableName = obj.toString().split("-")[0].trim();
       String tableComment = obj.toString().split("-")[1].trim();
       String fileName = getFileName(tableName);
       model.setObjectName(fileName);
       model.setModel(tableModel.getModel());
       model.setTableName(tableName);
       model.setTableComment(tableComment);
       model.setAuthor(tableModel.getAuthor());
       String writeFilePath = storePath + "/WebContent/" + 
         StringUtil.convertPoint(jspPath) + "/";
 
       model.setWriteFilePath(writeFilePath + 
         StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("List").toString(), "jsp"));
       readAndWriteFile(getResource("jsp"), model);
 
       model.setWriteFilePath(writeFilePath + 
         StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("Detail1").toString(), "jsp"));
       readAndWriteFile(getResource("editJsp"), model);
 
       model.setWriteFilePath(writeFilePath + 
         StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("Detail2").toString(), "jsp"));
       readAndWriteFile(getResource("editTableJsp"), model);
 
       if (isOneDomainBol) {
         model.setTempName(fileName);
         String oneFileName = 
           StringUtil.initialStrToUpper(objectName);
         model.setObjectName(oneFileName);
       } else {
         model.setTempName("");
       }
       model.setWriteFilePath(writeFilePath + 
         StringUtil.addPoint(fileName, "js"));
       readAndWriteFile(getResource("js"), model);
     }
   }
 
   public String parseLine(String readLine, GenerateModel model)
     throws Exception
   {
     readLine = filterCommon(readLine, model);
     readLine = filterJava(readLine, model);
     String tableName = model.getTableName();
     if (readLine.contains("@id")) {
       StringBuffer sb = new StringBuffer();
       List<ColumnModel> columns = getPrimryColumns(tableName, model
         .getModel().getDBtype());
       for (ColumnModel column : columns) {
         sb.append(filterColumn(readLine, column));
       }
       return sb.toString();
     }if (readLine.contains("@property")) {
       StringBuffer sb = new StringBuffer();
       List<ColumnModel> columns = getNormalColsDet(tableName, model
         .getModel().getDBtype());
       for (ColumnModel column : columns) {
         sb.append(filterColumn(readLine, column));
       }
       return sb.toString();
     }
     return readLine;
   }
 }

