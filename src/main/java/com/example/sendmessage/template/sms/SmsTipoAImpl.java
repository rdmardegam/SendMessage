package com.example.sendmessage.template.sms;

import com.example.sendmessage.enums.TypeTemplateEnum;
import com.example.sendmessage.exception.BaseException;
import com.example.sendmessage.exception.BusinessException;
import com.example.sendmessage.exception.TechnicalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sendmessage.enums.TypeMessageEnum;
import com.example.sendmessage.exception.ValidationException;
import com.example.sendmessage.model.Message;
import com.example.sendmessage.model.Sms;
import com.example.sendmessage.template.IMessageTemplate;
import com.example.sendmessage.util.StringUtil;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class SmsTipoAImpl extends SmsTemplateBase implements IMessageTemplate {

	private final String templateDefault = "Seu codigo sms ${otpCode}. Informe para ativar sua carteira ${walletName}";

	@Autowired
	private WebClient webClient;

	public void valid(Message message) throws ValidationException {
		super.valid(message);
		
		if(message.getOtpCode() == null) {
			throw new ValidationException("O campo otpCode deve ser informado para o layout sms");
		}
		
		if(message.getWalletName() == null) {
			throw new ValidationException("O campo WalletName deve ser informado para o layout sms");
		}
	}
	
	@Override
	public void send(Message message) throws BaseException {
		Sms sms = this.buildSms(message);

		// Enviando sms
		System.out.println(sms);

		try {
			webClient.post()
					.uri("http://localhost:3002/sms")
					.bodyValue(sms)
					.retrieve()
					.onStatus(status -> status.isError(), response -> {
						return this.handleErrorResponse(response);
					})
					.bodyToMono(Void.class)
					//.onErrorReturn(WebClientResponseException.class, new TechnicalException("Erro de comunicação com o serviço de sms"))
					.block();
		} catch (WebClientException e) {
			// add request info in exception
			throw new TechnicalException("Erro de comunicação com o serviço de sms -" + e.getMessage(), e);
		}
	}

	public Sms buildSms(Message message) {
		String msg = StringUtil.replaceSimpleValues(templateDefault, message);
		
		return Sms.builder()
			.otpCode(message.getOtpCode())
			.phone(message.getPhone())
			.message(msg).build();
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
		return TypeTemplateEnum.SMS_OTP;
	}


	private Mono<? extends Throwable> handleErrorResponse(ClientResponse response) {
		if (response.statusCode().is4xxClientError()) {
			switch (response.statusCode()) {
				case REQUEST_TIMEOUT:
				case TOO_MANY_REQUESTS:
					return Mono.error(new TechnicalException("Erro de comunicação com o serviço de sms" + response.statusCode()));

			}
		} else if (response.statusCode().is5xxServerError()) {
			return Mono.error(new TechnicalException("Erro de comunicação com o serviço de sms" + response.statusCode()));
		} else {
			return Mono.error(new BusinessException("Erro de negocio com o serviço de sms" + response.statusCode()));
		}
		return null;
	}
}
