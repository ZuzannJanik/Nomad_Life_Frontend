package nomad_vaadin.data.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Answer {
    private Long answerId;
    private String question;
    private String snippet;
    private String link;
}