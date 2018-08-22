package com.acmr.web.jsp.log;

import java.util.Enumeration;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import acmr.web.core.CurrentContext;

import com.acmr.core.util.FileCacheUtil;
import com.acmr.service.LogService;
/**
 * session监听类
 * @author caijl
 *
 */
public class SessionListener implements HttpSessionListener {
	

	@Override
	public void sessionCreated(HttpSessionEvent event) {
	
		//HttpServletRequest request = CurrentContext.getRequest();
		//LogService.logSessionStart(request, "", "访问");
	}

	/**
	 * 监听session销毁，删除缓存文件
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		LogService.logSessionEnd(event.getSession().getId());
		LogService.logOperation(event.getSession());
		clearFileCache(event.getSession());
	}

	/**
	 * 清除文件缓存中的文件
	 * @param session
	 */
	private void clearFileCache(HttpSession session) {
		Enumeration<String> attributenames = session.getAttributeNames();
		while (attributenames.hasMoreElements()) {
			String attname = attributenames.nextElement();
			if (attname != null
					&& attname.startsWith(FileCacheUtil.FILECACHENAMESPACE)) {
				int dirstart = attname.indexOf('.');
				int dirend = attname.lastIndexOf('.');
				String filename = attname.substring(dirend + 1);
				String[] filesuffixes = { "xls", "xlsx", "doc", "docx" };
				boolean issuffix = false;
				String dirpathstr = attname.substring(dirstart + 1, dirend);
				for (String filesuffix : filesuffixes) {
					if (filesuffix.equals(filename)) {
						issuffix = true;
						break;
					}
				}
				if (issuffix) {
					dirend = dirpathstr.lastIndexOf('.');
					dirpathstr = attname.substring(dirstart + 1, dirend);
					filename = attname.substring(dirend + 1);
				}
				if (dirstart > -1 && dirend > -1 && dirstart != dirend) {

					dirpathstr = dirpathstr.replace('.', '/');
					FileCacheUtil.deleteFileCache(dirpathstr, filename);
				}
			}
		}
	}
}
