package com.example.sendmessage.enums;

public enum TypeMessageEnum {

	SMS("SMS"),
	EMAIL("EMAIL");
	
//	EMAIL("EMAIL"),
//	SMS_AND_EMAIL("SMS_EMAIL")

	
	
	private String typeMessage;
	
	/** Contrutor Padrao */
	TypeMessageEnum(String typeMessage) {
		this.typeMessage = typeMessage;
	}

	/** GET */
	public String getTypeMessage() {
		return typeMessage;
	}
	
	
}