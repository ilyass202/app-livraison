package app.Backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.Backend.Dto.CheckoutRequestDto;
import app.Backend.Dto.StripeReponseDto;
import app.Backend.Service.StripeService;

@RestController
@RequestMapping("/paiement")
public class StripeControlleur {
    private StripeService strip;
    public StripeControlleur(StripeService strip){
        this.strip = strip;
    }
    @PostMapping("/session")
    public ResponseEntity<StripeReponseDto> session(@RequestBody CheckoutRequestDto request){
        StripeReponseDto reponse = strip.checkout(request);
        return ResponseEntity.ok(reponse);

    }

}
