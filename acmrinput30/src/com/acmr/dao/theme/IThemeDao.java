package com.acmr.dao.theme;

import java.util.List;

import com.acmr.model.security.User;
import com.acmr.model.theme.Lanmu;
import com.acmr.model.theme.MenuRight;
import com.acmr.model.theme.ThemePo;
import com.acmr.model.theme.ThemeSon;

public interface IThemeDao {

	public List<ThemePo> getThemeByProcode(String procode);

	public int checkCode(String code);

	public int save(ThemePo themePo);

	public int deleteByCode(String code);

	public ThemePo getByCode(String code);

	public int updatefl(ThemePo themePo);

	public List<Lanmu> getLanmu();

	public int saveMenu(List<ThemeSon> sons, String code);

	public int update(ThemePo themePo);

	public int insertRight(MenuRight menuright);

	public int deleteRight(String menucode);

	public List<MenuRight> findMenuRight(String menucode);

	public List<String> getMenuCode();

	public String getAllTopmenuRight(User user);

	public int move(String currentCode, String nextCode);

	public String cansee(String code, User user);

	public List<Lanmu> searchLanmu(String key);

}
