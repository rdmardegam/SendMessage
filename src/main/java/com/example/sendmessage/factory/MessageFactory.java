//package com.example.sendmessage.factory;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.expression.EvaluationContext;
//import org.springframework.expression.Expression;
//import org.springframework.expression.ExpressionParser;
//import org.springframework.expression.spel.standard.SpelExpressionParser;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.stereotype.Service;
//
//import com.example.sendmessage.enums.TypeMessageEnum;
//import com.example.sendmessage.model.Message;
//import com.example.sendmessage.service.IMessage;
//
//@Service
//public class MessageFactory {
//
//	private static String rules_sms   = "{'1', '2', '4'}.contains(codMessage)";
//	private static String rules_email = "codMessage.equals('2')";
//
//
//	private static final Map<TypeMessageEnum, IMessage> myServiceCache = new HashMap<>();
//
//	@Autowired
//	private MessageFactory(List<IMessage> services) {
//	     for(IMessage service : services) {
//	          myServiceCache.put(service.getTypeMessage(), service);
//	      }
//	}
//
//
//	public static List<IMessage> getServiceByMessage(Message message) throws Exception {
//		List<IMessage> listMessage =  new ArrayList<IMessage>();
//
//		ExpressionParser parser = new SpelExpressionParser();
//		Expression expression = parser.parseExpression(rules_sms);
//		EvaluationContext context = new StandardEvaluationContext(message);
//
//		if(expression.getValue(context, Boolean.class)) {
//			listMessage.add(myServiceCache.get(TypeMessageEnum.SMS));
//		}
//
//		expression = parser.parseExpression(rules_email);
//		context = new StandardEvaluationContext(message);
//		if(expression.getValue(context, Boolean.class)) {
//			listMessage.add(myServiceCache.get(TypeMessageEnum.EMAIL));
//		}
//
//
//		if(listMessage.isEmpty()) {
//			throw new Exception("This message not enable to send message(s)");
//		}
//
//
//		return listMessage;
//	}
//}
