import { Component, OnInit } from '@angular/core';
import { CommonModule, NgIf, NgFor, NgClass, CurrencyPipe } from '@angular/common';
import { ProduitService } from '../../services/produit.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MapData } from '../../services/map-data';
import { Router } from '@angular/router';

@Component({
  selector: 'app-panier',
  standalone: true,
  imports: [
    CommonModule, NgIf, NgFor, NgClass, CurrencyPipe,
    MatIconModule, MatButtonModule
  ],
  templateUrl: './panier.html',
  styleUrl: './panier.css'
})
export class Panier implements OnInit {

  panier: any[] = [];
  commande: any;
  panierProduit : any[] =[];
  constructor(
    private produitservice: ProduitService,
    private snack: MatSnackBar,
    private fb: FormBuilder,
    public dialog: MatDialog,private data : MapData,
    private router : Router
  ) {}

  ngOnInit(): void {
    this.getPanier();
    this.produitservice.getPanierById().subscribe(data=>{
      this.panierProduit = data.articles;
    })
  }

  getPanier() {
    this.panier = [];
    this.produitservice.getPanierById().subscribe((resp) => {
      if (resp && resp.articles) {
        this.panier = resp.articles;
        this.commande = resp;
      } else {
        this.panier = [];
        this.commande = null;
      }
    });
  }

  getQuantiteTotale(): number {
    return this.panier.reduce((acc, item) => acc + (item.quantite || 0), 0);
  }
  augmenterQuantite(id:any){
    this.produitservice.augmenterQuantite(id).subscribe(()=>{
      this.snack.open('la quantité a été augmenté ', 'ferme', {duration : 5000})
      this.getPanier();
    })
  }
  diminuerQuantite(id: any) {
    this.produitservice.diminuerQuantite(id).subscribe(() => {
      this.snack.open('La quantité a été diminuée', 'fermer', { duration: 5000 });
      this.getPanier();
    });
  }
  supprimerArticle(panierId: any) {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const userId = user.id;
      this.produitservice.supprimerArticle(panierId, userId).subscribe(() => {
        this.snack.open('Article supprimé du panier', 'fermer', { duration: 5000 });
        this.getPanier();
      });
    }
  }
  validerCommande(){
    this.produitservice.validerCommande().subscribe(
      resp =>{
        this.data.commandeInfo = resp;
        this.router.navigateByUrl('/map');
      }
    )
  }
  placerCommande(){
    const userStr = localStorage.getItem('loggedUser');
    if(userStr){
      const user = JSON.parse(userStr);
      console.log('panierProduit:', this.panierProduit); // debug
      const produits = this.panierProduit.map(p => ({
        nom: p.produitNom,
        prix: Math.round(p.prix * 100), // prix en centimes, arrondi
        quantite: p.quantite
      }));
      console.log('produits (après mapping):', produits); // debug
      const info = {
        userId: user.id,
        produits: produits
      };
      console.log('info envoyé au backend :', info); // debug
      this.produitservice.creerSessionStripe(info).subscribe((resp: any) => {
        localStorage.setItem('stripeSessionId', resp.sessionId);
        window.location.href = resp.url;
      });
    }
  }
}
