package com.brc.action.parse;

import com.brc.model.ColumnModel;
import com.brc.model.DBModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.StringUtil;

import java.util.Iterator;
import java.util.List;

public class XMLParser extends AbstractParser {
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

        for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            GenerateModel model = new GenerateModel();
            String tableName = obj.toString().split("-")[0].trim();
            String tableComment = obj.toString().split("-")[1].trim();
            String fileName = getFileName(tableName);
            model.setObjectName(fileName);
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
                String writeFilePath = storePath + "/domain/" + packageName +
                        "/" + fileName;
                model.setWriteFilePath(writeFilePath);
                readAndWriteFile(getResource("xml"), model);
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

