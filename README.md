# order-service

Service de gestion des commandes de la boutique.

## Prérequis

- Java 21
- Maven 3.9+

## Lancer

```bash
mvn spring-boot:run
```

- API : http://localhost:8080
- Console H2 : http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:orders`, user `sa`, sans mot de passe)

## Endpoints

| Méthode | Chemin      | Description              |
|---------|-------------|--------------------------|
| GET     | `/products` | Liste des produits       |
| POST    | `/orders`   | Passer une commande      |
| GET     | `/orders/search?customer=` | Commandes d'un client |

## Tests

```bash
mvn verify
```
