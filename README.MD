# Authentication Service - Chat4Me

## Descrizione

Il **servizio di autenticazione di Chat4Me** è un **microservizio scritto in Java utilizzando Spring Boot** che implementa un sistema di autenticazione basato su Strong Customer Authentication (SCA), garantendo un accesso sicuro tramite una combinazione di credenziali e autenticazione temporanea. Dopo aver effettuato il login con username e password, l’utente deve confermare la propria identità tramite un OTP (One-Time Password) inviato via email. Completata la verifica, vengono generati e assegnati i token di accesso.

## Architettura

L'architettura del progetto è esagonale, ideale per Spring Boot, separando i componenti e mantenendo la logica di business pura e indipendente dai dettagli esterni.

## Funzionamento

1. L'utente si registra fornendo le proprie credenziali. Se l’utente esiste già, l'operazione viene bloccata.
2. Hashing della password: La password viene hashata con l'algoritmo SHA-1 prima di essere inserita nel database, migliorando la sicurezza.
3. Per effettuare il login, l'utente inserisce username e password.
4. Se le credenziali sono corrette, viene inviato un OTP a sei cifre generato con SecureRandom.
5. L'OTP è associato a un UUID, salvato sia nel database che nella sessione del client.
6. L’OTP ha un massimo di tre tentativi e una scadenza temporale. Se scade o vengono esauriti i tentativi, l’utente deve ripetere la procedura di login.
7. Dopo la conferma dell’OTP, vengono generati Access Token e Refresh Token, utilizzando HS512 con una chiave segreta.
8. Invio di un nuovo OTP: L'utente può richiedere un nuovo OTP invalidando quello precedente.
9. Logout: È possibile effettuare il logout con l'invalidazione del Refresh Token nel database e l'invalidazione sia dell'Access Token che del Refresh Token nel client.

## Database

Il servizio utilizza JPA (Java Persistence API) con JPA Repository per gestire i dati. Le informazioni su utenti, OTP e token sono memorizzate nel database.

Per garantire la portabilità e la sicurezza, è stato configurato un Datasource centralizzato nel file `application.properties`, evitando di scrivere direttamente le credenziali nel codice e semplificando la gestione dei dati di connessione in ambienti diversi.

## Token

- **Access Token**: Contiene le informazioni dell’utente ed è incluso nell’header Authorization con il prefisso Bearer.
- **Refresh Token**: Usato per ottenere un nuovo access token senza necessità di ri-autenticazione. È salvato come cookie HTTP e nel database.

## Storico

Ogni OTP e Refresh Token ha uno storico salvato nel database, permettendo di tracciare il loro utilizzo anche se non più validi.

## Endpoints Principali

| Metodo | Endpoint           | Descrizione                                                             |
|--------|--------------------|-------------------------------------------------------------------------|
| POST   | /register          | Registra un nuovo utente.                                               |
| POST   | /login             | Effettua il login e invia l'OTP.                                        |
| POST   | /otp/reSend        | Invia un nuovo OTP invalidando quello precedente.                       |
| POST   | /verify-otp        | Verifica l’OTP inserito dall’utente.                                    |
| POST   | /verify-token      | Verifica la validità di un Access Token.                                |
| POST   | /refresh/token     | Genera un nuovo Access Token utilizzando il Refresh Token.              |
| POST   | /logout            | Possibilità di effettuare il logout con l'invalidazione dei token.      |
| GET    | /username-list     | Restituisce l'elenco completo degli utenti registrati.                  |

