package com.acmr.dao.security;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import com.acmr.model.pub.PageBean;
import com.acmr.model.templatecenter.ApplyDimensionInfo;
import com.acmr.model.templatecenter.ApplySynonymInfo;

public interface IhandleDao {

	PageBean<ApplyDimensionInfo> findDimensionBypage(PageBean<ApplyDimensionInfo> page,
			ApplyDimensionInfo dimensionInfo);

	ApplyDimensionInfo getDimensionInfo(String id);

	String getExcel(String id) throws SQLException;

	int replyAddDimension(ApplyDimensionInfo applyInfo) throws JAXBException;

	int updateApplySynonymState(ApplySynonymInfo applySynonymInfo);

	PageBean<ApplySynonymInfo> findSynonymBypage(PageBean<ApplySynonymInfo> page, ApplySynonymInfo applySynonymInfo);

	ApplySynonymInfo getApplySynonymInfo(String id) throws SQLException;

	byte[] getFileContent(String id);

	String getFileName(String id);

}
