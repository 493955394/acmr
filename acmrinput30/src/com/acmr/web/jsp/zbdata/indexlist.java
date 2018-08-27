package com.acmr.web.jsp.zbdata;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.service.zhzs.IndexListService;

import java.io.IOException;
import java.util.List;

public class indexlist extends BaseAction {
    public ModelAndView main() throws IOException {
        String test="this is the listjps";
        List<DataTableRow> indexlist= new IndexListService().getIndexlist();
        return new ModelAndView("/WEB-INF/jsp/zhzs/indexlist").addObject("test",test).addObject("indexlist",indexlist);
    }

}
