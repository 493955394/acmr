package com.acmr.web.jsp.theme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import acmr.cubeinput.MetaTableException;
import acmr.util.ListHashMap;
import acmr.util.PubInfo;
import acmr.web.control.BaseAction;
import acmr.web.core.CurrentContext;
import acmr.web.entity.ModelAndView;

import com.acmr.helper.util.StringUtil;
import com.acmr.model.pub.JSONReturnData;
import com.acmr.model.pub.TreeNode;
import com.acmr.model.security.Role;
import com.acmr.model.theme.Lanmu;
import com.acmr.model.theme.MenuRight;
import com.acmr.model.theme.ThemePo;
import com.acmr.model.theme.ThemeSon;
import com.acmr.service.security.RoleService;
import com.acmr.service.theme.ThemeService;
import com.alibaba.fastjson.JSON;

public class Theme extends BaseAction {

	private ThemeService themeService = ThemeService.factor.getInstance();

	public ModelAndView main() {
		List<ThemePo> tree = themeService.getThemeByProcode(null);
		if (tree.size() > 0) {
			// 设置第一个
			tree.get(0).setFirst("0");
			// 设置最后一个
			tree.get(tree.size() - 1).setLast("0");
		}
		return new ModelAndView("/WEB-INF/jsp/theme/index").addObject("list", tree);
	}

	// 目录树
	public void findTree() throws MetaTableException, IOException {
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("id");
		List<TreeNode> list = new ArrayList<TreeNode>();
		List<ThemePo> tree = themeService.getThemeByProcode(code);
		for (ThemePo themePo : tree) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(themePo.getCode());
			treeNode.setName(themePo.getCname());
			list.add(treeNode);

		}
		this.sendJson(list);
	}

	/***
	 * 获取子节点内容
	 * 
	 */
	public ModelAndView query() throws IOException {
		HttpServletRequest req = this.getRequest();
		// 获取查询数据
		String code = req.getParameter("id");
		// 判断是否pjax 请求
		String pjax = req.getHeader("X-PJAX");
		List<ThemePo> tree = themeService.getThemeByProcode(code);
		if (tree.size() > 0) {
			// 设置第一个
			tree.get(0).setFirst("0");
			// 设置最后一个
			tree.get(tree.size() - 1).setLast("0");
		}
		if (StringUtil.isEmpty(pjax)) {
			return new ModelAndView("/WEB-INF/jsp/theme/index").addObject("list", tree).addObject("procode", code).addObject("initTreePara", "/" + code);
		} else {
			return new ModelAndView("/WEB-INF/jsp/theme/tableList").addObject("list", tree);
		}
	}

	// 检查是否存在
	public void checkCode() {
		JSONReturnData data = new JSONReturnData(500, "id已存在");
		String code = this.getRequest().getParameter("code");
		try {
			if (!StringUtil.isEmpty(code)) {
				int count = themeService.checkCode(code);
				if (count == 0) {
					data.setReturncode(200);
					data.setReturndata("可以使用");
				}
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 检查是否存在
	public void checkLanmu() {
		JSONReturnData data = new JSONReturnData(500, "id不合法");
		String code = this.getRequest().getParameter("code");
		try {
			if (!StringUtil.isEmpty(code)) {
				int count = themeService.checkCode(code);
				if (count == 0) {
					List<Lanmu> lanmu = themeService.getLanmu();
					boolean flag = true;
					for (Lanmu lanmu2 : lanmu) {
						if (code.equals(lanmu2.getCode())) {
							data.setReturncode(200);
							data.setReturndata("可以使用");
							flag = false;
							break;
						}
					}
					if (flag) {
						data.setReturncode(500);
						data.setReturndata("ID不合法");
					}
				} else {
					data.setReturncode(500);
					data.setReturndata("id已存在");
				}
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跳转到新增页面
	 */
	public ModelAndView toAdd() {
		HttpServletRequest request = this.getRequest();
		String procodeId = request.getParameter("procodeId");
		List<ThemePo> tree = themeService.getThemeByProcode(null);
		List<Lanmu> lanmu = themeService.getLanmu();
		ListHashMap<Role> roles = RoleService.getRoles();
		return new ModelAndView("/WEB-INF/jsp/theme/add").addObject("tree", tree).addObject("procodeId", procodeId).addObject("lanmu", JSON.toJSONString(lanmu)).addObject("roles", roles).addObject("lanmus", lanmu);
	}

	// 保存模块
	public void toSave() throws Exception {
		Map<String, Object> map = this.getMulFormData();
		String code = PubInfo.getString(map.get("code"));
		String cname = PubInfo.getString(map.get("cname"));
		String procode = PubInfo.getString(map.get("fl"));
		String exp = PubInfo.getString(map.get("exp"));
		String childMode = PubInfo.getString(map.get("childMode"));
		FileItem tubiao = (FileItem) map.get("tubiao");
		String visible = PubInfo.getString(map.get("visible"));
		String modePower = PubInfo.getString(map.get("modePower"));
		ThemePo themePo = new ThemePo();
		themePo.setCode(code);
		themePo.setCname(cname);
		themePo.setHref("#");
		themePo.setProcode(procode);
		themePo.setExp(exp);
		themePo.setSort("2");
		themePo.setVisible(visible);

		if (tubiao != null) {
			InputStream inputStream = tubiao.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int b1 = 0;
			while ((b1 = inputStream.read()) != -1) {
				outStream.write(b1);
			}
			inputStream.close();
			outStream.flush();
			outStream.close();
			String photo = "data:image/jpeg;base64," + PubInfo.Base64FromBytes(outStream.toByteArray());
			themePo.setPhoto(photo);
		}
		themeService.save(themePo);
		// 三级菜单
		List<ThemeSon> parseArray = JSON.parseArray(childMode, ThemeSon.class);
		themeService.saveMenu(parseArray, code);

		if ("1".equals(visible)) { // 有权限
			themeService.insertRight(modePower, code);
		}
		this.getResponse().sendRedirect(this.getContextPath() + "/theme/theme.htm?m=query&id=" + procode);
	}

	/**
	 * 跳转到修改界面
	 */
	public ModelAndView toEdit() {
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		String procodeId = req.getParameter("procodeId");
		List<ThemePo> tree = themeService.getThemeByProcode(null);
		List<Lanmu> lanmu = themeService.getLanmu();
		ThemePo themePo = themeService.getByCode(code);
		List<ThemePo> list = themeService.getThemeByProcode(code);
		themePo.setChilds(list);
		ListHashMap<Role> roles = RoleService.getRoles();
		List<MenuRight> menuright = themeService.findMenuRight(code);
		return new ModelAndView("/WEB-INF/jsp/theme/edit").addObject("tree", tree).addObject("procodeId", procodeId).addObject("lanmu", JSON.toJSONString(lanmu)).addObject("themePo", themePo).addObject("roles", roles).addObject("menuright", JSON.toJSONString(menuright)).addObject("lanmus", lanmu);
	}

	// 编辑保存
	public void toEditSave() throws Exception {
		Map<String, Object> map = this.getMulFormData();
		String code = PubInfo.getString(map.get("code"));
		String cname = PubInfo.getString(map.get("cname"));
		String procode = PubInfo.getString(map.get("fl"));
		String exp = PubInfo.getString(map.get("exp"));
		String childMode = PubInfo.getString(map.get("childMode"));
		FileItem tubiao = (FileItem) map.get("tubiao");
		String visible = PubInfo.getString(map.get("visible"));
		String modePower = PubInfo.getString(map.get("modePower"));
		ThemePo themePo = new ThemePo();
		themePo.setCode(code);
		themePo.setCname(cname);
		themePo.setHref("#");
		themePo.setProcode(procode);
		themePo.setExp(exp);
		themePo.setSort("2");
		themePo.setVisible(visible);

		if (tubiao != null) {
			InputStream inputStream = tubiao.getInputStream();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int b1 = 0;
			while ((b1 = inputStream.read()) != -1) {
				outStream.write(b1);
			}
			inputStream.close();
			outStream.flush();
			outStream.close();
			String photo = PubInfo.Base64FromBytes(outStream.toByteArray());
			if (!StringUtil.isEmpty(photo)) {
				themePo.setPhoto("data:image/jpeg;base64," + photo);
			}
		}
		themeService.update(themePo);
		// 三级菜单
		List<ThemeSon> parseArray = JSON.parseArray(childMode, ThemeSon.class);
		themeService.saveMenu(parseArray, code);

		themeService.deleteRight(code);
		if ("1".equals(visible)) { // 有权限
			themeService.insertRight(modePower, code);
		}
		this.getResponse().sendRedirect(this.getContextPath() + "/theme/theme.htm?m=query&id=" + procode);
	}

	// 获取栏目
	public void getLanmu() {
		try {
			List<Lanmu> lanmu = themeService.getLanmu();
			JSONReturnData data = new JSONReturnData(200, "查询成功");
			data.setReturndata(lanmu);
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 保存分类
	public void savefl() {
		JSONReturnData data = new JSONReturnData(500, "添加失败");
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		String cname = req.getParameter("cname");
		ThemePo themePo = new ThemePo();
		themePo.setCode(code);
		themePo.setCname(cname);
		themePo.setHref("#");
		try {
			int count = themeService.save(themePo);
			if (count > 0) {
				data.setReturncode(200);
				data.setReturndata("添加成功");
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 修改分类
	public void updatefl() {
		JSONReturnData data = new JSONReturnData(500, "修改失败");
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		String cname = req.getParameter("cname");
		ThemePo themePo = new ThemePo();
		themePo.setCode(code);
		themePo.setCname(cname);
		themePo.setHref("#");
		try {
			int count = themeService.updatefl(themePo);
			if (count > 0) {
				data.setReturncode(200);
				data.setReturndata("修改成功");
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 删除
	public void toDelete() {
		JSONReturnData data = new JSONReturnData(500, "删除失败");
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		try {
			if (!StringUtil.isEmpty(code)) {
				int count = themeService.deleteByCode(code);
				if (count > 0) {
					data.setReturncode(200);
					data.setReturndata("删除成功");
				}
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getByCode() {
		JSONReturnData data = new JSONReturnData(500, "查询失败");
		HttpServletRequest req = this.getRequest();
		String code = req.getParameter("code");
		try {
			if (!StringUtil.isEmpty(code)) {
				ThemePo theme = themeService.getByCode(code);
				if (theme != null) {
					data.setReturncode(200);
					data.setReturndata(theme);
				}
			}
			this.sendJson(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上移下移
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @author chenyf
	 */
	public void move() throws IOException {
		// 构造返回对象
		JSONReturnData data = new JSONReturnData(501, "移动失败");
		HttpServletRequest req = CurrentContext.getRequest();
		String currentCode = req.getParameter("currentId");
		String siblingsCode = req.getParameter("siblingsId");
		int updateRows = themeService.move(currentCode, siblingsCode);
		if (updateRows == 2) {
			data.setReturncode(200);
			data.setReturndata("移动成功");
		}
		this.sendJson(data);
	}

	public void search() {
		JSONReturnData data = new JSONReturnData(500, "搜索失败");

		String key = this.getRequest().getParameter("key");
		if (!StringUtil.isEmpty(key)) {
			List<Lanmu> searchLanmu = themeService.searchLanmu(key);
			data.setReturncode(200);
			data.setReturndata(searchLanmu);
		}
		try {
			this.sendJson(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
