/**
 * FileCacheUtil.java
 *北京华通人商用信息有限公司版权所有
 */
package com.acmr.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletContextEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import acmr.web.core.CurrentContext;

/**
 * 文件缓存工具类 文件类缓存放在filecache文件夹下 根据分类自己建子文件夹
 * 在session以FILECACHENAMESPACE前缀存储文件缓存，将在session结束时会被自动清除，
 * filecache.后面的内容将会是在filecache文件夹下的子文件夹名称，多层时以.分割
 * 
 * @author zhangqiang
 *
 */
public class FileCacheUtil {
    private static Log log = LogFactory.getLog(FileCacheUtil.class);
    public static String FILECACHENAMESPACE = "filecache.";// session中属性前缀，

    /**
     * 
     * @param dirpath
     *            ：相对与filecache的子文件夹路径
     * @param filename
     *            :文件名
     */
    public static void write(String dirpath, String filename, byte[] datas) {
        if (datas == null || datas.length == 0) {
            return;
        }
        File cachefile = getCacheFile(dirpath, filename);
        if (cachefile != null) {
            OutputStream out = null;
            try {
                out = new BufferedOutputStream(new FileOutputStream(cachefile));
                out.write(datas);
            } catch (Exception e) {
                log.warn("写缓存文件时出错", e);
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        log.debug("关闭缓存输出流时出错", e);
                    }
                }
            }
        }
    }

    /**
     * 获得缓存文件内容
     * 
     * @param dirpath
     *            :相对与filecache的子文件夹路径
     * @param filename
     *            :文件名
     * @return
     */
    public static byte[] getCacheData(String dirpath, String filename) {
        File cachefile = getCacheFile(dirpath, filename);
        if (cachefile != null) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(cachefile));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int read = -1;
                while ((read = in.read(data)) != -1) {
                    out.write(data, 0, read);
                }
                out.close();
                return out.toByteArray();
            } catch (Exception e) {
                log.warn("读缓存文件时出错", e);
                return null;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.debug("关闭缓存输出流时出错", e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 删除缓存文件
     * 
     * @param dirpath
     *            :相对与filecache的子文件夹路径
     * @param filename
     *            :文件名
     * @return
     */
    public static void deleteFileCache(String dirpath, String filename) {
        File file = getCacheFile(dirpath, filename);
        if (file != null && file.exists()) {
            try {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        files[i].delete();
                    }
                }
                file.delete();
            } catch (Exception e) {
                log.debug("删除缓存文件出错", e);
            }
        }
    }

    public static File getCacheFile(String dirpath, String filename) {
    	 
        if (dirpath == null || filename == null || dirpath.isEmpty()
                || filename.isEmpty()
                || CurrentContext.getSession() == null) {
            return null;
        }
        String realdirpath = CurrentContext.getSession()
                .getServletContext()
                .getRealPath("WEB-INF/filecache/" + dirpath);
        try {
            File dirfile = new File(realdirpath);
            if (!dirfile.exists()) {
                dirfile.mkdirs();
            }
            return new File(dirfile.getAbsolutePath() + File.separator
                    + filename);
        } catch (Exception e) {
            log.warn("建立缓存文件出错", e);
            return null;
        }
    }

    public static void clear(ServletContextEvent context) {
        String realdirpath = context.getServletContext().getRealPath(
                "WEB-INF/filecache");
        File dirfile = new File(realdirpath);
        try {
            deleteDir(dirfile);
            dirfile.mkdirs();
        } catch (Exception e) {
            log.debug("初始化文件时出错", e);
        }

    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

}
