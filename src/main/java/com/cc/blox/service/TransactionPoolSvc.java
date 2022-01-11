package com.cc.blox.service;
 
import java.util.Map;

public interface TransactionPoolSvc {

	public void  setTransaction(Map<String, Object> txn);
	
	public Map<String, Object> getTransactionPoolMap();

	public void clearTransactionMap();

	public void syncTransaction();

}
