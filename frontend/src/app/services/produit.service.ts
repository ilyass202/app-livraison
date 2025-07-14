import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, EMPTY } from 'rxjs';


const BASIC_URL = "http://localhost:8080";

@Injectable({ providedIn: 'root' })
export class ProduitService {

  constructor(private http : HttpClient) { }
  
  getAllProduits():Observable<any> {
    return this.http.get( `${BASIC_URL}/user/produits`);
  }
  getAllProduitsBynom(nom : any) : Observable<any>{
    return this.http.get( `${BASIC_URL}/user/search/${nom}`);
  }
  ajouterPanier(id: any) {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const userId = user.id;
      const panierData = { userId, produitId: id };
      return this.http.post(`${BASIC_URL}/client/Panier`, panierData);
    }
    // Si l'utilisateur n'est pas connecté, on retourne null (ou on peut lever une erreur)
    return null;
  }
  getPanierById(): Observable<any> {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const userId = user.id;
      return this.http.get(`${BASIC_URL}/client/Panier/${userId}`);
    }
    return EMPTY;

  }
  augmenterQuantite(id: any): Observable<any> {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const userId = user.id;
      const panierData = { userId, produitId: id };
      return this.http.post(`${BASIC_URL}/client/Panier/augmenter`, panierData);
    }
  
    return EMPTY;
  }
  diminuerQuantite(id: any): Observable<any> {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const userId = user.id;
      const panierData = { userId, produitId: id };
      return this.http.post(`${BASIC_URL}/client/Panier/diminuer`, panierData);
    }
    // Si l'utilisateur n'est pas connecté, on retourne un Observable vide
    return EMPTY;
  }
  supprimerArticle(panierId: any, userId: any): Observable<any> {
    return this.http.delete(`${BASIC_URL}/client/Panier/${panierId}/${userId}`);
  }
  validerCommande(): Observable<any> {
    return new Observable(observer => {
      const userStr = localStorage.getItem('loggedUser');
      if (!userStr) {
        observer.error('Utilisateur non connecté');
        return;
      }
      navigator.geolocation.getCurrentPosition(
        position => {
          const user = JSON.parse(userStr);
          const userId = user.id;
          const Info = {
            userId: userId,
            clientLat: position.coords.latitude,
            clientLong: position.coords.longitude
          };
          this.http.post(`${BASIC_URL}/commande/valider`, Info)
            .subscribe(
              res => {
                observer.next(res);
                observer.complete();
              },
              err => observer.error(err)
            );
        },
        error => observer.error('Impossible de récupérer la position')
      );
    });
  }
  getCommandeInfo(userId: number): Observable<any> {
    return this.http.get(`${BASIC_URL}/commande/info/${userId}`);
  }
}
