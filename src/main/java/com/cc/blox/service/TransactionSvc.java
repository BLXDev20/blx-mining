package com.cc.blox.service;

import java.util.Map;

import com.cc.blox.domain.Transaction;

public interface TransactionSvc {
	public Transaction processTransaction(Map<String, Object> payload);
}
