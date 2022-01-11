package com.cc.blox.springconfig;

import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cc.blox.listener.BlockChainListener;
import com.cc.blox.listener.TransactionPoolListener;
import com.cc.blox.service.TransactionSvc;

@Configuration
public class RedissonConfig {
	public final static String CHANNEL_TEST = "TEST";
	public final static String CHANNEL_BLOCKCHAIN = "BLOCKCHAIN";
	public final static String CHANNEL_TRANSACTION = "TRANSACTION";
	public final static String CHANNEL_MINE = "MINE";
	
	@Autowired private BlockChainListener blockChainListener;
	@Autowired private TransactionPoolListener transactionPoolListener;
	@Autowired private TransactionSvc transactionSvc;
	
	@Bean("redisson")
	public RedissonClient connect() {
		Config config = new Config(); 
		config.useSingleServer().setAddress("redis://52.74.239.49:6379");
		return Redisson.create(config);
	}
	
	@Bean
	public MessageListener<String> listener( @Qualifier("redisson") RedissonClient redisson) {
		RTopic topicBlockchain = redisson.getTopic(CHANNEL_BLOCKCHAIN);
		topicBlockchain.addListener(String.class, new MessageListener<String>() {
		    @Override
		    public void onMessage(CharSequence charSequence, String message) {

		    	blockChainListener.receiveBlockChainMessage(message);
		    }
		});
		
		RTopic topicTxn = redisson.getTopic(CHANNEL_TRANSACTION);
		topicTxn.addListener(String.class, new MessageListener<String>() {
		    @Override
		    public void onMessage(CharSequence charSequence, String message) {

		    	transactionPoolListener.receiveTxnMessage(message);
		    }
		});
		
		RTopic topicMining= redisson.getTopic(CHANNEL_MINE);
		topicMining.addListener(String.class, new MessageListener<String>() {
		    @Override
		    public void onMessage(CharSequence charSequence, String message) {
		    	
		    	transactionSvc.mineBlocks(message);
		    }
		});
		
		return null;
	
	}
}
