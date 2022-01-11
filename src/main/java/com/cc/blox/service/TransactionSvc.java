package com.cc.blox.service;

import java.util.Map;

public interface TransactionSvc {
	public Map<String, Object> processTransaction(Map<String, Object> payload);

	public void mineBlocks(String args);

	public void setReady(boolean ready);
}
