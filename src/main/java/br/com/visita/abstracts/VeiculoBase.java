package br.com.visita.abstracts;

import java.util.List;

import javax.persistence.MappedSuperclass;

import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import br.com.visita.dto.ProdutoRequestDto;
import br.com.visita.dto.VeiculoDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@MappedSuperclass
public abstract class VeiculoBase {
	
	final String apiKey;
	final WebClient webClient;
	final ObjectMapper objectMapper;
	protected final OpenAiService openAiService;
    
    public VeiculoBase(OpenAiService openAiService, WebClient.Builder webClientBuilder, String apiKey) {
        this.openAiService = openAiService;
    	this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.apiKey = apiKey;
    	this.objectMapper = new ObjectMapper();
    }
    
    protected VeiculoDto createProductFromChatGpt(ProdutoRequestDto request) {
        
    	String prompt = "Baseado na seguinte pesquisa: 'Pesquise um veículo " + request.getUserQuery() + ". Considere os modelos lançados no ano. Onde o modelo é o nome e a versão'. Gere um JSON com os seguintes campos: 'placa' (string), 'marca' (string), 'modelo' (string), 'cor' (string) e 'ano' (string). Onde o ano é o ano de lançamento do modelo. Com as letras em maiusculo. Não inclua texto extra, apenas o objeto JSON.";
    	
        // 2. Envia a requisição para a API da OpenAI
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4.1")
                .messages(List.of(
                        new ChatMessage(ChatMessageRole.SYSTEM.value(), "Você é um assistente que pesquisa informações, reais, de veículos no formato JSON."),
                        new ChatMessage(ChatMessageRole.USER.value(), prompt)
                ))
                .build();

        // 3. Obtém a resposta do ChatGPT
        ChatMessage responseMessage = openAiService.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();
        String jsonResponse = responseMessage.getContent();

        try {
            // 4. Desserializa o JSON para um objeto Product
        	VeiculoDto product = objectMapper.readValue(jsonResponse, VeiculoDto.class);
      
            log.info("Produto salvo com sucesso: " + product.getMarca() + " " + product.getModelo());
            return product;
        } catch (Exception e) {
            log.error("Erro ao processar a resposta do ChatGPT: " + e.getMessage());
            throw new RuntimeException("Não foi possível gerar e salvar o produto.", e);
        }
    }
        
}
