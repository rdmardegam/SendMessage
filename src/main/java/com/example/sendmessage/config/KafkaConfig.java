package com.example.sendmessage.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import org.springframework.kafka.core.ConsumerFactory;

import org.springframework.kafka.listener.ContainerProperties.AckMode;


@Configuration
public class KafkaConfig {

	/**
	 * Consegue sempre receber a msg, mesmo que o json esteja com erro
	 * */
	/*@Bean
	public RecordMessageConverter converter() {
		return new StringJsonMessageConverter();
	}*/
	
	/*@Autowired
	private ConsumerFactory consumerFactory;*/

	//@Bean("kafkaListenerContainerFactoryFilter")

	//public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactoryFilter(ConsumerFactory consumerFactory) {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryFilter(ConsumerFactory consumerFactory) {
	    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(consumerFactory);

	    final Map<?, ?> unmodifiable = consumerFactory.getConfigurationProperties();
	    Map<String, Object> props  = new HashMap(unmodifiable);

	    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 7);
	    //props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 10000);
	    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


	    consumerFactory.updateConfigs(props);


	    factory.setBatchListener(true);
	    factory.getContainerProperties().setAckMode(AckMode.MANUAL);
	    factory.setCommonErrorHandler(null);

		//

		// set asyncacks false
		factory.getContainerProperties().setSyncCommits(true);

	    return factory;
	  }
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryOneByOneFilter(ConsumerFactory consumerFactory) {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();

		final Map<?, ?> unmodifiable = consumerFactory.getConfigurationProperties();
		Map<String, Object> props  = new HashMap(unmodifiable);

		//props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1);
		//props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 10000);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		consumerFactory.updateConfigs(props);
		factory.setConsumerFactory(consumerFactory);
		//factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(AckMode.MANUAL);
		factory.setCommonErrorHandler(null);

		// asyncacks false
		factory.getContainerProperties().setSyncCommits(true);


		return factory;
	}

}
