package com.cc.blox.listener;

import java.net.InetAddress;
import java.util.ArrayList; 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;

import com.cc.blox.domain.Block;
import com.cc.blox.service.BlockChainSvc;
import com.cc.blox.service.BlockSvc;
import com.cc.blox.service.TransactionSvc;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper; 

@Service
public class BlockChainListener {
	private static final Logger LOGGER = LogManager.getLogger(BlockChainListener.class);
	
	@Autowired private BlockChainSvc blockChainSvc; 
	
	@Value("${server.port}") private String port;
	
	public void receiveBlockChainMessage(String parseMessage) {

		
        try {
        	ObjectMapper objectMapper = new ObjectMapper();
        	
        	String localHost = InetAddress.getLocalHost().getHostName() + ":" + port;
        	if(!host.equals(localHost)) {
        		
        		
        		ArrayList<Block> localBlockChain =  blockChainSvc.getBlocks();
            	Block block = objectMapper.readValue(parseMessage, new TypeReference<Block>(){});
            	
            	if( (localBlockChain.get(localBlockChain.size() - 1).height + 1) != block.height ) {
            	
            		blockChainSvc.syncChains();
            	}
            	
            	
            	
            	localBlockChain.add(block);
            	
            	
        		
        		blockChainSvc.replaceChain(localBlockChain);
    			
    			
   
    			
        	}
		} catch (Exception e) {
			
			e.printStackTrace();
		} 

    }
}
