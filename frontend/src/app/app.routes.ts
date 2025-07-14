import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'Home', pathMatch: 'full' },
  {
    path: 'Home',
    loadComponent: () => import('./components/home/home').then(m => m.HomeComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./components/register/register').then(m => m.RegisterComponent)
  },{
  path : 'produits' ,
  loadComponent: () => import('./components/produits/produits').then(m => m.ProduitsComponent)
  },
  {
    path: 'panier',
    loadComponent: () => import('./components/panier/panier').then(m => m.Panier)
  },
  {
    path: 'map',
    loadComponent: () => import('./components/map/map').then(m => m.MapComponent)
  }
];
