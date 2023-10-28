package nomad_vaadin.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nomad_vaadin.config.BackendConfig;
import nomad_vaadin.data.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class CountryService {
    WebClient webClient = WebClient.create();
    public final BackendConfig backendConfig;
    @Getter
    List<Country> countryCache = new ArrayList<>();
    @Autowired
    public CountryService(BackendConfig backendConfig) {
        this.backendConfig = backendConfig;
    }

    public Country getCountryFlagURL(String countryName) {
            return webClient.get()
                    .uri(backendConfig.backendUrl + "/countries/{countryName}")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Country.class)
                    .block();
        }
    }


