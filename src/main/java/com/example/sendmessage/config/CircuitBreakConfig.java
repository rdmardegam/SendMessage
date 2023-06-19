package com.example.sendmessage.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakConfig {

    @Autowired
    private CircuitBreakerRegistry registry;

    @Bean(name = "circutSendsms")
    public CircuitBreaker circuitBreakerRegistry() {
        CircuitBreaker circuitBreaker = registry.circuitBreaker("sendsms");
        circuitBreaker.getEventPublisher().onStateTransition(this::onStateChange);
        return circuitBreaker;
    }

    private void onStateChange(CircuitBreakerOnStateTransitionEvent event) {
        // Add switch case to log state change
        String fromState = event.getStateTransition().getFromState().name();
        String toState = event.getStateTransition().getToState().name();

        System.out.println("CircuitBreaker state changed from " + fromState + " to " + toState);
    }
}
