package app.Backend.Dto;

import lombok.Data;

@Data
public class commandeInfo {
    private Long id;
    private double prix;
    private Long total;
    private String status;
    private double clientLong;
    private double clientLalt;
    private Double livreurLong;
    private Double livreurLat;
    
}
