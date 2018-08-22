package com.acmr.model.sysindex;

import java.util.ArrayList;

import com.acmr.helper.util.StringUtil;
import com.alibaba.fastjson.JSON;

public class Stream {
	private String vittedno;
	private String person;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVittedno() {
		return vittedno;
	}

	public void setVittedno(String vittedno) {
		this.vittedno = vittedno;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public static void main(String[] args) {
//		Stream stream1 = new Stream();
//		List<Stream> streams = new ArrayList<Stream>();
//		stream1.setVittedno("single");
//		stream1.setPerson("1,2");
//		Stream stream2 = new Stream();
//		stream2.setVittedno("all");
//		stream2.setPerson("1,2");
//		streams.add(stream1);
//		streams.add(stream2);
//		String s = "[{'vittedno':'single','person':'TTJX1,ZZZZ'},{'vittedno':'single2','person':'2,2'}]";
//		List<Stream> streamss = (List<Stream>) JSON.parseArray(s, Stream.class);
//		Stream audit1 = streamss.get(0);
//		Stream audit2 = streamss.get(1);
		String content = "|@SKWS@ZZZZ>|@ZZZZ>&@skwjcc@skwzhc";
		ArrayList<Stream> list = new ArrayList<Stream>();
		if(!StringUtil.isEmpty(content)){
			String[] split = content.split(">");
			for (String str : split) {
				Stream stream = new Stream();
				String mark = "all";
				switch (str.substring(0, 1)) {
				case "|":
					mark = "single";
					break;
				case "&":
					mark = "all";
					break;
				default:
					break;
				}
				stream.setVittedno(mark);
				String substring = str.substring(1);
				stream.setName(substring.substring(1).replaceAll("@", ","));
				stream.setPerson(substring.substring(1).replaceAll("@", ","));
				list.add(stream);
			}
		}
		
		System.out.println(JSON.toJSONString(list));
		
		
	}

}
