# Prova Finale Ingegneria del Software 2022
![alt text](src/main/resources/Images/Eriantys-Header.jpg)

La prova finale di ingegneria del software consiste nell'implementazione 
di un applicazione distribuita del gioco da tavolo [Eriantys](https://www.craniocreations.it/prodotto/eriantys/), 
attraverso il pattern Model View Controller (MVC). 
## Gruppo AM20

- ###   10656496    Simone Airaghi ([@SimoneAiraghi](https://github.com/SimoneAiraghi))<br>simone1.airaghi@mail.polimi.it
- ###   10706985    Matteo Boido ([@MatteoBoido](https://github.com/MatteoBoido))<br>matteo.boido@mail.polimi.it
- ###   10673045    Lorenzo Bossi ([@LorenzoBossi](https://github.com/LorenzoBossi))<br>lorenzo1.bossi@mail.polimi.it

## Esecuzione
Questo progetto richiede java 17 o superiore per essere eseguito.
Abbiamo creato un unico programma che si può comportare da client o da server in base ai parametri ricevuti.
Il jar è scaricabile qui: [Jar](https://github.com/LorenzoBossi/..).
Dopo avere scaricato il jar e aver verificato la propria versione di java, si può eseguire seguendo le istruzioni sottostanti.

### Client

#### GUI

Di default l'esecuzione del jar fa partire un client (GUI).
Può essere eseguito attraverso il seguente comando:
```
java -jar AM20-Eriantys.jar
```
Dopo di che verrà richiesto di inserire indirizzo e porta del server a cui collegarsi dall'interfaccia grafica.

#### CLI
É possibile utilizzare il client da linea di comando (CLI) aggiungendo 
il parametro --cli, oppure la versione ridotta -c.
```
java -jar AM20-Eriantys.jar --cli
```
```
java -jar AM20-Eriantys.jar -c
```

Per specificare l'indirizzo e la porta del serve a cui collegarsi è possibile utilizzare i parametri --ip e --port.
Se non si specifica il server l'applicazione cercherà di collegarsi a quello di default: (localhost,26000).
```
java -jar AM20-Eriantys.jar --cli --ip 129.168.10.5 --port 23456
```
Al posto di --ip e --port si possono usare le abbreviazioni -i e -p.
```
java -jar AM20-Eriantys.jar -c -i 129.168.10.5 -p 23456
```
### Server
Per utilizzare il jar come server è necessario aggiungere 
il parametro --server, oppure -s.
```
java -jar AM20-Eriantys.jar --server
```
```
java -jar AM20-Eriantys.jar -s
```
Di default il server partirà sulla porta 26000.
Se si vuole utilizzare una porta differente basta aggiungere 
un ulteriore parametro (--port o -p ), seguito dal numero di porta scelto.
```
java -jar AM20-Eriantys.jar --server --port 23456
```
```
java -jar AM20-Eriantys.jar --server -p 23456
```


## Advancements

| Functionality    |                       State                        |
|:-----------------|:--------------------------------------------------:|
| Basic rules      | 🟢 |
| Complete rules   | 🟢 |
| Socket           | 🟢 |
| GUI              | 🟢 |
| CLI              | 🟢 |
| 12 Characters    | 🟢 |
| 4 Players games  | 🔴  |
| Multiple games   | 🟢 |
| Persistence      | 🔴  |
| Resilience       | 🔴  |

## Test coverage
|Package|Lines|Methods|Classes|
|:---------------:|:-----------:|:-----------:|:-----------:|
|model|97%|95%|95%|
|controller|100%|100%|100%|
|client|2%|4%|10%|
|network|8%|7%|22%|
|utils|21%|40%|50%|