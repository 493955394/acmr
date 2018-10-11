package com.acmr.service.zhzs;



import acmr.util.DataTable;
import acmr.util.DataTableRow;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.dao.zhzs.IDataDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.zhzs.TaskModule;
import com.acmr.service.zbdata.OriginService;

import java.io.IOException;
import java.util.*;

public class PastViewService {

    /**
     * 获取所有任务code
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<String> getAllTask(String icode){
        List<String> taskcodes = new ArrayList<>();
        DataTable table= IndexTaskDao.Fator.getInstance().getIndexdatadao().getAllTaskList(icode);
        List<DataTableRow> list =table.getRows();
        for(int i=0;i<list.size();i++){
            String taskcode = list.get(i).getString("code");
            taskcodes.add(taskcode);
        }

        return taskcodes;
    }
    /**
     * 获取所有时间数据
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<String> getAllTime(String icode){
        List<String> pasttimes = new ArrayList<>();
        DataTable table= IndexTaskDao.Fator.getInstance().getIndexdatadao().getAllTaskList(icode);
        List<DataTableRow> list =table.getRows();
        for(int i=0;i<list.size();i++){
            String time = list.get(i).getString("ayearmon");
            pasttimes.add(time);
        }
        return pasttimes;
    }
    /**
     * 获取地区code
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<String> getRegions(String taskcode){
        List<String> pastregs = new ArrayList<>();
        //PastViewService pv = new PastViewService();
        //List<String> alltaskcode = pv.getAllTask(icode);
        DataTable table= IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskZb(taskcode);
        List<DataTableRow> list=table.getRows();
        String regions  = list.get(0).get(5).toString();
        pastregs= Arrays.asList(regions.split(","));
        return pastregs;
    }
    /**
     * 查询submod
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<TaskModule> toSubMod(String code){
        List<TaskModule> taskModules = new ArrayList<>();
        List<DataTableRow> data =new ArrayList<>();
        data = DataDao.Fator.getInstance().getIndexdatadao().getSubMods(code).getRows();
        for (int i = 0; i <data.size() ; i++) {
            TaskModule taskModule = new TaskModule();
            taskModule.setCode(data.get(i).getString("code"));
            taskModule.setCname(data.get(i).getString("cname"));
            taskModule.setTaskcode(data.get(i).getString("taskcode"));
            taskModule.setProcode(data.get(i).getString("procode"));
            taskModule.setIfzs(data.get(i).getString("ifzs"));
            taskModule.setIfzb(data.get(i).getString("ifzb"));
            taskModule.setFormula(data.get(i).getString("formula"));
            taskModule.setWeight(data.get(i).getString("weight"));
            taskModule.setSortcode(data.get(i).getString("sortcode"));
            taskModule.setDacimal(data.get(i).getString("dacimal"));
            taskModule.setOrcode(data.get(i).getString("orcode"));
            taskModules.add(taskModule);
        }
        return taskModules;
    }
    /**
     * 获取 taskmodule 表中的moduletree
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<Map> getModelTree (String taskcode){
        IndexTaskService indexTaskService = new IndexTaskService();
        List<TaskModule> zong = indexTaskService.findModRoot(taskcode);
        List<Map> list = new ArrayList<>();
        for (int i = 0; i <zong.size(); i++) {
            Map arr = new HashMap();
            arr.put("name",zong.get(i).getCname());
            arr.put("code",zong.get(i).getCode());
            arr.put("orcode",zong.get(i).getOrcode());
            arr.put("dotcount",zong.get(i).getDacimal());
            list.add(arr);
            list.addAll(getmodelList(taskcode,zong.get(i).getCode()));
        }
        return list;
    }

    public  List<Map> getmodelList(String taskcode,String modcode){
        List<Map> mode= new ArrayList<>();
        List<TaskModule> tmp = toSubMod(modcode);
        for (int i = 0; i <tmp.size() ; i++) {
            Map arr = new HashMap();
            arr.put("name",tmp.get(i).getCname());
            arr.put("code",tmp.get(i).getCode());
            arr.put("orcode",tmp.get(i).getOrcode());
            arr.put("dotcount",tmp.get(i).getDacimal());
            mode.add(arr);
            if(tmp.get(i).getIfzs().equals("1")){
                mode.addAll(getmodelList(taskcode,tmp.get(i).getCode()));
            }

        }
        return mode;
    }

    /**
     * 获取模型节点code
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<Map> getAllMods (List<String> alltaskcode){
        //OriginDataService od = new OriginDataService();
        PastViewService pv = new PastViewService();
        String singlecode = alltaskcode.get(0);
        List<Map> singlemod = pv.getModelTree(singlecode);
        //拿单组节点作对比再添加
        List<Map> allmods =  new ArrayList<>();
        for(int y=0;y<singlemod.size();y++){
            Map arr = new HashMap();
            arr.put("name",singlemod.get(y).get("name").toString());
            arr.put("code",singlemod.get(y).get("code").toString());
            //String modcode = singlemod.get(y).get("code").toString();
            allmods.add(arr);
        }
        //allmods.addAll(singlecode,singlemod);
        //得到全部节点code
        for(int i=0;i<alltaskcode.size();i++){
            List<Map> arr = pv.getModelTree(alltaskcode.get(i));
            //List<String> mods = new ArrayList<>();
            for(int j=0;j<arr.size();j++) {
                String mod = arr.get(j).get("code").toString();
                //mods.add(mod);

                for (int k = 0; k < allmods.size(); k++) {
                    String oldmod = allmods.get(k).get("code").toString();
                    if(!mod.equals(oldmod)){
                        continue;
                    }else if(mod.equals(allmods.get(k).get("code").toString())){
                            break;
                            }
                    Map arr1 = new HashMap();
                    arr1.put("name", arr.get(j).get("name").toString());
                    arr1.put("code", arr.get(j).get("code").toString());
                    allmods.add(arr1);

                    }

            }

        }
        return allmods;
    }
    /**
     * 单地区查询所有的data
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getModData(String reg,List<String> alltaskcode,List<Map> allmod,List<String> last5){
        PastViewService pv = new PastViewService();
        List<List<String>> moddatas = new ArrayList<>();

        for(int i=0;i<allmod.size();i++){
            String zbcode = allmod.get(i).get("code").toString();
            //List<String> zbdata = new ArrayList<>();
            List<String> datas = new ArrayList<>();
            String zbname = allmod.get(i).get("name").toString();
            datas.add(zbname);
            for(int j=0;j<alltaskcode.size();j++){
                //List<String> last5 = pv.getAllTime(alltaskcode.get(j)).subList(0,5);

                for (int k=0;k<last5.size();k++){
                    String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(alltaskcode.get(j),zbcode,reg,last5.get(k));

                    if(data != null){
                        datas.add(data);
                    }
                }
            }
            moddatas.add(datas);
        }
        return moddatas;
    }
    /**
     * 单模型节点查询所有的data
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getRegData(List<String> regs,List<String> alltaskcode,String mod,List<String> last5){
        OriginService originService=new OriginService();
        PastViewService pv = new PastViewService();
        List<List<String>> regdatas = new ArrayList<>();
        for(int i=0;i<regs.size();i++){
            String regioncode = regs.get(i);
            //List<String> zbdata = new ArrayList<>();
            for(int j=0;j<alltaskcode.size();j++){
                List<String> datas = new ArrayList<>();
                String regname = originService.getwdnode("reg",regioncode).getName();
                datas.add(regname);
                for (int k=0;k<last5.size();k++){
                    String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(alltaskcode.get(j),mod,regioncode,last5.get(k));
                    datas.add(data);
                    if(data == null){
                        data = ("");
                        datas.add(data);
                    }
                }
                regdatas.add(datas);
            }
        }
        return regdatas;
    }
}
