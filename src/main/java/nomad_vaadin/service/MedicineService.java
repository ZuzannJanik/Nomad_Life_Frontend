package nomad_vaadin.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nomad_vaadin.config.BackendConfig;
import nomad_vaadin.data.domain.Medicine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MedicineService {
    WebClient webClient = WebClient.create();
    public final BackendConfig backendConfig;
    @Getter
    List<Medicine> medicineCache = new ArrayList<>();

    @Autowired
    public MedicineService(BackendConfig backendConfig) {
        this.backendConfig = backendConfig;

    }

    public List<Medicine> getMedicines() {
        return webClient.get()
                .uri(backendConfig.backendUrl + "/medicines")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Medicine.class)
                .collectList()
                .block();
    }

    public void saveMedicine(Medicine medicine) {
        if (medicine.getMedicineId() == null) {
            webClient.put()
                    .uri(backendConfig.backendUrl + "/medicines")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(medicine)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } else {
            webClient.post()
                    .uri(backendConfig.backendUrl + "/medicines")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(medicine)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
    }

    public void deleteMedicine(Medicine medicine) {
        webClient.method(HttpMethod.DELETE)
                .uri(backendConfig.backendUrl + "/medicines/" + medicine.getMedicineId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(medicine)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
