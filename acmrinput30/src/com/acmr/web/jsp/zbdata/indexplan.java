package com.acmr.web.jsp.zbdata;

import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.zhzs.IndexList;
import com.acmr.service.zhzs.IndexListService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

    public class indexplan extends BaseAction {
        /**
         * 复制到
         * @author wf
         * @date
         * @param
         * @return
         */
        public void sharecopy() throws IOException {
            IndexListService indexListService = new IndexListService();
            HttpServletRequest req = this.getRequest();
            String cpcode = PubInfo.getString(req.getParameter("cosharecode"));//原code
            //String ifdata1 = PubInfo.getString(req.getParameter("cifdata"));
            //int ifdata = Integer.parseInt(ifdata1);
            String code = PubInfo.getString(req.getParameter("putcode"));
            JSONReturnData data = new JSONReturnData("");
            int x = indexListService.checkCode(code);
            if (x == 0) {
                data.setReturncode(300);
                this.sendJson(data);
                return;
            } else {
                data.setReturncode(200);
            }
            String cname = PubInfo.getString(req.getParameter("putname"));
            String nprocode = PubInfo.getString(req.getParameter("shareprocode"));
            //String createtime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            IndexList copydata = indexListService.getData(cpcode);

            copydata.setCode(code);
            copydata.setCname(cname);
            copydata.setProcode(nprocode);
            //data1.setCreatetime(createtime);
            indexListService.addCopyShare(cpcode,copydata);
        /*if (int1 == -1) {
            data.setReturncode(501);
            data.setReturndata("fail");
            this.sendJson(data);
            return;
        }*/
            data.setReturndata(copydata);
            this.sendJson(data);
        }
    }

