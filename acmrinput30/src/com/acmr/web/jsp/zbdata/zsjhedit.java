package com.acmr.web.jsp.zbdata;

import acmr.cubequery.service.CubeQuerySev;
import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zhzs.IndexListService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class zsjhedit extends BaseAction {
    //指数计划编辑页面
    /**
     * 获取service层对象（cube）
     *
     * @author chenyf
     */

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
    }

    /**
     * @author djj
     * 查找下一级地区
     * @throws IOException
     */
    public void getResultLeft() throws  IOException{
        HttpServletRequest req = this.getRequest();
        String procode = PubInfo.getString(req.getParameter("procode"));
        if (StringUtil.isEmpty(procode)){
            ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd");
        }
        ArrayList<CubeNode> nodes= RegdataService.getRegSubNodes("cuscxnd",procode);
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <nodes.size() ; i++) {
            Map<String, Object> obj = new HashMap<String, Object>();
            obj.put("code", nodes.get(i).getCode());
            obj.put("name", nodes.get(i).getCname());
            data.add(obj);
        }
        this.sendJson(data);
    }
    /**
     * 数据检查
     */
    public void getData() throws IOException{
        HttpServletRequest req = this.getRequest();
        String reg = PubInfo.getString(req.getParameter("reg"));//地区
        String sj = PubInfo.getString(req.getParameter("sj"));//时间
        String [] regs = reg.split(",");
        String [] sjs = sj.split(",");
        List<CubeQueryData> data1 = new ArrayList<CubeQueryData>();
        for (int i = 0; i <sjs.length ; i++) {
            CubeWdCodes where = new CubeWdCodes();
            where.Add("zb", Arrays.asList(new String[]{"185a8c443b36a50fb5fb20b2b1ea8759f8aeb100","2be7da8a7f023f703366a9bd61f262597ccfcdfa"}));
            where.Add("ds", Arrays.asList(new String[]{"A010600","A010600"}));
            where.Add("co", Arrays.asList(new String[]{"COG01","COG01"}));
            where.Add("reg", Arrays.asList(regs));
            where.Add("sj", sjs[i]);
            ArrayList<CubeQueryData> result = RegdataService.queryData("cuscxnd",where);
            data1.addAll(result);
        }
        List data = new ArrayList();
        for (int i = 0; i < data1.size(); i++) {
            PubInfo.printStr(data1.get(i).toString());
            data.add(data1.get(i).getData().getData());
        }
        this.sendJson(data);
    }
}
