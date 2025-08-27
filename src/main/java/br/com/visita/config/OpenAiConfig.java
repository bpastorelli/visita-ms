package br.com.visita.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.theokanning.openai.service.OpenAiService;

@Configuration
public class OpenAiConfig {
	
    @Value("${openai.api.key}")
    private String apiKey;

    @Bean
    OpenAiService openAiService() {
        return new OpenAiService(apiKey);
    }

}
