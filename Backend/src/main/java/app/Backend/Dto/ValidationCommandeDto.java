package app.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationCommandeDto {
    private Long userId;
    private double clientLong;
    private double clientLat; // Optionnel pour plus de détails
} 