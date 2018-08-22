package com.acmr.service.ssologin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import acmr.util.returnData;
import acmr.web.core.CurrentContext;

public class HttpClient {

	private String charset;
	private int connecttimeout;
	private int readtimeout;
	private String proxyhost;
	private Integer proxyport;

	public HttpClient() {
		charset = "utf-8";
		connecttimeout = 200 * 1000;
		readtimeout = 200 * 1000;
		proxyhost = null;
		proxyport = null;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getConnecttimeout() {
		return connecttimeout;
	}

	public void setConnecttimeout(int connecttimeout) {
		this.connecttimeout = connecttimeout;
	}

	public int getReadtimeout() {
		return readtimeout;
	}

	public void setReadtimeout(int readtimeout) {
		this.readtimeout = readtimeout;
	}

	public String getProxyhost() {
		return proxyhost;
	}

	public void setProxyhost(String proxyhost) {
		this.proxyhost = proxyhost;
	}

	public Integer getProxyport() {
		return proxyport;
	}

	public void setProxyport(Integer proxyport) {
		this.proxyport = proxyport;
	}

	public String getHttp(String urlstr) {
		returnData re = this.getHttpUrl(urlstr, "");
		if (re.getCode() < 300) {
			return re.getData().toString();
		} else {
			return "";
		}
	}

	public String getHttp(String urlstr, String args) {
		returnData re = this.getHttpUrl(urlstr, args);
		if (re.getCode() < 300) {
			return re.getData().toString();
		} else {
			return "";
		}
	}

	public String getHttp(String urlstr, Map<String, String> args) {
		String str1 = "";
		if (args != null) {
			for (String key : args.keySet()) {
				String value1 = args.get(key);
				if (value1 == null) {
					value1 = "";
				}
				str1 += "&" + key + "=" + acmr.util.PubInfo.UrlEncode(value1);
			}
			str1 = str1.substring(1);
		}
		return this.getHttp(urlstr, str1);
	}

	private returnData getHttpUrl(String urlstr, String args) {

		returnData red = new returnData();
		OutputStream os = null;
		OutputStreamWriter ot = null;

		InputStream is = null;
		InputStreamReader ir = null;
		BufferedReader br = null;

		try {
			if (urlstr.toLowerCase().indexOf("http://") != 0) {
				HttpServletRequest req = CurrentContext.getRequest();
				String server = req.getScheme() + "://" + req.getServerName();
				if (req.getServerPort() != 80) {
					server += ":" + req.getServerPort();
				}
				urlstr = server + urlstr;
			}
			URL url = new URL(urlstr);
			HttpURLConnection connl = this.getHttpConnect(url);
			connl.setRequestProperty("accept", "*/*");
			connl.setRequestProperty("Accept-Charset", this.charset);
			connl.setRequestProperty("connection", "Keep-Alive");
			connl.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			connl.setDoInput(true);
			connl.setReadTimeout(200 * 1000);
			connl.setConnectTimeout(200 * 1000);
			if (args == null || args.equals("")) {
				connl.setDoOutput(false);
				connl.setRequestMethod("GET");
			} else {
				connl.setDoOutput(true);
				connl.setRequestMethod("POST");
				connl.setRequestProperty("Content-Length", String.valueOf(args.length()));
				os = connl.getOutputStream();
				ot = new OutputStreamWriter(os, charset);
				ot.write(args);
				ot.flush();
				ot.close();
				os.close();
			}
			red.setCode(connl.getResponseCode());
			if (connl.getResponseCode() < 300) {
				is = connl.getInputStream();
				ir = new InputStreamReader(is, charset);
				br = new BufferedReader(ir);
				String strline = "";
				StringBuffer result = new StringBuffer();
				while ((strline = br.readLine()) != null) {
					result.append(strline + "\n");
				}
				red.setData(result.toString());
				br.close();
				ir.close();
				is.close();
			} else {
				red.setData(connl.getResponseMessage());
			}
		} catch (Exception e) {
			red.setCode(900);
			red.setData(e.getMessage());

		}
		return red;
	}

	private HttpURLConnection getHttpConnect(URL url1) throws IOException {
		HttpURLConnection connl = null;
		if (proxyhost != null && proxyport != null) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyhost, proxyport));
			connl = (HttpURLConnection) url1.openConnection(proxy);
		} else {
			connl = (HttpURLConnection) url1.openConnection();
		}
		connl.setConnectTimeout(connecttimeout);
		connl.setReadTimeout(readtimeout);
		return connl;
	}
}
