package com.cc.blox.service.impl;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext; 
import org.springframework.stereotype.Service;

import com.cc.blox.service.BlockChainSvc;
import com.cc.blox.service.SvcException;
import com.cc.blox.service.TransactionPoolSvc;
import com.cc.blox.service.TransactionSvc;
import com.cc.blox.utils.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionSvcImpl implements TransactionSvc {
	@Autowired private ApplicationContext ctx;  
	@Autowired TransactionPoolSvc transactionPoolSvc;
	@Autowired BlockChainSvc blockChainSvc;
	private final static double BLXREWARD = 200;
	
	Map<String, Object> transaction = new HashMap<String, Object>();
	
	public final static String CHANNEL_TRANSACTION = "TRANSACTION";
	@Value("${server.port}") private String port;
	@Value("${wallet.address}") private String walletAddress;
	private final static String SYMBOL = "BLX";
	
	boolean isReady = false;
	
	@Override
	public void setReady(boolean ready) {
		this.isReady =  ready; 
	}
	
	@Override
	public void mineBlocks(String args) {
		//System.out.println(args);
		if(!this.isReady ) {
    		
    		return;
    	}
		
		
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
	public Map<String, Object> processTransaction(Map<String, Object> payload) {
		String address = (String)payload.get("address"); 
		String amount = (String)payload.get("amount");  
		String symbol = (String)payload.get("symbol");
		
		if(Double.parseDouble(amount) <= 0) {
			throw new SvcException("Invalid amount", "FLD.REQ");
		}
		
		Map<String, Object> txn = createTransaction(address, Double.valueOf(amount));
		if(symbol == null) {
			symbol = SYMBOL;
		}
		txn.put("symbol",symbol);
		transactionPoolSvc.setTransaction(txn);
		
		broadcastTransaction(txn);
		return txn;
	}
	
	private void broadcastTransaction(Map<String, Object> txn) {
		
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			
			String txnStr = objectMapper.writeValueAsString(txn); 
			String msg = InetAddress.getLocalHost().getHostName() + ":" + port + "|" + txnStr;
			

			RedissonClient redisson = (RedissonClient) ctx.getBean("redisson"); 
			RTopic topic = redisson.getTopic(CHANNEL_TRANSACTION);
			topic.publish(msg);
			 
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	private Map<String, Object> createTransaction(String address, double amount) {
		
		Map<String, Object> txn = new HashMap<String, Object>();
		txn.put("senderWallet",null);
		txn.put("hash",CryptoUtils.toSha256(UUID.randomUUID().toString()));
		
		txn.put("recipient",address);
		txn.put("amount",amount);
		
		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put(address, amount); 
		txn.put("outputMap",outputMap);
		
		txn.put("input",createInput(outputMap, address));
		
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
