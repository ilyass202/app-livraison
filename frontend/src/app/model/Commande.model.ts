import { Livreur } from './Livreur.model';
import { Produit } from './Produit.model';
import { User } from './User.model';

export type CommandeStatus = 'EN_ATTENTE' | 'EN_COURS' | 'LIVREE' | 'ANNULEE';

export class Commande {
  id?: number;
  produits?: Produit[];
  total?: number;
  clientLong?: number;
  clientLalt?: number;
  livreurLong?: number;
  livreurLalt?: number;
  status?: CommandeStatus;
  utilisateur?: User;
  livreur?: Livreur;
} 