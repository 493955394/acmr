package com.acmr.dao.sqlserver.security;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.acmr.dao.AcmrInputDPFactor;
import com.acmr.dao.data.AcmrDataPageDao;
import com.acmr.dao.security.IhandleDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.PageBean;
import com.acmr.model.templatecenter.ApplyDimensionInfo;
import com.acmr.model.templatecenter.ApplyDimensionList;
import com.acmr.model.templatecenter.ApplySynonymInfo;

import acmr.data.DataQuery;
import acmr.util.DataTable;
import acmr.util.DataTableRow;

public class SqlHandleDaoImpl implements IhandleDao {
	@Override
	public PageBean<ApplyDimensionInfo> findDimensionBypage(PageBean<ApplyDimensionInfo> page,
			ApplyDimensionInfo dimensionInfo) {
		StringBuffer sbf = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		sbf.append("select t1.*,t2.cname as username from tb_msg_template t1 left join TB_RIGHT_USER t2 on t1.userid = t2.userid  where 1=1 ");
		if (!StringUtil.isEmpty(dimensionInfo.getName())) {
			sbf.append(" and lower(t1.cname) like ? ");
			objs.add("%"+dimensionInfo.getName()+"%");
		}
		sbf.append(" order by t1.createtime desc ");
		try {
			page = new AcmrDataPageDao<ApplyDimensionInfo>().findPage(sbf.toString()," createtime desc ", objs.toArray());
			List<ApplyDimensionInfo> dimensionList = this.dataToDimensionInfo(page.getDataTable());
			page.setData(dimensionList);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return page;
	}
	@Override
	public PageBean<ApplySynonymInfo> findSynonymBypage(PageBean<ApplySynonymInfo> page,
			ApplySynonymInfo applySynonymInfo) {
		StringBuffer sbf = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		sbf.append("select * from tb_msg_addsyn  where 1=1 ");
		if (!StringUtil.isEmpty(applySynonymInfo.getUserId())) {
			sbf.append(" and userid=? ");
			objs.add(applySynonymInfo.getUserId());
		}
		sbf.append(" order by createtime desc ");
		try {
			page = new AcmrDataPageDao<ApplySynonymInfo>().findPage(sbf.toString()," createtime ", objs.toArray());
			List<ApplySynonymInfo> synonymList = this.dataToSynonymInfo(page.getDataTable());
			page.setData(synonymList);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return page;
	}
	private List<ApplySynonymInfo> dataToSynonymInfo(DataTable dataTable) {
		List<ApplySynonymInfo> result = new ArrayList<ApplySynonymInfo>();
		if (dataTable == null ||dataTable.getRows().isEmpty()) {	// 如果没有查询到数据返回
			return result;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row:dataTableRows) {
			ApplySynonymInfo applySynonymInfo = new ApplySynonymInfo();
			applySynonymInfo.setId(row.getString("CODE"));
			applySynonymInfo.setCode(row.getString("WDCODE"));
			applySynonymInfo.setDimension(row.getString("VCODE"));
			applySynonymInfo.setName(row.getString("WDNAME"));			
			applySynonymInfo.setMemo(row.getString("MEMO"));
			applySynonymInfo.setState(row.getString("STATE"));
			applySynonymInfo.setSynonym(row.getString("SYVALUE"));
			result.add(applySynonymInfo);
		}
		return result;
	}
	private List<ApplyDimensionInfo> dataToDimensionInfo(DataTable dataTable) {
		List<ApplyDimensionInfo> result = new ArrayList<ApplyDimensionInfo>();
		if (dataTable == null ||dataTable.getRows().isEmpty()) {	// 如果没有查询到数据返回
			return result;
		}
		List<DataTableRow> dataTableRows = dataTable.getRows();
		for (DataTableRow row:dataTableRows) {
			ApplyDimensionInfo applyDimensionInfo = new ApplyDimensionInfo();
			applyDimensionInfo.setId(row.getString("CODE"));
			applyDimensionInfo.setName(row.getString("CNAME"));
			applyDimensionInfo.setMemo(row.getString("CEXP"));
			applyDimensionInfo.setUserName(row.getString("USERNAME"));
			applyDimensionInfo.setState(row.getString("STATE"));
			applyDimensionInfo.setCreateTime(row.getDate("CREATETIME"));
			result.add(applyDimensionInfo);
		}
		return result;
	}
	@Override
	public ApplyDimensionInfo getDimensionInfo(String id) {
		ApplyDimensionInfo applyDimensionInfo = new ApplyDimensionInfo();
		String strSql ="select t3.*, t4.cname as replyusername from (select t1.*, t2.cname as username from tb_msg_template t1 left join TB_RIGHT_USER t2 on t1.userid = t2.userid where code = ?) t3  left join TB_RIGHT_USER t4 on t3.reuserid = t4.userid";
		Object[] params ={id};
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		DataTable data = null;
		try {
			data = dataQuery.getDataTableSql(strSql, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (data == null ||data.getRows().isEmpty()) {	// 如果没有查询到数据返回
			return null;
		}
		List<DataTableRow> dataTableRows = data.getRows();
		DataTableRow row = dataTableRows.get(0);
		applyDimensionInfo.setMemo(row.getString("cexp"));
		applyDimensionInfo.setReplyMemo(row.getString("reexp"));
		applyDimensionInfo.setUserName(row.getString("username"));
		applyDimensionInfo.setCreateTime(row.getDate("createtime"));
		applyDimensionInfo.setState(row.getString("state"));
		applyDimensionInfo.setExcel(row.getString("excelcontent"));
		applyDimensionInfo.setReplyUserName(row.getString("replyusername"));
//		applyDimensionInfo.setReplyName(row.getString("rename"));
		applyDimensionInfo.setReplyMemo(row.getString("reexp"));
		if(row.getString("content")!=null && !"".equals(row.getString("content"))){
			ApplyDimensionList applyList =  XMLToObject(row.getString("content"));
			applyDimensionInfo.setApplyList(applyList);
		}
		if(row.getString("recontent")!=null && !"".equals(row.getString("recontent"))){
			ApplyDimensionList replyList =  XMLToObject(row.getString("recontent"));
			applyDimensionInfo.setReplyList(replyList);
		}
		return applyDimensionInfo;
	}
	private ApplyDimensionList XMLToObject(String xml){
        try {  
            StringReader reader = new StringReader(xml);  
            JAXBContext jaxbContext=JAXBContext.newInstance(ApplyDimensionList.class);  
            Unmarshaller unmarshaller =jaxbContext.createUnmarshaller();
            Object obj = unmarshaller.unmarshal(reader);  
            ApplyDimensionList list = (ApplyDimensionList)obj;
            return list;
        } catch (JAXBException e) {  
            throw new RuntimeException(e);  
        }  
	}
	@Override
	public String getExcel(String id) throws SQLException {
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		String sql = "select excelcontent from tb_msg_template where code = ?";
		ResultSet resultSet = dataQuery.getResultSetSql(sql, new Object[]{id});
		String result = "";
		if(resultSet.next()){
			result = resultSet.getString("excelcontent");
		}
		if (resultSet != null) {
			resultSet.close();
		}
		if (dataQuery != null) {
			dataQuery.releaseConnl();
		}
		return result;
	}
	@Override
	public int replyAddDimension(ApplyDimensionInfo applyInfo) throws JAXBException {
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		String sql = "update tb_msg_template set reexp=?,recontent=?,state='2',updatetime=?,reuserid=? "
		+ "where code= ?";
		List<Object> params = new ArrayList<Object>();
		Date date = new Date();
		params.add(applyInfo.getReplyMemo());
		params.add(dimensionToXML(applyInfo.getReplyList()));
		params.add(new java.sql.Date(date.getTime()));
		params.add(applyInfo.getReplyUserId());
		params.add(applyInfo.getId());
		int result=0;
		try {
			result=dataQuery.executeSql(sql, params.toArray());	
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {	
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		return result;
	}
	private String dimensionToXML(ApplyDimensionList obj) throws JAXBException{
		JAXBContext context=JAXBContext.newInstance(ApplyDimensionList.class);
	    Marshaller marshaller=context.createMarshaller();
	    StringWriter writer = new StringWriter();  
	    marshaller.marshal(obj, writer);  
		return writer.toString();
	}
	@Override
	public int updateApplySynonymState(ApplySynonymInfo applySynonymInfo) {
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		int result =0;
		String sql = "update tb_msg_addsyn set state=?,updatetime=? where code =?";
		List<Object> params = new ArrayList<Object>();
		Date date = new Date();
		params.add(applySynonymInfo.getState());
		params.add(new java.sql.Date(date.getTime()));
		params.add(applySynonymInfo.getId());
		try {
			result=dataQuery.executeSql(sql, params.toArray());	
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {	
			if (dataQuery != null) {
				dataQuery.releaseConnl();
			}
		}
		return result; 
	}
	@Override
	public ApplySynonymInfo getApplySynonymInfo(String id) throws SQLException {
		ApplySynonymInfo applySynonymInfo = new ApplySynonymInfo();
		DataQuery dataQuery = AcmrInputDPFactor.getDataPool().getDataQuery();
		String sql ="select * from tb_msg_addsyn where code =?";
		DataTable dt= dataQuery.getDataTableSql(sql, new Object[]{id});	
		
		List<DataTableRow> dataTableRows = dt.getRows();
		DataTableRow row = dataTableRows.get(0);
		applySynonymInfo.setId(row.getString("code"));
		applySynonymInfo.setCode(row.getString("wdcode"));
		applySynonymInfo.setName(row.getString("wdname"));
		applySynonymInfo.setCreateTime(row.getDate("createtime"));
		applySynonymInfo.setUpdateTime(row.getDate("updatetime"));
		applySynonymInfo.setDimension(row.getString("vcode"));
		applySynonymInfo.setSynonym(row.getString("syvalue"));
		applySynonymInfo.setState(row.getString("state"));
		applySynonymInfo.setMemo(row.getString("memo"));
		return applySynonymInfo;
	}
	@Override
	public byte[] getFileContent(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getFileName(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
