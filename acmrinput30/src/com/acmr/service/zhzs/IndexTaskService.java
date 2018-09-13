package com.acmr.service.zhzs;

import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.web.jsp.Index;

import java.util.ArrayList;
import java.util.List;

public class IndexTaskService {
    public boolean findSession(String sessionid,String taskcode){
        Boolean bool= IndexTaskDao.Fator.getInstance().getIndexdatadao().hasData(sessionid,taskcode);
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
    /**
     * 任务列表按照任务时间期查询
     */
    public IndexTask findByTime(String time,String icode){
        DataTableRow data = IndexTaskDao.Fator.getInstance().getIndexdatadao().findTask(time,icode).getRows().get(0);
        IndexTask task = new IndexTask();
        task.setCode(data.getString("code"));
        task.setAyearmon(data.getString("ayearmon"));
        task.setIndexcode(data.getString("indexcode"));
        task.setCreatetime(data.getDate("createtime"));
        task.setUpdatetime(data.getDate("updatetime"));
        return task;
    }
    /**
     * 指数任务删除
     */
    public int delTask(String code){
        return IndexTaskDao.Fator.getInstance().getIndexdatadao().delTask(code);
    }
}
