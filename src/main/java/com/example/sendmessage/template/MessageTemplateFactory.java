package com.example.sendmessage.template;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sendmessage.model.Message;

@Component
public class MessageTemplateFactory {
 
	private static final List<IMessageTemplate> listEmailTemplateServiceCache = new ArrayList<IMessageTemplate>();

	@Autowired
	private MessageTemplateFactory(List<IMessageTemplate> services) {
		listEmailTemplateServiceCache.addAll(services);
	}
	
	public static List<IMessageTemplate> getServiceMessageTemplateByMessage(Message message) {
		List<IMessageTemplate> lisTemplateSelected =  new ArrayList<IMessageTemplate>();
		
		for (IMessageTemplate template : listEmailTemplateServiceCache) {
            if (template.isApplicable(message)) {
            	lisTemplateSelected.add(template); 
            }
        }
		return lisTemplateSelected;
	}
}
