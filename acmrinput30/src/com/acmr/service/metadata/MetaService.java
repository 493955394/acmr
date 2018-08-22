package com.acmr.service.metadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import acmr.excel.Xls2Xlsx;

import com.acmr.helper.util.StringUtil;

/**
 * cube封装层
 * 
 * @author chenyf
 *
 */
public class MetaService {

	private MetaService() {
	}

	private Map<String, MetaServiceManager> metaService = new HashMap<String, MetaServiceManager>();

	private static MetaService instance = new MetaService();

	/**
	 * 实例化对象
	 * 
	 * @param tbcode
	 * @return
	 * @author chenyf
	 */
	public static MetaServiceManager getMetaService(String tbcode) {
		if (!instance.metaService.containsKey(tbcode)) {
			MetaServiceManager ms = new MetaServiceManager(tbcode);
			instance.metaService.put(tbcode, ms);
		}
		return instance.metaService.get(tbcode);
	}
	
	public static String getSearchReplace(String key){
		return key.replaceAll("%", "/%").replaceAll("/", "//").replaceAll("_", "/_");
	}

	/**
	 * 取得分页url
	 * 
	 * @param req
	 * @return
	 * 
	 */
	public static String getPageUrl(HttpServletRequest req) {
		Enumeration<String> keys = req.getParameterNames();
		int v = 0;
		StringBuffer urlpara = new StringBuffer();
		urlpara.append(req.getRequestURI());
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (!key.equals("_pjax") && !key.equals("pageNum")) {
				String value = req.getParameter(key);
				if (v++ == 0) {
					urlpara.append("?").append(key).append("=").append(value);
				} else {
					urlpara.append("&").append(key).append("=").append(value);
				}
			}
		}
		return urlpara.toString();
	}

	// 仅用于元数据数据导出使用
	public static String getMetaPara(HttpServletRequest req) {
		Enumeration<String> keys = req.getParameterNames();
		int v = 0;
		StringBuffer urlpara = new StringBuffer();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (!key.equals("_pjax") && !key.equals("pageNum")) {
				String value = req.getParameter(key);
				if (v++ == 0 || "m".equals(key)) {
					continue;
				} else {
					urlpara.append("&").append(key).append("=").append(value);
				}
			}
		}
		return urlpara.toString();
	}
	
	
	/**
	 * 获取03输出流
	 * @return
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	public static Workbook get07ExcelByte(byte[] blob,String oldType) throws InvalidFormatException, IOException{
		ByteArrayInputStream in = new ByteArrayInputStream(blob);
        XSSFWorkbook b1 = null;
		if("xlsx".equals(oldType.toLowerCase())){
			return new XSSFWorkbook(in);
		}else{
			HSSFWorkbook book2 = new HSSFWorkbook(in);
			b1 = new XSSFWorkbook();
			new Xls2Xlsx().transformHSSF(book2, b1);
			return b1;
		}
	}
	
	public static String getAdvParameter(HttpServletRequest req, String key) {
		String value = req.getParameter(key);
		if (StringUtil.isEmpty(value)) {
			return "";
		}
		if (req.getMethod().toLowerCase().equals("get")) {
			try {
				value = value.replaceAll("%20", " ");
				value = value.replaceAll("%25", "%");
				value = value.replaceAll("%27", "'");
				value = value.replaceAll("%", "###");
				value = java.net.URLDecoder.decode(value, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		return value.replaceAll("###", "%");
	}
}
