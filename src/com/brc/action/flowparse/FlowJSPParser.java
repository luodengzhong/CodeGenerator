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

public class FlowJSPParser extends FlowAbstractParser {
    public void generate(TableModel tableModel)
            throws Exception {
        String storePath = tableModel.getStorePath();
        String jspPath = tableModel.getJspPath();
        boolean isOneDomainBol = tableModel.isOneDomainBol();
        String objectName = getObjectName(tableModel.getObjectName());
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
                            detailModel.setMainTableName(tableName);
                            detailModel.setModel(tableModel.getModel());
                            detailModel.setTableName(detailTableName);
                            detailModel.setTableComment(detailTableComment);
                            detailModel.setTableId(detailTableId);
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
            List<ColumnModel> idCols = getPrimryColumns(tableName, tableModel.getModel().getDBtype());
            String tableId = idCols.get(0).getName();
            model.setTableId(tableId);
            model.setObjectName(fileName);
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);
            model.setTableComment(tableComment);
            model.setAuthor(tableModel.getAuthor());
            String writeFilePath = storePath + "/WebContent/" +
                    StringUtil.convertPoint(jspPath) + "/";

            model.setWriteFilePath(writeFilePath +
                    StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("Approval").toString(), "jsp"));
            readAndWriteFile(getResource("flow.approval.jsp"), model);

            model.setWriteFilePath(writeFilePath + StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("Approval").toString(), "js"));
            readAndWriteFile(getResource("flow.approval.js"), model);

            if(model.getDetailMode() != null){
                model.getDetailMode().setWriteFilePath(writeFilePath +
                        StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("Detail").toString(), "jsp"));
                readAndWriteFile(getResource("flow.detail.edit.jsp"), model.getDetailMode());

            }
/*

            model.setWriteFilePath(writeFilePath +
                    StringUtil.addPoint(new StringBuilder(String.valueOf(fileName)).append("List").toString(), "jsp"));
            readAndWriteFile(getResource("flow.list.jsp"), model);

            model.setWriteFilePath(writeFilePath + StringUtil.addPoint(fileName, "js"));
            readAndWriteFile(getResource("flow.list.js"), model);
*/



            if (isOneDomainBol) {
                model.setTempName(fileName);
                String oneFileName =
                        StringUtil.initialStrToUpper(objectName);
                model.setObjectName(oneFileName);
            } else {
                model.setTempName("");
            }

        }
    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        readLine = filterCommon(readLine, model);
        readLine = filterJava(readLine, model);
        readLine = filterDetail(readLine,model);
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
            List<ColumnModel> columns = getNormalColsDet(tableName, model  .getModel().getDBtype());
            String mainTableName = model.getMainTableName();
            String maintableId = "";
            if(mainTableName!=null && !"".equals(mainTableName)){
                List<ColumnModel> idcolumns = getPrimryColumns(mainTableName, model.getModel().getDBtype());
                maintableId = idcolumns.get(0).getName();
            }
            for (ColumnModel column : columns) {
                if(model.getMainTableName() == null && fixedColumnList.contains(column.getName())){
                    continue;
                }
                if(maintableId.equals(column.getName())){
                    continue;
                }
                sb.append(filterColumn(readLine, column));
            }
            return sb.toString();
        }
        if (readLine.contains("@ApprovalDetailContent")) {
            if (StringUtil.isBlank(model.getContent())&& model.getDetailMode()!= null) {
                return readFile(getResource("flow.approval.detail.js"), model.getDetailMode());
            }
            return "";
        }
        return readLine;
    }
    protected String filterDetail(String readLine, GenerateModel model) throws Exception {

        String mainTableName = model.getMainTableName();
        if(mainTableName != null && !"".equals(mainTableName)){
            List<ColumnModel> columns = getPrimryColumns(mainTableName, model.getModel().getDBtype());
            String maintableId = columns.get(0).getName();
            String mainObject = getFileName(mainTableName);
            readLine = StringUtil.replaceAll(readLine, "@maintableId", StringUtil.getPropertyName(maintableId));
            readLine = StringUtil.replaceAll(readLine, "@mainObject", StringUtil.initialStrToLower(mainObject));
        }
        return readLine;
    }

}

