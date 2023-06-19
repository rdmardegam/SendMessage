package com.example.sendmessage.controller;

import java.util.Arrays;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sendmessage.model.Message;
import com.example.sendmessage.service.impl.MessageService;

import javax.annotation.PostConstruct;

@RestController
public class TesteController {

	@Autowired
	MessageService messageService;
	
	@GetMapping(value = "teste")
	public ResponseEntity<java.lang.String> teste (@RequestParam(required = false,defaultValue = "1") Integer id) throws Exception {

		Message message = new Message();
		message.setCodMessage("OTP");
		message.setOtpCode("123456");
		message.setFrom("Teste");
		message.setWalletName("samsungPay");
		message.setPhone("+55119873216547");
		message.setSubject("123456");
		message.setTo(Arrays.asList("jose@teste.com"));

		try{
			messageService.sendMessage(message,id);
			System.out.println("OK");
			return new ResponseEntity<String>("OK", org.springframework.http.HttpStatus.OK);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}