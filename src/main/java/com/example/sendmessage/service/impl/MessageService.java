package com.example.sendmessage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sendmessage.enums.TypeTemplateEnum;
import com.example.sendmessage.exception.BusinessException;
import com.example.sendmessage.exception.MessageSendingException;
import com.example.sendmessage.exception.TechnicalException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.example.sendmessage.model.Message;
import com.example.sendmessage.template.IMessageTemplate;
import com.example.sendmessage.template.MessageTemplateFactory;

import javax.annotation.PostConstruct;

@Service
public class MessageService {

	@CircuitBreaker(name = "sendsms")
	public void sendMessage(Message message, Integer ... id) throws Exception {
		// syncronized hashmap to store exceptions
		Map<TypeTemplateEnum, Exception> mapTemplateException = new HashMap<>();

		/*if(1==1) {
			throw new TechnicalException("Erro tecnico");
		}*/

		// Seleciona os templates possiveis
		List<IMessageTemplate> listMessagemTemplate = MessageTemplateFactory.getServiceMessageTemplateByMessage(message);

		try {
			// try call in parallel all templates selected for message
			for(IMessageTemplate template : listMessagemTemplate) {
				template.valid(message);
				template.send(message);
			}

		} catch (Exception e) {
			//Thows exeption cause content in e.getcause()
			Exception ex = (Exception) e.getCause();
			throw ex;
		}

	}

}



//package com.example.sendmessage.service.impl;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.example.sendmessage.enums.TypeTemplateEnum;
//import com.example.sendmessage.exception.BusinessException;
//import com.example.sendmessage.exception.MessageSendingException;
//import com.example.sendmessage.exception.TechnicalException;
//import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.github.resilience4j.core.registry.EntryAddedEvent;
//import io.github.resilience4j.core.registry.EntryRemovedEvent;
//import io.github.resilience4j.core.registry.EntryReplacedEvent;
//import io.github.resilience4j.core.registry.RegistryEventConsumer;
//import io.github.resilience4j.retry.RetryRegistry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//
//import com.example.sendmessage.model.Message;
//import com.example.sendmessage.template.IMessageTemplate;
//import com.example.sendmessage.template.MessageTemplateFactory;
//
//import javax.annotation.PostConstruct;
//
//@Service
//public class MessageService {
//
//	@CircuitBreaker(name = "sendsms")
//	public void sendMessage(Message message, Integer ... id) throws Exception {
//
//		Thread.sleep(1000);
//
//		// gerar um valor random de 0 ou 1
//		//int random = (int) (Math.random() * 3);
////		int random =  id != null ? id[0] : (int) (Math.random() * 3);
////		if(random == 0) {
////			throw new BusinessException("Erro Negocio");
////			//throw new TechnicalException("Erro Tecnico");
////		} else if(random == 1) {
////			return;
////		} else if(random == 2) {
////			throw new TechnicalException("Erro Tecnico");
////		}
//
//
//		// syncronized hashmap to store exceptions
//		Map<TypeTemplateEnum, Exception> mapTemplateException = new HashMap<>();
//
//		// Seleciona os templates possiveis
//		List<IMessageTemplate> listMessagemTemplate = MessageTemplateFactory.getServiceMessageTemplateByMessage(message);
//
//		// try call in parallel all templates selected for message
//		listMessagemTemplate.forEach(template -> {
//			try {
//				template.valid(message);
//				template.send(message);
//			} catch (Exception e) {
//				e.printStackTrace();
//				mapTemplateException.put(template.getTypeTemplate(), e);
//			}
//		});
//
//		if(!mapTemplateException.isEmpty()) {
//			//throw new MessageSendingException(mapTemplateException);
//			throw new Exception(new MessageSendingException(mapTemplateException));
//		}
//	}
//
//
//
//
//
//
//
//}