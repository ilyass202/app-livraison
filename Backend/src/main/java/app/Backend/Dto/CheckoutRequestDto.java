package app.Backend.Dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequestDto {
    private Long userId;
    private List<ProduitStripeDto> produits;
}
