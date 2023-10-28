package nomad_vaadin.service;

import lombok.extern.slf4j.Slf4j;
import nomad_vaadin.config.BackendConfig;
import nomad_vaadin.data.domain.NomadUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Component
public class NomadUserService {
    WebClient webClient = WebClient.create();
    public final BackendConfig backendConfig;

    @Autowired
    public NomadUserService(BackendConfig backendConfig) {
        this.backendConfig = backendConfig;

    }
    public List<NomadUser> getUsers() {
        return webClient.get()
                .uri(backendConfig.backendUrl + "/users")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(NomadUser.class)
                .collectList()
                .block();
    }

    public void saveUser(NomadUser nomadUser) {
        if (nomadUser.getUserId() == null) {
            webClient.put()
                    .uri(backendConfig.backendUrl + "/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(nomadUser)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } else {
            webClient.post()
                    .uri(backendConfig.backendUrl + "/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(nomadUser)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
    }

    public void deleteUser(NomadUser nomadUser) {
        webClient.method(HttpMethod.DELETE)
                .uri(backendConfig.backendUrl + "/users/" + nomadUser.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(nomadUser)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
