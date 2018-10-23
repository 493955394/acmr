package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.model.zhzs.IndexList;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.zhzs.IndexTaskService;
import org.owasp.esapi.tags.ELEncodeFunctions;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class zstask extends BaseAction {

    public ModelAndView main() throws IOException {
        String icode = this.getRequest().getParameter("icode");
        String right=this.getRequest().getParameter("right");
        IndexTaskService task = new IndexTaskService();
        PageBean<IndexTask> page=new PageBean<>();
        List<IndexTask> alllist=task.getAllTask(icode);
        List<IndexTask> tasklist = task.getTaskByIcode(icode,page.getPageNum()-1,page.getPageSize());
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        sb.append("?m=turn&icode="+icode);
        page.setData(tasklist);
        page.setUrl(sb.toString());
        page.setTotalRecorder(alllist.size());
        return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/taskindex").addObject("page",page).addObject("icode",icode).addObject("right",right);
    }

    /**
    * @Description: 翻页
    * @Param: []
    * @return: acmr.web.entity.ModelAndView
    * @Author: lyh
    * @Date: 2018/9/19
    */

    public ModelAndView turn() throws IOException {
        HttpServletRequest req = this.getRequest();
        String pjax = req.getHeader("X-PJAX");
        String icode=req.getParameter("icode");
        PageBean<IndexTask> page=new PageBean<>();
        IndexTaskService indexTaskService=new IndexTaskService();
        List<IndexTask> alllist=indexTaskService.getAllTask(icode);
        List<IndexTask> taskList=indexTaskService.getTaskByIcode(icode,page.getPageNum()-1,page.getPageSize());
        page.setData(taskList);
        page.setTotalRecorder(alllist.size());
        StringBuffer sb = new StringBuffer();
        sb.append(req.getRequestURL());
        sb.append("?m=turn&icode="+icode);
        page.setUrl(sb.toString());
        if (StringUtil.isEmpty(pjax)) {
            PubInfo.printStr("isempty");
            this.getResponse().sendRedirect(this.getContextPath()+"/zbdata/zstask.htm?icode="+icode+"&right=2");
        } else {
            PubInfo.printStr("pjax");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/tasktable").addObject("page",page);
        }
        return null;
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
    public ModelAndView findTask() throws IOException{
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        IndexTaskService indexTaskService =new IndexTaskService();
        String time = StringUtil.toLowerString(req.getParameter("time"));
        String icode = PubInfo.getString(req.getParameter("id"));
        List<IndexTask> indexTask = new ArrayList<>();
       List<IndexTask> indexTaskList =  new ArrayList<>();
        if(time.equals("")){
            indexTask = indexTaskService.getAllTask(icode);

        }
        else {
            indexTask = indexTaskService.findByTime(time,icode);
        }
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        PageBean<IndexTask> page=new PageBean<>();
        StringBuffer sb = new StringBuffer();
        sb.append(this.getRequest().getRequestURI());
        sb.append("?m=findTask&time="+time+"&id="+icode);
        if(time.equals("")){
            indexTaskList = indexTaskService.getTaskByIcode(icode,page.getPageNum()-1,page.getPageSize());
        }
        else {
            indexTaskList = indexTaskService.findByTime(time,icode,page.getPageNum() - 1,page.getPageSize());
        }
        page.setData(indexTaskList);
        page.setTotalRecorder(indexTask.size());
        page.setUrl(sb.toString());
        if (StringUtil.isEmpty(pjax)) {

            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/taskindex").addObject("page",page).addObject("icode",icode);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zstask/tasktable").addObject("page",page);
        }
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
        }
        else {
            data.setReturncode(400);
            this.sendJson(data);
        }
    }
}
