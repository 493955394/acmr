package com.acmr.dao.grabdataindb;

import acmr.util.DataTable;

public interface IGrabDataDao {
      public String getOneForInputLog();
      public DataTable getOneLogDataForInput(String logid);
      public int UpdateLogState(String taskid,int state);
      public int UpdateLogDataState(String id,int state,int version);

      public DataTable getDiyDataForInput();
      public int UpdateDiyDataState(String id,int state,int version);
}
