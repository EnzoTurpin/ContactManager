# TaskManager

Le TaskManager est une application Java permettant de gérer des tâches avec une interface utilisateur graphique. Les utilisateurs peuvent ajouter, modifier, et supprimer des tâches, avec la possibilité d'assigner une date, un titre, une description, une catégorie et un niveau de priorité à chaque tâche. Les catégories peuvent être prédéfinies ou personnalisées, avec un stockage dans un fichier texte, tandis que les tâches sont cryptées et sauvegardées dans un fichier JSON pour plus de sécurité.

## Fonctionnalités

- Ajouter, modifier et supprimer des tâches.
- Assigner un titre, une description, une catégorie, un niveau de priorité et une date à chaque tâche.
- Trois niveaux de priorité prédéfinis (Faible, Moyenne, Haute) avec une couleur correspondante pour chaque priorité.
- Possibilité de créer des catégories personnalisées, sauvegardées dans un fichier texte.
- Filtrage des tâches par catégorie et priorité.
- Sauvegarde cryptée des tâches dans un fichier JSON.

## Gestion des Données

Pour garantir la confidentialité des informations, les tâches sont stockées de manière sécurisée grâce à un cryptage robuste. Le projet gère deux versions du fichier de tâches :

- Une version cryptée, qui assure la protection des données et est utilisée par l'application pour la sauvegarde et le chargement des tâches.
- Une version non cryptée, générée pour le débogage et le développement, qui permet une inspection facile des données stockées.

## Dépendances

Ce projet utilise les bibliothèques suivantes, qui sont incluses dans le dossier `lib` du dépôt :

- Java Development Kit (JDK) version 8 ou supérieure.
- JTattoo pour l'apparence de l'interface utilisateur.
- org.json pour la manipulation des données JSON.

Aucune action supplémentaire n'est nécessaire pour installer ces dépendances, elles sont fournies pour une mise en place rapide et facile.

## Installation

Pour utiliser TaskManager, suivez ces étapes :

1. Assurez-vous que Java est installé sur votre système.
2. Clonez ou téléchargez le code source du projet depuis ce dépôt GitHub.
3. Toutes les librairies nécessaires sont déjà présentes dans le dossier `lib/`.
4. Utilisez le Makefile inclus pour compiler et exécuter le projet avec les commandes suivantes :
   - `make build` pour compiler le projet.
   - `make run` pour exécuter l'application.
   - `make clean` pour nettoyer le projet.

## Compilation et Exécution

Avant d'exécuter les commandes suivantes, assurez-vous d'être à la racine du projet. Le Makefile inclus permet de simplifier la compilation et l'exécution de l'application.

- Pour compiler le projet, exécutez : `make build`
- Pour exécuter l'application, utilisez : `make run`
- Pour nettoyer le projet, tapez : `make clean`

## Structure du Projet

Le projet est organisé comme suit :

- `src/`: Contient le code source du projet.
- `lib/`: Contient les fichiers JAR des bibliothèques externes nécessaires.
- `bin/`: Dossier pour les fichiers compilés (créé par le Makefile).
- `resources/`: Contient les fichiers de données et de configuration nécessaires au fonctionnement de l'application.

## Contribution

Les contributions à ce projet sont les bienvenues. Pour contribuer, veuillez suivre ces étapes :

1. Forkez le projet.
2. Créez votre branche de fonctionnalité (`git checkout -b feature/AmazingFeature`).
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`).
4. Poussez vers la branche (`git push origin feature/AmazingFeature`).
5. Ouvrez une Pull Request.
