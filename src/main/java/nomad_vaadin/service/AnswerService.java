package nomad_vaadin.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nomad_vaadin.config.BackendConfig;
import nomad_vaadin.data.domain.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AnswerService {
        WebClient webClient = WebClient.create();
        public final BackendConfig backendConfig;
        @Getter
        List<Answer> countryCache = new ArrayList<>();
        @Autowired
        public AnswerService(BackendConfig backendConfig) {
            this.backendConfig = backendConfig;
        }

        public Answer getNewSnippet(String question) {
            return webClient.get()
                    .uri(backendConfig.backendUrl + "/answers/"+ question)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Answer.class)
                    .block();
        }
    }

