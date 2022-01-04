package com.cc.blox.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.cc.blox.domain.Block;
import com.cc.blox.service.BlockSvc;
import com.cc.blox.utils.CryptoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
 

@Service
public class BlockSvcImpl implements BlockSvc {
	private static final Logger LOGGER = LogManager.getLogger(BlockSvcImpl.class);
	public final static String CHANNEL_BLOCK = "BLOCK";
	public final static String ZERO = "0"; 
	public final static int difficulty = 24;
	@Autowired private SimpMessagingTemplate template;
	long startTime = System.currentTimeMillis();
	@Autowired private ApplicationContext ctx;  
	@Value("${wallet.address}") private String walletAddress;
	
	boolean miningStatus = true;
	
	@Override
	public void setMining(boolean status) {
		this.miningStatus = status;
		
	}
	
	@Override
	public Block mineBlock(Block lastBlock, Map<String, Object> data) {
		Block block = null;
		long timeStamp = 0;
		final String lastHash = lastBlock.hash;
		long lastHeight = lastBlock.height;
		int lastDifficulty = difficulty;
		String repeatZero = String.valueOf(lastDifficulty);
		long nonce = 0;
		String hash = "";

		long newHeight = lastHeight + 1;
		
		
		try {
			long miningStartTime = System.currentTimeMillis(); 
			do {
				nonce++;
				timeStamp = System.currentTimeMillis(); 
				
				hash = CryptoUtils.toSha256(lastHash + timeStamp + nonce + lastDifficulty + newHeight);
				repeatZero = new String(new char[lastDifficulty]).replace("\u0000" , ZERO);
				
				template.convertAndSend("/topic/mine", hash + "|" + (timeStamp - startTime));
				
				
			} while( !(CryptoUtils.hexToBinary(hash)).substring(0, lastDifficulty).equals(repeatZero) && miningStatus);
			
			long miningEndTime = System.currentTimeMillis(); 
			
			long elapseTime = miningEndTime - miningStartTime;
			
			if(!miningStatus) {
				
				return  null;
			}
			
			if(elapseTime > 0 && nonce > 0 && miningStatus) {
				double elapseTimeInSec = (Double.valueOf(elapseTime)/1000);
				double hashRate =  Double.valueOf(nonce)/elapseTimeInSec;
				
				LOGGER.info("New job received: Block " + hash);
				LOGGER.info("Speed at " + hashRate + " hash/s"); 
			}			
			
			
			block = new Block(data, hash, lastHash, timeStamp, nonce, lastDifficulty, newHeight);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return block;
	}



	

}
