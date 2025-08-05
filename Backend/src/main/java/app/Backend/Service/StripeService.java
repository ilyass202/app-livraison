package app.Backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import app.Backend.Dto.CheckoutRequestDto;
import app.Backend.Dto.ProduitStripeDto;
import app.Backend.Dto.StripeReponseDto;


import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {
    @Value("${stripe.secret.key}")
    private String secretKey;

    public StripeReponseDto checkout(CheckoutRequestDto dto) {
        Stripe.apiKey = secretKey;

        
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        for (ProduitStripeDto produit : dto.getProduits()) {
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity((long) produit.getQuantite()) // Correction du cast int -> long
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("mad")
                        .setUnitAmount(produit.getPrix()) // prix en centimes
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(produit.getNom())
                                .build()
                        )
                        .build()
                )
                .build();
            lineItems.add(lineItem);
        }

        // Créer la session Stripe Checkout
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl("http://localhost:4200/map") // adapte l'URL si besoin
            .setCancelUrl("http://localhost:4200/panier")
            .addAllLineItem(lineItems)
            .build();

        try {
            Session session = Session.create(params);
            // Retourne l'URL Stripe Checkout et le sessionId dans un DTO de réponse
            return new StripeReponseDto(session.getUrl(), session.getId());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la session Stripe", e);
        }
    }
}
