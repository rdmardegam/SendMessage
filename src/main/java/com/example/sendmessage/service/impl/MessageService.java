package com.example.sendmessage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sendmessage.enums.TypeTemplateEnum;
import com.example.sendmessage.exception.MultiErrorException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import com.example.sendmessage.model.Message;
import com.example.sendmessage.template.IMessageTemplate;
import com.example.sendmessage.template.MessageTemplateFactory;

@Service
public class MessageService {

//	@CircuitBreaker(name = "sendsms")
//	public void sendMessage(Message message, Integer ... id) throws Exception {
//		// syncronized hashmap to store exceptions
//		Map<TypeTemplateEnum, Exception> mapTemplateException = new HashMap<>();
//
////		if(1==1) {
////			throw new Exception("TESTE", new BusinessException("TechnicalException"));
////		}
//
//		// Seleciona os templates possiveis
//		List<IMessageTemplate> listMessagemTemplate = MessageTemplateFactory.getServiceMessageTemplateByMessage(message);
//
//
//			// try call in parallel all templates selected for message
//			for(IMessageTemplate template : listMessagemTemplate) {
//				template.valid(message);
//				template.send(message);
////				try {
////					template.valid(message);
////					template.send(message);
////				} catch (BusinessException e) {
////					mapTemplateException.put(template.getTypeTemplate(), e);
////				} catch (TechnicalException e) {
////					mapTemplateException.put(template.getTypeTemplate(), e);
////				} catch (Exception e) {
////					mapTemplateException.put(template.getTypeTemplate(), e);
////				}
//			}
//
//		// if all templates fail, throw exception
////		if(mapTemplateException.size() == listMessagemTemplate.size()) {
////			throw new MessageSendingException(mapTemplateException);
////		}
//
//
//	}
//}

	@CircuitBreaker(name = "sendsms")
	public void sendMessage(Message message, Integer ... id) throws Exception {
		// syncronized hashmap to store exceptions
		Map<TypeTemplateEnum, Exception> multiErrorExceptionMap = new HashMap<>();


//		if(1==1) {
//			throw new Exception("TESTE", new BusinessException("TechnicalException"));
//		}

		// Seleciona os templates possiveis
		List<IMessageTemplate> listMessagemTemplate = MessageTemplateFactory.getServiceMessageTemplateByMessage(message);


		// show all templates selected
		for(IMessageTemplate template : listMessagemTemplate) {
			System.out.println("Template Selected: " + template.getTypeTemplate());
		}


		// try call in parallel all templates selected for message
		for(IMessageTemplate template : listMessagemTemplate) {
			//template.valid(message);
			//template.send(message);
				try {
					template.valid(message);
					template.send(message);
				} catch (Exception e) {
					multiErrorExceptionMap.put(template.getTypeTemplate(), e);
				}
		}

		// check if exists any exception
		this.handlePossibleException(multiErrorExceptionMap);



		// if all templates fail, throw exception
//		if(mapTemplateException.size() == listMessagemTemplate.size()) {
//			throw new MessageSendingException(mapTemplateException);
//		}


	}

	private void handlePossibleException(Map<TypeTemplateEnum, Exception> mapTemplateException) throws Exception {
		if(mapTemplateException.size() > 0) {
			throw new MultiErrorException("Erro ao executar o envio da(s) mensssagen(s)",mapTemplateException);
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