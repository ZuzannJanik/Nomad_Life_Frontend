package nomad_vaadin.data.domain;

import lombok.*;
import nomad_vaadin.data.domain.enums.UserRole;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NomadUser {
    private Long userId;
    private String firstName;
    private String surname;
    private String homeland;
    private String login;
    private String password;
    private UserRole role;
}

