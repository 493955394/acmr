package com.acmr.web.jsp.zbdata;

import acmr.cubeinput.MetaTableException;
import acmr.cubeinput.service.metatable.entity.SqlWhere;
import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.constants.Const;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.range.Pinfo;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zhzs.IndexListService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    private IndexListService indexService = new IndexListService();
    /**
     * 获取service层对象（cube）
     *
     * @author chenyf
     */
    private MetaServiceManager metaService = MetaService.getMetaService("reg");


    public ModelAndView editIndex(){
        /* 第一个分页显示*/
        String code = this.getRequest().getParameter("id");
        String proname =null;
        IndexListService indexListService=new IndexListService();
        IndexList list =indexListService.getData(code);
        String procode = list.getProcode();
        if(procode!= null && procode!=""){//处理为空的步骤
            IndexList list1 =indexListService.getData(procode);
             proname = list1.getCname();
        }
        ArrayList<IndexList> indexlist= new IndexListService().getIndexList();
        /* 第一个分页显示*/

        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("proname",proname).addObject("list",list).addObject("indexlist",indexlist);

    }
    /**
     *
     * 获取树形结构
     *
     *
     * @throws IOException
     * @author chenyf
     */
    public void findZbTree() throws  IOException {
        String code = this.getRequest().getParameter("id");
        if (StringUtil.isEmpty(code)){
            ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd");
        }
        ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd",code);
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (int i = 0; i <nodes.size() ; i++) {
            ArrayList<CubeNode> childnodes= RegdataService.getRegSubNodes("cuscxnd",nodes.get(i).getCode());
            TreeNode treeNode = new TreeNode();
            treeNode.setId(nodes.get(i).getCode());
            Boolean ifd ;
            if(childnodes.size()>0){
                ifd = true;
            }else {
                ifd = false;
            }
            treeNode.setIsParent(ifd);
            treeNode.setName(nodes.get(i).getCname());
            treeNode.setPid(code);
            list.add(treeNode);
        }
        this.sendJson(list);
       /* List<SqlWhere> where1 = new ArrayList<SqlWhere>();
        String code = this.getRequest().getParameter("id");
        if (StringUtil.isEmpty(code)) {
            where1.add(new SqlWhere("procode", "NULL", 00));
        } else {
            where1.add(new SqlWhere("procode", code, 10));
        }
        DataTable queryData = metaService.getTree(null, where1, "sortcode");
        List<DataTableRow> rows = queryData.getRows();
        List<TreeNode> list = new ArrayList<TreeNode>();
        for (int i = 0; i < rows.size(); i++) {
            DataTableRow row = rows.get(i);
            TreeNode treeNode = new TreeNode();
            treeNode.setId(row.getString("code"));
            String ifdata = row.getString("ifdata");
            Boolean ifd = true;
            if (Const.IFDATA.equals(ifdata)) {
                where1.clear();
                where1.add(new SqlWhere("procode", row.getString("code"), 10));
                int queryCount = metaService.QueryCount(where1);
                if (queryCount > 0) {
                    treeNode.setIconSkin("icon01");
                } else {
                    ifd = false;
                }
            }
            treeNode.setIsParent(ifd);
            treeNode.setName(row.getString("cname"));
            treeNode.setPid(row.getString("procode"));
            list.add(treeNode);
        }
        this.sendJson(list);*/
    }
    public void choosereg(){
        String code = this.getRequest().getParameter("id");
    }
}
