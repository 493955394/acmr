package com.acmr.service.zhzs;

import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.IndexTask;

import java.util.ArrayList;
import java.util.List;

public class IndexTaskService {
    public boolean findSession(String sessionid){
        Boolean bool= IndexTaskDao.Fator.getInstance().getIndexdatadao().hasData(sessionid);
        return bool;
    }

    /**
     * 任务列表展示
     */
    public ArrayList<IndexTask> getTaskByIcode(String icode){
        ArrayList<IndexTask> indexTasks = new ArrayList<>();
        List<DataTableRow> data = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskList(icode).getRows();
        for (int i = 0; i <data.size() ; i++) {
            IndexTask task = new IndexTask();
            task.setCode(data.get(i).getString("code"));
            task.setAyearmon(data.get(i).getString("ayearmon"));
            task.setIndexcode(data.get(i).getString("indexcode"));
            task.setCreatetime(data.get(i).getDate("createtime"));
            task.setUpdatetime(data.get(i).getDate("updatetime"));
            indexTasks.add(task);
        }
        return indexTasks;
    }
}
