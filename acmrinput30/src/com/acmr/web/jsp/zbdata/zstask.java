package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.zhzs.IndexTask;
import com.acmr.service.zhzs.CreateTaskService;


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
}
