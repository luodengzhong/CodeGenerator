package com.brc.action.flowparse;

import com.brc.db.DBUtil;
import com.brc.db.DBUtilFactory;
import com.brc.model.ColumnModel;
import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.FormatTool;
import com.brc.tool.PropertyUtil;
import com.brc.tool.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class FlowAbstractParser {
    protected static final Log log = LogFactory.getLog(FlowAbstractParser.class);
    private static ResourceBundle bundle = null;
    protected String[] fixedColumn = {"SUBJECT", "BILL_CODE", "ORGAN_ID", "ORGAN_NAME", "CENTER_ID", "CENTER_NAME", "DEPT_ID",
            "DEPT_NAME", "POSITION_ID", "POSITION_NAME", "PERSON_MEMBER_ID", "PERSON_MEMBER_NAME", "FULL_ID", "FILLIN_DATE", "STATUS", "VERSION"};
    protected List<String> fixedColumnList = new ArrayList<>();
    {
        for(String col: fixedColumn)
            fixedColumnList.add(col);
    }
    public static String getResource(String key) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("parse");
        }
        return bundle.getString(key);
    }

    public abstract void generate(TableModel paramTableModel)
            throws Exception;

    public abstract String parseLine(String paramString, GenerateModel paramGenerateModel)
            throws Exception;

    protected String beginWrite() {
        return "";
    }

    protected String endWrite() {
        return "";
    }

    protected DBUtil getDBUtil(String type) {
        return DBUtilFactory.getDBUtil(type);
    }

    protected void readAndWriteFile(String readFilePath, GenerateModel model)
            throws Exception {
        try {
            InputStream in = PropertyUtil.class
                    .getResourceAsStream(readFilePath);
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String writeFilePath = model.getWriteFilePath();
            if (createFile(writeFilePath)) {
                OutputStreamWriter write = new OutputStreamWriter(
                        new FileOutputStream(writeFilePath), "UTF-8");
                BufferedWriter bw = new BufferedWriter(write);
                bw.write(beginWrite());
                bw.newLine();

                while (br.ready()) {
                    String myreadline = br.readLine();
                    myreadline = parseLine(myreadline, model);
                    bw.write(myreadline);
                    bw.newLine();
                }
                bw.write(endWrite());
                bw.flush();
                bw.close();
                write.close();
            }
            br.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    protected String readFile(String readFilePath, GenerateModel model)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream in = PropertyUtil.class
                    .getResourceAsStream(readFilePath);
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String myreadline = "";
            while (br.ready()) {
                myreadline = br.readLine();
                sb.append(parseLine(myreadline, model));
                sb.append("\r\n");
            }
            br.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return sb.toString();
    }

    protected void writeFile(String writeFilePath, String content)
            throws Exception {
        try {
            if (createFile(writeFilePath)) {
                OutputStreamWriter write = new OutputStreamWriter(
                        new FileOutputStream(writeFilePath), "UTF-8");
                BufferedWriter bw = new BufferedWriter(write);
                bw.write(beginWrite());
                bw.newLine();
                bw.write(content);
                bw.newLine();
                bw.write(endWrite());
                bw.flush();
                bw.close();
                write.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    private boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            log.info("创建单个文件" + destFileName + "，目标文件已存在,删除后重建！");
            file.delete();
        }
        if (destFileName.endsWith(File.separator)) {
            log.info("创建单个文件" + destFileName + "失败，目标不能是目录！");
            return false;
        }
        if (!file.getParentFile().exists()) {
            log.info("目标文件所在路径不存在，准备创建。。。");
            if (!file.getParentFile().mkdirs()) {
                log.info("创建目录文件所在的目录失败！");
                return false;
            }
        }
        try {
            if (file.createNewFile()) {
                log.info("创建单个文件" + destFileName + "成功！");
                return true;
            }
            log.info("创建单个文件" + destFileName + "失败！");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("创建单个文件" + destFileName + "失败！");
        }
        return false;
    }

    protected String filterCommon(String readLine, GenerateModel model) {
        String packageName = model.getPackageName();
        String objectName = model.getObjectName();
        String tableComment = model.getTableComment();
        String tableName = model.getTableName();
        String tableId = model.getTableId();
        GenerateModel detailMode = model.getDetailMode();
        String currentTime = FormatTool.getCurrentDate(3);
        readLine = StringUtil.replaceAll(readLine, "@entity", "\r\n\t");
        readLine = StringUtil.replaceAll(readLine, "@packageName", "package " + packageName);
        readLine = StringUtil.replaceAll(readLine, "@currentTime", currentTime);
        readLine = StringUtil.replaceAll(readLine, "@tableName", tableName);
        readLine = StringUtil.replaceAll(readLine, "@TABLE_ID", tableId);
        readLine = StringUtil.replaceAll(readLine, "@tableId", StringUtil.getPropertyName(tableId));
        readLine = StringUtil.replaceAll(readLine, "@TABLENAME", StringUtil.isBlank(tableName) ? "" : tableName.toUpperCase());
        readLine = StringUtil.replaceAll(readLine, "@Object", objectName);
        readLine = StringUtil.replaceAll(readLine, "@tableComment", tableComment);
        readLine = readLine.replaceAll("@object", StringUtil.initialStrToLower(objectName));
        readLine = readLine.replaceAll("@OBJECT", objectName.toUpperCase());
        if(detailMode != null){
            readLine = StringUtil.replaceAll(readLine, "@DETAILTABLENAME", detailMode != null ? detailMode.getTableName() : "");
            readLine = readLine.replaceAll("@detailObject", StringUtil.initialStrToLower(detailMode.getObjectName()));
        }

        readLine = readLine.replaceAll("@Author", StringUtil.isBlank(model.getAuthor()) ? "" : model.getAuthor());
        return readLine;
    }

    protected String filterColumn(String readLine, ColumnModel column) {
        if (column == null)
            return readLine;
        String columnName = column.getName();
        String name = StringUtil.getPropertyName(columnName);
        String type = column.getType();
        String length = column.getLength();
        String required = column.getNullable();
        String label = column.getComment();
        required = (required != null) && (required.toUpperCase().equals("N")) ? "true" :
                "false";
        readLine = StringUtil.replaceAll(readLine, "@id", "\r\n\t\t");
        readLine = StringUtil.replaceAll(readLine, "@property", "\r\n\t\t");
        readLine = StringUtil.replaceAll(readLine, "@columnName", columnName);
        readLine = StringUtil.replaceAll(readLine, "@name", name);
        readLine = StringUtil.replaceAll(readLine, "@type", type);
        readLine = StringUtil.replaceAll(readLine, "@length", length);
        readLine = StringUtil.replaceAll(readLine, "@required", required);
        readLine = StringUtil.replaceAll(readLine, "@label", label);
        return readLine;
    }

    protected String filterJava(String readLine, GenerateModel model) {
        String tempName = model.getTempName();
        String resourcePath = model.getResourcePath();
        String servicePackageName = model.getServicePackageName();
        String serviceName = model.getServiceName();
        GenerateModel detailModel = model.getDetailMode();
        readLine = StringUtil.replaceAll(readLine, "@tempName", tempName);
        readLine = StringUtil.replaceAll(readLine, "@resourcePath",
                resourcePath);
        readLine = StringUtil.replaceAll(readLine, "@serviceName", serviceName);
        readLine = StringUtil.replaceAll(readLine, "@servicePackageName",
                servicePackageName);

        readLine = readLine.replaceAll("@Author",
                StringUtil.isBlank(model.getAuthor()) ? "" : model.getAuthor());
        return readLine;
    }

    protected List<ColumnModel> getColumns(String tableName, String type)
            throws Exception {
        DBUtil util = getDBUtil(type);
        List columns = ColumnModel.parseColumnDetail(util
                .selectTableDetail(tableName));
        if ((columns == null) || (columns.size() == 0))
            throw new Exception("获取表字段失败！");
        return columns;
    }

    protected List<ColumnModel> getPrimryColumns(String tableName, String type)
            throws Exception {
        DBUtil util = getDBUtil(type);
        List columns = ColumnModel.parseColumnDetail(util
                .selectPrimryColumns(tableName));
        return columns;
    }

    protected List<ColumnModel> getNormalColsDet(String tableName, String type)
            throws Exception {
        DBUtil util = getDBUtil(type);
        List columns = ColumnModel.parseColumnDetail(util
                .selectTableNormalColsDet(tableName));
        if ((columns == null) || (columns.size() == 0))
            throw new Exception("获取表字段失败！");
        return columns;
    }

    protected String getFileName(String tableName) {
        int i = tableName.indexOf("_");
        if (i != -1) {
            tableName = tableName.substring(i + 1);
        }
        return StringUtil.initialStrToUpper(
                StringUtil.getPropertyName(tableName));
    }

    protected String getObjectName(String objectName) {
        if (StringUtil.isBlank(objectName)) {
            return "";
        }

        int i = objectName.indexOf("_");
        if (i != -1) {
            objectName = StringUtil.getPropertyName(objectName);
        }
        return StringUtil.initialStrToUpper(objectName);
    }
}

