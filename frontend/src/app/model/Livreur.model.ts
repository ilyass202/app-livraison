import { Commande } from './Commande.model';

export class Livreur {
    id?: number;
    nom?: string;
    location?: { longitude: number; latitude: number };
    commandes?: Commande[];
} 