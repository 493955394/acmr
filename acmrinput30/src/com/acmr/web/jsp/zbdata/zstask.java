package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
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
        CreateTaskService task = new CreateTaskService();
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
        IndexTaskService indexTaskService=new IndexTaskService();
        this.sendJson(indexTaskService.findSession(sessionid));
    }
}
