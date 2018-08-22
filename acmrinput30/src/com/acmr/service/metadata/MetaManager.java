package com.acmr.service.metadata;

import java.util.HashMap;
import java.util.Map;

import acmr.cubeinput.service.CubeMetaTbSev;

/**
 * cube操作帮助类
 * 
 * @author xufeng
 *
 */
public class MetaManager {

	/**
	 * key:表名，value:CMetaDao对象。 一个表对应一个CMetaDao对象
	 */
	private Map<String, CubeMetaTbSev> metaDaos = new HashMap<String, CubeMetaTbSev>();

	private MetaManager() {
	}

	private static MetaManager instance = new MetaManager();

	/**
	 * 根据表名获取操作cube meta的dao
	 * 
	 * @param tbcode
	 *            表名
	 * @return
	 */
	public static CubeMetaTbSev getMetaDao(String tbcode) {
		if (!instance.metaDaos.containsKey(tbcode)) {
			CubeMetaTbSev metaDao = new CubeMetaTbSev(tbcode);
			instance.metaDaos.put(tbcode, metaDao);
		}
		return instance.metaDaos.get(tbcode);
	}

}
