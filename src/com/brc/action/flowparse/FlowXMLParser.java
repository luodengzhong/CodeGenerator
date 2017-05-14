package com.brc.action.flowparse;

import com.brc.action.parse.AbstractParser;
import com.brc.model.ColumnModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.FormatTool;
import com.brc.tool.StringUtil;

import java.util.*;

public class FlowXMLParser extends FlowAbstractParser {

    protected String beginWrite() {
        return getResource("xml.begin");
    }

    protected String endWrite() {
        return getResource("xml.end");
    }

    public void generate(TableModel tableModel) throws Exception {
        String storePath = tableModel.getStorePath();
        String packageName = tableModel.getBasePath().replace("\\", ".");
        boolean isOneDomainBol = tableModel.isOneDomainBol();
        String objectName = getObjectName(tableModel.getObjectName());
        StringBuffer sb = new StringBuffer();
        StringBuffer detailTable = new StringBuffer();
        Map<String, GenerateModel> mainAndDetailTable = new HashMap<String, GenerateModel>();
        for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            String tableName = obj.toString().split("-")[0].trim();
            if (detailTable.indexOf(tableName) > -1) {
                continue;
            }
            List<ColumnModel> columns = getPrimryColumns(tableName, tableModel.getModel().getDBtype());
            String tableId = columns.get(0).getName();
            //查询明细表：
            for (Iterator interator = tableModel.getSelecedTables().iterator(); interator.hasNext(); ) {
                Object detail = interator.next();
                String detailTableName = detail.toString().split("-")[0].trim();
                if (!tableName.equals(detailTableName)) {
                    List<ColumnModel> cols = getNormalColsDet(detailTableName, tableModel.getModel().getDBtype());
                    for (ColumnModel column : cols) {
                        if (column.getName().equals(tableId)) {
                            detailTable.append(",").append(detailTableName).append(",");
                            GenerateModel detailModel = new GenerateModel();
                            String detailTableComment = obj.toString().split("-")[1].trim();
                            String detailFileName = getFileName(detailTableName);
                            List<ColumnModel> idCols = getPrimryColumns(detailTableName, tableModel.getModel().getDBtype());
                            String detailTableId = idCols.get(0).getName();
                            detailModel.setObjectName(detailFileName);
                            detailModel.setModel(tableModel.getModel());
                            detailModel.setTableName(detailTableName);
                            detailModel.setTableComment(detailTableComment);
                            detailModel.setTableId(detailTableId);
                            detailModel.setMainTableName(tableName);
                            mainAndDetailTable.put(tableName, detailModel);
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
            if (detailTable.indexOf("," + tableName + ",") > -1) {
                continue;
            }
            if (mainAndDetailTable.get(tableName) != null) {
                model.setDetailMode(mainAndDetailTable.get(tableName));
            }

            String tableComment = obj.toString().split("-")[1].trim();
            String fileName = getFileName(tableName);
            model.setObjectName(fileName);
            List<ColumnModel> idCols = getPrimryColumns(tableName, tableModel.getModel().getDBtype());
            String tableId = idCols.get(0).getName();
            model.setTableId(tableId);
            model.setPackageName(packageName);
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);
            model.setTableComment(tableComment);
            model.setAuthor(tableModel.getAuthor());
            if (isOneDomainBol) {
                sb.append(readFile(getResource("xml"), model));
            } else {
                fileName = StringUtil.addPoint(StringUtil.initialStrToLower(fileName), "xml");
                packageName = StringUtil.convertPoint(packageName);
                String writeFilePath = storePath + "/domain/" + packageName + "/" + fileName;
                model.setWriteFilePath(writeFilePath);
                readAndWriteFile(getResource("flow.xml"), model);
            }
        }
        if (isOneDomainBol) {
            String fileName = StringUtil.addPoint(StringUtil.initialStrToLower(objectName), "xml");
            packageName = StringUtil.convertPoint(packageName);
            String writeFilePath = storePath + "/domain/" + packageName + "/" +
                    fileName;
            writeFile(writeFilePath, sb.toString());
        }
    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        readLine = filterCommon(readLine, model);
        readLine = filterDetailEntity(readLine, model);
        String tableName = model.getTableName();
        if (readLine.contains("@id")) {
            StringBuffer sb = new StringBuffer();
            List<ColumnModel> columns = getPrimryColumns(tableName, model
                    .getModel().getDBtype());
            for (ColumnModel column : columns) {
                sb.append(filterColumn(readLine, column));
            }
            return sb.toString();
        }
        if (readLine.contains("@property")) {
            StringBuffer sb = new StringBuffer();
            List<ColumnModel> columns = getNormalColsDet(tableName, model.getModel().getDBtype());
            for (ColumnModel column : columns) {
                if (model.getMainTableName() == null && fixedColumnList.contains(column.getName())) {
                    continue;
                }
                sb.append(filterColumn(readLine, column));
            }
            return sb.toString();
        }
        if (readLine.contains("@detailEntity")) {
            if (StringUtil.isBlank(model.getContent())&& model.getDetailMode()!= null) {
                return readFile(getResource("flow.xml.detail"), model.getDetailMode());
            }
            return "";
        }
        return readLine;
    }

    protected String filterDetailEntity(String readLine, GenerateModel model) throws Exception {
        String mainTableName = model.getMainTableName();
        if(mainTableName != null && !"".equals(mainTableName)){
            List<ColumnModel> columns = getPrimryColumns(mainTableName, model.getModel().getDBtype());
            String maintableId = columns.get(0).getName();
            readLine = StringUtil.replaceAll(readLine, "@MAIN_TABLE_ID", maintableId);
            readLine = StringUtil.replaceAll(readLine, "@mainTableId", StringUtil.getPropertyName(maintableId));
        }

        return readLine;
    }
}

