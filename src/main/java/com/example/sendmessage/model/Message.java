package com.example.sendmessage.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {
	
	private String idMessage;
	
	private String codMessage;
	
    private String nameMessage;
	
    private String subject;
	 
	private String from;
	
	private List<String> to;
	
	private String content;
	
	private String phone;
	
	private String otpCode;
	
	private Map<String,String> anotherValuesMap;
	
	private LocalDateTime dateSend;
		
	private String template;
	
	private Object yourModel;
	
	private String walletName;
}