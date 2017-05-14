 package com.brc.action.parse;
 
 import com.brc.model.GenerateModel;
 import com.brc.model.TableModel;
 import com.brc.tool.StringUtil;
 import java.util.Iterator;
 import java.util.List;
 
 public class ActionParser extends AbstractParser
 {
   public void generate(TableModel tableModel)
     throws Exception
   {
     String storePath = tableModel.getStorePath();
     String packageName = tableModel.getBasePath().replace("\\", ".");
     String servicePackageName = 
       StringUtil.addPoint(packageName, "service;");
     boolean isOneDomainBol = tableModel.isOneDomainBol();
     String objectName = getObjectName(tableModel.getObjectName());
     StringBuffer sb_action = new StringBuffer();
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
       if (isOneDomainBol) {
         model.setServiceName(StringUtil.initialStrToLower(objectName));
         model.setTempName(fileName);
 
         sb_action
           .append(readFile(getResource("action.content"), model));
       } else {
         model.setTempName("");
         model.setResourcePath(tableModel.getJspPath());
         model.setServiceName(StringUtil.initialStrToLower(fileName));
 
         String actionPackageName = StringUtil.addPoint(packageName, 
           "action;");
         model.setPackageName(actionPackageName);
         String serviceImplName = 
           StringUtil.initialStrToUpper(fileName) + "Action.java";
         actionPackageName = 
           StringUtil.convertPoint(actionPackageName);
         String writeFilePath = storePath + "/src/" + 
           actionPackageName + "/" + serviceImplName;
         model.setServicePackageName(servicePackageName.replace(";", 
           ""));
         model.setWriteFilePath(writeFilePath);
         readAndWriteFile(getResource("action"), model);
       }
     }
 
     if (isOneDomainBol) {
       GenerateModel model = new GenerateModel();
       model.setModel(tableModel.getModel());
       model.setObjectName(objectName);
       model.setResourcePath(tableModel.getJspPath());
       model.setServiceName(StringUtil.initialStrToLower(objectName));
       model.setAuthor(tableModel.getAuthor());
 
       String serviceImplPackageName = StringUtil.addPoint(
         packageName, "action;");
       model.setPackageName(serviceImplPackageName);
       String serviceImplName = 
         StringUtil.initialStrToUpper(objectName) + "Action.java";
       serviceImplPackageName = 
         StringUtil.convertPoint(serviceImplPackageName);
       String writeFilePath = storePath + "/src/" + 
         serviceImplPackageName + "/" + serviceImplName;
       model.setServicePackageName(servicePackageName.replace(";", ""));
       model.setWriteFilePath(writeFilePath);
       model.setContent(sb_action.toString());
       readAndWriteFile(getResource("action"), model);
     }
   }
 
   public String parseLine(String readLine, GenerateModel model)
     throws Exception
   {
     readLine = filterCommon(readLine, model);
     readLine = filterJava(readLine, model);
     if (readLine.contains("@ActionContent")) {
       if (StringUtil.isBlank(model.getContent())) {
         return readFile(getResource("action.content"), model);
       }
       return model.getContent();
     }
 
     return readLine;
   }
 }

