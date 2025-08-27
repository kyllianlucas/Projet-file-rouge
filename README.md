# 🏸 E-commerce Badminton – Spring Boot & React

## 📌 Contexte
Ce projet est un site **e-commerce spécialisé dans le badminton**.  
Il permet de rechercher, consulter et réserver des biens ou services liés au badminton (raquettes, volants, terrains, cours, etc.).  

L’application est découpée en deux parties :
- **Backend** : API REST développée en **Java Spring Boot** (gestion utilisateurs, catalogue, réservations, JWT auth, traçabilité).  
- **Frontend** : interface utilisateur développée avec **React + Vite** (affichage catalogue, inscription, réservation).  

---

## 🚀 Ateliers de développement

### ✅ Atelier 1 – Initiation du projet
- Initialisation du dépôt GitHub/GitLab.  
- Ajout du `.gitignore`.  
- Rédaction du premier `README.md`.  
- Mise en place des dépendances principales (Spring Boot, React, JWT, BCrypt…).  

### ✅ Atelier 2 – Traçabilité des évènements

#### Étape 1 – Erreurs et avertissements
Revue du code et classification :  
- **Critique** :  
  - Absence de gestion d’authentification échouée → retour `500` au lieu de `401`.  
  - Utilisation de `.get()` sur `roleRepo.findById` → risque `NoSuchElementException`.  
- **Erreur** :  
  - `.findFirst().get()` sur adresse utilisateur → crash si aucune adresse trouvée.  
  - Vérification limitée des rôles (`ADMIN`/`USER`) → tout rôle inconnu provoque une exception.  
- **Avertissement** :  
  - Multi-rôles non gérés (`.findFirst()` uniquement).  
  - Couplage fort DTO ↔ Entity via `ModelMapper`.  
  - Email déjà existant géré uniquement par `DataIntegrityViolationException`.  

Cette version est historisée dans le dépôt avec le tag **`v0.2-erreurs-avertissements`**.  

#### Étape 2 – Information / Débogage
Ajout de logs avec **SLF4J** pour :  
- Suivi des tentatives et réussites d’**inscription** et de **connexion**.  
- Détection des cas suspects (ex. rôle inattendu).  
- Aide au débogage (ex. adresse créée ou récupérée, panier associé).  

Exemple :  
```text
INFO  Tentative d’inscription pour l’email: user@test.com
INFO  Inscription réussie pour l’email: user@test.com, rôle attribué: USER
WARN  Tentative d'inscription avec un email déjà existant: user@test.com
ERROR Erreur lors de l’enregistrement de l’utilisateur user@test.com: Rôle par défaut introuvable
