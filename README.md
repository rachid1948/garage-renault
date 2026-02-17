# Garage Service - Spring Boot + Kafka
Garage Service est un microservice développé avec Spring Boot permettant :
- ✅ La gestion des véhicules
- ✅ La publication d’un événement Kafka lors de la création d’un véhicule
- ✅ La consommation des événements `vehicle-created`
- ✅ La persistance via H2 (base en mémoire)
- ✅ Documentation API avec Swagger
---
# 🏗️ Architecture
Le projet suit une architecture de 3 couches :
```
controller
service
repository

```
### 🔄 Flow d’exécution
1. Appel API → `POST /vehicles`
2. Sauvegarde du véhicule en base
3. Publication d’un événement Kafka `VehicleCreatedEvent`
4. Consumer Kafka écoute le topic `vehicle-created`
5. Traitement de l’événement
---
# 🧰 Stack Technique
- Java 17+
- Spring Boot 3.3.8
- Spring Data JPA
- Spring Kafka
- H2 Database (in-memory)
- Swagger (Springdoc)
- Apache Kafka
- Lombok
---
# ⚙️ Configuration Principale
- Port : `8080`
- H2 DB : `jdbc:h2:mem:garage-db`
- Kafka Broker : `172.23.106.71:9092`
- Topic : `vehicle-created`
- Group ID : `garage-group`
---
# 🚀 Lancement du Projet
## 1️⃣ Démarrer Kafka (obligatoire)
⚠️ Kafka doit être démarré avant l’application.
Dans Ubuntu / WSL :
```bash
cd ~/kafka_2.13-3.5.0
```
### Terminal 1 – Zookeeper
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```
### Terminal 2 – Kafka Broker
```bash
bin/kafka-server-start.sh config/server.properties
```
---
## 2️⃣ Vérifier / Créer le topic
Lister les topics :
```bash
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```
Créer le topic si nécessaire :
```bash
bin/kafka-topics.sh \
--create \
--topic vehicle-created \
--bootstrap-server localhost:9092 \
--partitions 1 \
--replication-factor 1
```
---
## 3️⃣ Lancer l’application
Dans IntelliJ :
```
Run → GarageServiceApplication
```
L’application démarre sur :
```
http://localhost:8080
```
---
# 🧪 Tester l’API
Swagger :
```
http://localhost:8080/swagger-ui.html
```
Exemple JSON pour créer un véhicule :
```json
{
 "brand": "DACIA",
 "model": "LOGAN",
 "yearOfManufacture": 2009,
 "fuelType": "GASOLINE",
 "vehicleType": "CAR",
 "garageId": 1
}
```
---
# 📥 Vérifier les messages Kafka
Dans Ubuntu :
```bash
bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic vehicle-created \
--from-beginning
```
Exemple de message reçu :
```json
{
 "vehicleId": 51,
 "brand": "DACIA",
 "model": "LOGAN",
 "createdAt": 1771340062.247026900
}
```
---
# 🗄️ Console H2
Accès :
```
http://localhost:8080/h2-console
```
Configuration :
```
JDBC URL : jdbc:h2:mem:garage-db
User     : sa
Password : (vide)
```
---
# 👨‍💻 Auteur
Zakaria Elmardi  
