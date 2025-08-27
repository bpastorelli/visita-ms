package br.com.visita.amqp.producer.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.com.visita.amqp.producer.KafkaTemplateAbstract;
import br.com.visita.dto.VeiculoDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VeiculoProducer extends KafkaTemplateAbstract<VeiculoDto> {
	
	@Value("${veiculo.topic.name}")
	private String topic;
	
	public void producer(VeiculoDto dto) {
		
		kafkaTemplate.send(topic, dto);	
		
	}

	@Async("asyncKafka")
	public void producerAsync(VeiculoDto dto) {
		
		kafkaTemplate.send(topic, dto);
		
	}

	
}

