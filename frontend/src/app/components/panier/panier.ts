import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
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

  constructor(
    private produitservice: ProduitService,
    private snack: MatSnackBar,
    private fb: FormBuilder,
    public dialog: MatDialog, private data: MapData,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.getPanier();
  }

  getPanier() {
    this.produitservice.getPanierById().subscribe((resp) => {
      if (resp && resp.articles) {
        this.panier = resp.articles;
        this.commande = resp;
      } else {
        this.panier = [];
        this.commande = null;
      }
      this.cdr.detectChanges();
    });
  }

  getQuantiteTotale(): number {
    return this.panier.reduce((acc, item) => acc + (item.quantite || 0), 0);
  }

  augmenterQuantite(id: any) {
    const item = this.panier.find(p => p.id === id);
    if (item) {
      item.quantite += 1;
      this.panier = [...this.panier];
      this.cdr.detectChanges();
    }
    this.produitservice.augmenterQuantite(id).subscribe({
      next: () => {
        this.getPanier();
        this.snack.open('La quantité a été augmentée', 'fermer', { duration: 5000 });
      },
      error: () => {
        if (item) {
          item.quantite -= 1;
          this.panier = [...this.panier];
          this.cdr.detectChanges();
        }
        this.snack.open('Erreur lors de la mise à jour', 'fermer', { duration: 5000 });
      }
    });
  }

  diminuerQuantite(id: any) {
    const item = this.panier.find(p => p.id === id);
    if (item && item.quantite > 1) {
      item.quantite -= 1;
      this.panier = [...this.panier];
      this.cdr.detectChanges();
    } else if (item && item.quantite === 1) {
      this.panier = this.panier.filter(p => p.id !== id);
      this.cdr.detectChanges();
    }
    this.produitservice.diminuerQuantite(id).subscribe({
      next: () => {
        this.getPanier();
        this.snack.open('La quantité a été diminuée', 'fermer', { duration: 5000 });
      },
      error: () => {
        if (item) {
          item.quantite += 1;
          this.panier = [...this.panier];
          this.cdr.detectChanges();
        }
        this.snack.open('Erreur lors de la mise à jour', 'fermer', { duration: 5000 });
      }
    });
  }

  supprimerArticle(panierId: any) {
    this.panier = this.panier.filter(p => p.id !== panierId);
    this.cdr.detectChanges();
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const userId = user.id;
      this.produitservice.supprimerArticle(panierId, userId).subscribe({
        next: () => {
          this.getPanier();
          this.snack.open('Article supprimé du panier', 'fermer', { duration: 5000 });
        },
        error: () => {
          this.snack.open('Erreur lors de la suppression', 'fermer', { duration: 5000 });
          this.getPanier();
        }
      });
    }
  }

  validerCommande() {
    this.produitservice.validerCommande().subscribe(
      resp => {
        this.data.commandeInfo = resp;
        this.router.navigateByUrl('/map');
      }
    );
  }

  placerCommande() {
    const userStr = localStorage.getItem('loggedUser');
    if (userStr) {
      const user = JSON.parse(userStr);
      const produits = this.panier.map(p => ({
        nom: p.produitNom,
        prix: Math.round((p.prix / p.quantite) * 100),
        quantite: p.quantite
      }));
      const info = {
        userId: user.id,
        produits: produits
      };
      this.produitservice.creerSessionStripe(info).subscribe((resp: any) => {
        localStorage.setItem('stripeSessionId', resp.sessionId);
        window.location.href = resp.url;
      });
    }
  }
}
