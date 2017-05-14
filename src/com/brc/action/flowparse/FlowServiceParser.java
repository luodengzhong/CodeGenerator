package com.brc.action.flowparse;

import com.brc.action.parse.AbstractParser;
import com.brc.model.ColumnModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.StringUtil;

import java.util.*;

public class FlowServiceParser extends FlowAbstractParser {
    public void generate(TableModel tableModel)
            throws Exception {
        String storePath = tableModel.getStorePath();
        String packageName = tableModel.getBasePath().replace("\\", ".");
        String servicePackageName =
                StringUtil.addPoint(packageName, "service;");
        boolean isOneDomainBol = tableModel.isOneDomainBol();
        String objectName = getObjectName(tableModel.getObjectName());
        StringBuffer sb_service = new StringBuffer();
        StringBuffer sb_service_impl = new StringBuffer();
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
            List<ColumnModel> columns = getPrimryColumns(tableName, tableModel.getModel().getDBtype());
            String tableId = columns.get(0).getName();
            model.setObjectName(fileName);
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);
            model.setTableComment(tableComment);
            model.setTableId(tableId);
            model.setAuthor(tableModel.getAuthor());


            String resourceName = StringUtil.initialStrToLower(fileName);
            resourceName = StringUtil.addPoint(resourceName, "xml");
            model.setTempName("");
            model.setResourcePath(
                    StringUtil.convertPointToQuot(packageName) + "/" + resourceName);

            model.setPackageName(servicePackageName);
            String servicePackagePath =
                    StringUtil.convertPoint(servicePackageName);
            String serviceName = StringUtil.initialStrToUpper(fileName) + "Service.java";
            String writeFilePath = storePath + "/src/" + servicePackagePath + "/" + serviceName;
            model.setWriteFilePath(writeFilePath);
            readAndWriteFile(getResource("flow.service"), model);

            String serviceImplPackageName = StringUtil.addPoint( packageName, "service.impl;");
            model.setPackageName(serviceImplPackageName);
            String serviceImplName = StringUtil.initialStrToUpper(fileName) + "ServiceImpl.java";
            serviceImplPackageName = StringUtil.convertPoint(serviceImplPackageName);
            writeFilePath = storePath + "/src/" + serviceImplPackageName + "/" + serviceImplName;
            model.setServicePackageName(servicePackageName.replace(";", ""));
            model.setWriteFilePath(writeFilePath);
            readAndWriteFile(getResource("flow.service.impl"), model);

        }
    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        readLine = filterCommon(readLine, model);
        readLine = filterJava(readLine, model);
        if (readLine.contains("@ServiceContent")) {
            if (StringUtil.isBlank(model.getContent())) {
                return readFile(getResource("flow.service.content"), model);
            }
            return model.getContent();
        }

        if (readLine.contains("@ServiceDetailContent")) {
            if (StringUtil.isBlank(model.getContent())&& model.getDetailMode()!= null) {
                return readFile(getResource("flow.service.detail.content"), model.getDetailMode());
            }
            return "";
        }
        if (readLine.contains("@ServiceImplContent")) {
            if (StringUtil.isBlank(model.getContent())) {
                return readFile(getResource("flow.service.impl.content"), model);
            }
            return model.getContent();
        }
        if (readLine.contains("@ServiceImplDetailContent")) {
            if (StringUtil.isBlank(model.getContent())&& model.getDetailMode()!= null) {
                return readFile(getResource("flow.service.impl.detail.content"), model.getDetailMode());
            }
            return "";
        }

        return readLine;
    }
}

