package com.acmr.rmi.service;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.acmr.helper.util.PropertiesReaderUtil;

public enum RmiServiceFactory {
	RIMSOURCE;
	private RmiService rmiService;

	public RmiService getRmiService() {
		String rmiAddress = PropertiesReaderUtil.get("rmi.address");
		try {
			Registry registry = LocateRegistry.getRegistry(rmiAddress, 11999);
			rmiService = (RmiService) registry.lookup("RmiService");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rmiService;
	}
}
