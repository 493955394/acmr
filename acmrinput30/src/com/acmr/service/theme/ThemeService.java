package com.acmr.service.theme;

import java.util.List;

import com.acmr.dao.theme.IThemeDao;
import com.acmr.dao.theme.ThemeDao;
import com.acmr.helper.util.StringUtil;
import com.acmr.model.security.User;
import com.acmr.model.theme.Lanmu;
import com.acmr.model.theme.MenuRight;
import com.acmr.model.theme.ThemePo;
import com.acmr.model.theme.ThemeSon;
import com.alibaba.fastjson.JSON;

public class ThemeService {

	private IThemeDao themeDao = ThemeDao.Fator.getInstance().getDao();

	public static class factor {
		private static ThemeService service = new ThemeService();

		public static ThemeService getInstance() {
			return service;
		}
	}

	public List<ThemePo> getThemeByProcode(String procode) {
		return themeDao.getThemeByProcode(procode);
	}

	public int checkCode(String code) {
		return themeDao.checkCode(code);
	}

	public int save(ThemePo themePo) {
		return themeDao.save(themePo);
	}

	public int update(ThemePo themePo) {
		return themeDao.update(themePo);
	}

	public int updatefl(ThemePo themePo) {
		return themeDao.updatefl(themePo);
	}

	public int deleteByCode(String code) {
		return themeDao.deleteByCode(code);
	}

	public ThemePo getByCode(String code) {
		return themeDao.getByCode(code);
	}

	public List<Lanmu> getLanmu() {
		return themeDao.getLanmu();
	}

	public int saveMenu(List<ThemeSon> sons, String code) {
		return themeDao.saveMenu(sons, code);
	}

	public void insertRight(String modePower, String code) {
		if (StringUtil.isEmpty(modePower) || StringUtil.isEmpty(code)) {
			return;
		}
		List<MenuRight> modepower = JSON.parseArray(modePower, MenuRight.class);
		for (MenuRight menuRight2 : modepower) {
			menuRight2.setMenucode(code);
			themeDao.insertRight(menuRight2);
		}
	}

	public int deleteRight(String menucode) {
		return themeDao.deleteRight(menucode);
	}

	public List<MenuRight> findMenuRight(String menucode) {
		return themeDao.findMenuRight(menucode);
	}

	public List<String> getMenuCode() {
		return themeDao.getMenuCode();
	}

	public String getAllTopmenuRight(User user) {
		return themeDao.getAllTopmenuRight(user);
	}

	public String cansee(String code, User user) {
		return themeDao.cansee(code, user);
	}

	public int move(String currentCode, String nextCode) {
		return themeDao.move(currentCode, nextCode);
	}

	public List<Lanmu> searchLanmu(String key) {
		return themeDao.searchLanmu(key);
	}

}
