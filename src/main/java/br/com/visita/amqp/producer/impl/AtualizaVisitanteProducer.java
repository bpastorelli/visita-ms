package br.com.visita.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.visita.amqp.producer.KafkaTemplateAbstract;
import br.com.visita.dto.AtualizaVisitanteDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AtualizaVisitanteProducer extends KafkaTemplateAbstract<AtualizaVisitanteDto> {

	@Value("${visitante.topic.name}")
	private String topic;
	
	public void producer(@Payload AtualizaVisitanteDto dto) {
		
		kafkaTemplate.send(topic, dto);	
		
	}

	@Async("asyncKafka")
	public void producerAsync(@Payload AtualizaVisitanteDto dto) {
		
		kafkaTemplate.send(topic, dto);
		
	}
	
}
