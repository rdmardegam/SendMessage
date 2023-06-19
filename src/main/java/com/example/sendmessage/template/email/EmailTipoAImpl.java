package com.example.sendmessage.template.email;

import java.util.ArrayList;
import java.util.List;

import com.example.sendmessage.enums.TypeTemplateEnum;
import org.springframework.stereotype.Service;

import com.example.sendmessage.enums.TypeMessageEnum;
import com.example.sendmessage.exception.ValidationException;
import com.example.sendmessage.model.Email;
import com.example.sendmessage.model.Message;
import com.example.sendmessage.template.IMessageTemplate;
import com.example.sendmessage.util.StringUtil;

@Service
public class EmailTipoAImpl extends EmailTemplateBase implements IMessageTemplate {

	private final String templateDefault = "Olá ${to} seu codigo de verificacao é ${otpCode}";

	public void valid(Message message) throws  ValidationException{
		super.valid(message);
		
		List<String> errors = new ArrayList<>();
		
		if(message.getOtpCode() == null) {
			errors.add("O campo 'otpCode' deve ser informado para o layout default de email");
		}
		
		if(message.getTo() == null) {
			errors.add("O campo 'to' deve ser informado para o layout default de email");
		}

	    if (!errors.isEmpty()) {
	        throw new ValidationException(errors, this.getTypeMessage());
	    }
	}
	
	@Override
	public void send(Message message) {
		Email email = this.buildEmail(message);
		
		// Enviando
		System.out.println(email);
	}
		
	public Email buildEmail(Message message) {
		String contentEmail = StringUtil.replaceSimpleValues(templateDefault, message);
		
		Email email = Email.builder()
						    .from(message.getFrom())
						    .to(message.getTo())
						    .subject("TEMPLATE GENERICO")
						    .body(contentEmail).build();
		
		return email;
	}

	@Override
	public boolean isApplicable(Message message) {
		return "OTP".equalsIgnoreCase(message.getCodMessage());
	}

	@Override
	public TypeMessageEnum getTypeMessage() {
		return TypeMessageEnum.EMAIL;
	}

	@Override
	public TypeTemplateEnum getTypeTemplate() {
		return TypeTemplateEnum.EMAIL_RECOVERY_PASSWORD;
	}
}