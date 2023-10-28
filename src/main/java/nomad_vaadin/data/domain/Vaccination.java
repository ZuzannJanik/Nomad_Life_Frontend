package nomad_vaadin.data.domain;

import lombok.*;
import nomad_vaadin.data.domain.enums.VacType;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vaccination {
    private Long vacId;
    private String diseaseName;
    private LocalDate lastVac;
    private VacType vacType;
    private Long userId;
}
