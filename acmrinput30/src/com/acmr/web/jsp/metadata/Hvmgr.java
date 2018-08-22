package com.acmr.web.jsp.metadata;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import acmr.cubeinput.MetaTableException;
import acmr.cubeinput.service.CubeUnitManager;
import acmr.cubeinput.service.cubeinput.entity.CubeUnit;
import acmr.cubeinput.service.metatable.entity.SqlWhere;
import acmr.excel.pojo.Constants.XLSTYPE;
import acmr.excel.pojo.ExcelBook;
import acmr.excel.pojo.ExcelCell;
import acmr.excel.pojo.ExcelRow;
import acmr.excel.pojo.ExcelSheet;
import acmr.util.DataTable;
import acmr.web.control.BaseAction;
import acmr.web.core.CurrentContext;
import acmr.web.entity.ModelAndView;

import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.PageBean;
import com.acmr.service.metadata.MetaDataExport;
import com.acmr.service.metadata.MetaService;
import com.acmr.service.metadata.MetaServiceManager;

/**
 * 模块：元数据管理 -> 汇率管理 action层
 * 
 * @author bjwb1
 */
public class Hvmgr extends BaseAction {

	/**
	 * 获取service层对象（cube）
	 * 
	 * @author bjwb1
	 */
	private MetaServiceManager metaService = MetaService.getMetaService("hv");

	/**
	 * 主界面跳转方法
	 * 
	 * @author bjwb1
	 */
	public ModelAndView main() {
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(this.getRequest().getRequestURI());
		sb.append("?m=query&id=all");
		List<Map<String, Object>> allUnit = new ArrayList<Map<String, Object>>();
		try {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			where1.add(new SqlWhere("sj", "all", 10));
			List<Map<String, Object>> list = metaService.QueryData_ByPage(null, where1, "id", page.getPageNum() - 1, page.getPageSize());
			int queryCount = metaService.QueryCount(where1);
			CompletionName(list);
			page.setData(list);
			page.setTotalRecorder(queryCount);
			page.setUrl(sb.toString());
			String sql = "select distinct code from TB_BASIC_UNIT_SJ";
			// 查询当前可以看的所有货币
			DataTable dataTable = metaService.queryDataSql(sql, new Object[] {});
			getAllUnit(dataTable, allUnit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/WEB-INF/jsp/metadata/hvmgr/index").addObject("page", page).addObject("allunit", allUnit).addObject("procode", "all");
	}

	/***
	 * 获取子节点内容
	 * 
	 */
	public ModelAndView query() throws IOException {
		HttpServletRequest req = this.getRequest();
		// 获取查询数据
		String code = req.getParameter("id");
		String unitcode = req.getParameter("unitcode");
		String sjcode = req.getParameter("sjcode");

		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");

		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<Map<String, Object>> allUnit = new ArrayList<Map<String, Object>>();

		String pageUrl = MetaService.getPageUrl(req);
		try {
			List<SqlWhere> where1 = new ArrayList<SqlWhere>();
			if (!StringUtil.isEmpty(code)) {
				where1.add(new SqlWhere("sj", code + "%", 20));
			} else {
				where1.add(new SqlWhere("sj", "all", 18));
			}

			if (!StringUtil.isEmpty(unitcode)) {
				where1.add(new SqlWhere("code", unitcode, 10));
			}

			if (!StringUtil.isEmpty(sjcode)) {
				if ("M".equals(sjcode)) {
					where1.add(new SqlWhere("sj", "______", 20));
				} else if ("Q".equals(sjcode)) {
					where1.add(new SqlWhere("sj", "_____", 20));
				} else if ("Y".equals(sjcode)) {
					where1.add(new SqlWhere("sj", "____", 20));
				}
			}
			List<Map<String, Object>> list = metaService.QueryData_ByPage(null, where1, "id", page.getPageNum() - 1, page.getPageSize());
			int queryCount = metaService.QueryCount(where1);
			CompletionName(list);
			page.setData(list);
			page.setTotalRecorder(queryCount);
			page.setUrl(pageUrl);

			String sql = "select distinct code from TB_BASIC_UNIT_SJ";
			// 查询当前可以看的所有货币
			DataTable dataTable = metaService.queryDataSql(sql, new Object[] {});
			getAllUnit(dataTable, allUnit);

			// 发送返回数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/metadata/hvmgr/index").addObject("page", page).addObject("allunit", allUnit).addObject("unitcode", unitcode).addObject("sjcode", sjcode).addObject("procode", code);
		} else {
			return new ModelAndView("/WEB-INF/jsp/metadata/hvmgr/tableList").addObject("page", page);
		}
	}

	/**
	 * 跳转到新增页面
	 */
	public ModelAndView toAdd() {
		String procode = this.getRequest().getParameter("procodeId");
		String procodeName = this.getRequest().getParameter("procodeName");
		return new ModelAndView("/WEB-INF/jsp/metadata/hvmgr/add").addObject("procode", procode).addObject("procodeName", procodeName);
	}

	/**
	 * 保存数据
	 */
	public void toSave() throws IOException {
		HttpServletRequest req = this.getRequest();
		String id = req.getParameter("id"); // id
		String sj = req.getParameter("sj");
		String code = req.getParameter("code");
		String rate = req.getParameter("rate");

		JSONReturnData data = new JSONReturnData(500, "操作失败");
		Map<String, Object> codes = new HashMap<String, Object>();
		codes.put("sj", sj);
		codes.put("code", code);
		codes.put("rate", rate);

		try {
			List<Map<String, Object>> list = checkExist(sj, code);
			if (list != null && list.size() > 0) {
				id = (String) list.get(0).get("id");
			}

			// 添加时的数据
			if (StringUtil.isEmpty(id)) {
				codes.put("sort", metaService.getTbcode());
				int insertRow = metaService.InsertRow(codes);
				if (insertRow == 1) {
					data.setReturncode(200);
					data.setParam1(sj);
					data.setReturndata("操作成功");
				}
			} else {
				Map<String, Object> keys = new HashMap<String, Object>();
				keys.put("id", id);
				int updateRow = metaService.UpdateRow(keys, codes);
				if (updateRow == 1) {
					data.setReturncode(200);
					data.setParam1(sj);
					data.setReturndata("操作成功");
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.sendJson(data);
	}

	/**
	 * 跳转到修改界面
	 */
	public ModelAndView toEdit() {
		String id = this.getRequest().getParameter("id");
		String procodeName = "默认汇率";
		String unicode = "";// 单位名称

		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> queryData = metaService.QueryData(null, "id='" + id + "'", "id");
			if (queryData.size() > 0) {
				data = queryData.get(0);
			}
			String procode = (String) data.get("sj");
			String unitcode = (String) data.get("code");

			List<String> codes = new ArrayList<String>();
			codes.add("code");
			codes.add("cname");

			// 获取父节点名称
			if (!StringUtil.isEmpty(procode)) {
				DataTable tree = MetaService.getMetaService("sj").getTree(codes, "code='" + procode + "'", "sortcode");
				if (tree.getRows().size() > 0) {
					procodeName = tree.getRows().get(0).getString("cname");
				}
			}

			// 获取单位的名称
			if (!StringUtil.isEmpty(unitcode)) {
				List<Map<String, Object>> queryData2 = MetaService.getMetaService("unit").QueryData(codes, "code='" + unitcode + "'", "sortcode");
				if (queryData2.size() > 0) {
					unicode = (String) queryData2.get(0).get("cname");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("/WEB-INF/jsp/metadata/hvmgr/edit").addObject(data).addObject("procodeName", procodeName).addObject("unicode", unicode);
	}

	/**
	 * 进行单个数据的删除
	 */
	public void toDelete() {
		HttpServletRequest req = this.getRequest();
		String id = req.getParameter("delId");
		int result = 0;
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "删除失败");
		try {
			if (StringUtil.isEmpty(id)) {
				this.sendJson(data);
				return;
			}
			Map<String, Object> keys = new HashMap<String, Object>();
			keys.put("id", id);
			result = metaService.deleteRow(keys);
			if (result != 0) {
				data.setReturncode(200);
				data.setReturndata("删除成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.sendJson(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 检查是否存在
	public void checkExist() {
		JSONReturnData data = new JSONReturnData(200, "");
		String sj = this.getRequest().getParameter("sj");
		String code = this.getRequest().getParameter("code");
		try {
			if (!StringUtil.isEmpty(sj) && !StringUtil.isEmpty(code)) {
				List<Map<String, Object>> list = checkExist(sj, code);
				if (list != null && list.size() > 0) {
					data.setReturndata(list.get(0).get("rate"));
				}
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 检查是否存在
	private List<Map<String, Object>> checkExist(String sj, String code) throws MetaTableException {
		List<Map<String, Object>> list = null;
		PageBean<Map<String, Object>> page = new PageBean<Map<String, Object>>();
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		where1.add(new SqlWhere("sj", sj, 10));
		where1.add(new SqlWhere("code", code, 10));
		list = metaService.QueryData_ByPage(null, where1, "id", page.getPageNum() - 1, page.getPageSize());
		return list;
	}

	private void CompletionName(List<Map<String, Object>> list) throws MetaTableException {
		CubeUnitManager cubeunit = CubeUnitManager.CubeUnitManagerFactor.getInstance("");
		MetaServiceManager cubesj = MetaService.getMetaService("sj");
		List<SqlWhere> where = new ArrayList<SqlWhere>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String unitcode = (String) map.get("code");
			CubeUnit unit = cubeunit.getUnit(unitcode);
			String unitname = unit.getName();
			if (StringUtil.isEmpty(unitname)) {
				unitname = unitcode;
			}
			map.put("unitname", unitname);

			where.clear();

			String sjcode = (String) map.get("sj");
			String sjname = "";
			where.add(new SqlWhere("code", sjcode, 10));
			List<String> codes = new ArrayList<String>();
			codes.add("cname");
			List<Map<String, Object>> queryData = cubesj.QueryData(codes, where, "sortcode");
			if (queryData.size() > 0) {
				sjname = (String) queryData.get(0).get("cname");
			}
			if (StringUtil.isEmpty(sjname)) {
				sjname = sjcode;
			}
			map.put("sjname", sjname);
		}
	}

	private List<Map<String, Object>> getAllUnit(DataTable dataTable, List<Map<String, Object>> list) {
		CubeUnitManager cubeunit = CubeUnitManager.CubeUnitManagerFactor.getInstance("");
		if (dataTable != null && dataTable.getRows().size() > 0) {
			for (int i = 0; i < dataTable.getRows().size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				String unitcode = dataTable.getRows().get(i).getString("code");
				CubeUnit unit = cubeunit.getUnit(unitcode);
				String unitname = unit.getName();
				if (StringUtil.isEmpty(unitname)) {
					unitname = unitcode;
				}
				map.put("unitcode", unitcode);
				map.put("unitname", unitname);
				list.add(map);
			}
		}
		return list;
	}

	// 批量导出
	public void export() throws IOException {
		HttpServletRequest req = this.getRequest();
		// 获取查询数据
		String code = req.getParameter("id");
		String unitcode = req.getParameter("unitcode");
		String sjcode = req.getParameter("sjcode");

		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		if (!StringUtil.isEmpty(code)) {
			where1.add(new SqlWhere("sj", code + "%", 20));
		} else {
			where1.add(new SqlWhere("sj", "all", 18));
		}

		if (!StringUtil.isEmpty(unitcode)) {
			where1.add(new SqlWhere("code", unitcode, 10));
		}

		if (!StringUtil.isEmpty(sjcode)) {
			if ("M".equals(sjcode)) {
				where1.add(new SqlWhere("sj", "______", 20));
			} else if ("Q".equals(sjcode)) {
				where1.add(new SqlWhere("sj", "_____", 20));
			} else if ("Y".equals(sjcode)) {
				where1.add(new SqlWhere("sj", "____", 20));
			}
		}
		try {
			List<Map<String, Object>> list = metaService.QueryData(null, where1, "id");
			CompletionName(list);
			MetaDataExport.export(req, this.getResponse(), list, getTitle(), getField(), "", null, "汇率管理");
		} catch (MetaTableException e) {
			e.printStackTrace();
		}
	}

	public void exportData() throws IOException, MetaTableException {
		HttpServletRequest req = this.getRequest();
		// 获取查询数据
		String codes = req.getParameter("ids");
		// 构造返回对象
		if (StringUtil.isEmpty(codes)) {
			MetaDataExport.export(req, this.getResponse(), new ArrayList<Map<String, Object>>(), getTitle(), getField(), null, null, "汇率管理");
			return;
		}
		String[] ids = codes.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			sb.append(ids[i]);
			if (i < ids.length - 1) {
				sb.append(";");
			}
		}
		List<SqlWhere> where1 = new ArrayList<SqlWhere>();
		where1.add(new SqlWhere("id", sb.toString(), 16));
		List<Map<String, Object>> list = metaService.QueryData(null, where1, "id");
		CompletionName(list);
		MetaDataExport.export(req, this.getResponse(), list, getTitle(), getField(), null, null, "汇率管理");
	}

	/**
	 * @param 批量导入
	 */
	public void importData() {
		JSONReturnData data = new JSONReturnData("");
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
		uploader.setHeaderEncoding("utf-8");
		try {
			ArrayList<FileItem> files = (ArrayList<FileItem>) uploader.parseRequest(this.getRequest());
			if (files.size() > 0) {
				FileItem file = files.get(0);
				String name = file.getName();
				if (file != null) {
					try {
						XLSTYPE xlstype = XLSTYPE.XLS;
						if (name.endsWith("xlsx")) {
							xlstype = XLSTYPE.XLSX;
						}
						ExcelBook book1 = new ExcelBook();
						book1.LoadExcel(file.getInputStream(), xlstype);
						ExcelSheet sheet = book1.getSheets().get(0);
						if (sheet == null) {
							data.setReturncode(500);
							data.setReturndata("没有发现上传的文件");
							this.sendJson(data);
							return;
						}
						int rows = sheet.getRows().size();
						// 必填项
						Map<Integer, String> mkey = new HashMap<Integer, String>();
						// 数据量
						int count = 0;
						boolean flag = true;
						// 遍历标识列
						if (rows >= 1 && sheet.getRows().get(1) != null) {
							ExcelRow firstRow = sheet.getRows().get(1);
							if (firstRow != null) {
								int cells = firstRow.getCells().size();
								for (int i = 0; i < cells; i++) {
									ExcelCell cell = firstRow.getCells().get(i);
									if (cell != null) {
										String value = cell.getText() + "";
										if (StringUtil.isEmpty(value)) {
											continue;
										}
										if (isOther(value) && !mkey.containsValue(value)) {
											mkey.put(i, value);
										}
									}
								}
							}
						}
						List<Map<String, Object>> insert = new ArrayList<Map<String, Object>>(); // 插入
						boolean iState = false;
						boolean uState = false;
						List<Map<String, Object>> update = new ArrayList<Map<String, Object>>(); // 更新
						List<Map<String, Object>> upKey = new ArrayList<Map<String, Object>>(); // 更新 key

						// 如果标识列不为空，则遍历内容
						if (mkey.size() == getOther().size()) {
							// 遍历所有值
							for (int i = 2; i < rows; i++) {
								Map<String, Object> codes = new HashMap<String, Object>();
								ExcelRow row = sheet.getRows().get(i);
								if (row == null || row.isNull()) {
									continue;
								}
								// 处理非必填项列
								for (Iterator<Integer> iterator = mkey.keySet().iterator(); iterator.hasNext();) {
									Integer integer = iterator.next();
									String key = mkey.get(integer);
									ExcelCell cell = row.getCells().get(integer);
									if (cell == null) {
										cell = new ExcelCell();
									}
									String value = metaService.getValue(cell.getText() + "");
									if (!StringUtil.isEmpty(value)) {
										codes.put(key, value);
									}
								}
								codes.put("sort", metaService.getTbcode());
								Object sj = codes.get("sj");
								Object code = codes.get("code");
								if (sj == null || code == null) {
									flag = false;
								} else {
									// 查询是否存在
									String id = "";
									List<Map<String, Object>> list = checkExist((String) sj, (String) code);
									if (list != null && list.size() > 0) {
										id = (String) list.get(0).get("id");
									}

									if (StringUtil.isEmpty(id)) {
										insert.add(codes);
										iState = true;
									} else {
										update.add(codes);
										Map<String, Object> e = new HashMap<String, Object>();
										e.put("id", id);
										upKey.add(e);
										uState = true;
									}
									count++;
								}
							}

							// 如果插入的数据量大于10000条，则提示用户数量超标
							if (count >= 10000) {
								data.setReturncode(400);
								data.setReturndata("导入的数据不能超过10000行，目前有" + count + "行");
								return;
							}

							if (!flag) {
								data.setReturncode(400);
								data.setReturndata("导入的数据有错误，时间或单位有错误");
								return;
							}

							// 入库
							if (iState) {
								metaService.InsertRows(insert);
							}
							if (uState) {
								metaService.UpdateRows(upKey,update);
							}
							data.setParam1(count);
							data.setReturncode(200);
							data.setReturndata("数据文件上传成功");

						} else {
							data.setReturncode(400);
							Set<String> must = getOther().keySet();
							data.setReturndata("模板格式不正确，第二行数据必须包含：" + must.toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
						data.setReturncode(500);
						data.setReturndata("数据导入失败");
					}
				}
			} else {
				data.setReturncode(500);
				data.setReturndata("没有发现上传的文件");
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断模板中的字段是否为非必填项
	 * 
	 * @param code
	 * @return
	 * @author chenyf
	 */
	private static boolean isOther(String code) {
		Map<String, String> map = getOther();
		String value = map.get(code);
		if (value != null) {
			return true;
		}
		return false;
	}

	/**
	 * 非必填项
	 * 
	 * @return
	 */
	private static Map<String, String> getOther() {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("sj", "sj");
		hashMap.put("code", "code");
		hashMap.put("rate", "rate");
		return hashMap;
	}

	/**
	 * 批量导入模板下载
	 * 
	 */
	public void templateDownload() {
		HttpServletRequest req = CurrentContext.getRequest();
		HttpServletResponse resp = CurrentContext.getResponse();

		String realPath = req.getServletContext().getRealPath("/template");
		String file = "汇率导入模板.xlsx";
		try {
			// 定义输出类型
			resp.reset();
			resp.setContentType("application/vnd.ms-excel");
			resp.setHeader("Pragma", "public");
			resp.setHeader("Cache-Control", "max-age=30");
			String filename = new String(file.getBytes("gb2312"), "iso8859-1").replace(" ", "");
			resp.setHeader("Content-disposition", "attachment;filename=" + filename);
			// 生成Excel并响应客户端
			ServletOutputStream out = resp.getOutputStream();
			InputStream inputStream = new FileInputStream(realPath + "\\" + file);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int b1 = 0;
			while ((b1 = inputStream.read()) != -1) {
				outStream.write(b1);
			}
			inputStream.close();
			resp.setContentLength(outStream.size());
			outStream.writeTo(out);
			out.flush();
			out.close();
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出的文件列
	 * 
	 * @return
	 * @author chenyf
	 */
	private static List<String> getField() {
		List<String> field = new ArrayList<String>();
		field.add("code");
		field.add("unitname");
		field.add("sj");
		field.add("sjname");
		field.add("rate");
		return field;
	}

	/**
	 * 导出的文件Title
	 * 
	 * @return
	 * @author chenyf
	 */
	private static List<String> getTitle() {
		List<String> title = new ArrayList<String>();
		title.add("序号");
		title.add("货币代码");
		title.add("货币名称 ");
		title.add("时间代码");
		title.add("时间名称");
		title.add("美元汇率");
		return title;
	}

}
