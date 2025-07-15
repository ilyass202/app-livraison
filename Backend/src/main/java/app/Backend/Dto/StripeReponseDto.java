package app.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeReponseDto {
    private String url;
    private String sessionId;

}
