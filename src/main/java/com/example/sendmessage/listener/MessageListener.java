package com.example.sendmessage.listener;

import com.example.sendmessage.exception.BusinessException;
import com.example.sendmessage.model.Message;
import com.example.sendmessage.service.impl.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@EnableKafka
public class MessageListener {

    private static final int THREAD_POOL_SIZE = 100;
    private static final int NACK_SLEEP_MILLIS = 2000;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("circutSendsms")
    private CircuitBreaker circuitBreaker;

    private final Map<String, String> processedRecords = Collections.synchronizedMap(new HashMap<>());
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    @KafkaListener(topics = "MASTER_CARD_TOKEN_ACTIVATION",
            groupId = "groupo1",
            batch = "true",
            containerFactory = "kafkaListenerContainerFactory"
            , concurrency = "3"
    )
    public void messageListener(List<ConsumerRecord<String, String>> records, Acknowledgment ack) throws Exception {
        // Recebe todos os registros atuais do tópico em um SET
        Set<String> allRecords = getAllRecords(records);

        // Recebe todos os registros atuais do tópico que ainda não foram processados em uma execução anterior
        List<ConsumerRecord<String, String>> unprocessedRecords = getUnprocessedRecords(records);

        // Se o circuito estiver aberto, não processa os registros
        if (!isCircuitBreakerOpen()) {
            processRecords(unprocessedRecords);
        }

        // Se todos os registros foram processados, faz o ACK
        if (areAllRecordsProcessed(unprocessedRecords)) {
            acknowledgeRecords(ack, allRecords);
        } else {
            // Se nem todos os registros foram processados, faz o NACK e dorme por X segundos
            nackAndSleep(ack);
        }

        // printa processedRecords size
        System.out.println("processedRecords size: " + processedRecords.size());

    }

    private Set<String> getAllRecords(List<ConsumerRecord<String, String>> records) {
        return records.stream()
                .map(this::getTopicPartitionOffset)
                .collect(Collectors.toSet());
    }

    private List<ConsumerRecord<String, String>> getUnprocessedRecords(List<ConsumerRecord<String, String>> records) {
        return records.stream()
                .filter(record -> !processedRecords.containsKey(getTopicPartitionOffset(record)))
                .collect(Collectors.toList());
    }

    private void processRecords(List<ConsumerRecord<String, String>> records) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(records.size());

        for (ConsumerRecord<String, String> record : records) {
            executorService.submit(() -> processRecord(record, latch));
        }

        latch.await();
    }

    private void processRecord(ConsumerRecord<String, String> record, CountDownLatch latch) {
        String idMessage = getTopicPartitionOffset(record);

        try {
            if (isCircuitBreakerOpen()) {
                return;
            }

            Message message = objectMapper.readValue(record.value(), Message.class);

            int random = (int) (Math.random() * 4);

            messageService.sendMessage(message, random);

            markRecordAsProcessed(idMessage);

            System.out.println("Success: " + idMessage);
        } catch (Exception exception) {
            handleException(exception, idMessage);
        } finally {
            latch.countDown();
        }
    }


    private void markRecordAsProcessed(String idMessage) {
        processedRecords.put(idMessage, idMessage);
    }

    private boolean areAllRecordsProcessed(List<ConsumerRecord<String, String>> records) {
        return processedRecords.keySet()
                .containsAll(records.stream().map(this::getTopicPartitionOffset).collect(Collectors.toList()));
    }

    private void acknowledgeRecords(Acknowledgment ack, Set<String> allRecords) {
        ack.acknowledge();
        removeProcessedRecords(allRecords);
    }

    private void removeProcessedRecords(Set<String> allRecords) {
        processedRecords.keySet().removeAll(allRecords);
    }

    private void nackAndSleep(Acknowledgment ack) throws InterruptedException {
        ack.nack(0, Duration.ofMillis(0));
        System.out.println("NACK");
        Thread.sleep(NACK_SLEEP_MILLIS);
    }

    private void handleException(Exception exception, String idMessage) {
        // Se o circuito estiver aberto, não processa os registros
        if (exception instanceof CallNotPermittedException) {
            exception.printStackTrace();
            System.out.println("CircuitBreakerOpenException: " + idMessage);

        } else if (exception instanceof BusinessException || exception instanceof RuntimeException) {
            System.out.println("BusinessException: " + idMessage);
            markRecordAsProcessed(idMessage);
            // Implement DLQ sending
        } else {
            System.out.println(exception.getClass().getSimpleName() + ": " + idMessage);
            markRecordAsProcessed(idMessage);
            // Implement Retry sending
        }
    }

    private boolean isCircuitBreakerOpen() {
        return circuitBreaker.getState().equals(CircuitBreaker.State.OPEN);
    }

    private String getTopicPartitionOffset(ConsumerRecord<String, String> record) {
        return record.topic() + "-" + record.partition() + "-" + record.offset();
    }

}