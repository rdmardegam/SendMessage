package com.example.sendmessage.config;

import com.example.sendmessage.exception.BusinessException;
import com.example.sendmessage.exception.TechnicalException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Function;

@Configuration
public class WebclientConfig {

    /*@Bean
    public WebClient webClientA() {
        // Create factory reactor to use in the webclient
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(HttpClient.create().wiretap(true));

        // Create webclient
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .clientConnector(connector)
                .build();
    }*/

    @Bean
    public WebClient webClient() {
        // Configurar o HttpClient com pooling de conexões
        HttpClient httpClient = HttpClient.create()
                //.responseTimeout(Duration.ofSeconds(1))
                .tcpConfiguration(tcpClient -> tcpClient
                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10) // Timeout de conexão
                            .option(ChannelOption.TCP_NODELAY, true) // Desabilitar o Nagle
                            .option(ChannelOption.SO_KEEPALIVE, true)  // Manter conexão ativa
                        )
                        .doOnConnected(conn -> conn
                            .addHandlerLast(new ReadTimeoutHandler(1)) // Timeout de leitura e escrita
                 //           .addHandlerLast(new WriteTimeoutHandler(1000)) // Timeout de leitura e escrita
                )
                ;




        // Configurar o WebClient com o HttpClient
        // add time out
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequestAndResponse()) // Exemplo de filtro para log de solicitações
                .build();

        return webClient;
    }

    private ExchangeFilterFunction logRequestAndResponse() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request: " + clientRequest.method() + " " + clientRequest.url());
            HttpHeaders headers = clientRequest.headers();
            headers.forEach((name, values) -> System.out.println(name + ": " + values));
            return Mono.just(clientRequest);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response Status: " + clientResponse.statusCode());
            HttpHeaders headers = clientResponse.headers().asHttpHeaders();
            headers.forEach((name, values) -> System.out.println(name + ": " + values));
            return clientResponse.bodyToMono(byte[].class).doOnNext(body -> {
                String responseBody = new String(body, StandardCharsets.UTF_8);
                System.out.println("Response Body: " + responseBody);
            }).map(response -> clientResponse);
        }));
        //.andThen(ExchangeFilterFunction.ofResponseProcessor(this::processWebClientResponse));

    }

    private Mono<ClientResponse> processWebClientResponse(ClientResponse clientResponse) {
        HttpStatus responseStatus = clientResponse.statusCode();
        if (responseStatus.isError()) {
            System.out.println("********** Erro durante a chamada ao servidor " + responseStatus);
            return clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        // Tratar erros de acordo com o código de status
                        if (responseStatus.is4xxClientError()) {
                            return Mono.error(new BusinessException("Erro durante a chamada ao servidor " + responseStatus + " " + errorBody));
                        } else if (responseStatus.is5xxServerError()) {
                            return Mono.error(new TechnicalException("Erro durante a chamada ao servidor " + responseStatus + " " + errorBody));
                        }
                        return Mono.just(clientResponse);
                    });
        }
        return Mono.just(clientResponse);
    }



//    Function<ClientResponse, Mono<ClientResponse>> webclientResponseProcessor =
//            clientResponse -> {
//                HttpStatus responseStatus = clientResponse.statusCode();
//                if(responseStatus.isError()) {
//                    return clientResponse.bodyToMono(String.class)
//                            .flatMap(errorBody -> {
//                                // Tratar erros de acordo com o código de status
//                                if(responseStatus.is4xxClientError()) {
//                                    return Mono.error(new BusinessException("Erro durante a chamada ao servidor " + responseStatus + " " + errorBody));
//                                } else if(responseStatus.is5xxServerError()) {
//                                    return Mono.error(new TechnicalException("Erro durante a chamada ao servidor " + responseStatus + " " + errorBody));
//                                }
//                                return Mono.just(clientResponse);
//                            });
//                }
//                return Mono.just(clientResponse);
//            };
    }