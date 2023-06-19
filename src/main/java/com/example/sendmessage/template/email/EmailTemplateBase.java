package com.example.sendmessage.template.email;

import java.util.ArrayList;
import java.util.List;

import com.example.sendmessage.exception.ValidationException;
import com.example.sendmessage.model.Message;

public abstract class EmailTemplateBase {
	
	// Validações basicas para email template
	public void valid(Message message) throws ValidationException {
	 	List<String> errors = new ArrayList<>();

	    if (message.getTo() == null) {
	        errors.add("O campo 'to' deve ser informado para o layout default de email");
	    }

	    if (message.getFrom() == null) {
	        errors.add("O campo 'from' deve ser informado para o layout default de email");
	    }

	    if (message.getSubject() == null) {
	        errors.add("O campo 'subject' deve ser informado para o layout default de email");
	    }

	    if (!errors.isEmpty()) {
	        throw new ValidationException(errors);
	    }
	}
}