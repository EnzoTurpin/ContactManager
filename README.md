# ContactManager

Le ContactManager est une application Java permettant de gérer des contacts avec une interface utilisateur graphique. Les utilisateurs peuvent ajouter, modifier, et supprimer des contacts, avec la possibilité d'assigner un prénom, un nom, un numéro de téléphone, une adresse email, une biographie, et de les organiser par groupes. Les contacts sont sauvegardés dans un fichier JSON pour plus de sécurité.

## Fonctionnalités

- Ajouter, modifier et supprimer des contacts.
- Assigner un prénom, un nom, un numéro de téléphone, une adresse email et une biographie à chaque contact.
- Créer des groupes personnalisés pour organiser les contacts.
- Filtrage des contacts par groupe.
- Tri des contacts par nom (A-Z, Z-A).
- Recherche de contacts par prénom, nom, numéro de téléphone, ou email.
- Affichage des détails du contact lors de la sélection dans la liste.
- Sauvegarde des contacts dans un fichier JSON.

## Gestion des Données

Pour garantir la confidentialité des informations, les contacts sont stockés de manière sécurisée. Le projet gère deux versions du fichier de contacts :

- Une version cryptée, qui assure la protection des données et est utilisée par l'application pour la sauvegarde et le chargement des contacts.
- Une version non cryptée, générée pour le débogage et le développement, qui permet une inspection facile des données stockées.

## Dépendances

Ce projet utilise les bibliothèques suivantes, qui sont incluses dans le dossier `lib` du dépôt :

- Java Development Kit (JDK) version 8 ou supérieure.
- JTattoo pour l'apparence de l'interface utilisateur.
- org.json pour la manipulation des données JSON.

Aucune action supplémentaire n'est nécessaire pour installer ces dépendances, elles sont fournies pour une mise en place rapide et facile.

## Installation

Pour utiliser ContactManager, suivez ces étapes :

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
- `ressources/`: Contient les fichiers de données et de configuration nécessaires au fonctionnement de l'application.

## Contribution

Les contributions à ce projet sont les bienvenues. Pour contribuer, veuillez suivre ces étapes :

1. Forkez le projet.
2. Créez votre branche de fonctionnalité (`git checkout -b feature/AmazingFeature`).
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`).
4. Poussez vers la branche (`git push origin feature/AmazingFeature`).
5. Ouvrez une Pull Request.
