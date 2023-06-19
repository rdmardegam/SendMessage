package com.example.sendmessage.template.sms;

import com.example.sendmessage.exception.ValidationException;
import com.example.sendmessage.model.Message;

public abstract class SmsTemplateBase {
	
	// Validações basicas para email template
	public void valid(Message message) throws ValidationException {
		if(message.getPhone() == null) {
			throw new ValidationException("O campo phone deve ser informado para o layout default de sms");
		}
	}
}