package com.acmr.service.zhzs;



import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.zhzs.DataDao;
import com.acmr.dao.zhzs.IDataDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.dao.zhzs.IndexTaskDao;
import com.acmr.model.range.Pinfo;
import com.acmr.model.zhzs.IndexTask;
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
     * 获取单个任务地区code
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
    public  List<TaskModule> toSubMod(String code){
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
    public List<Map<String,String>>  getModelTree (String taskcode){
        IndexTaskService indexTaskService = new IndexTaskService();
        List<TaskModule> zong = indexTaskService.findModRoot(taskcode);
        List<Map<String,String>> list = new ArrayList<>();
        for (int i = 0; i <zong.size(); i++) {
            Map<String,String> arr = new HashMap<>();
            arr.put("name",zong.get(i).getCname());
            arr.put("code",zong.get(i).getCode());
            arr.put("orcode",zong.get(i).getOrcode());
            arr.put("dotcount",zong.get(i).getDacimal());
            arr.put("taskcode",zong.get(i).getTaskcode());
            list.add(arr);
            list.addAll(getmodelList(taskcode,zong.get(i).getCode()));
        }
        return list;
    }

    public  List<Map<String,String>> getmodelList(String taskcode,String modcode){
        List<Map<String,String>> mode= new ArrayList<>();
        List<TaskModule> tmp = toSubMod(modcode);
        for (int i = 0; i <tmp.size() ; i++) {
            Map<String,String> arr = new HashMap<>();
            arr.put("name",tmp.get(i).getCname());
            arr.put("code",tmp.get(i).getCode());
            arr.put("orcode",tmp.get(i).getOrcode());
            arr.put("dotcount",tmp.get(i).getDacimal());
            arr.put("taskcode",tmp.get(i).getTaskcode());
            mode.add(arr);
            if(tmp.get(i).getIfzs().equals("1")){
                mode.addAll(getmodelList(taskcode,tmp.get(i).getCode()));
            }

        }
        return mode;
    }




    /**
     * 获取去重的modlist,获取orcode和cname并集
     * @param
     * @return
     */
    public List<Map<String,String>> getModsList (List<String> alltaskcode){
        List<Map<String,String>> lists = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        if(alltaskcode.size()>0){//先把最近一年的orcode取出来
            List<Map<String,String>> modlist = getModelTree(alltaskcode.get(0));
            for (int i = 0; i <modlist.size() ; i++) {
                Map<String,String> arr = new HashMap<>();
                arr.put("name",modlist.get(i).get("name"));
                arr.put("code",modlist.get(i).get("orcode"));
              //  arr.put("taskcode",modlist.get(i).get("taskcode"));
                temp.add(modlist.get(i).get("orcode"));//加到临时的数组中，为了去重
                lists.add(arr);
            }
        }
        for (int i = 0; i <alltaskcode.size() ; i++) {//不在临时数组的orcode就给添加到后面
            List<Map<String,String>> modlist = getModelTree(alltaskcode.get(i));
            for (int j = 0; j <modlist.size() ; j++) {
                if(!temp.contains(modlist.get(j).get("orcode"))){
                    //如果不存在这个orcode就把它加进来
                    Map<String,String> arr = new HashMap<>();
                    arr.put("name",modlist.get(j).get("name"));
                    arr.put("code",modlist.get(j).get("orcode"));
                   // arr.put("taskcode",modlist.get(i).get("taskcode"));
                    temp.add(modlist.get(j).get("orcode"));//加到临时的数组中，为了去重
                    lists.add(arr);
                }
            }
        }
        return lists;
    }


    /**
    * @Description: 获取计划所有任务的地区的并集
    * @Param: [icode]
    * @return: java.util.Map<java.lang.String,java.lang.String>
    * @Author: lyh
    * @Date: 2018/10/17
    */
    public List<Map<String,String>> getRegList(String icode){
        Map<String,String> regs=new HashMap<>();
        List<String> alltask=getAllTask(icode);
        OriginService originService=new OriginService();
        IndexTaskService indexTaskService=new IndexTaskService();
        for (int i=0;i<alltask.size();i++){
            String taskcode=alltask.get(i);
            List<String> thisregs=indexTaskService.getTaskRegs(taskcode);
            for (int j=0;j<thisregs.size();j++){
                if (!regs.containsKey(thisregs.get(j))){
                    String dbcode=IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
                    String name=originService.getwdnode("reg",thisregs.get(j),dbcode).getName();
                    regs.put(thisregs.get(j),name);
                }
            }
        }
        List<String> regcodes=new ArrayList<>(regs.keySet());
        List<Map<String,String>> re=new ArrayList<>();
        for (int i=0;i<regcodes.size();i++){
            Map<String,String> m=new HashMap<>();
            m.put("code",regcodes.get(i));
            m.put("name",regs.get(regcodes.get(i)));
            re.add(m);
        }
        return re;
    }
    /**
     * 单个orcode查询得到模型节点code的list
     * @author wf
     * @date
     * @param
     * @return
     */

    public List<String> findModByOrcode(List<String> alltaskcode,String orcode){
        List<String> allMods = new ArrayList<>();
        for(int i=0;i<alltaskcode.size();i++){
            String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(alltaskcode.get(i),orcode);
            allMods.add(modcode);
        }
        return allMods;
    }
    /**
     * 选择地区查询所有的data，指标data+时间的封裝
     * @date
     * @param
     * @return
     */

    public List<List<String>> getModTime(String reg,List<String> taskcodes,String icode){

        PastViewService pv = new PastViewService();

        List<List<String>> regdatas = new ArrayList<>();
        List<Map<String,String>> orcodes = getModsList(taskcodes);//拿到這五年的去重的orcode
        for (int i = 0; i < orcodes.size() ; i++) {
            List<String> temp = new ArrayList<>();
            temp.add(orcodes.get(i).get("name"));//第一個是指數/指標名字
            for (int j = 0; j <taskcodes.size() ; j++) {
                String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcodes.get(j),orcodes.get(i).get("code"));
                if(modcode==null ||modcode ==""){//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                    temp.add("");
                }else{
                    String ayearmon =  new IndexTaskService().getTime(taskcodes.get(j));
                    if(reg==null){
                        List<Map<String,String>> regs = pv.getRegList(icode);
                        String freg = regs.get(0).get("code");
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(j),modcode,freg,ayearmon);
                        if(data==null ||data ==""){//要是返回null代表這一年沒有這個地區
                            temp.add("");
                        }else {
                            temp.add(data);
                        }
                    }else{
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(j),modcode,reg,ayearmon);
                        if(data==null ||data ==""){//要是返回null代表這一年沒有這個地區
                            temp.add("");
                        }else {
                            temp.add(data);
                        }
                    }

                }
            }
            regdatas.add(temp);
        }
        //最後得出來的是一行一行的數據，前臺循環就行
        return regdatas;
    }
    /**
     * 选择地区查询所有的data，时间data+指标的封裝
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getTimeMod(String reg,List<String> taskcodes,String icode){


        PastViewService pv = new PastViewService();
        List<Map<String,String>> orcodes = getModsList(taskcodes);//拿到這五年的去重的orcode
        List<List<String>> regdatas = new ArrayList<>();
        List<String> times=new ArrayList<>();
        for (int j=0;j<taskcodes.size();j++){
            String ayearmon=new IndexTaskService().getTaskAyearmon(taskcodes.get(j));
            times.add(ayearmon);
        }
        for (int i = 0; i <times.size() ; i++) {
            List<String> temp = new ArrayList<>();
            temp.add(times.get(i));//第一個时间
            for (int j = 0; j <orcodes.size() ; j++) {
                String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcodes.get(i),orcodes.get(j).get("code"));
                if(modcode==null ||modcode ==""){//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                    temp.add("");
                }else{
                    if(reg==null){
                        List<Map<String,String>> regs = pv.getRegList(icode);
                        String freg = regs.get(0).get("code");
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(i),modcode,freg,times.get(i));
                        if(data==null ||data ==""){//要是返回null代表這一年沒有這個地區
                            temp.add("");
                        }else {
                            temp.add(data);
                        }
                    }else{
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(i),modcode,reg,times.get(i));
                        if(data==null ||data ==""){//要是返回null代表這一年沒有這個地區
                            temp.add("");
                        }else {
                            temp.add(data);
                        }
                    }
                }
            }
            regdatas.add(temp);
        }
        //最後得出來的是一行一行的數據，前臺循環就行
        return regdatas;
    }
    /**
     * 选择模型节点查询所有的data，地区data+时间的封装
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getRegTime(List<String> taskcodes,String orcode,String icode){
        OriginService originService=new OriginService();
        PastViewService pv = new PastViewService();
        List<List<String>> moddatas = new ArrayList<>();
        //String taskcode = taskcodes.get(0);
        List<String> regs =new ArrayList<>();
        List<Map<String,String>> regmap=getRegList(icode);
        for (int i=0;i<regmap.size();i++){
            String regcode=regmap.get(i).get("code");
            regs.add(regcode);
        }
        for(int i=0;i<regs.size();i++){
            String regioncode = regs.get(i);
            //List<String> zbdata = new ArrayList<>();
            List<String> datas = new ArrayList<>();
            String dbcode=IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
            String regname = originService.getwdnode("reg",regioncode,dbcode).getName();
            datas.add(regname);
            for(int j=0;j<taskcodes.size();j++){
                if(orcode==null){
                    List<String> alltaskcode = pv.getAllTask(icode);
                    List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
                    String forcode = allorcodes.get(0).get("code");
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcodes.get(j),forcode);
                    if(modcode==null ||modcode ==""){//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    }else {
                        String ayearmon = new IndexTaskService().getTime(taskcodes.get(j));
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(j), modcode, regioncode, ayearmon);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }else{
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcodes.get(j),orcode);
                    if(modcode==null ||modcode ==""){//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    }else {
                        String ayearmon = new IndexTaskService().getTime(taskcodes.get(j));
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(j), modcode, regioncode, ayearmon);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }

            }
            moddatas.add(datas);
        }
        return moddatas;
    }
    /**
     * 选择模型节点查询所有的data，时间data+地区的封装
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getTimeReg(List<String> taskcodes,String orcode,String icode){

        PastViewService pv = new PastViewService();
        //String taskcode = taskcodes.get(0);
        List<String> regs =new ArrayList<>();
        List<Map<String,String>> regmap=getRegList(icode);
        for (int i=0;i<regmap.size();i++){
            String regcode=regmap.get(i).get("code");
            regs.add(regcode);
        }
        List<List<String>> moddatas = new ArrayList<>();
        IndexTaskService indexTaskService=new IndexTaskService();
        List<String> times=new ArrayList<>();
        for (int j=0;j<taskcodes.size();j++){
            String ayearmon=indexTaskService.getTaskAyearmon(taskcodes.get(j));
            times.add(ayearmon);
        }
        for(int i=0;i<times.size();i++){
            //String regioncode = regs.get(i);
            //List<String> zbdata = new ArrayList<>();
            List<String> datas = new ArrayList<>();
            //String regname = originService.getwdnode("reg",regioncode).getName();
            datas.add(times.get(i));
            for(int j=0;j<regs.size();j++){
                String regioncode = regs.get(j);
                if(orcode==null){
                    List<String> alltaskcode = pv.getAllTask(icode);
                    List<Map<String,String>> allorcodes = pv.getModsList(alltaskcode);
                    String forcode = allorcodes.get(0).get("code");
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcodes.get(i),forcode);
                    if(modcode==null ||modcode ==""){//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    }else {
                        String ayearmon = new IndexTaskService().getTime(taskcodes.get(i));
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(i), modcode, regioncode, ayearmon);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }else{
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcodes.get(i),orcode);
                    if(modcode==null ||modcode ==""){//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    }else {
                        String ayearmon = new IndexTaskService().getTime(taskcodes.get(i));
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcodes.get(i), modcode, regioncode, ayearmon);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }
            }
            moddatas.add(datas);
        }
        return moddatas;
    }
    /**
     * 单时间，地区data+指标
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getRegMod(String taskcode,String icode) {
        OriginService originService = new OriginService();
        PastViewService pv = new PastViewService();
        List<String> regs = new ArrayList<>();
        List<Map<String, String>> regmap = getRegList(icode);
        for (int i = 0; i < regmap.size(); i++) {
            String regcode = regmap.get(i).get("code");
            regs.add(regcode);
        }
        List<List<String>> timedatas = new ArrayList<>();
        if (taskcode == null) {
            String ftime = pv.getAllTime(icode).get(0);
            taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode, ftime);
            List<String> taskcodes = new ArrayList<>();
            taskcodes.add(taskcode);
            List<Map<String, String>> orcodes = getModsList(taskcodes);
            for (int i = 0; i < regs.size(); i++) {
                String regioncode = regs.get(i);
                List<String> datas = new ArrayList<>();
                String dbcode=IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
                String regname = originService.getwdnode("reg", regioncode,dbcode).getName();
                datas.add(regname);
                for (int j = 0; j < orcodes.size(); j++) {
                    String orcode = orcodes.get(j).get("code");
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcode, orcode);
                    if (modcode == null || modcode == "") {//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    } else {
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcode, modcode, regioncode, ftime);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }
                timedatas.add(datas);
            }

        } else {
            String ayearmon = new IndexTaskService().getTaskAyearmon(taskcode);
            List<String> taskcodes = new ArrayList<>();
            taskcodes.add(taskcode);
            List<Map<String, String>> orcodes = getModsList(taskcodes);
            for (int i = 0; i < regs.size(); i++) {
                String regioncode = regs.get(i);
                List<String> datas = new ArrayList<>();
                String dbcode=IndexListDao.Fator.getInstance().getIndexdatadao().getDbcode(icode);
                String regname = originService.getwdnode("reg", regioncode,dbcode).getName();
                datas.add(regname);
                for (int j = 0; j < orcodes.size(); j++) {

                    String orcode = orcodes.get(j).get("code");
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcode, orcode);
                    if (modcode == null || modcode == "") {//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    } else {
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcode, modcode, regioncode, ayearmon);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }
                timedatas.add(datas);
            }

        }
        return timedatas;
    }
    /**
     * 单时间，指标data+地区
     * @author wf
     * @date
     * @param
     * @return
     */
    public List<List<String>> getModReg(String taskcode,String icode){
        OriginService originService=new OriginService();
        PastViewService pv = new PastViewService();
        List<String> regs =new ArrayList<>();
        List<Map<String,String>> regmap=getRegList(icode);
        for (int i=0;i<regmap.size();i++){
            String regcode=regmap.get(i).get("code");
            regs.add(regcode);
        }
        List<List<String>> timedatas = new ArrayList<>();
        if (taskcode == null) {
            String ftime = pv.getAllTime(icode).get(0);
            taskcode = IndexTaskDao.Fator.getInstance().getIndexdatadao().getTaskcode(icode, ftime);
            List<String> taskcodes = new ArrayList<>();
            taskcodes.add(taskcode);
            List<Map<String, String>> orcodes = getModsList(taskcodes);
            for (int i = 0; i < orcodes.size(); i++) {
                List<String> datas = new ArrayList<>();
                datas.add(orcodes.get(i).get("name"));
                for (int j = 0; j < regs.size(); j++) {
                    String orcode = orcodes.get(i).get("code");
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcode, orcode);
                    if (modcode == null || modcode == "") {//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    } else {
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcode, modcode, regs.get(j), ftime);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }
                timedatas.add(datas);
            }

        } else {
                String ayearmon = new IndexTaskService().getTaskAyearmon(taskcode);
                List<String> taskcodes = new ArrayList<>();
                taskcodes.add(taskcode);
                List<Map<String, String>> orcodes = getModsList(taskcodes);
            for (int i = 0; i < orcodes.size(); i++) {
                List<String> datas = new ArrayList<>();
                datas.add(orcodes.get(i).get("name"));
                for (int j = 0; j < regs.size(); j++) {
                    String orcode = orcodes.get(i).get("code");
                    String modcode = DataDao.Fator.getInstance().getIndexdatadao().findModByOrode(taskcode, orcode);
                    if (modcode == null || modcode == "") {//如果這一年沒有這個orcode,代表沒有這個模型節點，就不用去查了
                        datas.add("");
                    } else {
                        String data = DataDao.Fator.getInstance().getIndexdatadao().getPastData(taskcode, modcode, regs.get(j), ayearmon);
                        if (data == null || data == "") {//要是返回null代表這一年沒有這個地區
                            datas.add("");
                        } else {
                            datas.add(data);
                        }
                    }
                }
                timedatas.add(datas);
            }

        }
            return timedatas;
        }
}
