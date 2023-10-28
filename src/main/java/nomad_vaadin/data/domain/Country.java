package nomad_vaadin.data.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country {
        private Long countryId;
        private String countryName;
        private String flagUrl;
}
