package com.example.sendmessage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Sms {
	
	private String phone;
	
	private String otpCode;
	
	private String walletName;
	
	private String message;
	
	
}
