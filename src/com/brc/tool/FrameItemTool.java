 package com.brc.tool;
 
 import com.brc.db.DBUtil;
 import com.brc.db.DBUtilFactory;
 import com.brc.model.DBModel;
 import java.util.List;
 import javax.swing.DefaultListModel;
 
 public class FrameItemTool
 {
   public static void tablesList(DefaultListModel listModel, DBModel model)
   {
     DBUtil util = DBUtilFactory.getDBUtil(model.getDBtype());
     List<String[]> tableList = util.selectAllTables();
     listModel.clear();
     for (String[] tables : tableList)
       listModel.addElement(tables[0] + " - " + tables[1]);
   }
 
   public static void tablesList(DefaultListModel listModel, DBModel model, String tableName)
   {
     DBUtil util = DBUtilFactory.getDBUtil(model.getDBtype());
     List<String[]> tableList = util.selectTable(tableName);
     listModel.clear();
     for (String[] tables : tableList)
       listModel.addElement(tables[0] + " - " + tables[1]);
   }
 }

