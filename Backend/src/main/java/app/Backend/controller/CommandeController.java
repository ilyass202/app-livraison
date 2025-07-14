package app.Backend.controller;

import app.Backend.Dto.ValidationCommandeDto;
import app.Backend.Dto.commandeInfo;
import app.Backend.Service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/commande")
@CrossOrigin(origins = "http://localhost:4200")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @PostMapping("/valider")
    public ResponseEntity<commandeInfo> validerCommande(@RequestBody ValidationCommandeDto dto) {
        commandeInfo info = commandeService.validerCommande(dto);
        return ResponseEntity.ok(info);
    }


}