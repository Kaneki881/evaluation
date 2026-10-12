# Exercices Hibernate / JPA

Ce dossier contient trois projets Maven indépendants. Ils utilisent tous les packages demandés (`ma.projet.*`) sans conflit, car chacun est compilé dans son propre module.

| Module | Sujet | Base MySQL créée au premier lancement |
| --- | --- | --- |
| `exercice1-stock` | Catégories, produits, commandes et lignes | `gestion_stock` |
| `exercice2-projets` | Employés, projets, tâches et réalisations | `gestion_projets` |
| `exercice3-etat-civil` | Personnes, hommes, femmes et mariages | `gestion_etat_civil` |

## Lancer les tests

Prérequis : Java 17 ou plus et Maven 3. Les tests utilisent H2 en mémoire, donc aucun serveur MySQL n'est nécessaire.

```sh
mvn test
```

Pour un seul exercice :

```sh
mvn -pl exercice1-stock test
mvn -pl exercice2-projets test
mvn -pl exercice3-etat-civil test
```
## Resultat
Exercice 1 
<img width="1449" height="681" alt="image" src="https://github.com/user-attachments/assets/898ec19d-21aa-40fc-b94b-2da57b973a8f" />
Exercice 2
<img width="1840" height="765" alt="image" src="https://github.com/user-attachments/assets/8524b1ba-5b99-4655-841c-bace6e168cf0" />
Exercice 3
<img width="1849" height="779" alt="image" src="https://github.com/user-attachments/assets/a698b8d1-8c7e-4b22-b2d1-72e751801cd0" />


Les classes `StockTest`, `ProjetsTest` et `EtatCivilTest` créent des données d'exemple et vérifient les fonctions demandées, les requêtes nommées, la requête SQL native, l'API Criteria et les affichages.

Sous Windows PowerShell, le script `voir-exercices.ps1` affiche les données d'exemple et les résultats dans le terminal :

```powershell
.\voir-exercices.ps1 1      # stock
.\voir-exercices.ps1 2      # projets
.\voir-exercices.ps1 3      # état civil
.\voir-exercices.ps1 tous   # les trois
```

Si PowerShell refuse le lancement du script à cause de sa politique d'exécution, utiliser `powershell -ExecutionPolicy Bypass -File .\voir-exercices.ps1 tous`.

## Utiliser MySQL

Chaque module possède son propre `src/main/resources/application.properties`. Ajuster `hibernate.connection.username` et `hibernate.connection.password` selon le serveur local. L'URL JDBC contient `createDatabaseIfNotExist=true` et Hibernate utilise `hibernate.hbm2ddl.auto=update` : avec un utilisateur MySQL autorisé à créer des bases, les bases et tables sont générées lors de la première ouverture de `HibernateUtil.getSessionFactory()`.

Pour une installation qui interdit la création automatique des bases, les créer d'abord :

```sql
CREATE DATABASE gestion_stock CHARACTER SET utf8mb4;
CREATE DATABASE gestion_projets CHARACTER SET utf8mb4;
CREATE DATABASE gestion_etat_civil CHARACTER SET utf8mb4;
```

Les clés de `application.properties` peuvent être remplacées par des propriétés JVM de même nom, par exemple `-Dhibernate.connection.username=mon_utilisateur`.

## Sens des périodes

- Stock : les bornes de date de commande sont incluses. La liste des produits commandés contient chaque produit une seule fois.
- Projets : une tâche réalisée entre deux dates possède une affectation terminée dont la période réelle chevauche l'intervalle demandé. Le coût global additionne les prix des tâches planifiées du projet.
- État civil : les épouses sur une période sont les femmes dont le mariage chevauche cette période. Le nombre d'enfants additionne les totaux des mariages **commencés** pendant la période : le modèle ne contient aucune date de naissance d'enfant. Le calcul Criteria compte les hommes ayant exactement quatre épouses distinctes, avec des mariages commencés dans la période et toujours en cours à sa date de fin.

Les méthodes `afficherProduitsDeCommande`, `afficherTachesRealisees` et `afficherMariages` renvoient une chaîne prête à afficher avec `System.out.println(...)`.
