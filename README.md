# Prova Finale Ingegneria del Software 2022
![alt text](src/main/resources/Images/Eriantys-Header.jpg)

The final project of the course "Ingegneria del Software" required to develope a distributed application 
of the board game [Eriantys](https://www.craniocreations.it/prodotto/eriantys/),
using the Model View Controller pattern (MVC).

## Gruppo AM20

- ###   10656496    Simone Airaghi ([@SimoneAiraghi](https://github.com/SimoneAiraghi))<br>simone1.airaghi@mail.polimi.it
- ###   10706985    Matteo Boido ([@MatteoBoido](https://github.com/MatteoBoido))<br>matteo.boido@mail.polimi.it
- ###   10673045    Lorenzo Bossi ([@LorenzoBossi](https://github.com/LorenzoBossi))<br>lorenzo1.bossi@mail.polimi.it

## Jar execution
The project requires java 17 or higher to be executed correctly.

We created a single program which can behave like a server or client according to the given parameters.
The final jar file can be found here: [Jar](https://github.com/LorenzoBossi/ingsw2022-AM20/tree/master/deliveries/jar).


After downloading the jar and verifying the installed java version, it can be executed by following the instructions below.


### Client

#### GUI

The execution of the jar starts a client (GUI) by default.
It can be done using the following command.
```
java -jar AM20-Eriantys.jar
```
Then the user interface will allows the user to select ip and port of the server to connect to. 


#### CLI
You can start a client with a command line interface (CLI) by adding the 
parameter --cli, or the shorter version -c.

```
java -jar AM20-Eriantys.jar --cli
```
```
java -jar AM20-Eriantys.jar -c
```
In order to select the ip and port of the server you can add the parameters --ip and --port.
Otherwise, the client will try to connect to the default server at : (localhost,26000).

```
java -jar AM20-Eriantys.jar --cli --ip 129.168.10.5 --port 23456
```
Instead of --ip and --port, is possible to use -i and -p.
```
java -jar AM20-Eriantys.jar -c -i 129.168.10.5 -p 23456
```
### Server
In order to use the jar as a server is necessary to add the parameter --server, or -s.

```
java -jar AM20-Eriantys.jar --server
```
```
java -jar AM20-Eriantys.jar -s
```
The server will be listening on port 26000 by default.
To use a different port you can add the parameter --port or -p, followed by the chosen port.

```
java -jar AM20-Eriantys.jar --server --port 23456
```
```
java -jar AM20-Eriantys.jar --server -p 23456
```


## Advancements

| Functionality   |                       State                        |
|:----------------|:--------------------------------------------------:|
| Basic rules     | 🟢 |
| Complete rules  | 🟢 |
| Socket          | 🟢 |
| CLI             | 🟢 |
| GUI             | 🟢 |
| 12 Characters   | 🟢 |
| 4 Players games | 🔴  |
| Multiple games  | 🟢 |
| Persistence     | 🔴  |
| Resilience      | 🔴  |

## Test coverage
|Package|Lines|Methods|Classes|
|:---------------:|:-----------:|:-----------:|:-----------:|
|model|97%|95%|95%|
|controller|100%|100%|100%|
|client|2%|4%|10%|
|network|8%|7%|22%|
|utils|21%|40%|50%|
