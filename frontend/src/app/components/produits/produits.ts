import { Component, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProduitService } from '../../services/produit.service';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-produits',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDividerModule,
    ReactiveFormsModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './produits.html',
  styleUrl: './produits.css'
})
export class ProduitsComponent {
   produits : any[] = []
   recherche !: FormGroup
   constructor(
     private produitservice: ProduitService,
     private snack: MatSnackBar,
     private form: FormBuilder,
     private cdr: ChangeDetectorRef
   ) {}
   
   ngOnInit(){
    this.getAllProduits();
    this.recherche = this.form.group({
      parametre : [null , [Validators.required]]
    })
   }

  getAllProduits() {
    this.produitservice.getAllProduits().subscribe((response)=>{
      if( typeof response=== 'string'){
        this.produits = JSON.parse(response);
      }
      else if (Array.isArray(response)){
        this.produits = response;
      }
      this.cdr.detectChanges();
    })
  }

  Submit(){
    this.produits = [];
    this.cdr.detectChanges();
    const parametre = this.recherche.get('parametre')?.value;
    this.produitservice.getAllProduitsBynom(parametre).subscribe((response)=>{
      if(typeof response === 'string'){
        this.produits = JSON.parse(response)
      }
      else if (Array.isArray(response)){
        this.produits = response ;
      }
      this.cdr.detectChanges();
    })
  }

  ajouterPanier(id : any){
    this.produitservice.ajouterPanier(id)?.subscribe(
      () => {
        this.snack.open("Produit ajouté dans le panier" , "voir Panier" , { duration : 5000 })
      }
    )
  }
}
