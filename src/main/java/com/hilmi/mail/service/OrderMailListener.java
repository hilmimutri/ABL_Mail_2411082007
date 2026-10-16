package com.hilmi.mail.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hilmi.mail.config.RabbitMQConfig;
import com.hilmi.mail.model.OrderMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class OrderMailListener {

  @Autowired
  private OrderMailSender orderMailSender;

  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE_NAME)
  public void receiveOrder(String payload) {
    try {
      OrderMessage order = objectMapper.readValue(payload, OrderMessage.class);
      System.out.println("Received order: " + payload);
      orderMailSender.sendOrderMail(order);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to read order message", e);
    }
  }
}
