# Progetto_SC
## Chat multiutente eseguita su container docker, orchestrati da Kubernetes su macchine virtuali Azure 

![alt text](https://github.com/stefano-colombo/Progetto_SC/blob/main/immagini_readme/struttura_progetto.png)

Il progetto è composto da 4 container: un container Rabbitmq, per la creazione e la gestione delle code, utilizzate per l’invio di messaggi; un container Mariadb, per la memorizzazione dello storico della chat e due container OpenJDK che eseguono applicazioni Spring Boot che contengono la logica del funzionamento della chat.

![alt text](https://github.com/stefano-colombo/Progetto_SC/blob/main/immagini_readme/configurazione_rete.png)

La configurazione di rete risulta essere molto semplice, si utilizzano due macchine virtuali Azure, una avrà il compito di Master e l’alta ospiterà i pod eseguendo il ruolo di worker (volendo si possono aggiungere tranquillamente anche altre VM worker); le VM sono situate all’interno della stessa subnet, così da poterle fare dialogare tranquillamente. Entrambe le macchine hanno un indirizzo IP pubblico necessario per permettere il collegamento da remoto, entrambe espongono la porta 22 per le connessione ssh e il kmaster in più espone anche la porta 8080 per fornire l’interfaccia web agli utenti.

![alt text](https://github.com/stefano-colombo/Progetto_SC/blob/main/immagini_readme/login.png)

Per serseguire il login l’utente si collega all’indirizzo 20.224.217.194 sulla porta 8080 (1); il frontend risponde inviando la pagina login.html, grazie al RestController, implementato tramite Spring Boot, che ritorna pagine html create tramite Thymeleaf(2); l’utente esegue il login(3); il frontend chiede lo storico dei messaggi al servizio MAriaDB grazie al framework JPA configurato su spring Boot(4); Il DB risponde inviando lo storico dei messaggi (5); Spring Boot ritorna la pagina interfaccia.html che conterrà tutti i messaggi inviati precedentemente e il codice javascript della pagina si metterà in ascolto, tramite il (7) frontend, grazie al protocollo STOMP, del servizio RAbbitMQ.

![alt text](https://github.com/stefano-colombo/Progetto_SC/blob/main/immagini_readme/invio_messaggio.png)

Per inviare un messaggio l’utente1 invia un messaggio(1); il frontend invia il messaggio sulla coda1 di RabbitMQ tramite il protocollo AMQP di Spring Boot(2); il backend legge i messaggi dalla coda1(3); memorizza i messaggi nel database MAriaDB grazie a JPA(4); inolta i messaggi sulla coda2(5); il frontend legge i messaggi dalla coda2(6); il frontend li inoltra a sua volta sulla coda utilizzata dal protocollo STOMP(7); e il codice javascript nell’interfaccia utente legge i messaggi della coda STOMP(9), tramite Spring Boot del frontend(8), e aggiorna la pagina html di tutti gli utente connessi in quel momento.

## Come far partire il progetto

[1]Creare due macchine virtuli come indicato nella relazione

[2]accedere alle macchine ed eseguire configurazioni.sh

(I seguenti comandi sono descritti anche nella relazione in un ordine leggermente diverso)

[3]Collegarsi al master.

[4]Installare il tool "Kompose".
```
wget https://github.com/kubernetes/kompose/releases/download/v1.26.1/kompose_amd64.deb
sudo apt install ./kompose_1.26.1_amd64.deb
```
[5]Inizializzazione del master, bisogna inizializzare il cluster. Per "IP_RETE_PRIVATA" si intende l’ip della subnet che è stato assegnato all VM.
```
sudo kubeadm init −−apiserver−advertise−address=IP_RETE_PRIVATA \
−−pod−network−cidr=192.168.0.0/16
```
[6]Eseguire i comandi che l’output del comando precedente ci suggerisce di usare.
```
mkdir −p $HOME/.kube
sudo rm −rf $HOME/.kube/config
sudo cp −i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id −u):$(id −g) $HOME/.kube/config
```
[7]Inviare il file calico.yaml alla macchina VM col comando scp eCreare la rete calico.
```
kubectl create −f calico.yaml
```
[8]Aggiungere un settaggio alla rete e riavviare i coredns per far funzionare la risoluzione
dei nomi dei servizi.
```
kubectl set env daemonset/calico−node −n kube−system \
IP_AUTODETECTION_METHOD=interface=eth0
kubectl rollout restart deployments −n kube−system coredns
```
[9]Sportarci sul kworker dove dobbiamo eseguire il comando fornito in output dal comando
precedente "kubeadm init" che ha una forma di questo tipo.
```
kubeadm join 10.1.1.10:6443 −−token 1fuuic.sdy3dyqxhn83ss4r \
−−discovery−token−ca−cert−hash sha256:1dfeb1c6
```
[10]si invia sul master il file docker-compose. Si lancia il seguente comando che creerà un insieme di file .yaml .
```
kompose convert −f docker−compose.yaml
```
[11]Kompose crea quasi tutti i file in maniera perfetta, ma ha delle difficoltà per quanto riguarda i file dei volumi persistenti, quindi a questo punto utilizzare i file .yaml relativi ai volumi, presenti nella cartella file_ymal github. Ed infine si possono lanciare i deployment.
```
kubectl apply −f .
```
[12]Nel caso in cui si possano presentare problemi perche il frontend e il backend non riescono
a risolvere i nomi dei servizi, utilizzare il seguente comando per eseguire il restart
del servizi:
```
kubectl rollout restart deployments client kubectl rollout restart deployments
server
```
Adesso l’applicazione risulta essere funzionante ma non è ancora raggiungibile
dall’esterno; quindi, eseguiamo il seguente comando che permette all’utente di
raggiungere l’interfaccia web con l’ip del kmaster alla porta 8080.
```
kubectl port−forward −−address IP_RETE_PRIVATA service/client 8080:8080
```

## Schermate App


### Login
![alt text](https://github.com/stefano-colombo/Progetto_SC/blob/main/immagini_readme/interfaccia_login.png)

### Chat 
![alt text](https://github.com/stefano-colombo/Progetto_SC/blob/main/immagini_readme/interfaccia_chat.png)


