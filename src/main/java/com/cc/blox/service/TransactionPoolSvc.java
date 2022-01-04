package com.cc.blox.service;
 
import java.util.Map;

import com.cc.blox.domain.Transaction;

public interface TransactionPoolSvc {

	public void  setTransaction(Transaction txn);
	
	public Map<String, Object> getTransactionPoolMap();

	public void clearTransactionMap();

	public void syncTransaction();

}
