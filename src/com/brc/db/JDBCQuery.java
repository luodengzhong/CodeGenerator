package com.brc.db;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBCQuery {
    private static final Log log = LogFactory.getLog(JDBCQuery.class);

    public static List<String[]> excuteSQL(String selectSQL, Connection connection) {
        List result_list = new ArrayList();
        try {
            if ((connection == null) || (connection.isClosed())) {
                System.out.println("Connection is closed or not exists !");
                log.error("Connection is closed or not exists !");
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

            log.info("selectSQL:" + selectSQL);
            ResultSet rs = null;
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(selectSQL);
                rs = preparedStatement.executeQuery();
                if (rs == null) {
                    return null;
                }
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String[] row_array = null;
                while (rs.next()) {
                    row_array = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row_array[i] = rs.getString(i + 1);
                    }
                    result_list.add(row_array);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (rs != null)
                        rs.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (Exception e2) {
                    e.printStackTrace();
                }
                try {
                    if (connection != null)
                        connection.close();
                } catch (Exception e3) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (connection != null)
                        connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result_list;
    }

    public static List<String[]> excuteSQL(String selectSQL, String[] parr, Connection connection) {
        List result_list = new ArrayList();
        try {
            if ((connection == null) || (connection.isClosed())) {
                System.out.println("Connection is closed or not exists !");
                return null;
            }
            log.info("selectSQL:" + selectSQL);
            for (String a : parr) {
                log.info("param:" + a);
            }
            ResultSet rs = null;
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.prepareStatement(selectSQL);
                for (int i = 0; i < parr.length; i++)
                    try {
                        if (parr[i] != null)
                            preparedStatement.setString(i + 1, parr[i]);
                        else
                            preparedStatement.setString(i + 1, "");
                    } catch (Exception localException2) {
                    }
                rs = preparedStatement.executeQuery();
                if (rs == null) {
                    return null;
                }
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                String[] row_array = null;
                while (rs.next()) {
                    row_array = new String[columnCount];
                    for (int i = 0; i < columnCount; i++) {
                        row_array[i] = rs.getString(i + 1);
                    }
                    result_list.add(row_array);
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    if (rs != null)
                        rs.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    if (connection != null)
                        connection.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (connection != null)
                        connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();


        }
        return result_list;
    }
}

