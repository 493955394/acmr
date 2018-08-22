package com.acmr.model.templatecenter;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Dimension")  
public class ApplyDimensionList {
	  
	private List<ApplyDimension> dimensionList;
	@XmlElementWrapper(name = "DimensionList")
	@XmlElement(name = "DimensionInfo")
	public List<ApplyDimension> getDimensionList() {
		return dimensionList;
	}

	public void setDimensionList(List<ApplyDimension> dimensionList) {
		this.dimensionList = dimensionList;
	}
}
