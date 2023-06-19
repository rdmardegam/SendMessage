package com.example.sendmessage.exception;

public class TechnicalException extends BaseException {
        public TechnicalException() {
            super();
        }

        public TechnicalException(String message) {
            super(message);
        }

        public TechnicalException(String message, Throwable cause) {
            super(message, cause);
        }

        public TechnicalException(Throwable cause) {
            super(cause);
        }
}
