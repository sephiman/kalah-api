# Kalah Service

This is a Kalah API service that allows you through API request to play the Kalah game. Kalah is a 2 players game that by default each player has 6 stones in its pit.
Find the rules of the game in wikipedia: https://en.wikipedia.org/wiki/Kalah

## Dependencies:

This is the list of dependencies you will need to run the project:

- Java 8 or later.
- Gradle 5 (5.6.x only) or Gradle 6 (6.3 or later)
 
## How to run it:

The application will be deployed in the domain localhost through the port 8080 by default.

- Run application using Gradle under project directory

`gradle bootRun`

- Run application using Java under project directory

`gradle build`

`java -jar build/libs/kalah-api-1.0-SNAPSHOT.jar`

- Run application with Docker. Requires docker app running:

`gradle build`

`docker build -f docker/Dockerfile -t game-api .`

`docker run -p 8080:8080 game-api:latest`

## Additional configuration:

Use the following properties to customize the application modifying _application.properties_

1. Default port. The default port may be change to any other adding the following property:

`server.port=8080`

2. H2 console. H2 with Spring boot allows to enable a web h2 client with the following properties:

`spring.h2.console.enabled=true`

`spring.h2.console.path=/h2`

3. The kalah game by default works adding 6 stones to each player's pit. The initial stones is configurable:

`kalah.config.stones=6`

## Endpoints

The game API provides the following endpoints including cURL examples running the application locally.


### Create kalah game

Creates a new Kalah game

- Method: POST
- URI: /games
- Content-Type: \*/\*

Possible responses are:

- Http 201 Created: It returns the created game

**Example cURL request**

    curl --location --request POST 'http://localhost:8080/games'

**Example response**

    HTTP 201 CREATED
    {
        "id": 2,
        "url": "http://192.168.31.10:8080/games/2"
    }

### Make a move

Perform a pit movement on the board

- Method: PUT
- URI: /games/{gameId}/pits/{pitId}
- Content-Type: \*/\*

Possible responses are:

- Http 200 Ok: Movement is done
- Http 400 Bad request: The movement is not valid
- Http 404 Not Found: the game has not been found

**Example cURL request**

    curl --location --request PUT 'http://localhost:8080/games/1/pits/1'

**Example response**

    {
        "id": 1,
        "url": "http://192.168.31.9:8080/games/1",
        "status": {
            "1": "0",
            "2": "5",
            "3": "5",
            "4": "5",
            "5": "5",
            "6": "4",
            "7": "0",
            "8": "4",
            "9": "4",
            "10": "4",
            "11": "4",
            "12": "4",
            "13": "4",
            "14": "0"
        }
    }
