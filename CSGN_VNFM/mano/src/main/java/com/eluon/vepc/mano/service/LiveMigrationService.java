package com.eluon.vepc.mano.service;

public interface LiveMigrationService {
	
	public String getInstanceList(String zoneName);
	
	public boolean setMigration(String serverId, String hipervisorId, boolean option);
}
