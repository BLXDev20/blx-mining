package com.cc.blox.service.impl;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.cc.blox.domain.Transaction;
import com.cc.blox.service.BlockChainSvc;
import com.cc.blox.service.SvcException;
import com.cc.blox.service.TransactionPoolSvc;
import com.cc.blox.service.TransactionSvc;
import com.cc.blox.utils.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionSvcImpl implements TransactionSvc {
	private static final Logger LOGGER = LogManager.getLogger(TransactionSvc.class);
	@Autowired private ApplicationContext ctx;  
	@Autowired TransactionPoolSvc transactionPoolSvc;
	@Autowired BlockChainSvc blockChainSvc;
	private final static double BLXREWARD = 200;
	
	Transaction transaction = new Transaction();
	
	public final static String CHANNEL_TRANSACTION = "TRANSACTION";
	@Value("${server.port}") private String port;
	@Value("${wallet.address}") private String walletAddress;
	

	public void mineBlocks(String args) {
		LOGGER.info(args);
		if(args.equals("MINE")) {
			Map<String, Object> transactionMap = transactionPoolSvc.getTransactionPoolMap();
			
			
			boolean isSuccess = blockChainSvc.addBlock(transactionMap);
			
			if(isSuccess) {
				transactionPoolSvc.clearTransactionMap(); 
				
				
				Map<String, Object> rewardData = new HashMap<String, Object>();
				rewardData.put("address", walletAddress);
				rewardData.put("amount", String.valueOf(BLXREWARD)); 
				processTransaction(rewardData);
			}
			
		}
		
	}
	
	@Override
	public Transaction processTransaction(Map<String, Object> payload) {
		String address = (String)payload.get("address"); 
		String amount = (String)payload.get("amount");  
		
		if(Double.parseDouble(amount) <= 0) {
			throw new SvcException("Invalid amount", "FLD.REQ");
		}
		
		Transaction txn = createTransaction(address, Double.valueOf(amount));
		
		
		transactionPoolSvc.setTransaction(txn);
		
		broadcastTransaction(txn);
		return txn;
	}
	
	private void broadcastTransaction(Transaction txn) {
		
		try {
			
			
			StringRedisTemplate redisTemplate = ctx.getBean(StringRedisTemplate.class); 
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			
			String txnStr = objectMapper.writeValueAsString(txn); 
			
			redisTemplate.convertAndSend(CHANNEL_TRANSACTION, txnStr);
			 
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private Transaction createTransaction(String address, double amount) {
		
		Transaction txn = new Transaction();
		
		txn.setSenderWallet(null);
		txn.setHash(CryptoUtils.toSha256(UUID.randomUUID().toString()));
		
		txn.setRecipient(address);
		txn.setAmount(amount);
		
		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put(address, amount); 
		txn.setOutputMap(outputMap);
		
		txn.setInput(createInput(txn.getOutputMap(), address));
		
		return txn;
	}
	
	private Map<String, Object> createInput(Map<String, Object> outputMap, String address) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("timeStamp", System.currentTimeMillis());
		
		input.put("amount", outputMap.get(address)); 
		input.put("address", null);
		input.put("signature", null);
		
		return input;
	}
}
