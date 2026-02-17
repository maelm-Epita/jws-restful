## Summary
A Quarkus powered RESTful API in a layered architecture written in Java as a 2 day long project.
The API defines multiple HTTP endpoints to create a pokemon-like game.

## Build instructions
### Dependencies 
- Postgresql
- Maven

### Build
In repository root:

Step 1:
- run ```source install-1.sh```

Once you see the log ```database system is ready to accept connections```, you can press enter and move on to step 2

Step 2:
- run ```./install-2.sh```

### Running
Run ```./run.sh``` to start the program

You can now access the quarkus application at http://localhost:8081

To view all available endpoints, you can go to http://localhost:8081/q/swagger-ui/

You can start a game by executing the start endpoint, the mapPath field must be a valid map absolute path

You can get a valid absolute map path by running ```echo $PWD/src/main/resources/maps/yakalos.epimap``` in the repository root

After executing the start endpoint, you can try out all other endpoints.

## Aside 
The game this API builds is meant to be played using a viewer server running alongside it, however, as this was a 2 day long assignment, the viewer's source code was given to students and belongs to EPITA, thus I cannot share it

## Technical stack
- Java
- Quarkus
- Postgresql
- Jakarta
- Junit
