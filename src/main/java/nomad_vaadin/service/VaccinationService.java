package nomad_vaadin.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nomad_vaadin.config.BackendConfig;
import nomad_vaadin.data.domain.Vaccination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class VaccinationService {
    WebClient webClient = WebClient.create();
    public final BackendConfig backendConfig;
    @Getter
    List<Vaccination> vaccinationCache = new ArrayList<>();

    @Autowired
    public VaccinationService(BackendConfig backendConfig) {
        this.backendConfig = backendConfig;
    }

    public List<Vaccination> getVaccinations() {
        return webClient.get()
                .uri(backendConfig.backendUrl + "/vaccinations")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Vaccination.class)
                .collectList()
                .block();
    }

    public void saveVaccination(Vaccination vaccination) {
        if (vaccination.getVacId() == null) {
            webClient.put()
                    .uri(backendConfig.backendUrl + "/vaccinations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(vaccination)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } else {
            webClient.post()
                    .uri(backendConfig.backendUrl + "/vaccinations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(vaccination)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
    }

    public void deleteVaccination(Vaccination vaccination) {
        webClient.method(HttpMethod.DELETE)
                .uri(backendConfig.backendUrl + "/vaccinations/" + vaccination.getVacId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(vaccination)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}