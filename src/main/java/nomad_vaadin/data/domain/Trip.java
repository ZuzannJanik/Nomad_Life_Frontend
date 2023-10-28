package nomad_vaadin.data.domain;

import lombok.*;
import nomad_vaadin.data.domain.enums.TripStatus;
import java.time.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    private Long tripId;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String destinationCountry;
    private TripStatus tripStatus;
    private Set<NomadUser> userList = new HashSet<>();
}
