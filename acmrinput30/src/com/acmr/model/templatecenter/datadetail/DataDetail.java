package com.acmr.model.templatecenter.datadetail;

import java.util.List;

public class DataDetail {
	private Dimension dimension;
	private List<VersionRecord> logs;

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public List<VersionRecord> getLogs() {
		return logs;
	}

	public void setLogs(List<VersionRecord> logs) {
		this.logs = logs;
	}

}
