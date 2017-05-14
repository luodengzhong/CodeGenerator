package com.brc.db;

import java.util.List;

public abstract interface DBUtil
{
  public abstract List<String[]> selectAllTables();

  public abstract List<String[]> selectTable(String paramString);

  public abstract List<String[]> selectTableDetail(String paramString);

  public abstract List<String[]> selectTableNormalColsDet(String paramString);

  public abstract List<String[]> selectPrimryColumns(String paramString);
}

