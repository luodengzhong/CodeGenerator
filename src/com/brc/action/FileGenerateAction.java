 package com.brc.action;
 
 import com.brc.action.parse.ActionParser;
 import com.brc.action.parse.BeanParser;
 import com.brc.action.parse.JSPParser;
 import com.brc.action.parse.ServiceParser;
 import com.brc.action.parse.XMLParser;
 import com.brc.model.TableModel;
 import com.brc.tool.StringUtil;
 
 public class FileGenerateAction
 {
   public static void engineEntry(TableModel tableModel)
     throws Exception
   {
     String basePath = StringUtil.replaceSlash(tableModel.getBasePath());
     tableModel.setBasePath(basePath);
     if (tableModel.isBeanBol()) {
       new BeanParser().generate(tableModel);
     }
     if (tableModel.isXMLBol()) {
       new XMLParser().generate(tableModel);
     }
     if (tableModel.isServiceBol()) {
       new ServiceParser().generate(tableModel);
     }
     if (tableModel.isActionBol()) {
       new ActionParser().generate(tableModel);
     }
     if (tableModel.isJspBol())
       new JSPParser().generate(tableModel);
   }
 }

