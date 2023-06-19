package com.example.sendmessage.template.email;

import com.example.sendmessage.enums.TypeTemplateEnum;
import org.springframework.stereotype.Service;

import com.example.sendmessage.enums.TypeMessageEnum;
import com.example.sendmessage.model.Email;
import com.example.sendmessage.model.Message;
import com.example.sendmessage.template.IMessageTemplate;

@Service
public class EmailTipoBImpl extends EmailTemplateBase implements IMessageTemplate {

	private final String templateDefault = "<b>Obrigado pelo seu cadastrado na ${nameMessage}</b>";
		
	@Override
	public void send(Message message) {
		Email email = this.buildEmail(message);
		
		// Enviando
		System.out.println(email);
	}
		
	public Email buildEmail(Message message) {
		String contentEmail = templateDefault.replaceAll("{nameMessage}", message.getNameMessage());
		
		Email email = Email.builder()
						    .from(message.getFrom())
						    .to(message.getTo())
						    .subject("TEMPLATE GENERICO")
						    .body(contentEmail).build();
		
		return email;
	}
	
	@Override
	public boolean isApplicable(Message message) {
		return !"OTP".equalsIgnoreCase(message.getCodMessage());
	}

	@Override
	public TypeMessageEnum getTypeMessage() {
		return TypeMessageEnum.EMAIL;
	}

	@Override
	public TypeTemplateEnum getTypeTemplate() {
		return TypeTemplateEnum.Email_Welcome;
	}

}