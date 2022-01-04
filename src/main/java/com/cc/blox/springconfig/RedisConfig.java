package com.cc.blox.springconfig;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.cc.blox.listener.BlockChainListener;
import com.cc.blox.listener.TransactionPoolListener;
import com.cc.blox.service.impl.TransactionSvcImpl;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;


@Configuration
public class RedisConfig {
	
	public final static String CHANNEL_TEST = "TEST";
	public final static String CHANNEL_BLOCKCHAIN = "BLOCKCHAIN";
	public final static String CHANNEL_TRANSACTION = "TRANSACTION";
	public final static String CHANNEL_MINE = "MINE";

	 
	 
	  @Bean("redisConnectionFactory")
	    public LettuceConnectionFactory redisConnectionFactory() {

	        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofMinutes(5)).build();

	        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

	        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
	                .commandTimeout(Duration.ofMinutes(5)).clientOptions(clientOptions).build();
	        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration("52.197.245.161", 6379);

	        final LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(serverConfig, clientConfig);
	        lettuceConnectionFactory.setValidateConnection(true);
	        return lettuceConnectionFactory;

	    }
	 
	@Bean("blockChainBean")
	MessageListenerAdapter blockChainListener(BlockChainListener receiver) {
		return new MessageListenerAdapter(receiver, "receiveBlockChainMessage");
	}
	
	@Bean("blockBean")
	MessageListenerAdapter blockListener(TransactionSvcImpl receiver) {
		return new MessageListenerAdapter(receiver, "mineBlocks");
	}

	
	@Bean("transactionPoolBean")
	MessageListenerAdapter txnListener(TransactionPoolListener txnReceiver) {
		return new MessageListenerAdapter(txnReceiver, "receiveTxnMessage");
	}
		
	 @Bean
	 RedisMessageListenerContainer redisMessageListenerContainer(
			 @Qualifier("redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory,
			 @Qualifier("blockChainBean") MessageListenerAdapter blockChainListener,
			 @Qualifier("transactionPoolBean") MessageListenerAdapter txnListener,
			 @Qualifier("blockBean") MessageListenerAdapter blockListener) {

		 RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		 container.setConnectionFactory(redisConnectionFactory);
		 container.addMessageListener(blockChainListener, new PatternTopic(CHANNEL_BLOCKCHAIN));
		 container.addMessageListener(txnListener, new PatternTopic(CHANNEL_TRANSACTION));
		 container.addMessageListener(blockListener, new PatternTopic(CHANNEL_MINE));

		return container;
	}
	
	
	@Bean
	public RedisTemplate<String, Object> template(@Qualifier("redisConnectionFactory") LettuceConnectionFactory redisConnectionFactory) {
		
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public ChannelTopic topic() {
		return new ChannelTopic(CHANNEL_BLOCKCHAIN);
	}

}
