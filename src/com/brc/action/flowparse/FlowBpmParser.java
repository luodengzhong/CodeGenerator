package com.brc.action.flowparse;

import com.brc.model.ColumnModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.FormatTool;
import com.brc.tool.StringUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FlowBpmParser extends FlowAbstractParser {
    public void generate(TableModel tableModel)
            throws Exception {
        String storePath = tableModel.getStorePath();
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

            String fileName = getFileName(tableName);
            model.setObjectName(fileName);
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);

            String bpmFileName = StringUtil.initialStrToLower(fileName) + "Proc.bpmn";
            String writeFilePath = storePath + "/config/bpm/" + bpmFileName;
            model.setWriteFilePath(writeFilePath);
            readAndWriteFile(getResource("flow.bpm"), model);

        }

    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        String objectName = model.getObjectName();
        readLine = readLine.replaceAll("@object", StringUtil.initialStrToLower(objectName));
        return readLine;
    }
}



