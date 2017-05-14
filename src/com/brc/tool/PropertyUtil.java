package com.brc.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtil {
    protected static final Log log = LogFactory.getLog(PropertyUtil.class);
    private static final String fileURL = "/system.properties";
    private static final String saveFileURL = "C:\\codeGenerate.properties";
    private static Properties prop;

    private static void init() {
        prop = new Properties();
        File file = new File("C:\\codeGenerate.properties");
        InputStream in = null;
        try {
            if (!file.exists())
                in = PropertyUtil.class.getResourceAsStream("/system.properties");
            else {
                in = new FileInputStream(file);
            }
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getProperty(String key) {
        if (prop == null) {
            init();
        }
        return prop.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (prop == null) {
            init();
        }
        return prop.getProperty(key, defaultValue);
    }

    public static void setProperty(String key, String value) {
        if (prop == null) {
            init();
        }
        prop.setProperty(key, value);
    }

    public static void writeData() {
        Properties updateProp = new Properties();
        InputStream fis = null;
        OutputStream fos = null;
        try {
            File file = new File("C:\\codeGenerate.properties");
            if (!file.exists())
                file.createNewFile();
            fis = new FileInputStream(file);
            updateProp.load(fis);
            fis.close();
            fos = new FileOutputStream(file);

            Map toSaveMap = new HashMap();
            Set keys = prop.keySet();
            for (Iterator itr = keys.iterator(); itr.hasNext(); ) {
                String key = (String) itr.next();
                Object value = prop.get(key);
                toSaveMap.put(key, value);
            }

            updateProp.putAll(toSaveMap);
            updateProp.store(fos, "Update value");
            fos.close();
        } catch (IOException e) {
            log.error("Visit /system.properties for updating  value error");
            try {
                fos.close();
                fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                fos.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
    }
}

