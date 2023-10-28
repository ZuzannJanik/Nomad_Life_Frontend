package nomad_vaadin.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nomad_vaadin.config.BackendConfig;
import nomad_vaadin.data.domain.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TripService {
    WebClient webClient = WebClient.create();
    public final BackendConfig backendConfig;
    @Getter
    List<Trip> tripCache = new ArrayList<>();

    @Autowired
    public TripService(BackendConfig backendConfig) {
        this.backendConfig = backendConfig;
    }

    public List<Trip> getTrips() {
        return webClient.get()
                .uri(backendConfig.backendUrl + "/trips")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Trip.class)
                .collectList()
                .block();
    }

    public void saveTrip(Trip trip) {
        if (trip.getTripId() == null) {
            webClient.put()
                    .uri(backendConfig.backendUrl + "/trips")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(trip)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } else {
            webClient.post()
                    .uri(backendConfig.backendUrl + "/trips")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(trip)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        }
    }

    public void deleteTrip(Trip trip) {
        webClient.method(HttpMethod.DELETE)
                .uri(backendConfig.backendUrl + "/trips/" + trip.getTripId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(trip)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}