package com.example.sendmessage.exception;

import com.example.sendmessage.enums.TypeTemplateEnum;

import java.util.Map;

public class MultiErrorException extends BaseException {
    private Map<TypeTemplateEnum, Exception> failedTemplateMap;


    public MultiErrorException(String message, Map<TypeTemplateEnum, Exception> failedTemplateMap) {
        super(message/*,getExceptions(failedTemplateMap)*/);
        this.failedTemplateMap = failedTemplateMap;

        // Lista de erros de validationException
        failedTemplateMap.forEach((k, v) -> {
            super.addSuppressed(v);
        });

    }

    private static Throwable getExceptions(Map<TypeTemplateEnum, Exception> failedTemplateMap) {
        // Join all errors in one inside by inside
        // Join all errors in one inside by inside
        Throwable ex  = failedTemplateMap.get(0).getCause();

        for (int x = 1; x < failedTemplateMap.size(); x++) {
            // add exception inside exception
            ex.addSuppressed(failedTemplateMap.get(x));
        }

        return ex;


        //return failedTemplateMap.values().stream().map(e -> ExceptionUtils.getRootCause(e)).reduce((a, b) -> a).get();
    }


    public Map<TypeTemplateEnum, Exception> getFailedTemplateMap() {
        return failedTemplateMap;
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder(super.getMessage());
        sb.append("\nExceptions:");
        failedTemplateMap.forEach((k, v) -> {
            // show exception name
            sb.append("[");
            sb.append(v.getClass().getSimpleName());
            sb.append(" - Template: (").append(k.getDescription());
            sb.append(") - ");
            sb.append(v.getMessage());
            sb.append("];\n");
        });

        return sb.toString();
    }














//
//
//    public MessageSendingException(Map<TypeTemplateEnum, Exception> errorMap) {
//        // Join all errors in one
//        //super(errorMap.values().stream().map(e -> ExceptionUtils.getRootCauseMessage(e)).reduce((a, b) -> a + " - " + b).get());
//
//
//
//
//        // add erros to log in super
////        super(ExceptionUtils.getMessage(errorMap.values().iterator().next()));
////        //super(ExceptionUtils.getRootCauseMessage(errorMap.values().iterator().next()));
////        super(ExceptionUtils.getRootCauseStackTrace(errorMap.values().iterator().next()));
//
//
//
////        ExceptionUtils.getThrowableList(errorMap.values().iterator().next()).forEach(e -> {
////            super.addSuppressed(e);
////        });
//
//
//        //super("TETE");
//
//        // Lista de erros de validationException
//        errorMap.forEach((k, v) -> {
//            super.addSuppressed(v);
////            if (v instanceof ValidationException) {
////                ValidationException ex = ((ValidationException) v);
////            } else {
////                super.addSuppressed(v);
////            }
//        });
//        //super("teste");
//        //super(ExceptionUtils.getMessage(errorMap.values().iterator().next()));
//        this.errorMap = errorMap;
//    }
//
//    public Map<TypeTemplateEnum, Exception> getErrorMap() {
//        return errorMap;
//    }
//
//    @Override
//    public String getMessage() {
//
//    }

}

