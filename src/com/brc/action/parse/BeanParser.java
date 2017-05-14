package com.brc.action.parse;

import com.brc.model.ColumnModel;
import com.brc.model.DBModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.StringUtil;

import java.util.Iterator;
import java.util.List;

public class BeanParser extends AbstractParser {
    public void generate(TableModel tableModel)
            throws Exception {
        String storePath = tableModel.getStorePath();
        String packageName = StringUtil.addPoint(tableModel.getBasePath()
                .replace("\\", "."), "model;");

        for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            GenerateModel model = new GenerateModel();
            String tableName = obj.toString().split("-")[0].trim();
            String tableComment = obj.toString().split("-")[1].trim();
            String fileName = getFileName(tableName);
            model.setObjectName(fileName);
            model.setPackageName(packageName);
            fileName = StringUtil.addPoint(fileName, "java");
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);
            model.setTableComment(tableComment);
            model.setAuthor(tableModel.getAuthor());
            String path = StringUtil.convertPoint(packageName);
            String writeFilePath = storePath + "/src/" + path + "/" + fileName;
            model.setWriteFilePath(writeFilePath);
            readAndWriteFile(getResource("bean"), model);
        }
    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        readLine = filterCommon(readLine, model);
        String tableName = model.getTableName();
        if (readLine.contains("@fieldDeclare")) {
            StringBuffer sb = new StringBuffer();
            List<ColumnModel> columns = getColumns(tableName, model.getModel()
                    .getDBtype());
            for (ColumnModel column : columns) {
                sb.append(makeField(column));
            }
            return sb.toString();
        }
        if (readLine.contains("@method")) {
            StringBuffer sb = new StringBuffer();
            List<ColumnModel> columns = getColumns(tableName, model.getModel()
                    .getDBtype());
            for (ColumnModel column : columns) {
                sb.append(makeGetSet(column));
            }
            return sb.toString();
        }
        return readLine;
    }

    private String makeField(ColumnModel column) {
        if (column == null)
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append("\t/**\r\n");
        sb.append("\t*@type " + column.getOriginType() + "\r\n");
        sb.append("\t*@length " + column.getLength() + "\r\n");
        sb.append("\t*@canNull " + column.getNullable() + "\r\n");
        sb.append("\t*@comment " + column.getComment() + "\r\n");
        sb.append("\t**/\r\n");
        sb.append("\tprotected ").append(column.getSureType()).append(" ")
                .append(StringUtil.getPropertyName(column.getName()))
                .append(";\r\n");
        return sb.toString();
    }

    private String makeGetSet(ColumnModel column) {
        if (column == null)
            return "";
        String name = StringUtil.getPropertyName(column.getName());
        StringBuffer getSetFunction = new StringBuffer();
        getSetFunction.append("\r\n    /*  " + column.getOriginType())
                .append("  " + column.getLength())
                .append(" CanNull " + column.getNullable())
                .append("  " + column.getComment())
                .append("  */\r\n    public ").append(column.getType())
                .append(" get").append(StringUtil.initialStrToUpper(name))
                .append("() {\r\n      return this.").append(name)
                .append("; \r\n    }");
        getSetFunction.append("\r\n\r\n");
        getSetFunction.append("    public void set")
                .append(StringUtil.initialStrToUpper(name)).append("(")
                .append(column.getSureType()).append(" ").append(name)
                .append(") {\r\n      ");
        if (column.getSureType().equals("Blob")) {
            getSetFunction.append("this.").append(name).append(" = ")
                    .append(name);
        } else if (column.getSureType().equals("String")) {
            getSetFunction.append("this.").append(name).append(" = ")
                    .append(name);
        } else if ("BigDecimal".equals(column.getSureType())) {
            getSetFunction.append("if (null == ").append(name)
                    .append(") {\r\n").append("        this.").append(name)
                    .append(" = null;\r\n").append("        return ;\r\n")
                    .append("    }\r\n");
            if ((column.getComment().endsWith("价")) ||
                    (column.getComment().endsWith("金额")) ||
                    (column.getComment().endsWith("计")))
                getSetFunction.append("    ").append(name).append(" = ")
                        .append(name)
                        .append(".setScale(2,BigDecimal.ROUND_HALF_UP);\r\n")
                        .append("    ").append("this.").append(name)
                        .append(" = new BigDecimal(").append(name)
                        .append(".toString().replaceAll(\"(0+)$\", \"\"))");
            else {
                getSetFunction.append("    ").append(name).append(" = ")
                        .append(name)
                        .append(".setScale(3,BigDecimal.ROUND_HALF_UP);\r\n")
                        .append("    ").append("this.").append(name)
                        .append(" = new BigDecimal(").append(name)
                        .append(".toString().replaceAll(\"(0+)$\", \"\"))");
            }
        } else if ("java.util.Date".equals(column.getType())) {
            getSetFunction.append("if(null==").append(name)
                    .append("){\r\n    ").append("  this.").append(name)
                    .append(" = null; \r\n    ")
                    .append("  return ;\r\n    }\r\n").append("    this.")
                    .append(name).append(" = new java.sql.Date(").append(name)
                    .append(".getTime())");
        } else if ("java.sql.Blob".equals(column.getType())) {
            getSetFunction.append("if(null==").append(name)
                    .append("){\r\n    ").append("  this.").append(name)
                    .append(" = null; \r\n    ")
                    .append("  return ;\r\n    }\r\n").append("    this.")
                    .append(name).append(" = new java.sql.Blob(").append(name)
                    .append(")");
        } else {
            getSetFunction.append("this.").append(name).append(" = ")
                    .append(name);
        }
        getSetFunction.append(";\r\n").append("    }\r\n");
        return getSetFunction.toString();
    }
}

