package com.brc.action.parse;

import com.brc.model.GenerateModel;
import com.brc.model.TableModel;
import com.brc.tool.StringUtil;

import java.util.Iterator;
import java.util.List;

public class ServiceParser extends AbstractParser {
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
        for (Iterator localIterator = tableModel.getSelecedTables().iterator(); localIterator.hasNext(); ) {
            Object obj = localIterator.next();
            GenerateModel model = new GenerateModel();
            String tableName = obj.toString().split("-")[0].trim();
            String tableComment = obj.toString().split("-")[1].trim();
            String fileName = getFileName(tableName);
            model.setObjectName(fileName);
            model.setModel(tableModel.getModel());
            model.setTableName(tableName);
            model.setTableComment(tableComment);
            model.setAuthor(tableModel.getAuthor());
            if (isOneDomainBol) {
                model.setTempName(fileName);

                sb_service.append(readFile(getResource("service.content"),
                        model));
                sb_service_impl.append(readFile(
                        getResource("service.impl.content"), model));
            } else {
                String resourceName = StringUtil.initialStrToLower(fileName);
                resourceName = StringUtil.addPoint(resourceName, "xml");
                model.setTempName("");
                model.setResourcePath(
                        StringUtil.convertPointToQuot(packageName) + "/" + resourceName);

                model.setPackageName(servicePackageName);
                String servicePackagePath =
                        StringUtil.convertPoint(servicePackageName);
                String serviceName = StringUtil.initialStrToUpper(fileName) +
                        "Service.java";
                String writeFilePath = storePath + "/src/" +
                        servicePackagePath + "/" + serviceName;
                model.setWriteFilePath(writeFilePath);
                readAndWriteFile(getResource("service"), model);

                String serviceImplPackageName = StringUtil.addPoint(
                        packageName, "service.impl;");
                model.setPackageName(serviceImplPackageName);
                String serviceImplName =
                        StringUtil.initialStrToUpper(fileName) + "ServiceImpl.java";
                serviceImplPackageName =
                        StringUtil.convertPoint(serviceImplPackageName);
                writeFilePath = storePath + "/src/" +
                        serviceImplPackageName + "/" + serviceImplName;
                model.setServicePackageName(servicePackageName.replace(";",
                        ""));
                model.setWriteFilePath(writeFilePath);
                readAndWriteFile(getResource("service.impl"), model);
            }
        }

        if (isOneDomainBol) {
            GenerateModel model = new GenerateModel();
            model.setModel(tableModel.getModel());
            model.setObjectName(objectName);
            model.setAuthor(tableModel.getAuthor());
            String resourceName = StringUtil.initialStrToLower(objectName);
            resourceName = StringUtil.addPoint(resourceName, "xml");
            model.setResourcePath(StringUtil.convertPointToQuot(packageName) +
                    "/" + resourceName);

            model.setPackageName(servicePackageName);
            String serviceName = StringUtil.initialStrToUpper(objectName) +
                    "Service.java";
            String servicePackagePath =
                    StringUtil.convertPoint(servicePackageName);
            String writeFilePath = storePath + "/src/" + servicePackagePath +
                    "/" + serviceName;
            model.setWriteFilePath(writeFilePath);
            model.setContent(sb_service.toString());
            readAndWriteFile(getResource("service"), model);

            String serviceImplPackageName = StringUtil.addPoint(
                    packageName, "service.impl;");
            model.setPackageName(serviceImplPackageName);
            String serviceImplName = objectName + "ServiceImpl.java";
            serviceImplPackageName =
                    StringUtil.convertPoint(serviceImplPackageName);
            writeFilePath = storePath + "/src/" +
                    serviceImplPackageName + "/" + serviceImplName;
            model.setServicePackageName(servicePackageName.replace(";", ""));
            model.setWriteFilePath(writeFilePath);
            model.setContent(sb_service_impl.toString());
            readAndWriteFile(getResource("service.impl"), model);
        }
    }

    public String parseLine(String readLine, GenerateModel model)
            throws Exception {
        readLine = filterCommon(readLine, model);
        readLine = filterJava(readLine, model);
        if (readLine.contains("@ServiceContent")) {
            if (StringUtil.isBlank(model.getContent())) {
                return readFile(getResource("service.content"), model);
            }
            return model.getContent();
        }
        if (readLine.contains("@ServiceImplContent")) {
            if (StringUtil.isBlank(model.getContent())) {
                return readFile(getResource("service.impl.content"), model);
            }
            return model.getContent();
        }

        return readLine;
    }
}

