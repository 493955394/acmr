package com.acmr.dao.datareport;

import java.util.List;

import com.acmr.model.datareport.Datareport;

public interface IDatareportDao {

	public List<Datareport> find();

	public int insert(Datareport report);

	public int update(Datareport report);

	public int delete(String id);

	public Datareport getById(String id);
}
