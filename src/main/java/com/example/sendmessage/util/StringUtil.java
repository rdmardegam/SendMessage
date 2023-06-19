package com.example.sendmessage.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;



public class StringUtil {
	
	private static final Pattern pattern = Pattern.compile("\\$\\{([\\w\\.]*)\\}");
	
	public static String replaceSimpleValues(final String text, final Object source) {
	    StringBuilder sb = new StringBuilder();
	    Matcher matcher = pattern.matcher(text);
	    
	    while (matcher.find()) {
	        String key = matcher.group(1);
	        Object replacement = getFieldValue(source, key);
	        
	        if (replacement != null) {
	            if (replacement instanceof Collection) {
	                @SuppressWarnings("unchecked")
	                Collection<Object> values = (Collection<Object>) replacement;
	                StringBuilder replacementValues = new StringBuilder();
	                
	                for (Object value : values) {
	                    replacementValues.append(String.valueOf(value)).append("</br>");
	                }
	                
	                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacementValues.toString()));
	            } else {
	                String replaceValue = String.valueOf(replacement);
	                matcher.appendReplacement(sb, Matcher.quoteReplacement(replaceValue));
	            }
	        }
	    }
	    
	    matcher.appendTail(sb);
	    return sb.toString();
	}
	
	private static Object getFieldValue(Object parent, String attr)  {
	    try {
	        SpelExpressionParser parser = new SpelExpressionParser();
	        Expression expression = parser.parseExpression(attr);
	        StandardEvaluationContext context = new StandardEvaluationContext(parent);
	        
	        synchronized (StringUtil.class) {
	            return expression.getValue(context);
	        }
	    } catch (Exception e) {
	        return null;
	    }
	}
	
//	public static String replaceSimpleValues(String text, Object source) {
//	    SpelExpressionParser parser = new SpelExpressionParser();
//	    StandardEvaluationContext context = new StandardEvaluationContext(source);
//
//	    Field[] fields = source.getClass().getDeclaredFields();
//
//	    for (Field field : fields) {
//	        field.setAccessible(true);
//	        String fieldName = field.getName();
//
//	        // Verificar se o campo existe na expressão de texto
//	        if (text.contains("{" + fieldName + "}")) {
//	            Object fieldValue;
//	            try {
//	                fieldValue = field.get(source);
//	            } catch (IllegalAccessException e) {
//	                throw new RuntimeException("Erro ao acessar o valor do campo: " + fieldName, e);
//	            }
//
//	            // Verificar o tipo do campo e substituir na expressão
//	            if (fieldValue != null) {
//	                String fieldExpression = "{" + fieldName + "}";
//	                String fieldValueAsString = String.valueOf(fieldValue);
//	                text = text.replace(fieldExpression, fieldValueAsString);
//	            }
//	        }
//	    }
//
//	    byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
//		text = new String(utf8Bytes, StandardCharsets.UTF_8);
//
//	    Expression expression = parser.parseExpression(text);
//	    String result = expression.getValue(context, String.class);
//
//	    return result;
//	}
	
}
