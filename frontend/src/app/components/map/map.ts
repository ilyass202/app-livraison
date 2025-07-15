import { Component, OnInit } from '@angular/core';
import { MapData } from '../../services/map-data';
import { ProduitService } from '../../services/produit.service';
declare var L: any;


@Component({
  selector: 'app-map',
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})
export class MapComponent implements OnInit {
  constructor(public data: MapData , public produit : ProduitService) {}

  ngOnInit(): void {
    this.produit.validerCommande().subscribe({
      next: (resp: any) => {
        this.data.commandeInfo = resp;
        const info = this.data.commandeInfo;
        if (!info || !info.clientLalt || !info.clientLong) {
          alert('Aucune information de commande ou coordonnées invalides');
          return;
        }
        // Initialiser la carte après avoir reçu les infos
        const map = L.map('map').setView([info.clientLalt, info.clientLong], 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap contributors'
        }).addTo(map);

        // Ajouter marqueurs pour le client et le livreur
        L.marker([info.clientLalt, info.clientLong])
          .addTo(map)
          .bindPopup('Vous êtes ici')
          .openPopup();
        if (info.livreurLat && info.livreurLong) {
          L.marker([info.livreurLat, info.livreurLong])
            .addTo(map)
            .bindPopup('Livreur');

          // Tracer l'itinéraire si les coordonnées du livreur sont présentes
          (L as any).Routing.control({
            waypoints: [
              L.latLng(info.livreurLat, info.livreurLong),
              L.latLng(info.clientLalt, info.clientLong)
            ],
            router: (L as any).Routing.osrmv1({
              serviceUrl: 'http://router.project-osrm.org/route/v1'
            }),
            lineOptions: { styles: [{ color: 'blue', weight: 4 }] }
          }).addTo(map);
        }
      },
      error: (err) => {
        alert('Erreur lors de la récupération de la commande ou de la position');
      }
    });
  }
}