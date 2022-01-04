package com.cc.blox.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cc.blox.domain.Block;
import com.cc.blox.domain.Transaction;
import com.cc.blox.service.BlockChainSvc;
import com.cc.blox.service.BlockSvc;
import com.cc.blox.service.TransactionPoolSvc;
import com.cc.blox.service.TransactionSvc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
 


@Service
public class TransactionPoolSvcImpl implements TransactionPoolSvc {
	private static final Logger LOGGER = LogManager.getLogger(TransactionPoolSvcImpl.class);
	public final static String CHANNEL_TRANSACTION = "TRANSACTION";
	public Map<String, Object> transactionMap = new HashMap<String, Object>();
	
	
	@Value("${node.root.url}") private String nodeRootUrl;
	@Value("${server.port}") private String port;
	@Autowired private HttpClient httpClient;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void setTransaction(Transaction txn) {
		boolean isRewardTxn = false;
		if(txn.getSenderWallet() == null) {
			isRewardTxn = true;
		}
		
		if(isRewardTxn) {
			int count = 0;
			for (Map.Entry<String, Object> entry : this.transactionMap.entrySet()) {
				
				if(entry.getValue() instanceof Transaction) {
					Transaction localTxn = (Transaction) entry.getValue();
					
					if(localTxn.getSenderWallet() == null) {
						count++;
					} 
				} else {
					Map<String, Object> localTxn = (Map<String, Object>) entry.getValue();
					
					if(localTxn.get("senderWallet") == null) {
						count++;
					}
				}
				
			}
			
			if(count > 0) {
				return;
			}
		}
		
		this.transactionMap.put(txn.getHash(),txn);
	}
	
	@Override
	public void clearTransactionMap() {
		transactionMap = new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> getTransactionPoolMap() {
		return this.transactionMap;
	}

	
	@Override
	public void syncTransaction() {
		
		GetMethod getMethod = new GetMethod(nodeRootUrl + "/api/transaction/pool");
		
		try {
			httpClient.executeMethod(getMethod);
			
			InputStream responseBody = getMethod.getResponseBodyAsStream();
			InputStreamReader isReader = new InputStreamReader(responseBody);
			BufferedReader reader = new BufferedReader(isReader);
			StringBuffer sb = new StringBuffer();	
			String str;
			while((str = reader.readLine())!= null){
				sb.append(str);
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> transactionMap = objectMapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
			
        	
        	
        	this.transactionMap = transactionMap;
        	
        	LOGGER.info("Synchronization: Transactions");
        	
        	
        	
        	
		} catch (IOException e1) { 
			e1.printStackTrace();
		}
	}
}
