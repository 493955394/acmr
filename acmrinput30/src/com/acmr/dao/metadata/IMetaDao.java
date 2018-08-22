package com.acmr.dao.metadata;

import java.util.List;

import com.acmr.model.pub.AjaxPageBean;
import com.acmr.service.metadata.DataDetails;
import com.acmr.service.metadata.MetaServiceManager;
import com.acmr.service.metadata.Template;

public interface IMetaDao {

	public void getAllData(MetaServiceManager metaService, AjaxPageBean<DataDetails> page, String code) throws Exception;

	public void toDatadetails(MetaServiceManager metaService, AjaxPageBean<DataDetails> page, String code, String flcode, String regcode, String datasource, StringBuffer url) throws Exception;

	int queryTemplate(String code, String type, String pre);

	List<Template> getTemplateData(String code, String type, AjaxPageBean<Template> page, String pre);

}
