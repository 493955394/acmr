package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.zhzs.CreateTaskService;
import com.acmr.service.zhzs.IndexTaskService;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class zstask extends BaseAction {

    public ModelAndView main() throws IOException {
        String icode = this.getRequest().getParameter("id");
        IndexTaskService task = new IndexTaskService();
        List<IndexTask> tasklist = task.getTaskByIcode(icode);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/taskindex").addObject("tasklist",tasklist);
    }
    /**
    * @Description:  根据传来的session在数据临时表中找是否有记录过的
    * @Param: []
    * @return: void
    * @Author: lyh
    * @Date: 2018/9/13
    */
    public void findSession() throws IOException {
        PubInfo.printStr("===================findsession");
        HttpServletRequest req=this.getRequest();
        String sessionid=req.getSession().getId();
        PubInfo.printStr("id:"+sessionid);
        String taskcode=req.getParameter("taskcode");
        IndexTaskService indexTaskService=new IndexTaskService();
        this.sendJson(indexTaskService.findSession(sessionid,taskcode));
    }

    /**
     * 指数任务的查询
     * @return
     * @throws IOException
     */
    public void findTask() throws IOException{
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        IndexTaskService indexTaskService =new IndexTaskService();
        String time = PubInfo.getString(req.getParameter("time"));
        String icode = PubInfo.getString(req.getParameter("icode"));
        IndexTask indexTask = indexTaskService.findByTime(time,icode);
        this.sendJson(indexTask);
        return;
    }

    /**
     * 删除任务
     * @throws IOException
     */
    public void delTask() throws IOException{
        HttpServletRequest req = this.getRequest();
        // 获取要删的数据
        JSONReturnData data = new JSONReturnData("");
        IndexTaskService indexTaskService =new IndexTaskService();
        String code = PubInfo.getString(req.getParameter("code"));
        int result = indexTaskService.delTask(code);
        if(result == 1){
            data.setReturncode(200);
            this.sendJson(data);
            return;
        }
    }
}
