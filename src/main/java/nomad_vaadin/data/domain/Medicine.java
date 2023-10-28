package nomad_vaadin.data.domain;

import lombok.*;
import nomad_vaadin.data.domain.enums.MedType;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    private Long medicineId;
    private String medicineName;
    private String designation;
    private MedType medType;
    private LocalDate expiryDate;
}
