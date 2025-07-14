import { Component, OnInit } from '@angular/core';
import { MapData } from '../../services/map-data';
declare var L: any;


@Component({
  selector: 'app-map',
  templateUrl: './map.html',
  styleUrls: ['./map.css']
})
export class MapComponent implements OnInit {
  constructor(public data: MapData) {}

  ngOnInit(): void {
    const info = this.data.commandeInfo;
    if (!info || !info.clientLalt || !info.clientLong) {
      alert('Aucune information de commande ou coordonnées invalides');
      return;
    }

   
    const map = L.map('map').setView([info.clientLalt, info.clientLong], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // Ajouter marqueurs pour le client et le livreur
    L.marker([info.clientLalt, info.clientLong])
      .addTo(map)
      .bindPopup('Vous êtes ici')
      .openPopup();
    L.marker([info.livreurLat, info.livreurLong])
      .addTo(map)
      .bindPopup('Livreur');

    
    (L as any).Routing.control({
      waypoints: [
        L.latLng(info.livreurLat, info.livreurLong),
        L.latLng(info.clientLalt, info.clientLong)
        
      ],
      router: (L as any).Routing.osrmv1({
        serviceUrl: 'http://router.project-osrm.org/route/v1'
      }),
      lineOptions: { styles: [{ color: 'blue', weight: 4 }] },
      fitSelectedRoutes: true
    }).addTo(map);
  }
}