# Projet de Livraison Optimisée

Ce projet fullstack permet la **gestion des livraisons géolocalisées**. Il repose sur une architecture multicouche avec une interface Angular, un backend Spring Boot REST, une base de données MySQL, et un calcul d’itinéraires grâce à **OSRM**. L’objectif est d’optimiser le choix du livreur et son itinéraire vers un client.
tout en intégrant un service de paiement 
---

## Technologies utilisées

### Frontend
- Angular
- Leaflet (visualisation cartographique)

### Backend
- Spring Boot (Java 17)
- JPA + MySQL
- API RESTful

### Services externes
- [OSRM (Open Source Routing Machine)](http://project-osrm.org/) pour le calcul d'itinéraires

---

##  Structure du projet

project-root/
├── Backend/ # Projet Spring Boot (API REST)
│ ├── src/main/java/
│ │ └── ... # Contrôleurs, Services, DAO
│ └── resources/ # Fichier application.properties
├── frontend/ # Application Angular
│ ├── src/
│ │ ├── app/ # Composants : map, form, livraison
│ │ └── assets/
└── README.md 



---

## Fonctionnalités

- Création de commandes clients
- Calcul de l’itinéraire entre le livreur et le client avec OSRM
- intégration de service Paiement Stripe
- Visualisation de la carte et des trajets dans l’interface Angular
- Choix automatique du livreur **le plus proche** (limitation discutée ci-dessous)
- API testée avec Postman

---

##  Limites actuelles

- Le chemin généré par OSRM est **statique** (pas de mise à jour dynamique)
- Le **livreur le plus proche** n'est pas toujours le plus **optimal** en durée (pente, trafic, etc.)
- Pas de mise à jour automatique de l’interface : il faut recharger pour voir les modifications
- Tests limités à Postman, sans tests unitaires (JUnit ou tests end-to-end Angular)
- Latence occasionnelle d'OSRM (jusqu'à 2s sur certaines requêtes)

---

##  Pistes d’amélioration

- Mise à jour **dynamique** des itinéraires via WebSocket ou Kafka
- Identification des **N livreurs les plus proches**, puis calcul de l’itinéraire pour chacun afin de choisir l’**optimum global** (ceci reste coûteux en calcul)
- Tests automatisés avec **JUnit**, **Postman/Newman** et **Cypress**
- Notifications push pour les livreurs
- Gestion multi-utilisateur et base de données scalable

---

##  Lancer le projet

### Backend
```bash
cd Backend
mvn spring-boot:run

```
### frontend
```bash
cd frontend
ng serve
