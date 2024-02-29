package org.snubi.did.main;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;



@SpringBootTest
class RabbitMqTest {

	@Autowired private  RabbitTemplate rabbitTemplate;
	
	@Test
	void contextLoads() {
		
		ObjectMapper objectMapper = new ObjectMapper();
        rabbitTemplate.convertAndSend("chat.exchange", "room.key", "test2");
        System.out.print("rabbitmq test");
	}

}