package com.example.autenticationservice.domain.exceptions;

// TODO stai creando molte eccezioni applicative, va benissimo ma creagli una base Exception, tutte le tue eccezioni applicative ne devono estendere una
// non devono estendere tutte RuntimeException(solo questa estende runtime ad esempio, le altre estenderanno questa) -> perchè se serve puoi facilmente fare un catch solo per le tue eccezioni applicative
public class AutenticationServiceException extends RuntimeException {
  public AutenticationServiceException(String message) {
    super(message);
  }

  public AutenticationServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
