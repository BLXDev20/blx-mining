package com.cc.blox.listener;

import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.cc.blox.domain.Transaction;
import com.cc.blox.service.TransactionPoolSvc; 
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionPoolListener {
	private static final Logger LOGGER = LogManager.getLogger(TransactionPoolListener.class);
	@Autowired TransactionPoolSvc transactionPoolSvc;

	@Value("${server.port}") private String port;
	
	public void receiveTxnMessage(String parseMessage) {


        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	
        	Transaction txn = objectMapper.readValue(parseMessage, Transaction.class);
			
        	String localHost = InetAddress.getLocalHost().getHostName() + ":" + port;
        	
        	if(!host.equals(localHost)) {
    			//LOGGER.info(parseMessage);
        		transactionPoolSvc.setTransaction(txn);
        		
        		
        	}

		} catch (Exception e) {
			
			e.printStackTrace();
		} 		
		 
    }
}
