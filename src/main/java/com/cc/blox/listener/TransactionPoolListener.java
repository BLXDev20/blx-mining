package com.cc.blox.listener;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cc.blox.service.TransactionPoolSvc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionPoolListener {
	@Autowired TransactionPoolSvc transactionPoolSvc;

	@Value("${server.port}") private String port;
	boolean isReady = false;
	
	public void setReady(boolean ready) {
		this.isReady =  ready; 
	}
	
	
	public void receiveTxnMessage(String message) {
	
		String separator = "|";
		String host = message.substring(0, message.indexOf(separator));
		String parseMessage = message.substring(message.indexOf(separator) + separator.length());

        try {
        	//System.out.println(this.isReady);
        	if(!this.isReady) {
        		
        		return;
        	}
        	ObjectMapper objectMapper = new ObjectMapper();
        	
        	Map<String, Object> txn = objectMapper.readValue(parseMessage, new TypeReference<Map<String, Object>>(){});
			
        	String localHost = InetAddress.getLocalHost().getHostName() + ":" + port;
        	
        	if(!host.equals(localHost)) {
        		TimeUnit.SECONDS.sleep(15); 
        		transactionPoolSvc.setTransaction(txn);
        		
        		
        	}

		} catch (Exception e) {
			
			e.printStackTrace();
		} 		
		 
    }
}
