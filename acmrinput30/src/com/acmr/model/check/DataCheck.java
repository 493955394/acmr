package com.acmr.model.check;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import acmr.cubeinput.CubeException;
import acmr.cubeinput.service.CubeInputSev;
import acmr.cubeinput.service.CubeUnitManager;
import acmr.cubeinput.service.cubeinput.CubeSjUtil;
import acmr.cubeinput.service.cubeinput.entity.CubeData;
import acmr.cubeinput.service.cubeinput.entity.CubeQueryData;
import acmr.cubeinput.service.cubeinput.entity.CubeQueryWhere;
import acmr.cubeinput.service.cubeinput.entity.CubeUnit;
import acmr.cubeinput.service.cubeinput.entity.CubeWdCode;
import acmr.cubeinput.service.cubeinput.entity.CubeWeidu;
import acmr.util.DataTable;
import acmr.util.DataTableRow;
import acmr.util.ListHashMap;
import acmr.util.PubInfo;

import com.acmr.helper.constants.Const;

/**
 * @author zengqu 数据检查
 */
public class DataCheck {

	private CubeInputSev cube1; // cube

	private ListHashMap<checkDataList> dls; // 临时表数据转成能快速定位的表示方式

	private List<CubeQueryWhere> querywheres; // 临时表数据对应的查询条件

	private CheckResult resultdt; // 结果数据

	/**
	 * 返回检查的结果
	 * 
	 * @return
	 */
	public CheckResult getResult() {
		return resultdt;
	}

	/**
	 * 第一步，初始化数据和数据库，把数据转成需要的格式
	 * 
	 * @param dbcode
	 *            cube数据库代码
	 * @param dt1
	 *            需要检查的数据
	 */
	public void Step1_InitData(String dbcode, DataTable dt1) {
		cube1 = CubeInputSev.CubeInputSevFactor.getInstance(dbcode);
		dls = new ListHashMap<checkDataList>();
		querywheres = new ArrayList<CubeQueryWhere>();
		ListHashMap<CubeWeidu> wlists = cube1.getWdList();
		for (int i = 0; i < dt1.getRows().size(); i++) {
			DataTableRow dr1 = dt1.getRows().get(i);
			checkDataList dl1 = new checkDataList();
			CubeQueryWhere w1 = new CubeQueryWhere();
			ListHashMap<CubeWdCode> wds = new ListHashMap<CubeWdCode>();
			String code1 = ""; // 关键字
			for (int j = 0; j < wlists.size(); j++) {
				String wcode = wlists.get(j).getCode();
				String wvalue = dr1.getString(wcode);
				wds.add(new CubeWdCode(wcode, wvalue));
				code1 += "_" + wcode + "." + wvalue; // 关键字
			}
			code1 = code1.substring(1);
			w1.setWds(wds);
			w1.setUnitcode(dr1.getString("unit"));
			w1.setDotcount(5);
			querywheres.add(w1);
			dl1.setCode(code1);
			CubeQueryData qdata = new CubeQueryData();
			qdata.setWds(w1.clone().getWds());
			CubeData data = new CubeData();
			data.setData(dr1.getDouble("data"));
			data.setUnitcode(w1.getUnitcode());
			qdata.setData(data);
			dl1.setData(qdata);
			dl1.setCelladdr(getExcellCellAddr(dr1.getString("cellloc")));
			dls.add(dl1);
		}
	}

	/**
	 * 得到关键字代码
	 * 
	 * @param wds
	 *            维度列表
	 * @param wdcode
	 *            需要替换的维度
	 * @param repvalue
	 *            需要替换的值
	 * @return 关键字
	 */
	private String getcode(ListHashMap<CubeWdCode> wds, String wdcode, String repvalue) {
		String code1 = "";
		ListHashMap<CubeWeidu> wlists = cube1.getWdList();
		for (int i = 0; i < wlists.size(); i++) {
			String wdcode1 = wlists.get(i).getCode();
			String code = wds.get(wdcode1).getCode();
			if (wdcode1.equalsIgnoreCase(wdcode) && !repvalue.equals("")) {
				code = repvalue;
			}
			code1 += "_" + wdcode1 + "." + code;
		}
		code1 = code1.substring(1);
		return code1;
	}

	/**
	 * 第三步：与底库的重复检查，现在只要有就重复就出来，不能与时间类型检查一起使用
	 * 
	 * @throws CubeException
	 */
	public void step3_checkCF() throws CubeException {
		CheckResult re1 = this.resultdt;
		if (re1 == null) {
			re1 = new CheckResult();
			this.resultdt = re1;
		}
		String sjwcode = cube1.FindWdbySort(3).getCode();
		List<CubeQueryData> qdatas = cube1.queryCubeData(querywheres);
		for (int i = 0; i < qdatas.size(); i++) {
			CubeQueryData qd = qdatas.get(i);
			if (qd.getData().isHasdata()) {
				checkDataList tmpqd1 = dls.get(getcode(qd.getWds(), "", ""));
				double qddata1 = qd.getData().getData();
				double tmpqddata1 = tmpqd1.getData().getData().getData();

				boolean isright = true;
				// 单位不相同
				if (qd.getData().isIfuniterr()) {
					qddata1 = 0;
					isright = false;
				}
				if (isright) {
					if (Math.abs(qddata1 - tmpqddata1) > acmr.cubeinput.Constants.DOUBLEPRECISION) {
						isright = false;
					}
				}
				if (!isright) {
					re1.additem(addDataCFBL(qd, tmpqd1.getData(), sjwcode, qd.getData().getData() - tmpqd1.getData().getData().getData(), tmpqd1.getCelladdr(), 0, 0));
				}
			}
		}
	}

	/**
	 * 得到同期的时间代码
	 * 
	 * @param sj
	 * @param tsort
	 *            需要比较的类型 同比tb 环比hb
	 * 
	 * @return 同期的时间代码
	 */
	public String getAddSj(String sj, String tsort, boolean ifadd) {
		String sort1 = CubeSjUtil.getCodeSort(sj);
		if (sort1.equalsIgnoreCase("")) {
			return "";
		}
		Date d1 = CubeSjUtil.getDate_code_min(sj);
		Calendar ca1 = Calendar.getInstance();
		ca1.setTime(d1);
		int time1 = 1;
		int amount = 0;
		if (tsort.equalsIgnoreCase("tb")) {
			time1 = Calendar.YEAR;
			amount = 1;
		}
		if (tsort.equalsIgnoreCase("hb")) {
			if (sort1.equals("q")) {
				time1 = Calendar.MONTH;
				amount = 3;
			}
			if (sort1.equals("m")) {
				time1 = Calendar.MONTH;
				amount = 1;
			}
		}
		if (!ifadd) {
			amount = -1 * amount;
		}
		ca1.add(time1, amount);
		String sj1 = CubeSjUtil.getCode_date(ca1.getTime(), sort1);
		ca1 = null;
		if (sj.equals(sj1)) {
			sj1 = "";
		}
		return sj1;
	}

	/**
	 * 第三步，进行摆动数据检查，不能与时间类型检查一起使用
	 * 
	 * @param tsort
	 *            摆动类型
	 * @param blmin
	 *            摆动的最小值
	 * @param blmax
	 *            摆动的最大值
	 * @throws CubeException
	 */
	public void step3_checkBL(String tsort, boolean ifadd, double blmin, double blmax) throws CubeException {
		CheckResult re1 = this.resultdt;
		if (re1 == null) {
			re1 = new CheckResult();
			this.resultdt = re1;
		}
		String sjwcode = cube1.FindWdbySort(3).getCode();
		List<CubeQueryWhere> where1 = new ArrayList<CubeQueryWhere>();
		for (int i = 0; i < querywheres.size(); i++) {
			CubeQueryWhere w1 = querywheres.get(i).clone();
			String sj1 = w1.getWds().get(sjwcode).getCode();
			sj1 = this.getAddSj(sj1, tsort, ifadd);
			if (!sj1.equals("")) {
				w1.getWds().get(sjwcode).setCode(sj1);
				where1.add(w1);
			}
		}
		List<CubeQueryData> qdatas = cube1.queryCubeData(where1);
		for (int i = 0; i < qdatas.size(); i++) {
			CubeQueryData qd = qdatas.get(i);
			String code1 = this.getcode(qd.getWds(), "", "");

			String code = getcode(qd.getWds(), sjwcode, this.getAddSj(qd.getWds().get(sjwcode).getCode(), tsort, !ifadd));
			checkDataList tmpqd1 = dls.get(code);

			if (dls.containsKey(code1)) {
				qd = dls.get(code1).getData().clone();
				qd.getData().changetoUnit(tmpqd1.getData().getData().getUnitcode()); // 单位换算
			}
			if (qd.getData().isHasdata()) {
				double data1 = tmpqd1.getData().getData().getData();
				double dkdata1 = qd.getData().getData();

				boolean isright = true;

				// 单位不相同
				if (qd.getData().isIfuniterr()) {
					dkdata1 = 0;
					isright = false;
				}
				double bl1 = 0;
				if (isright) {
					if (ifadd) {
						bl1 = (dkdata1 - data1) / Math.abs(data1);
					} else {
						bl1 = (data1 - dkdata1) / Math.abs(dkdata1);
					}
					if (bl1 < blmin || bl1 > blmax) {
						isright = false;
					}
				}

				if (!isright) {
					re1.additem(addDataCFBL(qd, tmpqd1.getData(), sjwcode, bl1 * 100, tmpqd1.getCelladdr(), blmin, blmax));
				}
			}
		}
	}

	/**
	 * 第三步：进行时间类型检查，可重复调用，不能和重复检查，摆动检查一起用
	 * 
	 * @param tt1
	 *            时间类型，临时表中取数
	 * @param tt2
	 *            时间类型，临时表，底库取数
	 * @param ifup
	 *            true,tt1大，false，tt1小
	 * @throws CubeException
	 */
	public void step3_CheckTT(String tt1, String tt2, boolean ifup) throws CubeException {
		CheckResult re1 = this.resultdt;
		if (re1 == null) {
			re1 = new CheckResult();
			this.resultdt = re1;
		}
		String ttwcode = cube1.FindWdbySort(6).getCode();
		List<CubeQueryWhere> where1 = new ArrayList<CubeQueryWhere>();
		for (int i = 0; i < querywheres.size(); i++) {
			CubeQueryWhere w1 = querywheres.get(i).clone();
			String timetype1 = w1.getWds().get(ttwcode).getCode();
			if (timetype1.equalsIgnoreCase(tt1)) {
				w1.getWds().get(ttwcode).setCode(tt2);
				where1.add(w1);
			}
		}
		List<CubeQueryData> qdatas = cube1.queryCubeData(where1);
		for (int i = 0; i < qdatas.size(); i++) {
			CubeQueryData qd = qdatas.get(i);
			String code1 = this.getcode(qd.getWds(), "", "");
			checkDataList tmpqd1 = dls.get(getcode(qd.getWds(), ttwcode, tt1));

			if (dls.containsKey(code1)) {
				qd = dls.get(code1).getData().clone();
				qd.getData().changetoUnit(tmpqd1.getData().getData().getUnitcode());// 单位换算
			}
			if (qd.getData().isHasdata()) {
				double data1 = tmpqd1.getData().getData().getData();
				double dkdata1 = qd.getData().getData();

				boolean isright = true;
				// 单位问题
				if (qd.getData().isIfuniterr()) {
					dkdata1 = 0;
					isright = false;
				}
				double bl1 = 0;
				if (isright) {
					if (ifup) {
						bl1 = data1 - dkdata1;
					} else {
						bl1 = dkdata1 - data1;
					}
					if(bl1<0){
						isright=false;
					}
				}
				if (!isright) {
					re1.additem(addDataTT(qd, tmpqd1.getData(), ttwcode, bl1, tmpqd1.getCelladdr()));
				}
			}
		}

	}

	/**
	 * 把检查有问题的数据插入到结果表格中，针对重复检查，摆动检查
	 * 
	 * @param sort1
	 *            问题类型
	 * @param re1
	 *            插入的表
	 * @param qd1
	 *            底库查询到的数据
	 * @param tmpqd1
	 *            临时表中对应的数据
	 * @param sjwcode
	 *            时间维度代码
	 * @param result
	 *            计算的结果
	 * @param celladdr
	 */
	private CheckItem addDataCFBL(CubeQueryData qd1, CubeQueryData tmpqd1, String sjwcode, double result, String celladdr, double blmin, double blmax) {
		CheckItem it1 = new CheckItem();
		for (int i = 0; i < tmpqd1.getWds().size(); i++) {
			CubeWdCode wd1 = tmpqd1.getWds().get(i);
			PubInfo.printStr(wd1.getWdcode() + ":" + wd1.getCode());
			it1.addwd(wd1.getWdcode(), new CheckWdCodeName(wd1.getWdcode(), wd1.getCode(), cube1.getWdNode(wd1.getWdcode(), wd1.getCode()).getName()));
		}
		it1.setResult("" + result);

		it1.setData(tmpqd1.getData().getData());
		it1.setUnitcode(tmpqd1.getData().getUnitcode());
		it1.setUnitname(getUnitName(it1.getUnitcode()));

		it1.setDbdata(qd1.getData().getData());
		it1.setDbunitcode(qd1.getData().getUnitcode());
		it1.setDbunitname(getUnitName(it1.getDbunitcode()));

		it1.setC1(qd1.getWds().get(sjwcode).getCode());
		it1.setC2(cube1.getWdNode(sjwcode, it1.getC1()).getName());
		it1.setC3("" + blmin);
		it1.setC4("" + blmax);
		it1.setC5("" + qd1.getData().isIfuniterr());
		it1.setCelladdr(celladdr);
		return it1;
	}

	/**
	 * 把检查有问题的数据插入到结果表格中，针对时间类型检查
	 * 
	 * @param sort1
	 *            问题类型
	 * @param dt1
	 *            插入的表格
	 * @param qd1
	 *            底库查询到的结果
	 * @param tmpqd1
	 *            临时表对应的结果
	 * @param ttwcode
	 *            时间类型维度代码
	 * @param result
	 *            计算的结果
	 * @param celladdr
	 * @return
	 */
	private CheckItem addDataTT(CubeQueryData qd1, CubeQueryData tmpqd1, String ttwcode, double result, String celladdr) {
		CheckItem it1 = new CheckItem();
		for (int i = 0; i < tmpqd1.getWds().size(); i++) {
			CubeWdCode wd1 = tmpqd1.getWds().get(i);
			it1.addwd(wd1.getWdcode(), new CheckWdCodeName(wd1.getWdcode(), wd1.getCode(), cube1.getWdNode(wd1.getWdcode(), wd1.getCode()).getName()));
		}
		it1.setResult("" + result);

		it1.setData(tmpqd1.getData().getData());
		it1.setUnitcode(tmpqd1.getData().getUnitcode());
		it1.setUnitname(getUnitName(it1.getUnitcode()));

		it1.setDbdata(qd1.getData().getData());
		it1.setDbunitcode(qd1.getData().getUnitcode());
		it1.setDbunitname(getUnitName(it1.getDbunitcode()));

		it1.setC1(qd1.getWds().get(ttwcode).getCode());
		it1.setC2(cube1.getWdNode(ttwcode, it1.getC1()).getName());
		it1.setCelladdr(celladdr);
		it1.setC5("" + qd1.getData().isIfuniterr());
		return it1;
	}

	private String getExcellCellAddr(String code1) {
		String[] s = code1.split(":");
		int row = Integer.parseInt(s[0]);
		int col = Integer.parseInt(s[1]);
		return acmr.excel.ExcelHelper.getExcelstrBH(row, col);

	}

	private String getUnitName(String code) {
		CubeUnit uni1 = CubeUnitManager.CubeUnitManagerFactor.getInstance("cn").getUnit(code);
		String name1 = code;
		if (uni1 != null) {
			name1 = uni1.getName();
		}
		return name1;
	}

	public static void main(String[] args) {
		DataCheck dc = new DataCheck();
		PubInfo.printStr(dc.getAddSj("2015", "tb", false));
		PubInfo.printStr(dc.getAddSj("2015", "hb", false));

		PubInfo.printStr(dc.getAddSj("201509", "tb", false));
		PubInfo.printStr(dc.getAddSj("201512", "hb", false));

		PubInfo.printStr(dc.getAddSj("2015A", "tb", false));
		PubInfo.printStr(dc.getAddSj("2015A", "hb", false));

	}

	public static void main1(String[] args) {
		DataCheck dc = new DataCheck();
		DataTable dt1 = new DataTable();
		dt1.AddColumn("loc");
		dt1.AddColumn("zb");
		dt1.AddColumn("fl");
		dt1.AddColumn("tt");
		dt1.AddColumn("ds");
		dt1.AddColumn("reg");
		dt1.AddColumn("sj");
		dt1.AddColumn("unitcode");
		dt1.AddColumn("data", Types.DOUBLE);
		DataTableRow dr = dt1.NewRow();
		dr.set("loc", "3:8");
		dr.set("zb", "ZGB000442");
		dr.set("fl", "FGB01");
		dr.set("tt", "SGB0301");
		dr.set("ds", "G010001");
		dr.set("reg", "GX000000");
		dr.set("sj", "201401");
		dr.set("unitcode", "UGB002000");
		dr.set("data", 71.09);
		dt1.getRows().add(dr);

		dr = dt1.NewRow();
		dr.set("loc", "3:9");
		dr.set("zb", "ZGB000442");
		dr.set("fl", "FGB01");
		dr.set("tt", "SGB0601");
		dr.set("ds", "G010001");
		dr.set("reg", "GX000000");
		dr.set("sj", "201401");
		dr.set("unitcode", "UGB002000");
		dr.set("data", 31.09);
		dt1.getRows().add(dr);
		try {
			dc.Step1_InitData(Const.DB_CODE, dt1);

			dc.step3_checkCF();
			PubInfo.printStr(dc.getResult().toString());
			dc.step3_checkBL("tb", true, -0.1, 0.1);
			dc.step3_checkBL("tb", false, -0.1, 0.1);
			PubInfo.printStr(dc.getResult().toString());
			// PubInfo.printStr("kkk"+dc.getResult().getRows().get(0).getfloat("data"));

			dc.step3_CheckTT("SGB0601", "SGB0301", true);
			dc.step3_CheckTT("SGB0301", "SGB0601", false);
			PubInfo.printStr(dc.getResult().toString());
		} catch (CubeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dc = null;
	}

}
