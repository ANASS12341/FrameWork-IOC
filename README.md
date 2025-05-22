# 🧠 Framework IoC (Inversion of Control) - Spring

Ce projet présente une implémentation pédagogique du principe **Inversion of Control (IoC)** à l’aide du framework **Spring**, illustrant comment gérer l’instanciation et l’injection des dépendances de manière centralisée et découplée.

---

## 🎯 Objectifs pédagogiques

- Comprendre le **principe d’Inversion de Contrôle**.
- Illustrer l’injection de dépendances via :
  - ✅ Fichier de configuration XML
  - ✅ Annotations (`@Component`, `@Autowired`, etc.)
- Appréhender les rôles des couches **DAO**, **Service**, et **Presentation**.
- Observer la configuration et la gestion de beans via **Spring IoC Container**.

---

## 🗂️ Architecture du projet

![image](https://github.com/user-attachments/assets/b52b237e-c82a-44ef-a87c-141e12da409b)

---

## 🔍 Description des packages

- `ioc.dao.DaoImpl`  
  → Fournit une source de données simulée.

- `ioc.service.ServiceImpl`  
  → Utilise le DAO injecté pour effectuer un traitement métier (ex : calcul, transformation...).

- `ioc.presentation.AppXML`  
  → Charge les beans Spring depuis le fichier `config.xml`.

- `ioc.presentation.AppAnnotation`  
  → Utilise les annotations Spring pour configurer et injecter automatiquement les dépendances.

---

## ⚙️ Configuration

### 1. 🧾 Fichier XML (`config.xml`)

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd">

  <bean id="dao" class="ioc.dao.DaoImpl" />
  <bean id="service" class="ioc.service.ServiceImpl">
    <constructor-arg ref="dao"/>
  </bean>

</beans>
```
2. 🧩 Annotations utilisées
@Component pour déclarer un composant Spring

@Autowired pour injecter automatiquement les dépendances

@Configuration et @ComponentScan pour activer le scan des composants

⚙️ Dépendances Maven
xml
Copier
Modifier
<dependencies>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>6.2.5</version>
  </dependency>
</dependencies>
▶️ Exécution
Mode 1 : Configuration XML
bash
Copier
Modifier
mvn compile exec:java -Dexec.mainClass=ioc.presentation.AppXML
Initialise le contexte Spring via config.xml

Affiche le résultat du traitement

Mode 2 : Configuration via annotations
bash
Copier
Modifier
mvn compile exec:java -Dexec.mainClass=ioc.presentation.AppAnnotation
Utilise les annotations pour configurer les beans

Affiche le résultat du traitement

✅ Exemple de sortie attendue
text
Copier
Modifier
[XML] Résultat = 42.0
[Annotation] Résultat = 42.0
