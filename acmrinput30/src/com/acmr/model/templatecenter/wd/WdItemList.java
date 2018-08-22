package com.acmr.model.templatecenter.wd;

import com.acmr.helper.util.StringUtil;
import com.acmr.helper.constants.TemplateConsts.WdType;

/**
 * 维度集合
 * @author caosl
 */
public class WdItemList implements Cloneable{
	//维度集合
	private OneWdList zb;
	private OneWdList fl;
	private OneWdList tt;
	private OneWdList sj;
	private OneWdList ds;
	private OneWdList unit;
	private OneWdList reg;
	private Double data;
	public WdItemList(){
		zb = new OneWdList();
		fl = new OneWdList();
		tt = new OneWdList();
		sj = new OneWdList();
		ds = new OneWdList();
		unit = new OneWdList();
		reg = new OneWdList();
	}
	public Double getData() {
		return data;
	}
	public void setData(Double data) {
		this.data = data;
	}
	public OneWdList getZb() {
		return zb;
	}
	public void setZb(OneWdList zb) {
		this.zb = zb;
	}
	public OneWdList getFl() {
		return fl;
	}
	public void setFl(OneWdList fl) {
		this.fl = fl;
	}
	public OneWdList getTt() {
		return tt;
	}
	public void setTt(OneWdList tt) {
		this.tt = tt;
	}
	public OneWdList getSj() {
		return sj;
	}
	public void setSj(OneWdList sj) {
		this.sj = sj;
	}
	public OneWdList getDs() {
		return ds;
	}
	public void setDs(OneWdList ds) {
		this.ds = ds;
	}
	public OneWdList getUnit() {
		return unit;
	}
	public void setUnit(OneWdList unit) {
		this.unit = unit;
	}
	public OneWdList getReg() {
		return reg;
	}
	public void setReg(OneWdList reg) {
		this.reg = reg;
	}
	
	

	
	/**
	 * 重写toString方法
	 */
	public String toString() {
		StringBuffer w = new StringBuffer();
		if(zb.isExclusive()){
			w.append(zb.getExclusiveStr()+";");
		}else{
			if(zb.getWd()!=null&&zb.getWd().size()>0){
				for(WdItem wd:zb.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("ZB_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		
		if(fl.isExclusive()){
			w.append(fl.getExclusiveStr()+";");
		}else{
			if(fl.getWd()!=null&&fl.getWd().size()>0){
				for(WdItem wd:fl.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("FL_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		
		if(ds.isExclusive()){
			w.append(ds.getExclusiveStr()+";");
		}else{
			if(ds.getWd()!=null&&ds.getWd().size()>0){
				for(WdItem wd:ds.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("LY_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		
		if(sj.isExclusive()){
			w.append(sj.getExclusiveStr()+";");
		}else{
			if(sj.getWd()!=null&&sj.getWd().size()>0){
				for(WdItem wd:sj.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("SJ_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		
		if(tt.isExclusive()){
			w.append(tt.getExclusiveStr()+";");
		}else{
			if(tt.getWd()!=null&&tt.getWd().size()>0){
				for(WdItem wd:tt.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("SF_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		
		if(reg.isExclusive()){
			w.append(reg.getExclusiveStr()+";");
		}else{
			if(reg.getWd()!=null&&reg.getWd().size()>0){
				for(WdItem wd:reg.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("DQ_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		 
		if(unit.isExclusive()){
			w.append(unit.getExclusiveStr()+";");
		}else{
			if(unit.getWd()!=null&&unit.getWd().size()>0){
				for(WdItem wd:unit.getWd()){
					if(!StringUtil.isEmpty(wd.getCode())){//如果不为空，则拼接
						if(!wd.isHasExpression()){
							w.append("DW_");
						}
						w.append(wd.getCode());
						w.append(";");
					}
				}
			}
		}
		
		return w.toString();
	}
	
	/**
	 * 获取一个维度
	 * @param type
	 * @return
	 */
	public OneWdList getOneWd(WdType type){
		if(type!=null){
			switch(type.toString()){
			case "zb": return zb;
			case "fl": return fl;
			case "sj": return sj;
			case "tt": return tt;
			case "ds": return ds;
			case "unit": return unit;
			case "dq": return reg;
			}
		}
		return null;
	}
	
	/**
	 * set
	 * @param type
	 * @param oneWd
	 */
	public void setOneWd(WdType type,OneWdList oneWd){
		if(type!=null){
			switch(type.toString().toUpperCase()){
			case "I":
			case "ZB": this.zb=oneWd;break;
			case "S":
			case "FL": this.fl=oneWd;break;	
			case "SJ": this.sj=oneWd;break;
			case "TT": this.tt=oneWd;break;
			case "DS": this.ds=oneWd;break;
			case "DW":
			case "UNIT": this.unit=oneWd;break;
			case "R":
			case "REG":
			case "DQ": this.reg=oneWd;break;
			}
		}
	}
}
