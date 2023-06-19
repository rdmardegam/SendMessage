package com.example.sendmessage.template;

import com.example.sendmessage.enums.TypeTemplateEnum;
import com.example.sendmessage.enums.TypeMessageEnum;
import com.example.sendmessage.exception.BaseException;
import com.example.sendmessage.exception.TechnicalException;
import com.example.sendmessage.exception.ValidationException;
import com.example.sendmessage.model.Message;


public interface IMessageTemplate {
	
	void valid(Message message) throws ValidationException;
		
	void send(Message message) throws BaseException;
	
	boolean isApplicable(Message message);
	
	TypeMessageEnum getTypeMessage();

	TypeTemplateEnum getTypeTemplate();
	
}