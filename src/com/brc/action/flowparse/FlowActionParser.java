package com.brc.action.flowparse;

import com.brc.action.parse.AbstractParser;
import com.brc.model.ColumnModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.StringUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FlowActionParser extends FlowAbstractParser {
    public void generate(TableModel tableModel)
            throws Exception {
        String storePath = tableModel.getStorePath();
        String packageName = tableModel.getBasePath().replace("\\", ".");
        String servicePackageName =
                StringUtil.addPoint(packageName, "service;");
        boolean isOneDomainBol = tableModel.isOneDomainBol();
        String objectName = getObjectName(tableModel.getObjectName());
        StringBuffer sb_action = new StringBuffer();

        StringBuffer detailTable = new StringBuffer();
        Map<String,GenerateModel> mainAndDetailTable = new HashMap<String,GenerateModel>();
        for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            String tableName = obj.toString().split("-")[0].trim();
            if(detailTable.indexOf(tableName) > -1){
                continue;
            }
            List<ColumnModel> columns = getPrimryColumns(tableName, tableModel.getModel().getDBtype());
            String tableId = columns.get(0).getName();
            //查询明细表：
            for (Iterator interator = tableModel.getSelecedTables().iterator(); interator.hasNext(); ) {
                Object detail = interator.next();
                String detailTableName = detail.toString().split("-")[0].trim();
                if(!tableName.equals(detailTableName)){
                    List<ColumnModel> cols = getNormalColsDet(detailTableName, tableModel.getModel().getDBtype());
                    for (ColumnModel column : cols) {
                        if(column.getName().equals(tableId)){
                            detailTable.append(",").append(detailTableName).append(",");
                            GenerateModel detailModel = new GenerateModel();
                            String detailTableComment = obj.toString().split("-")[1].trim();
                            String detailFileName = getFileName(detailTableName);
                            List<ColumnModel> idCols = getPrimryColumns(detailTableName, tableModel .getModel().getDBtype());
                            String detailTableId = idCols.get(0).getName();
                            detailModel.setObjectName(detailFileName);
                            detailModel.setModel(tableModel.getModel());
                            detailModel.setTableName(detailTableName);
                            detailModel.setTableComment(detailTableComment);
                            detailModel.setTableId(detailTableId);
                            mainAndDetailTable.put(tableName,detailModel);
                            break;
                        }
                    }
                }
            }
        }
        for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            GenerateModel model = new GenerateModel();
            String tableName = obj.toString().split("-")[0].trim();

            if(detailTable.indexOf(","+tableName+",") > -1){
                continue;
            }
            if(mainAndDetailTable.get(tableName)!=null){
                model.setDetailMode(mainAndDetailTable.get(tableName));
            }

            String tableComment = obj.toString().split("-")[1].trim();
            String fileName = getFileName(tableName);
            List<ColumnModel> idCols = getPrimryColumns(tableName, tableModel.getModel().getDBtype());
            String tableId = idCols.get(0).getName();
            model.setTableId(tableId);
            model.setObjectName(fileName);
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);
            model.setTableComment(tableComment);
            model.setAuthor(tableModel.getAuthor());

            model.setTempName("");
            model.setResourcePath(tableModel.getJspPath());
            model.setServiceName(StringUtil.initialStrToLower(fileName));

            String actionPackageName = StringUtil.addPoint(packageName, "action;");
            model.setPackageName(actionPackageName);
            String serviceImplName =    StringUtil.initialStrToUpper(fileName) + "Action.java";
            actionPackageName =  StringUtil.convertPoint(actionPackageName);
            String writeFilePath = storePath + "/src/" + actionPackageName + "/" + serviceImplName;
            model.setServicePackageName(servicePackageName.replace(";",  ""));
            model.setWriteFilePath(writeFilePath);
            readAndWriteFile(getResource("flow.action"), model);

        }

    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        readLine = filterCommon(readLine, model);
        readLine = filterJava(readLine, model);
        if (readLine.contains("@ActionContent")) {
            if (StringUtil.isBlank(model.getContent())) {
                return readFile(getResource("flow.action.content"), model);
            }
            return model.getContent();
        }
        if (readLine.contains("@ActionDetailContent")) {
            if (StringUtil.isBlank(model.getContent())&& model.getDetailMode()!= null) {
                return readFile(getResource("flow.action.detail.content"), model);
            }
            return "";
        }
        return readLine;
    }
}

