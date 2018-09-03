package com.acmr.web.jsp.zbdata;

import acmr.cubequery.service.cubequery.entity.CubeNode;
import acmr.cubequery.service.cubequery.entity.CubeQueryData;
import acmr.cubequery.service.cubequery.entity.CubeUnit;
import acmr.cubequery.service.cubequery.entity.CubeWdCodes;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.entity.ModelAndView;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zbdata.OriginService;
import com.acmr.service.zbdata.RegdataService;
import com.acmr.service.zbdata.ZBdataService;
import com.acmr.service.zhzs.IndexEditService;
import com.acmr.service.zhzs.IndexListService;
import com.alibaba.fastjson.JSONObject;

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
        JSONObject zbs=getZBS(code);
        return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("proname",proname).addObject("list",list).addObject("indexlist",indexlist).addObject("zbs",zbs);

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
        String zbcode = PubInfo.getString(req.getParameter("zb"));//zbcode
        String zbname = PubInfo.getString(req.getParameter("zbname"));//zbname
        String ds = PubInfo.getString(req.getParameter("ds"));//数据来源
        String co = PubInfo.getString(req.getParameter("co"));//主体
        String zbunit = PubInfo.getString(req.getParameter("zbunit"));//单位
        String [] regs = reg.split(",");
        String [] sjs = sj.split(",");
        String [] zbcodes = zbcode.split(",");
        String [] zbnames = zbname.split(",");
        String [] dss = ds.split(",");
        String [] cos = co.split(",");
        String [] units = zbunit.split(",");
        List<CubeQueryData> data1 = new ArrayList<CubeQueryData>();
        for (int i = 0; i <sjs.length ; i++) {
            for (int j = 0; j <zbcodes.length ; j++) {
                CubeWdCodes where = new CubeWdCodes();
                where.Add("zb", zbcodes[j]);
                where.Add("ds", dss[j]);
                where.Add("co", cos[j]);
                where.Add("reg", Arrays.asList(regs));
                where.Add("sj", sjs[i]);
                ArrayList<CubeQueryData> result = RegdataService.queryData("cuscxnd",where);//还没有换算单位
                data1.addAll(result);
            }
        }
        List data = new ArrayList();
        for (int i = 0; i < data1.size(); i++) {
            PubInfo.printStr(data1.get(i).toString());
            data.add(data1.get(i).getData().getData());
        }
        this.sendJson(data);
    }


    /**
     * @Description: 返回树
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public void testTreeNode() throws IOException {
        String id=this.getRequest().getParameter("id");
        ZBdataService zBdataService=new ZBdataService();
        List<TreeNode> list = new ArrayList<TreeNode>();
        List<CubeNode> zbs=new ArrayList<>();
        if(id==""){
            zbs=zBdataService.getZB();
        }
        else {
            zbs=zBdataService.getSubZB(id);
        }
        for (int i=0;i<zbs.size();i++){
            String code=zbs.get(i).getCode();
            String name=zbs.get(i).getName();
            TreeNode treeNode=new TreeNode();
            treeNode.setId(code);
            treeNode.setName(name);
            treeNode.setPId(id);
            if (zBdataService.getSubZB(code).size()>0){
                treeNode.setIsParent(true);
            }
            else treeNode.setIsParent(false);
            list.add(treeNode);
        }
        for(int j=0;j<list.size();j++){
            PubInfo.printStr(list.get(j).getId());
        }
        this.sendJson(list);

    }

    /**
     * @Description: 返回搜索
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public void testFind() throws IOException {
        System.out.println("=====================find");
        String query=this.getRequest().getParameter("query");
        ZBdataService zBdataService=new ZBdataService();
        List<CubeNode> nodes=zBdataService.findZB(query);
        List<CubeNode> zbs=new ArrayList<>();
        for (int i=0;i<nodes.size();i++){
            if(zBdataService.getSubZB(nodes.get(i).getCode()).size()==0){
                zbs.add(nodes.get(i));
            }
        }
        this.sendJson(zbs);

    }
    /**
     * @Description: 返回指标的path
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/9/2
     */
    public void getZBPath() throws IOException {
        ZBdataService zBdataService=new ZBdataService();
        String code=this.getRequest().getParameter("code");
        List<String> path=zBdataService.getZBPath(code);
        this.sendJson(path);
    }

    /**
     * @Description: 返回有数的ds，co，以及指标对应的unit
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public  void getDsCoUnit() throws IOException {
        String code=this.getRequest().getParameter("zbcode");
        // String code="888cab11761ccd4f77b12477c011e6e8188350f4";
        ZBdataService zBdataService=new ZBdataService();
        OriginService originService=new OriginService();
        List<String> dscodes=zBdataService.getHasDataNodeO(code,"ds");
        List<Map> dss=new ArrayList<>();
        PutMap(dss,dscodes,"ds");
        List<String> cocodes=zBdataService.getHasDataNodeO(code,"co");
        List<Map> cos=new ArrayList<>();
        PutMap(cos,cocodes,"co");
        CubeNode ZB=originService.getwdnode("zb",code);
        String unitcode=ZB.getUnitcode();
        String unitname=ZB.getUnitname();
        List<CubeUnit> unitlist=originService.getUnitList(unitcode);
        List<Map> units=new ArrayList<>();
        for(int i=0;i<unitlist.size();i++){
            Map jsonObj=new HashMap <String,String>();
            jsonObj.put(unitlist.get(i).getCode(),unitlist.get(i).getName());
            units.add(jsonObj);
        }

        JSONObject obj=new JSONObject();
        obj.put("ds",dss);
        obj.put("co",cos);
        obj.put("unit",units);
        this.sendJson(obj);
    }
    public  void PutMap(List<Map> map, List<String> codes,String wcode){
        OriginService originService=new OriginService();

        for(int i=0;i<codes.size();i++){
            String code=codes.get(i);
            String name=originService.getwdnode(wcode,code).getName();
            Map jsonObj=new HashMap <String,String>();
            jsonObj.put(code,name);
            map.add(jsonObj);
        }
    }

    /**
     * @Description: 返回该指标的已有筛选条件
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/30
     */
    public JSONObject getZBS(String icode){
        IndexEditService indexEditService=new IndexEditService();
        //String icode=this.getRequest().getParameter("icode");
        List<Map> zbchoose=indexEditService.getZBS(icode);
        JSONObject obj=new JSONObject();
        obj.put("zbchoose",zbchoose);
        return obj;
    }
    /**
     * @Description: 根据条件返回数据预览
     * @Param: []
     * @return: void
     * @Author: lyh
     * @Date: 2018/8/31
     */

    public ModelAndView getDataTest(){
        HttpServletRequest req = this.getRequest();
        // 获取查询数据
        String zbcode = req.getParameter("zbcode");
        String dscode = req.getParameter("dscode");
        String cocode = req.getParameter("cocode");
        String unitcode = req.getParameter("unitcode");
        // 判断是否pjax 请求
        String pjax = req.getHeader("X-PJAX");
        ZBdataService zBdataService=new ZBdataService();
        List<String> sjs=zBdataService.getHasDataNodeO(zbcode,"sj");
        List<String> regs=zBdataService.getHasDataNodeO(zbcode,"reg");
        OriginService originService=new OriginService();
        CubeWdCodes where = new CubeWdCodes();
        /*
        where.Add("sj",sjs);
        where.Add("reg",regs);
        List<CubeQueryData> datas=originService.querydata(where);*/
        //PubInfo.printStr(datas.toString());
        String funit=originService.getwdnode("zb",zbcode).getUnitcode();
        List<Double> rates=new ArrayList<>();
        for (int i=0;i<sjs.size();i++){
            String sj=sjs.get(i);
            double rate=originService.getRate(funit,unitcode,sj);
            rates.add(rate);
        }
        PubInfo.printStr(rates.toString());
        List<List<String>> rows=new ArrayList<>();
        for(int i=0;i<regs.size();i++){
            List<String> row=new ArrayList<>();
            if(originService.getwdnode("reg",regs.get(i))!=null){
                row.add(originService.getwdnode("reg",regs.get(i)).getName());
                for(int j=0;j<sjs.size();j++){
                    where.Add("zb",zbcode);
                    where.Add("ds",dscode);
                    where.Add("co",cocode);
                    where.Add("sj",sjs.get(j));
                    where.Add("reg",regs.get(i));
                    double data=originService.querydata(where).get(0).getData().getData()*rates.get(j);
                    row.add(data+"");
                    where.Clear();
                }

                rows.add(row);
            }

        }
        PubInfo.printStr(rows.toString());

        if (StringUtil.isEmpty(pjax)) {
            String code=req.getParameter("indexcode");
            JSONObject zbs=getZBS(code);
            IndexListService indexListService=new IndexListService();
            IndexList list =indexListService.getData(code);
            PubInfo.printStr("isempty");
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/zsjhEdit").addObject("zbs",zbs).addObject("list",list);
        } else {
            return new ModelAndView("/WEB-INF/jsp/zhzs/zsjh/ZBdataList").addObject("sjs",sjs).addObject("rows",rows);
        }
    }
}
