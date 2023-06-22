package com.example.sendmessage.config;

import com.example.sendmessage.exception.MultiErrorException;
import com.example.sendmessage.exception.TechnicalException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerProperties;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class CircuitBreakConfig {

    @Autowired
    private CircuitBreakerRegistry registry;

    @Autowired
    private CircuitBreakerProperties circuitBreakerProperties;

    @Bean(name = "circutSendsms")
    public CircuitBreaker circuitBreakerRegistry() {
        CircuitBreakerConfig existingConfig = registry.getDefaultConfig();

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.from(existingConfig)
                // Verifica se no stacktrace da exception contem a Exception TechnicalException
                .recordException(throwable -> hasThrowableInHierarchy(throwable, TechnicalException.class))
                .build();

        CircuitBreaker circuitBreaker = registry.circuitBreaker("sendsms",circuitBreakerConfig);
        circuitBreaker.getEventPublisher().onStateTransition(this::onStateChange);

        return circuitBreaker;
    }

    private void onStateChange(CircuitBreakerOnStateTransitionEvent event) {
        // Add switch case to log state change
        String fromState = event.getStateTransition().getFromState().name();
        String toState = event.getStateTransition().getToState().name();

        System.out.println("CircuitBreaker state changed from " + fromState + " to " + toState);
    }

    private boolean hasThrowableInHierarchy(Throwable throwable, Class<? extends Throwable> targetExceptionClass) {
        if (throwable == null || targetExceptionClass == null) {
            return false;
        }

        if (targetExceptionClass.isInstance(throwable)) {
            return true;
        }

        // check if throwable has MessageSendingException
        if (MultiErrorException.class.isInstance(throwable)) {
            if(throwable.getSuppressed().length > 0 && Arrays.stream(throwable.getSuppressed()).anyMatch(suppressed -> hasThrowableInHierarchy(suppressed, targetExceptionClass))) {
                return true;
            }
        }

        return hasThrowableInHierarchy(throwable.getCause(), targetExceptionClass);
    }
}
