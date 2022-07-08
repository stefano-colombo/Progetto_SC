# Progetto_SC
## Chat multiutente eseguita su container docker, orchestrati da Kubernetes su macchine virtuali Azure 


Il progetto è composto da 4 container: un container Rabbitmq, per la creazione e la gestione delle code, utilizzate per l’invio di messaggi; un container Mariadb, per la memorizzazione dello storico della chat e due container OpenJDK che eseguono applicazioni Spring Boot che contengono la logica del funzionamento della chat.

![alt text]()

La configurazione di rete risulta essere molto semplice, si utilizzano due macchine virtuali Azure, una avrà il compito di Master e l’alta ospiterà i pod eseguendo il ruolo di worker (volendo si possono aggiungere tranquillamente anche altre VM worker); le VM sono situate all’interno della stessa subnet, così da poterle fare dialogare tranquillamente. Entrambe le macchine hanno un indirizzo IP pubblico necessario per permettere il collegamento da remoto, entrambe espongono la porta 22 per le connessione ssh e il kmaster in più espone anche la porta 8080 per fornire l’interfaccia web agli utenti.

![alt text]()

L’utente si collega all’indirizzo 20.224.217.194 sulla porta 8080 (1); il frontend risponde inviando la pagina login.html, grazie al RestController, implementato tramite Spring Boot, che ritorna pagine html create tramite Thymeleaf(2); l’utente esegue il login(3); il frontend chiede lo storico dei messaggi al servizio MAriaDB grazie al framework JPA configurato su spring Boot(4); Il DB risponde inviando lo storico dei messaggi (5); Spring Boot ritorna la pagina interfaccia.html che conterrà tutti i messaggi inviati precedentemente e il codice javascript della pagina si metterà in ascolto, tramite il (7) frontend, grazie al protocollo STOMP, del servizio RAbbitMQ.

![alt text]()

L’utente1 invia un messaggio(1); il frontend invia il messaggio sulla coda1 di RabbitMQ tramite il protocollo AMQP di Spring Boot(2); il backend legge i messaggi dalla coda1(3); memorizza i messaggi nel database MAriaDB grazie a JPA(4); inolta i messaggi sulla coda2(5); il frontend legge i messaggi dalla coda2(6); il frontend li inoltra a sua volta sulla coda utilizzata dal protocollo STOMP(7); e il codice javascript nell’interfaccia utente legge i messaggi della coda STOMP(9), tramite Spring Boot del frontend(8), e aggiorna la pagina html di tutti gli utente connessi in quel momento.
