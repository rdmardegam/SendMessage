package com.example.sendmessage.exception;

import com.example.sendmessage.enums.TypeMessageEnum;
import com.example.sendmessage.enums.TypeTemplateEnum;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.Map;

public class MessageSendingException extends Exception {
    private Map<TypeTemplateEnum, Exception> errorMap;

    public MessageSendingException(Map<TypeTemplateEnum, Exception> errorMap) {
        // Join all errors in one
        //super(errorMap.values().stream().map(e -> ExceptionUtils.getRootCauseMessage(e)).reduce((a, b) -> a + " - " + b).get());




        // add erros to log in super
//        super(ExceptionUtils.getMessage(errorMap.values().iterator().next()));
//        //super(ExceptionUtils.getRootCauseMessage(errorMap.values().iterator().next()));
//        super(ExceptionUtils.getRootCauseStackTrace(errorMap.values().iterator().next()));



//        ExceptionUtils.getThrowableList(errorMap.values().iterator().next()).forEach(e -> {
//            super.addSuppressed(e);
//        });


        //super("TETE");

        // Lista de erros de validationException
        errorMap.forEach((k, v) -> {
            super.addSuppressed(v);
//            if (v instanceof ValidationException) {
//                ValidationException ex = ((ValidationException) v);
//            } else {
//                super.addSuppressed(v);
//            }
        });
        //super("teste");
        //super(ExceptionUtils.getMessage(errorMap.values().iterator().next()));
        this.errorMap = errorMap;
    }

    public Map<TypeTemplateEnum, Exception> getErrorMap() {
        return errorMap;
    }
}

