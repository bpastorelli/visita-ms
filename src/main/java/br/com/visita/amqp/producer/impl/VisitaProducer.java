package br.com.visita.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.visita.amqp.producer.KafkaTemplateAbstract;
import br.com.visita.dto.VisitaDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VisitaProducer extends KafkaTemplateAbstract<VisitaDto> {
	
	@Value("${visita.topic.name}")
	private String topic;
	
	public void producer(@Payload VisitaDto dto) {
		
		kafkaTemplate.send(topic, dto);
		
	}
	
}

