package com.acmr.test;

import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.PubInfo;
import com.acmr.dao.security.ISecurityDao;
import com.acmr.dao.security.SecurityDao;
import com.acmr.dao.zhzs.IndexListDao;
import com.acmr.model.pub.PageBean;
import com.acmr.model.security.Department;
import com.acmr.model.security.Role;
import com.acmr.model.security.User;
import com.acmr.service.security.DepartmentService;
import com.acmr.service.security.RoleService;
import com.acmr.service.security.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Mytest {
    public static void main(String[] args) {
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.YEAR, 2013);
        cal.set(Calendar.MONTH,12);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        Date time=cal.getTime();
        PubInfo.printStr(String.valueOf(time));

    }
}
