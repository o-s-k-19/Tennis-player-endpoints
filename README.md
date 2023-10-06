# Tennis-player-endpoints

Cette API permet de recupérer via les appels REST :
- La liste des joueurs de tennis triée selon l'ordre du meilleur au moins bon
- Les informations d'un joueur identifié par son ID
- Quelques statistique sur le tennis

## Configuration et prerequis
- Java 17 (requis)
- Maven 3.8.5 + (requis)
- IDE eclipse (optionel)
- Postman ou Curl (optionel)

## Documentation

https://tennis-player-endpoint.alwaysdata.net/swagger-ui/index.html

## Test

https://tennis-player-endpoint.alwaysdata.net

Ensuite pour utiliser les différentes fonctionnalités de l'API, vous pouvez utiliser des clients REST tels que Postman ou CURL.

Pour exécuter localement l'application, suivez ces étapes:

1. Clonez le dépôt.
```
git clone https://github.com/o-s-k-19/Tennis-player-endpoints.git
```
2. Assurez-vous d'avoir Java et Maven installés.
3. Fournir la configuration dans `application.properties`. (si besoin de changer le port d'ecoute du serveur)
4. Exécutez l'application avec les etapes suivantes:

Placez vous dabord dans le repertoire du projet cloné :
```
cd Tennis-player-endpoints
```
Et ensuite tapez l'une des commandes suivante pour exécuter l'application :

Avec maven :
```
mvn spring-boot:run
```
Avec java :
```
java -jar "chemin vers le fichier jar"
```

L'application sera alors accessible localement avec le port par defaut definis dans `application.properties` à l'adresse http://localhost:8500 et  l'interface swagger à l'adresse http://localhost:8500/swagger-ui/index.html

Ensuite pour utiliser les différentes fonctionnalités de l'API, vous pouvez utiliser des clients REST tels que Postman ou CURL.
